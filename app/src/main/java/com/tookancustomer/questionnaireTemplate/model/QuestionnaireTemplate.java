package com.tookancustomer.questionnaireTemplate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.checkoutTemplate.model.Template;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionnaireTemplate implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("marketplace_user_id")
    @Expose
    private Integer marketplaceUserId;
    @SerializedName("template")
    @Expose
    private ArrayList<Template> template = null;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("updation_datetime")
    @Expose
    private String updationDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(Integer marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public ArrayList<Template> getTemplate() {
        return template;
    }

    public void setTemplate(ArrayList<Template> template) {
        this.template = template;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }

    public void setUpdationDatetime(String updationDatetime) {
        this.updationDatetime = updationDatetime;
    }

}
