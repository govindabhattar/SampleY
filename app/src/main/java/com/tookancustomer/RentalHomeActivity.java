package com.tookancustomer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.bumptech.glide.request.animation.GlideAnimation;
import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RentalHomeActivity extends BaseActivity implements View.OnClickListener, Keys.APIFieldKeys, LocationFetcher.OnLocationChangedListener, DatePickerDialog.OnDateSetListener {

    private UserData userData;

    private Button btnFindHomes;
    public TextView tvAddress, tvWelcomeText;
    public Double latitude, longitude;

    //    private int valueEnabledPayment;
    private long lastBackPressed;

    private LocationFetcher locationFetcher;
    private String checkInDate, checkOutDate;
    private LinearLayout llCheckIn, llCheckOut, llfilter;
    private MaterialEditText etCheckInDate, etCheckOutDate;
    private RelativeLayout rlParent;
    private boolean dateType = false;
    private Calendar selectedStartDate;
    private Calendar selectedEndDate;
    /**
     * For ECOM flow
     * Seller view with seller name and price
     * and other sellers view
     */
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_home);
        mActivity = this;
        userData = StorefrontCommonData.getUserData();

//        showPaymentSlider();

        if (!Dependencies.isDemoRunning()) {
            if (getIntent().getStringExtra(SUCCESS_MESSAGE) != null) {
                Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(SUCCESS_MESSAGE));
            } else if (getIntent().getStringExtra(FAILURE_MESSAGE) != null) {
                Utils.snackBar(mActivity, getIntent().getStringExtra(FAILURE_MESSAGE));
            }
        }

        if (Dependencies.isEcomApp()) {
            if (getIntent().getExtras().containsKey(PARENT_CATEGORY_ID)) {
                categoryId = getIntent().getExtras().getString(PARENT_CATEGORY_ID);
            }
        }

        initViews();

        Location location = LocationUtils.getLastLocation(mActivity);
        latitude = location != null ? location.getLatitude() : 0.0;
        longitude = location != null ? location.getLongitude() : 0.0;
        setAddressFetcher();

    }

    /**
     * set Background Image
     */
    private void setBackgroundImage() {
        if (StorefrontCommonData.getFormSettings().getBackgroundImage() != null) {
           /* Glide.with(this).load(StorefrontCommonData.getFormSettings().getBackgroundImage())
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(mActivity.getResources(), resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                rlParent.setBackground(drawable);
                            }
                        }
                    });*/

            new GlideUtil.GlideUtilBuilder(rlParent)
                    .setLoadItem(StorefrontCommonData.getFormSettings().getBackgroundImage())
                    .setCenterCrop(true)
                    .build();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
//        SideMenuTransition.setSliderUI(mActivity, userData);
    }

//    public void sideMenuClick(View v) {
//        SideMenuTransition.sideMenuClick(v, mActivity, userData, valueEnabledPayment);
//    }

//    private void showPaymentSlider() {
//        valueEnabledPayment = Utils.getValuePayment(userData);
//        boolean isPaymentSliderEnabled = false;
//        if (valueEnabledPayment != 0) {
//            if (valueEnabledPayment != CASH) {
//                isPaymentSliderEnabled = true;
//            }
//        }
//        if (isPaymentSliderEnabled) {
//            findViewById(R.id.tvPaymentMethod).setVisibility(View.VISIBLE);
//            findViewById(R.id.vPaymentMethod).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.tvPaymentMethod).setVisibility(View.GONE);
//            findViewById(R.id.vPaymentMethod).setVisibility(View.GONE);
//        }
//    }

    /**
     * initialize the views
     */
    private void initViews() {
//        rlBack = findViewById(R.id.rlBack);
        btnFindHomes = findViewById(R.id.btnFindHomes);
        btnFindHomes.setText(getStrings(R.string.find) + " " + StorefrontCommonData.getTerminology().getProduct());
        tvWelcomeText = findViewById(R.id.tvWelcomeText);
        tvWelcomeText.setText(StorefrontCommonData.getFormSettings().getAppDescription());
        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setHint(getStrings(R.string.search_for_a) + " " + StorefrontCommonData.getTerminology().getProduct() + "...");
        llfilter = findViewById(R.id.llfilter);
        llfilter.setVisibility(UIManager.isShowDateFilter() ? View.VISIBLE : View.GONE);
        llCheckIn = findViewById(R.id.llCheckIn);
        llCheckOut = findViewById(R.id.llCheckOut);
        etCheckInDate = findViewById(R.id.etCheckInDate);
        etCheckInDate.setHint(getStrings(R.string.check_in));
        etCheckInDate.setFloatingLabelText(getStrings(R.string.check_in));
        etCheckOutDate = findViewById(R.id.etCheckOutDate);
        etCheckOutDate.setHint(getStrings(R.string.check_out));
        etCheckOutDate.setFloatingLabelText(getStrings(R.string.check_out));
        rlParent = findViewById(R.id.rlParent);
//        Utils.setOnClickListener(this, rlBack, tvAddress, btnFindHomes, findViewById(R.id.tvViewCart), findViewById(R.id.rlSavedCart), findViewById(R.id.ivDeleteCart));
        Utils.setOnClickListener(this, tvAddress, btnFindHomes, llCheckIn, llCheckOut, etCheckInDate, etCheckOutDate);
        //TODO commented for rental code as it cannot work in hyperlocal moreover glide cannot load on relative layout
        //setBackgroundImage();

    }

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }

        switch (v.getId()) {
            case R.id.tvAddress:
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

            case R.id.llCheckIn:
            case R.id.etCheckInDate:
                dateType = true;
                openDatePicker(System.currentTimeMillis());
                break;

            case R.id.llCheckOut:
            case R.id.etCheckOutDate:
                dateType = false;
                openDatePicker(System.currentTimeMillis());
                break;

            case R.id.btnFindHomes:
                if ((checkInDate != null && checkOutDate != null) || !UIManager.isShowDateFilter()) {
                    if (UIManager.isShowDateFilter() && selectedStartDate.after(selectedEndDate)) {
                        new AlertDialog.Builder(mActivity).message(getStrings(R.string.please_select_date_after_checkin_date)).listener(new AlertDialog.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {
//                            fetchFCMToken();
                            }
                        }).build().show();
                    } else {
                        Intent intent = new Intent(this, NLevelWorkFlowActivity.class);
                        intent.putExtra(PICKUP_ADDRESS, tvAddress.getText().toString());
                        intent.putExtra(PICKUP_LATITUDE, latitude);
                        intent.putExtra(PICKUP_LONGITUDE, longitude);
                        intent.putExtra(CHECK_IN_DATE, checkInDate);
                        intent.putExtra(CHECK_OUT_DATE, checkOutDate);
                        /**
                         * For ECOM flow
                         * Seller view with seller name and price
                         * and other sellers view
                         */
                        if (Dependencies.isEcomApp() && categoryId != null) {
                            intent.putExtra(PARENT_CATEGORY_ID, categoryId);
                        }

                        startActivity(intent);
                        finish();
                    }
                } else {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.please_select_checkin_checkout_date)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
//                            fetchFCMToken();
                        }
                    }).build().show();
                }
                break;


            default:
                break;
        }
    }

    /**
     * method for fav location
     */
    public void gotoFavLocationActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    /**
     * method to go on map
     */
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
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    userData = StorefrontCommonData.getUserData();
                    Transition.openCheckoutActivity(mActivity, data.getExtras());
                }
                break;

            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    tvAddress.setText(data.getStringExtra("address"));
                    latitude = data.getDoubleExtra("latitude", 0.0);
                    longitude = data.getDoubleExtra("longitude", 0.0);
                }
                break;

//            case Codes.Request.OPEN_PROFILE_ACTIVITY:
//                if (resultCode == RESULT_OK) {
//                    userData = (UserData) data.getExtras().getSerializable(UserData.class.getName());
//                    StorefrontCommonData.setUserData(userData);
//                    SideMenuTransition.setSliderUI(mActivity, userData);
//                }
//                break;

            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    startLocationFetcher();
                }
                break;
//            case Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY:
//                if (resultCode == Activity.RESULT_OK) {
//                    Dependencies.setDemoRun(false);
//                    userData = StorefrontCommonData.getUserData();
//                } else {
//                    Dependencies.setDemoRun(true);
//                }
//                break;
//            case Codes.Request.OPEN_HOME_ACTIVITY:
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
//                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
//                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
//                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
//                    }
//                }
//
//                if (data.getExtras() != null) {
//
//                    ProductTypeData storefrontData = (ProductTypeData) data.getExtras().getSerializable(STOREFRONT_DATA);
//                    int storefrontDataItemPos = data.getExtras().getInt(STOREFRONT_DATA_ITEM_POS);
//
//                    try {
//                        cityStorefrontsModel.getData().set(storefrontDataItemPos, storefrontData);
//                        if (adapter != null) {
//                            adapter.notifyDataSetChanged();
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                break;

            case Codes.Request.OPEN_CHECKOUT_SCREEN:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                }
                break;

        }
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
    public void onBackPressed() {
        performBackAction();
    }

    /**
     * perform back action
     */
    private void performBackAction() {
        long currentTimeStamp = System.currentTimeMillis();
        long difference = currentTimeStamp - lastBackPressed;

        if (difference > 2000) {
            Utils.snackBar(this, getStrings(R.string.tap_again_to_exit_text),false);
            lastBackPressed = currentTimeStamp;
        } else {
            Transition.exit(this);
        }
    }

    /**
     * set address fetcher
     */
    public void setAddressFetcher() {
        startLocationFetcher();
//        getCurrentLocation();
//        executeSetAddress();
    }

    /**
     * start the location fetcher
     */
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
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        executeSetAddress();
    }

    public void executeSetAddress() {
        //Log.e("executeSetAddress", "executeSetAddress called");
        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }

    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            //Log.i("doInBackground", "doInBackground");
            return MapUtils.getGapiJson(new LatLng(latitude, longitude), mActivity);
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null && jsonObject.has("address"))
                    tvAddress.setText(jsonObject.getString("address"));
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

    /**
     * @param miniDate min date for calender
     */
    private void openDatePicker(final long miniDate) {
//        Locale locale = getResources().getConfiguration().locale;
//        Locale.setDefault(locale);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(miniDate);
        datePickerFragment.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        if (dateType) {
            selectedStartDate = Calendar.getInstance();
            selectedStartDate.set(year, monthOfYear + 1, dayOfMonth);
            checkInDate = year + "-" + DateUtils.getInstance().dateFormatter(monthOfYear + 1) + "-" + DateUtils.getInstance().dateFormatter(dayOfMonth);
            etCheckInDate.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
            etCheckInDate.setFloatingLabelTextColor(getResources().getColor(R.color.white_80));
            etCheckInDate.setTextColor(getResources().getColor(R.color.white_80));
            etCheckInDate.setText(checkInDate);
        } else {
            selectedEndDate = Calendar.getInstance();
            selectedEndDate.set(year, monthOfYear + 1, dayOfMonth);
            checkOutDate = year + "-" + DateUtils.getInstance().dateFormatter(monthOfYear + 1) + "-" + DateUtils.getInstance().dateFormatter(dayOfMonth);
            etCheckOutDate.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
            etCheckOutDate.setFloatingLabelTextColor(getResources().getColor(R.color.white_80));
            etCheckOutDate.setTextColor(getResources().getColor(R.color.white_80));
            etCheckOutDate.setText(checkOutDate);
        }
    }
}