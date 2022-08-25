
package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppOptionalField implements Serializable{

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("required")
    @Expose
    private Integer required;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

}
