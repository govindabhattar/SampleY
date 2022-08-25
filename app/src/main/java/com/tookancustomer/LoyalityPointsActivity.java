package com.tookancustomer;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.loyalty_points.PointsData;
import com.tookancustomer.models.loyalty_points.PointsDetails;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Prefs.ACCESS_TOKEN;

public class LoyalityPointsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvHeading, tvMinimumPoints, tvEqualTo, tvLoyaltyText,
            tvPoints, tvAmountPoints, tvPointsValue, tvLoyaltyTotal,
            tvRewardConditions, tvNextOrderText, tvPointsExpiry;
    private LinearLayout llLoyaltyPoint;
    private ScrollView svLoyaltyPoint;
    private PointsDetails pointsDetails;
    private RelativeLayout rlBack;
    private Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loyality_points);
        init();
        getLoyaltyPoints();
    }

    private void getLoyaltyPoints() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }
        commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());


        RestClient.getApiInterface(this).getLoyaltyPoints(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                pointsDetails = new PointsDetails();
                PointsData[] taskDataa = baseModel.toResponseModel(PointsData[].class);
                pointsDetails.setData(new ArrayList<PointsData>(Arrays.asList(taskDataa)));
                setData();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void setData() {
        tvLoyaltyTotal.setText(pointsDetails.getData().get(0).getPoints() + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints());
        tvAmountPoints.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + "" + pointsDetails.getData().get(0).getValueCriteria() + ""));
        tvPointsValue.setText(pointsDetails.getData().get(0).getValuePoint() + "");
        tvPoints.setText(getStrings(R.string.get_text) + " " + 10);
        double amount = (pointsDetails.getData().get(0).getRateCriteria() * 10) / pointsDetails.getData().get(0).getRatePoint();
        tvMinimumPoints.setText(getStrings(R.string.purchase_of_text) + " " + (UIManager.getCurrency(Utils.getCurrencySymbol() + "" +  Utils.getDoubleTwoDigits(amount))));
        if (pointsDetails.getData().get(0).getPoints() != 0 && pointsDetails.getData().get(0).getLoyaltyPointHistory() != null) {
            tvPointsExpiry.setText(pointsDetails.getData().get(0).getLoyaltyPointHistory().get(0).getAvailablePoints() + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " +
                    getStrings(R.string.expiring_text) + " " + DateUtils.getInstance().convertToLocal(pointsDetails.getData().get(0).getLoyaltyPointHistory().get(0).getExpiryDate() != null ? pointsDetails.getData().get(0).getLoyaltyPointHistory().get(0).getExpiryDate() : "",
                    Constants.DateFormat.STANDARD_DATE_FORMAT_TZ, Constants.DateFormat.CHECKOUT_DATE_FORMAT));
        }
    }

    private void init() {
        tvHeading = findViewById(R.id.tvHeading);
        tvEqualTo = findViewById(R.id.tvEqualTo);
       // tvEqualTo.setText(getStrings(R.string.equalto_symbol));
        tvLoyaltyTotal = findViewById(R.id.tvLoyaltyTotal);
        tvMinimumPoints = findViewById(R.id.tvMinimumPoints);
        tvAmountPoints = findViewById(R.id.tvAmountPoints);
        tvNextOrderText = findViewById(R.id.tvNextOrderText);
        tvNextOrderText.setText(getStrings(R.string.use_this_loyalty_points_on_your_next_order).replace(LOYALTY_POINTS, StorefrontCommonData.getTerminology().getLoyaltyPoints()).replace(ORDER, StorefrontCommonData.getTerminology().getOrder()));
        rlBack = findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        tvRewardConditions = findViewById(R.id.tvRewardConditions);
        tvRewardConditions.setText(getStrings(R.string.reward_conditions));
        tvRewardConditions.setOnClickListener(this);
        tvPointsValue = findViewById(R.id.tvPointsValue);
        tvPoints = findViewById(R.id.tvPoints);
        tvLoyaltyText = findViewById(R.id.tvLoyaltyText);
        tvLoyaltyText.setText(StorefrontCommonData.getTerminology().getLoyaltyPoints());
        svLoyaltyPoint = findViewById(R.id.svLoyaltyPoint);
        tvPointsExpiry = findViewById(R.id.tvPointsExpiry);
        llLoyaltyPoint = findViewById(R.id.llLoyaltyPoint);
        tvHeading.setText(StorefrontCommonData.getTerminology().getLoyaltyPoints());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRewardConditions:
                showPopUp();
                break;
            case R.id.rlBack:
                finish();
                break;
        }
    }

    private void showPopUp() {
        alertDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        alertDialog.setContentView(R.layout.layout_reward_conditions);

        Window dialogWindow = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.dimAmount = 0.6f;
        layoutParams.dimAmount = 0.8f;

        dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;

        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.setCanceledOnTouchOutside(true);

        TextView tvCondition1 = alertDialog.findViewById(R.id.tvCondition1);
        if (pointsDetails.getData() != null && pointsDetails.getData().size() > 0)
            tvCondition1.setText("\u2022" + " " + getStrings(R.string.minimum) + " " + StorefrontCommonData.getTerminology().getOrder()
                    + " " + getStrings(R.string.amount_required_text) + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " + getStrings(R.string.is_text) + " " + UIManager.getCurrency(Utils.getCurrencySymbol() + " " + pointsDetails.getData().get(0).getMinAmount()));
        TextView tvCondition2 = alertDialog.findViewById(R.id.tvCondition2);
        TextView tvCondition3 = alertDialog.findViewById(R.id.tvCondition3);
        TextView tvCondition4 = alertDialog.findViewById(R.id.tvCondition4);
        TextView tvCondition5 = alertDialog.findViewById(R.id.tvCondition5);
        TextView tvCondition6 = alertDialog.findViewById(R.id.tvCondition6);
        TextView tvCondition7 = alertDialog.findViewById(R.id.tvCondition7);
        TextView tvClose = alertDialog.findViewById(R.id.tvClose);

        tvClose.setText(StorefrontCommonData.getString(mActivity,R.string.close));
        if (pointsDetails.getData().get(0).getPoints() != 0) {
            tvCondition2.setVisibility(View.VISIBLE);
            tvCondition2.setText("\u2022" + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints()
                    + " " + getStrings(R.string.expire_case_text) + " " + pointsDetails.getData().get(0).getExpiryLimit() + " " + getStrings(R.string.dayss));
        }
        tvCondition3.setText("\u2022" + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " + getStrings(R.string.usage_text));
        tvCondition4.setText("\u2022" + " " + getStrings(R.string.tAndC_change_anytime_text));
        tvCondition5.setText("\u2022" + " " + getStrings(R.string.maximum_text) + " " + pointsDetails.getData().get(0).getMaxUsable() + "% " + getStrings(R.string.cart_value_text) + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints());
        tvCondition6.setText("\u2022" + " " + getStrings(R.string.maximum_text) + " " + pointsDetails.getData().get(0).getMaxEarning() + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " + getStrings(R.string.earned_per_order) + " " + StorefrontCommonData.getTerminology().getOrder() + ".");
        tvCondition7.setText("\u2022" + " " + getStrings(R.string.minimum) + " " + StorefrontCommonData.getTerminology().getOrder() + " " + getStrings(R.string.amount_for_earning) + " " + StorefrontCommonData.getTerminology().getLoyaltyPoints() + " " + getStrings(R.string.is_text) + " " + UIManager.getCurrency(Utils.getCurrencySymbol() + " " + pointsDetails.getData().get(0).getMinEarningCriteria()));


        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }

}
