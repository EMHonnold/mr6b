package com.example.evan.evansmyruns;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.example.evan.evansmyruns.evanhelpers.ConversionHelpers;

import java.util.ArrayList;

/**
 * Created by Evan on 4/23/2016.
 *
 * A custom adapter for displaying the exercise entries.
 *
 */

public class ExerciseEntryAdapter extends BaseAdapter {

    private ArrayList<ExerciseEntry> exerciseEntries;
    private LayoutInflater inflater;
    private HistoryTab.ShowEntry clickAction;
    public boolean useMetric;

    public ExerciseEntryAdapter(Context context, ArrayList<ExerciseEntry> exercise_entry_list, boolean use_metric, HistoryTab.ShowEntry click_Action){
        exerciseEntries = exercise_entry_list;
        inflater = LayoutInflater.from(context);
        useMetric = use_metric;
        clickAction = click_Action;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null){
            view = inflater.inflate(R.layout.history_tab_indiv_item, null);
        }

        // makes a string out of the ExerciseEntry:
        String topString, bottomString;
        final ExerciseEntry currentEntry = exerciseEntries.get(position);
        topString = currentEntry.getActivityType() + ": ";

        if (useMetric) {
            topString += ConversionHelpers.roundDouble(currentEntry.getDistanceKilometers()) + " km";
        }
        else {
            topString += ConversionHelpers.roundDouble(currentEntry.getDistanceMiles()) + " mi";
        }

        String inputType = currentEntry.getInputType().toString();
        bottomString = inputType + ": "+"Entered at " + currentEntry.getTime() + " on " + currentEntry.getDate();

        int durationSeconds = currentEntry.getDuration();

        //Calculate the seconds to display:
        int sec = durationSeconds % 60;
        durationSeconds -= sec;
        //Calculate the minutes:
        long minutesCount = durationSeconds / 60;
        long minutes = minutesCount % 60;
        minutesCount -= minutes;
        //Calculate the hours:
        long hoursCount = minutesCount / 60;
        //Build the String
        String secStr, minStr, hourStr;
        if (sec > 9){
            secStr = Integer.toString(sec);
        }
        else {
            secStr = "0" + Integer.toString(sec);
        }
        if (minutes > 9){
            minStr = Long.toString(minutes);
        }
        else {
            minStr = "0" + Long.toString(minutes);
        }
        hourStr = Long.toString(hoursCount);
        String strb = "";
        if (hoursCount > 0){
            strb += hourStr + ":";
        }
        strb += minStr + ":" + secStr;


        TextView tvc = (TextView) view.findViewById(R.id.item_top_stringC);
        tvc.setText(strb);

        TextView topView = (TextView) view.findViewById(R.id.item_top_string);
        TextView bottomView = (TextView) view.findViewById(R.id.item_bottom_string);

        topView.setText(topString);
        bottomView.setText(bottomString);

        LinearLayout btn = (LinearLayout) view.findViewById(R.id.history_tab_btn_item);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAction.show(currentEntry);
            }
        });

        return view;
    }

    @Override
    public int getCount(){
        return exerciseEntries.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        return exerciseEntries.get(position);
    }
}
