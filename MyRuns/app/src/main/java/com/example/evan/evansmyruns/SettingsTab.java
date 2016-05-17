package com.example.evan.evansmyruns;

/**
 * Created by Evan on 4/10/2016.
 *
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsTab extends Fragment {

    public int randomID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("events", "settings tab " + randomID + " onCreate called");

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            FragmentManager mFragmentManager = getChildFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager
                    .beginTransaction();
            PrefsFragment mPrefsFragment = new PrefsFragment();
            mFragmentTransaction.replace(R.id.fragment_placeholder, mPrefsFragment);
            mFragmentTransaction.commit();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("events", "settings tab " + randomID + " onCreateView called");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_tab, container, false);
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            Log.d("events", "settings tab nested preference fragment onCreate called");

        }
    }

    public static boolean userPrefersMetric(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pref = preferences.getString("unit_preferences_dialog", "nothing");
        if (pref.equals("Metric")){
            return true;
        }
        return false;
    }

    @Override
    public void onResume(){
        Log.d("events", "settings tab " + randomID + " onResume called");
        super.onResume();
    }

}



