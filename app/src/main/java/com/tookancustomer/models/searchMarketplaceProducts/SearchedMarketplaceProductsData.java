package com.tookancustomer.models.searchMarketplaceProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Data;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;

import java.util.ArrayList;

public class SearchedMarketplaceProductsData extends BaseModel {

    @SerializedName("result")
    @Expose
    private ArrayList<Datum> resultList;
    @SerializedName("search_text")
    @Expose
    private String searchText="";

    public ArrayList<Datum> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<Datum> resultList) {
        this.resultList = resultList;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
