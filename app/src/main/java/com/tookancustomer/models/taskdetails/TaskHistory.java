
package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TaskHistory implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("fleet_id")
    @Expose
    private Object fleetId;
    @SerializedName("fleet_name")
    @Expose
    private Object fleetName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longitude")
    @Expose
    private Object longitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Object getFleetId() {
        return fleetId;
    }

    public void setFleetId(Object fleetId) {
        this.fleetId = fleetId;
    }

    public Object getFleetName() {
        return fleetName;
    }

    public void setFleetName(Object fleetName) {
        this.fleetName = fleetName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

}
