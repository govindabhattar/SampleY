package com.tookancustomer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.LoginOtpData;
import com.tookancustomer.models.OTPResponseModel;
import com.tookancustomer.models.userdata.Data;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.Utils;

public class LoginOTPActivity extends BaseActivity implements View.OnClickListener, Constants {
    private EditText etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6;
    private Dialog changeNumberDialog;
    private String accessToken;
    private boolean isLoginFromCheckout;
    private int otp_ID;
    private int isGuestCheckoutFlow = 0;

    private String email = "", phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        isLoginFromCheckout = getIntent().getBooleanExtra(Keys.Extras.IS_LOGIN_FROM_CHECKOUT, false);
        mActivity = this;
        accessToken = Dependencies.getAccessToken(mActivity);

        if (getIntent().hasExtra("isGuestCheckOutFlow") &&
                getIntent().getIntExtra("isGuestCheckOutFlow", 0) == 1) {
            phone = getIntent().getExtras().getString("phone");
            email = getIntent().getExtras().getString("email");
            isGuestCheckoutFlow = 1;

        } else {
            //  email = getIntent().getExtras().getString("email_or_phone");
            if (getIntent().getExtras().getBoolean(IS_PHONE, false)) {
                phone = getIntent().getExtras().getString("email_or_phone");
            } else {
                email = getIntent().getExtras().getString("email_or_phone");
            }
        }


        initData();
        setStrings();
        setFilters();
        fuguNotificationHandle();
        sendOtp(false);
    }

    private void sendOtp(boolean isResend) {
//        https://test-api-3031.yelo.red/customer/send_login_otp


        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        if (getIntent().getExtras().getBoolean(IS_PHONE, false)) {
            commonParams.add("phone", phone);
        } else {
            commonParams.add("email", email);
        }
        if (isResend) {
            commonParams.add("make_otp_invalid", 1);
            commonParams.add("otp_id", otp_ID);

        }

        if (getIntent() != null && getIntent().hasExtra("FROM_SPLASH")) {
            commonParams.add("is_login", 1);
        }


        RestClient.getApiInterface(this).sendLoginOTP(commonParams.build().getMap()).enqueue(resendOTPResponseResolver());

    }

    private void initData() {
        etOTP1 = findViewById(R.id.etOTP1);
        etOTP2 = findViewById(R.id.etOTP2);
        etOTP3 = findViewById(R.id.etOTP3);
        etOTP4 = findViewById(R.id.etOTP4);
        etOTP5 = findViewById(R.id.etOTP5);
        etOTP6 = findViewById(R.id.etOTP6);

        findViewById(R.id.rlBack).setVisibility(View.VISIBLE);
        Utils.setOnClickListener(this, findViewById(R.id.tvResendOTP), findViewById(R.id.rlDoneOTP), findViewById(R.id.rlBack));
    }

    public void setStrings() {
        ((TextView) findViewById(R.id.tvVerifyMobile)).setText(getStrings(R.string.verify_mobile));

        if (getIntent().getExtras().getBoolean(IS_PHONE, false))
            ((TextView) findViewById(R.id.tvOTPMessage)).setText(getStrings(R.string.otp_message_phone));
        else
            ((TextView) findViewById(R.id.tvOTPMessage)).setText(getStrings(R.string.otp_message_email));

        ((TextView) findViewById(R.id.tvVerify)).setText(getStrings(R.string.verify));
        ((TextView) findViewById(R.id.tvChangeNumber)).setText(getStrings(R.string.change_number));
        ((TextView) findViewById(R.id.tvChangeNumber)).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.tvResendOTP)).setText(getStrings(R.string.resend_otp));
    }


    public void setFilters() {
        etOTP1.addTextChangedListener(new GenericTextWatcher(etOTP1));
        etOTP2.addTextChangedListener(new GenericTextWatcher(etOTP2));
        etOTP3.addTextChangedListener(new GenericTextWatcher(etOTP3));
        etOTP4.addTextChangedListener(new GenericTextWatcher(etOTP4));
        etOTP5.addTextChangedListener(new GenericTextWatcher(etOTP5));
        etOTP6.addTextChangedListener(new GenericTextWatcher(etOTP6));

        etOTP1.setOnKeyListener(new GenericKeyListener(etOTP1));
        etOTP2.setOnKeyListener(new GenericKeyListener(etOTP2));
        etOTP3.setOnKeyListener(new GenericKeyListener(etOTP3));
        etOTP4.setOnKeyListener(new GenericKeyListener(etOTP4));
        etOTP5.setOnKeyListener(new GenericKeyListener(etOTP5));
        etOTP6.setOnKeyListener(new GenericKeyListener(etOTP6));
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED_OTP, returnIntent);
        finish();

    }

    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        switch (v.getId()) {
            case R.id.rlDoneOTP:
                validate();
                break;
            case R.id.tvResendOTP:
                sendOtp(true);
                break;
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }

    private void validate() {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(etOTP6.getText().toString())) {
            focusView = etOTP6;
            cancel = true;
        }
        if (TextUtils.isEmpty(etOTP5.getText().toString())) {
            focusView = etOTP5;
            cancel = true;
        }
        if (TextUtils.isEmpty(etOTP4.getText().toString())) {
            focusView = etOTP4;
            cancel = true;
        }
        if (TextUtils.isEmpty(etOTP3.getText().toString())) {
            focusView = etOTP3;
            cancel = true;
        }
        if (TextUtils.isEmpty(etOTP2.getText().toString())) {
            focusView = etOTP2;
            cancel = true;
        }
        if (TextUtils.isEmpty(etOTP1.getText().toString())) {
            focusView = etOTP1;
            cancel = true;
        }

        if (cancel) {
            Utils.snackBar(this, getStrings(R.string.verification_code_field_required));
            focusView.requestFocus();
        } else {
            verifyOTP();
        }
    }

    /**
     * verify OTP api call
     */
    private void verifyOTP() {
        String otp = etOTP1.getText().toString() + etOTP2.getText().toString() + etOTP3.getText().toString() + etOTP4.getText().toString() + etOTP5.getText().toString() + etOTP6.getText().toString();
        UserData userData = StorefrontCommonData.getUserData();
        if (userData != null)
            userData = StorefrontCommonData.getUserData();

//        HashMap<String, String> params = new HashMap<>();
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("otp", otp);
        commonParams.add("login_vendor_via_otp", "1");
//        commonParams.add(MARKETPLACE_USER_ID, userData.getVendorDetails().getMarketplaceUserId() + "");
//        commonParams.add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId() + "");
        if (!phone.isEmpty())
            commonParams.add("phone", phone);
        if (!email.isEmpty())
            commonParams.add("email", email);


        commonParams.remove(YELO_APP_TYPE);
        commonParams.remove(FORM_ID);
        commonParams.remove(USER_ID);

        commonParams.remove(APP_ACCESS_TOKEN);
        commonParams.remove(VENDOR_ID);


        RestClient.getApiInterface(this).verifyOTPLogin(commonParams.build().getMap()).enqueue(verifyOTPResponseResolver());


    }

    /**
     * @return verify OTP Response Resolver
     */
    private ResponseResolver<BaseModel> verifyOTPResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                OTPResponseModel otpResponseModel = new OTPResponseModel();
//                try {
//                    otpResponseModel.setData(baseModel.toResponseModel(OTPResponseModel.Data.class));
//                } catch (Exception e) {
//                    //e.printStackTrace();
//                }

                if (isGuestCheckoutFlow != 1) {
                    Dependencies.saveAccessToken(mActivity, ((LinkedTreeMap) baseModel.data).get("access_token").toString());
                } else {
                    Dependencies.saveAccessTokenGuest(mActivity, ((LinkedTreeMap) baseModel.data).get("access_token").toString());
                }

                Dependencies.saveAccessToken(mActivity, ((LinkedTreeMap) baseModel.data).get("access_token").toString());
                loginViaAccessToken();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }

        };

    }

    private void loginViaAccessToken() {

        Location location = LocationUtils.getLastLocation(this);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("lat", location.getLatitude())
                .add("lng", location.getLongitude());


        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        if (isGuestCheckoutFlow == 1) {
            commonParams.add("access_token", Dependencies.getAccessTokenGuest(mActivity));
        }

        RestClient.getApiInterface(this).loginViaAccessToken(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {


                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_IN_SUCCESS);
                final UserData userData = new UserData();
                try {

                    userData.setData(baseModel.toResponseModel(Data.class));
                    StorefrontCommonData.setUserData(userData);
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

//                if ((UIManager.isOTPAvailable() && userData.getData().getVendorDetails().getIsPhoneVerified() == 0)
//                        || (UIManager.getIsEmailVerificationRequried() == 1 && userData.getData().getVendorDetails().getIsEmailVerified() == 0)
//                        || (userData.getData().getVendorDetails().getRegistrationStatus() != RegistrationStatus.VERIFIED && userData.getData().getSignupTemplateData() != null && !userData.getData().getSignupTemplateData().isEmpty())
//                        || (StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1 && userData.getData().getVendorDetails().getSubscriptionPlan().get(0).getPaid() == 0)
//                        || (StorefrontCommonData.getAppConfigurationData().getIsDebtEnabled() == 1 && userData.getData().getVendorDetails().getDebtAmount() > 0)
//                        || (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionEnabled() == 1 && StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionMandatory() == 1)
//                ) {
                if (MyApplication.getInstance().getDeepLinkMerchantId() != 0) {
                    Intent intent = new Intent(LoginOTPActivity.this, HomeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("MERCHANT_ID", MyApplication.getInstance().getDeepLinkMerchantId());
                    extras.putBoolean("FROM_DEEP_LINK", true);
                    extras.putBoolean("FROM_DEEP_LINK_SIGN_IN", true);
                    intent.putExtras(extras);
                    startActivity(intent);
                    AnimationUtils.forwardTransition(LoginOTPActivity.this);
                    finish();
                } else {
                    if (isGuestCheckoutFlow == 1) {
                        CommonAPIUtils.getSuperCategories(userData, mActivity);
                    } else {
                        CommonAPIUtils.userLoginNavigation(mActivity, isLoginFromCheckout);
                        Dependencies.isAppFirstInstall = false;
                    }
                }

                //  CommonAPIUtils.userLoginNavigation(mActivity, isLoginFromCheckout);
                Dependencies.isAppFirstInstall = false;

//                }


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 900 && error.getMessage().equalsIgnoreCase(getStrings(R.string.socket_timeout_connection))) {
                    new AlertDialog.Builder(mActivity).message(error.getMessage()).button(getStrings(R.string.retry_text)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            loginViaAccessToken();
                        }
                    }).build().show();
                } else {
                    Dependencies.saveAccessToken(mActivity, "");
                }
            }
        });
    }

    /**
     * verify OTP api call
     */

    /**
     * @return resend response resolver
     */
    private ResponseResolver<BaseModel> resendOTPResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {

            @Override
            public void success(BaseModel baseModel) {
                Utils.setCommonText("", etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6);
                etOTP1.requestFocus();


                if (otp_ID != 0)
                    new AlertDialog.Builder(mActivity).message(baseModel.getMessage()).build().show();
                else
                    Utils.showToast(mActivity, baseModel.getMessage());

                LoginOtpData loginOtpData = baseModel.toResponseModel(LoginOtpData.class);
                otp_ID = loginOtpData.getOtpId();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Dependencies.setDemoRun(false);
                    Bundle bundleExtra = getIntent().getExtras();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    return;
                }
                break;
            case OPEN_RESET_PASSWORD:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundleExtra = getIntent().getExtras();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    return;
                }
                break;


        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private EditText editText;

        private GenericTextWatcher(EditText editText) {
            this.editText = editText;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() >= 1) {
                switch (editText.getId()) {
                    case R.id.etOTP1:
                        etOTP2.requestFocus();
                        break;
                    case R.id.etOTP2:
                        etOTP3.requestFocus();
                        break;
                    case R.id.etOTP3:
                        etOTP4.requestFocus();
                        break;
                    case R.id.etOTP4:
                        etOTP5.requestFocus();
                        break;
                    case R.id.etOTP5:
                        etOTP6.requestFocus();
                        break;
                    case R.id.etOTP6:
                        etOTP6.clearFocus();
                        Utils.hideSoftKeyboard(mActivity);
                        validate();
                        break;
                }
            } else if (charSequence.length() == 0) {
                switch (editText.getId()) {
                    case R.id.etOTP1:
                        break;
                    case R.id.etOTP2:
                        etOTP1.requestFocus();
                        break;
                    case R.id.etOTP3:
                        etOTP2.requestFocus();
                        break;
                    case R.id.etOTP4:
                        etOTP3.requestFocus();
                        break;
                    case R.id.etOTP5:
                        etOTP4.requestFocus();
                        break;
                    case R.id.etOTP6:
                        etOTP5.requestFocus();
                        break;
                }
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    private class GenericKeyListener implements View.OnKeyListener {
        private EditText editText;

        private GenericKeyListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (editText.getId()) {
                case R.id.etOTP1:
                    break;
                case R.id.etOTP2:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etOTP2.getText().toString().trim().length() == 0)
                            etOTP1.requestFocus();
                    }
                    break;
                case R.id.etOTP3:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etOTP3.getText().toString().trim().length() == 0)
                            etOTP2.requestFocus();
                    }
                    break;
                case R.id.etOTP4:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etOTP4.getText().toString().trim().length() == 0)
                            etOTP3.requestFocus();
                    }
                    break;
                case R.id.etOTP5:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etOTP5.getText().toString().trim().length() == 0)
                            etOTP4.requestFocus();
                    }
                    break;
                case R.id.etOTP6:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (etOTP6.getText().toString().trim().length() == 0)
                            etOTP5.requestFocus();
                    }
                    break;
            }
            return false;
        }
    }
}
