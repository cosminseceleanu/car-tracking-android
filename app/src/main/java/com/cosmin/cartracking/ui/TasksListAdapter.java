package com.cosmin.cartracking.ui;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmin.cartracking.R;
import com.cosmin.cartracking.TaskDestinationActivity;
import com.cosmin.cartracking.common.StatusMapper;
import com.cosmin.cartracking.common.TaskUpdater;
import com.cosmin.cartracking.model.Task;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class TasksListAdapter extends ArrayAdapter<Task> {
    private TaskUpdater taskUpdater;

    public TasksListAdapter(Context context, int resource, TaskUpdater taskUpdater) {
        super(context, resource);
        this.taskUpdater = taskUpdater;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }
        setupText(convertView, task);
        setupButtons(position, convertView);

        return convertView;
    }

    private void setupButtons(final int position, View convertView) {
        Button seeDetails = (Button) convertView.findViewById(R.id.taskSeeDetails);
        Button beginTask = (Button) convertView.findViewById(R.id.taskStart);
        Button finishTask = (Button) convertView.findViewById(R.id.taskFinish);
        Task.Status status = getItem(position).getStatus();
        if (status == Task.Status.NEW) {
            finishTask.setVisibility(View.GONE);
            beginTask.setVisibility(View.VISIBLE);
        } else if (status == Task.Status.IN_PROGRESS) {
            beginTask.setVisibility(View.GONE);
            finishTask.setVisibility(View.VISIBLE);
        } else if (status == Task.Status.FINISHED) {
            beginTask.setVisibility(View.GONE);
            finishTask.setVisibility(View.GONE);
        }
        bindListeners(position, seeDetails, beginTask, finishTask);
    }

    private void bindListeners(final int position, Button seeDetails, Button beginTask, Button finishTask) {
        seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskDestinationActivity.class);
                String jsonTask = new Gson().toJson(getItem(position));
                intent.putExtra("task", jsonTask);
                getContext().startActivity(intent);
            }
        });
        beginTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = getItem(position);
                task.setStatus(Task.Status.IN_PROGRESS.toString());
                updateTask(task, position);
            }
        });
        finishTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = getItem(position);
                task.setStatus(Task.Status.FINISHED.toString());
                updateTask(task, position);
            }
        });
    }

    private void setupText(View convertView, Task task) {
        TextView taskId = (TextView) convertView.findViewById(R.id.taskId);
        TextView taskStatus = (TextView) convertView.findViewById(R.id.taskStatus);
        TextView taskLimitDate = (TextView) convertView.findViewById(R.id.taskLimitDate);
        TextView taskAddress = (TextView) convertView.findViewById(R.id.taskAdress);

        taskId.setText(String.valueOf(task.getId()));
        taskStatus.setText(StatusMapper.getMessage(task.getStatus()));
        int color = ContextCompat.getColor(getContext(), StatusMapper.getColor(task.getStatus()));
        taskStatus.setTextColor(color);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        taskLimitDate.setText(dateFormat.format(task.getLimitDate()));
        if (!TextUtils.isEmpty(task.getAddress())) {
            taskAddress.setText(task.getAddress());
        }
    }

    private void updateTask(final Task task, final int position) {
        taskUpdater.update(task, new TaskUpdater.TaskCallback() {
            @Override
            public void onSuccess() {
                String msg =  "Statusul a fost modificat";
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                remove(task);
                insert(task, position);
            }

            @Override
            public void onFailure() {
                String msg =  "Statusul nu a putut fi modificat";
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
