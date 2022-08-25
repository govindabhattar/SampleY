package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class HyperlocalLandingActivity extends SideMenuBaseActivity implements View.OnClickListener {
    private RelativeLayout rlMenu;
    private ImageView ivBackgroundImage;
    private Button btnGoToMerchantList, btnGoToCustomOrder;
    private CityStorefrontsModel cityStorefrontsModel;
    private String preOrderDateTime;
    boolean isLanguageChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        addTopLayout(mActivity, R.layout.activity_hyperlocal_landing);
        gettingIntent();
        initViews();
        setBackgroundImage();
    }

    private void gettingIntent() {
        if (getIntent().hasExtra(STOREFRONT_MODEL)) {
            cityStorefrontsModel = (CityStorefrontsModel) getIntent().getSerializableExtra(STOREFRONT_MODEL);
        }
    }

    private void initViews() {
        rlMenu = findViewById(R.id.rlMenu);
        ivBackgroundImage = findViewById(R.id.ivBackgroundImage);
        btnGoToMerchantList = findViewById(R.id.btnGoToMerchantList);
        btnGoToMerchantList.setText(StorefrontCommonData.getTerminology().getGoToMarketplace());
        btnGoToCustomOrder = findViewById(R.id.btnGoToCustomOrder);
        btnGoToCustomOrder.setVisibility(UIManager.getCustomOrderActive() ? View.VISIBLE : View.GONE);
        btnGoToCustomOrder.setText(StorefrontCommonData.getTerminology().getCustomOrder());

        Utils.setOnClickListener(this, rlMenu, btnGoToMerchantList, btnGoToCustomOrder);
        Utils.showPopup(mActivity, Codes.Request.OPEN_POPUP, new Utils.adDialogInterface() {
            @Override
            public void onAdDialogDismiss() {
                StorefrontCommonData.getAppConfigurationData().setPopupEnabled(false);
            }
        });

    }

    /**
     * set Background Image
     */
    private void setBackgroundImage() {
        String bgImage = "";
        if (StorefrontCommonData.getAppConfigurationData().getMobileBackgroundImage() != null && !StorefrontCommonData.getAppConfigurationData().getMobileBackgroundImage().isEmpty()) {
            bgImage = StorefrontCommonData.getAppConfigurationData().getMobileBackgroundImage();

        } else if (StorefrontCommonData.getFormSettings().getBackgroundImage() != null) {
            bgImage = StorefrontCommonData.getFormSettings().getBackgroundImage();
        }


        (findViewById(R.id.rlTintLayout)).setBackground(ContextCompat.getDrawable(mActivity, R.drawable.overlay_selected_product));

        new GlideUtil.GlideUtilBuilder(ivBackgroundImage)
                    .setLoadItem(bgImage)
                    .setCenterCrop(true).build();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlMenu:
                openSideMenu();
                break;
            case R.id.btnGoToMerchantList:
                final Location location = LocationUtils.getLastLocation(mActivity);
                if (StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 1) {
                    if (cityStorefrontsModel != null && cityStorefrontsModel.getData().size() == 1) {
                        if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
                            StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModel.getData().get(0).getMerchantMinimumOrder());
                            Intent intent = new Intent(mActivity, HomeActivity.class);
                            Bundle extras = new Bundle();
                            extras.putSerializable(STOREFRONT_DATA, cityStorefrontsModel.getData().get(0));
                            extras.putDouble(PICKUP_LATITUDE, location != null ? location.getLatitude() : 0);
                            extras.putDouble(PICKUP_LONGITUDE, location != null ? location.getLongitude() : 0);
                            extras.putBoolean(IS_SIDE_MENU, false);
                            extras.putBoolean("isDirect", true);
                            intent.putExtras(extras);
                            startActivity(intent);
                            AnimationUtils.forwardTransition(mActivity);
                        } else {
                            final com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData = cityStorefrontsModel.getData().get(0);
                            StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModel.getData().get(0).getMerchantMinimumOrder());
                            if (preOrderDateTime == null)
                                Dependencies.setIsPreorderSelecetedForMenu(false);
                            if (cityStorefrontsModel.getData().get(0) != null && Dependencies.getSelectedProductsArrayList().size() > 0) {
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
                                            null, storefrontData, "", 0,false,0);

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
                        transitToRestaurants();
                    }
                } else {
                    transitToRestaurants();
                }
                break;
            case R.id.btnGoToCustomOrder:
                Bundle extraa = new Bundle();
//                extraa.putDouble(PICKUP_LATITUDE, latitude);
//                extraa.putDouble(PICKUP_LONGITUDE, longitude);
//                extraa.putString(PICKUP_ADDRESS, tvHeading.getText().toString());
                extraa.putBoolean("isCustomOrder", true);
                Transition.openCustomCheckoutActivity(mActivity, extraa);
                break;
        }
    }

    private void transitToRestaurants() {
        final Location location = LocationUtils.getLastLocation(mActivity);

        Intent intent = new Intent(mActivity, RestaurantListingActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable(STOREFRONT_MODEL, cityStorefrontsModel);
        extras.putDouble(PICKUP_LATITUDE, location != null ? location.getLatitude() : 0);
        extras.putDouble(PICKUP_LONGITUDE, location != null ? location.getLongitude() : 0);

        intent.putExtras(extras);
        startActivity(intent);
        AnimationUtils.forwardTransition(mActivity);
    }

    @Override
    public void onBackPressed() {
        performMainBackPress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codes.Request.OPEN_POPUP) {
            Utils.dismissAdDialog();
            return;
        }
        switch (requestCode) {
            case Codes.Request.OPEN_CUSTOM_CHECKOUT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                }
                break;
            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    Transition.openCustomCheckoutActivity(mActivity, data.getExtras());
                }
                break;
            case Codes.Request.OPEN_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    isLanguageChanged = data.getExtras().getBoolean("isLanguageChanged", false);
                    if (isLanguageChanged) restartActivity();
                }
                break;
        }
    }

    public void getSingleMerchantDetails(final int storefrontId) {
        final Location location = LocationUtils.getLastLocation(mActivity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.LATITUDE, location != null ? location.getLatitude() : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, location != null ? location.getLongitude() : 0);
        commonParams.add(USER_ID, storefrontId);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<Datum>(Arrays.asList(datum)));

                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                if (cityStorefrontsModels.getData().size() == 1) {
                    StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModels.getData().get(0).getMerchantMinimumOrder());

                    Intent intent = new Intent(mActivity, HomeActivity.class);
                    Bundle extras = new Bundle();

                    extras.putSerializable(STOREFRONT_DATA, cityStorefrontsModels.getData().get(0));
                    extras.putDouble(PICKUP_LATITUDE, location != null ? location.getLatitude() : 0);
                    extras.putDouble(PICKUP_LONGITUDE, location != null ? location.getLongitude() : 0);
                    extras.putBoolean(IS_SIDE_MENU, false);
                    extras.putBoolean("isDirect", true);

                    intent.putExtras(extras);
                    startActivity(intent);
                    AnimationUtils.forwardTransition(mActivity);
                } else {
                    Intent intent = new Intent(mActivity, RestaurantListingActivity.class);
                    startActivity(intent);
                    AnimationUtils.forwardTransition(mActivity);

                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Intent intent = new Intent(mActivity, RestaurantListingActivity.class);
                startActivity(intent);
                AnimationUtils.forwardTransition(mActivity);
            }
        });
    }

    public void restartActivity() {
//        recreate();
        if (getIntent().hasExtra(STOREFRONT_MODEL)) {
            getIntent().removeExtra(STOREFRONT_MODEL);
        }

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}