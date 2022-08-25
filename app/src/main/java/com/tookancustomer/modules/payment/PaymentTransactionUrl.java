package com.tookancustomer.modules.payment;

import android.app.Activity;

import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.modules.payment.callbacks.TransactionUrlListener;

import java.util.HashMap;

public class PaymentTransactionUrl {

    private PaymentTransactionUrl(PaymentTransactionBuilder builder) {
        if (builder.createTaskData != null) {
            PaymentTransactionUrlManager.getInstance(builder.mActivity,
                    builder.paymentMethod,
                    builder.paymentForFlow,
                    builder.userId,
                    builder.isLockEnabled,
                    builder.map,
                    builder.listener, builder.isOrderPayment,
                    builder.paymentMethodData,
                    builder.createTaskData, builder.additionalpaymentId, builder.jobId, builder.storeName).getPaymentUrl();
        } else {
            PaymentTransactionUrlManager.getInstance(builder.mActivity,
                    builder.paymentMethod,
                    builder.paymentForFlow,
                    builder.userId,
                    builder.isLockEnabled,
                    builder.map,
                    builder.listener, builder.isOrderPayment,
                    builder.paymentMethodData, builder.additionalpaymentId, builder.jobId, builder.storeName).getPaymentUrl();
        }
    }

    public static class PaymentTransactionBuilder {
        private Activity mActivity;
        private long paymentMethod;
        /*Payment for value refers to flow for payment
         * payment_for = 0 for payment screen
         * payment_for = 1 for signup fee
         * payment_for = 2 for wallet
         * payment_for = 3 for gift card
         * payment_for = 4 for Repayment from task details
         * */
        private long paymentForFlow;
        private int userId;
        private int isLockEnabled;
        private HashMap<String, String> map;
        private TransactionUrlListener listener;
        private boolean isOrderPayment = true;
        private Datum paymentMethodData;
        private HashMap<String, String> createTaskData;
        private String additionalpaymentId = "";
        private int jobId;
        private String storeName = "";

        public PaymentTransactionBuilder(Activity mActivity) {
            this.mActivity = mActivity;
        }

        public PaymentTransactionBuilder setPaymentMethod(long paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PaymentTransactionBuilder setPaymentForFlow(long paymentForFlow) {
            this.paymentForFlow = paymentForFlow;
            return this;
        }


        public PaymentTransactionBuilder setadditionalpaymentId(String additionalpaymentId) {
            this.additionalpaymentId = additionalpaymentId;
            return this;
        }

        public PaymentTransactionBuilder setMap(HashMap<String, String> map) {
            this.map = map;
            return this;
        }

        public PaymentTransactionBuilder setListener(TransactionUrlListener listener) {
            this.listener = listener;
            return this;
        }

        public PaymentTransactionBuilder setIsorderPayment(boolean isOrderPayment) {
            this.isOrderPayment = isOrderPayment;
            return this;
        }

        public PaymentTransactionUrl build() {
            return new PaymentTransactionUrl(this);
        }

        public PaymentTransactionBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public PaymentTransactionBuilder setJobId(int jobId) {
            this.jobId = jobId;
            return this;
        }

        public PaymentTransactionBuilder setPaymentData(Datum paymentMethodData) {
            this.paymentMethodData = paymentMethodData;
            return this;
        }

        public PaymentTransactionBuilder setCreateTaskData(HashMap<String, String> createTaskData) {
            this.createTaskData = createTaskData;
            return this;
        }

        public PaymentTransactionBuilder setIsLockEnabled(int isLockEnabled) {
            this.isLockEnabled = isLockEnabled;
            return this;
        }

        public PaymentTransactionBuilder setStoreName(String storeName) {
            this.storeName = storeName;
            return this;
        }

    }

}