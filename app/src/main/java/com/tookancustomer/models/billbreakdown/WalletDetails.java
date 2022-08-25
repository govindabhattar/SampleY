
package com.tookancustomer.models.billbreakdown;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;

public class WalletDetails implements Serializable {

    @SerializedName("wallet_id")
    @Expose
    private int walletId;
    @SerializedName("wallet_balance")
    @Expose
    public double walletBalance;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getWalletBalanceString() {
        return Utils.getDoubleTwoDigits(walletBalance);
    }
}
