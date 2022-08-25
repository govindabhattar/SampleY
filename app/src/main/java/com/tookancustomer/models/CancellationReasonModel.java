package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gurmail S. Kang on 5/4/16.
 */
public class CancellationReasonModel {

    public boolean check;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("reason")
    @Expose
    private String reason;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
