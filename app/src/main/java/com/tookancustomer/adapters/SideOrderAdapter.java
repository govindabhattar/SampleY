package com.tookancustomer.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;
import com.tookancustomer.DatesOnCalendarActivity;
import com.tookancustomer.NLevelWorkFlowActivity;
import com.tookancustomer.ProductCustomisationActivity;
import com.tookancustomer.ProductDetailActivity;
import com.tookancustomer.ProductDetailPDPView2Activity;
import com.tookancustomer.R;
import com.tookancustomer.ScheduleTimeActivity;
import com.tookancustomer.SearchProductActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.GenericMessageDialog;
import com.tookancustomer.dialog.ImageVideoScreenDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.tookancustomer.appdata.Codes.Request.OPEN_CUSTOMISATION_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_PRODUCT_DETAILS_SCREEN;
import static com.tookancustomer.appdata.Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY;
import static com.tookancustomer.appdata.Constants.NLevelLayoutType.LIST_LAYOUT;
import static com.tookancustomer.appdata.Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT;
import static com.tookancustomer.appdata.Keys.Extras.IS_START_TIME;
import static com.tookancustomer.appdata.Keys.Extras.KEY_ITEM_POSITION;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_CATEGORY_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_CATALOGUE_DATA;
import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_DETAIL_DATA;
import static com.tookancustomer.appdata.Keys.Extras.SELECTED_DATE;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by cl-macmini-25 on 19/12/16.
 */

public class SideOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TerminologyStrings {
    private Activity activity;
    private ArrayList<Datum> dataList;
    private String startDate;
    private int currentPosition = 0;
    private Callback callback;
    private double totalAmount = 0;
    /**
     * For ECOM flow
     * Seller view with seller name and price
     * and other sellers view
     */
    private String categoryId;
    private FragmentManager fragmentManager;

    public SideOrderAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<Datum> dataList, Callback callback) {
        this.activity = activity;
        this.dataList = dataList;
        this.callback = callback;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(Constants.NLevelLayoutType.getLayoutModeByValue(activity, viewType), parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.llNLevelParent.setClickable(false);
            Utils.setVisibility(View.GONE, viewHolder.tvDescription1, viewHolder.tvDescription2, viewHolder.tvDescription3);
            Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
            Utils.setVisibility(View.GONE, viewHolder.rlNLevelImageParent);

            //TODO
//            if ((dataList.get(position).getStorefrontData().getIsReviewRatingEnabled() == 0 || dataList.get(position).getProductRating().floatValue() <= 0)) {
//                viewHolder.llRatingImageLayout.setVisibility(View.GONE);
//            } else {
//                viewHolder.llRatingImageLayout.setVisibility(View.VISIBLE);
//                viewHolder.tvAverageRatingImage.setText(dataList.get(position).getProductRating().floatValue() + "");
//            }

            if (dataList.get(position).getIsVeg() != null) {
                viewHolder.ivFoodType.setVisibility(StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 ? View.VISIBLE : View.GONE);
//                viewHolder.ivFoodTypeNoImage.setVisibility(StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 && dataList.get(position).getUserId() == 65262 ? View.VISIBLE : View.GONE);

                viewHolder.ivFoodTypeNoImage.setVisibility(View.GONE);
                viewHolder.ivFoodType.setImageResource(dataList.get(position).getIsVeg() == 1 ? R.drawable.veg : R.drawable.nonveg);
                viewHolder.ivFoodTypeNoImage.setImageResource(dataList.get(position).getIsVeg() == 1 ? R.drawable.veg : R.drawable.nonveg);
            } else {
                viewHolder.ivFoodType.setVisibility(View.GONE);
                //Here false will replace by if image to be shown or not
                if (StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 && dataList.get(position).getLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue && false) {
                    viewHolder.ivFoodTypeNoImage.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.ivFoodTypeNoImage.setVisibility(View.GONE);
                }
            }
            if (dataList.get(position).getLayoutData() != null) {
                if (dataList.get(position).getLayoutData().getImages().size() > 0) {
                    if (dataList.get(position).getLayoutData().getImages().get(0).getSize() != (Constants.NLevelImageStyles.NONE.appStyleValue)) {
                        Utils.setVisibility(View.VISIBLE, viewHolder.rlNLevelImageParent);
                        try {
                            viewHolder.ivNLevelImage.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (dataList.get(position).getThumbUrl().isEmpty()) {
                                        viewHolder.ivNLevelImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                                    } else {
                                        try {


                                            new GlideUtil.GlideUtilBuilder(viewHolder.ivNLevelImage)
                                                    .setPlaceholder(R.drawable.ic_loading_image)
                                                    .setError(R.drawable.ic_image_placeholder)
                                                    .setFallback(R.drawable.ic_image_placeholder)
                                                    .setLoadItem(dataList.get(position).getImageUrl())
                                                    .setThumbnailItem(dataList.get(position).getThumbUrl())
                                                    .setTransformation(new RoundedCorners(4))
                                                    .setCenterCrop(true)
                                                    .build();


                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            });

                        } catch (Exception e) {

                               Utils.printStackTrace(e);
                        }
                    }
                }

                for (int i = 0; i < dataList.get(position).getLayoutData().getLines().size(); i++) {
                    switch (i) {
                        case 0:
                            if (dataList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription1);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription1);
                            }
                            viewHolder.tvDescription1.setText(dataList.get(position).getLayoutData().getLines().get(i).getData());
                            viewHolder.tvDescription1.setTypeface(viewHolder.tvDescription1.getTypeface(), Font.BOLD);
                            break;
                        case 1:
                            if (dataList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription2);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription2);
                            }
                            viewHolder.tvDescription2.setText(dataList.get(position).getLayoutData().getLines().get(i).getData());
                            //  viewHolder.tvDescription2.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, dataList.get(position).getLayoutData().getLines().get(i).getStyle())));
                            break;
                        case 2:
                            float productDiscount = dataList.get(position).getProductDiscount();
                            if (dataList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()
                                    || (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && dataList.get(position).getPrice().doubleValue() <= 0)
                            ) {
                                Utils.setVisibility(StorefrontCommonData.getFormSettings().getPdpView() == 1 ? View.GONE : View.INVISIBLE, viewHolder.tvDescription3);
                            } else {

                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription3);
                                if (productDiscount > 0) {
                                    Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription3Prime);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.tvDescriptionDiscount);
//                                    viewHolder.tvDescription3Prime.setPaintFlags(viewHolder.tvDescription3Prime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    viewHolder.tvDescription3Prime.setText(((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(dataList.get(position).getOriginalPrice())))));
                                    viewHolder.tvDescriptionDiscount.setText(getDiscountDescription(productDiscount));
                                }
                                viewHolder.tvDescription3.setText(UIManager.getCurrency(dataList.get(position).getLayoutData().getLines().get(i).getData()));
                            }

                            //viewHolder.tvDescription3.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, dataList.get(position).getLayoutData().getLines().get(i).getStyle())));
                            break;

                    }
                }

                if (StorefrontCommonData.getFormSettings().getPdpView() == 1) {
                    viewHolder.llNLevelParent.setEnabled(true);
                    Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);

                } else {
                    viewHolder.llNLevelParent.setEnabled(false);
                    for (int i = 0; i < dataList.get(position).getLayoutData().getButtons().size(); i++) {
                        switch (i) {
                            case 0:
                                int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, dataList.get(position).getLayoutData().getButtons().get(0).getType());
                                if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(true);
                                } else if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(false);
//                                    setParentLayoutVisibility(viewHolder);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.linearLayoutQuantitySelector);
                                    viewHolder.linearLayoutQuantitySelector.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(false);
//                                    setParentLayoutVisibility(viewHolder);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.tvSingleSelectionButton);
                                    viewHolder.tvSingleSelectionButton.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(true);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.ivForwardArrowButton);
                                    viewHolder.ivForwardArrowButton.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                }
                                break;
                        }
                    }
                }

                if (StorefrontCommonData.getFormSettings().getPdpView() != 1) {
                    viewHolder.rlNLevelImageParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dataList.get(position).getMultiImageUrl().size() > 0 ||
                                    dataList.get(position).getMultiVideoUrl().size() > 0) {
                                ImageVideoScreenDialog viewImagesDialogProducts = new ImageVideoScreenDialog(dataList.get(position), activity);
                                viewImagesDialogProducts.show(fragmentManager, "");
                            }
//                                new ViewImagesDialogProducts.Builder(activity)
//                                        .images(dataList.get(position).getMultiImageUrl())
//                                        .title(dataList.get(position).getName()).position(0).build().show();
                        }
                    });
                }


            }

            final int[] selectedQuantity = {dataList.get(position).getSelectedQuantity()};

            if (selectedQuantity[0] > 0) {
                viewHolder.textViewQuantity.setText(selectedQuantity[0] + "");
                if (dataList.get(position).getLayoutData() != null && dataList.get(position).getLayoutData().getButtons().size() > 0) {
                    int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, dataList.get(position).getLayoutData().getButtons().get(0).getType());
                    if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                        viewHolder.rlAddItem.setVisibility(View.VISIBLE);
                        viewHolder.rlRemoveItem.setVisibility(View.VISIBLE);
                        viewHolder.textViewQuantity.setVisibility(View.VISIBLE);
                    } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                        try {
                            viewHolder.tvSingleSelectionButton.setText(dataList.get(position).getStorefrontData().getButtons().getButtonNames().getRemove());
                        } catch (Exception e) {

                               Utils.printStackTrace(e);
                        }
                    }
                }

            } else {
                if (dataList.get(position).getLayoutData() != null && dataList.get(position).getLayoutData().getButtons().size() > 0) {
                    int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, dataList.get(position).getLayoutData().getButtons().get(0).getType());
                    if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                        viewHolder.rlAddItem.setVisibility(View.VISIBLE);
                        viewHolder.rlRemoveItem.setVisibility(View.GONE);
                        viewHolder.textViewQuantity.setVisibility(View.GONE);
                    } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                        try {
                            viewHolder.tvSingleSelectionButton.setText(dataList.get(position).getStorefrontData().getButtons().getButtonNames().getAdd());
                        } catch (Exception e) {

                               Utils.printStackTrace(e);
                        }
                    }
                }
            }

            viewHolder.rlAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StorefrontCommonData.getFormSettings().getProductView() == 0 &&
                            Dependencies.getSelectedProductsArrayList().size() > 0 &&
                            !Dependencies.getSelectedProductsArrayList().get(0).getUserId().equals(dataList.get(position).getUserId())) {
                        new OptionsDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.sure_to_select_this_product_will_delete_previous_data).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()).replace(CART, StorefrontCommonData.getTerminology().getCart(false))).listener(new OptionsDialog.Listener() {
                            @Override
                            public void performPositiveAction(int purpose, Bundle backpack) {

                                Dependencies.setSelectedProductsArrayList(
                                        new ArrayList<Datum>());
                                addCartItem(selectedQuantity, position, viewHolder);
                            }

                            @Override
                            public void performNegativeAction(int purpose, Bundle backpack) {
                            }
                        }).build().show();
                    } else if (Dependencies.getSelectedProductsArrayList().size() > 0 && !Dependencies.getSelectedProductsArrayList().get(0).getProductId().equals(dataList.get(position).getProductId()) && dataList.get(position).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                        Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.can_avail_only_one_product_at_time).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct(false)));
                    } else {
                        StorefrontCommonData.getFormSettings().setUserId(dataList.get(position).getUserId());
                        addCartItem(selectedQuantity, position, viewHolder);
                    }
                }
            });


            viewHolder.rlRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    removeFromCart(selectedQuantity, position, viewHolder);
                }
            });

            viewHolder.tvSingleSelectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedQuantity[0] == 0) {
                        viewHolder.rlAddItem.performClick();
                    } else {
                        viewHolder.rlRemoveItem.performClick();
                    }
                }
            });


            viewHolder.llNLevelParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String myJson = gson.toJson(dataList.get(position));
                    Intent intent;

                    if (StorefrontCommonData.getFormSettings().getPdpViewTemplates() == 0) {
                        intent = new Intent(activity, ProductDetailActivity.class);
                    } else {
                        intent = new Intent(activity, ProductDetailPDPView2Activity.class);
                    }

                    intent.putExtra(PRODUCT_DETAIL_DATA, myJson);
                    bundle.putInt(KEY_ITEM_POSITION, position);

                    if (activity instanceof NLevelWorkFlowActivity) {
                        bundle.putDouble(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
                        bundle.putDouble(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
                        bundle.putString(PICKUP_ADDRESS, ((NLevelWorkFlowActivity) activity).pickAddress);
                    } else if (activity instanceof SearchProductActivity) {
                        bundle.putDouble(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
                        bundle.putDouble(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
                        bundle.putString(PICKUP_ADDRESS, ((SearchProductActivity) activity).pickupAddress);
                    }

                    if (Dependencies.isEcomApp()) {
                        bundle.putString(PARENT_CATEGORY_ID, categoryId);
                    }
                    intent.putExtras(bundle);
                    activity.startActivityForResult(intent, OPEN_PRODUCT_DETAILS_SCREEN);
                    AnimationUtils.forwardTransition(activity);


                }
            });


            viewHolder.tvOutOfStockText.setVisibility(dataList.get(position).getAvailableQuantity() == 0 &&
                    dataList.get(position).getInventoryEnabled() == 1 ? View.VISIBLE : View.GONE);

            if (dataList.get(position).getMinProductquantity() > 1) {
                viewHolder.tvMinProductQuan.setVisibility(View.VISIBLE);
                viewHolder.tvMinProductQuan.setText(StorefrontCommonData.getString(activity, R.string.text_minimum_product_quantity) +
                        dataList.get(position).getMinProductquantity());
            } else {
                viewHolder.tvMinProductQuan.setVisibility(View.GONE);
            }

//            if (Constants.NLevelLayoutType.getLayoutModeByValue(activity, getParentLayout(position)) == LIST_LAYOUT.layoutMode) {
//                viewHolder.llNLevelParent.setForeground(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? ContextCompat.getDrawable(activity, R.drawable.overlay_out_of_stock_product) : null);
//            } else {
//                viewHolder.ivNLevelImage.setForeground(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? ContextCompat.getDrawable(activity, R.drawable.overlay_out_of_stock_product) : null);
//                viewHolder.rlQuantitySelector.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
//            }


            viewHolder.layoutProductDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!dataList.get(position).getLongDescription().isEmpty()) {
                        new GenericMessageDialog.Builder(activity)
                                .title(dataList.get(position).getName())
                                .message(dataList.get(position).getLongDescription()).build().show();

                    } else {
                        return;
                    }
                }
            });


            viewHolder.parentNameTV.setText(dataList.get(position).getParentCategoryName());

            if (activity instanceof ProductCustomisationActivity) {
                if (dataList.get(position).getParentCategoryId() == dataList.get(position).getPreviousCategoryID()) {
                    viewHolder.parentNameTV.setVisibility(View.GONE);
                } else {
                    viewHolder.parentNameTV.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void removeFromCart(final int[] selectedQuantity, final int position, final ViewHolder viewHolder) {


        if (selectedQuantity[0] <= 0) {
            dataList.get(position).setSelectedQuantity(0);
        } else {
            if (dataList.get(position).getMinProductquantity() > 1) {

                if (selectedQuantity[0] <= dataList.get(position).getMinProductquantity()) {

                    String message = (StorefrontCommonData.getString(activity, R.string.text_quantity_cannot_be_less_than_for_product) + StorefrontCommonData.getString(activity, R.string.text_move_to_cart_to_make_further_changes))
                            .replace(NAME, dataList.get(position).getName())
                            .replace(QUANTITY, dataList.get(position).getMinProductquantity() + "")
                            .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())
                            .replace(CART, StorefrontCommonData.getTerminology().getCart());

                    Utils.showToast(activity, message);
                    return;
                } else
                    dataList.get(position).setSelectedQuantity(selectedQuantity[0] - 1);
            } else
                dataList.get(position).setSelectedQuantity(selectedQuantity[0] - 1);
        }


        totalAmount = totalAmount - dataList.get(position).getPrice().doubleValue();
        notifyItemChanged(position);

        callback.updateItemTotalPrice(totalAmount);
    }

    private Spanned getStrikeLineText(String data) {
        String text = "<strike><font color=\'#ea4444\'>" + data + "</font></strike>";
        return Html.fromHtml(text);
    }

    private String getDiscountDescription(final float productDiscount) {
        return Utils.getDoubleTwoDigits(productDiscount) + activity.getString(R.string.discount_percent_off);
    }

    private String getDiscountedPrice(final float productDiscount, final String price) {
        double convertedPrice = Double.parseDouble(price);
        return Utils.getDoubleTwoDigits((convertedPrice - (convertedPrice * (productDiscount / 100))));
    }

    public void setItemCustomisation(int position) {
        Intent intent = new Intent(activity, ProductCustomisationActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, position);
        if (activity instanceof NLevelWorkFlowActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
        } else if (activity instanceof SearchProductActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
        }
        intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
        activity.startActivityForResult(intent, OPEN_CUSTOMISATION_ACTIVITY);
    }

//    private void setParentLayoutVisibility(ViewHolder viewHolder) {
//        if (StorefrontCommonData.getFormSettings().getProductView() == 0) {
//            viewHolder.llNLevelParent.setEnabled(false);
//        } else {
//            viewHolder.llNLevelParent.setEnabled(true);
//        }
//    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getParentLayout(position);
    }

    private int getParentLayout(int position) {

        return LIST_LAYOUT.layoutValue;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llRatingImageLayout;
        private TextView tvAverageRatingImage, parentNameTV;

        private LinearLayout llNLevelParent;
        private RelativeLayout rlNLevelImageParent, layoutProductDetails;
        private ImageView ivNLevelImage, ivFoodType, ivFoodTypeNoImage;

        private TextView tvDescription1, tvDescription2,
                tvDescription3, tvDescription3Prime,
                tvDescriptionDiscount, tvMinProductQuan;

        private RelativeLayout rlQuantitySelector;
        private LinearLayout linearLayoutQuantitySelector;
        private RelativeLayout rlRemoveItem, rlAddItem;
        private TextView textViewQuantity, tvSingleSelectionButton, tvOutOfStockText;
        private ImageView ivForwardArrowButton;

        ViewHolder(View itemView) {
            super(itemView);
            llNLevelParent = itemView.findViewById(R.id.llNLevelParent);
            rlNLevelImageParent = itemView.findViewById(R.id.rlNLevelImageParent);
            layoutProductDetails = itemView.findViewById(R.id.layoutProductDetails);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);
            ivFoodTypeNoImage = itemView.findViewById(R.id.ivFoodTypeNoImage);
            ivFoodType = itemView.findViewById(R.id.ivFoodType);
            parentNameTV = itemView.findViewById(R.id.parentNameTV);

            tvDescription1 = itemView.findViewById(R.id.tvDescription1);
            tvDescription2 = itemView.findViewById(R.id.tvDescription2);
            tvDescription3 = itemView.findViewById(R.id.tvDescription3);
            tvDescription3Prime = itemView.findViewById(R.id.tvDescription3_);
            tvDescriptionDiscount = itemView.findViewById(R.id.tvDescriptionDiscount);
            tvMinProductQuan = itemView.findViewById(R.id.tvMinProductQuan);

            rlQuantitySelector = itemView.findViewById(R.id.rlQuantitySelector);
            linearLayoutQuantitySelector = itemView.findViewById(R.id.linearLayoutQuantitySelector);
            rlRemoveItem = itemView.findViewById(R.id.rlRemoveItem);
            rlAddItem = itemView.findViewById(R.id.rlAddItem);
            tvOutOfStockText = itemView.findViewById(R.id.tvOutOfStockText);
            tvOutOfStockText.setVisibility(View.GONE);
            tvOutOfStockText.setText(StorefrontCommonData.getTerminology().getOutOfStock());
            tvSingleSelectionButton = itemView.findViewById(R.id.tvSingleSelectionButton);
            tvSingleSelectionButton.setVisibility(View.GONE);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            ivForwardArrowButton = itemView.findViewById(R.id.ivForwardArrowButton);

            llRatingImageLayout = itemView.findViewById(R.id.llRatingImageLayout);
            tvAverageRatingImage = itemView.findViewById(R.id.tvAverageRatingImage);
        }
    }

    public void addCartItem(int[] selectedQuantity, int position, ViewHolder viewHolder) {
        if (dataList.get(position).getMaxProductquantity() == 0 || dataList.get(position).getSelectedQuantity() < dataList.get(position).getMaxProductquantity()) {

            if (dataList.get(position).getInventoryEnabled() == 0 || (dataList.get(position).getSelectedQuantity() < dataList.get(position).getAvailableQuantity() && dataList.get(position).getInventoryEnabled() == 1)) {

                if (dataList.get(position).getCustomizeItem().size() > 0) {
                    setItemCustomisation(position);
                    return;
                }

                if (selectedQuantity[0] == 0 && dataList.get(position).getStorefrontData().getBusinessType() ==
                        Constants.BusinessType.SERVICES_BUSINESS_TYPE) {


                    if (dataList.get(position).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                            && (Constants.ProductsUnitType.getUnitType(dataList.get(position).getUnitType()) == Constants.ProductsUnitType.FIXED)
                            && dataList.get(position).getEnableTookanAgent() == 0) {

                        openDatePicker(position);

                    } else {

                        if (dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                && (Constants.ProductsUnitType.getUnitType(dataList.get(position).getUnitType()) == Constants.ProductsUnitType.PER_DAY)
                                && UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                            Intent intent = new Intent(activity, DatesOnCalendarActivity.class);
                            intent.putExtra(KEY_ITEM_POSITION, position);
                            intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
                            intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(IS_START_TIME, true);
                            intent.putExtra(SELECTED_DATE, "");
                            activity.startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                        } else {
                            Intent intent = new Intent(activity, ScheduleTimeActivity.class);
                            intent.putExtra(KEY_ITEM_POSITION, position);
                            intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
                            intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
                            intent.putExtra(IS_START_TIME, true);
                            intent.putExtra(SELECTED_DATE, "");
                            activity.startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                        }

                    }
                } else {
                    addToCart(selectedQuantity, position);
                }
            } else {
                Utils.showToast(activity, StorefrontCommonData.getString(activity, R.string.order_quantity_limited));
            }
        } else {
            String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                    .replace(NAME, dataList.get(position).getName())
                    .replace(QUANTITY, dataList.get(position).getMaxProductquantity() + "")
                    .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
            Utils.snackBar(activity, message);

        }
    }

    public void addToCart(int[] selectedQuantity, int position) {
        if (dataList.get(position).getMinProductquantity() > 1 &&
                selectedQuantity[0] < dataList.get(position).getMinProductquantity()) {
            selectedQuantity[0] = selectedQuantity[0] + dataList.get(position).getMinProductquantity();

        } else {
            selectedQuantity[0]++;
        }
        dataList.get(position).setSelectedQuantity(selectedQuantity[0]);

        totalAmount = totalAmount + dataList.get(position).getPrice().doubleValue();
        notifyItemChanged(position);


        callback.updateItemTotalPrice(totalAmount);

    }


    public void openDatePicker(int pos) {
        currentPosition = pos;
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        datePickerFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        if (datePicker.isShown()) {
            openTimePicker();
        }
    }

    public void openTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isValidTime(startDate + " " + hourOfDay + ":" + minute)) {
            startDate = startDate + " " + hourOfDay + ":" + minute;
            Date productStartDate = DateUtils.getInstance().getDateFromString(startDate, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC);
            dataList.get(currentPosition).setProductStartDate(productStartDate);
            dataList.get(currentPosition).setProductEndDate(productStartDate);

            final int[] selectedQuantity = {dataList.get(currentPosition).getSelectedQuantity()};

            addToCart(selectedQuantity, currentPosition);

        } else {
            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.invalid_selected_date));
        }
    }


    public boolean isValidTime(String date) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();

            if (DateUtils.getInstance().getDateFromString(date, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC).getTime() < calendar.getTime().getTime())
                isValidDate = false;
        }
        return isValidDate;
    }

    public void setTotalAmount(double initialTotal) {
        totalAmount = initialTotal;
    }

    public interface Callback {
        void updateItemTotalPrice(double totalPrice);

    }
}