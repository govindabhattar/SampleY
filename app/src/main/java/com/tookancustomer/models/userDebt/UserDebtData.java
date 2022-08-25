package com.tookancustomer.models.userDebt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.userdata.UserRights;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDebtData implements Serializable {
    @SerializedName("debt_amount")
    @Expose
    private double debtAmount = 0;
    @SerializedName("job_id")
    @Expose
    private int jobId = 0;
//    @SerializedName("debt_data")
//    @Expose
//    private List<DebtDataModel> debtDataArray = new ArrayList<>();

    public double getDebtAmount() {
        return debtAmount;
    }

//    public List<DebtDataModel> getDebtDataArray() {
//        return debtDataArray;
//    }

    public int getJobId() {
        return jobId;
    }
}
