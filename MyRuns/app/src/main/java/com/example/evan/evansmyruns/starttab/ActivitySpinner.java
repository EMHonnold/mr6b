package com.example.evan.evansmyruns.starttab;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.evan.evansmyruns.R;
import com.example.evan.evansmyruns.entry.ActivityType;
import com.example.evan.evansmyruns.entry.InputType;

/**
 * Created by Evan on 5/9/2016.
 *
 */
public class ActivitySpinner{

    private int mId;
    private View mView;

    public ActivitySpinner(Context context, View view, @IdRes int layoutID){
        mId = layoutID;
        mView = view;
        Spinner spinner = (Spinner) view.findViewById(mId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                ActivityType.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public ActivityType getSelected(){
        Spinner spinner = (Spinner) mView.findViewById(mId);
        String selected = (String) spinner.getSelectedItem();
        return ActivityType.get(selected);
    }

}
