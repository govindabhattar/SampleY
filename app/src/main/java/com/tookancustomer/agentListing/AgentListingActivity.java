package com.tookancustomer.agentListing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.BaseActivity;
import com.tookancustomer.DatesOnCalendarActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.ScheduleTimeActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by utkarsh  on 17/10/19.
 */

public class AgentListingActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    RecyclerView agentListRV;
    private RelativeLayout rlBack;
    private TextView tvHeading, tvNoResultFound;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerLayout;
    private double latitude, longitude;
    private Datum productDataItem;
    private int itemPos;
    private String startDate;
    private boolean isSERVICE_AS_PRODUCT;
    private int[] selectedQuantity;
    private ArrayList<AgentData> agentDataArrayList;
    private AgentListAdapter mAgentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_listing);
        mActivity = this;
        getIntentData();
        initUI();
        initislizeData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra(PICKUP_LATITUDE) && getIntent().hasExtra(PICKUP_LONGITUDE)) {
            latitude = bundle.getDouble(PICKUP_LATITUDE);
            longitude = bundle.getDouble(PICKUP_LONGITUDE);
        }
        if (getIntent().hasExtra(PRODUCT_CATALOGUE_DATA)) {
            productDataItem = (Datum) getIntent().getSerializableExtra(PRODUCT_CATALOGUE_DATA);
        }
        if (getIntent().hasExtra(KEY_ITEM_POSITION)) {
            itemPos = getIntent().getIntExtra(KEY_ITEM_POSITION, 0);
        }
        if (getIntent().hasExtra("SERVICE_AS_PRODUCT")) {
            isSERVICE_AS_PRODUCT = getIntent().getBooleanExtra("SERVICE_AS_PRODUCT", false);
        }
        if (getIntent().hasExtra("selectedQuantity")) {
            selectedQuantity = getIntent().getIntArrayExtra("selectedQuantity");
        }
    }

    private void initislizeData() {
        startShimmerAnimation(shimmerLayout);
        getAgentList();
    }

    private void getAgentList() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(PRODUCT_ID, productDataItem.getProductId());
        commonParams.add(USER_ID, productDataItem.getUserId());
        commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add("latitude", latitude);
        commonParams.add("longitude", longitude);
        commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        commonParams.add("product_id", productDataItem.getProductId());

        Dependencies.addCommonParameters(commonParams, this, StorefrontCommonData.getUserData());


        RestClient.getApiInterface(mActivity).getAgents(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        agentListRV.setVisibility(View.VISIBLE);
                        shimmerLayout.setVisibility(View.GONE);

                        agentDataArrayList = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                new TypeToken<ArrayList<AgentData>>() {
                                }.getType());
                        mAgentAdapter = new AgentListAdapter(mActivity, agentDataArrayList);
                        agentListRV.setAdapter(mAgentAdapter);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if (agentDataArrayList.size() > 0) {
                            tvNoResultFound.setVisibility(View.GONE);
                            agentListRV.setVisibility(View.VISIBLE);
                        }else {
                            tvNoResultFound.setVisibility(View.VISIBLE);
                            agentListRV.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

    }

    private void initUI() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvNoResultFound = findViewById(R.id.tvNoResultFound);
        tvNoResultFound.setText(getStrings(R.string.no_agent_found).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
        tvHeading.setText(getStrings(R.string.agent_list).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
        agentListRV = findViewById(R.id.agentListRV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        shimmerLayout = findViewById(R.id.shimmerLayout);

        mLayoutManager = new LinearLayoutManager(mActivity);
        agentListRV.setItemAnimator(new DefaultItemAnimator());
        agentListRV.setLayoutManager(mLayoutManager);

        Utils.setOnClickListener(this, rlBack);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAgentList();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rlBack:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void startShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
    }

    public void stopShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.GONE);
        shimmerLayout.stopShimmerAnimation();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                        Dependencies.addCartItem(mActivity, productDataItem);
                    }
                    if (data.hasExtra(KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
                    returnIntent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                    setResult(RESULT_OK, returnIntent);
                    onBackPressed();
                }
                break;
        }
    }

    public void onAgentSelected(int agentID) {
        setSelectedAgent(agentID);
        if (productDataItem.getSelectedQuantity() == 0 &&
                productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                (productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                        || productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT)) {


            if (productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                    && (Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.FIXED)
                    && productDataItem.getEnableTookanAgent() == 0) {
                openDatePicker();

            } else {
                if (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                        && (Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.PER_DAY)
                        && UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                    Intent intent = new Intent(mActivity, DatesOnCalendarActivity.class);
                    intent.putExtra(KEY_ITEM_POSITION, itemPos);
                    intent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
                    intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
                    intent.putExtra(IS_START_TIME, true);
                    intent.putExtra(SELECTED_DATE, "");
                    startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                } else {
                    Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                    intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                    intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                    intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                    intent.putExtra(Keys.Extras.IS_START_TIME, true);
                    intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                    if (MyApplication.getInstance().getSelectedPickUpMode() == 2) {
                        intent.putExtra(Keys.Extras.IS_SELF_PICKUP, 1);
                    } else {
                        intent.putExtra(Keys.Extras.IS_SELF_PICKUP, 0);
                    }
                    intent.putExtra(FROM_AGENT_SCREEN,true);

                    intent.putExtra("service_time", productDataItem.getServiceTime());
                    intent.putExtra(Keys.Extras.AGENT_ID, agentID);
                    intent.putExtra(Keys.Extras.IS_AGENT_SELECTED, true);
                    startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                }

            }
        } else {
            addToCart();
        }

    }

    private void setSelectedAgent(int agentID) {
        productDataItem.setSelectedAgentId(agentID);
        productDataItem.setAgentSelected(true);
    }

    public void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        datePickerFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        if (datePicker.isShown()) {
            openTimePicker();
        }
    }

    public void openTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isValidTime(startDate + " " + hourOfDay + ":" + minute)) {
            startDate = startDate + " " + hourOfDay + ":" + minute;
            Date productStartDate = DateUtils.getInstance().getDateFromString(startDate, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC);

            productDataItem.setProductStartDate(productStartDate);
            productDataItem.setProductEndDate(productStartDate);

            addToCart();

        } else {
            Utils.snackBar(mActivity, getStrings(R.string.invalid_selected_date));
        }
    }


    private boolean isValidTime(String date) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();

            if (DateUtils.getInstance().getDateFromString(date, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC).getTime() < calendar.getTime().getTime())
                isValidDate = false;
        }
        return isValidDate;
    }

    private void addToCart() {
        if (isSERVICE_AS_PRODUCT) {
            if (productDataItem.getMinProductquantity() > 1 &&
                    selectedQuantity[0] < productDataItem.getMinProductquantity()) {
                selectedQuantity[0] = selectedQuantity[0] + productDataItem.getMinProductquantity();
            } else {
                selectedQuantity[0]++;
            }
            productDataItem.setSelectedQuantity(selectedQuantity[0]);
            Dependencies.addCartItem(mActivity, productDataItem);
        }

        Dependencies.addCartItem(mActivity, productDataItem);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
        returnIntent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
        setResult(RESULT_OK, returnIntent);
        onBackPressed();
    }


    public void viewAgentDetails(AgentData agentData) {

        Intent templateIntent = new Intent(this, AgentDetailActivity.class);
        templateIntent.putExtra("agentName", agentData.getName());
        templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, agentData.getTemplate());

        templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, true);
        startActivity(templateIntent);
    }
}
