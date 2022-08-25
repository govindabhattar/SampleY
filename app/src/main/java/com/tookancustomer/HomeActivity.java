package com.tookancustomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hippo.ChatByUniqueIdAttributes;
import com.hippo.HippoConfig;
import com.tookancustomer.adapters.MerchantReviewsAdapter;
import com.tookancustomer.adapters.StoresImagesAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.MarketplaceStorefrontModel.LastReviewRating;
import com.tookancustomer.models.appConfiguration.DynamicPagesDetails;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userpages.UserPagesData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Arrays;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;


public class HomeActivity extends BaseActivity implements View.OnClickListener, LocationFetcher.OnLocationChangedListener,
        Keys.Extras {
    boolean isWishlistChanged = false;
    boolean isRateTouch = true;
    boolean isLanguageChanged = false;
    int merchantId;
    private DrawerLayout mDrawerLayout;
    private long lastBackPressed;
    private ImageView ivCompanyLogo2;
    private TextView tvCompanyName2;
    private View layoutRestaurantDescription;
    private RelativeLayout rlBack;
    private ImageView ivBack;
    private ImageView ivSelectWishlist, ivUnSelectWishlist;
    private TextView tvRestaurantAddress, tvRestaurantContactNumber;
    private TextView tvCompanyAppName, tvCompanyMainDescription;
    private Datum storefrontData;
    private int storefrontDataItemPos;
    private LocationFetcher locationFetcher;
    private Double latitude, longitude;
    private String address;
    private boolean isSideMenu = false;
    private LinearLayout llRateReviewTopLayout;
    private LinearLayout llEditMerchantRatings, llAverageMerchantRatings, llRatingImageLayout, llGoToWriteReview, llAverageRating;
    private TextView tvSelfRating, tvEditReview;
    private LinearLayout llSelfRatingLayout;
    private LinearLayout llAverageRatingStar;
    private LinearLayout llRateMerchantEmpty, llShareMerchant, llChatWithMerchant;
    private TextView tvAverageRating, tvAverageRatingImage, tvTotalRateReviews, tvRateMerchantText;
    private AppBarLayout appbarLayout;
    private int appbarHeight = 0;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvHeading;
    private RecyclerView rvReviewsList;
    private TextView tvCall, tvLocate, tvChat, tvRatingsReviewsHeading, tvShare;
    private TextView tvSeeAllReviews;
    private MerchantReviewsAdapter merchantReviewsAdapter;
    private ArrayList<LastReviewRating> merchantReviewsList = new ArrayList<>();
    private NestedScrollView nsvScrollBar;
    private RelativeLayout rlParentLayout, rlStoreImages;
    private CoordinatorLayout clParentLayout;
    private Button btnOrderOnline2;
    private String adminSelectedCategories = "";
    private Integer businessCategoryId;
    private int selectedPickupMode = 0; // none==0 , home_delivery==1, self_pickup==2
    private boolean isDirect = false;
    private TextView tvCustomOrderButton;
    private boolean isGuest;
    private String preOrderDateTime;
    private boolean isGuestAfterLogin;
    private Button btnViewPortfolio;
    private ArrayList<DynamicPagesDetails> pagesList;
    private StoresImagesAdapter storesImagesAdapter;
    private ViewPager pagerStore;
    private LinearLayout llIndicators;
    private boolean fromDeeplink = false;
    private boolean fromDeeplinkSignIn = false;
    private boolean isWishListChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        merchantReviewsAdapter = new MerchantReviewsAdapter(mActivity, merchantReviewsList);
        rvReviewsList.setAdapter(merchantReviewsAdapter);
        fromDeeplink = getIntent().getBooleanExtra("FROM_DEEP_LINK", false);
        fromDeeplinkSignIn = getIntent().getBooleanExtra("FROM_DEEP_LINK_SIGN_IN", false);
        if (fromDeeplink) {
            merchantId = getIntent().getIntExtra("MERCHANT_ID", 0);
        }

        if ((getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false)
                || fromDeeplink)) {
            if (Dependencies.getAccessToken(mActivity).isEmpty()) {
                Dependencies.setDemoRun(true);
            }
            if (fromDeeplink) {
                getSingleMerchantDetails(merchantId);
            } else {
                getSingleMerchantDetails(getIntent().getIntExtra("MERCHANT_ID", 0));
            }
            return;
        }

        mActivity = this;
        isGuest = Dependencies.getAccessToken(mActivity).isEmpty();
        setParameters();

        btnOrderOnline2.setText(StorefrontCommonData.getTerminology().getOrderOnline());
        btnViewPortfolio.setText(getStrings(R.string.viewPortfolio));

        tvCall.setText(getStrings(R.string.call));

        tvLocate.setText(getStrings(R.string.locate));

        tvChat.setText(getStrings(R.string.chat));
        tvShare.setText(getStrings(R.string.share_on));

        appbarLayout.post(new Runnable() {
            @Override
            public void run() {
                appbarHeight = appbarLayout.getMeasuredHeight();

                if (!UIManager.getIsReviewRatingRequired() && appbarLayout.getMeasuredHeight() > 0 && clParentLayout.getMeasuredHeight() > 0)
                    Log.e("IS_FIRST_SETTED", "true");
                if (nsvScrollBar.getMeasuredHeight() + appbarLayout.getMeasuredHeight() <= clParentLayout.getMeasuredHeight()) {
                    appbarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(appbarLayout.getMeasuredWidth(), clParentLayout.getMeasuredHeight() - nsvScrollBar.getMeasuredHeight()));

                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
                    params.setScrollFlags(0);
                } else {

                    appbarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(appbarLayout.getMeasuredWidth(), appbarHeight));
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                            | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
            }
        });


        setAddressFetcher();

        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                float offsetAlpha = (appBarLayout.getY() / appBarLayout.getTotalScrollRange());
                ivCompanyLogo2.setAlpha(1 - (offsetAlpha * -1));

                try {
                    if ((storefrontData.getLogo() != null) && (!storefrontData.getLogo().equals(""))) {
                        /*Glide.with(mActivity).load(storefrontData.getLogo()).asBitmap()
                        .placeholder(AppCompatResources.getDrawable(mActivity, R.drawable.ic_image_placeholder))
                        .diskCacheStrategy(SOURCE).into(ivCompanyLogo2);
                         */

                        new GlideUtil.GlideUtilBuilder(ivCompanyLogo2)
                                .setPlaceholder(R.drawable.ic_plcaeholder_marketplace_new)
                                .setLoadItem(storefrontData.getLogo())
                                .build();

                    }
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    tvHeading.setText(storefrontData.getStoreName());

                    if (isSideMenu) {
                        ivBack.setImageResource(R.drawable.ic_icon_menu);
                    } else {
                        ivBack.setImageResource(R.drawable.ic_back);
                    }
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.white));

                } else if (scrollRange + verticalOffset == scrollRange) {
                    isShow = false;
                    tvHeading.setText("");


                    if (isSideMenu) {
                        ivBack.setImageResource(R.drawable.ic_icon_menu_white);
                    } else {
                        ivBack.setImageResource((R.drawable.ic_back_white));
                    }
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.transparent));

//                } else if (scrollRange + verticalOffset < scrollRange / 4) {
//                    isShow = true;
//                    tvHeading.setText(storefrontData.getStoreName());
//
//                    if (isSideMenu) {
//                        findViewById(R.id.ivBack).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_icon_menu));
//                    } else {
//                        findViewById(R.id.ivBack).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_back));
//                    }
//                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.transparent));
                } else if (isShow) {
                    isShow = false;
                    tvHeading.setText("");
                    if (isSideMenu) {
                        ivBack.setImageResource((R.drawable.ic_icon_menu_white));
                    } else {
                        ivBack.setImageResource((R.drawable.ic_back_white));
                    }
                    collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        setData();


        if (!Dependencies.isDemoRunning()) {
            if (getIntent().getStringExtra(SUCCESS_MESSAGE) != null) {
                Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(SUCCESS_MESSAGE));
            } else if (getIntent().getStringExtra(FAILURE_MESSAGE) != null) {
                Utils.snackBar(mActivity, getIntent().getStringExtra(FAILURE_MESSAGE));
            }
        }
        getMarketplaceStorefronts();

        setPortfolioView();

    }


    private void setParameters() {
        try {
            if (getIntent() != null) {
                if (!(getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false)) || fromDeeplink) {
                    storefrontData = (Datum) getIntent().getExtras().getSerializable(STOREFRONT_DATA);
                } else {
                    //  storefrontData = (Datum) getIntent().getExtras().getSerializable(STOREFRONT_DATA);
                }
                selectedPickupMode = storefrontData.getSelectedPickupMode();
                storefrontDataItemPos = getIntent().getExtras().getInt(STOREFRONT_DATA_ITEM_POS, 0);
                isSideMenu = getIntent().getExtras().getBoolean(IS_SIDE_MENU, false);
                isDirect = getIntent().getBooleanExtra("isDirect", false);
                UIManager.isMarketplaceSingleRestaurant = isSideMenu;

                latitude = getIntent().getExtras().getDouble(PICKUP_LATITUDE);
                longitude = getIntent().getExtras().getDouble(PICKUP_LONGITUDE);
                address = getIntent().getExtras().getString(PICKUP_ADDRESS);
                preOrderDateTime = getIntent().getExtras().getString(DATE_TIME);
                if (getIntent().hasExtra(ADMIN_CATALOGUE_SELECTED_CATEGORIES)) {
                    adminSelectedCategories = getIntent().getStringExtra(ADMIN_CATALOGUE_SELECTED_CATEGORIES);
                }
                if (getIntent().hasExtra(BUSINESS_CATEGORY_ID)) {
                    businessCategoryId = getIntent().getIntExtra(BUSINESS_CATEGORY_ID, 0);
                }
            }
        } catch (Exception e) {
        }
    }

    private void initView() {
        ivSelectWishlist = findViewById(R.id.ivSelectWishlist);
        ivUnSelectWishlist = findViewById(R.id.ivUnSelectWishlist);
        llChatWithMerchant = findViewById(R.id.llChatWithMerchant);

        btnOrderOnline2 = findViewById(R.id.btnOrderOnline2);
        btnViewPortfolio = findViewById(R.id.btnViewPortfolio);

        clParentLayout = findViewById(R.id.clParentLayout);
        rlParentLayout = findViewById(R.id.rlParentLayout);
        nsvScrollBar = findViewById(R.id.nsvScrollBar);

        ivCompanyLogo2 = findViewById(R.id.ivCompanyLogo2);
        tvCompanyName2 = findViewById(R.id.tvCompanyName2);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tvCompanyAppName = findViewById(R.id.tvCompanyAppName);
        tvCompanyMainDescription = findViewById(R.id.tvCompanyMainDescription);
        layoutRestaurantDescription = findViewById(R.id.layoutRestaurantDescription);
        rlBack = findViewById(R.id.rlBack);
        ivBack = findViewById(R.id.ivBack);
        tvRestaurantAddress = findViewById(R.id.tvRestaurantAddress);
        tvRestaurantContactNumber = findViewById(R.id.tvRestaurantContactNumber);

        llRatingImageLayout = findViewById(R.id.llRatingImageLayout);
        llRateReviewTopLayout = findViewById(R.id.llRateReviewTopLayout);
        llEditMerchantRatings = findViewById(R.id.llEditMerchantRatings);
        tvSelfRating = findViewById(R.id.tvSelfRating);
        llSelfRatingLayout = findViewById(R.id.llSelfRatingLayout);
        tvEditReview = findViewById(R.id.tvEditReview);

        llAverageMerchantRatings = findViewById(R.id.llAverageMerchantRatings);
        llAverageRatingStar = findViewById(R.id.llAverageRatingStar);
        llAverageRating = findViewById(R.id.llAverageRating);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        tvAverageRatingImage = findViewById(R.id.tvAverageRatingImage);
        tvTotalRateReviews = findViewById(R.id.tvTotalRateReviews);
        tvRateMerchantText = findViewById(R.id.tvRateMerchantText);
        llRateMerchantEmpty = findViewById(R.id.llRateMerchantEmpty);
        llShareMerchant = findViewById(R.id.llShareMerchant);
        llGoToWriteReview = findViewById(R.id.llGoToWriteReview);

        tvSeeAllReviews = findViewById(R.id.tvSeeAllReviews);

        appbarLayout = findViewById(R.id.appbarLayout);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        tvHeading = findViewById(R.id.tvHeading);

        tvCall = findViewById(R.id.tvCall);
        tvLocate = findViewById(R.id.tvLocate);
        tvChat = findViewById(R.id.tvChat);
        tvShare = findViewById(R.id.tvShare);
        tvRatingsReviewsHeading = findViewById(R.id.tvRatingsReviewsHeading);

        rvReviewsList = findViewById(R.id.rvReviewsList);
        rvReviewsList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvReviewsList.setNestedScrollingEnabled(false);

        pagerStore = findViewById(R.id.pagerStore);
        llIndicators = findViewById(R.id.llIndicators);
        rlStoreImages = findViewById(R.id.rlStoreImages);

        setViewPagerListener();


        Utils.setOnClickListener(this, findViewById(R.id.llCall), findViewById(R.id.btnOrderOnline2),
                findViewById(R.id.llLocateUs2), findViewById(R.id.fabButton),
                ivUnSelectWishlist, ivSelectWishlist, rlBack, tvEditReview, tvSeeAllReviews, btnViewPortfolio, llShareMerchant, llChatWithMerchant);
        try {
            if (UIManager.isFuguChatEnabled() &&
                    (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                if (StorefrontCommonData.getAppConfigurationData().getIsMerchantChatEnable() == 1) {
                    llChatWithMerchant.setVisibility(View.VISIBLE);
                } else {
                    llChatWithMerchant.setVisibility(View.GONE);
                }
            } else {
                llChatWithMerchant.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   findViewById(R.id.llContactUs2).setVisibility(View.GONE);


    }

    private void setViewPagerListener() {
        pagerStore.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                inflateIndicators(position, llIndicators);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Method to inflate the indicator for the
     */
    private void inflateIndicators(int position, LinearLayout llIndicators) {
        if (storesImagesAdapter.getImagesList().size() <= 1) {
            llIndicators.setVisibility(View.GONE);
        } else {
            llIndicators.setVisibility(View.VISIBLE);
            llIndicators.removeAllViews();

            int pixels = Utils.convertDpToPixels(this, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);
            params.bottomMargin = pixels;
            for (int index = 0; index < storesImagesAdapter.getImagesList().size(); index++) {
                View view = new View(this);
                view.setLayoutParams(params);
                view.setBackgroundResource(index == position ? R.drawable.switcher_filled : R.drawable.switcher);
                llIndicators.addView(view);
            }
        }
    }


    public void setData() {
        layoutRestaurantDescription.setVisibility(View.VISIBLE);

        if (isSideMenu) {
            ivBack.setImageResource((R.drawable.ic_icon_menu_white));
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            ivBack.setImageResource((R.drawable.ic_back_white));
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        if (storefrontData != null) {
            tvCompanyAppName.setText(storefrontData.getStoreName());
            tvCompanyMainDescription.setText(storefrontData.getDescription());
            tvCompanyName2.setText(storefrontData.getStoreName());

            tvRestaurantAddress.setText(storefrontData.getDistance() + (!storefrontData.getDisplayAddress().isEmpty() ? "  \u2022  " + storefrontData.getDisplayAddress() : ""));
            tvRestaurantContactNumber.setText(storefrontData.getPhone());

            if (storefrontData.getIsBannersEnabledMerchant() == 1 && storefrontData.getBannerImages() != null && storefrontData.getBannerImages().size() > 0) {
                rlStoreImages.setVisibility(View.VISIBLE);
                ivCompanyLogo2.setVisibility(View.GONE);
                storesImagesAdapter = new StoresImagesAdapter(HomeActivity.this, storefrontData.getBannerImages());
                pagerStore.setAdapter(storesImagesAdapter);
            } else {
                rlStoreImages.setVisibility(View.GONE);
                ivCompanyLogo2.setVisibility(View.VISIBLE);
            }


        }


        setRatingsData();
        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {

            try {
                if (storefrontData.getIsWishlisted() == 1) {
                    ivSelectWishlist.setVisibility(View.VISIBLE);
                    ivUnSelectWishlist.setVisibility(View.GONE);
                } else {
                    ivSelectWishlist.setVisibility(View.GONE);
                    ivUnSelectWishlist.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ivSelectWishlist.setVisibility(View.GONE);
            ivUnSelectWishlist.setVisibility(View.GONE);
        }

    }


    private void setPortfolioView() {
        if (storefrontData.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {
            callbackToGetPortfolio();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (storefrontData != null)
            StorefrontCommonData.getFormSettings().setUserId(storefrontData.getStorefrontUserId());

        MyApplication.getInstance().trackScreenView("Restaurant Detail Screen");
        // findViewById(R.id.llContactUs2).setVisibility(UIManager.isFuguChatEnabled() ? View.VISIBLE : View.GONE);
        try {
            findViewById(R.id.llCall).setVisibility(StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 1 ? View.VISIBLE : View.GONE);
            findViewById(R.id.tvRestaurantContactNumber).setVisibility(StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 1 ? View.VISIBLE : View.GONE);
            findViewById(R.id.llLocateUs2).setVisibility(StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 1 ? View.VISIBLE : View.GONE);
            tvRestaurantAddress.setVisibility(StorefrontCommonData.getAppConfigurationData().getDisplayMerchantAddress() == 1 ? View.VISIBLE : View.GONE);

            if (tvRestaurantAddress.getVisibility() == View.GONE && tvRestaurantContactNumber.getVisibility() == View.GONE && tvCompanyName2.getVisibility() == View.GONE) {
                (findViewById(R.id.llRestaurantDetails)).setVisibility(View.GONE);
            } else {
                (findViewById(R.id.llRestaurantDetails)).setVisibility(View.VISIBLE);
            }

            if (!UIManager.isFuguChatEnabled() && StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 0 && StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 0) {
                //findViewById(R.id.llButtonLayout).setVisibility(View.GONE);
                findViewById(R.id.fabButton).setVisibility(View.GONE);
            } else if (
                    (UIManager.isFuguChatEnabled() && StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 0 && StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 0) ||
                            (!UIManager.isFuguChatEnabled() && StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 1 && StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 0) ||
                            (!UIManager.isFuguChatEnabled() && StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 0 && StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 1)
            ) {
                // findViewById(R.id.llButtonLayout).setVisibility(View.GONE);
                findViewById(R.id.fabButton).setVisibility(View.VISIBLE);

                if (UIManager.isFuguChatEnabled()) {
                    ((FloatingActionButton) findViewById(R.id.fabButton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_white));
                } else if (StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 1) {
                    ((FloatingActionButton) findViewById(R.id.fabButton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_call_white));
                } else if (StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 1) {
                    ((FloatingActionButton) findViewById(R.id.fabButton)).setImageDrawable(getResources().getDrawable(R.drawable.ic_locate_white));
                }

            } else {
                findViewById(R.id.llButtonLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.fabButton).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
        if ((getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false)) || fromDeeplink) {
            isGuestAfterLogin = Dependencies.getAccessToken(mActivity).isEmpty();
            if (isGuest != isGuestAfterLogin && !fromDeeplink) {
                getMarketplaceStorefronts();
            }
        } else {
            setRatingsData();
        }
        setStrings();
    }

    public void setRatingsData() {
        if (storefrontData != null) {
            if (UIManager.getIsReviewRatingRequired()) {

                if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) && storefrontData.getStoreRating().floatValue() <= 0) {
                    llRateReviewTopLayout.setVisibility(View.GONE);
                    llRatingImageLayout.setVisibility(View.GONE);
                } else {
                    llRateReviewTopLayout.setVisibility(View.VISIBLE);

                    if (storefrontData.getStoreRating().floatValue() <= 0) {
                        llRatingImageLayout.setVisibility(View.GONE);
                    } else {
                        llRatingImageLayout.setVisibility(View.VISIBLE);
                        tvAverageRatingImage.setText(String.valueOf(storefrontData.getStoreRating().floatValue()));
                    }

                    if (storefrontData.getMyRating().intValue() > 0) {
                        llAverageMerchantRatings.setVisibility(View.GONE);
                        llEditMerchantRatings.setVisibility(View.VISIBLE);
                        tvSelfRating.setText(String.valueOf(storefrontData.getMyRating().floatValue()));
                        Utils.addStarsToLayout(mActivity, llSelfRatingLayout, storefrontData.getMyRating().doubleValue(), R.dimen._15dp);

                    } else {

                        if (!(Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
                            llAverageMerchantRatings.setVisibility(View.GONE);
                        } else {
                            llAverageMerchantRatings.setVisibility(View.VISIBLE);
                        }

                        llEditMerchantRatings.setVisibility(View.GONE);
                        llAverageRating.setVisibility(View.VISIBLE);
                        Utils.addSingleStarToLayout(mActivity, llAverageRatingStar, storefrontData.getStoreRating().doubleValue(), R.dimen._20dp);
                        tvAverageRating.setText(storefrontData.getStoreRating().floatValue() + "");
                        if (storefrontData.getTotalRatingsCount() > 0 && storefrontData.getTotalReviewCount() > 0) {
                            tvTotalRateReviews.setText(storefrontData.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text)
                                    + " " + getStrings(R.string.and) + " "
                                    + storefrontData.getTotalReviewCount() + " " + getStrings(R.string.reviews_text));
                        } else if (storefrontData.getTotalRatingsCount() > 0) {
                            tvTotalRateReviews.setText(storefrontData.getTotalRatingsCount() + " " + getStrings(R.string.ratings_text));
                        } else if (storefrontData.getTotalReviewCount() > 0) {
                            tvTotalRateReviews.setText(storefrontData.getTotalReviewCount() + " " + getStrings(R.string.reviews_text));
                        } else {
                            tvTotalRateReviews.setText(getStrings(R.string.no_rate_reviews_be_first_one));
                            llAverageRating.setVisibility(View.GONE);
                        }

                        tvRateMerchantText.setText(getStrings(R.string.rate_storename).replace(NAME, storefrontData.getStoreName()));
                        Utils.addStarsToLayout(mActivity, llRateMerchantEmpty, 0.0, R.dimen._20dp);


                        for (int i = 0; i < llRateMerchantEmpty.getChildCount(); i++) {

                            llRateMerchantEmpty.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Log.e("llrateMErchantempty", "<<" + v.getId());
                                    if (isRateTouch) {
                                        isRateTouch = false;

                                        Intent intent = new Intent(mActivity, MerchantAddRateReviewActivity.class);
                                        intent.putExtra(SHOW_RATE_STARS, 5f);
                                        intent.putExtra(STOREFRONT_DATA, storefrontData);
                                        startActivityForResult(intent, OPEN_ADD_MERCHANT_RATE_REVIEW);
                                    }
                                }
                            });
                        }

//                llRateMerchantEmpty.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (isRateTouch) {
//                            isRateTouch = false;
//
//                            Intent intent = new Intent(mActivity, MerchantAddRateReviewActivity.class);
//                            intent.putExtra(SHOW_RATE_STARS, rbRateMerchant.getScore());
//                            intent.putExtra(STOREFRONT_DATA, storefrontData);
//                            startActivityForResult(intent, OPEN_ADD_MERCHANT_RATE_REVIEW);
//                        }
//                        return false;
//                    }
//                });

                    }

                    if (storefrontData.getLastReviewRating().size() > 0) {
                        rvReviewsList.setVisibility(View.VISIBLE);

                        if (storefrontData.getTotalReviewCount() > 0) {
                            tvSeeAllReviews.setVisibility(View.VISIBLE);
                            tvSeeAllReviews.setText(getStrings(R.string.see_all_reviews) + " (" + storefrontData.getTotalReviewCount() + ")");
                        } else {
                            tvSeeAllReviews.setVisibility(View.GONE);
                        }

                        merchantReviewsList.clear();
                        merchantReviewsList.addAll(storefrontData.getLastReviewRating());
                        merchantReviewsAdapter.notifyDataSetChanged();

                    } else {
                        rvReviewsList.setVisibility(View.GONE);
                        tvSeeAllReviews.setVisibility(View.GONE);
                    }

                }
            } else {
                llRateReviewTopLayout.setVisibility(View.GONE);
                llRatingImageLayout.setVisibility(View.GONE);
            }

            rvReviewsList.post(new Runnable() {
                @Override
                public void run() {
                    (findViewById(R.id.llRestaurantDetails)).requestFocus();
                    (findViewById(R.id.nsvScrollBar)).scrollTo(0, 0);
                }
            });


            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                llGoToWriteReview.setVisibility(View.VISIBLE);
            } else {
                llGoToWriteReview.setVisibility(View.GONE);
            }

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Log.e("Screen size", size.y + "");
            Log.e("clParentLayout size", clParentLayout.getMeasuredHeight() + "");
            Log.e("appbarLayout size", appbarLayout.getMeasuredHeight() + "");
            Log.e("nsvScrollBar size", nsvScrollBar.getMeasuredHeight() + "");

            if (appbarLayout.getMeasuredHeight() > 0 && clParentLayout.getMeasuredHeight() > 0)
                if (nsvScrollBar.getMeasuredHeight() + appbarLayout.getMeasuredHeight() <= clParentLayout.getMeasuredHeight()) {
                    appbarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(appbarLayout.getMeasuredWidth(), clParentLayout.getMeasuredHeight() - nsvScrollBar.getMeasuredHeight()));

                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
                    params.setScrollFlags(0);
                } else {

                    appbarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(appbarLayout.getMeasuredWidth(), appbarHeight));
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                            | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                }
        }
    }

    public void sideMenuClick(View v) {
        SideMenuTransition.sideMenuClick(v, mActivity);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnOrderOnline2:
                if (preOrderDateTime == null)
                    Dependencies.setIsPreorderSelecetedForMenu(false);
                if (storefrontData != null && Dependencies.getSelectedProductsArrayList().size() > 0) {
                    if (storefrontData.getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                        for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                            Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(storefrontData);
                        }
                    }
                }
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.RESTAURANT_ORDER_ONLINE, storefrontData.getStoreName() + "");
                if (storefrontData != null) {
                    if (UIManager.isMenuEnabled() && storefrontData.getIsMenuEnabled() &&
                            storefrontData.getScheduledTask() == 1 &&
                            storefrontData.getIsStorefrontOpened() == 0) {
                        new SelectPreOrderTimeDialog(this,
                                new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                                    @Override
                                    public void onDateTimeSelected(String dateTime) {
                                        StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                                storefrontData.getLogo(),
                                                new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                                        Double.valueOf(storefrontData.getLongitude())),
                                                new LatLng(latitude, longitude),
                                                address,
                                                storefrontData,
                                                adminSelectedCategories, dateTime, businessCategoryId);
                                    }
                                }).setStorefrontData(storefrontData)
                                .show();
//                        selectPreOrderTime();
                    } else {
                        if (preOrderDateTime == null)
                            StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                    storefrontData.getLogo(), new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                            Double.valueOf(storefrontData.getLongitude())), new LatLng(latitude, longitude),
                                    address, storefrontData, adminSelectedCategories, businessCategoryId, false, 0);

                        else
                            StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                    storefrontData.getLogo(),
                                    new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                            Double.valueOf(storefrontData.getLongitude())),
                                    new LatLng(latitude, longitude),
                                    address,
                                    storefrontData,
                                    adminSelectedCategories, preOrderDateTime, businessCategoryId);
                    }
                } else {
                    StorefrontConfig.getAppCatalogueV2(mActivity);
                }
                break;
            case R.id.fabButton:
                if (UIManager.isFuguChatEnabled()) {
                    if (UIManager.isFuguChatEnabled()) {
                        Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
//                        FuguConfig.getInstance().showConversations(this, getStrings(R.string.support));
                        HippoConfig.getInstance().showConversations(mActivity, StorefrontCommonData.getString(mActivity, R.string.support));
                    }
                } else if (StorefrontCommonData.getFormSettings().getDisplayMerchantPhone() == 1) {
                    Utils.openCallDialer(mActivity, storefrontData.getPhone());
                } else if (StorefrontCommonData.getFormSettings().getDisplayMerchantLocation() == 1) {
                    try {
                        Utils.openGoogleMapsApp(this, new LatLng(Double.valueOf(storefrontData.getLatitude()), Double.valueOf(storefrontData.getLongitude())));
                    } catch (Exception e) {
                        Utils.openGoogleMapsApp(this, new LatLng(0.0, 0.0));
                    }
                }
                break;

            case R.id.llCall:
                Utils.openCallDialer(mActivity, storefrontData.getPhone());
                break;
            case R.id.llChatWithMerchant:
                ArrayList<String> groupingTagsMerchant = new ArrayList<>();
                groupingTagsMerchant.add("gt_" + storefrontData.getStorefrontUserId().toString());
                //  Utils.openCallDialer(mActivity, storefrontData.getPhone());
                Utils.startChat(StorefrontCommonData.getUserData().getData().getVendorDetails().getUserId()
                                .toString() + StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString(),
                        storefrontData.getStoreName()
                        , 0, 1, groupingTagsMerchant);
                break;
            case R.id.llShareMerchant:
                String prefixShare = "Found this amazing store on " + getString(R.string.app_name);
                String isSingleStore = "store/";
                if (storefrontData.getIsSingleStorefront() == 1) {
                    isSingleStore = "single-store/";
                }
                String urlToShare = " " + "https://" + StorefrontCommonData.getAppConfigurationData().getDomainName()
                        + "/" + StorefrontCommonData.getSelectedLanguageCode().getLanguageCode()
                        + "/" + isSingleStore + storefrontData.getStoreName().replace(" ", "%20")
                        + "/" + storefrontData.getStorefrontUserId()
                        + "?merchant_id=" + storefrontData.getStorefrontUserId();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, prefixShare + urlToShare);
                sendIntent.setType("text/plain");
                this.startActivity(sendIntent);
                break;

            case R.id.llContactUs2:
                if (UIManager.isFuguChatEnabled()) {
                    Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
//                    FuguConfig.getInstance().showConversations(this, getStrings(R.string.support));
//                    HippoConfig.getInstance().showConversations(mActivity, StorefrontCommonData.getString(mActivity, R.string.support));

                    ArrayList<String> groupingTags = new ArrayList<>();
                    groupingTags.add("gt_" + storefrontData.getStorefrontUserId().toString());
                    ChatByUniqueIdAttributes attributes = new ChatByUniqueIdAttributes.Builder()
                            .setTransactionId(storefrontData.getStorefrontUserId().toString())
                            .setUserUniqueKey(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString())
                            .setChannelName(storefrontData.getStoreName())
                            .setGroupingTags(groupingTags)
                            .build();
                    HippoConfig.getInstance().openChatByUniqueId(attributes);

                }
                break;
            case R.id.llLocateUs2:
                try {
                    Utils.openGoogleMapsApp(this, new LatLng(Double.valueOf(storefrontData.getLatitude()), Double.valueOf(storefrontData.getLongitude())));
                } catch (Exception e) {
                    Utils.openGoogleMapsApp(this, new LatLng(0.0, 0.0));
                }
                break;
            case R.id.rlBack:
                if (isSideMenu) {
                    mDrawerLayout.openDrawer(Gravity.START);
                    Utils.clearLightStatusBar(mActivity, mDrawerLayout);
                } else {
                    onBackPressed();
                }
                break;

            case R.id.ivSelectWishlist:
                selectedWishlist(false);
                break;

            case R.id.ivUnSelectWishlist:
                selectedWishlist(true);

                break;

            case R.id.tvEditReview:
                Intent intent = new Intent(mActivity, MerchantAddRateReviewActivity.class);
                intent.putExtra(SHOW_RATE_STARS, storefrontData.getMyRating());
                intent.putExtra(STOREFRONT_DATA, storefrontData);
                startActivityForResult(intent, OPEN_ADD_MERCHANT_RATE_REVIEW);
                break;
            case R.id.tvSeeAllReviews:
                Intent reviewIntent = new Intent(mActivity, MerchantReviewsActivity.class);
                reviewIntent.putExtra(STOREFRONT_DATA, storefrontData);
                startActivityForResult(reviewIntent, OPEN_ALL_MERCHANT_RATE_REVIEW);
                break;
            case R.id.btnViewPortfolio:
                if (pagesList.size() > 1) {
                    openDynamicPages(pagesList);
                } else {
                    openPortfolioPage(pagesList.get(0));
                }
                break;

        }
    }

    private void openDynamicPages(ArrayList<DynamicPagesDetails> pagesList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DynamicPagesActivity.EXTRA_PAGE_LIST, pagesList);
        bundle.putString(DynamicPagesActivity.EXTRA_HEADER_NAME, storefrontData.getStoreName());
        Transition.startActivity(mActivity, DynamicPagesActivity.class, bundle, false);
    }

    private void openPortfolioPage(DynamicPagesDetails templateDatum) {
        Bundle bundle = new Bundle();
        bundle.putString(HEADER_WEBVIEW, templateDatum.getName());
        bundle.putString(URL_WEBVIEW, templateDatum.getTemplateData());
        bundle.putBoolean(IS_HTML, true);
        Transition.startActivity(this, WebViewActivity.class, bundle, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isRateTouch = true;
        switch (requestCode) {
            /*case Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    StorefrontConfig.getAppCatalogueV2(mActivity, place.getLatLng().latitude, place.getLatLng().longitude, place.getAddress().toString(), storefrontData, adminSelectedCategories);

                    //updateMap(place.getLatLng(), "" + place.getAddress());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.i("Error", "" + status.getStatusMessage());
                    Utils.snackBar(this, status.getStatusMessage());
                    // TODO: Handle the error.
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
                break;*/
            case OPEN_NLEVEL_ACTIVITY_AGAIN:
                if (resultCode == RESULT_OK) {
                    if (!isSideMenu) {
                        if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                            setResult(RESULT_OK, data);
                            transitionExit(data);
//                            Transition.exit(this);
                        }
                    } else {
                        if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                            Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                        } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                            Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                        }
                    }

                }
                break;
            case Codes.Request.OPEN_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    isLanguageChanged = data.getExtras().getBoolean("isLanguageChanged");
                    StorefrontCommonData.setUserData((UserData) data.getExtras().getSerializable(UserData.class.getName()));
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                    if (isLanguageChanged) restartActivity();
                }
                break;
            case Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Dependencies.setDemoRun(false);
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                } else {
                    Dependencies.setDemoRun(true);
                }
                break;
            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    startLocationFetcher();
                }
                break;
            case OPEN_ADD_MERCHANT_RATE_REVIEW:
            case OPEN_ALL_MERCHANT_RATE_REVIEW:
                if (resultCode == RESULT_OK) {
                    storefrontData = (Datum) data.getExtras().getSerializable(STOREFRONT_DATA);
                }
                setRatingsData();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Codes.Permission.LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationFetcher();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        performBackAction();
    }

    @SuppressLint("WrongConstant")
    private void performBackAction() {
        if (isWishListChanged) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        }

        if ((getIntent().getExtras() != null
                && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))
                || fromDeeplink) {
            Intent intent = new Intent(mActivity, RestaurantListingActivity.class);
            startActivity(intent);
            AnimationUtils.forwardTransition(mActivity);
            finish();
        } else {
            if (!isSideMenu) {
                transitionExit(new Intent());
            } else {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(Gravity.START);
                    Utils.setLightStatusBar(mActivity, mDrawerLayout);
                } else {
                    long currentTimeStamp = System.currentTimeMillis();
                    long difference = currentTimeStamp - lastBackPressed;

                    if (difference > 2000) {
                        Utils.snackBar(this, getStrings(R.string.tap_again_to_exit_text), false);
                        lastBackPressed = currentTimeStamp;
                    } else {
                        ActivityCompat.finishAffinity(this);
                        Transition.exit(this);
                    }
                }
            }
        }
    }

    public void transitionExit(Intent data) {
        Bundle bundleExtra = new Bundle();
        bundleExtra.putSerializable(STOREFRONT_DATA, storefrontData);
        bundleExtra.putInt(STOREFRONT_DATA_ITEM_POS, storefrontDataItemPos);
        data.putExtras(bundleExtra);
        setResult(RESULT_OK, data);
        finish();
        AnimationUtils.exitTransition(mActivity);
    }

    public void setAddressFetcher() {
        startLocationFetcher();
//        getCurrentLocation();
//        executeSetAddress();
    }

    private void startLocationFetcher() {
        if (!checkLocationPermissions()) {
            return;
        }
        if (!LocationUtils.isGPSEnabled(mActivity)) {
            LocationAccess.showImproveAccuracyDialog(mActivity);
            return;
        }
        // Start fetching the location
        if (locationFetcher == null) {
            locationFetcher = new LocationFetcher(this, Constants.TimeRange.LOCATION_FETCH_INTERVAL, Constants.LocationPriority.BEST);
        }
        locationFetcher.connect();
    }

    public void onLocationChanged(Location location, int priority) {
        if (location == null) {
            return;
        }

        // Check if no location is fetched
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }

        LocationUtils.saveLocation(mActivity, location);
        locationFetcher.destroy();
    }

    /**
     * Method to check whether the TrackingData Permission
     * is Granted by the User
     */
    private boolean checkLocationPermissions() {
        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        return AppManager.getInstance().askUserToGrantPermission(mActivity, permissionsRequired, getStrings(R.string.please_grant_permission_location_text), Codes.Permission.LOCATION);
    }

    public void getMarketplaceStorefronts() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.LATITUDE, latitude != null ? latitude : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, longitude != null ? longitude : 0);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    Datum[] datum = baseModel.toResponseModel(Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                    //TODO IsStorefrontOpened  commented
//                    if (storefrontData != null)
//                        cityStorefrontsModels.getData().get(0).setIsStorefrontOpened(storefrontData.getIsStorefrontOpened());
                    cityStorefrontsModels.getData().get(0).setSelectedPickupMode(selectedPickupMode);
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                storefrontData = cityStorefrontsModels.getData().get(0);
                storefrontData.setIsFreeDeliveryEnabled(cityStorefrontsModels.getData().get(0).getIsFreeDeliveryEnabled());
                storefrontData.setFree_delivery_amount(cityStorefrontsModels.getData().get(0).getFree_delivery_amount());
                storefrontData.setFree_delivery_toggle(cityStorefrontsModels.getData().get(0).getFree_delivery_toggle());
                StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(storefrontData.getMerchantMinimumOrder());
                setData();
                isGuest = false;

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    public void setStrings() {
        tvCall.setText(getStrings(R.string.call));
        tvLocate.setText(getStrings(R.string.locate));
        tvChat.setText(getStrings(R.string.chat));
        tvRatingsReviewsHeading.setText(getStrings(R.string.reviews_ratings));
        tvEditReview.setText(getStrings(R.string.edit_review));
        tvSeeAllReviews.setText(getStrings(R.string.see_all_reviews));
        ((TextView) findViewById(R.id.tvReviewedThisPlace)).setText(getStrings(R.string.you_reviewed_this_place));
        try {
            btnOrderOnline2.setText(StorefrontCommonData.getTerminology().getOrderOnline());

        } catch (Exception e) {
            e.printStackTrace();
            btnOrderOnline2.setText("Order Online");

        }

        ((TextView) findViewById(R.id.tvForCompleteDemo)).setText(getStrings(R.string.for_a_complete_demo));
        ((Button) findViewById(R.id.btnSignup)).setText(getStrings(R.string.sign_up_now));
    }

    @SuppressLint("WrongConstant")
    public void restartActivity() {
//        recreate();
        mDrawerLayout.closeDrawer(Gravity.START);

        getIntent().putExtra("isLanguageChanged", true);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void callbackToGetPortfolio() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(this, StorefrontCommonData.getUserData());
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());
        commonParams.add("is_admin_page", 0);
        commonParams.add("is_active", 1);

        RestClient.getApiInterface(mActivity).getUserPages(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        btnViewPortfolio.setVisibility(View.VISIBLE);
                        pagesList = baseModel.toResponseModel(UserPagesData.class).getTemplateData();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }

    private void getSingleMerchantDetails(int merchantId) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        //  final Location location = LocationUtils.getLastLocation(mActivity);

        //  if (mActivity instanceof RestaurantListingActivity) {
        commonParams.add(Keys.APIFieldKeys.LATITUDE, latitude)
                .add(Keys.APIFieldKeys.LONGITUDE, longitude);
        //  }
        commonParams.add(USER_ID, merchantId);
        commonParams.add(IS_QR_CODE, 1);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                storefrontData = cityStorefrontsModels.getData().get(0);

                storefrontData.setIsFreeDeliveryEnabled(cityStorefrontsModels.getData().get(0).getIsFreeDeliveryEnabled());
                storefrontData.setFree_delivery_amount(cityStorefrontsModels.getData().get(0).getFree_delivery_amount());
                storefrontData.setFree_delivery_toggle(cityStorefrontsModels.getData().get(0).getFree_delivery_toggle());
                setData();
                setParameters();
                setRatingsData();
                if (!fromDeeplink)
                    fromDeeplink = false;
                if (fromDeeplinkSignIn) {
                    MyApplication.getInstance().setDeepLinkMerchantId(0);
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 101 && fromDeeplink) {
                    MyApplication.getInstance().setDeepLinkMerchantId(merchantId);
                    new AlertDialog.Builder(mActivity)
                            .message("You need to login to use this feature.")
                            .button(StorefrontCommonData.getString(mActivity, R.string.ok_text))
                            .listener(new AlertDialog.Listener() {
                                @Override
                                public void performPostAlertAction(int purpose, Bundle backpack) {
                                    Intent intent = new Intent(mActivity.getApplicationContext(), SplashActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    AnimationUtils.exitTransition(mActivity);
                                }
                            }).build().show();

                }
                if (error.getStatusCode() == 201 && fromDeeplink) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(mActivity)
                                    .message(error.getMessage())
                                    .button(StorefrontCommonData.getString(mActivity, R.string.ok_text))
                                    .listener(new AlertDialog.Listener() {
                                        @Override
                                        public void performPostAlertAction(int purpose, Bundle backpack) {
                                            onBackPressed();
                                        }
                                    }).build().show();
                        }
                    }, 100);
                }
            }

            ;
        });
    }

    private void selectedWishlist(boolean wishlistSelected) {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        UserData userData = StorefrontCommonData.getUserData();

        commonParams.add("is_wishlisted", wishlistSelected ? 1 : 0)
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add(LANGUAGE, "en")
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable())
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(USER_ID, storefrontData.getStorefrontUserId())
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getVendorDetails().getAppAccessToken());


        RestClient.getApiInterface(mActivity).merchantWishlist(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {

            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(mActivity, "Success.");
                if (wishlistSelected) {
                    ivSelectWishlist.setVisibility(View.VISIBLE);
                    ivUnSelectWishlist.setVisibility(View.GONE);
                } else {
                    ivSelectWishlist.setVisibility(View.GONE);
                    ivUnSelectWishlist.setVisibility(View.VISIBLE);
                }
                isWishListChanged = true;

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Utils.snackBar(mActivity, error.getMessage());
            }
        });
    }


}
