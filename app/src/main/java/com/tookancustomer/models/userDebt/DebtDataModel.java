package com.tookancustomer.models.userDebt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DebtDataModel implements Serializable {
    @SerializedName("debt_amount")
    @Expose
    private double debtAmount = 0;
    @SerializedName("job_id")
    @Expose
    private int jobId;

    public double getDebtAmount() {
        return debtAmount;
    }

    public int getJobId() {
        return jobId;
    }
}
