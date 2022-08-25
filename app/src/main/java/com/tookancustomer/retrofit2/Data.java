package com.tookancustomer.retrofit2;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_no")
    @Expose
    public String phone_no;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return this.phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}

