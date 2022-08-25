package com.tookancustomer.modules.reward.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.modules.reward.adapter.RewardsAdapter;
import com.tookancustomer.modules.reward.model.rewardPlans.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;


import java.util.ArrayList;
import java.util.Arrays;

import static com.tookancustomer.appdata.Keys.Prefs.DEVICE_TOKEN;
import static com.tookancustomer.modules.reward.activity.PlanDetailsActivity.EXTRA_PAYMENT_FLOW_TYPE;

public class RewardsActivity extends BaseActivity implements View.OnClickListener, RewardsAdapter.OnItemSelectedListener {

    private RelativeLayout rlBack;
    private TextView tvHeading;

    private RecyclerView recyclerPlans;
    private RewardsAdapter rewardsAdapter;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PlanDetailsActivity.REQUEST_CODE_TO_OPEN_PLAN_DETAILS:
                    if (data.getIntExtra(EXTRA_PAYMENT_FLOW_TYPE, 0) == 2) {
                        Utils.snackbarSuccess(RewardsActivity.this, getStrings(R.string.reward_activated_successfully).replace(REWARDS,StorefrontCommonData.getTerminology().getRewards()));

                    }
                    getRewardPlans();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        init();
        setData();
        setOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRewardPlans();
    }

    private void setData() {
        tvHeading.setText(StorefrontCommonData.getTerminology().getRewards());
        setAdapter();
    }

    private void setOnClickListeners() {
        rlBack.setOnClickListener(this);
    }

    private void setAdapter() {
        rewardsAdapter = new RewardsAdapter(this);
        recyclerPlans.setAdapter(rewardsAdapter);
    }

    private void init() {
        rlBack = findViewById(R.id.rlBack);
        findViewById(R.id.rlInvisible).setVisibility(View.VISIBLE);
        tvHeading = findViewById(R.id.tvHeading);

        recyclerPlans = findViewById(R.id.recycler_plans);
        recyclerPlans.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemSelected(Datum plan) {
        Intent intent = new Intent(RewardsActivity.this, PlanDetailsActivity.class);
        intent.putExtra(PlanDetailsActivity.EXTRA_REWARD_PLAN_DETAILS, plan);
        startActivityForResult(intent, PlanDetailsActivity.REQUEST_CODE_TO_OPEN_PLAN_DETAILS);
    }

    public void getRewardPlans() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder();
        builder.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(APP_VERSION, Dependencies.getAppVersionCode(mActivity))
                .add(DEVICE_TOKEN, Dependencies.getDeviceToken(mActivity));


        RestClient.getApiInterface(this).getRewardPlans(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Log.e("REWARD PLANS", "SUCCESS");
                        Datum[] plans = baseModel.toResponseModel(Datum[].class);
                        ArrayList<Datum> plansList = new ArrayList<Datum>(Arrays.asList(plans));
                        rewardsAdapter.setPlansList(plansList);
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        Log.e("REWARD PLANS", "FAILURE");
                    }
                });

    }
}
