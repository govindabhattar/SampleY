package com.tookancustomer.modules.merchantCatalog.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.MetaInfo;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.models.ProductFilters.Data;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.modules.merchantCatalog.adapters.MerchantProductsAdapter;
import com.tookancustomer.modules.merchantCatalog.models.categories.Result;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tookancustomer.appdata.StorefrontCommonData.getFormSettings;
import static com.tookancustomer.appdata.TerminologyStrings.CUSTOM_ORDER;
import static com.tookancustomer.appdata.TerminologyStrings.ORDER;

/*
 * Products will be handled in fragment
 *
 * Layout supported for products are--> LIST, BANNER
 * */

public class MerchantCatalogFragment extends Fragment implements Keys.Extras, Keys.APIFieldKeys, View.OnClickListener {
    public RecyclerView rvProductsList;
    /* Intent Variables */
    public Datum storefrontData;
    public Result parentCategoryData;
    public String adminSelectedCategories = "";
    public String preOrderDateTime;
    public Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    public String pickupAddress = "";
    public String selectedStartDate, selectedEndDate;
    public int minPrice, maxPrice, deepLinkProductId, deepLinkCategoryId = 0;
    public Data filterData;
    public boolean canHitProduct;
    public ProductCatalogueData productCatalogueData;
    public ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum> productCatalogueArrayList = new ArrayList<>();
    private String deepLinkingCategoryName;
    private Activity mActivity;
    private int limit = 30;
    private Dialog mDialog;
    /*Initialise UI Parameters*/
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llLoadMoreView;
    private ShimmerFrameLayout shimmerFrameLayout;
    /*Initialising Variables*/
    private int mainOffset = 0;
    private boolean loading = true;
    private int totalProductsCount = 0; /* Total product count for pagination for hyperlocal */
    private int moreProductsAvailable = 1; /*  In case of rentals, moreProductsAvailable key determines pagination */
    private boolean showProductImage = true;
    private MerchantProductsAdapter merchantProductsAdapter;

    private LinearLayout llNoProductsFound;

    private TextView tvNoProductsFound;

    private LinearLayout llPlaceCustomOrder;
    private TextView tvCustomOrderTextView1, tvCustomOrderTextView2;
    private Button btnCustomOrder;

    private boolean isOftenBoughtMode = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_merchant_catalog, container, false);

        getIntents();
        initViews(rootView);
        /* canHitProduct key is used for fetching products at initialisation . There can be many categories at menu level so all fragments product will be lot of overhead*/
        if (canHitProduct) getProducts(false, 0);
        return rootView;
    }

    private void getIntents() {
        Bundle args = getArguments();
        if (args.getSerializable(PARENT_CATEGORY_DATA) != null) {
            parentCategoryData = (Result) args.getSerializable(PARENT_CATEGORY_DATA);
        }
        storefrontData = (Datum) args.getSerializable(STOREFRONT_DATA);
        adminSelectedCategories = args.getString(ADMIN_CATALOGUE_SELECTED_CATEGORIES);
        pickupLatitude = args.getDouble(PICKUP_LATITUDE);
        pickupLongitude = args.getDouble(PICKUP_LONGITUDE);
        pickupAddress = args.getString(PICKUP_ADDRESS);
        preOrderDateTime = args.getString(DATE_TIME);
        selectedStartDate = args.getString(CHECK_IN_DATE);
        deepLinkProductId = args.getInt("DEEP_LINK_PRODUCT_ID");
        deepLinkCategoryId = args.getInt("DEEP_LINK_CATEGORY_ID", 0);
        deepLinkingCategoryName = args.getString("DEEP_LINK_CATEGORY_NAME");
        selectedEndDate = args.getString(CHECK_OUT_DATE);
        minPrice = args.getInt(MIN_PRICE);
        maxPrice = args.getInt(MAX_PRICE);
        filterData = (Data) args.getSerializable(FILTER_DATA);
        canHitProduct = args.getBoolean("HIT_PRODUCTS");
        isOftenBoughtMode = args.getBoolean(IS_OFTEN_BOUGHT);
    }

    private void initViews(View v) {

        if (mActivity instanceof MerchantCatalogActivity)
            ((MerchantCatalogActivity)mActivity).hideWishlist();

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(getFormSettings().getProductView() == 1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts(true, 0);
            }
        });

        rvProductsList = v.findViewById(R.id.rvProductsList);
        rvProductsList.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvProductsList.getItemAnimator().setChangeDuration(0);
        rvProductsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (productCatalogueData != null) {
                    int visibleItemCount, totalItemCount, pastVisiblesItems;

                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = rvProductsList.getLayoutManager().getChildCount();
                        totalItemCount = rvProductsList.getLayoutManager().getItemCount();
                        try {
                            pastVisiblesItems = ((LinearLayoutManager) rvProductsList.getLayoutManager()).findFirstVisibleItemPosition();
                        } catch (Exception e) {
                            pastVisiblesItems = 0;
                        }

                        if (loading &&
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
                                            getProducts(true, getFormSettings().getProductView() == 1 ? mainOffset + limit : rvProductsList.getLayoutManager().getItemCount());
                                        }
                                    }, 1000);
                                }
                            }
                        }
                    }
                }
            }
        });

        llLoadMoreView = v.findViewById(R.id.llLoadMoreView);
        shimmerFrameLayout = v.findViewById(R.id.shimmerLayout);

        llNoProductsFound = v.findViewById(R.id.llNoProductsFound);
        llNoProductsFound.setVisibility(View.GONE);

        tvNoProductsFound = v.findViewById(R.id.tvNoProductsFound);
        tvNoProductsFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_products_found).replace(StorefrontCommonData.getString(mActivity, R.string.product).toLowerCase(), StorefrontCommonData.getTerminology().getProduct()));

        llPlaceCustomOrder = v.findViewById(R.id.llPlaceCustomOrder);
        tvCustomOrderTextView1 = v.findViewById(R.id.tvCustomOrderTextView1);
        tvCustomOrderTextView2 = v.findViewById(R.id.tvCustomOrderTextView2);
        btnCustomOrder = v.findViewById(R.id.btnCustomOrder);


        if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {

            tvCustomOrderTextView1.setText(StorefrontCommonData.getTerminology().getREADY_TO_PLACE_YOUR_ORDER());

            String customOrderString = storefrontData.getStoreName();
            Spannable string = CustomViewsUtil.createSpanForExtraBoldText(mActivity, customOrderString, customOrderString);
            tvCustomOrderTextView2.setText(string);
            btnCustomOrder.setText(StorefrontCommonData.getString(mActivity, R.string.text_create) + " "
                    + ((MerchantCatalogActivity) mActivity).getStrings(R.string.text_order_now).replace(ORDER, Utils.getCallTaskAs(true, true)));

        } else {
            tvCustomOrderTextView1.setText(StorefrontCommonData.getString(mActivity, R.string.could_not_found));

            String customOrderString = StorefrontCommonData.getString(mActivity, R.string.place_custom_order_per_requirement).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder());
            Spannable string = CustomViewsUtil.createSpanForExtraBoldText(mActivity, customOrderString, StorefrontCommonData.getTerminology().getCustomOrder());
            tvCustomOrderTextView2.setText(string);
            btnCustomOrder.setText(StorefrontCommonData.getString(mActivity, R.string.place_custom_order).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder()));
        }


        Utils.setOnClickListener(this, btnCustomOrder);
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

    public void getProducts(final boolean isSwipeRefresh, int offset) {

       /* try{
            if (!isSwipeRefresh) {
                if (StorefrontCommonData.getLastMerchantCachedData().getProductsListHashmap().containsKey(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0)) {
                    setProductCatalogueData(StorefrontCommonData.getLastMerchantCachedData().getProductsListHashmap().get(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0), offset);
                } else {
                    startShimmerAnimation(shimmerFrameLayout);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/


        Location location = LocationUtils.getLastLocation(getActivity());

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("point", location.getLatitude() + " " + location.getLongitude());
        commonParams.add("limit", limit);
        commonParams.add("offset", offset);

        commonParams.remove(USER_ID);
        commonParams.add(USER_ID, storefrontData.getStorefrontUserId());

        if (mActivity instanceof MerchantCatalogActivity) {
            ((MerchantCatalogActivity) mActivity).llVegSwitchLayout.setVisibility((StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegFilter() == 1
                    && storefrontData.isVegFilterActive()) ? View.VISIBLE : View.GONE);
            if (((MerchantCatalogActivity) mActivity).switchVegFilter.isChecked()) {
                commonParams.add("is_veg", 1);
            }
        }

        if (deepLinkCategoryId != 0) {
            commonParams.add("parent_category_id", deepLinkCategoryId);

        } else {

            if (!adminSelectedCategories.isEmpty()) {
                commonParams.add("parent_category_id", adminSelectedCategories);
            } else if (parentCategoryData != null) {
                commonParams.add("parent_category_id", parentCategoryData.getCatalogueId());
            }
        }

        if (getFormSettings().getProductView() == 1) {
            commonParams.build().getMap().remove("point");
            commonParams.add(LATITUDE, pickupLatitude);
            commonParams.add(LONGITUDE, pickupLongitude);

            if (filterData != null && setFilterData(filterData) != null) {
                commonParams.add("custom_fields", setFilterData(filterData));
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
        }

        commonParams.add(DATE_TIME, preOrderDateTime != null && !preOrderDateTime.isEmpty() ? preOrderDateTime : DateUtils.getInstance().getCurrentDateTimeUtc());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        if (getFormSettings().getProductView() == 0) {
            commonParams.add(IS_PREORDER_SELECTED_FOR_MENU, Dependencies.getIsPreorderSelecetedForMenu());
            if (isOftenBoughtMode) {
                commonParams.add("product_ids_array", Dependencies.getCartOftenBoughtIds());
            }
            RestClient.getApiInterface(getActivity()).getProductCatalogue(commonParams.build().getMap()).enqueue(finalProductResponseResolver(offset));
        } else {
            RestClient.getApiInterface(getActivity()).getMarketplaceProducts(commonParams.build().getMap()).enqueue(finalProductResponseResolver(offset));
        }
    }

    private ResponseResolver<BaseModel> finalProductResponseResolver(final int offset) {
        return new ResponseResolver<BaseModel>(mActivity, offset == 0, true) {
            @Override
            public void success(BaseModel baseModel) {
                ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();

                try {
                    MetaInfo metaInfo = baseModel.toResponseModelMetaInfo(MetaInfo.class);

                    if (metaInfo != null && metaInfo.getTotalCount() >= 0)
                        totalProductsCount = metaInfo.getTotalCount();

                    if (metaInfo != null && metaInfo.getTotalCount() >= 0) {
                        moreProductsAvailable = metaInfo.getMoreProductsAvailable();
                        showProductImage = metaInfo.getShowImages() == 1;
                        if (merchantProductsAdapter != null)
                            merchantProductsAdapter.showProductImage = showProductImage;
                    }


                    com.tookancustomer.models.ProductCatalogueData.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.ProductCatalogueData.Datum[].class);
                    productCatalogueDataResponse.setData(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>(Arrays.asList(datum)));

                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

                ((MerchantCatalogActivity) mActivity).showeHideCustomButton(productCatalogueDataResponse.getData().size());


//                StorefrontCommonData.getLastMerchantCachedData().getProductsListHashmap().put(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0, productCatalogueDataResponse);
                setProductCatalogueData(productCatalogueDataResponse, offset);
                boolean isShowAddButton;


                if (deepLinkProductId != 0) {
                    for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {
                        if (productCatalogueDataResponse.getData().get(i).getProductId().equals(deepLinkProductId)) {
                            /*((MerchantCatalogActivity) mActivity).updateHeading(
                                    deepLinkingCategoryName);*/
                            if (StorefrontCommonData.getFormSettings().getPdpView() == 1
                                    || !productCatalogueDataResponse.getData().get(i).getStorefrontData().isStoreAvailableForBooking()
                                    || productCatalogueDataResponse.getData().get(i).getMenuEnabledProduct() == 0) {
                                isShowAddButton = false;
                            } else {
                                isShowAddButton = true;

                            }

                            setDeepLinkPopUp(productCatalogueDataResponse.getData().get(i).getName(),
                                    productCatalogueDataResponse.getData().get(i).getDescription().toString(),
                                    productCatalogueDataResponse.getData().get(i).getImageUrl(),
                                    productCatalogueDataResponse.getData().get(i).getStorefrontData().getBusinessType(),
                                    productCatalogueDataResponse.getData().get(i).getUnitType(),
                                    productCatalogueDataResponse.getData().get(i).getPaymentSettings(),
                                    productCatalogueDataResponse.getData().get(i).getPrice(),
                                    productCatalogueDataResponse.getData().get(i).getUnit(),
                                    productCatalogueDataResponse.getData().get(i).getServiceTime(),
                                    productCatalogueDataResponse.getData().get(i), isShowAddButton);
                        }
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                setProductAdapter(offset);
            }
        };
    }

    private void setDeepLinkPopUp(String productName, String productDescription,
                                  String imageUrl, Integer businessType,
                                  int unitType, PaymentSettings mPaymentSettings,
                                  Number price, Number unit, int serviceTime,
                                  com.tookancustomer.models.ProductCatalogueData.Datum datum,
                                  boolean isShowAddButton) {

        mDialog = new Dialog(mActivity);


        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_deeplink);
        mDialog.setCanceledOnTouchOutside(true);
        // ImageView ivNLevelImage
        //TextView productNameTV = mDialog.findViewById(R.id.productNameTV);
        // TextView tvPrice = mDialog.findViewById(R.id.tvDescription3);
        TextView tvDescription1 = mDialog.findViewById(R.id.tvDescription1);
        TextView tvDescription2 = mDialog.findViewById(R.id.tvDescription2);
        TextView tvDescription3 = mDialog.findViewById(R.id.tvDescription3);
        TextView tvDescription4 = mDialog.findViewById(R.id.tvDescription4);
        TextView tvDescription4Text = mDialog.findViewById(R.id.tvDescription4Text);
        TextView tvSingleSelectionButton = mDialog.findViewById(R.id.tvSingleSelectionButton);
        ImageView ivNLevelImage = mDialog.findViewById(R.id.ivNLevelImage);
        ImageView ivClose = mDialog.findViewById(R.id.ivClose);
        // productNameTV.setText(productName);
        tvDescription1.setText(productDescription);
        tvDescription1.setText(productName);
        tvDescription2.setText(Html.fromHtml(productDescription.trim()));
        if (businessType == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(unitType) != Constants.ProductsUnitType.FIXED) {
            tvDescription3.setText(UIManager.getCurrency(Utils.getCurrencySymbol(mPaymentSettings) + Utils.getDoubleTwoDigits((price.doubleValue()))) + " /" +
                    Constants.ProductsUnitType.getUnitTypeText(mActivity, unit.intValue(), unitType, false));
        } else {
            tvDescription3.setText(UIManager.getCurrency(Utils.getCurrencySymbol(mPaymentSettings) + Utils.getDoubleTwoDigits(price.doubleValue())));
        }
        tvDescription4.setText(Utils.getServiceTime(serviceTime, mActivity, unitType));
        tvDescription4Text.setVisibility(UIManager.showServiceTime() && serviceTime > 0 ? View.VISIBLE : View.GONE);
        tvDescription4Text.setText(StorefrontCommonData.getString(mActivity, R.string.service_time) + ": ");
        tvDescription4.setVisibility(UIManager.showServiceTime() && serviceTime > 0 ? View.VISIBLE : View.GONE);
        tvDescription4.setText(Utils.getServiceTime(serviceTime, mActivity, unitType));

        new GlideUtil.GlideUtilBuilder(ivNLevelImage)
                .setLoadItem(imageUrl)
                .setCenterCrop(true)
                .setFitCenter(true)
                .setPlaceholder(R.drawable.ic_image_placeholder)
                .setTransformation(new CenterCrop())
                .build();


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        if (isShowAddButton) {
            tvSingleSelectionButton.setVisibility(View.VISIBLE);
        } else {
            tvSingleSelectionButton.setVisibility(View.GONE);

        }
        tvSingleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datum.setSelectedQuantity(datum.getSelectedQuantity() + 1);
                Dependencies.addCartItem(mActivity, datum);
                merchantProductsAdapter.notifyDataSetChanged();
                ((MerchantCatalogActivity) mActivity).setTotalQuantity();
                mDialog.dismiss();


            }
        });
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }


    private void setProductCatalogueData(ProductCatalogueData productCatalogueDataResponse, int offset) {

        for (int i = 0; i < productCatalogueDataResponse.getData().size(); i++) {
            productCatalogueDataResponse.getData().get(i).setStorefrontData((Datum) getArguments().getSerializable(STOREFRONT_DATA));
            productCatalogueDataResponse.getData().get(i).setFormId(getFormSettings().getFormId());
            productCatalogueDataResponse.getData().get(i).setVendorId(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());
            for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                if (productCatalogueDataResponse.getData().get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                    productCatalogueDataResponse.getData().get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());
                    productCatalogueDataResponse.getData().get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                    productCatalogueDataResponse.getData().get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());
                    productCatalogueDataResponse.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());
                    productCatalogueDataResponse.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());
                    productCatalogueDataResponse.getData().get(i).setPaymentSettings(Dependencies.getSelectedProductsArrayList().get(j).getPaymentSettings());
                    Dependencies.getSelectedProductsArrayList().set(j, productCatalogueDataResponse.getData().get(i));
                }
            }
        }

        if (offset == 0) {
            productCatalogueData = productCatalogueDataResponse;

            productCatalogueArrayList.clear();
            productCatalogueArrayList.addAll(productCatalogueData.getData());
        } else {
            ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum> datumArrayList = productCatalogueData.getData();
            datumArrayList.addAll(productCatalogueDataResponse.getData());
            productCatalogueData.setData(datumArrayList);

            productCatalogueArrayList.addAll(productCatalogueDataResponse.getData());
        }
        try {
            if (productCatalogueData.getData().size() > 0)
                StorefrontCommonData.getLastMerchantCachedData().getProductsListHashmap().put(parentCategoryData != null ? parentCategoryData.getCatalogueId() : 0, productCatalogueData);
            else
                StorefrontCommonData.getLastMerchantCachedData().getProductsListHashmap().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }


        setProductAdapter(offset);


    }


    private void setProductAdapter(int offset) {
        mainOffset = offset;

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        llLoadMoreView.setVisibility(View.GONE);
        loading = true;

        if (productCatalogueArrayList.size() == 0) {
            ((MerchantCatalogActivity) mActivity).rlCustomOrder.setVisibility(View.GONE);
            llNoProductsFound.setVisibility(View.VISIBLE);
            if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {
                llPlaceCustomOrder.setVisibility(View.VISIBLE);
                tvNoProductsFound.setVisibility(View.GONE);
            } else {
                llPlaceCustomOrder.setVisibility(View.GONE);
                tvNoProductsFound.setVisibility(View.VISIBLE);
            }

        } else {
            llNoProductsFound.setVisibility(View.GONE);
        }

        if (merchantProductsAdapter == null || offset == 0) {
            merchantProductsAdapter = new MerchantProductsAdapter(mActivity, getFragmentManager(), productCatalogueArrayList, showProductImage, new MerchantProductsAdapter.Callback() {
                @Override
                public void onQuantityUpdated() {
                    if (mActivity instanceof MerchantCatalogActivity) {
                        ((MerchantCatalogActivity) mActivity).setTotalQuantity();

                        if (storefrontData.getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT
                                && storefrontData.getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                            ((MerchantCatalogActivity) mActivity).openCheckoutActivity();
                        }
                    }
//                    else if(mActivity instanceof MandatoryCategoryActivity){
//                        ((MandatoryCategoryActivity) mActivity).checkIfAllMandatoryProductAdded();
//
//                    }
                }
            });
        }

        if (rvProductsList.getAdapter() != null) {
            if (productCatalogueArrayList.size() == 0 || offset == 0) {
//                rvProductsList.getAdapter().notifyDataSetChanged();
                rvProductsList.setAdapter(merchantProductsAdapter);
            } else {
                rvProductsList.getAdapter().notifyItemRangeInserted(offset, productCatalogueArrayList.size());
            }
        } else {
            rvProductsList.setAdapter(merchantProductsAdapter);
        }
        stopShimmerAnimation(shimmerFrameLayout);

        if (productCatalogueArrayList.size() > 0) {
            rvProductsList.setVisibility(View.VISIBLE);
        } else {
            rvProductsList.setVisibility(View.GONE);
        }

    }

    public void updateProductList(com.tookancustomer.models.ProductCatalogueData.Datum productDataItem, Integer itemPos) {

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
                        productCatalogueData.getData().get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());
                        productCatalogueData.getData().get(i).setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(j).getSurgeAmount());

                        for (int k = 0; k < productCatalogueData.getData().get(i).getItemSelectedList().size(); k++) {
                            if (productCatalogueData.getData().get(i).getItemSelectedList().get(k).getQuantity() == 0) {
                                productCatalogueData.getData().get(i).getItemSelectedList().remove(k);
                                k--;
                            }
                        }
                    }
                }
                if (!isThere) {
                    productCatalogueData.getData().get(i).setSelectedQuantity(0);
                    productCatalogueData.getData().get(i).setItemSelectedList(new ArrayList<ItemSelected>());
                }
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

        if (rvProductsList != null && rvProductsList.getAdapter() != null) {
//            if (itemPos != null) {
//                rvProductsList.getAdapter().notifyItemChanged(itemPos);
//            } else {
            rvProductsList.getAdapter().notifyDataSetChanged();
//            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCustomOrder:
                if (mActivity instanceof MerchantCatalogActivity) {
                    ((MerchantCatalogActivity) mActivity).isCustomCheckout = true;
                }
                Bundle extraa = new Bundle();

                if (UIManager.getCustomOrderActive() && storefrontData.getCustomOrderActiveForStore() == 1) {
                    extraa.putBoolean("isCustomOrderMerchantLevel", true);

                }

                extraa.putDouble(PICKUP_LATITUDE, pickupLatitude);
                extraa.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                extraa.putString(PICKUP_ADDRESS, pickupAddress);
                extraa.putBoolean("isCustomOrder", true);
                extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                Transition.openCustomCheckoutActivity(mActivity, extraa);
                break;
        }
    }

    public void startShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        swipeRefreshLayout.setVisibility(View.GONE);

        llNoProductsFound.setVisibility(View.GONE);

        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
    }

    public void stopShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);
        shimmerLayout.stopShimmerAnimation();
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

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