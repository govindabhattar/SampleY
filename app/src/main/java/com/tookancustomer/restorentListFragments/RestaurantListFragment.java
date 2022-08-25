package com.tookancustomer.restorentListFragments;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.HomeActivity;
import com.tookancustomer.MarketplaceSearchActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.adapters.AdminCatalogSubCategoryAdapter;
import com.tookancustomer.adapters.BannerAdapter;
import com.tookancustomer.adapters.BusinessCategoriesAdapter;
import com.tookancustomer.adapters.MarketplaceRestaurantListAdapter;
import com.tookancustomer.adapters.OngoingTaskAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.filter.constants.FilterConstants;
import com.tookancustomer.interfaces.RestaurantListingInterface;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.models.OnGoingOrdersData.OnGoingOrderData;
import com.tookancustomer.models.bannersData.Banner;
import com.tookancustomer.models.bannersData.BannersData;
import com.tookancustomer.models.businessCategoriesData.Data;
import com.tookancustomer.models.businessCategoriesData.Result;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static com.tookancustomer.appdata.Codes.Request.OPEN_HOME_ACTIVITY;
import static com.tookancustomer.appdata.Constants.MERCHANT_PAGINATION_LIMIT;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.BUSINESS_API_VERSION;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.CITY_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.CITY_NAME;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DUAL_USER_KEY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LANGUAGE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LATITUDE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.SEARCH_TEXT;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE_SELECTED_CATEGORIES;
import static com.tookancustomer.appdata.Keys.Extras.BUSINESS_CATEGORY_ID;
import static com.tookancustomer.appdata.Keys.Extras.FAILURE_MESSAGE;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.IS_WISHLIST_CHANGED;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA_ITEM_POS;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_MODEL;
import static com.tookancustomer.appdata.Keys.Extras.SUCCESS_MESSAGE;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Prefs.ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.Prefs.LONGITUDE;
import static com.tookancustomer.appdata.Keys.Prefs.REF_ID;
import static com.tookancustomer.appdata.TerminologyStrings.CART;
import static com.tookancustomer.appdata.TerminologyStrings.ITEMS;
import static com.tookancustomer.appdata.TerminologyStrings.SELFPICKUP_SELFPICKUP;
import static com.tookancustomer.appdata.TerminologyStrings.STORE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFragment extends Fragment implements View.OnClickListener, RestaurantListingInterface {

    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 8000; // time in milliseconds between successive task executions.
    public int parentId = 0;
    public TextView tvNoStorefrontFound, tvLabelItemsSavedInCart;
    Bundle bundle = new Bundle();
    Activity mActivity;
    boolean customCheckout = false;
    private boolean restaurantIsGuest;
    private ArrayList<com.tookancustomer.models.NLevelWorkFlowModel.Datum> adminCatalogueList = new ArrayList<>();
    private CityStorefrontsModel cityStorefrontsModel;
    private boolean isFirstScreen = true;
    private LinearLayout llNoStoresAvailable;
    private Button btnGoToLocationActivity;
    private RecyclerView rvRestaurantsList, rvAdminCategory;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MarketplaceRestaurantListAdapter adapter;
    private AdminCatalogSubCategoryAdapter adminCatalogSubCategoryAdapter;
    private RelativeLayout rlSavedCart;
    private ImageView ivHeaderImageOption;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private LinearLayout llSelfPickupDelivery, llHomeDelivery, llSelfPickup, llPickAndDrop;
    private TextView tvHomeDelivery, tvSelfPickup, tvPickAndDrop;
    private ViewPager pagerBanner;
    private BannerAdapter bannerAdapter;
    private LinearLayout llIndicators;
    private RelativeLayout rlBanner, rlFloating;
    //    private FloatingActionButton fabChatButton;
    private LinearLayoutManager linearLayoutManager;
    private ValueAnimator closeAnim, openAnim;
    private TextView tvCustomOrderButton;
    private int widthCustomOrder;
    //    private NestedScrollView nestedScrollView;
    private TextView vAdminCategoriesLabel;
    private RelativeLayout rlCustomOrder;
    private BannersData bannerData;
    private ArrayList<String> bannerList = new ArrayList<>();
    private boolean restaurantIsGuestAfterLogin;
    private AVLoadingIndicatorView pbBanner, pbCategory;
    private RestaurantListingInterface restaurantListingInterface;
    private TextView tvViewCart;
    private FloatingActionButton rlmap;
    private RelativeLayout rlPickup, rlDeliveryView, rlPickAndDropView;
    private List<com.tookancustomer.models.MarketplaceStorefrontModel.Datum> merchantsArrayList = new ArrayList<>();
    private LinearLayout llLoadMoreView;
    private boolean showMoreLoading = true; //If showMoreLoading is true then only scroll down on recyclerview will work.
    private ViewPager ongoingOrderPager;
    private Timer timer;
    private LinearLayout onGoingTaskIndicatorLL;
    private RelativeLayout ongoingorderRL;
    private ArrayList<OnGoingOrderData> onGoingOrderData = new ArrayList<>();
    private OngoingTaskAdapter ongoingTaskAdapter;
    private AppBarLayout appBarLayout;

    private ImageView ivHomedelivery, ivSelfPickup, ivPickAndDrop;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurent_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof RestaurantListingActivity) {
            ((RestaurantListingActivity) getActivity()).onPageSelected();
        }
        restaurantIsGuest = Dependencies.getAccessToken(getActivity()).isEmpty();
        bundle = getArguments();
        mActivity = getActivity();

        switch (StorefrontCommonData.getAppConfigurationData().getDefaultDeliveryMethod()) {
            case Constants.DeliveryMode.PICK_AND_DROP:
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
                break;
            case Constants.DeliveryMode.HOME_DELIVERY:
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
                break;
            case Constants.DeliveryMode.SELF_PICKUP:
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
                break;
        }

        Dependencies.setSelectedDelivery(((RestaurantListingActivity) mActivity).selectedPickupMode);


        if (bundle != null) {
            if (bundle.getSerializable(ADMIN_CATALOGUE) != null) {
                adminCatalogueList = (ArrayList<Datum>) bundle.getSerializable(ADMIN_CATALOGUE);
            }
            if (bundle.getSerializable(STOREFRONT_MODEL) != null) {
                cityStorefrontsModel = (CityStorefrontsModel) bundle.getSerializable("storefrontModel");
                merchantsArrayList = cityStorefrontsModel.getData();

                if (cityStorefrontsModel.getData().size() < MERCHANT_PAGINATION_LIMIT) {
                    showMoreLoading = false; //Show more loading sets to false because no more restaurants are there in the list.
                } else {
                    showMoreLoading = true;
                }
            }

            if (bundle.getSerializable(PARENT_ID) != null) {
                parentId = bundle.getInt(PARENT_ID, 0);
            }

            if (!Dependencies.isDemoRunning()) {
                if (bundle.getString(SUCCESS_MESSAGE) != null) {
                    Utils.snackbarSuccess(mActivity, bundle.getString(SUCCESS_MESSAGE));
                } else if (bundle.getString(FAILURE_MESSAGE) != null) {
                    Utils.snackBar(mActivity, bundle.getString(FAILURE_MESSAGE));
                }
            }

        }

        if (StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 1 && UIManager.getCustomOrderActive()) {
            isFirstScreen = false;
        }


        initViews(view);
        if (cityStorefrontsModel != null) {
            setMerchants();
        } else {
            getMarketplaceStorefronts();
        }


        if (!adminCatalogueList.isEmpty()) {
            adminCatalogSubCategoryAdapter = new AdminCatalogSubCategoryAdapter(mActivity, adminCatalogueList, new AdminCatalogSubCategoryAdapter.Callback() {
                @Override
                public void onCategorySelected(ArrayList<com.tookancustomer.models.NLevelWorkFlowModel.Datum> dataList) {
                    selectedIds.clear();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).isSelected()) {
                            selectedIds.add(dataList.get(i).getCatalogueId());
                        }
                    }
                    getMarketplaceStorefronts();
                }
            });
            rvAdminCategory.setAdapter(adminCatalogSubCategoryAdapter);
            vAdminCategoriesLabel.setVisibility(View.VISIBLE);
        } else {
            vAdminCategoriesLabel.setVisibility(View.GONE);
        }


        rlBanner.setVisibility(UIManager.getIsBannerEnabled() ? View.VISIBLE : View.GONE);
        pbBanner.setVisibility(UIManager.getIsBannerEnabled() ? View.VISIBLE : View.GONE);
        rvAdminCategory.setVisibility(UIManager.getIsBusinessCategoryEnabled() ? View.VISIBLE : View.GONE);
        vAdminCategoriesLabel.setVisibility(UIManager.getIsBusinessCategoryEnabled() ? View.VISIBLE : View.GONE);
        pbCategory.setVisibility(UIManager.getIsBusinessCategoryEnabled() ? View.VISIBLE : View.GONE);

        if (UIManager.getIsBusinessCategoryEnabled())
            callbackForBusinessCategories();
        if (UIManager.getIsBannerEnabled())
            callbackForBanners();


        rvRestaurantsList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisiblesItems, visibleItemCount, totalItemCount;

//            View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
//            int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (showMoreLoading) {
                        if (showMoreLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                showMoreLoading = false;
                                llLoadMoreView.setVisibility(View.VISIBLE);
                                       /* new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {*/
                                getMarketplaceStorefronts(cityStorefrontsModel != null ? cityStorefrontsModel.getData().size() : 0);
                                      /*      }
                                        }, 1000);*/
                            }
                        }
                    }
                }


                if (dy > 0) {

                    if (openAnim != null && openAnim.isRunning())
                        openAnim.cancel();

                    if (closeAnim == null || !closeAnim.isRunning()) {
                        closeAnim = ValueAnimator.ofInt(tvCustomOrderButton.getMeasuredWidth(), 0);
                        closeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                int val = (Integer) valueAnimator.getAnimatedValue();
                                ViewGroup.LayoutParams layoutParams = tvCustomOrderButton.getLayoutParams();
                                layoutParams.width = val;
                                tvCustomOrderButton.setLayoutParams(layoutParams);
                            }
                        });

                        closeAnim.setDuration(180);
                        closeAnim.start();
                    }


                } else {
                    if (closeAnim != null && closeAnim.isRunning())
                        closeAnim.cancel();
                    if (openAnim == null || !openAnim.isRunning()) {
                        openAnim = ValueAnimator.ofInt(tvCustomOrderButton.getMeasuredWidth(), widthCustomOrder);
                        openAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                int val = (Integer) valueAnimator.getAnimatedValue();
                                ViewGroup.LayoutParams layoutParams = tvCustomOrderButton.getLayoutParams();
                                layoutParams.width = val;
                                tvCustomOrderButton.setLayoutParams(layoutParams);
                            }
                        });
                        openAnim.setDuration(180);
                        openAnim.start();
                    }

                }


            }
        });
    }


    public void getMarketplaceStorefronts() {
        getMarketplaceStorefronts(0);
    }

    public void getMarketplaceStorefronts(int offset) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(CITY_ID, "")
                .add(CITY_NAME, "")
                .add(LATITUDE, ((RestaurantListingActivity) mActivity).latitude != null ? ((RestaurantListingActivity) mActivity).latitude : 0)
                .add(LONGITUDE, ((RestaurantListingActivity) mActivity).longitude != null ? ((RestaurantListingActivity) mActivity).longitude : 0)
                .add(SEARCH_TEXT, "");

        if (commonParams.build().getMap().containsKey(USER_ID))
            commonParams.build().getMap().remove(USER_ID);


//        if ((StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1)
//                || (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
//                || (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1)
//                || (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1)
//
//
//        ) {
        commonParams.add("home_delivery", ((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY ? 1 : 0);
        commonParams.add("self_pickup", ((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP ? 1 : 0);
        commonParams.add("pick_and_drop", ((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP ? 1 : 0);

//        }

        if (StorefrontCommonData.getUserData().getData().getAdminCategoryEnabled() == 1) {

            RestClient.getApiInterface(mActivity).getAdminMerchantList(commonParams.build().getMap())
                    .enqueue(getMerchantListResponseResolver(offset));
        } else {
            if (((RestaurantListingActivity) mActivity).businessCategoryId != null && !((RestaurantListingActivity) mActivity).isAllBusinessCategory)
                commonParams.add(BUSINESS_CATEGORY_ID, ((RestaurantListingActivity) mActivity).businessCategoryId);

            JSONObject filterObject = null;
            if (((RestaurantListingActivity) mActivity).isAnyFilterApplied())
                filterObject = ((RestaurantListingActivity) mActivity).generateFilterJsonObject();

            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
            }
            commonParams.add("skip", offset);
            commonParams.add("limit", MERCHANT_PAGINATION_LIMIT);

            RestClient.getApiInterface(mActivity).getMarketplaceStorefronts(commonParams.build().getMap(), filterObject)
                    .enqueue(getMerchantListResponseResolver(offset));
        }
    }

    private void setMerchants() {
        if (cityStorefrontsModel.getData().size() == 0) {
            ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
            llNoStoresAvailable.setVisibility(View.VISIBLE);
            if (isFirstScreen) btnGoToLocationActivity.setVisibility(View.VISIBLE);
        } else {
            llNoStoresAvailable.setVisibility(View.GONE);
            ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
        }
        merchantsArrayList = cityStorefrontsModel.getData();
        adapter = new MarketplaceRestaurantListAdapter(mActivity, merchantsArrayList, this);
        rvRestaurantsList.setAdapter(adapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        //handling guest and login session
        restaurantIsGuestAfterLogin = Dependencies.getAccessToken(mActivity).isEmpty();
        if (restaurantIsGuest != restaurantIsGuestAfterLogin) {
            getMarketplaceStorefronts();
            restaurantIsGuest = !restaurantIsGuest;
        }
        MyApplication.getInstance().trackScreenView(getClass().getSimpleName());
        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
        int ref_id = Prefs.with(mActivity).getInt(REF_ID, 0);
        if(ref_id != 0 && rlSavedCart != null) {
            CommonParams.Builder commonParams = new CommonParams.Builder();
            commonParams.add("reference_id", ref_id);
            RestClient.getJunglePaymentsApi(mActivity).razorpayPaymentStatus(commonParams.build().getMap()).enqueue
                    (new ResponseResolver<BaseModel>(mActivity, true, true) {

                        @Override
                        public void success(BaseModel baseModel) {
                            boolean status = (boolean) ((LinkedTreeMap) baseModel.getData()).get("status");
                            if (status && rlSavedCart != null) {
                                Prefs.with(mActivity).remove(REF_ID);
                                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                                rlSavedCart.setVisibility(Dependencies.getSelectedProductsArrayList().size() > 0 ? View.VISIBLE : View.GONE);
                            }
                        }

                        @Override
                        public void failure(APIError error, BaseModel baseModel) {

                        }
                    });
        }else if(rlSavedCart != null) {
            Handler handler = new Handler();
            handler.postDelayed(() -> rlSavedCart.setVisibility(Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0)
                    .getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.MULTI_PRODUCT ? View.VISIBLE : View.GONE), 500);

            setStrings();
        }
        if (StorefrontCommonData.getUserData() != null) {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())
                getOnGoingOrders(StorefrontCommonData.getUserData());
        }
    }

    private void setStrings() {
        tvViewCart.setText(StorefrontCommonData.getString(mActivity, R.string.view));
        setNodataText();
        btnGoToLocationActivity.setText(StorefrontCommonData.getString(mActivity, R.string.select_location));
        tvLabelItemsSavedInCart.setText(StorefrontCommonData.getString(mActivity, R.string.you_have_item_saved_your_cart)
                .replace(ITEMS, StorefrontCommonData.getTerminology().getItems(false)).replace(CART, StorefrontCommonData.getTerminology().getCart(false)));
    }


    private void initViews(View view) {

        tvCustomOrderButton = view.findViewById(R.id.tvCustomOrderButton);
        rlPickup = view.findViewById(R.id.rlPickup);
        rlDeliveryView = view.findViewById(R.id.rlDeliveryView);
        rlPickAndDropView = view.findViewById(R.id.rlPickAndDropView);

        tvCustomOrderButton.setText(StorefrontCommonData.getTerminology().getCustomOrder());
        rlCustomOrder = view.findViewById(R.id.rlCustomOrder);
//        nestedScrollView = ((NestedScrollView) view.findViewById(R.id.nestedScrollView));
        ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
        llNoStoresAvailable = view.findViewById(R.id.llNoStoresAvailable);
        tvNoStorefrontFound = view.findViewById(R.id.tvNoStorefrontFound);
        btnGoToLocationActivity = view.findViewById(R.id.btnGoToLocationActivity);
        rlmap = view.findViewById(R.id.rlmap);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        llLoadMoreView = view.findViewById(R.id.llLoadMoreView);
        llLoadMoreView.setVisibility(View.GONE);

        ongoingOrderPager = view.findViewById(R.id.ongoing_order_pager);
        onGoingTaskIndicatorLL = view.findViewById(R.id.onGoingTaskIndicatorLL);
        ongoingorderRL = view.findViewById(R.id.ongoingorderRL);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (UIManager.getIsBusinessCategoryEnabled())
                    callbackForBusinessCategories();
                if (UIManager.getIsBannerEnabled())
                    callbackForBanners();

                if (StorefrontCommonData.getUserData() != null) {
                    if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())
                        getOnGoingOrders(StorefrontCommonData.getUserData());
                }

                getMarketplaceStorefronts();
            }
        });

        appBarLayout = view.findViewById(R.id.appbar);
        // Disable "Drag" for AppBarLayout (i.e. User can't scroll appBarLayout by directly touching appBarLayout - User can only scroll appBarLayout by only using scrollContent)
        if (appBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    if (merchantsArrayList != null && merchantsArrayList.size() > 0)
                        return true;
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }


        rvRestaurantsList = view.findViewById(R.id.rvRestaurantsList);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        rvRestaurantsList.setLayoutManager(linearLayoutManager);
        rvRestaurantsList.setItemAnimator(new DefaultItemAnimator());
        ViewCompat.setNestedScrollingEnabled(rvRestaurantsList, true);
        rvRestaurantsList.setHasFixedSize(false);

        tvViewCart = view.findViewById(R.id.tvViewCart);
        rvAdminCategory = view.findViewById(R.id.rvAdminCategory);
        pbBanner = view.findViewById(R.id.pbBanner);
        pbCategory = view.findViewById(R.id.pbCategory);
        rvAdminCategory.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvAdminCategory.setItemAnimator(new DefaultItemAnimator());
        rvAdminCategory.setHasFixedSize(false);

        llSelfPickupDelivery = view.findViewById(R.id.llSelfPickupDelivery);
        llHomeDelivery = view.findViewById(R.id.llHomeDelivery);
        llSelfPickup = view.findViewById(R.id.llSelfPickup);
        llPickAndDrop = view.findViewById(R.id.llPickAndDrop);
        tvHomeDelivery = view.findViewById(R.id.tvHomeDelivery);
        tvPickAndDrop = view.findViewById(R.id.tvPickAndDrop);

        tvHomeDelivery.setText(StorefrontCommonData.getTerminology().getHomeDelivery());
        tvPickAndDrop.setText(StorefrontCommonData.getTerminology().getPickupAndDrop());
        tvSelfPickup = view.findViewById(R.id.tvSelfPickup);
        rlCustomOrder.setVisibility(UIManager.getCustomOrderActive() /*&& StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 1*/
                ? View.VISIBLE : View.GONE);
        tvSelfPickup.setText(StorefrontCommonData.getTerminology().getSelfPickup());
        //tvSelfPickup.setVisibility(View.GONE);
        ivHomedelivery = view.findViewById(R.id.ivHomedelivery);
        ivPickAndDrop = view.findViewById(R.id.ivPickAndDrop);
        ivSelfPickup = view.findViewById(R.id.ivSelfPickup);

        setAddressLayout();
        pagerBanner = view.findViewById(R.id.pagerBanner);

        ViewTreeObserver viewTreeObserver = pagerBanner.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pagerBanner.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Display display = mActivity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                try {
                    display.getRealSize(size);
                } catch (NoSuchMethodError err) {
                    display.getSize(size);
                }

                int viewPagerWidth = size.x;
                float viewPagerHeight = (float) (viewPagerWidth / 4);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.width = viewPagerWidth;
                layoutParams.height = (int) viewPagerHeight;

                pagerBanner.setLayoutParams(layoutParams);
            }
        });
        setTreeObserver();

        if (StorefrontCommonData.getAppConfigurationData().getMapView() == 1) {
            rlmap.show();
        } else {
            rlmap.hide();
        }

        setViewPagerListener();
        setViewPagerOnGoinTaskListener();
        llIndicators = view.findViewById(R.id.llIndicators);
        rlBanner = view.findViewById(R.id.rlBanner);

        rlSavedCart = view.findViewById(R.id.rlSavedCart);
        tvLabelItemsSavedInCart = view.findViewById(R.id.tvLabelItemsSavedInCart);
        tvLabelItemsSavedInCart.setText(StorefrontCommonData.getString(mActivity, R.string.you_have_item_saved_your_cart).replace(ITEMS, StorefrontCommonData.getTerminology().getItems(false)).replace(CART, StorefrontCommonData.getTerminology().getCart(false)));
        Utils.setOnClickListener(this, btnGoToLocationActivity, view.findViewById(R.id.tvViewCart), view.findViewById(R.id.rlSavedCart), view.findViewById(R.id.ivDeleteCart));
        Utils.setOnClickListener(this, /*fabChatButton,*/ rlmap, rlCustomOrder, llHomeDelivery, llSelfPickup, llPickAndDrop, tvPickAndDrop, tvSelfPickup, tvHomeDelivery);
        vAdminCategoriesLabel = view.findViewById(R.id.vAdminCategoriesLabel);
    }

    private void setAddressLayout() {

        boolean ifPD = StorefrontCommonData.getFormSettings().getPickupAndDrop() == 1;

        if ((StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && ifPD)
                || (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
                || (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && ifPD)
                || (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && ifPD)
        ) {
            if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.NONE)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            llSelfPickupDelivery.setVisibility(View.VISIBLE);

            if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && ifPD) {
//                llSelfPickupDelivery.setWeightSum(3);

                llPickAndDrop.setVisibility(View.VISIBLE);
                llSelfPickup.setVisibility(View.VISIBLE);
                llHomeDelivery.setVisibility(View.VISIBLE);
            } else if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)) {
//                llSelfPickupDelivery.setWeightSum(2);
                if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP)
                    ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;

                llPickAndDrop.setVisibility(View.GONE);
                llSelfPickup.setVisibility(View.VISIBLE);
                llHomeDelivery.setVisibility(View.VISIBLE);
            } else if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && ifPD) {
                if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY)
                    ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
//                llSelfPickupDelivery.setWeightSum(2);
                llPickAndDrop.setVisibility(View.VISIBLE);
                llSelfPickup.setVisibility(View.VISIBLE);
                llHomeDelivery.setVisibility(View.GONE);
            } else if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && ifPD) {
                if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP)
                    ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
//                llSelfPickupDelivery.setWeightSum(2);
                llPickAndDrop.setVisibility(View.VISIBLE);
                llSelfPickup.setVisibility(View.GONE);
                llHomeDelivery.setVisibility(View.VISIBLE);
            }


            resetSelfPickupDeliveryAssets();
        } else {
            if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
            else if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            else if (ifPD)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
            llSelfPickupDelivery.setVisibility(View.GONE);
        }


        Dependencies.setSelectedDelivery(((RestaurantListingActivity) mActivity).selectedPickupMode);
        MyApplication.getInstance().setSelectedPickUpMode(((RestaurantListingActivity) mActivity).selectedPickupMode);


    }

    private void setTreeObserver() {
        ViewTreeObserver viewTreeObserver = tvCustomOrderButton.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tvCustomOrderButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    widthCustomOrder = tvCustomOrderButton.getWidth();

                    rvRestaurantsList.addOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            int pastVisiblesItems, visibleItemCount, totalItemCount;

//            View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
//            int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                            if (dy > 0) //check for scroll down
                            {
                                visibleItemCount = linearLayoutManager.getChildCount();
                                totalItemCount = linearLayoutManager.getItemCount();
                                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                                if (showMoreLoading) {
                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                        showMoreLoading = false;
                                        llLoadMoreView.setVisibility(View.VISIBLE);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getMarketplaceStorefronts(cityStorefrontsModel != null ? cityStorefrontsModel.getData().size() : 0);
                                            }
                                        }, 1000);
                                    }
                                }
                            }


                            if (dy > 0) {

                                if (openAnim != null && openAnim.isRunning())
                                    openAnim.cancel();

                                if (closeAnim == null || !closeAnim.isRunning()) {
                                    closeAnim = ValueAnimator.ofInt(tvCustomOrderButton.getMeasuredWidth(), 0);
                                    closeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                            int val = (Integer) valueAnimator.getAnimatedValue();
                                            ViewGroup.LayoutParams layoutParams = tvCustomOrderButton.getLayoutParams();
                                            layoutParams.width = val;
                                            tvCustomOrderButton.setLayoutParams(layoutParams);
                                        }
                                    });

                                    closeAnim.setDuration(180);
                                    closeAnim.start();
                                }


                            } else {
                                if (closeAnim != null && closeAnim.isRunning())
                                    closeAnim.cancel();
                                if (openAnim == null || !openAnim.isRunning()) {
                                    openAnim = ValueAnimator.ofInt(tvCustomOrderButton.getMeasuredWidth(), widthCustomOrder);
                                    openAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                            int val = (Integer) valueAnimator.getAnimatedValue();
                                            ViewGroup.LayoutParams layoutParams = tvCustomOrderButton.getLayoutParams();
                                            layoutParams.width = val;
                                            tvCustomOrderButton.setLayoutParams(layoutParams);
                                        }
                                    });
                                    openAnim.setDuration(180);
                                    openAnim.start();
                                }

                            }


                        }
                    });
                }
            });
        }
    }

    private void setViewPagerOnGoinTaskListener() {
        ongoingOrderPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                inflateIndicatorsOngoingTask(position, onGoingTaskIndicatorLL);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void inflateIndicatorsOngoingTask(int position, LinearLayout onGoingTaskIndicatorLL) {
        if (onGoingOrderData.size() <= 1) {
            onGoingTaskIndicatorLL.setVisibility(View.GONE);
        } else {
            onGoingTaskIndicatorLL.setVisibility(View.VISIBLE);
            onGoingTaskIndicatorLL.removeAllViews();

            int pixels = Utils.convertDpToPixels(mActivity, 6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);
//            params.bottomMargin = pixels;
            for (int index = 0; index < onGoingOrderData.size(); index++) {
                View view = new View(mActivity);
                view.setLayoutParams(params);
//                view.setBackgroundResource(index == position ? R.drawable.filled_dash_switcher : R.drawable.dash_switcher);
                view.setBackgroundResource(index == position ? R.drawable.switcher_filled_accent : R.drawable.switcher_white);

                onGoingTaskIndicatorLL.addView(view);
            }
        }
    }


    private void setViewPagerListener() {
        pagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private void inflateIndicators(int position, LinearLayout llIndicators) {
        if (bannerAdapter.getImagesList().size() <= 1) {
            llIndicators.setVisibility(View.GONE);
        } else {
            llIndicators.setVisibility(View.VISIBLE);
            llIndicators.removeAllViews();

            int pixels = Utils.convertDpToPixels(mActivity, 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);
            params.bottomMargin = pixels;
            for (int index = 0; index < bannerAdapter.getImagesList().size(); index++) {
                View view = new View(mActivity);
                view.setLayoutParams(params);
                view.setBackgroundResource(index == position ? R.drawable.switcher_filled : R.drawable.switcher);
                llIndicators.addView(view);
            }
        }
    }

    public void resetSelfPickupDeliveryAssets() {
        if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvSelfPickup);

            setDeliveryIcon(Constants.SelectedPickupMode.SELF_PICKUP);
            //    tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_active, 0, 0, 0);
            rlPickup.setVisibility(View.VISIBLE);
            rlDeliveryView.setVisibility(View.INVISIBLE);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPickAndDrop);
            //   tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
            rlPickAndDropView.setVisibility(View.INVISIBLE);
            //   tvPickAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_unselected, 0, 0, 0);
        } else if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvHomeDelivery);
            setDeliveryIcon(Constants.SelectedPickupMode.HOME_DELIVERY);

            //    tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_active, 0, 0, 0);
            //    tvPickAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_unselected, 0, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPickAndDrop);
            //  tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
            rlPickup.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.INVISIBLE);
            rlDeliveryView.setVisibility(View.VISIBLE);
        } else if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvPickAndDrop);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);

            setDeliveryIcon(Constants.SelectedPickupMode.PICK_AND_DROP);
            //   tvPickAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_selected, 0, 0, 0);
            //   tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            //   tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
            rlPickup.setVisibility(View.INVISIBLE);
            rlDeliveryView.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.VISIBLE);
        } else {
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPickAndDrop);
            //   tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
            //   tvPickAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_unselected, 0, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            //   tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
            ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            rlPickup.setVisibility(View.INVISIBLE);
            rlDeliveryView.setVisibility(View.INVISIBLE);
            rlPickAndDropView.setVisibility(View.INVISIBLE);
        }
        Dependencies.setSelectedDelivery(((RestaurantListingActivity) mActivity).selectedPickupMode);

        llHomeDelivery.setSelected(((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY);
        llSelfPickup.setSelected(((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP);
        llPickAndDrop.setSelected(((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP);

    }




   /* private void setPickAndDropIcon(int deliveryMode) {
        if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {

            if (deliveryMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_selected)
                        .setFallback(R.drawable.ic_pick_drop_selected)
                        .build();
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_self_pickup_inactive)
                        .setFallback(R.drawable.ic_self_pickup_inactive)
                        .build();
            }
        }else{
            ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_selected));
            ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_self_pickup_inactive));
        }
    }*/

   /* private void setHomeDeliveryIcon(int deliveryMode) {
        if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
            if (deliveryMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();

                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_self_pickup_inactive)
                        .setFallback(R.drawable.ic_self_pickup_inactive)
                        .build();
            }
        }else{
            ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_active));
            ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_inactive));
        }
    }*/

    private void setDeliveryIcon(int deliveryMode) {

        if (deliveryMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_active)
                        .setFallback(R.drawable.ic_pickup_active)
                        .build();
            } else {
                ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_active));
            }
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
            } else {
                ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            }
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
            } else {
                ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            }

        } else if (deliveryMode == Constants.SelectedPickupMode.PICK_AND_DROP) {

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_selected)
                        .setFallback(R.drawable.ic_pick_drop_selected)
                        .build();
            } else {
                ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_selected));
            }

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_inactive)
                        .setFallback(R.drawable.ic_pickup_inactive)
                        .build();
            } else {
                ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_inactive));
            }
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
            } else {
                ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            }


        } else if (deliveryMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_active)
                        .setFallback(R.drawable.ic_home_delivery_active)
                        .build();
            } else {
                ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_active));
            }

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
            } else {
                ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            }

            if (StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_inactive)
                        .setFallback(R.drawable.ic_pickup_inactive)
                        .build();
            } else {
                ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_inactive));
            }
        }
    }


    public Integer getBusinessCategory() {
        return ((RestaurantListingActivity) mActivity).businessCategoryId;
    }

    public boolean isAllBusinessCategory() {
        return ((RestaurantListingActivity) mActivity).isAllBusinessCategory;
    }


    private ResponseResolver<BaseModel> getMerchantListResponseResolver(final int offset) {
        return new ResponseResolver<BaseModel>(mActivity, !swipeRefreshLayout.isRefreshing() && llLoadMoreView.getVisibility() == View.GONE, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>(Arrays.asList(datum)));
                } catch (Exception e) {
//
                    Utils.printStackTrace(e);
                }

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
                            cityStorefrontsModels.getData().get(i).setSelectedPickupMode(((RestaurantListingActivity) mActivity).selectedPickupMode);
                        } else {
                            cityStorefrontsModels.getData().get(i).setSelectedPickupMode(0);
                            MyApplication.getInstance().setSelectedPickUpMode(((RestaurantListingActivity) mActivity).selectedPickupMode);


                        }
                    }
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
                if (cityStorefrontsModels.getData().size() < MERCHANT_PAGINATION_LIMIT) {
                    showMoreLoading = false; //Show more loading sets to false because no more restaurants are there in the list.
                } else {
                    showMoreLoading = true;
                }

                if (merchantsArrayList.size() == 0) {
                    ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    setNodataText();
                    if (isFirstScreen) btnGoToLocationActivity.setVisibility(View.VISIBLE);
                } else {
                    llNoStoresAvailable.setVisibility(View.GONE);
                    ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
                }

                if (adapter == null) {
                    adapter = new MarketplaceRestaurantListAdapter(mActivity, merchantsArrayList, RestaurantListFragment.this);
                    rvRestaurantsList.setAdapter(adapter);
                } else {
                    if (merchantsArrayList.size() == 0 || offset == 0) {
                        adapter = null;
                        adapter = new MarketplaceRestaurantListAdapter(mActivity, merchantsArrayList, RestaurantListFragment.this);
                        rvRestaurantsList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyItemRangeInserted(offset, merchantsArrayList.size());
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (adapter == null) {
                    llNoStoresAvailable.setVisibility(View.VISIBLE);
                    setNodataText();
                    if (isFirstScreen) btnGoToLocationActivity.setVisibility(View.VISIBLE);
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
            if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
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


    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }

        if (v.getId() == R.id.rlSavedCart || v.getId() == R.id.tvViewCart) {
            boolean isStorefrontUserIdAdded = false;
            com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData = null;

            if (cityStorefrontsModel != null) {
                for (int i = 0; i < cityStorefrontsModel.getData().size(); i++) {
                    if (Dependencies.getSelectedProductsArrayList() != null
                            && Dependencies.getSelectedProductsArrayList().size() > 0 &&
                            Dependencies.getSelectedProductsArrayList().get(0).getUserId().equals(cityStorefrontsModel.getData().get(i).getStorefrontUserId())) {

                        isStorefrontUserIdAdded = true;
                        merchantData = cityStorefrontsModel.getData().get(i);
                    }
                }
                if (!isStorefrontUserIdAdded) {
                    getSingleMerchantDetails(Dependencies.getSelectedProductsArrayList().get(0).getUserId(), new MerchantCallback() {
                        @Override
                        public void onFetchSingleMerchantData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData) {

                            if (merchantData.getCanServe() == 1) {
                                viewCart(merchantData);
                            } else {
                                new AlertDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.this_cart_will_be_deleted)).button(StorefrontCommonData.getString(mActivity, R.string.ok_text)).listener(new AlertDialog.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {
                                        Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                                        rlSavedCart.setVisibility(Dependencies.getSelectedProductsArrayList().size() > 0 ? View.VISIBLE : View.GONE);
                                    }
                                }).build().show();
                            }
                        }
                    });

                } else {
                    viewCart(merchantData);
                }
            }

        } else if (v.getId() == R.id.ivDeleteCart) {

            new OptionsDialog.Builder(mActivity).message(StorefrontCommonData.getString(mActivity, R.string.sure_to_delete_this_cart)
                    .replace(StorefrontCommonData.getString(mActivity, R.string.cart), StorefrontCommonData.getTerminology()
                            .getCart(false))).listener(new OptionsDialog.Listener() {
                @Override
                public void performPositiveAction(int purpose, Bundle backpack) {
                    Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                    rlSavedCart.setVisibility(Dependencies.getSelectedProductsArrayList().size() > 0 ? View.VISIBLE : View.GONE);
                }

                @Override
                public void performNegativeAction(int purpose, Bundle backpack) {
                }
            }).build().show();
        } else if (v.getId() == R.id.llAddressLayout || v.getId() == R.id.btnGoToLocationActivity) {
            if (!Utils.internetCheck(mActivity)) {
                new AlertDialog.Builder(this).message(StorefrontCommonData.getString(mActivity, R.string.no_internet_try_again)).build().show();
                return;
            }
            if (Dependencies.isDemoRunning()) {
                ((RestaurantListingActivity) mActivity).gotoMapActivity();
            } else {
                ((RestaurantListingActivity) mActivity).gotoFavLocationActivity();
            }
        } else if (v.getId() == R.id.llHomeDelivery || v.getId() == R.id.tvHomeDelivery) {
            ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            MyApplication.getInstance().setSelectedPickUpMode(Constants.SelectedPickupMode.HOME_DELIVERY);


            resetSelfPickupDeliveryAssets();
            getMarketplaceStorefronts();
            if (UIManager.getIsBannerEnabled())
                callbackForBanners();
        } else if (v.getId() == R.id.llSelfPickup || v.getId() == R.id.tvSelfPickup) {
            ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
            MyApplication.getInstance().setSelectedPickUpMode(Constants.SelectedPickupMode.SELF_PICKUP);


            resetSelfPickupDeliveryAssets();
            getMarketplaceStorefronts();
            if (UIManager.getIsBannerEnabled())
                callbackForBanners();
        } else if (v.getId() == R.id.llPickAndDrop || v.getId() == R.id.tvPickAndDrop) {
            ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
            MyApplication.getInstance().setSelectedPickUpMode(Constants.SelectedPickupMode.PICK_AND_DROP);

            resetSelfPickupDeliveryAssets();
            getMarketplaceStorefronts();
            if (UIManager.getIsBannerEnabled())
                callbackForBanners();
        } else if (v.getId() == R.id.rlHeaderTextOption) {
            Dependencies.setIsPreorderSelecetedForMenu(false);
            Bundle bundle = new Bundle();
            bundle.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) mActivity).latitude);
            bundle.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) mActivity).longitude);
            bundle.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);

            Transition.transitForResult(mActivity, MarketplaceSearchActivity.class, Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY, bundle, false);

        }


        if (v.getId() == R.id.ivMap) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("cityStorefrontsModel", cityStorefrontsModel);
//            Intent mapIntent = new Intent(mActivity, MapActivity.class);
//            mapIntent.putExtras(bundle);
//            startActivityForResult(mapIntent, OPEN_MAP_SCREEN);
        }
        if (v.getId() == R.id.rlmap) {

            if (mActivity != null && mActivity instanceof RestaurantListingActivity)
                ((RestaurantListingActivity) mActivity).setMapFragment(bundle);
        }
        if (v.getId() == R.id.rlCustomOrder) {
            customCheckout = true;
            Bundle extraa = new Bundle();
            extraa.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            extraa.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) mActivity).latitude);
            extraa.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) mActivity).longitude);
            extraa.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);
            extraa.putBoolean("isCustomOrder", true);
            Transition.openCustomCheckoutActivity(mActivity, extraa);
        }

    }

    public void viewCart(com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData) {
        if (storefrontData == null) {
            return;
        }


        double minAmountPrice = storefrontData.getMerchantMinimumOrder();
        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(storefrontData.getMerchantMinimumOrder());


        for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
            Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(storefrontData);
        }

        if (minAmountPrice > Dependencies.getProductListSubtotal()) {
            StorefrontConfig.getAppCatalogueV2(mActivity, storefrontData.getStoreName(),
                    storefrontData.getLogo(), new LatLng(Double.valueOf(storefrontData.getLatitude()),
                            Double.valueOf(storefrontData.getLongitude())), new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude),
                    ((RestaurantListingActivity) mActivity).address, storefrontData, "",
//                    ((RestaurantListingActivity) mActivity).isAllBusinessCategory ? null : ((RestaurantListingActivity) mActivity).businessCategoryId
                    null, false, 0);
            return;
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
            extraa.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
            extraa.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) mActivity).latitude);
            extraa.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) mActivity).longitude);
            extraa.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);
            extraa.putSerializable(STOREFRONT_DATA, storefrontData);

            Transition.openCheckoutActivity(mActivity, extraa);
        }
    }

//    public void gotoFavLocationActivity() {
//        Bundle extras = new Bundle();
//        Intent intent = new Intent(mActivity, FavLocationActivity.class);
//        intent.putExtras(extras);
//        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
//    }
//
//    public void gotoMapActivity() {
////      Utils.searchPlace(mActivity, PlaceAutocomplete.MODE_FULLSCREEN, getCurrentLocation());
//        Bundle extras = new Bundle();
//        Intent intent = new Intent(mActivity, AddFromMapActivity.class);
//        intent.putExtras(extras);
//        startActivityForResult(intent, Codes.Request.OPEN_LOCATION_ACTIVITY);
//    }


    private void callbackForBusinessCategories() {
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder builder = new CommonParams.Builder()
                .add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add(BUSINESS_API_VERSION, "2");

        builder.add(LATITUDE, ((RestaurantListingActivity) mActivity).latitude != null ? ((RestaurantListingActivity) mActivity).latitude : 0)
                .add(LONGITUDE, ((RestaurantListingActivity) mActivity).longitude != null ? ((RestaurantListingActivity) mActivity).longitude : 0);
        if (userData != null && userData.getData() != null && userData.getData().getVendorDetails() != null) {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                builder.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
            }
//            commonParams.add(USER_ID, StorefrontCommonData.getFormSettings().getUserId());
        }

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            builder.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }

        RestClient.getApiInterface(mActivity).getBusinessCategories(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Data businessCategoriesData = baseModel.toResponseModel(Data.class);

                        ArrayList<Result> results = new ArrayList<>();
                        results.addAll(businessCategoriesData.getResult());

                        for (int i = 0; i < businessCategoriesData.getResult().size(); i++) {
                            if (businessCategoriesData.getResult().get(i).getIs_all_category() == 1 && (((RestaurantListingActivity) mActivity).businessCategoryId == null || ((RestaurantListingActivity) mActivity).businessCategoryId == 0)) {
                                ((RestaurantListingActivity) mActivity).businessCategoryId = businessCategoriesData.getResult().get(i).getId();
                                ((RestaurantListingActivity) mActivity).isAllBusinessCategory = true;
                            }
                            if (!UIManager.getCustomOrderActive()) {
                                if (businessCategoriesData.getResult().get(i).getIsCustomOrderActive() == 1) {
                                    for (int j = 0; j < results.size(); j++) {
                                        if (businessCategoriesData.getResult().get(i).getId() == results.get(j).getId()) {
                                            results.remove(j);
                                        }
                                    }

                                }
                            }
                        }


                        BusinessCategoriesAdapter businessCategoriesAdapter =
                                new BusinessCategoriesAdapter(mActivity, results, ((RestaurantListingActivity) mActivity).businessCategoryId,
                                        new BusinessCategoriesAdapter.BusinessCategoryListener() {
                                            @Override
                                            public void onBusinessCategorySelected(int pos, Result businessCategory, boolean isAllBusiness) {
                                                if (businessCategory != null && businessCategory.getIsCustomOrderActive() == 1) {
                                                    rlCustomOrder.performClick();
                                                } else {
                                                    ((RestaurantListingActivity) mActivity).isAllBusinessCategory = isAllBusiness;

                                                    if (businessCategory != null)
                                                        ((RestaurantListingActivity) mActivity).businessCategoryId = businessCategory.getId();
                                                    else {
                                                        ((RestaurantListingActivity) mActivity).businessCategoryId = 0;
                                                    }

                                                    if (((RestaurantListingActivity) mActivity).categoryFilterList != null &&
                                                            ((RestaurantListingActivity) mActivity).businessCategoryId != ((RestaurantListingActivity) mActivity).categoryFilterList.get(0).getBusinessCategoryId())
                                                        ((RestaurantListingActivity) mActivity).categoryFilterList = null;

                                                    getMarketplaceStorefronts();
                                                }
                                            }
                                        });
                        if (businessCategoriesData.getResult().size() > 0) {
                            vAdminCategoriesLabel.setVisibility(View.VISIBLE);
                            rvAdminCategory.setVisibility(View.VISIBLE);
                        } else {
                            vAdminCategoriesLabel.setVisibility(View.GONE);
                            rvAdminCategory.setVisibility(View.GONE);
                        }
                        pbCategory.setVisibility(View.GONE);
                        rvAdminCategory.setAdapter(businessCategoriesAdapter);
                        LinearLayoutManager lm = new LinearLayoutManager(mActivity,
                                LinearLayoutManager.HORIZONTAL, false);
                        lm.setStackFromEnd(true);
                        rvAdminCategory.setLayoutManager(lm);
                        rvAdminCategory.scrollToPosition(0);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if (results.size() > 0) {
                            rvAdminCategory.setVisibility(View.VISIBLE);
                            if (results.size() == 1 && results.get(0).getIs_all_category() == 1) {
                                rvAdminCategory.setVisibility(View.GONE);
                            }
                        } else {
                            rvAdminCategory.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        vAdminCategoriesLabel.setVisibility(View.GONE);
                        rvAdminCategory.setVisibility(View.GONE);
                        pbCategory.setVisibility(View.GONE);
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

    }

    private void callbackForBanners() {
        if (timer != null) timer.cancel();
        UserData userData = StorefrontCommonData.getUserData();

        CommonParams.Builder commonParams = new CommonParams.Builder()
                .add(LATITUDE, ((RestaurantListingActivity) mActivity).latitude != null ? ((RestaurantListingActivity) mActivity).latitude : 0)
                .add(LONGITUDE, ((RestaurantListingActivity) mActivity).longitude != null ? ((RestaurantListingActivity) mActivity).longitude : 0);

        /*if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }*/
        if (Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessTokenGuest(mActivity));
            commonParams.add(VENDOR_ID, Dependencies.getVendorIdForGuest(mActivity));

        } else {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
            }
        }

        if (userData != null && userData.getData() != null && userData.getData().getVendorDetails() != null) {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                commonParams.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
            }
//            commonParams.add(USER_ID, StorefrontCommonData.getFormSettings().getUserId());
            commonParams.add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());
        }

        if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY)
            commonParams.add("delivery_method", 0);
        else if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP)
            commonParams.add("delivery_method", 2);
        else
            commonParams.add("delivery_method", 1);

        RestClient.getApiInterface(mActivity).getBanners(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        BannersData bannersData = baseModel.toResponseModel(BannersData.class);
                        if (bannersData.getBanner().size() > 0) {
                            ArrayList<String> bannerList = new ArrayList<>();
                            for (Banner banner : bannersData.getBanner()) {
                                bannerList.add(banner.getImage());
                            }

                       /*     ArrayList<String> imagelll = new ArrayList<>();
                            for (int i = 0; i < bannerList.size(); i++) {
                                if (i / 2 == 0) {
                                    imagelll.add("https://3.bp.blogspot.com/-yCrKs2be1EQ/XDtApw5LXNI/AAAAAAAAAbc/JceMPjCyfVMdWMnqNRhRW8tPRgpWgHpeQCLcBGAs/s1600/social-media-banner-design.jpg");
                                } else {
                                    imagelll.add("https://image.freepik.com/free-vector/fast-food-banners-design_1284-1143.jpg");
                                }

                            }//bannerList*/

                            bannerAdapter = new BannerAdapter((RestaurantListingActivity) mActivity, RestaurantListFragment.this,
                                    bannerList, bannersData);
                            pagerBanner.setAdapter(bannerAdapter);
                            rlBanner.setVisibility(View.VISIBLE);
                            pbBanner.setVisibility(View.GONE);
                            inflateIndicators(0, llIndicators);
                        } else {
                            rlBanner.setVisibility(View.GONE);
                            pbBanner.setVisibility(View.GONE);
                        }
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if (bannersData.getBanner().size() > 1) {
                            /*After setting the adapter use the timer */
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    pagerBanner.setCurrentItem(pagerBanner.getCurrentItem() == bannerAdapter.getImagesList().size() - 1 ? 0 : pagerBanner.getCurrentItem() + 1, true);
                                }
                            };
                            if (timer != null) timer.cancel();
                            timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);
                        }
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        Log.e("on activity result", "onActivityResult   restaurent list frag");
        switch (requestCode) {
            case Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT:
                if (resultCode == RESULT_OK) {
                    if (customCheckout) {
                        Transition.openCustomCheckoutActivity(mActivity, data.getExtras());
                        customCheckout = false;
                    } else
                        Transition.openCheckoutActivity(mActivity, data.getExtras());
                }
                break;

            case Codes.Request.OPEN_LOCATION_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    if (data.hasExtra("label") && !data.getStringExtra("label").isEmpty())
                        ((RestaurantListingActivity) mActivity).tvHeading.setText(data.getStringExtra("label"));
                    else
                        ((RestaurantListingActivity) mActivity).tvHeading.setText(data.getStringExtra("address"));
                    ((RestaurantListingActivity) mActivity).address = data.getStringExtra("address");
                    ((RestaurantListingActivity) mActivity).latitude = data.getDoubleExtra("latitude", 0.0);
                    ((RestaurantListingActivity) mActivity).longitude = data.getDoubleExtra("longitude", 0.0);
                    getMarketplaceStorefronts();
                    if (UIManager.getIsBannerEnabled())
                        callbackForBanners();
                    if (UIManager.getIsBusinessCategoryEnabled())
                        callbackForBusinessCategories();

                }
                break;

            case Codes.Request.OPEN_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    StorefrontCommonData.setUserData((UserData) data.getExtras().getSerializable(UserData.class.getName()));
                    boolean isLanguageChanged;
                    isLanguageChanged = data.getExtras().getBoolean("isLanguageChanged");
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                    if (isLanguageChanged)
                        ((RestaurantListingActivity) mActivity).restartActivity();
                }
                break;

            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == RESULT_OK) {
                    ((RestaurantListingActivity) mActivity).startLocationFetcher();
                }
                break;
            case Codes.Request.OPEN_SIGN_UP_FROM_DEMO_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Dependencies.setDemoRun(false);
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                } else {
                    Dependencies.setDemoRun(true);
                }
                break;
            case Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN:
            case Codes.Request.OPEN_HOME_ACTIVITY:
                if (resultCode == RESULT_OK) {
                   // if (data.getBooleanExtra(IS_WISHLIST_CHANGED, false)) {
                        getMarketplaceStorefronts();
                  //  }
                   /* if (data.getExtras() != null && data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }


                    if (data.getExtras() != null && (data.hasExtra(STOREFRONT_DATA) && data.hasExtra(STOREFRONT_DATA_ITEM_POS))) {

                        com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) data.getExtras().getSerializable(STOREFRONT_DATA);
                        int storefrontDataItemPos = data.getExtras().getInt(STOREFRONT_DATA_ITEM_POS);

                        try {
                            if (storefrontDataItemPos >= 0 && cityStorefrontsModel.getData().size() > storefrontDataItemPos)
                                cityStorefrontsModel.getData().set(storefrontDataItemPos, storefrontData);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                        }
                    }*/
                }
                break;

            case Codes.Request.OPEN_SEARCH_PRODUCT_ACTIVITY:
            case Codes.Request.OPEN_CHECKOUT_SCREEN:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                }
                break;

            case Codes.Request.OPEN_CUSTOM_CHECKOUT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }
                    if (data.getExtras() != null && (data.hasExtra(STOREFRONT_DATA) && data.hasExtra(STOREFRONT_DATA_ITEM_POS))) {

                        com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) data.getExtras().getSerializable(STOREFRONT_DATA);
                        int storefrontDataItemPos = data.getExtras().getInt(STOREFRONT_DATA_ITEM_POS);

                        try {
                            cityStorefrontsModel.getData().set(storefrontDataItemPos, storefrontData);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                break;

            case FilterConstants.REQUEST_CODE_TO_OPEN_FILTER:
                if (resultCode == RESULT_OK) {
                    ((RestaurantListingActivity) mActivity).allFilterList = data.getParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ALL);
                    ((RestaurantListingActivity) mActivity).categoryFilterList = data.getParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY);
                    getMarketplaceStorefronts();
                }
                break;


        }
    }


    private void redirectToMerchant(com.tookancustomer.models.MarketplaceStorefrontModel.Datum
                                            merchantData, int position) {
//        if (merchantData.getIsStorefrontOpened() == 0 && merchantData.getScheduledTask() == 0) {
//                    Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.store_currently_closed));
//                    return;
//                }

        if (merchantData == null) {
            return;
        }
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.RESTAURANT_CLICK, merchantData.getStoreName() + "");
        StorefrontCommonData.getUserData().getData().getVendorDetails().setUserId(merchantData.getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setUserId(merchantData.getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(merchantData.getMerchantMinimumOrder());

        if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(STOREFRONT_DATA, merchantData);
            bundle.putInt(STOREFRONT_DATA_ITEM_POS, position);

            bundle.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) mActivity).latitude);
            bundle.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) mActivity).longitude);
            bundle.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);
            bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, "");

            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, OPEN_HOME_ACTIVITY);
            AnimationUtils.forwardTransition(mActivity);
        } else {
            Double latitude = 0.0, longitude = 0.0;
            String adminSelectedCategories = "", address = "";

            address = ((RestaurantListingActivity) mActivity).address;
            latitude = ((RestaurantListingActivity) mActivity).latitude;
            longitude = ((RestaurantListingActivity) mActivity).longitude;


            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                if (merchantData.getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                    for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                        Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(merchantData);
                    }
                }
            }
            StorefrontConfig.getAppCatalogueV2(mActivity,
                    merchantData.getStoreName(),
                    merchantData.getLogo(),
                    new LatLng(Double.valueOf(merchantData.getLatitude()),
                            Double.valueOf(merchantData.getLongitude())),
                    new LatLng(latitude, longitude), address,
                    merchantData,
                    adminSelectedCategories,
//                    ((RestaurantListingActivity) mActivity).isAllBusinessCategory ? null : ((RestaurantListingActivity) mActivity).businessCategoryId
                    null, false, 0);
        }
    }


    private void getSingleMerchantDetails(int merchantId,
                                          final MerchantCallback merchantCallback) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        if (mActivity instanceof RestaurantListingActivity) {
            commonParams.add(Keys.APIFieldKeys.LATITUDE, ((RestaurantListingActivity) mActivity).latitude != null ? ((RestaurantListingActivity) mActivity).latitude : 0)
                    .add(Keys.APIFieldKeys.LONGITUDE, ((RestaurantListingActivity) mActivity).longitude != null ? ((RestaurantListingActivity) mActivity).longitude : 0);
        }
        commonParams.add(USER_ID, merchantId);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(mActivity).getSingleMarketplaceStorefronts(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                merchantCallback.onFetchSingleMerchantData(cityStorefrontsModels.getData().get(0));
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    @Override
    public void redirectToMerchant(Integer merchantId, int position, Banner banner) {
        int merchantPosition = -1;


        if (banner.getExternalLinkToggle() == 1 && banner.getExternalLink() != null && !banner.getExternalLink().isEmpty()) {

            Intent intentExternalLink = new Intent(mActivity, WebViewActivity.class);
            intentExternalLink.putExtra(URL_WEBVIEW, banner.getExternalLink());
            intentExternalLink.putExtra(HEADER_WEBVIEW, banner.getName());
            startActivity(intentExternalLink);
            AnimationUtils.forwardTransition(mActivity);

        } else if (cityStorefrontsModel != null) {
            if (merchantId > 0) {
                for (int i = 0; i < cityStorefrontsModel.getData().size(); i++) {
                    if (cityStorefrontsModel.getData().get(i).getStorefrontUserId().intValue() == merchantId.intValue()) {
                        merchantPosition = i;
                        break;
                    }
                }

                if (merchantPosition != -1) {
                    redirectToMerchant(cityStorefrontsModel.getData().get(merchantPosition), merchantPosition);
                } else {
                    getSingleMerchantDetails(merchantId, new MerchantCallback() {
                        @Override
                        public void onFetchSingleMerchantData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData) {
                            if (merchantData.getCanServe() == 1) {
                                redirectToMerchant(merchantData, -1);
                            } else {
                                Utils.snackBar(mActivity, StorefrontCommonData.getTerminology().getStore() + " " + getString(R.string.unable_deliver_text));
                            }
                        }
                    });

//                Utils.snackbar(mActivity, StorefrontCommonData.getTerminology().getStore() + " " + getString(R.string.unable_deliver_text), rlFloating);
                }
            }
        } else
            Utils.snackBar(mActivity, StorefrontCommonData.getTerminology().getStore() + " " + getString(R.string.unable_deliver_text));

    }

    private void getOnGoingOrders(UserData userData) {


        CommonParams.Builder builder = new CommonParams.Builder()
                .add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId())
                .add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId())
                .add(ACCESS_TOKEN, userData.getData().getVendorDetails().getAppAccessToken());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            builder.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }


        RestClient.getApiInterface(mActivity).getOnGoingOrders(builder.build().getMap()).
                enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        try {


                            onGoingOrderData = new ArrayList<OnGoingOrderData>();
                            onGoingOrderData = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                    new TypeToken<ArrayList<OnGoingOrderData>>() {
                                    }.getType());

                            if (onGoingOrderData.size() > 0) {
                                ongoingorderRL.setVisibility(View.VISIBLE);
                                ongoingTaskAdapter = new OngoingTaskAdapter(mActivity, onGoingOrderData);
                                ongoingOrderPager.setAdapter(ongoingTaskAdapter);
                                inflateIndicatorsOngoingTask(0, onGoingTaskIndicatorLL);
                            } else {
                                ongoingorderRL.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            ongoingorderRL.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }

                });
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


    public interface MerchantCallback {
        void onFetchSingleMerchantData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData);
    }


}
