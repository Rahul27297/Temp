package com.example.prodigy.travelate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prodigy on 1/4/18.
 */

public class Api2Client {
    private static final String BaseUrl = "http://35.229.21.56/EAST/EAST/";
    private static Retrofit retrofit;

    public static Retrofit getApi2Client(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return  retrofit;
    }
}
