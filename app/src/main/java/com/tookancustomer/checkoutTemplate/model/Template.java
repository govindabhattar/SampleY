
package com.tookancustomer.checkoutTemplate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.ProductFilters.AllowValue;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.utility.Utils;

public class Template implements Serializable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("filter")
    @Expose
    private int filter;
    @SerializedName("required")
    @Expose
    private int required;
    @SerializedName("group")
    @Expose
    private int group;
    @SerializedName("group_headline")
    @Expose
    private String group_headline;
    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("allowed_values")
    @Expose
    private ArrayList<String> allowedValues = null;
    private ArrayList<AllowValue> allowValues = new ArrayList<>();
    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("value")
    @Expose
    private Object value;

    @SerializedName("option")
    @Expose
    private ArrayList<Option> option;
    @SerializedName("cost")
    @Expose
    private double cost;



    private String countryCode="";

    public String getGroup_headline() {
        return group_headline;
    }

    public void setGroup_headline(String group_headline) {
        this.group_headline = group_headline;
    }

    private transient SignupTemplateData.Listener listener;

    public SignupTemplateData.Listener getListener() {
        return listener;
    }

    public void setListener(SignupTemplateData.Listener listener) {
        this.listener = listener;
    }

    /**
     * Method to get the Current View of this Class
     *
     * @return
     */
    public Object getView() {

        if (listener != null)
            return listener.getView();

        return null;
    }

    public Object getData() {
        return value == null ? "" : value;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setData(Object data) {
        this.value = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getRequired() {
        return required;
    }

    public boolean isRequired() {
        return required == 1;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public ArrayList<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(ArrayList<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAllowedValuesWithIsSelected() {
        if (allowValues.size() == 0) {
            allowValues.clear();

            for (int i = 0; i < allowedValues.size(); i++) {
                String selectedV = allowedValues.get(i);
                if (option != null) {
                    try {
                        if (option.size() - 1 >= i)
                            allowValues.add(new AllowValue(selectedV, isInsideValue(selectedV), option.get(i).getCost()));
                    } catch (Exception e) {
                        Utils.printStackTrace(e);
                    }

                } else {
                    allowValues.add(new AllowValue(selectedV, isInsideValue(selectedV)));
                }
            }
        } else return;
    }

    public boolean isInsideValue(String selectedV) {
        if (value instanceof ArrayList) {
            ArrayList<String> values = (ArrayList<String>) value;
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).equals(selectedV)) {
                    return true;
                }
            }

        }

        return false;
    }

    public ArrayList<AllowValue> getAllowedValuesWithIsSelected() {
        return allowValues;
    }

    public Object getValue() {
        return value == null ? "" : value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Interface to make the CustomFields
     * Listen to the external events
     */
    public interface Listener {

        Object getView();

    }


    public ArrayList<Option> getOption() {
        return option;
    }

    public void setOption(ArrayList<Option> option) {
        this.option = option;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
