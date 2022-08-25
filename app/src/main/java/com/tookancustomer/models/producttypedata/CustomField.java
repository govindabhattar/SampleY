
package com.tookancustomer.models.producttypedata;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomField {

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
    @SerializedName("allowed_values")
    @Expose
    private List<String> allowedValues = null;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("value")
    @Expose
    private List<String> value = null;

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

    public List<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

}
