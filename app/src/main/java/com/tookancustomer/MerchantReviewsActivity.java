package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.adapters.MerchantReviewsAdapter;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.MarketplaceStorefrontModel.LastReviewRating;
import com.tookancustomer.models.MarketplaceStorefrontModel.MerchantAllReviewsModel;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class MerchantReviewsActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout rlUnavailNet;
    private TextView tvRetry, tvNoReviews;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private TextView tvAverageRating, tvTotalRatingsReviews;
    private LinearLayout llAverageRatingLayout;
    private RecyclerView rvReviewsList;
    private Button btnWriteReview;
    private Datum storefrontData;
    private com.tookancustomer.models.ProductCatalogueData.Datum productData;
    private boolean isFromOwnerProfile = false;

    private MerchantReviewsAdapter merchantReviewsAdapter;
    private ArrayList<LastReviewRating> merchantReviewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_reviews);
        mActivity = this;
        if (getIntent() != null) {
            storefrontData = (Datum) getIntent().getExtras().getSerializable(STOREFRONT_DATA);
            productData = (com.tookancustomer.models.ProductCatalogueData.Datum) getIntent().getExtras().getSerializable(PRODUCT_DATA);
            isFromOwnerProfile = getIntent().getExtras().getBoolean(IS_FROM_OWNER_PROFILE);
        }
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    public void initViews() {
        rlUnavailNet = findViewById(R.id.rlUnavailNet);
        rlUnavailNet.setVisibility(View.GONE);
        tvRetry = findViewById(R.id.tvRetry);
        tvRetry.setText(getStrings(R.string.retry_underlined));
        ((TextView) findViewById(R.id.tvNoNetConnect)).setText(getStrings(R.string.no_internet_connection));
        tvNoReviews = findViewById(R.id.tvNoReviews);
        tvNoReviews.setText(getStrings(R.string.no_rate_reviews_be_first_one));
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        llAverageRatingLayout = findViewById(R.id.llAverageRatingLayout);
        tvTotalRatingsReviews = findViewById(R.id.tvTotalRatingsReviews);
        rvReviewsList = findViewById(R.id.rvReviewsList);
        rvReviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
        merchantReviewsAdapter = new MerchantReviewsAdapter(mActivity, merchantReviewsList);
        rvReviewsList.setAdapter(merchantReviewsAdapter);
        btnWriteReview = findViewById(R.id.btnWriteReview);
        btnWriteReview.setText(getStrings(R.string.write_a_review));

        Utils.setOnClickListener(this, rlBack, btnWriteReview, tvRetry);
    }

    public void setData() {
        if (storefrontData != null) {
            tvHeading.setText(storefrontData.getStoreName());
            tvAverageRating.setText(storefrontData.getStoreRating().floatValue() + "");

            Utils.addStarsToLayout(mActivity, llAverageRatingLayout, storefrontData.getStoreRating().doubleValue(), R.dimen._17dp);

            if (storefrontData.getTotalRatingsCount() > 0 && storefrontData.getTotalReviewCount() > 0) {
                tvTotalRatingsReviews.setText("( " + storefrontData.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text)
                        + " " + getStrings(R.string.and) + " "
                        + storefrontData.getTotalReviewCount() + " " + getStrings(R.string.reviews_text) + " )");
            } else if (storefrontData.getTotalRatingsCount() > 0) {
                tvTotalRatingsReviews.setText("( " + storefrontData.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text) + " )");
            } else if (storefrontData.getTotalReviewCount() > 0) {
                tvTotalRatingsReviews.setText("( " + storefrontData.getTotalReviewCount() + " " + getStrings(R.string.reviews_text) + " )");
            } else {
                tvTotalRatingsReviews.setText("( " + getStrings(R.string.no_rate_reviews_be_first_one) + " )");
            }

//            merchantReviewsList.clear();
//            merchantReviewsList.addAll(storefrontData.getLastReviewRating());
//            merchantReviewsAdapter.notifyDataSetChanged();
            getAllReviewsRatings();

            if (storefrontData.getMyRating().intValue() > 0) {
                btnWriteReview.setVisibility(View.GONE);
            } else {
                if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty() && !isFromOwnerProfile) {
                    btnWriteReview.setVisibility(View.VISIBLE);
                } else {
                    btnWriteReview.setVisibility(View.GONE);
                }
            }
        } else if (productData != null) {
            tvHeading.setText(productData.getName());
            tvAverageRating.setText(productData.getProductRating().floatValue() + "");

            Utils.addStarsToLayout(mActivity, llAverageRatingLayout, productData.getProductRating().doubleValue(), R.dimen._17dp);

            if (productData.getTotalRatingsCount() > 0 && productData.getTotalReviewCount() > 0) {
                tvTotalRatingsReviews.setText("( " + productData.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text)
                        + " " + getStrings(R.string.and) + " "
                        + productData.getTotalReviewCount() + " " + getStrings(R.string.reviews_text) + " )");
            } else if (productData.getTotalRatingsCount() > 0) {
                tvTotalRatingsReviews.setText("( " + productData.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text) + " )");
            } else if (productData.getTotalReviewCount() > 0) {
                tvTotalRatingsReviews.setText("( " + productData.getTotalReviewCount() + " " + getStrings(R.string.reviews_text) + " )");
            } else {
                tvTotalRatingsReviews.setText("( " + getStrings(R.string.no_rate_reviews_be_first_one) + " )");
            }

//            merchantReviewsList.clear();
//            merchantReviewsList.addAll(productData.getLastReviewRating());
//            merchantReviewsAdapter.notifyDataSetChanged();
            getAllReviewsRatings();

            if (productData.getMyRating().intValue() > 0) {
                btnWriteReview.setVisibility(View.GONE);
            } else {
                if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                    btnWriteReview.setVisibility(View.VISIBLE);
                } else {
                    btnWriteReview.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
            case R.id.tvRetry:
                getAllReviewsRatings();
                break;
            case R.id.btnWriteReview:
                Intent intent = new Intent(mActivity, MerchantAddRateReviewActivity.class);
                intent.putExtra(SHOW_RATE_STARS, 5f);
                if (storefrontData != null) {
                    intent.putExtra(STOREFRONT_DATA, storefrontData);
                } else {
                    intent.putExtra(PRODUCT_DATA, productData);
                }
                startActivityForResult(intent, OPEN_ADD_MERCHANT_RATE_REVIEW);
                break;
        }
    }

    public void getAllReviewsRatings() {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        if (storefrontData != null) {
            if (isFromOwnerProfile) {
                commonParams.add(USER_ID, storefrontData.getStorefrontUserId());
            }
            RestClient.getApiInterface(mActivity).getStoreAllReviews(commonParams.build().getMap()).enqueue(getResponseResolver());
        } else {
            commonParams.add("product_id", productData.getProductId());
            RestClient.getApiInterface(mActivity).getProductReviews(commonParams.build().getMap()).enqueue(getResponseResolver());
        }
    }

    public ResponseResolver<BaseModel> getResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                rlUnavailNet.setVisibility(View.GONE);

                MerchantAllReviewsModel merchantAllReviewsModel = new MerchantAllReviewsModel();
                try {
                    LastReviewRating[] datum = baseModel.toResponseModel(LastReviewRating[].class);
                    merchantAllReviewsModel.setData(new ArrayList<LastReviewRating>(Arrays.asList(datum)));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                merchantReviewsList.clear();
                merchantReviewsList.addAll(merchantAllReviewsModel.getData());
                merchantReviewsAdapter.notifyDataSetChanged();

                if (merchantAllReviewsModel.getData().size() == 0) {
                    tvNoReviews.setVisibility(View.VISIBLE);
                } else {
                    tvNoReviews.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                rlUnavailNet.setVisibility(View.VISIBLE);
                merchantReviewsList.clear();
                merchantReviewsAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_ADD_MERCHANT_RATE_REVIEW:
                if (resultCode == RESULT_OK) {
                    if (storefrontData != null) {
                        storefrontData = (Datum) data.getExtras().getSerializable(STOREFRONT_DATA);
                    } else {
                        productData = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getExtras().getSerializable(PRODUCT_DATA);
                    }
                    setData();
                    getAllReviewsRatings();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Bundle bundleExtra = new Bundle();
        if (storefrontData != null) {
            bundleExtra.putSerializable(STOREFRONT_DATA, storefrontData);
        } else {
            bundleExtra.putSerializable(PRODUCT_DATA, productData);
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtras(bundleExtra);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}