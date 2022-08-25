package com.tookancustomer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.widget.NestedScrollView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hippo.ChatByUniqueIdAttributes;
import com.hippo.HippoConfig;
import com.tookancustomer.adapters.BillBreakdownAdapter;
import com.tookancustomer.adapters.OrderItemRecyclerAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.cancellationPolicy.ViewCancellationPolicyActivity;
import com.tookancustomer.cancellationPolicy.model.GetCancellationData;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.fcm.FCMMessagingService;
import com.tookancustomer.fragments.CancelReasonBottomSheetFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.CancellationReasonModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.PromosModel;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.billbreakdown.BillBreakdownData;
import com.tookancustomer.models.taskdetails.AgentInfo;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.models.taskdetails.TaskDetails;
import com.tookancustomer.models.taskdetails.TrackingDetails;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.rating.activity.RatingsActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.CustomTypefaceSpan;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.tookancustomer.appdata.Constants.DateFormat.BACKEND_PICKUP_TIME;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.INAPP_WALLET;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAYTM;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_LATER;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.PAY_ON_DELIVERY;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.UNACCOUNTED;

public class TaskDetailsNewActivity extends BaseActivity implements View.OnClickListener {

    BottomSheetBehavior sheetBehavior;
    LinearLayout bottom_sheet;
    ImageView agentChatIV, callIV;
    LinearLayout driverDetailRL;
    private View viewPickupAndDrop;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private LinearLayout rlUnavailNet, llPickupDetail;
    private TextView tvNoNetConnect, tvRetry;
    private RelativeLayout rlOrderStatus, rlSummaryHeader, rlOrderTime, rlOrderEndTime, rlPaymentStatus,
            rlPaymentMode, rlRefundAmount, rlRemainingAmount;
    private TextView tvTaskStatus, tvOrderTime, tvPickupAddressLabel, tvStoreNameLabel, tvStoreName,
            tvPickupAddress, tvOrderStatusLabel, tvDateTimeLabel, tvEndDateTimeLabel, tvEndOrderTime,
            tvPaymentStatusLabel, tvPaymentStatus, tvPaymentModeLabel, tvPaymentMode, tvRepay, tvPointsEarnedHeading;
    private RecyclerView rvOrderProducts, rvTaxesList;
    private BillBreakdownAdapter taxAdapter;
    private RelativeLayout rlTotalAmount, rlTotalPayableAmount, rlPickupEmail, rlPickupName, rlPickupAddress, rlPhoneNumber, rlPickupHeader;
    private TextView tvItemsQuantityVal, tvTotal, tvTotalPayable, tvPointsEarnedAmount, tvPointsEarnedExpiry,
            tvCancellationPolicy, tvRemainingAmountText, tvRemainingAmount, tvRefundAmountText, tvRefundAmount;
    private Button btnSubmitRate;
    private TextView btnCancelOrder;
    private LinearLayout cvPointsEarned, cvAdditionalPriceParent, cvRemainingAmount, cvRateReviews;
    private LinearLayout llOrderDetails, llBillBreakdown;
    private LinearLayout llShowOrderRatings;
    private TextView tvOrderComments;
    private NestedScrollView nsvScrollBar;
    private OrderItemRecyclerAdapter orderItemsAdapter;
    private TaskData currentTask;
    private int jobId;
    private String trackingLink = "";
    private int fromScreen = -1;
    private String pushID = "";
    private CheckBox checkboxReturn;
    private ArrayList<CancellationReasonModel> cancelReasonArrayList = new ArrayList<>();
    private View vDescription;
    private TextView etDescription;

    private LinearLayout cvHelp;
    private TextView tvNeedHelp;
    private Button btnChatWithUs/*, btnTrack*/;
    private boolean isBottomSheetDragable = false;


    private ImageView summeryDropDownIV, detailDropDownIV, helpDropDownIV, ratingDropDownIV, ivPickupDropdown;
    private LinearLayout orderSummeryViewLL, /*helpViewLL, */
            llRatingHeader, pickDropLL;
    private RelativeLayout orderDetailHeaderRL, /*helpHeaderRL,*/
            ratingHeadRL;
    private WebView track_view_wv;

    private ImageView driverImageIV;
    private TextView agentNameTV, riderTaskStatusTV, ratingHederTV;
    private LinearLayout llShowCustomerRatings;

    private Datum storefrontData;


    private RelativeLayout rlAdditionalAmount, parentRL;
    private TextView tvAdditionalHeading, tvAdditionalHeadingCustomNoPrice, tvAdditionalAmount, tvTipText;
    private ArrayList<Template> templateDataList;
    private AgentInfo agentInf;
    private TextView tvNotesHeader, tvPickupNameLabel, tvPickupNameValue, tvPickupEmailLabel,
            tvPickupEmailValue, tvPickupAndDropAddressLabel, tvPickupAndDropAddressValue, tvPhoneNumberLabel, tvPhoneNumberValue, tvPickupHeader;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            final String jobID = intent.getStringExtra("jobId");
            Integer jobStatus = intent.getIntExtra("jobStatus", -1);

            if (jobID.equals(jobId + "")) {
                jobId = Integer.parseInt(jobID);
                getTaskDetails(true);
            } else {
                new OptionsDialog.Builder(mActivity).message(Utils.getCallTaskAs(true, true) + " #" + jobID + "\n" + message).positiveButton(getStrings(R.string.view_text)).negativeButton(getStrings(R.string.cancel_text)).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        jobId = Integer.parseInt(jobID);
                        getTaskDetails(true);
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                    }
                }).build().show();
            }
        }
    };

    private void initViews() {


        viewPickupAndDrop = findViewById(R.id.viewPickupAndDrop);
        ivPickupDropdown = findViewById(R.id.ivPickupDropdown);
        rlPickupHeader = findViewById(R.id.rlPickupHeader);
        rlPhoneNumber = findViewById(R.id.rlPhoneNumber);
        rlPickupAddress = findViewById(R.id.rlPickupAddress);
        rlPickupName = findViewById(R.id.rlPickupName);
        rlPickupEmail = findViewById(R.id.rlPickupEmail);
        llPickupDetail = findViewById(R.id.llPickupDetail);

        tvPickupHeader = findViewById(R.id.tvPickupHeader);
        tvPhoneNumberValue = findViewById(R.id.tvPhoneNumberValue);
        tvPhoneNumberLabel = findViewById(R.id.tvPhoneNumberLabel);
        tvPickupAndDropAddressValue = findViewById(R.id.tvPickupAndDropAddressValue);
        tvPickupAndDropAddressLabel = findViewById(R.id.tvPickupAndDropAddressLabel);
        tvPickupAndDropAddressLabel = findViewById(R.id.tvPickupAndDropAddressLabel);

        tvPickupEmailValue = findViewById(R.id.tvPickupEmailValue);
        tvPickupEmailLabel = findViewById(R.id.tvPickupEmailLabel);
        tvPickupNameValue = findViewById(R.id.tvPickupNameValue);
        tvPickupNameLabel = findViewById(R.id.tvPickupNameLabel);
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);

        orderSummeryViewLL = findViewById(R.id.orderSummeryViewLL);
        summeryDropDownIV = findViewById(R.id.summeryDropDownIV);
        callIV = findViewById(R.id.callIV);
        agentChatIV = findViewById(R.id.agentChatIV);

        driverImageIV = findViewById(R.id.driverImageIV);
        agentNameTV = findViewById(R.id.agentNameTV);
        riderTaskStatusTV = findViewById(R.id.riderTaskStatusTV);
        llShowCustomerRatings = findViewById(R.id.llShowCustomerRatings);

        orderDetailHeaderRL = findViewById(R.id.orderDetailHeaderRL);
        driverDetailRL = findViewById(R.id.driverDetailRL);
        ratingHeadRL = findViewById(R.id.ratingHeadRL);
        detailDropDownIV = findViewById(R.id.detailDropDownIV);
        helpDropDownIV = findViewById(R.id.helpDropDownIV);
        llOrderDetails = findViewById(R.id.llOrderDetails);
        track_view_wv = findViewById(R.id.track_view_wv);
        ratingDropDownIV = findViewById(R.id.ratingDropDownIV);
        llRatingHeader = findViewById(R.id.llRatingHeader);
        pickDropLL = findViewById(R.id.pickDropLL);

        ((TextView) findViewById(R.id.detailHeaderTV)).setText(getStrings(R.string.order_detail).replace(TerminologyStrings.ORDER, StorefrontCommonData.getTerminology().getOrder()));
        ((TextView) findViewById(R.id.helpHeaderTV)).setText(getStrings(R.string.need_help));
        ((TextView) findViewById(R.id.ratingHederTV)).setText(getStrings(R.string.reviews_ratings));

        track_view_wv.setWebViewClient(new MyWebViewClient());
        track_view_wv.setWebChromeClient(new WebChromeClient());
        track_view_wv.getSettings().setLoadsImagesAutomatically(true);
        track_view_wv.getSettings().setJavaScriptEnabled(true);
        track_view_wv.getSettings().setDomStorageEnabled(true);
        track_view_wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        llOrderDetails.setVisibility(View.GONE);
//        helpViewLL.setVisibility(View.GONE);
        summeryDropDownIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_dropdown_closed));
        summeryDropDownIV.animate().rotation(180).start();
        detailDropDownIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_dropdown_closed));
        helpDropDownIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_dropdown_closed));
        ivPickupDropdown.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_dropdown_closed));

        findViewById(R.id.rlInvisible).setVisibility(View.VISIBLE);

        rlUnavailNet = findViewById(R.id.rlUnavailNet);
        tvNoNetConnect = findViewById(R.id.tvNoNetConnect);
        tvNoNetConnect.setText(getStrings(R.string.no_internet_connection));
        tvRetry = findViewById(R.id.tvRetry);
        tvRetry.setText(getStrings(R.string.retry_underlined));

        //Remaining Amount in laundry
        cvAdditionalPriceParent = findViewById(R.id.cvAdditionalPriceParent);
        cvRemainingAmount = findViewById(R.id.cvRemainingAmount);
        tvRemainingAmountText = findViewById(R.id.tvRemainingAmountText);
        tvRemainingAmount = findViewById(R.id.tvRemainingAmount);


        rlPaymentMode = findViewById(R.id.rlPaymentMode);
        tvPaymentModeLabel = findViewById(R.id.tvPaymentModeLabel);
        tvPaymentModeLabel.setText(getStrings(R.string.payment_mode) + ":");
        tvPaymentMode = findViewById(R.id.tvPaymentMode);
        tvRepay = findViewById(R.id.tvRepay);
        tvRepay.setText(StorefrontCommonData.getTerminology().getPay(true));

        rlPaymentStatus = findViewById(R.id.rlPaymentStatus);
        tvPaymentStatusLabel = findViewById(R.id.tvPaymentStatusLabel);
        tvPaymentStatusLabel.setText(getStrings(R.string.payment_status) + ":");
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus);
        rlOrderStatus = findViewById(R.id.rlOrderStatus);
        tvTaskStatus = findViewById(R.id.tvTaskStatus);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvPickupAddressLabel = findViewById(R.id.tvPickupAddressLabel);
        tvStoreNameLabel = findViewById(R.id.tvStoreNameLabel);
        tvStoreName = findViewById(R.id.tvStoreName);
        vDescription = findViewById(R.id.vDescription);
        etDescription = findViewById(R.id.etDescription);
        tvNotesHeader = findViewById(R.id.tvNotesHeader);

        tvPickupAddress = findViewById(R.id.tvPickupAddress);

        rvOrderProducts = findViewById(R.id.rvOrderProducts);
        rvOrderProducts.setLayoutManager(new LinearLayoutManager(mActivity));
        rvOrderProducts.setNestedScrollingEnabled(false);
        rvOrderProducts.setFocusable(false);


        rvTaxesList = findViewById(R.id.rvTaxesList);
        rvTaxesList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvTaxesList.setNestedScrollingEnabled(false);

        tvItemsQuantityVal = findViewById(R.id.tvItemsQuantityVal);


        rlTotalAmount = findViewById(R.id.rlTotalAmount);
        rlTotalPayableAmount = findViewById(R.id.rlTotalPayableAmount);
        cvPointsEarned = findViewById(R.id.cvPointsEarned);
        tvPointsEarnedHeading = findViewById(R.id.tvPointsEarnedHeading);
        tvPointsEarnedHeading.setText(getStrings(R.string.loyalty_points_earned).replace(LOYALTY_POINTS, StorefrontCommonData.getTerminology().getLoyaltyPoints()));
        tvPointsEarnedAmount = findViewById(R.id.tvPointsEarnedAmount);
        tvPointsEarnedExpiry = findViewById(R.id.tvPointsEarnedExpiry);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotalPayable = findViewById(R.id.tvTotalPayable);
        tvCancellationPolicy = findViewById(R.id.tvCancellationPolicy);

        cvRateReviews = findViewById(R.id.cvRateReviews);
        llShowOrderRatings = findViewById(R.id.llShowOrderRatings);
        tvOrderComments = findViewById(R.id.tvOrderComments);

        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnCancelOrder.setText(getStrings(R.string.cancelOrder).replace(ORDER, Utils.getCallTaskAs(true, true)));
        nsvScrollBar = findViewById(R.id.nsvScrollBar);


        rlOrderTime = findViewById(R.id.rlOrderTime);
        rlOrderEndTime = findViewById(R.id.rlOrderEndTime);
        tvEndOrderTime = findViewById(R.id.tvEndOrderTime);
        tvEndDateTimeLabel = findViewById(R.id.tvEndDateTimeLabel);
        tvDateTimeLabel = findViewById(R.id.tvDateTimeLabel);
        tvOrderStatusLabel = findViewById(R.id.tvOrderStatusLabel);
        llBillBreakdown = findViewById(R.id.llBillBreakdown);
        rlSummaryHeader = findViewById(R.id.rlSummaryHeader);
        tvOrderStatusLabel.setText(getStrings(R.string.order_status_text));

        checkboxReturn = findViewById(R.id.checkboxReturn);


        ((TextView) findViewById(R.id.tvItemSummary)).setText(getStrings(R.string.order_summary).replace(TerminologyStrings.ORDER, StorefrontCommonData.getTerminology().getOrder()));
        ((TextView) findViewById(R.id.tvTotalHeading)).setText(getStrings(R.string.total));
        ((TextView) findViewById(R.id.tvTotalPayableHeading)).setText(getStrings(R.string.net_paid_amount));

        ((TextView) findViewById(R.id.tvRatingsReviews)).setText(getStrings(R.string.reviews_ratings));
        ((Button) findViewById(R.id.btnSubmitRate)).setText(getStrings(R.string.review));

        rlAdditionalAmount = findViewById(R.id.rlAdditionalAmount);
        tvAdditionalHeading = findViewById(R.id.tvAdditionalHeading);
        tvAdditionalHeadingCustomNoPrice = findViewById(R.id.tvAdditionalHeadingCustomNoPrice);
        tvAdditionalAmount = findViewById(R.id.tvAdditionalAmount);

        rlRefundAmount = findViewById(R.id.rlRefundAmount);
        rlRemainingAmount = findViewById(R.id.rlRemainingAmount);
        tvRefundAmountText = findViewById(R.id.tvRefundAmountText);
        tvRefundAmount = findViewById(R.id.tvRefundAmount);

        btnSubmitRate = findViewById(R.id.btnSubmitRate);

        cvHelp = findViewById(R.id.cvHelp);
        tvNeedHelp = findViewById(R.id.tvNeedHelp);
        tvNeedHelp.setText(getStrings(R.string.need_help));
        btnChatWithUs = findViewById(R.id.btnChatWithUs);
        btnChatWithUs.setText(getStrings(R.string.chat_with_us));
        btnChatWithUs.setVisibility(UIManager.isFuguChatEnabled() ? View.VISIBLE : View.GONE);
//        btnTrack = findViewById(R.id.btnTrack);
//        btnTrack.setText(getStrings(R.string.track_order).replace(ORDER, StorefrontCommonData.getTerminology().getOrder()));
        isBottomSheetDragable = false;
        sheetBehavior.setPeekHeight(parentRL.getHeight());
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        setHelpSectionVisibility();

        Utils.setOnClickListener(this, rlBack, tvRetry, btnCancelOrder, tvAdditionalHeading, tvAdditionalHeadingCustomNoPrice,
                tvCancellationPolicy, btnSubmitRate, tvRepay, btnChatWithUs, /*btnTrack,*/ rlSummaryHeader, /*helpHeaderRL,*/ orderDetailHeaderRL
                , ratingHeadRL, agentChatIV, callIV, rlPickupHeader);
    }


    private void setPickupDetailData() {
        if (!TextUtils.isEmpty(currentTask.getCustomPickupAddress())) {
            rlPickupHeader.setVisibility(View.VISIBLE);
            viewPickupAndDrop.setVisibility(View.VISIBLE);
            pickDropLL.setVisibility(View.VISIBLE);
        } else {
            rlPickupHeader.setVisibility(View.GONE);
            viewPickupAndDrop.setVisibility(View.GONE);
            pickDropLL.setVisibility(View.GONE);
        }

        tvPickupHeader.setText(StorefrontCommonData.getTerminology().getPickupAndDrop());
        if (!TextUtils.isEmpty(currentTask.getCustomPickupName())) {
            rlPickupName.setVisibility(View.VISIBLE);
            tvPickupNameLabel.setText(getStrings(R.string.name) + getStrings(R.string.colon));
            tvPickupNameValue.setText(currentTask.getCustomPickupName());
        } else rlPickupName.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(currentTask.getCustomPickupEmail())) {
            rlPickupEmail.setVisibility(View.VISIBLE);
            tvPickupEmailLabel.setText(getStrings(R.string.email) + getStrings(R.string.colon));
            tvPickupEmailValue.setText(currentTask.getCustomPickupEmail());
        } else rlPickupEmail.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(currentTask.getCustomPickupAddress())) {
            rlPickupAddress.setVisibility(View.VISIBLE);
            tvPickupAndDropAddressLabel.setText(getStrings(R.string.pickup_address).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup()) + getStrings(R.string.colon));
            tvPickupAndDropAddressValue.setText(currentTask.getCustomPickupAddress());
        } else rlPickupAddress.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(currentTask.getCustomPickupPhone())) {
            rlPhoneNumber.setVisibility(View.VISIBLE);
            tvPhoneNumberLabel.setText(getStrings(R.string.phone) + getStrings(R.string.colon));
            tvPhoneNumberValue.setText(currentTask.getCustomPickupPhone());
        } else rlPhoneNumber.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RatingsActivity.REQUEST_CODE_FOR_RATING:
                if (resultCode == RESULT_OK) {
                    currentTask = (TaskData) data.getSerializableExtra(RatingsActivity.EXTRA_TASK_DETAILS);
                    setData();
                }
                break;
            case Codes.Request.OPEN_REPAYMENT_ACTIVITY:
                getTaskDetails(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_new2);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        parentRL = findViewById(R.id.parentRL);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setPeekHeight(parentRL.getHeight());

        mActivity = this;
        Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && StorefrontCommonData.getUserData() != null) {
            jobId = bundle.getInt(JOB_ID);
            trackingLink = bundle.getString("TRACKING_LINK", "");
            fromScreen = bundle.getInt("from", -1);
            pushID = bundle.getString("pushID", "");
            if (StorefrontCommonData.getUserData().getData() == null) {
                fromScreen = -1;
                StorefrontCommonData.setUserData((UserData) getIntent().getExtras().getSerializable(UserData.class.getName()));
                Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
            }
            initViews();
            readNotifications();
            getTaskDetails(true);
        }

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        if (!isBottomSheetDragable) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                            driverDetailRL.setVisibility(View.GONE);

                        }

                        if (isBottomSheetDragable) {
                            if (agentInf != null) {
                                driverDetailRL.setVisibility(View.VISIBLE);
                            } else {
                                driverDetailRL.setVisibility(View.GONE);
                            }
                        }

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        if (!isBottomSheetDragable) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                            driverDetailRL.setVisibility(View.GONE);
                        }

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        if (!isBottomSheetDragable) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                            driverDetailRL.setVisibility(View.GONE);

                        }
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bottomSheet.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = MotionEventCompat.getActionMasked(event);
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                return isBottomSheetDragable;
                            default:
                                return true;
                        }
                    }
                });
            }
        });

    }


    private void readNotifications() {
        if (pushID.isEmpty()) {
            return;
        }
        try {
            if (FCMMessagingService.mNotificationManager != null) {
                FCMMessagingService.mNotificationManager.cancel(Integer.parseInt(pushID));
                FCMMessagingService.clearNotification();
            }
        } catch (Exception e) {
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("push_id", pushID);

        RestClient.getApiInterface(this).updateAppNotifications(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
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

        if (cost > 0) {
            tvAdditionalAmount.setText(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + "" + Utils.getDoubleTwoDigits(cost));
        } else {
            tvAdditionalAmount.setText("");
        }

        tvAdditionalHeading.setText(CustomViewsUtil.createSpan(mActivity, cost > 0 ? getStrings(R.string.additional_amount) :
                        getStrings(R.string.additional_info),
                " (", getStrings(R.string.view), ")", ContextCompat.getColor(mActivity, R.color.colorPrimary)));

        tvAdditionalHeadingCustomNoPrice.setText(CustomViewsUtil.createSpan(mActivity, cost > 0 ? getStrings(R.string.additional_amount) :
                        getStrings(R.string.additional_info),
                " (", getStrings(R.string.view), ")", ContextCompat.getColor(mActivity, R.color.colorPrimary)));

    }

    public void setData() {
        if (!trackingLink.isEmpty())
            currentTask.setTrackingLink(trackingLink);
        //TODO

        if (!(currentTask.getPaymentType() == UNACCOUNTED.intValue)) {
            rlPaymentMode.setVisibility(View.VISIBLE);

            tvPaymentMode.setText(currentTask.getPaymentName());
//            for (int i = 0; i < StorefrontCommonData.getFormSettings().getPaymentMethods().size(); i++) {
//                if (StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getValue() == currentTask.getPaymentType()) {
//                    tvPaymentMode.setText(StorefrontCommonData.getFormSettings().getPaymentMethods().get(i).getName());
//                    break;
//                }
//            }


        } else {
            rlPaymentMode.setVisibility(View.GONE);
        }
        tvRepay.setVisibility(View.GONE);

        if (currentTask.getPaymentType() == CASH.intValue
                || currentTask.getPaymentType() == PAY_LATER.intValue
                /*|| currentTask.getPaymentType() == Constants.PaymentValue.STRIPE.intValue*/
                || currentTask.getPaymentType() == PAYTM.intValue
                /*|| currentTask.getPaymentType() == Constants.PaymentValue.PAYFORT.intValue*/
                || currentTask.getPaymentType() == INAPP_WALLET.intValue
                || currentTask.getPaymentType() == UNACCOUNTED.intValue
                || currentTask.getPaymentType() == PAY_ON_DELIVERY.intValue
        ) {
            /*In case of cash, stripe and paytm, payment deducted while creating order so always paid no need to show payment status*/
            rlPaymentStatus.setVisibility(View.GONE);

//            if (currentTask.getPaymentType() == CASH.intValue || currentTask.getPaymentType() == PAY_LATER.intValue || currentTask.getPaymentType() == UNACCOUNTED.intValue) {
////                setRepayButtonVisible();
////            }
        } else {
            rlPaymentStatus.setVisibility(View.VISIBLE);

            if (currentTask.getTransactionId().isEmpty() || currentTask.getTransactionId().equals("0")) {
                tvPaymentStatus.setText(getStrings(R.string.unpaid));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_failed));

                setRepayButtonVisible();
            } else if (currentTask.getOverallTransactionStatus() == Constants.TransactionStatus.ORDER_COMPlETE
                    || currentTask.getOverallTransactionStatus() == Constants.TransactionStatus.TRANSACTION_COMPLETE) {
                tvPaymentStatus.setText(getStrings(R.string.paid));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_successful));
            } else if (currentTask.getOverallTransactionStatus() == Constants.TransactionStatus.TRANSACTION_REFUNDED) {
                tvPaymentStatus.setText(getStrings(R.string.refund));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_refund));
            } else if (currentTask.getOverallTransactionStatus() == Constants.TransactionStatus.TRANSACTION_PARTIAL_REFUND) {
                tvPaymentStatus.setText(getStrings(R.string.partially_refunded));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_refund));
            } else if (currentTask.getOverallTransactionStatus() == Constants.TransactionStatus.TRANSACTION_PARTIAL) {
                tvPaymentStatus.setText(getStrings(R.string.partial_text));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_partial_laundry));
            } else if (currentTask.getOverallTransactionStatus() == Constants.TransactionStatus.TRANSACTION_ON_HOLD) {
                tvPaymentStatus.setText(getStrings(R.string.onhold_text));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_onhold_laundry));
            } else {
                tvPaymentStatus.setText(getStrings(R.string.unpaid));
                tvPaymentStatus.setTextColor(getResources().getColor(R.color.status_failed));
                setRepayButtonVisible();
            }


        }

        if (currentTask.getTaskType() == Constants.LaundryTaskType.DELIVERY_TASK) {
            tvStoreNameLabel.setText(StorefrontCommonData.getTerminology().getDeliveryFrom(true) + ":");
            tvPickupAddressLabel.setText(StorefrontCommonData.getTerminology().getDeliveryTo(true) + ":");
        } else if (currentTask.getTaskType() == Constants.LaundryTaskType.PICKUP_TASK) {
            tvStoreNameLabel.setText(StorefrontCommonData.getTerminology().getDeliveryTo(true) + ":");
            tvPickupAddressLabel.setText(getStrings(R.string.pickup_address).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)) + getStrings(R.string.colon));
        } else {
            if (currentTask.getDeliveryMethod() == Constants.DeliveryMode.SELF_PICKUP) {
                tvStoreNameLabel.setText(StorefrontCommonData.getTerminology().getPickup(true) + " " + getStrings(R.string.from) + ":");
                tvPickupAddressLabel.setText(getStrings(R.string.pickup_address).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup(true)) + getStrings(R.string.colon));
            } else {
                tvStoreNameLabel.setText(StorefrontCommonData.getTerminology().getDeliveryFrom(true) + ":");
                tvPickupAddressLabel.setText(StorefrontCommonData.getTerminology().getDeliveryTo(true) + ":");
            }
        }

        /**
         * Custom order == 2 refers to custom order with quotation i.e. pay via hippo in case of custom order
         * Custom order == 1 refers to custom order without quotation
         */

        if (currentTask.getIsCustomOrder() == 1 || currentTask.getIsCustomOrder() == 2) {
            vDescription.setVisibility(View.VISIBLE);
            etDescription.setHint(getStrings(R.string.no_description_available));
            etDescription.setText(currentTask.getJobDescription());
            etDescription.setMaxLines(150);
            tvNotesHeader.setText(getStrings(R.string.description_text));
            tvStoreNameLabel.setText(StorefrontCommonData.getTerminology().getPickup(true) + " " + getStrings(R.string.from) + ":");
            if (currentTask.getIsPickupAnywhere() == 1) {
                tvStoreName.setText(StorefrontCommonData.getTerminology().getAnywhere());
            } else {
                tvStoreName.setText(currentTask.getJobPickupAddress());
            }

            tvPickupAddressLabel.setText(StorefrontCommonData.getTerminology().getDeliveryTo(true) + ":");
            tvPickupAddress.setText((Utils.assign(currentTask.getJobAddress())));

        } else {
            vDescription.setVisibility(View.GONE);
            if (currentTask.getTaskType() == Constants.TaskTypeConstants.PICKUP) {
                tvPickupAddress.setText((Utils.assign(currentTask.getJobPickupAddress())));
            } else if (currentTask.getTaskType() == Constants.TaskTypeConstants.DELIVERY) {
                tvPickupAddress.setText((Utils.assign(currentTask.getJobAddress())));
            } else {
                tvPickupAddress.setText((Utils.assign(currentTask.getJobPickupAddress())));
            }
            tvStoreName.setText(currentTask.getMerchantName());
        }
//        llOrderDetails.setVisibility(View.VISIBLE);
        tvHeading.setText(Utils.getCallTaskAs(true, true) + " #" + currentTask.getJobId());


//        tvStoreNameLabel.setText(getStrings(currentTask.isTaskCompleted() ? R.string.delivered_from : R.string.delivery_from));

        setReviewsRatings();


        if (currentTask.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                && currentTask.getPd_or_appointment() == Constants.ServiceFlow.APPOINTMENT) {
            btnCancelOrder.setVisibility(View.GONE);
            tvCancellationPolicy.setVisibility(View.GONE);
            rlOrderStatus.setVisibility(View.GONE);
            setBottomSheetExpend();
        } else {
            btnCancelOrder.setVisibility(currentTask.getCancelAllowed() == 1 ? View.VISIBLE : View.GONE);
            tvCancellationPolicy.setVisibility(currentTask.getCancelAllowed() == 1 ? View.VISIBLE : View.GONE);
            rlOrderStatus.setVisibility(currentTask.getShowStatus() == 1 ? View.VISIBLE : View.GONE);
            tvTaskStatus.setText(Constants.TaskStatus.getTaskStatusByValue(currentTask.getJobStatus()).getPassive(mActivity));
            tvTaskStatus.setTextColor(getResources().getColor(Constants.TaskStatus.getColorRes(currentTask.getJobStatus())));

            if (currentTask.getDeliveryMethod() == Constants.DeliveryMode.HOME_DELIVERY ||
                    currentTask.getDeliveryMethod() == Constants.DeliveryMode.PICK_AND_DROP) {
                if (currentTask.getTrackingLink().isEmpty()) {
//                    btnTrack.setVisibility(View.GONE);
                    setBottomSheetExpend();
                } else {
//                    btnTrack.setVisibility(View.VISIBLE);


                }
            } else if (currentTask.getDeliveryMethod() == Constants.DeliveryMode.SELF_PICKUP) {
                if (currentTask.getJobStatus() == Constants.TaskStatus.CANCELLED.value
                        || currentTask.getJobStatus() == Constants.TaskStatus.DECLINED.value
                        || currentTask.getJobStatus() == Constants.TaskStatus.DELIVERED.value
                        || currentTask.getJobStatus() == Constants.TaskStatus.PENDING.value
                        || currentTask.getJobStatus() == Constants.TaskStatus.PENDING_STATUS.value
                        || currentTask.getJobStatus() == Constants.TaskStatus.FAILED.value
                        || currentTask.getJobStatus() == Constants.TaskStatus.SUCCESSFUL.value) {
//                    btnTrack.setVisibility(View.GONE);
                    setBottomSheetExpend();

                } else {
//                    btnTrack.setVisibility(View.VISIBLE);


                }
            }


        }
        setHelpSectionVisibility();

        String url = null;
        if ((currentTask.getDeliveryMethod() == Constants.DeliveryMode.HOME_DELIVERY
                || currentTask.getDeliveryMethod() == Constants.DeliveryMode.PICK_AND_DROP) &&
                currentTask.getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE &&
                !currentTask.getTrackingLink().isEmpty()) {
            url = currentTask.getTrackingLink();
            setMapUrl(url, currentTask.getAgentInfo());
        } else if ((currentTask.getDeliveryMethod() == Constants.DeliveryMode.HOME_DELIVERY
                || currentTask.getDeliveryMethod() == Constants.DeliveryMode.PICK_AND_DROP) &&
                currentTask.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                currentTask.getPd_or_appointment() != Constants.ServiceFlow.SERVICE_AS_PRODUCT) {

            boolean istrackingLink = false;
            for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {

                if (currentTask.getOrderDetails().get(i).getProduct().getServices() != null
                        && !currentTask.getOrderDetails().get(i).getProduct().getServices().getTrackingLink().isEmpty()
                        && currentTask.getPd_or_appointment() != Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
                    currentTask.getOrderDetails().get(i).getProduct().getServices().setSelected(true);
                    istrackingLink = true;
                    getAgentData(i);
                    break;
                }
            }
            if (!istrackingLink) {
                for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {

                    if (currentTask.getOrderDetails().get(i).getProduct().getServices() != null
                            && currentTask.getOrderDetails().get(i).getProduct().getAgentId() > 0
                            && currentTask.getPd_or_appointment() != Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
                        currentTask.getOrderDetails().get(i).getProduct().getServices().setSelected(true);

                        getAgentData(i);
                        break;
                    }
                }
            }

        } else if (currentTask.getDeliveryMethod() == Constants.DeliveryMode.HOME_DELIVERY &&
                currentTask.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                !currentTask.getTrackingLink().isEmpty()) {
            url = currentTask.getTrackingLink();
            setMapUrl(url, currentTask.getAgentInfo());
        } else if (currentTask.getDeliveryMethod() == Constants.DeliveryMode.SELF_PICKUP) {
            setBottomSheetExpend();
        }


        if (currentTask.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
//                && currentTask.getPd_or_appointment() == Constants.ServiceFlow.APPOINTMENT
        ) {
//            Utils.setVisibility(View.GONE, rlOrderEndTime, rlOrderTime);
            Utils.setVisibility(View.VISIBLE, rlOrderTime);
            String pickupDate, deliveryDate, endTimeLabel;
            if (currentTask.getPd_or_appointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
                Utils.setVisibility(View.VISIBLE, rlOrderEndTime);
                endTimeLabel = getStrings(R.string.scheduled_time);
                deliveryDate = DateUtils.getInstance().parseDateAs(currentTask.getJobDeliveryDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                deliveryDate = currentTask.getJobDeliveryDatetime();
                tvEndOrderTime.setText(deliveryDate);
                tvEndDateTimeLabel.setText(endTimeLabel + ":");
            } else {
                Utils.setVisibility(View.GONE, rlOrderEndTime);
            }
            tvDateTimeLabel.setText(Utils.getCallTaskAs(true, true) + " " + getStrings(R.string.created_at) + ":");
            String date = null;
            date = DateUtils.getInstance().convertToLocal(currentTask.getCreatedAt(), Constants.DateFormat.STANDARD_UTC);

            tvOrderTime.setText(date);


        } else {
            Utils.setVisibility(View.VISIBLE, rlOrderTime);

            if (currentTask.getIsStartEndTimeEnable().equals(1)) {
                Utils.setVisibility(View.VISIBLE, rlOrderEndTime);
                tvDateTimeLabel.setText(StorefrontCommonData.getTerminology().getStartTime(true) + ":");
                tvEndDateTimeLabel.setText(StorefrontCommonData.getTerminology().getEndTime(true) + ":");

                String date, endDate = "";
                if (Constants.TaskType.getTaskByValue(currentTask.getJobType()) == Constants.TaskType.APPOINTMENT || Constants.TaskType.getTaskByValue(currentTask.getJobType()) == Constants.TaskType.MOBILE_WORKFORCE) {
                    try {
                        date = DateUtils.getInstance().parseDateAs(currentTask.getJobPickupDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat()) + " - "
                                + DateUtils.getInstance().parseDateAs(currentTask.getJobDeliveryDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                        date = currentTask.getJobPickupDatetime() + " - " + currentTask.getJobDeliveryDatetime();
                    } catch (Exception e) {
//
                        Utils.printStackTrace(e);
                        date = DateUtils.getInstance().parseDateAs(currentTask.getJobPickupDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                        date = currentTask.getJobPickupDatetime();
                    }
                } else if (currentTask.getJobType() == Constants.TaskType.PICK_UP.value) {
                    date = DateUtils.getInstance().parseDateAs(currentTask.getJobPickupDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                    date = currentTask.getJobPickupDatetime();
                    endDate = DateUtils.getInstance().parseDateAs(currentTask.getJobDeliveryDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                    endDate = currentTask.getJobDeliveryDatetime();
                } else {
                    date = DateUtils.getInstance().parseDateAs(currentTask.getJobDeliveryDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
                    endDate = DateUtils.getInstance().parseDateAs(currentTask.getJobPickupDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                    date = currentTask.getJobDeliveryDatetime();
//                    endDate = currentTask.getJobPickupDatetime();
                }
                tvOrderTime.setText(date);
                tvEndOrderTime.setText(endDate);


            } else {
                tvDateTimeLabel.setText(Utils.getCallTaskAs(true, true) + " " + getStrings(R.string.order_date) + ":");
                String date = null;
                date = DateUtils.getInstance().convertToLocal(currentTask.getCreatedAt());
                tvOrderTime.setText(date);

                if (currentTask.getIsCustomOrder() == 0) {
                    Utils.setVisibility(View.VISIBLE, rlOrderEndTime);
//                    tvEndDateTimeLabel.setText(getStrings(R.string.scheduled_time) + " :");
                    String pickupDate, deliveryDate, endTimeLabel;
                    if (currentTask.getTaskType() == Constants.TaskTypeConstants.FOOD) {
                        endTimeLabel = getStrings(R.string.scheduled_time);
                        deliveryDate = DateUtils.getInstance().parseDatetaskDetalAs(currentTask.getJobDeliveryDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                        deliveryDate = currentTask.getJobDeliveryDatetime();
                        tvEndOrderTime.setText(deliveryDate);
                    } else {

                        if (currentTask.getTaskType() == Constants.TaskTypeConstants.PICKUP) {
                            endTimeLabel = StorefrontCommonData.getTerminology().getPickup(false);
                        } else {
                            endTimeLabel = StorefrontCommonData.getTerminology().getDelivery(false);
                        }
                        endTimeLabel += getStrings(R.string.empty_space) + getStrings(R.string.time);

                        pickupDate = DateUtils.getInstance().parseDateAs(currentTask.getJobPickupDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
                        deliveryDate = DateUtils.getInstance().parseDatetaskDetalAs(currentTask.getJobDeliveryDatetime(), BACKEND_PICKUP_TIME, UIManager.getDateTimeFormat());
//                        pickupDate = currentTask.getJobPickupDatetime();
//                        deliveryDate = currentTask.getJobDeliveryDatetime();
                        tvEndOrderTime.setText(createSpanForBoldText(pickupDate, " - \n", deliveryDate));
                    }

                    tvEndDateTimeLabel.setText(endTimeLabel + ":");
                } else {
                    Utils.setVisibility(View.GONE, rlOrderEndTime);
                }
            }
        }

        int totalOrders = 0;
        if (currentTask.getOrderDetails() != null) {
            for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {
                totalOrders += currentTask.getOrderDetails().get(i).getProduct().getQuantity();
            }

            tvItemsQuantityVal.setText(Utils.assign(totalOrders + ""));
        }
        orderItemsAdapter = new OrderItemRecyclerAdapter(mActivity, currentTask, currentTask.getOrderDetails());
        rvOrderProducts.setAdapter(orderItemsAdapter);


        boolean isDeliveryPromo = false, isSubtotalPromo = false;
        String deliveryPromoDescription = "", subtotalPromoDescription = "";

        if (currentTask.getPromoList().size() > 0)
            for (PromosModel appliedPromo : currentTask.getPromoList()) {
                if (appliedPromo.getPromoOn() == Constants.PromotionOn.DELIVERY_CHARGE) {
                    isDeliveryPromo = true;
                    deliveryPromoDescription = getStrings(R.string.applied_promo) + ": " + appliedPromo.getPromoCode();
                } else if (appliedPromo.getPromoOn() == Constants.PromotionOn.SUBTOTAL) {
                    isSubtotalPromo = true;
                    subtotalPromoDescription = getStrings(R.string.applied_promo) + ": " + appliedPromo.getPromoCode();
                }
            }

        ArrayList<BillBreakdownData> billBreakdownlist = new ArrayList<>();
        // Dependencies.getSelectedProductsArrayList().get(0).setPaymentSettings(currentTask.getOrderCurrencySymbol());


        if (currentTask.getIsCustomOrder() == 2) {
            TaxesModel subtotalData;
            if (currentTask.getSurgeAmount().compareTo(BigDecimal.ZERO) > 0) {
            /*    subtotalData = new TaxesModel(getStrings(R.string.subtotal)
                        , BigDecimal.valueOf(Double.valueOf(currentTask.getOrderAmount()))
                        , StorefrontCommonData.getString(this, R.string.surge).replace(SURGE, StorefrontCommonData.getTerminology().getSurge()) + " " + UIManager.getCurrency(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + currentTask.getSurgeAmount())
                        , true, currentTask.getOrderCurrencySymbol()
                        , currentTask.getDeliveryChargeSurgeAmount());*/
                billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.subtotal)
                        , BigDecimal.valueOf(Double.valueOf(currentTask.getOrderAmount()))
                        , StorefrontCommonData.getString(this, R.string.surge).replace(SURGE, StorefrontCommonData.getTerminology().getSurge()) + " " + UIManager.getCurrency(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + currentTask.getSurgeAmount())
                        , true, currentTask.getOrderCurrencySymbol()
                        , currentTask.getDeliveryChargeSurgeAmount()));
            } else {
                /*subtotalData = new TaxesModel(getStrings(R.string.subtotal), BigDecimal.valueOf(Double.valueOf(currentTask.getOrderAmount())), null, false, currentTask.getOrderCurrencySymbol()
                        , currentTask.getDeliveryChargeSurgeAmount());*/
                billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.subtotal), BigDecimal.valueOf(Double.valueOf(currentTask.getOrderAmount())), null, false, currentTask.getOrderCurrencySymbol()
                        , currentTask.getDeliveryChargeSurgeAmount()));
            }


            // billBreakdownlist.add(subtotalData);

        } else {


            if (currentTask.getSurgeAmount().compareTo(BigDecimal.ZERO) > 0) {
                billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.subtotal)
                        , BigDecimal.valueOf(Double.valueOf(getOrderAmount(currentTask, false)))
                        , StorefrontCommonData.getString(this, R.string.surge).replace(SURGE, StorefrontCommonData.getTerminology().getSurge()) + " " + UIManager.getCurrency(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + currentTask.getSurgeAmount())
                        , true, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));

            } else {
                billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.subtotal), BigDecimal.valueOf(Double.valueOf(getOrderAmount(currentTask, false))),
                        null, false, currentTask.getOrderCurrencySymbol(),
                        currentTask.getDeliveryChargeSurgeAmount()));
            }

            if (Double.valueOf(currentTask.getCouponDiscount()) > 0) {
                if (isSubtotalPromo) {
                    billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.discount), BigDecimal.valueOf(Double.valueOf(currentTask.getCouponDiscount())), true, subtotalPromoDescription, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
                } else {
                    billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.discount), BigDecimal.valueOf(Double.valueOf(currentTask.getCouponDiscount())), true, null, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
                }
            }

            if (currentTask.getAdditionalCharges() != null && currentTask.getAdditionalCharges().getOnSubTotal() != null && currentTask.getAdditionalCharges().getOnSubTotal().size() > 0) {
                {
                    for (int i = 0; i < currentTask.getAdditionalCharges().getOnSubTotal().size(); i++) {
                        // billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
                        billBreakdownlist.add(new BillBreakdownData(currentTask.getAdditionalCharges().getOnSubTotal().get(i).getName(),
                                currentTask.getAdditionalCharges().getOnSubTotal().get(i).getAmount(),
                                false, currentTask.getAdditionalCharges().getOnSubTotal().get(i).getPercentage(),
                                currentTask.getAdditionalCharges().getOnSubTotal().get(i).getType()));

                    }
                }

                // newBillBreakdownArrayListSubTotal.addAll(billBreakDown.getData().getAdditionalCharges().getOnSubTotal());
            }

            if (currentTask.getTaxableAmount() != null && currentTask.getTaxableAmount().compareTo(BigDecimal.ZERO) > 0) {

                billBreakdownlist.add(new BillBreakdownData("Taxable Amount" + ":", currentTask.getTaxableAmount(), false, 0, 1));

            }

            if (isDeliveryPromo) {
                billBreakdownlist.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                        BigDecimal.valueOf(Double.valueOf(currentTask.getDeliveryCharge())), deliveryPromoDescription, false, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
            } else if (Double.valueOf(currentTask.getDeliveryCharge()) > 0) {
                billBreakdownlist.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getDeliveryCharge(),
                        BigDecimal.valueOf(Double.valueOf(currentTask.getDeliveryCharge())), null, false, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
            }
            if (currentTask.getDeliveryTaxes().size() > 0) {
                /*for (int i = 0; i < currentTask.getUserTaxes().size(); i++) {
                    billBreakDown.getData().getUserTaxesArray().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
                }*/
                for (int i = 0; i < currentTask.getDeliveryTaxes().size(); i++) {
                    if (currentTask.getDeliveryTaxes().get(i).getTaxAppliedOn() == 2) {
                        billBreakdownlist.add(new BillBreakdownData(currentTask.getDeliveryTaxes().get(i).getTaxName(),
                                new BigDecimal(currentTask.getDeliveryTaxes().get(i).getTaxAmount()),
                                false, currentTask.getDeliveryTaxes().get(i).getTaxPercentage(),
                                currentTask.getDeliveryTaxes().get(i).getTaxType()));
                    }
                }

            }

            if (currentTask.getUserTaxes().size() > 0) {
                /*for (int i = 0; i < currentTask.getUserTaxes().size(); i++) {
                    billBreakDown.getData().getUserTaxesArray().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
                }*/
                for (int i = 0; i < currentTask.getUserTaxes().size(); i++) {
                    if (currentTask.getUserTaxes().get(i).getTaxAppliedOn() != 2) {
                        billBreakdownlist.add(new BillBreakdownData(currentTask.getUserTaxes().get(i).getTaxName(),
                                new BigDecimal(currentTask.getUserTaxes().get(i).getTaxAmount()),
                                false, currentTask.getUserTaxes().get(i).getTaxPercentage(),
                                currentTask.getUserTaxes().get(i).getTaxType()));
                    }
                }

            }

            if (currentTask.getAdditionalCharges() != null && currentTask.getAdditionalCharges().getOnTotal() != null && currentTask.getAdditionalCharges().getOnTotal().size() > 0) {
                {
                    for (int i = 0; i < currentTask.getAdditionalCharges().getOnTotal().size(); i++) {
                        // billBreakDown.getData().getAdditionalCharges().getOnSubTotal().get(i).setCurrencySymbol(billBreakDown.getData().getPaymentSettings().getSymbol());
                        billBreakdownlist.add(new BillBreakdownData(currentTask.getAdditionalCharges().getOnTotal().get(i).getName(),
                                currentTask.getAdditionalCharges().getOnTotal().get(i).getAmount(),
                                false, currentTask.getAdditionalCharges().getOnTotal().get(i).getPercentage(),
                                currentTask.getAdditionalCharges().getOnTotal().get(i).getType()));

                    }
                }

                // newBillBreakdownArrayListSubTotal.addAll(billBreakDown.getData().getAdditionalCharges().getOnSubTotal());
            }

            if (Double.valueOf(currentTask.getTip()) > 0) {
                billBreakdownlist.add(new BillBreakdownData(StorefrontCommonData.getTerminology().getTip(), BigDecimal.valueOf(Double.valueOf(currentTask.getTip())), null, false, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
            }


            if (currentTask.getLoyaltyPoints() != null && currentTask.getLoyaltyPoints().getDiscountAmount() != null) {
                if (currentTask.getLoyaltyPoints().getPointsRedeemed() > 0) {
                    billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.loyalty_points_used_amount).replace(LOYALTY_POINTS, StorefrontCommonData.getTerminology().getLoyaltyPoints())
                            .replace(AMOUNT, currentTask.getLoyaltyPoints().getPointsRedeemed() + "")
                            , BigDecimal.valueOf(currentTask.getLoyaltyPoints().getDiscountAmount().doubleValue()), true, null, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
                }
            }

            if (currentTask.getAdditionalAmount() != null &&
                    currentTask.getAdditionalAmount().doubleValue() > 0) {
            /*TaxesModel additionalAmountData = new TaxesModel(getStrings(R.string.additional_amount),
                    billBreakDown.getData().getAdditionalAmount(),
                    billBreakDown.getData().getPaymentSettings() != null ?
                            billBreakDown.getData().getPaymentSettings() : null,
                    billBreakDown.getData().getDeliverySurgeAmount());
            billBreakdownArrayList.add(additionalAmountData);*/
                billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.additional_amount),
                        currentTask.getAdditionalAmount(),
                        currentTask.getOrderCurrencySymbol(),
                        currentTask.getDeliverySurgeAmount()));
            }


            // billBreakdownlist.add(subtotalData);
            if (currentTask.getTransaction_charges() > 0) {

                billBreakdownlist.add(new BillBreakdownData(StorefrontCommonData.getTerminology()
                        .getTransactionCharge()
                        , BigDecimal.valueOf(Double.valueOf(currentTask.getTransaction_charges()))
                        , currentTask.getDeliveryChargeSurgeAmount()));

            }


        }


        //   billBreakdownlist.addAll(currentTask.getUserTaxes());


        if (currentTask.getRefundedAmount() > 0) {
            billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.refunded_amount),
                    new BigDecimal(currentTask.getRefundedAmount()), true, null, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
        }


        if (Dependencies.isLaundryApp() && currentTask.getTaskType() == 2) {
            if (!Utils.getDoubleTwoDigits(currentTask.getRemainingBalance().doubleValue()).equalsIgnoreCase("0.00")) {
                if (currentTask.getRemainingBalance().doubleValue() > 0) {
                    billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.remaining_amount),
                            currentTask.getRemainingBalance(), null, false, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
                } else {
                    billBreakdownlist.add(new BillBreakdownData(getStrings(R.string.remaining_amount),
                            new BigDecimal(-1 * currentTask.getRemainingBalance().doubleValue()), true, null, currentTask.getOrderCurrencySymbol(), currentTask.getDeliveryChargeSurgeAmount()));
                }
            }

        }

     /*   if (currentTask.getAdditionalCharges() != null && currentTask.getAdditionalCharges().size() > 0) {
            {
                for (int i = 0; i < currentTask.getAdditionalCharges().size(); i++)
                    currentTask.getAdditionalCharges().get(i).setCurrencySymbol(currentTask.getOrderCurrencySymbol());
            }

            newBillBreakdownArrayList.addAll(currentTask.getAdditionalCharges());
        }*/


        taxAdapter = new BillBreakdownAdapter(mActivity, billBreakdownlist);
        rvTaxesList.setAdapter(taxAdapter);

        String orderAmount = getOrderAmount(currentTask, true);

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Double.valueOf(getOrderAmount(currentTask, true)) <= 0) {
            rlTotalAmount.setVisibility(View.GONE);
            rlTotalPayableAmount.setVisibility(View.GONE);
            cvPointsEarned.setVisibility(View.GONE);
        } else {
            rlTotalAmount.setVisibility(View.VISIBLE);
            //check if loyalty points are coming
            if (currentTask.getLoyaltyPoints() != null) {
                //check discount amount need to be deducted from total
                if (currentTask.getLoyaltyPoints().getDiscountAmount() != null) {
                    rlTotalPayableAmount.setVisibility(View.GONE);
                    tvTotalPayable.setText((UIManager.getCurrency(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + getPayableAmount(orderAmount, currentTask))));

//                    tvTotalPayable.setText(Utils.getCurrencySymbol() + getPayableAmount(orderAmount, currentTask));
                }
                //check loyalty is enable & user is getting loyalty points or not & points expiry date
                if (UIManager.getIsLoyaltyEnable() == 1 && currentTask.getLoyaltyPoints().getLoyaltyPointsEarned() != 0 && currentTask.getLoyaltyPoints().getExpiryDate() != null) {
                    cvPointsEarned.setVisibility(View.VISIBLE);
                    tvPointsEarnedAmount.setText(currentTask.getLoyaltyPoints().getLoyaltyPointsEarned() + "");
                    tvPointsEarnedExpiry.setText(getStrings(R.string.these_loyalty_points_will_expire_on)
                            .replace(LOYALTY_POINTS, StorefrontCommonData.getTerminology().getLoyaltyPoints())
                            .replace(TerminologyStrings.DATE, DateUtils.getInstance().convertToLocal(currentTask.getLoyaltyPoints().getExpiryDate(), Constants.DateFormat.STANDARD_DATE_FORMAT_TZ, Constants.DateFormat.CHECKOUT_DATE_FORMAT)));
                } else {
                    cvPointsEarned.setVisibility(View.GONE);
                }
            }
        }

        if (Dependencies.isLaundryApp() && currentTask.getTaskType() == 2) {
            if (!Utils.getDoubleTwoDigits(currentTask.getRemainingBalance().doubleValue()).equalsIgnoreCase("0.00")) {
                cvRemainingAmount.setVisibility(View.VISIBLE);
                tvRemainingAmountText.setText(getStrings(R.string.remaining_amount));
                tvRemainingAmount.setText(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + Utils.getDoubleTwoDigits(currentTask.getRemainingBalance().doubleValue()));
            } else {
                cvRemainingAmount.setVisibility(View.GONE);
                rlRemainingAmount.setVisibility(View.GONE);
            }

        }

        if (currentTask.getRefundedAmount() > 0) {
            cvRemainingAmount.setVisibility(View.VISIBLE);
            rlRefundAmount.setVisibility(View.VISIBLE);
            tvRefundAmountText.setText(getStrings(R.string.refunded_amount));
            tvRefundAmount.setText(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + Utils.getDoubleTwoDigits(currentTask.getRefundedAmount()));
        } else {
            rlRefundAmount.setVisibility(View.GONE);
        }

        cvRemainingAmount.setVisibility(View.GONE);


        if (currentTask.getReturn_enabled() == 1) {
            checkboxReturn.setVisibility(View.VISIBLE);
        }


        tvTotal.setText(Utils.getCurrencySymbolNew(currentTask.getOrderCurrencySymbol()) + orderAmount);

        if (currentTask.getIsCustomOrder() != 0
                && currentTask.getPromoList().size() == 0
                && (Double.valueOf(getOrderAmount(currentTask, true)) == 0)) {
            rlSummaryHeader.setVisibility(View.GONE);
            llBillBreakdown.setVisibility(View.GONE);


            llOrderDetails.setVisibility(View.VISIBLE);
            detailDropDownIV.animate().rotation(180).start();


            if (currentTask.getCheckoutTemplate() != null && currentTask.getCheckoutTemplate().size() > 0) {
                cvAdditionalPriceParent.setVisibility(View.VISIBLE);

            } else {
                cvAdditionalPriceParent.setVisibility(View.GONE);
            }
        } else {
            llBillBreakdown.setVisibility(View.VISIBLE);
            rlSummaryHeader.setVisibility(View.VISIBLE);
            cvAdditionalPriceParent.setVisibility(View.GONE);
        }

        setCancellationPolicy();
    }

    public void getAgentData(int position) {
        HashMap<String, String> map = new HashMap<>();


        for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {
            if (i == position)
                currentTask.getOrderDetails().get(i).getProduct().getServices().setSelected(true);
            else
                currentTask.getOrderDetails().get(i).getProduct().getServices().setSelected(false);
        }
        if (orderItemsAdapter != null)
            orderItemsAdapter.notifyDataSetChanged();

        map.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString());
        map.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        map.put(JOB_ID, currentTask.getJobId().toString());

        if (currentTask.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                && currentTask.getPd_or_appointment() != Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
            map.put("product_id", currentTask.getOrderDetails().get(position).getProduct().getServices().getProductId().toString());
        } else {
            map.put("product_id", "0");
        }

        RestClient.getApiInterface(this).getTrackingDetails(map)
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        TrackingDetails data = baseModel.toResponseModel(TrackingDetails.class);
                        setMapUrl(data.getTrackingLink(), data.getAgentInfo());
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (error.getStatusCode() == 900) {
                            rlUnavailNet.setVisibility(View.VISIBLE);
                            Utils.snackBar(mActivity, error.getMessage());
                        } else {
                            new AlertDialog.Builder(mActivity).message(error.getMessage()).listener(new AlertDialog.Listener() {
                                @Override
                                public void performPostAlertAction(int purpose, Bundle backpack) {
                                    onBackPressed();
                                }
                            }).build().show();
                        }
                    }
                });
    }

    private void setBottomSheetExpend() {
        sheetBehavior.setPeekHeight(parentRL.getHeight());
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        isBottomSheetDragable = false;

    }

    private void setBottomSheetCollapsed() {
        isBottomSheetDragable = true;
        if (driverDetailRL.getHeight() == 0) {
            sheetBehavior.setPeekHeight(280);
        } else
            sheetBehavior.setPeekHeight(driverDetailRL.getHeight());
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void setMapUrl(String url, AgentInfo agentInfo) {
        //  (!appConfig.enabled_tracking_link_after_dispatched
        //  || appConfig.enabled_tracking_link_after_dispatched && order.job_status == 12)
        if (StorefrontCommonData.getAppConfigurationData().getEnabledTrackingLinkAfterDispatched() == 0
                || (StorefrontCommonData.getAppConfigurationData().getEnabledTrackingLinkAfterDispatched() == 1
                && currentTask.getJobStatus() == Constants.TaskStatus.DISPATCHED.value)) {

            if (agentInfo != null /*&& url != null && !url.isEmpty()*/) {
                agentInf = agentInfo;
                driverDetailRL.setVisibility(View.VISIBLE);

                if ((agentInfo.getFleetImage() != null) && (!agentInfo.getFleetImage().equals(""))) {

                    new GlideUtil.GlideUtilBuilder(driverImageIV)
                            .setPlaceholder(R.drawable.ic_profile_placeholder)
                            .setCenterCrop(true)
                            .setLoadItem(agentInfo.getFleetImage())
                            .setTransformation(new CircleCrop())
                            .build();
                } else {
                    driverImageIV.setImageResource(R.drawable.ic_profile_placeholder);
                }

                agentNameTV.setText(agentInfo.getUsername());
                if (agentInfo.getMessage() != null && !agentInfo.getMessage().isEmpty()) {
                    riderTaskStatusTV.setVisibility(View.VISIBLE);
                    riderTaskStatusTV.setText(agentInfo.getMessage());
                } else {
                    riderTaskStatusTV.setVisibility(View.GONE);
                }
                Utils.addStarsToLayout(mActivity, llShowCustomerRatings, agentInfo.getFleetRating().doubleValue());

                if (agentInfo.getPhone() != null && !agentInfo.getPhone().isEmpty()) {
                    agentChatIV.setVisibility(UIManager.isFuguChatEnabled() ? View.VISIBLE : View.GONE);
                } else {
                    agentChatIV.setVisibility(View.GONE);

                }

                if (agentInfo.getPhone() != null && !agentInfo.getPhone().isEmpty()) {
                    // agentChatIV.setVisibility(View.VISIBLE);
                    callIV.setVisibility(View.VISIBLE);
                } else {
                    //  agentChatIV.setVisibility(View.GONE);
                    callIV.setVisibility(View.GONE);
                }

            } else {
                agentInf = null;
                driverDetailRL.setVisibility(View.GONE);
            }

            if (url != null) {
                track_view_wv.loadUrl(url);
            }/* else {
            agentInf = null;
            driverDetailRL.setVisibility(View.GONE);
        }*/


        }
    }


    private void setRepayButtonVisible() {
        if (!(currentTask.getIsCustomOrder() == 2) && currentTask.isRepayButtonShown()) {
            tvRepay.setVisibility(View.VISIBLE);
        }
    }

    private void setCancellationPolicy() {
        boolean isCancelOrder = false;
        if (currentTask.getOrderDetails() != null) {
            for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {
                if (currentTask.getOrderDetails().get(i).getProduct().getServices() != null
                        && currentTask.getOrderDetails().get(i).getProduct().getServices().getCancelAllowed() == 1) {
                    isCancelOrder = true;
                    break;
                }
            }
        }
        if ((currentTask.getCancelAllowed() == 1
                || isCancelOrder)
                && UIManager.isCancellationPolicyEnabled()) {
            tvCancellationPolicy.setText(getStrings(R.string.asterisk) + getStrings(R.string.cancellation_policy));
            tvCancellationPolicy.setVisibility(View.VISIBLE);
        }

    }

    private String getPayableAmount(String orderAmount, TaskData currentTask) {
        double payableAmount = Double.parseDouble(orderAmount) - currentTask.getLoyaltyPoints().getDiscountAmount().doubleValue();
        return (Utils.getDoubleTwoDigits(payableAmount)) + "";
    }

    private void setReviewsRatings() {
        boolean showRating = !(Dependencies.isLaundryApp() && currentTask.getTaskType() == 1);

        if (currentTask.showRatings() && showRating) {
//            cvRateReviews.setVisibility(View.VISIBLE);
            llRatingHeader.setVisibility(View.VISIBLE);
            llShowOrderRatings.setVisibility(View.VISIBLE);
            btnSubmitRate.setVisibility(View.GONE);
            Utils.addStarsToLayout(mActivity, llShowOrderRatings, currentTask.getCustomerRating().doubleValue());

            tvOrderComments.setText(currentTask.getCustomerComment());
            tvOrderComments.setVisibility(tvOrderComments.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        } else {
//            cvRateReviews.setVisibility(currentTask.isShowRatingButton() && showRating ? View.VISIBLE : View.GONE);
            llRatingHeader.setVisibility(currentTask.isShowRatingButton() && showRating ? View.VISIBLE : View.GONE);
            btnSubmitRate.setVisibility(currentTask.isShowRatingButton() && showRating ? View.VISIBLE : View.GONE);
            tvOrderComments.setVisibility(View.GONE);
            llShowOrderRatings.setVisibility(View.GONE);

        }
//        if (showRating) {
//            if (currentTask.isRatingDialogForced()) {
//                btnSubmitRate.performClick();
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("refresh"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onBackPressed() {
        if (fromScreen == -1) {
            CommonAPIUtils.getSuperCategories(StorefrontCommonData.getUserData(), mActivity);
        } else if (fromScreen == 1) {
            Bundle bundle = new Bundle();
            bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");

            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Bundle extras = new Bundle();
            Transition.exit(this, TasksActivity.class, extras);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnChatWithUs:
                if (currentTask != null)
                    Utils.startChat(currentTask.getHippoTransactionId(), currentTask.getMerchantName()
                            , currentTask.getJobId(), currentTask.getIsCustomOrder(), currentTask.getGroupingTags());
                break;
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.tvRetry:
                getTaskDetails(true);
                break;
            case R.id.btnCancelOrder:
                if (UIManager.isCancellationPolicyEnabled())
                    getCancellationCharges();
                else {
                    if (UIManager.getCancellationReasonType() == 0) {
                        openCancelPopup();
                    } else {
                        getCancellationReason(null);
                    }
                }

                break;
            case R.id.btnSubmitRate:


                /**
                 * Agent rating is implemented for Product marketplace and Multiple services as
                 * single order marketplace only
                 * Multiple services as seperate order on tookan doesnot support agent rating in yelo
                 */
                Intent intent = new Intent(this, RatingsActivity.class);
                intent.putExtra(RatingsActivity.EXTRA_TASK_DETAILS, currentTask);
                startActivityForResult(intent, RatingsActivity.REQUEST_CODE_FOR_RATING);
                break;

            case R.id.tvAdditionalHeadingCustomNoPrice:
            case R.id.tvAdditionalHeading:
                Intent templateIntent = new Intent(this, CheckoutTemplateActivity.class);
                if (templateDataList != null) {
                    templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);
                    if (currentTask.getIsCustomOrder() != 0)
                        templateIntent.putExtra(CheckoutTemplateConstants.IS_CUSTOM_ORDER, true);
                    templateIntent.putExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, true);
                    templateIntent.putExtra("OrderCurrencySymbol", currentTask.getOrderCurrencySymbol());
                }
                startActivityForResult(templateIntent, CheckoutTemplateConstants.REQUEST_CODE_TO_OPEN_TEMPLATE);

                break;

            case R.id.tvCancellationPolicy:
                Intent cancellationIntent = new Intent(this, ViewCancellationPolicyActivity.class);
                cancellationIntent.putExtra(JOB_ID, currentTask.getJobId());
                startActivity(cancellationIntent);
                break;
//            case R.id.btnTrack:
//                if (currentTask.getDeliveryMethod() == Constants.DeliveryMode.HOME_DELIVERY && !currentTask.getTrackingLink().isEmpty()) {
//                    Intent intentPayment = new Intent(mActivity, WebViewTrackingActivity.class);
//                    intentPayment.putExtra(URL_WEBVIEW, currentTask.getTrackingLink());
//                    intentPayment.putExtra(HEADER_WEBVIEW, getStrings(R.string.track));
//                    startActivity(intentPayment);
//                    AnimationUtils.forwardTransition(mActivity);
//                } else {
//                    Intent mapintent = new Intent(android.content.Intent.ACTION_VIEW,
//                            Uri.parse("http://maps.google.com/maps?daddr=" + currentTask.getJobPickupLatitude() + "," + currentTask.getJobPickupLongitude()));
//                    startActivity(mapintent);
//                }
//                break;
            case R.id.tvRepay:
                Intent intentRePayment = new Intent(mActivity, MakePaymentActivity.class);
                intentRePayment.putExtra(TASK_DETAILS, currentTask);
                startActivityForResult(intentRePayment, OPEN_REPAYMENT_ACTIVITY);
                AnimationUtils.forwardTransition(mActivity);
                break;

            case R.id.orderDetailHeaderRL:

                if (llOrderDetails.getVisibility() == View.VISIBLE) {
                    llOrderDetails.setVisibility(View.GONE);
                    detailDropDownIV.animate().rotation(0).start();
                } else {
                    llOrderDetails.setVisibility(View.VISIBLE);
                    detailDropDownIV.animate().rotation(180).start();

                }

                break;
            case R.id.rlPickupHeader:

                if (llPickupDetail.getVisibility() == View.VISIBLE) {
                    llPickupDetail.setVisibility(View.GONE);
                    ivPickupDropdown.animate().rotation(0).start();
                } else {
                    llPickupDetail.setVisibility(View.VISIBLE);
                    ivPickupDropdown.animate().rotation(180).start();

                }
                break;
            case R.id.ratingHeadRL:

                if (cvRateReviews.getVisibility() == View.VISIBLE) {
                    cvRateReviews.setVisibility(View.GONE);
                    ratingDropDownIV.animate().rotation(0).start();
                } else {
                    cvRateReviews.setVisibility(View.VISIBLE);
                    ratingDropDownIV.animate().rotation(180).start();

                }

                break;
            case R.id.helpHeaderRL:
//                if (helpViewLL.getVisibility() == View.VISIBLE) {
//                    helpViewLL.setVisibility(View.GONE);
//                    helpDropDownIV.animate().rotation(0).start();
//                } else {
//                    helpViewLL.setVisibility(View.VISIBLE);
//                    helpDropDownIV.animate().rotation(180).start();
//
//                }
                break;
            case R.id.rlSummaryHeader:
                if (orderSummeryViewLL.getVisibility() == View.VISIBLE) {
                    orderSummeryViewLL.setVisibility(View.GONE);
                    summeryDropDownIV.animate().rotation(0).start();
                } else {
                    orderSummeryViewLL.setVisibility(View.VISIBLE);
                    summeryDropDownIV.animate().rotation(180).start();

                }
                break;

            case R.id.agentChatIV:
                if (agentInf != null && agentInf.getFleetId() > 0) {
                    ArrayList<String> agentList = new ArrayList<>();
                    ChatByUniqueIdAttributes attributes;


                    agentList.add("driver" + agentInf.getFleetId());

                    attributes = new ChatByUniqueIdAttributes.Builder()
//                            .setTransactionId(currentTask.getJobId() + "")
                            .setTransactionId(currentTask.getJobId() + "_driver" + agentInf.getFleetId())
                            .setUserUniqueKey(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString())
                            .setOtherUserUniqueKeys(agentList)
                            .setChannelName("#" + currentTask.getJobId())
                            .setGroupingTags(currentTask.getGroupingTags())
                            .build();

                    HippoConfig.getInstance().openChatByUniqueId(attributes);
                }

                break;
            case R.id.callIV:
                Utils.openCallDialer(mActivity, agentInf.getPhone());

                break;

        }
    }


    private String getOrderAmount(TaskData data, boolean isTotalAmount) {
        double price = 0.00;

        if (isTotalAmount && data != null) {
            return data.getTotalAmount();
        } else {
            if (data != null) {
                return (Utils.getDoubleTwoDigits(Double.valueOf(data.getOrderAmount())));
            }
            return (Utils.getDoubleTwoDigits(price)) + "";
        }
    }

    private void getCancellationReason(final GetCancellationData data) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("access_token", Dependencies.getAccessToken(mActivity))
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("user_id", StorefrontCommonData.getFormSettings().getUserId())
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        Dependencies.addCommonParameters(commonParams, mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(this).getCancellationReason(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        CancellationReasonModel[] cancellationReasonArrayList = baseModel.toResponseModel(CancellationReasonModel[].class);
                        cancelReasonArrayList = new ArrayList<CancellationReasonModel>(Arrays.asList(cancellationReasonArrayList));
                        if (data == null)
                            openCancelPopup();
                        else
                            openCancelPopup(data);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }

    private void openCancelPopup() {
        CancelReasonBottomSheetFragment cancelReasonBottomSheetFragment = new CancelReasonBottomSheetFragment(mActivity, jobId,
                cancelReasonArrayList,
                new CancelReasonBottomSheetFragment.CallbackCancelFragment() {
                    @Override
                    public void cancelApiSuccess() {
                        getTaskDetails(true);

                    }

                });
        cancelReasonBottomSheetFragment.show(getSupportFragmentManager(), cancelReasonBottomSheetFragment.getTag());
    }

    private void openCancelPopup(GetCancellationData data) {
        CancelReasonBottomSheetFragment cancelReasonBottomSheetFragment = new CancelReasonBottomSheetFragment(mActivity, jobId,
                cancelReasonArrayList,
                data,
                new CancelReasonBottomSheetFragment.CallbackCancelFragment() {
                    @Override
                    public void cancelApiSuccess() {
                        getTaskDetails(true);

                    }

                });
        cancelReasonBottomSheetFragment.show(getSupportFragmentManager(), cancelReasonBottomSheetFragment.getTag());
    }

    private void getTaskDetails(final boolean showLoader) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("job_id", jobId);
        commonParams.add("business_model_type", UIManager.getBusinessModelType());
        commonParams.add("agent_info", "1");

        RestClient.getApiInterface(this).getJobDetails(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, showLoader, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        rlUnavailNet.setVisibility(View.GONE);

//                taskDetails.setData(baseModel.toResponseModel(TaskData[].class));
//                MoreItemModel[] moreItemModels = commonResponse.toResponseModel(MoreItemModel[].class);

                        TaskDetails taskDetails = new TaskDetails();
                        TaskData[] taskDataa = baseModel.toResponseModel(TaskData[].class);
                        taskDetails.setData(new ArrayList<TaskData>(Arrays.asList(taskDataa)));
                        if (taskDetails.getData().size() > 0) {
                            currentTask = taskDetails.getData().get(0);
                        }
                        setData();

                        if (currentTask.getCheckoutTemplate() != null && currentTask.getCheckoutTemplate().size() > 0) {
                            templateDataList = currentTask.getCheckoutTemplate();
                            displayAdditionalTotalPrice(templateDataList);
                        }
                        setPickupDetailData();

                    }


                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (error.getStatusCode() == 900) {
                            rlUnavailNet.setVisibility(View.VISIBLE);
                            Utils.snackBar(mActivity, error.getMessage());
                        } else {
                            new AlertDialog.Builder(mActivity).message(error.getMessage()).listener(new AlertDialog.Listener() {
                                @Override
                                public void performPostAlertAction(int purpose, Bundle backpack) {
                                    onBackPressed();
                                }
                            }).build().show();
                        }
                    }
                });
    }

    private void getCancellationCharges() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(JOB_ID, currentTask.getJobId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            builder.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            builder.add("language", "en");
        }

        RestClient.getApiInterface(this).getCancellationCharges(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        GetCancellationData data = baseModel.toResponseModel(GetCancellationData.class);
                        if (UIManager.getCancellationReasonType() == 0) {
                            openCancelPopup(data);
                        } else {
                            getCancellationReason(data);
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });


    }

    /**
     * bold " -  " when displaying Collection and delivery slot in laundry
     *
     * @param text1
     * @param boldText
     * @param text2
     * @return
     */
    public Spannable createSpanForBoldText(String text1, String boldText, String text2) {
        Spannable wordToSpan = null;
        try {
            wordToSpan = new SpannableString(text1 + boldText + text2);
            wordToSpan.setSpan(new CustomTypefaceSpan("", Font.getSemiBold(this)),
                    text1.length(), text1.length() + boldText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        } catch (Exception e) {

            Utils.printStackTrace(e);
            return wordToSpan;
        }

    }

    /*
     * Set visiblity of help section depending on chat and track buttons
     */
    public void setHelpSectionVisibility() {
        if (btnChatWithUs.getVisibility() == View.GONE /*&& btnTrack.getVisibility() == View.GONE*/) {
            cvHelp.setVisibility(View.GONE);
        } else {
            cvHelp.setVisibility(View.VISIBLE);
            if (btnChatWithUs.getVisibility() == View.VISIBLE /*&& btnTrack.getVisibility() == View.VISIBLE*/) {

                btnChatWithUs.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_button_background_slightly_rounded));
                btnChatWithUs.setTextColor(getResources().getColor(R.color.white));
//                btnTrack.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white_stroke_accent));
//                btnTrack.setTextColor(getResources().getColor(R.color.secondary_text_color));
            } else {
                btnChatWithUs.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_button_background_slightly_rounded));
                btnChatWithUs.setTextColor(getResources().getColor(R.color.white));
//                btnTrack.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_button_background_slightly_rounded));
//                btnTrack.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    private boolean onOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http")) return false;//open web links as usual
        //try to find browse activity to handle uri
        Uri parsedUri = Uri.parse(url);
        PackageManager packageManager = getPackageManager();
        Intent browseIntent = new Intent(Intent.ACTION_VIEW).setData(parsedUri);
        if (browseIntent.resolveActivity(packageManager) != null) {
            startActivity(browseIntent);
            return true;
        }
        //if not activity found, try to parse intent://
        if (url.startsWith("intent:")) {
            try {

                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                //try to find fallback url
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                if (fallbackUrl != null) {
                    view.loadUrl(fallbackUrl);
                    return true;
                }
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    return true;
                }
                //invite to install
                Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(
                        Uri.parse("market://details?id=" + intent.getPackage()));
                if (marketIntent.resolveActivity(packageManager) != null) {
                    startActivity(marketIntent);
                    return true;
                }
            } catch (URISyntaxException e) {
                //not an intent uri
            }
        }
        return true;//do nothing in other cases

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return onOverrideUrlLoading(view, request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return onOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

//            ProgressDialog.dismiss();
            if (StorefrontCommonData.getAppConfigurationData().getEnabledTrackingLinkAfterDispatched() == 0
                    || (StorefrontCommonData.getAppConfigurationData().getEnabledTrackingLinkAfterDispatched() == 1
                    && currentTask.getJobStatus() == Constants.TaskStatus.DISPATCHED.value)) {
                setBottomSheetCollapsed();
            }


            Log.e("URL", url + "");
            if (url.startsWith("http:") || url.startsWith("https:")) {
                if (url.toLowerCase().contains("success")) {
                    Bundle extras = new Bundle();
                    extras.putBoolean("hitFetchMerchantCard", true);
                    Intent intent = new Intent();
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (url.toLowerCase().contains("error")) {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.card_cannot_be_added)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            Bundle extras = new Bundle();
                            extras.putBoolean("hitFetchMerchantCard", false);
                            Intent intent = new Intent();
                            intent.putExtras(extras);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).build().show();
                }
            }
            super.onPageFinished(view, url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            ProgressDialog.show(mActivity);
            super.onPageStarted(view, url, favicon);
        }
    }

}
