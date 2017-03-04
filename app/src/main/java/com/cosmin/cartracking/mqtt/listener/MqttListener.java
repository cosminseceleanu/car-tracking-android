package com.cosmin.cartracking.mqtt.listener;


import android.util.Log;

import com.cosmin.cartracking.gson.GsonFactory;
import com.cosmin.cartracking.mqtt.Mqtt;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class MqttListener<T> implements IMqttMessageListener {

    private Gson gson;

    public MqttListener() {
        gson = GsonFactory.create();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (!topic.equals(getTopic())) {
            Log.d(Mqtt.TAG, String.format("skip message from %s! current listner is subscribed to %s",
                    topic, getTopic()));
            return;
        }
        String json = new String(message.getPayload());
        onMessageReceived(gson.fromJson(json, getClassType()));
    }

    public abstract String getTopic();

    public abstract void onMessageReceived(T message);

    public abstract Class<T> getClassType();
}
