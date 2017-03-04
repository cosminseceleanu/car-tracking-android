package com.cosmin.cartracking.service;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import com.cosmin.cartracking.mqtt.TaskLogPublisher;
import com.cosmin.cartracking.listener.LocationListener;
import com.cosmin.cartracking.mqtt.Mqtt;
import com.cosmin.cartracking.mqtt.listener.AlertsListener;
import com.cosmin.cartracking.security.Security;

@SuppressWarnings({"MissingPermission"})
public class MqttService extends Service {
    private static final String TAG = "mqtt-service";
    private static final int LOCATION_INTERVAL = 30000;
    private static final float LOCATION_DISTANCE = 0;

    private LocationManager locationManager = null;
    private LocationListener[] locationListeners;

    private Security security;
    private Mqtt mqttClient;
    private TaskLogPublisher taskLogPublisher;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        security = new Security(getApplicationContext());
        mqttClient = new Mqtt();
        taskLogPublisher = new TaskLogPublisher(mqttClient);
        locationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER, taskLogPublisher, security.get()),
            new LocationListener(LocationManager.NETWORK_PROVIDER, taskLogPublisher, security.get())
        };
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        AlertsListener listener = new AlertsListener(security.get().getId(),
                getApplicationContext(), notificationManager);
        mqttClient.subscribe(listener);

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
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
        mqttClient.disconnect();
    }

    private void initializeLocationManager() {
        Log.d(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void requestLocations() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                LOCATION_INTERVAL,
                LOCATION_DISTANCE,
                locationListeners[1]
            );

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_INTERVAL,
                LOCATION_DISTANCE,
                locationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
    }
}
