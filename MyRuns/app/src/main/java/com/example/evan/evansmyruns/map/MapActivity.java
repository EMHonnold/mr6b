package com.example.evan.evansmyruns.map;

/**
 * Created by Evan on 4/10/2016.
 *
 * The map activity starts when the user selects "GPS" or "Automatic" in the
 * "input type" dropdown and then presses the "start" button.
 *
 * Displays the run the user is currently on, or an old run loaded from the
 * options in the History Tab.
 *
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.evan.evansmyruns.ManualEntryActivity;
import com.example.evan.evansmyruns.R;
import com.example.evan.evansmyruns.SettingsTab;
import com.example.evan.evansmyruns.database.DeleteDataTask;
import com.example.evan.evansmyruns.database.EntryLoader;
import com.example.evan.evansmyruns.database.ExerciseEntryDbHelper;
import com.example.evan.evansmyruns.database.WriteDataTask;
import com.example.evan.evansmyruns.entry.ActivityType;
import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.entry.InputType;
import com.example.evan.evansmyruns.evanhelpers.ConversionHelpers;
import com.example.evan.evansmyruns.evanhelpers.EHelpers;
import com.example.evan.evansmyruns.location.LocationService;
import com.example.evan.evansmyruns.location.MyBinder;
import com.example.evan.evansmyruns.location.UpdateReceiver;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class MapActivity
        extends Activity
        implements OnMapReadyCallback, ServiceConnection, LoaderManager.LoaderCallbacks<ExerciseEntry> {

    public static final String TAG = "MapActivity";
    public static final int DEFAULT_ZOOM = 17;

    private GoogleMap mMap;
    UpdateReceiver receiver;
    Marker runStartMarker, runEndMarker;
    Polyline runPath;

    public LocationService service;
    public ExerciseEntry currentEntry;     // either the new entry that's being made,
                                            // or the old one that's being shown
    /* persistent variables */
    private long runToShow;             // for displaying old runs
    private InputType inputType;
    private boolean showingNewRun;

    /* keys for retrieving variables */
    private static final String KEY_RUN_TO_SHOW = "run to show";
    private static final String KEY_INPUT_TYPE = "automatic/gps";
    private static final String KEY_SHOWING_NEW_RUN = "old or new run";

    /* constructor method - to make launching the activity easier*/
    public static void showNewRun(Context context, InputType input_type){
        Log.d(TAG, "Show new run called.");
        Intent intent = new Intent(context, MapActivity.class);
        Bundle args = new Bundle();
        args.putInt(KEY_INPUT_TYPE, input_type.toInteger());
        args.putBoolean(KEY_SHOWING_NEW_RUN, true);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    /* constructor method - to make launching the activity easier*/
    public static void showOldRun(Context context, long run_to_show){
        Log.d(TAG, "Show Old run called");
        Intent intent = new Intent(context, MapActivity.class);
        Bundle args = new Bundle();
        args.putLong(KEY_RUN_TO_SHOW, run_to_show);
        args.putBoolean(KEY_SHOWING_NEW_RUN, false);
        args.putInt(KEY_INPUT_TYPE, InputType.NULL.toInteger());
        intent.putExtras(args);
        context.startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INPUT_TYPE, inputType.toInteger());
        outState.putBoolean(KEY_SHOWING_NEW_RUN, showingNewRun);
        outState.putLong(KEY_RUN_TO_SHOW, runToShow);
    }

    /* Retrieve variables from Intent (if new) or savedInstanceState */
    private void loadVariables(Bundle savedInstanceState){
        Bundle bundle;
        if (savedInstanceState == null)
            bundle = getIntent().getExtras();
        else
            bundle = savedInstanceState;

        runToShow = bundle.getLong(KEY_RUN_TO_SHOW, -1);
        inputType = InputType.get(bundle.getInt(KEY_INPUT_TYPE, -1));
        showingNewRun = bundle.getBoolean(KEY_SHOWING_NEW_RUN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        loadVariables(savedInstanceState);

        // Sets up the google map fragment:
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);  // so the map uses the callback function in this activity (see below)

        // the mMap reference is assigned in the mapReady callback
        if (showingNewRun) {

            // Sets up the receiver for broadcasts from the Service (location updates):
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(UpdateReceiver.PROMPT_LOCATION_UPDATE_ACTION);
            receiver = new UpdateReceiver(this);
            registerReceiver(receiver, intentFilter);
        }
        else {
            ExerciseEntryDbHelper database = ExerciseEntryDbHelper.getDatabase(this);
            currentEntry = database.fetchEntryByIndex(runToShow);

            Button saveBtn = (Button) findViewById(R.id.map_save_button);
            Button cancelBtn = (Button) findViewById(R.id.map_cancel_button);
            saveBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if (showingNewRun) {
            bindService(
                    new Intent(
                            this,
                            LocationService.class),
                    this,
                    Context.BIND_AUTO_CREATE
            );
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (showingNewRun)
            unbindService(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("events---", "MapActivity onDestroy called");
        if (showingNewRun) {
            Log.d("events---", "unregistering receiver");
            this.unregisterReceiver(receiver);
        }
    }

    public void saveButtonAction(View view){
        saveEntry();
        stopService(new Intent(this, LocationService.class));
        finish();
    }

    public void cancelButtonAction(View view){
        stopService(new Intent(this, LocationService.class));
        EHelpers.makeToast(this, "Entry discarded.");
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        stopService(new Intent(this, LocationService.class));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(getClass().getName(), "onMapReady called");

        mMap = googleMap;

        if (!showingNewRun)
            promptMapRedraw();
    }

    public void promptMapRedraw(){
        Log.d("MapActivity", "promptMapRedraw - the MapActivity has been instructed to draw the current entry");
        ExerciseEntry entryToDraw = currentEntry;
        if (entryToDraw == null){
            Log.d("MapActivity", "ERROR: promptMapRedraw called when the current entry is null");
        }
        else {
            draw();
            //MapWriter.draw(entryToDraw, mMap, showingNewRun, runStartMarker, runEndMarker, runPath);
            updateMapText(entryToDraw);
        }
    }

    // updates the text fields displayed over the map (e.g. Distance):
    private void updateMapText(ExerciseEntry entry){
        TextView typeField = (TextView) findViewById(R.id.map_strings_type);
        String typeFieldText = "Type: " + entry.getActivityType();
        typeField.setText(typeFieldText);

        TextView distanceField = (TextView) findViewById(R.id.map_strings_distance);
        String distText = "Distance: ";
        boolean useMiles = !SettingsTab.userPrefersMetric(getApplicationContext());
        if (useMiles){
            distText += roundDouble(entry.getDistanceMiles()) + " mi";
        }
        else {
            distText += roundDouble(entry.getDistanceKilometers()) + " km";
        }
        distanceField.setText(distText);

        TextView avgSpeed = (TextView) findViewById(R.id.map_strings_avg_speed);
        String text = "Avg speed: ";
        if (useMiles)
            text += ConversionHelpers.roundDouble(entry.getAverageSpeedMiles()) + " m/h";
        else
            text += ConversionHelpers.roundDouble(entry.getAverageSpeedKilos()) + " km/h";
        avgSpeed.setText(text);

        TextView currentSpeed = (TextView) findViewById(R.id.map_strings_current_speed);
        text = "Curr speed: ";
        if (useMiles)
            text += ConversionHelpers.roundDouble(entry.getCurrentSpeedMiles()) + " m/h";
        else
            text += ConversionHelpers.roundDouble(entry.getCurrentSpeedKilos()) + " km/h";
        currentSpeed.setText(text);

        TextView climb = (TextView) findViewById(R.id.map_strings_climb);
        Double theClimb = entry.getClimb();
        if (theClimb == null)
            theClimb = (double) 0;
        text = "Climb: " + theClimb + " meters";
        climb.setText(text);

        setTextField(R.id.map_strings_calorie, "Calories: " + entry.getCalories());
        setTextField(R.id.map_strings_duration, "Duration: " + convertSeconds(entry.getDuration()));

    }

    private void setTextField(int viewId, String text){
        TextView textView = (TextView) findViewById(viewId);
        textView.setText(text);
    }

    public void saveEntry(){
        ExerciseEntry entry = service.mEntry;
        entry.setInputType(inputType);

        // time/date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        entry.setDate("" + Integer.toString(year) + " " + ManualEntryActivity.getMonth(month) + " " + Integer.toString(day));

        c = Calendar.getInstance();
        int hourN = c.get(Calendar.HOUR_OF_DAY);
        int minuteN = c.get(Calendar.MINUTE);
        String hour, minute, AmPm;
        AmPm = "";
        if (hourN < 12) {
            AmPm = " AM";
        } else {
            AmPm = " PM";
            hourN = hourN - 12;
        }
        hour = "" + hourN;
        if (minuteN < 10) {
            minute = "0" + minuteN;
        } else {
            minute = "" + minuteN;
        }
        entry.setTime("" + hour + ":" + minute + AmPm);

        // save in background:
        WriteDataTask.write(entry, this);
    }

    // For showing the "delete" option at the top when viewing an old run:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (!showingNewRun) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.map_bar_menu, menu);
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        return true;
    }

    // For handling clicks of the "action bar menu" (top of the app, right of the name):
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemID = item.getItemId();

        switch (clickedItemID) {
            case R.id.delete_data:
                Log.d("events", "delete button clicked");
                DeleteDataTask.deleteEntry(runToShow, this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Callbacks for ServiceConnection */

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d("LocationConnection", "onServiceDisconnected");
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Log.d("MapActivity", "MapActivity the service was connected");
        MyBinder b = (MyBinder) binder;
        service = b.service;
    }

    /* End ServiceConnection callbacks */

    @Override
    public Loader<ExerciseEntry> onCreateLoader(int i, Bundle bundle) {
        return new EntryLoader(this, runToShow); // DataLoader is your AsyncTaskLoader.
    }

    @Override
    public void onLoadFinished(Loader<ExerciseEntry> loader, ExerciseEntry entry) {
        currentEntry = entry;
    }

    @Override
    public void onLoaderReset(Loader<ExerciseEntry> loader) {
    }

    /* Map Drawing: */
    private void draw() {

        if (mMap == null) {
            Log.e(TAG, "Google Map was null");
            return;
        }
        if (currentEntry == null) {
            Log.e(TAG, "ExerciseEntry was null");
            return;
        }

        ArrayList<LatLng> path = currentEntry.getLocations();

        LatLng startLocation = path.get(0);
        LatLng endLocation = path.get(path.size() - 1);

        /* sets up the MARKERS */
        if (runStartMarker == null)
            runStartMarker = addMarker(mMap, startLocation, Color.RED);
        else
            runStartMarker.setPosition(startLocation);

        if (runEndMarker == null) {
            if (path.size() > 1)
                runEndMarker = addMarker(mMap, endLocation, Color.GREEN);
        } else {
            runEndMarker.setPosition(endLocation);
        }

        /* sets up the POLYLINE */
        if (runPath == null) {
            if (path.size() > 1) {
                PolylineOptions polylineOptions = new PolylineOptions();
                for (LatLng l : path) {
                    polylineOptions.add(l);
                }
                runPath = mMap.addPolyline(polylineOptions);
            }
        } else {
            runPath.setPoints(path);
        }

        /* If we're showing an active run, zoom in on the current location: */
        if (showingNewRun) {

            boolean zoomNecessary = false;

            // Is it necessary to zoom?
            if (path.size() == 1)
                zoomNecessary = true;
            else if (path.size() % 10 == 0)
                zoomNecessary = true;

            // zoom in if we're too far out (and only check every 4th update):
            float zoom = mMap.getCameraPosition().zoom;
            if (Math.abs(zoom - DEFAULT_ZOOM) > 2 && path.size() % 4 == 0)
                zoomNecessary = true;

            if (zoomNecessary)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endLocation, DEFAULT_ZOOM));

        }
        /* If we're showing a completed run, adjust the camera to show the entire run path: */
        else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng loc : path) {
                builder.include(loc);
            }
            LatLngBounds bounds = builder.build();
            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
        }

        /* If it's Automatic mode, guess the current activity: */
        if (inputType == InputType.AUTOMATIC)
            guessActivityType(currentEntry);
    }

    private enum Color{
        RED,
        GREEN
    }

    /* Adds a marker to the map at the specified position, with the given color -
        colors accessed like this: BitmapDescriptorFractory.COLOR_RED   */
    private static Marker addMarker(GoogleMap mMap, LatLng position, Color color){

        float hue;
        switch(color){
            case RED:
                hue = BitmapDescriptorFactory.HUE_RED;
                break;
            case GREEN:
                hue = BitmapDescriptorFactory.HUE_GREEN;
                break;
            default:
                hue = BitmapDescriptorFactory.HUE_MAGENTA;
        }

        return mMap.addMarker(
                new MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(hue)));
    }

    private void guessActivityType(ExerciseEntry entry){

        /* Makes a guess based on current speed */
        ActivityType guessedType;
        if (entry.getCurrentSpeedKilos() < 3)
            guessedType = ActivityType.STANDING;
        else if (entry.getCurrentSpeedKilos() < 8)
            guessedType = ActivityType.WALKING;
        else if (entry.getCurrentSpeedKilos() < 18)
            guessedType = ActivityType.RUNNING;
        else
            guessedType = ActivityType.OTHER;
        submitActivityGuess(guessedType, entry);
    }

    private void submitActivityGuess(ActivityType guessedType, ExerciseEntry entry){
        TextView standingDisplay = (TextView) findViewById(R.id.map_standing_guess);
        TextView walkingDisplay = (TextView) findViewById(R.id.map_walking_guess);
        TextView runningDisplay = (TextView) findViewById(R.id.map_running_guess);
        TextView otherDisplay = (TextView) findViewById(R.id.map_other_guess);

        standingDisplay.setText("Standing: " + entry.standingGuesses);
        walkingDisplay.setText("Walking: " + entry.walkingGuesses);
        runningDisplay.setText("Running: " + entry.runningGuesses);
        otherDisplay.setText("Other: " + entry.otherGuesses);
    }

    // rounds to two decimal places
    public static String roundDouble(double number){
        return String.format("%.2f", number);
    }

    // converts a number of seconds into a HH:MM:SS string format:
    public static String convertSeconds(int totalSecs){
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
