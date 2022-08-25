
package com.tookancustomer.models.getCountryCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("accuracy_radius")
    @Expose
    private Integer accuracyRadius;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;

    public Integer getAccuracyRadius() {
        return accuracyRadius;
    }

    public void setAccuracyRadius(Integer accuracyRadius) {
        this.accuracyRadius = accuracyRadius;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
