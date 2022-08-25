package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cl-macmini-83 on 19/11/16.
 */

public class ImageUrlPojo extends BaseModel {
    @SerializedName("data")
    @Expose
    private ImageUrl data;
    /**
     *
     * @return
     *     The data
     */
    public ImageUrl getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(ImageUrl data) {
        this.data = data;
    }

}
