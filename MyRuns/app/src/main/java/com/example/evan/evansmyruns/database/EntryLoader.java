package com.example.evan.evansmyruns.database;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.evan.evansmyruns.entry.ExerciseEntry;

/**
 * Created by Evan on 5/4/2016.
 *
 */
public class EntryLoader extends AsyncTaskLoader<ExerciseEntry> {

    Context mContext;
    long id;


    public EntryLoader(Context context, long entryID) {
        super(context);
        mContext = context;
        id = entryID;
    }

    public ExerciseEntry loadInBackground() {
        ExerciseEntryDbHelper database = ExerciseEntryDbHelper.getDatabase(mContext);
        ExerciseEntry entry = database.fetchEntryByIndex(id);
        return entry;
    }

}