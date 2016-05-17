package com.example.evan.evansmyruns.map;

import android.util.Log;

import com.example.evan.evansmyruns.entry.ExerciseEntry;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Evan on 5/8/2016.
 */
public class MapWriter {

    public static final String TAG = "MapWriter";
    public static final int DEFAULT_ZOOM = 17;

    public static void draw(ExerciseEntry entry, GoogleMap mMap,
                            boolean runIsActive,
                            Marker runStartMarker, Marker runEndMarker,
                            Polyline runPath){

        if (mMap == null){
            Log.e(TAG, "Google Map was null");
            return;
        }
        if (entry == null){
            Log.e(TAG, "ExerciseEntry was null");
            return;
        }

        mMap.clear();

        ArrayList<LatLng> path = entry.getLocations();

        LatLng startLocation = path.get(0);
        LatLng endLocation = path.get(path.size() - 1);

        /* sets up the MARKERS */
        if (runStartMarker == null)
            runStartMarker = addMarker(mMap, startLocation, Color.RED);
        else
            runStartMarker.setPosition(startLocation);

        if (runEndMarker == null) {
            if (path.size() > 1)
                runEndMarker = addMarker(mMap, endLocation, Color.GREEN);
        }
        else {
            runEndMarker.setPosition(endLocation);
        }

        /* sets up the POLYLINE */
        if (runPath == null) {
            if (path.size() > 1) {
                PolylineOptions polylineOptions = new PolylineOptions();
                for (LatLng l : path) {
                    polylineOptions.add(l);
                }
                runPath = mMap.addPolyline(polylineOptions);
            }
        }
        else {
            runPath.setPoints(path);
        }

        /* If we're showing an active run, zoom in on the current location: */
        if (runIsActive) {

            boolean zoomNecessary = false;

            // Is it necessary to zoom?
            if (path.size() == 1)
                zoomNecessary = true;
            else if (path.size() % 10 == 0)
                zoomNecessary = true;

            // zoom in if we're too far out (and only check every 4th update):
            float zoom = mMap.getCameraPosition().zoom;
            if (Math.abs(zoom - DEFAULT_ZOOM) > 2 && path.size() % 4 == 0)
                zoomNecessary = true;

            if (zoomNecessary)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endLocation, DEFAULT_ZOOM));

        }
        /* If we're showing a completed run, adjust the camera to show the entire run path: */
        else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng loc : path) {
                builder.include(loc);
            }
            LatLngBounds bounds = builder.build();
            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
        }
    }

    private enum Color{
        RED,
        GREEN
    }

    /* Adds a marker to the map at the specified position, with the given color -
        colors accessed like this: BitmapDescriptorFractory.COLOR_RED   */
    private static Marker addMarker(GoogleMap mMap, LatLng position, Color color){

        float hue;
        switch(color){
            case RED:
                hue = BitmapDescriptorFactory.HUE_RED;
                break;
            case GREEN:
                hue = BitmapDescriptorFactory.HUE_GREEN;
                break;
            default:
                hue = BitmapDescriptorFactory.HUE_MAGENTA;
        }

        return mMap.addMarker(
                new MarkerOptions()
                        .position(position)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(hue)));
    }

}
