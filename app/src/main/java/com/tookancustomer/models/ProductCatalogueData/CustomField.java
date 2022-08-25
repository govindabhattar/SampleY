package com.tookancustomer.models.ProductCatalogueData;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomField implements Serializable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("required")
    @Expose
    private Boolean required;
    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("value")
    @Expose
    private Object value;
    @SerializedName("allowed_values")
    @Expose
    private List<String> allowedValues = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getValue() {
        return value != null ? value : "";
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

}