package com.cosmin.cartracking.model;


import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("rid")
    private long id;
    private String email;
    private String name;
    private long adminId;

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }
}
