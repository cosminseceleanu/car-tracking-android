package com.cosmin.cartracking.mqtt;

import com.cosmin.cartracking.model.TaskLog;
import com.cosmin.cartracking.mqtt.exception.PublishException;

public class TaskLogPublisher {
    private final static String TOPIC = "/topic/task.logs.%d";

    private MqttClient mqttClient;

    public TaskLogPublisher(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void publish(TaskLog taskLog, long userid) {
        String topic = String.format(TOPIC, userid);
        try {
//            mqttClient.publish(topic, taskLog);
        } catch (PublishException e) {
            e.printStackTrace();
        }
    }
}
