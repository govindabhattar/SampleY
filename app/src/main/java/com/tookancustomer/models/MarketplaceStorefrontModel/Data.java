package com.tookancustomer.models.MarketplaceStorefrontModel;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

    @SerializedName("total_ratings_count")
    @Expose
    private Integer totalRatingsCount = 0;
    @SerializedName("total_review_count")
    @Expose
    private Integer totalReviewCount = 0;
    @SerializedName("store_rating")
    @Expose
    private Number storeRating = 0;
    @SerializedName("last_review_rating")
    @Expose
    private ArrayList<LastReviewRating> lastReviewRating = new ArrayList<>();

    public Integer getTotalRatingsCount() {
        return totalRatingsCount;
    }

    public void setTotalRatingsCount(Integer totalRatingsCount) {
        this.totalRatingsCount = totalRatingsCount;
    }

    public Integer getTotalReviewCount() {
        return totalReviewCount;
    }

    public void setTotalReviewCount(Integer totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public Number getStoreRating() {
        return storeRating != null ? storeRating : 0;
    }

    public void setStoreRating(Number storeRating) {
        this.storeRating = storeRating;
    }

    public ArrayList<LastReviewRating> getLastReviewRating() {
        return lastReviewRating;
    }

    public void setLastReviewRating(ArrayList<LastReviewRating> lastReviewRating) {
        this.lastReviewRating = lastReviewRating;
    }

    public Data() {
    }

}