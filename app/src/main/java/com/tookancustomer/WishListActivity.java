package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.filter.model.AllowedDataList;
import com.tookancustomer.mapfiles.placeapi.Location;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tookancustomer.BaseActivity.getStrings;
import static com.tookancustomer.appdata.Constants.MERCHANT_PAGINATION_LIMIT;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_TYPE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DUAL_USER_KEY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FORM_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_DEMO_APP;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LANGUAGE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LATITUDE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.YELO_APP_TYPE;
import static com.tookancustomer.appdata.Keys.Prefs.ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.Prefs.LONGITUDE;
import static com.tookancustomer.appdata.TerminologyStrings.SELFPICKUP_SELFPICKUP;
import static com.tookancustomer.appdata.TerminologyStrings.STORE;

public class WishListActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private final int ivBack = R.id.ivBack;
    public ArrayList<com.tookancustomer.filter.model.Result> categoryFilterList;
    public ArrayList<com.tookancustomer.filter.model.Result> allFilterList;
    JSONObject filterObject;
    private RecyclerView rvRestaurantsList;
    private LinearLayoutManager linearLayoutManager;
    private List<Datum> merchantsArrayList = new ArrayList<>();
    private CityStorefrontsModel cityStorefrontsModel;
    private LinearLayout llNoStoresAvailable;
    private TextView tvNoStorefrontFound;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Location location;
    private boolean isChanged = false;
    private Button btnGoToLocationActivity;
    private ImageView ivCurvedBg;
    private CardView card_view;
    private LinearLayout llLoadMoreView;
    private boolean showMoreLoading = true; //If showMoreLoading is true then only scroll down on recyclerview will work.
    private WishlistAdapter adapter;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mActivity = this;
        llLoadMoreView = findViewById(R.id.llLoadMoreView);
        llNoStoresAvailable = findViewById(R.id.llNoStoresAvailable);
        card_view = findViewById(R.id.card_view2);

        ivCurvedBg = findViewById(R.id.ivCurvedBg);
        tvNoStorefrontFound = findViewById(R.id.tvNoStorefrontFound);

        rvRestaurantsList = findViewById(R.id.rvRestaurantsList);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(mActivity,R.string.favourite_merchant));

        linearLayoutManager = new LinearLayoutManager(mActivity);
        rvRestaurantsList.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setAutoMeasureEnabled(true);//if RecyclerView does not render items.

        rvRestaurantsList.setItemAnimator(new DefaultItemAnimator());
        rvRestaurantsList.setHasFixedSize(false);
        rlBack.setOnClickListener(this);


        getMarketplaceStorefronts();

        setData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getMarketplaceStorefronts();
            }
        });

    }


    public void getMarketplaceStorefronts() {
        getMarketplaceStorefronts(0);
    }

    public void getMarketplaceStorefronts(int offset) {
        CommonParams.Builder commonParams = new CommonParams.Builder();

        commonParams.add(LATITUDE, getIntent().getDoubleExtra(Keys.Extras.PICKUP_LATITUDE, 0))
                .add(LONGITUDE, getIntent().getDoubleExtra(Keys.Extras.PICKUP_LONGITUDE, 0))
                .add(FORM_ID, StorefrontCommonData.getAppConfigurationData().getFormId())
                .add(IS_DEMO_APP, Dependencies.isDemoApp() ? 1 : 0)
                .add(APP_TYPE, "ANDROID")
                .add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(YELO_APP_TYPE, 2)
                .add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                .add(APP_ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getAppAccessToken())
                .add("is_wishlist", 1);

        if (commonParams.build().getMap().containsKey(USER_ID))
            commonParams.build().getMap().remove(USER_ID);

        commonParams.add(VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        {
            filterObject = null;

            if (isAnyFilterApplied())
                filterObject = generateFilterJsonObject();

            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
            }
            commonParams.add("skip", offset);
            commonParams.add("limit", MERCHANT_PAGINATION_LIMIT);

            RestClient.getApiInterface(mActivity).getMarketplaceStorefronts(commonParams.build().getMap(), filterObject)
                    .enqueue(getMerchantListResponseResolver(offset));
        }
    }

    private ResponseResolver<BaseModel> getMerchantListResponseResolver(final int offset) {
        return new ResponseResolver<BaseModel>(mActivity, true && llLoadMoreView.getVisibility() == View.VISIBLE, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<>(Arrays.asList(datum)));
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                if (offset == 0) {
                    cityStorefrontsModel = cityStorefrontsModels;
                    merchantsArrayList.clear();
                    merchantsArrayList.addAll(cityStorefrontsModel.getData());

                } else {
                    ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum> storeList = new ArrayList<>();
                    storeList.addAll(cityStorefrontsModel.getData());
                    storeList.addAll(cityStorefrontsModels.getData());
                    cityStorefrontsModel.setData(storeList);
                    merchantsArrayList.addAll(cityStorefrontsModels.getData());
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                llLoadMoreView.setVisibility(View.GONE);
                showMoreLoading = cityStorefrontsModels.getData().size() >= MERCHANT_PAGINATION_LIMIT; //Show more loading sets to false because no more restaurants are there in the list.

                if (merchantsArrayList.size() == 0) {

                    card_view.setCardBackgroundColor(mActivity.getResources().getColor(R.color.grey_bg));

                    setNodataText();
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    tvNoStorefrontFound.setText(getStrings(mActivity, R.string.no_fav_merchant_found));
//                    btnGoToLocationActivity.setVisibility(View.VISIBLE);
                } else {
                    card_view.setCardBackgroundColor(mActivity.getResources().getColor(R.color.white));
                    ivCurvedBg.setVisibility(View.VISIBLE);
                }

                if (adapter == null) {
                    adapter = new WishlistAdapter(mActivity, merchantsArrayList);
                    rvRestaurantsList.setAdapter(adapter);
                } else {
                    if (merchantsArrayList.size() == 0 || offset == 0) {
                        adapter = null;

                        adapter = new WishlistAdapter(mActivity, merchantsArrayList);
                        rvRestaurantsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemRangeInserted(offset, merchantsArrayList.size());
                    }
                }

                // Its Update the selected Product info when admin change the data
                if (cityStorefrontsModels != null && cityStorefrontsModels.getData().size() > 0) {
                    for (int i = 0; i < cityStorefrontsModels.getData().size(); i++) {
                        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                            if (cityStorefrontsModels.getData().get(i).getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                                    Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(cityStorefrontsModels.getData().get(i));
                                }
                            }
                        }
                        if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
                            cityStorefrontsModels.getData().get(i).setSelectedPickupMode(StorefrontCommonData.getFormSettings().getSelfPickup());
                        } else {
                            cityStorefrontsModels.getData().get(i).setSelectedPickupMode(0);
                            MyApplication.getInstance().setSelectedPickUpMode(StorefrontCommonData.getFormSettings().getSelfPickup());
                        }
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (adapter == null) {
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    ivCurvedBg.setVisibility(View.GONE);
                    card_view.setCardBackgroundColor(mActivity.getResources().getColor(R.color.grey_bg));

                    setNodataText();
//                    if (isFirstScreen) btnGoToLocationActivity.setVisibility(View.VISIBLE);
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                showMoreLoading = true;
                llLoadMoreView.setVisibility(View.GONE);
            }
        };
    }

    private void setNodataText() {
        if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
            if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
                tvNoStorefrontFound.setText(StorefrontCommonData.getString(mActivity, R.string.change_DeliveryMode_or_location).replace(SELFPICKUP_SELFPICKUP, StorefrontCommonData.getTerminology().getSelfPickup()));
            } else {
                tvNoStorefrontFound.setText((StorefrontCommonData.getString(mActivity, R.string.stores_not_available_selfPickup).replace(SELFPICKUP_SELFPICKUP, StorefrontCommonData.getTerminology().getSelfPickup())
                        .replace(STORE, StorefrontCommonData.getTerminology().getStore(false))));
            }
        } else {
            if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1)
                tvNoStorefrontFound.setText((StorefrontCommonData.getString(mActivity, R.string.stores_not_available_selfPickup).replace(SELFPICKUP_SELFPICKUP, StorefrontCommonData.getTerminology().getSelfPickup())
                        .replace(STORE, StorefrontCommonData.getTerminology().getStore(false))));
            else if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
                tvNoStorefrontFound.setText(StorefrontCommonData.getString(mActivity, R.string.no_restaurants_found_please_change_your_location));
        }

    }

    public JSONObject generateFilterJsonObject() {
        ArrayList<com.tookancustomer.filter.model.Result> filterList = new ArrayList<>();
        if (categoryFilterList != null) {
            filterList.addAll(categoryFilterList);
        }
        filterList.addAll(allFilterList);

        JSONObject filterObject = new JSONObject();
        for (int i = 0; i < filterList.size(); i++) {
            com.tookancustomer.filter.model.Result filterData = filterList.get(i);
            ArrayList<AllowedDataList> allowedDataList = filterData.getAllowedDataList();
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < allowedDataList.size(); j++) {
                if (allowedDataList.get(j).isSelected()) {
                    jsonArray.put(allowedDataList.get(j).getValue());
                }
            }

            if (jsonArray.length() > 0) {
                try {
                    filterObject.put(filterData.getLabel(), jsonArray);
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }
        }

        return filterObject;
    }

    public boolean isAnyFilterApplied() {
        boolean isAnyFilterApplied = false;

        ArrayList<com.tookancustomer.filter.model.Result> filterList = new ArrayList<>();
        if (categoryFilterList != null) {
            filterList.addAll(categoryFilterList);
        }
        if (allFilterList != null) {
            filterList.addAll(allFilterList);
        }


        for (int i = 0; i < filterList.size(); i++) {
            com.tookancustomer.filter.model.Result filterData = filterList.get(i);
            ArrayList<AllowedDataList> allowedDataList = filterData.getAllowedDataList();
            for (int j = 0; j < allowedDataList.size(); j++) {
                if (allowedDataList.get(j).isSelected()) {
                    isAnyFilterApplied = true;
                    break;
                }
            }
            if (isAnyFilterApplied)
                break;
        }
        return isAnyFilterApplied;
    }

    private void setData() {
        if (cityStorefrontsModel != null) {
            setMerchants();
        }
    }

    private void setMerchants() {
        if (cityStorefrontsModel.getData().size() == 0) {
//            ((NavigationActivity) mActivity).ivMap.setVisibility(View.GONE);
//            llNoStoresAvailable.setVisibility(View.VISIBLE);
            card_view.setCardBackgroundColor(mActivity.getResources().getColor(R.color.grey_bg));
            ivCurvedBg.setVisibility(View.GONE);
//            if (((NavigationActivity) mActivity).isFirstScreen)
            btnGoToLocationActivity.setVisibility(View.VISIBLE);
        } else {
//            llNoStoresAvailable.setVisibility(View.GONE);
            card_view.setCardBackgroundColor(mActivity.getResources().getColor(R.color.white));

            ivCurvedBg.setVisibility(View.VISIBLE);

//            ((NavigationActivity) mActivity).ivMap.setVisibility(View.GONE);
        }
        for (int i = 0; i < merchantsArrayList.size(); i++) {

            if (merchantsArrayList.get(i).getIsWishlisted() == 1) {
                merchantsArrayList = cityStorefrontsModel.getData();
                adapter = new WishlistAdapter(mActivity, merchantsArrayList);
                rvRestaurantsList.setAdapter(adapter);
            }
        }
    }


    public void addRemoveWishlist(boolean wishlistSelected, int adapterPosition) {
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


        RestClient.getApiInterface(mActivity).merchantWishlist(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {

            @Override
            public void success(BaseModel baseModel) {
                isChanged = true;
                Utils.snackbarSuccess(mActivity, "Success.");
                if(adapter.getItemCount() == 1) {
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    tvNoStorefrontFound.setText(getStrings(mActivity, R.string.no_fav_merchant_found));
                }
                adapter.addRemoveItem(adapterPosition);

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Utils.snackBar(mActivity, error.getMessage());
            }
        });
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
        }
    }

    @Override
    public void onBackPressed() {
        performBackAction();
       /* Intent returnIntent = new Intent();
        returnIntent.putExtra("name", tvCustomerName.getText().toString());
        returnIntent.putExtra("image", imageFile);
        setResult(RESULT_OK, returnIntent);
        finish();*/
    }

    private void performBackAction() {
        Intent returnIntent = new Intent();
        if (isChanged) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED, returnIntent);
        }
        finish();
    }


}
