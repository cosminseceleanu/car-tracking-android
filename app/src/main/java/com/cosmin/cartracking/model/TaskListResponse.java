package com.cosmin.cartracking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskListResponse {
    @SerializedName("taskResourceList")
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
