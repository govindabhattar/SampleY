package com.tookancustomer.modules.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tookancustomer.BuildConfig;
import com.tookancustomer.OTPActivity;
import com.tookancustomer.R;
import com.tookancustomer.WalletAddMoneyActivity;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.WebViewPaymentActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.WalletTransactionResponse.WalletTransactionData;
import com.tookancustomer.models.paymentMethodData.Datum;
import com.tookancustomer.models.paymentMethodData.PaymentMethods;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.payment.activities.AddCardWebViewActivity;
import com.tookancustomer.modules.payment.callbacks.FetchCardsListener;
import com.tookancustomer.modules.payment.callbacks.PaytmBalanceListener;
import com.tookancustomer.modules.payment.callbacks.WalletBalanceListener;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.ResponseResolverWithoutStatusCheck;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.List;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.PAYMENT_METHOD_FIELD;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Extras.BALANCE_TO_BE_ADDED;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.PAYMENT_METHOD_DATA;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.VALUE_PAYMENT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYFORT;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_LATER;

public class PaymentManager {

    /*
     * open add card webView activity in case of stripe and payfort.
     * */
    public static void openAddPaymentCardWebViewActivity(final Activity fromContext, final long paymentMethod) {
        if (!Utils.internetCheck(fromContext)) {
            new AlertDialog.Builder(fromContext).message(StorefrontCommonData.getString(fromContext, R.string.no_internet_try_again)).build().show();
            return;
        }

        if (PaymentMethodsClass.getEnabledPaymentMethod() == PAYFORT.intValue) {
            RestClient.getApiInterface(fromContext).fetchIp("https://api.ipify.org/?format=json")
                    .enqueue(new ResponseResolverWithoutStatusCheck<BaseModel>(fromContext, true, false) {
                        @Override
                        public void success(BaseModel baseModel) {
                            Intent intentPayment = new Intent(fromContext, AddCardWebViewActivity.class);
                            intentPayment.putExtra(URL_WEBVIEW, Dependencies.getAddPaymentURl() + "&customer_ip=" + baseModel.getIp());
                            intentPayment.putExtra(VALUE_PAYMENT, paymentMethod);
                            fromContext.startActivityForResult(intentPayment, Codes.Request.OPEN_ADD_PAYMENT_CARD_ACTIVITY);
                            AnimationUtils.forwardTransition(fromContext);
                        }

                        @Override
                        public void failure(APIError error) {

                        }

                    });
        } else {
            Intent intentPayment = new Intent(fromContext, AddCardWebViewActivity.class); //WebViewActivity
            intentPayment.putExtra(URL_WEBVIEW, Dependencies.getAddPaymentURl());
            intentPayment.putExtra(VALUE_PAYMENT, paymentMethod);
            fromContext.startActivityForResult(intentPayment, Codes.Request.OPEN_ADD_PAYMENT_CARD_ACTIVITY);
            AnimationUtils.forwardTransition(fromContext);
        }
    }


    /*
     * Fetch merchant cards when cards payment method activated e.g. stripe and payfort
     * else no need to hit fetchMerchantCards
     * */
    public static void fetchMerchantCards(Activity mActivity, boolean showLoading, final long valuePaymentMethod, final FetchCardsListener fetchCardsListener) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(PAYMENT_METHOD_FIELD, valuePaymentMethod);
        commonParams.add("address", Prefs.with(mActivity).getString(Keys.Prefs.ADDRESS, ""));
        commonParams.add("zipcode","");

        RestClient.getApiInterface(mActivity).fetchMerchantCards(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, showLoading, true) {
            @Override
            public void success(BaseModel baseModel) {
                PaymentMethods paymentMethodModel = new PaymentMethods();
                try {
                    paymentMethodModel.setData(baseModel.toResponseModel(com.tookancustomer.models.paymentMethodData.Data.class));
                    for (Datum paymentData : paymentMethodModel.getData().getData()) {
                        paymentData.setPaymentMethod(valuePaymentMethod);
                    }
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                StorefrontCommonData.getUserData().getData().setAddCardLink(paymentMethodModel.getData().getAdd_card_link());
                fetchCardsListener.onFetchCardsSuccess(paymentMethodModel.getData().getData());
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                fetchCardsListener.onFetchCardsFailure();
            }
        });
    }

    /*
     * Open add wallet balance activity
     * payableAmount --> refers to amount to be paid from wallet. In case of less amount, add wallet balance screen will be opened with prefilled amount.
     * */
    public static void openAddWalletBalanceActivity(Activity mActivity, double payableAmount) {
        Bundle bundle = new Bundle();

        if (payableAmount > 0) {
            if (payableAmount - Dependencies.getWalletBalance() < 1) {
                bundle.putDouble(BALANCE_TO_BE_ADDED, 1);
            } else {
                bundle.putDouble(BALANCE_TO_BE_ADDED, payableAmount - Dependencies.getWalletBalance());
            }
        }

        Intent intentPayment = new Intent(mActivity, WalletAddMoneyActivity.class);
        intentPayment.putExtras(bundle);
        mActivity.startActivityForResult(intentPayment, Codes.Request.OPEN_WALLET_ADD_MONEY_ACTIVITY);
        AnimationUtils.forwardTransition(mActivity);
    }

    /*
     * Get wallet balance
     * */
    public static void getWalletBalance(Activity mActivity, boolean showLoading, final WalletBalanceListener walletBalanceListener) {

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("need_balance_only", 1); //Need wallet balance key determines if you need only wallet balance or transactions as well

        RestClient.getApiInterface(mActivity)
                .getWalletTxnHistory(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, showLoading, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        WalletTransactionData walletTransactionData = baseModel.toResponseModel(WalletTransactionData.class);
                        Dependencies.setWalletBalance(walletTransactionData.getWalletBalance());

                        if (walletBalanceListener != null)
                            walletBalanceListener.onWalletBalanceSuccess();

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (walletBalanceListener != null)
                            walletBalanceListener.onWalletBalanceFailure();
                    }
                });
    }

    /*
     * Open webview activity for add money in paytm
     * */
    public static void openAddPaytmMoneyWebview(Activity mActivity, Integer isPaytmVerified, String paytmAddMoneyUrl) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
            return;
        }
        if (isPaytmVerified == null) {
            Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.error_paytm_money_load));
            return;
        }
        if (isPaytmVerified == 0) {
            Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.paytm_account_not_linked));
            return;
        }

        Intent intentPayment = new Intent(mActivity, AddCardWebViewActivity.class); //WebViewPaymentActivity

        intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(mActivity, R.string.add_paytm_money));
        intentPayment.putExtra(URL_WEBVIEW, paytmAddMoneyUrl);
        intentPayment.putExtra(VALUE_PAYMENT, PAYTM.intValue);

        mActivity.startActivityForResult(intentPayment, Codes.Request.OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY);
        AnimationUtils.forwardTransition(mActivity);
    }

    /*
     * Get Paytm balance
     * */
    public static void getPaytmBalance(Activity mActivity, boolean showLoading, final PaytmBalanceListener paytmBalanceListener) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(PAYMENT_METHOD_FIELD, PAYTM.intValue)
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("app_access_token", Dependencies.getAccessToken(mActivity))
                .add("dual_user_key", UIManager.isDualUserEnable())
                .add("app_device_type", "ANDROID");

        RestClient.getApiInterface(mActivity)
                .paytmCheckBalance(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, showLoading, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        PaymentMethods paymentMethodModel = new PaymentMethods();
                        paymentMethodModel.setData(baseModel.toResponseModel(com.tookancustomer.models.paymentMethodData.Data.class));

                        if (paytmBalanceListener != null)
                            paytmBalanceListener.onPaytmBalanceSuccess(paymentMethodModel.getData().getPaytm());
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (paytmBalanceListener != null)
                            paytmBalanceListener.onPaytmBalanceFailure();
                    }
                });
    }

    public static void setCustomPaymentMethods(final List<PaymentMethod> paymentMethods) {
        if (UIManager.isMerchantPaymentMethodsEnabled()) {
            PaymentMethodsClass.clearPaymentHashMaps();


            if (paymentMethods != null) {
                UserData userData = StorefrontCommonData.getUserData();

                for (int i = 0; i < paymentMethods.size(); i++) {
                    if (paymentMethods.get(i).getValue() == CASH.intValue &&
                            userData.getData().getVendorDetails().getVendorCashTagEnabled() == 1) {

                        paymentMethods.get(i).setEnabled(1);

                    }

                    if (paymentMethods.get(i).getValue() == PAY_LATER.intValue &&
                            userData.getData().getVendorDetails().getVendorPaylatertagEnabled() == 1) {

                        paymentMethods.get(i).setEnabled(1);

                    }

                }
            }


            StorefrontCommonData.getFormSettings().setPaymentMethodsForStore(paymentMethods);


        }

    }

    /*
     * Request for otp if paytm not linked
     * */
    public static void requestOtpForPaytm(final Activity mActivity) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("app_access_token", Dependencies.getAccessToken(mActivity))
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(mActivity).paytmRequestOTP(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Intent intent = new Intent(mActivity, OTPActivity.class);
                        intent.putExtra(Keys.Extras.OPEN_OTP_FOR_PAYTM, true);
                        mActivity.startActivityForResult(intent, Codes.Request.OPEN_OTP_FOR_PAYTM);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }

}