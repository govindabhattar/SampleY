package com.tookancustomer.models.billbreakdown;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

public class TipModel  implements Serializable{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("value")
    @Expose
    private BigDecimal value;
     @SerializedName("amount")
    @Expose
    private BigDecimal amount;
    @SerializedName("type_id")
    @Expose
    private int typeId;
    @SerializedName("is_active")
    @Expose
    private int isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getValueString() {
        return value != null ? Utils.getDoubleTwoDigits(value) : "0";
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountString() {
        return amount != null ? Utils.getDoubleTwoDigits(amount) : "0";
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

}