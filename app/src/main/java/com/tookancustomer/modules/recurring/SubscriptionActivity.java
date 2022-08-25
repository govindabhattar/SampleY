package com.tookancustomer.modules.recurring;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.RecuringSurgeDetailsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.RecurringSurgeData;
import com.tookancustomer.models.subscription.Days;
import com.tookancustomer.models.subscription.SubscriptionData;
import com.tookancustomer.models.subscription.SubscriptionSlots;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.tookancustomer.modules.recurring.fragments.SubscriptionRepeatFragment;

import org.json.JSONArray;

import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE;
import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE_NEW;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

/**
 * Created by Ashutosh Ojha on 2/14/19.
 */
public class SubscriptionActivity extends BaseActivity implements View.OnClickListener, SubscriptionRepeatFragment.Subscription, DatePickerDialog.OnDateSetListener {

    private FrameLayout flContainer;
    private TextView tvRepeat, tvRepeatDays, tvSelectedDays, tvSubscribe, tvStartDateLabel, tvStartTimeLabel, tvEndDateLabel, tvEndAfterLabel, tvOccurrenceLabel;
    private EditText etStartDate, etStartTime, etEndDate, etEndAfter;
    private RadioGroup rgSubscriptionEnd;
    private RadioButton rbEndDate, rbEndAfter;
    private CardView cvEndDate, cvEndAfter;
    private ArrayList<Days> selectedDaysList;
    private String startDate = "", endDate = "", endAfter;
    private boolean isStartDate;
    private SubscriptionData subscriptionData;
    private ArrayList<SubscriptionSlots> subscriptionSlotsArrayList;
    private String startTime;
    private boolean isSubscriptionCount;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);


        if (getIntent() != null) {
            startDate = getIntent().getStringExtra("startDate");
            startTime = getIntent().getStringExtra("startTime");
            endDate = getIntent().getStringExtra("endDate");
            endAfter = getIntent().getStringExtra("endAfter");
            isSubscriptionCount = getIntent().getBooleanExtra("isSubscriptionCount", false);
            selectedDaysList = (ArrayList<Days>) getIntent().getSerializableExtra("selectedDays");
            hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("hashMap");

        }
        initializeFields();

        getRecurringSlotsApi();
    }

    private void getRecurringSlotsApi() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        if (startDate != null && !startDate.isEmpty()) {
            commonParams.add(Keys.APIFieldKeys.DATE, startDate);

        } else {
            commonParams.add(Keys.APIFieldKeys.DATE, DateUtils.getInstance().getTodaysDate(ONLY_DATE_NEW));

        }

        RestClient.getApiInterface(mActivity).getRecurringSlots(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(final BaseModel baseModel) {

                subscriptionData = baseModel.toResponseModel(SubscriptionData.class);
                subscriptionSlotsArrayList = new ArrayList<>();

                for (int i = 0; i < subscriptionData.getSlotsArray().size(); i++) {


                    String timeString = subscriptionData.getSlotsArray().get(i);
                    Date timeHour = DateUtils.getInstance().getDateFromString(timeString, STANDARD_DATE_FORMAT_TZ);

                    Calendar startDateCalendar = Calendar.getInstance();
                    startDateCalendar.setTime(timeHour);

                    Calendar currentCalendar = Calendar.getInstance();
//                    currentCalendar.add(Calendar.MINUTE, merchantTimeSlotData.getPreBookingBuffer());


                    if (currentCalendar.getTime().before(startDateCalendar.getTime())) {

                        SubscriptionSlots subscriptionSlots = new SubscriptionSlots();
                        subscriptionSlots.setSlotTime(subscriptionData.getSlotsArray().get(i));
                        subscriptionSlotsArrayList.add(subscriptionSlots);
                    }


                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void initializeFields() {
        ((TextView) findViewById(R.id.tvHeading)).setText(StorefrontCommonData.getTerminology().getSubscribe());
        flContainer = findViewById(R.id.flContainer);
        tvRepeat = findViewById(R.id.tvRepeat);
        tvRepeatDays = findViewById(R.id.tvRepeatDays);
        tvSelectedDays = findViewById(R.id.tvSelectedDays);
        tvStartDateLabel = findViewById(R.id.tvStartDateLabel);
        tvStartTimeLabel = findViewById(R.id.tvStartTimeLabel);
        etStartDate = findViewById(R.id.etStartDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndDate = findViewById(R.id.etEndDate);
        etEndAfter = findViewById(R.id.etEndAfter);
        tvEndDateLabel = findViewById(R.id.tvEndDateLabel);
        tvEndAfterLabel = findViewById(R.id.tvEndAfterLabel);
        tvSubscribe = findViewById(R.id.tvSubscribe);
        tvOccurrenceLabel = findViewById(R.id.tvOccurrenceLabel);
        rgSubscriptionEnd = findViewById(R.id.rgSubscriptionEnd);
        rgSubscriptionEnd = findViewById(R.id.rgSubscriptionEnd);
        rbEndDate = findViewById(R.id.rbEndDate);
        rbEndAfter = findViewById(R.id.rbEndAfter);
        cvEndDate = findViewById(R.id.cvEndDate);
        cvEndAfter = findViewById(R.id.cvEndAfter);

        tvSubscribe.setText(StorefrontCommonData.getTerminology().getSubscribe());
        tvRepeat.setText(getStrings(R.string.repeat_text));
        tvRepeatDays.setText(getStrings(R.string.every_text));
        tvStartDateLabel.setText(getStrings(R.string.start_date));
        tvStartTimeLabel.setText(getStrings(R.string.start_time));
        tvEndDateLabel.setText(getStrings(R.string.end_date));
        tvEndAfterLabel.setText(getStrings(R.string.ends_after));
        tvOccurrenceLabel.setText(getStrings(R.string.occurrences));
        rbEndDate.setText(getStrings(R.string.end_date));
        rbEndAfter.setText(getStrings(R.string.ends_after));
//        etStartDate.setHint(getStrings(R.string.select_start_date).replace(TerminologyStrings.START__TEXT, getStrings(R.string.start_text)));
        etStartDate.setHint(getStrings(R.string.select_start_date).replace(TerminologyStrings.START__TEXT_SMALL, getStrings(R.string.start_text)));

        etStartTime.setHint(getStrings(R.string.select_startTime).replace(TerminologyStrings.START_TIME, StorefrontCommonData.getTerminology().getStartTime(true)));
        etEndDate.setHint(getStrings(R.string.select_end_date));
        tvSelectedDays.setText(R.string.select_days_of_week);

        tvRepeat.setText(CustomViewsUtil.createSpan(mActivity, tvRepeat.getText().toString(), getStrings(R.string.asterik)));
        tvStartDateLabel.setText(CustomViewsUtil.createSpan(mActivity, tvStartDateLabel.getText().toString(), getStrings(R.string.asterik)));
        tvStartTimeLabel.setText(CustomViewsUtil.createSpan(mActivity, tvStartTimeLabel.getText().toString(), getStrings(R.string.asterik)));
        tvEndDateLabel.setText(CustomViewsUtil.createSpan(mActivity, tvEndDateLabel.getText().toString(), getStrings(R.string.asterik)));
        tvEndAfterLabel.setText(CustomViewsUtil.createSpan(mActivity, tvEndAfterLabel.getText().toString(), getStrings(R.string.asterik)));


        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(R.id.tvRepeat), etStartDate, etEndDate,
                findViewById(R.id.tvSubscribe), findViewById(R.id.llSelectDays), etStartTime);

        rgSubscriptionEnd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, final int checkedId) {
                if (checkedId == R.id.rbEndDate) {
                    cvEndDate.setVisibility(View.VISIBLE);
                    cvEndAfter.setVisibility(View.GONE);
                } else {
                    endDate = "";
                    etEndDate.setText("");
                    cvEndDate.setVisibility(View.GONE);
                    cvEndAfter.setVisibility(View.VISIBLE);
                }
            }
        });

        if (startDate != null) {
            etStartDate.setText(startDate);
        }

        if (startTime != null) {
            etStartTime.setText(DateUtils.getInstance().parseDateAs(startTime, STANDARD_DATE_FORMAT_TZ,
                    UIManager.getTimeFormat()));
        }

        if (isSubscriptionCount) {
            rbEndAfter.setChecked(true);
            etEndAfter.setText(endAfter);
        } else {
            rbEndDate.setChecked(true);

            etEndDate.setText(endDate);
        }

        if (selectedDaysList != null) {
            repetitionDays(selectedDaysList);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;

            case R.id.etStartDate:
                isStartDate = true;
                openDatePicker();
                break;

            case R.id.etEndDate:
                isStartDate = false;
                openDatePicker();
                break;

            case R.id.etStartTime:
                if (startDate != null && !startDate.isEmpty()) {

                    if (subscriptionSlotsArrayList.size() > 0) {
                        Intent intent = new Intent(this, SubscriptionSlotsActivity.class);
                        intent.putExtra("slotsList", (Serializable) subscriptionSlotsArrayList);
                        startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                    } else {
                        Utils.snackBar(this, getStrings(R.string.no_slots_available));

                    }

                }

                break;

            case R.id.tvSubscribe:

                if (isValid()) {

                    if (true) {

                        getSurgeData();

                    } else {
                        gotoNext();
                    }
                }


                break;
            case R.id.llSelectDays:
                flContainer.setVisibility(View.VISIBLE);
                if (subscriptionData != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new SubscriptionRepeatFragment(subscriptionData.getActiveDays()
                            , selectedDaysList)).commit();
                }

                break;
        }
    }

    private void getSurgeData() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("marketplace_user_id", hashMap.get("marketplace_user_id"));
        stringStringHashMap.put("access_token", hashMap.get("access_token"));
        stringStringHashMap.put("user_id", hashMap.get("user_id"));
        stringStringHashMap.put("app_type", hashMap.get("app_type"));
        stringStringHashMap.put("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        stringStringHashMap.put("dual_user_key", hashMap.get("dual_user_key"));
        stringStringHashMap.put("marketplace_reference_id", hashMap.get("marketplace_reference_id"));
        stringStringHashMap.put("reference_id", hashMap.get("reference_id"));
        stringStringHashMap.put("yelo_app_type", hashMap.get("yelo_app_type"));
        stringStringHashMap.put("app_access_token", hashMap.get("app_access_token"));
        stringStringHashMap.put("form_id", hashMap.get("form_id"));
        stringStringHashMap.put("is_demo_app", hashMap.get("is_demo_app"));
        stringStringHashMap.put("vendor_id", hashMap.get("vendor_id"));
        stringStringHashMap.put("app_version", hashMap.get("app_version"));
        stringStringHashMap.put("device_token", hashMap.get("device_token"));
        stringStringHashMap.put("domain_name", hashMap.get("domain_name"));
        stringStringHashMap.put("latitude", hashMap.get("latitude"));
        stringStringHashMap.put("longitude", hashMap.get("longitude"));
        stringStringHashMap.put("job_pickup_latitude", hashMap.get("job_pickup_latitude"));
        stringStringHashMap.put("job_pickup_longitude", hashMap.get("job_pickup_longitude"));
//        stringStringHashMap.put("is_custom_order", hashMap.get("is_custom_order"));
        stringStringHashMap.put("customer_address", hashMap.get("customer_address"));
//        stringStringHashMap.put("job_pickup_datetime", hashMap.get("job_pickup_datetime"));
        stringStringHashMap.put("schedule_time", DateUtils.getInstance().parseDateAs(startTime, STANDARD_DATE_FORMAT_TZ,
                Constants.DateFormat.TIME_FORMAT_24_WITHOUT_SECOND));

        if (hashMap.containsKey("pick_and_drop") && hashMap.get("pick_and_drop").equalsIgnoreCase("1")) {
            stringStringHashMap.put(CUSTOME_PICKUP_ADDRESS, hashMap.get(CUSTOME_PICKUP_ADDRESS));
            stringStringHashMap.put("custom_pickup_latitude", hashMap.get("custom_pickup_latitude"));
            stringStringHashMap.put("custom_pickup_longitude", hashMap.get("custom_pickup_longitude"));
            stringStringHashMap.put("pick_and_drop", hashMap.get("pick_and_drop"));

        }

        JSONArray jsonArray = new JSONArray();
        if (selectedDaysList != null) {
            for (int i = 0; i < selectedDaysList.size(); i++) {
                if (selectedDaysList.get(i).isActive() && selectedDaysList.get(i).isSelected()) {
                    jsonArray.put(selectedDaysList.get(i).getDayId());
                }
            }
        }
        stringStringHashMap.put("day_ids", jsonArray.toString());


        RestClient.getApiInterface(this).getRecurringSurgeDetails(stringStringHashMap)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {

                        RecurringSurgeData recurringSurgeData = baseModel.toResponseModel(RecurringSurgeData.class);


                        if (recurringSurgeData.getData() != null && recurringSurgeData.getData().size() > 0) {
                            new RecuringSurgeDetailsDialog(SubscriptionActivity.this, new RecuringSurgeDetailsDialog.SelectionListner() {
                                @Override
                                public void onDialogDismiss() {
                                    gotoNext();
                                }
                            }, recurringSurgeData.getData(), 3).show();
                        } else {
                            gotoNext();
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }

    private void gotoNext() {
        Intent intentSubscription = getIntent();
        intentSubscription.putExtra("selectedDays", selectedDaysList);
        intentSubscription.putExtra("startDate", startDate);
        intentSubscription.putExtra("startTime", startTime);
        intentSubscription.putExtra("endDate", endDate);
        intentSubscription.putExtra("endAfter", etEndAfter.getText().toString());
        intentSubscription.putExtra("isSubscriptionCount", rbEndAfter.isChecked());
        setResult(RESULT_OK, intentSubscription);
        finish();
    }

    private boolean isValid() {

        boolean isDaysSelected = false;

        if (selectedDaysList != null) {
            for (int i = 0; i < selectedDaysList.size(); i++) {
                if (selectedDaysList.get(i).isActive() && selectedDaysList.get(i).isSelected()) {
                    isDaysSelected = true;
                    break;
                }
            }
        }

        if (!isDaysSelected) {
            Utils.snackBar(this, getStrings(R.string.error_schedule_days));
            return false;
        }

        if (TextUtils.isEmpty(startDate)) {
            Utils.snackBar(this, getStrings(R.string.error_schedule_start_date));
            return false;
        }

        if (TextUtils.isEmpty(startTime)) {
            Utils.snackBar(this, getString(R.string.error_schedule_start_time));
            return false;
        }

        if (rbEndDate.isChecked()) {
            if (TextUtils.isEmpty(endDate)) {
                Utils.snackBar(this, getString(R.string.error_schedule_end_date));
                return false;
            }
        } else if (rbEndAfter.isChecked()) {
            if (TextUtils.isEmpty(etEndAfter.getText().toString())) {
                Utils.snackBar(this, getString(R.string.error_schedule_end_after));
                return false;
            } else {
                int occurrenceCount = Integer.parseInt(etEndAfter.getText().toString());
                if (occurrenceCount < 1) {
                    Utils.snackBar(this, getStrings(R.string.error_min_occurrence_count));
                    return false;
                }
                if (occurrenceCount > 365) {
                    Utils.snackBar(this, getStrings(R.string.error_max_occurrence_count));
                    return false;
                }
            }
        } else {
            Utils.snackBar(this, getString(R.string.error_schedule_end_type));
            return false;

        }

        return true;
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    public void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        if (isStartDate) {
            datePickerFragment.setMinDate(System.currentTimeMillis());
//            datePickerFragment.setMinDate(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        } else {
            if (startDate != null) {
                Date start = DateUtils.getInstance().getDateFromString(startDate, ONLY_DATE);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
//                calendar.getTimeInMillis();
                datePickerFragment.setMinDate((calendar.getTimeInMillis() + 1000 * 60 * 60 * 24 * 7));
                datePickerFragment.setMaxDate((calendar.getTimeInMillis() + 1000 * 60 * 60 * 24 * 90L));

//                datePickerFragment.setMinDate((1000 * 60 * 60 * 24 * 7) + System.currentTimeMillis());


            }


        }
        datePickerFragment.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void repetitionDays(final ArrayList<Days> selectedDaysList) {

        this.selectedDaysList = selectedDaysList;

        String days = "";
        for (int i = 0; i < selectedDaysList.size(); i++) {

            if (selectedDaysList.get(i).isActive() && selectedDaysList.get(i).isSelected()) {
                if (i == 0) {
                    days = days + selectedDaysList.get(i).getDisplayValueShort();

                } else {
                    if (days.equals("")) {
                        days = selectedDaysList.get(i).getDisplayValueShort();
                    } else {
                        days = days + ", " + selectedDaysList.get(i).getDisplayValueShort();

                    }

                }
            }


        }

        tvSelectedDays.setText(days);
        flContainer.setVisibility(View.GONE);

        if (getSupportFragmentManager().getBackStackEntryCount() != 0)
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.flContainer)).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case OPEN_SCHEDULE_TIME_ACTIVITY:

                if (resultCode == RESULT_OK & data != null) {
                    startTime = data.getStringExtra("selectedSlot");
                    etStartTime.setText(DateUtils.getInstance().parseDateAs(startTime, STANDARD_DATE_FORMAT_TZ,
                            UIManager.getTimeFormat()));
                }
                break;
        }
    }

    @Override
    public void close() {
        flContainer.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.flContainer)).commit();
    }

    @Override
    public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {

        if (isStartDate) {
            startDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            SimpleDateFormat format = new SimpleDateFormat(StorefrontCommonData.getAppConfigurationData().getDate_format());
            String strDate = format.format(calendar.getTime());

            //StorefrontCommonData.getAppConfigurationData().getDate_format()
            etStartDate.setText(strDate);
            getRecurringSlotsApi();
            startTime = "";
            etStartTime.setText("");
        } else if (cvEndDate.isShown()) {
            if (startDate == null) {
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.pickuo_date_time_is_required));

            } else {

                if (endDate != null && !endDate.isEmpty()) {
                    Date start = DateUtils.getInstance().getDateFromString(startDate, ONLY_DATE);
                    Date end = DateUtils.getInstance().getDateFromString(endDate, ONLY_DATE);
                    if (end.before(start)) {
                        endDate = "";
                        etEndDate.setText("");
                        Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.invalid_end_date_greater_than_current_time));

                    } else {
                        endDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        Calendar calendarend = Calendar.getInstance();
                        calendarend.set(year, month, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat(StorefrontCommonData.getAppConfigurationData().getDate_format());
                        String endDatee = format.format(calendarend.getTime());
                        etEndDate.setText(endDatee);
                    }
                } else {
                    endDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    Calendar calendarend = Calendar.getInstance();
                    calendarend.set(year, month, dayOfMonth);

                    SimpleDateFormat format = new SimpleDateFormat(StorefrontCommonData.getAppConfigurationData().getDate_format());
                    String endDatee = format.format(calendarend.getTime());
                    etEndDate.setText(endDatee);
                }


            }


        }


    }


}
