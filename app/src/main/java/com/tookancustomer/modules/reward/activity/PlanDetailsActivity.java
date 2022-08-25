package com.tookancustomer.modules.reward.activity;

import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.reward.model.rewardPlans.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class PlanDetailsActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_REWARD_PLAN_DETAILS = "EXTRA_REWARD_PLAN_DETAILS";
    public static final String EXTRA_TRANSACTION_ID = "EXTRA_TRANSACTION_ID";
    public static final String EXTRA_PAYMENT_FLOW = "EXTRA_PAYMENT_FLOW";
    public static final String EXTRA_PAYMENT_FLOW_TYPE = "EXTRA_PAYMENT_FLOW_TYPE";
    public static final String PAYMENT_METHOD = "EXTRA_PAYMENT_FLOW_TYPE";
    public static final int REQUEST_CODE_TO_OPEN_PLAN_DETAILS = 1001;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private TextView tvPlanName, tvPlanDesc, tvCashback,
            tvMaxAmount, tvOrderCount, tvPlanFee, tvValidUpto,
            tvActivated, tvOrdersLeft;
    private Button btnActivate;
    private Datum plan;
    private boolean isPlanUpdated = false;
    private int paymentFlowType = 0;
    private long paymentFlow = 0;
    AlertDialog.Builder builder;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MakePaymentActivity.REQUEST_CODE_TO_OPEN_PAYMENT_PAGE:
                    if (data.getIntExtra(EXTRA_PAYMENT_FLOW_TYPE, 0) == 2) {
                        isPlanUpdated = true;
                        paymentFlowType = data.getIntExtra(EXTRA_PAYMENT_FLOW_TYPE, 0);
                        paymentFlow = data.getIntExtra(EXTRA_PAYMENT_FLOW, 0);
                        setData();
                        onBackPressed();

                    } else
                        apiHitToBuyPlan(data.getIntExtra(EXTRA_PAYMENT_FLOW, 0), data.getStringExtra(EXTRA_TRANSACTION_ID));
                    break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (isPlanUpdated) {
            Intent intent = new Intent();
            intent.putExtra(PlanDetailsActivity.EXTRA_PAYMENT_FLOW_TYPE, paymentFlowType);
            intent.putExtra(PlanDetailsActivity.EXTRA_PAYMENT_FLOW, paymentFlow);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        init();
        setParameters();
        setData();
        setOnClickListener();
    }

    private void setParameters() {
        plan = (Datum) getIntent().getSerializableExtra(EXTRA_REWARD_PLAN_DETAILS);
    }

    private void setData() {

        tvHeading.setText(StorefrontCommonData.getTerminology().getRewards());

        tvPlanName.setText(plan.getRewardName());
        tvPlanDesc.setText(plan.getDescription());
        tvCashback.setText(Utils.getFormattedDoubleValue(plan.getPercentageCashback()) +
                getStrings(R.string.percentage) +
                getStrings(R.string.empty_space) +
                getStrings(R.string.text_cashback));
        tvMaxAmount.setText(getStrings(R.string.maximum_amount) +
                getStrings(R.string.empty_space) + UIManager.getCurrency(
                Utils.getCurrencyCode() +
                        getStrings(R.string.empty_space) +
                        Utils.getFormattedDoubleValue(plan.getFixedCashback())));

        tvPlanFee.setText(UIManager.getCurrency(Utils.getCurrencyCode() +
                getStrings(R.string.empty_space) +
                Utils.getFormattedDoubleValue(plan.getPlanFees())));

        tvValidUpto.setText(getStrings(R.string.valid_upto) +
                getStrings(R.string.empty_space) +
                plan.getPlanUpto());

        btnActivate.setText(getStrings(R.string.text_activate));


        handlePlanStatus();

    }


    public void handlePlanStatus() {
        int totalOrderCount;

        if (plan.getIsPurchased()) {
            totalOrderCount = plan.getTotalCount();
            btnActivate.setVisibility(View.GONE);
            tvActivated.setVisibility(View.VISIBLE);
            if (plan.getIsExpired()) {
                tvActivated.setText(getStrings(R.string.text_expired));
                tvActivated.setTextColor(ContextCompat.getColor(this, R.color.color_red));
            } else {
                tvActivated.setText(getStrings(R.string.text_activated));
                tvActivated.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            }

            if (plan.getOrderCount() != plan.getTotalCount()) {
                tvOrdersLeft.setVisibility(View.VISIBLE);
                tvOrdersLeft.setText(StorefrontCommonData.getTerminology().getOrders() +
                        getStrings(R.string.empty_space) +
                        getStrings(R.string.text_left) +
                        getStrings(R.string.colon) +
                        getStrings(R.string.empty_space) +
                        plan.getOrderCount());
            }

        } else {
            totalOrderCount = plan.getOrderCount();
            btnActivate.setVisibility(View.VISIBLE);
            tvActivated.setVisibility(View.GONE);
        }


        tvOrderCount.setText(StorefrontCommonData.getTerminology().getOrders() +
                getStrings(R.string.empty_space) +
                getStrings(R.string.text_count) +
                getStrings(R.string.colon) +
                getStrings(R.string.empty_space) +
                totalOrderCount);


    }

    private void setOnClickListener() {
        rlBack.setOnClickListener(this);
        btnActivate.setOnClickListener(this);
    }

    private void init() {
        rlBack = findViewById(R.id.rlBack);
        findViewById(R.id.rlInvisible).setVisibility(View.VISIBLE);
        tvHeading = findViewById(R.id.tvHeading);

        tvPlanName = findViewById(R.id.tvPlanName);
        tvPlanDesc = findViewById(R.id.tvPlanDesc);
        tvCashback = findViewById(R.id.tvCashback);
        tvMaxAmount = findViewById(R.id.tvMaxAmount);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvPlanFee = findViewById(R.id.tvPlanFee);
        tvValidUpto = findViewById(R.id.tvValidUpto);
        tvActivated = findViewById(R.id.tvActivated);
        tvOrdersLeft = findViewById(R.id.tvOrdersLeft);

        btnActivate = findViewById(R.id.btnActivate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.btnActivate:
                double amount = Double.parseDouble(Utils.getFormattedDoubleValue(plan.getPlanFees()));
                if (amount > 0) {

                    Intent intent = new Intent(this, MakePaymentActivity.class);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("actual_amount", Double.valueOf(plan.getPlanFees()));
                        jsonObject.put("total_amount", Double.valueOf(plan.getPlanFees()));
                        jsonObject.put("order_id", 0);
                        jsonObject.put("reward_id", plan.getId());

                    } catch (JSONException e) {

                        Utils.printStackTrace(e);
                    }
                    intent.putExtra("PAYMENT_VIA_HIPPO_DATA", jsonObject.toString());
                    startActivityForResult(intent, MakePaymentActivity.REQUEST_CODE_TO_OPEN_PAYMENT_PAGE);
                } else {
                    builder = new AlertDialog.Builder(this);
                    showAlert();
                }
                break;
        }
    }

    private void showAlert() {
        //Uncomment the below code to Set the message and title from the strings.xml file
        //  builder.setMessage("Buy Plan") .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setCancelable(false)
                .setPositiveButton(getStrings(R.string.continu), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        apiHitToBuyPlan(CASH.intValue, "cash_");
                        // finish();
                    }
                })
                .setNegativeButton(getStrings(R.string.no_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Buy Plan");
        alert.show();
    }

    private void apiHitToBuyPlan(final long paymentFlow, String transactionId) {

        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder builder = new CommonParams.Builder();
        builder.add("vendor_id", userData.getData().getVendorDetails().getVendorId())
                .add("access_token", Dependencies.getAccessToken(mActivity))
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId())
                .add("reward_id", plan.getId())
                .add("payment_method", paymentFlow)
                .add("transaction_id", transactionId);

        RestClient.getApiInterface(this).buyRewardPlan(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        plan = baseModel.toResponseModel(Datum.class);
                        isPlanUpdated = true;
//                        plan.setIsPurchased(isPlanUpdated);
                        setData();
                        Utils.snackbarSuccess(PlanDetailsActivity.this, baseModel.getMessage());
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }


}
