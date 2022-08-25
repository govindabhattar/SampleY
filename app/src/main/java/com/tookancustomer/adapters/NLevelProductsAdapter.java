package com.tookancustomer.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tookancustomer.DatesOnCalendarActivity;
import com.tookancustomer.LongDescriptionActivity;
import com.tookancustomer.NLevelWorkFlowActivity;
import com.tookancustomer.ProductCustomisationActivity;
import com.tookancustomer.ProductDetailActivity;
import com.tookancustomer.ProductDetailPDPView2Activity;
import com.tookancustomer.R;
import com.tookancustomer.ScheduleTimeActivity;
import com.tookancustomer.SearchProductActivity;
import com.tookancustomer.agentListing.AgentListingActivity;
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
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Transition;
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
import static com.tookancustomer.appdata.Constants.NLevelLayoutType.LIST_LAYOUT;
import static com.tookancustomer.appdata.ExtraConstants.EXTRA_LONG_DESCRIPTION;
import static com.tookancustomer.appdata.ExtraConstants.EXTRA_PRODUCT_NAME;
import static com.tookancustomer.appdata.Keys.Extras.AGENT_ID;
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
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;
import static com.tookancustomer.appdata.Keys.Extras.UPDATE_QUESTIONNAIRE;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by cl-macmini-25 on 19/12/16.
 */

public class NLevelProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TerminologyStrings {
    public ArrayList<Integer> parentId = new ArrayList<Integer>();
    public boolean showProductImage = true;
    private Activity activity;
    private ArrayList<Datum> dataList;
    private String startDate;
    private int currentPosition = 0;
    private FragmentManager fragmentManager;
    private BottomSheetDialog dialog;
    private EditText etQuantity;
    /**
     * For ECOM flow
     * Seller view with seller name and price
     * and other sellers view
     */
    private String categoryId;

    public NLevelProductsAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<Datum> dataList, ArrayList<Integer> parentId, boolean showProductImage) {
        this.activity = activity;
        this.dataList = dataList;
        this.parentId = parentId;
        this.showProductImage = showProductImage;
        this.fragmentManager = fragmentManager;
    }

    public NLevelProductsAdapter(Activity activity, FragmentManager fragmentManager, ArrayList<Datum> dataList, ArrayList<Integer> parentId,
                                 String categoryId, boolean showProductImage) {
        this.activity = activity;
        this.dataList = dataList;
        this.parentId = parentId;
        this.categoryId = categoryId;
        this.showProductImage = showProductImage;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(Constants.NLevelLayoutType.getLayoutModeByValue(activity, viewType), parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.llNLevelParent.setClickable(false);
            Utils.setVisibility(View.GONE, viewHolder.tvDescription1, viewHolder.tvDescription2, viewHolder.tvDescription3);
            Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
            Utils.setVisibility(View.GONE, viewHolder.rlNLevelImageParent);
            Utils.setVisibility(View.GONE, viewHolder.ivSelectedProduct);

            if ((dataList.get(position).getStorefrontData().getIsReviewRatingEnabled() == 0 || dataList.get(position).getProductRating().floatValue() <= 0)) {
                viewHolder.llRatingImageLayout.setVisibility(View.GONE);
            } else {
                viewHolder.llRatingImageLayout.setVisibility(View.VISIBLE);
                viewHolder.tvAverageRatingImage.setText(dataList.get(position).getProductRating().floatValue() + "");
            }

            if (dataList.get(position).getIsVeg() != null) {
                viewHolder.ivFoodType.setVisibility(StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 ? View.VISIBLE : View.GONE);
                viewHolder.ivFoodTypeNoImage.setVisibility(StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 && !(showProductImage) ? View.VISIBLE : View.GONE);
                if (dataList.get(position).getIsVeg() == 1) {
                    viewHolder.ivFoodType.setImageResource(R.drawable.veg);
                    viewHolder.ivFoodType.setImageResource(R.drawable.veg);

                } else if (dataList.get(position).getIsVeg() == 0) {
                    viewHolder.ivFoodType.setImageResource(R.drawable.nonveg);
                    viewHolder.ivFoodTypeNoImage.setImageResource(R.drawable.nonveg);
                } else {
                    viewHolder.ivFoodType.setVisibility(View.GONE);
                    if (StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1
                            && dataList.get(position).getStorefrontData().getProductLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue
                            && !(showProductImage)) {
                        viewHolder.ivFoodTypeNoImage.setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.ivFoodTypeNoImage.setVisibility(View.GONE);
                    }
                }
            } else {
                viewHolder.ivFoodType.setVisibility(View.GONE);
                //Here false will replace by if image to be shown or not
                if (StorefrontCommonData.getAppConfigurationData().getEnableVegNonVegLabel() == 1 && dataList.get(position).getLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue && !(showProductImage)) {
                    viewHolder.ivFoodTypeNoImage.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.ivFoodTypeNoImage.setVisibility(View.GONE);
                }
            }
            if (dataList.get(position).getLayoutData() != null) {
                if (dataList.get(position).getLayoutData().getImages().size() > 0) {
                    if (showProductImage) {
                        Utils.setVisibility(View.VISIBLE, viewHolder.rlNLevelImageParent);
                        try {
                            viewHolder.ivNLevelImage.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (dataList.get(position).getThumbUrl().isEmpty()) {
                                            viewHolder.ivNLevelImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                                        } else {


                                            new GlideUtil.GlideUtilBuilder(viewHolder.ivNLevelImage)
                                                    .setFitCenter(true)
                                                    .setPlaceholder(R.drawable.ic_loading_image)
                                                    .setError(R.drawable.ic_image_placeholder)
                                                    .setFallback(R.drawable.ic_image_placeholder)
                                                    .setLoadItem(dataList.get(position).getImageUrl())
                                                    .setThumbnailItem(dataList.get(position).getThumbUrl())
                                                    .setTransformation(new RoundedCorners(4))
                                                    .build();


                                        }
                                    } catch (Exception e) {
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
                            viewHolder.tvDescription1.setText(dataList.get(position).getLayoutData().getLines().get(i).getData().trim());
                            viewHolder.tvDescription1.setTypeface(viewHolder.tvDescription1.getTypeface(), Font.BOLD);
                            break;
                        case 1:
                            if (dataList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription2);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription2);
                            }
                            viewHolder.tvDescription2.setText(dataList.get(position).getLayoutData().getLines().get(i).getData().trim());
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
                                    viewHolder.tvDescription3Prime.setText(UIManager.getCurrency((Utils.getCurrencySymbol(dataList.get(position).getPaymentSettings()) + Utils.getDoubleTwoDigits(dataList.get(position).getOriginalPrice()))));
                                    viewHolder.tvDescriptionDiscount.setText(getDiscountDescription(productDiscount));


                                    if (dataList.get(position).getMaxDiscountAmount() > 0) {
                                        viewHolder.tvDescriptionDiscount.setText(getMaxDiscountDescription(productDiscount, dataList.get(position).getMaxDiscountAmount(), dataList.get(position).getPaymentSettings()));
                                    }
                                }
                                viewHolder.tvDescription3.setText(dataList.get(position).getLayoutData().getLines().get(i).getData().trim());

                            }

                            //viewHolder.tvDescription3.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, dataList.get(position).getLayoutData().getLines().get(i).getStyle())));
                            break;

                    }
                }

                viewHolder.tvDescription4Text.setText(StorefrontCommonData.getString(activity, R.string.service_time) + ": ");
                viewHolder.tvDescription4.setText(Utils.getServiceTime(dataList.get(position).getServiceTime(),
                        viewHolder.tvDescription4.getContext(),
                        dataList.get(position).getUnitType()));
                viewHolder.tvDescription4.setVisibility(UIManager.showServiceTime() && dataList.get(position).getServiceTime() > 0 ? View.VISIBLE : View.GONE);
                viewHolder.tvDescription4Text.setVisibility(UIManager.showServiceTime() && dataList.get(position).getServiceTime() > 0 ? View.VISIBLE : View.GONE);


                if (StorefrontCommonData.getFormSettings().getPdpView() == 1
                        || !dataList.get(position).getStorefrontData().isStoreAvailableForBooking()
                        || dataList.get(position).getMenuEnabledProduct() == 0) {

                    viewHolder.llNLevelParent.setEnabled(StorefrontCommonData.getFormSettings().getPdpView() == 1);
                    Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);

                } else {
                    viewHolder.llNLevelParent.setEnabled(false);
                    for (int i = 0; i < dataList.get(position).getLayoutData().getButtons().size(); i++) {
                        switch (i) {
                            case 0:
                                int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, dataList.get(position).getLayoutData().getButtons().get(0).getType());
                                if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(true);
                                } else if (i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(true);
//                                    Utils.setVisibility(View.VISIBLE, viewHolder.ivForwardArrowButton);
//                                    viewHolder.ivForwardArrowButton.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(Dependencies.isLaundryApp());
//                                    setParentLayoutVisibility(viewHolder);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.linearLayoutQuantitySelector);
                                    viewHolder.linearLayoutQuantitySelector.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(false);
//                                    setParentLayoutVisibility(viewHolder);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.tvSingleSelectionButton);
                                    viewHolder.tvSingleSelectionButton.setVisibility(dataList.get(position).getAvailableQuantity() == 0 && dataList.get(position).getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                }
                                break;
                        }
                    }
                }
                /*For laundry flow (viewHolder.llNLevelParent.isEnabled()) is commented . Now full image will be shown at laundry too  */
                if (StorefrontCommonData.getFormSettings().getPdpView() != 1 /*&& !viewHolder.llNLevelParent.isEnabled()*/) {
                    viewHolder.rlNLevelImageParent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dataList.get(position).getMultiImageUrl().size() > 0 || dataList.get(position).getMultiVideoUrl().size() > 0) {
//                                new ViewImagesDialogProducts.Builder(activity)
//                                        .images(dataList.get(position).getMultiImageUrl())
//                                        .title(dataList.get(position).getName()).position(0).build().show();
                                ImageVideoScreenDialog viewImagesDialogProducts = new ImageVideoScreenDialog(dataList.get(position), activity);
                                viewImagesDialogProducts.show(fragmentManager, "");
                            }
                        }
                    });
                }


            }

            final int[] selectedQuantity = {dataList.get(position).getSelectedQuantity()};

            viewHolder.addText.setText(StorefrontCommonData.getString(activity, R.string.add));


            if (selectedQuantity[0] > 0) {
                viewHolder.textViewQuantity.setText(selectedQuantity[0] + "");
                if (dataList.get(position).getLayoutData() != null && dataList.get(position).getLayoutData().getButtons().size() > 0) {
                    int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, dataList.get(position).getLayoutData().getButtons().get(0).getType());
                    if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
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
                    } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                        try {
                            viewHolder.tvSingleSelectionButton.setText(dataList.get(position).getStorefrontData().getButtons().getButtonNames().getRemove());
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                        }
                    } else if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue || i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                        viewHolder.ivSelectedProduct.setVisibility(View.VISIBLE);
//                        viewHolder.rlNLevelParent.setBackgroundColor(activity.getResources().getColor(R.color.black_10));
                        if (!Dependencies.isLaundryApp())
                            viewHolder.rlNLevelParent.setForeground(ContextCompat.getDrawable(activity, R.drawable.overlay_selected_product));
                    }
                }

            } else {
                if (dataList.get(position).getLayoutData() != null && dataList.get(position).getLayoutData().getButtons().size() > 0) {
                    int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, dataList.get(position).getLayoutData().getButtons().get(0).getType());
                    if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                        if (Dependencies.isLaundryApp())
                            viewHolder.rlAddItem.setVisibility(View.GONE);
                        else
                            viewHolder.rlAddItem.setVisibility(View.VISIBLE);
                        viewHolder.rlRemoveItem.setVisibility(View.GONE);
                        viewHolder.textViewQuantity.setVisibility(View.GONE);
                        viewHolder.addText.setVisibility(View.VISIBLE);


                    } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                        try {
                            viewHolder.tvSingleSelectionButton.setText(dataList.get(position).getStorefrontData().getButtons().getButtonNames().getAdd());
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                        }
                    } else if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue || i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                        viewHolder.ivSelectedProduct.setVisibility(View.GONE);
//                        viewHolder.rlNLevelParent.setBackgroundColor(activity.getResources().getColor(R.color.white));

                        viewHolder.rlNLevelParent.setForeground(null);
                    }
                }
            }

            viewHolder.rlAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRemoveItem(dataList.get(position), viewHolder.getAdapterPosition(), selectedQuantity, false, viewHolder);

                  /*  if (StorefrontCommonData.getFormSettings().getProductView() == 0 &&
                            Dependencies.getSelectedProductsArrayList().size() > 0 &&
                            !Dependencies.getSelectedProductsArrayList().get(0).getUserId().equals(dataList.get(position).getUserId())) {
                        new OptionsDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.sure_to_select_this_product_will_delete_previous_data).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()).replace(CART, StorefrontCommonData.getTerminology().getCart(false)))
                                .listener(new OptionsDialog.Listener() {
                                    @Override
                                    public void performPositiveAction(int purpose, Bundle backpack) {

                                        Dependencies.setSelectedProductsArrayList(
                                                new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
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
                    }*/
                }
            });


            viewHolder.rlRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataList.get(position).getItemSelectedList().size() > 1) {
                        Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_remove_individual_items_from_checkout)
                                .replace(ITEM, StorefrontCommonData.getTerminology().getItem(false))
                                .replace(CHECKOUT, StorefrontCommonData.getTerminology().getCheckout(false))
                                .replace(CART, StorefrontCommonData.getTerminology().getCart(false)));
                    } else {
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

                                    Utils.snackBar(activity, message);
                                    return;
                                } else
                                    dataList.get(position).setSelectedQuantity(selectedQuantity[0] - 1);
                            } else
                                dataList.get(position).setSelectedQuantity(selectedQuantity[0] - 1);
                        }
                        if (dataList.get(position).getItemSelectedList().size() == 1) {
                            dataList.get(position).getItemSelectedList().get(0).setQuantity(dataList.get(position).getSelectedQuantity());
                            if (dataList.get(position).getSelectedQuantity() == 0) {
                                dataList.get(position).getItemSelectedList().remove(0);
                            }
                        }
                        notifyItemChanged(position);
                        Dependencies.addCartItem(activity, dataList.get(position));
                        if (activity instanceof NLevelWorkFlowActivity) {
                            ((NLevelWorkFlowActivity) activity).setTotalQuantity(true);
                        } else if (activity instanceof SearchProductActivity) {
                            ((SearchProductActivity) activity).setTotalQuantity(true);
                        }
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

//            viewHolder.tvAddRemoveButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (selectedQuantity[0] == 0) {
//                        if (dataList.get(position).getCustomizeItem().size() > 0) {
//                            setItemCustomisation(position);
//                            return;
//                        }
//                        selectedQuantity[0]++;
//                        dataList.get(position).setSelectedQuantity(selectedQuantity[0]);
//
//                    } else {
//                        if (dataList.get(position).getItemSelectedList().size() > 1) {
//                            Utils.showToast(context, context.getString(R.string.please_remove_items_from_checkout));
//                        } else {
//                            if (selectedQuantity[0] <= 0) {
//                                dataList.get(position).setSelectedQuantity(0);
//                            } else {
//                                dataList.get(position).setSelectedQuantity(selectedQuantity[0] - 1);
//                            }
//                            if (dataList.get(position).getItemSelectedList().size() == 1) {
//                                dataList.get(position).getItemSelectedList().get(0).setQuantity(dataList.get(position).getSelectedQuantity());
//                                if (dataList.get(position).getSelectedQuantity() == 0) {
//                                    dataList.get(position).getItemSelectedList().remove(0);
//                                }
//                            }
//                        }
//                    }
//
//
//                    notifyItemChanged(position);
//                    Dependencies.addCartItem(dataList.get(position));
//                    if (activity instanceof NLevelWorkFlowActivity) {
//                        ((NLevelWorkFlowActivity) activity).setTotalQuantity(true);
//                    }
//                }
//            });


            viewHolder.llNLevelParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StorefrontCommonData.getFormSettings().getPdpView() == 1) {

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
                    } else if (Dependencies.isLaundryApp() && dataList.get(position).getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                        viewHolder.tvSingleSelectionButton.performClick();
                    } else if (dataList.get(position).getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue
                            || dataList.get(position).getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {

                        viewHolder.tvSingleSelectionButton.performClick();
                    }
                }
            });

            viewHolder.textViewQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataList.get(position).getItemSelectedList().size() > 1) {

                        Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.not_here_please_edit_your).
                                replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct(false)), false);
                    } else
                        openBottomSheet(viewHolder.textViewQuantity, viewHolder, viewHolder.getAdapterPosition(), dataList.get(position));
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

            if (!viewHolder.llNLevelParent.isEnabled()) {
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

            viewHolder.tvMore.setText(StorefrontCommonData.getString(viewHolder.tvMore.getContext(),
                    R.string.read_more));


            viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Dependencies.isLaundryApp()) {
                        Intent intent = new Intent(activity, LongDescriptionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_LONG_DESCRIPTION, dataList.get(position).getDescription().toString());
                        bundle.putString(EXTRA_PRODUCT_NAME, dataList.get(position).getName());
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                }
            });

            if (UIManager.getRecurringTaskActive() &&
                    (dataList.get(position).getStorefrontData().getRecurrinTask() == 1)
                    && (dataList.get(position).getRecurring_enabled() == 1)) {
                viewHolder.tvRecurring.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvRecurring.setVisibility(View.GONE);

            }


        }
    }

    private void addRemoveItem(Datum productData, final int adapterPosition, final int[] selectedQuantity, final boolean isQuantityEditable, final ViewHolder viewHolder) {
        if (StorefrontCommonData.getFormSettings().getProductView() == 0 &&
                Dependencies.getSelectedProductsArrayList().size() > 0 &&
                !Dependencies.getSelectedProductsArrayList().get(0).getUserId().equals(productData.getUserId())) {
            new OptionsDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.sure_to_select_this_offering_will_delete_previous_data).replace(StorefrontCommonData.getString(activity, R.string.product), StorefrontCommonData.getTerminology().getProduct()).replace(StorefrontCommonData.getString(activity, R.string.cart), StorefrontCommonData.getTerminology().getCart(false))).listener(new OptionsDialog.Listener() {
                @Override
                public void performPositiveAction(int purpose, Bundle backpack) {
                    Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());
                    if (dataList.get(adapterPosition).getMaxProductquantity() == 0 || selectedQuantity[0] < dataList.get(adapterPosition).getMaxProductquantity()) {
                        addCartItem(selectedQuantity, adapterPosition, isQuantityEditable, viewHolder);
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
            addCartItem(selectedQuantity, adapterPosition, isQuantityEditable, viewHolder);
        }
    }

    private Spanned getStrikeLineText(String data) {
        String text = "<strike><font color=\'#ea4444\'>" + data + "</font></strike>";
        return Html.fromHtml(text);
    }

    private String getDiscountDescription(final float productDiscount) {
        return Utils.getDoubleTwoDigits(productDiscount) + activity.getString(R.string.discount_percent_off);
    }

    private String getMaxDiscountDescription(final float productDiscount, float productMaxDiscount, PaymentSettings paymentSettings) {
        return Utils.getDoubleTwoDigits(productDiscount) +
                StorefrontCommonData.getString(activity, R.string.discount_percent_off) + " " +
                StorefrontCommonData.getString(activity, R.string.upto) + " " + Utils.getCurrencySymbol(paymentSettings) + Utils.getDoubleTwoDigits(productMaxDiscount);
    }

    private String getDiscountedPrice(final float productDiscount, final String price) {
        double convertedPrice = Double.parseDouble(price);
        return Utils.getDoubleTwoDigits((convertedPrice - (convertedPrice * (productDiscount / 100))));
    }

    private void openBottomSheet(final TextView textViewQuantity, final ViewHolder viewHolder, final int adapterPosition, final Datum productData) {
        View view = activity.getLayoutInflater().inflate(R.layout.addremove_bottom_sheet, null);
        dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.show();

        final Button btnContinue = dialog.findViewById(R.id.btnContinue);
        btnContinue.setText(StorefrontCommonData.getString(activity,R.string.save));

        etQuantity = dialog.findViewById(R.id.etQuantity);
        etQuantity.setText(textViewQuantity.getText().toString());
        TextView tvItemName = dialog.findViewById(R.id.tvItemName);
        TextView tvItemDescription = dialog.findViewById(R.id.tvItemDescription);
        TextView tvEditQuantity = dialog.findViewById(R.id.tvEditQuantity);
        tvEditQuantity.setText(StorefrontCommonData.getString(activity, R.string.edit_quantity));

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
                    addRemoveItem(productData, adapterPosition, new int[]{(Integer.parseInt(etQuantity.getText().toString()))}, true, viewHolder);
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

        if (dataList != null && dataList.size() > 0) {
            try {
//        for (int i = 0; i < dataList.size(); i++) {
//            return dataList.get(position).getLayoutType();
                return dataList.get(0).getLayoutType();
//        }

//
//        if (parentId.size() > 0) {
//            for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel - 1).size(); i++) {
//                if (nLevelWorkFlowData.getData().get(categoryLevel - 1).get(i).getCatalogueId().equals(parentId.get(0))) {
//                    return nLevelWorkFlowData.getData().get(categoryLevel - 1).get(i).getChildLayoutType();
//                }
//            }
//        }
            } catch (Exception e) {
            }
        }
//
//        if (parentId.size() > 0) {
//            for (int i = 0; i < nLevelWorkFlowData.getData().get(categoryLevel - 1).size(); i++) {
//                if (nLevelWorkFlowData.getData().get(categoryLevel - 1).get(i).getCatalogueId().equals(parentId.get(0))) {
//                    return nLevelWorkFlowData.getData().get(categoryLevel - 1).get(i).getChildLayoutType();
//                }
//            }
//        }

        return LIST_LAYOUT.layoutValue;
    }

    public void addCartItem(int[] selectedQuantity, int position, boolean isQuantityEditable, ViewHolder viewHolder) {
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
            String message = (StorefrontCommonData.getString(activity, R.string.text_quantity_cannot_be_less_than_for_product) +
                    StorefrontCommonData.getString(activity, R.string.text_remove_product_from_cart))
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

                    if (dataList.get(position).getCustomizeItem().size() > 0) {
                        setItemCustomisation(position);
                        return;
                    }

                    if (selectedQuantity[0] == 0 && dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                            (dataList.get(position).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY || dataList.get(position).getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT)) {

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
                                intent.putExtra("service_time", dataList.get(position).getServiceTime());
                                intent.putExtra(AGENT_ID, dataList.get(position).getAgentId());
                                activity.startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                            }

                        }
                    } else {
                        if (dataList.get(position).getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                && dataList.get(position).getIsProductTemplateEnabled() == 1)
                            setQuestionnaireTemplate(selectedQuantity, position);
                        else
                            addToCart(selectedQuantity, position, isQuantityEditable, viewHolder);
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

    public void setQuestionnaireTemplate(int[] selectedQuantity, int position) {


        if (dataList.get(position).getMaxProductquantity() == 0 || dataList.get(position).getSelectedQuantity() < dataList.get(position).getMaxProductquantity()) {
            Intent intent = new Intent(activity, QuestionnaireTemplateActivity.class);
            intent.putExtra(KEY_ITEM_POSITION, position);
            intent.putExtra(UPDATE_QUESTIONNAIRE, true);
            intent.putExtra("SERVICE_AS_PRODUCT", true);
            if (activity instanceof NLevelWorkFlowActivity) {
                intent.putExtra(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
                intent.putExtra(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
            } else if (activity instanceof SearchProductActivity) {
                intent.putExtra(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
                intent.putExtra(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
            }
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

    public void setQuestionnaireTemplate(int position) {
        Intent intent = new Intent(activity, QuestionnaireTemplateActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, position);
        if (activity instanceof NLevelWorkFlowActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
        } else if (activity instanceof SearchProductActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
        }
        intent.putExtra(UPDATE_QUESTIONNAIRE, true);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
        activity.startActivityForResult(intent, OPEN_QUESTIONNAIRE_ACTIVITY);
    }

    public void setAgentList(int position) {
        Intent intent = new Intent(activity, AgentListingActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, position);


        if (activity instanceof NLevelWorkFlowActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
        } else if (activity instanceof SearchProductActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
        }
        intent.putExtra(UPDATE_QUESTIONNAIRE, true);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, dataList.get(position));
        activity.startActivityForResult(intent, OPEN_AGENT_LIST_ACTIVITY);
    }

    public void addToCart(int[] selectedQuantity, int position, boolean isQuantityEditable, ViewHolder viewHolder) {
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
            if (dataList.get(position).getMaxProductquantity() == 0 || dataList.get(position).getSelectedQuantity() < dataList.get(position).getMaxProductquantity()
                    || quantityCheck) {
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
                if (isQuantityEditable) {
                    viewHolder.textViewQuantity.setText(etQuantity.getText().toString());
                }

                Dependencies.addCartItem(activity, dataList.get(position));
                if (activity instanceof NLevelWorkFlowActivity) {
                    ((NLevelWorkFlowActivity) activity).setTotalQuantity(true);
                } else if (activity instanceof SearchProductActivity) {
                    ((SearchProductActivity) activity).setTotalQuantity(true);
                }

                if (dataList.get(position).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && dataList.get(position).getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                    Bundle extraa = new Bundle();
                    extraa.putSerializable(STOREFRONT_DATA, dataList.get(position).getStorefrontData());
                    if (activity instanceof NLevelWorkFlowActivity) {
                        extraa.putDouble(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
                        extraa.putDouble(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
                        extraa.putString(PICKUP_ADDRESS, ((NLevelWorkFlowActivity) activity).pickAddress);
                    } else if (activity instanceof SearchProductActivity) {
                        extraa.putDouble(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
                        extraa.putDouble(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
                        extraa.putString(PICKUP_ADDRESS, ((SearchProductActivity) activity).pickupAddress);
                    }
                    Transition.openCheckoutActivity(activity, extraa);
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
//                    viewHolder.tvProductQuantity.setText(selectedQuantity[0]+"");
            notifyItemChanged(position);

            Dependencies.addCartItem(activity, dataList.get(position));
            if (activity instanceof NLevelWorkFlowActivity) {
                ((NLevelWorkFlowActivity) activity).setTotalQuantity(true);
            } else if (activity instanceof SearchProductActivity) {
                ((SearchProductActivity) activity).setTotalQuantity(true);
            }

            if (dataList.get(position).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT && dataList.get(position).getStorefrontData().getMerchantMinimumOrder() <= Dependencies.getProductListSubtotal()) {
                Bundle extraa = new Bundle();
                extraa.putSerializable(STOREFRONT_DATA, dataList.get(position).getStorefrontData());
                if (activity instanceof NLevelWorkFlowActivity) {
                    extraa.putDouble(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
                    extraa.putDouble(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
                    extraa.putString(PICKUP_ADDRESS, ((NLevelWorkFlowActivity) activity).pickAddress);
                } else if (activity instanceof SearchProductActivity) {
                    extraa.putDouble(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
                    extraa.putDouble(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
                    extraa.putString(PICKUP_ADDRESS, ((SearchProductActivity) activity).pickupAddress);
                }
                Transition.openCheckoutActivity(activity, extraa);
            }
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llRatingImageLayout;
        private TextView tvAverageRatingImage;

        private LinearLayout llNLevelParent;
        private RelativeLayout rlNLevelImageParent, layoutProductDetails;
        private ImageView ivNLevelImage, ivFoodType, ivFoodTypeNoImage;

        private TextView tvDescription1, tvDescription2,
                tvDescription3, tvDescription3Prime,
                tvDescriptionDiscount, tvMinProductQuan, tvMore;

        private RelativeLayout rlQuantitySelector;
        private LinearLayout linearLayoutQuantitySelector;
        private RelativeLayout rlRemoveItem, rlAddItem;
        private TextView textViewQuantity, tvSingleSelectionButton, tvOutOfStockText, tvDescription4, tvDescription4Text;
        private ImageView ivForwardArrowButton;

        private FrameLayout rlNLevelParent;
        private ImageView ivSelectedProduct;
        private TextView tvRecurring, addText;


        ViewHolder(View itemView) {
            super(itemView);
            llNLevelParent = itemView.findViewById(R.id.llNLevelParent);
            rlNLevelImageParent = itemView.findViewById(R.id.rlNLevelImageParent);
            layoutProductDetails = itemView.findViewById(R.id.layoutProductDetails);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);
            ivFoodTypeNoImage = itemView.findViewById(R.id.ivFoodTypeNoImage);
            ivFoodType = itemView.findViewById(R.id.ivFoodType);

            tvDescription1 = itemView.findViewById(R.id.tvDescription1);
            tvDescription2 = itemView.findViewById(R.id.tvDescription2);
            tvDescription3 = itemView.findViewById(R.id.tvDescription3);
            tvDescription4 = itemView.findViewById(R.id.tvDescription4);
            addText = itemView.findViewById(R.id.addText);
            tvDescription4Text = itemView.findViewById(R.id.tvDescription4Text);
            tvMore = itemView.findViewById(R.id.tvMore);


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

            tvRecurring = itemView.findViewById(R.id.tvRecurring);
            tvRecurring.setText(StorefrontCommonData.getTerminology().getSubscriptionsAvailable());

        }
    }
}
