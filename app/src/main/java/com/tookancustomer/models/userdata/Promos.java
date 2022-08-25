
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Promos implements Serializable {

    public boolean isSelected;
    @SerializedName("promo_id")
    @Expose
    private Integer promo_id;
    @SerializedName("access_id")
    @Expose
    private Integer access_id;
    @SerializedName("promo_code")
    @Expose
    private String promo_code;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("form_id")
    @Expose
    private Integer form_id;
    @SerializedName("promo_type")
    @Expose
    private Integer promo_type;
    @SerializedName("benefit_type")
    @Expose
    private Integer benefit_type;
    @SerializedName("max_count")
    @Expose
    private Integer max_count;
    @SerializedName("current_count")
    @Expose
    private Integer current_count;
    @SerializedName("start_datetime_utc")
    @Expose
    private String start_datetime_utc;
    @SerializedName("expiry_datetime_utc")
    @Expose
    private String expiry_datetime_utc;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("creation_datetime")
    @Expose
    private String promocreation_datetime_code;
    @SerializedName("promo_value")
    @Expose
    private Integer promo_value;
    @SerializedName("per_customer_limit")
    @Expose
    private Integer per_customer_limit;
    @SerializedName("minimum_transactions")
    @Expose
    private Integer minimum_transactions;

    public Integer getPromo_id() {
        return promo_id;
    }

    public void setPromo_id(Integer promo_id) {
        this.promo_id = promo_id;
    }

    public String getPromo_code() {
        if (promo_code != null)
            return promo_code;
        else
            return "";
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getForm_id() {
        return form_id;
    }

    public void setForm_id(Integer form_id) {
        this.form_id = form_id;
    }

    public Integer getPromo_type() {
        return promo_type;
    }

    public void setPromo_type(Integer promo_type) {
        this.promo_type = promo_type;
    }

    public Integer getBenefit_type() {
        return benefit_type;
    }

    public void setBenefit_type(Integer benefit_type) {
        this.benefit_type = benefit_type;
    }

    public Integer getMax_count() {
        return max_count;
    }

    public void setMax_count(Integer max_count) {
        this.max_count = max_count;
    }

    public Integer getCurrent_count() {
        return current_count;
    }

    public void setCurrent_count(Integer current_count) {
        this.current_count = current_count;
    }

    public String getStart_datetime_utc() {
        return start_datetime_utc;
    }

    public void setStart_datetime_utc(String start_datetime_utc) {
        this.start_datetime_utc = start_datetime_utc;
    }

    public String getExpiry_datetime_utc() {
        return expiry_datetime_utc;
    }

    public void setExpiry_datetime_utc(String expiry_datetime_utc) {
        this.expiry_datetime_utc = expiry_datetime_utc;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromocreation_datetime_code() {
        return promocreation_datetime_code;
    }

    public void setPromocreation_datetime_code(String promocreation_datetime_code) {
        this.promocreation_datetime_code = promocreation_datetime_code;
    }

    public Integer getPromo_value() {
        return promo_value;
    }

    public void setPromo_value(Integer promo_value) {
        this.promo_value = promo_value;
    }

    public Integer getPer_customer_limit() {
        return per_customer_limit;
    }

    public void setPer_customer_limit(Integer per_customer_limit) {
        this.per_customer_limit = per_customer_limit;
    }

    public Integer getMinimum_transactions() {
        return minimum_transactions;
    }

    public void setMinimum_transactions(Integer minimum_transactions) {
        this.minimum_transactions = minimum_transactions;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Integer getAccess_id() {
        return access_id;
    }

    public void setAccess_id(Integer access_id) {
        this.access_id = access_id;
    }

}
