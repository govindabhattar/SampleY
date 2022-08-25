
package com.tookancustomer.models.favLocations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;

public class LocationsModel extends BaseModel implements Serializable {


    @SerializedName("data")
    @Expose
    private FavouriteLocations favouriteLocations;

    /**
     *
     * @return
     *     The data
     */
    public FavouriteLocations getData() {
        return favouriteLocations;
    }

    /**
     *
     * @param favouriteLocations
     *     The data
     */
    public void setData(FavouriteLocations favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }

}
