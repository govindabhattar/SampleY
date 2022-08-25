package com.tookancustomer.models.MarketplaceStorefrontModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LastReviewRating implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rating")
    @Expose
    private Number rating = 0;
    @SerializedName("review")
    @Expose
    private String review = "";
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("customer_name")
    @Expose
    private String customerName = "";
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("storefront_user_id")
    @Expose
    private String storefrontUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Number getRating() {
        return rating != null ? rating : 0;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review != null ? review : "";
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCreationDatetime() {
        return creationDatetime!=null?creationDatetime:"";
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getStorefrontUserId() {
        return storefrontUserId;
    }

    public void setStorefrontUserId(String storefrontUserId) {
        this.storefrontUserId = storefrontUserId;
    }

    public LastReviewRating() {
    }

}
