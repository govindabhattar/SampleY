package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class NLevelWorkFlowData extends BaseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private Object data = new Object();

    private final static long serialVersionUID = -215534080089682073L;


    public ArrayList<ArrayList<Datum>> getData() {
        try {
            ArrayList<ArrayList<Datum>> mList
                    = new Gson().fromJson(new Gson().toJson(data),
                    new TypeToken<ArrayList<ArrayList<Datum>>>() {
                    }.getType());
            return mList;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }

        return new ArrayList<>();
    }

    public ArrayList<Datum> getDataAdminCategory() {
        try {
            ArrayList<Datum> mList
                    = new Gson().fromJson(new Gson().toJson(data),
                    new TypeToken<ArrayList<Datum>>() {
                    }.getType());
            return mList;
        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }

        return new ArrayList<>();
    }

    public void setData(ArrayList<ArrayList<Datum>> data) {
        this.data = data;
    }

    public void setDataAdminCategory(ArrayList<Datum> data) {
        this.data = data;
    }

}
