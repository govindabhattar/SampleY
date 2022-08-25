package com.tookancustomer.models.referralData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    public Integer getNewCredits() {
        return newCredits;
    }

    public void setNewCredits(Integer newCredits) {
        this.newCredits = newCredits;
    }

    @SerializedName("new_credits")
    @Expose
    private Integer newCredits;

}
