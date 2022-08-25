package com.tookancustomer.models.ProductCatalogueData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MultiVideoUrl implements Serializable
{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("thumb_urls")
    @Expose
    private ThumbUrls thumbUrls;
    private final static long serialVersionUID = -7142801431750135616L;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ThumbUrls getThumbUrls() {
        return thumbUrls;
    }

    public void setThumbUrls(ThumbUrls thumbUrls) {
        this.thumbUrls = thumbUrls;
    }

}
