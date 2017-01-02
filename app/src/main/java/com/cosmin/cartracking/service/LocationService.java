package com.cosmin.cartracking.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.cosmin.cartracking.mqtt.TaskLogPublisher;
import com.cosmin.cartracking.listener.LocationListener;
import com.cosmin.cartracking.mqtt.MqttClient;
import com.cosmin.cartracking.security.Security;

@SuppressWarnings({"MissingPermission"})
public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "LocationService";

    private static final String TAG = "location-service";
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager locationManager = null;
    private LocationListener[] locationListeners;

    private Security security;
    private MqttClient mqttClient;
    private TaskLogPublisher taskLogPublisher;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        security = new Security(getApplicationContext());
        mqttClient = new MqttClient(getApplicationContext());
        taskLogPublisher = new TaskLogPublisher(mqttClient);
        long userid = security.get().getId();
        locationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER, taskLogPublisher, userid),
            new LocationListener(LocationManager.NETWORK_PROVIDER, taskLogPublisher, userid)
        };

        initializeLocationManager();
        requestLocations();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (locationManager != null) {
            for (LocationListener listener : locationListeners) {
                try {
                    locationManager.removeUpdates(listener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void requestLocations() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
}
