package com.tookancustomer.agentListing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.checkoutTemplate.model.Template;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Utkarsh Shukla on 2019-10-22.
 */

public class AgentData implements Serializable {

    @SerializedName("fleet_id")
    @Expose
    private int fleetId;
    @SerializedName("fleet_thumb_image")
    @Expose
    private String fleetThumbImage;
    @SerializedName("fleet_image")
    @Expose
    private String fleetImage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("login_id")
    @Expose
    private String loginId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("is_available")
    @Expose
    private Integer isAvailable;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("fleet_status_color")
    @Expose
    private String fleetStatusColor;
    @SerializedName("ratings")
    @Expose
    private double ratings;
    @SerializedName("distance")
    @Expose
    private Double distance;

    @SerializedName("signup_data")
    @Expose
    private ArrayList<Template> template = null;

    public ArrayList<Template> getTemplate() {
        return template;
    }

    public void setTemplate(ArrayList<Template> template) {
        this.template = template;
    }

    private final static long serialVersionUID = 5010533427363673255L;

    public int getFleetId() {
        return fleetId;
    }

    public void setFleetId(int fleetId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFleetStatusColor() {
        return fleetStatusColor;
    }

    public void setFleetStatusColor(String fleetStatusColor) {
        this.fleetStatusColor = fleetStatusColor;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}