package com.example.evan.evansmyruns.database;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.database.ExerciseEntryDbHelper;

import java.util.ArrayList;

/**
 * Created by Evan on 4/25/2016.
 *
 *
 */
public class DataLoader extends AsyncTaskLoader<ArrayList<ExerciseEntry>> {

    private Context mContext;
    //Your code here.

    public DataLoader(Context context) {
        super(context);
        mContext = context;
        //Your code here.
    }

    @Override
    protected void onStartLoading() {

        forceLoad(); //Force an asynchronous load.

    }
    @Override
    public ArrayList<ExerciseEntry> loadInBackground() {

        ExerciseEntryDbHelper database = ExerciseEntryDbHelper.getDatabase(mContext);
        return database.fetchEntries();

    }

}
