package com.tookancustomer.models.favLocations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Locations implements Serializable {
    @SerializedName("is_custom_address")
    @Expose
    private int isCustomAddress = 0;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("fav_id")
    @Expose
    private Integer fav_id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("vendor_id")
    @Expose
    private Integer vendor_id;
    @SerializedName("default_location")
    @Expose
    private Integer default_location;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("landmark")
    @Expose
    private String landmark = "";
    @SerializedName("house")
    @Expose
    private String house = "";
    @SerializedName("locType")
    @Expose
    private Integer locType;

    public int getIsCustomAddress() {
        return isCustomAddress;
    }

    public void setIsCustomAddress(int isCustomAddress) {
        this.isCustomAddress = isCustomAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFav_id() {
        return fav_id;
    }

    public void setFav_id(Integer fav_id) {
        this.fav_id = fav_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public Integer getDefault_location() {
        return default_location;
    }

    public void setDefault_location(Integer default_location) {
        this.default_location = default_location;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Integer getLocType() {
        return locType;
    }

    public void setLocType(Integer locType) {
        this.locType = locType;
    }
}
