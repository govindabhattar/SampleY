
package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;

import java.io.Serializable;

public class Image implements Serializable{

    @SerializedName("data")
    @Expose
    public String data;
    @SerializedName("size")
    @Expose
    public Integer size;

    public String getData() {
        return data != null ? data : "";
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getSize() {
        return size != null ? size : Constants.NLevelImageStyles.NONE.appStyleValue;
    }

    public void setSize(Integer style) {
        this.size = style;
    }
}
