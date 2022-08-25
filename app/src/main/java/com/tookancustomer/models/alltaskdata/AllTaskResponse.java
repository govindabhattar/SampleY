package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;

public class AllTaskResponse extends BaseModel implements Serializable {

    @Expose
    private ArrayList<ArrayList<Data>> data = null;
    private final static long serialVersionUID = 310698901313454510L;

    public ArrayList<ArrayList<Data>> getData() {
        return data;
    }

    public void setData(ArrayList<ArrayList<Data>> data) {
        this.data = data;
    }
}
