package com.example.evan.evansmyruns.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.evan.evansmyruns.entry.ActivityType;
import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.entry.InputType;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Evan on 4/23/2016.
 *
 */
public class ExerciseEntryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "evan_myruns.dbi";
    private static final int DATABASE_VERSION = 1; // we don't really use versions but the constructor needs something
    private SQLiteDatabase database;

    private static ExerciseEntryDbHelper singleton;

    // Constructor
    public ExerciseEntryDbHelper(Context context) {
        // DATABASE_NAME is, of course the name of the database, which is defined as a string constant
        // DATABASE_VERSION is the version of database, which is defined as an integer constant
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    // Only some ExerciseEntryDbHelper will ever be needed, so we use this static method
    // to get it (and to create it, if it hasn't been created yet).
    public static ExerciseEntryDbHelper getDatabase(Context context){
        if (singleton == null){
            singleton = new ExerciseEntryDbHelper(context);
        }
        return singleton;
    }

    public static ExerciseEntryDbHelper getDatabase(){
        return singleton;
    }


    // Insert a item given each column value
    public long insertEntry(ExerciseEntry entry) {

        // A "ContentValues" object holds the data that's about to be added:
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_INPUT_TYPE, entry.getInputType().toInteger());
        values.put(COLUMN_NAME_ACTIVITY_TYPE, entry.getActivityType().toInteger());
        values.put(COLUMN_NAME_DATE, entry.getDate());
        values.put(COLUMN_NAME_TIME, entry.getTime());
        values.put(COLUMN_NAME_DURATION, entry.getDuration());
        values.put(COLUMN_NAME_DISTANCE, entry.getDistanceMiles());
        values.put(COLUMN_NAME_CALORIES, entry.getCalories());
        values.put(COLUMN_NAME_HEART_RATE, entry.getHeartRate());
        values.put(COLUMN_NAME_COMMENT, entry.getComment());

        // Convert the locations to a String and save it:
        ArrayList<LatLng> locations = entry.getLocations();
        if (locations != null) {
            String encodedLocations = "";
            for (LatLng currentLocation : locations) {
                double lat = currentLocation.latitude;
                double lng = currentLocation.longitude;
                String locString = "(" + Double.toString(lat) + "," + Double.toString(lng) + ")";    // N indicates the start of an entry
                encodedLocations += locString;
            }
            encodedLocations += "E";    // E = end char
            values.put(COLUMN_NAME_LOCATIONS, encodedLocations);
        }

        // puts the ContentValues into the database, getting the ID in the process:
        long insertId = database.insert(DATABASE_TABLE_NAME, null,
                values);

        return  insertId;
    }

    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
        database.delete(DATABASE_TABLE_NAME, COLUMN_NAME_ID + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public ExerciseEntry fetchEntryByIndex(long rowId) {

        Cursor cursor = database.query(
                DATABASE_TABLE_NAME,
                LIST_OF_ALL_COLUMNS,
                COLUMN_NAME_ID + " = " + rowId,
                null, null, null, null);

        cursor.moveToFirst();

        ExerciseEntry entry = cursorToEntry(cursor);
        cursor.close();

        return entry;
    }

    // Query the entire table, return all rows
    public ArrayList<ExerciseEntry> fetchEntries() {
        ArrayList<ExerciseEntry> entries = new ArrayList<ExerciseEntry>();

        Cursor cursor = database.query(DATABASE_TABLE_NAME,
                LIST_OF_ALL_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            ExerciseEntry currentEntry = cursorToEntry(cursor);
            entries.add(currentEntry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    private static final String DATABASE_TABLE_NAME = "runInfoTable";
    private static final String COLUMN_NAME_ID = "_id";
    private static final String COLUMN_NAME_INPUT_TYPE = "inputtype";
    private static final String COLUMN_NAME_ACTIVITY_TYPE = "activitytype";
    private static final String COLUMN_NAME_DATE = "date";
    private static final String COLUMN_NAME_TIME = "time";
    private static final String COLUMN_NAME_DURATION = "duration";
    private static final String COLUMN_NAME_DISTANCE = "distance";
    private static final String COLUMN_NAME_CALORIES = "calories";
    private static final String COLUMN_NAME_HEART_RATE = "heartrate";
    private static final String COLUMN_NAME_COMMENT = "comment";
    private static final String COLUMN_NAME_CLIMB = "climb";
    private static final String COLUMN_NAME_LOCATIONS = "locations";

    private static final String LIST_OF_ALL_COLUMNS[] = { COLUMN_NAME_ID, COLUMN_NAME_INPUT_TYPE, COLUMN_NAME_ACTIVITY_TYPE,
        COLUMN_NAME_DATE, COLUMN_NAME_TIME, COLUMN_NAME_DURATION, COLUMN_NAME_DISTANCE, COLUMN_NAME_CALORIES,
        COLUMN_NAME_HEART_RATE, COLUMN_NAME_COMMENT, COLUMN_NAME_CLIMB, COLUMN_NAME_LOCATIONS };


    // my setup command:
    private static final String CREATE_DATABASE_COMMAND = "create table " + DATABASE_TABLE_NAME + "("
            + COLUMN_NAME_ID + " integer primary key autoincrement,"
            + COLUMN_NAME_INPUT_TYPE + " integer,"
            + COLUMN_NAME_ACTIVITY_TYPE + " integer,"
            + COLUMN_NAME_DATE + " string,"
            + COLUMN_NAME_TIME + " string,"
            + COLUMN_NAME_DURATION + " integer,"
            + COLUMN_NAME_DISTANCE + " double,"
            + COLUMN_NAME_CALORIES + " integer,"
            + COLUMN_NAME_HEART_RATE + " integer,"
            + COLUMN_NAME_COMMENT + " string,"
            + COLUMN_NAME_CLIMB + " double,"
            + COLUMN_NAME_LOCATIONS + " string"
            + ");";

    private ExerciseEntry cursorToEntry(Cursor cursor){

        ExerciseEntry entry = new ExerciseEntry();

        entry.setID((long) cursor.getInt(0));
        entry.setInputType(InputType.get(cursor.getInt(1)));
        entry.setActivityType(ActivityType.get(cursor.getInt(2)));
        entry.setDate(cursor.getString(3));
        entry.setTime(cursor.getString(4));
        entry.setDuration(cursor.getInt(5));
        entry.setDistanceMiles(cursor.getDouble(6));
        entry.setCalories(cursor.getInt(7));
        entry.setHeartRate(cursor.getInt(8));
        entry.setComment(cursor.getString(9));
        entry.setClimb(cursor.getDouble(10));

        // Parse the "locations" string:
        String locationsEncoded = cursor.getString(11);
        ArrayList<LatLng> retrievedLocations = new ArrayList<LatLng>();

        if (locationsEncoded == null){
            Log.d("ExerciseEntryDbHelper", "location string is null");
        }
        else if (locationsEncoded.length() == 0){
            Log.d("ExerciseEntryDbHelper", "location string has length 0");
        }
        else {
            char currentChar;
            int charPos = 1;
            currentChar = locationsEncoded.charAt(charPos);

            while (true){
                String lat = "";
                while (currentChar != ','){
                    lat += currentChar;

                    charPos ++;
                    currentChar = locationsEncoded.charAt(charPos);
                }

                // skip the comma:
                charPos++;
                currentChar = locationsEncoded.charAt(charPos);

                String lng = "";
                while (currentChar != ')'){
                    lng += currentChar;
                    charPos++;
                    currentChar = locationsEncoded.charAt(charPos);
                }

                // convert the current Lat/Lng strings to LatLng object
                double latValue = Double.parseDouble(lat);
                double lngValue = Double.parseDouble(lng);
                LatLng newLL = new LatLng(latValue, lngValue);
                retrievedLocations.add(newLL);

                // are we done?
                charPos ++;
                currentChar = locationsEncoded.charAt(charPos);
                if (currentChar == 'E'){
                    break;
                }
                else {
                    // skip past the (
                    charPos++;
                    currentChar = locationsEncoded.charAt(charPos);
                }
            }
        }
        entry.setLocations(retrievedLocations);
        return entry;
    }

    /*

    provided setup command, for reference:

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS ENTRIES (\n" +
            "        _id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "        input_type INTEGER NOT NULL, \n" +
            "        activity_type INTEGER NOT NULL, \n" +
            "        date_time DATETIME NOT NULL, \n" +
            "        duration INTEGER NOT NULL, \n" +
            "        distance FLOAT, \n" +
            "        avg_pace FLOAT, \n" +
            "        avg_speed FLOAT,\n" +
            "        calories INTEGER, \n" +
            "        climb FLOAT, \n" +
            "        heartrate INTEGER, \n" +
            "        comment TEXT, \n" +
            "        gps_data BLOB );";

            */

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("events", "database helper's onCreate called");
        database.execSQL(CREATE_DATABASE_COMMAND);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // we don't use upgrading here so this function does not matter.

        /*
        Log.w(ExerciseEntryDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
        */
    }
}
