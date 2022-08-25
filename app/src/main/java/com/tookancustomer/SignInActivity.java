package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
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
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.tookancustomer.appdata.Codes.StatusCode.USER_NOT_REGISTERED;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements View.OnFocusChangeListener, OnClickListener, Constants {
    private final int iBack = R.id.rlBack;
    private final int iForgotPassword = R.id.tvForgotPassword;
    private MaterialEditText mEmailView;
    private MaterialEditText mPasswordView;
    private View vErrorIcon;
    private ImageButton ibViewPassword;
    private TextView tvSignIn, tvGuestLogin;
    private TextView tvSigninHeading, tvSigninSubHeading;
    private RelativeLayout mEmailSignInButton, guestLoginButton;
    private boolean sigInInProgress;
    private TextView signUpTV, tvNewUser;
    private ValidateClass validateClass;
    private String countryCode = "", continentCode = "";
    private boolean isOpenCheckoutActivity = false;
    private LinearLayout llSignupLayout;

    private LinearLayout llCountryCode;
    private TextView tvCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mActivity = this;
        validateClass = new ValidateClass(mActivity);

        if (getIntent().getStringExtra(CONTINENT_CODE) != null) {
            continentCode = getIntent().getStringExtra(CONTINENT_CODE);
        }
        if (getIntent().getStringExtra(COUNTRY_CODE) != null) {
            countryCode = getIntent().getStringExtra(COUNTRY_CODE);
        }
        if (getIntent().hasExtra(CheckOutActivity.class.getName()) || getIntent().hasExtra(CheckOutCustomActivity.class.getName())) {
            if (getIntent().hasExtra(CheckOutActivity.class.getName()))
                isOpenCheckoutActivity = getIntent().getBooleanExtra(CheckOutActivity.class.getName(), false);
            else
                isOpenCheckoutActivity = getIntent().getBooleanExtra(CheckOutCustomActivity.class.getName(), false);

            if (isOpenCheckoutActivity) {
                Dependencies.setDemoRun(true);
                ((TextView) findViewById(R.id.tvSigninHeading)).setText(getStrings(R.string.sign_in_to_continue_demo));
//            Utils.snackBar(mActivity, "Please login to proceed further.");
            }
        }

        // Set up the login form.
        llSignupLayout = findViewById(R.id.llSignupLayout);
        llSignupLayout.setVisibility(UIManager.isSignUpAvailable() ? View.VISIBLE : View.GONE);

        mEmailView = findViewById(R.id.email);
        guestLoginButton = findViewById(R.id.guest_login_button);
        mEmailView.setHint(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));
        mEmailView.setFloatingLabelText(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));
        mEmailView.setFilters(new InputFilter[]{FilterUtils.emojiFilter});
        vErrorIcon = findViewById(R.id.vErrorIcon);
        mEmailView.setOnFocusChangeListener(this);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setHint(getStrings(R.string.pass));
        mPasswordView.setFloatingLabelText(getStrings(R.string.pass));
        FilterUtils.setPasswordFilter(mPasswordView, ValidateClass.PASSWORD_MAX_LENGTH);

        tvSigninHeading = findViewById(R.id.tvSigninHeading);
        tvGuestLogin = findViewById(R.id.tvGuestLogin);
        tvSigninHeading.setText(getStrings(R.string.sign_in_to));
        tvSigninSubHeading = findViewById(R.id.tvSigninSubHeading);
        tvSigninSubHeading.setText(getStrings(R.string.signin_demo_account_message));
        tvSigninSubHeading.setVisibility(Dependencies.isDemoApp() ? View.VISIBLE : View.GONE);
        tvNewUser = findViewById(R.id.tvNewUser);
        tvNewUser.setText(getStrings(R.string.new_user));
        signUpTV = findViewById(R.id.signUpTV);
        signUpTV.setText(getStrings(R.string.sign_up));
        mPasswordView.setOnFocusChangeListener(this);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        ibViewPassword = findViewById(R.id.ibViewPassword);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        signUpTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenCheckoutActivity) {
                    Transition.transitForResult(mActivity, SignUpActivity.class, OPEN_LOGIN_BEFORE_CHECKOUT, getIntent().getExtras(), false);
                } else {
                    Transition.startActivity(mActivity, SignUpActivity.class, getIntent().getExtras(), true);
                }

            }
        });
        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setText(getStrings(R.string.sign_in));

        tvGuestLogin = findViewById(R.id.tvGuestLogin);
        tvGuestLogin.setText(getStrings(R.string.text_guest_checkout));

        if (StorefrontCommonData.getAppConfigurationData() != null && StorefrontCommonData.getAppConfigurationData().getIsGuestCheckoutEnabled() == 1 &&
                getIntent().getBooleanExtra("hideGuestCheckout", true)) {
            tvGuestLogin.setVisibility(View.VISIBLE);
            guestLoginButton.setVisibility(View.VISIBLE);
        } else {
            tvGuestLogin.setVisibility(View.GONE);
            guestLoginButton.setVisibility(View.GONE);
        }
        Dependencies.saveAccessTokenGuest(mActivity, "");
        // tvGuestCheckout.setVisibility(StorefrontCommonData.getAppConfigurationData().getIsGuestCheckoutEnabled()?);
        tvGuestLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition.startActivity(mActivity, GuestLoginActivity.class, null, false);
            }
        });


        TextView tvForgotPassword = findViewById(iForgotPassword);
        tvForgotPassword.setText(getStrings(R.string.forgot_password));
        mEmailView.setHint(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));
        mEmailView.setFloatingLabelText(getStrings(Dependencies.isDemoApp() ? R.string.email : R.string.your_email_or_phone));
        Utils.setOnClickListener(this, findViewById(iBack), ibViewPassword, tvForgotPassword);

        llCountryCode = findViewById(R.id.llCountryCode);
        tvCountryCode = findViewById(R.id.tvCountryCode);
        tvCountryCode.setText(Utils.getDefaultCountryCode(this));
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                llCountryCode.setVisibility(validateClass.checkIfPhoneNumber(s.toString()) && !Dependencies.isDemoApp() ? View.VISIBLE : View.GONE);
            }
        });
        Utils.setOnClickListener(this, llCountryCode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEmailView.setTransitionName(ACTIVITY_EMAIL_TRANSITION);
            mEmailView.setTransitionName(ACTIVITY_PHONE_TRANSITION);
        }

        if (getIntent().getStringExtra(EMAIL_OR_PHONE) != null) {
            if (validateClass.checkIfPhoneNumber(getIntent().getStringExtra(EMAIL_OR_PHONE))) {

                try {
//                    String[] phoneNumber = Utils.splitNumberByCode(mActivity, getIntent().getStringExtra(EMAIL_OR_PHONE));
                    String[] phoneNumber = Utils.splitNumberByCodeNew(mActivity, getIntent().getStringExtra(EMAIL_OR_PHONE));
                    tvCountryCode.setText(phoneNumber[0]);
                    mEmailView.setText(phoneNumber[1].replace("+", ""));
                } catch (Exception e) {
                    String countryCode = Utils.getCountryCode(mActivity, getIntent().getStringExtra(EMAIL_OR_PHONE).trim());
                    tvCountryCode.setText(countryCode);
                    mEmailView.setText(getIntent().getStringExtra(EMAIL_OR_PHONE).trim().replace(countryCode, "").replace("+", "").replace("-", " ").trim());
                }

            } else {
                mEmailView.setText(getIntent().getStringExtra(EMAIL_OR_PHONE));
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Utils.hideSoftKeyboard(this, mPasswordView);
        if (validate()) {
            login();
        }
    }

    Boolean validate() {
        if (Dependencies.isDemoApp()) {
            if (!validateClass.checkEmail(mEmailView, getStrings(R.string.enter_valid_email))) {
                return false;
            }
        } else {
            if (!validateClass.checkEmailPhoneNumber(mEmailView)) {
                return false;
            }
        }
        if (!validateClass.checkPasswordString(mPasswordView, getStrings(R.string.password_field_required),
                getStrings(R.string.password_field_invalid))) {
            return false;
        }
        return true;
    }

    private void login() {
        Location location = LocationUtils.getLastLocation(this);

        tvSignIn.setText(getStrings(R.string.signing_in));
        sigInInProgress = true;
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        if (Dependencies.isDemoApp()) {
            final JSONObject ipConfigObject = new JSONObject();
            try {
                Log.v("country_code", countryCode);
                Log.v("continent_code", continentCode);

                ipConfigObject.put("country", countryCode);
                ipConfigObject.put("continent_code", continentCode);
//            ipConfigArray.put(ipConfigObject);
            } catch (JSONException e) {

                Utils.printStackTrace(e);
            }
            commonParams.add("ipConfig", ipConfigObject);
        }
        commonParams.add("password", mPasswordView.getText().toString())
                .add("lat", location.getLatitude())
                .add("lng", location.getLongitude());

        if (llCountryCode.getVisibility() == View.VISIBLE) {
            commonParams.add("phone_no", tvCountryCode.getText().toString().trim() + " " + mEmailView.getText().toString().trim());
        } else {
            commonParams.add("email", mEmailView.getText().toString().trim());
        }

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(this).login(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        } else {
            RestClient.getApiInterface(this).loginDualUser(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        }
    }

    /**
     * @return final sign in response
     */
    private ResponseResolver<BaseModel> finalSignInResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(final BaseModel baseModel) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_IN_SUCCESS);
                final UserData userData = new UserData();
                try {
                    userData.setData(baseModel.toResponseModel(Data.class));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                Utils.saveUserInfo(mActivity, userData, !isOpenCheckoutActivity);
                if (MyApplication.getInstance().getDeepLinkMerchantId() != 0) {
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("MERCHANT_ID", MyApplication.getInstance().getDeepLinkMerchantId());
                    extras.putBoolean("FROM_DEEP_LINK", true);
                    extras.putBoolean("FROM_DEEP_LINK_SIGN_IN", true);
                    intent.putExtras(extras);
                    startActivity(intent);
                    AnimationUtils.forwardTransition(SignInActivity.this);
                    finish();
                }else{
                    CommonAPIUtils.userLoginNavigation(mActivity, isOpenCheckoutActivity);
                }


                Dependencies.isAppFirstInstall = false;
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_IN_FAILURE);
                sigInInProgress = false;
                //  tvSignIn.setText(getStrings(R.string.sign_in));
                //  mEmailSignInButton.setText(getStrings(R.string.sign_in));


                if (error.getStatusCode() == 800) {
                    //  if (baseModel != null && ((LinkedTreeMap) baseModel.data).containsKey("email")) {}
                    // ((BaseModel) error.getData()).data
                    try {
                        String json = ((BaseModel) error.getData()).getData().toString();
                        Bundle extras = getIntent().getExtras();

                        JSONObject obj = new JSONObject(json);
                        if (extras == null)
                            extras = new Bundle();
                        if (!obj.getString("email").equals("null")) {
                            extras.putString("GUEST_EMAIL", obj.getString("email"));
                        }
                        if (!obj.getString("phone_no").equals("null")) {
                            int countryCode = PhoneNumberUtil.getInstance().parse("+" + obj.getString("phone_no"), "IN").getCountryCode();
                            long phoneNumber = PhoneNumberUtil.getInstance().parse("+" + obj.getString("phone_no"), "IN").getNationalNumber();
                            extras.putString(EMAIL_OR_PHONE, "+" + countryCode + " " + phoneNumber);
                        }
                        extras.putString("PASSWORD", mPasswordView.getText().toString());
                        extras.putBoolean("IS_GUEST_SIGNUP", true);

                        Transition.startActivity(mActivity, SignUpActivity.class, extras, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {


                    if (UIManager.isSignUpAvailable()) {
                        if (error.getStatusCode() == USER_NOT_REGISTERED.getStatusCode()) {
                            new AlertDialog.Builder(mActivity).message(error.getMessage()).button(R.string.ok_text)
                                    .listener(new AlertDialog.Listener() {
                                        @Override
                                        public void performPostAlertAction(int purpose, Bundle backpack) {
                                            Bundle extras = getIntent().getExtras();
                                            if (extras == null)
                                                extras = new Bundle();

                                            if (validateClass.checkIfPhoneNumber(getIntent().getStringExtra(EMAIL_OR_PHONE))) {
                                                extras.putString(EMAIL_OR_PHONE, tvCountryCode.getText().toString().trim() + " " + mEmailView.getText().toString().trim());
                                            } else {
                                                extras.putString(EMAIL_OR_PHONE, mEmailView.getText().toString().trim());
                                            }
                                            if (isOpenCheckoutActivity) {
                                                Transition.transitForResult(mActivity, SignUpActivity.class, OPEN_LOGIN_BEFORE_CHECKOUT, extras, false);
                                            } else {
                                                Transition.startActivity(mActivity, SignUpActivity.class, extras, true);
                                            }
                                        }
                                    }).build().show();
                        } else {
                            Utils.snackBar(mActivity, error.getMessage());
                        }
                    } else {
                        Utils.snackBar(mActivity, error.getMessage());
                    }
                }
            }
        };


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

            case R.id.password:
                if (isFocused) {
                    ibViewPassword.setVisibility(View.VISIBLE);
                } else {
                    ibViewPassword.setVisibility(View.GONE);
                    mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                    ibViewPassword.setImageResource(R.drawable.ic_eye_inactive);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        if (sigInInProgress) {
            return;
        }
        switch (view.getId()) {
            case R.id.ibViewPassword:
                boolean isPasswordVisible = mPasswordView.getTransformationMethod() == null;
                if (isPasswordVisible) {
                    mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                    ibViewPassword.setImageResource(R.drawable.ic_eye_inactive);
                } else {
                    mPasswordView.setTransformationMethod(null);
                    ibViewPassword.setImageResource(R.drawable.ic_eye_active);
                }
                break;
            case iBack:
                onBackPressed();
                break;
            case iForgotPassword:
                Bundle bundle = new Bundle();
                if (llCountryCode.getVisibility() == View.VISIBLE) {
                    bundle.putString(EMAIL_OR_PHONE, tvCountryCode.getText().toString().trim() + " " + mEmailView.getText().toString().trim());
                } else {
                    bundle.putString(EMAIL_OR_PHONE, mEmailView.getText().toString().trim());
                }
                Transition.startActivity(this, ForgotPasswordActivity.class, bundle, false);
                break;
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            case R.id.llCountryCode:
                Utils.hideSoftKeyboard(this);
                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvCountryCode.setText(country.getCountryCode());
                        Utils.hideSoftKeyboard(SignInActivity.this);
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (sigInInProgress) {
            return;
        }
        Transition.exit(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY:
            case OPEN_LOGIN_BEFORE_CHECKOUT:
            case OPEN_OTP_SCREEN:
                if (resultCode == Activity.RESULT_OK) {
                    Dependencies.setDemoRun(false);
                    Bundle bundleExtra = getIntent().getExtras();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    return;
                } else if (resultCode == RESULT_CANCELED_OTP) {
                    Bundle bundleExtra = getIntent().getExtras();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_CANCELED_OTP, returnIntent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
    }

}