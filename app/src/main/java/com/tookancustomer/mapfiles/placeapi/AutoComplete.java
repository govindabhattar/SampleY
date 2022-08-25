package com.tookancustomer.mapfiles.placeapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.retrofit2.BaseResponse;

import java.util.ArrayList;

public class AutoComplete extends BaseResponse {

    @SerializedName("data")
    @Expose
    private ArrayList<Predictions> data;

    public ArrayList<Predictions> getPredictions() {
        return data;
    }

}
