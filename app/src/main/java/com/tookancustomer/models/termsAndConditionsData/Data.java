package com.tookancustomer.models.termsAndConditionsData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("application_name")
    @Expose
    private String application_Name;
    @SerializedName("website_name")
    @Expose
    private String websiteName;
    @SerializedName("date_of_establishment")
    @Expose
    private String dateOfEstablishment;
    @SerializedName("tnc_type")
    @Expose
    private int tncType;
    @SerializedName("template_data")
    @Expose
    private String templateData;
    @SerializedName("tnc_user_link")
    @Expose
    private String tncUserLink;

    public String getTemplateData() {
        return templateData;
    }

    public int getTncType() {
        return tncType;
    }

    public String getTncUserLink() {
        return tncUserLink;
    }
}