package com.tookancustomer.socialLogin.facebook;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class FacebookLoginData implements Serializable {

    public static Parcelable.Creator<FacebookLoginData> creator = new Parcelable.Creator<FacebookLoginData>() {
        @Override
        public FacebookLoginData createFromParcel(Parcel parcel) {
            return new FacebookLoginData(parcel);
        }

        @Override
        public FacebookLoginData[] newArray(int i) {
            return new FacebookLoginData[i];
        }
    };

    private String email;
    private String firstName;
    private String lastName;
    private String picBig;
    private String accessToken;
    private String socialUserID;


    public FacebookLoginData(Parcel source) {
        email = source.readString();
        firstName = source.readString();
        lastName = source.readString();
        picBig = source.readString();
        accessToken = source.readString();
        socialUserID = source.readString();
    }

    public FacebookLoginData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicBig() {
        return picBig;
    }

    public void setPicBig(String picBig) {
        this.picBig = picBig;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSocialUserID() {
        return socialUserID;
    }

    public void setSocialUserID(String socialUserID) {
        this.socialUserID = socialUserID;
    }


}
