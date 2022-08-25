package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TrackingDetails implements Serializable {

    @SerializedName("agent_info")
    @Expose
    private AgentInfo agentInfo;
    @SerializedName("tracking_link")
    @Expose
    private String trackingLink;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 1873589731390345584L;

    public AgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
