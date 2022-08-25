package com.tookancustomer.models.MarketplaceStorefrontModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ashutosh Ojha on 2019-05-10.
 */
public class BannerImage implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("mobile_image")
    @Expose
    private String mobileImage;
    @SerializedName("text")
    @Expose
    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobileImage() {
        return mobileImage!=null?mobileImage:image;
    }

    public void setMobileImage(String mobileImage) {
        this.mobileImage = mobileImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
