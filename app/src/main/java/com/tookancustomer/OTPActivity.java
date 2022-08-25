package com.tookancustomer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ForgotPasswordVerifyOTPModel;
import com.tookancustomer.models.OTPResponseModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

public class OTPActivity extends BaseActivity implements View.OnClickListener, Constants {
    private EditText etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6;
    private Dialog changeNumberDialog;
    private String accessToken;
    private boolean isLoginFromCheckout, isOTPFromProfile, openOtpForPaytm, openOtpForForgotPassword;
    private String email = "", phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        openOtpForPaytm = getIntent().getBooleanExtra(Keys.Extras.OPEN_OTP_FOR_PAYTM, false);
        isLoginFromCheckout = getIntent().getBooleanExtra(IS_LOGIN_FROM_CHECKOUT, false);
        isOTPFromProfile = getIntent().getBooleanExtra(IS_OTP_FROM_PROFILE, false);
        openOtpForForgotPassword = getIntent().getBooleanExtra(OPEN_OTP_FOR_FORGOT_PASSWORD, false);
        if (getIntent().hasExtra("phone")) {
            phone = getIntent().getStringExtra("phone");
        } else if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        }

        mActivity = this;
        accessToken = Dependencies.getAccessToken(mActivity);
        if (!(openOtpForPaytm || openOtpForForgotPassword))
            Dependencies.saveAccessToken(mActivity, "");
        initData();
        setStrings();
        setFilters();
        fuguNotificationHandle();
    }

    private void initData() {
        etOTP1 = findViewById(R.id.etOTP1);
        etOTP2 = findViewById(R.id.etOTP2);
        etOTP3 = findViewById(R.id.etOTP3);
        etOTP4 = findViewById(R.id.etOTP4);
        etOTP5 = findViewById(R.id.etOTP5);
        etOTP6 = findViewById(R.id.etOTP6);

        findViewById(R.id.rlBack).setVisibility(isOTPFromProfile ? View.GONE : View.VISIBLE);
        if (openOtpForPaytm || openOtpForForgotPassword) {
            findViewById(R.id.tvChangeNumber).setVisibility(View.GONE);
        }

        Utils.setOnClickListener(this, findViewById(R.id.tvResendOTP), findViewById(R.id.tvChangeNumber), findViewById(R.id.rlDoneOTP), findViewById(R.id.rlBack));
    }

    public void setStrings() {
        ((TextView) findViewById(R.id.tvVerifyMobile)).setText(getStrings(R.string.verify_mobile));
        ((TextView) findViewById(R.id.tvOTPMessage)).setText(getStrings(R.string.otp_message));
        ((TextView) findViewById(R.id.tvVerify)).setText(getStrings(R.string.verify));
        ((TextView) findViewById(R.id.tvChangeNumber)).setText(getStrings(R.string.change_number));
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

    @Override
    public void onBackPressed() {
        if (isOTPFromProfile) {
//            Intent intent = new Intent(mActivity, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            ActivityCompat.finishAffinity(mActivity);

        } else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED_OTP, returnIntent);
            finish();
        }
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
                if (openOtpForPaytm) {
                    resendPaytmOTP();
                } else if (openOtpForForgotPassword) {
                    resendOTPForForgotPassword();
                } else {
                    resendOTP();
                }
                break;
            case R.id.tvChangeNumber:
                changeNumberDialog();
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
            if (openOtpForPaytm) {
                paytmVerifyOTP();
            } else if (openOtpForForgotPassword) {
                verifyOTPForForgotPassword();
            } else {
                verifyOTP();
            }
        }
    }

    private void paytmVerifyOTP() {
        String otp = etOTP1.getText().toString() + etOTP2.getText().toString() + etOTP3.getText().toString() + etOTP4.getText().toString() + etOTP5.getText().toString() + etOTP6.getText().toString();
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("otp", otp)
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("app_access_token", accessToken);


        RestClient.getApiInterface(mActivity).paytmLoginWithOTP(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }

    private void resendPaytmOTP() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("app_access_token", accessToken)
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(mActivity).paytmRequestOTP(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });


    }

    private void resendOTPForForgotPassword() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (email.isEmpty()) {
            commonParams.add("phone", phone);
        } else {
            commonParams.add("email", email);
        }

        commonParams.add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId());

        RestClient.getApiInterface(mActivity).forgotPassword(commonParams.build().getMap()).
                enqueue(resendOTPResponseResolver());
    }

    /**
     * verify OTP api call
     */
    private void verifyOTP() {
        String otp = etOTP1.getText().toString() + etOTP2.getText().toString() + etOTP3.getText().toString() + etOTP4.getText().toString() + etOTP5.getText().toString() + etOTP6.getText().toString();
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("otp", otp)
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("access_token", accessToken);


        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(this).verifyOTP(commonParams.build().getMap()).enqueue(verifyOTPResponseResolver());
        } else {
//            commonParams.add("marketplace_storefront_user_id", otp);
            RestClient.getApiInterface(this).dualUserVerifyOTP(commonParams.build().getMap()).enqueue(verifyOTPResponseResolver());
        }

    }

    /**
     * verify OTP api call
     */
    private void verifyOTPForForgotPassword() {
        String otp = etOTP1.getText().toString() + etOTP2.getText().toString() + etOTP3.getText().toString() + etOTP4.getText().toString() + etOTP5.getText().toString() + etOTP6.getText().toString();
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("otp", otp)
                .add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (email.isEmpty()) {
            commonParams.add("phone", phone);
        } else {
            commonParams.add("email", email);
        }

        RestClient.getApiInterface(this).verifyOTPForForgotPassword(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {

                ForgotPasswordVerifyOTPModel verifyOTPModel = baseModel.toResponseModel(ForgotPasswordVerifyOTPModel.class);

                Intent intent = new Intent(mActivity, ResetPasswordActivity.class);
                intent.putExtra("secretToken", verifyOTPModel.getSecretToken());
                startActivityForResult(intent, OPEN_RESET_PASSWORD);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    /**
     * @return verify OTP Response Resolver
     */
    private ResponseResolver<BaseModel> verifyOTPResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                OTPResponseModel otpResponseModel = new OTPResponseModel();
                try {
                    otpResponseModel.setData(baseModel.toResponseModel(OTPResponseModel.Data.class));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                StorefrontCommonData.getUserData().getData().getVendorDetails().setPhoneNo(otpResponseModel.getData().getPhoneNumber());
                StorefrontCommonData.getUserData().getData().getVendorDetails().setIsPhoneVerified(1);
//                StorefrontCommonData.getUserData().getData().getVendorDetails().setRegistrationStatus(3);
                Dependencies.saveAccessToken(mActivity, accessToken);
                Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), !isLoginFromCheckout);

                Bundle extras = new Bundle();
                extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());

                if (isOTPFromProfile) {
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();

                } else {

                    if ((StorefrontCommonData.getUserData().getData().getVendorDetails().getRegistrationStatus() != RegistrationStatus.VERIFIED) && StorefrontCommonData.getUserData().getData().getSignupTemplateData() != null && !StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty()) {
//                        Intent intent = new Intent(getApplicationContext(), SignupCustomFieldsActivity.class);
                        Intent intent = new Intent(getApplicationContext(), RegistrationOnboardingActivity.class);
                        extras.putBoolean(IS_LOGIN_FROM_CHECKOUT, isLoginFromCheckout);

                        if (!isLoginFromCheckout) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityCompat.finishAffinity(mActivity);
                        }

                        intent.putExtras(extras);
                        startActivityForResult(intent, OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY);
                        AnimationUtils.forwardTransition(mActivity);
                    } else if (isLoginFromCheckout) {
                        Bundle bundleExtra = getIntent().getExtras();
                        bundleExtra.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                        Intent returnIntent = new Intent();
                        returnIntent.putExtras(bundleExtra);
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } else {
                        CommonAPIUtils.getSuperCategories(StorefrontCommonData.getUserData(), mActivity);

                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        };

    }

    /**
     * resend OTP api call
     */
    private void resendOTP() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("access_token", accessToken)
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(this).resendOTP(commonParams.build().getMap()).enqueue(resendOTPResponseResolver());
        } else {
            RestClient.getApiInterface(this).dualUserResendOTP(commonParams.build().getMap()).enqueue(resendOTPResponseResolver());
        }
    }

    /**
     * @return resend response resolver
     */
    private ResponseResolver<BaseModel> resendOTPResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {

            @Override
            public void success(BaseModel baseModel) {
                Utils.setCommonText("", etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6);
                etOTP1.requestFocus();
                if (openOtpForForgotPassword) {
                    Utils.snackbarSuccess(mActivity, baseModel.getMessage());
                } else {
                    new AlertDialog.Builder(mActivity).message(baseModel.getMessage()).build().show();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        };
    }

    private void changeNumberDialog() {
        try {
            if (changeNumberDialog != null) {
                changeNumberDialog.dismiss();
                changeNumberDialog = null;
            }
            changeNumberDialog = new Dialog(mActivity, R.style.NotificationDialogTheme);
            changeNumberDialog.setContentView(R.layout.custom_dialog_view_change_number);

            Window window = changeNumberDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            DisplayMetrics metrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            params.gravity = Gravity.BOTTOM;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setAttributes(params);

            changeNumberDialog.setCancelable(true);
            changeNumberDialog.setCanceledOnTouchOutside(false);


//            changeNumberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            changeNumberDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//            changeNumberDialog.getWindow().setGravity(Gravity.CENTER);
//
//            params.dimAmount = 0.6f;
//            window.getAttributes().windowAnimations = R.style.CustomDialog;
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


            final EditText etCountryCode = changeNumberDialog.findViewById(R.id.etCountryCode);
            final EditText etPhoneNumber = changeNumberDialog.findViewById(R.id.etPhoneNumber);
            etPhoneNumber.setHint(getStrings(R.string.enter_phone_number));
            Button btnCancel = changeNumberDialog.findViewById(R.id.btnCancel);
            btnCancel.setText(getStrings(R.string.cancel));
            Button btnDone = changeNumberDialog.findViewById(R.id.btnDone);
            btnDone.setText(getStrings(R.string.submit));

            etCountryCode.setText(Utils.getDefaultCountryCode(this));

            etCountryCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideSoftKeyboard(mActivity);

                    CountrySelectionDailog.getInstance(OTPActivity.this, new CountryPickerAdapter.OnCountrySelectedListener() {
                        @Override
                        public void onCountrySelected(Country country) {
                            etCountryCode.setText(country.getCountryCode());
                            CountrySelectionDailog.dismissDialog();
                        }
                    }).show();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeNumberDialog.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utils.hideSoftKeyboard(mActivity);
                        }
                    }, 100);
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String contact = etCountryCode.getText().toString() + " " + etPhoneNumber.getText().toString();
                    String phoneNumber = etPhoneNumber.getText().toString();

                    boolean cancel = false;
                    View focusView = null;

                    // Check for a valid email address.
                    if (TextUtils.isEmpty(phoneNumber) || !Utils.isValidPhoneNumber(phoneNumber)) {
                        etPhoneNumber.setError(getStrings(TextUtils.isEmpty(phoneNumber) ? R.string.error_enter_contact : R.string.enter_valid_phone));
//                        Utils.snackBar(mActivity, getStrings(TextUtils.isEmpty(contact) ? R.string.error_enter_contact : R.string.enter_valid_phone));
                        focusView = etPhoneNumber;
                        cancel = true;
                    }

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {
                        changeNumber(contact);
                    }
                }
            });
//            changeNumberDialog.show();

            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            changeNumberDialog.show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, 200);

        } catch (WindowManager.BadTokenException badTokenExcep) {
            //badTokenExcep.printStackTrace();
        }
    }

    private void changeNumber(final String contact) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("access_token", accessToken)
                .add("phone_no", contact)
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(this).changePhoneNumber(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {

                if (changeNumberDialog != null) changeNumberDialog.dismiss();
                StorefrontCommonData.getUserData().getData().getVendorDetails().setPhoneNo(contact);
                Utils.setCommonText("", etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6);
                etOTP1.requestFocus();
                new AlertDialog.Builder(mActivity).message(baseModel.getMessage()).build().show();
            }

            @Override
            public void failure(final APIError error, BaseModel baseModel) {
                if (changeNumberDialog != null) {
                    changeNumberDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    Utils.hideSoftKeyboard(mActivity, changeNumberDialog.findViewById(R.id.etPhoneNumber));
                } else {
                    Utils.hideSoftKeyboard(mActivity);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(mActivity).message(error.getMessage()).build().show();

                    }
                }, 100);
            }
        });
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
}