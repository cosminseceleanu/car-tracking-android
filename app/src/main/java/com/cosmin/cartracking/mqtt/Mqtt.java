package com.cosmin.cartracking.mqtt;


import android.content.Context;
import android.util.Log;

import com.cosmin.cartracking.mqtt.exception.PublishException;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt {
    private final static String uri = "tcp://192.168.100.4:1883";
    private final static String username = "admin";
    private final static String pass = "admin";
    private final static String TAG = "mqtt-client";

    private MqttClient client;
    private MqttMessageFactory messageFactory;
    private boolean isConnected = false;

    public Mqtt() {
        this.messageFactory = new MqttMessageFactory();
    }

    public void connect() {
        if (isConnected) {
            return;
        }
        try {
            String clientId = MqttClient.generateClientId();
            this.client = new MqttClient(uri, clientId, new MemoryPersistence());
            client.connect(getOptions());
            isConnected = true;
            Log.d(TAG, "successful connected to mqtt server");
        } catch (MqttException e) {
            Log.d(TAG, "failed to connect mqtt server");

            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, Object payload) {
        if (!isConnected) {
            this.connect();
        }
        try {
            client.publish(topic, messageFactory.create(payload));
            Log.d(TAG, "successful publish to topic " + topic);
        } catch (MqttException e) {
            Log.d(TAG, "Failed to publish to topic. Message: " + e.getMessage());
            throw new PublishException(e);
        }
    }

    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setUserName(username);
        options.setPassword(pass.toCharArray());

        return options;
    }
}
