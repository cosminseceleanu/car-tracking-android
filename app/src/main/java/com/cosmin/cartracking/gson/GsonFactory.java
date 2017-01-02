package com.cosmin.cartracking.gson;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.Date;

public class GsonFactory {
    @NonNull
    public static Gson create() {
        return new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }
}
