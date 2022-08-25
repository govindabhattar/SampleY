package com.tookancustomer.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.MarketplaceSearchActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.adapters.MarketplaceRestaurantListAdapter;
import com.tookancustomer.adapters.ProductsWithMarketplaceAdapter;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.searchMarketplaceProducts.SearchedMarketplaceProductsData;
import com.tookancustomer.models.searchMarketplaceProducts.SearchedMarketplaceProductsModel;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import retrofit2.Callback;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.DUAL_USER_KEY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LANGUAGE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Prefs.ACCESS_TOKEN;
import static com.tookancustomer.appdata.TerminologyStrings.CUSTOM_ORDER;
import static com.tookancustomer.appdata.TerminologyStrings.TERMINOLOGY;

public class MarketplaceSearchFragment extends Fragment {
    public RecyclerView rvMarketplaceSearchList;
    public CityStorefrontsModel cityStorefrontsModel = new CityStorefrontsModel();
    private Activity mActivity;
    private boolean onlyMarketplaces;
    private LinearLayout llNoProductsFound;
    private TextView tvNoProductsFound;
    private LinearLayout llPlaceCustomOrder;
    private TextView tvCustomOrderTextView1, tvCustomOrderTextView2;
    private Button btnCustomOrder;

    private MarketplaceRestaurantListAdapter marketplaceRestaurantListAdapter;
    private ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum> onlyMarketplaceList = new ArrayList<>();

    private ProductsWithMarketplaceAdapter productsMarketplaceAdapter;
    private ArrayList<Datum> productCatalogueDataList = new ArrayList<>();

    public MarketplaceSearchFragment() {
    }

    public MarketplaceSearchFragment(boolean onlyMarketplaces, Activity activity/*,
                                     ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum> onlyMarketplaceList,
                                     ArrayList<Datum> productCatalogueDataList*/) {
        this.onlyMarketplaces = onlyMarketplaces;
        this.mActivity = activity;

//        this.productCatalogueDataList = productCatalogueDataList;
//        this.onlyMarketplaceList = onlyMarketplaceList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_marketplace_search, container, false);
//        mActivity = getActivity();
        initViews(rootView);
        return rootView;
    }

    public void initViews(View v) {

        tvNoProductsFound = v.findViewById(R.id.tvNoProductsFound);
        // tvNoProductsFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getProduct()));
        llPlaceCustomOrder = v.findViewById(R.id.llPlaceCustomOrder);
        tvCustomOrderTextView1 = v.findViewById(R.id.tvCustomOrderTextView1);
        tvCustomOrderTextView2 = v.findViewById(R.id.tvCustomOrderTextView2);
        btnCustomOrder = v.findViewById(R.id.btnCustomOrder);
        tvCustomOrderTextView1.setText(StorefrontCommonData.getString(mActivity, R.string.could_not_found));
        String customOrderString = StorefrontCommonData.getString(mActivity, R.string.place_custom_order_per_requirement).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder());
        Spannable string = CustomViewsUtil.createSpanForExtraBoldText(mActivity, customOrderString, StorefrontCommonData.getTerminology().getCustomOrder());
        tvCustomOrderTextView2.setText(string);
        btnCustomOrder.setText(StorefrontCommonData.getString(mActivity, R.string.place_custom_order).replace(CUSTOM_ORDER, StorefrontCommonData.getTerminology().getCustomOrder()));


        rvMarketplaceSearchList = v.findViewById(R.id.rvMarketplaceSearchList);
        llNoProductsFound = v.findViewById(R.id.llNoProductsFound);
        rvMarketplaceSearchList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        if (onlyMarketplaces) {
            marketplaceRestaurantListAdapter = new MarketplaceRestaurantListAdapter(mActivity, onlyMarketplaceList, MarketplaceSearchFragment.this);
            rvMarketplaceSearchList.setAdapter(marketplaceRestaurantListAdapter);
        } else {
            productsMarketplaceAdapter = new ProductsWithMarketplaceAdapter(mActivity, getFragmentManager(), productCatalogueDataList, new ArrayList<Integer>());
            rvMarketplaceSearchList.setAdapter(productsMarketplaceAdapter);
        }

        btnCustomOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MarketplaceSearchActivity) mActivity).isCustomCheckout = true;

                Bundle extraaCustomOrder = new Bundle();
                extraaCustomOrder.putDouble(PICKUP_LATITUDE, ((MarketplaceSearchActivity) mActivity).pickupLatitude);
                extraaCustomOrder.putDouble(PICKUP_LONGITUDE, ((MarketplaceSearchActivity) mActivity).pickupLongitude);
                extraaCustomOrder.putString(PICKUP_ADDRESS, ((MarketplaceSearchActivity) mActivity).pickupAddress);
                extraaCustomOrder.putBoolean("isCustomOrder", true);
                Transition.openCustomCheckoutActivity(mActivity, extraaCustomOrder);
            }
        });

        if (onlyMarketplaceList.size() > 0 || productCatalogueDataList.size() > 0) {
            llNoProductsFound.setVisibility(View.GONE);
            rvMarketplaceSearchList.setVisibility(View.VISIBLE);
        } else {
            setVisibilityNoProduct();
            rvMarketplaceSearchList.setVisibility(View.GONE);
        }


    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getSearchedProducts();
//    }

    public void activityResultCheckoutScreen(Datum productDataItem) {
        if (!onlyMarketplaces && productCatalogueDataList != null && productCatalogueDataList.size() > 0) {
            if (Dependencies.getSelectedProductsArrayList().size() == 0) {
                for (int i = 0; i < productCatalogueDataList.size(); i++) {
                    productCatalogueDataList.get(i).setSelectedQuantity(0);
                    productCatalogueDataList.get(i).setItemSelectedList(new ArrayList<ItemSelected>());
                }
            }

            for (int i = 0; i < productCatalogueDataList.size(); i++) {
                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                    if (productCatalogueDataList.get(i).getProductId().equals(Dependencies.getSelectedProductsArrayList().get(j).getProductId())) {
                        productCatalogueDataList.get(i).setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(j).getSelectedQuantity());

                        productCatalogueDataList.get(i).setProductStartDate(Dependencies.getSelectedProductsArrayList().get(j).getProductStartDate());
                        productCatalogueDataList.get(i).setProductEndDate(Dependencies.getSelectedProductsArrayList().get(j).getProductEndDate());

                        productCatalogueDataList.get(i).setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(j).getItemSelectedList());

                        for (int k = 0; k < productCatalogueDataList.get(i).getItemSelectedList().size(); k++) {
                            if (productCatalogueDataList.get(i).getItemSelectedList().get(k).getQuantity() == 0) {
                                productCatalogueDataList.get(i).getItemSelectedList().remove(k);
                                k--;
                            }
                        }
                    }
                }
            }

            if (productDataItem != null) {
                for (int i = 0; i < productCatalogueDataList.size(); i++) {
                    if (productCatalogueDataList.get(i).getProductId().equals(productDataItem.getProductId())) {
                        productCatalogueDataList.set(i, productDataItem);
                    }
                }
            }
        }

        if (rvMarketplaceSearchList.getAdapter() != null) {
            rvMarketplaceSearchList.getAdapter().notifyDataSetChanged();
        }


        if (onlyMarketplaceList.size() > 0 || productCatalogueDataList.size() > 0) {
            llNoProductsFound.setVisibility(View.GONE);
            rvMarketplaceSearchList.setVisibility(View.VISIBLE);
        } else {
            setVisibilityNoProduct();
            rvMarketplaceSearchList.setVisibility(View.GONE);
        }


    }

    private void setVisibilityNoProduct() {
        llNoProductsFound.setVisibility(View.VISIBLE);
        if (StorefrontCommonData.getAppConfigurationData().getIsCustomOrderActive() == 1) {
            llPlaceCustomOrder.setVisibility(View.VISIBLE);
            tvNoProductsFound.setVisibility(View.GONE);
        } else {
            tvNoProductsFound.setVisibility(View.VISIBLE);
        }
    }


    public void getSearchedProducts() {
        if (!Utils.internetCheck(mActivity)) {
            Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again));
            ((MarketplaceSearchActivity) mActivity).pbLoading.setVisibility(View.GONE);
            return;
        }

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("latitude", ((MarketplaceSearchActivity) mActivity).pickupLatitude);
        commonParams.add("longitude", ((MarketplaceSearchActivity) mActivity).pickupLongitude);
        commonParams.add("search_text", ((MarketplaceSearchActivity) mActivity).etSearch.getText().toString().trim());
//        commonParams.add(DATE_TIME, (preorderDateTime != null && !preorderDateTime.isEmpty()) ? preorderDateTime : DateUtils.getInstance().getCurrentDateTimeUtc());
//        commonParams.add(IS_PREORDER_SELECTED_FOR_MENU, Dependencies.getIsPreorderSelecetedForMenu());

        ((MarketplaceSearchActivity) mActivity).pbLoading.setVisibility(View.VISIBLE);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        if (MyApplication.getInstance().getSelectedPickUpMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
            commonParams.add("self_pickup", 1);
        } else {
            commonParams.add("self_pickup", 0);
        }


        if (onlyMarketplaces)
            RestClient.getApiInterface(mActivity).searchMerchant(commonParams.build().getMap())
                    .enqueue(responceresolver());
        else
            RestClient.getApiInterface(mActivity).searchProductGlobal(commonParams.build().getMap())
                    .enqueue(responceresolver());
    }


    private Callback<BaseModel> responceresolver() {
        return new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                SearchedMarketplaceProductsModel searchedMarketplaceProductsModel = new SearchedMarketplaceProductsModel();
                try {
                    searchedMarketplaceProductsModel.setData(baseModel.toResponseModel(SearchedMarketplaceProductsData.class));
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                ((MarketplaceSearchActivity) mActivity).searchedString = searchedMarketplaceProductsModel.getData().getSearchText();

                if (((MarketplaceSearchActivity) mActivity).etSearch.getText().toString().trim().equals(((MarketplaceSearchActivity) mActivity).searchedString)) {
                    ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum> onlyMarketplacesList = new ArrayList<>();
                    ArrayList<Datum> bothMarketplaceProductList = new ArrayList<>();

//                    cityStorefrontsModels.setData(searchedMarketplaceProductsModel.getData().getResultList());
                    cityStorefrontsModel.setData(searchedMarketplaceProductsModel.getData().getResultList());

                    for (int i = 0; i < cityStorefrontsModel.getData().size(); i++) {
                        com.tookancustomer.models.MarketplaceStorefrontModel.Datum cityStorefrontData = cityStorefrontsModel.getData().get(i);
                        com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData = new com.tookancustomer.models.MarketplaceStorefrontModel.Datum(
                                cityStorefrontData.getMerchantMinimumOrder(), cityStorefrontData.getPhone(), cityStorefrontData.getStorefrontUserId(),
                                cityStorefrontData.getStoreName(), cityStorefrontData.getLogo(), cityStorefrontData.getAddress(), cityStorefrontData.getEmail(), cityStorefrontData.getDisplayAddress(),
                                cityStorefrontData.getDescription(), cityStorefrontData.getLatitude(), cityStorefrontData.getLongitude(), cityStorefrontData.getServingDistance(),
                                cityStorefrontData.getDistance(), cityStorefrontData.getStoreRating(), cityStorefrontData.getLastReviewRating(), cityStorefrontData.getTotalRatingsCount(),
                                cityStorefrontData.getTotalReviewCount(), cityStorefrontData.getMyReview(), cityStorefrontData.getMyRating(), cityStorefrontData.getInstantTask(),
                                cityStorefrontData.getScheduledTask(), cityStorefrontData.getShowOutstockedProduct(), cityStorefrontData.getEnableTookanAgent(),
                                cityStorefrontData.getBufferSchedule(), cityStorefrontData.getButtons(), cityStorefrontData.getBusinessType(), cityStorefrontData.getMultipleProductInSingleCart(),
                                cityStorefrontData.getPdOrAppointment(), cityStorefrontData.getIsStartEndTimeEnable(), cityStorefrontData.getIsStorefrontOpened(),
                                cityStorefrontData.getIsReviewRatingEnabled(), cityStorefrontData.getHomeDelivery(),
                                cityStorefrontData.getSelfPickup(), cityStorefrontData.getBusinessCategoriesName(),
                                cityStorefrontData.merchantDiscount, cityStorefrontData.getIs_sponsored(),
                                cityStorefrontData.getIsMenuEnabled(), cityStorefrontData.getRecurrinTask(),
                                cityStorefrontData.getCustomOrderActiveForStore(),
                                cityStorefrontData.getDeliveryCharge(), cityStorefrontData.getMerchantAsDeliveryManager(),cityStorefrontData.getIsWishlisted());
                        storefrontData.setCategoryButtonType(cityStorefrontData.getCategoryButtonType());
                        storefrontData.setCategoryLayoutType(cityStorefrontData.getCategoryLayoutType());
                        storefrontData.setLastLevelCatalogView(cityStorefrontData.getLastLevelCatalogView());
                        storefrontData.setProductButtonType(cityStorefrontData.getProductButtonType());
                        storefrontData.setProductLayoutType(cityStorefrontData.getProductLayoutType());
                        storefrontData.setEstimatedAddOn(cityStorefrontData.getEstimatedAddOn());
                        storefrontData.setEstimatedTime(cityStorefrontData.getEstimatedTime());
                        storefrontData.setPaymentMethods(cityStorefrontData.getPaymentMethods());
                        storefrontData.setPreBookingBuffer(cityStorefrontData.getPreBookingBuffer());
                        storefrontData.setCustomOrderActiveForStore(cityStorefrontData.getCustomOrderActiveForStore());
                        storefrontData.setPaymentSettings(cityStorefrontData.getPaymentSettings());
                        storefrontData.setIsOrderAgentschedulingEnabled(cityStorefrontData.getIsOrderAgentschedulingEnabled());
                        storefrontData.setIsPdFlow(cityStorefrontData.getPdFlow());

                        storefrontData.setDeliveryTime(cityStorefrontData.getDeliveryTime());
                        storefrontData.setMerchantDeliveryTime(cityStorefrontData.getMerchantDeliveryTime());
                        storefrontData.setEstimatedTime(cityStorefrontData.getEstimatedTime());
                        storefrontData.setEstimatedAddOn(cityStorefrontData.getEstimatedAddOn());
                        storefrontData.setOrderPreparationTime(cityStorefrontData.getOrderPreparationTime());
                        storefrontData.setMerchantAsDeliveryManager(cityStorefrontData.getMerchantAsDeliveryManager());
                        storefrontData.setMerchantMinimumOrder(cityStorefrontData.getMerchantMinimumOrder());

                        onlyMarketplacesList.add(storefrontData);

                        Datum productCatalogueData = new Datum();
                        productCatalogueData.setStorefrontData(storefrontData);
                        productCatalogueData.setHeader(true);
                        if (!onlyMarketplaces)
                            bothMarketplaceProductList.add(productCatalogueData);

                        for (int j = 0; j < cityStorefrontsModel.getData().get(i).getProductList().size(); j++) {

                            Datum productData = cityStorefrontsModel.getData().get(i).getProductList().get(j);

                            productData.setStorefrontData(storefrontData);
                            productData.setSellerId(cityStorefrontsModel.getData().get(i).getStorefrontUserId());
                            productData.setFormId(StorefrontCommonData.getFormSettings().getFormId());
                            productData.setVendorId(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

                            productData.setIsMenuEnabled(cityStorefrontData.getIsMenuEnabled() ? 1 : 0);

                            for (int k = 0; k < Dependencies.getSelectedProductsArrayList().size(); k++) {
                                if (productData.getProductId().equals(Dependencies.getSelectedProductsArrayList().get(k).getProductId())) {
                                    productData.setSelectedQuantity(Dependencies.getSelectedProductsArrayList().get(k).getSelectedQuantity());
                                    productData.setProductStartDate(Dependencies.getSelectedProductsArrayList().get(k).getProductStartDate());
                                    productData.setProductEndDate(Dependencies.getSelectedProductsArrayList().get(k).getProductEndDate());
                                    productData.setItemSelectedList(Dependencies.getSelectedProductsArrayList().get(k).getItemSelectedList());
                                    productData.setSurgeAmount(Dependencies.getSelectedProductsArrayList().get(k).getSurgeAmount());
                                    productData.setPaymentSettings(Dependencies.getSelectedProductsArrayList().get(k).getPaymentSettings());

                                    Dependencies.getSelectedProductsArrayList().set(k, productData);
                                }
                            }
                            if (!onlyMarketplaces)
                                bothMarketplaceProductList.add(productData);
                        }
                    }

                    if (onlyMarketplaces) {
                        onlyMarketplaceList = onlyMarketplacesList;
                        marketplaceRestaurantListAdapter = new MarketplaceRestaurantListAdapter(mActivity, onlyMarketplaceList, MarketplaceSearchFragment.this);
                        rvMarketplaceSearchList.setAdapter(marketplaceRestaurantListAdapter);
                        rvMarketplaceSearchList.setVisibility(View.VISIBLE);
                        tvNoProductsFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getStore()));
                    } else {
                        productCatalogueDataList = bothMarketplaceProductList;
                        productsMarketplaceAdapter = new ProductsWithMarketplaceAdapter(mActivity, getFragmentManager(), productCatalogueDataList, new ArrayList<Integer>());
                        rvMarketplaceSearchList.setAdapter(productsMarketplaceAdapter);
                        rvMarketplaceSearchList.setVisibility(View.VISIBLE);
                        tvNoProductsFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_terminology_found).replace(TERMINOLOGY, StorefrontCommonData.getTerminology().getProduct()));
                    }


                } else if (((MarketplaceSearchActivity) mActivity).etSearch.getText().toString().trim().isEmpty()) {
//                    tabLayout.setVisibility(View.GONE);
//                    viewPager.setVisibility(View.GONE);
//                    setVisibilityNoProduct();
                    cityStorefrontsModel.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>());
                }
                ((MarketplaceSearchActivity) mActivity).pbLoading.setVisibility(View.GONE);

                if (onlyMarketplaceList.size() > 0 || productCatalogueDataList.size() > 0) {
                    llNoProductsFound.setVisibility(View.GONE);
                    rvMarketplaceSearchList.setVisibility(View.VISIBLE);
                } else {
                    setVisibilityNoProduct();
                    rvMarketplaceSearchList.setVisibility(View.GONE);
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
//                setVisibilityNoProduct();
                cityStorefrontsModel = new CityStorefrontsModel();
//                viewPager.setVisibility(View.GONE);
//                tabLayout.setVisibility(View.GONE);
                ((MarketplaceSearchActivity) mActivity).pbLoading.setVisibility(View.GONE);
            }
        };
    }

    public void selectedWishlist(boolean wishlistSelected, int adapterPosition) {

        CommonParams.Builder commonParams = new CommonParams.Builder();
        UserData userData = StorefrontCommonData.getUserData();

        commonParams.add("is_wishlisted", wishlistSelected ? 1 : 0)
                .add("domain_name", StorefrontCommonData.getFormSettings().getDomainName())
                .add(LANGUAGE, "en")
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable())
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(USER_ID, cityStorefrontsModel.getData().get(adapterPosition).getStorefrontUserId())
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getVendorDetails().getAppAccessToken());


        RestClient.getApiInterface(mActivity).merchantWishlist(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {

            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(mActivity, "Success.");
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Utils.snackBar(mActivity, error.getMessage());
            }
        });
    }


}