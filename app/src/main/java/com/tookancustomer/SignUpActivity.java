package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tookancustomer.adapters.LanguageSpinnerAdapter;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.AlertDialogSignUp;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.socialLogin.facebook.FacebookLoginData;
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

public class SignUpActivity extends BaseActivity implements View.OnFocusChangeListener, OnClickListener, Constants {
    private MaterialEditText mEmailView,referralCode;
    private MaterialEditText mPasswordView;
    private MaterialEditText etAddress;
    private View vErrorIcon;
    private ImageButton ibViewPassword;
    private final int iViewPassword = R.id.ibViewPassword;
    private final int iBack = R.id.rlBack;
    private RelativeLayout rlPassword;
    private MaterialEditText mContactView;
    private MaterialEditText mUsernameView;
    private View vContactErrorIcon;
    private String countryCode = "", continentCode = "";
    private RelativeLayout emailRL;
    private Button mEmailSignUPButton;
    private boolean signupInProgress;
    private TextView tvCountryCode;
    private final int iCountryCode = R.id.rlCountryCode;
    private FacebookLoginData fbLoginData;
    private LinearLayout llTermsConditions;
    private CheckBox cbTermsConditions;
    private TextView tvTermsConditions;
    private TextView signInTV, emailTV;
    private TextView tvSignUpHeading, tvSignUpSubHeading;
    private ValidateClass validateClass;
    private boolean isOpenCheckoutActivity = false;
    private EditText etChooseLanguage;
    private Spinner spinnerChooseLanguage;
    private LanguagesCode selectedLanguageCode;
    private String instaName, instaID, instaProfileImage;
    private GoogleSignInAccount googleAccount;
    private LinearLayout llReferralCode;
    private TextView tvGuestCheckout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mActivity = this;
        validateClass = new ValidateClass(mActivity);
        /*if (getIntent().hasExtra(CheckOutActivity.class.getName())) {
            isOpenCheckoutActivity = getIntent().getBooleanExtra(CheckOutActivity.class.getName(), false);
            if (isOpenCheckoutActivity) {
                Dependencies.setDemoRun(true);
                ((TextView) findViewById(R.id.tvSignUpHeading)).setText(getStrings(R.string.sign_up_to_continue_demo));
            }
        }*/

        if (!getIntent().hasExtra("IS_GUEST_SIGNUP")) {
            if (getIntent().hasExtra(CheckOutActivity.class.getName())) {
                isOpenCheckoutActivity = getIntent().getBooleanExtra(CheckOutActivity.class.getName(), false);
                if (isOpenCheckoutActivity) {
                    Dependencies.setDemoRun(true);
                    ((TextView) findViewById(R.id.tvSignUpHeading)).setText(getStrings(R.string.sign_up_to_continue_demo));
                }
            }
        }

        tvGuestCheckout = findViewById(R.id.tvGuestCheckout);
        tvGuestCheckout.setText(getStrings(R.string.text_guest_checkout));

        if (StorefrontCommonData.getAppConfigurationData().getIsGuestCheckoutEnabled() == 1 &&
                getIntent().getBooleanExtra("hideGuestCheckout", true)) {
            tvGuestCheckout.setVisibility(View.VISIBLE);
        } else {
            tvGuestCheckout.setVisibility(View.GONE);
        }

        tvGuestCheckout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition.startActivity(mActivity, GuestLoginActivity.class, null, false);
            }
        });


        // Set up the login form.
        signInTV = (TextView) findViewById(R.id.signInTV);
        signInTV.setText(getStrings(R.string.sign_in));
        emailTV = (TextView) findViewById(R.id.emailTV);
        emailRL = (RelativeLayout) findViewById(R.id.emailRL);

        llReferralCode = findViewById(R.id.llReferralCode);

        if (StorefrontCommonData.getAppConfigurationData().getReferral_code_for_signup() == 1) {
            llReferralCode.setVisibility(View.VISIBLE);
        } else {
            llReferralCode.setVisibility(View.GONE);
        }
        referralCode = findViewById(R.id.referralCode);
        referralCode.setHint(getStrings(R.string.referral_code));
        referralCode.setFloatingLabelText(getStrings(R.string.referral_code));


        llTermsConditions = (LinearLayout) findViewById(R.id.llTermsConditions);
        cbTermsConditions = (CheckBox) findViewById(R.id.cbTermsConditions);
        tvTermsConditions = (TextView) findViewById(R.id.tvTermsConditions);
        setTermsConditionsText();
        mEmailView = findViewById(R.id.email);
        mEmailView.setHint(getStrings(R.string.your_email));
        mEmailView.setFloatingLabelText(getStrings(R.string.your_email));
        spinnerChooseLanguage = findViewById(R.id.spinnerChooseLanguage);
        etChooseLanguage = findViewById(R.id.etChooseLanguage);
        etChooseLanguage.setHint(getStrings(R.string.choose_language));
        ((MaterialEditText) etChooseLanguage).setFloatingLabelText((getStrings(R.string.selected_language)));
        Utils.setOnClickListener(this, etChooseLanguage);
        etAddress = findViewById(R.id.etAddress);
        etAddress.setHint(StorefrontCommonData.getTerminology().getAddress());
        mUsernameView = findViewById(R.id.username);
        mUsernameView.setHint(getStrings(R.string.your_name));
        mUsernameView.setFloatingLabelText(getStrings(R.string.your_name));
        mContactView = findViewById(R.id.contact);
        mContactView.setHint(getStrings(R.string.your_phone));
        mContactView.setFloatingLabelText(getStrings(R.string.your_phone));
        tvSignUpHeading = findViewById(R.id.tvSignUpHeading);
        tvSignUpHeading.setText(getStrings(R.string.sign_up_with));
        tvSignUpSubHeading = findViewById(R.id.tvSignUpSubHeading);
        tvSignUpSubHeading.setText(getStrings(R.string.signup_demo_account_message));
        tvSignUpSubHeading.setVisibility(Dependencies.isDemoApp() ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.tvHaveAnAccount)).setText(getStrings(Dependencies.isDemoApp() ? R.string.already_registered_on_yelo : R.string.have_an_account));
        vErrorIcon = findViewById(R.id.vErrorIcon);
        mEmailView.setOnFocusChangeListener(this);

        tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
        tvCountryCode.setText(Utils.getDefaultCountryCode(this));

        mContactView.setOnFocusChangeListener(this);
        mUsernameView.setOnFocusChangeListener(this);
        vContactErrorIcon = findViewById(R.id.vContactErrorIcon);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setHint(getStrings(R.string.pass));
        mPasswordView.setFloatingLabelText(getStrings(R.string.pass));

        mPasswordView.setOnFocusChangeListener(this);
        FilterUtils.setPasswordFilter(mPasswordView, ValidateClass.PASSWORD_MAX_LENGTH);
        //in case of otp signup

        signInTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenCheckoutActivity) {
                    Transition.exit(mActivity);
                } else {
                    Transition.startActivity(mActivity, SignInActivity.class, getIntent().getExtras(), true);
                }
            }
        });
        mContactView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    validateUsernameContact();

                    return true;
                }
                return false;
            }
        });

        ibViewPassword = (ImageButton) findViewById(iViewPassword);
        rlPassword = (RelativeLayout) findViewById(R.id.rlPassword);
        mEmailSignUPButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignUPButton.setText(getStrings(R.string.sign_up));
        mEmailSignUPButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailSignUPButton.setEnabled(false);
                validateUsernameContact();
            }
        });

        if (UIManager.isOtpLoginAvailable())
            passwordVisibilityGone();


        mEmailSignUPButton.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideSoftKeyboard(mActivity, textView);
                    validateUsernameContact();
                    return true;
                }
                return false;
            }
        });
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            LinearLayout llCountryCode = (LinearLayout) findViewById(iCountryCode);
            llCountryCode.setPadding(0, 8, 0, 0);
        }

        Utils.setOnClickListener(this, findViewById(iBack), findViewById(iCountryCode), ibViewPassword);

        getFbIntent();
        getInstagramIntent();
        getGoogleIntent();
        if (emailTV.getText().toString().trim().isEmpty()) {
            mEmailView.setEnabled(true);
            emailRL.setVisibility(View.GONE);
            mEmailView.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEmailView.setTransitionName(ACTIVITY_EMAIL_TRANSITION);
        }
        if (getIntent().getStringExtra("GUEST_EMAIL") != null) {
            mEmailView.setText(getIntent().getStringExtra("GUEST_EMAIL"));
        }
        if (getIntent().getStringExtra("PASSWORD") != null) {
            mPasswordView.setText(getIntent().getStringExtra("PASSWORD"));
        }



        if (getIntent().getStringExtra(EMAIL_OR_PHONE) != null) {

            if (validateClass.checkIfPhoneNumber(getIntent().getStringExtra(EMAIL_OR_PHONE))) {
                mContactView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] phoneNumber = Utils.splitNumberByCodeNew(mActivity, getIntent().getStringExtra(EMAIL_OR_PHONE));
                            tvCountryCode.setText(phoneNumber[0]);
                            mContactView.setText(phoneNumber[1].replace("+", ""));
                        } catch (Exception e) {
                            String countryCode = Utils.getCountryCode(mActivity, getIntent().getStringExtra(EMAIL_OR_PHONE).trim());
                            tvCountryCode.setText(countryCode);
                            mContactView.setText(getIntent().getStringExtra(EMAIL_OR_PHONE).trim().replace(countryCode, "").replace("+", "").replace("-", " ").trim());
                        }
                    }
                });
            } else {
                mEmailView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getIntent().getStringExtra("GUEST_EMAIL") == null) {
                            mEmailView.setText(getIntent().getStringExtra(EMAIL_OR_PHONE));
                        }
                        if (mEmailView.getText().toString().length() != 0) {
                            mEmailView.setSelection(mEmailView.getText().length());
                        }
                    }
                });
            }
        }
        if (getIntent().getStringExtra(CONTINENT_CODE) != null) {
            continentCode = getIntent().getStringExtra(CONTINENT_CODE);
        }
        if (getIntent().getStringExtra(COUNTRY_CODE) != null) {
            countryCode = getIntent().getStringExtra(COUNTRY_CODE);
        }

        LanguageSpinnerAdapter languageSpinnerAdapter = new LanguageSpinnerAdapter(mActivity, android.R.layout.simple_spinner_item, UIManager.getLanguagesArrayList());
        languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseLanguage.setAdapter(languageSpinnerAdapter);
        spinnerChooseLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedLanguageCode = (LanguagesCode) parent.getItemAtPosition(position);
                    etChooseLanguage.setText(selectedLanguageCode.getLanguageDisplayName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getGoogleIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            googleAccount = bundle.getParcelable("googleAccount");
            if (googleAccount != null) {
                passwordVisibilityGone();
                setGoogleData();
                mContactView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
        }
    }

    private void getInstagramIntent() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            instaName = bundle.getString("name");
            instaID = bundle.getString("instaId");
            instaProfileImage = bundle.getString("instaProfileImage");
            if (instaID != null) {
                passwordVisibilityGone();
                setInstagramData();
                mContactView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
        }
    }

    public void getFbIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fbLoginData = (FacebookLoginData) bundle.getSerializable(FACEBOOK_DETAILS);
            if (fbLoginData != null) {
                passwordVisibilityGone();
                setFbData();
                mContactView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
        }
    }

    public void passwordVisibilityGone() {
        rlPassword.setVisibility(View.GONE);
    }

    public void setFbData() {
        mUsernameView.setText(fbLoginData.getFirstName() + " " + fbLoginData.getLastName());
        mEmailView.setText(fbLoginData.getEmail());
        emailTV.setText(fbLoginData.getEmail());

        if (fbLoginData.getEmail().isEmpty()) {
            mEmailView.setEnabled(true);
            emailRL.setVisibility(View.GONE);
            mEmailView.setVisibility(View.VISIBLE);
        } else {
            mEmailView.setEnabled(false);
            emailRL.setVisibility(View.VISIBLE);
            mEmailView.setVisibility(View.GONE);
        }

        emailTV.setSelected(true);
    }

    public void setInstagramData() {
        mUsernameView.setText(instaName + "");
        emailTV.setSelected(true);
    }

    public void setGoogleData() {
        mUsernameView.setText(googleAccount.getDisplayName() != null ? googleAccount.getDisplayName() : "");
        mEmailView.setText(googleAccount.getEmail());
        emailTV.setText(googleAccount.getEmail());

        if (googleAccount.getEmail().isEmpty()) {
            mEmailView.setEnabled(true);
            emailRL.setVisibility(View.GONE);
            mEmailView.setVisibility(View.VISIBLE);
        } else {
            //TODO uncomment this
            mEmailView.setEnabled(false);
            emailRL.setVisibility(View.VISIBLE);
            mEmailView.setVisibility(View.GONE);
        }

        emailTV.setSelected(true);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void validateUsernameContact() {
        if (!Utils.preventMultipleClicks()) {
            mEmailSignUPButton.setEnabled(true);
            return;
        }

        if (Utils.internetCheck(mActivity)) {
            if (validate()) {
                signup();
            } else mEmailSignUPButton.setEnabled(true);
        } else {
            mEmailSignUPButton.setEnabled(true);
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
        }
    }

    Boolean validate() {

        if (!validateClass.checkName(mUsernameView)) {
            return false;
        }

        if (!(UIManager.getSignupField() == 1 && mEmailView.getText().toString().trim().isEmpty())) {
            if (!validateClass.checkEmail(mEmailView, getStrings(R.string.email_field_required))) {
                return false;
            }
        }

        if (!(UIManager.getSignupField() == 0 && mContactView.getText().toString().trim().isEmpty())) {
            if (!validateClass.checkPhoneNumber(mContactView)) {
                return false;
            }
        }

        if (fbLoginData == null && instaID == null && googleAccount == null && !UIManager.isOtpLoginAvailable()) {
            if (!validateClass.checkPasswordString(mPasswordView, getStrings(R.string.password_field_required),
                    getStrings(R.string.password_field_invalid))) {
                return false;
            }
        }

        if (((UIManager.getIsTnCActive() != null && UIManager.getIsTnCActive() == 1) || (Dependencies.isDemoApp() && UIManager.isTnCAvailable())) && !cbTermsConditions.isChecked()) {
            Utils.snackBar(this, getStrings(R.string.please_accept_terms_conditions));
            return false;
        }

        return true;
    }

    private void signup() {
        Location location = LocationUtils.getLastLocation(this);
        String username = mUsernameView.getText().toString().trim();
        String contact = "";
        if (!mContactView.getText().toString().trim().isEmpty())
            contact = tvCountryCode.getText().toString().trim() + " " + mContactView.getText().toString().trim();
        mEmailSignUPButton.setText(getStrings(R.string.signinig_up));
        signupInProgress = true;
        String name[] = Utils.splitViaFirstCharacter(username, ' ');

        final String firstName = name[0];
        String lastName = name.length > 1 ? name[1] : "";
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        if (StorefrontCommonData.getAppConfigurationData().getReferral_code_for_signup() == 1
                && !referralCode.getText().toString().trim().isEmpty()) {
            commonParams.add("referral_code", referralCode.getText().toString().trim());
        }

        if (Dependencies.isDemoApp()) {
            final JSONObject ipConfigObject = new JSONObject();
            try {
                Log.v("country_code", countryCode);
                Log.v("continent_code", continentCode);

                ipConfigObject.put("country", countryCode);
                ipConfigObject.put("continent_code", continentCode);
            } catch (JSONException e) {

                Utils.printStackTrace(e);
            }
            commonParams.add("ipConfig", ipConfigObject);
        }
        commonParams.add("email", mEmailView.getText().toString().trim())
                .add(TIMEZONE, Dependencies.getTimeZoneInMinutes())
                .add("first_name", FilterUtils.toCamelCase(firstName))
                .add("last_name", FilterUtils.toCamelCase(lastName))
                .add("phone_no", contact);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }
        if (fbLoginData != null) {
            commonParams.add("fb_token", fbLoginData.getSocialUserID())
                    .add("lat", location.getLatitude())
                    .add("lng", location.getLongitude());
            if (fbLoginData.getPicBig() != null) {
                commonParams.add("vendor_image", fbLoginData.getPicBig());
            }

            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).socialRegister(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).socialRegisterDualUser(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        } else if (instaID != null) {
            commonParams.add("instagram_token", instaID);
            if (instaProfileImage != null)
                commonParams.add("vendor_image", instaProfileImage);
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).instagramRegister(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).instagramRegisterDualUser(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        } else if (googleAccount != null) {
            commonParams.add("google_token", googleAccount.getId());
            if (googleAccount.getPhotoUrl() != null)
                commonParams.add("vendor_image", googleAccount.getPhotoUrl());
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).googleRegister(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).googleRegisterDualUser(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        } else if (UIManager.isOtpLoginAvailable()) {
            commonParams.add("lat", location.getLatitude())
                    .add("lng", location.getLongitude());
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).signup(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).dualUserSignUp(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        } else {
            commonParams.add("password", mPasswordView.getText().toString())
                    .add("lat", location.getLatitude())
                    .add("lng", location.getLongitude());
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).signup(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).dualUserSignUp(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        }
    }

    ResponseResolver<BaseModel> signupApiResponse = new ResponseResolver<BaseModel>(mActivity, false, false) {
        @Override
        public void success(BaseModel baseModel) {
            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_UP_SUCCESS);
            final UserData userData = new UserData();
            try {
                userData.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));
            } catch (Exception e) {

                Utils.printStackTrace(e);
            }

            if (Dependencies.isDemoApp() && userData.getData().getWelcomePopUp() != null && !userData.getData().getWelcomePopUp().isEmpty()) {
                new AlertDialogSignUp.Builder(mActivity)
                        .message(userData.getData().getWelcomePopUp())
                        .listener(new AlertDialogSignUp.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {

                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(mActivity);

                            }
                        })
                        .build().show();
            } else {
                Utils.saveUserInfo(mActivity, userData, !isOpenCheckoutActivity);
                CommonAPIUtils.userLoginNavigation(mActivity, isOpenCheckoutActivity);
                mEmailSignUPButton.setEnabled(true);
            }
        }

        @Override
        public void failure(APIError error, BaseModel baseModel) {
            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_UP_FAILURE);
            mEmailSignUPButton.setText(getStrings(R.string.sign_up));
            signupInProgress = false;
            mEmailSignUPButton.setEnabled(true);
            Utils.snackBar(mActivity, error.getMessage());
        }
    };

    @Override
    public void onFocusChange(View view, boolean isFocused) {
        switch (view.getId()) {
            case R.id.email:
                if (isFocused) {
                    vErrorIcon.setVisibility(View.GONE);
                } else {
                    boolean isEmailEmpty = mEmailView.getText().toString().isEmpty();
                    vErrorIcon.setVisibility(isEmailEmpty ? View.GONE : View.VISIBLE);
                    vErrorIcon.setBackgroundResource(Utils.isEmailValid(mEmailView.getText().toString()) ? R.drawable.ic_icon_verified : R.drawable.ic_failure);
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

            case R.id.contact:
                setContactIcon(isFocused);
                break;
        }
    }

    private void setContactIcon(boolean isFocused) {
        if (isFocused) {
            vContactErrorIcon.setVisibility(View.GONE);
        } else {
            boolean isContactEmpty = mContactView.getText().toString().isEmpty();
            vContactErrorIcon.setVisibility(isContactEmpty ? View.GONE : View.VISIBLE);
            vContactErrorIcon.setBackgroundResource(Utils.isValidPhoneNumber(mContactView.getText().toString()) ? R.drawable.ic_icon_verified : R.drawable.ic_failure);
        }
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        if (signupInProgress) {
            return;
        }
        switch (view.getId()) {
            case iViewPassword:
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
            case iCountryCode:
                Utils.hideSoftKeyboard(this, mPasswordView);
                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvCountryCode.setText(country.getCountryCode());
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();

                break;
            case R.id.etChooseLanguage:
                spinnerChooseLanguage.performClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (signupInProgress) {
            return;
        }
        Transition.exit(this);
    }

    public void setTermsConditionsText() {
        if (UIManager.getIsTnCActive() != null && UIManager.getIsTnCActive() == 1) {
            llTermsConditions.setVisibility(View.VISIBLE);
        } else if (Dependencies.isDemoApp() && UIManager.isTnCAvailable()) {
            llTermsConditions.setVisibility(View.VISIBLE);
        } else {
            llTermsConditions.setVisibility(View.GONE);
        }

        tvTermsConditions.setText(getStrings(R.string.by_proceeding_to_create_your_account));
        tvTermsConditions.append(" ");
        Spannable spannable = new SpannableString(getStrings(R.string.terms_of_service_and_privacy_policy));
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                Intent intent = new Intent(SignUpActivity.this, WebViewActivity.class);
                intent.putExtra(URL_WEBVIEW, UIManager.getIsTnCActive() == 1 ? null : UIManager.getTNC());
                intent.putExtra(HEADER_WEBVIEW, getStrings(R.string.terms_of_service));
                startActivity(intent);
                AnimationUtils.forwardTransition(mActivity);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.hyperlink));
                ds.setUnderlineText(false);
            }
        }, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTermsConditions.append(spannable);
        tvTermsConditions.append(".");
        tvTermsConditions.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
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
                } else if (resultCode == RESULT_CANCELED) {
                    Bundle bundleExtra = getIntent().getExtras();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_CANCELED, returnIntent);
                    finish();
                }
                break;
        }
    }
}