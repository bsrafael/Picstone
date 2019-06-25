package com.example.picstone.models.output;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenViewModel {
    @SerializedName("token")
    @Expose
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
