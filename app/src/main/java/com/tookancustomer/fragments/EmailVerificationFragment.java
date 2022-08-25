package com.tookancustomer.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tookancustomer.LoginOTPActivity;
import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import java.util.Locale;

public class EmailVerificationFragment extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private TextView tvVerifyEmailHeading, tvVerifyEmailMessage, tvChangeEmail, tvResendEmailLink;
    private Button btnContinue;
    private Dialog changeEmailDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_email_verification, container, false);
        mActivity = getActivity();
        initViews(rootView);

        return rootView;
    }

    public void initViews(View view) {
        tvVerifyEmailHeading = view.findViewById(R.id.tvVerifyEmailHeading);
        tvVerifyEmailHeading.setText(StorefrontCommonData.getString(mActivity, R.string.confirm_your_email_address));
        tvResendEmailLink = view.findViewById(R.id.tvResendEmailLink);
        tvResendEmailLink.setText(StorefrontCommonData.getString(mActivity, R.string.resend_link));
        btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setText(StorefrontCommonData.getString(mActivity, R.string.continu));

        tvChangeEmail = view.findViewById(R.id.tvChangeEmail);
        String email = "";
        if (mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).isOnboardingFromProfile) {
            email = StorefrontCommonData.getUserData().getData().getVendorDetails().getUpdatedEmail();
            tvChangeEmail.setText(StorefrontCommonData.getString(mActivity, R.string.cancel_step_onboarding));
        } else {
            email = StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail();
            tvChangeEmail.setText(StorefrontCommonData.getString(mActivity, R.string.change));
        }
        tvVerifyEmailMessage = view.findViewById(R.id.tvVerifyEmailMessage);
        tvVerifyEmailMessage.setText(StorefrontCommonData.getString(mActivity, R.string.confirmation_mail_sent_message).replace("x@y.z", email));
//        tvVerifyEmailMessage.setText(StorefrontCommonData.getString(mActivity, R.string.verify_email_message).replace("123", email));

        Utils.setOnClickListener(this, tvResendEmailLink, tvChangeEmail, btnContinue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                loginViaAccessToken();
                break;
            case R.id.tvResendEmailLink:
                resendEmailVerificationLink();
                break;
            case R.id.tvChangeEmail:
                if (mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).isOnboardingFromProfile) {
                    StorefrontCommonData.getUserData().getData().getVendorDetails().setIsEmailVerified(1);
                    ((RegistrationOnboardingActivity) mActivity).setCurentStep();

                } else {
                    changeEmailDialog();
                }
                break;
        }
    }

    private void loginViaAccessToken() {
        Location location = LocationUtils.getLastLocation(mActivity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add("latitude", location.getLatitude())
                .add("longitude", location.getLongitude());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        RestClient.getApiInterface(mActivity).loginViaAccessToken(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserData userData = new UserData();
                try {
                    userData.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }
                Utils.saveUserInfo(mActivity, userData, !(mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).isLoginFromCheckout));

                if (mActivity instanceof RegistrationOnboardingActivity) {
                    ((RegistrationOnboardingActivity) mActivity).setCurentStep();
                }
                if (userData.getData().getVendorDetails().getIsEmailVerified() == 0) {
                    Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.email_not_verified));
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    private void resendEmailVerificationLink() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add("language", Locale.getDefault());

        if (mActivity instanceof RegistrationOnboardingActivity && ((RegistrationOnboardingActivity) mActivity).isOnboardingFromProfile) {
            commonParams.add("change_vendor_profile", 1);
            commonParams.add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getUpdatedEmail());
        } else {
            commonParams.add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail());
        }

        RestClient.getApiInterface(mActivity).resendEmailVerificationLink(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                new AlertDialog.Builder(mActivity).message(baseModel.getMessage()).build().show();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    private void changeEmailDialog() {
        try {
            if (changeEmailDialog != null) {
                changeEmailDialog.dismiss();
                changeEmailDialog = null;
            }
            changeEmailDialog = new Dialog(mActivity, R.style.NotificationDialogTheme);
            changeEmailDialog.setContentView(R.layout.custom_dialog_registration_onboarding);

            Window window = changeEmailDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();

            DisplayMetrics metrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            params.gravity = Gravity.BOTTOM;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setAttributes(params);

            changeEmailDialog.setCancelable(true);
            changeEmailDialog.setCanceledOnTouchOutside(false);


//            changeEmailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            changeEmailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//            changeEmailDialog.getWindow().setGravity(Gravity.CENTER);
//
//            params.dimAmount = 0.6f;
//            window.getAttributes().windowAnimations = R.style.CustomDialog;
//            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


            final EditText etCountryCode = changeEmailDialog.findViewById(R.id.etCountryCode);
            etCountryCode.setVisibility(View.GONE);

            final EditText etEmail = changeEmailDialog.findViewById(R.id.etPhoneNumber);
            etEmail.setHint(StorefrontCommonData.getString(mActivity, R.string.enter_email));
            etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

            Button btnCancel = changeEmailDialog.findViewById(R.id.btnCancel);
            btnCancel.setText(StorefrontCommonData.getString(mActivity, R.string.cancel));
            Button btnDone = changeEmailDialog.findViewById(R.id.btnDone);
            btnDone.setText(StorefrontCommonData.getString(mActivity, R.string.submit));

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeEmailDialog.dismiss();
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
                    String email = etEmail.getText().toString().trim();

                    // Check for a valid email address.
                    if (TextUtils.isEmpty(email) || !Utils.isEmailValid(email)) {
                        etEmail.setError(StorefrontCommonData.getString(mActivity, TextUtils.isEmpty(email) ? R.string.email_is_required : R.string.enter_valid_email));
//                        Utils.snackBar(mActivity, getStrings(TextUtils.isEmpty(contact) ? R.string.error_enter_contact : R.string.enter_valid_phone));
                        etEmail.requestFocus();
                    } else {
                        changeEmail(email);
                    }
                }
            });
//            changeEmailDialog.show();

            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            changeEmailDialog.show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }, 200);

        } catch (WindowManager.BadTokenException badTokenExcep) {
//            badTokenExcep.printStackTrace();
        }
    }

    private void changeEmail(final String newEmail) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("email", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail())
                .add("new_email", newEmail)
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());

        RestClient.getApiInterface(mActivity).changeEmailForVerification(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                if (changeEmailDialog != null) changeEmailDialog.dismiss();
                StorefrontCommonData.getUserData().getData().getVendorDetails().setEmail(newEmail);
                tvVerifyEmailMessage.setText(StorefrontCommonData.getString(mActivity, R.string.confirmation_mail_sent_message).replace("x@y.z", StorefrontCommonData.getUserData().getData().getVendorDetails().getEmail()));
                new AlertDialog.Builder(mActivity).message(baseModel.getMessage()).build().show();
            }

            @Override
            public void failure(final APIError error, BaseModel baseModel) {
                if (changeEmailDialog != null) {
                    changeEmailDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Utils.hideSoftKeyboard(mActivity, changeEmailDialog.findViewById(R.id.etPhoneNumber));
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