package com.tookancustomer.models;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.getCountryCode.Geo;
import com.tookancustomer.models.getCountryCode.Original;

/**
 * Created by cl-macmini-83 on 10/10/16.
 */

public class BaseModel {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ip")
    @Expose
    private String ip;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    public Object data;
    @SerializedName("metaInfo")
    @Expose
    public Object metaInfo;
    @SerializedName("geo")
    @Expose
    private Geo geo;
    @SerializedName("original")
    @Expose
    private Original original;


    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getIp() {
        return ip;
    }

    /**
     * Get data model.
     *
     * @param classRef
     * @param <T>
     * @return
     */
    public <T> T toResponseModel(final Class<T> classRef) {
        return new Gson().fromJson(new Gson().toJson(data), classRef);
    }

    /**
     * Get data model.
     *
     * @param classRef
     * @param <T>
     * @return
     */
    public <T> T toResponseModelMetaInfo(final Class<T> classRef) {
        return new Gson().fromJson(new Gson().toJson(metaInfo), classRef);
    }


}
