package com.example.evan.evansmyruns.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.evan.evansmyruns.map.MapActivity;

/**
 * Created by Evan on 5/4/2016.
 *
 */
public class UpdateReceiver extends BroadcastReceiver {

    public static final String PROMPT_LOCATION_UPDATE_ACTION = "pass_to_intents_to_prompt_update";
    public static final String UPDATE_LOCATIONS_KEY ="updateLocations";
    public static final int UPDATE_LOCATIONS_COMMAND = 2734;    // arbitrary number

    MapActivity mActivity;

    public UpdateReceiver(MapActivity activity){
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent){

        Log.d("MapActivity", "MapActivity onReceive called");

        int command = intent.getIntExtra(UPDATE_LOCATIONS_KEY, -1);
        if (command == UPDATE_LOCATIONS_COMMAND) {
            Log.d("MapActivity", "MapActivity onReceive - command was to update the map - calling updateMap");
            mActivity.currentEntry = mActivity.service.mEntry;
            mActivity.promptMapRedraw();
        }
    }
}