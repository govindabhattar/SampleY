
package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;

import java.io.Serializable;

public class Line implements Serializable{

    @SerializedName("data")
    @Expose
    public String data;
    @SerializedName("style")
    @Expose
    public Integer style;

    public String getData() {
        return data != null ? data : "";
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStyle() {
        return style != null ? style : Constants.NLevelAppStyles.REGULAR.appStyleValue;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }
}
