package com.tookancustomer.filter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.filter.adapter.FilterRecyclerAdapter;
import com.tookancustomer.filter.adapter.OptionsRecyclerAdater;
import com.tookancustomer.filter.constants.FilterConstants;
import com.tookancustomer.filter.model.AllowedDataList;
import com.tookancustomer.filter.model.Data;
import com.tookancustomer.filter.model.Result;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class FilterActivity extends BaseActivity implements FilterRecyclerAdapter.FilterSelectionListener, View.OnClickListener {

    private RecyclerView recyclerFilter, recyclerOptions;
    private FilterRecyclerAdapter filterAdapter;
    private OptionsRecyclerAdater optionsAdapter;

    private Button btnApply;

    private ImageView ivClose;
    private RelativeLayout rlClose, rlOutside;
    private TextView tvClearAll, tvDisplayName;

    private int businessCategoryId /*= 16*/;
    private ArrayList<Result> allfilterDataList;
    private ArrayList<Result> categoryfilterDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        init();
        setData();

        if (businessCategoryId != 0) {
            if (categoryfilterDataList != null) {
                setFilterAdapter(generateAdapterList());
            } else {
                callbackForFilters();
            }

        } else {
            setFilterAdapter(generateAdapterList());
        }

    }

    private ArrayList<Result> generateAdapterList() {
        ArrayList<Result> filterList = new ArrayList<>();

        if (categoryfilterDataList != null && categoryfilterDataList.size() > 0)
            filterList.addAll(categoryfilterDataList);

        filterList.addAll(allfilterDataList);
        /**
         * whenever filter screen is opened by default first filter is highlighted
         */
        for (Result result : filterList) {
            result.setSelected(false);
        }

        if (filterList.size() > 0) filterList.get(0).setSelected(true);

        return filterList;
    }

    private void setData() {
        tvDisplayName.setText(getStrings(R.string.filter));
        tvClearAll.setText(getStrings(R.string.clear_all));
        btnApply.setText(getStrings(R.string.apply));

        businessCategoryId = getIntent().getIntExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ID, 0);
        allfilterDataList = getIntent().getParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ALL);

        if (getIntent().hasExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY)) {
            categoryfilterDataList = getIntent().getParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        AnimationUtils.BottonToTopTransition(FilterActivity.this);
    }

    private void init() {
        recyclerFilter = findViewById(R.id.recyclerFilter);
        recyclerOptions = findViewById(R.id.recyclerOptions);

//        recyclerFilter.addItemDecoration(new SimpleDividerItemDecorationFull(this));
//        recyclerOptions.addItemDecoration(new SimpleDividerItemDecorationFull(this));

        btnApply = findViewById(R.id.btnApply);

        ivClose = findViewById(R.id.ivClose);
        rlClose = findViewById(R.id.rlClose);
        rlOutside = findViewById(R.id.rlOutside);
        tvClearAll = findViewById(R.id.tvClearAll);
        tvDisplayName = findViewById(R.id.tvDisplayName);

        recyclerFilter.setLayoutManager(new LinearLayoutManager(this));
        recyclerOptions.setLayoutManager(new LinearLayoutManager(this));

        Utils.setOnClickListener(this, btnApply, ivClose, tvClearAll, rlClose, rlOutside);
    }


    private void callbackForFilters() {

        CommonParams.Builder builder = new CommonParams.Builder()
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(BUSINESS_CATEGORY_ID, businessCategoryId/*16*/);


        RestClient.getApiInterface(this).getFilters(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Data data = baseModel.toResponseModel(Data.class);
                        if (data.getResult().size() > 0)
                            categoryfilterDataList = data.getResult();
                        setFilterAdapter(generateAdapterList());
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }

    private void setFilterAdapter(ArrayList<Result> filterList) {
        if (filterList.isEmpty()) {
            Utils.snackBar(FilterActivity.this, getStrings(R.string.no_filters_available));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        onBackPressed();
                    } catch (Exception e) {
                        Utils.printStackTrace(e);
                    }

                }
            }, 1000);
        } else {
            for (int i = 0; i < filterList.size(); i++) {
                Result filterData = filterList.get(i);
                if (filterData.getAllowedDataList() == null) {
                    ArrayList<AllowedDataList> allowedDataList = new ArrayList<>();
                    for (int j = 0; j < filterData.getAllowedValues().size(); j++) {
                        allowedDataList.add(new AllowedDataList(filterData.getAllowedValues().get(j), false));
                    }

                    filterData.setAllowedDataList(allowedDataList);
                }
            }

            filterAdapter = new FilterRecyclerAdapter(filterList, this);
            recyclerFilter.setAdapter(filterAdapter);

            optionsAdapter = new OptionsRecyclerAdater(filterList, filterAdapter);
            recyclerOptions.setAdapter(optionsAdapter);
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btnApply:
                intent.putExtra(FilterConstants.EXTRA_ACTION, FilterConstants.ACTION_APPLY);
                intent.putParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ALL, allfilterDataList);
                intent.putParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY, categoryfilterDataList);
                setResult(RESULT_OK, intent);
                finish();
                AnimationUtils.BottonToTopTransition(FilterActivity.this);
                break;

            case R.id.ivClose:
            case R.id.rlClose:
            case R.id.rlOutside:
                onBackPressed();
                break;
            case R.id.tvClearAll:
                clearFilter();
                filterAdapter.notifyDataSetChanged();
                optionsAdapter.notifyDataSetChanged();
                /*intent.putExtra(FilterConstants.EXTRA_ACTION, FilterConstants.ACTION_CLEAR_ALL);
                intent.putParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ALL, allfilterDataList);
                intent.putParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY, categoryfilterDataList);
                setResult(RESULT_OK, intent);
                finish()*/
                ;
                break;
        }
    }

    private void clearFilter() {
        if (filterAdapter != null && filterAdapter.getFilterList() != null) {
            for (int i = 0; i < filterAdapter.getFilterList().size(); i++) {
                Result filterData = filterAdapter.getFilterList().get(i);
                if (filterData.getAllowedDataList() != null) {
                    for (int j = 0; j < filterData.getAllowedDataList().size(); j++) {
                        filterData.getAllowedDataList().get(j).setSelected(false);
                    }
                }
            }
        }


        if (allfilterDataList != null) {
            for (int i = 0; i < allfilterDataList.size(); i++) {
                Result filterData = allfilterDataList.get(i);
                if (filterData.getAllowedDataList() != null) {
                    for (int j = 0; j < filterData.getAllowedDataList().size(); j++) {
                        filterData.getAllowedDataList().get(j).setSelected(false);
                    }
                }
            }
        }
    }


    @Override
    public void onFilterSelected(Result filter, int filterPos) {
        if (optionsAdapter != null) {
            optionsAdapter.setFilterPosition(filterPos);
        }
    }

}
