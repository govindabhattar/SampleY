package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.adapters.FreshSortingAdapter;
import com.tookancustomer.adapters.MerchantDynamicPagesAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.comparators.ItemCompareAtoZ;
import com.tookancustomer.comparators.ItemCompareAtoZProduct;
import com.tookancustomer.comparators.ItemComparePriceHighToLowProduct;
import com.tookancustomer.comparators.ItemComparePriceLowToHighProduct;
import com.tookancustomer.comparators.SortSelection;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.FreshSortDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.dialog.SingleBtnDialog;
import com.tookancustomer.fragments.NLevelWorkFlowFragment;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.models.NLevelWorkFlowModel.NLevelWorkFlowData;
import com.tookancustomer.models.ProductFilters.AllowValue;
import com.tookancustomer.models.ProductFilters.Data;
import com.tookancustomer.models.SortResponseModel;
import com.tookancustomer.models.userpages.UserPagesData;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class NLevelWorkFlowActivity extends BaseActivity implements View.OnClickListener, Keys.Extras, LocationFetcher.OnLocationChangedListener {
    private static final int REQ_CODE_FILTER = 1013;
    public NLevelWorkFlowData nLevelWorkFlowData, nLevelWorkFlowDataTemp;
    public int categoryLevel = 0;
    public String headerName = "", headerIcon = "";
    public ArrayList<Integer> parentId = new ArrayList<Integer>();

    public RelativeLayout rlBackHeader, rlSearchHeader, rlFilterSortHeader;
    private EditText etSearch;
    private TextView tvHeading;

    private FrameLayout frameLayoutNLevel;

    public FragmentManager fm;
    private NLevelWorkFlowFragment fragment;
    private FreshSortDialog freshSortDialog;
    private ArrayList<SortResponseModel> slots = new ArrayList<>();
    private TextView tvAddress;
    private Double latitude = 0.0, longitude = 0.0;
    public Double pickLatitude = 0.0, pickLongitude = 0.0;
    public String pickAddress = "";
    private boolean isFirstLocationFetched = true;
    private LocationFetcher locationFetcher;
    private LinearLayout llNoProductAvailableLoc;
    private TextView tvNoProductFoundLoc;
    private android.widget.Button btnGoToLocationActivity;
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private ImageView ivBackHeader;
    private DrawerLayout mDrawerLayout;
    private long lastBackPressed;
    private LinearLayout llfilterView, llDate, llFilter;
    private SingleBtnDialog calenderDialog;
    private MaterialEditText etFilterDate;
    private String checkInDate, checkOutDate = "";
    //    private RecyclerView rvFilterList;
//    private FilterAdapter filterAdapter;
//    private ArrayList<String> selectedFilterList = new ArrayList<>();
    private HashMap<String, ArrayList<AllowValue>> filterMap;
    private int minPrice, maxPrice;
    private Data filterData;
    /**
     * For ECOM flow
     * Seller view with seller name and price
     * and other sellers view
     */
    private String categoryId;
    private String adminSelectedCategories = "";
    private Integer businessCategoryId;

    public LinearLayout llVegSwitchLayout;
    public Switch switchVegFilter;

    public RelativeLayout rlMerchantDynamicPages;
    public RecyclerView rvMerchantDynamicPages;
    public ImageView ivLeftArrow, ivRightArrow;
    LinearLayoutManager horizontalLayoutManager;

    private String preOrderDateTime;
    private ImageView ivPreOrderDateTime;
    private RelativeLayout layoutPreorder;
    private TextView tvPreorderText, tvPreorderDateTime;
    public boolean showProductImages = true;
    private boolean isSideMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nlevel_work_flow);

        if (!UIManager.getCustomOrderActive() && StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 1
                && StorefrontCommonData.getFormSettings().getProductView() == 0 &&
                StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 0) {
            isSideMenu = true;
        }
        mActivity = this;
        nLevelWorkFlowData = (NLevelWorkFlowData) getIntent().getSerializableExtra(NLevelWorkFlowData.class.getName());
        headerName = getIntent().getStringExtra(HEADER_NAME);
        headerIcon = getIntent().getStringExtra(HEADER_LOGO);
        checkInDate = getIntent().getStringExtra(CHECK_IN_DATE);
        checkOutDate = getIntent().getStringExtra(CHECK_OUT_DATE);
        pickAddress = getIntent().getStringExtra(PICKUP_ADDRESS);
        preOrderDateTime = getIntent().getStringExtra(DATE_TIME);
        pickLatitude = getIntent().getDoubleExtra(PICKUP_LATITUDE, 0.0);
        pickLongitude = getIntent().getDoubleExtra(PICKUP_LONGITUDE, 0.0);
        categoryLevel = getIntent().getIntExtra(CATEGORY_LEVEL, 0);
        parentId = getIntent().getIntegerArrayListExtra(PARENT_ID);
        showProductImages = getIntent().getBooleanExtra(SHOW_PRODUCT_IMAGES, true);
        storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) getIntent().getSerializableExtra(STOREFRONT_DATA);
        if (getIntent().hasExtra(ADMIN_CATALOGUE_SELECTED_CATEGORIES)) {
            adminSelectedCategories = getIntent().getStringExtra(ADMIN_CATALOGUE_SELECTED_CATEGORIES);
        }
        UIManager.isMarketplaceSingleRestaurant = isSideMenu;

        if (getIntent().hasExtra(BUSINESS_CATEGORY_ID)) {
            businessCategoryId = getIntent().getIntExtra(BUSINESS_CATEGORY_ID, 0);
        }

        if (parentId == null) {
            parentId = new ArrayList<>();
        }
        if (storefrontData == null) {
            com.tookancustomer.models.userdata.Datum formSettings = StorefrontCommonData.getFormSettings();
            storefrontData = new com.tookancustomer.models.MarketplaceStorefrontModel.Datum(formSettings.getMerchantMinimumOrder(), ""
                    , formSettings.getUserId(), formSettings.getFormName(), formSettings.getLogo(), "", "", ""
                    , "", "", "", 0, "", 0, null, 0,
                    0, "", 0, formSettings.getInstantTask(), formSettings.getScheduledTask(), formSettings.getShowOutStockedProduct(), formSettings.getEnableTookanAgent(),
                    formSettings.getBufferSchedule(), formSettings.getButtons(), formSettings.getBusinessType(), formSettings.getMultipleProductInSingleCart(), formSettings.getPdOrAppointment(), formSettings.getIsStartEndTimeEnable(), 1, formSettings.getIsReviewRatingEnabled()
                    , formSettings.getHomeDelivery(), formSettings.getSelfPickup(), "", 0.0, 0,0,0);
        }
        if (nLevelWorkFlowData == null) {
            nLevelWorkFlowData = new NLevelWorkFlowData();
            pickLatitude = LocationUtils.getLastLocation(mActivity) != null ? LocationUtils.getLastLocation(mActivity).getLatitude() : 0.0;
            pickLongitude = LocationUtils.getLastLocation(mActivity) != null ? LocationUtils.getLastLocation(mActivity).getLongitude() : 0.0;
//            extras.putString(PICKUP_ADDRESS, address);

//                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.ProductTypeData>());


//            nLevelWorkFlowData = Dependencies.getNLevelData();
        } else {
            if (nLevelWorkFlowData.getData().size() > 0) {
                ArrayList<ArrayList<Datum>> nLevelWorkFlowDataArray = new ArrayList<>();
                nLevelWorkFlowDataArray.add(nLevelWorkFlowData.getData().get(0));
                nLevelWorkFlowData.setData(nLevelWorkFlowDataArray);
            }
        }
        if (headerName == null) {
//            headerName = Dependencies.getNLevelHeader();
            headerName = StorefrontCommonData.getFormSettings().getFormName();
        }
        if (headerIcon == null) {
            headerIcon = StorefrontCommonData.getFormSettings().getLogo();
        }

        if (getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE) != null) {
            Utils.snackbarSuccess(mActivity, getIntent().getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
        } else if (getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE) != null) {
            Utils.snackBar(mActivity, getIntent().getStringExtra(Keys.Extras.FAILURE_MESSAGE));
        }

        if (Dependencies.isEcomApp()) {
            if (getIntent().hasExtra(PARENT_CATEGORY_ID)) {
                categoryId = getIntent().getStringExtra(PARENT_CATEGORY_ID);
            }
        }

        SideMenuTransition.setDualUserToggle(mActivity);
        initializeFields();
        initPreorderView();
        setSelectedDate();
        if (StorefrontCommonData.getAppConfigurationData().getIsDynamicPagesActive() == 1 && Dependencies.isLaundryApp())
            setMerchantDynamicPages();

//        if (StorefrontCommonData.getFormSettings().getProductView() == 1) {
//            Location location = LocationUtils.getLastLocation(mActivity);
//            pickLatitude = location != null ? location.getLatitude() : 0.0;
//            pickLongitude = location != null ? location.getLongitude() : 0.0;
//            setAddressFetcher();
//        }


//        ProgressDialog.show(mActivity);
        if (!Dependencies.isEcomApp())
            fragment = new NLevelWorkFlowFragment(preOrderDateTime,
                    storefrontData, StorefrontCommonData.getUserData(), nLevelWorkFlowData, categoryLevel, parentId, pickLatitude, pickLongitude, pickAddress, checkInDate, checkOutDate,
                    false, 0, adminSelectedCategories);
        else
            fragment = new NLevelWorkFlowFragment(storefrontData, StorefrontCommonData.getUserData(), nLevelWorkFlowData, categoryLevel, parentId, pickLatitude, pickLongitude, pickAddress, checkInDate, checkOutDate,
                    false, 0, categoryId, adminSelectedCategories);

        fm.beginTransaction().add(R.id.frameLayoutNLevel, fragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

//        getFilters();


        if (Dependencies.isEcomApp()) {
            tvAddress.setVisibility(View.GONE);
        }
        if (isSideMenu) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


    }

    private void initPreorderView() {
        ivPreOrderDateTime = findViewById(R.id.ivPreOrderDateTime);
        layoutPreorder = findViewById(R.id.layoutPreorder);
        tvPreorderText = findViewById(R.id.tvPreorderText);
        tvPreorderDateTime = findViewById(R.id.tvPreorderDateTime);

        tvPreorderText.setText(getStrings(R.string.pre_order));

        if (categoryLevel == 0 &&
                UIManager.isMenuEnabled() &&
                storefrontData.getIsMenuEnabled() &&
                storefrontData.getScheduledTask() == 1) {
            layoutPreorder.setVisibility(View.VISIBLE);
            tvPreorderText.setVisibility(View.VISIBLE);
            setPreorderDateTime(preOrderDateTime);
//            ivPreOrderDateTime.setVisibility(View.VISIBLE);
        }

        Utils.setOnClickListener(this, layoutPreorder, ivPreOrderDateTime);
    }

    private void setPreorderDateTime(String dateTime) {
        preOrderDateTime = dateTime;
        tvPreorderText.setVisibility(View.VISIBLE);

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

    private void initializeFields() {
        frameLayoutNLevel = findViewById(R.id.frameLayoutNLevel);

//        rvFilterList = findViewById(R.id.rvFilterList);
//        rvFilterList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
//        rvFilterList.setItemAnimator(new DefaultItemAnimator());
//        rvFilterList.setHasFixedSize(false);
//        rvFilterList.addItemDecoration(new SimpleDividerItemDecorationFull(mActivity));

        ivBackHeader = findViewById(R.id.ivBackHeader);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tvAddress = findViewById(R.id.tvAddress);
        tvAddress.setHint(StorefrontCommonData.getTerminology().getAddress());
        etFilterDate = findViewById(R.id.etFilterDate);
        etFilterDate.setHint(getStrings(R.string.select_date));
        etFilterDate.setFloatingLabelText(getStrings(R.string.selected_date));
        ((TextView) findViewById(R.id.tvFilter)).setText(getStrings(R.string.filter));

        llfilterView = findViewById(R.id.llfilterView);
        llFilter = findViewById(R.id.llFilter);
        llDate = findViewById(R.id.llDate);
//        if (Dependencies.isMarketplaceApp() || categoryLevel != 0) {
//            tvAddress.setVisibility(View.GONE);
//        } else {
//            tvAddress.setVisibility(View.VISIBLE);
//        }
        llNoProductAvailableLoc = findViewById(R.id.llNoProductAvailableLoc);
        tvNoProductFoundLoc = findViewById(R.id.tvNoProductFoundLoc);
        tvNoProductFoundLoc.setText(getStrings(R.string.no_products_found_please_change_your_location).replace(getStrings(R.string.product), StorefrontCommonData.getTerminology().getProduct()));

        btnGoToLocationActivity = findViewById(R.id.btnGoToLocationActivity);
        btnGoToLocationActivity.setText(getStrings(R.string.select_location));
        rlBackHeader = findViewById(R.id.rlBackHeader);
        rlSearchHeader = findViewById(R.id.rlSearchHeader);
        rlFilterSortHeader = findViewById(R.id.rlFilterSortHeader);
        etSearch = findViewById(R.id.etSearch);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(headerName);

        rlMerchantDynamicPages = findViewById(R.id.rlMerchantDynamicPages);
        ivLeftArrow = findViewById(R.id.ivLeftArrow);
        ivRightArrow = findViewById(R.id.ivRightArrow);
        rvMerchantDynamicPages = findViewById(R.id.rvMerchantDynamicPages);
        rvMerchantDynamicPages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalLayoutManager = (LinearLayoutManager) rvMerchantDynamicPages.getLayoutManager();

        Utils.setOnClickListener(this, ivLeftArrow, ivRightArrow);

        ((TextView) findViewById(R.id.tvVegOnlyLabel)).setText(StorefrontCommonData.getTerminology().getVegOnly());
        llVegSwitchLayout = findViewById(R.id.llVegSwitchLayout);
//        llVegSwitchLayout.setVisibility(UIManager.getEnableVegNonVegFilter() == 1 ? View.VISIBLE : View.GONE);
        switchVegFilter = findViewById(R.id.switchVegFilter);
        switchVegFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Fragment mFragment = getCurrentFragment();
                if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                    ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, 0);
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Fragment mFragment = getCurrentFragment();
                if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                    ((NLevelWorkFlowFragment) mFragment).searchCatalogue(editable.toString());
                }
            }
        });
        setSortingList();

        //setting menu button visibilty
        if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
            if (Dependencies.isLaundryApp()) {
                rlSearchHeader.setVisibility(View.INVISIBLE);
            } else {
                rlSearchHeader.setVisibility(View.VISIBLE);
            }
            if (isSideMenu && categoryLevel == 0) {
                ivBackHeader.setImageResource(R.drawable.ic_icon_menu);
            } else {
                ivBackHeader.setImageResource(R.drawable.ic_back);
            }
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            tvAddress.setVisibility(View.GONE);
            llfilterView.setVisibility(View.GONE);
        } else {
            llfilterView.setVisibility(UIManager.isShowDateFilter() ? View.VISIBLE : View.GONE);
            tvAddress.setVisibility(View.VISIBLE);
            rlSearchHeader.setVisibility(View.INVISIBLE);
            ivBackHeader.setImageResource(R.drawable.ic_icon_menu);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            if (Dependencies.isEcomApp()) {
                ivBackHeader.setImageResource(R.drawable.ic_back);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                tvAddress.setVisibility(View.GONE);

            }

        }

        if (pickAddress != null) {
            tvAddress.setText(pickAddress);
        }

        fm = getSupportFragmentManager();

        Utils.setOnClickListener(this, tvAddress, rlBackHeader,
                rlSearchHeader, rlFilterSortHeader,
                btnGoToLocationActivity, llDate,
                llFilter, etFilterDate);
    }

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        int i = v.getId();
        if (i == R.id.rlBackHeader || i == R.id.rlBack) {
            if (etSearch.getVisibility() == View.VISIBLE) {
                etSearch.setVisibility(View.GONE);
            } else {
                if (isSideMenu && categoryLevel == 0) {
                    mDrawerLayout.openDrawer(Gravity.START);
                    Utils.clearLightStatusBar(mActivity, mDrawerLayout);
                } else
                    rlBackPerformClick();
            }
        } else if (i == R.id.rlSearchHeader) {
//            if (etSearch.getVisibility() == View.GONE) {
//                etSearch.setVisibility(View.VISIBLE);
//            }

            Bundle extras = new Bundle();
            extras.putDouble(PICKUP_LATITUDE, pickLatitude);
            extras.putDouble(PICKUP_LONGITUDE, pickLongitude);
            extras.putString(PICKUP_ADDRESS, pickAddress);
            extras.putString(DATE_TIME, preOrderDateTime);
            extras.putSerializable(STOREFRONT_DATA, storefrontData);

            Transition.transitForResult(mActivity, SearchProductActivity.class, OPEN_SEARCH_PRODUCT_ACTIVITY, extras, false);

        } else if (i == R.id.rlFilterSortHeader) {
            openFreshSortDialog();

        } else if (v.getId() == R.id.tvAddress || v.getId() == R.id.btnGoToLocationActivity) {
            if (!Utils.internetCheck(this)) {
                new AlertDialog.Builder(this).message(getStrings(R.string.no_internet_try_again)).build().show();
                return;
            }
            if (Dependencies.isDemoRunning()) {
                gotoMapActivity();
            } else {
                gotoFavLocationActivity();
            }
        } else if (i == R.id.llDate || i == R.id.etFilterDate) {

            if (calenderDialog != null) {
//                calenderDialog.dismiss();
                calenderDialog.show();
            } else {
                if (checkInDate != null && checkOutDate != null) {
                    openCalenderDialog(checkInDate, checkOutDate);
//                    openCalenderDialog(null, null);
                } else {
                    openCalenderDialog(null, null);
                }
            }

            calenderDialog.setCanceledOnTouchOutside(false);
            calenderDialog.setCancelable(false);
            calenderDialog.show();
        } else if (i == R.id.llFilter) {
//            Intent intent = new Intent(mActivity, FiltersActivity.class);
//            intent.putExtra(FILTER_DATA, filterData);
//            intent.putExtra(FILTER_MIN_PRICE, minPrice);
//            intent.putExtra(FILTER_MAX_PRICE, maxPrice);
//            intent.putExtra(FILTER_LIST_MAP, filterMap);
//            startActivityForResult(intent, OPEN_FILTER_SCREEN);
            if (filterData == null) {
                getFilters();
            } else {
                openFilterScreen();
            }
        } else if (i == R.id.ivLeftArrow) {
            if (horizontalLayoutManager.findFirstVisibleItemPosition() > 0) {
                rvMerchantDynamicPages.smoothScrollToPosition(horizontalLayoutManager.findFirstVisibleItemPosition() - 1);
            } else {
                rvMerchantDynamicPages.smoothScrollToPosition(0);
            }
        } else if (i == R.id.ivRightArrow) {
            rvMerchantDynamicPages.smoothScrollToPosition(horizontalLayoutManager.findLastVisibleItemPosition() + 1);
        } else if (i == R.id.ivPreOrderDateTime || i == R.id.layoutPreorder) {
            new SelectPreOrderTimeDialog(this,
                    new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                        @Override
                        public void onDateTimeSelected(String dateTime) {
//                            setPreorderDateTime(dateTime);
                            getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                                    storefrontData.getLogo(),
                                    new LatLng(Double.valueOf(storefrontData.getLatitude()),
                                            Double.valueOf(storefrontData.getLongitude())),
                                    new LatLng(latitude, longitude),
                                    "",
                                    storefrontData,
                                    adminSelectedCategories, dateTime);
                        }
                    }).setStorefrontData(storefrontData)
                    .show();
        }
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
                Fragment mFragment = getCurrentFragment();
                if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                    ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude, tvAddress.getText().toString(), DateUtils.getInstance().getFormattedDate(startDate, Constants.DateFormat.ONLY_DATE), DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.ONLY_DATE)
                            , filterData, minPrice, maxPrice);
                }
            }
        };
    }

    public void setSortingList() {
        slots.clear();
        slots.add(new SortResponseModel(0, getStrings(R.string.a_z), false));
        slots.add(new SortResponseModel(1, getStrings(R.string.price_low_high), false));
        slots.add(new SortResponseModel(2, getStrings(R.string.price_high_low), false));
    }

    public void openFreshSortDialog() {
        freshSortDialog = new FreshSortDialog(new FreshSortingAdapter.Callback() {
            @Override
            public void onSlotSelected(int position, SortResponseModel sort) {
                onSortEvent(new SortSelection(position));
                if (freshSortDialog != null) {
                    freshSortDialog.dismiss();
                }
            }
        }, R.style.Feed_Popup_Theme, this, slots);
        freshSortDialog.show(rlFilterSortHeader);
    }

    public void onSortEvent(SortSelection event) {
        try {
            Comparator comparator = null;
            Comparator comparatorProduct = null;
            switch (event.postion) {
                case 0:
                    comparator = new ItemCompareAtoZ();
                    comparatorProduct = new ItemCompareAtoZProduct();
                    break;
                case 1:
                    comparator = new ItemCompareAtoZ();
                    comparatorProduct = new ItemComparePriceLowToHighProduct();
                    break;
                case 2:
                    comparator = new ItemCompareAtoZ();
                    comparatorProduct = new ItemComparePriceHighToLowProduct();
                    break;
                default:
                    break;
            }
            Fragment mFragment = getCurrentFragment();
            if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                ((NLevelWorkFlowFragment) mFragment).setComparator(comparator, comparatorProduct);
            }

        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }
    }

    public void rlBackPerformClick() {
        if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
            onBackPressed();
        } else {
            if (Dependencies.isEcomApp()) {
                finish();
            } else {
                mDrawerLayout.openDrawer(Gravity.START);
                Utils.clearLightStatusBar(mActivity, mDrawerLayout);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
            if (isSideMenu && categoryLevel == 0) {
                finishIfSideMenuActivity();
            } else {
                finish();
            }
        } else {
            if (Dependencies.isEcomApp()) {
                finish();
            } else {
                finishIfSideMenuActivity();
            }
        }
    }

    public void finishIfSideMenuActivity() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
            Utils.setLightStatusBar(mActivity, mDrawerLayout);
        } else {
            long currentTimeStamp = System.currentTimeMillis();
            long difference = currentTimeStamp - lastBackPressed;

            if (difference > 2000) {
                Utils.snackBar(this, getStrings(R.string.tap_again_to_exit_text),false);
                lastBackPressed = currentTimeStamp;
            } else {
                ActivityCompat.finishAffinity(this);
                Transition.exit(this);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null ) {
////                        Bundle bundle = new Bundle();
////                        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
//                        getActivity().setBanner(getActivity().RESULT_OK, data);
//                        Transition.exit(getActivity());
//                    }
//                }
//                break;
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
                        getAppCatalogue();
                    } else {
                        Fragment mFragment = getCurrentFragment();
                        if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                            if (checkInDate != null && checkOutDate != null) {
                                ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude, tvAddress.getText().toString(), checkInDate, checkOutDate, filterData, minPrice, maxPrice);
                            } else {
                                ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude, tvAddress.getText().toString(), null, null, filterData, minPrice, maxPrice);
                            }

                        }
                    }
                }
                break;

            case Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
            case Codes.Request.OPEN_PRODUCT_DETAILS_SCREEN:
            case Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN:
                if (StorefrontCommonData.getFormSettings().getProductView() == 1) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                } else {
                    if (!UIManager.isMarketplaceSingleRestaurant) {
                        if (resultCode == Activity.RESULT_OK && categoryLevel == 0) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                setResult(RESULT_OK, data);
                                Transition.exit(this);
                            }
                        } else if (resultCode == Activity.RESULT_OK && categoryLevel != 0) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                setResult(RESULT_OK, data);
                                Transition.exit(this);
                            }
                        }
                    } else {
                        if (resultCode == Activity.RESULT_OK && categoryLevel == 0) {
                            if (Dependencies.isDemoRunning()) {
                                if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
                                    setResult(RESULT_OK, data);
                                    Transition.exit(this);
                                }
                            } else {
                                if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                                    Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                                } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                                    Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                                }
                            }

//                    setTotalQuantity(true);
//                    activityResultCheckoutScreen(null);
                        }
                    }
                }

                break;

            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    startLocationFetcher();
                }
                break;


            case Codes.Request.OPEN_FILTER_SCREEN:
                if (resultCode == RESULT_OK) {
                    filterMap = new HashMap<>();
                    filterMap = (HashMap<String, ArrayList<AllowValue>>) data.getSerializableExtra(FILTER_LIST_MAP);
                    minPrice = data.getIntExtra(FILTER_MIN_PRICE, 0);
                    maxPrice = data.getIntExtra(FILTER_MAX_PRICE, 0);
//                    selectedFilterList = data.getStringArrayListExtra(SELECTED_FILTER_LIST);
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                        getAppCatalogue();
                    } else {
                        Fragment mFragment = getCurrentFragment();
                        if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                            if (checkInDate != null && checkOutDate != null) {
                                ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude,
                                        tvAddress.getText().toString(), checkInDate, checkOutDate, filterData, minPrice, maxPrice);
                            } else {
                                ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude,
                                        tvAddress.getText().toString(), null, null, filterData, minPrice, maxPrice);
                            }

                        }
                    }
                }
                break;

            case REQ_CODE_FILTER:
                if (resultCode == RESULT_OK) {
                    filterData = (Data) data.getSerializableExtra(FiltersActivity.EXTRA_FILTER_DATA);
                    minPrice = data.getIntExtra(FILTER_MIN_PRICE, 0);
                    maxPrice = data.getIntExtra(FILTER_MAX_PRICE, 0);
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
                        getAppCatalogue();
                    } else {
                        Fragment mFragment = getCurrentFragment();
                        if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
                            if (checkInDate != null && checkOutDate != null) {
                                ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude,
                                        tvAddress.getText().toString(), checkInDate, checkOutDate, filterData, minPrice, maxPrice);
                            } else {
                                ((NLevelWorkFlowFragment) mFragment).getFinalProducts(false, pickLatitude, pickLongitude,
                                        tvAddress.getText().toString(), null, null, filterData, minPrice, maxPrice);
                            }

                        }
                    }


                }

                break;
        }
        Fragment mFragment = getCurrentFragment();
        if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setTotalQuantity(boolean isVisible) {
        Fragment mFragment = getCurrentFragment();
        if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
            ((NLevelWorkFlowFragment) mFragment).setTotalQuantity(isVisible);
        }
    }

    public Fragment getCurrentFragment() {
        return ((NLevelWorkFlowActivity) mActivity).fm.findFragmentById(R.id.frameLayoutNLevel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment mFragment = getCurrentFragment();
        if (mFragment != null && mFragment instanceof NLevelWorkFlowFragment) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        switch (requestCode) {
            case Codes.Permission.LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationFetcher();
                break;
        }
    }

    public void gotoFavLocationActivity() {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, FavLocationActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
    }

    public void gotoMapActivity() {
//      Utils.searchPlace(mActivity, PlaceAutocomplete.MODE_FULLSCREEN, getCurrentLocation());
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AddFromMapActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
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
        Fragment mFragment = getCurrentFragment();

//        Log.e("onLocationChanged", "=" + location);
        if (location == null) {
            return;
        }

        // Check if no location is fetched
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }

        LocationUtils.saveLocation(mActivity, location);
        pickLatitude = location.getLatitude();
        pickLongitude = location.getLongitude();
        if (isFirstLocationFetched) {
            isFirstLocationFetched = false;
            executeSetAddress();
        }
    }

    public void executeSetAddress() {
        Log.e("executeSetAddress", "executeSetAddress called");
        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }


    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");
            return MapUtils.getGapiJson(new LatLng(pickLatitude, pickLongitude), mActivity);
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null && jsonObject.has("address"))
                    tvAddress.setText(jsonObject.getString("address"));
                pickAddress = tvAddress.getText().toString();
//                getAppCatalogue();
            } catch (JSONException e) {
            }
        }
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


    public void getAppCatalogue() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(CURRENT_LATITUDE, pickLatitude)
                .add(CURRENT_LONGITUDE, pickLongitude)
                .add(COMPANY_LATITUDE, StorefrontCommonData.getUserData().getData().getStoreLatitude())
                .add(COMPANY_LONGITUDE, StorefrontCommonData.getUserData().getData().getStoreLongitude())
                .add(DATE_TIME, DateUtils.getInstance().getCurrentDateTimeUtc());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        RestClient.getApiInterface(mActivity).getAppCatalogue(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        frameLayoutNLevel.setVisibility(View.VISIBLE);
                        llNoProductAvailableLoc.setVisibility(View.GONE);

                        NLevelWorkFlowData nLevelWorkFlow = new NLevelWorkFlowData();
                        try {
                            ArrayList<ArrayList<Datum>> mList
                                    = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                    new TypeToken<ArrayList<ArrayList<Datum>>>() {
                                    }.getType());
                            nLevelWorkFlow.setData(mList);
                        } catch (Exception e) {
                        }

                        nLevelWorkFlowData = nLevelWorkFlow;

                        if (nLevelWorkFlowData.getData().size() > 0) {
                            ArrayList<ArrayList<Datum>> nLevelWorkFlowDataArray = new ArrayList<>();
                            nLevelWorkFlowDataArray.add(nLevelWorkFlowData.getData().get(0));
                            nLevelWorkFlowData.setData(nLevelWorkFlowDataArray);
                        }

//                if (fragment != null && fragment.isAdded()) {
////                    fm.beginTransaction().remove(fragment).commit();
//                    frameLayoutNLevel.removeAllViews();
//                }
//                fragment = new NLevelWorkFlowFragment(StorefrontCommonData.getUserData(), nLevelWorkFlowData, categoryLevel, parentId);
//                fm.beginTransaction().add(R.id.frameLayoutNLevel, fragment)
////                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .commit();

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        frameLayoutNLevel.setVisibility(View.GONE);
                        llNoProductAvailableLoc.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
        if (storefrontData != null)
            StorefrontCommonData.getFormSettings().setUserId(storefrontData.getStorefrontUserId());
    }

    public void sideMenuClick(View v) {
        SideMenuTransition.sideMenuClick(v, mActivity);
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
//                llfilter.setVisibility(View.GONE);
//                rvFilterList.setVisibility(View.GONE);
            }
        });

    }

    private void openFilterScreen() {

        startActivityForResult(FiltersActivity
                        .createIntent(NLevelWorkFlowActivity.this, filterData, minPrice, maxPrice),
                REQ_CODE_FILTER);
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

    public void getAppCatalogueV2(String dateTime) {
        setPreorderDateTime(dateTime);
        getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                storefrontData.getLogo(),
                new LatLng(Double.valueOf(storefrontData.getLatitude()),
                        Double.valueOf(storefrontData.getLongitude())),
                new LatLng(latitude, longitude),
                "",
                storefrontData,
                adminSelectedCategories, dateTime);
    }

    public void getAppCatalogueV2(final Activity activity, final String headerName, final String headerLogo,
                                  final LatLng storefrontLatLng, final LatLng currentLatLng, String address,
                                  com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData,
                                  String adminSelectedCategories, String utcDateTime) {
        StorefrontCommonData.getUserData().getData().setStoreLatitude(storefrontLatLng.latitude + "");
        StorefrontCommonData.getUserData().getData().setStoreLongitude(storefrontLatLng.longitude + "");
        getAppCatalogueV2(activity, headerName, headerLogo,
                currentLatLng.latitude, currentLatLng.longitude,
                address, storefrontData, adminSelectedCategories, utcDateTime);
    }

    public void getAppCatalogueV2(final Activity activity, final String headerName, final String headerLogo,
                                  final Double latitude, final Double longitude,
                                  final String address, final com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData,
                                  final String adminSelectedCategories, final String utcDateTime) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());

        commonParams.add(CURRENT_LATITUDE, latitude == null ?
                (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLatitude() : 0.0) :
                latitude)
                .add(CURRENT_LONGITUDE, longitude == null ?
                        (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLongitude() : 0.0) :
                        longitude)
                .add(COMPANY_LATITUDE, StorefrontCommonData.getUserData().getData().getStoreLatitude())
                .add(COMPANY_LONGITUDE, StorefrontCommonData.getUserData().getData().getStoreLongitude());

        commonParams.add(DATE_TIME, utcDateTime);

        if (businessCategoryId != null && businessCategoryId != 0 && businessCategoryId != -1 && storefrontData.getBusinessCatalogMappingEnabled() == 1)
            commonParams.add(BUSINESS_CATEGORY_ID, businessCategoryId);

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        ProgressDialog.show(activity);
        RestClient.getApiInterface(activity).getAppCatalogue(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(activity, false, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        setPreorderDateTime(utcDateTime);
                        try {
                            ArrayList<ArrayList<Datum>> mList
                                    = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                    new TypeToken<ArrayList<ArrayList<Datum>>>() {
                                    }.getType());
                            nLevelWorkFlowData.setData(mList);

                            if (!Dependencies.isEcomApp())
                                fragment = new NLevelWorkFlowFragment(preOrderDateTime,
                                        storefrontData, StorefrontCommonData.getUserData(), nLevelWorkFlowData, categoryLevel, parentId, pickLatitude, pickLongitude, pickAddress, checkInDate, checkOutDate,
                                        false, 0, adminSelectedCategories);
                            else
                                fragment = new NLevelWorkFlowFragment(storefrontData, StorefrontCommonData.getUserData(), nLevelWorkFlowData, categoryLevel, parentId, pickLatitude, pickLongitude, pickAddress, checkInDate, checkOutDate,
                                        false, 0, categoryId, adminSelectedCategories);

                            fm.beginTransaction().replace(R.id.frameLayoutNLevel, fragment)
                                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();


                        } catch (Exception e) {
                        }


                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        ProgressDialog.dismiss();

//                    if (error.getStatusCode() == 103) {
//                        new OptionsDialog.Builder(activity).message(error.getMessage() + " Please change your location.")
//                                .positiveButton(R.string.ok).negativeButton(R.string.cancel).listener(new OptionsDialog.Listener() {
//                            @Override
//                            public void performPositiveAction(int purpose, Bundle backpack) {
//                                Utils.searchPlace(activity, PlaceAutocomplete.MODE_OVERLAY, getCurrentLocation(activity));
//                            }
//
//                            @Override
//                            public void performNegativeAction(int purpose, Bundle backpack) {
//                            }
//                        }).build().show();
//                    } else {
//                        Utils.snackBar(activity, error.getMessage());
//                    }

                    }
                });
    }


}