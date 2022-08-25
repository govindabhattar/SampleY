package com.tookancustomer.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.OTPResponseModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

public class OTPFragment extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private EditText etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6;
    private TextView tvOTPMessage;
    private Dialog changeNumberDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_otp, container, false);
        mActivity = getActivity();
        initViews(rootView);
        setFilters();

        return rootView;
    }

    public void initViews(View v) {
        etOTP1 = v.findViewById(R.id.etOTP1);
        etOTP2 = v.findViewById(R.id.etOTP2);
        etOTP3 = v.findViewById(R.id.etOTP3);
        etOTP4 = v.findViewById(R.id.etOTP4);
        etOTP5 = v.findViewById(R.id.etOTP5);
        etOTP6 = v.findViewById(R.id.etOTP6);

        tvOTPMessage = v.findViewById(R.id.tvOTPMessage);
        String phoneNumber = "";
        if (mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).isOnboardingFromProfile) {
            phoneNumber = StorefrontCommonData.getUserData().getData().getVendorDetails().getUpdatedPhoneNo();
            ((TextView) v.findViewById(R.id.tvChangeNumber)).setText(StorefrontCommonData.getString(mActivity, R.string.cancel_step_onboarding));
        } else {
            phoneNumber = StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo();
            ((TextView) v.findViewById(R.id.tvChangeNumber)).setText(StorefrontCommonData.getString(mActivity, R.string.change_number));
        }
        tvOTPMessage.setText(StorefrontCommonData.getString(mActivity, R.string.verify_phone_message).replace("123", phoneNumber));

        ((TextView) v.findViewById(R.id.tvVerifyMobile)).setText(StorefrontCommonData.getString(mActivity, R.string.verify_mobile));
        ((TextView) v.findViewById(R.id.tvVerify)).setText(StorefrontCommonData.getString(mActivity, R.string.verify));
        ((TextView) v.findViewById(R.id.tvResendOTP)).setText(StorefrontCommonData.getString(mActivity, R.string.resend_otp));

        Utils.setOnClickListener(this, v.findViewById(R.id.tvResendOTP), v.findViewById(R.id.tvChangeNumber), v.findViewById(R.id.rlDoneOTP));
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
    public void onClick(View v) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
            return;
        }
        switch (v.getId()) {
            case R.id.rlDoneOTP:
                validate();
                break;
            case R.id.tvResendOTP:
                resendOTP();
                break;
            case R.id.tvChangeNumber:
                if (mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).isOnboardingFromProfile) {
                    StorefrontCommonData.getUserData().getData().getVendorDetails().setIsPhoneVerified(1);
                    ((RegistrationOnboardingActivity) mActivity).setCurentStep();

                } else {
                    changeNumberDialog();
                }
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
            Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.verification_code_field_required));
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
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("otp", otp)
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                .add("access_token", Dependencies.getAccessToken(mActivity));

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(mActivity).verifyOTP(commonParams.build().getMap()).enqueue(verifyOTPResponseResolver());
        } else {
//            commonParams.add("marketplace_storefront_user_id", otp);
            RestClient.getApiInterface(mActivity).dualUserVerifyOTP(commonParams.build().getMap()).enqueue(verifyOTPResponseResolver());
        }
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

                if (mActivity instanceof RegistrationOnboardingActivity) {
                    ((RegistrationOnboardingActivity) mActivity).setCurentStep();
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
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity))
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(mActivity).resendOTP(commonParams.build().getMap()).enqueue(resendOTPResponseResolver());
        } else {
            RestClient.getApiInterface(mActivity).dualUserResendOTP(commonParams.build().getMap()).enqueue(resendOTPResponseResolver());
        }
    }

    /**
     * @return resend response resolver
     */
    private ResponseResolver<BaseModel> resendOTPResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {

            @Override
            public void success(BaseModel userData) {
                Utils.setCommonText("", etOTP1, etOTP2, etOTP3, etOTP4, etOTP5, etOTP6);
                etOTP1.requestFocus();
                new AlertDialog.Builder(mActivity).message(userData.getMessage()).build().show();
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
            etPhoneNumber.setHint(StorefrontCommonData.getString(mActivity, R.string.enter_phone_number));
            etPhoneNumber.setKeyListener(DigitsKeyListener.getInstance());
            Button btnCancel = changeNumberDialog.findViewById(R.id.btnCancel);
            btnCancel.setText(StorefrontCommonData.getString(mActivity, R.string.cancel));
            Button btnDone = changeNumberDialog.findViewById(R.id.btnDone);
            btnDone.setText(StorefrontCommonData.getString(mActivity, R.string.submit));

            etCountryCode.setText(Utils.getDefaultCountryCode(mActivity));

            etCountryCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideSoftKeyboard(mActivity);
                    CountrySelectionDailog.getInstance(mActivity, new CountryPickerAdapter.OnCountrySelectedListener() {
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
                        etPhoneNumber.setError(StorefrontCommonData.getString(mActivity, TextUtils.isEmpty(phoneNumber) ? R.string.error_enter_contact : R.string.enter_valid_phone));
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
//            badTokenExcep.printStackTrace();
        }
    }

    private void changeNumber(final String contact) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity))
                .add("phone_no", contact)
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        RestClient.getApiInterface(mActivity).changePhoneNumber(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                if (changeNumberDialog != null) changeNumberDialog.dismiss();
                StorefrontCommonData.getUserData().getData().getVendorDetails().setPhoneNo(contact);
                tvOTPMessage.setText(StorefrontCommonData.getString(mActivity, R.string.verify_phone_message).replace("123", StorefrontCommonData.getUserData().getData().getVendorDetails().getPhoneNo()));
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
}