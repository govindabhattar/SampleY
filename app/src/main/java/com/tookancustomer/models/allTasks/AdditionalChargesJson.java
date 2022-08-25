
package com.tookancustomer.models.allTasks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalChargesJson {

    @SerializedName("onTotal")
    @Expose
    private List<Object> onTotal = null;
    @SerializedName("onSubTotal")
    @Expose
    private List<Object> onSubTotal = null;

    public List<Object> getOnTotal() {
        return onTotal;
    }

    public void setOnTotal(List<Object> onTotal) {
        this.onTotal = onTotal;
    }

    public List<Object> getOnSubTotal() {
        return onSubTotal;
    }

    public void setOnSubTotal(List<Object> onSubTotal) {
        this.onSubTotal = onSubTotal;
    }

}
