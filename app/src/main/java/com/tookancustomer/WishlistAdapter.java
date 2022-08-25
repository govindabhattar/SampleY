package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.StorefrontConfig;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.SelectPreOrderTimeDialog;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.restorentListFragments.RestaurantListFragment;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.tookancustomer.appdata.Codes.Request.OPEN_HOME_ACTIVITY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DATE_TIME;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE_SELECTED_CATEGORIES;
import static com.tookancustomer.appdata.Keys.Extras.BUSINESS_CATEGORY_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA_ITEM_POS;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> implements TerminologyStrings {
    private Activity activity;
    private List<Datum> dataList;
    private Date preOrderDateObject;
    private Fragment fragment;
    private int calculatedtime = 0;
    private boolean wishlistSelected;
    private ColorMatrix matrix;
    private ColorMatrix blackWhitematrix;
    private ColorMatrixColorFilter colorfilter;
    private ColorMatrixColorFilter blackWhitefilter;


    public WishlistAdapter(Activity activity, List<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        this.fragment = null;
        matrix = new ColorMatrix();
        blackWhitematrix = new ColorMatrix();
        matrix.setSaturation(0);
        blackWhitematrix.setSaturation(1);
        colorfilter = new ColorMatrixColorFilter(matrix);
        blackWhitefilter = new ColorMatrixColorFilter(blackWhitematrix);
    }


    public void setNotifyAdapter(List<Datum> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_restaurant_list, viewGroup, false);
        return new WishlistAdapter.ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final int adapterPos = viewHolder.getAdapterPosition();
        viewHolder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.selector_grey));


        if (dataList.get(adapterPos).getCustom_tag_for_merchant() != null && !dataList.get(adapterPos).getCustom_tag_for_merchant().isEmpty()) {
            String mCustomTag = dataList.get(adapterPos).getCustom_tag_for_merchant();
            if (mCustomTag.length() >= 15) {
                viewHolder.tvCustomTag.setText(mCustomTag.substring(0, 13) + "...");
            } else {
                viewHolder.tvCustomTag.setText(mCustomTag);
            }

            viewHolder.tvCustomTag.setVisibility(VISIBLE);
        } else {
            viewHolder.tvCustomTag.setVisibility(GONE);
        }

        viewHolder.viewSeprator.setVisibility(GONE);
        viewHolder.viewSeprator2.setVisibility(VISIBLE);

        if (dataList.get(adapterPos).getIsWishlisted() == 1) {
            viewHolder.ivWishlistSelected.setVisibility(VISIBLE);
            viewHolder.ivWishlistUnselected.setVisibility(GONE);
        } else {
            viewHolder.ivWishlistSelected.setVisibility(GONE);
            viewHolder.ivWishlistUnselected.setVisibility(VISIBLE);
        }

        viewHolder.tvRestaurantName.setText(dataList.get(adapterPos).getStoreName());
        viewHolder.tvRestaurantName.setVisibility(viewHolder.tvRestaurantName.getText().toString().isEmpty() ? GONE : VISIBLE);
        viewHolder.tvRestaurantAddress.setText(/*dataList.get(adapterPos).getDistance() + */(!dataList.get(adapterPos).getDisplayAddress().isEmpty() ? dataList.get(adapterPos).getDisplayAddress() : ""));
        viewHolder.tvRestaurantAddress.setVisibility(viewHolder.tvRestaurantAddress.getText().toString().isEmpty() ||
                StorefrontCommonData.getAppConfigurationData().getDisplayMerchantAddress() == 0 ? GONE : VISIBLE);

        calculatedtime = 0;
        try {
            if (dataList.get(adapterPos).getShowMerchantTimings() == 1 && dataList.get(adapterPos).getStoreTimingsArr().size() != 0) {
                viewHolder.tvRestaurantTimings.setVisibility(VISIBLE);
                viewHolder.tvRestaurantTimings.setText(String.format("%s - %s", dataList.get(adapterPos).getStoreTimingsArr().get(0).getStartTime(), dataList.get(adapterPos).getStoreTimingsArr().get(0).getEndTime()));
            } else {
                viewHolder.tvRestaurantTimings.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
            viewHolder.tvDeliveryTime.setVisibility(VISIBLE);
            viewHolder.tvDeliveryTime.setText(Utils.calculateHourMinsFrmMins(calculatedtime));
        } else {
            viewHolder.tvDeliveryTime.setVisibility(GONE);
        }


        if (dataList.get(adapterPos).getLogo() != null && !dataList.get(adapterPos).getLogo().isEmpty()) {

            Glide.with(activity).load(dataList.get(adapterPos).getThumbUrl()).placeholder(R.drawable.ic_loading_image).into(viewHolder.ivImage);
            try {
                new GlideUtil.GlideUtilBuilder(viewHolder.ivImage)
                        .setLoadItem(dataList.get(adapterPos).getLogo())
//                                .setTransformation(new RoundedCorners(7))
                        .setCenterCrop(true)
                        .setPlaceholder(R.drawable.ic_loading_image)
                        .build();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        } else {

            viewHolder.ivImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_plcaeholder_marketplace_new));
            viewHolder.ivImage.setBackground(null);
        }

        if ((!UIManager.getIsReviewRatingRequired() || dataList.get(adapterPos).getStoreRating().floatValue() <= 0)) {
            viewHolder.llRatingImageLayout.setVisibility(GONE);
        } else {
            viewHolder.llRatingImageLayout.setVisibility(VISIBLE);
            viewHolder.tvAverageRatingImage.setText(dataList.get(adapterPos).getStoreRating().floatValue() + "");

        }

        if (dataList.get(adapterPos).getIsStorefrontOpened() == 0) {

            if (dataList.get(adapterPos).getScheduledTask() == 1) {
                viewHolder.tvRestaurantOpenStatus.setVisibility(VISIBLE);
                viewHolder.tvRestaurantOpenStatus.setText(StorefrontCommonData.getString(activity, R.string.pre_order));
                Utils.setTextColor(activity, R.color.textcolor_preorder, viewHolder.tvRestaurantOpenStatus);

            } else {
                viewHolder.tvRestaurantOpenStatus.setVisibility(GONE);
                viewHolder.ivImage.setColorFilter(colorfilter);

            }
        } else {
            viewHolder.tvRestaurantOpenStatus.setVisibility(GONE);
            viewHolder.ivImage.setColorFilter(blackWhitefilter);
        }


        viewHolder.ivSponsoredRibbon.setVisibility(dataList.get(adapterPos).getIs_sponsored() == 1 ? VISIBLE : GONE);
        viewHolder.tvMerchantDiscount.setVisibility(dataList.get(adapterPos).merchantDiscount != null && dataList.get(adapterPos).merchantDiscount > 0 ? VISIBLE : GONE);
        viewHolder.tvMerchantDiscount.setText(dataList.get(adapterPos).getMerchantDiscount() + StorefrontCommonData.getString(activity, R.string.discount_percent_off));
        viewHolder.tvRestaurantCategories.setText(dataList.get(adapterPos).getBusinessCategoriesName());
        viewHolder.tvRestaurantCategories.setVisibility(dataList.get(adapterPos).getBusinessCategoriesName().isEmpty() ? GONE : VISIBLE);
        viewHolder.tvRestaurantAddress.setVisibility(viewHolder.tvRestaurantAddress.getText().toString().isEmpty() || StorefrontCommonData.getAppConfigurationData().getDisplayMerchantAddress() == 0 ? GONE : VISIBLE);

        if (dataList.get(adapterPos).getIsStorefrontOpened() == 0) {
            ColorMatrix matrix = new ColorMatrix();
            if (dataList.get(adapterPos).getScheduledTask() == 1) {
                matrix.setSaturation(1);
            } else {
                matrix.setSaturation(0);
            }
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            viewHolder.ivImage.setColorFilter(filter);

        }

        if (StorefrontCommonData.getAppConfigurationData().getShowDeliveryChargeOnListPage() == 1 && dataList.get(adapterPos).getDeliveryCharge() > 0) {

            viewHolder.tvDeliveryCharge.setText(StorefrontCommonData.getTerminology().getDeliveryCharge() + " " + UIManager.getCurrency(Utils.getCurrencySymbol(dataList.get(adapterPos).getPaymentSettings()) + Utils.getDoubleTwoDigits(dataList.get(adapterPos).getDeliveryCharge())));

            viewHolder.tvDeliveryCharge.setVisibility(VISIBLE);
        } else if (StorefrontCommonData.getAppConfigurationData().getShowDeliveryChargeOnListPage() == 1 && dataList.get(adapterPos).getDeliveryCharge() == 0) {
            viewHolder.tvDeliveryCharge.setVisibility(VISIBLE);
            viewHolder.tvDeliveryCharge.setText(StorefrontCommonData.getString(activity, R.string.free_delivery).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));

        } else {
            viewHolder.tvDeliveryCharge.setVisibility(GONE);

        }

        String deliverByText;
        if (dataList.get(adapterPos).getMerchantAsDeliveryManager() == 1) {
            deliverByText = StorefrontCommonData.getTerminology().getMerchant();
        } else {
            deliverByText = StorefrontCommonData.getTerminology().getAgent();
        }

        viewHolder.tvDeliverBy.setText(StorefrontCommonData.getString(activity, R.string.delivery_by).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()) + " " + deliverByText);

        if (StorefrontCommonData.getAppConfigurationData().getDeliveryByMerchant() == 1) {
            viewHolder.tvDeliverBy.setVisibility(VISIBLE);
        } else {
            viewHolder.tvDeliverBy.setVisibility(GONE);

        }

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
            } else
                StorefrontConfig.getAppCatalogueV2(activity);
        }
    }

    public void addRemoveItem(int adapterPosition) {
        dataList.remove(adapterPosition);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llParentView;
        LinearLayout llRatingImageLayout;
        ImageView ivImage, ivAverageRatingStar, ivSponsoredRibbon, ivWishlistUnselected, ivWishlistSelected;
        TextView tvCustomTag, tvRestaurantName, tvRestaurantAddress, tvAverageRatingImage,
                tvRestaurantOpenStatus, tvRestaurantTimings, tvMerchantDiscount, tvDeliveryCharge, tvDeliverBy, tvRestaurantCategories, tvDeliveryTime, tvRestaurantDistance;
        View viewSeprator, viewSeprator2;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomTag = itemView.findViewById(R.id.tvCustomTag);

            llParentView = itemView.findViewById(R.id.llParentView);
            tvDeliveryTime = itemView.findViewById(R.id.tvDeliveryTime);
            ivSponsoredRibbon = itemView.findViewById(R.id.ivSponsoredRibbon);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivWishlistUnselected = itemView.findViewById(R.id.iv_wishlist_unselected);
            ivWishlistSelected = itemView.findViewById(R.id.iv_wishlist_selected);
            tvRestaurantName = itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantAddress = itemView.findViewById(R.id.tvRestaurantAddress);
            tvRestaurantOpenStatus = itemView.findViewById(R.id.tvRestaurantOpenStatus);
            tvDeliveryCharge = itemView.findViewById(R.id.tvDeliveryCharge);
            tvRestaurantTimings = itemView.findViewById(R.id.tvRestaurantTimings);

            llRatingImageLayout = itemView.findViewById(R.id.llRatingImageLayout);
            ivAverageRatingStar = itemView.findViewById(R.id.ivAverageRatingStar);
            tvAverageRatingImage = itemView.findViewById(R.id.tvAverageRatingImage);
            tvMerchantDiscount = itemView.findViewById(R.id.tvMerchantDiscount);
            tvRestaurantCategories = itemView.findViewById(R.id.tvRestaurantCategories);
            tvDeliverBy = itemView.findViewById(R.id.tvDeliverBy);
            viewSeprator = itemView.findViewById(R.id.view_seprator);
            viewSeprator2 = itemView.findViewById(R.id.view_seprator1);

            llParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openRestaurantActivity(getAdapterPosition());

                }
            });

            ivWishlistUnselected.setOnClickListener(view -> {
                ivWishlistSelected.setVisibility(VISIBLE);
                ivWishlistUnselected.setVisibility(GONE);
                wishlistSelected = true;
                ((WishListActivity) activity).addRemoveWishlist(wishlistSelected, getAdapterPosition());
            });

            ivWishlistSelected.setOnClickListener(view -> {
                ivWishlistSelected.setVisibility(GONE);
                ivWishlistUnselected.setVisibility(VISIBLE);
                wishlistSelected = false;

                ((WishListActivity) activity).addRemoveWishlist(wishlistSelected, getAdapterPosition());
            });
        }
    }
}
