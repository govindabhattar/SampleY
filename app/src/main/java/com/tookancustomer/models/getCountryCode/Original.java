
package com.tookancustomer.models.getCountryCode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Original {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("continent")
    @Expose
    private String continent;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("subdivision")
    @Expose
    private String subdivision;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

}
