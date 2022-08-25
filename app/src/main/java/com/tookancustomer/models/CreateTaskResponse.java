package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.appConfiguration.MappedPages;

import java.util.ArrayList;

public class CreateTaskResponse {

    @SerializedName("job_id")
    @Expose
    private int orderId;
    @SerializedName("authentication_required")
    @Expose
    private int authenticationRequired;
    @SerializedName("authentication_url")
    @Expose
    private String authenticationUrl;
    @SerializedName("mapped_pages")
    @Expose
    private ArrayList<MappedPages> mappedPages = new ArrayList<>();

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public int getAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(int authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

    public int getOrderId() {
        return orderId;
    }

    public ArrayList<MappedPages> getMappedPages() {
        return mappedPages;
    }

    public void setMappedPages(ArrayList<MappedPages> mappedPages) {
        this.mappedPages = mappedPages;
    }
}
