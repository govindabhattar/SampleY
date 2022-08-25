package com.tookancustomer.mapfiles.placeapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Predictions implements Parcelable {
    private String place_id;
    private String name="";
    private String address;
    private double lat;
    private double lng;

    protected Predictions(Parcel in) {
        place_id = in.readString();
        name = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Predictions> CREATOR = new Creator<Predictions>() {
        @Override
        public Predictions createFromParcel(Parcel in) {
            return new Predictions(in);
        }

        @Override
        public Predictions[] newArray(int size) {
            return new Predictions[size];
        }
    };

    public String getPlaceID() {
        return place_id;
    }

    public String getName() {
        if (name==null)
            return "";
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public Location getLocation() {
        return new Location(String.valueOf(lat), String.valueOf(lng));
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(place_id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
}
