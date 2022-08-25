package com.tookancustomer.modules.payment.callbacks;

public interface WalletBalanceListener {
    void onWalletBalanceSuccess();

    void onWalletBalanceFailure();
}
