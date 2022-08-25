package com.tookancustomer.modules.recurring.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.TasksActivity;
import com.tookancustomer.adapters.AllRecurringTasksAdapter;
import com.tookancustomer.adapters.AllTasksAdapter;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.MetaInfo;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItem;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItemSelected;
import com.tookancustomer.models.ProductCatalogueData.CustomizeOption;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.ProductCatalogueData.ProductCatalogueData;
import com.tookancustomer.models.allrecurringdata.AllRecurringData;
import com.tookancustomer.models.allrecurringdata.Result;
import com.tookancustomer.models.alltaskdata.AllTaskResponse;
import com.tookancustomer.models.alltaskdata.Data;
import com.tookancustomer.models.alltaskdata.OrderDetails;
import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.listDialog.ListDialog;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.DATE_TIME;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_PREORDER_SELECTED_FOR_MENU;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Prefs.LATITUDE;
import static com.tookancustomer.appdata.Keys.Prefs.LONGITUDE;
import static com.tookancustomer.appdata.StorefrontCommonData.getFormSettings;
import static com.tookancustomer.appdata.TerminologyStrings.CART;
import static com.tookancustomer.appdata.TerminologyStrings.PRODUCTS;
import static com.tookancustomer.appdata.TerminologyStrings.STORE;


public class AllTaskFragment extends Fragment {
    boolean isValid = false;
    private AppCompatSpinner spOrderFilter;
    private int typeOrderFilter = 0;
    private LinearLayout llNoOrders;
    private ImageView ivDropDown;
    private TextView tvWaiting, tvOrderFilter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvAllItems;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<ArrayList<Data>> dataList = new ArrayList<>();
    private ArrayList<ArrayList<Data>> dataListFiltered = new ArrayList<>();
    private AllTasksAdapter allTasksAdapter;
    private LinearLayout llLoadMoreView;
    private ShimmerFrameLayout shimmerLayout;
    private Activity activity;
    private EditText etSearchOrder;
    private ArrayList<ArrayList<com.tookancustomer.models.allTasks.Datum>> data = new ArrayList<>();
    private ArrayList<Result> dataListRecurring = new ArrayList();
    private AllRecurringTasksAdapter allTasksAdapterRecurring;
    private ArrayList<String> jobStatusFilter = new ArrayList();
    private ArrayList<String> jobStatusFilterValue = new ArrayList();

    private boolean showMoreLoading = true; //If showMoreLoading is true then only scroll down on recyclerview will work.
    private int limit = 0; //Here limit refers to offset for  fetching nextBookings.
    private boolean isRecurring;


    private Datum storefrontData;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            initializeData();
        }
    };
    private Double minAmountPrice = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_all_tasks, container, false);

        //Setting the ArrayAdapter data on the Spinner
        isRecurring = getArguments().getBoolean("isRecurring");

        initViews(rootView);


        initializeData();
        LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver, new IntentFilter("refresh"));

        return rootView;
    }

    private void initViews(View view) {
        jobStatusFilter.add("All");
        jobStatusFilter.add(StorefrontCommonData.getTerminology().getPending());
        if (StorefrontCommonData.getAppConfigurationData().getIsFoodReadyOptionEnable() == 1) {
            jobStatusFilter.add(StorefrontCommonData.getTerminology().getFoodReady());
        }
        jobStatusFilter.add(StorefrontCommonData.getTerminology().getOrdered());
        if (StorefrontCommonData.getAppConfigurationData().getAdditionalTookanStatus() && UIManager.isTookanActive()) {
            jobStatusFilter.add(StorefrontCommonData.getTerminology().getJobassigned());
            jobStatusFilter.add(StorefrontCommonData.getTerminology().getProcessed());
            jobStatusFilter.add(StorefrontCommonData.getTerminology().getPickedup());
        }
        jobStatusFilter.add(StorefrontCommonData.getTerminology().getDispatched());
        jobStatusFilter.add(StorefrontCommonData.getTerminology().getCompleted());
        if (StorefrontCommonData.getAppConfigurationData().getAcceptRejectEnabled() == 1) {
            jobStatusFilter.add(StorefrontCommonData.getTerminology().getRejected());
        }
        jobStatusFilter.add(StorefrontCommonData.getTerminology().getCancelled());
        // StorefrontCommonData.getAppConfigurationData().getAdditionalTookanStatus()
        // UIManager.isTookanActive()  ----> PickUP Noti
        //StorefrontCommonData.getAppConfigurationData().getAcceptRejectEnabled()
        // ----> Show Accept Reject
        //StorefrontCommonData.getAppConfigurationData().getIsFoodReadyOptionEnable()
        // --->>> Show foodReady

        jobStatusFilterValue.add("0");
        jobStatusFilterValue.add("9");
        if (StorefrontCommonData.getAppConfigurationData().getIsFoodReadyOptionEnable() == 1) {
            jobStatusFilterValue.add("47");
        }
        jobStatusFilterValue.add("10");

        if (StorefrontCommonData.getAppConfigurationData().getAdditionalTookanStatus() && UIManager.isTookanActive()) {
            jobStatusFilterValue.add("44");
            jobStatusFilterValue.add("45");
            jobStatusFilterValue.add("46");
        }

        jobStatusFilterValue.add("12");
        jobStatusFilterValue.add("13");

        if (StorefrontCommonData.getAppConfigurationData().getAcceptRejectEnabled() == 1) {
            jobStatusFilterValue.add("15");
        }
        jobStatusFilterValue.add("15");


        ivDropDown = view.findViewById(R.id.ivDropDown);
        ivDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOrderFilter.performClick();
            }
        });
        tvOrderFilter = view.findViewById(R.id.tvOrderFilter);
        tvWaiting = view.findViewById(R.id.tvWaiting);
        tvWaiting.setText(((TasksActivity) activity).getStrings(R.string.please_wait));

        etSearchOrder = view.findViewById(R.id.etSearchOrder);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        llNoOrders = view.findViewById(R.id.llNoOrders);
        llNoOrders.setVisibility(View.GONE);
        TextView tvNoData = view.findViewById(R.id.tvNoData);
        tvNoData.setText(((TasksActivity) activity).getStrings(R.string.No_text) + " " + Utils.getCallTaskAs(StorefrontCommonData.getUserData(), false,
                false) + " " + ((TasksActivity) activity).getStrings(R.string.found) + ".");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvAllItems = view.findViewById(R.id.rvAllTasks);
        mLayoutManager = new LinearLayoutManager(activity);
        rvAllItems.setItemAnimator(new DefaultItemAnimator());
        rvAllItems.setLayoutManager(mLayoutManager);


        allTasksAdapterRecurring = new AllRecurringTasksAdapter(activity, dataListRecurring);

        allTasksAdapter = new AllTasksAdapter(activity, dataList, this);

        if (!isRecurring) {
            rvAllItems.setAdapter(allTasksAdapter);
        } else {
            rvAllItems.setAdapter(allTasksAdapterRecurring);

        }

        etSearchOrder.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 5) {
                    getNextBookings(0, !swipeRefreshLayout.isRefreshing(), 0, s.toString());
                }

                if (s.length() == 0) {
                    getNextBookings(0, !swipeRefreshLayout.isRefreshing(), 0, "");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        llLoadMoreView = view.findViewById(R.id.llLoadMoreView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeData();
            }
        });
        tvOrderFilter.setText("All");
        tvOrderFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListDialog.with(getActivity()).show("Select", jobStatusFilter, new ListDialog.OnListItemClickListener() {
                    @Override
                    public void onListItemSelected(int pos, String itemString) {
                        tvOrderFilter.setText(jobStatusFilter.get(pos));
                        typeOrderFilter = Integer.parseInt(jobStatusFilterValue.get(pos));
                        getNextBookings(0, !swipeRefreshLayout.isRefreshing(), typeOrderFilter, "");
                    }
                });
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void getNextBookingsRecurring(int newLimit, boolean showLoading) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
        commonParams.add("start", newLimit);


        RestClient.getApiInterface(activity).getRecurringTask(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(activity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {

                        AllRecurringData allRecurringData = baseModel.toResponseModel(AllRecurringData.class);

                        dataListRecurring.addAll(allRecurringData.getResult());

                        ((TasksActivity) activity).stopShimmerAnimation(shimmerLayout);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        setupAdapter();

                        if (allRecurringData.getResult().size() == 0) {
                            showMoreLoading = false; //Show more loading sets to false because no more orders are there in the list.
                        } else {
                            showMoreLoading = true;
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        ((TasksActivity) activity).stopShimmerAnimation(shimmerLayout);
                        setupAdapter();
                        showMoreLoading = true;
                    }
                });
    }

    private void initializeData() {
        /* Here reset all data --> Clear list , set limit to 0 and set moreLoading to true */
        showMoreLoading = true;
        dataList.clear();
        dataListRecurring.clear();
        limit = 0;

        tvWaiting.setVisibility(View.GONE);
        ((TasksActivity) activity).startShimmerAnimation(shimmerLayout);

        llNoOrders.setVisibility(View.GONE);

        if (!isRecurring) {
            getNextBookings(limit, !swipeRefreshLayout.isRefreshing(), typeOrderFilter, "");
        } else {
            getNextBookingsRecurring(limit, !swipeRefreshLayout.isRefreshing());
        }

        rvAllItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int pastVisiblesItems, visibleItemCount, totalItemCount;

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (showMoreLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            showMoreLoading = false;
                            limit = dataList.size(); //Here limit specifies the next element for fetch nextBookings
                            if (isRecurring)
                                limit = dataListRecurring.size(); //Here limit specifies the next element for fetch nextBookings
//                            limit = limit + 10;


                            llLoadMoreView.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isRecurring) {
                                        getNextBookings(limit, !swipeRefreshLayout.isRefreshing(), typeOrderFilter, "");
                                    } else {
                                        getNextBookingsRecurring(limit, !swipeRefreshLayout.isRefreshing());
                                    }
                                }
                            }, 1000);
                        }
                    }
                }
            }
        });
    }

    private void getNextBookings(int newLimit, boolean showLoading, final int type, final String jobId) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
        commonParams.add("limit", newLimit);
        commonParams.add("new_response", 1);
//        commonParams.add("skip", newLimit);

        if (!jobId.isEmpty()) {
            commonParams.add("job_id", jobId);
        }

        if (type != 0) {
            commonParams.add("job_status_filter", type);
        }

        RestClient.getApiInterface(activity).getAllTasks(commonParams.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(activity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        AllTaskResponse allTaskResponse = new AllTaskResponse();
                        try {
                            ArrayList<ArrayList<Data>> mList = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                    new TypeToken<ArrayList<ArrayList<Data>>>() {
                                    }.getType());
                            allTaskResponse.setData(mList);
                        } catch (Exception e) {
                        }
                        if (limit == 0) {
                            dataList.clear();
                            dataListFiltered.clear();
                            // dataList = new ArrayList<>();
                            if (tvOrderFilter.getText().toString().equals(StorefrontCommonData.getTerminology().getCancelled())) {
                                for (int i = 0; i < allTaskResponse.getData().size(); i++) {
                                    if (allTaskResponse.getData().get(i).get(0).getJobStatus() == 15) {
                                        dataListFiltered.add(allTaskResponse.getData().get(i));
                                    }
                                }

                            } else if (tvOrderFilter.getText().toString().equals(StorefrontCommonData.getTerminology().getRejected())) {
                                for (int i = 0; i < allTaskResponse.getData().size(); i++) {
                                    if (allTaskResponse.getData().get(i).get(0).getJobStatus() == 14) {
                                        dataListFiltered.add(allTaskResponse.getData().get(i));
                                    }
                                }
                            }
                        }
                        if (type == 15) {
                            //dataList = dataListFiltered;
                            dataList.addAll(dataListFiltered);
                        } else {
                            dataList.addAll(allTaskResponse.getData());
                        }

                        ((TasksActivity) activity).stopShimmerAnimation(shimmerLayout);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        setupAdapter();

                        if (allTaskResponse.getData().size() == 0) {
                            showMoreLoading = false; //Show more loading sets to false because no more orders are there in the list.
                        } else {
                            showMoreLoading = true;
                        }

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        ((TasksActivity) activity).stopShimmerAnimation(shimmerLayout);
                        setupAdapter();
                        showMoreLoading = true;
                    }
                });
    }

    private void setupAdapter() {
        tvWaiting.setVisibility(View.GONE);
        llLoadMoreView.setVisibility(View.GONE);
        try {
            if (dataList.size() > 0 || dataListRecurring.size() > 0) {
//            if (dataList.size() > 0) {
                rvAllItems.getRecycledViewPool().clear();

                if (!isRecurring) {
                    allTasksAdapter.notifyDataSetChanged();
                } else {
                    allTasksAdapterRecurring.notifyDataSetChanged();
                }
                llNoOrders.setVisibility(View.GONE);
            } else {
                llNoOrders.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
            llNoOrders.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mMessageReceiver);
    }

    public void getMarketplaceStorefronts(final int pos, final Data currentTask, final boolean isEditOrder) {
        Location location = LocationUtils.getLastLocation(activity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());


        commonParams.add(Keys.APIFieldKeys.LATITUDE, location != null ? location.getLatitude() : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, location != null ? location.getLongitude() : 0);

        if (isEditOrder) {
            LatLng latLng = new LatLng(Double.valueOf(currentTask.getJobLatitude()), Double.valueOf(currentTask.getJobLongitude()));
            commonParams.add(Keys.APIFieldKeys.LATITUDE, latLng.latitude)
                    .add(Keys.APIFieldKeys.LONGITUDE, latLng.longitude);
        }

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        commonParams.add(USER_ID, currentTask.getMerchantId());
        commonParams.add("skip_geofence", "1");

        RestClient.getApiInterface(activity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    Datum[] datum = baseModel.toResponseModel(Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<Datum>(Arrays.asList(datum)));

                    storefrontData = cityStorefrontsModels.getData().get(0);
                    StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(storefrontData.getMerchantMinimumOrder());
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

                    if (storefrontData.getSelfPickup() == 1 && currentTask.getDelivery_method() == Constants.DeliveryMode.SELF_PICKUP) {
                        storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.SELF_PICKUP);

                    } else if (storefrontData.getHomeDelivery() == 1 && currentTask.getDelivery_method() == Constants.DeliveryMode.HOME_DELIVERY) {
                        storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.HOME_DELIVERY);
                    } else if (storefrontData.getIsPdFlow() && currentTask.getDelivery_method() == Constants.DeliveryMode.PICK_AND_DROP) {
                        storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.PICK_AND_DROP);
                    } else {

                        if (storefrontData.getIsPdFlow()) {
                            storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.PICK_AND_DROP);
                        }
                        if (storefrontData.getSelfPickup() == 1) {
                            storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.SELF_PICKUP);
                        }
                        if (storefrontData.getHomeDelivery() == 1) {
                            storefrontData.setSelectedPickupMode(Constants.SelectedPickupMode.HOME_DELIVERY);
                        }
                    }


                    if (storefrontData.getCanServe() == 1 &&
                            (storefrontData.getIsStorefrontOpened() == 1
                                    || storefrontData.getScheduledTask() == 1
                                    || storefrontData.getIsStartEndTimeEnable() == 1)) {
                        getProducts(pos, currentTask, isEditOrder);

                    } else {
                        if (storefrontData.getCanServe() == 0) {
                            Utils.snackBar(activity, getString(R.string.store_dont_serve_at_location).replace(STORE, StorefrontCommonData.getTerminology().getStore(false)));
                        } else {
                            Utils.snackBar(activity, getString(R.string.store_currently_closed));
                        }
                    }


                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }

//                setData();
//                isGuest = false;

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Utils.snackBar(activity, getString(R.string.store_currently_closed));

            }
        });
    }

    public void getProducts(int pos, Data currentTask, boolean isEditOrder) {


        Location location = LocationUtils.getLastLocation(activity);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
        commonParams.add("point", location.getLatitude() + " " + location.getLongitude());
        commonParams.add("limit", 30);
        commonParams.add("offset", 0);

        commonParams.remove(USER_ID);
        commonParams.add(USER_ID, currentTask.getMerchantId());

        if (activity instanceof MerchantCatalogActivity) {
            ((MerchantCatalogActivity) activity).llVegSwitchLayout.setVisibility((StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegFilter() == 1
                    && storefrontData.isVegFilterActive()) ? View.VISIBLE : View.GONE);
            if (((MerchantCatalogActivity) activity).switchVegFilter.isChecked()) {
                commonParams.add("is_veg", 1);
            }
        }

        if (getFormSettings().getProductView() == 1) {
            commonParams.build().getMap().remove("point");
            commonParams.add(LATITUDE, location.getLatitude());
            commonParams.add(LONGITUDE, location.getLongitude());

        }

        JSONArray productIds = new JSONArray();
        for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {
            productIds.put(currentTask.getOrderDetails().get(i).getProduct().getProductId());

        }
        commonParams.add("product_ids_array", productIds);
        commonParams.add(DATE_TIME, DateUtils.getInstance().getCurrentDateTimeUtc());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        commonParams.add(IS_PREORDER_SELECTED_FOR_MENU, Dependencies.getIsPreorderSelecetedForMenu());
        RestClient.getApiInterface(activity).getProductCatalogue(commonParams.build().getMap()).enqueue(finalProductResponseResolver(pos, currentTask, isEditOrder));

    }

    private ResponseResolver<BaseModel> finalProductResponseResolver(final int pos, final Data currentTask, final boolean isEditOrder) {
        return new ResponseResolver<BaseModel>(activity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                final ProductCatalogueData productCatalogueDataResponse = new ProductCatalogueData();

                try {
                    MetaInfo metaInfo = baseModel.toResponseModelMetaInfo(MetaInfo.class);
//
                    com.tookancustomer.models.ProductCatalogueData.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.ProductCatalogueData.Datum[].class);

                    productCatalogueDataResponse.setData(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>(Arrays.asList(datum)));


                    if (productCatalogueDataResponse.getData() != null && productCatalogueDataResponse.getData().size() > 0) {
                        Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                        addproductsToCart(productCatalogueDataResponse, pos, currentTask, isEditOrder);

                    } else {
                        Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.products_not_available).replace(PRODUCTS, StorefrontCommonData.getTerminology().getProducts()));

                    }


                } catch (
                        Exception e) {

                    Utils.printStackTrace(e);
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        };
    }

    private void addproductsToCart(ProductCatalogueData productCatalogueDataResponse, final int pos, final Data currentTask, final boolean isEditOrder) {

        boolean allProductavailable = true;
        boolean minimumqtyCheck = true;


        for (int i = 0; i < currentTask.getOrderDetails().size(); i++) {
            for (int j = 0; j < productCatalogueDataResponse.getData().size(); j++) {
                com.tookancustomer.models.ProductCatalogueData.Datum datum = productCatalogueDataResponse.getData().get(j);

                if (datum.getProductId().equals(currentTask.getOrderDetails().get(i).getProduct().getProductId())) {

                    datum.setStorefrontData(storefrontData);

                    if (datum.getCustomizeItem().size() > 0) {
                        datum = setList(datum, j, currentTask.getOrderDetails().get(i), currentTask.getOrderDetails().get(i).getProduct().getQuantity() + "");

                    }
                    datum.setSelectedQuantity(datum.getSelectedQuantity() + currentTask.getOrderDetails().get(i).getProduct().getQuantity());


                    if (datum.getMaxProductquantity() == 0 || datum.getSelectedQuantity()
                            <= datum.getMaxProductquantity()) {

                        if (datum.getInventoryEnabled() == 0 || (datum.getSelectedQuantity() <= datum.getAvailableQuantity()
                                && datum.getInventoryEnabled() == 1)) {
                            isValid = true;
                            Dependencies.addCartItem(activity, datum);
                        } else {
                            allProductavailable = false;
                            Dependencies.addCartItem(activity, datum);

//                    Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.some_items_changed_in_cart).replace(CART, StorefrontCommonData.getTerminology().getCart()));
                        }
                    } else {
                        isValid = false;
                        allProductavailable = false;
                        datum.setSelectedQuantity(datum.getMaxProductquantity());
                        Dependencies.addCartItem(activity, datum);
//                    String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
//                            .replace(NAME, productCatalogueDataResponse.getData().get(j).getName())
//                            .replace(QUANTITY, productCatalogueDataResponse.getData().get(j).getMaxProductquantity() + "")
//                            .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
//                    Utils.snackBar(activity, message);

                    }
                }
            }

        }


        if (!allProductavailable) {

            if (Dependencies.getSelectedProductsArrayList() != null && Dependencies.getSelectedProductsArrayList().size() > 0) {

                String message = StorefrontCommonData.getString(activity, R.string.some_items_changed_in_cart).replace(CART, StorefrontCommonData.getTerminology().getCart());
                new OptionsDialog.Builder(this).message(message).positiveButton(getString(R.string.yes_text)).negativeButton(getString(R.string.no_text))
                        .listener(new OptionsDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack) {
                                isValid = true;
                                gotoCheckout(isEditOrder, currentTask);
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {
                                isValid = false;
                                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());

//
                            }
                        }).build().show();
            } else {
                Utils.snackBar(activity, StorefrontCommonData.getString(activity,
                        R.string.products_not_available).replace(PRODUCTS, StorefrontCommonData.getTerminology().getProducts()));
            }
        } else {
            if (isValid)
                gotoCheckout(isEditOrder, currentTask);
            else
                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());

        }


    }

    public com.tookancustomer.models.ProductCatalogueData.Datum setList(com.tookancustomer.models.ProductCatalogueData.Datum itemm, final int ss, final OrderDetails currentTask, String quantity) {
        com.tookancustomer.models.ProductCatalogueData.Datum item = itemm;
        ItemSelected itemSelected = new ItemSelected();
        itemSelected.setRestaurantItemId(item.getParentCategoryId());

        double customizationPrice = 0.0;

//        itemSelected.setRestaurantItemId(item.getRestaurantItemId());
        if (item.getMinProductquantity() > 1) {
            itemSelected.setQuantity(item.getMinProductquantity());
        } else {
            itemSelected.setQuantity(1);
        }


        ArrayList<CustomizeOption> customizeOptions = new ArrayList<>();
        CustomizeOption coItem = new CustomizeOption();
        coItem.setIsItem(1);
        customizeOptions.add(coItem);

        /**
         * to display long description in ProductCustomizationActivity
         * for catering market place
         */
        if (!item.getLongDescription().isEmpty()) {
            CustomizeOption coItemForLongDesc = new CustomizeOption();
            coItemForLongDesc.setLongDescription(item.getLongDescription());
            customizeOptions.add(coItemForLongDesc);
        }

        double totalPrice = item.getPrice().doubleValue();
        for (int i = 0; i < item.getCustomizeItem().size(); i++) {
            CustomizeItem customizeItem = item.getCustomizeItem().get(i);
            CustomizeOption coCustomizeItem = new CustomizeOption();
            coCustomizeItem.setIsCustomizeItem(1);
            coCustomizeItem.setCustomizeItemPos(i);
            customizeOptions.add(coCustomizeItem);

            CustomizeItemSelected customizeItemSelected = null;
            customizeItemSelected = new CustomizeItemSelected(customizeItem.getCustomizeId());

            int j = 0;
            for (CustomizeOption customizeOption : customizeItem.getCustomizeOptions()) {
                customizeOption.setIsMultiSelect(customizeItem.getIsCheckBox());
                customizeOption.setCustomizeItemPos(i);
                customizeOptions.add(customizeOption);
                if (customizeItemSelected != null) {
                    double optionPrice = 0d;
                    if (customizeItemSelected.getCustomizeOptions().size() == 0 && customizeItem.getCustomizeOptions().get(j).getIsDefault() == 1) {
                        customizeItemSelected.setDefaultSelected(true);
                        customizeItemSelected.getCustomizeOptions().add(customizeOption.getCustomizeOptionId());
                        optionPrice = customizeOption.getCustomizePrice();
                    }
                    totalPrice = totalPrice + optionPrice;
                    customizationPrice = customizationPrice + optionPrice;
                }
                j = j + 1;
            }

        }
        itemSelected.setTotalPrice(totalPrice);
        itemSelected.setCustomizationPrice(customizationPrice);
        itemSelected.setUnitCount(item.getUnitCount());


        for (int i = 0; i < currentTask.getCustomizations().size(); i++) {
            for (int j = 0; j < item.getCustomizeItem().size(); j++) {
                for (int k = 0; k < item.getCustomizeItem().get(j).getCustomizeOptions().size(); k++) {
                    if (item.getCustomizeItem().get(j).getCustomizeOptions().get(k).getCustomizeOptionId()
                            .equals(currentTask.getCustomizations().get(i).getCustId())) {

                        CustomizeOption customizeOption = item.getCustomizeItem().get(j).getCustomizeOptions().get(k);
                        CustomizeItem customizeItem = getCustomizeItem(customizeOption, item);
                        CustomizeItemSelected customizeItemSelected = item.getCustomizeItemSelected(customizeItem, true, itemSelected);
                        if (customizeOption.getIsMultiSelect() == 1) {
                            if (customizeItemSelected.getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                                customizeItemSelected.getCustomizeOptions().remove(customizeOption.getCustomizeOptionId());
                            } else {
                                customizeItemSelected.getCustomizeOptions().add(customizeOption.getCustomizeOptionId());
                            }
                        } else {
                            if (customizeItemSelected.getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                                if (!customizeItemSelected.isDefaultSelected())
                                    customizeItemSelected.getCustomizeOptions().remove(customizeOption.getCustomizeOptionId());
                            } else {
                                customizeItemSelected.getCustomizeOptions().clear();
                                customizeItemSelected.getCustomizeOptions().add(customizeOption.getCustomizeOptionId());
                            }
                        }
                        break;

                    }

                }
            }
        }

        itemSelected.setCustomizationPrice(item.getCustomizeItemsSelectedPriceForItemSelected(itemSelected));
        if (!Integer.valueOf(quantity).equals(0)) {
            itemSelected.setQuantity(Integer.valueOf(quantity));
        }

        item.getItemSelectedList().add(itemSelected);


        return item;
    }


    private CustomizeItem getCustomizeItem(CustomizeOption customizeOption, com.tookancustomer.models.ProductCatalogueData.Datum item) {
        return item.getCustomizeItem().get(customizeOption.getCustomizeItemPos());
    }

    private void gotoCheckout(final boolean isEditOrder, final Data currentTask) {
        if (StorefrontCommonData.getUserData() != null) {
            if (!Utils.internetCheck(activity)) {
                new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                return;
            }

            Boolean goToReviewCart = false;

            for (int i = 0; i < Dependencies.getSelectedProductsArrayList().size(); i++) {
                if (Dependencies.getSelectedProductsArrayList().get(i).getSelectedQuantity() > 0) {
                    goToReviewCart = true;
                }
            }

            if (!goToReviewCart) {
                new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.choose_products_for_proceeding).replace(StorefrontCommonData.getString(activity, R.string.product), StorefrontCommonData.getTerminology().getProduct())).build().show();
                return;
            }

            if (minAmountPrice > Dependencies.getProductListSubtotal()) {


//                Utils.snackBar(activity, getString(R.string.minimumOrderAmountIs).replace(ORDER, Utils.getCallTaskAs(true, false))
//                        .replace(AMOUNT, Utils.getCurrencySymbol(storefrontData.getPaymentSettings()) + (UIManager.getCurrency(Utils.getDoubleTwoDigits(minAmountPrice)))));
//                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());

                String message = StorefrontCommonData.getString(activity, R.string.some_items_changed_in_cart).replace(CART, StorefrontCommonData.getTerminology().getCart());
                new OptionsDialog.Builder(this).message(message).positiveButton(getString(R.string.yes_text)).negativeButton(getString(R.string.no_text))
                        .listener(new OptionsDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack) {
                                if (isEditOrder) {
                                    gotoAppCatalogue(currentTask);
                                } else {
                                    Bundle extraa = new Bundle();
                                    extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                                    Transition.openCheckoutActivity(activity, extraa);
                                }
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {
                                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());

//
                            }
                        }).build().show();

                return;
            }

            if (isEditOrder) {
                gotoAppCatalogue(currentTask);
            } else {
                Bundle extraa = new Bundle();
                extraa.putSerializable(STOREFRONT_DATA, storefrontData);
                Transition.openCheckoutActivity(activity, extraa);
            }
        }
    }

    private void gotoAppCatalogue(final Data currentTask) {


        StorefrontConfig.getAppCatalogueV2(activity, storefrontData.getStoreName(),
                storefrontData.getLogo(), new LatLng(Double.valueOf(storefrontData.getLatitude()),
                        Double.valueOf(storefrontData.getLongitude()))
                , new LatLng(Double.valueOf(currentTask.getJobLatitude()), Double.valueOf(currentTask.getJobLongitude())),
                currentTask.getJobAddress(), storefrontData, "", 0, true, currentTask.getJobId());
    }


}
