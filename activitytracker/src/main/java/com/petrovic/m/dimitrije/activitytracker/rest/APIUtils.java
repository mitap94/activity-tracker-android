package com.petrovic.m.dimitrije.activitytracker.rest;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class APIUtils {

    public static final String REST_API_URI = "http://192.168.0.26:8000/api/";

    // Endpoints
    public static final String TOKEN_URI = "user/token/";
    public static final String GOOGLE_TOKEN_URI = "user/google_token/";
    public static final String ME_URI = "user/me/";

    // ApiService for making REST calls
    private static APIService apiService = null;

    // static class
    private APIUtils() { }

    public static APIService getApiService(Context context) {

        // Initialize ApiService if not initialized yet
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIUtils.REST_API_URI)
                    .client(createOkHttpClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            apiService = retrofit.create(APIService.class);
        }

        return apiService;
    }

    private static OkHttpClient createOkHttpClient(Context context) {
        return (new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context))
                .build());
    }


}
