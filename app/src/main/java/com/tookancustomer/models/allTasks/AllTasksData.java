
package com.tookancustomer.models.allTasks;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllTasksData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private ArrayList<ArrayList<Datum>> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<ArrayList<Datum>> getData() {
        return data;
    }

    public void setData(ArrayList<ArrayList<Datum>> data) {
        this.data = data;
    }

}
