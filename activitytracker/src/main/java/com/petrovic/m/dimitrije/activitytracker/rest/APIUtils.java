package com.petrovic.m.dimitrije.activitytracker.rest;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.petrovic.m.dimitrije.activitytracker.R;

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

    // Client for making Google calls
    private static GoogleSignInClient googleSignInClient = null;

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

    public static GoogleSignInClient getGoogleClient(Context context) {
        if (googleSignInClient == null) {
            // Configure Google sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestServerAuthCode(context.getString(R.string.server_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            googleSignInClient = GoogleSignIn.getClient(context, gso);
        }

        return googleSignInClient;
    }
}
