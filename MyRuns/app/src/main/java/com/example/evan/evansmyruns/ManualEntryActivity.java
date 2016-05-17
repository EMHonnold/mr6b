package com.example.evan.evansmyruns;

/**
 * Created by Evan on 4/10/2016.
 * <p/>
 * This is the activity that runs after the user clicks "start" on the main screen.
 * <p/>
 * It contains a list with items such as date, time, and duration, and clicking
 * on each item brings up a dialog to enter related information.
 * <p/>
 * The class uses a ListView.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.evan.evansmyruns.entry.ActivityType;
import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.database.ExerciseEntryDbHelper;
import com.example.evan.evansmyruns.database.WriteDataTask;
import com.example.evan.evansmyruns.entry.InputType;
import com.example.evan.evansmyruns.evanhelpers.EHelpers;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ManualEntryActivity extends Activity {

    static final String ID_DATE = "date_id", ID_TIME = "time_id", ID_DURATION = "duration_id", ID_DISTANCE = "distance_id",
            ID_CALORIES = "calories_id", ID_HEART_RATE = "heart_rate_id", ID_COMMENT = "comment_id", ID_ACTIVITY = "act";

    /* Variables to keep track of the contents of the input fields: */
    private static String date = null;
    private static String time = null;
    private static Integer duration = null;
    private static Double distance = null;
    private static Integer calories = null;
    private static Integer heart_rate = null;
    private static String comment = null;
    private static ActivityType activity_type;
    private final String[] manual_entry_fields = new String[]{
            "Date", "Time", "Duration (in seconds)", "Distance (in miles)",
            "Calories", "Heart Rate", "Comment"
    };

    /* custom static method for starting this activity easily: */
    public static void start(Context context, ActivityType activityType){
        Intent intent = new Intent(context, ManualEntryActivity.class);
        intent = ActivityType.addToIntent(activityType, intent);
        context.startActivity(intent);
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_entry);

        if (savedInstanceState == null)
            activity_type = ActivityType.getFromIntent(getIntent());

        ListView listView = (ListView) findViewById(R.id.data_list);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                manual_entry_fields);
        listView.setAdapter(mAdapter);

        // defines what happens when you click on one of the list items
        OnItemClickListener mListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String clickedItemText = ((TextView) view).getText().toString();

                switch (clickedItemText) {

                    case "Date":
                        showDialog(999);
                        break;
                    case "Time":
                        showDialog(666);
                        break;
                    case "Duration (in minutes)":
                        MyDialog.newInstance(ID_DURATION).show(getFragmentManager(), "duration");
                        break;
                    case "Distance (in miles)":
                        MyDialog.newInstance(ID_DISTANCE).show(getFragmentManager(), "distance");
                        break;
                    case "Calories":
                        MyDialog.newInstance(ID_CALORIES).show(getFragmentManager(), "calories");
                        break;
                    case "Heart Rate":
                        MyDialog.newInstance(ID_HEART_RATE).show(getFragmentManager(), "heart_rate");
                        break;
                    case "Comment":
                        MyDialog.newInstance(ID_COMMENT).show(getFragmentManager(), "comment");
                        break;
                    default:
                        Log.d("PROBLEM", "ManualEntryActivity registered an unusual clicked option - see ManualEntryActivity.java");
                }
            }
        };

        // Get the ListView and wire the listener
        listView.setOnItemClickListener(mListener);

        // handles persistence
        if (savedInstanceState != null) {
            date = savedInstanceState.getString(ID_DATE, null);
            time = savedInstanceState.getString(ID_TIME, null);
            activity_type = ActivityType.get(savedInstanceState.getInt(ID_ACTIVITY));

            Integer temp_dur = savedInstanceState.getInt(ID_DURATION, Integer.MIN_VALUE);
            if (temp_dur != Integer.MIN_VALUE) {
                duration = temp_dur;
            }
            Double tempDist = savedInstanceState.getDouble(ID_DISTANCE, Integer.MIN_VALUE);
            if (tempDist != Integer.MIN_VALUE) {
                distance = tempDist;
            }
            int temp_cal = savedInstanceState.getInt(ID_CALORIES, Integer.MIN_VALUE);
            if (temp_cal != Integer.MIN_VALUE) {
                calories = temp_cal;
            }
            int temp_hr = savedInstanceState.getInt(ID_HEART_RATE, Integer.MIN_VALUE);
            if (temp_hr != Integer.MIN_VALUE) {
                heart_rate = temp_hr;
            }
            comment = savedInstanceState.getString(ID_COMMENT, null);
            activity_type = ActivityType.get(savedInstanceState.getInt(ID_ACTIVITY, Integer.MIN_VALUE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (date != null) {
            outState.putString(ID_DATE, date);
        }
        if (time != null) {
            outState.putString(ID_TIME, time);
        }
        if (duration != null) {
            outState.putDouble(ID_DURATION, duration);
        }
        if (distance != null) {
            outState.putDouble(ID_DISTANCE, distance);
        }
        if (calories != null) {
            outState.putInt(ID_CALORIES, calories);
        }
        if (heart_rate != null) {
            outState.putInt(ID_HEART_RATE, heart_rate);
        }
        if (comment != null) {
            outState.putString(ID_COMMENT, comment);
        }
        if (activity_type != null) {
            outState.putInt(ID_ACTIVITY, activity_type.toInteger());
        }
        super.onSaveInstanceState(outState);
    }

    public void saveButtonAction(View view) {

        InputType input_type = InputType.MANUAL;

        // puts in default values if any are null:
        if (date == null) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            date = "" + Integer.toString(year) + " " + getMonth(month) + " " + Integer.toString(day);
        }

        if (time == null) {
            Calendar c = Calendar.getInstance();
            int hourN = c.get(Calendar.HOUR_OF_DAY);
            int minuteN = c.get(Calendar.MINUTE);
            String hour, minute, AmPm;
            AmPm = "";
            if (hourN < 12) {
                AmPm = " AM";
            } else {
                AmPm = " PM";
                hourN = hourN - 12;
            }
            hour = "" + hourN;
            if (minuteN < 10) {
                minute = "0" + minuteN;
            } else {
                minute = "" + minuteN;
            }
            time = "" + hour + ":" + minute + AmPm;
        }

        if (distance == null) {
            distance = (double) 0;
        }
        if (duration == null) {
            duration = 0;
        }
        if (calories == null) {
            calories = 0;
        }
        if (heart_rate == null) {
            heart_rate = 0;
        }
        if (comment == null) {
            comment = "";
        }

        // saves the data to the database
        Log.d("data", "activity type : " + activity_type);
        Log.d("data", "input type    : " + input_type);
        Log.d("data", "date          : " + date);
        Log.d("data", "time          : " + time);
        Log.d("data", "distance      : " + distance);
        Log.d("data", "duration      : " + duration);
        Log.d("data", "calories      : " + calories);
        Log.d("data", "heart_rate    : " + heart_rate);
        Log.d("data", "comment       : " + comment);

        ExerciseEntry newEntry = new ExerciseEntry();
        newEntry.setActivityType(activity_type);
        newEntry.setInputType(input_type);
        newEntry.setDate(date);
        newEntry.setTime(time);
        newEntry.setDistanceMiles(distance);
        newEntry.setDuration(duration);
        newEntry.setCalories(calories);
        newEntry.setHeartRate(heart_rate);
        newEntry.setComment(comment);

        // writes the new exercise entry to the database (first making sure the database is set up):
        ExerciseEntryDbHelper.getDatabase(getApplicationContext());
        WriteDataTask.write(newEntry, this);

        // clears the data
        clearAndClose();
    }

    public void cancelButtonAction(View view) {
        EHelpers.makeToast(this, "Entry discarded.");
        clearAndClose();
    }

    private void clearAndClose() {
        date = null;
        time = null;
        distance = null;
        duration = null;
        calories = null;
        heart_rate = null;
        comment = null;
        finish();
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 999) {

            Calendar calendar = Calendar.getInstance();

            int year, month, day;
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            // private class for the openDatePicker function:
            DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    date = "" + arg1 + " " + getMonth(arg2) + " " + arg3;
                    Log.d("events", date);
                }
            };

            return new DatePickerDialog(this, myDateListener, year, month, day);
        } else if (id == 666) {

            Calendar calendar = Calendar.getInstance();

            int hour, minute;
            hour = calendar.get(Calendar.HOUR);
            minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
                    // whatever should happen when the time is set...
                    int hour = arg1;

                    if (hour < 12) {
                        time = Integer.toString(hour) + ":" + arg2 + " AM";
                    } else {
                        if (hour > 12)
                            hour = hour - 12;

                        time = Integer.toString(hour) + ":" + arg2 + " PM";
                    }
                    Log.d("events", time);

                }
            };

            return new TimePickerDialog(this, myTimeListener, hour, minute, false);
        }
        return null;
    }

    public static class MyDialog extends DialogFragment {

        // So the fragment can remember which information (e.g. duration, distance, calories)
        // is to be modified if the user clicks "OK" after entering stuff in the dialog:
        static String currentFieldID;

        // Keeps track of the text currently in the text-entry field so it can be saved if
        // the app is minimized, the screen is turned, etc:
        static String currentText;

        // the box in the dialog where the user types
        EditText userInputField;

        // call this function (instead of the constructor) to create the dialog, so you can
        // give an argument indicating which data field is being modified:
        static MyDialog newInstance(String content_identifier) {
            MyDialog newDialog = new MyDialog();

            currentFieldID = content_identifier;

            // Put the ID (e.g. indicating "duration" or "distance") into the new fragment's Bundle:
            Bundle args = new Bundle();
            args.putString("content_id", content_identifier);
            newDialog.setArguments(args);

            return newDialog;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("content_id", currentFieldID);
            currentText = userInputField.getText().toString();
            outState.putString("current_text", currentText);
        }

        // This function loads the saved values that identify the data that the dialog box is
        // editing & whatever the user has already typed in, and then constructs (or re-constructs)
        // the dialog box based on that information.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // reloads the saved information:
            if (savedInstanceState != null) {
                currentFieldID = savedInstanceState.getString("content_id");
                currentText = savedInstanceState.getString("current_text");
            }

            // Sets up the field where the user enters stuff:
            userInputField = new EditText(getActivity());
            switch (currentFieldID) {
                case ID_DISTANCE:
                case ID_DURATION:
                    userInputField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                case ID_CALORIES:
                case ID_HEART_RATE:
                    userInputField.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                    break;
                case ID_COMMENT:
                    userInputField.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                    break;
                default:
                    Log.d("problem", "couldn't assign an input type for field :" + currentFieldID);
            }

            // Causes the field to display the current value.
            // If the dialog is being reloaded, and the old one had text entered into it, use that text:
            if (currentText != null) {
                userInputField.setText(currentText);
            }
            // Otherwise, use the Activity's already-set values:
            else {
                switch (currentFieldID) {
                    case ID_DURATION:
                        if (duration != null) {
                            userInputField.setText(String.format("%f", duration));
                        }
                        break;
                    case ID_DISTANCE:
                        if (distance != null) {
                            userInputField.setText(String.format("%f", distance));
                        }
                        break;
                    case ID_CALORIES:
                        if (calories != null) {
                            userInputField.setText(String.format("%d", calories));
                        }
                        break;
                    case ID_HEART_RATE:
                        if (heart_rate != null) {
                            userInputField.setText(String.format("%d", heart_rate));
                        }
                        break;
                    case ID_COMMENT:
                        if (comment != null) {
                            userInputField.setText(comment);
                        }
                        break;
                    default:
                        Log.d("problem", "couldn't load the text field correctly for " +
                                "the field with ID = " + currentFieldID);
                }
            }

            // sets what happens when the user clicks the "positive button" (OK):
            DialogInterface.OnClickListener posBtnListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // gets whatever the user typed into the text entry field:
                    String userInput = userInputField.getEditableText().toString();

                    // resets the field based on what the user typed - or makes a Toast to let
                    // the user know their input was invalid:
                    try {
                        switch (currentFieldID) {
                            case ID_DURATION:
                                if (userInput.equals(""))
                                    duration = null;
                                else
                                    duration = Integer.parseInt(userInput);
                                break;
                            case ID_DISTANCE:
                                if (userInput.equals(""))
                                    distance = null;
                                else
                                    distance = Double.parseDouble(userInput);
                                break;
                            case ID_CALORIES:
                                if (userInput.equals(""))
                                    calories = null;
                                else
                                    calories = Integer.parseInt(userInput);
                                break;
                            case ID_HEART_RATE:
                                if (userInput.equals(""))
                                    heart_rate = null;
                                else
                                    heart_rate = Integer.parseInt(userInput);
                                break;
                            case ID_COMMENT:
                                comment = userInput;
                                break;
                            default:
                                Log.d("problem", "couldn't find a variable to set for " + currentFieldID);
                        }
                    } catch (Exception e) {
                        EHelpers.makeToast(getActivity(), "Field not set - input was invalid (maybe too long).");
                        e.printStackTrace();
                    }

                    currentText = null;
                    currentFieldID = null;
                }
            };

            // sets what happens when the user clicks the "negative button":
            DialogInterface.OnClickListener negBtnListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentText = null;
                    currentFieldID = null;
                }
            };

            // sets the word(s) that appear at the top of the dialog box:
            String dialogTitle = "";
            switch (currentFieldID) {
                case ID_DURATION:
                    dialogTitle = "Duration";
                    break;
                case ID_DISTANCE:
                    dialogTitle = "Distance";
                    break;
                case ID_CALORIES:
                    dialogTitle = "Calories";
                    break;
                case ID_HEART_RATE:
                    dialogTitle = "Heart Rate";
                    break;
                case ID_COMMENT:
                    dialogTitle = "Comment";
                    userInputField.setHint("How did it go? Notes here.");
                    break;
                default:
                    Log.d("problem", "couldn't find dialog title");
            }

            // builds the dialog itself using the above stuff:
            AlertDialog.Builder theDialogBuilder = new AlertDialog.Builder(getActivity());
            theDialogBuilder.setTitle(dialogTitle);
            theDialogBuilder.setView(userInputField);
            theDialogBuilder.setPositiveButton(android.R.string.yes, posBtnListener);
            theDialogBuilder.setNegativeButton(android.R.string.no, negBtnListener);
            return theDialogBuilder.create();
        }
    }
}
