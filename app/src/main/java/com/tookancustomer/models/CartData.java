package com.tookancustomer.models;

import com.tookancustomer.models.ProductCatalogueData.Datum;

import java.util.ArrayList;

public class CartData {

    private ArrayList<Datum> productsArrayList = new ArrayList<>();
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData;

    public ArrayList<Datum> getProductsArrayList() {
        return productsArrayList;
    }

    public void setProductsArrayList(ArrayList<Datum> productsArrayList) {
        this.productsArrayList = productsArrayList;
    }

    public com.tookancustomer.models.MarketplaceStorefrontModel.Datum getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData) {
        this.merchantData = merchantData;
    }
}