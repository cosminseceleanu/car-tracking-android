package com.cosmin.cartracking.http.endpoints;

import com.cosmin.cartracking.model.PagedResponse;
import com.cosmin.cartracking.model.Task;
import com.cosmin.cartracking.model.TaskListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskEndpoint {

    @GET("/api/employees/{id}/tasks")
    Call<PagedResponse<TaskListResponse>> get(@Path("id") long id, @Query("page") int page, @Query("size") int size);

    @PUT("/api/employees/{userId}/tasks/{id}")
    Call<ResponseBody> update(@Path("id") long id, @Path("userId") long userId, @Body Task task);
}
