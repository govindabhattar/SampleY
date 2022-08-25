package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.Data;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.FilterUtils;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

import java.util.Objects;

public class GuestLoginActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener, Constants {
    private final int ivBack = R.id.ivBack;

    private final int iCountryCode = R.id.rlCountryCode;
    private ValidateClass validateClass;
    private Button btnContinue;
    private TextView tvCountryCode, tvContinueAsGuest, tvContinueAsGuestHeader;
    private MaterialEditText mEmailView;
    private View vErrorIcon;
    private View vContactErrorIcon;
    private MaterialEditText mContactView;
    private int isGuestCheckOutFlow = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
        init();

//        ((TextView) findViewById(R.id.tvUserEmailabel)).setText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), "*"));


    }

    private void init() {
        mActivity = this;
        validateClass = new ValidateClass(mActivity);
        vErrorIcon = findViewById(R.id.vErrorIcon);
        vContactErrorIcon = findViewById(R.id.vContactErrorIcon);
        tvContinueAsGuest = findViewById(R.id.tvContinueAsGuest);
        tvContinueAsGuestHeader = findViewById(R.id.tvContinueAsGuestHeader);
        //  btnContinue = findViewById(R.id.btnContinue);

        tvContinueAsGuest.setText(StorefrontCommonData.getString(mActivity, R.string.text_guest_checkout));
        tvContinueAsGuestHeader.setText(StorefrontCommonData.getString(mActivity, R.string.text_continue_guest_login_user));
        // btnContinue.setText(StorefrontCommonData.getString(mActivity, R.string.text_guest_checkout));

        mContactView = findViewById(R.id.contact);
        // mContactView.setHint(getStrings(R.string.your_phone));
        mContactView.setOnFocusChangeListener(this);


        mEmailView = findViewById(R.id.email);

        // mEmailView.setHint(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.email));
        //  mEmailView.setFloatingLabelText(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));
        mEmailView.setFilters(new InputFilter[]{FilterUtils.emojiFilter});
        mEmailView.setOnFocusChangeListener(this);
        if (StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 2
                || StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 1) {

            mEmailView.setFloatingLabelText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), "*"));
            mEmailView.setHint(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.email), "*"));
        } else {
            mEmailView.setFloatingLabelText(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));
            mEmailView.setHint(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));

        }

        if (StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 2
                || StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 1) {
            mContactView.setFloatingLabelText(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.your_phone), "*"));
            mContactView.setHint(CustomViewsUtil.createSpan(mActivity, getStrings(R.string.your_phone), "*"));

        } else {
            mContactView.setFloatingLabelText(getStrings(R.string.your_phone));
            mContactView.setHint(getStrings(R.string.your_phone));
        }


        tvCountryCode = findViewById(R.id.tvCountryCode);
        tvCountryCode.setText(Utils.getDefaultCountryCode(this));


        // setTitleOfLayout();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            LinearLayout llCountryCode = findViewById(iCountryCode);
            llCountryCode.setPadding(0, 8, 0, 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEmailView.setTransitionName(ACTIVITY_EMAIL_TRANSITION);
            mEmailView.setTransitionName(ACTIVITY_PHONE_TRANSITION);
        }


        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(iCountryCode), findViewById(R.id.guest_login_button));
    }


    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }

        switch (v.getId()) {

            case iCountryCode:
                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvCountryCode.setText(country.getCountryCode());
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();

                break;

            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.guest_login_button:
                if (validate()) {
                    guestLogin();
                }
                break;


        }

    }

    @Override
    public void onFocusChange(View view, boolean isFocused) {
        switch (view.getId()) {
            case R.id.email:
                if (isFocused) {
                    vErrorIcon.setVisibility(View.GONE);
                } else {
                    boolean isEmailEmpty = mEmailView.getText().toString().trim().isEmpty();
                    vErrorIcon.setVisibility(isEmailEmpty ? View.GONE : View.VISIBLE);
                    vErrorIcon.setBackgroundResource(Utils.isEmailValid(mEmailView.getText().toString().trim()) || validateClass.checkIfPhoneNumber(mEmailView.getText().toString().trim()) ? R.drawable.ic_icon_verified : R.drawable.ic_failure);
                }
                break;


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void guestLogin() {
        String contact = "";
        if (!mContactView.getText().toString().trim().isEmpty())
            contact = tvCountryCode.getText().toString().trim() + " " + mContactView.getText().toString().trim();
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add("email", mEmailView.getText().toString().trim())
                .add(TIMEZONE, Dependencies.getTimeZoneInMinutes())
                .add("guest_checkout_flow", 1)
                .add("phone_no", contact);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }
        RestClient.getApiInterface(this).signup(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Dependencies.saveAccessTokenGuest(mActivity, ((LinkedTreeMap) baseModel.data).get("access_token").toString());
                Dependencies.saveVendorIdForGuest(mActivity,
                        new Gson().toJsonTree(((LinkedTreeMap) baseModel.data).get("vendor_details"))
                                .getAsJsonObject().get("vendor_id").getAsInt());
                Dependencies.setGuestCheckoutFlowOngoing(getApplicationContext(), 1);
                isGuestCheckOutFlow = 1;

                if (StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 2 ||
                        StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 2) {
                    Intent intent = new Intent(mActivity, LoginOTPActivity.class);
                    intent.putExtra("phone", tvCountryCode.getText().toString().trim()
                            + " " + mContactView.getText().toString().trim());
                    intent.putExtra("email", mEmailView.getText().toString().trim());
                    intent.putExtra("isGuestCheckOutFlow", 1);
                    if (StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 2) {
                        intent.putExtra(IS_PHONE, true);
                    } else {
                        intent.putExtra(IS_PHONE, false);
                    }
                    startActivityForResult(intent, Codes.Request.OPEN_OTP_SCREEN);
                } else {
                    Dependencies.saveAccessTokenGuest(mActivity, ((LinkedTreeMap) baseModel.data).get("app_access_token").toString());
                    Dependencies.setDemoRun(true);
                    loginViaAccessToken();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });


    }

    Boolean validate() {
        if (StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 2
                || StorefrontCommonData.getAppConfigurationData().getEmailConfigForGuestCheckout() == 1) {
            if (!validateClass.checkEmail(mEmailView, getStrings(R.string.enter_valid_email))) {
                return false;
            }
        }
        //else {
        if (StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 2
                || StorefrontCommonData.getAppConfigurationData().getPhoneConfigForGuestCheckout() == 1) {
            if (mContactView.getText().toString().trim().isEmpty()) {
                if (!validateClass.checkPhoneNumber(mContactView)) {
                    return false;
                }
                //}
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.Request.OPEN_OTP_SCREEN:
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

    private void loginViaAccessToken() {

        Location location = LocationUtils.getLastLocation(this);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("lat", location.getLatitude())
                .add("lng", location.getLongitude());


        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        if (isGuestCheckOutFlow == 1) {
            commonParams.add("access_token", Dependencies.getAccessTokenGuest(mActivity));
            commonParams.add("app_access_token", Dependencies.getAccessTokenGuest(mActivity));
            commonParams.add("vendor_id", Dependencies.getVendorIdForGuest(mActivity));
        }

        commonParams.remove(YELO_APP_TYPE);
        commonParams.remove(FORM_ID);
        commonParams.remove(USER_ID);
        commonParams.remove(APP_ACCESS_TOKEN);
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
                    Intent intent = new Intent(GuestLoginActivity.this, HomeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("MERCHANT_ID", MyApplication.getInstance().getDeepLinkMerchantId());
                    extras.putBoolean("FROM_DEEP_LINK", true);
                    extras.putBoolean("FROM_DEEP_LINK_SIGN_IN", true);
                    intent.putExtras(extras);
                    startActivity(intent);
                    AnimationUtils.forwardTransition(GuestLoginActivity.this);
                    finish();
                } else {
                    if (isGuestCheckOutFlow == 1) {
                        CommonAPIUtils.getSuperCategories(userData, mActivity);
                    } else {
                        CommonAPIUtils.userLoginNavigation(mActivity, false);
                    }
                }
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

}
