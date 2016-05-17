package com.example.evan.evansmyruns.starttab;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.evan.evansmyruns.entry.InputType;

/**
 * Created by Evan on 5/9/2016.
 *
 */
public class InputSpinner {

    private int mId;
    private View mView;

    public InputSpinner(Context context, View view, @IdRes int layoutID){

        mId = layoutID;
        mView = view;

        Spinner inputTypeSpinner = (Spinner) view.findViewById(layoutID);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                InputType.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputTypeSpinner.setAdapter(adapter);
    }
    
    public InputType getSelected(){
        Spinner spinner = (Spinner) mView.findViewById(mId);
        String selected = (String) spinner.getSelectedItem();
        return InputType.get(selected);
    }
}
