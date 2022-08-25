package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FleetMovement implements Serializable {

@SerializedName("fleet_id")
@Expose
private Integer fleetId;
@SerializedName("longitude")
@Expose
private String longitude;
@SerializedName("latitude")
@Expose
private String latitude;
@SerializedName("creation_datetime")
@Expose
private String creationDatetime;
@SerializedName("lat")
@Expose
private String lat;
@SerializedName("lng")
@Expose
private String lng;

public Integer getFleetId() {
return fleetId;
}

public void setFleetId(Integer fleetId) {
this.fleetId = fleetId;
}

public String getLongitude() {
return longitude;
}

public void setLongitude(String longitude) {
this.longitude = longitude;
}

public String getLatitude() {
return latitude;
}

public void setLatitude(String latitude) {
this.latitude = latitude;
}

public String getCreationDatetime() {
return creationDatetime;
}

public void setCreationDatetime(String creationDatetime) {
this.creationDatetime = creationDatetime;
}

public double getLat() {
return Double.parseDouble(lat);
}

public void setLat(String lat) {
this.lat = lat;
}

public Double getLng() {
    return Double.parseDouble(lng);
}

public void setLng(String lng) {
this.lng = lng;
}

}