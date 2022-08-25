
package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskDetails extends BaseModel implements Serializable {


    @SerializedName("data")
    @Expose
    private List<TaskData> data = new ArrayList<>();


    public List<TaskData> getData() {
        return data;
    }

    public void setData(List<TaskData> data) {
        this.data = data;
    }

}
