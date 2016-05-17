package com.example.evan.evansmyruns;

/**
 * Created by Evan on 4/9/2016.
 * <p/>
 * This tab shows a list of all the entries (runs, bike rides, etc.) that are
 * in the database.
 */

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.evan.evansmyruns.database.DataLoader;
import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.entry.InputType;
import com.example.evan.evansmyruns.map.MapActivity;

import java.util.ArrayList;

public class HistoryTab extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ExerciseEntry>> {

    // used by the fragmentManager to get the tab:
    public static final String HISTORY_TAB_TAG = "history_tab";

    public final int SHOW_DATA_ENTRY = 2345;
    public int randomID;
    ArrayList<ExerciseEntry> exerciseEntries;
    ExerciseEntryAdapter adapter;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("events", "history tab " + randomID + " onCreateView called");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.history_tab, container, false);

        return view;
    }

    public void refreshData() {
        Log.d("events", "history tab refresh data called");
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    // This function launches a new activity to display the exercise entry,
    // If the entry was created manually, launches DisplayEntryActivity
    // If the entry was created with the GPS, launches MapActivity
    private void displayExerciseEntry(ExerciseEntry entry){

        InputType inputType = entry.getInputType();
        switch (inputType){
            case MANUAL:
                Intent intent = new Intent(getContext(), DisplayEntryActivity.class);
                intent.putExtra(DisplayEntryActivity.ENTRY_ID_KEY, entry.getID());
                startActivityForResult(intent, 23452); // random request code since we don't care
                break;
            case AUTOMATIC:
            case GPS:
                MapActivity.showOldRun(getActivity(), entry.getID());
                break;
            default:
                Log.d(getClass().getName(), "displayExerciseEntry - the inputType value (e.g. GPS) was strange: " + inputType);
        }
    }

    public class ShowEntry {
        public void show(ExerciseEntry entry){
            HistoryTab.this.displayExerciseEntry(entry);
        }
    }

    public void refreshView() {
        Log.d("events", "history tab refresh view called");

        // loads the data entries to the UI
        ListView dataView = (ListView) getView().findViewById(R.id.history_data_list);
        boolean userPrefersMetric = SettingsTab.userPrefersMetric(getActivity());
        ShowEntry clickAction = new ShowEntry();
        adapter = new ExerciseEntryAdapter(getContext(), exerciseEntries, userPrefersMetric, clickAction);
        dataView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        Log.d("events", "history tab onResume called");
        refreshData();
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshData();
    }

    @Override
    public Loader<ArrayList<ExerciseEntry>> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(mContext); // DataLoader is your AsyncTaskLoader.
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ExerciseEntry>> loader, ArrayList<ExerciseEntry> entries) {
        exerciseEntries = entries;
        refreshView();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ExerciseEntry>> loader) {
    }
}
