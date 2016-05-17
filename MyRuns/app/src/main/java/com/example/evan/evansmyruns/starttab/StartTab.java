package com.example.evan.evansmyruns.starttab;

/**
 * Created by Evan on 4/9/2016.
 * <p/>
 * One of the three tabs of the main page of the app.
 * <p/>
 * Shows options for the user to choose a type of exercise and
 * choose a way to add data about the exercise session (manually, gps, etc)
 */

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.evan.evansmyruns.MainActivity;
import com.example.evan.evansmyruns.ManualEntryActivity;
import com.example.evan.evansmyruns.R;
import com.example.evan.evansmyruns.ServerUtilities;
import com.example.evan.evansmyruns.entry.ActivityType;
import com.example.evan.evansmyruns.entry.InputType;
import com.example.evan.evansmyruns.location.LocationService;
import com.example.evan.evansmyruns.map.MapActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class StartTab extends Fragment implements View.OnClickListener {
    public int randomID;
    ActivitySpinner activitySpinner;
    InputSpinner inputSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_tab, container, false);

        inputSpinner = new InputSpinner(getActivity(), view, R.id.start_tab_input_type_dropdown);
        activitySpinner = new ActivitySpinner(getActivity(), view, R.id.start_tab_activity_type_dropdown);

        Button start = (Button) view.findViewById(R.id.start_tab_start_button);
        Button sync = (Button) view.findViewById(R.id.start_tab_sync_button);
        start.setOnClickListener(this);
        sync.setOnClickListener(this);

        return view;
    }

    private void onStartClicked(View v) {

        ActivityType activityType = activitySpinner.getSelected();
        InputType inputType = inputSpinner.getSelected();

        switch (inputType) {
            case MANUAL:
                ManualEntryActivity.start(getActivity(), activityType);
                break;
            case GPS:
                MapActivity.showNewRun(getActivity(), InputType.GPS);
                LocationService.start(getActivity(), 1000, activityType, InputType.GPS);
                break;
            case AUTOMATIC:
                MapActivity.showNewRun(getActivity(), InputType.AUTOMATIC);
                LocationService.start(getActivity(), 1000, activityType, InputType.AUTOMATIC);
                break;
            default:
                Log.d("Problem:", "onStart clicked with a weird value for the input type - see StartTab.java");
        }
    }

    // Required implementation of abstract method:
    private void onSyncClicked(View v) {
        new AsyncTask<Void, Void, String>() {

            @Override
            // Get history and upload it to the server.
            protected String doInBackground(Void... arg0) {


                // Upload the history of all entries using upload().
                String uploadState="";
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", "varun");
                    params.put("addr", "address");
                    params.put("phone", "123123");

                    ServerUtilities.post(MainActivity.SERVER_ADDR + "/add.do", params);
                } catch (IOException e1) {
                    uploadState = "Sync failed: " + e1.getCause();
                    Log.e("TAGG", "data posting error " + e1);
                }

                return uploadState;
            }

            @Override
            protected void onPostExecute(String errString) {
                String resultString;
                if(errString.equals("")) {
                    resultString =  " entry uploaded.";
                } else {
                    resultString = errString;
                }

                Toast.makeText(getContext(), resultString,
                        Toast.LENGTH_SHORT).show();

            }

        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_tab_start_button:
                onStartClicked(v);
                break;
            case R.id.start_tab_sync_button:
                onSyncClicked(v);
                break;
            default:
                throw new InputMismatchException();
        }
    }
}