package com.tookancustomer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.NLevelWorkFlowActivity;
import com.tookancustomer.R;
import com.tookancustomer.adapter.ViewPagerAdapter;
import com.tookancustomer.adapters.NLevelProductsAdapter;
import com.tookancustomer.adapters.NLevelWorkFlowAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MetaInfo;
import com.tookancustomer.models.NLevelWorkFlowModel.NLevelWorkFlowData;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.models.ProductFilters.Data;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.CustomTypefaceSpan;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.tookancustomer.appdata.Codes.StatusCode.NO_DATA_FOUND;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DATE_TIME;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FILTER_END_DATE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FILTER_START_DATE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_PREORDER_SELECTED_FOR_MENU;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LATITUDE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.Extras.KEY_ITEM_POSITION;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Prefs.LONGITUDE;
import static com.tookancustomer.appdata.StorefrontCommonData.getFormSettings;

/**
 * Created by cl-macmini-85 on 12/06/17.
 */

public class NLevelWorkFlowFragment extends Fragment implements View.OnClickListener {
    public Activity mActivity;
    public NLevelWorkFlowActivity mNLevelActivity;

    public UserData userData;
    public NLevelWorkFlowData nLevelWorkFlowData;
    public int categoryLevel = 0;
    public ArrayList<Integer> parentId = new ArrayList<Integer>();

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ViewPagerAdapter mPagerAdapter;
    private TextView tvNoCatalogueFound;
    private RecyclerView rvNLevelWorkFlowList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NLevelWorkFlowAdapter nLevelWorkFlowAdapter;
    private NLevelProductsAdapter nLevelProductsAdapter;
    private ProductCatalogueData productCatalogueData;
    public RelativeLayout rlTotalQuantity;
    public TextView tvTotalQuantity, tvSubTotal, tvMinOrderAmount;

    private Double minAmountPrice = 0.0;

    private Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    private String pickupAddress = "";

    public boolean isMenuSubLayout = false;
    private int menuChildPos = 0;

    private LinearLayout llLoadMoreView;
    private int totalProductsCount = 0, moreProductsAvailable = 1;
    private boolean showImages = true;
    private boolean loading = true;
    private boolean isLoadMore = true;
    private ArrayList<Datum> productCatalogueArrayList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private String selectedStartDate, selectedEndDate;
    private Data filterData;
    private int minPrice, maxPrice;
    private int limit = 30;
    private int mainOffset = 0;
    /**
     * For ECOM flow
     * Seller view with seller name and price
     * and other sellers view
     */
    private String categoryId;
    private String adminSelectedCategories = "";

    private String preOrderDateTime;


    public NLevelWorkFlowFragment() {
    }

    public NLevelWorkFlowFragment(com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData, UserData userData, NLevelWorkFlowData nLevelWorkFlowData, int categoryLevel, ArrayList<Integer> parentId
            , Double pickupLatitude, Double pickupLongitude, String pickupAddress, String checkInDate, String checkOutDate, boolean isMenuSubLayout, int menuChildPos,
                                  String categoryId, String adminSelectedCategories) {
        this.storefrontData = storefrontData;
        this.userData = userData;
        this.nLevelWorkFlowData = nLevelWorkFlowData;
        this.categoryLevel = categoryLevel;
        this.parentId = parentId;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.pickupAddress = pickupAddress;
        this.isMenuSubLayout = isMenuSubLayout;
        this.menuChildPos = menuChildPos;
        this.selectedStartDate = checkInDate;
        this.selectedEndDate = checkOutDate;
        this.categoryId = categoryId;
        this.adminSelectedCategories = adminSelectedCategories;
    }

    public NLevelWorkFlowFragment(String utcDateTime,
                                  com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData,
                                  UserData userData,
                                  NLevelWorkFlowData nLevelWorkFlowData,
                                  int categoryLevel,
                                  ArrayList<Integer> parentId,
                                  Double pickupLatitude, Double pickupLongitude,
                                  String pickupAddress,
                                  String checkInDate,
                                  String checkOutDate,
                                  boolean isMenuSubLayout,
                                  int menuChildPos,
                                  String adminSelectedCategories) {
        this.preOrderDateTime = utcDateTime;
        this.storefrontData = storefrontData;
        this.userData = userData;
        this.nLevelWorkFlowData = nLevelWorkFlowData;
        this.categoryLevel = categoryLevel;
        this.parentId = parentId;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.pickupAddress = pickupAddress;
        this.isMenuSubLayout = isMenuSubLayout;
        this.menuChildPos = menuChildPos;
        this.selectedStartDate = checkInDate;
        this.selectedEndDate = checkOutDate;
        this.adminSelectedCategories = adminSelectedCategories;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_nlevel_work_flow, container, false);

        if (nLevelWorkFlowData != null) {
            if (storefrontData != null) {
                minAmountPrice = storefrontData.getMerchantMinimumOrder();
            } else {
                minAmountPrice = getFormSettings().getMerchantMinimumOrder();
            }
            init(rootView);

            if (!isMenuSubLayout || menuChildPos == 0) {
                setNLevelUI(false);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof NLevelWorkFlowActivity) {
            ((NLevelWorkFlowActivity) getActivity()).setTotalQuantity(true);
        }
    }

    public void init(View v) {
        mActivity = getActivity();
        if (mActivity instanceof NLevelWorkFlowActivity) {
            mNLevelActivity = (NLevelWorkFlowActivity) mActivity;
        }
//        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager = new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);

        viewPager = v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        mPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tvNoCatalogueFound = v.findViewById(R.id.tvNoCatalogueFound);
        tvNoCatalogueFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_products_found).replace(StorefrontCommonData.getString(mActivity, R.string.product).toLowerCase(), StorefrontCommonData.getTerminology().getProduct()));
        Log.e("tvNoCatalogueFound", StorefrontCommonData.getString(mActivity, R.string.no_products_found).replace(StorefrontCommonData.getString(mActivity, R.string.product), StorefrontCommonData.getTerminology().getProduct()));
        Log.e("no_products_found", StorefrontCommonData.getTerminology().getProduct().toString());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                try {
                    if (mPagerAdapter.getFragment(tab.getPosition()).rvNLevelWorkFlowList.getAdapter() == null) {
//                                ProgressDialog.show(mActivity);
                        mPagerAdapter.getFragment(tab.getPosition()).setNLevelUI();
                    }
                } catch (Exception e) {
//                            ProgressDialog.dismiss();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
//        if (categoryLevel > nLevelWorkFlowData.getData().size() - 1) {
        if (getFormSettings().getProductView() == 1) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(false);

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (categoryLevel > nLevelWorkFlowData.getData().size() - 1) {
//                    int layoutType = getLayoutType(true);
                    getFinalProducts(true);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        llLoadMoreView = v.findViewById(R.id.llLoadMoreView);
        rvNLevelWorkFlowList = v.findViewById(R.id.rvNLevelWorkFlowList);
        rvNLevelWorkFlowList.setLayoutManager(linearLayoutManager);
//        rvNLevelWorkFlowList.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));


//        rvNLevelWorkFlowList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        rvNLevelWorkFlowList.getItemAnimator().setChangeDuration(0);
        rlTotalQuantity = v.findViewById(R.id.rlTotalQuantity);
        tvTotalQuantity = v.findViewById(R.id.tvTotalQuantity);
        tvSubTotal = v.findViewById(R.id.tvSubTotal);


        Utils.setVisibility(View.GONE, rlTotalQuantity);
        Utils.setOnClickListener(this, rlTotalQuantity);

        tvMinOrderAmount = v.findViewById(R.id.tvMinOrderAmount);

        if (!Dependencies.isEcomApp()) {
            nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), getFragmentManager(), productCatalogueArrayList, parentId, showImages);
        } else {
            nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), getFragmentManager(), productCatalogueArrayList, parentId, categoryId, showImages);
        }

//        nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), productCatalogueArrayList, parentId);

        rvNLevelWorkFlowList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (productCatalogueData != null) {
                    int visibleItemCount, totalItemCount, pastVisiblesItems;

                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = rvNLevelWorkFlowList.getLayoutManager().getChildCount();
                        totalItemCount = rvNLevelWorkFlowList.getLayoutManager().getItemCount();
                        try {
                            pastVisiblesItems = ((LinearLayoutManager) rvNLevelWorkFlowList.getLayoutManager()).findFirstVisibleItemPosition();
                        } catch (Exception e) {
                            pastVisiblesItems = 0;
                        }

                        if (loading &&
//                                totalItemCount >= (limit + mainOffset) &&
                                (getFormSettings().getProductView() == 1 ? moreProductsAvailable == 1 : totalItemCount < totalProductsCount)) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = false;


                                if (!Utils.internetCheck(mActivity)) {
                                    new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).listener(new AlertDialog.Listener() {
                                        @Override
                                        public void performPostAlertAction(int purpose, Bundle backpack) {
                                            loading = true;
                                        }
                                    }).build().show();
                                } else {
                                    llLoadMoreView.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getFinalProducts(true, getFormSettings().getProductView() == 1 ? mainOffset + limit : rvNLevelWorkFlowList.getLayoutManager().getItemCount());

//                                            getFinalProducts(true, rvNLevelWorkFlowList.getLayoutManager().getItemCount());
                                        }
                                    }, 1000);
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    private void setNLevelUI() {
        ProgressDialog.show(mActivity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setNLevelUI(true);
            }
        }, 400);
    }

    private void setNLevelUI(final boolean isMenuSubLayout) {

        if (!Utils.internetCheck(mActivity)) {
            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).button(StorefrontCommonData.getString(mActivity, R.string.retry_text)).listener(new AlertDialog.Listener() {
                @Override
                public void performPostAlertAction(int purpose, Bundle backpack) {
                    setNLevelUI(isMenuSubLayout);
                }
            }).build().show();
        } else {
            UpdateNLevelUI updateNLevelUI = new UpdateNLevelUI();
            updateNLevelUI.execute();
        }
    }


    private class UpdateNLevelUI extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getActivity() != null)
                ProgressDialog.show(getActivity());

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (categoryLevel > nLevelWorkFlowData.getData().size() - 1) {
//            int layoutType = getLayoutType(true);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            getFinalProducts(false);
                        }
                    });
                } else {
                    boolean isAllDummy = true;

                    if (parentId.size() == 0) {
//                    if (nLevelWorkFlowData.getData().get(categoryLevel).size() > 0) {
//                        if (nLevelWorkFlowData.getData().get(categoryLevel).get(0).getIsDummy() == 0) {

//                            isAllDummy = false;
//                        }
//                    }
                        for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
                            if (nLevelWorkFlowData.getData().get(categoryLevel).get(i).getIsDummy() == 0) {
                                isAllDummy = false;
                                break;
                            }
                        }
                    } else {
//                    if ( nLevelWorkFlowData.getData().get(categoryLevel).size()> 0) {
//                            if (nLevelWorkFlowData.getData().get(categoryLevel).get(0).getIsDummy() == 0 ) {
//                                isAllDummy = false;
//                        }
//                    }
                        for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
                            for (int j = 0; j < parentId.size(); j++) {
                                if (nLevelWorkFlowData.getData().get(categoryLevel).get(i).getIsDummy() == 0 && nLevelWorkFlowData.getData().get(categoryLevel).get(i).getParentCategoryId().equals(parentId.get(j))) {
                                    isAllDummy = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (!isAllDummy) {
                        int layoutType = getLayoutType(false);

                        if (layoutType == Constants.NLevelLayoutType.MENU_LAYOUT.layoutValue) {
//                    swipeRefreshLayout.setVisibility(View.GONE);

                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tabLayout.setVisibility(View.VISIBLE);
                                            viewPager.setVisibility(View.VISIBLE);
                                            viewPager.setOffscreenPageLimit(nLevelWorkFlowData.getData().get(categoryLevel).size() + 1);
                                            rvNLevelWorkFlowList.setVisibility(View.GONE);

                                            for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
                                                if (parentId.size() == 0) {
                                                    ArrayList<Integer> tempParentId = new ArrayList<>();
                                                    tempParentId.add(nLevelWorkFlowData.getData().get(categoryLevel).get(i).getCatalogueId());
                                                    NLevelWorkFlowFragment nLevelWorkFlowFragment =
                                                            new NLevelWorkFlowFragment(preOrderDateTime,
                                                                    storefrontData,
                                                                    userData, nLevelWorkFlowData, categoryLevel + 1,
                                                                    tempParentId, pickupLatitude, pickupLongitude,
                                                                    pickupAddress, selectedStartDate, selectedEndDate,
                                                                    true, i, adminSelectedCategories);
                                                    mPagerAdapter.addFragment(nLevelWorkFlowFragment, nLevelWorkFlowData.getData().get(categoryLevel).get(i).getName());
//                                    viewPager.setAdapter(mPagerAdapter);
                                                    mPagerAdapter.notifyDataSetChanged();
                                                } else {
                                                    for (int j = 0; j < parentId.size(); j++) {
                                                        if (nLevelWorkFlowData.getData().get(categoryLevel).get(i).getParentCategoryId().equals(parentId.get(j))) {
                                                            ArrayList<Integer> tempParentId = new ArrayList<>();
                                                            tempParentId.add(nLevelWorkFlowData.getData().get(categoryLevel).get(i).getCatalogueId());
                                                            NLevelWorkFlowFragment nLevelWorkFlowFragment =
                                                                    new NLevelWorkFlowFragment(preOrderDateTime,
                                                                            storefrontData,
                                                                            userData, nLevelWorkFlowData, categoryLevel + 1,
                                                                            tempParentId, pickupLatitude, pickupLongitude,
                                                                            pickupAddress, selectedStartDate, selectedEndDate,
                                                                            true, mPagerAdapter.getCount(), adminSelectedCategories);
                                                            mPagerAdapter.addFragment(nLevelWorkFlowFragment, nLevelWorkFlowData.getData().get(categoryLevel).get(i).getName());
//                                            viewPager.setAdapter(mPagerAdapter);
                                                            mPagerAdapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }
                                            }
                                            mPagerAdapter.notifyDataSetChanged();
//                            viewPager.setAdapter(mPagerAdapter);

//                    float myTabLayoutSize = 360;
//                    if (DeviceInfo.getWidthDP(this) >= myTabLayoutSize ){
//                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//                    } else {
//                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//                    }
//                                changeTabsFont();

                                        }
                                    }, 100);

                                }
                            });

                        } else {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressDialog.dismiss();
                                    rvNLevelWorkFlowList.setLayoutManager(linearLayoutManager);
                                    setWorkflowAdapter("");
                                }
                            });
                        }
                    } else {
                        ArrayList<Integer> tempParentId = new ArrayList<>();

                        if (parentId.size() == 0) {
                            for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
                                tempParentId.add(nLevelWorkFlowData.getData().get(categoryLevel).get(i).getCatalogueId());
                            }
                        } else {
                            for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
                                for (int j = 0; j < parentId.size(); j++) {
                                    if (nLevelWorkFlowData.getData().get(categoryLevel).get(i).getCatalogueId().equals(parentId.get(j))) {
                                        tempParentId.add(nLevelWorkFlowData.getData().get(categoryLevel).get(i).getCatalogueId());
                                    }
                                }
                            }
                        }

                        parentId.clear();
                        parentId.addAll(tempParentId);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (parentId.size() == 0) {
                                    tvMinOrderAmount.setVisibility(View.GONE);
                                    tvNoCatalogueFound.setVisibility(View.VISIBLE);
                                    rvNLevelWorkFlowList.setVisibility(View.GONE);
                                    ProgressDialog.dismiss();
                                } else {
                                    categoryLevel = categoryLevel + 1;
                                    setNLevelUI(false);
                                }
                            }
                        });
                    }
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            ProgressDialog.dismiss();
        }
    }


    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    try {
                        if (nLevelWorkFlowData.getData().get(categoryLevel).get(i).getLayoutData().getLines().size() > 0) {
                            ((TextView) tabViewChild).setTypeface(
                                    Font.getTypeFaceNLevel(getActivity(),
                                            Constants.NLevelAppStyles.getAppFontByValue(getActivity(),
                                                    nLevelWorkFlowData.getData().get(categoryLevel).get(i).getLayoutData().getLines().get(0).getStyle())));
                        } else {
                            ((TextView) tabViewChild).setTypeface(
                                    Font.getTypeFaceNLevel(getActivity(),
                                            Constants.NLevelAppStyles.getAppFontByValue(getActivity(), -1)));
                        }
                    } catch (Exception e) {
                        ((TextView) tabViewChild).setTypeface(
                                Font.getTypeFaceNLevel(getActivity(),
                                        Constants.NLevelAppStyles.getAppFontByValue(getActivity(), -1)));

                    }
                }
            }
        }
    }

    public void getFinalProducts(final boolean isSwipeRefresh) {
        getFinalProducts(isSwipeRefresh, 0);
    }

    public void getFinalProducts(final boolean isSwipeRefresh, final Double lat, final Double lng, final String address,
                                 final String startdate, final String endDate, final Data filtersData
            , final int minFilterPrice, final int maxFilterPrice) {
        pickupAddress = address;
        pickupLatitude = lat;
        pickupLongitude = lng;
        selectedStartDate = startdate;
        selectedEndDate = endDate;
        filterData = filtersData;
        minPrice = minFilterPrice;
        maxPrice = maxFilterPrice;
        getFinalProducts(isSwipeRefresh, 0);
//        setFilterData(selectedFilterListMap);
    }

    /**
     * setFilter data
     */
    private JSONObject setFilterData(Data filterData) {
        JSONObject jsonObj = null;
        if (filterData != null) {

            for (int i = 0; i < filterData.getFilterAndValues().size(); i++) {

                jsonObj = new JSONObject();

                ArrayList<String> selectedList = new ArrayList<>();
                for (int j = 0; j < filterData.getFilterAndValues().get(i).getAllowedValuesWithIsSelected().size(); j++) {
                    if (filterData.getFilterAndValues().get(i).getAllowedValuesWithIsSelected().get(j).isChecked())
                        selectedList.add(filterData.getFilterAndValues().get(i).getAllowedValuesWithIsSelected().get(j).getDisplayName().toString());
                }

                try {
                    jsonObj.put(filterData.getFilterAndValues().get(i).getDisplayName(), new JSONArray(selectedList));
                } catch (JSONException e) {

                               Utils.printStackTrace(e);
                }

                Log.v("setFilterData", jsonObj.toString());

                return jsonObj;


            }
        }
        return jsonObj;

    }


    public void getFinalProducts(final boolean isSwipeRefresh, final int offset) {
        mainOffset = offset;
        if (!Utils.internetCheck(mActivity)) {
//            new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity,R.string.no_internet_try_again)).build().show();
            llLoadMoreView.setVisibility(View.GONE);
            loading = true;
            ProgressDialog.dismiss();
            return;
        }


        Location location = LocationUtils.getLastLocation(getActivity());
        String parentIdString = "";
        if (parentId != null && parentId.size() > 0) {
            for (int i = 0; i < parentId.size(); i++) {
                if (i == parentId.size() - 1) {
                    parentIdString = parentId.get(0) + "";
                }
            }
        }
//        if (StorefrontCommonData.getAuthenticateMerchantData() == null)
//            return;

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, userData);
        commonParams.add("point", location.getLatitude() + " " + location.getLongitude());
        commonParams.add("limit", limit);
        commonParams.add("offset", offset);

        commonParams.remove(USER_ID);
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());

        if (mActivity instanceof NLevelWorkFlowActivity) {
            ((NLevelWorkFlowActivity) mActivity).llVegSwitchLayout.setVisibility((StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegFilter() == 1
                    && storefrontData.isVegFilterActive()) ? View.VISIBLE : View.GONE);
            if (((NLevelWorkFlowActivity) mActivity).switchVegFilter.isChecked()) {
                commonParams.add("is_veg", 1);
            }
        }

        if (!adminSelectedCategories.isEmpty()) {
            commonParams.add("parent_category_id", adminSelectedCategories);
        } else {
            if (!(parentIdString.equals("") || parentIdString.isEmpty())) {
                commonParams.add("parent_category_id", parentIdString);
            } else {
//            commonParams.add("parent_category_id", 7861);
            }
        }
        if (getFormSettings().getProductView() == 1) {
//            commonParams.build().getMap().remove(REFERENCE_ID);
            commonParams.build().getMap().remove("point");
            commonParams.add(LATITUDE, pickupLatitude);
            commonParams.add(LONGITUDE, pickupLongitude);

            if (filterData != null && setFilterData(filterData) != null) {
                commonParams.add("custom_fields", setFilterData(filterData));
                Log.v("customFields", setFilterData(filterData).toString());
            }


            JSONObject jsonOjMain = new JSONObject();
            try {
                jsonOjMain.put("min", minPrice);
                jsonOjMain.put("max", maxPrice);
            } catch (JSONException e) {

                               Utils.printStackTrace(e);
            }

            if (minPrice != 0 && maxPrice != 0) {
                commonParams.add("price_range", jsonOjMain);
            }


            if (selectedStartDate != null && selectedEndDate != null) {
                commonParams.add(FILTER_START_DATE, selectedStartDate);
                commonParams.add(FILTER_END_DATE, selectedEndDate);
            }
//            commonParams.build().getMap().remove(APP_VERSION);
//            commonParams.build().getMap().remove(YELO_APP_TYPE);
//            commonParams.build().getMap().remove(DEVICE_TOKEN);
//            commonParams.build().getMap().remove(USER_ID);
        }

        commonParams.add(DATE_TIME, preOrderDateTime != null && !preOrderDateTime.isEmpty() ? preOrderDateTime : DateUtils.getInstance().getCurrentDateTimeUtc());
//        Dependencies.setPreOrderDateTime(preOrderDateTime);

        Log.e("preOrderDateTime", preOrderDateTime + "===" + DateUtils.getInstance().getCurrentDateTimeUtc());

        if (!isSwipeRefresh)
            ProgressDialog.show(getActivity());

        if (getFormSettings().getProductView() == 0) {
            commonParams.add(IS_PREORDER_SELECTED_FOR_MENU, Dependencies.getIsPreorderSelecetedForMenu());

            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
            }

            RestClient.getApiInterface(getActivity()).getProductCatalogue(commonParams.build().getMap()).enqueue(finalProductResponseResolver(offset));
        } else {
            if (Dependencies.isEcomApp()) {
                CommonParams.Builder parmasBuilder = Dependencies.setCommonParamsForAPI(mActivity, userData);
                parmasBuilder.add("category_id", categoryId);

                RestClient.getApiInterface(getActivity()).getProductWithSeller(parmasBuilder.build().getMap()).enqueue(finalProductResponseResolver(offset));
            } else {

                RestClient.getApiInterface(getActivity()).getMarketplaceProducts(commonParams.build().getMap()).enqueue(finalProductResponseResolver(offset));
            }
        }
    }

    private ResponseResolver<BaseModel> finalProductResponseResolver(final int offset) {

        return new ResponseResolver<BaseModel>(getActivity(), false, false) {
            @Override
            public void success(BaseModel baseModel) {
                UpdateNLevelProductData updateNLevelProductData = new UpdateNLevelProductData(offset);
                updateNLevelProductData.execute(baseModel);
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 900 && productCatalogueData != null) {
                    Utils.snackBar(mActivity, error.getMessage());
                } else {
                    tvNoCatalogueFound.setVisibility(View.VISIBLE);
                    rvNLevelWorkFlowList.setVisibility(View.GONE);

                    if (error.getStatusCode() == NO_DATA_FOUND.getStatusCode()
                            && error.getMessage().equalsIgnoreCase(StorefrontCommonData.getString(mActivity, R.string.no_data_found_backend_message))) {
                    } else {
                        new AlertDialog.Builder(getActivity()).message(error.getMessage()).build().show();
                    }
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                llLoadMoreView.setVisibility(View.GONE);
                loading = true;
                ProgressDialog.dismiss();
            }
        };
    }


    private class UpdateNLevelProductData extends AsyncTask<BaseModel, Void, ArrayList<Datum>> {
        int offset;

        private UpdateNLevelProductData(int offset) {
            this.offset = offset;
        }

        @Override
        protected ArrayList<Datum> doInBackground(BaseModel... baseModels) {
            BaseModel baseModel = baseModels[0];

            ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();

            try {
                MetaInfo metaInfo = baseModel.toResponseModelMetaInfo(MetaInfo.class);
                if (metaInfo != null && metaInfo.getTotalCount() >= 0)
                    totalProductsCount = metaInfo.getTotalCount();

                if (metaInfo != null) {
                    moreProductsAvailable = metaInfo.getMoreProductsAvailable();
                    showImages = metaInfo.getShowImages() == 1;
                    if (nLevelProductsAdapter != null)
                        nLevelProductsAdapter.showProductImage = showImages;
                }

                Datum[] datum = baseModel.toResponseModel(Datum[].class);
                productCatalogueDataResponse.setData(new ArrayList<Datum>(Arrays.asList(datum)));
            } catch (Exception e) {

                               Utils.printStackTrace(e);
            }

            if (productCatalogueDataResponse.getData().size() == 0) {
                isLoadMore = false;
            } else {
                isLoadMore = true;
            }

            for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {
//                if (productCatalogueDataResponse.getData().get(i).getProductEnabled() == 0 || (storefrontData.getShowOutstockedProduct() == 0 && productCatalogueDataResponse.getData().get(i).getAvailableQuantity() == 0 &&  productCatalogueDataResponse.getData().get(i).getInventoryEnabled()==1)) {
//                    productCatalogueDataResponse.getData().remove(i);
//                    i--;
////                    totalProductsCount--;
//                } else {
                productCatalogueDataResponse.getData().get(i).setStorefrontData(storefrontData);
//                productCatalogueDataResponse.getData().get(i).setSellerId(userData.getData().getVendorDetails().getUserId());
                productCatalogueDataResponse.getData().get(i).setFormId(getFormSettings().getFormId());
                productCatalogueDataResponse.getData().get(i).setVendorId(userData.getData().getVendorDetails().getVendorId());
                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                    if (productCatalogueDataResponse.getData().get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                        productCatalogueDataResponse.getData().get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                        productCatalogueDataResponse.getData().get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                        productCatalogueDataResponse.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());
                        productCatalogueDataResponse.getData().get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());
                        productCatalogueDataResponse.getData().get(i).setPaymentSettings(Dependencies.getSelectedProductsArrayList().get(j).getPaymentSettings());

                        productCatalogueDataResponse.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());

                        Dependencies.getSelectedProductsArrayList().set(j, productCatalogueDataResponse.getData().get(i));
                    }
                }
//                }
            }

            if (offset == 0) {
                productCatalogueData = productCatalogueDataResponse;

                productCatalogueArrayList.clear();
                productCatalogueArrayList.addAll(productCatalogueData.getData());
            } else {
                ArrayList<Datum> datumArrayList = productCatalogueData.getData();
                datumArrayList.addAll(productCatalogueDataResponse.getData());
                productCatalogueData.setData(datumArrayList);

                productCatalogueArrayList.addAll(productCatalogueDataResponse.getData());
            }

            return productCatalogueDataResponse.getData();
        }

        @Override
        protected void onPostExecute(ArrayList<Datum> data) {
            super.onPostExecute(data);

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            if (productCatalogueData.getData().size() == 0) {
                tvNoCatalogueFound.setVisibility(View.VISIBLE);
                tvMinOrderAmount.setVisibility(View.GONE);
            } else {
                if (!isMenuSubLayout)
//                    if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
//                        tvMinOrderAmount.setVisibility(View.GONE);
//                    } else {
                    tvMinOrderAmount.setVisibility(View.VISIBLE);
//                    }
//                    tvMinOrderAmount.setVisibility(View.VISIBLE);
            }


            if (productCatalogueData.getData().size() == 0) {
                tvNoCatalogueFound.setVisibility(View.VISIBLE);
            } else {
                tvNoCatalogueFound.setVisibility(View.GONE);
            }

            int layoutType = getLayoutType(true);

            if (getActivity() instanceof NLevelWorkFlowActivity) {
                ((NLevelWorkFlowActivity) getActivity()).setTotalQuantity(true);
            }

            rvNLevelWorkFlowList.setLayoutManager(linearLayoutManager);
//            rvNLevelWorkFlowList.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));


            if (nLevelProductsAdapter == null) {
                if (!Dependencies.isEcomApp()) {
                    nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), getFragmentManager(), productCatalogueArrayList, parentId, showImages);
                } else {
                    nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), getFragmentManager(), productCatalogueArrayList, parentId, categoryId, showImages);
                }
            }

            if (rvNLevelWorkFlowList.getAdapter() != null) {
//                        nLevelProductsAdapter.setDataList(productCatalogueArrayList);
                if (productCatalogueArrayList.size() == 0) {
                    rvNLevelWorkFlowList.getAdapter().notifyDataSetChanged();
                } else {
                    rvNLevelWorkFlowList.getAdapter().notifyItemRangeInserted(offset, data.size());
                }
//                        rvNLevelWorkFlowList.getAdapter().notifyDataSetChanged();
            } else {
                rvNLevelWorkFlowList.setAdapter(nLevelProductsAdapter);
            }

            llLoadMoreView.setVisibility(View.GONE);
            loading = true;
            ProgressDialog.dismiss();

        }
    }

    public void setTotalQuantity(boolean isVisible) {
        if (isVisible && Dependencies.getCartSize() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                .getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.MULTI_PRODUCT) {
            rlTotalQuantity.setVisibility(View.VISIBLE);
        } else {
            rlTotalQuantity.setVisibility(View.GONE);
        }

        String checkoutString = (StorefrontCommonData.getTerminology().getCheckout() + " (" + Dependencies.getCartSize() + " " + (Dependencies.getCartSize() > 1 ? StorefrontCommonData.getString(mActivity, R.string.items) : StorefrontCommonData.getString(mActivity, R.string.item)) + ")");
        Spannable sb = new SpannableString(checkoutString);
//        sb.setSpan((Font.getSemiBold(getActivity())), checkoutString.indexOf(StorefrontCommonData.getString(mActivity,R.string.checkout)), (StorefrontCommonData.getString(mActivity,R.string.checkout).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
        sb.setSpan(new CustomTypefaceSpan("", Font.getSemiBold(getActivity())), checkoutString.indexOf(StorefrontCommonData.getTerminology().getCheckout()), ((StorefrontCommonData.getTerminology().getCheckout()).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
        tvTotalQuantity.setText(sb);

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
            if (tvNoCatalogueFound.getVisibility() == View.GONE)
                if (!isMenuSubLayout)
//                    if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
//                        tvMinOrderAmount.setVisibility(View.GONE);
//                    } else {
                    tvMinOrderAmount.setVisibility(View.VISIBLE);
//                    }
                else
                    tvMinOrderAmount.setVisibility(View.GONE);

            tvMinOrderAmount.setText(StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount) + " " + UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(minAmountPrice)));
//            rlTotalQuantity.setEnabled(false);
//            btnBookNow.setEnabled(false);
        } else {
            tvMinOrderAmount.setVisibility(View.GONE);
//            rlTotalQuantity.setEnabled(true);
//            btnBookNow.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        int i1 = v.getId();
        if (i1 == R.id.rlBack) {
            mNLevelActivity.rlBackPerformClick();

        } else if (i1 == R.id.rlTotalQuantity) {
            /*if (!validateCartData())
                return;*/

            if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null) {
                if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder() > Dependencies.getProductListSubtotal()) {
                    Utils.snackBar(getActivity(), StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount_is) + " " + UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMerchantMinimumOrder().intValue())));
                    return;
                }
            } else {
                if (minAmountPrice > Dependencies.getProductListSubtotal()) {
                    Utils.snackBar(getActivity(), StorefrontCommonData.getString(mActivity, R.string.minimum) + " " + Utils.getCallTaskAs(true, false) + " " + StorefrontCommonData.getString(mActivity, R.string.minimum_order_amount_is) + " " + UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(minAmountPrice.intValue())));
                    return;
                }
            }
            if (userData != null) {

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
                    new AlertDialog.Builder(getActivity()).message(StorefrontCommonData.getString(mActivity, R.string.choose_products_for_proceeding).replace(StorefrontCommonData.getString(mActivity, R.string.product), StorefrontCommonData.getTerminology().getProduct())).build().show();
                    return;
                }

                Bundle extraa = new Bundle();
                extraa.putSerializable(UserData.class.getName(), userData);
                extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                extraa.putDouble(PICKUP_LATITUDE, pickupLatitude);
                extraa.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                extraa.putString(PICKUP_ADDRESS, pickupAddress);
                Transition.openCheckoutActivity(getActivity(), extraa);
                // Intent intent1 = new Intent(getActivity(), MapActivity.class);
//                Intent intent1 = new Intent(getActivity(), CheckOutActivity.class);
//                intent1.putExtras(extraa);
//                startActivityForResult(intent1, OPEN_CHECKOUT_SCREEN);
//                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        } else if (v.getId() == R.id.layoutPreorder) {
            new SelectPreOrderTimeDialog(mActivity,
                    new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                        @Override
                        public void onDateTimeSelected(String dateTime) {
                            ((NLevelWorkFlowActivity) mActivity).getAppCatalogueV2(dateTime);
                        }
                    }).setStorefrontData(storefrontData)
                    .show();


        }
    }

    private boolean validateCartData() {
        ArrayList<Datum> productList = Dependencies.getSelectedProductsArrayList();

        for (int i = 0; i < productList.size(); i++) {
            Datum productData = productList.get(i);
            ItemSelected itemSelected = productList.get(i).getItemSelectedList().get(0);

            if (itemSelected.getQuantity() < productData.getMinProductquantity()) {
                Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.text_in) +
                        productData.getName() +
                        StorefrontCommonData.getString(mActivity, R.string.empty_space) +
                        StorefrontCommonData.getString(mActivity, R.string.text_quantity_cannot_be_less_than) +
                        StorefrontCommonData.getString(mActivity, R.string.empty_space) + productData.getMinProductquantity());

                return false;
            }

        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.hideSoftKeyboard(mActivity);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY:
//                if (resultCode == Activity.RESULT_OK) {
//                    setTotalQuantity(true);
//                    activityResultCheckoutScreen(null);
//                }
//
//                break;

            case Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
            case Codes.Request.OPEN_PRODUCT_DETAILS_SCREEN:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras() != null) {
                        if (data.getExtras().containsKey(PRODUCT_DATA)) {
                            Datum productData = (Datum) data.getExtras().getSerializable(PRODUCT_DATA);
                            int ItemPos = data.getExtras().getInt(KEY_ITEM_POSITION);

                            try {
                                productCatalogueData.getData().set(ItemPos, productData);
                                productCatalogueArrayList.set(ItemPos, productData);
                                if (nLevelProductsAdapter != null) {
                                    nLevelProductsAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                            }
                        }
                    }

                    setTotalQuantity(true);
                    activityResultCheckoutScreen(null);
                }
                if (getFormSettings().getProductView() == 1) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                } else {
                    if (!UIManager.isMarketplaceSingleRestaurant) {
                        if (resultCode == Activity.RESULT_OK && categoryLevel == 0) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                getActivity().setResult(getActivity().RESULT_OK, data);
                                Transition.exit(getActivity());
                            }
                        } else if (resultCode == Activity.RESULT_OK && categoryLevel != 0) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                                getActivity().setResult(getActivity().RESULT_OK, data);
                                Transition.exit(getActivity());
                            }
                        }
                    } else if (resultCode == Activity.RESULT_OK && categoryLevel == 0) {
                        if (Dependencies.isDemoRunning()) {
                            if (data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(Keys.Extras.NEUTRAL_MESSAGE, "");
                                getActivity().setResult(getActivity().RESULT_OK, data);
                                Transition.exit(getActivity());
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
                    } else if (resultCode == Activity.RESULT_OK) {
                        if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null || data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null || data.getExtras().getString(Keys.Extras.NEUTRAL_MESSAGE) != null) {
                            getActivity().setResult(getActivity().RESULT_OK, data);
                            Transition.exit(getActivity());
                        }
                    }
                }
                break;

            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == Activity.RESULT_OK) {
                    userData = StorefrontCommonData.getUserData();
                    Transition.openCheckoutActivity(getActivity(), data.getExtras());
                } else {
                    if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                        if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                            Dependencies.clearSelectedProductArraylist();
                            setTotalQuantity(true);
                            activityResultCheckoutScreen(null);
                        }
                    }
                }
                break;
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;

                    Datum productDataItem = null;

//                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
//                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
//                    }
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                        Dependencies.addCartItem(mActivity, productDataItem);
                    }


                    if (data.hasExtra(KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    setTotalQuantity(true);
                    activityResultCheckoutScreen(productDataItem, itemPos);

                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }

            case Codes.Request.OPEN_CUSTOMISATION_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int itemPos = 0;

                    Datum productDataItem = null;

//                    if (data.hasExtra(Keys.Extras.KEY_ITEM_POSITION)) {
//                        itemPos = data.getIntExtra(Keys.Extras.KEY_ITEM_POSITION, 0);
//                    }
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                    }


                    if (data.hasExtra(KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    setTotalQuantity(true);
                    activityResultCheckoutScreen(productDataItem, itemPos);

                    if (productDataItem.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && productDataItem.getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                        rlTotalQuantity.performClick();
                    }
                }
                break;
        }
    }

    public void activityResultCheckoutScreen(Datum productDataItem) {
        activityResultCheckoutScreen(productDataItem, null);
    }

    public void activityResultCheckoutScreen(Datum productDataItem, Integer itemPos) {

        if (productCatalogueData != null && productCatalogueData.getData() != null) {
            if (Dependencies.getSelectedProductsArrayList().size() == 0) {
                for (int i = 0; i < productCatalogueData.getData().size(); i++) {
                    productCatalogueData.getData().get(i).setSelectedQuantity(0);
                    productCatalogueData.getData().get(i).setItemSelectedList(new ArrayList<ItemSelected>());
                }
            }

            for (int i = 0; i < productCatalogueData.getData().size(); i++) {
                boolean isThere = false;
                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                    if (productCatalogueData.getData().get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                        isThere = true;
                        productCatalogueData.getData().get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                        productCatalogueData.getData().get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                        productCatalogueData.getData().get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());
                        productCatalogueData.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());

                        productCatalogueData.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());

                        for (int k = 0; k < productCatalogueData.getData().get(i).getItemSelectedList().size(); k++) {
                            if (productCatalogueData.getData().get(i).getItemSelectedList().get(k).getQuantity() == 0) {
                                productCatalogueData.getData().get(i).getItemSelectedList().remove(k);
                                k--;
                            }
                        }
                    }
                }
                if (!isThere)
                    productCatalogueData.getData().get(i).setSelectedQuantity(0);
            }

            if (productDataItem != null) {
                for (int i = 0; i < productCatalogueData.getData().size(); i++) {
                    if (productCatalogueData.getData().get(i).getProductId().equals(productDataItem.getProductId())) {
                        productCatalogueData.getData().set(i, productDataItem);
                        productCatalogueArrayList.set(i, productDataItem);
                    }
                }
            }
        }

        if (mPagerAdapter != null && mPagerAdapter.getCount() > 0) {
            for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                Fragment fragment = mPagerAdapter.getItem(i);
                if (fragment instanceof NLevelWorkFlowFragment) {
                    ((NLevelWorkFlowFragment) fragment).activityResultCheckoutScreen(productDataItem, itemPos);

                    if (((NLevelWorkFlowFragment) fragment).nLevelProductsAdapter != null) {
                        ((NLevelWorkFlowFragment) fragment).nLevelProductsAdapter.notifyDataSetChanged();
                    }
                    if (((NLevelWorkFlowFragment) fragment).nLevelWorkFlowAdapter != null) {
                        ((NLevelWorkFlowFragment) fragment).nLevelWorkFlowAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        if (rvNLevelWorkFlowList.getAdapter() != null) {
            if (itemPos != null) {
                rvNLevelWorkFlowList.getAdapter().notifyDataSetChanged();
            } else {
                rvNLevelWorkFlowList.getAdapter().notifyDataSetChanged();
            }
        }
        if (nLevelWorkFlowAdapter != null) {
            if (itemPos != null) {
                nLevelWorkFlowAdapter.notifyItemChanged(itemPos);
            } else {
                nLevelWorkFlowAdapter.notifyDataSetChanged();
            }
        }

    }

    public int getLayoutType(boolean isProduct) {
        int layoutType = Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue;

        if (isProduct) {
            if (productCatalogueData == null) {
                return layoutType;
            } else {
                if (productCatalogueData.getData().size() > 0) {
                    return productCatalogueData.getData().get(0).getLayoutType();
                }
            }
        } else {
            if (nLevelWorkFlowData.getData().get(categoryLevel).size() > 0) {
                return nLevelWorkFlowData.getData().get(categoryLevel).get(0).getLayoutType();
            }


//            if (parentId != null && parentId.size() > 0) {
//                for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
//                    for (int j = 0; j < parentId.size(); j++) {
//
//
//                        if (nLevelWorkFlowData.getData().get(categoryLevel).get(i).getParentCategoryId().equals(parentId.get(0))) {
//                            return nLevelWorkFlowData.getData().get(categoryLevel).get(i).getLayoutType();
////                            layoutType = nLevelWorkFlowData.getData().get(categoryLevel).get(i).getLayoutType();
////                }
//                        }
//                    }
//
////            for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel - 1).size(); i++) {
////                    if (nLevelWorkFlowData.getData().get(categoryLevel - 1).get(i).getCatalogueId().equals(parentId.get(0))) {
////                        layoutType = nLevelWorkFlowData.getData().get(categoryLevel - 1).get(i).getChildLayoutType();
////                }
////            }
//                }
//            } else {
//                for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel).size(); i++) {
//                    return nLevelWorkFlowData.getData().get(categoryLevel).get(0).getLayoutType();
////                    layoutType = nLevelWorkFlowData.getData().get(categoryLevel).get(0).getLayoutType();
//                }
//            }
////            }
//


        }
        return layoutType;
    }

    public void setComparator(Comparator comparator, Comparator comparatorProduct) {
        try {
            if (comparator != null && comparatorProduct != null) {
                if (productCatalogueData != null) {
                    Collections.sort(productCatalogueData.getData(), comparatorProduct);
                    nLevelProductsAdapter.notifyDataSetChanged();
                } else {
                    int layoutType = getLayoutType(false);

                    if (layoutType == Constants.NLevelLayoutType.MENU_LAYOUT.layoutValue) {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            if (mPagerAdapter.getItem(i) instanceof NLevelWorkFlowFragment) {
                                ((NLevelWorkFlowFragment) mPagerAdapter.getItem(i)).setComparator(comparator, comparatorProduct);
                            }
                        }
                    } else {
                        Collections.sort(nLevelWorkFlowData.getData().get(categoryLevel), comparator);
                        nLevelWorkFlowAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void searchCatalogue(String searchString) {
        try {
            if (searchString != null) {
                if (productCatalogueData != null) {
                    setProductAdapter(productCatalogueData.getData(), searchString);
                } else {
                    int layoutType = getLayoutType(false);

                    if (layoutType == Constants.NLevelLayoutType.MENU_LAYOUT.layoutValue) {
                        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                            if (mPagerAdapter.getItem(i) instanceof NLevelWorkFlowFragment) {
                                ((NLevelWorkFlowFragment) mPagerAdapter.getItem(i)).searchCatalogue(searchString);
                            }
                        }
                    } else {
                        setWorkflowAdapter(searchString);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void setWorkflowAdapter(String searchString) {
        nLevelWorkFlowAdapter = new NLevelWorkFlowAdapter(preOrderDateTime, getActivity(), tvNoCatalogueFound, userData, nLevelWorkFlowData,
                categoryLevel, parentId, searchString, pickupLatitude, pickupLongitude, pickupAddress, storefrontData);
        rvNLevelWorkFlowList.setAdapter(nLevelWorkFlowAdapter);
    }

    private void setProductAdapter(ArrayList<Datum> productCatalogueList, String
            searchString) {
        productCatalogueArrayList.clear();
        if (searchString == null)
            return;

        if (searchString.isEmpty()) {
            productCatalogueArrayList.addAll(productCatalogueList);
            if (productCatalogueList.size() == 0) {
                tvNoCatalogueFound.setVisibility(View.VISIBLE);
            } else {
                tvNoCatalogueFound.setVisibility(View.GONE);
            }
        } else {
            ArrayList<Datum> tempProductList = new ArrayList<>();

            for (int i = 0; i < productCatalogueList.size(); i++) {
                if (productCatalogueList.get(i).getName().toLowerCase().contains(searchString.toLowerCase())) {
                    tempProductList.add(productCatalogueList.get(i));
                }
            }
            if (tempProductList.size() == 0) {
                tvNoCatalogueFound.setVisibility(View.VISIBLE);
            } else {
                tvNoCatalogueFound.setVisibility(View.GONE);
            }
            productCatalogueArrayList.addAll(tempProductList);
        }

        if (nLevelProductsAdapter == null) {
            if (!Dependencies.isEcomApp()) {
                nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), getFragmentManager(), productCatalogueArrayList, parentId, showImages);
            } else {
                nLevelProductsAdapter = new NLevelProductsAdapter(getActivity(), getFragmentManager(), productCatalogueArrayList, parentId, categoryId, showImages);
            }
        }

        if (rvNLevelWorkFlowList.getAdapter() != null) {
            rvNLevelWorkFlowList.getAdapter().notifyDataSetChanged();
        } else {
            rvNLevelWorkFlowList.setAdapter(nLevelProductsAdapter);
        }
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

}