package com.cosmin.cartracking.mqtt;

import com.cosmin.cartracking.model.TaskLog;
import com.cosmin.cartracking.mqtt.exception.PublishException;

public class TaskLogPublisher {
    private final static String TOPIC = "user.%d.employee.%d.task.logs";

    private Mqtt mqttClient;

    public TaskLogPublisher(Mqtt mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void publish(TaskLog taskLog, long userid, long adminId) {
        String topic = String.format(TOPIC, adminId, userid);
        try {
            mqttClient.publish(topic, taskLog);
        } catch (PublishException e) {
            e.printStackTrace();
        }
    }
}
