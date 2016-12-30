package com.cosmin.cartracking.model;


import com.google.gson.annotations.SerializedName;

public class PagedResponse<T> {
    @SerializedName("_embedded")
    private T data;
    private Page page;

    public T getData() {
        return data;
    }

    public Page getPage() {
        return page;
    }
}
