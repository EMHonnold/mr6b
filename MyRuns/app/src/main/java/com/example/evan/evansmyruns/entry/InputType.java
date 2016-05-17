package com.example.evan.evansmyruns.entry;

import android.content.Intent;

import java.util.InputMismatchException;

/**
 * Created by Evan on 5/4/2016.
 *
 *  Functions for converting between an Input Type enumerator
 *  and associated string and int values.
 *
 *  Also: functions to add and retrieve the data type from an Intent.
 *
 */

public enum InputType{

    MANUAL,
    GPS,
    AUTOMATIC,
    NULL;

    private static final int I_NULL = 0;
    private static final int I_MANUAL = 1;
    private static final int I_GPS = 2;
    private static final int I_AUTOMATIC = 3;

    private static final String S_NULL = "Null";
    private static final String S_MANUAL = "Manual Entry";
    private static final String S_GPS = "GPS";
    private static final String S_AUTOMATIC = "Automatic";

    public int toInteger(){
        switch (this){
            case MANUAL:
                return I_MANUAL;
            case GPS:
                return I_GPS;
            case AUTOMATIC:
                return I_AUTOMATIC;
            case NULL:
                return I_NULL;
            default:
                throw new NullPointerException();
        }
    }

    public String toString(){
        switch (this){
            case MANUAL:
                return S_MANUAL;
            case GPS:
                return S_GPS;
            case AUTOMATIC:
                return S_AUTOMATIC;
            case NULL:
                return S_NULL;
            default:
                throw new NullPointerException();
        }
    }

    public static InputType get(Integer intCode){
        switch(intCode){
            case I_MANUAL:
                return MANUAL;
            case I_GPS:
                return GPS;
            case I_AUTOMATIC:
                return AUTOMATIC;
            case I_NULL:
                return NULL;
            default:
                throw new InputMismatchException();
        }
    }

    public static InputType get(String string){
        switch (string){
            case S_MANUAL:
                return MANUAL;
            case S_GPS:
                return GPS;
            case S_AUTOMATIC:
                return AUTOMATIC;
            case S_NULL:
                return NULL;
            default:
                throw new InputMismatchException();
        }
    }

    private static final String KEY = "INPUT_TYPE_KEY";

    public static Intent addToIntent(InputType inputType, Intent intent){
        intent.putExtra(KEY, inputType.toInteger());
        return intent;
    }
    public static InputType getFromIntent(Intent intent){
        int code = intent.getIntExtra(KEY, -999);
        if (code == -999)
            throw new NullPointerException("InputType.java " +
                    "- Tried to get an inputType from an Intent," +
                    "but couldn't find it.");
        else
            return InputType.get(code);
    }

    public static String[] toArray(){
        return new String[]{S_MANUAL, S_GPS, S_AUTOMATIC};
    }
}