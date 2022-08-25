package com.tookancustomer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.filter.FilterActivity;
import com.tookancustomer.filter.constants.FilterConstants;
import com.tookancustomer.filter.model.AllowedDataList;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.favLocations.FavouriteLocations;
import com.tookancustomer.models.favLocations.LocationsModel;
import com.tookancustomer.restorentListFragments.RestaurantListFragment;
import com.tookancustomer.restorentListFragments.RestaurantListMapFragmnt;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RestaurantListingActivity extends BaseActivity implements View.OnClickListener, Keys.APIFieldKeys, LocationFetcher.OnLocationChangedListener {
    public TextView tvHeading;
    public Double latitude = 0.0, longitude = 0.0;
    public String headerName = "", address = "";
    public Integer businessCategoryId = 0;
    public boolean isAllBusinessCategory = true;
    public ArrayList<com.tookancustomer.filter.model.Result> allFilterList;
    public ArrayList<com.tookancustomer.filter.model.Result> categoryFilterList;
    public ImageView ivMap;
    public int selectedPickupMode = Constants.SelectedPickupMode.NONE;
    public ImageView ivWishlist;
    private FrameLayout frame_container;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout rlBack, rlHeaderTextOption, rlFloating;
    private LinearLayout llAddressLayout;
    private long lastBackPressed;
    private LocationFetcher locationFetcher;
    private boolean isFirstScreen = true;
    private com.mapbox.mapboxsdk.maps.MapView mFlightMapView;
    private boolean restaurantIsGuest;
    private RelativeLayout rlMerchantFilter;
    private ImageView ivHeaderImageOption;
    private LinearLayout rlActionBar;

    public void onPageSelected() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment instanceof RestaurantListMapFragmnt) {

            RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(28, 60, 28, 0);
            rlActionBar.setLayoutParams(buttonLayoutParams);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.removeRule(RelativeLayout.BELOW);
            frame_container.setLayoutParams(params);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            setTheme(R.style.AppThemeMap);
        } else {
            RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonLayoutParams.setMargins(0, 0, 0, 0);
            rlActionBar.setLayoutParams(buttonLayoutParams);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.rlActionBar);
            frame_container.setLayoutParams(params);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            setTheme(R.style.AppTheme);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resturant_listing_new);
        mActivity = this;

        Log.e("rlEnd", "----------------------------------");
        restaurantIsGuest = Dependencies.getAccessToken(mActivity).isEmpty();

        Bundle bundle = getIntent().getExtras();

//        intentData

        if (getIntent().hasExtra(HEADER_NAME)) {
            isFirstScreen = false;
            headerName = getIntent().getStringExtra(HEADER_NAME);
        } else {
            isFirstScreen = true;
            headerName = StorefrontCommonData.getUserData() != null ? StorefrontCommonData.getUserData().getData().getStoreName() : "";
        }
        if (bundle != null) {

            if (bundle.getSerializable(PICKUP_ADDRESS) != null) {
                address = bundle.getString(PICKUP_ADDRESS);
                ((TextView) findViewById(R.id.tvHeading)).setText(address);
            }

            if (bundle.getSerializable(PICKUP_LATITUDE) != null) {
                latitude = bundle.getDouble(PICKUP_LATITUDE, 0.0);
            }
            if (bundle.getSerializable(PICKUP_LONGITUDE) != null) {
                longitude = bundle.getDouble(PICKUP_LONGITUDE, 0.0);
            }
            try {
                if (StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 1 && UIManager.getCustomOrderActive()) {
                    isFirstScreen = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!Dependencies.isDemoRunning()) {
            if (getIntent().getStringExtra(SUCCESS_MESSAGE) != null) {
                Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(SUCCESS_MESSAGE));
            } else if (getIntent().getStringExtra(FAILURE_MESSAGE) != null) {
                Utils.snackBar(mActivity, getIntent().getStringExtra(FAILURE_MESSAGE));
            }
        }

        initViews();
        callbackForAllFilters();
        setListFragment(bundle);


    }

    public void setListFragment(Bundle bundle) {
        Fragment fragment = new RestaurantListFragment();
        if (bundle != null)
            fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    public void setMapFragment(Bundle bundle) {
        if (checkLocationPermissions()) {
            Fragment fragment = new RestaurantListMapFragmnt();
//            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }
    }


    private void callbackForAllFilters() {

        CommonParams.Builder builder = new CommonParams.Builder()
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());


        RestClient.getApiInterface(this).getFilters(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, false, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        com.tookancustomer.filter.model.Data data = baseModel.toResponseModel(com.tookancustomer.filter.model.Data.class);
                        if (data.getIsFiltersEnabled() == 1) {
                            allFilterList = data.getResult();
                            rlMerchantFilter.setVisibility(View.VISIBLE);
                        } else {
                            rlMerchantFilter.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(Gravity.START, false);

        }
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
        setStrings();
        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            getNearestSavedAddress(false);
            ivWishlist.setVisibility(View.VISIBLE);
        } else {
            ivWishlist.setVisibility(View.GONE);

        }
    }

    public void setStrings() {
        tvHeading.setHint(getStrings(R.string.text_select_address_v2).replace(TerminologyStrings.ADDRESS, StorefrontCommonData.getTerminology().getAddress()));
    }

    public void sideMenuClick(View v) {
        SideMenuTransition.sideMenuClick(v, mActivity);
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        frame_container = findViewById(R.id.frame_container);
        rlActionBar = findViewById(R.id.rlActionBar);
        rlBack = findViewById(R.id.rlBack);
        ivMap = findViewById(R.id.ivMap);
        ivMap = findViewById(R.id.ivMap);
        ivWishlist = findViewById(R.id.ivWishlist);
        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            getNearestSavedAddress(false);
            ivWishlist.setVisibility(View.VISIBLE);
        } else {
            ivWishlist.setVisibility(View.GONE);

        }

        rlHeaderTextOption = findViewById(R.id.rlHeaderTextOption);
        rlMerchantFilter = findViewById(R.id.rlMerchantFilter);
        rlMerchantFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (businessCategoryId != 0 || !allFilterList.isEmpty() || categoryFilterList != null) {

                    Intent filterIntent = new Intent(RestaurantListingActivity.this, FilterActivity.class);
                    filterIntent.putExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ID, businessCategoryId);

                    filterIntent.putParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ALL, allFilterList);

                    if (categoryFilterList != null)
                        filterIntent.putParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY, categoryFilterList);

                    startActivityForResult(filterIntent, FilterConstants.REQUEST_CODE_TO_OPEN_FILTER);
                    AnimationUtils.BottonToTopTransition(RestaurantListingActivity.this);

                } else {
                    Utils.snackBar(mActivity, getStrings(R.string.no_filters_available));
                }
            }
        });

        ivHeaderImageOption = findViewById(R.id.ivHeaderImageOption);
        ivHeaderImageOption.setImageResource(R.drawable.ic_search_new_pressed);
        if (!Dependencies.isLaundryApp()) ivHeaderImageOption.setVisibility(View.VISIBLE);
        ivMap.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tvSubHeading)).setText(getStrings(R.string.all_stores).replace(STORES, StorefrontCommonData.getTerminology().getMerchants()));
        tvHeading = findViewById(R.id.tvHeading);
        llAddressLayout = findViewById(R.id.llAddressLayout);
        rlFloating = findViewById(R.id.rlFloating);
        Utils.setOnClickListener(this, ivMap, rlBack, llAddressLayout, ivWishlist);
        Utils.setOnClickListener(this, rlHeaderTextOption);

        if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
            selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;

        } else {
            if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1)
                selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
            else if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
                selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
        }


        if (isFirstScreen) {
            ((ImageView) findViewById(R.id.ivBack)).setImageResource(R.drawable.ic_icon_menu);
            latitude = UIManager.getLatitude();
            longitude = UIManager.getLongitude();
            address = UIManager.getAddress();
            if (address == null)
                setAddressFetcher();
            else {
                if (latitude == UIManager.getMerchetApiLatitudeSplash() &&
                        longitude == UIManager.getMerchantApiLongitudeSplash()) {
                    tvHeading.setText(address);
                    if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                        getNearestSavedAddress(false);
                    }
                } else {
                    latitude = UIManager.getMerchetApiLatitudeSplash();
                    longitude = UIManager.getMerchantApiLongitudeSplash();
                    executeSetAddress();
                }
            }
            //getMarketplaceStorefronts();
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            //getMarketplaceStorefronts();
//            llAddressLayout.setVisibility(View.GONE);
            latitude = UIManager.getLatitude();
            longitude = UIManager.getLongitude();
            address = UIManager.getAddress();
            if (address == null)
                setAddressFetcher();
            else {
                tvHeading.setText(address);
            }
            //getMarketplaceStorefronts();
        }

        if (isFirstScreen) {
            Utils.showPopup(mActivity, Codes.Request.OPEN_POPUP, new Utils.adDialogInterface() {
                @Override
                public void onAdDialogDismiss() {
                    StorefrontCommonData.getAppConfigurationData().setPopupEnabled(false);
                }
            });
        }


    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        if (v.getId() == R.id.ivWishlist) {
            Intent wishlist = new Intent(mActivity, WishListActivity.class);
            wishlist.putExtra(PICKUP_LATITUDE, latitude);
            wishlist.putExtra(PICKUP_LONGITUDE, longitude);
            mActivity.startActivityForResult(wishlist, WISHLIST_REQUEST_CODE);
        }
        if (v.getId() == R.id.llAddressLayout || v.getId() == R.id.btnGoToLocationActivity) {
            if (!Utils.internetCheck(this)) {
                new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                return;
            }
            if (Dependencies.isDemoRunning()) {
                gotoMapActivity();
            } else {
                gotoFavLocationActivity();
            }
        } else if (v.getId() == R.id.rlBack) {
            if (isFirstScreen) {
                mDrawerLayout.openDrawer(Gravity.START);
                Utils.setLightStatusBar(mActivity, mDrawerLayout);
            } else {
                onBackPressed();
            }
        } else if (v.getId() == R.id.rlHeaderTextOption) {
            Dependencies.setIsPreorderSelecetedForMenu(false);
            Bundle bundle = new Bundle();
            bundle.putDouble(PICKUP_LATITUDE, latitude);
            bundle.putDouble(PICKUP_LONGITUDE, longitude);
//            bundle.putString(PICKUP_ADDRESS, tvHeading.getText().toString());
            bundle.putString(PICKUP_ADDRESS, address);

            Transition.transitForResult(mActivity, MarketplaceSearchActivity.class, Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY, bundle, false);
//            activity.startActivityForResult(intent, OPEN_HOME_ACTIVITY);

        }


        if (v.getId() == R.id.ivMap) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("cityStorefrontsModel", cityStorefrontsModel);
//            Intent mapIntent = new Intent(mActivity, MapActivity.class);
//            mapIntent.putExtras(bundle);
//            startActivityForResult(mapIntent, OPEN_MAP_SCREEN);
        }


    }

    public void gotoFavLocationActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoMapActivity() {
//      Utils.searchPlace(mActivity, PlaceAutocomplete.MODE_FULLSCREEN, getCurrentLocation());
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codes.Request.OPEN_POPUP) {
            Utils.dismissAdDialog();
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment != null) {
            if (fragment instanceof RestaurantListFragment)
                ((RestaurantListFragment) fragment).onActivityResult(requestCode, resultCode, data);
            else if (fragment instanceof RestaurantListMapFragmnt)
                ((RestaurantListMapFragmnt) fragment).onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == WISHLIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ((RestaurantListFragment) fragment).getMarketplaceStorefronts();
            }
        }


    }

    public JSONObject generateFilterJsonObject() {
        ArrayList<com.tookancustomer.filter.model.Result> filterList = new ArrayList<>();
        if (categoryFilterList != null) {
            filterList.addAll(categoryFilterList);
        }
        filterList.addAll(allFilterList);

        JSONObject filterObject = new JSONObject();
        for (int i = 0; i < filterList.size(); i++) {
            com.tookancustomer.filter.model.Result filterData = filterList.get(i);
            ArrayList<AllowedDataList> allowedDataList = filterData.getAllowedDataList();
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < allowedDataList.size(); j++) {
                if (allowedDataList.get(j).isSelected()) {
                    jsonArray.put(allowedDataList.get(j).getValue());
                }
            }

            if (jsonArray.length() > 0) {
                try {
                    filterObject.put(filterData.getLabel(), jsonArray);
                } catch (JSONException e) {

                    Utils.printStackTrace(e);
                }
            }
        }

        return filterObject;

    }

    public boolean isAnyFilterApplied() {
        boolean isAnyFilterApplied = false;

        ArrayList<com.tookancustomer.filter.model.Result> filterList = new ArrayList<>();
        if (categoryFilterList != null) {
            filterList.addAll(categoryFilterList);
        }
        if (allFilterList != null) {
            filterList.addAll(allFilterList);
        }


        for (int i = 0; i < filterList.size(); i++) {
            com.tookancustomer.filter.model.Result filterData = filterList.get(i);
            ArrayList<AllowedDataList> allowedDataList = filterData.getAllowedDataList();
            for (int j = 0; j < allowedDataList.size(); j++) {
                if (allowedDataList.get(j).isSelected()) {
                    isAnyFilterApplied = true;
                    break;
                }
            }

            if (isAnyFilterApplied)
                break;

        }


        return isAnyFilterApplied;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Codes.Permission.LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationFetcher();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

    @SuppressLint("WrongConstant")
    private void performBackAction() {
        if (isFirstScreen) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
                Utils.setLightStatusBar(mActivity, mDrawerLayout);
            } else {
                long currentTimeStamp = System.currentTimeMillis();
                long difference = currentTimeStamp - lastBackPressed;

                if (difference > 2000) {
                    Utils.snackBar(this, getStrings(R.string.tap_again_to_exit_text), false);
                    lastBackPressed = currentTimeStamp;
                } else {
                    Transition.exit(this);
                }
            }
        } else {
            super.onBackPressed();
            AnimationUtils.exitTransition(mActivity);
        }
    }


    public void setAddressFetcher() {
        startLocationFetcher();
//        getCurrentLocation();
//        executeSetAddress();
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

    public void onLocationChanged(Location location, int priority) {
//        Log.e("onLocationChanged", "=" + location);
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

    private void getNearestSavedAddress(boolean updatelist) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("latitude", latitude);
        commonParams.add("longitude", longitude);

        RestClient.getApiInterface(this).getFavLocations(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                LocationsModel locationsModel = new LocationsModel();
                try {
                    locationsModel.setData(baseModel.toResponseModel(FavouriteLocations.class));

                    if (locationsModel != null && locationsModel.getData() != null && locationsModel.getData().getFavouriteLocations() != null
                            && locationsModel.getData().getFavouriteLocations().size() > 0) {
                        latitude = locationsModel.getData().getFavouriteLocations().get(0).getLatitude();
                        longitude = locationsModel.getData().getFavouriteLocations().get(0).getLongitude();
                        address = locationsModel.getData().getFavouriteLocations().get(0).getAddress();
                        tvHeading.setText(locationsModel.getData().getFavouriteLocations().get(0).getName());

                        if (updatelist) {
                            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);

                            if (fragment instanceof RestaurantListFragment)
                                ((RestaurantListFragment) fragment).getMarketplaceStorefronts();
                            else if (fragment instanceof RestaurantListMapFragmnt)
                                ((RestaurantListMapFragmnt) fragment).updateLocation();
                        }

                    }


                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                SetAddress setAddress = new SetAddress();
                setAddress.execute();
            }
        });
    }

    /**
     * Method to check whether the TrackingData Permission
     * is Granted by the User
     */

    private boolean checkLocationPermissions() {
        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        return AppManager.getInstance().askUserToGrantPermission(mActivity, permissionsRequired, getStrings(R.string.please_grant_permission_location_text), Codes.Permission.LOCATION);
    }

    @SuppressLint("WrongConstant")
    public void restartActivity() {
//        recreate();
        mDrawerLayout.closeDrawer(Gravity.START);
        if (getIntent().hasExtra(STOREFRONT_MODEL)) {
            getIntent().removeExtra(STOREFRONT_MODEL);
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
                            tvHeading.setText(jsonObject.getString("address"));
                            address = jsonObject.getString("address");
                        } catch (JSONException e) {
                        }
                    }
                });

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);

            if (fragment instanceof RestaurantListFragment)
                ((RestaurantListFragment) fragment).getMarketplaceStorefronts();
            else if (fragment instanceof RestaurantListMapFragmnt)
                ((RestaurantListMapFragmnt) fragment).updateLocation();

            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                getNearestSavedAddress(true);
            }


        }
    }
}
