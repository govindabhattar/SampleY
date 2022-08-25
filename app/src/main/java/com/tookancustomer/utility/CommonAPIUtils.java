package com.tookancustomer.utility;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.GuestLoginActivity;
import com.tookancustomer.HomeActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.RentalHomeActivity;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.SignInActivity;
import com.tookancustomer.UserDebtActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.NLevelWorkFlowModel.NLevelWorkFlowData;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.customerSubscription.CustomerSubscriptionActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tookancustomer.appdata.Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT;
import static com.tookancustomer.appdata.Codes.Request.OPEN_OTP_SCREEN;
import static com.tookancustomer.appdata.Codes.Request.OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY;
import static com.tookancustomer.appdata.Constants.MERCHANT_PAGINATION_LIMIT;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.CITY_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.CITY_NAME;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LATITUDE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LONGITUDE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.SEARCH_TEXT;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;

/**
 * Created by cl-macmini-85 on 24/04/17.
 */

public class CommonAPIUtils implements Keys.Extras {

    private static int selectedPickupMode = Constants.SelectedPickupMode.NONE;
    private static int dummyCounter = 1;
    private static String preOrderDateTime;

    public static void userLoginNavigation(final Activity mActivity, boolean isLoginFromCheckout) {
        UserData userData = StorefrontCommonData.getUserData();

        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), userData);

        Intent intent;

        if ((UIManager.isOTPAvailable() && userData.getData().getVendorDetails().getIsPhoneVerified() == 0)
                || (UIManager.getIsEmailVerificationRequried() == 1 && userData.getData().getVendorDetails().getIsEmailVerified() == 0)
                || (StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1 && userData.getData().getVendorDetails().getSubscriptionPlan().get(0).getPaid() == 0)) {

            intent = new Intent(mActivity, RegistrationOnboardingActivity.class);
            extras.putBoolean(IS_LOGIN_FROM_CHECKOUT, isLoginFromCheckout);
            intent.putExtras(extras);
            mActivity.startActivityForResult(intent, OPEN_OTP_SCREEN);
            AnimationUtils.forwardTransition(mActivity);

        } else if ((userData.getData().getVendorDetails().getRegistrationStatus() != Constants.RegistrationStatus.VERIFIED)
                && userData.getData().getSignupTemplateData() != null
                && !userData.getData().getSignupTemplateData().isEmpty()) {
            intent = new Intent(mActivity, RegistrationOnboardingActivity.class);
            extras.putBoolean(IS_LOGIN_FROM_CHECKOUT, isLoginFromCheckout);
            intent.putExtras(extras);

            if (!isLoginFromCheckout) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityCompat.finishAffinity(mActivity);
            }

            if (isLoginFromCheckout) {
                mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
            } else {
                mActivity.startActivityForResult(intent, OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY);
            }
            AnimationUtils.forwardTransition(mActivity);

        } else if (StorefrontCommonData.getAppConfigurationData().getIsDebtEnabled() == 1 && userData.getData().getVendorDetails().getDebtAmount() > 0) {

            intent = new Intent(mActivity, UserDebtActivity.class);
            extras.putBoolean(IS_LOGIN_FROM_CHECKOUT, isLoginFromCheckout);
            intent.putExtras(extras);

            if (isLoginFromCheckout) {
                mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityCompat.finishAffinity(mActivity);
                mActivity.startActivityForResult(intent, OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY);
            }

            AnimationUtils.forwardTransition(mActivity);

        } else if (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionEnabled() == 1
                && userData.getData().getVendorDetails().getIsCustomerSubscriptionPlanExpired() == 1) {
            Log.e("insideElseif:", "OpenSubscription");
            OpenSubscription(mActivity, isLoginFromCheckout, extras);
        } else if (isLoginFromCheckout) {
            Dependencies.setDemoRun(false);

            Bundle bundleExtra = mActivity.getIntent().getExtras();
            bundleExtra.putSerializable(UserData.class.getName(), userData);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(bundleExtra);
            mActivity.setResult(RESULT_OK, returnIntent);
            mActivity.finish();
            AnimationUtils.exitTransition(mActivity);
        } else {
            CommonAPIUtils.getSuperCategories(userData, mActivity);
        }
    }

    private static void OpenSubscription(Activity mActivity, boolean isLoginFromCheckout, Bundle extras) {
        Dependencies.setDemoRun(false);

        Intent intent = new Intent(mActivity, CustomerSubscriptionActivity.class);
        extras.putBoolean(IS_LOGIN_FROM_CHECKOUT, isLoginFromCheckout);
        intent.putExtras(extras);

        if (isLoginFromCheckout) {
            mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityCompat.finishAffinity(mActivity);
            mActivity.startActivityForResult(intent, OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY);
        }

        AnimationUtils.forwardTransition(mActivity);
    }

    public static void getSuperCategories(final UserData userData, final Activity mActivity) {
        if (MyApplication.getInstance().getDeepLinkMerchantId() != 0) {
            Intent intent = new Intent(mActivity, HomeActivity.class);
            Bundle extras = new Bundle();
            extras.putInt("MERCHANT_ID", MyApplication.getInstance().getDeepLinkMerchantId());
            extras.putBoolean("FROM_DEEP_LINK", true);
            extras.putBoolean("FROM_DEEP_LINK_SIGN_IN", true);
            intent.putExtras(extras);
            mActivity.startActivity(intent);
            AnimationUtils.forwardTransition(mActivity);
           // finish();
        } else {
            getSuperCategories(userData, mActivity, false);
        }
    }

    public static void getSuperCategories(final UserData userData, final Activity mActivity, boolean showLoading) {

        if (Dependencies.isEcomApp()
            /*userData.getData().getAdminCategoryEnabled() == 1*/) {
            getAdminCategories(mActivity);
        } else if (UIManager.getNlevelEnabled() == 1 /*userData.getData().getAdminCategoryEnabled() == 1*/) {
            getAdminCategories(mActivity);
        } else if (StorefrontCommonData.getFormSettings().getProductView() == 1) {
            getStorefronts(mActivity, userData, showLoading);
        } else {
            navigateHomeScreen(userData, mActivity, showLoading);
        }

    }

    public static void navigateHomeScreen(final UserData userData, final Activity mActivity, boolean showLoading) {
        UIManager.setTotalStores(StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size());

        if (!UIManager.getCustomOrderActive() && StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 1
                && StorefrontCommonData.getFormSettings().getProductView() == 0) {

            StorefrontCommonData.getUserData().getData().getVendorDetails().setUserId(StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().get(0));
            StorefrontCommonData.getFormSettings().setUserId(StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().get(0));
            getMarketplaceStorefronts(mActivity, StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().get(0), userData, showLoading);

        } else {
            if (UIManager.getCustomOrderActive() && StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 0 &&
                    StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchHomeContent(mActivity, userData, null);
                    }
                }, 500);
            } else {
                getStorefronts(mActivity, userData, showLoading);
            }

        }
    }


    private static void launchHomeContent(Activity mActivity, UserData userData, CityStorefrontsModel cityStorefrontsModel) {
       /* if (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionEnabled() == 1 && userData.getData().getVendorDetails().getIsCustomerSubscriptionPlanExpired() == 1) {
            Bundle extras = new Bundle();
            extras.putSerializable(UserData.class.getName(), userData);
            Log.e("insideElseif:", "OpenSubscription");
            OpenSubscription(mActivity, false, extras);
        } else {*/
        Intent intent = new Intent(mActivity, Transition.launchHomeActivity());
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), userData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        extras.putSerializable("storefrontModel", cityStorefrontsModel);
        intent.putExtras(extras);
        mActivity.startActivity(intent);
        ActivityCompat.finishAffinity(mActivity);
        AnimationUtils.forwardTransition(mActivity);

    }


    public static void getMarketplaceStorefronts(final Activity mActivity, final int storefrontId, final UserData userData, final boolean showLoading) {
        final Location location = LocationUtils.getLastLocation(mActivity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.LATITUDE, location != null ? location.getLatitude() : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, location != null ? location.getLongitude() : 0);
        commonParams.add(USER_ID, storefrontId);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, showLoading, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>(Arrays.asList(datum)));


                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                if (cityStorefrontsModels.getData().size() == 1) {
                    if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
                        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModels.getData().get(0).getMerchantMinimumOrder());

                        Intent intent = new Intent(mActivity, HomeActivity.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable(STOREFRONT_DATA, cityStorefrontsModels.getData().get(0));
                        extras.putDouble(PICKUP_LATITUDE, location != null ? location.getLatitude() : 0);
                        extras.putDouble(PICKUP_LONGITUDE, location != null ? location.getLongitude() : 0);
                        extras.putBoolean(IS_SIDE_MENU, true);
                        extras.putBoolean("isDirect", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(extras);
                        mActivity.startActivity(intent);
                        ActivityCompat.finishAffinity(mActivity);
                        AnimationUtils.forwardTransition(mActivity);
                    } else {
                        final com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData = cityStorefrontsModels.getData().get(0);
                        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModels.getData().get(0).getMerchantMinimumOrder());
                        if (preOrderDateTime == null)
                            Dependencies.setIsPreorderSelecetedForMenu(false);
                        if (cityStorefrontsModels.getData().get(0) != null && Dependencies.getSelectedProductsArrayList().size() > 0) {
                            if (storefrontData.getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                                    Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(storefrontData);
                                }
                            }
                        }
                        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.RESTAURANT_ORDER_ONLINE, storefrontData.getStoreName() + "");
                        if (UIManager.isMenuEnabled() && storefrontData.getIsMenuEnabled() &&
                                storefrontData.getScheduledTask() == 1 &&
                                storefrontData.getIsStorefrontOpened() == 0) {
                            new SelectPreOrderTimeDialog(mActivity,
                                    new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                                        @Override
                                        public void onDateTimeSelected(String dateTime) {
                                            StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                                    storefrontData.getLogo(),
                                                    new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                                            Double.valueOf(storefrontData.getLongitude())),
                                                    new LatLng(location != null ? location.getLatitude() : 0, location != null ? location.getLongitude() : 0),
                                                    null,
                                                    storefrontData,
                                                    "", dateTime, 0);
                                        }
                                    }).setStorefrontData(storefrontData)
                                    .show();
//                        selectPreOrderTime();
                        } else {
                            if (preOrderDateTime == null)
                                StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                        storefrontData.getLogo(), new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                                Double.valueOf(storefrontData.getLongitude())), new LatLng(location != null ? location.getLatitude() : 0, location != null ? location.getLongitude() : 0),
                                        null, storefrontData, "", 0, false, 0);

                            else
                                StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                        storefrontData.getLogo(),
                                        new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                                Double.valueOf(storefrontData.getLongitude())),
                                        new LatLng(location != null ? location.getLatitude() : 0, location != null ? location.getLongitude() : 0),
                                        null,
                                        storefrontData,
                                        "", preOrderDateTime, 0);
                        }

                    }
                } else {
                    launchHomeContent(mActivity, userData, cityStorefrontsModels);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                getStorefronts(mActivity, userData, showLoading);
            }
        });
    }

    public static void getStorefronts(final Activity mActivity, final UserData userData, boolean showLoading) {
        final Location location = LocationUtils.getLastLocation(mActivity);

        UIManager.setMerchantApiLongitudeSplash(location.getLongitude());
        UIManager.setMerchetApiLatitudeSplash(location.getLatitude());

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(CITY_ID, "")
                .add(CITY_NAME, "")
                .add(LATITUDE, location != null ? location.getLatitude() : 0)
                .add(LONGITUDE, location != null ? location.getLongitude() : 0)
                .add(SEARCH_TEXT, "");


        commonParams.build().getMap().remove(USER_ID);

        if (selectedPickupMode == 0) {
            switch (StorefrontCommonData.getAppConfigurationData().getDefaultDeliveryMethod()) {
                case Constants.DeliveryMode.PICK_AND_DROP:
                    selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
                    break;
                case Constants.DeliveryMode.HOME_DELIVERY:
                    selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
                    break;
                case Constants.DeliveryMode.SELF_PICKUP:
                    selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
                    break;
            }
        }

        if ((StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1)
                || (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
                || (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1)
                || (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1)


        ) {


            commonParams.add("home_delivery", selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY ? 1 : 0);
            commonParams.add("self_pickup", selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP ? 1 : 0);
            commonParams.add("pick_and_drop", selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP ? 1 : 0);


        }


        if (StorefrontCommonData.getUserData().getData().getAdminCategoryEnabled() == 1) {

            RestClient.getApiInterface(mActivity).getAdminMerchantList(commonParams.build().getMap())
                    .enqueue(getMerchantListResponseResolver(mActivity, userData, showLoading));
        } else {

            JSONObject filterObject = null;

            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
            }

            commonParams.add("skip", 0);
            commonParams.add("limit", MERCHANT_PAGINATION_LIMIT);

            RestClient.getApiInterface(mActivity).getMarketplaceStorefronts(commonParams.build().getMap(), filterObject)
                    .enqueue(getMerchantListResponseResolver(mActivity, userData, showLoading));
        }
    }

    private static ResponseResolver<BaseModel> getMerchantListResponseResolver(final Activity mActivity, final UserData userData, final boolean showLoading) {
        return new ResponseResolver<BaseModel>(mActivity, showLoading, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                UIManager.setTotalStores(cityStorefrontsModels.getData().size());

                if (cityStorefrontsModels.getData().size() > 0) {
                    for (int i = 0; i < cityStorefrontsModels.getData().size(); i++) {
                        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                            if (cityStorefrontsModels.getData().get(i).getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                                    Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(cityStorefrontsModels.getData().get(i));
                                }
                            }
                        }
                        if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
                            cityStorefrontsModels.getData().get(i).setSelectedPickupMode(selectedPickupMode);
                        } else {
                            cityStorefrontsModels.getData().get(i).setSelectedPickupMode(0);
                        }
                    }
                }

                launchHomeContent(mActivity, userData, cityStorefrontsModels);


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() != Codes.StatusCode.INVALID_ACCESS_TOKEN.getStatusCode())
                    new AlertDialog.Builder(mActivity).message(error.getMessage()).button(StorefrontCommonData.getString(mActivity, R.string.retry_text)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            getStorefronts(mActivity, userData, true);
                        }
                    }).build().show();
                else
                    launchSignInScreen(mActivity);

            }
        };
    }

    private static void launchSignInScreen(Activity mActivity) {
        Intent intent = new Intent(mActivity, SignInActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }


    public static void getAdminCategories(final Activity mActivity) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(mActivity).getAdminMerchantCatalog(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                NLevelWorkFlowData nLevelWorkFlowData = new NLevelWorkFlowData();
                try {
                    com.tookancustomer.models.NLevelWorkFlowModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.NLevelWorkFlowModel.Datum[].class);
                    nLevelWorkFlowData.setDataAdminCategory(new ArrayList<>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                boolean moreThanOneCategoryLevel = false;

                for (int i = 0; i < nLevelWorkFlowData.getDataAdminCategory().size(); i++) {
                    if (!nLevelWorkFlowData.getDataAdminCategory().get(i).getSubCategories().isEmpty()) {
                        moreThanOneCategoryLevel = true;
                        break;
                    }
                }

                Bundle extras = new Bundle();
                extras.putSerializable(ADMIN_CATALOGUE, nLevelWorkFlowData.getDataAdminCategory());
                Intent intent;

                if (moreThanOneCategoryLevel) {
                    intent = new Intent(mActivity, Transition.launchHomeActivity());
                } else if (StorefrontCommonData.getFormSettings().getProductView() == 1) {
                    intent = new Intent(mActivity, RentalHomeActivity.class);
                } else {
                    intent = new Intent(mActivity, RestaurantListingActivity.class);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(extras);
                mActivity.startActivity(intent);
                ActivityCompat.finishAffinity(mActivity);
                AnimationUtils.forwardTransition(mActivity);

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    public static void getDummyUserDetailsForDemo(final Activity mActivity, final String countryCode, final String continentCode) {
        Location location = LocationUtils.getLastLocation(mActivity);

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


        commonParams.add("lat", location.getLatitude())
                .add("lng", location.getLongitude())
                .add("access_token", Dependencies.getAccessToken(mActivity))
                .add("user_id", StorefrontCommonData.getAppConfigurationData().getUserId())
                .add("form_id", StorefrontCommonData.getAppConfigurationData().getFormId())
                .add("reference_id", Dependencies.getReferenceId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        RestClient.getApiInterface(mActivity)
                .getDummyUserDetailsForDemo(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        if (Dependencies.getSelectedProductsArrayList() == null && Dependencies.getSelectedProductsArrayList().size() <= 0)
                            Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());
                        UserData userData = new UserData();
                        try {
                            userData.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                        }
                        boolean isUserDataNull = StorefrontCommonData.getUserData() == null;
                        Utils.saveUserInfo(mActivity, userData);
                        if (isUserDataNull) {
                            Dependencies.setDemoRun(true);
                            getSuperCategories(userData, mActivity);
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        ProgressDialog.dismiss();
                        if (StorefrontCommonData.getUserData() == null) {
                            new AlertDialog.Builder(mActivity).message(error.getMessage()).button(StorefrontCommonData.getString(getApplicationContext(), R.string.retry_text)).listener(new AlertDialog.Listener() {
                                @Override
                                public void performPostAlertAction(int purpose, Bundle backpack) {
                                    CommonAPIUtils.getDummyUserDetailsForDemo(mActivity, countryCode, continentCode);
                                }
                            }).build().show();
                        } else {
                            if (dummyCounter <= 3) {
                                if (mActivity != null) {
                                    CommonAPIUtils.getDummyUserDetailsForDemo(mActivity, countryCode, continentCode);
                                } else {
                                    if (getApplicationContext() instanceof Activity)
                                        CommonAPIUtils.getDummyUserDetailsForDemo((Activity) getApplicationContext(), countryCode, continentCode);
                                }
                                ++dummyCounter;
                            }
                        }
                    }
                });
    }

}
