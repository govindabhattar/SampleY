
package com.tookancustomer.models.listingdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.ProductCatalogueData.Datum;

import java.util.ArrayList;

public class Data {

    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("is_review_rating_enabled")
    @Expose
    private int isReviewRatingEnabled;
    @SerializedName("result")
    @Expose
    private ArrayList<Datum> result=new ArrayList();

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public int getIsReviewRatingEnabled() {
        return isReviewRatingEnabled;
    }

    public void setIsReviewRatingEnabled(int isReviewRatingEnabled) {
        this.isReviewRatingEnabled = isReviewRatingEnabled;
    }

    public ArrayList<Datum> getResult() {
        return result;
    }

    public void setResult(ArrayList<Datum> result) {
        this.result = result;
    }
}
