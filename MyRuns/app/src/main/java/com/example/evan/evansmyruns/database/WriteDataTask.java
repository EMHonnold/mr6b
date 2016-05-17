package com.example.evan.evansmyruns.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.evan.evansmyruns.entry.ExerciseEntry;

/**
 * Created by Evan on 5/4/2016.
 *
 * A class for adding data entries to the database 
 * in an async task. 
 *
 * USAGE: call the static method rather than creating an instance
 *
 */
public class WriteDataTask extends AsyncTask<ExerciseEntry, Void, String> {

    private static Context mContext;    // for showing a Toast

    public static void write(ExerciseEntry entryToAdd, Context context){
        mContext = context;
        new WriteDataTask().execute(entryToAdd);
    }

    @Override
    protected String doInBackground(ExerciseEntry... entry) {

        ExerciseEntryDbHelper database = ExerciseEntryDbHelper.getDatabase();
        long num = database.insertEntry(entry[0]);

        return "Entry #" + num + " saved.";
    }

    @Override
    protected void onPostExecute(String result) {
        // Getting reference to the TextView tv_counter of the layout activity_main
        try {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        } catch (Exception e){}
    }
}

