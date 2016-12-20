package com.cosmin.cartracking.http.interceptors;

import com.cosmin.cartracking.security.Security;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {

    private List<String> excludedPaths;
    private Security security;

    public AuthorizationInterceptor(List<String> excludedPaths, Security security) {
        this.excludedPaths = excludedPaths;
        this.security = security;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!shouldApplyToken(request)) {
            return chain.proceed(request);
        }

        return chain.proceed(applyToken(request));
    }

    private boolean shouldApplyToken(Request request) {
        String path = request.url().url().getPath();

        return !excludedPaths.contains(path);
    }

    private Request applyToken(Request request) {
        return request.newBuilder()
            .addHeader("Authorization", "Bearer " + security.getToken())
            .build();
    }
}
