package com.tookancustomer.modules.payment.callbacks;

import com.tookancustomer.models.paymentMethodData.Datum;

import java.util.List;

public interface FetchCardsListener {
    void onFetchCardsSuccess(List<Datum> paymentCardList);

    void onFetchCardsFailure();
}
