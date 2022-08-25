package com.tookancustomer.models.WalletTransactionResponse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletTransactionData {

    @SerializedName("wallet_id")
    @Expose
    public int walletId;
    @SerializedName("vendor_id")
    @Expose
    public int vendorId;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("marketplace_user_id")
    @Expose
    public int marketplaceUserId;
    @SerializedName("wallet_balance")
    @Expose
    public double walletBalance;
    @SerializedName("creation_datetime")
    @Expose
    public String creationDatetime;
    @SerializedName("updation_datetime")
    @Expose
    public String updationDatetime;
    @SerializedName("txn_history")
    @Expose
    public List<TxnHistory> txnHistory = new ArrayList<>();

    public int getWalletId() {
        return walletId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }

    public List<TxnHistory> getTxnHistory() {
        return txnHistory;
    }

    public int getCount() {
        return count;
    }
}
