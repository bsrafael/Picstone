package com.example.picstone.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientFactory {
    // TODO alterar para a url real depois de hosteado
    public static final String BASE_URL = "http://localhost:5000";

    public static final SoapstoneClient GetSoapstoneClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SoapstoneClient.class);
    }
}
