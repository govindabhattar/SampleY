package com.tookancustomer.restorentListFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import com.tookancustomer.HomeActivity;

import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.adapters.BusinessCategoriesMapAdapter;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.filter.constants.FilterConstants;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.CityStorefrontsModel;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.models.appConfiguration.MapObject;
import com.tookancustomer.models.businessCategoriesData.Data;
import com.tookancustomer.models.businessCategoriesData.Result;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.tookancustomer.appdata.Codes.Request.OPEN_HOME_ACTIVITY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.BUSINESS_API_VERSION;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.CITY_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.CITY_NAME;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FIRST_RADIUS;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.LATITUDE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MAP_VIEW_FLAG;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.SEARCH_TEXT;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.SECOND_RADIUS;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE_SELECTED_CATEGORIES;
import static com.tookancustomer.appdata.Keys.Extras.BUSINESS_CATEGORY_ID;
import static com.tookancustomer.appdata.Keys.Extras.FAILURE_MESSAGE;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA_ITEM_POS;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_MODEL;
import static com.tookancustomer.appdata.Keys.Extras.SUCCESS_MESSAGE;
import static com.tookancustomer.appdata.Keys.Prefs.LONGITUDE;
import static com.tookancustomer.appdata.TerminologyStrings.ORDER;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListMapFragmnt extends Fragment implements OnMapReadyCallback, com.mapbox.mapboxsdk.maps.OnMapReadyCallback, View.OnClickListener, MapboxMap.OnCameraIdleListener {


    private MapView mMapView;
    private com.mapbox.mapboxsdk.maps.MapView mFlightMapView;
    private GoogleMap googleMap;
    private MapboxMap flightMap;

    Bundle bundle = new Bundle();
    private boolean restaurantIsGuest;
    private ArrayList<com.tookancustomer.models.NLevelWorkFlowModel.Datum> adminCatalogueList = new ArrayList<>();
    private CityStorefrontsModel cityStorefrontsModel;
    private boolean isFirstScreen = true;
    public int parentId = 0;
    Activity mActivity;
    private ImageView deliveryTypeIV;
    private LinearLayout pickupDeliveryLL;
    private TextView tvHomeDelivery, tvSelfPickup, selectedCategoryTV, tvPuckupAndDrop;

    ImageView rllist;
    private LinearLayout llSelfPickupDelivery;
    private LinearLayout deliveryTypeLL, businessCategoryLL;
    private RecyclerView rvAdminCategory;

    boolean customCheckout = false;
    private boolean isLanguageChanged = false;
    private HashMap<Marker, com.tookancustomer.models.MarketplaceStorefrontModel.Datum> mDataMap = new HashMap<>();
    private HashMap<com.mapbox.mapboxsdk.annotations.Marker, com.tookancustomer.models.MarketplaceStorefrontModel.Datum> mDataFlightMap = new HashMap<>();
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum selectedMerchent = null;
    LinearLayout merchantDetailRL;
    private Marker lastClicked = null;
    private com.mapbox.mapboxsdk.annotations.Marker lastClickedFlightmap = null;

    private ImageView ivSponsoredRibbon, ivImage, ivAverageRatingStar;
    private TextView tvRestaurantName, tvDeliveryTime, tvRestaurantOpenStatus, tvRestaurantCategories, tvAverageRatingImage, tvRestaurantAddress, tvMerchantDiscount;
    private LinearLayout llRatingImageLayout;
    private View seperaterView;
    private ImageView locationIV;
    private List<com.tookancustomer.models.MarketplaceStorefrontModel.Datum> merchantsArrayList = new ArrayList<>();
    private float previousZoomLevel = 20;
    private double previousRadii = 0.0;
    private boolean isCurrentLoc = false;
    private LatLng prevLatLong = null;
    MapObject mapObject;
    boolean isflightmap = false;
    private ImageView ivHomedelivery, ivSelfPickup, ivPickAndDrop;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (RestaurantListingActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mapObject.getMapType() == Constants.MapConstants.GOOGLEMAP) {
            return inflater.inflate(R.layout.fragment_restaurant_list_map_fragmnt, container, false);
        } else {
            Mapbox.getInstance(mActivity, mapObject.getAndroidMapApiKey());
            return inflater.inflate(R.layout.fragment_restaurant_list_flightmap_fragmnt, container, false);
        }


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
        if (bundle != null) {
            if (bundle.getSerializable(ADMIN_CATALOGUE) != null) {
                adminCatalogueList = (ArrayList<Datum>) bundle.getSerializable(ADMIN_CATALOGUE);
            }
            if (bundle.getSerializable(STOREFRONT_MODEL) != null) {
                cityStorefrontsModel = (CityStorefrontsModel) bundle.getSerializable("storefrontModel");
                merchantsArrayList = cityStorefrontsModel.getData();
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

        initUI(view);
        setMap(view, savedInstanceState);
        setViews();
        setAddressLayout();

    }

    private void getData() {


        if (UIManager.getIsBusinessCategoryEnabled())
            callbackForBusinessCategories();

    }

    private void setMerchantsOnMap(boolean initial) {

        mDataMap.clear();
        lastClicked = null;
        if (googleMap != null) {
            googleMap.clear();
            Marker m;
            for (com.tookancustomer.models.MarketplaceStorefrontModel.Datum object : merchantsArrayList) {
                m = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(object.getLatitude()), Double.parseDouble(object.getLongitude())))
                        .title(object.getStoreName())
                        .icon(bitmapDescriptorFromVector(mActivity, R.color.transparent, true, true)));

                mDataMap.put(m, object);
            }


            Log.e("no fo stores", "no fo stores>>" + mDataMap.size());
        }
        if (flightMap != null) {
            flightMap.clear();
            com.mapbox.mapboxsdk.annotations.Marker m;
            for (com.tookancustomer.models.MarketplaceStorefrontModel.Datum object : merchantsArrayList) {

                m = flightMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions()
                        .position(new com.mapbox.mapboxsdk.geometry.LatLng(Double.parseDouble(object.getLatitude()), Double.parseDouble(object.getLongitude())))
                        .title(object.getStoreName())
                        .icon(setcustommapboxmarkericon(mActivity, R.color.transparent, true, true)));

//                        .icon(IconFactory.getInstance(mActivity).fromResource(R.drawable.marker_merchent_store)));
//                        .icon(IconFactory.getInstance(mActivity).fromResource(R.drawable.marker_merchent_store)));


                mDataFlightMap.put(m, object);
            }


            Log.e("no fo stores", "no fo stores>>" + mDataMap.size());
        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId, boolean isDefault, boolean isStore) {
        Drawable background;
        if (isStore) {
            if (isDefault)
                background = ContextCompat.getDrawable(context, R.drawable.marker_merchent_store);
            else
                background = ContextCompat.getDrawable(context, R.drawable.merchent_selected_marker);
        } else {
            background = ContextCompat.getDrawable(context, R.drawable.ic_icon_pin_location);
        }
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Icon setcustommapboxmarkericon(Context context, @DrawableRes int vectorDrawableResourceId, boolean isDefault, boolean isStore) {
        Drawable background;
        if (isStore) {
            if (isDefault)
                background = ContextCompat.getDrawable(context, R.drawable.marker_merchent_store);
            else
                background = ContextCompat.getDrawable(context, R.drawable.merchent_selected_marker);
        } else {
            background = ContextCompat.getDrawable(context, R.drawable.ic_icon_pin_location);
        }
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), vectorDrawableResourceId, context.getTheme());
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        DrawableCompat.setTint(vectorDrawable, colorRes);
        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    private void setViews() {
        if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
            if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.NONE)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            llSelfPickupDelivery.setVisibility(View.VISIBLE);
            resetSelfPickupDeliveryAssets();
        } else {
            if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
            else if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)
                ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            llSelfPickupDelivery.setVisibility(View.GONE);
        }

        businessCategoryLL.setVisibility(UIManager.getIsBusinessCategoryEnabled() ? View.VISIBLE : View.GONE);

    }

//    public void resetSelfPickupDeliveryAssets() {
//        if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
//            Utils.setTextColor(mActivity, R.color.colorAccent, tvSelfPickup);
//            tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_active, 0, 0, 0);
//            tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_active, 0, 0, 0);
//            deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_active));
//            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
//            tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
//        } else if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
//            Utils.setTextColor(mActivity, R.color.colorAccent, tvHomeDelivery);
//            tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_active, 0, 0, 0);
//            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
//            tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
//            deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_active));
//
//        }
//        else {
//            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
//            tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
//            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
//            tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
//            deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
//
//        }
//
//    }

    public void resetSelfPickupDeliveryAssets() {
        if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvSelfPickup);
            //   tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_active, 0, 0, 0);
            // tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_active, 0, 0, 0);
            // deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_active));
            setDeliveryMethodIcon(Constants.SelectedPickupMode.SELF_PICKUP);
            setDeliveryTypeIcon(Constants.SelectedPickupMode.SELF_PICKUP);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPuckupAndDrop);
            //   tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
            //  tvPuckupAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_unselected, 0, 0, 0);

        } else if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY) {

            Utils.setTextColor(mActivity, R.color.colorAccent, tvHomeDelivery);
            //  tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_active, 0, 0, 0);
            //   tvPuckupAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_unselected, 0, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPuckupAndDrop);
            // tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
            //deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_active));

            setDeliveryMethodIcon(Constants.SelectedPickupMode.HOME_DELIVERY);
            setDeliveryTypeIcon(Constants.SelectedPickupMode.HOME_DELIVERY);

        } else if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            Utils.setTextColor(mActivity, R.color.colorAccent, tvPuckupAndDrop);

            setDeliveryMethodIcon(Constants.SelectedPickupMode.PICK_AND_DROP);
            // tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
            //  tvPuckupAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_selected, 0, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            // tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
            //   deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_selected));
            setDeliveryTypeIcon(Constants.SelectedPickupMode.PICK_AND_DROP);
        } else {
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvHomeDelivery);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvPuckupAndDrop);
            tvHomeDelivery.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_home_delivery_inactive, 0, 0, 0);
            tvPuckupAndDrop.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pick_drop_unselected, 0, 0, 0);
            Utils.setTextColor(mActivity, R.color.primary_text_color, tvSelfPickup);
            tvSelfPickup.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pickup_inactive, 0, 0, 0);
            deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));

        }

        Dependencies.setSelectedDelivery(((RestaurantListingActivity) mActivity).selectedPickupMode);


    }


    private void setDeliveryMethodIcon(int deliveryMode) {

        if (deliveryMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_active)
                        .setFallback(R.drawable.ic_pickup_active)
                        .build();
            }else{
                ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_active));
            }
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")){
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
            }else{
                ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            }
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
            }else{
                ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            }

        }else if(deliveryMode == Constants.SelectedPickupMode.PICK_AND_DROP){

            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_selected)
                        .setFallback(R.drawable.ic_pick_drop_selected)
                        .build();
            }else{
                ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_selected));
            }

            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_inactive)
                        .setFallback(R.drawable.ic_pickup_inactive)
                        .build();
            }else{
                ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_inactive));
            }
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")){
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_inactive)
                        .setFallback(R.drawable.ic_home_delivery_inactive)
                        .build();
            }else{
                ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
            }



        }else if(deliveryMode == Constants.SelectedPickupMode.HOME_DELIVERY){
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")){
                new GlideUtil.GlideUtilBuilder(ivHomedelivery)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_active)
                        .setFallback(R.drawable.ic_home_delivery_active)
                        .build();
            }else{
                ivHomedelivery.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_home_delivery_active));
            }

            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null
                    && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivPickAndDrop)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_unselected)
                        .setFallback(R.drawable.ic_pick_drop_unselected)
                        .build();
            }else{
                ivPickAndDrop.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pick_drop_unselected));
            }

            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(ivSelfPickup)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pickup_inactive)
                        .setFallback(R.drawable.ic_pickup_inactive)
                        .build();
            }else{
                ivSelfPickup.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pickup_inactive));
            }
        }
    }


    private void setDeliveryTypeIcon(int deliveryMode) {

        if (deliveryMode == Constants.SelectedPickupMode.HOME_DELIVERY) {
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image().equalsIgnoreCase("")) {
                new GlideUtil.GlideUtilBuilder(deliveryTypeIV)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_home_delivery_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_home_delivery_active)
                        .setFallback(R.drawable.ic_home_delivery_active)
                        .build();
            }
            else{
                deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_active));
            }

        } else if (deliveryMode == Constants.SelectedPickupMode.PICK_AND_DROP) {
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image().equalsIgnoreCase("")){
                new GlideUtil.GlideUtilBuilder(deliveryTypeIV)
                        .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_pick_and_drop_image())
                        .setFitCenter(true)
                        .setError(R.drawable.ic_pick_drop_selected)
                        .setFallback(R.drawable.ic_pick_drop_selected)
                        .build();
            }else{
                deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pick_drop_selected));
            }

        } else if (deliveryMode == Constants.SelectedPickupMode.SELF_PICKUP) {
            if(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image() != null && !StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image().equalsIgnoreCase("")){
                new GlideUtil.GlideUtilBuilder(deliveryTypeIV)
                    .setLoadItem(StorefrontCommonData.getAppConfigurationData().getMobile_icon_self_pickup_image())
                    .setFitCenter(true)
                    .setError(R.drawable.ic_pickup_active)
                    .setFallback(R.drawable.ic_pickup_active)
                    .build();}
            else{
                deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_pickup_active));
            }

        } else {
            deliveryTypeIV.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_delivery_inactive));
        }
    }


    private void initUI(View view) {
        rllist = view.findViewById(R.id.rllist);
        deliveryTypeIV = view.findViewById(R.id.deliveryTypeIV);
        tvDeliveryTime = view.findViewById(R.id.tvDeliveryTime);
        pickupDeliveryLL = view.findViewById(R.id.pickupDeliveryLL);
        llSelfPickupDelivery = view.findViewById(R.id.llSelfPickupDelivery);
        deliveryTypeLL = view.findViewById(R.id.deliveryTypeLL);
        businessCategoryLL = view.findViewById(R.id.businessCategoryLL);
        rvAdminCategory = view.findViewById(R.id.rvAdminCategory);
        selectedCategoryTV = view.findViewById(R.id.selectedCategoryTV);
        merchantDetailRL = view.findViewById(R.id.merchantDetailRL);
        tvMerchantDiscount = view.findViewById(R.id.tvMerchantDiscount);
        seperaterView = view.findViewById(R.id.seperaterView);
        locationIV = view.findViewById(R.id.locationIV);
        rvAdminCategory.setItemAnimator(new DefaultItemAnimator());
        rvAdminCategory.setHasFixedSize(false);
        rvAdminCategory.setVisibility(View.GONE);
        view.findViewById(R.id.seperaterView).setVisibility(View.GONE);

        tvHomeDelivery = view.findViewById(R.id.tvHomeDelivery);
        tvHomeDelivery.setText(StorefrontCommonData.getTerminology().getHomeDelivery());
        tvSelfPickup = view.findViewById(R.id.tvSelfPickup);
        tvSelfPickup.setText(StorefrontCommonData.getTerminology().getSelfPickup());

        ivHomedelivery = view.findViewById(R.id.ivHomedelivery);
        ivSelfPickup = view.findViewById(R.id.ivSelfPickup);
        ivPickAndDrop = view.findViewById(R.id.ivPickAndDrop);
        tvPuckupAndDrop = view.findViewById(R.id.tvPuckupAndDrop);
        tvHomeDelivery.setText(StorefrontCommonData.getTerminology().getHomeDelivery());


        ivSponsoredRibbon = view.findViewById(R.id.ivSponsoredRibbon);
        ivImage = view.findViewById(R.id.ivImage);
        tvRestaurantName = view.findViewById(R.id.tvRestaurantName);
        tvRestaurantOpenStatus = view.findViewById(R.id.tvRestaurantOpenStatus);
        tvRestaurantCategories = view.findViewById(R.id.tvRestaurantCategories);
        tvAverageRatingImage = view.findViewById(R.id.tvAverageRatingImage);
        ivAverageRatingStar = view.findViewById(R.id.ivAverageRatingStar);
        llRatingImageLayout = view.findViewById(R.id.llRatingImageLayout);
        tvRestaurantAddress = view.findViewById(R.id.tvRestaurantAddress);


        Utils.setOnClickListener(this, rllist, deliveryTypeLL, tvSelfPickup, tvHomeDelivery, tvPuckupAndDrop, selectedCategoryTV, merchantDetailRL, locationIV, ivHomedelivery, ivSelfPickup, ivPickAndDrop);
    }


    public void getMarketplaceStorefronts(boolean initial, double previousRadii, double currentradii) {
        merchantDetailRL.setVisibility(View.GONE);
        rvAdminCategory.setVisibility(View.GONE);
        deliveryTypeLL.setVisibility(View.VISIBLE);
        pickupDeliveryLL.setVisibility(View.GONE);
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add(CITY_ID, "")
                .add(CITY_NAME, "")
                .add(LATITUDE, ((RestaurantListingActivity) mActivity).latitude != null ? ((RestaurantListingActivity) mActivity).latitude : 0)
                .add(LONGITUDE, ((RestaurantListingActivity) mActivity).longitude != null ? ((RestaurantListingActivity) mActivity).longitude : 0)
                .add(SEARCH_TEXT, "")
                .add(MAP_VIEW_FLAG, "1");

        if (commonParams.build().getMap().containsKey(USER_ID))
            commonParams.build().getMap().remove(USER_ID);

        if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1) {
            commonParams.add("home_delivery", ((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY ? 1 : 0);
            commonParams.add("self_pickup", ((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP ? 1 : 0);
        }
        if (!initial) {
            commonParams.add(FIRST_RADIUS, previousRadii);
            commonParams.add(SECOND_RADIUS, currentradii);
            prevLatLong = null;
        } else {
            commonParams.add(FIRST_RADIUS, currentradii);
        }


        if (StorefrontCommonData.getUserData().getData().getAdminCategoryEnabled() == 1) {

            RestClient.getApiInterface(mActivity).getAdminMerchantList(commonParams.build().getMap())
                    .enqueue(getMerchantListResponseResolver(initial));
        } else {
            if (((RestaurantListingActivity) mActivity).businessCategoryId != null && !((RestaurantListingActivity) mActivity).isAllBusinessCategory)
                commonParams.add(BUSINESS_CATEGORY_ID, ((RestaurantListingActivity) mActivity).businessCategoryId);

            JSONObject filterObject = null;
            if (((RestaurantListingActivity) mActivity).isAnyFilterApplied())
                filterObject = ((RestaurantListingActivity) mActivity).generateFilterJsonObject();

            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
                commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
            }


            RestClient.getApiInterface(mActivity).getMarketplaceStorefronts(commonParams.build().getMap(), filterObject)
                    .enqueue(getMerchantListResponseResolver(initial));
        }
    }


    public ResponseResolver<BaseModel> getMerchantListResponseResolver(final boolean initial) {
        return new ResponseResolver<BaseModel>(mActivity, true, true) {
            @Override
            public void success(BaseModel baseModel) {

                CityStorefrontsModel cityStorefrontsModels = new CityStorefrontsModel();
                try {
                    com.tookancustomer.models.MarketplaceStorefrontModel.Datum[] datum = baseModel.toResponseModel(com.tookancustomer.models.MarketplaceStorefrontModel.Datum[].class);
                    cityStorefrontsModels.setData(new ArrayList<com.tookancustomer.models.MarketplaceStorefrontModel.Datum>(Arrays.asList(datum)));
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
//                UIManager.setTotalStores(cityStorefrontsModel.getData().size());

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
                        }
                    }
                }

                if (initial) {
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


                if (merchantsArrayList.size() == 0) {
                    ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
                } else {
                    ((RestaurantListingActivity) mActivity).ivMap.setVisibility(View.GONE);
                }

                setMerchantsOnMap(initial);
                Log.e("no fo stores", "no fo stores>>>>" + merchantsArrayList.size() + "");
                if (merchantsArrayList != null && merchantsArrayList.size() > 0) {
                    for (int i = 0; i < merchantsArrayList.size(); i++) {
                        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                            if (merchantsArrayList.get(i).getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                                for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                                    Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(merchantsArrayList.get(i));
                                }
                            }
                        }
                        merchantsArrayList.get(i).setSelectedPickupMode(((RestaurantListingActivity) mActivity).selectedPickupMode);

                    }
                }

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        };
    }


    private void setMap(View view, Bundle savedInstanceState) {
        if (mapObject.getMapType() == Constants.MapConstants.GOOGLEMAP) {
            mMapView = view.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            isflightmap = false;
            mMapView.onResume();
            mMapView.getMapAsync(this);
        } else {
            mFlightMapView = view.findViewById(R.id.flightmapView);
            mFlightMapView.onCreate(savedInstanceState);
            isflightmap = true;
//            mFlightMapView.onResume();
            mFlightMapView.getMapAsync(this);
        }


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        if (isflightmap == true) {
            mFlightMapView.onResume();
        } else {
//            mMapView.onResume();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (isflightmap == true) {
            mFlightMapView.onStart();
        } else {
            mMapView.onStart();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isflightmap == true) {
            mFlightMapView.onPause();
        } else {
            mMapView.onPause();
        }
        resetBottomViewAndMarker();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isflightmap == true) {
            mFlightMapView.onDestroy();
        } else {
            mMapView.onDestroy();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (isflightmap == true) {
            mFlightMapView.onLowMemory();
        } else {
            mMapView.onLowMemory();
        }
    }

    public Integer getBusinessCategory() {
        return ((RestaurantListingActivity) mActivity).businessCategoryId;
    }

    public boolean isAllBusinessCategory() {
        return ((RestaurantListingActivity) mActivity).isAllBusinessCategory;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        googleMap.setMyLocationEnabled(false);
        googleMap.setMinZoomPreference(8.0f);
        googleMap.setMaxZoomPreference(15.0f);
        isflightmap = false;
        movemap(true);


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                rvAdminCategory.setVisibility(View.GONE);
                deliveryTypeLL.setVisibility(View.VISIBLE);
                pickupDeliveryLL.setVisibility(View.GONE);
                merchantDetailRL.setVisibility(View.GONE);
                selectedMerchent = mDataMap.get(marker);

                if (selectedMerchent != null) {
                    if (lastClicked != null)
                        if (mDataMap.get(marker).getStorefrontUserId() != mDataMap.get(lastClicked).getStorefrontUserId())
                            lastClicked.setIcon(bitmapDescriptorFromVector(mActivity, R.color.transparent, true, true));
                    marker.setIcon(bitmapDescriptorFromVector(mActivity, R.color.transparent, false, true));
                    lastClicked = marker;
                    setBottomView();
                }
                return true;
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                resetBottomViewAndMarker();

            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
                LatLng latLng = googleMap.getCameraPosition().target;

                merchantDetailRL.setVisibility(View.GONE);
                if (lastClicked != null)
                    lastClicked.setIcon(bitmapDescriptorFromVector(mActivity, R.color.transparent, true, true));
                lastClicked = null;
                selectedMerchent = null;
                ((RestaurantListingActivity) mActivity).latitude = latLng.latitude;
                ((RestaurantListingActivity) mActivity).longitude = latLng.longitude;

                if (prevLatLong == null) {
                    prevLatLong = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                }
                Double distanceCalculated = (Utils.getDistance(prevLatLong.latitude,
                        prevLatLong.longitude, latLng.latitude, latLng.longitude)) / 1000;
                Log.e("Distance ", "<<<<" + distanceCalculated);
                isCurrentLoc = false;
                ((RestaurantListingActivity) mActivity).executeSetAddress();

                if (googleMap.getCameraPosition().zoom < previousZoomLevel) {
                    if (distanceCalculated > (previousRadii * 0.75)) {
                        prevLatLong = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                        previousRadii = 0.0;
                    }
                    checkRedii(visibleRegion);
                } else {
                    if (distanceCalculated > (previousRadii * 0.75)) {
                        prevLatLong = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                        previousRadii = 0.0;
                        checkRedii(visibleRegion);
                    }

                }
            }
        });


        getData();

    }

    @Override
    public void onMapReady(@NonNull MapboxMap fmaps) {

        flightMap = fmaps;
        flightMap.getUiSettings().setCompassEnabled(false);
        flightMap.setStyle(
                new Style.Builder().fromUri("https://maps.flightmap.io/styles//default.json")
        );
//            flightMap.setMyLocationEnabled(false);
//        flightMap.setMinZoomPreference(8.0f);
//        flightMap.setMaxZoomPreference(15.0f);
        flightMap.getUiSettings().setAttributionMargins(0, 0, 30, 30);
        flightMap.getUiSettings().setAttributionGravity(Gravity.RIGHT | Gravity.BOTTOM);

        movemap(true);

        flightMap.addOnCameraIdleListener(this);
        flightMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull com.mapbox.mapboxsdk.annotations.Marker markers) {
                rvAdminCategory.setVisibility(View.GONE);
                deliveryTypeLL.setVisibility(View.VISIBLE);
                pickupDeliveryLL.setVisibility(View.GONE);
                merchantDetailRL.setVisibility(View.GONE);
                selectedMerchent = mDataFlightMap.get(markers);

                if (selectedMerchent != null) {
                    if (lastClickedFlightmap != null)
                        if (mDataFlightMap.get(markers).getStorefrontUserId() != mDataFlightMap.get(lastClickedFlightmap).getStorefrontUserId())
                            lastClickedFlightmap.setIcon(setcustommapboxmarkericon(mActivity, R.color.transparent, true, true));


                    markers.setIcon(setcustommapboxmarkericon(mActivity, R.color.transparent, false, true));

                    lastClickedFlightmap = markers;
                    setBottomView();
                }
                return true;
            }


        });
        fmaps.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull com.mapbox.mapboxsdk.geometry.LatLng point) {
                resetBottomViewAndMarker();
                return true;
            }
        });

        getData();

    }

    @Override
    public void onCameraIdle() {

        com.mapbox.mapboxsdk.geometry.VisibleRegion visibleRegion = flightMap.getProjection().getVisibleRegion();
        com.mapbox.mapboxsdk.geometry.LatLng latLng = flightMap.getCameraPosition().target;

        merchantDetailRL.setVisibility(View.GONE);
        if (lastClicked != null)
            lastClicked.setIcon(bitmapDescriptorFromVector(mActivity, R.color.transparent, true, true));
        lastClicked = null;
        selectedMerchent = null;
        ((RestaurantListingActivity) mActivity).latitude = latLng.latitude;
        ((RestaurantListingActivity) mActivity).longitude = latLng.longitude;

        if (prevLatLong == null) {
            prevLatLong = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
        }
        Double distanceCalculated = (Utils.getDistance(prevLatLong.latitude,
                prevLatLong.longitude, latLng.latitude, latLng.longitude)) / 1000;
        Log.e("Distance ", "<<<<" + distanceCalculated);
        isCurrentLoc = false;
        ((RestaurantListingActivity) mActivity).executeSetAddress();

        if (flightMap.getCameraPosition().zoom < previousZoomLevel) {
            if (distanceCalculated > (previousRadii * 0.75)) {
                prevLatLong = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                previousRadii = 0.0;
            }
            checkRediiflightmap(visibleRegion);
        } else {
            if (distanceCalculated > (previousRadii * 0.75)) {
                prevLatLong = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                previousRadii = 0.0;
                checkRediiflightmap(visibleRegion);

            }

        }

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
                ivHomedelivery.setVisibility(View.VISIBLE);
                ivPickAndDrop.setVisibility(View.VISIBLE);
                ivSelfPickup.setVisibility(View.VISIBLE);
                tvPuckupAndDrop.setVisibility(View.VISIBLE);
                tvSelfPickup.setVisibility(View.VISIBLE);
                tvHomeDelivery.setVisibility(View.VISIBLE);
            } else if ((StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && StorefrontCommonData.getFormSettings().getHomeDelivery() == 1)) {
//                llSelfPickupDelivery.setWeightSum(2);
                if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.PICK_AND_DROP)
                    ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;

                ivHomedelivery.setVisibility(View.VISIBLE);
                ivPickAndDrop.setVisibility(View.GONE);
                ivSelfPickup.setVisibility(View.VISIBLE);
                tvPuckupAndDrop.setVisibility(View.GONE);
                tvSelfPickup.setVisibility(View.VISIBLE);
                tvHomeDelivery.setVisibility(View.VISIBLE);

            } else if (StorefrontCommonData.getFormSettings().getSelfPickup() == 1 && ifPD) {
                if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.HOME_DELIVERY)
                    ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
//                llSelfPickupDelivery.setWeightSum(2);
                ivHomedelivery.setVisibility(View.GONE);
                ivPickAndDrop.setVisibility(View.VISIBLE);
                ivSelfPickup.setVisibility(View.VISIBLE);

                tvPuckupAndDrop.setVisibility(View.VISIBLE);
                tvSelfPickup.setVisibility(View.VISIBLE);
                tvHomeDelivery.setVisibility(View.GONE);
            } else if (StorefrontCommonData.getFormSettings().getHomeDelivery() == 1 && ifPD) {
                if (((RestaurantListingActivity) mActivity).selectedPickupMode == Constants.SelectedPickupMode.SELF_PICKUP)
                    ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
//                llSelfPickupDelivery.setWeightSum(2);
                ivHomedelivery.setVisibility(View.VISIBLE);
                ivPickAndDrop.setVisibility(View.VISIBLE);
                ivSelfPickup.setVisibility(View.GONE);

                tvPuckupAndDrop.setVisibility(View.VISIBLE);
                tvSelfPickup.setVisibility(View.GONE);
                tvHomeDelivery.setVisibility(View.VISIBLE);
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


    }




    private void resetBottomViewAndMarker() {
        rvAdminCategory.setVisibility(View.GONE);
        deliveryTypeLL.setVisibility(View.VISIBLE);
        pickupDeliveryLL.setVisibility(View.GONE);
        merchantDetailRL.setVisibility(View.GONE);
        if (isflightmap == true) {
            if (lastClickedFlightmap != null) {
                lastClickedFlightmap.setIcon(setcustommapboxmarkericon(mActivity, R.color.transparent, false, true));
            }

        } else {
            if (lastClicked != null)
                lastClicked.setIcon(bitmapDescriptorFromVector(mActivity, R.color.transparent, true, true));
            lastClicked = null;
        }
        selectedMerchent = null;
    }

    private void checkRedii(VisibleRegion visibleRegion) {
        double visableRadius = Utils.getVisablemapRadius(visibleRegion);

        double currentradii = visableRadius / 1000;

        if (previousRadii > 0.0) {
            getMarketplaceStorefronts(false, previousRadii, currentradii);
            previousRadii = currentradii;
        } else {
            getMarketplaceStorefronts(true, 0.0, currentradii);
            previousRadii = currentradii;
        }

        previousZoomLevel = googleMap.getCameraPosition().zoom;

        if (UIManager.getIsBusinessCategoryEnabled())
            callbackForBusinessCategories();

    }

    private void checkRediiflightmap(com.mapbox.mapboxsdk.geometry.VisibleRegion visibleRegion) {
        double visableRadius = Utils.getVisableFlightmapRadius(visibleRegion);

        double currentradii = visableRadius / 1000;

        if (previousRadii > 0.0) {
            getMarketplaceStorefronts(false, previousRadii, currentradii);
            previousRadii = currentradii;
        } else {
            getMarketplaceStorefronts(true, 0.0, currentradii);
            previousRadii = currentradii;
        }

        previousZoomLevel = (float) flightMap.getCameraPosition().zoom;
    }

    private void getMerchentdetails(com.tookancustomer.models.MarketplaceStorefrontModel.Datum datum) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());

        commonParams.add(Keys.APIFieldKeys.LATITUDE, ((RestaurantListingActivity) mActivity).latitude != null ? ((RestaurantListingActivity) mActivity).latitude : 0)
                .add(Keys.APIFieldKeys.LONGITUDE, ((RestaurantListingActivity) mActivity).longitude != null ? ((RestaurantListingActivity) mActivity).longitude : 0);
        commonParams.add(USER_ID, datum.getStorefrontUserId());
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

                    cityStorefrontsModels.getData().get(0).setSelectedPickupMode(((RestaurantListingActivity) mActivity).selectedPickupMode);
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
                selectedMerchent = cityStorefrontsModels.getData().get(0);
                StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(selectedMerchent.getMerchantMinimumOrder());
                openRestaurantActivity();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    private void movemap(boolean isZoomout) {

        if (isZoomout) {
            previousRadii = 0.0;
            previousZoomLevel = 20;
            if (isflightmap == true) {
                com.mapbox.mapboxsdk.geometry.LatLng flightMaplatLng = new com.mapbox.mapboxsdk.geometry.LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                // For zooming automatically to the location of the marker

                com.mapbox.mapboxsdk.camera.CameraPosition cameraPositionflightmap;

                cameraPositionflightmap = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder().target(flightMaplatLng).zoom(14).build();

                flightMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionflightmap));
            } else {
                LatLng latLng = new LatLng(((RestaurantListingActivity) mActivity).latitude, ((RestaurantListingActivity) mActivity).longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();
                googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }


    }

    private void setBottomView() {
        merchantDetailRL.setVisibility(View.VISIBLE);

        tvRestaurantName.setText(selectedMerchent.getStoreName());
        tvRestaurantName.setTypeface(tvRestaurantName.getTypeface(), Typeface.BOLD);
        tvRestaurantName.setVisibility(tvRestaurantName.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        tvRestaurantAddress.setText(selectedMerchent.getDistance() + (!selectedMerchent.getDisplayAddress().isEmpty() ? " \u2022 " + selectedMerchent.getDisplayAddress() : ""));
        tvRestaurantAddress.setVisibility(tvRestaurantAddress.getText().toString().isEmpty() || StorefrontCommonData.getAppConfigurationData().getDisplayMerchantAddress() == 0 ? View.GONE : View.VISIBLE);


        if (selectedMerchent.getLogo() != null && !selectedMerchent.getLogo().isEmpty()) {
            try {
                ivImage.post(new Runnable() {
                    @Override
                    public void run() {
                        new GlideUtil.GlideUtilBuilder(ivImage)
                                .setLoadItem(selectedMerchent.getLogo())
                                .setTransformation(new RoundedCorners(7))
                                .setPlaceholder(R.drawable.ic_plcaeholder_marketplace_new)
                                .build();

                    }
                });
            } catch (Exception e) {

                Utils.printStackTrace(e);
            }
        } else {

            ivImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_plcaeholder_marketplace_new));
            ivImage.setBackground(null);
        }

        if ((!UIManager.getIsReviewRatingRequired() || selectedMerchent.getStoreRating().floatValue() <= 0)) {
            llRatingImageLayout.setVisibility(View.GONE);
        } else {
            llRatingImageLayout.setVisibility(View.VISIBLE);
            tvAverageRatingImage.setText(selectedMerchent.getStoreRating().floatValue() + "");
            tvAverageRatingImage.setTypeface(tvAverageRatingImage.getTypeface(), Typeface.BOLD);
        }

        if (selectedMerchent.getIsStorefrontOpened() == 0) {
            ColorMatrix matrix = new ColorMatrix();
            if (selectedMerchent.getScheduledTask() == 1) {
                tvRestaurantOpenStatus.setText(StorefrontCommonData.getString(mActivity, R.string.pre_order));
                Utils.setTextColor(mActivity, R.color.textcolor_preorder, tvRestaurantOpenStatus);
                matrix.setSaturation(1);
            } else {
                tvRestaurantOpenStatus.setText(StorefrontCommonData.getString(mActivity, R.string.closed));
                Utils.setTextColor(mActivity, R.color.textcolor_closed, tvRestaurantOpenStatus);
                matrix.setSaturation(0);
            }
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            ivImage.setColorFilter(filter);

        } else {
            tvRestaurantOpenStatus.setText(StorefrontCommonData.getString(mActivity, R.string.text_order_online).replace(ORDER, Utils.getCallTaskAs(true, true)));
            Utils.setTextColor(mActivity, R.color.textcolor_open, tvRestaurantOpenStatus);

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(1);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            ivImage.setColorFilter(filter);
        }

        int calculatedtime = 0;

        if (selectedMerchent.getSelectedPickupMode() == Constants.SelectedPickupMode.HOME_DELIVERY) {
            if (selectedMerchent.getMerchantAsDeliveryManager() == 1) {
                calculatedtime = selectedMerchent.getMerchantDeliveryTime() + selectedMerchent.getOrderPreparationTime();
            } else {
                calculatedtime = selectedMerchent.getDeliveryTime() + selectedMerchent.getOrderPreparationTime();
            }

            if (selectedMerchent.getEstimatedAddOn() == 1) {
                calculatedtime = selectedMerchent.getEstimatedTime() + selectedMerchent.getOrderPreparationTime();
            }


        } else if (selectedMerchent.getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
            calculatedtime = selectedMerchent.getOrderPreparationTime();
        } else {
            if (selectedMerchent.getSelfPickup() == 0 && selectedMerchent.getHomeDelivery() == 1) {
                if (selectedMerchent.getMerchantAsDeliveryManager() == 1) {
                    calculatedtime = selectedMerchent.getMerchantDeliveryTime() + selectedMerchent.getOrderPreparationTime();
                } else {
                    calculatedtime = selectedMerchent.getDeliveryTime() + selectedMerchent.getOrderPreparationTime();
                }
            } else {
                calculatedtime = selectedMerchent.getOrderPreparationTime();
            }
        }


        if (!Utils.calculateHourMinsFrmMins(calculatedtime).isEmpty() &&
                selectedMerchent.getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE && !Dependencies.isLaundryApp()
                && UIManager.isShowDeliveryTimeEnable()) {
            tvDeliveryTime.setVisibility(View.VISIBLE);
            tvDeliveryTime.setText(Utils.calculateHourMinsFrmMins(calculatedtime));
        } else {
            tvDeliveryTime.setVisibility(View.GONE);

        }
        ivSponsoredRibbon.setVisibility(selectedMerchent.getIs_sponsored() == 1 ? View.VISIBLE : View.GONE);
        tvMerchantDiscount.setVisibility(selectedMerchent.merchantDiscount != null && selectedMerchent.merchantDiscount > 0 ? View.VISIBLE : View.GONE);
        tvMerchantDiscount.setText(selectedMerchent.getMerchantDiscount() + StorefrontCommonData.getString(mActivity, R.string.discount_percent_off));
        tvRestaurantCategories.setText(selectedMerchent.getBusinessCategoriesName());
        tvRestaurantCategories.setVisibility(selectedMerchent.getBusinessCategoriesName().isEmpty() ? View.GONE : View.VISIBLE);


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
                    ((RestaurantListingActivity) mActivity).tvHeading.setText(data.getStringExtra("address"));
                    ((RestaurantListingActivity) mActivity).latitude = data.getDoubleExtra("latitude", 0.0);
                    ((RestaurantListingActivity) mActivity).longitude = data.getDoubleExtra("longitude", 0.0);

                    movemap(true);
                }
                break;

            case Codes.Request.OPEN_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    StorefrontCommonData.setUserData((UserData) data.getExtras().getSerializable(UserData.class.getName()));
                    isLanguageChanged = data.getExtras().getBoolean("isLanguageChanged");
                    SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
                    if (isLanguageChanged)
                        ((RestaurantListingActivity) mActivity).restartActivity();
                }
                break;

            case Codes.Request.LOCATION_ACCESS_REQUEST:
                if (resultCode == RESULT_OK) {
                    isCurrentLoc = true;
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
                    if (data.getExtras().getString(Keys.Extras.SUCCESS_MESSAGE) != null) {
                        Utils.snackbarSuccess(mActivity, data.getStringExtra(Keys.Extras.SUCCESS_MESSAGE));
                    } else if (data.getExtras().getString(Keys.Extras.FAILURE_MESSAGE) != null) {
                        Utils.snackBar(mActivity, data.getStringExtra(Keys.Extras.FAILURE_MESSAGE));
                    }


                    if (data.getExtras() != null && (data.hasExtra(STOREFRONT_DATA) && data.hasExtra(STOREFRONT_DATA_ITEM_POS))) {

                        com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData = (com.tookancustomer.models.MarketplaceStorefrontModel.Datum) data.getExtras().getSerializable(STOREFRONT_DATA);
                        int storefrontDataItemPos = data.getExtras().getInt(STOREFRONT_DATA_ITEM_POS);

                        try {
                            merchantsArrayList.set(storefrontDataItemPos, storefrontData);
                            setMerchantsOnMap(true);
                        } catch (Exception e) {
                        }
                    }
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
                            merchantsArrayList.set(storefrontDataItemPos, storefrontData);
                            setMerchantsOnMap(true);
                        } catch (Exception e) {
                        }
                    }
                }
                break;

            case FilterConstants.REQUEST_CODE_TO_OPEN_FILTER:
                if (resultCode == RESULT_OK) {
                    ((RestaurantListingActivity) mActivity).allFilterList = data.getParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY_ALL);
                    ((RestaurantListingActivity) mActivity).categoryFilterList = data.getParcelableArrayListExtra(FilterConstants.EXTRA_BUSINESS_CATEGORY);

                    movemap(true);
                }
                break;


        }
    }

    @Override
    public void onClick(View v) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }

        if (v.getId() == R.id.rllist) {

            if (mActivity != null && mActivity instanceof RestaurantListingActivity)
                ((RestaurantListingActivity) mActivity).setListFragment(null);
            //                ((RestaurantListingActivity) mActivity).setListFragment(bundle);
        }
        if (v.getId() == R.id.deliveryTypeLL) {

            pickupDeliveryLL.setVisibility(View.VISIBLE);
            deliveryTypeLL.setVisibility(View.GONE);

        }
        if (v.getId() == R.id.tvHomeDelivery) {
            ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.HOME_DELIVERY;
            resetSelfPickupDeliveryAssets();
            deliveryTypeLL.setVisibility(View.VISIBLE);
            pickupDeliveryLL.setVisibility(View.GONE);
            movemap(true);

        }
        if (v.getId() == R.id.tvSelfPickup) {
            ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.SELF_PICKUP;
            resetSelfPickupDeliveryAssets();
            deliveryTypeLL.setVisibility(View.VISIBLE);
            pickupDeliveryLL.setVisibility(View.GONE);
            movemap(true);
        }

        if(v.getId() == R.id.tvPuckupAndDrop){
            ((RestaurantListingActivity) mActivity).selectedPickupMode = Constants.SelectedPickupMode.PICK_AND_DROP;
            resetSelfPickupDeliveryAssets();
            deliveryTypeIV.setVisibility(View.VISIBLE);
            pickupDeliveryLL.setVisibility(View.GONE);
            movemap(true);
        }

        if (v.getId() == R.id.locationIV) {
            isCurrentLoc = true;
            ((RestaurantListingActivity) mActivity).startLocationFetcher();
        }
        if (v.getId() == R.id.selectedCategoryTV) {
            if (rvAdminCategory.getVisibility() == View.VISIBLE) {
                rvAdminCategory.setVisibility(View.GONE);
                seperaterView.setVisibility(View.GONE);
                selectedCategoryTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);

            } else {
                rvAdminCategory.setVisibility(View.VISIBLE);
                seperaterView.setVisibility(View.VISIBLE);
                selectedCategoryTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_navigate_close, 0);

            }

        }
        if (v.getId() == R.id.merchantDetailRL) {
            if (selectedMerchent != null) {
//                if (selectedMerchent.getIsStorefrontOpened() == 0 && selectedMerchent.getScheduledTask() == 0) {
//                    Utils.snackBar(mActivity, StorefrontCommonData.getString(mActivity, R.string.store_currently_closed));
//                    return;
//                }
                getMerchentdetails(selectedMerchent);
            }

        }

    }

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
                            if (businessCategoriesData.getResult().get(i).getIs_all_category() == 1 && (((RestaurantListingActivity) mActivity).businessCategoryId == null
                                    || ((RestaurantListingActivity) mActivity).businessCategoryId == 0)) {
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


                        BusinessCategoriesMapAdapter businessCategoriesAdapter =
                                new BusinessCategoriesMapAdapter(mActivity, results, ((RestaurantListingActivity) mActivity).businessCategoryId,
                                        new BusinessCategoriesMapAdapter.BusinessCategoryMapListener() {
                                            @Override
                                            public void onBusinessCategorySelectedMap(int pos, Result businessCategory, boolean isAllBusiness) {
                                                ((RestaurantListingActivity) mActivity).isAllBusinessCategory = isAllBusiness;
                                                selectedCategoryTV.setText(businessCategory.getName());

                                                if (businessCategory != null && businessCategory.getIsCustomOrderActive() == 1) {
                                                    customCheckout = true;
                                                    Bundle extraa = new Bundle();
                                                    extraa.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                                                    extraa.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) mActivity).latitude);
                                                    extraa.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) mActivity).longitude);
                                                    extraa.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);
                                                    extraa.putBoolean("isCustomOrder", true);
                                                    Transition.openCustomCheckoutActivity(mActivity, extraa);
                                                } else {

                                                    if (businessCategory != null)
                                                        ((RestaurantListingActivity) mActivity).businessCategoryId = businessCategory.getId();
                                                    else {
                                                        ((RestaurantListingActivity) mActivity).businessCategoryId = 0;
                                                    }

                                                    if (((RestaurantListingActivity) mActivity).categoryFilterList != null &&
                                                            ((RestaurantListingActivity) mActivity).businessCategoryId != ((RestaurantListingActivity) mActivity).categoryFilterList.get(0).getBusinessCategoryId())
                                                        ((RestaurantListingActivity) mActivity).categoryFilterList = null;

                                                    selectedCategoryTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);

                                                    rvAdminCategory.setVisibility(View.GONE);
                                                    seperaterView.setVisibility(View.GONE);
                                                    movemap(true);
                                                }
                                            }

                                        });


                        rvAdminCategory.setAdapter(businessCategoriesAdapter);
                        LinearLayoutManager lm = new LinearLayoutManager(mActivity,
                                LinearLayoutManager.VERTICAL, false);
                        lm.setStackFromEnd(true);
                        rvAdminCategory.setLayoutManager(lm);
                        rvAdminCategory.scrollToPosition(0);

                        for (int i = 0; i < results.size(); i++) {
                            if (results.get(i).isSelected()) {
                                selectedCategoryTV.setText(results.get(i).getName());
                            }

                        }


                        if (results.size() > 0) {
                            businessCategoryLL.setVisibility(View.VISIBLE);
                            if (results.size() == 1 && results.get(0).getIs_all_category() == 1) {
                                businessCategoryLL.setVisibility(View.GONE);
                            }
                        } else {
                            businessCategoryLL.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        businessCategoryLL.setVisibility(View.GONE);
                    }
                });

    }


    public void updateLocation() {
        movemap(isCurrentLoc);
    }


    private void openRestaurantActivity() {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.RESTAURANT_CLICK, selectedMerchent.getStoreName() + "");

        StorefrontCommonData.getUserData().getData().getVendorDetails().setUserId(selectedMerchent.getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setUserId(selectedMerchent.getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(selectedMerchent.getMerchantMinimumOrder());

        if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(STOREFRONT_DATA, selectedMerchent);

            if (mActivity instanceof RestaurantListingActivity) {
                bundle.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) mActivity).latitude);
                bundle.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) mActivity).longitude);
                bundle.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) mActivity).address);
                bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, "");

                bundle.putInt(BUSINESS_CATEGORY_ID, getBusinessCategory());
                Dependencies.setIsPreorderSelecetedForMenu(false);
            }

            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtras(bundle);
            mActivity.startActivityForResult(intent, OPEN_HOME_ACTIVITY);
            AnimationUtils.forwardTransition(mActivity);

        } else {
            Double latitude = 0.0, longitude = 0.0;
            String adminSelectedCategories = "", address = "";
            Integer businessCategoryId = null;
            String preOrderDateTime = null;

            if (mActivity instanceof RestaurantListingActivity) {
                latitude = ((RestaurantListingActivity) mActivity).latitude;
                longitude = ((RestaurantListingActivity) mActivity).longitude;
                address = ((RestaurantListingActivity) mActivity).address;
                if (getBusinessCategory() != null
                        && !isAllBusinessCategory())
                    businessCategoryId = getBusinessCategory();
                Dependencies.setIsPreorderSelecetedForMenu(false);
            }

            if (selectedMerchent != null && Dependencies.getSelectedProductsArrayList().size() > 0) {
                if (selectedMerchent.getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                    for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                        Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(selectedMerchent);
                    }
                }
            }
            if (selectedMerchent != null) {
                if (UIManager.isMenuEnabled() && selectedMerchent.getIsMenuEnabled() &&
                        selectedMerchent.getScheduledTask() == 1 &&
                        selectedMerchent.getIsStorefrontOpened() == 0) {
                    final String finalAdminSelectedCategories = adminSelectedCategories;
                    final String finalAddress = address;
                    final Double finalLatitude = latitude;
                    final Double finalLongitude = longitude;
                    final Integer finalbusinessCategoryId = businessCategoryId;
                    new SelectPreOrderTimeDialog(mActivity, new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                        @Override
                        public void onDateTimeSelected(String dateTime) {
                            StorefrontConfig.getAppCatalogueV2(mActivity, selectedMerchent.getStoreName(),
                                    selectedMerchent.getLogo(),
                                    new LatLng(Double.valueOf(selectedMerchent.getLatitude()),
                                            Double.valueOf(selectedMerchent.getLongitude())),
                                    new LatLng(finalLatitude, finalLongitude),
                                    finalAddress,
                                    selectedMerchent,
                                    finalAdminSelectedCategories,
                                    dateTime, finalbusinessCategoryId);
                        }
                    }).setStorefrontData(selectedMerchent)
                            .show();
                } else {
                    if (preOrderDateTime == null)
                        StorefrontConfig.getAppCatalogueV2(mActivity, selectedMerchent.getStoreName(),
                                selectedMerchent.getLogo(),
                                new LatLng(Double.valueOf(selectedMerchent.getLatitude()),
                                        Double.valueOf(selectedMerchent.getLongitude())),
                                new LatLng(latitude, longitude), address, selectedMerchent,
                                adminSelectedCategories, businessCategoryId, false, 0);
                    else
                        StorefrontConfig.getAppCatalogueV2(mActivity, selectedMerchent.getStoreName(),
                                selectedMerchent.getLogo(),
                                new LatLng(Double.valueOf(selectedMerchent.getLatitude()),
                                        Double.valueOf(selectedMerchent.getLongitude())),
                                new LatLng(latitude, longitude), address, selectedMerchent,
                                adminSelectedCategories, preOrderDateTime, businessCategoryId);
                }


            } else
                StorefrontConfig.getAppCatalogueV2(mActivity);
        }
    }


}
