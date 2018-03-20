package com.example.prodigy.travelate;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prodigy on 15/3/18.
 */

public class TranslateClass {

    @SerializedName("image")
    private String Image;

    @SerializedName("result")
    private String result;

    protected String getResult(){
        return result;
    }
}
