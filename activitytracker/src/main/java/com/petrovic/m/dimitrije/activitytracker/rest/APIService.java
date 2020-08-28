package com.petrovic.m.dimitrije.activitytracker.rest;

import com.petrovic.m.dimitrije.activitytracker.data.pojo.Token;
import com.petrovic.m.dimitrije.activitytracker.data.pojo.User;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @POST(APIUtils.TOKEN_URI)
    @FormUrlEncoded
    Single<Token> token(@Field("email") String email,
                                  @Field("password") String password);

    @POST(APIUtils.GOOGLE_TOKEN_URI)
    @FormUrlEncoded
    Single<Token> googleToken(@Field("code") String authCode);

    @GET(APIUtils.ME_URI)
    Single<User> getUserInfo();
}

