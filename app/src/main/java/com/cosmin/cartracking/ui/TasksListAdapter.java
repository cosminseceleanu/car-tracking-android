package com.cosmin.cartracking.ui;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cosmin.cartracking.R;
import com.cosmin.cartracking.TaskDestinationActivity;
import com.cosmin.cartracking.common.StatusMapper;
import com.cosmin.cartracking.model.Task;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class TasksListAdapter extends ArrayAdapter<Task> {
    public TasksListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TasksListAdapter(Context context, int resource, Task[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }

        TextView taskId = (TextView) convertView.findViewById(R.id.taskId);
        TextView taskStatus = (TextView) convertView.findViewById(R.id.taskStatus);
        TextView taskLimitDate = (TextView) convertView.findViewById(R.id.taskLimitDate);
        Button seeDetails = (Button) convertView.findViewById(R.id.taskSeeDetails);
        taskId.setText(String.valueOf(task.getId()));
        taskStatus.setText(StatusMapper.getMessage(task.getStatus()));
        taskStatus.setBackgroundResource(StatusMapper.getColor(task.getStatus()));
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        taskLimitDate.setText(dateFormat.format(task.getLimitDate()));
        seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskDestinationActivity.class);
                String jsonTask = new Gson().toJson(getItem(position));
                intent.putExtra("task", jsonTask);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
