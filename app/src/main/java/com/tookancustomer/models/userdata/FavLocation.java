
package com.tookancustomer.models.userdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class FavLocation implements Serializable {

    @SerializedName("fav_id")
    @Expose
    private Integer favId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("vendor_id")
    @Expose
    private Integer vendorId;
    @SerializedName("default_location")
    @Expose
    private Integer defaultLocation;

    /**
     * 
     * @return
     *     The favId
     */
    public Integer getFavId() {
        return favId;
    }

    /**
     * 
     * @param favId
     *     The fav_id
     */
    public void setFavId(Integer favId) {
        this.favId = favId;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The vendorId
     */
    public Integer getVendorId() {
        return vendorId;
    }

    /**
     * 
     * @param vendorId
     *     The vendor_id
     */
    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * 
     * @return
     *     The defaultLocation
     */
    public Integer getDefaultLocation() {
        return defaultLocation;
    }

    /**
     * 
     * @param defaultLocation
     *     The default_location
     */
    public void setDefaultLocation(Integer defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

}
