package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cl-macmini-83 on 17/12/16.
 */

public class ImageUrl {
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @SerializedName("ref_image")
    @Expose
    private String imageUrl;
}
