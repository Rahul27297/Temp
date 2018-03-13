package com.example.prodigy.travelate;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prodigy on 20/2/18.
 */

public class ImageClass {
    @SerializedName("image")
    private String Image;

    @SerializedName("monumentTitle")
    private String monumentTitle;

    @SerializedName("monumentInfo")
    private String monumentInfo;

    public String getMonumentTitle(){
        return monumentTitle;
    }

    public String getMonumentInfo(){
        return monumentInfo;
    }
}
