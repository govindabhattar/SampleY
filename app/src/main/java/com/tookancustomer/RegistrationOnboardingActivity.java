package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tookancustomer.adapter.ViewPagerAdapter;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customviews.NonSwipeableViewPager;
import com.tookancustomer.fragments.EmailVerificationFragment;
import com.tookancustomer.fragments.OTPFragment;
import com.tookancustomer.fragments.SignupFeeFragment;
import com.tookancustomer.fragments.SignupTemplateFragment;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.HashMap;
import java.util.Set;

import params.com.stepview.StatusViewScroller;

public class RegistrationOnboardingActivity extends BaseActivity implements View.OnClickListener {
    public NonSwipeableViewPager viewPager;
    public ViewPagerAdapter mPagerAdapter;
    public int OTPFragmentPos = 0, EmailFragmentPos = 1, SignUpTemplateFragmentPos = 2, SignUpFeeFragmentPos = 3;
    public boolean isLoginFromCheckout, isOnboardingFromProfile;
    private RelativeLayout rlBack;
    private StatusViewScroller statusViewScroller;
    private Set<String> registrationEnabledMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_onboarding);
        mActivity = this;
        isLoginFromCheckout = getIntent().getBooleanExtra(IS_LOGIN_FROM_CHECKOUT, false);
        isOnboardingFromProfile = getIntent().getBooleanExtra(IS_ONBOARDING_FROM_PROFILE, false);
        initViews();
        setStatusViewController();
        setCurentStep();
    }

    private void initViews() {
        rlBack = findViewById(R.id.rlBack);
        if (isOnboardingFromProfile) {
            rlBack.setVisibility(View.GONE);
        } else {
            rlBack.setVisibility(View.VISIBLE);
        }
        statusViewScroller = findViewById(R.id.statusViewScroller);
        viewPager = findViewById(R.id.viewPager);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new OTPFragment(), "");
        mPagerAdapter.addFragment(new EmailVerificationFragment(), "");
        mPagerAdapter.addFragment(new SignupTemplateFragment(), "");
        mPagerAdapter.addFragment(new SignupFeeFragment(), "");
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        Utils.setOnClickListener(this, rlBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }

    public void setStatusViewController() {
        statusViewScroller.setVisibility(View.VISIBLE);
        if (isOnboardingFromProfile) {
            if ((UIManager.isOTPAvailable() && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsPhoneVerified() == 0) && (UIManager.getIsEmailVerificationRequried() == 1 && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsEmailVerified() == 0)) {
                statusViewScroller.getStatusView().setStepCount(2);
            } else {
                statusViewScroller.setVisibility(View.GONE);
            }
        } else {
            statusViewScroller.getStatusView().setStepCount(getEnabledRegistrationProcess().size());
            if (getEnabledRegistrationProcess().size() < 2) {
                statusViewScroller.setVisibility(View.GONE);
            }

//            if (UIManager.isOTPAvailable() && UIManager.getIsEmailVerificationRequried() == 1 && !StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty()) {
//                statusViewScroller.getStatusView().setStepCount(3);
//            } else if ((UIManager.isOTPAvailable() && UIManager.getIsEmailVerificationRequried() == 1) || (UIManager.getIsEmailVerificationRequried() == 1 && !StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty()) || (UIManager.isOTPAvailable() && !StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty())) {
//                statusViewScroller.getStatusView().setStepCount(2);
//            } else {
//                statusViewScroller.setVisibility(View.GONE);
//            }
        }
    }

    public void setCurentStep() {
        /* Preference would be --> First check for otp , then email, then sign up template and then sign up fee */

        if (UIManager.isOTPAvailable() && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsPhoneVerified() == 0) {
            //OTP fragment
            statusViewScroller.getStatusView().setCurrentCount(1);
            viewPager.setCurrentItem(OTPFragmentPos);
        } else if (UIManager.getIsEmailVerificationRequried() == 1 && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsEmailVerified() == 0) {
            //Email fragment
            if (UIManager.isOTPAvailable()) {
                statusViewScroller.getStatusView().setCurrentCount(2);
            } else {
                statusViewScroller.getStatusView().setCurrentCount(1);
            }
            viewPager.setCurrentItem(EmailFragmentPos);
        } else if (!isOnboardingFromProfile && (StorefrontCommonData.getUserData().getData().getVendorDetails().getRegistrationStatus() != Constants.RegistrationStatus.VERIFIED) && StorefrontCommonData.getUserData().getData().getSignupTemplateData() != null && !StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty()) {
            //Signup template fragment
            if (UIManager.isOTPAvailable() && UIManager.getIsEmailVerificationRequried() == 1) {
                statusViewScroller.getStatusView().setCurrentCount(3);
            } else if ((UIManager.isOTPAvailable()) || (UIManager.getIsEmailVerificationRequried() == 1)) {
                statusViewScroller.getStatusView().setCurrentCount(2);
            } else {
                statusViewScroller.getStatusView().setCurrentCount(1);
            }
            if (mPagerAdapter.getItem(SignUpTemplateFragmentPos) instanceof SignupTemplateFragment) {
                ((SignupTemplateFragment) mPagerAdapter.getItem(SignUpTemplateFragmentPos)).setUserData();
            }
            viewPager.setCurrentItem(SignUpTemplateFragmentPos);
        } else if (!isOnboardingFromProfile && StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1
                && StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPaid() == 0) {
            //Signup fee fragment
            statusViewScroller.getStatusView().setCurrentCount(getEnabledRegistrationProcess().size()); /*last step equals to size of array*/

            viewPager.setCurrentItem(SignUpFeeFragmentPos);

            if (mPagerAdapter.getItem(SignUpFeeFragmentPos) instanceof SignupFeeFragment) {
                ((SignupFeeFragment) mPagerAdapter.getItem(SignUpFeeFragmentPos)).fetchPaymentData();
            }
        } else if (isOnboardingFromProfile) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
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

    public Set<String> getEnabledRegistrationProcess() {
        HashMap<String, Boolean> hashMap = new HashMap<>();

        if (UIManager.isOTPAvailable()) {
            hashMap.put(Constants.RegistrationProcess.PHONE_VERIFICATION, true);
        }
        if (UIManager.getIsEmailVerificationRequried() == 1) {
            hashMap.put(Constants.RegistrationProcess.EMAIL_VERIFICATION, true);
        }
        if (!StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty()) {
            hashMap.put(Constants.RegistrationProcess.SIGNUP_TEMPLATE, true);
        }
        if (StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1) {
            hashMap.put(Constants.RegistrationProcess.SIGNUP_FEE, true);
        }
        return hashMap.keySet();
    }

    @Override
    public void onBackPressed() {
        if (!isOnboardingFromProfile) {
            Dependencies.saveAccessToken(mActivity, "");


            Bundle bundleExtra = getIntent().getExtras();
            bundleExtra.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            setResult(RESULT_CANCELED_OTP, returnIntent);
            finish();
        }
        //        super.onBackPressed();

    }

    public void deleteCustomFieldImageSignup(int position) {
        ((SignupTemplateFragment) mPagerAdapter.getItem(SignUpTemplateFragmentPos)).deleteCustomFieldImageSignup(position);
    }

    public void setCustomFieldPosition(SignupTemplateData item) {
        ((SignupTemplateFragment) mPagerAdapter.getItem(SignUpTemplateFragmentPos)).setCustomFieldPosition(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((SignupTemplateFragment) mPagerAdapter.getItem(SignUpTemplateFragmentPos)).onActivityResult(requestCode, resultCode, data);
        ((SignupFeeFragment) mPagerAdapter.getItem(SignUpFeeFragmentPos)).onActivityResult(requestCode, resultCode, data);
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
        }
    }
}