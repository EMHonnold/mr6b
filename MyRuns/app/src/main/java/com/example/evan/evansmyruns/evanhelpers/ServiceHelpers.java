package com.example.evan.evansmyruns.evanhelpers;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by Evan on 5/1/2016.
 *
 * Static, self-contained helper functions relating to services.
 *
 */
public class ServiceHelpers {

    // A function to check if a service is running, given the service's class name (e.g. MyLocationService.class)
    public static boolean isMyServiceRunning(Class<?> serviceClass, Activity currentActivity) {
        ActivityManager manager = (ActivityManager) currentActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void printAllRunningServices(Activity currentActivity){
        Log.d("service_printout", "-----Printing out all currently running services: -----");
        ActivityManager manager = (ActivityManager) currentActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            Log.d("service_printout", service.service.getClassName());
        }
        Log.d("service_printout", "----- Done printing out all currently running services -----");
    }

}
