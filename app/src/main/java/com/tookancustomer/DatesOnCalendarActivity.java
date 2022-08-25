package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customviews.rangedatepicker.CalendarPickerView;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.availableDates.AvailableDate;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Utils;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.CURRENCY_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.PRODUCT_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.Extras.KEY_ITEM_POSITION;
import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_CATALOGUE_DATA;
import static com.tookancustomer.appdata.Keys.Prefs.MARKETPLACE_REF_ID;

public class DatesOnCalendarActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout rlBack;
    private TextView tvHeading;
    private UserData userData;
    private Datum data;
    private Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    private String pickupAddress = "";
    private com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;
    private AvailableDate availableDate;

    private CalendarPickerView calendar;
    private Button btnSelectDate;
    private ArrayList<Date> disableDatesList;
    private String startDateFormatted, endDateFormatted;
    private int itemPos = 0;
    private Date startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates_on_calender);
        mActivity = this;
        userData = StorefrontCommonData.getUserData();

//        Gson gson = new Gson();
//        data = gson.fromJson(getIntent().getStringExtra(PRODUCT_DETAIL_DATA), ProductTypeData.class);
//        Bundle bundle = getIntent().getExtras();
//        pickupLatitude = bundle.getDouble(PICKUP_LATITUDE);
//        pickupLongitude = bundle.getDouble(PICKUP_LONGITUDE);
//        pickupAddress = bundle.getString(PICKUP_ADDRESS);

        if (getIntent().hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
            productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) getIntent().getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
        }

        if (getIntent().hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
            itemPos = getIntent().getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
        }

        init();
        getAvailableDates();

    }

    /**
     * initialize view
     */
    private void init() {
        rlBack = findViewById(R.id.rlBack);
//        rlBack.setVisibility(View.GONE);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(R.string.select_date);
//        calendar = findViewById(R.id.calendar);
//        button = findViewById(R.id.button);


//        Utils.setVisibility(View.GONE,);
//        Utils.setVisibility(View.GONE, rlTotalQuantity);


        calendar = findViewById(R.id.calendar_view);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectDate.setText(getStrings(R.string.select_date));
        Utils.setOnClickListener(this, findViewById(R.id.rlBack), findViewById(R.id.btnSelectDate));
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(2);

//        calendar.deactivateDates(list);
//        ArrayList<Date> arrayList = new ArrayList<>();
//        try {
//            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
//            String strdate = "22-2-2018";
//            String strdate2 = "26-2-2018";
//            Date newdate = dateformat.parse(strdate);
//            Date newdate2 = dateformat.parse(strdate2);
//            arrayList.add(newdate);
//            arrayList.add(newdate2);
//        } catch (ParseException e) {
//             if (BuildConfig.DEBUG)
//                                Utils.printStackTrace(e);
//        }


        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 2);

        final Calendar lastYear = Calendar.getInstance();
//        lastYear.add(Calendar.YEAR, -10);

        initCalendar(null, lastYear.getTime(), nextYear.getTime());


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;

            case R.id.btnSelectDate:
                if (calendar.getSelectedDates() != null && calendar.getSelectedDates().size() > 0) {

                    if (calendar.getSelectedDates().size() == 1) {
                        new AlertDialog.Builder(mActivity).message(getStrings(R.string.please_select_checkin_checkout_date)).listener(new AlertDialog.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {
                            }
                        }).build().show();
                    } else {
                        startDateFormatted = DateUtils.getInstance().getFormattedDate(calendar.getSelectedDates().get(0), Constants.DateFormat.ONLY_DATE);
                        endDateFormatted = DateUtils.getInstance().getFormattedDate(calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1), Constants.DateFormat.ONLY_DATE);
                        Log.d("startDate<><>><>", startDateFormatted);
                        Log.d("endDate<><>><>", endDateFormatted);
                        checkTimeSlots(startDateFormatted, endDateFormatted);
                    }
                }
                Log.d("list", calendar.getSelectedDates().toString());
                break;

        }
    }

    /**
     * get AvailableDates call
     */
    private void getAvailableDates() {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(PRODUCT_ID, productDataItem.getProductId());

        RestClient.getApiInterface(mActivity).getAvailableDates(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                availableDate = baseModel.toResponseModel(com.tookancustomer.models.availableDates.AvailableDate.class);

                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(dateformat.parse(availableDate.getEndDate()));
                    c.add(Calendar.DATE, 1);
                } catch (ParseException e) {

                               Utils.printStackTrace(e);
                }

                try {
                    startDate = dateformat.parse(availableDate.getStartDate());
                    endDate = c.getTime();
                } catch (ParseException e) {

                               Utils.printStackTrace(e);
                }

                //if start date is before current date then show cal from current date
                final Calendar currDate = Calendar.getInstance();
                if (startDate.before(currDate.getTime())) {
                    startDate = currDate.getTime();
                }


                disableDatesList = new ArrayList<>();
//                final Calendar currDate = Calendar.getInstance();
//                currDate.add(Calendar.DAY_OF_MONTH, -1);
                try {
//                    SimpleDateFormat dateformated = new SimpleDateFormat("dd-MM-yyyy");

                    DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();


                    if (availableDate.getNotAvailableDates() != null && availableDate.getNotAvailableDates().size() > 0) {
                        for (int i = 0; i < availableDate.getNotAvailableDates().size(); i++) {
                            Date newdate = dateformat.parse(availableDate.getNotAvailableDates().get(i));
                            int retVal = dateTimeComparator.compare(startDate, newdate);
                            if (retVal == 0) {
                                disableDatesList.add(newdate);
                            }
                            if (newdate.after(startDate)) {
                                disableDatesList.add(newdate);
                            }
                        }
                    }

                    if (disableDatesList != null && disableDatesList.size() > 0) {
                        initCalendar(disableDatesList, startDate, endDate);
                    } else {
                        initCalendar(null, startDate, endDate);
                    }


                } catch (ParseException e) {

                               Utils.printStackTrace(e);
                }


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                final Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 2);

                final Calendar lastYear = Calendar.getInstance();
//        lastYear.add(Calendar.YEAR, -10);

                initCalendar(null, lastYear.getTime(), nextYear.getTime());
            }
        });

    }

    /**
     * get AvailableDates call
     */
    private void checkTimeSlots(final String startSlot, final String endSlot) {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(PRODUCT_ID, productDataItem.getProductId());
        commonParams.add(Keys.APIFieldKeys.START_TIME, startSlot);
        commonParams.add(Keys.APIFieldKeys.END_TIME, endSlot);
        commonParams.add(USER_ID, productDataItem.getUserId());
        commonParams.add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add(CURRENCY_ID, Utils.getCurrencyId());
        commonParams.add(MARKETPLACE_REF_ID, Dependencies.getMarketplaceReferenceId());

        Dependencies.addCommonParameters(commonParams, this, userData);

        RestClient.getApiInterface(mActivity).checkTimeSlots(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                if (((LinkedTreeMap) baseModel.data).containsKey("surge_amount")) {
                    double surge_amount = (double) ((LinkedTreeMap) baseModel.data).get("surge_amount");
                    productDataItem.setSurgeAmount(surge_amount);
                }
                productDataItem.setProductStartDate(calendar.getSelectedDates().get(0));
                productDataItem.setProductEndDate(calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1));

//                            productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity()+1);
//                            Dependencies.addCartItem(mActivity, productDataItem);

                Bundle extras = new Bundle();
                extras.putSerializable(PRODUCT_CATALOGUE_DATA, productDataItem);
                Intent returnIntent = new Intent();
                returnIntent.putExtras(extras);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    /**
     * initialize calendar
     *
     * @param disableDatesList
     */
    private void initCalendar(ArrayList<Date> disableDatesList, Date startDate, Date endDate) {
//        final Calendar nextYear = Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 2);

//        final Calendar lastYear = Calendar.getInstance();
//        lastYear.add(Calendar.YEAR, -10);

        if (disableDatesList != null) {
            calendar.init(startDate, endDate, new SimpleDateFormat("MMMM, yyyy", Locale.getDefault())) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
//                    .withSelectedDate(new Date())
//                .withDeactivateDates(list)
//                    .withDeactivateDates(arrayList)
                    .withHighlightedDates(disableDatesList);
        } else {
            calendar.init(startDate, endDate, new SimpleDateFormat("MMMM, yyyy", Locale.getDefault())) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE); //
//                    .withSelectedDate(new Date());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Utils.hideSoftKeyboard(this, tvSubTotal);
        /* Code to analyse the User action on asking to enable gps */
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

}
