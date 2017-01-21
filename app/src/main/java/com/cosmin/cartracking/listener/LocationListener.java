package com.cosmin.cartracking.listener;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.cosmin.cartracking.model.TaskLog;
import com.cosmin.cartracking.mqtt.TaskLogPublisher;

public class LocationListener implements android.location.LocationListener {
    private Location lastLocation;
    private static final String TAG = "location-listener";
    private TaskLogPublisher taskLogPublisher;
    private long userid;

    public LocationListener(String provider, TaskLogPublisher taskLogPublisher, long userid) {
        Log.e(TAG, "LocationListener " + provider);
        lastLocation = new Location(provider);
        this.taskLogPublisher = taskLogPublisher;
        this.userid = userid;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);
        lastLocation.set(location);
        try {
            taskLogPublisher.publish(createTaskLog(location), userid);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }

    private TaskLog createTaskLog(Location location) {
        TaskLog taskLog = new TaskLog(location.getTime(), location.getLatitude(), location.getLongitude());
        taskLog.setAltitude(location.getAltitude());
        taskLog.setSpeed(location.getSpeed());

        return taskLog;
    }
}
