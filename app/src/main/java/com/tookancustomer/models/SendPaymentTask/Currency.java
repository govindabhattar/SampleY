
package com.tookancustomer.models.SendPaymentTask;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency implements Parcelable {

    @SerializedName("currency_id")
    @Expose
    private Integer currencyId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("is_zero_decimal_currency")
    @Expose
    private String isZeroDecimalCurrency;
    @SerializedName("minimum_amount")
    @Expose
    private String minimumAmount;

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Currency() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.currencyId);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.isZeroDecimalCurrency);
        dest.writeString(this.minimumAmount);
    }

    protected Currency(Parcel in) {
        this.currencyId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.name = in.readString();
        this.symbol = in.readString();
        this.isZeroDecimalCurrency = in.readString();
        this.minimumAmount = in.readString();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}
