package com.example.evan.evansmyruns.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Evan on 5/4/2016.
 *
 *  A class for deleting ExerciseEntries from
 *  the database using a background task.
 *
 *  Usage: call the static method deleteEntry -
 *  no need to instantiate a member of the class.
 *
 *  Input: the database-ID of the entry (stored
 *  in the entry's ID field) and the current
 *  context (can use getContext, getApplication).
 */
// async task for deleting the database item (extra credit):
public class DeleteDataTask extends AsyncTask<Long, Void, Void> {

    // for making a Toast after deletion:
    private static Context mContext;

    public static void deleteEntry(long entryID, Context context){
        mContext = context;
        new DeleteDataTask().execute(entryID);
    }

    @Override
    protected Void doInBackground(Long... removeID) {

        ExerciseEntryDbHelper database = ExerciseEntryDbHelper.getDatabase();
        database.removeEntry(removeID[0]);
        return null;

    }

    @Override
    protected void onPostExecute(Void v) {
        try {
            Toast.makeText(mContext, "The entry was deleted using a background thread.", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Log.d("Error", "DeleteDataTask toast failed");
        }
    }
}
