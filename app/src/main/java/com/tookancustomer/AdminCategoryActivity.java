package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.adapters.AdminCatalogSuperCategoryAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.models.NLevelWorkFlowModel.NLevelWorkFlowData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminCategoryActivity extends BaseActivity implements View.OnClickListener, LocationFetcher.OnLocationChangedListener {
    private DrawerLayout mDrawerLayout;

    private RelativeLayout rlBack;
    private TextView tvHeading;

    private RecyclerView rvCategoriesList;
    private AdminCatalogSuperCategoryAdapter adapter;
    private ArrayList<Datum> adminCatalogueList = new ArrayList<>();

    private LinearLayout llNoCategoriesAvailable;
    public TextView tvAddress, tvNoCategoryFound;
    private Button btnGoToLocationActivity;

    private LocationFetcher locationFetcher;
    public Double latitude, longitude;
    public String address = "";
    public String headerName = "";

    boolean isLanguageChanged = false;
    private long lastBackPressed;
    private boolean isFirstScreen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        mActivity = this;

        if (getIntent() != null) {
            adminCatalogueList = (ArrayList<Datum>) getIntent().getExtras().getSerializable(ADMIN_CATALOGUE);
            if (getIntent().hasExtra(HEADER_NAME)) {
                isFirstScreen = false;
                headerName = getIntent().getStringExtra(HEADER_NAME);
            } else {
                isFirstScreen = true;
                headerName = StorefrontCommonData.getUserData().getData().getStoreName();
            }
            if (getIntent().hasExtra(PICKUP_LATITUDE)) {
                latitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
            }
            if (getIntent().hasExtra(PICKUP_LONGITUDE)) {
                longitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
            }
            if (getIntent().hasExtra(PICKUP_ADDRESS)) {
                address = getIntent().getStringExtra(PICKUP_ADDRESS);
            }
        }

        initViews();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(headerName);

        rvCategoriesList = findViewById(R.id.rvCategoriesList);
        rvCategoriesList.setLayoutManager(new GridLayoutManager(mActivity, 2, LinearLayoutManager.VERTICAL, false));
        rvCategoriesList.setItemAnimator(new DefaultItemAnimator());
        rvCategoriesList.setHasFixedSize(false);
//        rvCategoriesList.addItemDecoration(new SimpleDividerItemDecorationFull(mActivity));

        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setHint(StorefrontCommonData.getTerminology().getAddress());
        llNoCategoriesAvailable = findViewById(R.id.llNoCategoriesAvailable);
        tvNoCategoryFound = findViewById(R.id.tvNoCategoryFound);
        tvNoCategoryFound.setText(getStrings(R.string.no_restaurants_found_please_change_your_location));
        btnGoToLocationActivity = findViewById(R.id.btnGoToLocationActivity);
        btnGoToLocationActivity.setText(getStrings(R.string.select_location));
        btnGoToLocationActivity.setVisibility(View.GONE);
        Utils.setOnClickListener(this, rlBack, tvAddress, btnGoToLocationActivity);

        if (isFirstScreen) {
            ((ImageView) findViewById(R.id.ivBack)).setImageResource(R.drawable.ic_icon_menu);
            setAddressFetcher();
        } else {
            tvAddress.setVisibility(View.GONE);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        if (Dependencies.isEcomApp()) {
            tvAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                if (isFirstScreen) {
                    mDrawerLayout.openDrawer(Gravity.START);
                    Utils.setLightStatusBar(mActivity, mDrawerLayout);
                } else {
                    onBackPressed();
                }
                break;
            case R.id.tvAddress:
            case R.id.btnGoToLocationActivity:
                if (!Utils.internetCheck(this)) {
                    new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                    return;
                }
                if (Dependencies.isDemoRunning()) {
                    gotoMapActivity();
                } else {
                    gotoFavLocationActivity();
                }
                break;
        }
    }

    public void sideMenuClick(View v) {
        SideMenuTransition.sideMenuClick(v, mActivity);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Codes.Permission.LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationFetcher();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    Transition.openCheckoutActivity(mActivity, data.getExtras());
                }
                break;

            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    address = data.getStringExtra("address");
                    tvAddress.setText(data.getStringExtra("address"));
                    latitude = data.getDoubleExtra("latitude", 0.0);
                    longitude = data.getDoubleExtra("longitude", 0.0);
                    getAdminCatalog();
                }
                break;

            case Codes.Request.OPEN_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    StorefrontCommonData.setUserData((UserData) data.getExtras().getSerializable(UserData.class.getName()));
                    isLanguageChanged = data.getExtras().getBoolean("isLanguageChanged");
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                    if (isLanguageChanged) restartActivity();
                }
                break;

            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    startLocationFetcher();
                }
                break;
            case Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Dependencies.setDemoRun(false);
                } else {
                    Dependencies.setDemoRun(true);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

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
                    ActivityCompat.finishAffinity(this);
                    Transition.exit(this);
                }
            }
        } else {
            super.onBackPressed();
            AnimationUtils.exitTransition(mActivity);
        }
    }

    public void getAdminCatalog() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(LATITUDE, latitude != null ? latitude : 0)
                .add(LONGITUDE, longitude != null ? longitude : 0);

        if (commonParams.build().getMap().containsKey(USER_ID))
            commonParams.build().getMap().remove(USER_ID);

        RestClient.getApiInterface(mActivity).getAdminMerchantCatalog(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                NLevelWorkFlowData nLevelWorkFlowData = new NLevelWorkFlowData();
                try {
                    com.tookancustomer.models.NLevelWorkFlowModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.NLevelWorkFlowModel.Datum[].class);
                    nLevelWorkFlowData.setDataAdminCategory(new ArrayList<>(Arrays.asList(datum)));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }

                adminCatalogueList = nLevelWorkFlowData.getDataAdminCategory();
                setAdapter();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (adapter == null) {
                    llNoCategoriesAvailable.setVisibility(View.VISIBLE);
//                    btnGoToLocationActivity.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setAddressFetcher() {
        startLocationFetcher();
//        getCurrentLocation();
//        executeSetAddress();
    }

    private void startLocationFetcher() {
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

        if (latitude == null || longitude == null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (adapter == null) {
                executeSetAddress();
            }
        }
    }

    public void executeSetAddress() {
        Log.e("executeSetAddress", "executeSetAddress called");
        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }

    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");
            return MapUtils.getGapiJson(new LatLng(latitude, longitude), mActivity);
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null && jsonObject.has("address")) {
                    address = jsonObject.getString("address");
                    tvAddress.setText(jsonObject.getString("address"));
                }
                getAdminCatalog();
            } catch (JSONException e) {
            }
        }
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


    public void restartActivity() {
//        recreate();
        mDrawerLayout.closeDrawer(Gravity.START);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void setAdapter() {
        if (adminCatalogueList.size() == 0) {
            llNoCategoriesAvailable.setVisibility(View.VISIBLE);
//            btnGoToLocationActivity.setVisibility(View.GONE);
        } else {
            llNoCategoriesAvailable.setVisibility(View.GONE);
        }

        adapter = new AdminCatalogSuperCategoryAdapter(mActivity, adminCatalogueList);
        rvCategoriesList.setAdapter(adapter);
    }
}
