
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {

    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("cancellation_charges")
    @Expose
    private Integer cancellationCharges;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("base_fare")
    @Expose
    private Number baseFare;
    @SerializedName("per_minute_charges")
    @Expose
    private Number perMinuteCharges;
    @SerializedName("per_kilometer_charges")
    @Expose
    private Number perKilometerCharges;
    @SerializedName("minimum_fare")
    @Expose
    private Number minimumFare;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("selected_category_image")
    @Expose
    private String selectedCategoryImage;
    @SerializedName("card_view")
    @Expose
    private String cardView;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("max_size")
    @Expose
    private Integer maxSize;
    @SerializedName("taxis")
    @Expose
    private List<Taxi> taxis = new ArrayList<>();

    private Integer selectedQuantity = 0;

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getSelectedCategoryImage() {
        return selectedCategoryImage;
    }

    public void setSelectedCategoryImage(String selectedCategoryImage) {
        this.selectedCategoryImage = selectedCategoryImage;
    }

    public String getCardView() {
        return cardView;
    }

    public void setCardView(String cardView) {
        this.cardView = cardView;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Number getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Number baseFare) {
        this.baseFare = baseFare;
    }

    public Number getPerMinuteCharges() {
        return perMinuteCharges;
    }

    public void setPerMinuteCharges(Number perMinuteCharges) {
        this.perMinuteCharges = perMinuteCharges;
    }

    public Number getPerKilometerCharges() {
        return perKilometerCharges;
    }

    public void setPerKilometerCharges(Number perKilometerCharges) {
        this.perKilometerCharges = perKilometerCharges;
    }

    public Number getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(Number minimumFare) {
        this.minimumFare = minimumFare;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public List<Taxi> getTaxis() {
        return taxis;
    }

    public void setTaxis(List<Taxi> taxis) {
        this.taxis = taxis;
    }

    public Integer getCancellationCharges() {
        return cancellationCharges;
    }

    public void setCancellationCharges(Integer cancellationCharges) {
        this.cancellationCharges = cancellationCharges;
    }
}
