
package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Fields implements Serializable {

    @SerializedName("app_optional_fields")
    @Expose
    private List<AppOptionalField> appOptionalFields = null;
    @SerializedName("custom_field")
    @Expose
    private List<CustomField> customField = null;
    @SerializedName("extras")
    @Expose
    private Extras extras;
    @SerializedName("ref_images")
    @Expose
    private List<Object> refImages = null;
    @SerializedName("req_popup")
    @Expose
    private String reqPopup;

    public List<AppOptionalField> getAppOptionalFields() {
        return appOptionalFields;
    }

    public void setAppOptionalFields(List<AppOptionalField> appOptionalFields) {
        this.appOptionalFields = appOptionalFields;
    }

    public List<CustomField> getCustomField() {
        return customField;
    }

    public void setCustomField(List<CustomField> customField) {
        this.customField = customField;
    }

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }

    public List<Object> getRefImages() {
        return refImages;
    }

    public void setRefImages(List<Object> refImages) {
        this.refImages = refImages;
    }

    public String getReqPopup() {
        return reqPopup;
    }

    public void setReqPopup(String reqPopup) {
        this.reqPopup = reqPopup;
    }

}
