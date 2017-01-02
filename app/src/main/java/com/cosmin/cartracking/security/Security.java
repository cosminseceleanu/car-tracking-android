package com.cosmin.cartracking.security;


import android.content.Context;
import android.text.TextUtils;

import com.cosmin.cartracking.common.SharedPrefsStorage;
import com.cosmin.cartracking.gson.GsonFactory;
import com.cosmin.cartracking.model.User;
import com.cosmin.cartracking.security.Exception.NotLoggedException;
import com.google.gson.Gson;

public class Security {
    private static final String TOKEN_KEY = "token";
    private static final String USER_KEY = "user";

    private Context context;
    private SharedPrefsStorage storage;
    private TokenParser tokenParser;
    private Gson gson;

    public Security(Context context) {
        this.context = context;
        this.storage = new SharedPrefsStorage(context);
        this.tokenParser = new TokenParser();
        this.gson = GsonFactory.create();
    }

    public boolean isLogged() {
        if (!storage.has(TOKEN_KEY)) {
            return false;
        }

        return !tokenParser.isExpired(getToken());
    }

    public void logout() {
        storage.delete(TOKEN_KEY);
        storage.delete(USER_KEY);
    }

    public User get() throws NotLoggedException {
        String json = storage.get(USER_KEY, "");
        if (TextUtils.isEmpty(json)) {
            throw new NotLoggedException();
        }

        return gson.fromJson(json, User.class);
    }

    public String getToken() {
        return storage.get(TOKEN_KEY, "");
    }

    public void setToken(String token) {
        storage.set(TOKEN_KEY, token);
    }

    public void setUser(User user) {
        String json = gson.toJson(user);
        storage.set(USER_KEY, json);
    }
}
