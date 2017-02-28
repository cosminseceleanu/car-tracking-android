package com.cosmin.cartracking.mqtt.model;


public class RejectedMessage {
    private String topic;
    private Object payload;

    public RejectedMessage(String topic, Object payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public Object getPayload() {
        return payload;
    }
}
