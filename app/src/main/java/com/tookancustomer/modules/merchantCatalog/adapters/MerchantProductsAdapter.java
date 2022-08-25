package com.tookancustomer.modules.merchantCatalog.adapters;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tookancustomer.CheckOutActivity;
import com.tookancustomer.DatesOnCalendarActivity;
import com.tookancustomer.LongDescriptionActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.ProductCustomisationActivity;
import com.tookancustomer.ProductDetailActivity;
import com.tookancustomer.ProductDetailPDPView2Activity;
import com.tookancustomer.R;
import com.tookancustomer.ScheduleTimeActivity;
import com.tookancustomer.agentListing.AgentListingActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.GenericMessageDialog;
import com.tookancustomer.dialog.ImageVideoScreenDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.modules.merchantCatalog.activities.MerchantCatalogActivity;
import com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.tookancustomer.appdata.Codes.Request.OPEN_AGENT_LIST_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_CUSTOMISATION_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_PRODUCT_DETAILS_SCREEN;
import static com.tookancustomer.appdata.Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY;
import static com.tookancustomer.appdata.ExtraConstants.EXTRA_LONG_DESCRIPTION;
import static com.tookancustomer.appdata.ExtraConstants.EXTRA_PRODUCT_NAME;
import static com.tookancustomer.appdata.Keys.Extras.AGENT_ID;
import static com.tookancustomer.appdata.Keys.Extras.FROM_AGENT_SCREEN;
import static com.tookancustomer.appdata.Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT;
import static com.tookancustomer.appdata.Keys.Extras.IS_START_TIME;
import static com.tookancustomer.appdata.Keys.Extras.KEY_ITEM_POSITION;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_CATALOGUE_DATA;
import static com.tookancustomer.appdata.Keys.Extras.PRODUCT_DETAIL_DATA;
import static com.tookancustomer.appdata.Keys.Extras.SELECTED_DATE;
import static com.tookancustomer.appdata.Keys.Extras.SERVICE_TIME;
import static com.tookancustomer.appdata.Keys.Extras.UPDATE_QUESTIONNAIRE;
import static com.tookancustomer.appdata.TerminologyStrings.CART;
import static com.tookancustomer.appdata.TerminologyStrings.NAME;
import static com.tookancustomer.appdata.TerminologyStrings.PRODUCT;
import static com.tookancustomer.appdata.TerminologyStrings.QUANTITY;
import static com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants.CategoryLayoutTypes.GRID_LAYOUT;
import static com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants.CategoryLayoutTypes.LIST_LAYOUT;
import static com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants.ProductLayoutTypes.getLayoutModeByValue;

public class MerchantProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, MerchantCatalogConstants {

    public boolean showProductImage = true;
    public Callback mCallback;
    FragmentManager fragmentManager;
    private Activity activity;
    private ArrayList<Datum> dataList;
    private String startDate;
    private int currentPosition = 0;
    private BottomSheetDialog dialog;
    private EditText etQuantity;

    public MerchantProductsAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<Datum> dataList, boolean showProductImage, Callback mCallback) {
        this.activity = activity;
        this.dataList = dataList;
        this.showProductImage = showProductImage;
        this.mCallback = mCallback;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutType = dataList != null && dataList.size() > 0 ? dataList.get(0).getStorefrontData().getProductLayoutType() : ProductLayoutTypes.LIST_LAYOUT.layoutValue;
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getLayoutModeByValue(activity, layoutType), parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;

            Utils.setVisibility(View.GONE, viewHolder.tvDescription3Prime);
            Utils.setVisibility(View.GONE, viewHolder.tvDescriptionDiscount);


            final Datum productData = dataList.get(position);
            final int productButtonType = productData.getStorefrontData().getProductButtonType();
            if (productData.getStorefrontData().getProductLayoutType() == Constants.NLevelLayoutType.BANNER_LAYOUT.layoutValue) {

                int videoImageCount = 0;
                if (productData.getMultiImageUrl().size() > 0)
                    videoImageCount = videoImageCount + productData.getMultiImageUrl().size();

                if (productData.getMultiVideoUrl().size() > 0)
                    videoImageCount = videoImageCount + productData.getMultiVideoUrl().size();


                if (videoImageCount > 1) {
                    viewHolder.llStackIcon.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.llStackIcon.setVisibility(View.GONE);
                }
            }
            if (StorefrontCommonData.getAppConfigurationData().getIsProductShareEnabled() == 1) {
                if (viewHolder.tvOutOfStockText.getVisibility() == View.GONE)
                    viewHolder.llShareProduct.setVisibility(View.VISIBLE);
            } else {
                viewHolder.llShareProduct.setVisibility(View.GONE);

            }
            viewHolder.llShareProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String isSingleStore = "store/";
                    if (productData.getStorefrontData().getIsSingleStorefront() == 1) {
                        isSingleStore = "single-store/";
                    }
                    String urlToShare = " " + "https://" + StorefrontCommonData.getAppConfigurationData().getDomainName()
                            + "/" + StorefrontCommonData.getSelectedLanguageCode().getLanguageCode()
                            + "/" + isSingleStore + productData.getStorefrontData().getStoreName().replace(" ", "%20")
                            + "/" + productData.getStorefrontData().getStorefrontUserId() +
                            "?prodname=" + productData.getProductId()
                            + "&pordCat=" + productData.getParentCategoryId();
                    String prefixShare = "Found this amazing product on " + activity.getString(R.string.app_name);

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, prefixShare + urlToShare);
                    sendIntent.setType("text/plain");
                    activity.startActivity(sendIntent);

                }
            });



            viewHolder.llNLevelParent.setClickable(false);
            Utils.setVisibility(View.GONE, viewHolder.tvDescription1, viewHolder.tvDescription2, viewHolder.tvDescription3);
            Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
            Utils.setVisibility(View.GONE, viewHolder.rlNLevelImageParent);
            if (!Dependencies.isLaundryApp())
                Utils.setVisibility(View.GONE, viewHolder.ivSelectedProduct);

            if ((productData.getStorefrontData().getIsReviewRatingEnabled() == 0 || productData.getProductRating().floatValue() <= 0)) {
                viewHolder.llRatingImageLayout.setVisibility(View.GONE);
            } else {
                viewHolder.llRatingImageLayout.setVisibility(View.VISIBLE);
                viewHolder.tvAverageRatingImage.setText(productData.getProductRating().floatValue() + "");
            }

            if (productData.getIsVeg() != null) {
                viewHolder.ivFoodType.setVisibility(StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 ? View.VISIBLE : View.GONE);
                viewHolder.ivFoodTypeNoImage.setVisibility(StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 && !(showProductImage) ? View.VISIBLE : View.GONE);
                if (productData.getIsVeg() == 1) {
                    viewHolder.ivFoodType.setImageResource(R.drawable.veg);

                } else if (productData.getIsVeg() == 0) {
                    viewHolder.ivFoodType.setImageResource(R.drawable.nonveg);
                    viewHolder.ivFoodTypeNoImage.setImageResource(R.drawable.nonveg);
                } else {
                    viewHolder.ivFoodType.setVisibility(View.GONE);
                    if (StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1
                            && productData.getStorefrontData().getProductLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue
                            && !(showProductImage)) {
                        viewHolder.ivFoodTypeNoImage.setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.ivFoodTypeNoImage.setVisibility(View.GONE);
                    }
                }

            } else {
                viewHolder.ivFoodType.setVisibility(View.GONE);
                if (StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1
                        && productData.getStorefrontData().getProductLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue
                        && !(showProductImage)) {
                    viewHolder.ivFoodTypeNoImage.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.ivFoodTypeNoImage.setVisibility(View.GONE);
                }
            }

            if (showProductImage) {
                Utils.setVisibility(View.VISIBLE, viewHolder.rlNLevelImageParent);
                try {
//                    viewHolder.ivNLevelImage.post(new Runnable() {
//                        @Override
//                        public void run() {
                    if (productData.getThumbUrl().isEmpty()) {
                        viewHolder.ivNLevelImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                    } else {
                        try {
                            viewHolder.progressBarAvtar.setVisibility(View.VISIBLE);
                            viewHolder.ivNLevelImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
//                            new getImageSize(productData.getImageUrl(), viewHolder.ivNLevelImage, productData.getThumbUrl(), viewHolder.progressBarAvtar).execute();
                            try {
                                boolean cropAccToImageSize = true;
                                if (productData.getStorefrontData().getProductLayoutType() == GRID_LAYOUT.layoutValue
                                        || productData.getStorefrontData().getProductLayoutType() == LIST_LAYOUT.layoutValue) {
                                    cropAccToImageSize = false;

                                }
                                new GlideUtil.GlideUtilBuilder(viewHolder.ivNLevelImage)
                                        .setError(R.drawable.ic_image_placeholder)
                                        .setFallback(R.drawable.ic_image_placeholder)
                                        .setLoadItem(productData.getImageUrl())
                                        .cropAccToImageSize(cropAccToImageSize)
                                        .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                                            @Override
                                            public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {
                                                viewHolder.progressBarAvtar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) {
                                                viewHolder.progressBarAvtar.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onLoadFailed() {
                                                viewHolder.progressBarAvtar.setVisibility(View.GONE);

                                            }
                                        })
                                        .setThumbnailItem(productData.getThumbUrl())
                                        .setTransformation(new RoundedCorners(6))
                                        .build();


                            } catch (Exception e) {
                                viewHolder.progressBarAvtar.setVisibility(View.GONE);
                                Utils.printStackTrace(e);
                            }
                        } catch (Exception e) {
                        }
                    }
//                        }
//                    });

                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }

            Utils.setVisibility(productData.getName().isEmpty() ? View.GONE : View.VISIBLE, viewHolder.tvDescription1);
            viewHolder.tvDescription1.setText(productData.getName().trim());
            viewHolder.tvDescription1.setTypeface(viewHolder.tvDescription1.getTypeface(), Typeface.BOLD);

            Utils.setVisibility(productData.getDescription().toString().isEmpty() ? View.GONE : View.VISIBLE, viewHolder.tvDescription2);
            viewHolder.tvDescription2.setText(Html.fromHtml(productData.getDescription().toString().trim()));

            float productDiscount = productData.getProductDiscount();
            if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && productData.getPrice().doubleValue() <= 0) {
                Utils.setVisibility(StorefrontCommonData.getFormSettings().getPdpView() == 1 ? View.GONE : View.INVISIBLE, viewHolder.tvDescription3);
            } else {
                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription3);
                if (productData.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(productData.getUnitType()) != Constants.ProductsUnitType.FIXED) {
                    viewHolder.tvDescription3.setText(UIManager.getCurrency(Utils.getCurrencySymbol(productData.getStorefrontData().getPaymentSettings()) + Utils.getDoubleTwoDigits((productData.getPrice().doubleValue()))) + " /" +
                            Constants.ProductsUnitType.getUnitTypeText(activity, productData.getUnit().intValue(), productData.getUnitType(), false));
                } else {
                    viewHolder.tvDescription3.setText(UIManager.getCurrency(Utils.getCurrencySymbol(productData.getStorefrontData().getPaymentSettings()) + Utils.getDoubleTwoDigits(productData.getPrice().doubleValue())));
                }

                if (productDiscount > 0) {
                    Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription3Prime);
                    Utils.setVisibility(View.VISIBLE, viewHolder.tvDescriptionDiscount);
                    viewHolder.tvDescription3Prime.setText(UIManager.getCurrency((Utils.getCurrencySymbol(productData.getStorefrontData().getPaymentSettings()) + Utils.getDoubleTwoDigits(productData.getOriginalPrice()))));
                    viewHolder.tvDescriptionDiscount.setText(getDiscountDescription(productDiscount));

                    if (productData.getMaxDiscountAmount() > 0) {
                        viewHolder.tvDescriptionDiscount.setText(getMaxDiscountDescription(productDiscount, productData.getMaxDiscountAmount(), productData.getStorefrontData().getPaymentSettings()));
                    }
                }
            }

            viewHolder.tvDescription4Text.setVisibility(UIManager.showServiceTime() && productData.getServiceTime() > 0 ? View.VISIBLE : View.GONE);
            viewHolder.tvDescription4Text.setText(StorefrontCommonData.getString(activity, R.string.service_time) + ": ");
            viewHolder.tvDescription4.setVisibility(UIManager.showServiceTime() && productData.getServiceTime() > 0 ? View.VISIBLE : View.GONE);
            viewHolder.tvDescription4.setText(Utils.getServiceTime(productData.getServiceTime(), viewHolder.tvDescription4.getContext(), productData.getUnitType()));


            if (StorefrontCommonData.getFormSettings().getPdpView() == 1
                    || !productData.getStorefrontData().isStoreAvailableForBooking()
                    || productData.getMenuEnabledProduct() == 0) {


                viewHolder.llNLevelParent.setEnabled(StorefrontCommonData.getFormSettings().getPdpView() == 1);
                Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);

            } else {
                viewHolder.llNLevelParent.setEnabled(false);

                if (productButtonType == ButtonTypes.SELECT_TEXT_BUTTON.buttonValue) {
                    viewHolder.llNLevelParent.setEnabled(false);
                    viewHolder.tvSingleSelectionButton.setVisibility(productData.getAvailableQuantity() == 0 && productData.getInventoryEnabled() == 1
                            ? View.GONE : View.VISIBLE);
                } else if (productButtonType == ButtonTypes.HIDDEN_BUTTON.buttonValue || productButtonType == ButtonTypes.NEXT_ARROW_BUTTON.buttonValue) {
                    viewHolder.llNLevelParent.setEnabled(true);
                } else {
                    /*productButtonType == ButtonTypes.ADD_AND_REMOVE_BUTTON.buttonValue*/
                    viewHolder.llNLevelParent.setEnabled(Dependencies.isLaundryApp());
                    viewHolder.linearLayoutQuantitySelector.setVisibility(productData.getAvailableQuantity() == 0 && productData.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                }
            }

            /*For laundry flow (viewHolder.llNLevelParent.isEnabled()) is commented . Now full image will be shown at laundry too  */
            if (StorefrontCommonData.getFormSettings().getPdpView() != 1 /*&& !viewHolder.llNLevelParent.isEnabled()*/) {
                viewHolder.rlNLevelImageParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (productData.getMultiImageUrl().size() > 0 || productData.getMultiVideoUrl().size() > 0) {

                            ImageVideoScreenDialog viewImagesDialogProducts = new ImageVideoScreenDialog(productData, activity);
                            viewImagesDialogProducts.show(fragmentManager, "");
                        }
                            /*new ViewImagesDialogProducts.Builder(activity)
                                    .images(productData.getMultiImageUrl())
                                    .videos(productData.getMultiVideoUrl())
                                    .title(productData.getName()).position(0).build().show();*/


                    }
                });
            }


            final int[] selectedQuantity = {productData.getSelectedQuantity()};
           /* viewHolder.addText.setText(!TextUtils.isEmpty(!TextUtils.isEmpty(productData.getStorefrontData().getButtons().getButtonNames().getAdd())?
                    productData.getStorefrontData().getButtons().getButtonNames().getAdd():StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getAdd())?
                    !TextUtils.isEmpty(productData.getStorefrontData().getButtons().getButtonNames().getAdd())?
                            productData.getStorefrontData().getButtons().getButtonNames().getAdd():StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getAdd():StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getAdd());*/
            viewHolder.addText.setText((StorefrontCommonData.getString(activity, R.string.add)));
            if (selectedQuantity[0] > 0) {
                viewHolder.textViewQuantity.setText(selectedQuantity[0] + "");

                if (productButtonType == ButtonTypes.SELECT_TEXT_BUTTON.buttonValue) {
                    try {
                        viewHolder.tvSingleSelectionButton.setText(!TextUtils.isEmpty(productData.getStorefrontData().getButtons().getButtonNames().getRemove()) ?
                                !TextUtils.isEmpty(productData.getStorefrontData().getButtons().getButtonNames().getAdd()) ?
                                        productData.getStorefrontData().getButtons().getButtonNames().getAdd() : StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getAdd() : StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getRemove());
                        viewHolder.ivSelectedProduct.setVisibility(View.VISIBLE);

                    } catch (Exception e) {

                        Utils.printStackTrace(e);
                    }
                } else if (productButtonType == ButtonTypes.HIDDEN_BUTTON.buttonValue || productButtonType == ButtonTypes.NEXT_ARROW_BUTTON.buttonValue) {
                    viewHolder.ivSelectedProduct.setVisibility(View.VISIBLE);
//                        viewHolder.rlNLevelParent.setBackgroundColor(activity.getResources().getColor(R.color.black_10));
                    if (!Dependencies.isLaundryApp())
                        viewHolder.rlNLevelParent.setForeground(ContextCompat.getDrawable(activity, R.drawable.overlay_selected_product));
                } else {
                    /* productButtonType == ButtonTypes.ADD_AND_REMOVE_BUTTON.buttonValue */
                    viewHolder.rlAddItem.setVisibility(View.VISIBLE);
                    viewHolder.rlRemoveItem.setVisibility(View.VISIBLE);
                    viewHolder.textViewQuantity.setVisibility(View.VISIBLE);
                    viewHolder.addText.setVisibility(View.GONE);


                    if (Dependencies.isLaundryApp()) {
                        viewHolder.ivSelectedProduct.setVisibility(View.VISIBLE);
                        viewHolder.llNLevelParent.setEnabled(true);
                    }
//                        viewHolder.rlNLevelParent.setBackgroundColor(activity.getResources().getColor(R.color.black_10));
                    //viewHolder.rlNLevelParent.setForeground(ContextCompat.getDrawable(activity, R.drawable.overlay_selected_product));

                }


            } else {
                if (productButtonType == ButtonTypes.SELECT_TEXT_BUTTON.buttonValue) {
                    try {
                        viewHolder.tvSingleSelectionButton.setText(!TextUtils.isEmpty(productData.getStorefrontData().getButtons().getButtonNames().getAdd()) ?
                                productData.getStorefrontData().getButtons().getButtonNames().getAdd() : StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getAdd());
                        viewHolder.ivSelectedProduct.setVisibility(View.GONE);

                    } catch (Exception e) {
                        Utils.printStackTrace(e);
                    }
                } else if (productButtonType == ButtonTypes.HIDDEN_BUTTON.buttonValue || productButtonType == ButtonTypes.NEXT_ARROW_BUTTON.buttonValue) {
                    viewHolder.ivSelectedProduct.setVisibility(View.GONE);
//                        viewHolder.rlNLevelParent.setBackgroundColor(activity.getResources().getColor(R.color.white));

                    viewHolder.rlNLevelParent.setForeground(null);
                } else {
                    /* productButtonType == ButtonTypes.ADD_AND_REMOVE_BUTTON.buttonValue */
                    if (Dependencies.isLaundryApp()) {
                        if (productData.getStorefrontData().getMultipleProductInSingleCart()
                                == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                            viewHolder.tvSingleSelectionButton.setText(!TextUtils.isEmpty(productData.getStorefrontData().getButtons().getButtonNames().getAdd()) ?
                                    productData.getStorefrontData().getButtons().getButtonNames().getAdd() : StorefrontCommonData.getFormSettings().getButtons().getButtonNames().getAdd());
                            viewHolder.tvSingleSelectionButton.setVisibility(View.VISIBLE);
                            viewHolder.rlAddItem.setVisibility(View.GONE);
                            viewHolder.rlRemoveItem.setVisibility(View.GONE);
                            viewHolder.textViewQuantity.setVisibility(View.GONE);
                            viewHolder.addText.setVisibility(View.GONE);
                        } else {
                            viewHolder.addText.setVisibility(View.VISIBLE);
                            viewHolder.rlAddItem.setVisibility(View.VISIBLE);
                            viewHolder.ivSelectedProduct.setVisibility(View.GONE);
                        }
                    } else
                        viewHolder.rlAddItem.setVisibility(View.VISIBLE);
                    viewHolder.rlRemoveItem.setVisibility(View.GONE);
                    viewHolder.textViewQuantity.setVisibility(View.GONE);
                    viewHolder.addText.setVisibility(View.VISIBLE);


                }
            }
            viewHolder.rlAddItem.setTag(position);
            viewHolder.rlAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    addRemoveItem(productData, position, selectedQuantity, false, (ViewHolder) holder);

                }
            });


            viewHolder.rlRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productData.getItemSelectedList().size() > 1) {
                        Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_remove_items_from_checkout).replace(StorefrontCommonData.getString(activity, R.string.item),
                                StorefrontCommonData.getTerminology().getItem(false))
                                .replace(StorefrontCommonData.getString(activity, R.string.checkout), StorefrontCommonData.getTerminology().getCheckout(false)).replace(StorefrontCommonData.getString(activity, R.string.cart), StorefrontCommonData.getTerminology().getCart(false)));


                    } else {
                        if (selectedQuantity[0] <= 0) {
                            productData.setSelectedQuantity(0);
                        } else {
                            if (productData.getMinProductquantity() > 1) {

                                if (selectedQuantity[0] <= productData.getMinProductquantity()) {

                                    productData.setSelectedQuantity(selectedQuantity[0] - productData.getMinProductquantity());
                                } else
                                    productData.setSelectedQuantity(selectedQuantity[0] - 1);
                            } else
                                productData.setSelectedQuantity(selectedQuantity[0] - 1);
                        }
                        if (productData.getItemSelectedList().size() == 1) {
                            productData.getItemSelectedList().get(0).setQuantity(productData.getSelectedQuantity());
                            if (productData.getSelectedQuantity() == 0) {
                                productData.getItemSelectedList().remove(0);
                            }
                        }
                        notifyDataSetChanged();
                        Dependencies.addCartItem(activity, productData);
                        mCallback.onQuantityUpdated();
                    }
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
                    if (StorefrontCommonData.getFormSettings().getPdpView() == 1) {

                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        String myJson = gson.toJson(productData);
                        Intent intent;

                        if (StorefrontCommonData.getFormSettings().getPdpViewTemplates() == 0) {
                            intent = new Intent(activity, ProductDetailActivity.class);
                        } else {
                            intent = new Intent(activity, ProductDetailPDPView2Activity.class);
                        }

                        intent.putExtra(PRODUCT_DETAIL_DATA, myJson);
                        bundle.putInt(KEY_ITEM_POSITION, position);

                        if (activity instanceof MerchantCatalogActivity) {
                            bundle.putDouble(PICKUP_LATITUDE, ((MerchantCatalogActivity) activity).pickLatitude);
                            bundle.putDouble(PICKUP_LONGITUDE, ((MerchantCatalogActivity) activity).pickLongitude);
                            bundle.putString(PICKUP_ADDRESS, ((MerchantCatalogActivity) activity).pickAddress);
                        }

                        intent.putExtras(bundle);
                        activity.startActivityForResult(intent, OPEN_PRODUCT_DETAILS_SCREEN);
                        AnimationUtils.forwardTransition(activity);
                    } else if ((Dependencies.isLaundryApp() && productButtonType == ButtonTypes.ADD_AND_REMOVE_BUTTON.buttonValue)
                            || productButtonType == ButtonTypes.HIDDEN_BUTTON.buttonValue
                            || productButtonType == ButtonTypes.NEXT_ARROW_BUTTON.buttonValue) {
                        viewHolder.tvSingleSelectionButton.performClick();
                    }
                }
            });


            viewHolder.tvOutOfStockText.setVisibility(productData.getAvailableQuantity() == 0 &&
                    productData.getInventoryEnabled() == 1 ? View.VISIBLE : View.GONE);

            if (productData.getMinProductquantity() > 1) {
                viewHolder.tvMinProductQuan.setVisibility(View.VISIBLE);
                viewHolder.tvMinProductQuan.setText(StorefrontCommonData.getString(activity, R.string.text_minimum_product_quantity) + productData.getMinProductquantity());
            } else {
                viewHolder.tvMinProductQuan.setVisibility(View.GONE);
            }


            if (!viewHolder.llNLevelParent.isEnabled()) {
                viewHolder.layoutProductDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!productData.getLongDescription().isEmpty()) {
                            new GenericMessageDialog.Builder(activity)
                                    .title(productData.getName())
                                    .message(productData.getLongDescription()).build().show();
                        }
                    }
                });
            }

            if (Dependencies.isLaundryApp()) {
                ViewTreeObserver vto = viewHolder.tvDescription2.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Layout l = viewHolder.tvDescription2.getLayout();
                        if (l != null) {
                            int lines = l.getLineCount();
                            if (lines > 0)
                                if (l.getEllipsisCount(lines - 1) > 0) {
                                    viewHolder.tvMore.setVisibility(View.VISIBLE);
                                } else {
                                    viewHolder.tvMore.setVisibility(View.GONE);
                                }
                        }
                    }
                });
            }

            viewHolder.tvMore.setText(StorefrontCommonData.getString(viewHolder.tvMore.getContext(), R.string.read_more));

            viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Dependencies.isLaundryApp()) {
                        Intent intent = new Intent(activity, LongDescriptionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_LONG_DESCRIPTION, productData.getDescription().toString());
                        bundle.putString(EXTRA_PRODUCT_NAME, productData.getName());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                }
            });

            viewHolder.textViewQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productData.getItemSelectedList().size() > 1) {

                        Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.not_here_please_edit_your).
                                replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct(false)), false);
                    } else
                        openBottomSheet(viewHolder.textViewQuantity, viewHolder, viewHolder.getAdapterPosition(), productData, holder);
                }
            });


            if (UIManager.getRecurringTaskActive() && (dataList.get(position).getStorefrontData().getRecurrinTask() == 1)
                    && (productData.getRecurring_enabled() == 1)) {

                viewHolder.tvRecurring.setVisibility(View.VISIBLE);
            } else {

                viewHolder.tvRecurring.setVisibility(View.GONE);

            }
        }
    }


    private void addRemoveItem(Datum productData, int adapterPosition, final int[] selectedQuantity, final boolean isQuantityEditable, final ViewHolder holder) {
        Log.e("position is ", String.valueOf(adapterPosition));
        if (StorefrontCommonData.getFormSettings().getProductView() == 0 &&
                Dependencies.getSelectedProductsArrayList().size() > 0 &&
                !Dependencies.getSelectedProductsArrayList().get(0).getUserId().equals(productData.getUserId())) {
            new OptionsDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.sure_to_select_this_offering_will_delete_previous_data).replace(StorefrontCommonData.getString(activity, R.string.product), StorefrontCommonData.getTerminology().getProduct()).replace(StorefrontCommonData.getString(activity, R.string.cart), StorefrontCommonData.getTerminology().getCart(false))).listener(new OptionsDialog.Listener() {
                @Override
                public void performPositiveAction(int purpose, Bundle backpack) {
                    Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());
                    if (dataList.get(adapterPosition).getMaxProductquantity() == 0 || selectedQuantity[0] < dataList.get(adapterPosition).getMaxProductquantity()) {

                        addCartItem(selectedQuantity, adapterPosition, isQuantityEditable, holder);
                    } else {
                        String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                                .replace(NAME, dataList.get(adapterPosition).getName())
                                .replace(QUANTITY, dataList.get(adapterPosition).getMaxProductquantity() + "")
                                .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                        Utils.snackBar(activity, message);
                    }
                }

                @Override
                public void performNegativeAction(int purpose, Bundle backpack) {
                }
            }).build().show();
        } else if (Dependencies.getSelectedProductsArrayList().size() > 0 && !Dependencies.getSelectedProductsArrayList().get(0).getProductId().equals(productData.getProductId())
                && productData.getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.can_avail_only_one_product).replace(StorefrontCommonData.getString(activity, R.string.product), StorefrontCommonData.getTerminology().getProduct(false)));
        } else {
            StorefrontCommonData.getFormSettings().setUserId(productData.getUserId());
            addCartItem(selectedQuantity, adapterPosition, isQuantityEditable, holder);
        }

        notifyDataSetChanged();


    }

    private void openBottomSheet(final TextView textViewQuantity, final ViewHolder viewHolder, final int adapterPosition, final Datum productData, final RecyclerView.ViewHolder holder) {
        View view = activity.getLayoutInflater().inflate(R.layout.addremove_bottom_sheet, null);
        dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetDialog d = (BottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }, 0);
            }
        });
        dialog.setContentView(view);
        dialog.show();

        final Button btnContinue = dialog.findViewById(R.id.btnContinue);

        TextView tvEditQuantity = dialog.findViewById(R.id.tvEditQuantity);
        tvEditQuantity.setText(StorefrontCommonData.getString(activity, R.string.edit_quantity));
        btnContinue.setText(StorefrontCommonData.getString(activity, R.string.save));

        etQuantity = dialog.findViewById(R.id.etQuantity);
        etQuantity.setText(textViewQuantity.getText().toString());
        etQuantity.setSelection(etQuantity.getText().length());

        TextView tvItemName = dialog.findViewById(R.id.tvItemName);
        TextView tvItemDescription = dialog.findViewById(R.id.tvItemDescription);
        ImageView ivItemImage = dialog.findViewById(R.id.ivItemImage);
        ImageView ivCloseSheet = dialog.findViewById(R.id.ivCloseSheet);

        tvItemName.setText(productData.getName());
        if (productData.getDescription().toString().isEmpty()) {
            tvItemDescription.setVisibility(View.GONE);
        } else {
            tvItemDescription.setText(productData.getDescription().toString());
        }
        if (productData.getImageUrl().isEmpty()) {
            ivItemImage.setVisibility(View.GONE);
        } else {
            try {
                new GlideUtil.GlideUtilBuilder(ivItemImage)
                        .setPlaceholder(R.drawable.ic_loading_image)
                        .setError(R.drawable.ic_image_placeholder)
                        .setFallback(R.drawable.ic_image_placeholder)
                        .setLoadItem(productData.getImageUrl())
                        .setThumbnailItem(productData.getImageUrl())
                        .setCenterCrop(true)
                        .build();


            } catch (Exception e) {
            }
            // ivItemImage.setBackgroundDrawable(Drawable.createFromPath(productData.getImageUrl()));
        }
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks
                if (etQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                } else if (etQuantity.getText().toString().equals("0")) {
                    Toast.makeText(activity, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    addRemoveItem(productData, adapterPosition, new int[]{(Integer.parseInt(etQuantity.getText().toString()))}, true, (ViewHolder) holder);
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //Find the currently focused view, so we can grab the correct window token from it.
                    imm.hideSoftInputFromWindow(btnContinue.getWindowToken(), 0);
                    dialog.dismiss();//to hide it
                }
            }
        });
        ivCloseSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();//to hide it

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    private String getDiscountDescription(final float productDiscount) {
        return Utils.getDoubleTwoDigits(productDiscount) + StorefrontCommonData.getString(activity, R.string.discount_percent_off);
    }

    private String getMaxDiscountDescription(final float productDiscount, float productMaxDiscount, PaymentSettings paymentSettings) {
        return Utils.getDoubleTwoDigits(productDiscount) +
                StorefrontCommonData.getString(activity, R.string.discount_percent_off) + " " +
                StorefrontCommonData.getString(activity, R.string.upto) + " " + Utils.getCurrencySymbol(paymentSettings) + Utils.getDoubleTwoDigits(productMaxDiscount);
    }

    public void setItemCustomisation(int position) {
        if (dataList.get(0).getStorefrontData().getProductLayoutType() == 3) {
            if (activity instanceof CheckOutActivity) {
                ((CheckOutActivity) activity).oftenDialog.findViewById(R.id.tvViewMore).performClick();
            }
            return;
        }
        Intent intent = new Intent(activity, ProductCustomisationActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, position);
        if (activity instanceof MerchantCatalogActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((MerchantCatalogActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((MerchantCatalogActivity) activity).pickLongitude);
        }
        intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
        activity.startActivityForResult(intent, OPEN_CUSTOMISATION_ACTIVITY);
    }

    public void setQuestionnaireTemplate(int position) {
        Intent intent = new Intent(activity, QuestionnaireTemplateActivity.class);
        if (activity instanceof MerchantCatalogActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((MerchantCatalogActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((MerchantCatalogActivity) activity).pickLongitude);
        }
        intent.putExtra(KEY_ITEM_POSITION, position);
        intent.putExtra(UPDATE_QUESTIONNAIRE, true);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
        activity.startActivityForResult(intent, OPEN_QUESTIONNAIRE_ACTIVITY);
    }

    public void setAgentList(int position) {
        Intent intent = new Intent(activity, AgentListingActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, position);
        if (activity instanceof MerchantCatalogActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((MerchantCatalogActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((MerchantCatalogActivity) activity).pickLongitude);
        }
        intent.putExtra(UPDATE_QUESTIONNAIRE, true);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
        activity.startActivityForResult(intent, OPEN_AGENT_LIST_ACTIVITY);
    }

    public void setQuestionnaireTemplate(int[] selectedQuantity, int position) {


        if (dataList.get(position).getMaxProductquantity() == 0 || dataList.get(position).getSelectedQuantity() < dataList.get(position).getMaxProductquantity()) {
            Intent intent = new Intent(activity, QuestionnaireTemplateActivity.class);
            intent.putExtra(KEY_ITEM_POSITION, position);
            if (activity instanceof MerchantCatalogActivity) {
                intent.putExtra(PICKUP_LATITUDE, ((MerchantCatalogActivity) activity).pickLatitude);
                intent.putExtra(PICKUP_LONGITUDE, ((MerchantCatalogActivity) activity).pickLongitude);
            }
            intent.putExtra(UPDATE_QUESTIONNAIRE, true);
            intent.putExtra("SERVICE_AS_PRODUCT", true);
            intent.putExtra("selectedQuantity", selectedQuantity);
            intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
            activity.startActivityForResult(intent, OPEN_QUESTIONNAIRE_ACTIVITY);
        } else {
            String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                    .replace(NAME, dataList.get(position).getName())
                    .replace(QUANTITY, dataList.get(position).getMaxProductquantity() + "")
                    .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
            Utils.snackBar(activity, message);
        }


    }

    public void addCartItem(int[] selectedQuantity, int position, boolean isQuantityEditable, final ViewHolder holder) {

        int qty;
        if (isQuantityEditable)
            qty = selectedQuantity[0];
        else
            qty = dataList.get(position).getSelectedQuantity();
        boolean quantityCheck;
        boolean minQuantityCheck;

        if (dataList.get(position).getMinProductquantity() > 0 && dataList.get(position).getCustomizeItem().size() > 0 && !isQuantityEditable)
            qty = qty + dataList.get(position).getMinProductquantity();


        if (isQuantityEditable) {
            quantityCheck = qty <= dataList.get(position).getMaxProductquantity();
            minQuantityCheck = qty < dataList.get(position).getMinProductquantity();
        } else {
            quantityCheck = dataList.get(position).getSelectedQuantity() + 1 <= dataList.get(position).getMaxProductquantity();
            minQuantityCheck = dataList.get(position).getSelectedQuantity() + 1 < dataList.get(position).getMinProductquantity();
            if (dataList.get(position).getMinProductquantity() > 0 && dataList.get(position).getCustomizeItem().size() > 0) {
                quantityCheck = qty <= dataList.get(position).getMaxProductquantity();
                minQuantityCheck = qty < dataList.get(position).getMinProductquantity();
            }
        }
        if (minQuantityCheck && isQuantityEditable) {
            String message = (StorefrontCommonData.getString(activity, R.string.text_quantity_cannot_be_less_than_for_product) + StorefrontCommonData.getString(activity, R.string.text_remove_product_from_cart))
                    .replace(NAME, dataList.get(position).getName())
                    .replace(QUANTITY, dataList.get(position).getMinProductquantity() + "")
                    .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())
                    .replace(CART, StorefrontCommonData.getTerminology().getCart());
            Utils.snackBar(activity, message);

        } else {

            if (dataList.get(position).getMaxProductquantity() == 0 || quantityCheck) {


                boolean availableinventry;

                if (isQuantityEditable)
                    availableinventry = selectedQuantity[0] <= dataList.get(position).getAvailableQuantity();
                else
                    availableinventry = selectedQuantity[0] < dataList.get(position).getAvailableQuantity();


                if (dataList.get(position).getInventoryEnabled() == 0
                        || (availableinventry
                        && dataList.get(position).getInventoryEnabled() == 1)) {


                    if (dataList.get(position).getItemSelectedList().size() == 1 && isQuantityEditable && dataList.get(position).getCustomizeItem().size() > 0) {

                        dataList.get(position).getItemSelectedList().get(0).setQuantity(selectedQuantity[0]);

                    } else if (dataList.get(position).getCustomizeItem().size() > 0) {
                        setItemCustomisation(position);
                        return;
                    }

                    if (selectedQuantity[0] == 0 && dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                            (dataList.get(position).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                    || dataList.get(position).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT)) {

                        if (dataList.get(position).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                && (Constants.ProductsUnitType.getUnitType(dataList.get(position).getUnitType()) == Constants.ProductsUnitType.FIXED)
                                && dataList.get(position).getEnableTookanAgent() == 0) {

                            openDatePicker(position);

                        } else {
                            if (dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                    && dataList.get(position).getIsProductTemplateEnabled() == 1) {
                                setQuestionnaireTemplate(position);

                            } else if (dataList.get(position).getIsAgentsOnProductTagsEnabled() == 1) {
                                setAgentList(position);
                            } else if (dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
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
                                intent.putExtra(FROM_AGENT_SCREEN, true);
                                if (MyApplication.getInstance().getSelectedPickUpMode() == 2) {
                                    intent.putExtra(Keys.Extras.IS_SELF_PICKUP, 1);
                                } else {
                                    intent.putExtra(Keys.Extras.IS_SELF_PICKUP, 0);
                                }

                                intent.putExtra(AGENT_ID, dataList.get(position).getAgentId());
                                intent.putExtra(SERVICE_TIME, dataList.get(position).getServiceTime());
                                activity.startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                            }

                        }
                    } else {
                        if (dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                && dataList.get(position).getIsProductTemplateEnabled() == 1)
                            setQuestionnaireTemplate(selectedQuantity, position);
                        else {
                            addToCart(selectedQuantity, position, isQuantityEditable, holder);

                        }
                    }
                } else {

                    Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.order_quantity_limited));
                }
            } else {
                String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                        .replace(NAME, dataList.get(position).getName())
                        .replace(QUANTITY, dataList.get(position).getMaxProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                Utils.snackBar(activity, message);

            }
        }

    }

   /* public void addToCart(int[] selectedQuantity, int position) {
        addToCart(selectedQuantity, position, false);
    }*/

    public void addToCart(int[] selectedQuantity, int position, boolean isQuantityEditable, final ViewHolder holder) {
        int qty;

        if (isQuantityEditable)
            qty = selectedQuantity[0];
        else
            qty = dataList.get(position).getSelectedQuantity();
        boolean quantityCheck;
        boolean minQuantityCheck;
        if (dataList.get(position).getMinProductquantity() > 0
                && dataList.get(position).getCustomizeItem().size() > 0
                && !isQuantityEditable)
            qty = qty + dataList.get(position).getMinProductquantity();

        if (isQuantityEditable) {


            quantityCheck = qty <= dataList.get(position).getMaxProductquantity();
            minQuantityCheck = qty < dataList.get(position).getMinProductquantity();
        } else {
            quantityCheck = dataList.get(position).getSelectedQuantity() + 1 <= dataList.get(position).getMaxProductquantity();
            minQuantityCheck = dataList.get(position).getSelectedQuantity() + 1 < dataList.get(position).getMinProductquantity();
        }
        if (minQuantityCheck && isQuantityEditable) {
            String message = (StorefrontCommonData.getString(activity, R.string.text_quantity_cannot_be_less_than_for_product) + StorefrontCommonData.getString(activity, R.string.text_remove_product_from_cart))
                    .replace(NAME, dataList.get(position).getName())
                    .replace(QUANTITY, dataList.get(position).getMinProductquantity() + "")
                    .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())
                    .replace(CART, StorefrontCommonData.getTerminology().getCart());
            Utils.snackBar(activity, message);

        } else {
            if (dataList.get(position).getMaxProductquantity() == 0 || quantityCheck) {
                if (!isQuantityEditable) {
                    if (dataList.get(position).getMinProductquantity() > 1 &&
                            selectedQuantity[0] < dataList.get(position).getMinProductquantity()) {
                        selectedQuantity[0] = selectedQuantity[0] + dataList.get(position).getMinProductquantity();
                    } else {
                        selectedQuantity[0]++;
                    }
                }
                dataList.get(position).setSelectedQuantity(selectedQuantity[0]);

                //       notifyItemChanged(position);
                if (isQuantityEditable) {
                    holder.textViewQuantity.setText(etQuantity.getText().toString());
                }

                Dependencies.addCartItem(activity, dataList.get(position));
                mCallback.onQuantityUpdated();
            } else {
                String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                        .replace(NAME, dataList.get(position).getName())
                        .replace(QUANTITY, dataList.get(position).getMaxProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                Utils.snackBar(activity, message);
            }

        }
    }

    public void addToCart(int[] selectedQuantity, int position, boolean isQuantityEditable) {
        if (dataList.get(position).getMaxProductquantity() == 0 || dataList.get(position).getSelectedQuantity() < dataList.get(position).getMaxProductquantity()) {
            if (!isQuantityEditable) {
                if (dataList.get(position).getMinProductquantity() > 1 &&
                        selectedQuantity[0] < dataList.get(position).getMinProductquantity()) {
                    selectedQuantity[0] = selectedQuantity[0] + dataList.get(position).getMinProductquantity();
                } else {
                    selectedQuantity[0]++;
                }
            }
            dataList.get(position).setSelectedQuantity(selectedQuantity[0]);
            notifyItemChanged(position);
            //holder.textViewQuantity.setText(etQuantity.getText().toString());

            Dependencies.addCartItem(activity, dataList.get(position));

            mCallback.onQuantityUpdated();
        } else {
            String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                    .replace(NAME, dataList.get(position).getName())
                    .replace(QUANTITY, dataList.get(position).getMaxProductquantity() + "")
                    .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
            Utils.snackBar(activity, message);
        }

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

            addToCart(selectedQuantity, currentPosition, false);

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

    public interface Callback {
        void onQuantityUpdated();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llRatingImageLayout;
        private TextView tvAverageRatingImage;

        private LinearLayout llNLevelParent, llStackIcon, llShareProduct;
        private RelativeLayout rlNLevelImageParent, layoutProductDetails;
        private ImageView ivNLevelImage, ivFoodType, ivFoodTypeNoImage;

        private TextView tvDescription1, tvDescription2,
                tvDescription3, tvDescription3Prime,
                tvDescriptionDiscount, tvMinProductQuan, tvMore;

        private RelativeLayout rlQuantitySelector;
        private TextView addText;

        private LinearLayout linearLayoutQuantitySelector;
        private RelativeLayout rlRemoveItem, rlAddItem;
        private TextView textViewQuantity, tvSingleSelectionButton, tvOutOfStockText, tvDescription4, tvDescription4Text;
        private TextView tvRecurring;
        private ImageView ivForwardArrowButton;

        private FrameLayout rlNLevelParent;
        private ImageView ivSelectedProduct;
        private ImageView progressBarAvtar;

        ViewHolder(View itemView) {
            super(itemView);
            llNLevelParent = itemView.findViewById(R.id.llNLevelParent);
            progressBarAvtar = itemView.findViewById(R.id.progressBarAvtar);
            rlNLevelImageParent = itemView.findViewById(R.id.rlNLevelImageParent);
            layoutProductDetails = itemView.findViewById(R.id.layoutProductDetails);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);
            ivFoodTypeNoImage = itemView.findViewById(R.id.ivFoodTypeNoImage);
            ivFoodType = itemView.findViewById(R.id.ivFoodType);
            addText = itemView.findViewById(R.id.addText);
            llStackIcon = itemView.findViewById(R.id.llStackIcon);
            llShareProduct = itemView.findViewById(R.id.llShareProduct);


            tvDescription1 = itemView.findViewById(R.id.tvDescription1);
            tvDescription2 = itemView.findViewById(R.id.tvDescription2);
            tvDescription3 = itemView.findViewById(R.id.tvDescription3);
            tvDescription4 = itemView.findViewById(R.id.tvDescription4);
            tvDescription4Text = itemView.findViewById(R.id.tvDescription4Text);
            tvMore = itemView.findViewById(R.id.tvMore);
            tvRecurring = itemView.findViewById(R.id.tvRecurring);


            tvDescription4.setVisibility(Dependencies.isLaundryApp() ? View.VISIBLE : View.GONE);
            tvDescription4Text.setVisibility(Dependencies.isLaundryApp() ? View.VISIBLE : View.GONE);

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

            rlNLevelParent = itemView.findViewById(R.id.rlNLevelParent);
            ivSelectedProduct = itemView.findViewById(R.id.ivSelectedProduct);

            tvRecurring.setText(StorefrontCommonData.getTerminology().getSubscriptionsAvailable());
        }
    }
}
