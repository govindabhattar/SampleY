package com.tookancustomer.mapfiles.placeapi;

import android.os.Parcel;
import android.os.Parcelable;

public class StructuredFormatting implements Parcelable {
    private String main_text;
    private String secondary_text;

    protected StructuredFormatting(Parcel in) {
        main_text = in.readString();
        secondary_text = in.readString();
    }

    public static final Creator<StructuredFormatting> CREATOR = new Creator<StructuredFormatting>() {
        @Override
        public StructuredFormatting createFromParcel(Parcel in) {
            return new StructuredFormatting(in);
        }

        @Override
        public StructuredFormatting[] newArray(int size) {
            return new StructuredFormatting[size];
        }
    };

    public String getMainText() {
        return main_text;
    }

    public String getSecondaryText() {
        return secondary_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(main_text);
        dest.writeString(secondary_text);
    }
}
