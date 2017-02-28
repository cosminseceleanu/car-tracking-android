package com.cosmin.cartracking.mqtt;

import com.cosmin.cartracking.model.TaskLog;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
