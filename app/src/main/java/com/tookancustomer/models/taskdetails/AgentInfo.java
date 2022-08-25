package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgentInfo implements Serializable {

    @SerializedName("fleet_id")
    @Expose
    private Integer fleetId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fleet_image")
    @Expose
    private String fleetImage;
    @SerializedName("fleet_thumb_image")
    @Expose
    private String fleetThumbImage;
    @SerializedName("fleet_rating")
    @Expose
    private Number fleetRating;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getFleetId() {
        return fleetId;
    }

    public void setFleetId(Integer fleetId) {
        this.fleetId = fleetId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getFleetImage() {
        return fleetImage;
    }

    public void setFleetImage(String fleetImage) {
        this.fleetImage = fleetImage;
    }

    public String getFleetThumbImage() {
        return fleetThumbImage;
    }

    public void setFleetThumbImage(String fleetThumbImage) {
        this.fleetThumbImage = fleetThumbImage;
    }

    public Number getFleetRating() {
        return fleetRating != null ? fleetRating : 0;

    }

    public void setFleetRating(Number fleetRating) {
        this.fleetRating = fleetRating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}