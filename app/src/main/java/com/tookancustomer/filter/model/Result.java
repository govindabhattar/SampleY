
package com.tookancustomer.filter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("order")
    @Expose
    private int order;
    //    @SerializedName("value")
//    @Expose
    private ArrayList<String> values = new ArrayList<>();
    @SerializedName("filter")
    @Expose
    private int filter;
    @SerializedName("required")
    @Expose
    private boolean required;
    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("allowed_values")
    @Expose
    private ArrayList<String> allowedValues = null;

    private ArrayList<AllowedDataList> allowedDataList;

    private boolean isSelected;

    @SerializedName("business_category_id")
    @Expose
    private int businessCategoryId;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
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

    public ArrayList<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(ArrayList<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public ArrayList<AllowedDataList> getAllowedDataList() {
        if (allowedDataList == null) {
            allowedDataList = new ArrayList<>();
            for (int j = 0; j < getAllowedValues().size(); j++) {
                allowedDataList.add(new AllowedDataList(getAllowedValues().get(j), false));
            }
            return allowedDataList;

        } else
            return allowedDataList;
    }

    public void setAllowedDataList(ArrayList<AllowedDataList> allowedDataList) {
        this.allowedDataList = allowedDataList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBusinessCategoryId(int businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeInt(this.order);
        dest.writeStringList(this.values);
        dest.writeInt(this.filter);
        dest.writeInt(this.businessCategoryId);
        dest.writeByte(this.required ? (byte) 1 : (byte) 0);
        dest.writeString(this.dataType);
        dest.writeString(this.displayName);
        dest.writeStringList(this.allowedValues);
        dest.writeTypedList(this.allowedDataList);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public Result() {
    }

    protected Result(Parcel in) {
        this.label = in.readString();
        this.order = in.readInt();
        this.values = in.createStringArrayList();
        this.filter = in.readInt();
        this.businessCategoryId = in.readInt();
        this.required = in.readByte() != 0;
        this.dataType = in.readString();
        this.displayName = in.readString();
        this.allowedValues = in.createStringArrayList();
        this.allowedDataList = in.createTypedArrayList(AllowedDataList.CREATOR);
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
