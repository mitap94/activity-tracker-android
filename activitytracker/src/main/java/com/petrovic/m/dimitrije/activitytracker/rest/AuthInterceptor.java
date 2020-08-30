package com.petrovic.m.dimitrije.activitytracker.rest;

import android.content.Context;
import android.util.Log;

import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private static final String LOG_TAG = Utils.getLogTag(AuthInterceptor.class);

    private SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        this.sessionManager = SessionManager.getInstance(context);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Log.d(LOG_TAG, "intercept");

        String token = sessionManager.fetchAuthToken();

        Log.d(LOG_TAG, "token = " + token);

        if (token != null) {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.addHeader("Authorization", "Token " + token);
            return chain.proceed(requestBuilder.build());
        }

        return chain.proceed(chain.request());
    }
}
