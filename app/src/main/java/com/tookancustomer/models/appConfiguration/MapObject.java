package com.tookancustomer.models.appConfiguration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.userdata.CancelConfig;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.models.userdata.UserOptions;
import com.tookancustomer.models.userdata.UserRights;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapObject implements Serializable {


    @SerializedName("map_type")
    @Expose
    private int mapType = 0;
    @SerializedName("skip_cache")
    @Expose
    private int skipCache = 0;

    @SerializedName("android_google_api_key")
    @Expose
    private String googleApiKey = "";


    //key for flight map
    @SerializedName("android_map_api_key")
    @Expose
    private String androidMapApiKey = "";

    @SerializedName("google_client_id")
    @Expose
    private String googleClientId = "";
    @SerializedName("google_signature_id")
    @Expose
    private String googleSignatureKey = "";
    @SerializedName("map_plan_type")
    @Expose
    private int mapPlanType = 1;

    public String getGoogleApiKey() {
        return googleApiKey != null ? googleApiKey : "";
    }

    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }

    public String getGoogleClientId() {
        return googleClientId != null ? googleClientId : "";
    }

    public void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public String getGoogleSignatureKey() {
        return googleSignatureKey != null ? googleSignatureKey : "";
    }

    public void setGoogleSignatureKey(String googleSignatureKey) {
        this.googleSignatureKey = googleSignatureKey;
    }

    public int getMapPlanType() {
        return mapPlanType;
    }

    public void setMapPlanType(int mapPlanType) {
        this.mapPlanType = mapPlanType;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getSkipCache() {
        return skipCache;
    }

    public void setSkipCache(int skipCache) {
        this.skipCache = skipCache;
    }

    public String getAndroidMapApiKey() {
        return androidMapApiKey;
    }

    public void setAndroidMapApiKey(String androidMapApiKey) {
        this.androidMapApiKey = androidMapApiKey;
    }
}
