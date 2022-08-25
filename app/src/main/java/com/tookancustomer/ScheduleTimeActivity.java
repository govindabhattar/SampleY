package com.tookancustomer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.tookancustomer.adapters.ScheduleDateListAdapter;
import com.tookancustomer.adapters.ScheduleTimeSlotsAdapter;
import com.tookancustomer.adapters.TimeSlotAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.models.AvailableTimeSlotsModelResponse;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.LaundryScheduleTimeSlotsResponse;
import com.tookancustomer.models.SortedDatesModel;
import com.tookancustomer.models.TimeSlotsResponseModel.Data;
import com.tookancustomer.models.TimeSlotsResponseModel.NewData;
import com.tookancustomer.models.TimeSlotsResponseModel.NewTimeSlotsModelResponse;
import com.tookancustomer.models.TimeSlotsResponseModel.Slot;
import com.tookancustomer.models.TimeSlotsResponseModel.TimeSlotsModelResponse;
import com.tookancustomer.models.TookanScheduleTimeSlotsResponse;
import com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots.MerchantTimeSlotData;
import com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots.MerchantTimeSlots;
import com.tookancustomer.models.tookanSchedulingModel.Datum;
import com.tookancustomer.models.tookanSchedulingModel.TookanAgentScheduling;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.tookancustomer.appdata.Constants.DateFormat.CHECKOUT_DATE_FORMAT;
import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE_NEW;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;
import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_24_no_seconds;

public class ScheduleTimeActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    public boolean isStartDate = true;
    public boolean isSchedulingFromCheckout = true;
    public Date selectedDateToBeShown;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private LinearLayout rlUnavailNet, llSelectDate;
    private TextView tvNoNetConnect, tvRetry, tvNoSlotsAvailable;
    private RecyclerView /*rvDateRecyclerList,*/ rvTimeSlotsRecyclerList;
    private TextView tvSelectDate, tvSelectTimeLabel;
    private ScheduleDateListAdapter scheduleDateListAdapter;
    private ArrayList<Date> dateArrayList = new ArrayList<>();
    private ScheduleTimeSlotsAdapter scheduleTimeSlotsAdapter;
    private ArrayList<AvailableTimeSlotsModelResponse> availTimeSlotsList = new ArrayList<>();
    private ArrayList<TookanScheduleTimeSlotsResponse> tookanAvailTimeSlotsList = new ArrayList<>();
    private ArrayList<LaundryScheduleTimeSlotsResponse> laundryAvailTimeSlotsList = new ArrayList<>();
    private Date selectedDate;
    private Integer bufferSchedule = 0;
    private String selectedDateTimeCheckoutString;
    private Date selectedDateTimeCheckout;
    private com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private int itemPos = 0;
    private int serviceTime = 0;
    private int agentId = 0;
    private boolean isCustomOrder = false;
    private boolean isAgentSelected;
    private int isSelfPickUp = 0;
    public Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_time);
        mActivity = this;

        if (getIntent() != null) {
            isSchedulingFromCheckout = getIntent().getBooleanExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, true);
            isStartDate = getIntent().getBooleanExtra(Keys.Extras.IS_START_TIME, true);
            selectedDateTimeCheckoutString = getIntent().getStringExtra(Keys.Extras.SELECTED_DATE);
            serviceTime = getIntent().getIntExtra("service_time", 0);
            agentId = getIntent().getIntExtra(Keys.Extras.AGENT_ID, 0);
            isCustomOrder = getIntent().getBooleanExtra("isCustomOrder", false);
            isAgentSelected = getIntent().getBooleanExtra(Keys.Extras.IS_AGENT_SELECTED, false);
        }
        selectedDateTimeCheckout = DateUtils.getInstance().getDate(selectedDateTimeCheckoutString);

        if (getIntent().hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
            itemPos = getIntent().getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
        }
        if (getIntent().hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
            productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) getIntent().getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
        }

        if (isSchedulingFromCheckout && Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0) != null) {
            productDataItem = Dependencies.getSelectedProductsArrayList().get(0);
        }

        if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
            storefrontData = Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData();
        }

        initViews();

        if (productDataItem != null) {
            bufferSchedule = productDataItem.getStorefrontData().getBufferSchedule().intValue();
        } else {
            bufferSchedule = StorefrontCommonData.getFormSettings().getBufferSchedule().intValue();
        }

        if (getIntent().hasExtra("SAME_ACTIVITY")) {
            if (getIntent().getIntExtra("SAME_ACTIVITY", 0) == 1) {
                isSelfPickUp = 1;
            } else {
                isSelfPickUp = 0;

            }
        } else {

            if (storefrontData != null && storefrontData.getScheduledTask().equals(1) && storefrontData.getIsStorefrontOpened() == 0
                    && (getIntent() != null
                    && getIntent().getIntExtra(IS_SELF_PICKUP, 0) == 0)) {
                isSelfPickUp = 0;
            } else if (getIntent().hasExtra(FROM_SEARCH_SCREEN) && getIntent().getBooleanExtra(FROM_SEARCH_SCREEN, false)) {
                if (getIntent().hasExtra(IS_SELF_PICKUP) && getIntent().getIntExtra(IS_SELF_PICKUP, 0) == 0) {
                    isSelfPickUp = 0;
                } else {
                    isSelfPickUp = 1;
                }
            } else if (productDataItem.getStorefrontData().getScheduledTask().equals(0)) {
                isSelfPickUp = 1;
            } else if (getIntent().hasExtra(FROM_AGENT_SCREEN) && getIntent().getBooleanExtra(FROM_AGENT_SCREEN, false)) {
                if (getIntent().hasExtra(IS_SELF_PICKUP) && getIntent().getIntExtra(IS_SELF_PICKUP, 0) == 0) {
                    isSelfPickUp = 0;
                } else {
                    isSelfPickUp = 1;
                }
            } else {
                isSelfPickUp = 1;
            }
        }


        if ((storefrontData != null && storefrontData.getDisplayRangeIntervals() == 1)
                && (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE
                || (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                && productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT
                && !productDataItem.getStorefrontData().isOrderAgentShedulingEnabled()))) {

            llSelectDate.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);

            getTimeSlots(cal.getTime());
            dateArrayList.add(cal.getTime());
        } else if (selectedDateTimeCheckoutString.isEmpty()
                || (!(isSchedulingFromCheckout ? (productDataItem != null && productDataItem.getStorefrontData().getEnableTookanAgent() == 1) : (productDataItem != null && productDataItem.getEnableTookanAgent() == 1)) && isStartDate)) {

            llSelectDate.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            getTimeSlots(currentDate);

            cal.add(Calendar.YEAR, 3);
            Date endDate = cal.getTime();

            while (currentDate.before(endDate)) {
                dateArrayList.add(currentDate);
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, 1);
                currentDate = cal.getTime();
            }
        } else if (!selectedDateTimeCheckoutString.isEmpty() && !isStartDate &&
                (!(isSchedulingFromCheckout ? (productDataItem != null && productDataItem.getStorefrontData().getEnableTookanAgent() == 1) : (productDataItem != null && productDataItem.getEnableTookanAgent() == 1)))) {
//            rvDateRecyclerList.setVisibility(View.VISIBLE);
            llSelectDate.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);


            if (Dependencies.isLaundryApp()) {
                cal.add(Calendar.MINUTE, serviceTime);
            }

            if (productDataItem != null && productDataItem.getUnitType() == Constants.ProductsUnitType.PER_DAY.value) {
                cal.add(Calendar.DATE, 1);
            }


            Date currentDate = cal.getTime();
            getTimeSlots(currentDate);

//            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.YEAR, 3);
            Date endDate = cal.getTime();

            while (currentDate.before(endDate)) {
                dateArrayList.add(currentDate);
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, 1);
                currentDate = cal.getTime();
            }
        } else {
//            rvDateRecyclerList.setVisibility(View.GONE);
            llSelectDate.setVisibility(View.GONE);

            findViewById(R.id.vwShadow).setVisibility(View.VISIBLE);

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);
            getTimeSlots(cal.getTime());
            dateArrayList.add(cal.getTime());
        }

        scheduleDateListAdapter = new ScheduleDateListAdapter(mActivity, isSchedulingFromCheckout, dateArrayList, new ScheduleDateListAdapter.Callback() {
            @Override
            public void onDateSelected(Date date) {
                getTimeSlots(date, true);
            }
        });
//        rvDateRecyclerList.setAdapter(scheduleDateListAdapter);
    }

    private void initViews() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        findViewById(R.id.vwShadow).setVisibility(View.GONE);
        rlUnavailNet = findViewById(R.id.rlUnavailNet);
        tvNoNetConnect = findViewById(R.id.tvNoNetConnect);
        tvNoNetConnect.setText(getStrings(R.string.no_internet_connection));
        tvRetry = findViewById(R.id.tvRetry);
        tvRetry.setText(getStrings(R.string.retry_underlined));

        tvNoSlotsAvailable = findViewById(R.id.tvNoSlotsAvailable);
        tvNoSlotsAvailable.setText(getStrings(R.string.no_slots_available));
//        rvDateRecyclerList = findViewById(R.id.rvDateRecyclerList);
//        rvDateRecyclerList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        tvSelectDate = findViewById(R.id.tvSelecteDate);
        llSelectDate = findViewById(R.id.llSelectDate);
        tvSelectTimeLabel = findViewById(R.id.tvSelectTimeLabel);
        tvSelectTimeLabel.setText(getStrings(R.string.select_date));

        rvTimeSlotsRecyclerList = findViewById(R.id.rvTimeSlotsRecyclerList);
        rvTimeSlotsRecyclerList.setLayoutManager(new LinearLayoutManager(mActivity));

        Utils.setOnClickListener(this, rlBack, tvRetry, tvSelectDate);
        if (!isSchedulingFromCheckout) {
            if (isStartDate)
                tvHeading.setText(getStrings(R.string.Select_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getStartTime(true)));
            else
                tvHeading.setText(getStrings(R.string.Select_terminology).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getEndTime(true)));
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlBack) {
            onBackPressed();
        } else if (v.getId() == R.id.tvRetry) {
            getTimeSlots(selectedDate);
        } else if (v.getId() == R.id.tvSelecteDate) {
            minimumdate();
        }
    }

    private void minimumdate() {
        if ((storefrontData != null && storefrontData.getDisplayRangeIntervals() == 1)
                && (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE
                || (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                && productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT
                && !productDataItem.getStorefrontData().isOrderAgentShedulingEnabled()))) {

            llSelectDate.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);

            openDatePicker(cal.getTime());
        } else if (selectedDateTimeCheckoutString.isEmpty()
                || (!(isSchedulingFromCheckout ? (productDataItem != null && productDataItem.getStorefrontData().getEnableTookanAgent() == 1) : (productDataItem != null && productDataItem.getEnableTookanAgent() == 1)) && isStartDate)) {

            llSelectDate.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            openDatePicker(currentDate);


        } else if (!selectedDateTimeCheckoutString.isEmpty() && !isStartDate &&
                (!(isSchedulingFromCheckout ? (productDataItem != null && productDataItem.getStorefrontData().getEnableTookanAgent() == 1) : (productDataItem != null && productDataItem.getEnableTookanAgent() == 1)))) {
//            rvDateRecyclerList.setVisibility(View.VISIBLE);
            llSelectDate.setVisibility(View.VISIBLE);
            findViewById(R.id.vwShadow).setVisibility(View.GONE);

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);

            if (Dependencies.isLaundryApp()) {
                cal.add(Calendar.MINUTE, serviceTime);
            }

            if (productDataItem != null && productDataItem.getUnitType() == Constants.ProductsUnitType.PER_DAY.value) {
                cal.add(Calendar.DATE, 1);
            }
            Date currentDate = cal.getTime();
            openDatePicker(currentDate);

        } else {
//            rvDateRecyclerList.setVisibility(View.GONE);
            llSelectDate.setVisibility(View.GONE);

            findViewById(R.id.vwShadow).setVisibility(View.VISIBLE);

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);
            openDatePicker(cal.getTime());
        }
    }


    public void openDatePicker(Date minDate) {
        Utils.hideSoftKeyboard(this, tvSelectDate);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(selectedDateTimeCheckout.getTime());
        datePickerFragment.setMaxdays(StorefrontCommonData.getAppConfigurationData().getMaxScheduleDaysLimit());
        datePickerFragment.show(this.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getTimeSlots(final Date date) {
        getTimeSlots(date, false);
    }

    private void getTimeSlots(final Date date, boolean autoSelect) {
        if (date == null) {
            return;
        }
        tvSelectDate.setText(DateUtils.getInstance().getFormattedDate(date, CHECKOUT_DATE_FORMAT));

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String monthDay = DateUtils.getInstance().getDateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)];
        if (isSchedulingFromCheckout) {
//            if (rvDateRecyclerList.getVisibility() == View.GONE) {
            if (llSelectDate.getVisibility() == View.GONE) {
                tvHeading.setText(cal.get(Calendar.DATE) + " " + monthDay + " " + cal.get(Calendar.YEAR));
            } else {
//                tvHeading.setText(monthDay + " " + cal.get(Calendar.YEAR));
                tvHeading.setText(getStrings(R.string.scheduled_time));
            }
        }

        selectedDate = date;

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.DATE, DateUtils.getInstance().getFormattedDate(date, ONLY_DATE_NEW));

        if (isCustomOrder && Dependencies.isLaundryApp()) {
            if (commonParams.build().getMap().containsKey(USER_ID)) {
                commonParams.add(USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
            }
        } else {
            if (productDataItem != null) {
                if (commonParams.build().getMap().containsKey(USER_ID)) {
                    commonParams.add(USER_ID, productDataItem.getUserId() + "");
                }
            }
        }

        if (Dependencies.isLaundryApp()) {
            if (isCustomOrder) {
                commonParams.add("product_ids", new JSONArray());
                commonParams.add("is_custom_order", isCustomOrder ? 1 : 0);
            } else {
                if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                    ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum> selectedProductsList = Dependencies.getSelectedProductsArrayList();
                    JSONArray jsonArray = new JSONArray();


                    if (storefrontData.getCreateDeliverySlots() == 1) {

                        for (int i = 0; i < selectedProductsList.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("id", selectedProductsList.get(i).getProductId() + "");
                                jsonObject.put("quantity", selectedProductsList.get(i).getSelectedQuantity() + "");

                            } catch (JSONException e) {
                                Utils.printStackTrace(e);
                            }
                            jsonArray.put(jsonObject);
                        }
                    } else {

                        for (int i = 0; i < selectedProductsList.size(); i++) {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("product_id", selectedProductsList.get(i).getProductId());
                            jsonArray.put(selectedProductsList.get(i).getProductId());
                        }
                    }

                    commonParams.add("product_ids", jsonArray);
                }
            }
        } else if (productDataItem != null) {
            commonParams.add(PRODUCT_ID, productDataItem.getProductId() + "");
        }

        if (isAgentSelected)
            commonParams.add("agent_id", agentId);

        if (Dependencies.isLaundryApp() && !isStartDate) {
            Calendar calll = Calendar.getInstance();
            calll.setTime(selectedDateTimeCheckout);
            if (storefrontData.getCreateDeliverySlots() != 1)
                calll.add(Calendar.MINUTE, serviceTime);
            commonParams.add("datetime", DateUtils.getInstance().getFormattedDate(calll.getTime(), STANDARD_DATE_FORMAT_TZ));
        }
        if (isSchedulingFromCheckout) {
            commonParams.add("new_flow", 1);

            if (Dependencies.isLaundryApp()) {
                RestClient.getApiInterface(mActivity).getLaundryStorefrontTimeSlots(commonParams.build().getMap()).enqueue(getResponseResolver(date, false));
            } else {
                if (isAgentSelected) {
                    commonParams.add("self_pickup", isSelfPickUp);
                    RestClient.getApiInterface(mActivity).getProductTimeSlots(commonParams.build().getMap()).enqueue(getResponseResolver(date, autoSelect));
                } else {
                    if (storefrontData != null && storefrontData.getDisplayRangeIntervals() == 1) {

                        CommonParams.Builder commonParam = new CommonParams.Builder()
                                .add(Keys.APIFieldKeys.DATE, DateUtils.getInstance().getFormattedDate(date, ONLY_DATE_NEW))
                                .add(USER_ID, StorefrontCommonData.getFormSettings().getUserId())
                                .add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                                .add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());

                        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                            commonParam.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));


                        }
                        commonParam.add("self_pickup", isSelfPickUp);


                        RestClient.getApiInterface(mActivity).getProductTimeRange(commonParam.build().getMap()).enqueue(getResponseResolver(date, autoSelect));

                    } else {
                        commonParams.add("self_pickup", isSelfPickUp);
                        RestClient.getApiInterface(mActivity).getStorefrontTimeSlots(commonParams.build().getMap()).enqueue(getResponseResolver(date, autoSelect));
                    }
                }
            }
        } else {
            commonParams.add("self_pickup", isSelfPickUp);
            RestClient.getApiInterface(mActivity).getProductTimeSlots(commonParams.build().getMap()).enqueue(getResponseResolver(date, autoSelect));
        }
    }

    public ResponseResolver<BaseModel> getResponseResolver(final Date date, final boolean autoSelect) {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                rlUnavailNet.setVisibility(View.GONE);

                try {
                    /*When tookan agent is enabled on a service then this method will be called
                     * it will only be called in case of getProductTimeSlots */

                    TookanAgentScheduling tookanAgentScheduling = new TookanAgentScheduling();
                    Datum[] datum = baseModel.toResponseModel(Datum[].class);
                    tookanAgentScheduling.setData(new ArrayList<Datum>(Arrays.asList(datum)));

                    if (tookanAgentScheduling.getData().size() == 0) {
                        tvNoSlotsAvailable.setVisibility(View.VISIBLE);
                        rvTimeSlotsRecyclerList.setVisibility(View.GONE);
                    } else {
                        setScheduleTimeSlotAdapter(tookanAgentScheduling.getData(), autoSelect);
                    }

                } catch (JsonSyntaxException jsonException) {
                    try {

                        NewTimeSlotsModelResponse newTimeSlotsModelResponse = new NewTimeSlotsModelResponse();
                        newTimeSlotsModelResponse.setData(baseModel.toResponseModel(NewData.class));

                        if (newTimeSlotsModelResponse.getData().getSlots().size() == 0) {
                            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
                            rvTimeSlotsRecyclerList.setVisibility(View.GONE);
                        } else {
                            setNewTimeSlotAdapter(date, newTimeSlotsModelResponse, autoSelect);
                        }

                    } catch (JsonSyntaxException jsonExc) {
                        try {

                         /* New workflow for merchant slots (From checkout) when is_booked key is provided.
                             And booked slots will greyed out.*/

                            MerchantTimeSlots merchantTimeSlots = new MerchantTimeSlots();
                            merchantTimeSlots.setData(baseModel.toResponseModel(MerchantTimeSlotData.class));

                            if (merchantTimeSlots.getData().getNewFlow() == 1 || merchantTimeSlots.getData().getIsGoogleCalendarActive() == 1) {
                                /*
                                 * new_flow key is introduced to distinguish between old and new flow.
                                 * In new flow of merchant timeslots, is_booked will be provided for showing booked slots.
                                 * */

                                List<com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots.Slot> slots;
                                if (Dependencies.isLaundryApp() && storefrontData.getCreateDeliverySlots() == 1 && !isStartDate) {
                                    slots = merchantTimeSlots.getData().getDeliverySlots();
                                } else {
                                    slots = merchantTimeSlots.getData().getSlots();
                                }

                                if (slots.size() == 0) {
                                    tvNoSlotsAvailable.setVisibility(View.VISIBLE);
                                    rvTimeSlotsRecyclerList.setVisibility(View.GONE);
                                } else {
                                    /* if new_flow is 0 then old flow will be called where is_booked key is not coming.*/

                                    if (storefrontData != null && storefrontData.getDisplayRangeIntervals() == 1) //slots time range
                                        setTimeSlotsModelResponse(date, baseModel, autoSelect);
                                    else
                                        setMerchantTimeSlotAdapter(date, merchantTimeSlots.getData(), autoSelect);
                                }
                            } else {

                                setTimeSlotsModelResponse(date, baseModel, autoSelect);

                            }

                        } catch (JsonSyntaxException jsonSyntaxExc) {

                            setTimeSlotsModelResponse(date, baseModel, autoSelect);

                        }
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                rlUnavailNet.setVisibility(View.VISIBLE);
                tvNoSlotsAvailable.setVisibility(View.GONE);
                rvTimeSlotsRecyclerList.setVisibility(View.GONE);
            }
        };
    }

    private void setTimeSlotsModelResponse(Date date, BaseModel baseModel, boolean autoSelect) {
        TimeSlotsModelResponse timeSlotsModelResponse = new TimeSlotsModelResponse();
        timeSlotsModelResponse.setData(baseModel.toResponseModel(Data.class));

        if (timeSlotsModelResponse.getData().getSlots().size() == 0) {
            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
            rvTimeSlotsRecyclerList.setVisibility(View.GONE);
        } else {
            if (Dependencies.isLaundryApp() || (storefrontData != null && storefrontData.getDisplayRangeIntervals() == 1)) {
                /*  In case of laundry app, start and end time are displayed like start time-end time(11:00 -12:00)
                 *  On selecting pickup date, delivery slots are selected on basis of service time(in minutes)
                 *  */

                setLaundryTimeSlotAdapter(date, timeSlotsModelResponse, autoSelect);
            } else {

                /*  In case of hyperlocal app, this is called and we get start date and end date , eg. 4:00 pm ,8:00 pm
                 *  Then we find all time slots according buffer_schedule key. eg. buffer_schedule = 30 then slots be like 4:00 ,4:30, 5:00...8:00
                 *  */


                setTimeSlotAdapter(date, timeSlotsModelResponse, autoSelect);
            }
        }
    }

    //TODO
    private void setScheduleTimeSlotAdapter(List<Datum> tookanAgentScheduleArrayList, boolean autoSelect) {
        ArrayList<Datum> timeSlotsList = new ArrayList<>();
        ArrayList<Datum> tempArrayList = new ArrayList<>();

        for (int i = 0; i < tookanAgentScheduleArrayList.size(); i++) {
            Date startTimeDate = tookanAgentScheduleArrayList.get(i).getStartTimeDate();
            Date endTimeDate = tookanAgentScheduleArrayList.get(i).getEndTimeDate();

            if (Calendar.getInstance().getTime().before(startTimeDate)) {

                if (selectedDateTimeCheckoutString.isEmpty()) {
//                if (isStartDate || selectedDateTimeCheckoutString.isEmpty()) {
                    if (tookanAgentScheduleArrayList.get(i).getAvailableStatus() == 0) {
                        timeSlotsList.add(tookanAgentScheduleArrayList.get(i));
                    }
                } else {
                    if (isStartDate) {
                        if (startTimeDate.before(selectedDateTimeCheckout)) {
                            tempArrayList.add(tookanAgentScheduleArrayList.get(i));
                        }
                    } else {
                        if (startTimeDate.after(selectedDateTimeCheckout) || startTimeDate.equals(selectedDateTimeCheckout)) {
                            tempArrayList.add(tookanAgentScheduleArrayList.get(i));
                        }
                    }
                }
            }
        }

        if (!selectedDateTimeCheckoutString.isEmpty()) {
            if (isStartDate) {
                int posStartFirstPos = 0;
                for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                    if (tempArrayList.get(i).getAvailableStatus() != 0) {
                        posStartFirstPos = i;
                        break;
                    }
                }

                for (int i = posStartFirstPos + 1; i < tempArrayList.size(); i++) {
                    timeSlotsList.add(tempArrayList.get(i));
                }

            } else {
                for (int i = 0; i < tempArrayList.size(); i++) {
                    if (tempArrayList.get(i).getAvailableStatus() != 0) {
                        break;
                    }
                    timeSlotsList.add(tempArrayList.get(i));
                }
            }
        }

        tookanAvailTimeSlotsList = new ArrayList<>();
        ArrayList<Datum> morningTimeSlotsList = new ArrayList<>(),
                noonTimeSlotsList = new ArrayList<>(),
                eveningTimeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsList.size(); i++) {
            if (timeSlotsList.get(i).getTimeOfDay() == 0) {
                morningTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 1) {
                noonTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 2) {
                eveningTimeSlotsList.add(timeSlotsList.get(i));
            }
        }

        if (morningTimeSlotsList.size() > 0) {
            TookanScheduleTimeSlotsResponse availableTimeSlotsModelResponse = new TookanScheduleTimeSlotsResponse(getStrings(R.string.morning), morningTimeSlotsList);
            tookanAvailTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (noonTimeSlotsList.size() > 0) {
            TookanScheduleTimeSlotsResponse availableTimeSlotsModelResponse = new TookanScheduleTimeSlotsResponse(getStrings(R.string.afternoon), noonTimeSlotsList);
            tookanAvailTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (eveningTimeSlotsList.size() > 0) {
            TookanScheduleTimeSlotsResponse availableTimeSlotsModelResponse = new TookanScheduleTimeSlotsResponse(getStrings(R.string.evening), eveningTimeSlotsList);
            tookanAvailTimeSlotsList.add(availableTimeSlotsModelResponse);
        }

        if (tookanAvailTimeSlotsList.size() > 0) {
            tvNoSlotsAvailable.setVisibility(View.GONE);
            rvTimeSlotsRecyclerList.setVisibility(View.VISIBLE);
            if (tookanAvailTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect) {

            }
        } else {
            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
            rvTimeSlotsRecyclerList.setVisibility(View.GONE);
        }

        if (tookanAvailTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect) {
//            onTimeSlotSelectedd(tookanAvailTimeSlotsList.get(0).getTimeSlotsArrayList().get(0).getStartTimeDate(),
//                    tookanAvailTimeSlotsList.get(0).getTimeSlotsArrayList().get(0).getEndTimeDate());

            onTimeSlotSelectedd(tookanAvailTimeSlotsList.get(0).getTimeSlotsArrayList().get(0).getStartTimeDate());
        }

        scheduleTimeSlotsAdapter = new ScheduleTimeSlotsAdapter(tookanAvailTimeSlotsList, mActivity, tookanAvailTimeSlotsList.size() == 1
                && timeSlotsList.size() == 1 && autoSelect, new TimeSlotAdapter.CallbackTookanAgent() {
            @Override
            public void onTimeSlotSelected(Date date, Date endDate) {

                onTimeSlotSelectedd(date);
//                onTimeSlotSelectedd(date, endDate);
            }
        });

        rvTimeSlotsRecyclerList.setAdapter(scheduleTimeSlotsAdapter);
    }

    private void setLaundryTimeSlotAdapter(Date date, final TimeSlotsModelResponse timeSlotsModelResponse, boolean autoSelect) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        ArrayList<Slot> timeSlotsList = new ArrayList<>();
        List<Slot> slots = new ArrayList<>();
        if (Dependencies.isLaundryApp() && storefrontData.getCreateDeliverySlots() == 1 && !isStartDate) {
            slots = timeSlotsModelResponse.getData().getDeliverySlots();
        } else {
            slots = timeSlotsModelResponse.getData().getSlots();
        }

        if (storefrontData.getIsBufferSlotForEverydayEnabled() == 1) {

            if (slots != null && slots.size() > 0) {
                Calendar orderStartSlot;
                orderStartSlot = toCalendar(DateUtils.getInstance().getDateFromString(slots.get(0).getStartTime(),
                        STANDARD_DATE_FORMAT_TZ));
                orderStartSlot.add(Calendar.MINUTE, timeSlotsModelResponse.getData().getPreBookingBuffer());

                Iterator<Slot> it = slots.iterator();
                while (it.hasNext()) {
                    Slot filtered = it.next();
                    if (DateUtils.getInstance().getDateFromString(filtered.getStartTime(), STANDARD_DATE_FORMAT_TZ)
                            .before(orderStartSlot.getTime()))
                        it.remove();
                    else break;
                }
            }
        }


        for (int i = 0; i < slots.size(); i++) {

            String startTimeString = slots.get(i).getStartTime();
            String endTimeString = slots.get(i).getEndTime();

            Date startTimeDate = DateUtils.getInstance().getDateFromString(startTimeString, STANDARD_DATE_FORMAT_TZ);
            Date endTimeDate = DateUtils.getInstance().getDateFromString(endTimeString, STANDARD_DATE_FORMAT_TZ);

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.MINUTE, timeSlotsModelResponse.getData().getPreBookingBuffer());

            if (currentCalendar.getTime().before(startTimeDate)) {
                if (selectedDateTimeCheckoutString.isEmpty() || isStartDate) {
                    timeSlotsList.add(slots.get(i));
                } else if (startTimeDate.after(selectedDateTimeCheckout) && (selectedDateTimeCheckout.getTime() + serviceTime * 60 * 1000 < startTimeDate.getTime())) {
                    timeSlotsList.add(slots.get(i));
                }
            }
        }

        laundryAvailTimeSlotsList = new ArrayList<>();
        ArrayList<Slot> morningTimeSlotsList = new ArrayList<>(), noonTimeSlotsList = new ArrayList<>(), eveningTimeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsList.size(); i++) {
            if (timeSlotsList.get(i).getTimeOfDay() == 0) {
                morningTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 1) {
                noonTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 2) {
                eveningTimeSlotsList.add(timeSlotsList.get(i));
            }
        }

        if (morningTimeSlotsList.size() > 0) {
            LaundryScheduleTimeSlotsResponse availableTimeSlotsModelResponse = new LaundryScheduleTimeSlotsResponse(getStrings(R.string.morning), morningTimeSlotsList);
            laundryAvailTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (noonTimeSlotsList.size() > 0) {
            LaundryScheduleTimeSlotsResponse availableTimeSlotsModelResponse = new LaundryScheduleTimeSlotsResponse(getStrings(R.string.afternoon), noonTimeSlotsList);
            laundryAvailTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (eveningTimeSlotsList.size() > 0) {
            LaundryScheduleTimeSlotsResponse availableTimeSlotsModelResponse = new LaundryScheduleTimeSlotsResponse(getStrings(R.string.evening), eveningTimeSlotsList);
            laundryAvailTimeSlotsList.add(availableTimeSlotsModelResponse);
        }

        if (laundryAvailTimeSlotsList.size() > 0) {
            tvNoSlotsAvailable.setVisibility(View.GONE);
            rvTimeSlotsRecyclerList.setVisibility(View.VISIBLE);
        } else {
            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
            rvTimeSlotsRecyclerList.setVisibility(View.GONE);
        }

        scheduleTimeSlotsAdapter = new ScheduleTimeSlotsAdapter(laundryAvailTimeSlotsList, mActivity, new TimeSlotAdapter.CallbackTookanAgent() {
            @Override
            public void onTimeSlotSelected(Date date, Date endDate) {
                onTimeSlotSelectedd(date, endDate, timeSlotsModelResponse.getData().getServiceTime());
            }
        });


        rvTimeSlotsRecyclerList.setAdapter(scheduleTimeSlotsAdapter);
    }

    private void setNewTimeSlotAdapter(Date date, NewTimeSlotsModelResponse timeSlotsModelResponse, boolean autoSelect) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        ArrayList<SortedDatesModel> timeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsModelResponse.getData().getSlots().size(); i++) {

            String startTimeString = timeSlotsModelResponse.getData().getSlots().get(i);
            Date startTimeHour = DateUtils.getInstance().getDateFromString(startTimeString, STANDARD_DATE_FORMAT_TZ);

            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startTimeHour);
            startDateCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.MINUTE, timeSlotsModelResponse.getData().getPreBookingBuffer());


            if (currentCalendar.getTime().before(startDateCalendar.getTime())) {

                if (selectedDateTimeCheckoutString.isEmpty()) {
                    SortedDatesModel sortedDatesModel = new SortedDatesModel(startDateCalendar.getTime());
                    timeSlotsList.add(sortedDatesModel);
                } else {
                    SortedDatesModel sortedDatesModel = new SortedDatesModel(startDateCalendar.getTime());
                    if (isStartDate) {
                        if (sortedDatesModel.getDateTime().before(selectedDateTimeCheckout)) {
                            timeSlotsList.add(sortedDatesModel);
                        }
                    } else {
                        if (sortedDatesModel.getDateTime().after(selectedDateTimeCheckout)) {
                            timeSlotsList.add(sortedDatesModel);
                        }
                    }
                }
            }
        }

//        Collections.sort(timeSlotsList);
//
//        Set<Date> hs = new HashSet<>();
//        for (int i = 0; i < timeSlotsList.size(); i++) {
//            hs.add(timeSlotsList.get(i).getDateTime());
//        }
//        timeSlotsList.clear();
//        for (int i = 0; i < hs.size(); i++) {
//            SortedDatesModel sortedDatesModel = new SortedDatesModel((Date) hs.toArray()[i]);
//            timeSlotsList.add(sortedDatesModel);
//        }
////        timeSlotsList.addAll(hs);
//        Collections.sort(timeSlotsList);

        availTimeSlotsList = new ArrayList<>();
        ArrayList<SortedDatesModel> morningTimeSlotsList = new ArrayList<>(), noonTimeSlotsList = new ArrayList<>(), eveningTimeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsList.size(); i++) {
            if (timeSlotsList.get(i).getTimeOfDay() == 0) {
                morningTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 1) {
                noonTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 2) {
                eveningTimeSlotsList.add(timeSlotsList.get(i));
            }
        }

        if (morningTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.morning), morningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (noonTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.afternoon), noonTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (eveningTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.evening), eveningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }

        if (availTimeSlotsList.size() > 0) {

            tvNoSlotsAvailable.setVisibility(View.GONE);
            rvTimeSlotsRecyclerList.setVisibility(View.VISIBLE);


        } else {
            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
            rvTimeSlotsRecyclerList.setVisibility(View.GONE);

        }

        if (availTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect) {
            onTimeSlotSelectedd(availTimeSlotsList.get(0).getSortedDatesArrayList().get(0).getDateTime());
        }

        scheduleTimeSlotsAdapter = new ScheduleTimeSlotsAdapter(mActivity, availTimeSlotsList, availTimeSlotsList.size() == 1
                && timeSlotsList.size() == 1 && autoSelect, new TimeSlotAdapter.Callback() {
            @Override
            public void onTimeSlotSelected(Date date) {
                onTimeSlotSelectedd(date);
            }
        });


        rvTimeSlotsRecyclerList.setAdapter(scheduleTimeSlotsAdapter);

    }

    private void setTimeSlotAdapter(Date date, TimeSlotsModelResponse timeSlotsModelResponse, boolean autoSelect) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        ArrayList<SortedDatesModel> timeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsModelResponse.getData().getSlots().size(); i++) {

            String startTimeString = timeSlotsModelResponse.getData().getSlots().get(i).getStartTime();
            String endTimeString = timeSlotsModelResponse.getData().getSlots().get(i).getEndTime();

            Date startTimeHour = DateUtils.getInstance().getDateFromString(startTimeString, TIME_FORMAT_24_no_seconds);
            Date endTimeHour = DateUtils.getInstance().getDateFromString(endTimeString, TIME_FORMAT_24_no_seconds);

            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startTimeHour);
            startDateCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

            Calendar endDateCalendar = Calendar.getInstance();
            endDateCalendar.setTime(endTimeHour);
            endDateCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.MINUTE, timeSlotsModelResponse.getData().getPreBookingBuffer());

            while ((startDateCalendar.getTime().before(endDateCalendar.getTime())) || (startDateCalendar.getTime().equals(endDateCalendar.getTime()))) {
                if (currentCalendar.getTime().before(startDateCalendar.getTime())) {

                    if (selectedDateTimeCheckoutString.isEmpty()) {
                        SortedDatesModel sortedDatesModel = new SortedDatesModel(startDateCalendar.getTime());
                        timeSlotsList.add(sortedDatesModel);
                    } else {
                        SortedDatesModel sortedDatesModel = new SortedDatesModel(startDateCalendar.getTime());
                        if (isStartDate) {
                            if (sortedDatesModel.getDateTime().before(selectedDateTimeCheckout)) {
                                timeSlotsList.add(sortedDatesModel);
                            }
                        } else {
                            if (sortedDatesModel.getDateTime().after(selectedDateTimeCheckout)) {
                                timeSlotsList.add(sortedDatesModel);
                            }
                        }
                    }


                }
                startDateCalendar.add(Calendar.MINUTE, bufferSchedule);
            }
        }

        Collections.sort(timeSlotsList);

        Set<Date> hs = new HashSet<>();
        for (int i = 0; i < timeSlotsList.size(); i++) {
            hs.add(timeSlotsList.get(i).getDateTime());
        }
        timeSlotsList.clear();
        for (int i = 0; i < hs.size(); i++) {
            SortedDatesModel sortedDatesModel = new SortedDatesModel((Date) hs.toArray()[i]);
            timeSlotsList.add(sortedDatesModel);
        }
//        timeSlotsList.addAll(hs);
        Collections.sort(timeSlotsList);

        availTimeSlotsList = new ArrayList<>();
        ArrayList<SortedDatesModel> morningTimeSlotsList = new ArrayList<>(), noonTimeSlotsList = new ArrayList<>(), eveningTimeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsList.size(); i++) {
            if (timeSlotsList.get(i).getTimeOfDay() == 0) {
                morningTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 1) {
                noonTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 2) {
                eveningTimeSlotsList.add(timeSlotsList.get(i));
            }
        }

        if (morningTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.morning), morningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (noonTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.afternoon), noonTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (eveningTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.evening), eveningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }

        if (availTimeSlotsList.size() > 0) {

            tvNoSlotsAvailable.setVisibility(View.GONE);
            rvTimeSlotsRecyclerList.setVisibility(View.VISIBLE);


        } else {
            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
            rvTimeSlotsRecyclerList.setVisibility(View.GONE);

        }

        if (availTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect) {
            onTimeSlotSelectedd(availTimeSlotsList.get(0).getSortedDatesArrayList().get(0).getDateTime());
        }

        scheduleTimeSlotsAdapter = new ScheduleTimeSlotsAdapter(mActivity, availTimeSlotsList, availTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect, new TimeSlotAdapter.Callback() {
            @Override
            public void onTimeSlotSelected(Date date) {
                onTimeSlotSelectedd(date);
            }
        });


        rvTimeSlotsRecyclerList.setAdapter(scheduleTimeSlotsAdapter);
    }

    private void setMerchantTimeSlotAdapter(Date date, MerchantTimeSlotData merchantTimeSlotData, boolean autoSelect) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        ArrayList<SortedDatesModel> timeSlotsList = new ArrayList<>();

        List<com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots.Slot> slots;

        if (Dependencies.isLaundryApp() && storefrontData.getCreateDeliverySlots() == 1 && !isStartDate) {
            slots = merchantTimeSlotData.getDeliverySlots();
        } else {
            slots = merchantTimeSlotData.getSlots();
        }

        if (storefrontData.getIsBufferSlotForEverydayEnabled() == 1) {

            if (slots != null && slots.size() > 0) {
                Calendar orderStartSlot;
                orderStartSlot = toCalendar(DateUtils.getInstance().getDateFromString(slots.get(0).getDate(),
                        STANDARD_DATE_FORMAT_TZ));
                orderStartSlot.add(Calendar.MINUTE, merchantTimeSlotData.getPreBookingBuffer());
                Iterator<com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots.Slot> it = slots.iterator();
                while (it.hasNext()) {
                    com.tookancustomer.models.scheduleTimeSlots.merchantTimeSlots.Slot filtered = it.next();
                    if (DateUtils.getInstance().getDateFromString(filtered.getDate(), STANDARD_DATE_FORMAT_TZ)
                            .before(orderStartSlot.getTime()))
                        it.remove();
                    else break;
                }

            }
        }



        for (int i = 0; i < slots.size(); i++) {

            String timeString = slots.get(i).getDate();
            Date timeHour = DateUtils.getInstance().getDateFromString(timeString, STANDARD_DATE_FORMAT_TZ);

            Calendar startDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(timeHour);

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.MINUTE, merchantTimeSlotData.getPreBookingBuffer());

            if (currentCalendar.getTime().before(startDateCalendar.getTime())) {

                if (selectedDateTimeCheckoutString.isEmpty()) {
                    SortedDatesModel sortedDatesModel = new SortedDatesModel(startDateCalendar.getTime(), slots.get(i).getIsBooked() == 1);
                    timeSlotsList.add(sortedDatesModel);
                } else {
                    SortedDatesModel sortedDatesModel = new SortedDatesModel(startDateCalendar.getTime(), slots.get(i).getIsBooked() == 1);
                    if (isStartDate) {
                        if (sortedDatesModel.getDateTime().before(selectedDateTimeCheckout)) {
                            timeSlotsList.add(sortedDatesModel);
                        }
                    } else {
                        if (sortedDatesModel.getDateTime().after(selectedDateTimeCheckout)) {
                            timeSlotsList.add(sortedDatesModel);
                        }
                    }
                }
            }

        }

        availTimeSlotsList = new ArrayList<>();
        ArrayList<SortedDatesModel> morningTimeSlotsList = new ArrayList<>(), noonTimeSlotsList = new ArrayList<>(), eveningTimeSlotsList = new ArrayList<>();

        for (int i = 0; i < timeSlotsList.size(); i++) {
            if (timeSlotsList.get(i).getTimeOfDay() == 0) {
                morningTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 1) {
                noonTimeSlotsList.add(timeSlotsList.get(i));
            } else if (timeSlotsList.get(i).getTimeOfDay() == 2) {
                eveningTimeSlotsList.add(timeSlotsList.get(i));
            }
        }

        if (morningTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.morning), morningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (noonTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.afternoon), noonTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }
        if (eveningTimeSlotsList.size() > 0) {
            AvailableTimeSlotsModelResponse availableTimeSlotsModelResponse = new AvailableTimeSlotsModelResponse(getStrings(R.string.evening), eveningTimeSlotsList);
            availTimeSlotsList.add(availableTimeSlotsModelResponse);
        }

        if (availTimeSlotsList.size() > 0) {
            tvNoSlotsAvailable.setVisibility(View.GONE);
            rvTimeSlotsRecyclerList.setVisibility(View.VISIBLE);
        } else {
            tvNoSlotsAvailable.setVisibility(View.VISIBLE);
            rvTimeSlotsRecyclerList.setVisibility(View.GONE);
        }

        if (availTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect) {
            onTimeSlotSelectedd(availTimeSlotsList.get(0).getSortedDatesArrayList().get(0).getDateTime());
        }

        scheduleTimeSlotsAdapter = new ScheduleTimeSlotsAdapter(mActivity, availTimeSlotsList, availTimeSlotsList.size() == 1 && timeSlotsList.size() == 1 && autoSelect, new TimeSlotAdapter.Callback() {
            @Override
            public void onTimeSlotSelected(Date date) {
                onTimeSlotSelectedd(date);
            }
        });

        rvTimeSlotsRecyclerList.setAdapter(scheduleTimeSlotsAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
                    if (data.getSerializableExtra(PRODUCT_CATALOGUE_DATA) != null) {
                        returnIntent.putExtra(PRODUCT_CATALOGUE_DATA, data.getSerializableExtra(PRODUCT_CATALOGUE_DATA));
                    } else {
                        returnIntent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
                    }
                    setResult(RESULT_OK, returnIntent);
                    onBackPressed();
                }
                break;
        }
    }

    public void onTimeSlotSelectedd(Date date, Date endDate) {
        onTimeSlotSelectedd(date, endDate, 0);
    }

    public void onTimeSlotSelectedd(Date date, Date endDate, int serviceTime) {/*Here service time is used for laundry to maintain some gap between start and end Date*/
        long differenceMultiple = 0, currentDifference = 0;
        if (productDataItem != null) {
            differenceMultiple = Constants.ProductsUnitType.getStartEndDifferenceMultiple(productDataItem.getUnitType(), productDataItem.getUnit().intValue());
            currentDifference = endDate.getTime() - selectedDateTimeCheckout.getTime();
        }
        if (isStartDate && !selectedDateTimeCheckoutString.isEmpty() && (date.after(selectedDateTimeCheckout) || date.equals(selectedDateTimeCheckout))) {
            Utils.showToast(mActivity, getStrings(R.string.errorText_startTime_shouldBe_less_than_endTime).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)).replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
        } else if (!isStartDate && !selectedDateTimeCheckoutString.isEmpty() && (endDate.before(selectedDateTimeCheckout) || endDate.equals(selectedDateTimeCheckout))) {
            Utils.showToast(mActivity, getStrings(R.string.errorText_endTime_shouldBe_greater_than_startTime).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(false)).replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
        } else if (!isStartDate && !isSchedulingFromCheckout && productDataItem != null && productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && !(Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.FIXED) && !(currentDifference % differenceMultiple == 0)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.errorText_diff_bw_startTime_endTime_should_be_multiple_of)
                    .replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true))
                    .replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true))
                    .replace(UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(mActivity, productDataItem.getUnit().intValue(), productDataItem.getUnitType(), true)))
                    .button(getStrings(R.string.ok_text))
                    .build().show();
        } else if (!isStartDate && Dependencies.isLaundryApp() && isSchedulingFromCheckout && (date.getTime() - selectedDateTimeCheckout.getTime()) / (1000 * 60) < serviceTime) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDateTimeCheckout);
            cal.add(Calendar.MINUTE, serviceTime);

            Utils.showToast(mActivity, getStrings(R.string.please_select_deliver_date_after) + " " +
                    DateUtils.getInstance().getFormattedDate(cal.getTime(), UIManager.getDateTimeFormat()));
        } else {
            if (isStartDate)
                selectedDateToBeShown = date;
            else
                selectedDateToBeShown = endDate;

            if (scheduleTimeSlotsAdapter != null) {
                scheduleTimeSlotsAdapter.isHeaderVisibilityManage = false;
                scheduleTimeSlotsAdapter.notifyDataSetChanged();
            }

            if (isSchedulingFromCheckout) {
                Bundle extras = new Bundle();
                if (Dependencies.isLaundryApp()) {
                    extras.putLong("date", date.getTime());
                    extras.putLong("endDate", endDate.getTime());
                    extras.putInt("service_time", serviceTime);
                } else {
                    if (isStartDate)
                        extras.putLong("date", date.getTime());
                    else
                        extras.putLong("date", endDate.getTime());
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtras(extras);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                if (isStartDate) {
                    Bundle extras = new Bundle();
                    extras.putInt(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                    extras.putSerializable(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                    extras.putBoolean(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                    extras.putString(Keys.Extras.SELECTED_DATE, DateUtils.getInstance().getFormattedDate(date, Constants.DateFormat.STANDARD_DATE_FORMAT));
                    extras.putBoolean(Keys.Extras.IS_START_TIME, false);
                    if (isAgentSelected) {
                        extras.putInt(Keys.Extras.AGENT_ID, agentId);
                        extras.putBoolean(Keys.Extras.IS_AGENT_SELECTED, true);
                    }

                    Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                    intent.putExtras(extras);
                    startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                } else {
                    productDataItem.setProductStartDate(selectedDateTimeCheckout);
                    productDataItem.setProductEndDate(endDate);

//                            productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity()+1);
//                            Dependencies.addCartItem(mActivity, productDataItem);

                    Bundle extras = new Bundle();
                    extras.putSerializable(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(extras);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }

            }
        }
    }

    public void onTimeSlotSelectedd(Date date) {
        long differenceMultiple = 0, currentDifference = 0;
        if (productDataItem != null) {
            differenceMultiple = Constants.ProductsUnitType.getStartEndDifferenceMultiple(productDataItem.getUnitType(), productDataItem.getUnit().intValue());
            currentDifference = date.getTime() - selectedDateTimeCheckout.getTime();
        }

        if (isStartDate && !selectedDateTimeCheckoutString.isEmpty() && (date.after(selectedDateTimeCheckout) || date.equals(selectedDateTimeCheckout))) {
            Utils.showToast(mActivity, getStrings(R.string.errorText_startTime_shouldBe_less_than_endTime).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)).replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
        } else if (!isStartDate && !selectedDateTimeCheckoutString.isEmpty() && (date.before(selectedDateTimeCheckout) || date.equals(selectedDateTimeCheckout))) {
            Utils.showToast(mActivity, getStrings(R.string.errorText_endTime_shouldBe_greater_than_startTime).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(false)).replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true)));
        } else if (!isStartDate && !isSchedulingFromCheckout && productDataItem != null &&
                productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                && !(Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.FIXED)
                && !(currentDifference % differenceMultiple == 0)) {

            new AlertDialog.Builder(mActivity).message(getStrings(R.string.errorText_diff_bw_startTime_endTime_should_be_multiple_of)
                    .replace(TerminologyStrings.END_TIME, StorefrontCommonData.getTerminology().getEndTime(true))
                    .replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true))
                    .replace(UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(mActivity, productDataItem.getUnit().intValue(), productDataItem.getUnitType(), true)))
                    .button(getStrings(R.string.ok_text))
                    .build().show();
        } else {
            selectedDateToBeShown = date;
            if (scheduleTimeSlotsAdapter != null) {
                scheduleTimeSlotsAdapter.isHeaderVisibilityManage = false;
                scheduleTimeSlotsAdapter.notifyDataSetChanged();
            }
            if (isSchedulingFromCheckout) {
                Bundle extras = new Bundle();
                extras.putLong("date", date.getTime());
                Intent returnIntent = new Intent();
                returnIntent.putExtras(extras);
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                if (isStartDate) {
                    productDataItem.setProductStartDate(date);

                    /**
                     * if service time is greater than 0 then
                     * then set end date as start date + service time
                     */
                    if (serviceTime > 0) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.MINUTE, serviceTime);
                        productDataItem.setProductEndDate(cal.getTime());
                        checkTimeSlots(date,
                                cal.getTime());

                        return;
                    }

                    Bundle extras = new Bundle();
                    extras.putInt(KEY_ITEM_POSITION, itemPos);
                    extras.putSerializable(PRODUCT_CATALOGUE_DATA, productDataItem);
                    extras.putInt("SAME_ACTIVITY", isSelfPickUp);
                    extras.putBoolean(IS_SCHEDULING_FROM_CHECKOUT, false);
                    extras.putString(SELECTED_DATE, DateUtils.getInstance().getFormattedDate(date, Constants.DateFormat.STANDARD_DATE_FORMAT_NEW));
                    extras.putBoolean(IS_START_TIME, false);
                    if (isAgentSelected) {
                        extras.putInt(AGENT_ID, agentId);
                        extras.putBoolean(IS_AGENT_SELECTED, true);
                    }

                    Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                    intent.putExtras(extras);
                    startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                } else {
                    productDataItem.setProductStartDate(selectedDateTimeCheckout);
                    productDataItem.setProductEndDate(date);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.MINUTE, serviceTime);
                    checkTimeSlots(date,
                            cal.getTime());

                }
            }
        }
    }

    private void checkTimeSlots(final Date startDate, final Date endDate) {
        UserData userData = StorefrontCommonData.getUserData();
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(PRODUCT_ID, productDataItem.getProductId());
        commonParams.add(Keys.APIFieldKeys.START_TIME, DateUtils.getInstance().getFormattedDate(startDate, STANDARD_DATE_FORMAT_TZ));
        commonParams.add(Keys.APIFieldKeys.END_TIME, DateUtils.getInstance().getFormattedDate(endDate, STANDARD_DATE_FORMAT_TZ));
        commonParams.add(USER_ID, productDataItem.getUserId());
        commonParams.add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add(CURRENCY_ID, Utils.getCurrencyId());
        commonParams.add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId());

        Dependencies.addCommonParameters(commonParams, this, userData);

        if (isAgentSelected)
            commonParams.add("agent_id", agentId);

        RestClient.getApiInterface(mActivity).checkTimeSlots(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {


                        if (((LinkedTreeMap) baseModel.data).containsKey("surge_amount") &&
                                ((LinkedTreeMap) baseModel.data).get("surge_amount") != null
                                && ((Double) ((LinkedTreeMap) baseModel.data).get("surge_amount") > 0.0)) {
                            Double surge_amount = (Double) ((LinkedTreeMap) baseModel.data).get("surge_amount");
                            productDataItem.setSurgeAmount(surge_amount);

                            new OptionsDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.surge_msg).replace(SURGE_MSG, StorefrontCommonData.getTerminology().getSurgeApplied())).listener(new OptionsDialog.Listener() {
                                @Override
                                public void performPositiveAction(int purpose, Bundle backpack) {
                                    Bundle extras = new Bundle();
                                    extras.putInt(KEY_ITEM_POSITION, itemPos);
                                    extras.putSerializable(PRODUCT_CATALOGUE_DATA, productDataItem);
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtras(extras);
                                    setResult(RESULT_OK, returnIntent);
                                    finish();
                                }

                                @Override
                                public void performNegativeAction(int purpose, Bundle backpack) {
                                }
                            }).build().show();


                        } else {
                            productDataItem.setSurgeAmount(0.0);
                            Bundle extras = new Bundle();
                            extras.putInt(KEY_ITEM_POSITION, itemPos);
                            extras.putSerializable(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtras(extras);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedStartDate = Calendar.getInstance();
        selectedStartDate.set(year, month, dayOfMonth);
        getTimeSlots(selectedStartDate.getTime(), true);


    }
}