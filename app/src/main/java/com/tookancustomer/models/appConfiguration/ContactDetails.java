package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContactDetails implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDateTime;
    @SerializedName("t_n_c")
    @Expose
    private String tnc;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getcontactEmail() {
        return contactEmail;
    }

    public void setcontactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getcontactNumber() {
        return contactNumber;
    }

    public void setcontactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

}
