package com.cosmin.cartracking.common;

import com.cosmin.cartracking.http.RetrofitFactory;
import com.cosmin.cartracking.http.endpoints.TaskEndpoint;
import com.cosmin.cartracking.model.Task;
import com.cosmin.cartracking.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskUpdater {
    private RetrofitFactory retrofitFactory;
    private User user;

    public interface TaskCallback {
        public void onSuccess();
        public void onFailure();

    }

    public TaskUpdater(RetrofitFactory retrofitFactory, User user) {
        this.retrofitFactory = retrofitFactory;
        this.user = user;
    }

    public void update(Task task, final TaskCallback callback) {
        Call<ResponseBody> call = retrofitFactory.create()
                .create(TaskEndpoint.class)
                .update(task.getId(), user.getId(), task);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                    return;
                }

                callback.onFailure();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });

    }
}
