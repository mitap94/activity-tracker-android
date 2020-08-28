package com.petrovic.m.dimitrije.activitytracker.rest;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        this.sessionManager = SessionManager.getInstance(context);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        String token = sessionManager.fetchAuthToken();

        if (token != null) {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader("Authorization", "Token " + token);
            return chain.proceed(requestBuilder.build());
        }

        return chain.proceed(chain.request());
    }
}
