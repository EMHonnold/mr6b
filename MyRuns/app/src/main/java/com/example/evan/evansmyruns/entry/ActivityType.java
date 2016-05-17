package com.example.evan.evansmyruns.entry;

import android.content.Intent;

import java.util.InputMismatchException;

/**
 * Created by Evan on 5/9/2016.
 *
 * For converting between an Activity Type
 * enum and related String and integer.
 *
 * Also, functions for adding and retrieving
 * a single instance of this data type from an
 * Intent.
 *
 */
public enum ActivityType {

    RUNNING,
    WALKING,
    STANDING,
    CYCLING,
    HIKING,
    DOWNHILL_SKIING,
    CROSS_COUNTRY_SKIING,
    SNOWBOARDING,
    SKATING,
    SWIMMING,
    MOUNTAIN_BIKING,
    WHEELCHAIR,
    ELLIPTICAL,
    OTHER,
    UNKNOWN;

    private static final String S_RUNNING = "Running";
    private static final String S_WALKING = "Walking";
    private static final String S_STANDING = "Standing";
    private static final String S_CYCLING = "Cycling";
    private static final String S_HIKING = "Hiking";
    private static final String S_DOWNHILL_SKIING = "Downhill Skiing";
    private static final String S_CROSS_COUNTRY_SKIING = "Cross-Country Skiing";
    private static final String S_SNOWBOARDING = "Snowboarding";
    private static final String S_SKATING = "Skating";
    private static final String S_SWIMMING = "Swimming";
    private static final String S_MOUNTAIN_BIKING = "Mountain Biking";
    private static final String S_WHEELCHAIR = "Wheelchair";
    private static final String S_ELLIPTICAL = "Elliptical";
    private static final String S_OTHER = "Other";
    private static final String S_UNKNOWN = "Unknown";

    private static final int I_RUNNING = 1;
    private static final int I_WALKING = 2;
    private static final int I_STANDING = 3;
    private static final int I_CYCLING = 4;
    private static final int I_HIKING = 5;
    private static final int I_DOWNHILL_SKIING = 6;
    private static final int I_CROSS_COUNTRY_SKIING = 7;
    private static final int I_SNOWBOARDING = 8;
    private static final int I_SKATING = 9;
    private static final int I_SWIMMING = 10;
    private static final int I_MOUNTAIN_BIKING = 11;
    private static final int I_WHEELCHAIR = 12;
    private static final int I_ELLIPTICAL = 13;
    private static final int I_OTHER = 14;
    private static final int I_UNKNOWN = 15;

    public int toInteger(){
        switch (this){
            case RUNNING: return I_RUNNING;
            case WALKING: return I_WALKING;
            case STANDING: return I_STANDING;
            case CYCLING: return I_CYCLING;
            case HIKING: return I_HIKING;
            case DOWNHILL_SKIING: return I_DOWNHILL_SKIING;
            case CROSS_COUNTRY_SKIING: return I_CROSS_COUNTRY_SKIING;
            case SNOWBOARDING: return I_SNOWBOARDING;
            case SKATING: return I_SKATING;
            case SWIMMING: return I_SWIMMING;
            case MOUNTAIN_BIKING: return I_MOUNTAIN_BIKING;
            case WHEELCHAIR: return I_WHEELCHAIR;
            case ELLIPTICAL: return I_ELLIPTICAL;
            case OTHER: return I_OTHER;
            case UNKNOWN: return I_UNKNOWN;
            default: throw new InputMismatchException();
        }
    }
    public String toString(){
        switch (this){
            case RUNNING: return S_RUNNING;
            case WALKING: return S_WALKING;
            case STANDING: return S_STANDING;
            case CYCLING: return S_CYCLING;
            case HIKING: return S_HIKING;
            case DOWNHILL_SKIING: return S_DOWNHILL_SKIING;
            case CROSS_COUNTRY_SKIING: return S_CROSS_COUNTRY_SKIING;
            case SNOWBOARDING: return S_SNOWBOARDING;
            case SKATING: return S_SKATING;
            case SWIMMING: return S_SWIMMING;
            case MOUNTAIN_BIKING: return S_MOUNTAIN_BIKING;
            case WHEELCHAIR: return S_WHEELCHAIR;
            case ELLIPTICAL: return S_ELLIPTICAL;
            case OTHER: return S_OTHER;
            case UNKNOWN: return S_UNKNOWN;
            default: throw new InputMismatchException();
        }
    }
    public static ActivityType get(Integer intCode){
        switch (intCode){
            case I_RUNNING: return RUNNING;
            case I_WALKING: return WALKING;
            case I_STANDING: return STANDING;
            case I_CYCLING: return CYCLING;
            case I_HIKING: return HIKING;
            case I_DOWNHILL_SKIING: return DOWNHILL_SKIING;
            case I_CROSS_COUNTRY_SKIING: return CROSS_COUNTRY_SKIING;
            case I_SNOWBOARDING: return SNOWBOARDING;
            case I_SKATING: return SKATING;
            case I_SWIMMING: return SWIMMING;
            case I_MOUNTAIN_BIKING: return MOUNTAIN_BIKING;
            case I_WHEELCHAIR: return WHEELCHAIR;
            case I_ELLIPTICAL: return ELLIPTICAL;
            case I_OTHER: return OTHER;
            case I_UNKNOWN: return UNKNOWN;
            default: throw new InputMismatchException("Bad activity-type code: " + intCode);
        }
    }
    public static ActivityType get(String string){
        switch(string){
            case S_RUNNING: return RUNNING;
            case S_WALKING: return WALKING;
            case S_STANDING: return STANDING;
            case S_CYCLING: return CYCLING;
            case S_HIKING: return HIKING;
            case S_DOWNHILL_SKIING: return DOWNHILL_SKIING;
            case S_CROSS_COUNTRY_SKIING: return CROSS_COUNTRY_SKIING;
            case S_SNOWBOARDING: return SNOWBOARDING;
            case S_SKATING: return SKATING;
            case S_SWIMMING: return SWIMMING;
            case S_MOUNTAIN_BIKING: return MOUNTAIN_BIKING;
            case S_WHEELCHAIR: return WHEELCHAIR;
            case S_ELLIPTICAL: return ELLIPTICAL;
            case S_OTHER: return OTHER;
            case S_UNKNOWN: return UNKNOWN;
            default: throw new InputMismatchException();
        }
    }

    private static final String KEY = "ACTIVITY_TYPE_KEY";

    public static Intent addToIntent(ActivityType activityType, Intent intent){
        intent.putExtra(KEY, activityType.toInteger());
        return intent;
    }
    public static ActivityType getFromIntent(Intent intent){
        int code = intent.getIntExtra(KEY, -999);
        if (code == -999)
            throw new NullPointerException("ActivityType.java " +
                    "- Tried to get an activityType from an Intent," +
                    "but couldn't find it.");
        else
            return ActivityType.get(code);
    }

    public static String[] toArray(){
        return new String[]{S_RUNNING, S_WALKING, S_STANDING, S_CYCLING, S_HIKING, S_DOWNHILL_SKIING,
        S_CROSS_COUNTRY_SKIING, S_SNOWBOARDING, S_SKATING, S_SWIMMING, S_MOUNTAIN_BIKING,
        S_WHEELCHAIR, S_ELLIPTICAL, S_OTHER, S_UNKNOWN};
    }
}
