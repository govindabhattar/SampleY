package com.tookancustomer.cancellationPolicy;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.cancellationPolicy.adapter.CancellationPolicyAdapter;
import com.tookancustomer.cancellationPolicy.model.CancellationData;
import com.tookancustomer.cancellationPolicy.model.CancellationRules;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class ViewCancellationPolicyActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlBack;
    private TextView tvHeading;
    private RecyclerView recyclerCancellationPolicy;
    private int jobId, storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cancellation_policy);
        initView();
        getDataFromIntent();
        setData();
        getCancellationPolicy();
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra(JOB_ID)) {
            jobId = getIntent().getIntExtra(JOB_ID, 0);
        }
        if (getIntent().hasExtra("STORE_ID")) {
            storeId = getIntent().getIntExtra("STORE_ID", 0);
        }
    }

    private void setData() {
        tvHeading.setText(getStrings(R.string.cancellation_policy));
        recyclerCancellationPolicy.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapter(ArrayList<CancellationRules> cancellationDataList) {
        if (cancellationDataList != null && cancellationDataList.size() > 0) {

            ArrayList<CancellationRules> pendingRulesList = new ArrayList<>();
            ArrayList<CancellationRules> acceptedRulesList = new ArrayList<>();
            ArrayList<CancellationRules> dispatchedRulesList = new ArrayList<>();

            for (int i = 0; i < cancellationDataList.size(); i++) {
                if (cancellationDataList.get(i).getStatus() == Constants.TaskStatus.PENDING_STATUS.value) {
                    pendingRulesList.add(cancellationDataList.get(i));
                } else if (cancellationDataList.get(i).getStatus() == Constants.TaskStatus.ORDERED.value) {
                    acceptedRulesList.add(cancellationDataList.get(i));
                } else {
                    dispatchedRulesList.add(cancellationDataList.get(i));
                }
            }

            if (acceptedRulesList.size() > 1) {
                CancellationRules maxThresholdRule = acceptedRulesList.get(0);
                acceptedRulesList.remove(0);
                acceptedRulesList.add(maxThresholdRule);
            }

            cancellationDataList.clear();
            cancellationDataList.addAll(pendingRulesList);
            cancellationDataList.addAll(acceptedRulesList);
            cancellationDataList.addAll(dispatchedRulesList);


            recyclerCancellationPolicy.setAdapter(new CancellationPolicyAdapter(cancellationDataList, acceptedRulesList.size()));
        } else {
            recyclerCancellationPolicy.setAdapter(new CancellationPolicyAdapter(null, 0));
        }

    }

    private void initView() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        recyclerCancellationPolicy = findViewById(R.id.recyclerCancellationPolicy);

        Utils.setOnClickListener(this, rlBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }

    private ArrayList<CancellationRules> prepareCancellationPolicyData() {
        ArrayList<CancellationRules> cancellationDataList = new ArrayList<>();
        CancellationRules cancellationData = new CancellationRules(Constants.TaskStatus.PENDING_STATUS.value,
                StorefrontCommonData.getTerminology().getMerchant(),
                StorefrontCommonData.getTerminology().getOrder(),
                StorefrontCommonData.getTerminology().getDispatched(),
                50, 200);

        cancellationDataList.add(cancellationData);

        cancellationData = new CancellationRules(Constants.TaskStatus.ACCEPTED.value,
                StorefrontCommonData.getTerminology().getMerchant(),
                StorefrontCommonData.getTerminology().getOrder(),
                StorefrontCommonData.getTerminology().getDispatched(),
                50, 200,
                5942);
        cancellationDataList.add(cancellationData);

        cancellationData = new CancellationRules(Constants.TaskStatus.ACCEPTED.value,
                StorefrontCommonData.getTerminology().getMerchant(),
                StorefrontCommonData.getTerminology().getOrder(),
                StorefrontCommonData.getTerminology().getDispatched(),
                50, 200,
                4454);

        cancellationDataList.add(cancellationData);

        cancellationData = new CancellationRules(Constants.TaskStatus.DISPATCHED.value,
                StorefrontCommonData.getTerminology().getMerchant(),
                StorefrontCommonData.getTerminology().getOrder(),
                StorefrontCommonData.getTerminology().getDispatched(),
                50, 200);

        cancellationDataList.add(cancellationData);

        return cancellationDataList;


    }

    private void getCancellationPolicy() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());

        if (jobId != 0)
            builder.add("job_id", jobId);

        if (storeId != 0)
            builder.add("store_id", storeId);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            builder.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            builder.add("language", "en");
        }

        RestClient.getApiInterface(this).getCancellationPolicy(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        CancellationData data = baseModel.toResponseModel(CancellationData.class);
                        setAdapter(data.getCancellationRules());
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });


    }

}
