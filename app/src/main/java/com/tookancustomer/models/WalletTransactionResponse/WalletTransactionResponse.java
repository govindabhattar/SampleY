package com.tookancustomer.models.WalletTransactionResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletTransactionResponse {

    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("data")
    @Expose
    public WalletTransactionData WalletTransactionData;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public com.tookancustomer.models.WalletTransactionResponse.WalletTransactionData getWalletTransactionData() {
        return WalletTransactionData;
    }
}
