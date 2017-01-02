package com.cosmin.cartracking.common;


import com.cosmin.cartracking.R;
import com.cosmin.cartracking.model.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StatusMapper {
    private final static Map<Task.Status, Integer> colors;
    private final static Map<Task.Status, String> messages;

    static {
        HashMap<Task.Status, Integer> map = new HashMap<>();
        map.put(Task.Status.NEW, R.color.colorNew);
        map.put(Task.Status.FINISHED, R.color.colorDone);
        map.put(Task.Status.IN_PROGRESS, R.color.colorInProgress);
        colors = Collections.unmodifiableMap(map);
    }
    static {
        HashMap<Task.Status, String> map = new HashMap<>();
        map.put(Task.Status.NEW, "Task Nou");
        map.put(Task.Status.IN_PROGRESS, "Task In lucru");
        map.put(Task.Status.FINISHED, "Task Finalizat");
        messages = Collections.unmodifiableMap(map);
    }

    public static int getColor(Task.Status status) {
        return colors.get(status);
    }

    public static String getMessage(Task.Status status) {
        return messages.get(status);
    }
}
