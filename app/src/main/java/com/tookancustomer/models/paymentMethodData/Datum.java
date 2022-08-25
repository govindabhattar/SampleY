
package com.tookancustomer.models.paymentMethodData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable {

    public boolean selectedCard = false;
    @SerializedName("payment_method")
    @Expose
    private long paymentMethod;
    @SerializedName("last4_digits")
    @Expose
    private String last4DigitsIntegerValue;

    private String last4Digits;


    private int paymentFlowType;

    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("funding")
    @Expose
    private String funding;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("card_id")
    @Expose
    private Long cardId;

    public long getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(long paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getLast4Digits() {
        try {
            return last4Digits != null ? last4Digits : last4DigitsIntegerValue;
        } catch (Exception e) {
            return "";
        }
    }

    public String getBrand() {
        return brand != null ? brand : "";
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public int getPaymentFlowType() {
        //1 for add card
        //0 for webview
        //2 for paytm
        return paymentFlowType;
    }

    public void setPaymentFlowType(int paymentFlowType) {
        this.paymentFlowType = paymentFlowType;
    }


    public String getCardId() {
        return cardId != null ? cardId + "" : "";
    }


    public Datum(long paymentMethod, String last4Digits, long cardId, int paymentFlowType) {
        this.paymentMethod = paymentMethod;
        this.last4Digits = last4Digits;
        this.cardId = cardId;
        this.paymentFlowType = paymentFlowType;
    }

    public Datum() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.selectedCard ? (byte) 1 : (byte) 0);
        dest.writeValue(this.paymentMethod);
        dest.writeValue(this.last4DigitsIntegerValue);
        dest.writeString(this.last4Digits);
        dest.writeString(this.brand);
        dest.writeString(this.funding);
        dest.writeString(this.expiryDate);
        dest.writeValue(this.cardId);
        dest.writeValue(this.paymentFlowType);
    }

    protected Datum(Parcel in) {
        this.selectedCard = in.readByte() != 0;
        this.paymentMethod = (long) in.readValue(Integer.class.getClassLoader());
        this.last4DigitsIntegerValue = (String) in.readValue(String.class.getClassLoader());
        this.last4Digits = in.readString();
        this.brand = in.readString();
        this.funding = in.readString();
        this.expiryDate = in.readString();
        this.cardId = (Long) in.readValue(Long.class.getClassLoader());
        this.paymentFlowType = (int) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<Datum> CREATOR = new Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}
