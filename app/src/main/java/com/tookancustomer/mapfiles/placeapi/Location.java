package com.tookancustomer.mapfiles.placeapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//public class Location implements Parcelable {


public class Location implements Parcelable {

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("add")
    @Expose

    private String add;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    protected Location(Parcel in) {
        lat = in.readString();
        lng = in.readString();
        add = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location(String lat, String lng, String add) {
        this.lat = lat;
        this.lng = lng;
        this.add = add;
    }

    public String getAdd() {
        return add;
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(add);
    }
}


//    private String lat;
//    private String lng;
//    private String add;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
//
//    public Location(String lat, String lng) {
//        this.lat = lat;
//        this.lng = lng;
//    }
//
//    public Location(String lat, String lng, String add) {
//        this.lat = lat;
//        this.lng = lng;
//        this.add = add;
//    }
//
//    protected Location(Parcel in) {
//        lat = in.readString();
//        lng = in.readString();
//        add = in.readString();
//    }
//
//    public static final Creator<Location> CREATOR = new Creator<Location>() {
//        @Override
//        public Location createFromParcel(Parcel in) {
//            return new Location(in);
//        }
//
//        @Override
//        public Location[] newArray(int size) {
//            return new Location[size];
//        }
//    };
//
//    /**
//     * @return The add
//     */
//    public String getAdd() {
//        return add;
//    }
//
//    public LatLng getLatLng() {
//        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(lat);
//        parcel.writeString(lng);
//        parcel.writeString(add);
//    }
//}add
