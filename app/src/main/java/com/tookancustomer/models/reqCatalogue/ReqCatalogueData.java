package com.tookancustomer.models.reqCatalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.modules.merchantCatalog.models.categories.Result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ashutosh Ojha on 2019-05-07.
 */
public class ReqCatalogueData  implements Serializable {


    @SerializedName("result")
    @Expose
    private List<Result> reqCatalogues = null;


    @SerializedName("has_image")
    @Expose
    public int hasImage;

    public int getHasImage() {
        return hasImage;
    }

    public void setHasImage(final int hasImage) {
        this.hasImage = hasImage;
    }

    public List<Result> getReqCatalogues() {
        return reqCatalogues;
    }

    public void setReqCatalogues(final List<Result> reqCatalogues) {
        this.reqCatalogues = reqCatalogues;
    }


}
