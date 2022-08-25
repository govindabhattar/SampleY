package com.tookancustomer.models.MarketplaceStorefrontModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class MerchantAllReviewsModel extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private ArrayList<LastReviewRating> data = new ArrayList<>();

    public ArrayList<LastReviewRating> getData() {
        return data;
    }

    public void setData(ArrayList<LastReviewRating> data) {
        this.data = data;
    }

    public MerchantAllReviewsModel() {
    }

}
