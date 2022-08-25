package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CreateStorefrontReviewModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Data;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CustomRatingBar;
import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.SHOW_RATE_STARS;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;

public class MerchantAddRateReviewActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlBack;
    private TextView tvHeading;
    private ScrollView scrollView;
    private CustomRatingBar rbRate;
    private EditText etFeedback;
    private Button btnSubmit;
    private TextView tvCharCount;

    private Datum storefrontData;
    private com.tookancustomer.models.ProductCatalogueData.Datum productData;
    private Float showRateStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_add_rate_review);
        mActivity = this;
        if (getIntent() != null) {
            storefrontData = (Datum) getIntent().getExtras().getSerializable(STOREFRONT_DATA);
            productData = (com.tookancustomer.models.ProductCatalogueData.Datum) getIntent().getExtras().getSerializable(PRODUCT_DATA);
            showRateStars = getIntent().getExtras().getFloat(SHOW_RATE_STARS, 5f);
//            showRateStars = 0f;
        }
        initViews();

    }


    public void initViews() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.write_a_review));
        scrollView = findViewById(R.id.scrollView);
        rbRate = findViewById(R.id.rbRate);
        rbRate.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {

            }
        });
        etFeedback = findViewById(R.id.etFeedback);
        etFeedback.setHint(getStrings(R.string.tell_us_about_your_experience));
        etFeedback.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etFeedback.setImeActionLabel(getStrings(R.string.done), EditorInfo.IME_ACTION_DONE);
        etFeedback.setRawInputType(InputType.TYPE_CLASS_TEXT);

        etFeedback.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                Utils.hideSoftKeyboard(mActivity);
                return false;
            }
        });

        etFeedback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCharCount.setText((250 - s.toString().length()) + "");
            }
        });

        tvCharCount = findViewById(R.id.tvCharCount);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setText(getStrings(R.string.submit));
        if (storefrontData != null && storefrontData.getMyRating().intValue() > 0) {
            rbRate.setScore(storefrontData.getMyRating().floatValue());
            etFeedback.setText(storefrontData.getMyReview());
            etFeedback.setSelection(etFeedback.getText().toString().length());
        } else if (productData != null && productData.getMyRating().intValue() > 0) {
            rbRate.setScore(productData.getMyRating().floatValue());
            etFeedback.setText(productData.getMyReview());
            etFeedback.setSelection(etFeedback.getText().toString().length());
        } else {
            rbRate.setScore(showRateStars);
            etFeedback.setText("");
        }

        Utils.setOnClickListener(this, rlBack, btnSubmit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                finish();
                break;

            case R.id.btnSubmit:
                submitRate();
                break;
        }
    }

    public void submitRate() {
        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
            return;
        }

        if ((int) rbRate.getScore() <= 0) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.please_rate_before_proceeding)).build().show();
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("review", etFeedback.getText().toString().trim());
        commonParams.add("rating", (int) rbRate.getScore());

        if (storefrontData != null) {
            RestClient.getApiInterface(mActivity).createStorefrontReview(commonParams.build().getMap()).enqueue(getResponseResolver());
        } else {
            commonParams.add("product_id", productData.getProductId());
            RestClient.getApiInterface(mActivity).createProductReview(commonParams.build().getMap()).enqueue(getResponseResolver());
        }
    }

    public ResponseResolver getResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                CreateStorefrontReviewModel createStorefrontReviewModel = new CreateStorefrontReviewModel();
                try {
                    createStorefrontReviewModel.setData(baseModel.toResponseModel(Data.class));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                Bundle bundleExtra = new Bundle();

                if (storefrontData != null) {
                    storefrontData.setMyRating((int) rbRate.getScore());
                    storefrontData.setMyReview(etFeedback.getText().toString().trim());
                    storefrontData.setStoreRating(createStorefrontReviewModel.getData().getStoreRating());
                    storefrontData.setLastReviewRating(createStorefrontReviewModel.getData().getLastReviewRating());
                    storefrontData.setTotalRatingsCount(createStorefrontReviewModel.getData().getTotalRatingsCount());
                    storefrontData.setTotalReviewCount(createStorefrontReviewModel.getData().getTotalReviewCount());
                    bundleExtra.putSerializable(STOREFRONT_DATA, storefrontData);
                } else {
                    productData.setMyRating((int) rbRate.getScore());
                    productData.setMyReview(etFeedback.getText().toString().trim());
                    productData.setProductRating(createStorefrontReviewModel.getData().getStoreRating());
                    productData.setLastReviewRating(createStorefrontReviewModel.getData().getLastReviewRating());
                    productData.setTotalRatingsCount(createStorefrontReviewModel.getData().getTotalRatingsCount());
                    productData.setTotalReviewCount(createStorefrontReviewModel.getData().getTotalReviewCount());
                    bundleExtra.putSerializable(PRODUCT_DATA, productData);
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtras(bundleExtra);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        };
    }
}