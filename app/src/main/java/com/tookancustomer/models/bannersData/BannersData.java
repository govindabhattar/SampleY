
package com.tookancustomer.models.bannersData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannersData {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("result")
    @Expose
    private List<Banner> banner = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

}
