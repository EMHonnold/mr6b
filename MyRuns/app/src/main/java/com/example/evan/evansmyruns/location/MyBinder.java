package com.example.evan.evansmyruns.location;
import android.os.Binder;

/**
 * Created by Evan on 4/30/2016.
 *
 * Basically just holds a reference to the Service
 *
 */
public class MyBinder extends Binder {
    public LocationService service;
    public MyBinder(LocationService _service){
        service = _service;
    }
}
