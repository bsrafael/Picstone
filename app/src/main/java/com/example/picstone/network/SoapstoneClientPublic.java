package com.example.picstone.network;

import com.example.picstone.models.input.TokenInputModel;
import com.example.picstone.models.input.UserInputModel;
import com.example.picstone.models.output.TokenViewModel;
import com.example.picstone.models.output.UserViewModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SoapstoneClientPublic {
    // Tokens
    @POST("api/tokens")
    Call<TokenViewModel> getToken(@Body TokenInputModel inputModel);

    // Users
    @POST("api/users")
    Call<UserViewModel> createUser(@Body UserInputModel inputModel); // NOTE public
}
