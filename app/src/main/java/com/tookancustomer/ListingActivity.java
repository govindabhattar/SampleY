package com.tookancustomer;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.adapters.ListingAdapter;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.interfaces.ListingInterface;
import com.tookancustomer.models.BaseModel;



import com.tookancustomer.models.listingdata.Datum;
import com.tookancustomer.models.listingdata.ListingData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by neerajwadhwani on 26/07/18.
 */

public class ListingActivity extends BaseActivity implements ListingInterface,View.OnClickListener {

    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private RecyclerView rvListing;
    private ArrayList<Datum> dataList;
    private ListingAdapter listAdapter;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private TextView tvNoList;
    private Button btnProductOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        init();

        apiForListing();
    }

    private void apiForListing() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add(ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getAppAccessToken());
        commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getFormSettings().getUserId());
        commonParams.add(USER_TYPE, "3");
        commonParams.add(USER_ID, StorefrontCommonData.getFormSettings().getUserId());
        commonParams.add(FORM_ID, StorefrontCommonData.getFormSettings().getFormId());
        commonParams.add(APP_ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getAppAccessToken());
        commonParams.add(OFFSET, 0);
        commonParams.add(LIMIT, 30);


        RestClient.getApiInterface(this).getCategoryListing(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                ListingData data = new ListingData();
                try {
                    Datum[] datum = baseModel.toResponseModel(Datum[].class);
                    data.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }

                dataList.addAll(data.getData());
                if (dataList.size() > 0) {
                    rvListing.setVisibility(View.VISIBLE);
                    btnProductOnly.setVisibility(View.GONE);
                    setupAdapter();
                } else {
                    rvListing.setVisibility(View.GONE);
                    btnProductOnly.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });

    }

    private void setupAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listAdapter = new ListingAdapter(dataList, this);
        rvListing.setLayoutManager(manager);
        rvListing.setAdapter(listAdapter);
    }

    private void init() {
        dataList = new ArrayList<>();
        rvListing = findViewById(R.id.rvListing);
        rlBack = findViewById(R.id.rlBack);
        rvListing = findViewById(R.id.rvListing);
        tvHeading = findViewById(R.id.tvHeading);
        btnProductOnly = findViewById(R.id.btnProductOnly);
        btnProductOnly.setText(getStrings(R.string.add_product_text)+" "+ StorefrontCommonData.getTerminology().getProduct());
        tvNoList = findViewById(R.id.tvNoList);
        Utils.setOnClickListener(this,btnProductOnly,rlBack);
        tvHeading.setText(getStrings(R.string.Listing));
    }

    @Override
    public void showNoListText() {
        rvListing.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnProductOnly:
                Transition.startActivity(ListingActivity.this, AddProductActivity.class, null, true);
                break;
            case R.id.rlBack:
                finish();
            break;
        }
    }
}
