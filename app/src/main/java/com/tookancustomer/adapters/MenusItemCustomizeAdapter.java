package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItem;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItemSelected;
import com.tookancustomer.models.ProductCatalogueData.CustomizeOption;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.TerminologyStrings.CART;
import static com.tookancustomer.appdata.TerminologyStrings.NAME;
import static com.tookancustomer.appdata.TerminologyStrings.PRODUCT;
import static com.tookancustomer.appdata.TerminologyStrings.QUANTITY;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;


/**
 * Created by Shankar on 7/17/15.
 */
public class MenusItemCustomizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int CUSTOMIZE_ITEM = 1;
    private static final int CUSTOMIZE_OPTION = 2;
    private static final int ITEM_LONG_DESCRIPTION = 3;
    private int itemPos;
    private Activity context;
    private Datum item;
    private ItemSelected itemSelected;
    private ArrayList<CustomizeOption> customizeOptions;
    private Callback callback;
    private boolean isEditCustomization = false;
    private String quantity = "0";
    private boolean isClicked = false;
    private double customizationPrice = 0.0;
    private boolean isAdded = false;
    private ArrayList<ItemSelected> itemSelecteds = new ArrayList<>();
    private EditText etQuantity;
    private BottomSheetDialog dialog;

    public MenusItemCustomizeAdapter(Activity context, Datum item, int itemPos, String quantity, boolean isEditCustomization, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.quantity = quantity;
        this.isEditCustomization = isEditCustomization;
        this.itemPos = itemPos;
        customizeOptions = new ArrayList<>();
        setList(item, quantity);
    }

    public void setList(Datum item, String quantity) {
        this.item = item;
        this.isClicked = false;
        itemSelected = new ItemSelected();
        itemSelected.setRestaurantItemId(item.getParentCategoryId());
//        itemSelected.setRestaurantItemId(item.getRestaurantItemId());
        if (item.getMinProductquantity() > 1) {
            itemSelected.setQuantity(item.getMinProductquantity());
        } else {
            itemSelected.setQuantity(1);
        }


        customizeOptions.clear();
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

        if (isEditCustomization) {
            double totalPrice = item.getPrice().doubleValue();
            for (int i = 0; i < item.getCustomizeItem().size(); i++) {
                CustomizeItem customizeItem = item.getCustomizeItem().get(i);
                CustomizeOption coCustomizeItem = new CustomizeOption();
                coCustomizeItem.setIsCustomizeItem(1);
                coCustomizeItem.setCustomizeItemPos(i);
                customizeOptions.add(coCustomizeItem);

                ArrayList<CustomizeItemSelected> customizeItemSelected = new ArrayList<CustomizeItemSelected>();

                if (item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().size() == 1) {
                    for (int l = 0; l < item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().size(); l++) {
                        customizeItemSelected.add(new CustomizeItemSelected());
                        customizeItemSelected.get(0).setCustomizeOptions(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(l).getCustomizeOptions());
                        customizeItemSelected.get(0).setCustomizeId(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(l).getCustomizeId());

                    }
                    customizationPrice = item.getItemSelectedList().get(itemPos).getCustomizationPrice();
                } else {

                    for (int l = 0; l < item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().size(); l++) {
                        customizeItemSelected.add(new CustomizeItemSelected());
                        customizeItemSelected.get(l).setCustomizeOptions(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(l).getCustomizeOptions());
                        customizeItemSelected.get(l).setCustomizeId(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(l).getCustomizeId());
                    }
                }
                int j = 0;
                for (CustomizeOption customizeOption : customizeItem.getCustomizeOptions()) {
                    customizeOption.setIsMultiSelect(customizeItem.getIsCheckBox());
                    customizeOption.setCustomizeItemPos(i);
                    customizeOptions.add(customizeOption);
                    if (customizeItemSelected != null) {
                        double optionPrice = 0d;
                        for (int k = 0; k < customizeItemSelected.size(); k++) {
                            if (customizeItem.getCustomizeOptions().get(j).getIsDefault() == 1) {
                                customizeItemSelected.get(k).setDefaultSelected(true);
                            }

                            totalPrice = totalPrice + optionPrice;
                            customizationPrice = customizationPrice + optionPrice;
                        }
                    }
                    j = j + 1;
                }


                if (customizeItemSelected != null) {
                    itemSelected.getCustomizeItemSelectedList().clear();
                    itemSelected.getCustomizeItemSelectedList().addAll(customizeItemSelected);

                }
            }
            itemSelected.setTotalPrice(totalPrice);
            itemSelected.setCustomizationPrice(customizationPrice);
            itemSelected.setUnitCount(item.getUnitCount());
        } else {
            double totalPrice = item.getPrice().doubleValue();
            double customizationPrice = 0.0;
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

                if (customizeItemSelected != null) {
                    itemSelected.getCustomizeItemSelectedList().add(customizeItemSelected);

                }
            }
            itemSelected.setTotalPrice(totalPrice);
            itemSelected.setCustomizationPrice(customizationPrice);
            itemSelected.setUnitCount(item.getUnitCount());
        }


        if (!Integer.valueOf(quantity).equals(0)) {
            itemSelected.setQuantity(Integer.valueOf(quantity));
            //itemSelected.setCustomizeItemSelectedList(itemSelected.getCustomizeItemSelectedList());
        }
        callback.updateItemTotalPrice(itemSelected, customizationPrice, false, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menus_item, parent, false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(layoutParams);
//            ASSL.DoMagic(v);
            return new ViewHolderItem(v, context);
        } else if (viewType == ITEM_LONG_DESCRIPTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_long_description, parent, false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(layoutParams);
//            ASSL.DoMagic(v);
            return new ViewHolderLongDescription(v, context);
        } else if (viewType == CUSTOMIZE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_menus_subcategory, parent, false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(layoutParams);
//            ASSL.DoMagic(v);
            return new ViewHolderCustomizeItem(v, context);
        } else if (viewType == CUSTOMIZE_OPTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customize_option_item, parent, false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(layoutParams);
//            ASSL.DoMagic(v);
            return new ViewHolderCustomizeOption(v, context);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if (customizeOptions.get(position).getIsItem() == 1) {
            return ITEM;
        } else if (customizeOptions.get(position).getLongDescription() != null) {
            return ITEM_LONG_DESCRIPTION;
        } else if (customizeOptions.get(position).getIsCustomizeItem() == 1) {
            return CUSTOMIZE_ITEM;
        } else {
            return CUSTOMIZE_OPTION;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CustomizeOption customizeOption = customizeOptions.get(position);
        if (holder instanceof ViewHolderItem) {
            final ViewHolderItem viewHolder = (ViewHolderItem) holder;

            viewHolder.llNLevelParent.setClickable(false);
            Utils.setVisibility(View.GONE, viewHolder.tvDescription1, viewHolder.tvDescription2, viewHolder.tvDescription3);
            Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
            Utils.setVisibility(View.GONE, viewHolder.rlNLevelImageParent);

            if (item.getLayoutData() != null) {
                if (item.getLayoutData().getImages().size() > 0) {
                    if (item.getLayoutData().getImages().get(0).getSize() != (Constants.NLevelImageStyles.NONE.appStyleValue)) {
                        Utils.setVisibility(View.VISIBLE, viewHolder.rlNLevelImageParent);
                        try {
                            viewHolder.ivNLevelImage.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (item.getThumbUrl().isEmpty()) {
                                        viewHolder.ivNLevelImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_placeholder));
                                    } else {
                                        try {

                                            new GlideUtil.GlideUtilBuilder(viewHolder.ivNLevelImage)
                                                    .setLoadItem(item.getThumbUrl())
                                                    .setFitCenter(true)
                                                    .setPlaceholder(R.drawable.ic_loading_image)
                                                    .setError(R.drawable.ic_image_placeholder)
                                                    .setFallback(R.drawable.ic_image_placeholder)
                                                    .setTransformation(new RoundedCorners(10))
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

                for (int i = 0; i < item.getLayoutData().getLines().size(); i++) {
                    switch (i) {
                        case 0:
                            if (item.getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription1);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription1);
                            }
                            viewHolder.tvDescription1.setText(item.getLayoutData().getLines().get(i).getData());
                            break;
                        case 1:
                            if (item.getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription2);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription2);
                            }
                            viewHolder.tvDescription2.setText(item.getLayoutData().getLines().get(i).getData());
                            break;
                        case 2:
                            if (item.getLayoutData().getLines().get(i).getData().isEmpty()
                                    || (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && item.getPrice().doubleValue() <= 0)
                            ) {
                                Utils.setVisibility(View.INVISIBLE, viewHolder.tvDescription3);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription3);
                            }
//                            String customizedAmount = item.getLayoutData().getLines().get(i).getData().replace(",", "");
//                            viewHolder.tvDescription3.setText(UIManager.getCurrency(item.getLayoutData().getLinxes().get(i).getData()));

                            viewHolder.tvDescription3.setText(UIManager.getCurrency(Utils.getCurrencySymbol(item.getStorefrontData().getPaymentSettings()) + Utils.getDoubleTwoDigits(item.getPrice().doubleValue())));

                            break;
                    }
                }

                if (StorefrontCommonData.getFormSettings().getPdpView() == 1) {
                    viewHolder.llNLevelParent.setEnabled(true);
                    Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
                } else {
                    for (int i = 0; i < item.getLayoutData().getButtons().size(); i++) {
                        switch (i) {
                            case 0:
                                int i1 = Constants.NLevelButtonType.getButtonIdByValue(context, item.getLayoutData().getButtons().get(0).getType());
                                if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(true);
                                } else if (i1 == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(false);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.linearLayoutQuantitySelector);
                                    viewHolder.linearLayoutQuantitySelector.setVisibility(item.getAvailableQuantity() == 0 && item.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(false);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.tvSingleSelectionButton);
                                    viewHolder.tvSingleSelectionButton.setVisibility(item.getAvailableQuantity() == 0 && item.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                } else if (i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                                    viewHolder.llNLevelParent.setEnabled(true);
                                    Utils.setVisibility(View.VISIBLE, viewHolder.ivForwardArrowButton);
                                    viewHolder.ivForwardArrowButton.setVisibility(item.getAvailableQuantity() == 0 && item.getInventoryEnabled() == 1 ? View.GONE : View.VISIBLE);
                                }
                                break;
                        }
                    }
                }

            }

            viewHolder.llNLevelParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });


            viewHolder.tvOutOfStockText.setVisibility(item.getAvailableQuantity() == 0 && item.getInventoryEnabled() == 1 ? View.VISIBLE : View.GONE);


            final int total = itemSelected.getQuantity();
            viewHolder.textViewQuantity.setText(String.valueOf(total));

            if (StorefrontCommonData.getFormSettings().getPdpView() == 1) {
                viewHolder.llNLevelParent.setEnabled(true);
                Utils.setVisibility(View.GONE, viewHolder.tvSingleSelectionButton, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
            } else {
                if (item.getLayoutData() != null && item.getLayoutData().getButtons() != null
                        && item.getLayoutData().getButtons().size() > 0
                        && Constants.NLevelButtonType.getButtonIdByValue(context, item.getLayoutData().getButtons().get(0).getType()) == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {

                    viewHolder.tvSingleSelectionButton.setVisibility(View.VISIBLE);
                    try {
                        if (total > 0) {
                            viewHolder.tvSingleSelectionButton.setText(item.getStorefrontData().getButtons().getButtonNames().getRemove());
                        } else {
                            viewHolder.tvSingleSelectionButton.setText(item.getStorefrontData().getButtons().getButtonNames().getAdd());
                        }
                    } catch (Exception e) {

                        Utils.printStackTrace(e);
                    }
                } else {
                    viewHolder.tvSingleSelectionButton.setVisibility(View.GONE);

                    viewHolder.rlRemoveItem.setVisibility(View.VISIBLE);
                    viewHolder.textViewQuantity.setVisibility(View.VISIBLE);
                    viewHolder.rlAddItem.setVisibility(View.VISIBLE);

                    if (total == 0) {
                        viewHolder.imageViewMinus.setVisibility(View.GONE);
                        viewHolder.textViewQuantity.setVisibility(View.GONE);
                    } else {
                        viewHolder.imageViewMinus.setVisibility(View.VISIBLE);
                        viewHolder.textViewQuantity.setVisibility(View.VISIBLE);
                    }
                }
            }

            viewHolder.imageViewMinus.setTag(position);
            viewHolder.imageViewPlus.setTag(position);

            View.OnClickListener plusClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addActionItem(total, false, viewHolder);
                }
            };

            viewHolder.textViewQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBottomSheet(viewHolder.textViewQuantity, viewHolder, viewHolder.getAdapterPosition(), item);
                }
            });

            viewHolder.rlAddItem.setOnClickListener(plusClick);
            viewHolder.rlRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (itemSelected.getQuantity() > item.getMinProductquantity()) {
                            itemSelected.setQuantity(itemSelected.getQuantity() - 1);
                            itemSelected.setTotalPrice(item.getCustomizeItemsSelectedTotalPriceForItemSelected(itemSelected));

                            notifyDataSetChanged();
                            callback.updateItemTotalPrice(itemSelected, itemSelected.getCustomizationPrice(), true, false);
                            callback.onItemMinusClick(false);
                        } else {
                            callback.onItemMinusClick(true);
                        }
                    } catch (Exception e) {
                    }
                }
            });

            viewHolder.tvSingleSelectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemSelected.getQuantity() == 0) {
                        viewHolder.rlAddItem.performClick();
                    } else {
                        viewHolder.rlRemoveItem.performClick();
                    }
                }
            });


//
            if (UIManager.getRecurringTaskActive() &&
                    (item.getStorefrontData().getRecurrinTask() == 1)
                    && (item.getRecurring_enabled() == 1)) {

                viewHolder.tvRecurring.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvRecurring.setVisibility(View.GONE);
            }


        } else if (holder instanceof ViewHolderCustomizeItem) {
            /**
             * if is_check_box = 0 single select
             * else multiselect
             * if single select then display (Select any 1)
             * else if multiselect {
             * if minimum_selection_required = 0 then display (optional)
             * else display (Select any $minimum_selection)
             * }
             */

            ViewHolderCustomizeItem mHolder = ((ViewHolderCustomizeItem) holder);
            CustomizeItem customizeItem = getCustomizeItem(customizeOption);
            mHolder.textViewSubCategoryName.setText("");
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            final SpannableStringBuilder sb = new SpannableStringBuilder(customizeItem.getCustomizeItemName().toUpperCase());
            sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mHolder.textViewSubCategoryName.append(sb);
            mHolder.textViewSubCategoryName.append(" ");
            if (customizeItem.getIsCheckBox() == 0) {
                mHolder.textViewSubCategoryName.append("(" +
                        StorefrontCommonData.getString(context, R.string.name_select_any_number)
                                .replace(NAME,"")
                                .replace(QUANTITY, "1")
                        + ")");
            } else {

                mHolder.textViewSubCategoryName.append(customizeItem.getMinimumSelectionRequired() == 0 ?
                        StorefrontCommonData.getString(context, R.string.optional_bracket) :
                        "(" + StorefrontCommonData.getString(context, R.string.name_select_any_number)
                                .replace(NAME,"")
                                .replace(QUANTITY, String.valueOf(customizeItem.getMinimumSelection()))
                                + ")");

            }
            /**
             * code to display minimum selection in add ons
             * for catering marketplace
             */
            mHolder.tvMinSelection.setVisibility(View.GONE);


        } else if (holder instanceof ViewHolderCustomizeOption) {
            ViewHolderCustomizeOption mHolder = ((ViewHolderCustomizeOption) holder);

            CustomizeOption itemUp = customizeOptions.get(position - 1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mHolder.cvRoot.getLayoutParams();
            int topMargin, bottomMargin;
            if (itemUp.getIsCustomizeItem() == 1) {
//                topMargin = (int)(6.0f*ASSL.Yscale());
            } else {
//                topMargin = (int)(-6.0f*ASSL.Yscale());
            }

            CustomizeOption itemDown = (position < (customizeOptions.size() - 1)) ? customizeOptions.get(position + 1) : null;
            if (itemDown == null) {
                mHolder.vSep.setVisibility(View.GONE);
            } else if (itemDown.getIsCustomizeItem() == 1) {
                mHolder.vSep.setVisibility(View.VISIBLE);
            } else {
                mHolder.vSep.setVisibility(View.GONE);
            }
            mHolder.cvRoot.setLayoutParams(layoutParams);

            mHolder.tvCustomizeOptionItemName.setText(customizeOption.getCustomizeOptionName());

            if (customizeOption.getCustomizePrice() > 0) {
                mHolder.tvCustomizeOptionItemPrice.setText("+ " + UIManager.getCurrency(
                        Utils.getCurrencySymbol(item.getPaymentSettings()) + "" + Utils.getDoubleTwoDigits(customizeOption.getCustomizePrice()))
                );
            } else {
                mHolder.tvCustomizeOptionItemPrice.setText("");
            }

            if (isEditCustomization && !isClicked) {
                for (int l = 0; l < item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().size(); l++) {
                    // if(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(l).getCustomizeId() == getCustomizeItem(customizeOption).getCustomizeId()){
                    if (item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(l).getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                        mHolder.ivCustomizeOptionItem.setImageResource(R.drawable.ic_icon_checkbox_ticked);
                        break;
                    } else {
                        mHolder.ivCustomizeOptionItem.setImageResource(R.drawable.ic_icon_checkbox_unticked);
                    }
                    //  }
                }


            } else {
                if (item.getCustomizeItemSelected(getCustomizeItem(customizeOption), true, itemSelected).getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                    mHolder.ivCustomizeOptionItem.setImageResource(R.drawable.ic_icon_checkbox_ticked);
                } else {
                    mHolder.ivCustomizeOptionItem.setImageResource(R.drawable.ic_icon_checkbox_unticked);
                }
            }

            mHolder.cvRoot.setTag(position);
            mHolder.cvRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        isClicked = true;
                        int pos = (int) v.getTag();

                        if (isEditCustomization) {
                            alreadyselectedData(pos);
                        } else {
                            CustomizeOption customizeOption = customizeOptions.get(pos);
                            CustomizeItem customizeItem = getCustomizeItem(customizeOption);
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
                        }
                        updateItemSelectedTotalPrice();
                        notifyDataSetChanged();

                    } catch (Exception e) {
                        Utils.printStackTrace(e);
                    }
                }
            });

        } else {
            final ViewHolderLongDescription viewHolder = (ViewHolderLongDescription) holder;

            viewHolder.tvLongDescriptionLabel.setText(StorefrontCommonData.getString(context, R.string.text_description));
            viewHolder.tvLongDescription.setText(item.getLongDescription().replace("\\n", "\n"));
        }
    }

    private void addActionItem(int quantity, boolean isQuantityEditable, ViewHolderItem viewHolder) {
        int qty;

        if (isQuantityEditable)
            qty = quantity;
        else
            qty = itemSelected.getQuantity();
        boolean quantityCheck;
        boolean minQuantityCheck;

        if (isQuantityEditable) {
            quantityCheck = qty <= item.getMaxProductquantity();
            minQuantityCheck = qty < item.getMinProductquantity();
        } else {
            quantityCheck = qty < item.getMaxProductquantity();
            minQuantityCheck = qty < item.getMinProductquantity();
        }

        try {
            if (minQuantityCheck) {
                String message = (StorefrontCommonData.getString(context, R.string.text_quantity_cannot_be_less_than_for_product) + StorefrontCommonData.getString(context, R.string.text_remove_product_from_cart))
                        .replace(NAME, item.getName())
                        .replace(QUANTITY, item.getMinProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())
                        .replace(CART, StorefrontCommonData.getTerminology().getCart());
                Utils.snackBar(context, message);

            } else {


                if (item.getMaxProductquantity() == 0 || quantityCheck) {

                    if (item.getInventoryEnabled() == 0 || (item.getTotalQuantity() + qty < item.getAvailableQuantity() && item.getInventoryEnabled() == 1)) {

                        if (isQuantityEditable) {
                            itemSelected.setQuantity(quantity);
                        } else {
                            itemSelected.setQuantity(itemSelected.getQuantity() + 1);
                        }
                        itemSelected.setTotalPrice(item.getCustomizeItemsSelectedTotalPriceForItemSelected(itemSelected));

                        notifyDataSetChanged();
                        if (isQuantityEditable) {
                            viewHolder.textViewQuantity.setText(etQuantity.getText().toString());
                        }

                        callback.updateItemTotalPrice(itemSelected, itemSelected.getCustomizationPrice(), true, true);
                        callback.onItemPlusClick(isQuantityEditable);
                    } else {
                        Utils.showToast(context, StorefrontCommonData.getString(context, R.string.order_quantity_limited));
                    }
                } else {
                    String message = (StorefrontCommonData.getString(context, R.string.maximum_quantity_error))
                            .replace(NAME, item.getName())
                            .replace(QUANTITY, item.getMaxProductquantity() + "")
                            .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                    Utils.showToast(context, message);

                }

            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return customizeOptions == null ? 0 : customizeOptions.size();
    }

    private CustomizeItem getCustomizeItem(CustomizeOption customizeOption) {
        return item.getCustomizeItem().get(customizeOption.getCustomizeItemPos());
    }

    private void updateItemSelectedTotalPrice() {
        itemSelected.setTotalPrice(item.getCustomizeItemsSelectedTotalPriceForItemSelected(itemSelected));

        itemSelected.setCustomizationPrice(item.getCustomizeItemsSelectedPriceForItemSelected(itemSelected));
        callback.updateItemTotalPrice(itemSelected, itemSelected.getCustomizationPrice(), true, true);
    }

    public ItemSelected getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(ItemSelected itemSelected) {
        this.itemSelected = itemSelected;
    }

    public Datum getItem() {
        if (isEditCustomization) {
            if (!isAdded) {
                itemSelecteds.addAll(item.getItemSelectedList());
                if (!itemSelecteds.contains(itemSelected)) {
                    itemSelecteds.add(itemSelected);
                }
                isAdded = true;
            }
        }
        return item;
    }

    public void setItem(Datum item) {
        this.item = item;
    }

    private void alreadyselectedData(int itemPoss) {
        CustomizeOption customizeOption = customizeOptions.get(itemPoss);
        CustomizeItem customizeItem = getCustomizeItem(customizeOption);
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
    }

    private void openBottomSheet(final TextView textViewQuantity, final ViewHolderItem viewHolder, final int adapterPosition, Datum dataList) {
        View view = context.getLayoutInflater().inflate(R.layout.addremove_bottom_sheet, null);
        dialog = new BottomSheetDialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.show();

        final Button btnContinue = dialog.findViewById(R.id.btnContinue);
        etQuantity = dialog.findViewById(R.id.etQuantity);
        etQuantity.setText(textViewQuantity.getText().toString());

        TextView tvItemName = dialog.findViewById(R.id.tvItemName);
        TextView tvItemDescription = dialog.findViewById(R.id.tvItemDescription);
        ImageView ivItemImage = dialog.findViewById(R.id.ivItemImage);
        ImageView ivCloseSheet = dialog.findViewById(R.id.ivCloseSheet);

        tvItemName.setText(dataList.getName());
        if (dataList.getDescription().toString().isEmpty()) {
            tvItemDescription.setVisibility(View.GONE);
        } else {
            tvItemDescription.setText(dataList.getDescription().toString());
        }
        if (dataList.getImageUrl().isEmpty()) {
            ivItemImage.setVisibility(View.GONE);
        } else {
            try {
                new GlideUtil.GlideUtilBuilder(ivItemImage)
                        .setPlaceholder(R.drawable.ic_loading_image)
                        .setError(R.drawable.ic_image_placeholder)
                        .setFallback(R.drawable.ic_image_placeholder)
                        .setLoadItem(dataList.getImageUrl())
                        .setThumbnailItem(dataList.getImageUrl())
                        .setCenterCrop(true)
                        .build();


            } catch (Exception e) {
            }
        }


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks
                if (etQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(etQuantity.getText().toString()) == 0) {
                    Toast.makeText(context, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    addActionItem((Integer.parseInt(etQuantity.getText().toString())), true, viewHolder);
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
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


    public interface Callback {
        void updateItemTotalPrice(ItemSelected itemSelected, double customizationPrice, boolean isEditCustoMi, boolean isEditAddons);

        void onItemMinusClick(boolean allItemsFinished);

        void onItemPlusClick(boolean isEditable);
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {

        private LinearLayout llNLevelParent;
        private RelativeLayout rlNLevelImageParent;
        private ImageView ivNLevelImage;

        private TextView tvDescription1, tvDescription2, tvDescription3;

        private RelativeLayout rlQuantitySelector;
        private LinearLayout linearLayoutQuantitySelector;
        private RelativeLayout rlRemoveItem, rlAddItem;
        private TextView textViewQuantity, tvSingleSelectionButton, tvOutOfStockText;
        private ImageView ivForwardArrowButton, imageViewMinus, imageViewPlus;
        private TextView tvRecurring;


        public ViewHolderItem(View itemView, Context context) {
            super(itemView);

            llNLevelParent = itemView.findViewById(R.id.llNLevelParent);
            rlNLevelImageParent = itemView.findViewById(R.id.rlNLevelImageParent);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);

            tvDescription1 = itemView.findViewById(R.id.tvDescription1);
            tvDescription2 = itemView.findViewById(R.id.tvDescription2);
            tvDescription3 = itemView.findViewById(R.id.tvDescription3);

            rlQuantitySelector = itemView.findViewById(R.id.rlQuantitySelector);
            linearLayoutQuantitySelector = itemView.findViewById(R.id.linearLayoutQuantitySelector);
            rlRemoveItem = itemView.findViewById(R.id.rlRemoveItem);
            rlAddItem = itemView.findViewById(R.id.rlAddItem);
            tvOutOfStockText = itemView.findViewById(R.id.tvOutOfStockText);
            tvOutOfStockText.setText(StorefrontCommonData.getTerminology().getOutOfStock());
            tvOutOfStockText.setVisibility(View.GONE);
            tvSingleSelectionButton = itemView.findViewById(R.id.tvSingleSelectionButton);
            tvSingleSelectionButton.setVisibility(View.GONE);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            ivForwardArrowButton = itemView.findViewById(R.id.ivForwardArrowButton);

            imageViewMinus = itemView.findViewById(R.id.imageViewMinus);
            imageViewPlus = itemView.findViewById(R.id.imageViewPlus);

            tvRecurring = itemView.findViewById(R.id.tvRecurring);
            tvRecurring.setText(StorefrontCommonData.getTerminology().getSubscriptionsAvailable());


        }
    }

    class ViewHolderCustomizeItem extends RecyclerView.ViewHolder {

        public TextView textViewSubCategoryName, tvMinSelection;

        public ViewHolderCustomizeItem(View itemView, Context context) {
            super(itemView);
            textViewSubCategoryName = itemView.findViewById(R.id.tvSubCategoryName);
            tvMinSelection = itemView.findViewById(R.id.tvMinSelection);
        }
    }

    class ViewHolderLongDescription extends RecyclerView.ViewHolder {

        public TextView tvLongDescriptionLabel, tvLongDescription;

        public ViewHolderLongDescription(View itemView, Context context) {
            super(itemView);
            tvLongDescriptionLabel = itemView.findViewById(R.id.tvLongDescriptionLabel);
            tvLongDescription = itemView.findViewById(R.id.tvLongDescription);
        }
    }

    class ViewHolderCustomizeOption extends RecyclerView.ViewHolder {

        public RelativeLayout cvRoot;
        public TextView tvCustomizeOptionItemName, tvCustomizeOptionItemPrice;
        private ImageView ivCustomizeOptionItem;
        private View vSep;

        public ViewHolderCustomizeOption(View itemView, Context context) {
            super(itemView);
            cvRoot = itemView.findViewById(R.id.cvRoot);
            ivCustomizeOptionItem = itemView.findViewById(R.id.ivCustomizeOptionItem);
            vSep = itemView.findViewById(R.id.vSep);
            tvCustomizeOptionItemName = itemView.findViewById(R.id.tvCustomizeOptionItemName);
            tvCustomizeOptionItemPrice = itemView.findViewById(R.id.tvCustomizeOptionItemPrice);
        }
    }

}
