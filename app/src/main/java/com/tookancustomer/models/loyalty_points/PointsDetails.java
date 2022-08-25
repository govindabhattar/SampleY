package com.tookancustomer.models.loyalty_points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.taskdetails.TaskData;

import java.util.ArrayList;
import java.util.List;

public class PointsDetails {

    @SerializedName("data")
    @Expose
    private List<PointsData> data = new ArrayList<>();

    public List<PointsData> getData() {
        return data;
    }

    public void setData(List<PointsData> data) {
        this.data = data;
    }

}
