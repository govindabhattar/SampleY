package com.tookancustomer.models.ProductCatalogueData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Parminder Saini on 28/07/17.
 */

public class CustomiseOptionsId  implements Serializable {
    @SerializedName("id")
    private Integer id;

    public Integer getId() {
        return id;
    }
}
