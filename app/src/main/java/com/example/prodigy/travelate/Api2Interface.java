package com.example.prodigy.travelate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by prodigy on 14/3/18.
 */

public interface Api2Interface {

    @FormUrlEncoded
    @POST("upload_2.php")
    Call<TranslateClass> uploadImage(@Field("Image") String Image);
}
