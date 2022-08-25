package com.tookancustomer.models.searchedProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

public class SearchedProductsModel extends BaseModel {

    @SerializedName("data")
    @Expose
    private SearchedProductsData data ;

    public SearchedProductsData getData() {
        return data;
    }

    public void setData(SearchedProductsData data) {
        this.data = data;
    }
}
