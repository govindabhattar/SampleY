
package com.tookancustomer.models.favLocations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavouriteLocations implements Serializable {


    @SerializedName("favLocations")
    @Expose
    private List<Locations> locationses = new ArrayList<>();


    public List<Locations> getFavouriteLocations() {
        return locationses;
    }

    public void setFavouriteLocations(List<Locations> locationses) {
        this.locationses = locationses;
    }


}
