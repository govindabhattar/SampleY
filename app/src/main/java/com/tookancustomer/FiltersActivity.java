package com.tookancustomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customviews.CustomFieldCheckListFilter;
import com.tookancustomer.customviews.customRangeBar.interfaces.OnRangeSeekbarChangeListener;
import com.tookancustomer.customviews.customRangeBar.widgets.CrystalRangeSeekbar;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductFilters.Data;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.Extras.FILTER_MAX_PRICE;
import static com.tookancustomer.appdata.Keys.Extras.FILTER_MIN_PRICE;


public class FiltersActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_FILTER_DATA = "extra_filter_data";
    private ImageView ivBack;
    private TextView tvHeading, tvHeaderOption;
    private Button btnApply;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMinRange, tvMaxRange, tvPrice;
    private LinearLayout llCustomFields;
    private CustomFieldCheckListFilter customFieldCheckListFilter;
    private int minPrice, maxPrice;
    private Data filterData;
    private ScrollView svFilter;

    /**
     * @param mContext
     * @param filterData
     * @return
     */
    public static Intent createIntent(final Context mContext, final Data filterData, final int minPrice, final int maxPrice) {
        Intent intent = new Intent(mContext, FiltersActivity.class);
        intent.putExtra(EXTRA_FILTER_DATA, filterData);
        intent.putExtra(FILTER_MIN_PRICE, minPrice);
        intent.putExtra(FILTER_MAX_PRICE, maxPrice);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        mActivity = this;
        filterData = (Data) getIntent().getSerializableExtra(EXTRA_FILTER_DATA);
        minPrice = getIntent().getIntExtra(FILTER_MIN_PRICE, 0);
        maxPrice = getIntent().getIntExtra(FILTER_MAX_PRICE, 0);
        initViews();
        setData();

    }

    /**
     * setPrefilled data
     */
//    private void setPrefilledData() {
//        tvPrice.setText("$" + minPrice + " - " + "$" + maxPrice + "");
//        rangeSeekbar.setMinStartValue(minPrice).setMaxStartValue(maxPrice).apply();
//
//
//    }


    /**
     * init views
     */
    private void initViews() {
        ivBack = findViewById(R.id.ivBack);
        ivBack.setImageResource(R.drawable.ic_close);
        tvHeaderOption = findViewById(R.id.tvHeaderOption);
        tvHeaderOption.setText(getStrings(R.string.clear));
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.filters));
        btnApply = findViewById(R.id.btnApply);
        btnApply.setText(getStrings(R.string.apply));
        ((TextView) findViewById(R.id.tvPriceRange)).setText(getStrings(R.string.price_range));

        tvMinRange = findViewById(R.id.tvMinRange);
        tvMaxRange = findViewById(R.id.tvMaxRange);
        tvPrice = findViewById(R.id.tvPrice);

        llCustomFields = findViewById(R.id.llCustomFields);

        svFilter = findViewById(R.id.svFilter);
//        svFilter.scrollTo(0, 0);
        svFilter.post(new Runnable() {
            public void run() {
                svFilter.smoothScrollTo(0, 0);
            }
        });

//        svFilter.scr(ScrollView.FOCUS_UP);


        rangeSeekbar = findViewById(R.id.rangeSeekbar1);
//        rangeSeekbar.setBarHighlightColor(getResources().getColor(R.color.colorAccent));

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                    tvPrice.setText(Utils.getCurrencySymbol() + minValue + " - " + Utils.getCurrencySymbol() + maxValue + " " + getStrings(R.string.per) + " " + getStrings(R.string.night));

                } else {
                    tvPrice.setText(Utils.getCurrencySymbol() + minValue + " - " + Utils.getCurrencySymbol() + maxValue + "");
                }
//                tvPrice.setText(Utils.getCurrencySymbol() + minValue + " - " + Utils.getCurrencySymbol() + maxValue + " " + getStrings(R.string.per) + " " + getStrings(R.string.night));
//                rangeSeekbar.setMinStartValue((float) minValue).setMaxStartValue((float) maxValue).apply();

                if (minValue.intValue() != 0 && maxValue.intValue() != 0) {
                    minPrice = minValue.intValue();
                    maxPrice = maxValue.intValue();
                }
            }
        });

//        // set final value listener
//        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
//            @Override
//            public void finalValue(Number minValue, Number maxValue) {
//                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
//                minPrice = minValue.intValue();
//                maxPrice = maxValue.intValue();
//
//            }
//        });

        Utils.setOnClickListener(this, tvHeaderOption, btnApply);
        findViewById(R.id.rlBack).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
            case R.id.btnApply:
                onBackPressed();
                break;

            case R.id.tvHeaderOption:
                minPrice = filterData.getMinPrice();
                maxPrice = filterData.getMaxPrice();
                rangeSeekbar.setMinStartValue(filterData.getMinPrice()).setMaxStartValue(filterData.getMaxPrice()).apply();

//                if (customFieldCheckListFilter != null) {
//                    customFieldCheckListFilter.clearFilters();
//                }

                getFilters();
                break;
        }
    }


    /**
     * get filters
     */
    private void getFilters() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }

        if (StorefrontCommonData.getUserData() != null && StorefrontCommonData.getUserData().getData() != null && StorefrontCommonData.getUserData().getData().getVendorDetails() != null) {
            commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId());
        }

        Dependencies.addCommonParameters(commonParams, mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(mActivity).getProductFilters(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                try {
                    filterData = baseModel.toResponseModel(com.tookancustomer.models.ProductFilters.Data.class);
                    for (int i = 0; i < filterData.getFilterAndValues().size(); i++) {
                        filterData.getFilterAndValues().get(i).setAllowedValuesWithIsSelected();
                    }

                    setData();
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
//                llfilter.setVisibility(View.GONE);
//                rvFilterList.setVisibility(View.GONE);
            }
        });

    }


    /**
     * setData
     */
    private void setData() {
        if (filterData != null) {
            tvMinRange.setText(String.valueOf(filterData.getMinPrice()));
            tvMaxRange.setText(String.valueOf(filterData.getMaxPrice()));

            rangeSeekbar.setMinValue(filterData.getMinPrice());
            rangeSeekbar.setMaxValue(filterData.getMaxPrice());

            if (minPrice == 0 && maxPrice == 0) {
                minPrice = filterData.getMinPrice();
                maxPrice = filterData.getMaxPrice();
                rangeSeekbar.setMinStartValue(filterData.getMinPrice()).setMaxStartValue(filterData.getMaxPrice()).apply();
            } else {
                rangeSeekbar.setMinStartValue(minPrice).setMaxStartValue(maxPrice).apply();
            }

//            tvPrice.setText("$" + rangeSeekbar.getSelectedMinValue() + " - " + "$" + rangeSeekbar.getSelectedMaxValue() + "");

            renderCustomFields();
        }

    }


    /**
     * render custom fields
     */
    private void renderCustomFields() {
        llCustomFields.removeAllViews();
        if (filterData.getFilterAndValues() == null) {
            return;
        }
        View view = null;
        for (int i = 0; i < filterData.getFilterAndValues().size(); i++) {
            customFieldCheckListFilter = new CustomFieldCheckListFilter(this);
            view = customFieldCheckListFilter.render(filterData.getFilterAndValues().get(i));
            if (view != null) {
                llCustomFields.addView(view);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_FILTER_DATA, filterData);
        intent.putExtra(FILTER_MIN_PRICE, minPrice);
        intent.putExtra(FILTER_MAX_PRICE, maxPrice);
        setResult(RESULT_OK, intent);
        finish();
    }
}
