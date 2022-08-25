package com.tookancustomer.mapfiles.placeapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.retrofit2.BaseResponse;
import com.tookancustomer.retrofit2.CommonResponse_;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceDetails extends CommonResponse_ {

    @SerializedName("data")
    @Expose
    private PlacesData data;

//    public <T> T toResponseModel(Class<T> classRef) {
//        return new Gson().fromJson(new Gson().toJson(data), classRef);
//    }
//
//    public JSONObject getJson() {
//        try {
//            return new JSONObject(new Gson().toJson(data));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String getData() {
        return data.toString();
    }

    public PlacesData getDataObject() {
        return data;
    }


//    private Result results;
//
//    protected PlaceDetails(Parcel in) {
//        results = in.readParcelable(Result.class.getClassLoader());
//    }
//
//    public static final Creator<PlaceDetails> CREATOR = new Creator<PlaceDetails>() {
//        @Override
//        public PlaceDetails createFromParcel(Parcel in) {
//            return new PlaceDetails(in);
//        }
//
//        @Override
//        public PlaceDetails[] newArray(int size) {
//            return new PlaceDetails[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(results, flags);
//    }
//
//    public Result getResult() {
//        return results;
//    }
}
