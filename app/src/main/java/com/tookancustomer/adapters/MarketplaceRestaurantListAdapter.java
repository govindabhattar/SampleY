package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.HomeActivity;
import com.tookancustomer.MarketplaceSearchActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.fragments.MarketplaceSearchFragment;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.restorentListFragments.RestaurantListFragment;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.Date;
import java.util.List;

import static com.tookancustomer.appdata.Codes.Request.OPEN_HOME_ACTIVITY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DATE_TIME;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE_SELECTED_CATEGORIES;
import static com.tookancustomer.appdata.Keys.Extras.BUSINESS_CATEGORY_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA_ITEM_POS;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class MarketplaceRestaurantListAdapter extends RecyclerView.Adapter<MarketplaceRestaurantListAdapter.ViewHolder> implements TerminologyStrings {
    private Activity activity;
    private List<Datum> dataList;
    private Date preOrderDateObject;
    private Fragment fragment;

    public MarketplaceRestaurantListAdapter(Activity activity, List<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        this.fragment = null;
    }

    public MarketplaceRestaurantListAdapter(Activity activity, List<Datum> dataList, Fragment fragment) {
        this.activity = activity;
        this.dataList = dataList;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_restaurant_list, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final int adapterPos = viewHolder.getAdapterPosition();

        viewHolder.llParentView.setOnClickListener(v -> openRestaurantActivity(adapterPos));

        try {
            if (dataList.get(adapterPos).getShowMerchantTimings() == 1 && dataList.get(adapterPos).getStoreTimingsArr().size() != 0) {
                viewHolder.tvRestaurantTimings.setVisibility(View.VISIBLE);
                viewHolder.tvRestaurantTimings.setText(String.format("%s - %s", dataList.get(adapterPos).getStoreTimingsArr().get(0).getStartTime(), dataList.get(adapterPos).getStoreTimingsArr().get(0).getEndTime()));
            } else {
                viewHolder.tvRestaurantTimings.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dataList.get(adapterPos).getCustom_tag_for_merchant() != null && !dataList.get(adapterPos).getCustom_tag_for_merchant().isEmpty()) {
            String mCustomTag = dataList.get(adapterPos).getCustom_tag_for_merchant();
            if (mCustomTag.length() >= 15) {
                viewHolder.tvCustomTag.setText(String.format("%s...", mCustomTag.substring(0, 13)));
            } else {
                viewHolder.tvCustomTag.setText(mCustomTag);
            }
            // textView.setText(userName);
            // viewHolder.tvCustomTag.setText(dataList.get(adapterPos).getCustom_tag_for_merchant());
            viewHolder.tvCustomTag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvCustomTag.setVisibility(View.GONE);
        }

        if (Dependencies.getAccessToken(activity) != null && !Dependencies.getAccessToken(activity).isEmpty()) {
            if (dataList.get(adapterPos).getIsWishlisted() == 1) {
                viewHolder.ivWishlistSelected.setVisibility(View.VISIBLE);
                viewHolder.ivWishlistUnselected.setVisibility(View.GONE);

            } else {
                viewHolder.ivWishlistSelected.setVisibility(View.GONE);
                viewHolder.ivWishlistUnselected.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.ivWishlistSelected.setVisibility(View.GONE);
            viewHolder.ivWishlistUnselected.setVisibility(View.GONE);
        }


        viewHolder.ivWishlistUnselected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewHolder.ivWishlistSelected.setVisibility(View.VISIBLE);
                viewHolder.ivWishlistUnselected.setVisibility(View.GONE);
                if (fragment instanceof RestaurantListFragment)
                    ((RestaurantListFragment) fragment).selectedWishlist(true, adapterPos);
                else if (fragment instanceof MarketplaceSearchFragment)
                    ((MarketplaceSearchFragment) fragment).selectedWishlist(true, adapterPos);
            }
        });

        viewHolder.ivWishlistSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                viewHolder.ivWishlistSelected.setVisibility(View.GONE);
                viewHolder.ivWishlistUnselected.setVisibility(View.VISIBLE);
                if (fragment instanceof RestaurantListFragment)
                    ((RestaurantListFragment) fragment).selectedWishlist(false, adapterPos);
                else if (fragment instanceof MarketplaceSearchFragment)
                    ((MarketplaceSearchFragment) fragment).selectedWishlist(false, adapterPos);
            }
        });


        viewHolder.tvRestaurantName.setText(dataList.get(adapterPos).getStoreName());
        viewHolder.tvRestaurantName.setTypeface(viewHolder.tvRestaurantName.getTypeface(), Typeface.BOLD);
        viewHolder.tvRestaurantName.setVisibility(viewHolder.tvRestaurantName.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
        viewHolder.tvRestaurantAddress.setText(dataList.get(adapterPos).getDistance() + (!dataList.get(adapterPos).getDisplayAddress().isEmpty() ? " \u2022 " + dataList.get(adapterPos).getDisplayAddress() : ""));
        viewHolder.tvRestaurantAddress.setVisibility(viewHolder.tvRestaurantAddress.getText().toString().isEmpty() || StorefrontCommonData.getAppConfigurationData().getDisplayMerchantAddress() == 0 ? View.GONE : View.VISIBLE);

        int calculatedtime = 0;

        if (dataList.get(adapterPos).getSelectedPickupMode() == Constants.SelectedPickupMode.HOME_DELIVERY) {
            if (dataList.get(adapterPos).getMerchantAsDeliveryManager() == 1) {
                calculatedtime = dataList.get(adapterPos).getMerchantDeliveryTime() + dataList.get(adapterPos).getOrderPreparationTime();
            } else {
                calculatedtime = dataList.get(adapterPos).getDeliveryTime() + dataList.get(adapterPos).getOrderPreparationTime();
            }

            if (dataList.get(adapterPos).getEstimatedAddOn() == 1) {
                calculatedtime = dataList.get(adapterPos).getEstimatedTime() + dataList.get(adapterPos).getOrderPreparationTime();
            }
        } else if (dataList.get(adapterPos).getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP) {
            calculatedtime = dataList.get(adapterPos).getOrderPreparationTime();
        } else {
            if (dataList.get(adapterPos).getSelfPickup() == 0 && dataList.get(adapterPos).getHomeDelivery() == 1) {
                if (dataList.get(adapterPos).getMerchantAsDeliveryManager() == 1) {
                    calculatedtime = dataList.get(adapterPos).getMerchantDeliveryTime() + dataList.get(adapterPos).getOrderPreparationTime();
                } else {
                    calculatedtime = dataList.get(adapterPos).getDeliveryTime() + dataList.get(adapterPos).getOrderPreparationTime();
                }

                if (dataList.get(adapterPos).getEstimatedAddOn() == 1) {
                    calculatedtime = dataList.get(adapterPos).getEstimatedTime() + dataList.get(adapterPos).getOrderPreparationTime();
                }
            } else {
                calculatedtime = dataList.get(adapterPos).getOrderPreparationTime();
            }
        }


        if (!Utils.calculateHourMinsFrmMins(calculatedtime).isEmpty() &&
                dataList.get(adapterPos).getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE && !Dependencies.isLaundryApp()
                && UIManager.isShowDeliveryTimeEnable()) {
            viewHolder.tvDeliveryTime.setVisibility(View.VISIBLE);
            viewHolder.tvDeliveryTime.setText(Utils.calculateHourMinsFrmMins(calculatedtime));
        } else {
            viewHolder.tvDeliveryTime.setVisibility(View.GONE);

        }


        if (dataList.get(adapterPos).getLogo() != null && !dataList.get(adapterPos).getLogo().isEmpty()) {
            try {
                viewHolder.ivImage.post(new Runnable() {
                    @Override
                    public void run() {
                        new GlideUtil.GlideUtilBuilder(viewHolder.ivImage)
                                .setLoadItem(dataList.get(adapterPos).getLogo())
//                                .setTransformation(new RoundedCorners(7))
                                .setPlaceholder(R.drawable.ic_plcaeholder_marketplace_new)
                                .build();

                    }
                });
            } catch (Exception e) {

                Utils.printStackTrace(e);
            }
        } else {
//            viewHolder.ivImage.setImageDrawable(null);
//            viewHolder.ivImage.setImageResource(R.drawable.ic_image_placeholder);
            viewHolder.ivImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_plcaeholder_marketplace_new));
            viewHolder.ivImage.setBackground(null);
        }

        if ((!UIManager.getIsReviewRatingRequired() || dataList.get(adapterPos).getStoreRating().floatValue() <= 0)) {
            viewHolder.llRatingImageLayout.setVisibility(View.GONE);
        } else {
            viewHolder.llRatingImageLayout.setVisibility(View.VISIBLE);
            viewHolder.tvAverageRatingImage.setText(dataList.get(adapterPos).getStoreRating().floatValue() + "");
            viewHolder.tvAverageRatingImage.setTypeface(viewHolder.tvAverageRatingImage.getTypeface(), Typeface.BOLD);
        }

        if (StorefrontCommonData.getAppConfigurationData().getShowDeliveryChargeOnListPage() == 1 && dataList.get(adapterPos).getDeliveryCharge() > 0) {

            viewHolder.tvDeliveryCharge.setText(StorefrontCommonData.getTerminology().getDeliveryCharge() + " " + UIManager.getCurrency(Utils.getCurrencySymbol(dataList.get(adapterPos).getPaymentSettings()) + Utils.getDoubleTwoDigits(dataList.get(adapterPos).getDeliveryCharge())));

            viewHolder.tvDeliveryCharge.setVisibility(View.VISIBLE);
        } else if (StorefrontCommonData.getAppConfigurationData().getShowDeliveryChargeOnListPage() == 1 && dataList.get(adapterPos).getDeliveryCharge() == 0) {
            viewHolder.tvDeliveryCharge.setVisibility(View.VISIBLE);
            viewHolder.tvDeliveryCharge.setText(StorefrontCommonData.getString(activity, R.string.free_delivery).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));

        } else {
            viewHolder.tvDeliveryCharge.setVisibility(View.GONE);

        }

        if (activity instanceof MarketplaceSearchActivity)
            viewHolder.tvDeliveryCharge.setVisibility(View.GONE);

        String deliverByText;
        if (dataList.get(adapterPos).getMerchantAsDeliveryManager() == 1) {
            deliverByText = StorefrontCommonData.getTerminology().getMerchant();
        } else {
            deliverByText = StorefrontCommonData.getTerminology().getAgent();
        }

        viewHolder.tvDeliverBy.setText(StorefrontCommonData.getString(activity, R.string.delivery_by).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()) + " " + deliverByText);

        if (StorefrontCommonData.getAppConfigurationData().getDeliveryByMerchant() == 1) {
            viewHolder.tvDeliverBy.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvDeliverBy.setVisibility(View.GONE);

        }

        if (dataList.get(adapterPos).getIsStorefrontOpened() == 0) {
            ColorMatrix matrix = new ColorMatrix();
            if (dataList.get(adapterPos).getScheduledTask() == 1) {
                viewHolder.tvRestaurantOpenStatus.setText(StorefrontCommonData.getString(activity, R.string.pre_order));
                Utils.setTextColor(activity, R.color.textcolor_preorder, viewHolder.tvRestaurantOpenStatus);

                matrix.setSaturation(1);
            } else {
                viewHolder.tvRestaurantOpenStatus.setText(StorefrontCommonData.getString(activity, R.string.closed));
                Utils.setTextColor(activity, R.color.textcolor_closed, viewHolder.tvRestaurantOpenStatus);

                matrix.setSaturation(0);
            }
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            viewHolder.ivImage.setColorFilter(filter);

        } else {
            viewHolder.tvRestaurantOpenStatus.setText(StorefrontCommonData.getTerminology().getOrderOnline());
            Utils.setTextColor(activity, R.color.textcolor_open, viewHolder.tvRestaurantOpenStatus);

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(1);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            viewHolder.ivImage.setColorFilter(filter);
        }


        viewHolder.ivSponsoredRibbon.setVisibility(dataList.get(adapterPos).getIs_sponsored() == 1 ? View.VISIBLE : View.GONE);
        viewHolder.tvMerchantDiscount.setVisibility(dataList.get(adapterPos).merchantDiscount != null && dataList.get(adapterPos).merchantDiscount > 0 ? View.VISIBLE : View.GONE);
        viewHolder.tvMerchantDiscount.setText(dataList.get(adapterPos).getMerchantDiscount() + StorefrontCommonData.getString(activity, R.string.discount_percent_off));
        viewHolder.tvRestaurantCategories.setText(dataList.get(adapterPos).getBusinessCategoriesName());
        viewHolder.tvRestaurantCategories.setVisibility(dataList.get(adapterPos).getBusinessCategoriesName().isEmpty() ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 1;
    }

    private void openRestaurantActivity(final int adapterPos) {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.RESTAURANT_CLICK, dataList.get(adapterPos).getStoreName() + "");
//        PaymentMethodsClass.clearPaymentHashMaps();

        StorefrontCommonData.getUserData().getData().getVendorDetails().setUserId(dataList.get(adapterPos).getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setUserId(dataList.get(adapterPos).getStorefrontUserId());
        StorefrontCommonData.getFormSettings().setMerchantMinimumOrder(dataList.get(adapterPos).getMerchantMinimumOrder());
        StorefrontCommonData.getFormSettings().setMinimum_self_pickup_amount(dataList.get(adapterPos).getMinimumSelfPickupAmount());
        dataList.get(adapterPos).getRecurrinTask();

        if (StorefrontCommonData.getFormSettings().getDisplayMerchantDetailsPage() == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(STOREFRONT_DATA, dataList.get(adapterPos));
            bundle.putInt(STOREFRONT_DATA_ITEM_POS, adapterPos);

            if (activity instanceof RestaurantListingActivity && fragment != null && fragment instanceof RestaurantListFragment) {
                bundle.putDouble(PICKUP_LATITUDE, ((RestaurantListingActivity) activity).latitude);
                bundle.putDouble(PICKUP_LONGITUDE, ((RestaurantListingActivity) activity).longitude);
                bundle.putString(PICKUP_ADDRESS, ((RestaurantListingActivity) activity).address);
                bundle.putString(ADMIN_CATALOGUE_SELECTED_CATEGORIES, "");
                if (((RestaurantListFragment) fragment).getBusinessCategory() != null
                        && !((RestaurantListFragment) fragment).isAllBusinessCategory())
                    bundle.putInt(BUSINESS_CATEGORY_ID, ((RestaurantListFragment) fragment).getBusinessCategory());
                Dependencies.setIsPreorderSelecetedForMenu(false);
            } else if (activity instanceof MarketplaceSearchActivity) {
                bundle.putDouble(PICKUP_LATITUDE, ((MarketplaceSearchActivity) activity).pickupLatitude);
                bundle.putDouble(PICKUP_LONGITUDE, ((MarketplaceSearchActivity) activity).pickupLongitude);
                bundle.putString(PICKUP_ADDRESS, ((MarketplaceSearchActivity) activity).pickupAddress);
                bundle.putString(DATE_TIME, ((MarketplaceSearchActivity) activity).getPreorderDateTime());
            }

            Intent intent = new Intent(activity, HomeActivity.class);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, OPEN_HOME_ACTIVITY);
            AnimationUtils.forwardTransition(activity);

        } else {
            Double latitude = 0.0, longitude = 0.0;
            String adminSelectedCategories = "", address = "";
            Integer businessCategoryId = null;
            String preOrderDateTime = null;

            if (activity instanceof RestaurantListingActivity && fragment != null && fragment instanceof RestaurantListFragment) {
                latitude = ((RestaurantListingActivity) activity).latitude;
                longitude = ((RestaurantListingActivity) activity).longitude;
                address = ((RestaurantListingActivity) activity).address;
//                adminSelectedCategories = ((RestaurantListFragment) fragment).getCatalogId();
                if (((RestaurantListFragment) fragment).getBusinessCategory() != null
                        && !((RestaurantListFragment) fragment).isAllBusinessCategory())
                    businessCategoryId = ((RestaurantListFragment) fragment).getBusinessCategory();
                Dependencies.setIsPreorderSelecetedForMenu(false);
            } else if (activity instanceof MarketplaceSearchActivity) {
                latitude = ((MarketplaceSearchActivity) activity).pickupLatitude;
                longitude = ((MarketplaceSearchActivity) activity).pickupLongitude;
                address = ((MarketplaceSearchActivity) activity).pickupAddress;
                preOrderDateTime = ((MarketplaceSearchActivity) activity).getPreorderDateTime();
            }

            if (dataList.get(adapterPos) != null && Dependencies.getSelectedProductsArrayList().size() > 0) {
                if (dataList.get(adapterPos).getStorefrontUserId().equals(Dependencies.getSelectedProductsArrayList().get(0).getUserId())) {
                    for (int j = 0; j < Dependencies.getSelectedProductsArrayList().size(); j++) {
                        Dependencies.getSelectedProductsArrayList().get(j).setStorefrontData(dataList.get(adapterPos));
                    }
                }
            }
            if (dataList.get(adapterPos) != null) {
                if (UIManager.isMenuEnabled() && dataList.get(adapterPos).getIsMenuEnabled() &&
                        dataList.get(adapterPos).getScheduledTask() == 1 &&
                        dataList.get(adapterPos).getIsStorefrontOpened() == 0) {
                    final String finalAdminSelectedCategories = adminSelectedCategories;
                    final String finalAddress = address;
                    final Double finalLatitude = latitude;
                    final Double finalLongitude = longitude;
                    final Integer finalbusinessCategoryId = businessCategoryId;
                    new SelectPreOrderTimeDialog(activity, new SelectPreOrderTimeDialog.OnPreOrderTimeSelectionListener() {
                        @Override
                        public void onDateTimeSelected(String dateTime) {
                            StorefrontConfig.getAppCatalogueV2(activity, dataList.get(adapterPos).getStoreName(),
                                    dataList.get(adapterPos).getLogo(),
                                    new LatLng(Double.valueOf(dataList.get(adapterPos).getLatitude()),
                                            Double.valueOf(dataList.get(adapterPos).getLongitude())),
                                    new LatLng(finalLatitude, finalLongitude),
                                    finalAddress,
                                    dataList.get(adapterPos),
                                    finalAdminSelectedCategories,
                                    dateTime, finalbusinessCategoryId);
                        }
                    }).setStorefrontData(dataList.get(adapterPos))
                            .show();
//                    selectPreOrderTime(dataList.get(adapterPos), latitude, longitude, address, adminSelectedCategories);
                } else {
                    if (preOrderDateTime == null)
                        StorefrontConfig.getAppCatalogueV2(activity, dataList.get(adapterPos).getStoreName(),
                                dataList.get(adapterPos).getLogo(),
                                new LatLng(Double.valueOf(dataList.get(adapterPos).getLatitude()),
                                        Double.valueOf(dataList.get(adapterPos).getLongitude())),
                                new LatLng(latitude, longitude), address, dataList.get(adapterPos),
                                adminSelectedCategories, businessCategoryId, false, 0);
                    else
                        StorefrontConfig.getAppCatalogueV2(activity, dataList.get(adapterPos).getStoreName(),
                                dataList.get(adapterPos).getLogo(),
                                new LatLng(Double.valueOf(dataList.get(adapterPos).getLatitude()),
                                        Double.valueOf(dataList.get(adapterPos).getLongitude())),
                                new LatLng(latitude, longitude), address, dataList.get(adapterPos),
                                adminSelectedCategories, preOrderDateTime, businessCategoryId);
                }
//                StorefrontConfig.getAppCatalogueV2(activity, dataList.get(adapterPos).getStoreName(), dataList.get(adapterPos).getLogo(), new LatLng(Double.valueOf(dataList.get(adapterPos).getLatitude()), Double.valueOf(dataList.get(adapterPos).getLongitude())), new LatLng(latitude, longitude), address, dataList.get(adapterPos), adminSelectedCategories);
            } else
                StorefrontConfig.getAppCatalogueV2(activity);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llParentView;
        LinearLayout llRatingImageLayout;
        ImageView ivWishlistUnselected, ivWishlistSelected, ivImage, ivAverageRatingStar, ivSponsoredRibbon;
        TextView tvCustomTag, tvRestaurantTimings, tvRestaurantName, tvRestaurantAddress, tvAverageRatingImage,
                tvRestaurantOpenStatus, tvMerchantDiscount, tvRestaurantCategories, tvDeliveryTime, tvDeliveryCharge, tvDeliverBy;

        public ViewHolder(View itemView) {
            super(itemView);
            ivWishlistUnselected = itemView.findViewById(R.id.iv_wishlist_unselected);
            ivWishlistSelected = itemView.findViewById(R.id.iv_wishlist_selected);

            llParentView = itemView.findViewById(R.id.llParentView);
            tvDeliveryTime = itemView.findViewById(R.id.tvDeliveryTime);
            ivSponsoredRibbon = itemView.findViewById(R.id.ivSponsoredRibbon);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvCustomTag = itemView.findViewById(R.id.tvCustomTag);
            tvRestaurantAddress = itemView.findViewById(R.id.tvRestaurantAddress);
            tvRestaurantOpenStatus = itemView.findViewById(R.id.tvRestaurantOpenStatus);
            tvDeliveryCharge = itemView.findViewById(R.id.tvDeliveryCharge);
            tvDeliverBy = itemView.findViewById(R.id.tvDeliverBy);
            tvRestaurantTimings = itemView.findViewById(R.id.tvRestaurantTimings);

            llRatingImageLayout = itemView.findViewById(R.id.llRatingImageLayout);
            ivAverageRatingStar = itemView.findViewById(R.id.ivAverageRatingStar);
            tvAverageRatingImage = itemView.findViewById(R.id.tvAverageRatingImage);
            tvMerchantDiscount = itemView.findViewById(R.id.tvMerchantDiscount);
            tvRestaurantCategories = itemView.findViewById(R.id.tvRestaurantCategories);
        }
    }


}