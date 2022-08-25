
package com.tookancustomer.models.listingdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Button {

    @SerializedName("type")
    @Expose
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
