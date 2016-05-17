package com.example.evan.evansmyruns;

/**
 * Created by Evan on 4/9/2016.
 *
 * Adapted from example code from the CS65 demo on fragments (credit to Fanglin Chen).
 *
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class TabsManager extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public static final int START_TAB_ID = 0;
    public static final int HISTORY_TAB_ID = 1;
    public static final int SETTINGS_TAB_ID = 2;
    public static final String START_TAB_LABEL = "START";
    public static final String SETTINGS_TAB_LABEL = "SETTINGS";
    public static final String HISTORY_TAB_LABEL = "HISTORY";


    public TabsManager(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int pos){
        Fragment theFragment = fragments.get(pos);
        Log.d("LOG", "GOT THE ITEM at " + pos);
        return theFragment;
    }

    public int getCount(){
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case START_TAB_ID:
                return START_TAB_LABEL;
            case HISTORY_TAB_ID:
                return HISTORY_TAB_LABEL;
            case SETTINGS_TAB_ID:
                return SETTINGS_TAB_LABEL;
            default:
                Log.d("ERROR", "GOT IN HERE WTF");
                break;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
