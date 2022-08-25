package com.tookancustomer.retrofit2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class CommonResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

//    @SerializedName("data")
//    @Expose
//    private  T data;


    public String getMessage() {
        return message;
    }

//    public T getData() {
//        return data;
//    }
//
//    @Override
//    public String toString() {
//        return status + " " + message + "\n" + data;
//    }


}
