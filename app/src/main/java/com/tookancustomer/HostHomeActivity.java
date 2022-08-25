package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.requests.RequestsData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.CompleteRequestActivity.REQUEST_DATA;


public class HostHomeActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private long lastBackPressed;
    private ImageView ivLocation, ivMenu;
    public String address = "";
    public Double latitude, longitude;
    private LocationFetcher locationFetcher;
    private LinearLayout llPending, llDispatched, llCompleted, llCanceled;
    private TextView tvPendingCount, tvDispatchedCount, tvCompletedCount, tvCancelledCount;
    private RequestsData requestsData;
    private TextView tvViewListing;
    private TextView tvPending,tvDispatched,tvCompleted,tvCanceled,tvViewProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_home);
        mActivity = this;
        SideMenuTransition.setDualUserToggle(this);
        init();
        Location location = LocationUtils.getLastLocation(mActivity);
        latitude = location != null ? location.getLatitude() : 0.0;
        longitude = location != null ? location.getLongitude() : 0.0;
//        setAddressFetcher();

        if (getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE) != null) {
            Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
        } else if (getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE) != null) {
            Utils.snackBar(mActivity, getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE));
        }
        getAllRequests();


    }

    /**
     * initialize views
     */
    private void init() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ivLocation = findViewById(R.id.ivLocation);
        tvViewListing = findViewById(R.id.tvViewListing);
        ivMenu = findViewById(R.id.ivMenu);
        llPending = findViewById(R.id.llPending);
        llDispatched = findViewById(R.id.llDispatched);
        llCompleted = findViewById(R.id.llCompleted);
        llCanceled = findViewById(R.id.llCanceled);
        tvPendingCount = findViewById(R.id.tvPendingCount);
        tvDispatchedCount = findViewById(R.id.tvDispatchedCount);
        tvCompletedCount = findViewById(R.id.tvCompletedCount);
        tvCancelledCount = findViewById(R.id.tvCancelledCount);
        tvViewProduct = findViewById(R.id.tvViewProduct);
        tvViewProduct.setText(getStrings(R.string.total_product_text) + " " + StorefrontCommonData.getTerminology().getProduct());
        tvPending = findViewById(R.id.tvPending);
        tvPending.setText(StorefrontCommonData.getTerminology().getPending());
        tvDispatched = findViewById(R.id.tvDispatched);
        tvPending.setText(StorefrontCommonData.getTerminology().getDispatched());
        tvCompleted = findViewById(R.id.tvCompleted);
        tvPending.setText(StorefrontCommonData.getTerminology().getCompleted());
        tvCanceled = findViewById(R.id.tvCanceled);
        tvPending.setText(StorefrontCommonData.getTerminology().getCancelled());


        Utils.setOnClickListener(this, ivLocation, ivMenu, llPending, llDispatched, llCompleted, llCanceled, tvViewListing);

    }

    private void showRequestsCount() {
        if (requestsData != null) {
            tvPendingCount.setText(String.valueOf(requestsData.getPendingCount()));
            tvCancelledCount.setText(String.valueOf(requestsData.getCancelledCount()));
            tvCompletedCount.setText(String.valueOf(requestsData.getCompletedCount()));
            tvDispatchedCount.setText(String.valueOf(requestsData.getDispatchedCount()));
        }
    }

    private void getAllRequests() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add(ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getAppAccessToken());
        commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getFormSettings().getUserId());
        commonParams.add(USER_TYPE, "3");

        RestClient.getApiInterface(this).getRequests(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                requestsData = baseModel.toResponseModel(RequestsData.class);
                showRequestsCount();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Utils.snackBar(HostHomeActivity.this, error.getMessage());
            }
        });

    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(REQUEST_DATA, requestsData);
        switch (view.getId()) {

            case R.id.ivMenu:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                Utils.clearLightStatusBar(mActivity, mDrawerLayout);
                break;
            case R.id.llCanceled:
                bundle.putString(TAG, CANCELLED);
                Transition.startActivity(HostHomeActivity.this, CompleteRequestActivity.class, bundle, false);
                break;
            case R.id.llCompleted:
                bundle.putString(TAG, COMPLETED);
                Transition.startActivity(HostHomeActivity.this, CompleteRequestActivity.class, bundle, false);
                break;
            case R.id.llDispatched:
                bundle.putString(TAG, DISPATCHED);
                Transition.startActivity(HostHomeActivity.this, CompleteRequestActivity.class, bundle, false);
                break;
            case R.id.llPending:
                bundle.putString(TAG, PENDING);
                Transition.startActivity(HostHomeActivity.this, CompleteRequestActivity.class, bundle, false);
                break;
            case R.id.tvViewListing:
                Transition.startActivity(HostHomeActivity.this, ListingActivity.class, bundle, false);
                break;

            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
            finish();
        } else {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                Utils.setLightStatusBar(mActivity, mDrawerLayout);
            } else {
                long currentTimeStamp = System.currentTimeMillis();
                long difference = currentTimeStamp - lastBackPressed;

                if (difference > 2000) {
                    Utils.snackBar(this, getStrings(R.string.tap_again_to_exit_text),false);
                    lastBackPressed = currentTimeStamp;
                } else {
                    ActivityCompat.finishAffinity(this);
                    Transition.exit(this);
                }
            }
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
    protected void onResume() {
        super.onResume();
        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
    }


    public void sideMenuClick(View v) {
        SideMenuTransition.sideMenuClick(v, mActivity);
    }

}
