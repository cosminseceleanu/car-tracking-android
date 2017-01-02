package com.cosmin.cartracking.mqtt;


import com.cosmin.cartracking.gson.GsonFactory;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MqttMessageFactory {

    public MqttMessage create(Object object) {
        Gson gson = GsonFactory.create();
        String json = gson.toJson(object);

        return doCreate(json);
    }

    private MqttMessage doCreate(String json) {
        try {
            byte[] encodedPayload = json.getBytes("UTF-8");
            return new MqttMessage(encodedPayload);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
