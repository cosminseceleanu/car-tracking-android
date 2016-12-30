package com.cosmin.cartracking.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cosmin.cartracking.R;
import com.cosmin.cartracking.model.Task;


public class TasksListAdapter extends ArrayAdapter<Task> {
    public TasksListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TasksListAdapter(Context context, int resource, Task[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        TextView taskId = (TextView) convertView.findViewById(R.id.taskId);
        TextView taskStatus = (TextView) convertView.findViewById(R.id.taskStatus);
        taskId.setText(String.valueOf(task.getId()));
        taskStatus.setText(task.getStatus());

        return convertView;
    }
}
