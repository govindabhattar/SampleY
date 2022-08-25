package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

public class ForgotPasswordActivity extends BaseActivity implements OnClickListener {
    private UserLoginTask mAuthTask = null;

    // UI references.
    private MaterialEditText mEmailView;

    private View mLoginFormView;
    private View vErrorIcon;

    private final int iBack = R.id.rlBack;

    private LinearLayout llCountryCode;
    private TextView tvCountryCode;
    private ValidateClass validateClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mActivity = this;
        validateClass = new ValidateClass(mActivity);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        // vEmailIcon = findViewById(R.id.vEmailIcon);
        vErrorIcon = findViewById(R.id.vErrorIcon);

        llCountryCode = findViewById(R.id.llCountryCode);
        tvCountryCode = findViewById(R.id.tvCountryCode);
        tvCountryCode.setText(Utils.getDefaultCountryCode(this));

        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
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

        RelativeLayout rlSendLink = findViewById(R.id.rlSendLink);
        rlSendLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mEmailView.setHint(getStrings(Dependencies.isDemoApp() ? R.string.your_email : R.string.your_email_or_phone));
        mEmailView.setFloatingLabelText(getStrings(Dependencies.isDemoApp() ? R.string.your_email : R.string.your_email_or_phone));

        ((TextView) findViewById(R.id.tvForgotPassword)).setText(getStrings(R.string.forgot_password));
        ((TextView) findViewById(R.id.tvForgotPasswordMessage)).setText(getStrings(R.string.forgot_password_message));
        ((TextView) findViewById(R.id.tvSubmit)).setText(getStrings(R.string.submit));

        Utils.setOnClickListener(this, findViewById(iBack), llCountryCode);
        RelativeLayout rlEmail = findViewById(R.id.rlEmail);

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

    private float convertDpToPx(int i) {
        final float density = getResources().getDisplayMetrics().density;
        return density * i + 0.5f;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (validate()) {
            getLink();
        }

//        if (mAuthTask != null) {
//            return;
//        }
//
//        // Reset errors.
//        mEmailView.setError(null);
//
//        // Store values at the time of the login attempt.
//        String email = mEmailView.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            Utils.snackBar(this, getStrings(R.string.enter_valid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (!Utils.isEmailValid(email)) {
//            Utils.snackBar(this, getStrings(R.string.enter_valid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            getLink();
//        }
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

        return true;
    }

    private void getLink() {
        final String emailPhone = mEmailView.getText().toString();

        CommonParams.Builder commonParams = new CommonParams.Builder();
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (llCountryCode.getVisibility() == View.VISIBLE) {
            commonParams.add("phone", tvCountryCode.getText().toString().trim() + " " + emailPhone);
        } else {
            commonParams.add("email", emailPhone);
        }
        commonParams.add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId());


        RestClient.getApiInterface(this).forgotPassword(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Intent intent = new Intent(mActivity, OTPActivity.class);
                intent.putExtra(OPEN_OTP_FOR_FORGOT_PASSWORD, true);

                if (llCountryCode.getVisibility() == View.VISIBLE) {
                    intent.putExtra("phone", tvCountryCode.getText().toString().trim() + " " + emailPhone);
                } else {
                    intent.putExtra("email", emailPhone);
                }
                startActivityForResult(intent, Codes.Request.OPEN_OTP_SCREEN);

//                Utils.snackbarSuccess(mActivity, baseModel.getMessage());

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        performBackAction();
//                    }
//                }, 1000);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case iBack:
                performBackAction();
                break;
            case R.id.llCountryCode:
                Utils.hideSoftKeyboard(this);

                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvCountryCode.setText(country.getCountryCode());
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();
                break;
        }
    }

    private void performBackAction() {
        Transition.exit(this);
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                finish();
            } else {
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(800);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
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
}