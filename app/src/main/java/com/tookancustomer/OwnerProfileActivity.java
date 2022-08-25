package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.transition.Transition;
import com.hippo.HippoConfig;
import com.tookancustomer.adapters.MerchantReviewsAdapter;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.MarketplaceStorefrontModel.LastReviewRating;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;


public class OwnerProfileActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlBack;
    private TextView tvHeading, tvOwnerName, tvOwnerEmail, tvSeeAllReviews;

    private ImageView ivAvtar;
    private ProgressBar progressBarAvtar;

    private LinearLayout llShowCustomerRatings, llRateReviewTopLayout;
    private Button btnChat, btnCall;
    private Datum datum;
    private RecyclerView rvReviewsList;
    private MerchantReviewsAdapter merchantReviewsAdapter;
    private ArrayList<LastReviewRating> merchantReviewsList = new ArrayList<>();
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);
        mActivity = this;
        userId = getIntent().getIntExtra(USER_ID, 0);

        initializeData();
        getOwnerProfileData();

    }

    private void initializeData() {
        ((TextView) findViewById(R.id.tvRatingsReviewsHeading)).setText(getStrings(R.string.reviews_ratings));
        ((TextView) findViewById(R.id.tvSeeAllReviews)).setText(getStrings(R.string.see_all_reviews));
        ((Button) findViewById(R.id.btnChat)).setText(getStrings(R.string.chat));
        ((Button) findViewById(R.id.btnCall)).setText(getStrings(R.string.call));

        rlBack = findViewById(R.id.rlBack);

        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.owner_profile));
        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvOwnerEmail = findViewById(R.id.tvOwnerEmail);
        tvSeeAllReviews = findViewById(R.id.tvSeeAllReviews);

        llShowCustomerRatings = findViewById(R.id.llShowCustomerRatings);
        llRateReviewTopLayout = findViewById(R.id.llRateReviewTopLayout);

        btnChat = findViewById(R.id.btnChat);
        btnCall = findViewById(R.id.btnCall);

        ivAvtar = findViewById(R.id.ivAvtar);
        progressBarAvtar = findViewById(R.id.progressBarAvtar);


        rvReviewsList = findViewById(R.id.rvReviewsList);
        rvReviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvReviewsList.setNestedScrollingEnabled(false);
        merchantReviewsAdapter = new MerchantReviewsAdapter(mActivity, merchantReviewsList);
        rvReviewsList.setAdapter(merchantReviewsAdapter);


//        Utils.setOnClickListener(this, etAddress);


        Utils.setOnClickListener(this, rlBack, ivAvtar, btnChat, btnCall, tvSeeAllReviews);

    }


    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;

            case R.id.btnChat:
                if (UIManager.isFuguChatEnabled()) {
                    Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
                    HippoConfig.getInstance().showConversations(mActivity, StorefrontCommonData.getString(mActivity, R.string.support));
                }
                break;

            case R.id.btnCall:
                Utils.openCallDialer(mActivity, datum.getPhone());
                break;

            case R.id.tvSeeAllReviews:
                Intent reviewIntent = new Intent(mActivity, MerchantReviewsActivity.class);
                reviewIntent.putExtra(IS_FROM_OWNER_PROFILE, true);
                reviewIntent.putExtra(STOREFRONT_DATA, datum);
                startActivity(reviewIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        AnimationUtils.exitTransition(mActivity);
    }

    /**
     * get owner profile data
     */
    private void getOwnerProfileData() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(OwnerProfileActivity.this, StorefrontCommonData.getUserData());

        commonParams.add(USER_ID, userId);
        RestClient.getApiInterface(OwnerProfileActivity.this).viewOwnerProfile(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(OwnerProfileActivity.this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                try {
                    datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum.class);
                    setData();

                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });

    }


    /**
     * set data to views
     */
    private void setData() {
        if ((datum.getLogo() != null) && (!datum.getLogo().equals(""))) {
            progressBarAvtar.setVisibility(View.VISIBLE);
            /*Glide.with(this).load(datum.getLogo()).asBitmap().centerCrop()
            .diskCacheStrategy(SOURCE).placeholder(AppCompatResources.getDrawable(mActivity, R.drawable.ic_profile_placeholder))
            .into(new BitmapImageViewTarget(ivAvtar) {
                @Override
                protected void setResource(Bitmap resource) {
                    progressBarAvtar.setVisibility(View.GONE);
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivAvtar.setImageDrawable(circularBitmapDrawable);
                }
            });*/

            new GlideUtil.GlideUtilBuilder(ivAvtar)
                    .setPlaceholder(R.drawable.ic_profile_placeholder)
                    .setLoadItem(datum.getLogo())
                    .setTransformation(new CircleCrop())
                    .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                        @Override
                        public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {
                            progressBarAvtar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) {

                        }

                        @Override
                        public void onLoadFailed() {
                            progressBarAvtar.setVisibility(View.GONE);
                        }
                    }).build();


        } else {
            progressBarAvtar.setVisibility(View.GONE);
            ivAvtar.setImageResource(R.drawable.ic_profile_placeholder);
        }

        tvOwnerName.setText(datum.getStoreName());
        if (datum.getEmail().equalsIgnoreCase("")) {
            tvOwnerEmail.setVisibility(View.GONE);
        } else {
            tvOwnerEmail.setVisibility(View.VISIBLE);
            tvOwnerEmail.setText(datum.getEmail());
        }
        Utils.addStarsToLayout(mActivity, llShowCustomerRatings, datum.getStoreRating().doubleValue());

        if (datum.getIsReviewRatingEnabled() == 1) {
            if (datum.getLastReviewRating().size() > 0) {
                llRateReviewTopLayout.setVisibility(View.VISIBLE);
                rvReviewsList.setVisibility(View.VISIBLE);

                if (datum.getTotalReviewCount() > 0) {
                    tvSeeAllReviews.setVisibility(View.VISIBLE);
                    tvSeeAllReviews.setText(getStrings(R.string.see_all_reviews) + " (" + datum.getTotalReviewCount() + ")");
                } else {
                    tvSeeAllReviews.setVisibility(View.GONE);
                }

                merchantReviewsList.clear();
                merchantReviewsList.addAll(datum.getLastReviewRating());
                merchantReviewsAdapter.notifyDataSetChanged();

            } else {
                rvReviewsList.setVisibility(View.GONE);
                tvSeeAllReviews.setVisibility(View.GONE);
                llRateReviewTopLayout.setVisibility(View.GONE);
            }

        } else {
            llRateReviewTopLayout.setVisibility(View.GONE);
        }


    }


}