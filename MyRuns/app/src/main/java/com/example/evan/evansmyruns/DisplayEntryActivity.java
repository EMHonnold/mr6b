package com.example.evan.evansmyruns;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.database.DeleteDataTask;
import com.example.evan.evansmyruns.database.ExerciseEntryDbHelper;

public class DisplayEntryActivity extends Activity {

    public static final String DATA_FIELD_DATE = "date-indicat";
    public static final String DATA_FIELD_TIME = "time-indicat";
    public static final String DATA_FIELD_DURATION = "duration-indicat";
    public static final String DATA_FIELD_DISTANCE = "distance-indicat";
    public static final String DATA_FIELD_CALORIES = "calories-indicat";
    public static final String DATA_FIELD_HEART_RATE = "heartrate-indicat";
    public static final String DATA_FIELD_COMMENT = "comment-indicat";

    public static final String ENTRY_ID_KEY = "get-data-entry-ID";
    public static final int DELETE_THE_ENTRY = 34523;
    public static final int DONT_DELETE = 34523;

    private long data_ID;
    private ExerciseEntry displayedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_run);

        Log.d("events", "Display Entry Activity's onCreate was called");

        // if the saved instance state is null, the activity was launched, so get the Intent
        if (savedInstanceState == null){
            Intent intent = getIntent();
            data_ID = intent.getLongExtra(ENTRY_ID_KEY, -1);
            if (data_ID == -1){
                Log.d("error", "couldn't get the id");
            }
        }
        else {
            // get the data out of the bundle
            data_ID = savedInstanceState.getLong(ENTRY_ID_KEY);
        }

        // load the data into the UI
        ExerciseEntryDbHelper database = ExerciseEntryDbHelper.getDatabase(getApplicationContext());
        displayedEntry = database.fetchEntryByIndex(data_ID);

        //displayedEntry = HistoryTab.getTestItemA();

        String inputTypeString;
        String activityTypeString;
        String dateTimeString;
        String durationString;
        String distanceString;
        String caloriesString;
        String heartRateString;
        String commentString;

        inputTypeString = getDataString(displayedEntry.getInputType(), null);
        activityTypeString = getDataString(displayedEntry.getActivityType(), null);
        dateTimeString = displayedEntry.getDate() + " " + displayedEntry.getTime();
        durationString = getDataString(displayedEntry.getDuration(), " minutes");

        boolean useMetric = userPrefersMetric();
        if (useMetric){
            distanceString = getDataString(roundDouble(displayedEntry.getDistanceKilometers()), " Kilometers");
        }
        else {
            distanceString = getDataString(roundDouble(displayedEntry.getDistanceMiles()), " Miles");
        }

        caloriesString = getDataString(displayedEntry.getCalories(), " cals");
        heartRateString = getDataString(displayedEntry.getHeartRate(), " bpm");
        commentString = getDataString(displayedEntry.getComment(), null);

        setTextField(R.id.display_entry_input_type, inputTypeString);
        setTextField(R.id.display_entry_activity_type, activityTypeString);
        setTextField(R.id.display_entry_date_time, dateTimeString);
        setTextField(R.id.display_entry_duration, durationString);
        setTextField(R.id.display_entry_distance, distanceString);
        setTextField(R.id.display_entry_calories, caloriesString);
        setTextField(R.id.display_entry_heart_rate, heartRateString);
        setTextField(R.id.display_entry_comment, commentString);
    }

    private void setTextField(int fieldID, String text){
        TextView textView = (TextView) findViewById(fieldID);
        textView.setText(text);
    }
    private static final String NO_DATA_INDICATOR = "None specified.";

    private String getDataString(Object object, String suffix){
        if (object == null){
            return NO_DATA_INDICATOR;
        }
        else {
            String str = object.toString();
            if (suffix != null){
                str += suffix;
            }
            return str;
        }
    }

    private String getDataStringFromID(Integer id, String suffix){
        if (id == null){
            return NO_DATA_INDICATOR;
        }
        else {
            String str = getString(id);
            if (suffix != null){
                str += suffix;
            }
            return str;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
                DeleteDataTask.deleteEntry(data_ID, this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.d("events", "Display Entry Activity's onSaveInstanceState was called");
        outState.putLong(ENTRY_ID_KEY, data_ID);
        super.onSaveInstanceState(outState);
    }

    private boolean userPrefersMetric(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String pref = preferences.getString("unit_preferences_dialog", "nothing");
        if (pref.equals("Metric"))
            return true;
        return false;
    }

    // rounds to two decimal places
    public static String roundDouble(double number){
        return String.format("%.2f", number);
    }
}
