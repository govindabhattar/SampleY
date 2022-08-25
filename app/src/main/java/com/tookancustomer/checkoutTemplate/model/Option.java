package com.tookancustomer.checkoutTemplate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;

public class Option implements Serializable{

    @SerializedName("cost")
    @Expose
    private double cost;
    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("currency_id")
    @Expose
    private int currency_id;


    public int getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
