package com.tookancustomer.retrofit2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("status")
    @Expose
    private int    status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

}
