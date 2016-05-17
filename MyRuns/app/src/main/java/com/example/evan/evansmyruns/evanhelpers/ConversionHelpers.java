package com.example.evan.evansmyruns.evanhelpers;

/**
 * Re-usable conversion functions: Created by Evan on 5/3/2016.
 */
public class ConversionHelpers {

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
