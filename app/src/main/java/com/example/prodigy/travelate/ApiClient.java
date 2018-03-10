package com.example.prodigy.travelate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prodigy on 20/2/18.
 */

public class ApiClient {
    private static final String BaseUrl = "http://35.229.21.56/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return  retrofit;
    }
}
