package com.tookancustomer.modules.customerSubscription;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.BaseActivity;
import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.payment.constants.PaymentConstants;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomerSubscriptionActivity extends BaseActivity implements View.OnClickListener, Keys.Extras {
    private int selectedPlanCustomerSubscriptionId;


    private TextView tvHeading;
    private TextView  tvCurrentPlan, tvBrowsePlan;


    private TextView tvSkip;
    private RecyclerView subscriptionListRV;
    private CustomerPlansAdapter customerPlansAdapter;
    private LinearLayoutManager mLayoutManager;
    private Activity activity;
    private Button btnSkip;
    private TextView noDataTV;
    private ShimmerFrameLayout shimmerLayout;
    private CustomerSubscriptionData customerSubscriptionData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout rlBack;

    private TextView planNameTV, planAmountTV, planDescriptionTV, validityTV, validDateTV, remainingOrderTV;
    private Button choosePlanBT;
    private ImageView planIV, subscriptionLabelIV;
    private RelativeLayout customerPlanCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_subscription);
        activity = this;
        initUI();

        getPaymentMethods();
    }

    private void getPaymentMethods() {
        shimmerLayout.setVisibility(View.VISIBLE);

        HashMap<String, String> paymenthashMap = new HashMap<>();

        paymenthashMap.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        paymenthashMap.put(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(USER_ID, StorefrontCommonData.getFormSettings().getUserId() + "");
        paymenthashMap.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
        paymenthashMap.put(LANGUAGE, StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());

        RestClient.getApiInterface(mActivity).getPaymentMethods(paymenthashMap).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
                paymentMethods = new Gson().fromJson(new Gson().toJson(baseModel.data), new TypeToken<ArrayList<PaymentMethod>>() {
                }.getType());
                StorefrontCommonData.getFormSettings().setPaymentMethods(paymentMethods);

                getSubscriptionPlan(false);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                getSubscriptionPlan(false);

            }
        });
    }


    private void initUI() {
        tvCurrentPlan = findViewById(R.id.tvCurrentPlan);
        tvBrowsePlan = findViewById(R.id.tvBrowsePlan);
        tvCurrentPlan.setText(StorefrontCommonData.getString(mActivity, R.string.current_plan));
        tvBrowsePlan.setText(StorefrontCommonData.getString(mActivity, R.string.browse_plans));

        tvHeading = findViewById(R.id.tvHeading);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        noDataTV = findViewById(R.id.noDataTV);
        btnSkip = findViewById(R.id.btnSkip);
        subscriptionListRV = findViewById(R.id.subscriptionListRV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rlBack = findViewById(R.id.rlBack);
        customerPlanCV = findViewById(R.id.customerPlanCV);
        validDateTV = findViewById(R.id.validDateTV);
        remainingOrderTV = findViewById(R.id.remainingOrderTV);

        planIV = findViewById(R.id.planIV);
        planNameTV = findViewById(R.id.planNameTV);
        planAmountTV = findViewById(R.id.planAmountTV);
        planDescriptionTV = findViewById(R.id.planDescriptionTV);
        planDescriptionTV.setVisibility(View.GONE);
        validityTV = findViewById(R.id.validityTV);
        choosePlanBT = findViewById(R.id.choosePlanBT);
        subscriptionLabelIV = findViewById(R.id.subscriptionLabelIV);


        tvHeading.setText(getStrings(R.string.subscription_plan));
        noDataTV.setText(getStrings(R.string.no_subscription_plans_available));

        if (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionMandatory() == 1)
            btnSkip.setText(getStrings(R.string.logout));
        else
            btnSkip.setText(getStrings(R.string.skip));


        if (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionMandatory() == 1)
            btnSkip.setVisibility(View.GONE);
        else
            btnSkip.setVisibility(View.VISIBLE);


        if (getIntent() != null && getIntent().getExtras().containsKey("fromSideMenu")) {
            rlBack.setVisibility(View.VISIBLE);
            btnSkip.setVisibility(View.GONE);
        } else {
            rlBack.setVisibility(View.GONE);
            btnSkip.setVisibility(View.VISIBLE);

        }


        mLayoutManager = new LinearLayoutManager(this);
        subscriptionListRV.setItemAnimator(new DefaultItemAnimator());
        subscriptionListRV.setLayoutManager(mLayoutManager);
        ViewCompat.setNestedScrollingEnabled(subscriptionListRV, false);
        subscriptionListRV.setHasFixedSize(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubscriptionPlan(false);
            }
        });


        Utils.setOnClickListener(this, btnSkip, rlBack,choosePlanBT);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSkip:
                UserData userData = StorefrontCommonData.getUserData();


                CommonAPIUtils.getSuperCategories(userData, mActivity);
                break;
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.choosePlanBT:
                new OptionsDialog.Builder(activity)
                        .message(StorefrontCommonData.getString(activity, R.string.cancel_subscription))
                        .positiveButton(StorefrontCommonData.getString(activity, R.string.confirm))
                        .negativeButton(StorefrontCommonData.getString(activity, R.string.cancel)).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        apiHitForCancelSubscription(customerSubscriptionData.getCustomerPlan().get(0).getPlanID());
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                    }
                }).build().show();
                break;

        }
    }


    private void getSubscriptionPlan(boolean showLoding) {


        HashMap<String, String> map = new HashMap<>();

        map.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        map.put(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");

        RestClient.getApiInterface(mActivity).getSubscriptionPlans(map).enqueue(new ResponseResolver<BaseModel>(mActivity, showLoding, true) {
            @Override
            public void success(BaseModel baseModel) {
                customerSubscriptionData = baseModel.toResponseModel(CustomerSubscriptionData.class);
                setData();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                shimmerLayout.setVisibility(View.GONE);
                if (customerSubscriptionData != null && customerSubscriptionData.getPlans().size() > 0) {
                    noDataTV.setVisibility(View.GONE);
                    subscriptionListRV.setVisibility(View.VISIBLE);
                } else {
                    noDataTV.setVisibility(View.VISIBLE);
                    subscriptionListRV.setVisibility(View.GONE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
    }

    private void setData() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        shimmerLayout.setVisibility(View.GONE);
        customerPlansAdapter = new CustomerPlansAdapter(activity, customerSubscriptionData.getPlans(), customerSubscriptionData.getCustomerPlan());
        subscriptionListRV.setAdapter(customerPlansAdapter);


        if (customerSubscriptionData.getCustomerPlan() != null && customerSubscriptionData.getCustomerPlan().size() > 0) {
            selectedPlanCustomerSubscriptionId = customerSubscriptionData.getCustomerPlan().get(0).getSubscriptionId();
            customerPlanCV.setVisibility(View.VISIBLE);
            subscriptionLabelIV.setVisibility(View.VISIBLE);
            tvCurrentPlan.setVisibility(View.VISIBLE);
            tvBrowsePlan.setVisibility(View.VISIBLE);
            planNameTV.setText(customerSubscriptionData.getCustomerPlan().get(0).getPlanName());
            choosePlanBT.setText(StorefrontCommonData.getString(mActivity,R.string.cancel));

            if (customerSubscriptionData.getCustomerPlan().get(0).getIsCancel() == 1) {
                choosePlanBT.setEnabled(false);
                choosePlanBT.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                choosePlanBT.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.color.disable_date_color));

            } else {
                choosePlanBT.setEnabled(true);
                choosePlanBT.getBackground().setColorFilter(null);
            }
            planAmountTV.setText(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(customerSubscriptionData.getCustomerPlan().get(0).getAmount()));
            validDateTV.setVisibility(View.VISIBLE);
            remainingOrderTV.setVisibility(View.VISIBLE);
            String date = DateUtils.getInstance().convertToLocal(customerSubscriptionData.getCustomerPlan().get(0).getExpiryDatetime(), Constants.DateFormat.STANDARD_DATE_FORMAT_TZ, Constants.DateFormat.CHECKOUT_DATE_FORMAT);

//            StorefrontCommonData.getTerminology().getCheckoutTemplate()

            validDateTV.setText(getStrings(R.string.expiry_date_time) + " : " + date);
            remainingOrderTV.setText(getStrings(R.string.orders_remaining).replace(TerminologyStrings.ORDERS, StorefrontCommonData.getTerminology().getOrders())
                    + " : " + customerSubscriptionData.getCustomerPlan().get(0).getNumberOfOrdersRemaining());
            if (customerSubscriptionData.getCustomerPlan().get(0).getNumberOfOrders() != null && customerSubscriptionData.getCustomerPlan().get(0).getNumberOfOrders() > 0) {
                validityTV.setText(customerSubscriptionData.getCustomerPlan().get(0).getNumberOfOrders() + " (" + getStrings(R.string.allowed_orders).replace(TerminologyStrings.ORDERS, StorefrontCommonData.getTerminology().getOrders().toLowerCase()) + ")");
                validityTV.setVisibility(View.VISIBLE);
            } else {
                validityTV.setVisibility(View.GONE);
            }


            if (customerSubscriptionData.getCustomerPlan().get(0).getImageUrl().isEmpty()) {
                planIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
            } else {

                new GlideUtil.GlideUtilBuilder(planIV)
                        .setLoadItem(customerSubscriptionData.getCustomerPlan().get(0).getImageUrl())
                        .setPlaceholder(R.drawable.ic_loading_image)
                        .setError(R.drawable.ic_image_placeholder)
                        .setFallback(R.drawable.ic_image_placeholder)
                        .build();
            }

        } else {
            customerPlanCV.setVisibility(View.GONE);
            tvCurrentPlan.setVisibility(View.GONE);
            tvBrowsePlan.setVisibility(View.GONE);

        }

        if (customerSubscriptionData.getPlans().size() > 0) {
            noDataTV.setVisibility(View.GONE);
            subscriptionListRV.setVisibility(View.VISIBLE);
            // tvCurrentPlan.setVisibility(View.VISIBLE);
            tvBrowsePlan.setVisibility(View.VISIBLE);

        } else {
            noDataTV.setVisibility(View.VISIBLE);
            subscriptionListRV.setVisibility(View.GONE);
            tvCurrentPlan.setVisibility(View.GONE);
            tvBrowsePlan.setVisibility(View.GONE);

        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_ACTIVITY_USER_SUBSCRIPTION:
                if (resultCode == RESULT_OK) {

                    if (getIntent() != null && getIntent().getExtras().containsKey("fromSideMenu")) {
                        getSubscriptionPlan(true);
                    } else {
                        UserData userData = StorefrontCommonData.getUserData();
                        CommonAPIUtils.getSuperCategories(userData, mActivity);
                    }
                } else if (resultCode == RESULT_ERROR) {
                    Utils.snackBar(mActivity, getStrings(R.string.debt_transaction_failed));
                }
                break;
        }
    }

    public void selectPlan(int pos) {


        Intent intentRePayment = new Intent(mActivity, MakePaymentActivity.class);
        intentRePayment.putExtra("paymentForFlow", PaymentConstants.PaymentForFlow.USER_SUBSCRIPTION.intValue);
        intentRePayment.putExtra("subscriptionPlan", customerSubscriptionData.getPlans().get(pos));
        startActivityForResult(intentRePayment, OPEN_ACTIVITY_USER_SUBSCRIPTION);
        AnimationUtils.forwardTransition(mActivity);

    }

    public void apiHitForCancelSubscription(int planId) {
        UserData userData = StorefrontCommonData.getUserData();

        HashMap<String, String> map = new HashMap<>();

        map.put(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        map.put("customer_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
        map.put("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId() + "");
        map.put("is_cancel", "1");
        map.put("subscription_id", selectedPlanCustomerSubscriptionId + "");
        map.put("language", "en");
        map.put("dual_user_key", "0");
        map.put("plan_id", planId + "");

        RestClient.getApiInterface(mActivity).cancelSubscriptionPlan(map).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                //  choosePlanBT.setText("Cancel Subscription");
                choosePlanBT.setText(StorefrontCommonData.getString(activity, R.string.choose_plan));
                finish();


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                shimmerLayout.setVisibility(View.GONE);
                if (customerSubscriptionData != null && customerSubscriptionData.getPlans().size() > 0) {
                    noDataTV.setVisibility(View.GONE);
                    subscriptionListRV.setVisibility(View.VISIBLE);
                } else {
                    noDataTV.setVisibility(View.VISIBLE);
                    subscriptionListRV.setVisibility(View.GONE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
    }



}
