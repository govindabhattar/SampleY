package com.tookancustomer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tookancustomer.adapters.NotificationAdapter;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.NotificationsModelResponse.Data;
import com.tookancustomer.models.NotificationsModelResponse.Datum;
import com.tookancustomer.models.NotificationsModelResponse.NotificationsModelResponse;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlBack, rlHeaderTextOption;
    private TextView tvHeading, tvHeaderOption;
    private ImageView ivHeaderImageOption;
    private TextView tvNoData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvAllNotifications;
    private ArrayList<Datum> dataList = new ArrayList<>();
    private NotificationAdapter adapter;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mActivity = this;
        initViews();
        getAllNotifications(true);
    }

    private void initViews() {
        rlBack = findViewById(R.id.rlBack);
        rlHeaderTextOption = findViewById(R.id.rlHeaderTextOption);
        tvHeaderOption = findViewById(R.id.tvHeaderOption);
        tvHeaderOption.setText(getStrings(R.string.clear_all));
        ivHeaderImageOption = findViewById(R.id.ivHeaderImageOption);
        ivHeaderImageOption.setVisibility(View.GONE);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(StorefrontCommonData.getTerminology().getNotifications(true));
        tvNoData = findViewById(R.id.tvNoData);
        tvNoData.setText(getStrings(R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getNotifications(false)));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rvAllNotifications = findViewById(R.id.rvAllNotifications);
        rvAllNotifications.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startShimmerAnimation(shimmerFrameLayout);
                getAllNotifications(false);
            }
        });

        tvHeaderOption.setVisibility(View.GONE);

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        startShimmerAnimation(shimmerFrameLayout);

        Utils.setOnClickListener(this, rlBack, rlHeaderTextOption);
    }

    @Override
    public void onBackPressed() {
//        if (tvHeaderOption.getVisibility() == View.VISIBLE) {
//            tvHeaderOption.setVisibility(View.GONE);
//            ivHeaderImageOption.setVisibility(View.VISIBLE);
//        } else {
        finish();
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlBack) {
            onBackPressed();
        } else if (v.getId() == R.id.rlHeaderTextOption) {
//            if (ivHeaderImageOption.getVisibility() == View.VISIBLE) {
//                ivHeaderImageOption.setVisibility(View.GONE);
//                tvHeaderOption.setVisibility(View.VISIBLE);
//            } else
            if (tvHeaderOption.getVisibility() == View.VISIBLE) {
                clearAllNotifications();
            }
        }
    }

    private void clearAllNotifications() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(this).updateAppNotifications(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(mActivity, getStrings(R.string.notifications_has_been_cleared).replace(NOTIFICATION, StorefrontCommonData.getTerminology().getNotifications(true)));
                dataList.clear();
                setupAdapter();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    private void getAllNotifications(boolean showLoading) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("read_all", 1);

        RestClient.getApiInterface(this).getAppNotifications(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                dataList.clear();
                NotificationsModelResponse notificationsModelResponse = new NotificationsModelResponse();
                try {
                    notificationsModelResponse.setData(baseModel.toResponseModel(Data.class));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }

                dataList.addAll(notificationsModelResponse.getData().getNotifications());

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                stopShimmerAnimation(shimmerFrameLayout);
                setupAdapter();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                dataList.clear();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                stopShimmerAnimation(shimmerFrameLayout);
                setupAdapter();
            }
        });
    }

    private void setupAdapter() {
        if (dataList.size() > 0) {
            tvNoData.setVisibility(View.GONE);
//            ivHeaderImageOption.setVisibility(View.VISIBLE);
            tvHeaderOption.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.VISIBLE);
//            ivHeaderImageOption.setVisibility(View.GONE);
            tvHeaderOption.setVisibility(View.GONE);
        }
        adapter = new NotificationAdapter(mActivity, dataList);
        rvAllNotifications.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("refresh"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            getAllNotifications(false);
        }
    };

}