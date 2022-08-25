package com.tookancustomer.utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.Result;
import com.tookancustomer.HomeActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

import static com.tookancustomer.appdata.Codes.Request.OPEN_HOME_ACTIVITY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_QR_CODE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE_SELECTED_CATEGORIES;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA_ITEM_POS;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler, View.OnClickListener, LocationFetcher.OnLocationChangedListener {
    public static Camera cam = null;// has to be static, otherwise onDestroy() destroys it
    private ZXingScannerView mScannerView;
    private Activity mActivity;
    private CameraManager mCameraManager;
    private String mCameraId;
    private ImageView ivBack, ivFlashOn, ivFlashOff;
    private TextView tvHeader;
    private LocationFetcher locationFetcher;
    private Double latitude = 0.0, longitude = 0.0;
    private String address;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        mActivity = this;
        init();


    }

    private void init() {
        mScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        mScannerView.setBorderColor(getResources().getColor(R.color.colorAccent));
        mScannerView.setLaserColor(getResources().getColor(R.color.colorAccent));
        mScannerView.setLaserEnabled(false);

        ivBack = findViewById(R.id.ivBack);
        tvHeader = findViewById(R.id.tvHeader);
        ivFlashOn = findViewById(R.id.ivFlashOn);
        ivFlashOff = findViewById(R.id.ivFlashOff);
//        tvHeader.setText(StorefrontCommonData.getString(mActivity, R.string.scan_qr_code).replace(TerminologyStrings.QRCODE_QRCODE, StorefrontCommonData.getTerminology().getQrcode()));
        ivBack.setOnClickListener(this);
        ivFlashOn.setOnClickListener(this);
        ivFlashOff.setOnClickListener(this);
        startLocationFetcher();

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText().replaceAll("[^0-9]", "").trim()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        if (rawResult.getText().contains("single-store")) {
            if (Build.VERSION.SDK_INT >= 30) {
                launchNativeApi30(mActivity, Uri.parse(rawResult.getText()));
                new CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(mActivity, Uri.parse(rawResult.getText()));
            } else {
                openChromeCustomTab(rawResult.getText());
            }
        }

        if (rawResult.getText().contains("merchant_id") && !rawResult.getText().contains("single-store")) {
            Uri myString = Uri.parse(rawResult.getText());
            getSingleMerchantDetails(Integer.parseInt(Objects.requireNonNull(myString.getLastPathSegment())), new MerchantCallback() {
                @Override
                public void onFetchSingleMerchantData(Datum merchantData) {
                    if (merchantData.getCanServe() == 1) {
                        redirectToMerchant(merchantData, -1);
                    } else {
                        Utils.snackBar(mActivity, StorefrontCommonData.getTerminology().getStore() + " " + getString(R.string.unable_deliver_text));
                    }
                }
            });

        } else {
            if (!rawResult.getText().contains("StoreId")) {
                Utils.snackBar(this, StorefrontCommonData.getString(mActivity, R.string.qr_code_not_valid).replace(TerminologyStrings.QRCODE_QRCODE, StorefrontCommonData.getTerminology().getQrcode()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            } else {
                getSingleMerchantDetails(Integer.parseInt(rawResult.getText().replaceAll("[^0-9]", "").trim()), new MerchantCallback() {

                    @Override
                    public void onFetchSingleMerchantData(Datum merchantData) {
                        if (merchantData.getCanServe() == 1) {
                            redirectToMerchant(merchantData, -1);
                        } else {
                            Utils.snackBar(mActivity, StorefrontCommonData.getTerminology().getStore() + " " + getString(R.string.unable_deliver_text));
                        }
                    }
                });
                // If you would like to resume scanning, call this method b    elow:
                // mScannerView.resumeCameraPreview(this);
            }
        }
    }

    private void getSingleMerchantDetails(int merchantId,
                                          final MerchantCallback merchantCallback) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        //  final Location location = LocationUtils.getLastLocation(mActivity);

        //  if (mActivity instanceof RestaurantListingActivity) {
        commonParams.add(Keys.APIFieldKeys.LATITUDE, latitude)
                .add(Keys.APIFieldKeys.LONGITUDE, longitude);
        //  }
        commonParams.add(USER_ID, merchantId);
        commonParams.add(IS_QR_CODE, 1);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                merchantCallback.onFetchSingleMerchantData(cityStorefrontsModels.getData().get(0));
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 201 || error.getStatusCode() == 101) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }
            }

            ;
        });
    }

    /**
     * Method to check whether the TrackingData Permission
     * is Granted by the User
     */

    private boolean checkLocationPermissions() {
        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        return AppManager.getInstance().askUserToGrantPermission(mActivity, permissionsRequired, mActivity.getString(R.string.please_grant_permission_location_text), Codes.Permission.LOCATION);
    }

    public void startLocationFetcher() {
        if (!checkLocationPermissions()) {
            return;
        }
        if (!LocationUtils.isGPSEnabled(mActivity)) {
            LocationAccess.showImproveAccuracyDialog(mActivity);
            return;
        }
        // Start fetching the location
        if (locationFetcher == null) {
            locationFetcher = new LocationFetcher(this, Constants.TimeRange.LOCATION_FETCH_INTERVAL, Constants.LocationPriority.BEST);
        }
        locationFetcher.connect();
    }

    private void redirectToMerchant(com.tookancustomer.models.MarketplaceStorefrontModel.Datum
                                            merchantData, int position) {
        // final Location location = LocationUtils.getLastLocation(mActivity);


        if (merchantData == null) {
            return;
        }
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.RESTAURANT_CLICK, merchantData.getStoreName() + "");
        // MyApplication.getInstance().trackEventAppsFlyer(Constants.GoogleAnalyticsValues.RESTAURANT_CLICK, merchantData.getStoreName() + "");
        StorefrontCommonData.getUserData().getData().getVendorDetails().setUserId(merchantData.getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setUserId(merchantData.getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(merchantData.getMerchantMinimumOrder());

        if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(STOREFRONT_DATA, merchantData);
            bundle.putInt(STOREFRONT_DATA_ITEM_POS, position);

            bundle.putDouble(PICKUP_LATITUDE, latitude);
            bundle.putDouble(PICKUP_LONGITUDE, longitude);
            //  bundle.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);
            bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, "");

            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, OPEN_HOME_ACTIVITY);
            AnimationUtils.forwardTransition(mActivity);
            finish();
        } else {
            //Double latitude = 0.0, longitude = 0.0;
            String adminSelectedCategories = "", address = "";

            //   address = ((RestaurantListingActivity) mActivity).address;
            // latitude = location != null ? location.getLatitude() : 0;
            //longitude = location != null ? location.getLongitude() : 0;


            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                if (merchantData.getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                    for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                        Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(merchantData);
                    }
                }
            }
            StorefrontConfig.getAppCatalogueV2(mActivity,
                    merchantData.getStoreName(),
                    merchantData.getLogo(),
                    new LatLng(Double.valueOf(merchantData.getLatitude()),
                            Double.valueOf(merchantData.getLongitude())),
                    new LatLng(latitude, longitude), address,
                    merchantData,
                    adminSelectedCategories,
//                    ((RestaurantListingActivity) mActivity).isAllBusinessCategory ? null : ((RestaurantListingActivity) mActivity).businessCategoryId
                    null, false, 0);
            finish();

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivFlashOn:
                mScannerView.setFlash(true);
                ivFlashOn.setVisibility(View.GONE);
                ivFlashOff.setVisibility(View.VISIBLE);
                break;
            case R.id.ivFlashOff:
                mScannerView.setFlash(false);
                ivFlashOn.setVisibility(View.VISIBLE);
                ivFlashOff.setVisibility(View.GONE);
                break;
            default:
                break;


        }

    }

    @Override
    public void onLocationChanged(Location location, int priority) {
        if (location == null) {
            return;
        }

        // Check if no location is fetched
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }

        LocationUtils.saveLocation(mActivity, location);

//        if (latitude == null || longitude == null || latitude == 0.0 || longitude == 0.0) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        executeSetAddress();

//        }
        locationFetcher.destroy();

    }

    public void executeSetAddress() {
        //Log.e("executeSetAddress", "executeSetAddress called");

        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }

    public interface MerchantCallback {
        void onFetchSingleMerchantData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData);
    }

    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");
            return MapUtils.getGapiJson(new LatLng(latitude, longitude), mActivity);
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(final JSONObject jsonObject) {

            if (jsonObject != null && jsonObject.has("address"))
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            address = jsonObject.getString("address");
                        } catch (JSONException e) {
                        }
                    }
                });

          /*  Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);

            if (fragment instanceof RestaurantListFragment)
                ((RestaurantListFragment) fragment).getMarketplaceStorefronts();
            else if (fragment instanceof RestaurantListMapFragmnt)
                ((RestaurantListMapFragmnt) fragment).updateLocation();

            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                getNearestSavedAddress(true);
            }*/


        }
    }

    static boolean launchNativeApi30(Context context, Uri uri) {
        Intent nativeAppIntent = new Intent(Intent.ACTION_VIEW, uri)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER);
        try {
            context.startActivity(nativeAppIntent);
            return true;
        } catch (ActivityNotFoundException ex) {
            return false;
        }
    }

    private void openChromeCustomTab(String url) {

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(mActivity.getResources()
                        .getColor(R.color.colorPrimary))
                .setShowTitle(false)
                .setUrlBarHidingEnabled(true)
                .build();

// This is optional but recommended
        CustomTabsHelper.addKeepAliveExtra(mActivity, customTabsIntent.intent);

// This is where the magic happens...
        CustomTabsHelper.openCustomTab(mActivity, customTabsIntent,
                Uri.parse(url),
                new WebViewFallback());
    }

}

