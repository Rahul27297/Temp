package com.example.prodigy.travelate;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prodigy on 15/3/18.
 */

public class TranslateClass {

    @SerializedName("image")
    private String Image;

    @SerializedName("English")
    private String English;


    @SerializedName("Hindi")
    private String Hindi;


    @SerializedName("Marathi")
    private String Marathi;

    protected String getHindi(){
        return Hindi;
    }

    protected String getEnglish(){
        return English;
    }

    protected String getMarathi(){
        return Marathi;
    }
}
