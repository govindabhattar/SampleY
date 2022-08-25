package com.tookancustomer.modules.recurring;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.adapters.BillBreakdownAdapter;
import com.tookancustomer.adapters.OrderItemRecyclerAdapter;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.dialog.RecuringSurgeDetailsDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.allrecurringdata.AllRecurringData;
import com.tookancustomer.models.allrecurringdata.Result;
import com.tookancustomer.models.billbreakdown.BillBreakdownData;
import com.tookancustomer.models.recurringFilteredVacation.FilteredDates;
import com.tookancustomer.models.recurringFilteredVacation.FilteredVacationData;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.modules.recurring.listener.CalenderListener;
import com.tookancustomer.modules.recurring.model.DateInfo;
import com.tookancustomer.modules.recurring.model.DateItem;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE;
import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE_NEW;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

/**
 * Created by Ashutosh Ojha on 2/20/19.
 */
public class RecurringTaskDetailsActivity extends BaseActivity implements View.OnClickListener, CalenderListener {
    private int ruleId;
    private Result result;
    private TextView tvTaskStatus, tvOrderTime, tvPickupAddressLabel, tvStoreNameLabel,
            tvOrderStatusLabel, tvStartDateLabel, tvStartOrderDate,
            tvOrderTimeLabel, tvSelectedDaysLabel, tvSelectedDays, tvHeading, tvEndOrderDate, tvTotalPayableHeading,
            tvTotal, tvOrderToggle, tvViewAllOrder, tvSkipDateLabel, tvSkipDates, tvEndDateLabel;
    private RecyclerView rvOrderProducts, rvTaxesList;
    private RelativeLayout rlTotalAmount, rlSkipDates;
    private int isRecurringPaused;
    private RelativeLayout rlAdditionalAmount, rlDeliverTo, rlStoreName;
    private TextView tvAdditionalHeading, tvAdditionalAmount;
    private ArrayList<Template> templateDataList;
    private TextView tvStoreName;
    private BillBreakdownAdapter taxAdapter;
    private List<FilteredDates> filteredDatesList;
    private OrderItemRecyclerAdapter orderItemsAdapter;


    ///Calender
    private ArrayList<DateInfo> dateInfoList;
    private CalendarAdapter calendarAdapter;
    private MyGridView gvCalendar;
    private DateUtils dateUtils;
    private int month, year, startMonth, startYear, endMonth, endYear;
    private View layoutCalender;
    private ImageView ivCalendarOverlay;
    private TextView tvMonthYear, tvCalendar, tvSaveSkipDates, tvDeliveryAddressLabel, tvDeliveryAddress;
    private LinearLayout llCloseCalender;
    private TextView deliverySurgeChargesTV;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_task_details);
        mActivity = this;

        ruleId = getIntent().getIntExtra(Keys.Extras.RULE_ID, 0);
        initView();


        getRecurringDetails();

    }

    private void initView() {


        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(Utils.getCallTaskAs(true, true) + " #" + ruleId);


        tvEndOrderDate = findViewById(R.id.tvEndOrderDate);
        deliverySurgeChargesTV = findViewById(R.id.deliverySurgeChargesTV);
        rvOrderProducts = findViewById(R.id.rvOrderProducts);
        rvOrderProducts.setLayoutManager(new LinearLayoutManager(mActivity));
        tvPickupAddressLabel = findViewById(R.id.tvPickupAddressLabel);
        tvStoreNameLabel = findViewById(R.id.tvStoreNameLabel);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderTimeLabel = findViewById(R.id.tvOrderTimeLabel);
        tvOrderStatusLabel = findViewById(R.id.tvOrderStatusLabel);
        tvTaskStatus = findViewById(R.id.tvTaskStatus);
        tvStartDateLabel = findViewById(R.id.tvStartDateLabel);
        tvStartOrderDate = findViewById(R.id.tvStartOrderDate);
        tvSelectedDaysLabel = findViewById(R.id.tvSelectedDaysLabel);
        tvSelectedDays = findViewById(R.id.tvSelectedDays);
        tvEndDateLabel = findViewById(R.id.tvEndDateLabel);
        tvTotalPayableHeading = findViewById(R.id.tvTotalPayableHeading);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrderToggle = findViewById(R.id.tvOrderToggle);
        tvViewAllOrder = findViewById(R.id.tvViewAllOrder);
        rlTotalAmount = findViewById(R.id.rlTotalAmount);
        rlStoreName = findViewById(R.id.rlStoreName);
        tvSkipDateLabel = findViewById(R.id.tvSkipDateLabel);
        tvSkipDates = findViewById(R.id.tvSkipDates);
        rlSkipDates = findViewById(R.id.rlSkipDates);


        tvStoreName = findViewById(R.id.tvStoreName);
        rlDeliverTo = findViewById(R.id.rlDeliverTo);
        tvDeliveryAddressLabel = findViewById(R.id.tvDeliveryAddressLabel);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);


        rvTaxesList = findViewById(R.id.rvTaxesList);
        rvTaxesList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvTaxesList.setNestedScrollingEnabled(false);


        ((TextView) findViewById(R.id.tvItemSummary)).setText(getStrings(R.string.item_summary));

        ((TextView) findViewById(R.id.tvTotalHeading)).setText(getStrings(R.string.total));


        rlAdditionalAmount = findViewById(R.id.rlAdditionalAmount);
        tvAdditionalHeading = findViewById(R.id.tvAdditionalHeading);
        tvAdditionalAmount = findViewById(R.id.tvAdditionalAmount);

        tvOrderStatusLabel.setText(Utils.getCallTaskAs(true, true) + " " + getStrings(R.string.order_status_text));
        tvStartDateLabel.setText(getStrings(R.string.start_date));
        tvStoreNameLabel.setText(StorefrontCommonData.getTerminology().getDeliveryFrom(true) + ":");
        tvPickupAddressLabel.setText(StorefrontCommonData.getTerminology().getDeliveryFrom(true) + ":");
        tvDeliveryAddressLabel.setText(StorefrontCommonData.getTerminology().getDeliveryTo(true) + ":");
        tvOrderTimeLabel.setText(getStrings(R.string.delivery_time).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)) + ":");
        tvSelectedDaysLabel.setText(getStrings(R.string.selected_days) + ":");
        tvTotalPayableHeading.setText(getStrings(R.string.total) + ":");
        tvOrderToggle.setText(getStrings(R.string.pause_order).replace(TerminologyStrings.ORDER,
                StorefrontCommonData.getTerminology().getOrder()));
        tvViewAllOrder.setText(getStrings(R.string.view_all_orders));
        tvSkipDateLabel.setText(getStrings(R.string.txt_vacation));
        tvSkipDates.setText(getStrings(R.string.txt_vacation));
        tvSkipDates.setText(getStrings(R.string.skip_dates));
        deliverySurgeChargesTV.setText(getStrings(R.string.view_delivery_surge_charges).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()));


        Utils.setOnClickListener(this, findViewById(R.id.rlBack), tvOrderToggle, rlSkipDates, deliverySurgeChargesTV,
                tvAdditionalHeading, findViewById(R.id.btnPrevious), findViewById(R.id.btnNext), findViewById(R.id.tvMonthYear)
                , findViewById(R.id.llCloseCalender), findViewById(R.id.llToday), findViewById(R.id.tvSaveSkipDates));


        //Calender
        dateUtils = DateUtils.getInstance();

        findViewById(R.id.llToday).setVisibility(View.GONE);
        llCloseCalender = findViewById(R.id.llCloseCalender);

        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvCalendar = findViewById(R.id.tvCalendar);
        tvCalendar.setText(getStrings(R.string.calender));
        tvSaveSkipDates = findViewById(R.id.tvSaveSkipDates);
        tvSaveSkipDates.setText(getStrings(R.string.save_vacation));
        gvCalendar = findViewById(R.id.gvCalendar);
        layoutCalender = findViewById(R.id.layout_calendar_view);
        ivCalendarOverlay = findViewById(R.id.ivCalendarOverlay);
        layoutCalender.setVisibility(View.GONE);

        ivCalendarOverlay.setVisibility(View.GONE);
        tvMonthYear = findViewById(R.id.tvMonthYear);

        ((TextView) findViewById(R.id.tvSubscribe)).setText(getStrings(R.string.subscribed));
        ((TextView) findViewById(R.id.tvSkipped)).setText(getStrings(R.string.skipped));
        ((TextView) findViewById(R.id.tvDisabled)).setText(getStrings(R.string.disabled));


        ((TextView) findViewById(R.id.tvTodayDateLabel)).setText(getStrings(R.string.today));
        TextView tvTodayDate = findViewById(R.id.tvTodayDate);
        tvTodayDate.setText(DateUtils.getInstance().getTodaysDate("dd"));

    }


    private void getRecurringDetails() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.RULE_ID, ruleId);
        RestClient.getApiInterface(mActivity).getRecurringDetails(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(final BaseModel baseModel) {
                try {
                    AllRecurringData allRecurringData = baseModel.toResponseModel(AllRecurringData.class);
                    result = allRecurringData.getResult().get(0);
                    TaskData taskDetails = new TaskData();
                    taskDetails.setBusinessType(result.getBusinessType());
                    taskDetails.setPd_or_appointment(result.getPd_or_appointment());
                    taskDetails.setTaskType(result.getTaskType());
                    orderItemsAdapter = new OrderItemRecyclerAdapter(mActivity, taskDetails, result.getOrderDetails());
                    rvOrderProducts.setAdapter(orderItemsAdapter);
                    setData();

                } catch (Exception e) {
//
                    Utils.printStackTrace(e);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void displayAdditionalTotalPrice(ArrayList<Template> templateDataList) {
        double cost = 0;
        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);
            if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT)
                    || template.getDataType().equals(CustomViewsConstants.CHECKBOX)
                    || template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {

                cost += template.getCost();
            }
        }

        rlAdditionalAmount.setVisibility(View.VISIBLE);
        tvAdditionalAmount.setText(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(cost));

        tvAdditionalHeading.setText(CustomViewsUtil.createSpan(mActivity, cost > 0 ? getStrings(R.string.additional_amount) :
                        getStrings(R.string.additional_info),
                "(", getStrings(R.string.view), ")", ContextCompat.getColor(mActivity, R.color.colorPrimary)));

    }

    private void setData() {
        isRecurringPaused = result.getIsPaused();
        setRecurringPausedUi();

        String startDate = result.getStartSchedule();


        tvStartOrderDate.setText(DateUtils.getInstance().parseDateAs(result.getStartSchedule(), STANDARD_DATE_FORMAT_TZ, ONLY_DATE));
        startYear = Integer.parseInt(startDate.split("-")[0]);
        startMonth = Integer.parseInt(startDate.split("-")[1]) - 1;

        tvOrderTime.setText(DateUtils.getInstance().parseDateAs(result.getScheduleTime(), Constants.DateFormat.TIME_FORMAT_24_WITHOUT_SECOND,
                UIManager.getTimeFormat()));

        if (result.getScheduleType() == Constants.RecurringType.OCCURENCE_COUNT) {
            tvEndOrderDate.setText(result.getOccurenceCount() + "");
            tvEndDateLabel.setText(getStrings(R.string.occurrences) + ":");

            String endDate = getEndDate(startDate, result.getOccurenceCount(), (ArrayList<Integer>) result.getDayArray());
            endYear = Integer.parseInt(endDate.split("-")[0]);
            endMonth = Integer.parseInt(endDate.split("-")[1]) - 1;

        } else {

            String endDate = result.getEndSchedule();

            tvEndOrderDate.setText(DateUtils.getInstance().parseDateAs(result.getEndSchedule(), STANDARD_DATE_FORMAT_TZ, ONLY_DATE));
            tvEndDateLabel.setText(getStrings(R.string.end_date) + ":");

            endYear = Integer.parseInt(endDate.split("-")[0]);
            endMonth = Integer.parseInt(endDate.split("-")[1]) - 1;

        }

        if (result.getJobPickupAddress() != null && !result.getJobPickupAddress().isEmpty()) {

            tvDeliveryAddress.setText(result.getJobPickupAddress());

        } else {
            rlDeliverTo.setVisibility(View.GONE);


        }

        if (!result.getMerchantName().isEmpty()) {
            rlStoreName.setVisibility(View.VISIBLE);
            tvStoreName.setText(result.getMerchantName());
        }

        tvTaskStatus.setText(Constants.RecurringStatus.getTaskStatusByValue(result.getStatus()).getPassive(mActivity));
        tvTaskStatus.setTextColor(mActivity.getResources().getColor(Constants.RecurringStatus.getColorRes(result.getStatus())));


        if (result.getStatus() == Constants.RecurringStatus.REJECTED.value) {
            rlSkipDates.setVisibility(View.GONE);
        }
        {
            rlSkipDates.setVisibility(View.VISIBLE);
        }

        //ArrayList<TaxesModel> billBreakdownlist = new ArrayList<>();
        ArrayList<BillBreakdownData> billBreakdownDataArrayList = new ArrayList<>();
        billBreakdownDataArrayList.add(new BillBreakdownData(getStrings(R.string.subtotal), BigDecimal.valueOf(Double.valueOf(result.getOrderAmount())), null,null));


        if (Double.parseDouble(result.getTransaction_charges().replace("\"", "")) > 0) {
            billBreakdownDataArrayList.add(new BillBreakdownData(
                    StorefrontCommonData.getTerminology()
                            .getTransactionCharge()
                    , BigDecimal.valueOf(Double.parseDouble(result.getTransaction_charges()
                    .replace("\"", ""))), BigDecimal.valueOf(0)));

           /* billBreakdownlist.add(new TaxesModel(StorefrontCommonData.getTerminology()
                    .getTransactionCharge()
                    , BigDecimal.valueOf(Double.parseDouble(result.getTransaction_charges()
                    .replace("\"", ""))), BigDecimal.valueOf(0)));*/

        }


        if (Double.valueOf(result.getDeliveryCharge()) > 0) {
            billBreakdownDataArrayList.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                    BigDecimal.valueOf(Double.valueOf(result.getDeliveryCharge())), null));
        }
        if (result.getUserTaxes().size()!=0){
            for (int i = 0; i < result.getUserTaxes().size(); i++) {
                billBreakdownDataArrayList.add(new BillBreakdownData(result.getUserTaxes().get(i).getTaxName(),
                        new BigDecimal (result.getUserTaxes().get(i).getTaxAmount()),
                        false, result.getUserTaxes().get(i).getTaxPercentage(),
                        result.getUserTaxes().get(i).getTaxType()));
            }
        }

        taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownDataArrayList);
        rvTaxesList.setAdapter(taxAdapter);

        String days = "";

        for (int i = 0; i < result.getDayArray().size(); i++) {
            if (days.isEmpty()) {
                days = getResources().getStringArray(R.array.days_array)[result.getDayArray().get(i)];
            } else {
                days = days + "," + getResources().getStringArray(R.array.days_array)[result.getDayArray().get(i)];

            }
        }

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Double.valueOf(result.getTotalAmount()) <= 0) {
            rlTotalAmount.setVisibility(View.GONE);

        } else {
            rlTotalAmount.setVisibility(View.VISIBLE);

        }
        tvSelectedDays.setText(days);


        tvTotal.setText(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Double.valueOf(result.getTotalAmount())));

        renderCurrentMonth();

        if (result.getCheckoutTemplate() != null && result.getCheckoutTemplate().size() > 0) {
            templateDataList = result.getCheckoutTemplate();
            displayAdditionalTotalPrice(templateDataList);
        }

        if (result.getRecurringSurgeListData() != null && result.getRecurringSurgeListData().size() > 0)
            deliverySurgeChargesTV.setVisibility(View.VISIBLE);
        else
            deliverySurgeChargesTV.setVisibility(View.GONE);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.rlBack:
                finish();
                break;
            case R.id.deliverySurgeChargesTV:
                new RecuringSurgeDetailsDialog(RecurringTaskDetailsActivity.this, new RecuringSurgeDetailsDialog.SelectionListner() {
                    @Override
                    public void onDialogDismiss() {

                    }
                }, result.getRecurringSurgeListData(), 4).show();
                break;

            case R.id.tvOrderToggle:
                String positiveBtnMsg, msg;
                if (isRecurringPaused == 1) {
                    positiveBtnMsg = getStrings(R.string.resume);
                    msg = getStrings(R.string.msg_pause_subscription);
                } else {
                    positiveBtnMsg = getStrings(R.string.pause);
                    msg = getStrings(R.string.msg_pause_subscription);
                }
                new OptionsDialog.Builder(mActivity).negativeButton(getStrings(R.string.cancel))
                        .positiveButton(positiveBtnMsg).message(msg).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {

                        updateRecurringRuleApi();
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {

                    }
                }).build().show();

                break;

            case R.id.rlSkipDates:
                month = startMonth;
                year = startYear;
                getFilteredVacationRule();

                break;

            case R.id.tvAdditionalHeading:
                Intent templateIntent = new Intent(this, CheckoutTemplateActivity.class);
                if (templateDataList != null) {
                    templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);
                    templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, true);
                }
                startActivityForResult(templateIntent, CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE);

                break;

            case R.id.llCloseCalender:
                manageCalenderVisibility(true);

                break;

            case R.id.tvMonthYear:
                renderCurrentMonth();

                break;

            case R.id.btnPrevious:
                if (startYear < year || (startYear == year && startMonth <= month - 1)) {
                    renderPreviousMonth();
                } else {
                    Toast.makeText(mActivity, getStrings(R.string.msg_start_date_subscription), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnNext:
                if (endYear > year || (endYear == year && endMonth >= month + 1)) {
                    renderNextMonth();
                } else {
                    Toast.makeText(mActivity, getStrings(R.string.msg_end_date_subscription), Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.llToday:
                manageCalenderVisibility(true);

            case R.id.tvSaveSkipDates:
                setVacationRuleApi();
                break;


        }
    }

    private void getFilteredVacationRule() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.RULE_ID, ruleId);
        commonParams.add(USER_ID, result.getUserId());

        RestClient.getApiInterface(mActivity).getFilteredVacationRule(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(final BaseModel baseModel) {
                FilteredVacationData filteredVacationData = baseModel.toResponseModel(FilteredVacationData.class);


                filteredDatesList = filteredVacationData.getResult();

                if (filteredDatesList != null && filteredDatesList.size() > 0) {

                    String endDate = filteredDatesList.get(filteredDatesList.size() - 1).getDate();
                    endYear = Integer.parseInt(endDate.split("-")[0]);
                    endMonth = Integer.parseInt(endDate.split("-")[1]) - 1;
                }


                manageCalenderVisibility(false);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void setVacationRuleApi() {

        llCloseCalender.performClick();
        manageCalenderVisibility(true);
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < filteredDatesList.size(); i++) {

            if (filteredDatesList.get(i).getIsSkipped() == 1) {
                jsonArray.put(filteredDatesList.get(i).getDate());
            }
        }


        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("vacation_dates", jsonArray);
        commonParams.add(Keys.APIFieldKeys.RULE_ID, ruleId);
        commonParams.add(USER_ID, result.getUserId());


        RestClient.getApiInterface(mActivity).addVacationRule(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(final BaseModel baseModel) {


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void updateRecurringRuleApi() {

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.RULE_ID, ruleId);
        commonParams.add("is_paused", isRecurringPaused == 1 ? 0 : 1);
        RestClient.getApiInterface(mActivity).updateRecurringRule(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(final BaseModel baseModel) {

                if (isRecurringPaused == 1)
                    isRecurringPaused = 0;
                else isRecurringPaused = 1;

                setRecurringPausedUi();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void setRecurringPausedUi() {

        if (isRecurringPaused == 1) {

            tvOrderToggle.setText(getStrings(R.string.resume_order).replace(TerminologyStrings.ORDER,
                    StorefrontCommonData.getTerminology().getOrder()
                    )
            );
            tvOrderToggle.setSelected(true);
            tvOrderToggle.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
        } else {
            tvOrderToggle.setText(getStrings(R.string.pause_order).replace(TerminologyStrings.ORDER,
                    StorefrontCommonData.getTerminology().getOrder()));

            tvOrderToggle.setSelected(false);
            tvOrderToggle.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));


        }
    }


    private void manageCalenderVisibility(final boolean isCalenderVisible) {
        if (!isCalenderVisible) {
            renderAndFetch();
            YoYo.with(Techniques.BounceInDown).duration(500).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    layoutCalender.setVisibility(View.VISIBLE);
                    ivCalendarOverlay.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).duration(550).playOn(ivCalendarOverlay);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).playOn(layoutCalender);
        } else {
            YoYo.with(Techniques.SlideOutUp).duration(300).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    YoYo.with(Techniques.FadeOut).duration(350).playOn(ivCalendarOverlay);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutCalender.setVisibility(View.GONE);
                    ivCalendarOverlay.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).playOn(layoutCalender);
        }
    }

    /**
     * Method to renderUI the Basic Date Pattern for Month
     * and then download the Tasks from Server
     */
    private void renderAndFetch() {

        // Sets the Current Month and Year
        setMonthYear();

        // Create the basic pattern of Calendar for Month
        renderMonthOnCalendar();

    }

    /**
     * Method to set the Month Year to the View
     */
    private void setMonthYear() {

        Date intendedDate = new GregorianCalendar(year, month, 1).getTime();
        String monthYear = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(intendedDate);

        tvMonthYear.setText(monthYear);
    }


    /**
     * Method to renderUI the previous month
     */
    private void renderPreviousMonth() {
        if (month == Calendar.JANUARY) {
            month = Calendar.DECEMBER;
            --year;
        } else {
            --month;
        }

        renderAndFetch();
    }

    /**
     * Method to renderUI next month
     */
    private void renderNextMonth() {
        if (month == Calendar.DECEMBER) {
            month = Calendar.JANUARY;
            ++year;
        } else {
            ++month;
        }
        renderAndFetch();
    }

    /**
     * Method to create a CalendarAdapter
     */
    private void renderMonthOnCalendar() {

        dateInfoList = new ArrayList<>();

        ArrayList<DateItem> allDatesInMonth = dateUtils.getAllDatesInMonth(month, year);

        DateItem firstDateInMonthItem = allDatesInMonth.get(0);

        Calendar calendar = new GregorianCalendar(firstDateInMonthItem.getYear(),
                firstDateInMonthItem.getMonth(), firstDateInMonthItem.getDay());

        int firstDayOfWeek = dateUtils.get(calendar, Calendar.DAY_OF_WEEK);

        int daysInLastMonth;
        if (month == Calendar.JANUARY) {
            daysInLastMonth = dateUtils.getDaysCount(Calendar.DECEMBER, year - 1);
        } else {
            daysInLastMonth = dateUtils.getDaysCount(month - 1, year);
        }

        for (int i = 0; i < allDatesInMonth.size(); i++) {
            DateItem dateItem = allDatesInMonth.get(i);
            DateInfo dateInfo = new DateInfo();
            dateInfo.setDateItem(dateItem);

            for (int j = 0; j < (filteredDatesList != null ? filteredDatesList.size() : 0); j++) {
                Date date = new GregorianCalendar(dateItem.getYear(), dateItem.getMonth(), dateItem.getDay()).getTime();

                if (date.compareTo(getFilteredDate(filteredDatesList.get(j).getDate())) == 0) {
                    dateInfo.setActive(true);

                    if (filteredDatesList.get(j).getIsSkipped() == 1) {
                        dateInfo.setSelected(false);
                    } else {
                        dateInfo.setSelected(true);

                    }
                }
            }


            dateInfoList.add(dateInfo);
        }

        for (int position = 0; position < firstDayOfWeek - 1; position++) {

            DateItem dateItem = DateItem.create(daysInLastMonth - position);
            DateInfo dateInfo = new DateInfo();
            dateInfo.setDateItem(dateItem);
            dateInfo.setIsExtra(true);
            dateInfoList.add(0, dateInfo);
        }

        int remainingColumns = 42 - dateInfoList.size();

        for (int position = 1; position <= remainingColumns; position++) {

            DateItem dateItem = DateItem.create(position);
            DateInfo dateInfo = new DateInfo();
            dateInfo.setDateItem(dateItem);
            dateInfo.setIsExtra(true);
            dateInfoList.add(dateInfo);

            allDatesInMonth.add(DateItem.create(position));
        }


        // Creates and sets the Adapter
        invokeAdapter();
    }

    private Date getFilteredDate(String filteredDateString) {


        java.text.DateFormat formatter;

        formatter = new SimpleDateFormat(ONLY_DATE_NEW);
        Date date = null;
        try {
            date = formatter.parse(filteredDateString);

        } catch (ParseException e) {

            Utils.printStackTrace(e);
        }

        return date;
    }

    private String getEndDate(String str_date, int count, ArrayList<Integer> arrayList) {

        List<Date> dates = new ArrayList<>();


        java.text.DateFormat formatter;

        formatter = new SimpleDateFormat(ONLY_DATE);
        Date startDate = null;
        try {
            startDate = formatter.parse(str_date);

        } catch (ParseException e) {

            Utils.printStackTrace(e);
        }
        long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
        long curTime = startDate.getTime();
        while (count != 0) {
            Date date = new Date(curTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            for (int i = 0; i < arrayList.size(); i++) {
                if (cal.get(Calendar.DAY_OF_WEEK) - 1 == arrayList.get(i)) {
                    dates.add(date);
                    count--;
                }
            }

            curTime += interval;


        }

        return formatter.format(dates.get(dates.size() - 1));
    }

    /**
     * Method to create an Adapter from the Date Info List
     */
    private void invokeAdapter() {
        calendarAdapter = new CalendarAdapter(RecurringTaskDetailsActivity.this, dateInfoList);
        gvCalendar.setAdapter(calendarAdapter);
    }


    @Override
    public boolean isDateSelected(final DateItem dateTasks) {
        return false;
    }

    @Override
    public void dateChecked(final DateInfo dateInfo, final int pos) {
        if (dateInfo.isSelected()) {
            dateInfoList.get(pos).setSelected(false);
        } else {

            dateInfoList.get(pos).setSelected(true);

        }

        for (int j = 0; j < filteredDatesList.size(); j++) {
            Date date = new GregorianCalendar(dateInfo.getDateItem().getYear(), dateInfo.getDateItem().getMonth(), dateInfo.getDateItem().getDay()).getTime();

            if (date.compareTo(getFilteredDate(filteredDatesList.get(j).getDate())) == 0) {

                int isSelected;
                if (dateInfo.isSelected()) {
                    isSelected = 0;
                } else {
                    isSelected = 1;
                }
                filteredDatesList.get(j).setIsSkipped(isSelected);
            }


        }
        calendarAdapter.notifyDataSetChanged();


    }

    /**
     * Method to renderUI the Current Month on the Calendar
     */
    private void renderCurrentMonth() {

        int currentMonth = startMonth;
        int currentYear = startYear;

        /* Check whether the User is viewing current month
           if yes, then return, renderUI otherwise */
        if (month == currentMonth && year == currentYear)
            return;

        month = currentMonth;
        year = currentYear;

        // Render the month and then fetch tasks count
        renderAndFetch();
    }
}
