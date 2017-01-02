package com.cosmin.cartracking.mqtt;


import android.content.Context;
import android.util.Log;

import com.cosmin.cartracking.mqtt.exception.PublishException;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttClient {
    private final static String uri = "tcp://192.168.100.4:1884";
    private final static String username = "admin";
    private final static String pass = "admin";
    private final static String TAG = "mqtt-client";

    private MqttAndroidClient client;
    private MqttMessageFactory messageFactory;
    private boolean isConnected = false;

    public MqttClient(Context context) {
        String clientId = org.eclipse.paho.client.mqttv3.MqttClient.generateClientId();
        this.client = new MqttAndroidClient(context, uri, clientId);
        this.messageFactory = new MqttMessageFactory();
    }

    public void connect() {
        try {
            IMqttToken token = client.connect(getOptions());
            token.waitForCompletion(25000);
            isConnected = true;
        } catch (MqttException e) {
            isConnected = false;
            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, Object payload) {
        Log.d(TAG, "publish to topic");
        if (!isConnected) {
            this.connect();
        }
        try {
            client.publish(topic, messageFactory.create(payload));
        } catch (MqttException e) {
            throw new PublishException(e);
        }
    }

    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(pass.toCharArray());

        return options;
    }
}
