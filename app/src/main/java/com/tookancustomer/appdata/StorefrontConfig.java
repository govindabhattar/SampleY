package com.tookancustomer.appdata;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Utils;

import java.text.ParseException;
import java.util.Calendar;

import static com.tookancustomer.appdata.Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DATE_TIME;

/**
 * Created by shwetaaggarwal on 18/09/17.
 */

public class StorefrontConfig implements Keys.Extras {

    public static void getAppCatalogueV2(final Activity activity) {
        getAppCatalogueV2(activity, null, null, null, null, "");
    }

    public static void getAppCatalogueV2(final Activity activity, Double latitude, Double longitude, String address
            , com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData, String adminSelectedCategories) {
        try {
            getAppCatalogueV2(activity, StorefrontCommonData.getFormSettings().getFormName(),
                    StorefrontCommonData.getFormSettings().getLogo()
                    , latitude, longitude, address, storefrontData, adminSelectedCategories,
                    DateUtils.getInstance().convertDateObjectToUtc(Calendar.getInstance().getTime()), null, false, 0);
        } catch (ParseException e) {

            Utils.printStackTrace(e);
        }
    }

    //yhan lagana h
    public static void getAppCatalogueV2(final Activity activity, final String headerName, final String headerLogo,
                                         final LatLng storefrontLatLng, final LatLng currentLatLng, String address,
                                         com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData,
                                         String adminSelectedCategories, Integer businessCategoryId, boolean isEdotOrder, Integer jobId) {
        StorefrontCommonData.getUserData().getData().setStoreLatitude(storefrontLatLng.latitude + "");
        StorefrontCommonData.getUserData().getData().setStoreLongitude(storefrontLatLng.longitude + "");
        try {
            getAppCatalogueV2(activity, headerName, headerLogo,
                    currentLatLng.latitude, currentLatLng.longitude,
                    address, storefrontData, adminSelectedCategories,
                    DateUtils.getInstance().convertDateObjectToUtc(Calendar.getInstance().getTime()), businessCategoryId, isEdotOrder, jobId);
        } catch (ParseException e) {

            Utils.printStackTrace(e);
        }
    }

    public static void getAppCatalogueV2(final Activity activity, final String headerName, final String headerLogo,
                                         final LatLng storefrontLatLng, final LatLng currentLatLng, String address,
                                         com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData,
                                         String adminSelectedCategories, String utcDateTime, Integer businessCategoryId) {
        StorefrontCommonData.getUserData().getData().setStoreLatitude(storefrontLatLng.latitude + "");
        StorefrontCommonData.getUserData().getData().setStoreLongitude(storefrontLatLng.longitude + "");
        getAppCatalogueV2(activity, headerName, headerLogo,
                currentLatLng.latitude, currentLatLng.longitude,
                address, storefrontData, adminSelectedCategories, utcDateTime, businessCategoryId, false, 0);
    }

    public static void getAppCatalogueV2(final Activity activity, final String headerName, final String headerLogo,
                                         final Double latitude, final Double longitude,
                                         final String address, final com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData,
                                         final String adminSelectedCategories, final String utcDateTime
            , final Integer businessCategoryId, boolean isEditOrder, Integer jobId) {
        Location location = LocationUtils.getLastLocation(activity);

        Bundle bundle = new Bundle();
        bundle.putSerializable(STOREFRONT_DATA, storefrontData);
        bundle.putSerializable(DATE_TIME, utcDateTime);
        bundle.putSerializable(PICKUP_LATITUDE, latitude == null ? (location != null ? location.getLatitude() : 0.0) : latitude);
        bundle.putSerializable(PICKUP_LONGITUDE, longitude == null ? (location != null ? location.getLongitude() : 0.0) : longitude);
        bundle.putSerializable(PICKUP_ADDRESS, address);
        bundle.putSerializable(IS_EDIT_ORDER, isEditOrder);

        if (isEditOrder)
            bundle.putSerializable(EDIT_JOB_ID, jobId);

        bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, adminSelectedCategories);
        if (businessCategoryId != null)
            bundle.putInt(BUSINESS_CATEGORY_ID, businessCategoryId);
        Intent intentCatalog = new Intent(activity, MerchantCatalogActivity.class);
        intentCatalog.putExtras(bundle);
        activity.startActivityForResult(intentCatalog, OPEN_NLEVEL_ACTIVITY_AGAIN);
        return;

        //TODO Handle new nlevel


//        if (storefrontData.getHasCategories() == 1) {
//
//            CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(activity, StorefrontCommonData.getUserData());
//            commonParams.add(USER_ID, storefrontData.getStorefrontUserId());
//
//            commonParams.add(CURRENT_LATITUDE, latitude == null ?
//                    (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLatitude() : 0.0) :
//                    latitude)
//                    .add(CURRENT_LONGITUDE, longitude == null ?
//                            (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLongitude() : 0.0) :
//                            longitude)
//                    .add(COMPANY_LATITUDE, StorefrontCommonData.getUserData().getData().getStoreLatitude())
//                    .add(COMPANY_LONGITUDE, StorefrontCommonData.getUserData().getData().getStoreLongitude());
//
//            commonParams.add(DATE_TIME, utcDateTime);
//
//            if (businessCategoryId != null && businessCategoryId != 0 && businessCategoryId != -1 && storefrontData.getBusinessCatalogMappingEnabled() == 1)
//                commonParams.add(BUSINESS_CATEGORY_ID, businessCategoryId);
//
//            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
//                commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
//            }
//            ProgressDialog.show(activity);
//            RestClient.getApiInterface(activity).getAppCatalogue(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, false, true) {
//                @Override
//                public void success(BaseModel baseModel) {
//                    NLevelWorkFlowData nLevelWorkFlowData = new NLevelWorkFlowData();
//                    try {
//                        ArrayList<ArrayList<Datum>> mList
//                                = new Gson().fromJson(new Gson().toJson(baseModel.data),
//                                new TypeToken<ArrayList<ArrayList<Datum>>>() {
//                                }.getType());
//                        nLevelWorkFlowData.setData(mList);
//                    } catch (Exception e) {
//                    }
//
//                    Bundle extras = new Bundle();
//                    extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
//                    extras.putSerializable(NLevelWorkFlowData.class.getName(), nLevelWorkFlowData);
//                    extras.putSerializable(STOREFRONT_DATA, storefrontData);
//                    extras.putString(HEADER_NAME, headerName);
//                    extras.putString(HEADER_LOGO, headerLogo);
//                    extras.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, adminSelectedCategories);
//                    if (businessCategoryId != null)
//                        extras.putInt(BUSINESS_CATEGORY_ID, businessCategoryId);
//                    extras.putInt(CATEGORY_LEVEL, 0);
//                    extras.putIntegerArrayList(PARENT_ID, new ArrayList<Integer>());
//
//                    extras.putDouble(PICKUP_LATITUDE, latitude == null ? (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLatitude() : 0.0) : latitude);
//                    extras.putDouble(PICKUP_LONGITUDE, longitude == null ? (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLongitude() : 0.0) : longitude);
//                    extras.putString(PICKUP_ADDRESS, address);
//
////                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.ProductTypeData>());
//                    extras.putString(DATE_TIME, utcDateTime);
//                    ProgressDialog.dismiss();
//                    Intent intent = new Intent(activity, NLevelWorkFlowActivity.class);
//                    intent.putExtras(extras);
//                    activity.startActivityForResult(intent, OPEN_NLEVEL_ACTIVITY_AGAIN);
//                    AnimationUtils.forwardTransition(activity);
//                }
//
//                @Override
//                public void failure(APIError error, BaseModel baseModel) {
//                    ProgressDialog.dismiss();
//
////                    if (error.getStatusCode() == 103) {
////                        new OptionsDialog.Builder(activity).message(error.getMessage() + " Please change your location.")
////                                .positiveButton(R.string.ok).negativeButton(R.string.cancel).listener(new OptionsDialog.Listener() {
////                            @Override
////                            public void performPositiveAction(int purpose, Bundle backpack) {
////                                Utils.searchPlace(activity, PlaceAutocomplete.MODE_OVERLAY, getCurrentLocation(activity));
////                            }
////
////                            @Override
////                            public void performNegativeAction(int purpose, Bundle backpack) {
////                            }
////                        }).build().show();
////                    } else {
////                        Utils.snackBar(activity, error.getMessage());
////                    }
//
//                }
//            });
//        } else {
//
//            Bundle extras = new Bundle();
//            extras.putSerializable(STOREFRONT_DATA, storefrontData);
//            extras.putString(HEADER_NAME, headerName);
//            extras.putString(HEADER_LOGO, headerLogo);
//            extras.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, adminSelectedCategories);
//            if (businessCategoryId != null)
//                extras.putInt(BUSINESS_CATEGORY_ID, businessCategoryId);
//            extras.putInt(CATEGORY_LEVEL, 0);
//            extras.putIntegerArrayList(PARENT_ID, new ArrayList<Integer>());
//
//            extras.putDouble(PICKUP_LATITUDE, latitude == null ? (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLatitude() : 0.0) : latitude);
//            extras.putDouble(PICKUP_LONGITUDE, longitude == null ? (LocationUtils.getLastLocation(activity) != null ? LocationUtils.getLastLocation(activity).getLongitude() : 0.0) : longitude);
//            extras.putString(PICKUP_ADDRESS, address);
//
////                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.ProductTypeData>());
//            extras.putString(DATE_TIME, utcDateTime);
//            ProgressDialog.dismiss();
//            Intent intent = new Intent(activity, NLevelWorkFlowActivity.class);
//            intent.putExtras(extras);
//            activity.startActivityForResult(intent, OPEN_NLEVEL_ACTIVITY_AGAIN);
//            AnimationUtils.forwardTransition(activity);
//        }
    }

    private static LatLng getCurrentLocation(Activity activity) {
        Location location = LocationUtils.getLastLocation(activity);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

}