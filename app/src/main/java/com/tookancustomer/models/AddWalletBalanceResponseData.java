package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWalletBalanceResponseData {

    @SerializedName("wallet_balance_after_txn")
    @Expose
    private double walletBalanceAfterTxn;


    public double getWalletBalanceAfterTxn() {
        return walletBalanceAfterTxn;
    }

    public void setWalletBalanceAfterTxn(double walletBalanceAfterTxn) {
        this.walletBalanceAfterTxn = walletBalanceAfterTxn;
    }
}