package com.tookancustomer.modules.payment.callbacks;

import com.tookancustomer.models.paymentMethodData.PaytmData;

public interface PaytmBalanceListener {
    void onPaytmBalanceSuccess(PaytmData paytmData);

    void onPaytmBalanceFailure();
}
