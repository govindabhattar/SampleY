package com.tookancustomer.models;

import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.modules.merchantCatalog.models.categories.MerchantCategoriesData;

import java.util.HashMap;

public class MerchantCachedData {
    private Datum storefrontData;
    private HashMap<Integer, MerchantCategoriesData> categoriesListHashmap = new HashMap<>();
    private HashMap<Integer, ProductCatalogueData> productsListHashmap = new HashMap<>();
    private String dateTime;
    private Integer businessCategoryId;

    public Integer getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(Integer businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Datum getStorefrontData() {
        return storefrontData;
    }

    public void setStorefrontData(Datum storefrontData) {
        this.storefrontData = storefrontData;
    }


    public HashMap<Integer, MerchantCategoriesData> getCategoriesListHashmap() {
        return categoriesListHashmap;
    }

    public void setCategoriesListHashmap(HashMap<Integer, MerchantCategoriesData> categoriesListHashmap) {
        this.categoriesListHashmap = categoriesListHashmap;
    }

    public HashMap<Integer, ProductCatalogueData> getProductsListHashmap() {
        return productsListHashmap;
    }

    public void setProductsListHashmap(HashMap<Integer, ProductCatalogueData> productsListHashmap) {
        this.productsListHashmap = productsListHashmap;
    }

    public void clearMerchantCachedData() {
        setStorefrontData(null);
        setCategoriesListHashmap(null);
        setProductsListHashmap(null);
        setDateTime(null);
        setBusinessCategoryId(null);
    }
}
