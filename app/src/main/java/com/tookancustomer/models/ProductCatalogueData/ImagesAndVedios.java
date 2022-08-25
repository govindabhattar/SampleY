package com.tookancustomer.models.ProductCatalogueData;

import java.io.Serializable;

public class ImagesAndVedios implements Serializable {

    private String imageVideoUrl;
    private int type = 1;
    private String videoThumb = "";


    public String getVideoThumb() {
        if (videoThumb == null || videoThumb.isEmpty()) {
            return "";
        }
        return videoThumb;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }


    public String getImageVideoUrl() {
        return imageVideoUrl;
    }

    public void setImageVideoUrl(String imageVideoUrl) {
        this.imageVideoUrl = imageVideoUrl;
    }

    public int getUrlType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
