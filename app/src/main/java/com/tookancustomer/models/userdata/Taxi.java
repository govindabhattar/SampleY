
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Taxi implements Serializable {

    @SerializedName("device_type")
    @Expose
    private Integer deviceType;
    @SerializedName("fleet_id")
    @Expose
    private String fleetId;
    @SerializedName("fleet_thumb_image")
    @Expose
    private String fleetThumbImage;
    @SerializedName("fleet_image")
    @Expose
    private String fleetImage;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("transport_type")
    @Expose
    private String transportType;
    @SerializedName("transport_desc")
    @Expose
    private String transportDesc;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("registration_status")
    @Expose
    private Integer registrationStatus;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("is_available")
    @Expose
    private Integer isAvailable;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("last_updated_location_time")
    @Expose
    private String lastUpdatedLocationTime;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("last_updated_timings")
    @Expose
    private String lastUpdatedTimings;
    @SerializedName("fleet_status_color")
    @Expose
    private String fleetStatusColor;
    @SerializedName("view_status")
    @Expose
    private Integer viewStatus;
    @SerializedName("edit_status")
    @Expose
    private Integer editStatus;
    @SerializedName("eta")
    @Expose
    private Double eta;

    public Double getEta() {
        return eta;
    }

    public void setEta(Double eta) {
        this.eta = eta;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getFleetId() {
        return fleetId;
    }

    public void setFleetId(String fleetId) {
        this.fleetId = fleetId;
    }

    public String getFleetThumbImage() {
        return fleetThumbImage;
    }

    public void setFleetThumbImage(String fleetThumbImage) {
        this.fleetThumbImage = fleetThumbImage;
    }

    public String getFleetImage() {
        return fleetImage;
    }

    public void setFleetImage(String fleetImage) {
        this.fleetImage = fleetImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportDesc() {
        return transportDesc;
    }

    public void setTransportDesc(String transportDesc) {
        this.transportDesc = transportDesc;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(Integer registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastUpdatedLocationTime() {
        return lastUpdatedLocationTime;
    }

    public void setLastUpdatedLocationTime(String lastUpdatedLocationTime) {
        this.lastUpdatedLocationTime = lastUpdatedLocationTime;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLastUpdatedTimings() {
        return lastUpdatedTimings;
    }

    public void setLastUpdatedTimings(String lastUpdatedTimings) {
        this.lastUpdatedTimings = lastUpdatedTimings;
    }

    public String getFleetStatusColor() {
        return fleetStatusColor;
    }

    public void setFleetStatusColor(String fleetStatusColor) {
        this.fleetStatusColor = fleetStatusColor;
    }

    public Integer getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Integer viewStatus) {
        this.viewStatus = viewStatus;
    }

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }

}
