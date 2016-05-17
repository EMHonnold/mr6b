package com.example.evan.evansmyruns.entry;

import android.location.Location;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Evan on 4/23/2016.
 *
 * This class implements the Exercise Entry object. These objects
 * are used to translate between information in the database and
 * information on the UI (the database entries themselves are the
 * individual primitives that compose an ExerciseEntry.
 *
 * Also includes some static helpers for conversions, e.g. from
 * integer to string representations of a time.
 *
 */

public class ExerciseEntry {
    private String mDate;    // When does this entry happen
    private String mTime;
    private Integer mDuration;         // Exercise duration in seconds
    private Integer mCalorie;          // Calories burnt
    private Integer mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private Long id;               // this is the ID used by the database to identify entries
    private InputType mInputType;        // Manual, GPS or automatic
    private ActivityType mActivityType;     // Running, cycling etc.
    private Double mDistanceMiles;      // Distance traveled.
    private Double mDistanceKilometers;
    private ArrayList<LatLng> mLocationList; // Location list

    private Double averageSpeed;        // kilos/hour
    private Double climb;               // meters - cumulative, not absolute
    private Double currentSpeed;        // kilos/hour

    // this boolean is true when the phone has been moving in a direction
    // for several seconds, so we know that position changes are a result
    // of actual movement rather than variations in the GPS readings:
    boolean verifiedMovement;

    /* A temporary list of Location objects used
    for the calculation of variables while a run
    is still going on: */
    ArrayList<Location> locationObjs;

    /* A list of all the position changes between locations */
    ArrayList <Double> distances;

    public ExerciseEntry(){
        currentSpeed = (double)0;
        averageSpeed = (double) 0;
        mDuration = 0;
        mDistanceKilometers = (double) 0;
        mDistanceMiles = (double) 0;
        climb = (double)0;
        mActivityType = ActivityType.UNKNOWN;
        verifiedMovement = false;
    }

    /* Functions for automatic classification of activity type */
    private void pollGuesses(){
        // determine the activity type based on guesses so far
        if (standingGuesses > runningGuesses && standingGuesses > walkingGuesses && standingGuesses > otherGuesses)
            mActivityType = ActivityType.STANDING;
        else if (runningGuesses > standingGuesses && runningGuesses > walkingGuesses && runningGuesses > otherGuesses)
            mActivityType = ActivityType.RUNNING;
        else if (walkingGuesses > runningGuesses && walkingGuesses > standingGuesses && walkingGuesses >otherGuesses)
            mActivityType = ActivityType.WALKING;
        else if (otherGuesses > walkingGuesses && otherGuesses > runningGuesses && otherGuesses > standingGuesses)
            mActivityType = ActivityType.OTHER;
    }

    public int standingGuesses, runningGuesses, walkingGuesses, otherGuesses;

    /* For Automatic mode: sends in a guess and refreshes the overall guess */
    public void addActivityGuess(ActivityType activityType){
        if (activityType == ActivityType.STANDING)
            standingGuesses++;
        else if (activityType == ActivityType.WALKING)
            walkingGuesses++;
        else if (activityType == ActivityType.RUNNING)
            runningGuesses++;
        else if (activityType == ActivityType.OTHER)
            otherGuesses++;
        else {
            Log.d("ExerciseEntry", "AddActivityGuess given an unexpected activity: " + activityType);
        }
        pollGuesses();
    }

    /* getters */
    public Long getID() {return id;}
    public InputType getInputType() {return mInputType;}
    public ActivityType getActivityType() {return mActivityType;}
    public String getDate() { return mDate;}
    public String getTime() {return mTime;}
    public Integer getDuration() {return mDuration;}
    public Double getDistanceMiles() {return mDistanceMiles;}
    public Double getDistanceKilometers() {return mDistanceKilometers;}
    public Integer getHeartRate() {return mHeartRate;}
    public String getComment() {return mComment;}
    public Double getAverageSpeedMiles(){return getAverageSpeedKilos() / 1.609344;}
    public Double getAverageSpeedKilos(){
        return (mDistanceKilometers/mDuration) * 3600.0;
    }
    public Double getCurrentSpeedMiles(){
        return getCurrentSpeedKilos() / 1.609344;
    }
    public Double getCurrentSpeedKilos(){
        return currentSpeed;
    }
    public Double getClimb(){
        return climb;
    }
    public Integer getCalories() {
        try {   return (int)(mDuration / 10.0);}
        catch (Exception e){ return 0;}}

    /* setters */
    public void setID(Long new_id) {id = new_id;}
    public void setInputType(InputType inputType){mInputType = inputType;}
    public void setCurrentSpeedKilos(double newCurrentSpeed){currentSpeed = newCurrentSpeed;}
    public void setAverageSpeedKilos(double newAverageSpeed){averageSpeed = newAverageSpeed;}
    public void setActivityType(ActivityType activity_type) {mActivityType = activity_type;}
    public void setDate(String date) {  mDate = date;}
    public void setLocations(ArrayList<LatLng> location_list){mLocationList = location_list;}
    public void setTime(String time) {  mTime = time;}
    public void setDuration(Integer duration) {mDuration = duration;}
    public void setCalories(int calories) {mCalorie = calories;}
    public void setHeartRate(int heart_rate) {mHeartRate = heart_rate;}
    public void setComment(String comment) {mComment = comment;}
    public void setClimb(double the_climb) {climb = the_climb;}
    public void setDistanceMiles(double miles) {
        mDistanceMiles = miles;
        mDistanceKilometers = miles * 1.609344;
    }
    public void setDistanceKilometers(double kilometers) {
        mDistanceKilometers = kilometers;
        mDistanceMiles = kilometers / 1.609344;
    }

    /* Updates various values as well as adding the location: */
    public void addLocation(Location newLocation){
        LatLng coordinates = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

        /* add new location: */
        if (mLocationList == null){
            mLocationList = new ArrayList<LatLng>();
        }
        mLocationList.add(coordinates);
        if (locationObjs == null)
            locationObjs = new ArrayList<Location>();
        locationObjs.add(newLocation);

        /* calculate the distance (+ add to distance array list) */
        if (distances == null)
            distances = new ArrayList<Double>();
        if (mLocationList.size() > 1) {
            LatLng previousPos = mLocationList.get(mLocationList.size() - 2);
            LatLng newPos = mLocationList.get(mLocationList.size() - 1);
            double distanceKilometers = distanceBetweenLocations(previousPos, newPos);
            distances.add(distanceKilometers);

            // updates the sum:
            mDistanceKilometers += distanceKilometers;
            mDistanceMiles += (distanceKilometers / 1.609344);
        }

        /* calculate the duration */
        if (locationObjs.size() > 1) {
            Location startLocation = locationObjs.get(0);
            Location currentLocation = locationObjs.get(locationObjs.size() - 1);
            long startTime = startLocation.getElapsedRealtimeNanos();
            long currentTime = currentLocation.getElapsedRealtimeNanos();
            long timeDiffNanos = currentTime - startTime;
            double timeDiffSeconds = timeDiffNanos / 1000000000.0;
            mDuration = (int) timeDiffSeconds;
        }

        /* calculate the climb */
        if (locationObjs.size() > 1){

            Location lastLocation = locationObjs.get(locationObjs.size() -1);
            double lastAltitude = lastLocation.getAltitude();
            double currentAltitude = newLocation.getAltitude();

            // sometimes the locations will have the value erroneously set to 0;
            // we don't want to account for that:
            if (lastAltitude != 0 && currentAltitude != 0){
                if (lastAltitude < currentAltitude)
                    climb += (currentAltitude - lastAltitude);
            }

        }

        /* calculate the current speed */
        if (locationObjs.size() > 3){
            Location recentLocation = locationObjs.get(locationObjs.size() - 3);
            LatLng recentCoords = mLocationList.get(mLocationList.size() - 3);
            LatLng currentCoords = mLocationList.get(mLocationList.size() - 1);
            double distance = distanceBetweenLocations(recentCoords, currentCoords);
            double recentTimeNanos = recentLocation.getElapsedRealtimeNanos();
            double currentTimeNanos = newLocation.getElapsedRealtimeNanos();
            double secondsDifference = (currentTimeNanos - recentTimeNanos) / 1000000000.0;
            currentSpeed = (distance/secondsDifference) * 3600.0;   // convert from distance/second to distance/hour
        }
    }

    // Returns the distance in Kilometers between the locations of two LatLng objects.
    private double distanceBetweenLocations(LatLng firstLocation, LatLng secondLocation){

        // the distance in METERS is filled into the first position of this float array:
        float[] results = new float[1];

        // this call fills in the value but returns nothing:
        Location.distanceBetween(firstLocation.latitude,
                firstLocation.longitude,
                secondLocation.latitude,
                secondLocation.longitude,
                results);

        // convert from meters to kilometers:
        double distanceKilos = results[0] / 1000;

        return distanceKilos;
    }

    public ArrayList<LatLng> getLocations(){
        return mLocationList;
    }
}
