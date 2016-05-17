package com.example.evan.evansmyruns;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import com.example.evan.evansmyruns.starttab.StartTab;
import com.example.evan.evansmyruns.tabs_stuff.SlidingTabLayout;

import java.util.ArrayList;

/*
 MyRuns Main Activity
 Evan Honnold

 The main activity is a screen that contains a "tab view" - a row of three tabs on top, like the tabs
 in Google Chrome, and a space below to display the content of the currently selected tab.
 The three tabs are called "start", "history", and "settings" - each defined as a Fragment
 in its own Java file.

 */

public class MainActivity extends Activity {

    public static String SERVER_ADDR = "https://helical-element-131321.appspot.com";

    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets up the tab layout:
        ArrayList<Fragment> tabContents = new ArrayList<>();
        StartTab startTab = new StartTab();
        HistoryTab historyTab = new HistoryTab();
        SettingsTab settingsTab = new SettingsTab();

        tabContents.add(startTab);
        tabContents.add(historyTab);
        tabContents.add(settingsTab);

        setupTabs(R.id.tab_displayer, R.id.tab_content_viewer, tabContents);

        // prompt the user for permissions
        requestPermissions();

        new GCMRegistrationAsyncTask(this).execute();
    }

    // links the “action bar” with the XML file that
    // describes the menu options the “action bar” should display
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Helper function to set up the tab layout
    //   (should be called during onCreate)
    private void setupTabs(@IdRes int upper_tabview_manager_xml_id,
                           @IdRes int lower_tabview_manager_xml_id,
                           final ArrayList<Fragment> tab_contents) {

        Log.d("events", "setup tabs was called");

        // creates the managers for the top part (the tabs)
        // and the bottom part (the place that shows the
        // current tab's contents):
        final SlidingTabLayout upperTabviewManager = (SlidingTabLayout) findViewById(upper_tabview_manager_xml_id);
        final ViewPager lowerTabviewManager = (ViewPager) findViewById(lower_tabview_manager_xml_id);

        // sets up the manager to coordinate all
        // the different parts above:
        final TabsManager tabsManager = new TabsManager(getFragmentManager(), tab_contents);
        lowerTabviewManager.setAdapter(tabsManager);
        upperTabviewManager.setDistributeEvenly(true);
        upperTabviewManager.setViewPager(lowerTabviewManager);

        // Calls methods when sliding between tabs:
        upperTabviewManager.setOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        if (position == 1) {
                            Log.d("events", "the listener registered that the history tab was clicked, so refreshing the history tab (the data entries)");
                            tabsManager.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int position) {
                    }

                    @Override
                    public void onPageScrolled(int a, float b, int c) {
                    }
                }
        );
    }

    //
    private void requestPermissions() {
        String[] requiredPermissionList = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, requiredPermissionList, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
    }

    // Handles the results of requesting permissions:
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }

            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }

    }
}