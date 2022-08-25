
package com.tookancustomer.customfield;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.ProductFilters.AllowValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @SerializedName("allowed_values")
    @Expose
    private List<String> allowValues = null;
    private ArrayList<AllowValue> allowedValues = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("value")
    @Expose
    private Object value = null;

    transient private Listener listener;
     private boolean readOnly;

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

    public Boolean isRequired() {
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
        return allowValues;
    }

    public void setAllowedValues(List<String> allowedValues) {
        this.allowValues = allowedValues;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return value == null ? "" : value;
    }

    public void setData(Object value) {
        this.value = value;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public interface Listener {

        Object getView();

    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setAllowedValuesWithIsSelected() {
        allowedValues.clear();
        for (String selectedV : getAllowedValues()) {
            allowedValues.add(new AllowValue(selectedV, false));
        }
    }

    public ArrayList<AllowValue> getAllowedValuesWithIsSelected() {
        return allowedValues;
    }


}
