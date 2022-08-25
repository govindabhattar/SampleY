package com.tookancustomer.models.appConfiguration;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MappedPages implements Serializable
{


    @SerializedName("template_data")
    @Expose
    private String templateData;
    @SerializedName("type")
    @Expose
    private String type;

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String url) {
        this.templateData = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}