package com.example.prodigy.travelate;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prodigy on 20/2/18.
 */

public class ImageClass {
    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse(){
        return Response;
    }
}
