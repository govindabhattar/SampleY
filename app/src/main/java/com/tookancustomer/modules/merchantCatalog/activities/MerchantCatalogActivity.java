package com.tookancustomer.modules.merchantCatalog.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.tookancustomer.AddFromMapActivity;
import com.tookancustomer.FavLocationActivity;
import com.tookancustomer.FiltersActivity;
import com.tookancustomer.HomeActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.SearchProductActivity;
import com.tookancustomer.SideMenuBaseActivity;
import com.tookancustomer.adapter.ViewPagerAdapter;
import com.tookancustomer.adapters.MerchantDynamicPagesAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.dialog.SingleBtnDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.MerchantCachedData;
import com.tookancustomer.models.ProductFilters.Data;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userpages.UserPagesData;
import com.tookancustomer.modules.merchantCatalog.adapters.MerchantCategoriesAdapter;
import com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants;
import com.tookancustomer.modules.merchantCatalog.fragment.MerchantCatalogFragment;
import com.tookancustomer.modules.merchantCatalog.models.categories.MerchantCategoriesData;
import com.tookancustomer.modules.merchantCatalog.models.categories.Result;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.tookancustomer.appdata.StorefrontCommonData.getFormSettings;

/*
 * Categories will be handled in activity
 * and
 * Products will be handled in fragment because they are called under view pager (MENU LAYOUT)
 *
 * Layouts handled for categories --> LIST, BANNER, GRID, MENU (Menu layout will only be there for last level of categories)
 * */

public class MerchantCatalogActivity extends SideMenuBaseActivity implements View.OnClickListener, Keys.Extras, Keys.APIFieldKeys {
    private static final int REQ_CODE_FILTER = 1013;
    public LinearLayout llVegSwitchLayout;
    public Switch switchVegFilter;
    public ViewPagerAdapter mPagerAdapter;
    public Double pickLatitude = 0.0, pickLongitude = 0.0;
    public String pickAddress = "";
    public boolean isCustomCheckout = false;
    public RelativeLayout rlCustomOrder;
    //cached Data
    public int currentIndex = 0, currentLevel = 0;
    private ImageView ivSelectWishlist, ivUnSelectWishlist;
    private boolean isWishListChanged = false;
    /*Initialise UI Parameters*/
    private RelativeLayout rlBack;
    private ImageView ivBack, ivSearchHeader;
    private TextView tvHeading;
    private RelativeLayout rlTotalQuantity;
    private TextView tvMinOrderAmount, tvTotalQuantity, tvSubTotal;
    private RelativeLayout rlPreorder;
    private TextView tvPreorderText, tvPreorderDateTime;
    private TextView tvAddress;
    private LinearLayout llfilterView, llDate, llFilter;
    private MaterialEditText etFilterDate;
    private TextView tvFilter, tvMerchantCloseText;
    private RelativeLayout rlMerchantDynamicPages;
    private ImageView ivLeftArrow, ivRightArrow;
    private RecyclerView rvMerchantDynamicPages;
    private LinearLayoutManager horizontalLayoutManagerDynamicPage;
    private RecyclerView rvMerchantCategoriesList;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvCustomOrderButton;
    private LinearLayout llNoProductAvailableLoc;
    private TextView tvNoProductFoundLoc;
    private Button btnGoToLocationActivity;
    /* Intents */
    private Datum storefrontData;
    private Result parentCategoryData;
    private String checkInDate, checkOutDate = "";
    private String preOrderDateTime;
    private int businessCategoryId;
    private String adminSelectedCategories = "";
    //    private HashMap<String, ArrayList<AllowValue>> filterMap;
    /* Initialising Variables */
    private MerchantCategoriesData merchantCategoriesData;
    private Data filterData;
    private int minPrice, maxPrice;
    private SingleBtnDialog calenderDialog;
    private boolean isSideMenu = false;
    private Double minAmountPrice = 0.0;
    private ShimmerFrameLayout shimmerFrameLayout;
    private boolean isFromMandatory = false;
    private boolean isCustomOrderTapped = false;
    private boolean isEditOrder;
    private int editJobId;
    private boolean isHas = false;
    private int deepLinkCategoryId, newDeepLinkCategoryId, deepLinkProductId, deepLinkMerchantId;
    private String deepLinkingCategoryName;

    private boolean isOftenBoughtMode = false;
    private ImageView ivShareMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        addTopLayout(mActivity, R.layout.activity_merchant_catalog);

        if (getIntent().getExtras().containsKey(IS_OFTEN_BOUGHT) && getIntent().getExtras().getBoolean(IS_OFTEN_BOUGHT, false)) {
            isOftenBoughtMode = true;
        }

        if (!UIManager.getCustomOrderActive() && StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 1
                && StorefrontCommonData.getFormSettings().getProductView() == 0 &&
                StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 0 && !isOftenBoughtMode) {
            isSideMenu = true;
        }
        UIManager.isMarketplaceSingleRestaurant = isSideMenu;


        getIntents();

        if ((getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))) {
            // final Uri data = getIntent().getData();
            if (Dependencies.getAccessToken(mActivity).isEmpty()) {
                Dependencies.setDemoRun(true);
            }
            deepLinkCategoryId = getIntent().getIntExtra("DEEP_LINK_CATEGORY_ID", 0);
            deepLinkProductId = getIntent().getIntExtra("DEEP_LINK_PRODUCT_ID", 0);
            deepLinkMerchantId = getIntent().getIntExtra("MERCHANT_ID", 0);
            deepLinkingCategoryName = getIntent().getStringExtra("DEEP_LINK_CATEGORY_NAME");
            getSingleMerchantDetails(deepLinkMerchantId);
            newDeepLinkCategoryId = deepLinkCategoryId;
            return;
        } else {
            initViews();
        }
        //https://www.kuyapabili.com/en/store/Kuya-Padala-Motorcycle/935127?prodname=41071830&pordCat=5548215

        if (StorefrontCommonData.getLastMerchantCachedData() != null
                && StorefrontCommonData.getLastMerchantCachedData().getStorefrontData().getStorefrontUserId().equals(storefrontData.getStorefrontUserId())
                && StorefrontCommonData.getLastMerchantCachedData().getBusinessCategoryId() != null
                && StorefrontCommonData.getLastMerchantCachedData().getBusinessCategoryId() == businessCategoryId
        ) {
            //TODO when last merchant cache data not null and same store id
//            if (parentCategoryData == null) {
//                StorefrontCommonData.setLastMerchantCachedData(null);
//            }
        } else {
            StorefrontCommonData.setLastMerchantCachedData(new MerchantCachedData());
            StorefrontCommonData.getLastMerchantCachedData().setStorefrontData(storefrontData);
            StorefrontCommonData.getLastMerchantCachedData().setBusinessCategoryId(businessCategoryId);
        }
        int selectedPickupMode = 0;

        if (Dependencies.getSelectedDeliveryMode() == 0) {

            switch (StorefrontCommonData.getAppConfigurationData().getDefaultDeliveryMethod()) {
                case Constants.DeliveryMode.PICK_AND_DROP:
                    selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
                    break;
                case Constants.DeliveryMode.HOME_DELIVERY:
                    selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
                    break;
                case Constants.DeliveryMode.SELF_PICKUP:
                    selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
                    break;
            }

            Dependencies.setSelectedDelivery(selectedPickupMode);
        } else {
            storefrontData.setSelectedPickupMode(Dependencies.getSelectedDeliveryMode());
        }

        if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP)
            minAmountPrice = storefrontData.getMinimumSelfPickupAmount();
        else if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.HOME_DELIVERY)
            minAmountPrice = storefrontData.getMerchantMinimumOrder();
        else {

            if (storefrontData.getSelfPickup() == 1) {
                minAmountPrice = storefrontData.getMinimumSelfPickupAmount();
                storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.SELF_PICKUP);
            }
            if (storefrontData.getHomeDelivery() == 1) {
                minAmountPrice = storefrontData.getMerchantMinimumOrder();
                storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.HOME_DELIVERY);
            }

        }

        initPreorderView();
        setSelectedDate();
        if (StorefrontCommonData.getAppConfigurationData().getIsDynamicPagesActive() == 1 && Dependencies.isLaundryApp())
            setMerchantDynamicPages();

        if (isFromMandatory && merchantCategoriesData != null) {
            tvHeading.setText(getStrings(R.string.mandatory_categories));

            handleMerchantCategory();


        } else if (parentCategoryData == null) {

            if (storefrontData.getHasCategories() == 1 && !isOftenBoughtMode) {
                getMerchantCategories();
                getMarketplaceStorefronts();
            } else {
                setMerchantProductsFragment(parentCategoryData);
            }
        } else {

            if (parentCategoryData.getHasChildren() == 1 && !isOftenBoughtMode) {
                getMerchantCategories();
                getMarketplaceStorefronts();
            } else {
                setMerchantProductsFragment(parentCategoryData);
            }
        }

        updateHeading("");

    }


    public void updateHeading(String mDeepLinkingCategoryName) {
        if (!mDeepLinkingCategoryName.isEmpty()) {
            tvHeading.setText(mDeepLinkingCategoryName);

        } else {

            if (isFromMandatory && merchantCategoriesData != null) {
                tvHeading.setText(getStrings(R.string.mandatory_categories));
            } else if (parentCategoryData == null) {

                tvHeading.setText(storefrontData.getStoreName());
            } else {
                tvHeading.setText(parentCategoryData.getName());
            }
        }

        if (isOftenBoughtMode) {
            tvHeading.setText(R.string.text_view_more);
        }
    }

    public void getMarketplaceStorefronts() {
        final Location location = LocationUtils.getLastLocation(mActivity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(Keys.APIFieldKeys.LATITUDE, location != null ? location.getLatitude() : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, location != null ? location.getLongitude() : 0);
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<>(Arrays.asList(datum)));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }
                StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(cityStorefrontsModels.getData().get(0).getMerchantMinimumOrder());
                cityStorefrontsModels.getData().get(0).setSelectedPickupMode(storefrontData.getSelectedPickupMode());
                storefrontData = cityStorefrontsModels.getData().get(0);
                if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {
                    if ((storefrontData.getSelfPickup() == 0
                            || storefrontData.getHomeDelivery() == 1
                            || storefrontData.getIsPdFlow())
                            && storefrontData.getIsStorefrontOpened() == 1
                            && (storefrontData.getMerchantAsDeliveryManager() == 1 ||
                            (storefrontData.getMerchantAsDeliveryManager() == 0
                                    && StorefrontCommonData.getAppConfigurationData()
                                    .getIsMarketPlaceDeliveryAvailabile() == 1)))
                        rlCustomOrder.setVisibility(View.VISIBLE);
                } else {
                    rlCustomOrder.setVisibility(View.GONE);
                }

                updateHeading("");
                if (isCustomOrderTapped) {
                    isCustomOrderTapped = false;
                    if (mActivity instanceof MerchantCatalogActivity) {
                        ((MerchantCatalogActivity) mActivity).isCustomCheckout = true;
                    }
                    Bundle extraa = new Bundle();
                    extraa.putDouble(PICKUP_LATITUDE, pickLatitude);
                    extraa.putDouble(PICKUP_LONGITUDE, pickLongitude);
                    extraa.putString(PICKUP_ADDRESS, pickAddress);
                    extraa.putBoolean("isCustomOrder", true);
                    if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1)
                        extraa.putBoolean("isCustomOrderMerchantLevel", true);
                    extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                    Transition.openCustomCheckoutActivity(mActivity, extraa);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (storefrontData != null)
            StorefrontCommonData.getFormSettings().setUserId(storefrontData.getStorefrontUserId());
        if (!(getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))) {
            setTotalQuantity();
        }
    }

    private void getIntents() {
        if (getIntent().hasExtra(STOREFRONT_DATA)) {
            storefrontData = (Datum) getIntent().getSerializableExtra(STOREFRONT_DATA);
        } else {
            com.tookancustomer.models.userdata.Datum formSettings = StorefrontCommonData.getFormSettings();
            storefrontData = new Datum(formSettings.getMerchantMinimumOrder(), ""
                    , formSettings.getUserId(), formSettings.getFormName(), formSettings.getLogo(), "", "", ""
                    , "", "", "", 0, "", 0, null, 0,
                    0, "", 0, formSettings.getInstantTask(), formSettings.getScheduledTask(), formSettings.getShowOutStockedProduct(), formSettings.getEnableTookanAgent(),
                    formSettings.getBufferSchedule(), formSettings.getButtons(), formSettings.getBusinessType(), formSettings.getMultipleProductInSingleCart(), formSettings.getPdOrAppointment(), formSettings.getIsStartEndTimeEnable(), 1, formSettings.getIsReviewRatingEnabled()
                    , formSettings.getHomeDelivery(), formSettings.getSelfPickup(), "", 0.0, 0,
                    StorefrontCommonData.getAppConfigurationData().getRecurringTask(),
                    0);
        }

        if (getIntent().hasExtra(PARENT_CATEGORY_DATA)) {
            parentCategoryData = (Result) getIntent().getSerializableExtra(PARENT_CATEGORY_DATA);
        }

//        EDIT_JOB_ID
//                IS_EDIT_ORDER

        isEditOrder = getIntent().getBooleanExtra(IS_EDIT_ORDER, false);

        if (isEditOrder)
            editJobId = getIntent().getIntExtra(EDIT_JOB_ID, 0);

        checkInDate = getIntent().getStringExtra(CHECK_IN_DATE);
        checkOutDate = getIntent().getStringExtra(CHECK_OUT_DATE);

        pickLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        pickLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        pickAddress = getIntent().getStringExtra(PICKUP_ADDRESS);
        isFromMandatory = getIntent().getBooleanExtra(IS_FROM_MANDATORY_CATEGORY, false);
//        reqError = (ReqError) getIntent().getSerializableExtra("reqCatalogue");
        merchantCategoriesData = (MerchantCategoriesData) getIntent().getSerializableExtra("reqCatalogue");

        preOrderDateTime = getIntent().getStringExtra(DATE_TIME);
        if (storefrontData.getInstantTask() == 0
                && storefrontData.getScheduledTask() == 1) {
            try {
                Date currentDate = new Date();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MINUTE, storefrontData.getPreBookingBuffer());
                currentDate = c.getTime();
                preOrderDateTime = DateUtils.getInstance().convertDateObjectToUtc(currentDate);
            } catch (ParseException e) {
            }

        }


        if (getIntent().hasExtra(ADMIN_CATALOGUE_SELECTED_CATEGORIES)) {
            adminSelectedCategories = getIntent().getStringExtra(ADMIN_CATALOGUE_SELECTED_CATEGORIES);
        }

        if (getIntent().hasExtra(BUSINESS_CATEGORY_ID)) {
            businessCategoryId = getIntent().getIntExtra(BUSINESS_CATEGORY_ID, 0);
        }
    }

    private void initViews() {
        rlBack = findViewById(R.id.rlBack);
        ivSelectWishlist = findViewById(R.id.ivSelectWishlist);
        ivUnSelectWishlist = findViewById(R.id.ivUnSelectWishlist);

        ivShareMerchant = findViewById(R.id.ivShareMerchant);

        if (Objects.requireNonNull(getFormSettings()).getDisplayMerchantDetailsPage() != 1) {
            ivShareMerchant.setVisibility(View.VISIBLE);
        } else {
            ivShareMerchant.setVisibility(View.GONE);
        }


        ivBack = findViewById(R.id.ivBack);
        ivSearchHeader = findViewById(R.id.ivSearchHeader);
        tvHeading = findViewById(R.id.tvHeading);
        tvCustomOrderButton = findViewById(R.id.tvCustomOrderButton);

        tvCustomOrderButton.setText(StorefrontCommonData.getTerminology().getCustomOrder());

        rlTotalQuantity = findViewById(R.id.rlTotalQuantity);
        tvMinOrderAmount = findViewById(R.id.tvMinOrderAmount);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantity);
        tvSubTotal = findViewById(R.id.tvSubTotal);

        rlCustomOrder = findViewById(R.id.rlCustomOrder);

        tvMerchantCloseText = findViewById(R.id.tvMerchantCloseText);
        tvMerchantCloseText.setText(StorefrontCommonData.getTerminology().getStoreClosedMessage());
        tvMerchantCloseText.setVisibility(storefrontData.isStoreAvailableForBooking() ? View.GONE : View.VISIBLE);
        findViewById(R.id.vwShadow).setVisibility(storefrontData.isStoreAvailableForBooking() ? View.VISIBLE : View.GONE);

        rlPreorder = findViewById(R.id.rlPreorder);
        tvPreorderText = findViewById(R.id.tvPreorderText);
        tvPreorderDateTime = findViewById(R.id.tvPreorderDateTime);

        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setHint(StorefrontCommonData.getTerminology().getAddress());

        llfilterView = findViewById(R.id.llfilterView);
        llDate = findViewById(R.id.llDate);
        etFilterDate = findViewById(R.id.etFilterDate);
        etFilterDate.setHint(getStrings(R.string.select_date));
        etFilterDate.setFloatingLabelText(getStrings(R.string.selected_date));
        llFilter = findViewById(R.id.llFilter);
        tvFilter = findViewById(R.id.tvFilter);
        ((TextView) findViewById(R.id.tvFilter)).setText(getStrings(R.string.filter));

        rlMerchantDynamicPages = findViewById(R.id.rlMerchantDynamicPages);
        ivLeftArrow = findViewById(R.id.ivLeftArrow);
        ivRightArrow = findViewById(R.id.ivRightArrow);
        rvMerchantDynamicPages = findViewById(R.id.rvMerchantDynamicPages);
        rvMerchantDynamicPages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalLayoutManagerDynamicPage = (LinearLayoutManager) rvMerchantDynamicPages.getLayoutManager();

        llVegSwitchLayout = findViewById(R.id.llVegSwitchLayout);
        ((TextView) findViewById(R.id.tvVegOnlyLabel)).setText(StorefrontCommonData.getTerminology().getVegOnly());
        switchVegFilter = findViewById(R.id.switchVegFilter);
        switchVegFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProductFragment();

            }
        });

        shimmerFrameLayout = findViewById(R.id.shimmerLayout);

        rvMerchantCategoriesList = findViewById(R.id.rvMerchantCategoriesList);
        if (storefrontData.getCategoryLayoutType() == MerchantCatalogConstants.CategoryLayoutTypes.GRID_LAYOUT.layoutValue) {
            rvMerchantCategoriesList.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rvMerchantCategoriesList.setLayoutManager(new LinearLayoutManager(this));
        }
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);


        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                /*Getting default tablayout textview */
                LinearLayout mTabLayout = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) mTabLayout.getChildAt(1);
                tabTextView.setTypeface(Font.getBold(mActivity));


                try {/* We are updating previous and next fragment as well so that user have less loading */
                    updateProductFragmentIfProductDataNull(tab.getPosition());
                    showeHideCustomButton(mPagerAdapter.getMerchantCatalogFragment(tab.getPosition()).productCatalogueArrayList.size());

                    if (tab.getPosition() > 0) {/*Updating previous fragment*/
                        updateProductFragmentIfProductDataNull(tab.getPosition() - 1);
                    }

                    if (tab.getPosition() < mPagerAdapter.getCount() - 1) { /*Updating next fragment*/
                        updateProductFragmentIfProductDataNull(tab.getPosition() + 1);
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout mTabLayout = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) mTabLayout.getChildAt(1);
                tabTextView.setTypeface(Font.getRegular(mActivity));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        llNoProductAvailableLoc = findViewById(R.id.llNoProductAvailableLoc);
        tvNoProductFoundLoc = findViewById(R.id.tvNoProductFoundLoc);
//        tvNoProductFoundLoc.setText(getStrings(R.string.no_products_found_please_change_your_location).replace(getStrings(R.string.product), StorefrontCommonData.getTerminology().getProduct()));
        tvNoProductFoundLoc.setText(getStrings(R.string.no_products_found).replace(StorefrontCommonData.getString(mActivity, R.string.product).toLowerCase(), StorefrontCommonData.getTerminology().getProduct()));
        btnGoToLocationActivity = findViewById(R.id.btnGoToLocationActivity);
        btnGoToLocationActivity.setText(getStrings(R.string.select_location));
        btnGoToLocationActivity.setVisibility(View.GONE);

        Utils.setOnClickListener(this, rlBack, ivSearchHeader, rlTotalQuantity, rlPreorder, tvAddress
                , llDate, ivSelectWishlist, ivUnSelectWishlist, etFilterDate, ivShareMerchant, llFilter, ivLeftArrow, ivRightArrow, btnGoToLocationActivity, rlCustomOrder);

        //setting menu button visibilty
        if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
            ivSearchHeader.setVisibility(Dependencies.isLaundryApp() ? View.INVISIBLE : View.VISIBLE);

            if (isSideMenu && parentCategoryData == null) {
                ivBack.setImageResource(R.drawable.ic_icon_menu);
                unlockSideMenu();
            } else {
                ivBack.setImageResource(R.drawable.ic_back);
                lockSideMenu();
            }
            tvAddress.setVisibility(View.GONE);
            llfilterView.setVisibility(View.GONE);
        } else {
            llfilterView.setVisibility(UIManager.isShowDateFilter() ? View.VISIBLE : View.GONE);
            tvAddress.setVisibility(View.VISIBLE);
            ivSearchHeader.setVisibility(View.INVISIBLE);
            ivBack.setImageResource(R.drawable.ic_icon_menu);
            unlockSideMenu();
        }

        if (isOftenBoughtMode) {
            ivSearchHeader.setVisibility(View.GONE);
            llVegSwitchLayout.setVisibility(View.GONE);
        }


        if (pickAddress != null) {
            tvAddress.setText(pickAddress);
        }

        if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 0) {
            if (storefrontData.getIsWishlisted() == 1) {
                ivSelectWishlist.setVisibility(View.VISIBLE);
                ivUnSelectWishlist.setVisibility(View.GONE);
            } else {
                ivSelectWishlist.setVisibility(View.GONE);
                ivUnSelectWishlist.setVisibility(View.VISIBLE);
            }
        } else {
            ivSelectWishlist.setVisibility(View.GONE);
            ivUnSelectWishlist.setVisibility(View.GONE);
        }

        ivSelectWishlist.setVisibility(View.GONE);
        ivUnSelectWishlist.setVisibility(View.GONE);
    }

    public void showeHideCustomButton(int size) {

       /* restaurantInfo && restaurantInfo.custom_order_active_for_store
                && restaurantInfo.available &&
                formSetting.is_custom_order_active
                && !(restaurantInfo.self_pickup
                && (!restaurantInfo.home_delivery
                && !restaurantInfo.pick_and_drop))
                &&(restaurantInfo.merchant_as_delivery_manager ||
                (restaurantInfo.merchant_as_delivery_manager ==0
                        && formSetting.is_marketplace_delivery_availabile))*/

        if (size > 0 && UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {
            if ((storefrontData.getSelfPickup() == 0
                    || storefrontData.getHomeDelivery() == 1
                    || storefrontData.getIsPdFlow())
                    && storefrontData.getIsStorefrontOpened() == 1
                    && (storefrontData.getMerchantAsDeliveryManager() == 1 ||
                    (storefrontData.getMerchantAsDeliveryManager() == 0
                            && StorefrontCommonData.getAppConfigurationData()
                            .getIsMarketPlaceDeliveryAvailabile() == 1)))
                rlCustomOrder.setVisibility(View.VISIBLE);
        } else {
            rlCustomOrder.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                if (isOftenBoughtMode) {
                    if (getIntent().getIntExtra("PRODUCT_SIZE", 0) < Dependencies.getSelectedProductsArrayList().size()) {
                        MyApplication.getInstance().setShowOftenBoughtDialog(true);
                    } else {
                        MyApplication.getInstance().setShowOftenBoughtDialog(false);
                    }
                }
                if (isSideMenu && parentCategoryData == null) {
                    openSideMenu();
                } else if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                    onBackPressed();
                } else {
                    openSideMenu();
                }
                break;
            case R.id.ivSearchHeader:
                Bundle extras = new Bundle();
                extras.putDouble(PICKUP_LATITUDE, pickLatitude);
                extras.putDouble(PICKUP_LONGITUDE, pickLongitude);
                extras.putString(PICKUP_ADDRESS, pickAddress);
                extras.putString(DATE_TIME, preOrderDateTime);
                extras.putBoolean(IS_FROM_MANDATORY_CATEGORY, isFromMandatory);

                extras.putSerializable(STOREFRONT_DATA, storefrontData);

                Transition.transitForResult(mActivity, SearchProductActivity.class, OPEN_SEARCH_PRODUCT_ACTIVITY, extras, false);
                break;
            case R.id.rlTotalQuantity:

               /* if (isOftenBoughtMode) {
                    rlBack.performClick();
                    break;
                }*/


                if (isOftenBoughtMode) {
                    if (getIntent().getIntExtra("PRODUCT_SIZE", 0) < Dependencies.getSelectedProductsArrayList().size()) {
                        MyApplication.getInstance().setShowOftenBoughtDialog(true);
                    } else {
                        MyApplication.getInstance().setShowOftenBoughtDialog(false);
                    }
                }

                if (isFromMandatory) {

                    Bundle extrasMandatory = new Bundle();
                    extrasMandatory.putBoolean(IS_FROM_MANDATORY_CATEGORY, true);
                    Intent intent = new Intent();
                    intent.putExtras(extrasMandatory);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
                    if (storefrontData.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
                        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMinimumSelfPickupAmount() > Dependencies.getProductListSubtotal()) {
                            Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " "
                                    + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount_is) + " "
                                    + Utils.getCurrencySymbol(Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings()) + Utils.getDoubleTwoDigits(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMinimumSelfPickupAmount().intValue()));
                            return;
                        }
                    } else {
                        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
                            Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount_is) + " "
                                    + Utils.getCurrencySymbol(Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings()) + Utils.getDoubleTwoDigits(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder().intValue()));
                            return;
                        }
                    }

                } else {
                    if (minAmountPrice > Dependencies.getProductListSubtotal()) {
                        Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount_is) + " " + Utils.getCurrencySymbol(Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings()) + Utils.getDoubleTwoDigits(minAmountPrice.intValue()));
                        return;
                    }
                }
                if (StorefrontCommonData.getUserData() != null) {
                    if (!Utils.internetCheck(mActivity)) {
                        new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
                        return;
                    }

                    Boolean goToReviewCart = false;

                    for (int i = 0; i < Dependencies.getSelectedProductsArrayList().size(); i++) {
                        if (Dependencies.getSelectedProductsArrayList().get(i).getSelectedQuantity() > 0) {
                            goToReviewCart = true;
                        }
                    }

                    if (!goToReviewCart) {
                        new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.choose_products_for_proceeding).replace(StorefrontCommonData.getString(mActivity, R.string.product), StorefrontCommonData.getTerminology().getProduct())).build().show();
                        return;
                    }

                    Bundle extraa = new Bundle();
                    extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                    extraa.putDouble(PICKUP_LATITUDE, pickLatitude);
                    extraa.putDouble(PICKUP_LONGITUDE, pickLongitude);
                    extraa.putString(PICKUP_ADDRESS, pickAddress);
                    extraa.putBoolean(IS_EDIT_ORDER, isEditOrder);
                    extraa.putBoolean("IS_OFTEN", isOftenBoughtMode);
                    if (isEditOrder)
                        extraa.putInt(EDIT_JOB_ID, editJobId);
                    Transition.openCheckoutActivity(mActivity, extraa);
                }
                break;
            case R.id.rlPreorder:
                new SelectPreOrderTimeDialog(this,
                        new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                            @Override
                            public void onDateTimeSelected(String dateTime) {
                                StorefrontCommonData.getLastMerchantCachedData().setDateTime(dateTime);
                                setPreorderDateTime(dateTime);
                                getMerchantCategories(dateTime);
                                setTotalQuantity();
                            }
                        }).setStorefrontData(storefrontData).show();
                break;
            case R.id.ivShareMerchant:
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
            case R.id.tvAddress:
            case R.id.btnGoToLocationActivity:
                if (!Utils.internetCheck(this)) {
                    new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                    return;
                }
                if (Dependencies.isDemoRunning()) {
                    gotoMapActivity();
                } else {
                    gotoFavLocationActivity();
                }
                break;
            case R.id.llDate:
            case R.id.etFilterDate:
                if (calenderDialog != null) {
                    calenderDialog.show();
                } else {
                    if (checkInDate != null && checkOutDate != null) {
                        openCalenderDialog(checkInDate, checkOutDate);
                    } else {
                        openCalenderDialog(null, null);
                    }
                }
                calenderDialog.setCanceledOnTouchOutside(false);
                calenderDialog.setCancelable(false);
                calenderDialog.show();
                break;
            case R.id.llFilter:
                if (filterData == null) {
                    getFilters();
                } else {
                    openFilterScreen();
                }
                break;
            case R.id.ivLeftArrow:
                if (horizontalLayoutManagerDynamicPage.findFirstVisibleItemPosition() > 0) {
                    rvMerchantDynamicPages.smoothScrollToPosition(horizontalLayoutManagerDynamicPage.findFirstVisibleItemPosition() - 1);
                } else {
                    rvMerchantDynamicPages.smoothScrollToPosition(0);
                }
                break;
            case R.id.ivSelectWishlist:
                selectedWishlist(false);
                break;

            case R.id.ivUnSelectWishlist:
                selectedWishlist(true);

                break;

            case R.id.ivRightArrow:
                rvMerchantDynamicPages.smoothScrollToPosition(horizontalLayoutManagerDynamicPage.findLastVisibleItemPosition() + 1);
                break;

            case R.id.rlCustomOrder:
                isCustomOrderTapped = true;
                getMarketplaceStorefronts();


                break;
        }
    }

    public void gotoFavLocationActivity() {
        Intent intent = new Intent(this, FavLocationActivity.class);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoMapActivity() {
        Intent intent = new Intent(this, AddFromMapActivity.class);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    /**
     * @param startDate startDate if already selected
     * @param endDate   endDate if already selected
     */
    private void openCalenderDialog(String startDate, String endDate) {
        calenderDialog = new SingleBtnDialog(this, startDate, endDate) {

            @Override
            public void onSuccessApply(Date startDate, Date endDate) {
                Log.e("data<><><>", startDate + "  " + endDate);
                String selectStartDate = DateUtils.getInstance().getFormattedDate(startDate, Constants.DateFormat.DATE_FORMAT);
                String selectedEndDate = DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.DATE_FORMAT);
                etFilterDate.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
                etFilterDate.setFloatingLabelText(getStrings(R.string.selected_date));
                etFilterDate.setText(selectStartDate + " - " + selectedEndDate);

                for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                    mPagerAdapter.getMerchantCatalogFragment(i).selectedStartDate = DateUtils.getInstance().getFormattedDate(startDate, Constants.DateFormat.ONLY_DATE);
                    mPagerAdapter.getMerchantCatalogFragment(i).selectedEndDate = DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.ONLY_DATE);
                }
                updateProductFragment();
            }
        };
    }

    @Override
    public void onBackPressed() {
        if ((getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))) {
            if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
                Intent intent = new Intent(mActivity, HomeActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable(STOREFRONT_DATA, storefrontData);
                extras.putDouble(PICKUP_LATITUDE, 0);
                extras.putDouble(PICKUP_LONGITUDE, 0);
                extras.putBoolean(IS_SIDE_MENU, false);
                extras.putBoolean("FROM_DEEP_LINK", true);
                extras.putInt("MERCHANT_ID", deepLinkMerchantId);
                extras.putBoolean("isDirect", true);
                intent.putExtras(extras);
                startActivity(intent);
                AnimationUtils.forwardTransition(mActivity);
                finish();
            } else {
                Intent intent = new Intent(mActivity, RestaurantListingActivity.class);
                startActivity(intent);
                AnimationUtils.forwardTransition(mActivity);
                finish();
            }
        } else if (isWishListChanged) {
            Bundle extrasMandatory = new Bundle();
            extrasMandatory.putBoolean(IS_WISHLIST_CHANGED, true);
            Intent returnIntent = new Intent();
            returnIntent.putExtras(extrasMandatory);
            mActivity.setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            {
                if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                    if (isSideMenu && parentCategoryData == null) {
                        performMainBackPress();
                    } else {
                        finish();
                    }
                } else {
                    performMainBackPress();
                }
            }
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Dependencies.setDemoRun(false);
                } else {
                    Dependencies.setDemoRun(true);
                }
                break;
            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    tvAddress.setText(data.getStringExtra("address"));
                    pickAddress = tvAddress.getText().toString();
                    pickLatitude = data.getDoubleExtra("latitude", 0.0);
                    pickLongitude = data.getDoubleExtra("longitude", 0.0);
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                        getMerchantCategories();
                    } else {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            mPagerAdapter.getMerchantCatalogFragment(i).pickupLatitude = pickLatitude;
                            mPagerAdapter.getMerchantCatalogFragment(i).pickupLongitude = pickLongitude;
                        }
                        updateProductFragment();
                    }
                }
                break;
            case Codes.Request.OPEN_CUSTOM_CHECKOUT_ACTIVITY:
            case Codes.Request.OPEN_MERCHANT_CATALOG_ACTIVITY:
            case Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
            case Codes.Request.OPEN_PRODUCT_DETAILS_SCREEN:
            case Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras() != null) {

                        if ((data.getIntExtra("PRODUCT_SIZE", -1) == 0 && isOftenBoughtMode)) {
                            isOftenBoughtMode=false;
                            updateHeading(" ");
                            updateProductFragment();
                        }
                        if (data.getBooleanExtra(IS_FROM_MANDATORY_CATEGORY, false)) {


//                            Bundle extrasMandatory = new Bundle();
//                            extrasMandatory.putBoolean("is_from_mandatory", true);
                            setResult(RESULT_OK);
                            finish();

                            return;
                        }
                        if (data.getExtras().containsKey(PRODUCT_DATA)) {
                            com.tookancustomer.models.ProductCatalogueData.Datum productData = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getExtras().getSerializable(PRODUCT_DATA);
                            int ItemPos = data.getExtras().getInt(KEY_ITEM_POSITION);

                            try {
                                if (getProductFragment() != null) {
                                    getProductFragment().productCatalogueData.getData().set(ItemPos, productData);
                                    getProductFragment().productCatalogueArrayList.set(ItemPos, productData);
                                    if (getProductFragment().rvProductsList.getAdapter() != null) {
                                        getProductFragment().rvProductsList.getAdapter().notifyDataSetChanged();
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                    updateProductFragment(null, null);
                }
                if (getFormSettings().getProductView() == 1) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                } else {
                    if (!UIManager.isMarketplaceSingleRestaurant) {
                        if (resultCode == Activity.RESULT_OK && parentCategoryData == null) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                setResult(RESULT_OK, data);
                                Transition.exit(mActivity);
                            }
                        } else if (resultCode == Activity.RESULT_OK) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                setResult(RESULT_OK, data);
                                Transition.exit(mActivity);
                            }
                        }
                    } else if (resultCode == Activity.RESULT_OK && parentCategoryData == null) {
                        if (Dependencies.isDemoRunning()) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                setResult(RESULT_OK, data);
                                Transition.exit(mActivity);
                            }
                        } else {
                            if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                                Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                            } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                                Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                            }
                        }

                    } else if (resultCode == Activity.RESULT_OK) {
                        if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null || data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null || data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                            setResult(RESULT_OK, data);
                            Transition.exit(mActivity);
                        }
                    }
                }
                break;

            case Codes.Request.OPEN_FILTER_SCREEN:
                if (resultCode == RESULT_OK) {
//                    filterMap = new HashMap<>();
//                    filterMap = (HashMap<String, ArrayList<AllowValue>>) data.getSerializableExtra(FILTER_LIST_MAP);
                    minPrice = data.getIntExtra(FILTER_MIN_PRICE, 0);
                    maxPrice = data.getIntExtra(FILTER_MAX_PRICE, 0);
//                    selectedFilterList = data.getStringArrayListExtra(SELECTED_FILTER_LIST);
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                        getMerchantCategories();
                    } else {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            mPagerAdapter.getMerchantCatalogFragment(i).minPrice = minPrice;
                            mPagerAdapter.getMerchantCatalogFragment(i).maxPrice = maxPrice;
                        }
                        updateProductFragment();
                    }
                }
                break;
            case REQ_CODE_FILTER:
                if (resultCode == RESULT_OK) {
                    filterData = (Data) data.getSerializableExtra(FiltersActivity.EXTRA_FILTER_DATA);
                    minPrice = data.getIntExtra(FILTER_MIN_PRICE, 0);
                    maxPrice = data.getIntExtra(FILTER_MAX_PRICE, 0);
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                        getMerchantCategories();
                    } else {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            mPagerAdapter.getMerchantCatalogFragment(i).filterData = filterData;
                            mPagerAdapter.getMerchantCatalogFragment(i).minPrice = minPrice;
                            mPagerAdapter.getMerchantCatalogFragment(i).maxPrice = maxPrice;
                        }
                        updateProductFragment();
                    }
                }
                break;
            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    if (isCustomCheckout) {
                        Transition.openCustomCheckoutActivity(mActivity, data.getExtras());
                        isCustomCheckout = false;
                    } else
                        Transition.openCheckoutActivity(mActivity, data.getExtras());
                } else {
                    if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                            Dependencies.clearSelectedProductArraylist();
                            setTotalQuantity();
                            updateProductFragment(null, null);
                        }
                    }
                }
                break;
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;
                    com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                        Dependencies.addCartItem(mActivity, productDataItem);
                    }
                    if (data.hasExtra(KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    setTotalQuantity();
                    updateProductFragment(productDataItem, itemPos);
                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }
                break;
            case Codes.Request.OPEN_CUSTOMISATION_ACTIVITY:
            case Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY:
            case Codes.Request.OPEN_AGENT_LIST_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;
                    com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }
                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    setTotalQuantity();
                    updateProductFragment(productDataItem, itemPos);
                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }
                break;
            case Codes.Request.OPEN_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    StorefrontCommonData.setUserData((UserData) data.getExtras().getSerializable(UserData.class.getName()));
                    boolean isLanguageChanged;
                    isLanguageChanged = data.getExtras().getBoolean("isLanguageChanged");
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                    if (isLanguageChanged)
                        restartActivity();
                }
                break;

        }
    }

    public void restartActivity() {
        if (getIntent().hasExtra(STOREFRONT_MODEL)) {
            getIntent().removeExtra(STOREFRONT_MODEL);
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void getMerchantCategories() {
        getMerchantCategories(preOrderDateTime);
    }

    private void getMerchantCategories(final String dateTime) {
        if (!(getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))) {
            if (StorefrontCommonData.getLastMerchantCachedData().getCategoriesListHashmap().containsKey(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0)) {
                merchantCategoriesData = StorefrontCommonData.getLastMerchantCachedData().getCategoriesListHashmap().get(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0);
                handleMerchantCategory();
            } else {
                startShimmerAnimation(shimmerFrameLayout);
            }
        }

        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(USER_ID, storefrontData.getStorefrontUserId());

        if (!Dependencies.getAccessToken(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }
        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessTokenGuest(mActivity));
            commonParams.add(VENDOR_ID, Dependencies.getVendorIdForGuest(mActivity));
        }
        if (!(getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))) {
            if (parentCategoryData != null) {
                commonParams.add("parent_category_id", parentCategoryData.getCatalogueId());
            }/* else if (UIManager.isMenuEnabled() && storefrontData.getIsMenuEnabled() && storefrontData.getScheduledTask() == 1) {
                commonParams.add("show_all_sub_categories", "1");
            }*/
        } else {
            if (storefrontData.getHasCategories() != 1 || isHas) {
                commonParams.add("parent_category_id", deepLinkCategoryId);
            }
        }
        if (UIManager.isMenuEnabled() && storefrontData.getIsMenuEnabled() && storefrontData.getScheduledTask() == 1) {
            commonParams.add("show_all_sub_categories", "1");
        }

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        if (dateTime != null && !dateTime.isEmpty())
            commonParams.add(DATE_TIME, dateTime);

        if (businessCategoryId != 0 && businessCategoryId != -1 && storefrontData.getBusinessCatalogMappingEnabled() == 1)
            commonParams.add(BUSINESS_CATEGORY_ID, businessCategoryId);


        Dependencies.addCommonParameters(commonParams, mActivity, StorefrontCommonData.getUserData());

        RestClient.getApiInterface(mActivity).getMerchantCategories(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        if (dateTime != null && !dateTime.isEmpty())
                            setPreorderDateTime(dateTime);

                        merchantCategoriesData = baseModel.toResponseModel(MerchantCategoriesData.class);
                        if (!(getIntent().getExtras() != null && getIntent().getBooleanExtra("FROM_DEEP_LINK", false))) {
                            StorefrontCommonData.getLastMerchantCachedData().getCategoriesListHashmap().put(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0, merchantCategoriesData);
                        }
                        if (deepLinkCategoryId != 0) {
                            parentCategoryData = baseModel.toResponseModel(MerchantCategoriesData.class).getResult().get(0);
                            if (parentCategoryData.getHasChildren() == 1) {
                                isHas = true;
                                deepLinkCategoryId = parentCategoryData.getCatalogueId();
                                getMerchantCategories("");
                                //getMarketplaceStorefronts();
                                //   getSingleMerchantDetails(deepLinkMerchantId);
                            } else {
                                isHas = false;
                                if (parentCategoryData.getHasProducts() == 1) {
                                    setMerchantProductsFragment(parentCategoryData);
                                    deepLinkCategoryId = newDeepLinkCategoryId;
                                    for (int i = 0; i < merchantCategoriesData.getResult().size(); i++) {
                                        if (merchantCategoriesData.getResult().get(i).getCatalogueId() == newDeepLinkCategoryId) {
                                            tvHeading.setText(merchantCategoriesData.getResult().get(i).getName());
                                        }
                                    }
                                    rvMerchantCategoriesList.setVisibility(View.GONE);
                                } else {
                                    handleMerchantCategory();
                                }
                                // setMerchantProductsFragment(parentCategoryData);
                            }
                        } else {
                            handleMerchantCategory();
                        }

                        /*if (deepLinkCategoryId != 0 && storefrontData.getHasCategories() == 1) {
                            parentCategoryData = baseModel.toResponseModel(MerchantCategoriesData.class).getResult().get(0);
                            deepLinkCategoryId = parentCategoryData.getCatalogueId();
                            if (parentCategoryData == null) {
                                if (storefrontData.getHasCategories() == 1) {
                                    getMerchantCategories();
                                    getMarketplaceStorefronts();
                                } else {
                                    setMerchantProductsFragment(parentCategoryData);
                                }
                            } else {

                                if (parentCategoryData.getHasChildren() == 1) {
                                    getMerchantCategories();
                                    getMarketplaceStorefronts();
                                } else {
                                    setMerchantProductsFragment(parentCategoryData);
                                }
                            }
                        }*/
                        // handleMerchantCategory();

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        stopShimmerAnimation(shimmerFrameLayout);

                    }
                });
    }

    void handleMerchantCategory() {

        ArrayList<Result> merchantCategoriesList = merchantCategoriesData.getResult();

        if (merchantCategoriesList.size() == 0) {
            rvMerchantCategoriesList.setVisibility(View.GONE);
            mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mPagerAdapter);

            setMerchantProductsFragment(parentCategoryData);
//                            llNoProductAvailableLoc.setVisibility(View.VISIBLE);

        } else if (merchantCategoriesData.getHasChildren() == 0 && (merchantCategoriesList.size() > 0) && storefrontData.getLastLevelCatalogView() == 1) {
            llNoProductAvailableLoc.setVisibility(View.GONE);
            mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mPagerAdapter);
//                            mPagerAdapter.removeAllFragment();

            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setOffscreenPageLimit(merchantCategoriesList.size() + 1);

            for (int i = 0; i < merchantCategoriesList.size(); i++) {
                /* Currently loading only 2 fragments at a time to reduce all hits at same time.*/
                setMerchantProductsFragment(merchantCategoriesList.get(i), i == 0 || i == 1);
            }

            mPagerAdapter.notifyDataSetChanged();

        } else {
            llNoProductAvailableLoc.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);

            MerchantCategoriesAdapter merchantCategoriesAdapter = new MerchantCategoriesAdapter(mActivity, storefrontData, merchantCategoriesList, merchantCategoriesData.getHasImage() == 1, new MerchantCategoriesAdapter.Callback() {
                @Override
                public void onCategoryClick(Result categoryData) {
                    if (!Utils.internetCheck(mActivity)) {
                        new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(STOREFRONT_DATA, storefrontData);
                    bundle.putSerializable(PARENT_CATEGORY_DATA, categoryData);

                    bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, adminSelectedCategories);
                    bundle.putDouble(PICKUP_LATITUDE, pickLatitude);
                    bundle.putDouble(PICKUP_LONGITUDE, pickLongitude);
                    bundle.putString(PICKUP_ADDRESS, pickAddress);
                    bundle.putString(DATE_TIME, "");
                    bundle.putBoolean(IS_EDIT_ORDER, isEditOrder);
                    if (isEditOrder)
                        bundle.putSerializable(EDIT_JOB_ID, editJobId);
                    bundle.putString(CHECK_IN_DATE, checkInDate);
                    bundle.putString(CHECK_OUT_DATE, checkOutDate);
                    bundle.putBoolean(IS_FROM_MANDATORY_CATEGORY, isFromMandatory);


                    Intent intent = new Intent(mActivity, MerchantCatalogActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Codes.Request.OPEN_MERCHANT_CATALOG_ACTIVITY);
                }
            });
            rvMerchantCategoriesList.setVisibility(View.VISIBLE);

            rvMerchantCategoriesList.setAdapter(merchantCategoriesAdapter);
        }

        stopShimmerAnimation(shimmerFrameLayout);


    }

    private void setMerchantProductsFragment(Result parentCategoryData) {
        setMerchantProductsFragment(parentCategoryData, true);
    }

    private void setMerchantProductsFragment(Result parentCategoryData,
                                             boolean hitProductAtInitialisation) {
        viewPager.setVisibility(View.VISIBLE);


        MerchantCatalogFragment merchantCatalogFragment = new MerchantCatalogFragment();

        Bundle args = new Bundle();
        args.putSerializable(STOREFRONT_DATA, storefrontData);
        args.putSerializable(PARENT_CATEGORY_DATA, parentCategoryData);
        args.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, adminSelectedCategories);
        args.putDouble(PICKUP_LATITUDE, pickLatitude);
        args.putDouble(PICKUP_LONGITUDE, pickLongitude);
        args.putString(PICKUP_ADDRESS, pickAddress);
        args.putString(DATE_TIME, preOrderDateTime);
        args.putString(CHECK_IN_DATE, checkInDate);
        args.putString(CHECK_OUT_DATE, checkOutDate);
        args.putInt("DEEP_LINK_PRODUCT_ID", deepLinkProductId);
        args.putInt("DEEP_LINK_CATEGORY_ID", deepLinkCategoryId);
        args.putInt(MIN_PRICE, minPrice);
        args.putInt(MAX_PRICE, maxPrice);
        args.putSerializable(FILTER_DATA, filterData);
        args.putBoolean("HIT_PRODUCTS", hitProductAtInitialisation);
        args.putBoolean(IS_OFTEN_BOUGHT, isOftenBoughtMode);

        merchantCatalogFragment.setArguments(args);

        mPagerAdapter.addFragment(merchantCatalogFragment, parentCategoryData != null ? parentCategoryData.getName() : storefrontData.getStoreName());
        mPagerAdapter.notifyDataSetChanged();

        if (parentCategoryData == null) {
            tabLayout.setVisibility(View.GONE);
        } else {
            if (storefrontData.getLastLevelCatalogView() == 1)
                tabLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initPreorderView() {
        tvPreorderText.setText(getStrings(R.string.pre_order));

        if (StorefrontCommonData.getLastMerchantCachedData().getDateTime() != null &&
                !StorefrontCommonData.getLastMerchantCachedData().getDateTime().isEmpty()) {
            preOrderDateTime = StorefrontCommonData.getLastMerchantCachedData().getDateTime();
        }

        if (parentCategoryData == null && UIManager.isMenuEnabled() && storefrontData.getIsMenuEnabled() && storefrontData.getScheduledTask() == 1) {
            rlPreorder.setVisibility(View.VISIBLE);
            tvPreorderText.setVisibility(View.VISIBLE);
            setPreorderDateTime(preOrderDateTime);
        }
    }

    private void setPreorderDateTime(String dateTime) {
        preOrderDateTime = dateTime;
        tvPreorderText.setVisibility(View.VISIBLE);

        if (StorefrontCommonData.getLastMerchantCachedData().getDateTime() != null &&
                !StorefrontCommonData.getLastMerchantCachedData().getDateTime().isEmpty()) {
            tvPreorderDateTime.setVisibility(View.VISIBLE);
        }

        if (Dependencies.isPreorderSelecetedForMenu())
            tvPreorderDateTime.setVisibility(View.VISIBLE);
        tvPreorderDateTime.setText(DateUtils.getInstance().convertToLocal(dateTime));
    }

    /**
     * set selected date
     */
    private void setSelectedDate() {
        if (checkInDate != null && checkOutDate != null) {
            String selectStartDate = DateUtils.getInstance().parseDateAs(checkInDate, Constants.DateFormat.DATE_FORMAT);
            String selectedEndDate = DateUtils.getInstance().parseDateAs(checkOutDate, Constants.DateFormat.DATE_FORMAT);
            etFilterDate.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
            etFilterDate.setFloatingLabelText(getStrings(R.string.selected_date));
            etFilterDate.setText(selectStartDate + " - " + selectedEndDate);
        }
    }

    public void setMerchantDynamicPages() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(this, StorefrontCommonData.getUserData());
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());
        commonParams.add("is_admin_page", 0);
        commonParams.add("is_active", 1);

        RestClient.getApiInterface(mActivity).getUserPages(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                final UserPagesData userPagesData = baseModel.toResponseModel(UserPagesData.class);
                if (userPagesData.getTemplateData().size() > 0) {
                    rlMerchantDynamicPages.setVisibility(View.VISIBLE);
                } else {
                    rlMerchantDynamicPages.setVisibility(View.GONE);
                }

                if (userPagesData.getTemplateData().size() > 2) {
                    ivRightArrow.setVisibility(View.VISIBLE);
                    ivLeftArrow.setVisibility(View.VISIBLE);
                } else {
                    ivRightArrow.setVisibility(View.GONE);
                    ivLeftArrow.setVisibility(View.GONE);
                }

                ViewTreeObserver viewTreeObserver = rvMerchantDynamicPages.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            rvMerchantDynamicPages.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            MerchantDynamicPagesAdapter adapter = new MerchantDynamicPagesAdapter(mActivity, userPagesData.getTemplateData(), rvMerchantDynamicPages.getWidth());
                            rvMerchantDynamicPages.setAdapter(adapter);
                        }
                    });
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                rlMerchantDynamicPages.setVisibility(View.GONE);
            }
        });
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
                    filterData = baseModel.toResponseModel(Data.class);
                    for (int i = 0; i < filterData.getFilterAndValues().size(); i++) {
                        filterData.getFilterAndValues().get(i).setAllowedValuesWithIsSelected();
                    }

                    if (filterData != null) {
                        openFilterScreen();
                    } else {
                        new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_filters_available)).listener(new AlertDialog.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {

                            }
                        }).build().show();
                    }
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    private void openFilterScreen() {
        startActivityForResult(FiltersActivity.createIntent(mActivity, filterData, minPrice, maxPrice), REQ_CODE_FILTER);
    }

    public void setTotalQuantity() {
        try {
            if (Dependencies.getCartSize() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                    .getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.MULTI_PRODUCT) {
                rlTotalQuantity.setVisibility(View.VISIBLE);
            } else {
                rlTotalQuantity.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String checkoutString = getString(R.string.checkout_quantity_item)
                .replace(CHECKOUT, StorefrontCommonData.getTerminology().getCheckout())
                .replace(QUANTITY, Dependencies.getCartSize() + "")
                .replace(ITEM, (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getTerminology().getItems(false) : StorefrontCommonData.getTerminology().getItem(false)));

        Spannable string = CustomViewsUtil.createSpanForBoldText(mActivity, checkoutString, StorefrontCommonData.getTerminology().getCheckout());
        tvTotalQuantity.setText(string);

        if (Dependencies.getSelectedProductsArrayList().size() > 0)
            try {
                tvSubTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbol(Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal()) + ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            tvSubTotal.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + "" + Utils.getDoubleTwoDigits(Dependencies.getProductListSubtotal()) + ""));


        if (getFormSettings().getShowProductPrice() == 0 && Dependencies.getProductListSubtotal() <= 0) {
            tvSubTotal.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tvTotalQuantity.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            tvTotalQuantity.setLayoutParams(lp);
        } else {
            tvSubTotal.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tvTotalQuantity.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.removeRule(RelativeLayout.CENTER_IN_PARENT);
            tvTotalQuantity.setLayoutParams(lp);
        }

        if (minAmountPrice > Dependencies.getProductListSubtotal()) {

            tvMinOrderAmount.setVisibility(View.VISIBLE);
            tvMinOrderAmount.setText(UIManager.getCurrency(StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount) + " "
                    + Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + Utils.getDoubleTwoDigits(minAmountPrice)));

        } else {
            tvMinOrderAmount.setVisibility(View.GONE);
        }
    }

    public void openCheckoutActivity() {
        if (storefrontData.getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT
                && storefrontData.getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
            rlTotalQuantity.performClick();
        }
    }

    public MerchantCatalogFragment getProductFragment() {
        if (mPagerAdapter.getCount() > 0)
            return mPagerAdapter.getMerchantCatalogFragment(0);
        else return null;
    }

    public void updateProductFragment(com.tookancustomer.models.ProductCatalogueData.Datum
                                              productDataItem, Integer itemPos) {
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            mPagerAdapter.getMerchantCatalogFragment(i).updateProductList(productDataItem, itemPos);
        }
    }

    public void updateProductFragment() {
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            updateProductFragment(i);
        }
    }

    public void updateProductFragment(int tabPosition) {
        if (mPagerAdapter.getMerchantCatalogFragment(tabPosition).productCatalogueData != null) {
            mPagerAdapter.getMerchantCatalogFragment(tabPosition).getProducts(false, 0);
        }
    }

    public void updateProductFragmentIfProductDataNull(int tabPosition) {
        if (mPagerAdapter.getMerchantCatalogFragment(tabPosition).productCatalogueData == null) {
            mPagerAdapter.getMerchantCatalogFragment(tabPosition).getProducts(false, 0);
        }

    }

    private void getSingleMerchantDetails(int merchantId) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        //  final Location location = LocationUtils.getLastLocation(mActivity);

        //  if (mActivity instanceof RestaurantListingActivity) {
        commonParams.add(Keys.APIFieldKeys.LATITUDE, 0.0)
                .add(Keys.APIFieldKeys.LONGITUDE, 0.0);
        //  }
        commonParams.add(USER_ID, merchantId);
        commonParams.add(IS_QR_CODE, 1);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
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
                initViews();
                getMerchantCategories("");
                updateHeading("");


            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 201) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
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
                Utils.snackbarSuccess(mActivity, "Account updated successfully");
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

    public void hideWishlist() {
        ivSelectWishlist.setVisibility(View.GONE);
        ivUnSelectWishlist.setVisibility(View.GONE);
    }


}
