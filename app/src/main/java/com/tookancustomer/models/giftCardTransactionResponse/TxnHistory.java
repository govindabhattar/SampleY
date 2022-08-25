package com.tookancustomer.models.giftCardTransactionResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxnHistory {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("marketplace_user_id")
    @Expose
    public int marketplaceUserId;
    @SerializedName("sender_vendor_id")
    @Expose
    public int senderVendorId;
    @SerializedName("sender_name")
    @Expose
    public String senderName;
    @SerializedName("receiver_vendor_id")
    @Expose
    public int receiverVendorId;
    @SerializedName("receiver_name")
    @Expose
    public String receiverName;
    @SerializedName("receiver_email")
    @Expose
    public String receiverEmail;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("payment_method")
    @Expose
    public long paymentMethod;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("is_redeemed")
    @Expose
    public int isRedeemed;
    @SerializedName("creation_datetime")
    @Expose
    public String creationDatetime;
    @SerializedName("updation_datetime")
    @Expose
    public String updationDatetime;

    public int getId() {
        return id;
    }

    public int getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public int getSenderVendorId() {
        return senderVendorId;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getReceiverVendorId() {
        return receiverVendorId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public long getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getIsRedeemed() {
        return isRedeemed;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public String getUpdationDatetime() {
        return updationDatetime;
    }
}
