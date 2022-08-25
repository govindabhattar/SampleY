package com.tookancustomer.models.ProductFilters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterAndValue implements Serializable{

    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("allowed_Values")
    @Expose
    private List<String> allowedValues = new ArrayList<>();
    private ArrayList<AllowValue> allowValues = new ArrayList<>();

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
//        setAllowedValuesWithIsSelected();
    }

    public void setAllowedValuesWithIsSelected() {
        for (String selectedV : getAllowedValues()) {
            allowValues.add(new AllowValue(selectedV, false));
        }
    }

    public ArrayList<AllowValue> getAllowedValuesWithIsSelected() {
        return allowValues;
    }
}