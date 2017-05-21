package com.cosmin.cartracking.http;


import com.cosmin.cartracking.gson.GsonFactory;
import com.cosmin.cartracking.http.interceptors.AuthorizationInterceptor;
import com.cosmin.cartracking.security.Security;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private final static String dev_host = "http://192.168.100.4:8080";
    private final static String host = "http://35.189.207.159:8080";
    private final static List<String> UNSECURED_PATHS = Arrays.asList("/login");
    private Security security;

    public RetrofitFactory(Security security) {
        this.security = security;
    }

    public Retrofit create() {
        return new Retrofit.Builder()
            .baseUrl(host)
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.create()))
            .client(getHttpClient())
            .build();
    }

    private OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
            .addInterceptor(new AuthorizationInterceptor(UNSECURED_PATHS, security))
            .build();
    }
}
