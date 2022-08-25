package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.fcm.FCMMessagingService;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userDebt.UserDebtData;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDebtActivity extends BaseActivity implements View.OnClickListener, Keys.Extras {

    private TextView tvHeading, tvPendingAmount, tvPendingAmountSubHeading, tvAmountSettlementMessage;
    private Button btnPay;
    private TextView tvSkip;
    public boolean isLoginFromCheckout;
    public boolean fromPush;
    private String pushID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_debt);
        mActivity = this;
        isLoginFromCheckout = getIntent().getBooleanExtra(IS_LOGIN_FROM_CHECKOUT, false);
        fromPush = getIntent().hasExtra("pushID");
        pushID = getIntent().getExtras().getString("pushID", "");

        initViews();
        getPaymentMethods();


    }

    private void initViews() {
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.outstanding_amount));

        tvPendingAmount = findViewById(R.id.tvPendingAmount);
        tvPendingAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount())));

        tvPendingAmountSubHeading = findViewById(R.id.tvPendingAmountSubHeading);
        tvPendingAmountSubHeading.setText(getStrings(R.string.amount_pending_last_order));
        tvAmountSettlementMessage = findViewById(R.id.tvAmountSettlementMessage);
        tvAmountSettlementMessage.setText(getStrings(R.string.settle_amount_to_continue));
        btnPay = findViewById(R.id.btnPay);
        btnPay.setText(getStrings(R.string.continue_to_pay));
        tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setText(getStrings(R.string.skip));

        findViewById(R.id.rlBack).setVisibility(View.GONE);
        findViewById(R.id.vwShadow).setVisibility(View.GONE);

        findViewById(R.id.tvSkip).setVisibility(StorefrontCommonData.getAppConfigurationData().getIsDebtPaymentCompulsory() == 1 ? View.GONE : View.VISIBLE);
        findViewById(R.id.tvAmountSettlementMessage).setVisibility(StorefrontCommonData.getAppConfigurationData().getIsDebtPaymentCompulsory() == 1 ? View.VISIBLE : View.GONE);

        Utils.setOnClickListener(this, btnPay, tvSkip);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPendingAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPay:
                if (PaymentMethodsClass.getEnabledPaymentMethod() == PaymentConstants.PaymentValue.CASH.intValue
                        || PaymentMethodsClass.getEnabledPaymentMethod() == PaymentConstants.PaymentValue.PAY_LATER.intValue
                        || PaymentMethodsClass.getEnabledPaymentMethod() == PaymentConstants.PaymentValue.PAYTM_LINK.intValue
                        || PaymentMethodsClass.getEnabledPaymentMethod() == PaymentConstants.PaymentValue.PAY_ON_DELIVERY.intValue) {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.contact_to_admin_to_clear_debt)).build().show();
                } else {
                    getJobIds(false);
                }
                break;
            case R.id.tvSkip:
                navigateBack();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_PAYMENT_ACTIVITY_USER_DEBT:
                if (resultCode == RESULT_OK) {

                    if (data.hasExtra("debtAmount") && data.getDoubleExtra("debtAmount", 0) <= 0) {
                        StorefrontCommonData.getUserData().getData().getVendorDetails().setDebtAmount(0);
                        navigateBack();
                    } else {
                        initViews();
                    }


                } else if (resultCode == RESULT_ERROR) {
                    Utils.snackBar(mActivity, getStrings(R.string.debt_transaction_failed));
                }
                break;
        }
    }


    private void getJobIds(final boolean isInitialHit) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity))
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(mActivity).getDebtList(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserDebtData userDebtData = baseModel.toResponseModel(UserDebtData.class);
                StorefrontCommonData.getUserData().getData().getVendorDetails().setDebtAmount(userDebtData.getDebtAmount());

                if (isInitialHit) {

                    initViews();
                } else {
                    if (userDebtData.getDebtAmount() > 0) {

                        Intent intentRePayment = new Intent(mActivity, MakePaymentActivity.class);
                        intentRePayment.putExtra("paymentForFlow", PaymentConstants.PaymentForFlow.DEBT.intValue);
                        intentRePayment.putExtra("userDebtData", userDebtData);
                        startActivityForResult(intentRePayment, OPEN_PAYMENT_ACTIVITY_USER_DEBT);
                        AnimationUtils.forwardTransition(mActivity);
                    } else {
                        navigateBack();
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void navigateBack() {
        if (isLoginFromCheckout) {
            Bundle bundleExtra = getIntent().getExtras();
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            CommonAPIUtils.getSuperCategories(StorefrontCommonData.getUserData(), mActivity, true);
        }
    }

    private void readNotifications() {
        if (pushID.isEmpty()) {
            return;
        }
        try {
            if (FCMMessagingService.mNotificationManager != null) {
                FCMMessagingService.mNotificationManager.cancel(Integer.parseInt(pushID));
                FCMMessagingService.clearNotification();
            }
        } catch (Exception e) {
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("push_id", pushID);

        RestClient.getApiInterface(this).updateAppNotifications(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    private void getPaymentMethods() {

        HashMap<String, String> paymenthashMap = new HashMap<>();

        paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        paymenthashMap.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
        paymenthashMap.put(LANGUAGE, StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());

        RestClient.getApiInterface(mActivity).getPaymentMethods(paymenthashMap).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Log.e("payment......", "payment");


                ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
                paymentMethods = new Gson().fromJson(new Gson().toJson(baseModel.data), new TypeToken<ArrayList<PaymentMethod>>() {
                }.getType());
                Log.e("payment......", "payment");
                PaymentMethodsClass.clearPaymentHashMaps();
                StorefrontCommonData.getFormSettings().setPaymentMethods(paymentMethods);


                if (fromPush) {
                    getJobIds(true);
                    readNotifications();
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

}
