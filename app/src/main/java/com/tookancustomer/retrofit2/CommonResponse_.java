package com.tookancustomer.retrofit2;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonResponse_ extends BaseResponse {
//    @SerializedName("data")
//    @Expose
//    private Object data;

    @SerializedName("app_id")
    @Expose
    private String appId;

//    public <T> T toResponseModel(Class<T> classRef) {
//        return new Gson().fromJson(new Gson().toJson(data), classRef);
//    }

//    public JSONObject getJson() {
//        try {
//            return new JSONObject(new Gson().toJson(data));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String getData() {
//        return data.toString();
//    }
//
//    public Object getDataObject() {
//        return data;
//    }

    public String getAppId() {
        return appId == null || appId.isEmpty() ? "" : appId;
    }

}
