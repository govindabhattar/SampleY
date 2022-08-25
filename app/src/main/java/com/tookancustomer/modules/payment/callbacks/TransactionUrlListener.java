package com.tookancustomer.modules.payment.callbacks;

public interface TransactionUrlListener {
    /*In case of payfort, two keys returned transactionId and jobPaymentDetailId
     * else only transactionId is returned only */
    void onTransactionSuccess(String transactionId, String jobPaymentDetailId);

    /* Some payment gateways return transaction id also on failure. */
    void onTransactionFailure(String transactionId);

    void onTransactionApiError();

}
