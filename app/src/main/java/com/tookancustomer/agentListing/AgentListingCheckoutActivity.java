package com.tookancustomer.agentListing;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by utkarsh  on 13/11/19.
 */

public class AgentListingCheckoutActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView agentListRV;
    private RelativeLayout rlBack;
    private TextView tvHeading, tvNoResultFound;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerLayout;
    private double latitude, longitude;
    private Datum productDataItem;
    private int itemPos;
    private String startDate;
    private boolean isSERVICE_AS_PRODUCT;
    private int[] selectedQuantity;
    private ArrayList<AgentData> agentDataArrayList;
    private AgentListAdapter mAgentAdapter;
    JSONArray selectedProductItemArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_listing);
        mActivity = this;
        getIntentData();
        initUI();
        initislizeData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (getIntent().hasExtra(PICKUP_LATITUDE) && getIntent().hasExtra(PICKUP_LONGITUDE)) {
            latitude = bundle.getDouble(PICKUP_LATITUDE);
            longitude = bundle.getDouble(PICKUP_LONGITUDE);
        }


        ArrayList<Datum> data = Dependencies.getSelectedProductsArrayList();

        selectedProductItemArray = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            selectedProductItemArray.put(data.get(i).getProductId());
        }

    }

    private void initislizeData() {
        startShimmerAnimation(shimmerLayout);
        getAgentList();
    }

    private void getAgentList() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
//        commonParams.add(PRODUCT_ID, selectedProductItemArray);
        commonParams.add(USER_ID, Dependencies.getSelectedProductsArrayList().get(0).getUserId());
        commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        commonParams.add("latitude", latitude);
        commonParams.add("longitude", longitude);
        commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        commonParams.add("product_ids", selectedProductItemArray.toString());

        Dependencies.addCommonParameters(commonParams, this, StorefrontCommonData.getUserData());


        RestClient.getApiInterface(mActivity).getAgents(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        agentListRV.setVisibility(View.VISIBLE);
                        shimmerLayout.setVisibility(View.GONE);
                        if (agentDataArrayList != null)
                            agentDataArrayList.clear();
                        agentDataArrayList = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                new TypeToken<ArrayList<AgentData>>() {
                                }.getType());
                        mAgentAdapter = new AgentListAdapter(mActivity, agentDataArrayList);
                        agentListRV.setAdapter(mAgentAdapter);


                        if (agentDataArrayList.size() > 0) {
                            agentListRV.setVisibility(View.VISIBLE);
                            tvNoResultFound.setVisibility(View.GONE);
                        } else {
                            agentListRV.setVisibility(View.GONE);
                            tvNoResultFound.setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }

    private void initUI() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.agent_list).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
        agentListRV = findViewById(R.id.agentListRV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        tvNoResultFound = findViewById(R.id.tvNoResultFound);
        tvNoResultFound.setVisibility(View.GONE);
        tvNoResultFound.setText(StorefrontCommonData.getString(this, R.string.no_agent_found).replace(AGENT, StorefrontCommonData.getTerminology().getAgent()));
        mLayoutManager = new LinearLayoutManager(mActivity);
        agentListRV.setItemAnimator(new DefaultItemAnimator());
        agentListRV.setLayoutManager(mLayoutManager);

        Utils.setOnClickListener(this, rlBack);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAgentList();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rlBack:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void startShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
    }

    public void stopShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.GONE);
        shimmerLayout.stopShimmerAnimation();
    }


    public void onAgentSelected(int agentID, AgentData agentData) {

        addToCart(agentID, agentData);


    }


    private void addToCart(int agentID, AgentData agentData) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
        returnIntent.putExtra("agentData", agentData);
        returnIntent.putExtra(AGENT_ID, agentID);
        setResult(RESULT_OK, returnIntent);
        onBackPressed();
    }


}
