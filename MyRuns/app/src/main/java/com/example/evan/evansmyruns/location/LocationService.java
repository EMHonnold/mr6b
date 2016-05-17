package com.example.evan.evansmyruns.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.evan.evansmyruns.classifier.FFT;
import com.example.evan.evansmyruns.classifier.WekaClassifierNew;
import com.example.evan.evansmyruns.entry.ActivityType;
import com.example.evan.evansmyruns.map.MapActivity;
import com.example.evan.evansmyruns.R;
import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.entry.InputType;
import com.example.evan.evansmyruns.reusablecode.CustomServiceNotification;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Evan on 5/8/2016.
 *
 */
public class LocationService
        extends     Service
        implements  GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    LocationListener,
                    SensorEventListener
{
    /* ----- STATICS ----- */

    public static final String TAG = "LocationServiceNew";      // Used to identify this in the Log debug statements
    public static int UPDATE_FREQUENCY;             // How often we get new location updates (in milliseconds)
    public static final String KEY_ACTIVITY_TYPE = "activity_type";
    public static final String KEY_INPUT_TYPE = "input_type";

    /* Method to start this service without having to type out the Intent stuff: */
    public static void start(Context context, int updateFrequency, ActivityType activityType, InputType input_type){
        Log.v(TAG, "start called");
        UPDATE_FREQUENCY = updateFrequency;
        Intent intent = new Intent(context, LocationService.class);
        intent = ActivityType.addToIntent(activityType, intent);
        intent = InputType.addToIntent(input_type, intent);
        context.startService(intent);
    }

    /* ----- NON-STATICS ----- */

    private GoogleApiClient mGoogleApiClient;       // Used when starting the location updates
    private CustomServiceNotification notification; // Custom class for the notification
    private MyBinder mBinder;                       // Used to connect this service to an activity
    public ExerciseEntry mEntry;                    // Saves the ExerciseEntry that's currently being recorded
    private InputType inputType;
    private ActivityType activityType;

    /* When the service is created, set up the Google API to get the location: */
    @Override
    public void onCreate(){
        Log.v(TAG, "onCreate called");

        // Sets up the ExerciseEntry and activity connection stuff:
        mEntry = new ExerciseEntry();
        mBinder = new MyBinder(this);

        // Sets up the location tracker:
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        // Sets up the notification:
        notification = new CustomServiceNotification(this, MapActivity.class,
                "MyRuns: recording your path now",
                "Touch to return to the app",
                R.drawable.black_white_android_running);
        notification.show(this);

        super.onCreate();
    }

    /* The return value of START_NOT_STICKY ensures the service won't
    be restarted when the app is closed: */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand called");

        inputType = InputType.getFromIntent(intent);
        activityType = ActivityType.getFromIntent(intent);
        mEntry.setInputType(inputType);
        if (inputType == InputType.GPS)
            mEntry.setActivityType(activityType);
        else if (inputType == InputType.AUTOMATIC)
            mEntry.setActivityType(ActivityType.UNKNOWN);

         /* Classification: */
        if (inputType == InputType.AUTOMATIC) {
            mAccBuffer = new ArrayBlockingQueue<Double>(
                    ACCELEROMETER_BUFFER_CAPACITY);

            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mAccelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);

            activityTask = new UpdateActivityTask();
            activityTask.execute();
            Log.e(TAG, "The task's execute was called");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        Log.v(TAG, "onDestroy called");
        mGoogleApiClient.disconnect();
        notification.remove(this);

        /* Classification */
        if (inputType == InputType.AUTOMATIC) {
            activityTask.cancel(true);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){}
            mSensorManager.unregisterListener(this);
        }
        /*                 */

        super.onDestroy();
    }

    /* This method is called when the Google API service connects to this service;
    Sets up the recurring location updates */
    public void onConnected(Bundle arg0) {
        Log.v(TAG, "onConnected - google service ready");
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_FREQUENCY);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e){
            Toast.makeText(getApplicationContext(), "Permission not granted " +
                    "- location updates can't happen", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onLocationChanged(Location loc) {
        Log.v(TAG, "the location was changed");
        mEntry.addLocation(loc);

        // Sends a broadcast to update the locations:
        Intent intent = new Intent();
        intent.setAction(UpdateReceiver.PROMPT_LOCATION_UPDATE_ACTION);
        intent.putExtra(UpdateReceiver.UPDATE_LOCATIONS_KEY, UpdateReceiver.UPDATE_LOCATIONS_COMMAND);
        sendBroadcast(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(getClass().getSimpleName(), "GoogleApiClient connection has been suspended");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(getClass().getSimpleName(), "GoogleApiClient connection has failed");
    }

    /* Makes sure the service stops when the app closes unexpectedly: */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    /* --------------------------------------*/
    /* Code for the activity classification: */
    /* --------------------------------------*/
    public void onAccuracyChanged(Sensor sensor, int val){    }

    private static ArrayBlockingQueue<Double> mAccBuffer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private UpdateActivityTask activityTask;
    private static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    private static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;

    public void onSensorChanged(SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            double m = Math.sqrt(event.values[0] * event.values[0]
                    + event.values[1] * event.values[1] + event.values[2]
                    * event.values[2]);

            // Inserts the specified element into this queue if it is possible
            // to do so immediately without violating capacity restrictions,
            // returning true upon success and throwing an IllegalStateException
            // if no space is currently available. When using a
            // capacity-restricted queue, it is generally preferable to use
            // offer.
            try {
                mAccBuffer.add(new Double(m));
            } catch (IllegalStateException e) {

                // Exception happens when reach the capacity.
                // Doubling the buffer. ListBlockingQueue has no such issue,
                // But generally has worse performance
                ArrayBlockingQueue<Double> newBuf = new ArrayBlockingQueue<Double>(
                        mAccBuffer.size() * 2);

                mAccBuffer.drainTo(newBuf);
                mAccBuffer = newBuf;
                mAccBuffer.add(new Double(m));
            }
        }
    }

    /* Decodes the classification - given a code from the Classifier,
     * send a guess of the current activity to the ExerciseEntry.*/
    private void guessActivity(double classifierCode){

        ActivityType guessedType;

        // convert double to int:
        int code = (int) classifierCode;

        // parse the code:
        switch (code){
            case 0:
                guessedType = ActivityType.STANDING;
                break;
            case 1:
                guessedType = ActivityType.WALKING;
                break;
            case 2:
                guessedType = ActivityType.RUNNING;
                break;
            case 3:
                guessedType = ActivityType.OTHER;
                break;
            default:
                guessedType = ActivityType.CROSS_COUNTRY_SKIING;
                Log.e("WTF", "classifier gave unexpected result: " + classifierCode);
        }

        mEntry.addActivityGuess(guessedType);
    }

    /* This is the AsyncTask that handles activity classification in Automatic mode.
     * The task takes accelerometer readings, gets an overall magnitude using FFT
     * (Fast Fourier Transform), collects these magnitudes in blocks of 64 (+1 max),
     * and feeds them into the classifier to get an activity type guess.
     *
     * Adapted from DataCollector demo code.
     * */
    private class UpdateActivityTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            int blockSize = 0;
            ArrayList<Double> accList = new ArrayList<Double>();
            int classifyActivity;
            FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double[] re = accBlock;
            double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];

            double max = Double.MIN_VALUE;

            while (true) {
                try {
                    // need to check if the AsyncTask is cancelled or not in the while loop
                    if (isCancelled () == true)
                        return null;

                    // Dumping buffer
                    accBlock[blockSize++] = mAccBuffer.take().doubleValue();

                    if (blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;

                        max = .0;
                        for (double val : accBlock) {
                            if (max < val) {
                                max = val;
                            }
                        }

                        fft.fft(re, im);

                        for (int i = 0; i < re.length; i++) {
                            double mag = Math.sqrt(re[i] * re[i] + im[i]
                                    * im[i]);

                            accList.add(mag);

                            im[i] = .0; // Clear the field
                        }

                        accList.add(max);

                        /* transforms the "accList" into an Array and takes the last 64 values from it,
                          then feeds those values into the classifier */
                        Object[] theArray = accList.toArray();
                        Log.e(TAG, "Array Size: " + theArray.length);
                        Object[] last65vals = new Object[65];
                        for (int i = 0; i < 65; i++){
                            last65vals[i] = theArray[theArray.length - 65 + i];
                        }

                        // prints out those last 65 values:
                        String input = "";
                        for (Object obj : last65vals){
                            double d = (double) obj;
                            input += ", " + d;
                        }
                        Log.e(TAG, "input:" + input);

                        /* ------------------------*/
                        /* This is the big line - takes an array of objects and returns an activity code : */
                        double result = WekaClassifierNew.classify(last65vals);
                        /* ------------------------*/

                        // Classification logic in this method:
                        guessActivity(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled() {}
    }
}
