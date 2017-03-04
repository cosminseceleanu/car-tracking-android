package com.cosmin.cartracking.http.endpoints;

import com.cosmin.cartracking.model.PagedResponse;
import com.cosmin.cartracking.model.Task;
import com.cosmin.cartracking.model.TaskListResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface TaskEndpoint {

    @GET("/api/tasks/search")
    Call<PagedResponse<TaskListResponse>> search(@QueryMap Map<String, String> filters);

    @PUT("/api/employees/{userId}/tasks/{id}")
    Call<ResponseBody> update(@Path("id") long id, @Path("userId") long userId, @Body Task task);
}
