package com.cosmin.cartracking.mqtt;


import android.util.Log;

import com.cosmin.cartracking.mqtt.model.RejectedMessage;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Mqtt {
    private final static String uri = "tcp://192.168.100.4:1883";
    private final static String username = "admin";
    private final static String pass = "admin";
    private final static String TAG = "mqtt-client";

    private MqttClient client;
    private MqttMessageFactory messageFactory;
    private boolean isConnected = false;
    private Queue<RejectedMessage> rejectedMessageQueue;

    public Mqtt() {
        this.messageFactory = new MqttMessageFactory();
        rejectedMessageQueue = new ConcurrentLinkedQueue<>();
    }

    public void connect() {
        if (isConnected) {
            return;
        }
        try {
            String clientId = MqttClient.generateClientId();
            client = new MqttClient(uri, clientId, new MemoryPersistence());
            setMqttCallback();
            client.connect(getOptions());
            isConnected = true;
            Log.d(TAG, "successful connected to mqtt server");
        } catch (MqttException e) {
            Log.d(TAG, "failed to connect mqtt server");

            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, Object payload) {
        if (checkConnection(topic, payload)) {
            return;
        }
        try {
            client.publish(topic, messageFactory.create(payload));
            Log.d(TAG, "successful publish to topic " + topic);
        } catch (MqttException e) {
            Log.d(TAG, "Failed to publish to topic. Message: " + e.getMessage());
            addToRejectedQueue(topic, payload);
        }
    }

    private boolean checkConnection(String topic, Object payload) {
        if (!isConnected) {
            try {
                this.connect();
                flushRejectedQueue();
            } catch (RuntimeException e) {
                addToRejectedQueue(topic, payload);
                return true;
            }
        }
        return false;
    }

    public void disconnect() {
        if (!isConnected) {
            return;
        }
        try {
            client.disconnect();
            isConnected = false;
        } catch (MqttException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void addToRejectedQueue(String topic, Object payload) {
        rejectedMessageQueue.add(new RejectedMessage(topic, payload));
    }

    private void setMqttCallback() {
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                flushRejectedQueue();
            }

            @Override
            public void connectionLost(Throwable cause) {
                isConnected = false;
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {}

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
    }

    private void flushRejectedQueue() {
        if (rejectedMessageQueue.isEmpty()) {
            return;
        }
        Log.d(TAG, "flush rejected queue -- contains " +  rejectedMessageQueue.size() + " messages");
        while (!rejectedMessageQueue.isEmpty()) {
            RejectedMessage rejectedMessage = rejectedMessageQueue.poll();
            publish(rejectedMessage.getTopic(), rejectedMessage.getPayload());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setUserName(username);
        options.setPassword(pass.toCharArray());
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        options.setMaxInflight(200);

        return options;
    }
}
