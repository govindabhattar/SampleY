package com.tookancustomer.models.businessCategoriesData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ThumbList
{

    @SerializedName("100x100")
    @Expose
    private String _100x100;
    @SerializedName("200x200")
    @Expose
    private String _200x200;

    public String get250x250() {
        return _100x100;
    }
    public String get400x400() {
        return _200x200;
    }
}