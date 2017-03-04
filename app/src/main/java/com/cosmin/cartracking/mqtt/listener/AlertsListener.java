package com.cosmin.cartracking.mqtt.listener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cosmin.cartracking.MainActivity;
import com.cosmin.cartracking.R;
import com.cosmin.cartracking.mqtt.Mqtt;
import com.cosmin.cartracking.mqtt.model.Alert;

public class AlertsListener extends MqttListener<Alert> {

    private long userid;
    private Context context;
    private NotificationManager notificationManager;

    public AlertsListener(long userid, Context context, NotificationManager notificationManager) {
        super();
        this.userid = userid;
        this.context = context;
        this.notificationManager = notificationManager;
    }

    @Override
    public String getTopic() {
        return String.format("user.%d.alerts", userid);
    }

    @Override
    public void onMessageReceived(Alert message) {
        Log.d(Mqtt.TAG, "message received from alerts listener");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_car3)
                        .setContentTitle(message.getTitle())
                        .setAutoCancel(true)
                        .setContentText(message.getMessage());

        try {
            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);
            notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        } catch (Exception e) {
            Log.d(Mqtt.TAG, "Failed to show notif");
            Log.d(Mqtt.TAG, e.getMessage());
        }
    }

    @Override
    public Class<Alert> getClassType() {
        return Alert.class;
    }
}
