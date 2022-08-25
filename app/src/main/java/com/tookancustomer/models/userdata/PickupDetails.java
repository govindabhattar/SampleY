package com.tookancustomer.models.userdata;

import java.io.Serializable;

/**
 * Created by cl-macmini-83 on 29/11/16.
 */

public class PickupDetails implements Serializable {
    String username;
    String description;
    String address;
    double latitude;
    double longitude;
    String email;
    String pickupTime;
    private UserOptions userOptions;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String countryCode;
    String phoneNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public PickupDetails()
    {

    }

    public void setUserOptions(UserOptions userOptions) {
            this.userOptions=userOptions;
    }
    public UserOptions getUserOptions()
    {
        return userOptions;
    }
}
