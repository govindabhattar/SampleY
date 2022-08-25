package com.tookancustomer.payulatam;

import android.app.Activity;
import android.content.Intent;

import com.tookancustomer.R;
import com.tookancustomer.WebViewActivityPayuLatum;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.PaypalModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Utils;

import java.util.HashMap;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_VERSION;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DUAL_USER_KEY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FORM_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_DEMO_APP;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_REF_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.REFERENCE_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.YELO_APP_TYPE;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;
import static com.tookancustomer.appdata.Keys.MetaDataKeys.PAYMENT_METHOD;
import static com.tookancustomer.appdata.Keys.Prefs.DEVICE_TOKEN;

public class PayulatamPaymentManager {

    private Activity mActivity;
    private HashMap<String, String> map;
    private Payulatam.PayulatamListener listener;
    private static PayulatamPaymentManager manager;

    public static PayulatamPaymentManager getInstance(Activity mActivity, HashMap<String, String> map, Payulatam.PayulatamListener listener) {
        if (manager == null) {
            manager = new PayulatamPaymentManager(mActivity, map, listener);
        } else {
            manager.mActivity = mActivity;
            manager.map = map;
            manager.listener = listener;
        }

        return manager;
    }

    public static PayulatamPaymentManager getInstance() {
        return manager;
    }


    public static HashMap<String, String> getMap(int orderId, Activity mActivity, String payableAmount) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("amount", payableAmount)
                .add("payment_method", PaymentMethodsClass.getEnabledPaymentMethod())
                .add("currency", Utils.getCurrencyCodeNew())
                .add("job_id", orderId)
                .add("customer_name", StorefrontCommonData.getUserData().getData().getVendorDetails().getFirstName() + " "
                        + StorefrontCommonData.getUserData().getData().getVendorDetails().getLastName()).add("customer_email",
                StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());

        commonParams.build().getMap().remove(APP_ACCESS_TOKEN);
        commonParams.build().getMap().remove(DUAL_USER_KEY);
        commonParams.build().getMap().remove(PAYMENT_METHOD);
        commonParams.build().getMap().remove(APP_VERSION);
        commonParams.build().getMap().remove(REFERENCE_ID);
        commonParams.build().getMap().remove(MARKETPLACE_REF_ID);
        commonParams.build().getMap().remove(FORM_ID);
        commonParams.build().getMap().remove(YELO_APP_TYPE);
        commonParams.build().getMap().remove(DEVICE_TOKEN);
        commonParams.build().getMap().remove(IS_DEMO_APP);

        return commonParams.build().getMap();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK && data.hasExtra("transactionId")) {
            if (listener != null)
                listener.onPayulatamSuccess(data.getStringExtra("transactionId"));
        }
    }

    private PayulatamPaymentManager(Activity mActivity, HashMap<String, String> map, Payulatam.PayulatamListener listener) {
        this.mActivity = mActivity;
        this.map = map;
        this.listener = listener;
    }

    public void executePayment() {
        RestClient.getApiInterface(mActivity).initiatePayulatam(map)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {

                        PaypalModel paypalModel = baseModel.toResponseModel(PaypalModel.class);
                        Intent intentPayment = new Intent(mActivity, WebViewActivityPayuLatum.class);
                        intentPayment.putExtra(URL_WEBVIEW, paypalModel.getUrl());
                        intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(mActivity, R.string.processing_payment));
                        mActivity.startActivityForResult(intentPayment, Codes.Request.OPEN_PAYULATAM_WEBVIEW_ACTIVITY);
                        AnimationUtils.forwardTransition(mActivity);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        listener.onPayulatamFailure();
                    }
                });
    }
}
