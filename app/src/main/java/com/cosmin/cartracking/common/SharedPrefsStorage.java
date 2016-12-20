package com.cosmin.cartracking.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


public class SharedPrefsStorage {
    private static final String TOKEN_KEY = "car-tracking";
    private Context context;

    public SharedPrefsStorage(Context context) {
        this.context = context;
    }

    public boolean has(String key) {
        return !TextUtils.isEmpty(get(key, ""));
    }

    public String get(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public void set(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }

    public void delete(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }
}
