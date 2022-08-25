package com.tookancustomer.models.WalletTransactionResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxnHistory {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("wallet_id")
    @Expose
    public int walletId;
    @SerializedName("marketplace_user_id")
    @Expose
    public int marketplaceUserId;
    @SerializedName("transacting_amount")
    @Expose
    public double transactingAmount = 0.0;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("wallet_balance_after_txn")
    @Expose
    public double walletBalanceAfterTxn = 0.0;
    @SerializedName("job_id")
    @Expose
    public int jobId;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("creation_datetime")
    @Expose
    public String creationDatetime;
    @SerializedName("updation_datetime")
    @Expose
    public String updationDatetime;
    @SerializedName("payment_method")
    @Expose
    public String paymentMethod;

    public int getId() {
        return id;
    }

    public int getWalletId() {
        return walletId;
    }

    public int getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public double getTransactingAmount() {
        return transactingAmount;
    }

    public int getType() {
        return type;
    }

    public double getWalletBalanceAfterTxn() {
        return walletBalanceAfterTxn;
    }

    public int getJobId() {
        return jobId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
