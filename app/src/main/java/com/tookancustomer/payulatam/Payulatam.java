package com.tookancustomer.payulatam;

import android.app.Activity;

import java.util.HashMap;

public class Payulatam {

    private Payulatam(PayulatamBuilder builder) {
        PayulatamPaymentManager.getInstance(builder.mActivity,
                builder.map,
                builder.listener).executePayment();
    }


    public static class PayulatamBuilder {
        private Activity mActivity;
        private HashMap<String, String> map;
        private PayulatamListener listener;

        public PayulatamBuilder(Activity mActivity) {
            this.mActivity = mActivity;
        }

        public PayulatamBuilder setMap(HashMap<String, String> map) {
            this.map = map;
            return this;
        }

        public PayulatamBuilder setListener(PayulatamListener listener) {
            this.listener = listener;
            return this;
        }


        public Payulatam build() {
            return new Payulatam(this);
        }

    }

    public interface PayulatamListener {
        void onPayulatamSuccess(String payuTransactionId);

        void onPayulatamFailure();
    }

}
