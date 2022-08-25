package com.tookancustomer.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;
import com.hippo.HippoConfig;
import com.hippo.MobileCampaignBuilder;
import com.tookancustomer.CatalogueActivity;
import com.tookancustomer.CheckOutActivity;
import com.tookancustomer.DynamicPagesActivity;
import com.tookancustomer.FavLocationActivity;
import com.tookancustomer.GiftCardActivity;
import com.tookancustomer.HomeActivity;
import com.tookancustomer.HostHomeActivity;
import com.tookancustomer.HyperlocalLandingActivity;
import com.tookancustomer.LoyalityPointsActivity;
import com.tookancustomer.NotificationActivity;
import com.tookancustomer.PaymentMethodActivity;
import com.tookancustomer.ProfileActivity;
import com.tookancustomer.R;
import com.tookancustomer.ReferActivity;
import com.tookancustomer.RentalHomeActivity;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.SignInActivity;
import com.tookancustomer.SplashActivity;
import com.tookancustomer.TasksActivity;
import com.tookancustomer.WalletDetailsActivity;
import com.tookancustomer.adapters.LanguageSpinnerAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.PaymentMethodsClass;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.LanguageStrings.LanguageStringsModel;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.appConfiguration.Terminology;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.customerSubscription.CustomerSubscriptionActivity;
import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.modules.reward.activity.RewardsActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;

import org.json.JSONObject;

import java.util.Map;

import static com.tookancustomer.appdata.Constants.GoogleApiResultStatus.OK;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by cl-macmini-85 on 04/05/17.
 */

public class SideMenuTransition implements Keys.Extras {
    public static final int PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE = 1900;
    private static int check;
    private static androidx.appcompat.app.AlertDialog mAlertDialog;

    private static String[] PERMISSION_CAMERA_READ_WRITE = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String CAMERA_READ_WRITE_PERMISSION_MSG = "Permissions are required for camera & storage.";

    public static void sideMenuClick(View v, final Activity mActivity) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        if (!Utils.internetCheck(mActivity) && v.getId() != R.id.tvFuguChat) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
            return;
        }

        switch (v.getId()) {

            case R.id.llChooseLanguage:
                mActivity.findViewById(R.id.spinnerChooseLanguage).performClick();
                break;
            case R.id.tvInbox:
                Transition.startActivity(mActivity, NotificationActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;

            case R.id.tvAllOrders:
                Transition.startActivity(mActivity, TasksActivity.class, new Bundle(), false);
//                Transition.startActivity(mActivity, AllTasksActivity.class, new Bundle(), false);
                break;
            case R.id.tvWallet:
                Transition.startActivity(mActivity, WalletDetailsActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvDQRCode:
            case R.id.tvDQRCodeGuest:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestMarshMallowPermission(mActivity);
                        return;
                    }
                }
                // Transition.startActivity(mActivity, SimpleScannerActivity.class, new Bundle(), false);
                Intent intention = new Intent(mActivity, SimpleScannerActivity.class);
                mActivity.startActivity(intention);

                closeSideMenu(mActivity);

                break;
            case R.id.tvGiftCard:
                Transition.startActivity(mActivity, GiftCardActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvPaymentMethod:
                Transition.startActivity(mActivity, PaymentMethodActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvReferalAndEran:
                Transition.startActivity(mActivity, ReferActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvLoyaltyPoint:
                Transition.startActivity(mActivity, LoyalityPointsActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvDynamicPage:
                Transition.startActivity(mActivity, DynamicPagesActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvRewards:
                Transition.startActivity(mActivity, RewardsActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvSubscription:
                Bundle bundleeee = new Bundle();
                bundleeee.putBoolean("fromSideMenu", true);
                Transition.startActivity(mActivity, CustomerSubscriptionActivity.class, bundleeee, false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvMyAddresses:
                Bundle addressBundle = new Bundle();
                addressBundle.putBoolean(FROM_ACCOUNT_SCREEN, true);
                Transition.startActivity(mActivity, FavLocationActivity.class, addressBundle, false);
                closeSideMenu(mActivity);
                break;
            case R.id.tvFuguChat:
                Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
                HippoConfig.getInstance().showConversations(mActivity, StorefrontCommonData.getString(mActivity, R.string.support));
//                ArrayList<String> groupingTags = new ArrayList<>();
//                ChatByUniqueIdAttributes attributes = new ChatByUniqueIdAttributes.Builder()
//                        .setTransactionId(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString())
//                        .setUserUniqueKey(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString())
//                        .setChannelName(StorefrontCommonData.getString(mActivity, R.string.chat_with_fugu))
//                        .setGroupingTags(groupingTags)
//                        .build();
//                HippoConfig.getInstance().openChatByUniqueId(attributes);
                closeSideMenu(mActivity);
                break;
            case R.id.tvFuguChatCampaigns:
                MobileCampaignBuilder attributes = new MobileCampaignBuilder.Builder()
                        .setNotificationTitle(StorefrontCommonData.getTerminology().getAnnouncements())
                        .setClearText(StorefrontCommonData.getString(mActivity, R.string.clear_all))
                        .build();

                HippoConfig.getInstance().openMobileCampaigns(mActivity, attributes);

                break;
            case R.id.rlProfileLayout:
                Dependencies.setGuestCheckoutFlowOngoing(mActivity, 0);

                if (Dependencies.isDemoRunning() && !UIManager.isCustomerLoginRequired()) {
                    Dependencies.setDemoRun(false);
                    Bundle bundle = new Bundle();
                    if (UIManager.isFacebookAvailable() || UIManager.isGPlusAvailable() || UIManager.isInstagramAvailable() || UIManager.isOtpLoginAvailable()) {
                        bundle.putBoolean(CheckOutActivity.class.getName(), true);
                        if(!Dependencies.getAccessTokenGuest(mActivity).isEmpty()){
                            bundle.putBoolean("hideGuestCheckout", false);
                        }

                        Transition.transitForResult(mActivity, SplashActivity.class, Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY, bundle);
                    } else {
                        bundle.putBoolean(CheckOutActivity.class.getName(), true);
                        if(!Dependencies.getAccessTokenGuest(mActivity).isEmpty()){
                            bundle.putBoolean("hideGuestCheckout", false);
                        }

                        Transition.transitForResult(mActivity, SignInActivity.class, Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY, bundle);
                    }
                } else {
                    Transition.transitForResult(mActivity, ProfileActivity.class, Codes.Request.OPEN_PROFILE_ACTIVITY, new Bundle(), false);
                }
                closeSideMenu(mActivity);
                break;

            case R.id.tvCatalog:
                Transition.startActivity(mActivity, CatalogueActivity.class, new Bundle(), false);
                closeSideMenu(mActivity);
                break;
        }
    }

    public static void closeSideMenu(final Activity mActivity) {
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("WrongConstant")
            @Override
            public void run() {
                ((DrawerLayout) mActivity.findViewById(R.id.drawer_layout)).closeDrawer(Gravity.START, false);
            }
        }, 1500);
    }

//    public static void closeSideMenu(Activity mActivity) {
////        ((DrawerLayout) mActivity.findViewById(R.id.drawer_layout)).closeDrawer(Gravity.START);
//    }

    public static void setSliderUI(final Activity mActivity, UserData userData) {


        check = 0;
        if (userData != null && userData.getData().getVendorDetails() != null) {
            ((TextView) mActivity.findViewById(R.id.tvChooseLanguage)).setHint(StorefrontCommonData.getString(mActivity, R.string.choose_language));
            ((TextView) mActivity.findViewById(R.id.tvChooseLanguage)).setText(StorefrontCommonData.getString(mActivity, R.string.choose_language));
            ((TextView) mActivity.findViewById(R.id.tvAllOrders)).setText(Utils.getCallTaskAs(userData, false, true));
            ((TextView) mActivity.findViewById(R.id.tvInbox)).setText(StorefrontCommonData.getTerminology().getNotifications(true));
            ((TextView) mActivity.findViewById(R.id.tvWallet)).setText(StorefrontCommonData.getTerminology().getWallet());
            ((TextView) mActivity.findViewById(R.id.tvGiftCard)).setText(StorefrontCommonData.getTerminology().getGiftCard());
            ((TextView) mActivity.findViewById(R.id.tvMyAddresses)).setText(StorefrontCommonData.getString(mActivity, R.string.my_addresses_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
            ((TextView) mActivity.findViewById(R.id.tvLoyaltyPoint)).setText(StorefrontCommonData.getTerminology().getLoyaltyPoints());
            ((TextView) mActivity.findViewById(R.id.tvPaymentMethod)).setText(StorefrontCommonData.getTerminology().getPayment(true));
            ((TextView) mActivity.findViewById(R.id.tvReferalAndEran)).setText(StorefrontCommonData.getTerminology().getReferral(true));
            ((TextView) mActivity.findViewById(R.id.tvFuguChat)).setText(StorefrontCommonData.getString(mActivity, R.string.chat_with_fugu));
            ((TextView) mActivity.findViewById(R.id.tvFuguChatCampaigns)).setText(StorefrontCommonData.getTerminology().getAnnouncements());
            ((TextView) mActivity.findViewById(R.id.tvDynamicPage)).setText(StorefrontCommonData.getTerminology().getDynamicPages());
            ((TextView) mActivity.findViewById(R.id.tvRewards)).setText(StorefrontCommonData.getTerminology().getRewards());
            ((TextView) mActivity.findViewById(R.id.tvSubscription)).setText(StorefrontCommonData.getString(mActivity, R.string.subscription_plan));
            ((TextView) mActivity.findViewById(R.id.tvDQRCode)).setText(StorefrontCommonData.getString(mActivity, R.string.scan_qr_code).replace(TerminologyStrings.QRCODE_QRCODE, StorefrontCommonData.getTerminology().getQrcode()));
            ((TextView) mActivity.findViewById(R.id.tvDQRCodeGuest)).setText(StorefrontCommonData.getString(mActivity, R.string.scan_qr_code).replace(TerminologyStrings.QRCODE_QRCODE, StorefrontCommonData.getTerminology().getQrcode()));


            final ImageView ivAvtar = mActivity.findViewById(R.id.ivAvtar);
            final View pbAvtar = mActivity.findViewById(R.id.pbAvtar);

            if (Dependencies.isDemoRunning() && !UIManager.isCustomerLoginRequired()) {
                ((TextView) mActivity.findViewById(R.id.tvCustomerNameSideMenu)).setText(StorefrontCommonData.getString(mActivity, (R.string.guest_user)));
                mActivity.findViewById(R.id.tvCustomerPhone).setVisibility(View.GONE);
                pbAvtar.setVisibility(View.GONE);
                ivAvtar.setImageResource(R.drawable.ic_profile_img_placeholder);
            } else {
                ((TextView) mActivity.findViewById(R.id.tvCustomerNameSideMenu)).setText(userData.getData().getVendorDetails().getFirstName());
                ((TextView) mActivity.findViewById(R.id.tvCustomerPhone)).setText(userData.getData().getVendorDetails().getPhoneNo());
                mActivity.findViewById(R.id.tvCustomerPhone).setVisibility(userData.getData().getVendorDetails().getPhoneNo().isEmpty() ? View.GONE : View.VISIBLE);

                final String profilePic = userData.getData().getVendorDetails().getVendorImage();

                if ((profilePic != null) && (!profilePic.equals(""))) {
                    pbAvtar.setVisibility(View.VISIBLE);
                    /*Glide.with(mActivity).load(profilePic)
                            .asBitmap()
                            .centerCrop()
                            .diskCacheStrategy(SOURCE)
                            .placeholder(AppCompatResources.getDrawable(mActivity, R.drawable.ic_profile_img_placeholder))
                            .into(new BitmapImageViewTarget(ivAvtar) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    pbAvtar.setVisibility(View.GONE);
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mActivity.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivAvtar.setImageDrawable(circularBitmapDrawable);
                                }
                            });*/

                    new GlideUtil.GlideUtilBuilder(ivAvtar)
                            .setLoadItem(profilePic)
                            .setCenterCrop(true)
                            .setPlaceholder(R.drawable.ic_profile_img_placeholder)
                            .setTransformation(new CircleCrop())
                            .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                                @Override
                                public void onLoadCompleted(@NonNull Object resource, @Nullable com.bumptech.glide.request.transition.Transition transition) {
                                    pbAvtar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onLoadCompleted(@NonNull Object resource, @Nullable com.bumptech.glide.request.transition.Transition transition, ImageView view) {

                                }

                                @Override
                                public void onLoadFailed() {
                                    pbAvtar.setVisibility(View.GONE);
                                }
                            }).build();

                } else {
                    pbAvtar.setVisibility(View.GONE);
                    ivAvtar.setImageResource(R.drawable.ic_profile_img_placeholder);
                }
            }

            LinearLayout llReferalEarnLayout = mActivity.findViewById(R.id.llReferalEarnLayout);
            llReferalEarnLayout.setVisibility(userData.getData().getReferral().getStatus() == 1 && Dependencies.getToggleView(mActivity) == 0 ? View.VISIBLE : View.GONE);

            LinearLayout llLoyaltyPoint = mActivity.findViewById(R.id.llLoyaltyPoint);
            llLoyaltyPoint.setVisibility(UIManager.getIsLoyaltyEnable() == 1 ? View.VISIBLE : View.GONE);

            LinearLayout llSubscriptionLayout = mActivity.findViewById(R.id.llSubscriptionLayout);
//            llSubscriptionLayout.setVisibility(StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1 ? View.VISIBLE : View.GONE);
            llSubscriptionLayout.setVisibility(StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionEnabled() == 1 ? View.VISIBLE : View.GONE);

            LinearLayout llDynamicPageLayout = mActivity.findViewById(R.id.llDynamicPageLayout);
            llDynamicPageLayout.setVisibility(StorefrontCommonData.getAppConfigurationData().getIsDynamicPagesActive() == 1 && StorefrontCommonData.getAppConfigurationData().getDynamicPagesDetails().size() > 0 ? View.VISIBLE : View.GONE);

            LinearLayout llFuguChatLayout = mActivity.findViewById(R.id.llFuguChatLayout);
            LinearLayout llFuguChatCampaigns = mActivity.findViewById(R.id.llFuguChatCampaigns);
            llFuguChatLayout.setVisibility(UIManager.isFuguChatEnabled() && Dependencies.getToggleView(mActivity) == 0 ? View.VISIBLE : View.GONE);
            llFuguChatCampaigns.setVisibility(UIManager.isFuguChatEnabled() ? View.VISIBLE : View.GONE);

            LinearLayout llWallet = mActivity.findViewById(R.id.llWallet);
            llWallet.setVisibility(PaymentMethodsClass.isInAppWalletPaymentEnabled() ? View.VISIBLE : View.GONE);

            LinearLayout llRewardsLayout = mActivity.findViewById(R.id.llRewardsLayout);
            llRewardsLayout.setVisibility(UIManager.isRewardsActive() ||
                    StorefrontCommonData.getUserData().getData().getVendorDetails().getIsRewardActive()
                    ? View.VISIBLE : View.GONE);

            LinearLayout llGiftCard = mActivity.findViewById(R.id.llGiftCard);
            llGiftCard.setVisibility(StorefrontCommonData.getAppConfigurationData().getIsGiftCardActivated() == 1 && PaymentMethodsClass.isInAppWalletPaymentEnabled() ? View.VISIBLE : View.GONE);

            (mActivity.findViewById(R.id.llChooseLanguage)).setVisibility(Dependencies.isDemoRunning() && UIManager.getLanguagesArrayList().size() > 1 ? View.VISIBLE : View.GONE);

            mActivity.findViewById(R.id.llPaymentMethodLayout).setVisibility(PaymentMethodsClass.getCardPaymentMethodsKeySet().size() > 0 || PaymentMethodsClass.isPaytmEnabled() ? View.VISIBLE : View.GONE);

            final TextView tvChooseLanguage = mActivity.findViewById(R.id.tvChooseLanguage);
            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                tvChooseLanguage.setText(StorefrontCommonData.getSelectedLanguageCode().getLanguageDisplayName());
            }
            Spinner spinnerChooseLanguage = mActivity.findViewById(R.id.spinnerChooseLanguage);
            LanguageSpinnerAdapter languageSpinnerAdapter = new LanguageSpinnerAdapter(mActivity, android.R.layout.simple_spinner_item, UIManager.getLanguagesArrayList());
            languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerChooseLanguage.setAdapter(languageSpinnerAdapter);
            for (int i = 0; i < StorefrontCommonData.getAppConfigurationData().getLanguages().size(); i++) {
                if (StorefrontCommonData.getSelectedLanguageCode() != null && StorefrontCommonData.getAppConfigurationData().getLanguages().get(i).getLanguageCode().equalsIgnoreCase(StorefrontCommonData.getSelectedLanguageCode().getLanguageCode())) {
                    spinnerChooseLanguage.setSelection(i);
                    break;
                }

            }
            spinnerChooseLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (++check > 1) {
                        LanguagesCode selectedLanguageCode = (LanguagesCode) parent.getItemAtPosition(position);
                        setLanguageChange(mActivity, selectedLanguageCode);
                        tvChooseLanguage.setText(selectedLanguageCode.getLanguageDisplayName());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            setSideMenuForDemo(mActivity);
        }
    }

    public static void setDualUserToggle(final Activity mActivity) {

        final LinearLayout llToggleView = mActivity.findViewById(R.id.llToggleView);
        final TextView tvToggleText = mActivity.findViewById(R.id.tvToggleText);
        tvToggleText.setText(StorefrontCommonData.getString(mActivity, R.string.switch_to_host));

        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")
                && !(Dependencies.isDemoRunning() && !UIManager.isCustomerLoginRequired())) {

            llToggleView.setVisibility(View.VISIBLE);

            final RadioGroup radioGroup = mActivity.findViewById(R.id.rbToggle);

            if (Dependencies.getToggleView(mActivity) == 0) {
                mActivity.findViewById(R.id.tvCatalog).setVisibility(View.GONE);
                mActivity.findViewById(R.id.llReferalEarnLayout).setVisibility(View.VISIBLE);

                radioGroup.clearCheck();
                tvToggleText.setText(StorefrontCommonData.getString(mActivity, R.string.switch_to_host));
//            int index = radioGroup.indexOfChild(radioGroup);
                radioGroup.check(R.id.rbGuest);
//            rbGuest.setChecked(true);
//            rbHost.setChecked(false);
            } else {
                mActivity.findViewById(R.id.tvCatalog).setVisibility(View.VISIBLE);
                mActivity.findViewById(R.id.llReferalEarnLayout).setVisibility(View.GONE);

                tvToggleText.setText(StorefrontCommonData.getString(mActivity, R.string.switch_to_guest));
                ((TextView) mActivity.findViewById(R.id.tvCatalog)).setText(StorefrontCommonData.getString(mActivity, R.string.catalog));
                radioGroup.clearCheck();
                radioGroup.check(R.id.rbHost);
            }


            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // checkedId is the RadioButton selected
                    View radioButton = radioGroup.findViewById(checkedId);
                    int index = radioGroup.indexOfChild(radioButton);

                    // Add logic here
                    switch (index) {
                        case 0: // first button
                            tvToggleText.setText(StorefrontCommonData.getString(mActivity, R.string.switch_to_host));
                            Dependencies.saveToggleView(mActivity, 0);
                            dualUserToggle(mActivity, Dependencies.getToggleView(mActivity));

                            break;
                        case 1: // secondbutton
                            tvToggleText.setText(StorefrontCommonData.getString(mActivity, R.string.switch_to_guest));
                            Dependencies.saveToggleView(mActivity, 1);
//                        Intent intent = new Intent(mActivity, HostHomeActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mActivity.startActivity(intent);
//                        mActivity.finishAffinity();
                            dualUserToggle(mActivity, Dependencies.getToggleView(mActivity));
                            break;
                    }
                }
            });
        } else if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
            llToggleView.setVisibility(View.VISIBLE);
        } else {
            llToggleView.setVisibility(View.GONE);
        }

    }

    public static void setSideMenuForDemo(Activity mActivity) {
        ((FrameLayout) mActivity.findViewById(R.id.frameOverlay)).setForeground(Dependencies.isDemoRunning() ? ContextCompat.getDrawable(mActivity, R.drawable.overlay_menu) : null);
        if (Dependencies.isDemoRunning()) {
            mActivity.findViewById(R.id.llQRLayoutGuest).setVisibility(View.VISIBLE);
            mActivity.findViewById(R.id.llQRLayout).setVisibility(View.GONE);
        } else {
            mActivity.findViewById(R.id.llQRLayoutGuest).setVisibility(View.GONE);
            mActivity.findViewById(R.id.llQRLayout).setVisibility(View.VISIBLE);
        }
        mActivity.findViewById(R.id.rbToggle).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.rbToggle).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.rbGuest).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.rbHost).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvCatalog).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvAllOrders).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvPaymentMethod).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvReferalAndEran).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvInbox).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvMyAddresses).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvFuguChat).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvFuguChatCampaigns).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvDynamicPage).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvLoyaltyPoint).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvWallet).setEnabled(!Dependencies.isDemoRunning());
        mActivity.findViewById(R.id.tvGiftCard).setEnabled(!Dependencies.isDemoRunning());
    }

    private static void setLanguageChange(final Activity mActivity, final LanguagesCode languageCode) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add("language", languageCode.getLanguageCode());

        RestClient.getApiInterface(mActivity).languageChange(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
//                LanguageStringsModel languageStringsModel  = (LanguageStringsModel) baseModel.toResponseModel(LanguageStringsModel.class);
//                try {
//                    languageStringsModel.setLanguageStrings();
//                } catch (Exception e) {
//                if (BuildConfig.DEBUG)
//                    Utils.printStackTrace(e);
//                }

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(baseModel.data));
                    Terminology terminology = new Gson().fromJson(jsonObject.getJSONObject("terminology").toString(), Terminology.class);
                    StorefrontCommonData.setTerminology(terminology);
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }


                LanguageStringsModel languageStringsModel = new LanguageStringsModel();
                try {
                    languageStringsModel.setLanguageStrings((Map<String, String>) baseModel.data);
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                StorefrontCommonData.setLanguageCode(languageCode);
                StorefrontCommonData.setLanguageStrings(mActivity, languageStringsModel.getLanguageStrings());
                setSliderUI(mActivity, StorefrontCommonData.getUserData());
                if (mActivity instanceof RestaurantListingActivity) {
                    ((RestaurantListingActivity) mActivity).setStrings();
                    ((RestaurantListingActivity) mActivity).restartActivity();

                } else if (mActivity instanceof HomeActivity) {
                    ((HomeActivity) mActivity).setStrings();
                    ((HomeActivity) mActivity).restartActivity();

                } else if (mActivity instanceof HyperlocalLandingActivity) {
                    ((HyperlocalLandingActivity) mActivity).restartActivity();
                } else if (mActivity instanceof MerchantCatalogActivity) {
                    ((MerchantCatalogActivity) mActivity).restartActivity();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                new AlertDialog.Builder(mActivity).message(error.getMessage()).button(StorefrontCommonData.getString(mActivity, R.string.retry_text)).listener(new AlertDialog.Listener() {
                    @Override
                    public void performPostAlertAction(int purpose, Bundle backpack) {
                        setLanguageChange(mActivity, languageCode);
                    }
                }).build().show();
            }
        });
    }

    /**
     * get AvailableDates call
     */
    private static void dualUserToggle(final Activity mActivity, final int toggleStatus) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("dual_user_current_status", toggleStatus);
        RestClient.getApiInterface(mActivity).dualUserToggle(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {

                if (Dependencies.getToggleView(mActivity) == 1) {
                    Intent intent = new Intent(mActivity, HostHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    mActivity.finishAffinity();
                } else {
                    Intent intent = new Intent(mActivity, RentalHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mActivity.startActivity(intent);
                    mActivity.finishAffinity();
                }


//                try {
//                } catch (ParseException e) {
//                if (BuildConfig.DEBUG)
//                    Utils.printStackTrace(e);
//                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });


    }

    private static void requestMarshMallowPermission(Activity mActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (mAlertDialog != null && mAlertDialog.isShowing())
                mAlertDialog.dismiss();
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
            builder.setMessage(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.please_grant_permission_to_access_camera) : CAMERA_READ_WRITE_PERMISSION_MSG)
                    .setPositiveButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.ok_text) : OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(mActivity, PERMISSION_CAMERA_READ_WRITE, PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE);
                        }
                    })
                    .setNegativeButton(mActivity != null ? StorefrontCommonData.getString(mActivity, R.string.cancel) : "CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mAlertDialog.dismiss();
                        }
                    });
            mAlertDialog = builder.create();
            mAlertDialog.show();
        } else {
            ActivityCompat.requestPermissions(mActivity, PERMISSION_CAMERA_READ_WRITE, PERMISSION_REQUEST_CODE_CAMERA_READ_WRITE);
        }
    }
}