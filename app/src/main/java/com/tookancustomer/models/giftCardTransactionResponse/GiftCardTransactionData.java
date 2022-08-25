package com.tookancustomer.models.giftCardTransactionResponse;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftCardTransactionData {

    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("txn_history")
    @Expose
    public List<TxnHistory> txnHistory = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TxnHistory> getTxnHistory() {
        return txnHistory;
    }

    public void setTxnHistory(List<TxnHistory> txnHistory) {
        this.txnHistory = txnHistory;
    }
}
