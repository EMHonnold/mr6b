package com.example.evan.evansmyruns.evanhelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Evan on 4/10/2016.
 *
 * Helper functions for MyRuns labs.
 */
public class EHelpers {

    // returns the current time in the format e.g. 2:32:12 for 2:32am
    public static String getCurrentTimeHHMMSS(){
        Calendar c = Calendar.getInstance();
        return "" + c.get(Calendar.HOUR_OF_DAY) + ":" + getCurrentTimeMinutes(c) + ":" + getCurrentTimeSeconds(c);
    }

    // returns a two-character string
    public static String getCurrentTimeSeconds(Calendar c){
        int seconds = c.get(Calendar.SECOND);
        if (seconds < 10){
            return "0" + seconds;
        }
        else {
            return "" + seconds;
        }
    }

    // returns a two-character string
    public static String getCurrentTimeMinutes(Calendar c) {
        int minutes = c.get(Calendar.MINUTE);
        if (minutes < 10) {
            return "0" + minutes;
        } else {
            return "" + minutes;
        }
    }

    public static void printError(Activity activity, String message){
        Log.d("ERROR", "ERR MSG: " + message);
        EHelpers.makeToast(activity, message);
    }

    // Helper function to create a spinner.
    // The "view" parameter is the thing returned by onCreateView.
    //    If using this function in onCreateView,
    //       get the view from inflater.inflate [...].
    //    If using this function after onCreateView is called,
    //       use getView().
    //    If used before onCreateView is called, this function will cause a crash.
    // The "id" parameter is the ID given to the spinner in the XML.
    // The "context" parameter is returned by getActivity() in a fragment.
    public static Spinner createSpinner(View view, int spinnerID, Context context, int contentsArrayID){

        Spinner newSpinner = (Spinner) view.findViewById(spinnerID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                contentsArrayID,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newSpinner.setAdapter(adapter);

        return newSpinner;
    }

    // helper function for displaying a Toast:
    // (get the context with getApplicationContext()
    public static void makeToast(Activity currentActivity, String message){
        Toast.makeText(currentActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    // helper function:
    // checks if a permission has been granted.
    // The permission string must be from Manifest.permission
    public static boolean permissionGranted(Activity activity, String permission){

        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else if (permissionCheck == PackageManager.PERMISSION_DENIED){
            return false;
        }
        else {
            Log.d("ERROR", "Strange result from permissionGranted check");
            makeToast(activity, "strange result from permissionGranted check");
            return false;
        }
    }
}
