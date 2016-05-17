package com.example.evan.evansmyruns.evanhelpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Evan on 5/3/2016.
 *
 */
public class PermissionHelpers {

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
            EHelpers.makeToast(activity, "strange result from permissionGranted check");
            return false;
        }
    }
}
