package com.petrovic.m.dimitrije.activitytracker.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("key")
    @Expose
    private String key;

    /**
     * No args constructor for use in serialization
     *
     */
    public Token() {
    }

    /**
     * @param key
     * @param token
     */
    public Token(String token, String key) {
        super();
        this.token = token;
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        if (token != null && !token.isEmpty()) {
            return token;
        }
        return key;
    }
}