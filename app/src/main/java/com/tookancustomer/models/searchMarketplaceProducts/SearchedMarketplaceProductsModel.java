package com.tookancustomer.models.searchMarketplaceProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class SearchedMarketplaceProductsModel extends BaseModel {

    @SerializedName("data")
    @Expose
    private SearchedMarketplaceProductsData data ;

    public SearchedMarketplaceProductsData getData() {
        return data;
    }

    public void setData(SearchedMarketplaceProductsData data) {
        this.data = data;
    }
}
