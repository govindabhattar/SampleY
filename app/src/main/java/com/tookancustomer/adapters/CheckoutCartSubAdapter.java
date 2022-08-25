package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tookancustomer.NLevelWorkFlowActivity;
import com.tookancustomer.ProductCustomisationActivity;
import com.tookancustomer.R;
import com.tookancustomer.SearchProductActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.appConfiguration.AppConfigurationModel;
import com.tookancustomer.models.billbreakdown.BillBreakdownData;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.utility.CustomTypefaceSpan;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static com.tookancustomer.appdata.Codes.Request.OPEN_CUSTOMISATION_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_QUESTIONNAIRE_ACTIVITY;
import static com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class CheckoutCartSubAdapter extends RecyclerView.Adapter<CheckoutCartSubAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys, TerminologyStrings {
    private Activity activity;
    private Datum datum;
    private CheckoutCartAdapter cartRecyclerAdapter;
    private BottomSheetDialog dialog;
    private EditText etQuantity;

    public CheckoutCartSubAdapter(CheckoutCartAdapter cartRecyclerAdapter, Activity activity, Datum datum) {
        this.cartRecyclerAdapter = cartRecyclerAdapter;
        this.activity = activity;
        this.datum = datum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_cart, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();

        if (datum.getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
            holder.tvSingleSelectionBtnCheckout.setVisibility(View.GONE);
            holder.ivDeleteItem.setVisibility(View.GONE);
            holder.llMultiSelectLayout.setVisibility(View.VISIBLE);
        } else if (datum.getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.SELECT_TEXT_BUTTON.buttonValue) {
            holder.tvSingleSelectionBtnCheckout.setVisibility(View.VISIBLE);
            try {
                if (datum.getSelectedQuantity() > 0) {
                    holder.tvSingleSelectionBtnCheckout.setText(datum.getStorefrontData().getButtons().getButtonNames().getRemove());
                } else {
                    holder.tvSingleSelectionBtnCheckout.setText(datum.getStorefrontData().getButtons().getButtonNames().getAdd());
                }
            } catch (Exception e) {

                Utils.printStackTrace(e);
            }
            holder.llMultiSelectLayout.setVisibility(View.GONE);
            holder.ivDeleteItem.setVisibility(View.GONE);
        } else {
            holder.tvSingleSelectionBtnCheckout.setVisibility(View.GONE);
            holder.llMultiSelectLayout.setVisibility(View.GONE);
            holder.ivDeleteItem.setVisibility(View.VISIBLE);
        }
        int upSymbolUnicode = 0x2191;
        String text = Character.toString((char) upSymbolUnicode);
        if (datum.getSurgeAmount() > 0) {

            String subtotalData = StorefrontCommonData.getString(activity, R.string.surge).replace(SURGE, StorefrontCommonData.getTerminology().getSurge())
                    + " " + UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings()) + (datum.getSurgeAmount() * datum.getSelectedQuantity()));
            holder.tvsurgeAmount.setText("(" + subtotalData + ")" + text);

            holder.tvsurgeAmount.setVisibility(View.VISIBLE);
        } else {
            holder.tvsurgeAmount.setVisibility(View.GONE);

        }

        holder.tvCartName.setText(datum.getName());

        AppConfigurationModel appConfig = new AppConfigurationModel();


        if (!Dependencies.isLaundryApp() && StorefrontCommonData.getAppConfigurationData().getIsProductImageVisibleOnCheckout() == 1 && !datum.getImageUrl().isEmpty()) {
            try {
                holder.imageCV.setVisibility(View.VISIBLE);

                new GlideUtil.GlideUtilBuilder(holder.ivNLevelImage)
                        .setCenterCrop(true)
                        .setPlaceholder(R.drawable.ic_loading_image)
                        .setError(R.drawable.ic_image_placeholder)
                        .setFallback(R.drawable.ic_image_placeholder)
                        .setLoadItem(datum.getImageUrl())
                        .setThumbnailItem(datum.getThumbUrl())
                        .setTransformation(new RoundedCorners(6))
                        .build();

            } catch (Exception e) {
                holder.ivNLevelImage.setVisibility(View.GONE);
                holder.imageCV.setVisibility(View.GONE);
            }
        } else {
            holder.ivNLevelImage.setVisibility(View.GONE);
            holder.imageCV.setVisibility(View.GONE);

        }


        if (datum.getStorefrontData().getBusinessType().equals(Constants.BusinessType.SERVICES_BUSINESS_TYPE)
                && (datum.getStorefrontData().getPdOrAppointment().equals(Constants.ServiceFlow.PICKUP_DELIVERY)
                || (datum.getStorefrontData().getPdOrAppointment().equals(Constants.ServiceFlow.APPOINTMENT)))) {
            holder.llProductTiming.setVisibility(View.VISIBLE);
        } else {
            holder.llProductTiming.setVisibility(View.GONE);
        }

        Calendar startCal = Calendar.getInstance(), endCal = Calendar.getInstance();
        startCal.setTime(datum.getProductStartDate());
        endCal.setTime(datum.getProductEndDate());

        if (datum.getStorefrontData().getBusinessType().equals(Constants.BusinessType.SERVICES_BUSINESS_TYPE)
                && datum.getStorefrontData().getPdOrAppointment().equals(Constants.ServiceFlow.PICKUP_DELIVERY)
                && Constants.ProductsUnitType.getUnitType(datum.getUnitType()) == Constants.ProductsUnitType.FIXED
                && datum.getEnableTookanAgent() == 0) {
            holder.tvProductStartTime.setText(StorefrontCommonData.getTerminology().getStartTime(true) + ": "
                    + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(), UIManager.getDateTimeFormat()));
        } else {
            String productStartTimeString = "";

            if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                if (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) ==
                        endCal.get(Calendar.MONTH) && startCal.get(Calendar.DATE) == endCal.get(Calendar.DATE)) {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ": " + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(),
                            Constants.DateFormat.CHECKOUT_DATE_FORMAT);
                } else {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ":\n" + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(),
                            Constants.DateFormat.CHECKOUT_DATE_FORMAT) + " -\n" +
                            DateUtils.getInstance().getFormattedDate(datum.getProductEndDate(), Constants.DateFormat.CHECKOUT_DATE_FORMAT);
                }
            } else {
                if (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) ==
                        endCal.get(Calendar.MONTH) && startCal.get(Calendar.DATE) == endCal.get(Calendar.DATE)) {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ": "
                            + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(),
                            UIManager.getDateTimeFormat()) + " - " + DateUtils.getInstance().getFormattedDate(datum.getProductEndDate(),
                            UIManager.getTimeFormat());
                } else {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ":\n" + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(),
                            UIManager.getDateTimeFormat()) + " -\n" + DateUtils.getInstance().getFormattedDate(datum.getProductEndDate(),
                            UIManager.getDateTimeFormat());
                }
            }


            Spannable sb = new SpannableString(productStartTimeString);
//        sb.setSpan((Font.getSemiBold(getActivity())), checkoutString.indexOf(getString(R.string.checkout)), (getString(R.string.checkout).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
            sb.setSpan(new CustomTypefaceSpan("", Font.getBold(activity)), productStartTimeString.indexOf(StorefrontCommonData.getString(activity, R.string.duration)), (StorefrontCommonData.getString(activity, R.string.duration).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
            sb.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.medium_grey)),
                    productStartTimeString.indexOf(StorefrontCommonData.getString(activity, R.string.duration)), (StorefrontCommonData.getString(activity, R.string.duration).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvProductStartTime.setText(sb);
        }


        try {
            if (datum.getItemSelectedList().size() > 0) {
                holder.tvCartDesc.setText(datum.getItemSelectedList().get(adapterPos).getCustomizeText(datum));
                holder.tvCartDesc.setVisibility(holder.tvCartDesc.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
                holder.tvQuantity.setText(datum.getItemSelectedList().get(adapterPos).getQuantity() + "");


                if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && datum.getItemSelectedList().get(adapterPos).getTotalPrice() <= 0) {
                    holder.tvInitialPrice.setVisibility(View.GONE);
                } else {
                    holder.tvInitialPrice.setVisibility(View.VISIBLE);
                }

                if (datum.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(datum.getUnitType()) != Constants.ProductsUnitType.FIXED) {
                    holder.tvInitialPrice.setText(((UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings() != null ? datum.getPaymentSettings() : datum.getStorefrontData().getPaymentSettings())
                            + Utils.getDoubleTwoDigits(datum.getPrice().doubleValue()))) + " "
                            + (StorefrontCommonData.getString(activity, R.string.per_unitType).replace(UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(activity, datum.getUnit().intValue(), datum.getUnitType(), false)))));

                } else {
                    holder.tvInitialPrice.setText((UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings() != null ? datum.getPaymentSettings() : datum.getStorefrontData().getPaymentSettings())
                            + Utils.getDoubleTwoDigits(datum.getPrice().doubleValue()) + "")));
                    if (datum.getPrice().doubleValue() > 0) {
                        holder.tvInitialPrice.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvInitialPrice.setVisibility(View.GONE);

                    }

                }


            } else {
                holder.tvCartDesc.setVisibility(View.GONE);
                holder.tvQuantity.setText(datum.getSelectedQuantity() + "");
                if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && datum.getPrice().doubleValue() <= 0) {
                    if (!Dependencies.isLaundryApp())
                        holder.tvInitialPrice.setVisibility(View.GONE);
                } else {
                    holder.tvInitialPrice.setVisibility(View.VISIBLE);
                }

                if (datum.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(datum.getUnitType()) != Constants.ProductsUnitType.FIXED) {
                    holder.tvInitialPrice.setText(((UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings())
                            + Utils.getDoubleTwoDigits((datum.getPrice().doubleValue())))) + " "
                            + (StorefrontCommonData.getString(activity, R.string.per_unitType).replace(UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(activity, datum.getUnit().intValue(), datum.getUnitType(), false)))));
                } else {
                    if (Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings() != null)
                        holder.tvInitialPrice.setText((UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings() != null ? datum.getPaymentSettings() : Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings())
                                + Utils.getDoubleTwoDigits((datum.getPrice().doubleValue())))));

                    else
                        holder.tvInitialPrice.setText((UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings())
                                + Utils.getDoubleTwoDigits((datum.getPrice().doubleValue())))));
                }

            }
        } catch (Exception e) {
        }


        ArrayList<BillBreakdownData> productPricesList = new ArrayList<>();
        double productTotalCalculatedPrice = 0.0;

        TaxesModel taxesModel;
        if (datum.getItemSelectedList().size() > 0) {
            if (datum.getServiceTime() > 0) {
                productTotalCalculatedPrice = (((datum.getPrice().doubleValue() * Dependencies.getPredefiendInterval(datum)) + (datum.getItemSelectedList().get(adapterPos).getCustomizationPrice())) * datum.getItemSelectedList().get(adapterPos).getQuantity());

            } else if (Dependencies.getInterval(datum) == 0.0) {
                productTotalCalculatedPrice = (((datum.getPrice().doubleValue() * 1) + (datum.getItemSelectedList().get(adapterPos).getCustomizationPrice())) * datum.getItemSelectedList().get(adapterPos).getQuantity());

            } else {
                productTotalCalculatedPrice = (((datum.getPrice().doubleValue() * Dependencies.getInterval(datum)) + (datum.getItemSelectedList().get(adapterPos).getCustomizationPrice())) * datum.getItemSelectedList().get(adapterPos).getQuantity());
            }
            productTotalCalculatedPrice = productTotalCalculatedPrice + datum.getQuestionnaireTemplateCost();
            productTotalCalculatedPrice = productTotalCalculatedPrice + (datum.getSurgeAmount() * datum.getItemSelectedList().get(adapterPos).getQuantity());

            productPricesList.add(new BillBreakdownData(StorefrontCommonData.getString(activity, R.string.product_subtotal).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()),
                    new BigDecimal(Utils.getDoubleTwoDigits(Double.parseDouble(Utils.getDoubleTwoDigits((productTotalCalculatedPrice))))), false, null,
                    ((datum.getPaymentSettings() != null
                            && datum.getPaymentSettings().getSymbol() != null)
                            ? datum.getPaymentSettings().getSymbol() : null), null));

        } else {
            if (datum.getServiceTime() > 0) {
                productTotalCalculatedPrice = (datum.getSelectedQuantity() * datum.getPrice().doubleValue() * Dependencies.getPredefiendInterval(datum));

            } else if (Dependencies.getInterval(datum) == 0.0) {
                productTotalCalculatedPrice = (datum.getSelectedQuantity() * datum.getPrice().doubleValue() * 1);
            } else
                productTotalCalculatedPrice = (datum.getSelectedQuantity() * datum.getPrice().doubleValue() * Dependencies.getInterval(datum));

            productTotalCalculatedPrice = productTotalCalculatedPrice + datum.getQuestionnaireTemplateCost();
            productTotalCalculatedPrice = productTotalCalculatedPrice + (datum.getSurgeAmount() * datum.getSelectedQuantity());

            if (datum.getPaymentSettings() != null)
                productPricesList.add(new BillBreakdownData(StorefrontCommonData.getString(activity, R.string.product_subtotal).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()),
                        BigDecimal.valueOf(productTotalCalculatedPrice), false, null,
                        datum.getPaymentSettings().getSymbol(), null));
            else
                productPricesList.add(new BillBreakdownData(StorefrontCommonData.getString(activity, R.string.product_subtotal).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()),
                        BigDecimal.valueOf(productTotalCalculatedPrice), false, null,
                        ((Dependencies.getSelectedProductsArrayList().size() > 0 && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings() != null)
                                ? Dependencies.getSelectedProductsArrayList().get(0).
                                getPaymentSettings().getSymbol() : null), null));
        }

       /* if (!(StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && Double.valueOf(taxesModel.getTaxAmount()) <= 0))
            productPricesList.add(taxesModel);*/

        for (int i = 0; i < datum.getTaxesArrayList().size(); i++) {
            if (datum.getTaxesArrayList().get(i).getTaxType() == 1) {
                datum.getTaxesArrayList().get(i).setTaxAmount(BigDecimal.valueOf(datum.getTaxesArrayList().get(i).getTaxPercentage()));

                if (datum.getPaymentSettings() != null)
                    datum.getTaxesArrayList().get(i).setCurrencySymbol(datum.getPaymentSettings().getSymbol());
                else
                    datum.getTaxesArrayList().get(i).setCurrencySymbol((datum.getStorefrontData().getPaymentSettings() != null && !datum.getStorefrontData().getPaymentSettings().getSymbol().isEmpty()) ? datum.getStorefrontData().getPaymentSettings().getSymbol() : "");

                productPricesList.add(new BillBreakdownData(datum.getTaxesArrayList().get(i).getTaxName(),
                        new BigDecimal( datum.getTaxesArrayList().get(i).getTaxAmount()),
                        false,
                        datum.getTaxesArrayList().get(i).getTaxPercentage(),
                        datum.getTaxesArrayList().get(i).getTaxType()));
                productTotalCalculatedPrice = productTotalCalculatedPrice + Double.valueOf(datum.getTaxesArrayList().get(i).getTaxAmount());
            } else {
                datum.getTaxesArrayList().get(i).setTaxAmount(BigDecimal.valueOf(
                        (datum.getTaxesArrayList().get(i).getTaxPercentage() * ((Double.valueOf(datum.getTaxesArrayList().get(i).getTaxAmount()) /*+ datum.getQuestionnaireTemplateCost()*/))) / 100
                ));

                if (datum.getPaymentSettings() != null)
                    datum.getTaxesArrayList().get(i).setCurrencySymbol(datum.getPaymentSettings().getSymbol());
                else
                    datum.getTaxesArrayList().get(i).setCurrencySymbol((datum.getStorefrontData().getPaymentSettings() != null && !datum.getStorefrontData().getPaymentSettings().getSymbol().isEmpty()) ? datum.getStorefrontData().getPaymentSettings().getSymbol() : "");

                if (!(Double.valueOf(datum.getTaxesArrayList().get(i).getTaxAmount()) <= 0))
                    productPricesList.add(new BillBreakdownData(datum.getTaxesArrayList().get(i).getTaxName(),
                            new BigDecimal(datum.getTaxesArrayList().get(i).getTaxAmount()),
                            false,
                            datum.getTaxesArrayList().get(i).getTaxPercentage(),
                            datum.getTaxesArrayList().get(i).getTaxType()));
                productTotalCalculatedPrice = productTotalCalculatedPrice + Double.valueOf(datum.getTaxesArrayList().get(i).getTaxAmount());
            }
        }


        holder.viewQuestionnaireTV.setText(CustomViewsUtil.createSpan(activity,
                StorefrontCommonData.getString(activity, R.string.productInfo).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()),
                " (", StorefrontCommonData.getString(activity, R.string.edit), ")", ContextCompat.getColor(activity, R.color.colorPrimary)));

        holder.viewQuestionnaireTV.setTag(position);

        if (datum.getIsProductTemplateEnabled() == 1 && datum.getQuestionnaireTemplate() != null) {
//            productTotalCalculatedPrice = productTotalCalculatedPrice + datum.getQuestionnaireTemplateCost();
            holder.questionnaireAmountTV.setText((UIManager.getCurrency(Utils.getCurrencySymbol(datum.getPaymentSettings()) + " " + Utils.getDoubleTwoDigits(datum.getQuestionnaireTemplateCost()))));
            holder.questionnaireRL.setVisibility(View.VISIBLE);
        } else {
            holder.questionnaireRL.setVisibility(View.GONE);
        }

        holder.viewQuestionnaireTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Intent templateIntent = new Intent(activity, QuestionnaireTemplateActivity.class);
                if (datum.getQuestionnaireTemplate() != null) {
                    templateIntent.putExtra(UPDATE_QUESTIONNAIRE, true);
                    templateIntent.putExtra(KEY_ITEM_POSITION, pos);
                    templateIntent.putExtra(PRODUCT_CATALOGUE_DATA, datum);
                    templateIntent.putExtra(EXTRA_TEMPLATE_LIST, datum.getQuestionnaireTemplate());
                    activity.startActivityForResult(templateIntent, OPEN_QUESTIONNAIRE_ACTIVITY);
                }
            }
        });

        BillBreakdownAdapter productPricesAdapter = new BillBreakdownAdapter(activity, productPricesList, true);
        holder.rvProductTaxes.setAdapter(productPricesAdapter);

        if (datum.getItemSelectedList().size() > 0) {
            datum.getItemSelectedList().get(adapterPos).setProductTotalCalculatedPrice(productTotalCalculatedPrice);
        } else {
            datum.setProductTotalCalculatedPrice(productTotalCalculatedPrice);
        }
        setSubTotal();
        final int totalSelectedQuantity;
        // if (etQuantity != null && !etQuantity.getText().toString().isEmpty()) {
        //   totalSelectedQuantity = Integer.parseInt(etQuantity.getText().toString());
        //} else {
        totalSelectedQuantity = datum.getSelectedQuantity();
        //}

        holder.rlAddItem.setTag(position);
        holder.rlAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();

                actionItem(pos, datum.getSelectedQuantity(), adapterPos, false, holder);
        /*        if (datum.getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                    int pos = (int) v.getTag();

                    int qty;
                    if (datum.getItemSelectedList().size() > 0)
                        qty = datum.getItemSelectedList().get(pos).getQuantity();
                    else
                        qty = datum.getSelectedQuantity();

                    if (datum.getMaxProductquantity() == 0 || qty < datum.getMaxProductquantity()) {

                        if (datum.getInventoryEnabled() == 0 || (datum.getSelectedQuantity() < datum.getAvailableQuantity() && datum.getInventoryEnabled() == 1)) {
                            datum.setSelectedQuantity(totalSelectedQuantity + 1);
                            if (datum.getItemSelectedList().size() > 0)
                                datum.getItemSelectedList().get(adapterPos).setQuantity(datum.getItemSelectedList().get(adapterPos).getQuantity() + 1);


                            Dependencies.addCartItem(activity, datum);
                            setSubTotal();
                            notifyItemChanged(adapterPos);
                        } else {
                            Utils.showToast(activity, StorefrontCommonData.getString(activity, R.string.order_quantity_limited));
                        }
                    } else {
                        String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                                .replace(NAME, datum.getName())
                                .replace(QUANTITY, datum.getMaxProductquantity() + "")
                                .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                        Utils.snackBar(activity, message);

                    }
                }*/
            }
        });

        holder.rlRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalSelectedQuantity <= 0) {
                    datum.setSelectedQuantity(0);
                    notifyWhenRemovePressed(adapterPos);
                } else {
                    if (datum.getMinProductquantity() >= 1) {
                        String message = (StorefrontCommonData.getString(activity, R.string.text_quantity_cannot_be_less_than_for_product) + StorefrontCommonData.getString(activity, R.string.text_remove_product_from_cart))
                                .replace(NAME, datum.getName())
                                .replace(QUANTITY, datum.getMinProductquantity() + "")
                                .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())
                                .replace(CART, StorefrontCommonData.getTerminology().getCart());

                        if (datum.getItemSelectedList().size() > 0 &&
                                datum.getItemSelectedList().get(adapterPos).getQuantity() <= datum.getMinProductquantity()) {
                            new OptionsDialog.Builder(activity)
                                    .message(message)
                                    .positiveButton(StorefrontCommonData.getString(activity, R.string.remove))
                                    .negativeButton(StorefrontCommonData.getString(activity, R.string.retain))
                                    .listener(new OptionsDialog.Listener() {
                                        @Override
                                        public void performPositiveAction(int purpose, Bundle backpack) {
                                            datum.setSelectedQuantity(datum.getSelectedQuantity() -
                                                    datum.getItemSelectedList().get(adapterPos).getQuantity());
                                            datum.getItemSelectedList().get(adapterPos).setQuantity(0);
                                            notifyWhenRemovePressed(adapterPos);
                                        }

                                        @Override
                                        public void performNegativeAction(int purpose, Bundle backpack) {
                                        }
                                    }).build().show();

                        } else {
                            if (datum.getSelectedQuantity() <= datum.getMinProductquantity() &&
                                    datum.getMinProductquantity() != 1) {
                                new OptionsDialog.Builder(activity)
                                        .message(message)
                                        .positiveButton(StorefrontCommonData.getString(activity, R.string.remove))
                                        .negativeButton(StorefrontCommonData.getString(activity, R.string.retain))
                                        .listener(new OptionsDialog.Listener() {
                                            @Override
                                            public void performPositiveAction(int purpose, Bundle backpack) {
                                                datum.setSelectedQuantity(0);
                                                notifyWhenRemovePressed(adapterPos);
                                            }

                                            @Override
                                            public void performNegativeAction(int purpose, Bundle backpack) {
                                            }
                                        }).build().show();
                            } else {
                                datum.setSelectedQuantity(totalSelectedQuantity - 1);
                                notifyWhenRemovePressed(adapterPos);
                            }

                        }
                    }
                }


            }
        });

        holder.tvSingleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalSelectedQuantity == 0) {
                    holder.rlAddItem.performClick();
                } else {
                    holder.rlRemoveItem.performClick();
                }
            }
        });
        holder.tvSingleSelectionBtnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalSelectedQuantity == 0) {
                    holder.rlAddItem.performClick();
                } else {
                    holder.rlRemoveItem.performClick();
                }
            }
        });

        holder.ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalSelectedQuantity == 0) {
                    holder.rlAddItem.performClick();
                } else {
                    holder.rlRemoveItem.performClick();
                }
            }
        });

        if (datum.getCustomizeItem().size() > 0) {
            holder.tvEditCustomization.setVisibility(View.VISIBLE);
        } else {
            holder.tvEditCustomization.setVisibility(View.GONE);
        }

        holder.tvEditCustomization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datum.getCustomizeItem().size() > 0) {
                    setItemCustomisation(position, holder.tvQuantity.getText().toString());
                }
            }
        });

        if (datum.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                datum.getReturn_enabled() == 1) {
            holder.rbtnReturn.setVisibility(View.VISIBLE);
        } else {
            holder.rbtnReturn.setVisibility(View.GONE);
        }

        holder.rbtnReturn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                datum.setIsReturn(b ? 1 : 0);
            }
        });


        holder.rbtnReturn.setChecked(datum.getIsReturn() == 1);


        if (datum.getMinProductquantity() > 1) {
            holder.tvMinProductQuan.setVisibility(View.VISIBLE);
            holder.tvMinProductQuan.setText(StorefrontCommonData.getString(activity, R.string.text_minimum_product_quantity) +
                    datum.getMinProductquantity());
        } else {
            holder.tvMinProductQuan.setVisibility(View.GONE);
        }
        if (datum.getMaxProductquantity() > 1) {
            holder.tvMaxProductQuan.setVisibility(View.VISIBLE);
            holder.tvMaxProductQuan.setText(StorefrontCommonData.getString(activity, R.string.text_maximum_product_quantity) +
                    datum.getMaxProductquantity());
        } else {
            holder.tvMaxProductQuan.setVisibility(View.GONE);
        }

        holder.tvQuantity.setTag(position);
        holder.tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                openBottomSheet(holder.tvQuantity, holder.getAdapterPosition(), datum, pos, holder);
            }
        });
    }

    public void setItemCustomisation(int position, String quantity) {
        Intent intent = new Intent(activity, ProductCustomisationActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, position);

        if (activity instanceof NLevelWorkFlowActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((NLevelWorkFlowActivity) activity).pickLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((NLevelWorkFlowActivity) activity).pickLongitude);
        } else if (activity instanceof SearchProductActivity) {
            intent.putExtra(PICKUP_LATITUDE, ((SearchProductActivity) activity).pickupLatitude);
            intent.putExtra(PICKUP_LONGITUDE, ((SearchProductActivity) activity).pickupLongitude);
        }

        intent.putExtra(PRODUCT_CATALOGUE_DATA, datum);
        intent.putExtra(PRODUCT_QUANTITY, quantity);
        intent.putExtra(PRODUCT_PRICE, getSubTotal(position));
        intent.putExtra(IS_EDIT_CUSTOMIZATION, true);
        activity.startActivityForResult(intent, OPEN_CUSTOMISATION_ACTIVITY);
    }

    private void actionItem(int posi, int totalSelectedQuantity, int adapterPos, boolean isQuantityEditable, ViewHolder holder) {
        if (datum.getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
            int qty;

            if (isQuantityEditable) {
                qty = totalSelectedQuantity;

                if (datum.getItemSelectedList().size() > 0) {
                    int customizedQnty = 0;
                    for (int i = 0; i < datum.getItemSelectedList().size(); i++) {
                        if (i != adapterPos) {
                            customizedQnty = customizedQnty + datum.getItemSelectedList().get(i).getQuantity();
                        }
                    }
                    qty = customizedQnty + qty;
                }

            } else if (datum.getItemSelectedList().size() > 0) {
                qty = datum.getItemSelectedList().get(posi).getQuantity();
                if (datum.getItemSelectedList().size() > 0) {
                    int customizedQnty = 0;
                    for (int i = 0; i < datum.getItemSelectedList().size(); i++) {
                        if (i != adapterPos) {
                            customizedQnty = customizedQnty + datum.getItemSelectedList().get(i).getQuantity();
                        }
                    }
                    qty = customizedQnty + qty;
                }
            } else
                qty = datum.getSelectedQuantity();

            boolean quantityCheck;
            boolean minQuantityCheck;
            if (isQuantityEditable) {
                quantityCheck = qty <= datum.getMaxProductquantity();
                minQuantityCheck = qty < datum.getMinProductquantity();
            } else {
                quantityCheck = qty < datum.getMaxProductquantity();
                minQuantityCheck = qty < datum.getMinProductquantity();
            }
            if (minQuantityCheck) {
                String message = (StorefrontCommonData.getString(activity, R.string.text_quantity_cannot_be_less_than_for_product) + StorefrontCommonData.getString(activity, R.string.text_remove_product_from_cart))
                        .replace(NAME, datum.getName())
                        .replace(QUANTITY, datum.getMinProductquantity() + "")
                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct())
                        .replace(CART, StorefrontCommonData.getTerminology().getCart());
                Utils.snackBar(activity, message);

            } else {
                if (datum.getMaxProductquantity() == 0 || quantityCheck) {
                    boolean availableinventry;

                    if (isQuantityEditable)
                        availableinventry = totalSelectedQuantity <= datum.getAvailableQuantity();
                    else
                        availableinventry = totalSelectedQuantity < datum.getAvailableQuantity();


                    if (datum.getInventoryEnabled() == 0 || (availableinventry && datum.getInventoryEnabled() == 1)) {


                        if (isQuantityEditable)
                            datum.setSelectedQuantity(totalSelectedQuantity);
                        else
                            datum.setSelectedQuantity(totalSelectedQuantity + 1);


                        if (datum.getItemSelectedList().size() > 0)
                            if (isQuantityEditable)
                                datum.getItemSelectedList().get(adapterPos).setQuantity(totalSelectedQuantity);
                            else
                                datum.getItemSelectedList().get(adapterPos).setQuantity(datum.getItemSelectedList().get(adapterPos).getQuantity() + 1);


                        if (datum.getItemSelectedList().size() > 0) {
                            int totalQty = 0;

                            for (int i = 0; i < datum.getItemSelectedList().size(); i++) {
                                totalQty = totalQty + datum.getItemSelectedList().get(i).getQuantity();
                            }
                            datum.setSelectedQuantity(totalQty);
                        }

                        Dependencies.addCartItem(activity, datum);
                        setSubTotal();
                        notifyItemChanged(adapterPos);
                        if (isQuantityEditable) {
                            holder.tvQuantity.setText(etQuantity.getText().toString());
                        }
                    } else {
                        Utils.showToast(activity, StorefrontCommonData.getString(activity, R.string.order_quantity_limited));
                    }
                } else {
                    String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
                            .replace(NAME, datum.getName())
                            .replace(QUANTITY, datum.getMaxProductquantity() + "")
                            .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
                    Utils.snackBar(activity, message);

                }
            }


            //int pos = (int) v.getTag();
//            int qty;
//            if (datum.getItemSelectedList().size() > 0)
//
//                qty = datum.getItemSelectedList().get(pos).getQuantity();
//            else {
//                qty = datum.getSelectedQuantity();
//            }
//            if (datum.getMaxProductquantity() == 0 || qty < datum.getMaxProductquantity()) {
//
//                if (datum.getInventoryEnabled() == 0 || (datum.getSelectedQuantity() < datum.getAvailableQuantity() && datum.getInventoryEnabled() == 1)) {
//                    if (isQuantityEditable) {
//                        datum.setSelectedQuantity(totalSelectedQuantity);
//                    } else {
//                        datum.setSelectedQuantity(qty);
//                    }
//
//                    if (datum.getItemSelectedList().size() > 0)
//                        if (isQuantityEditable) {
//                            datum.getItemSelectedList().get(adapterPos).setQuantity(totalSelectedQuantity);
//                        } else {
//                            datum.getItemSelectedList().get(adapterPos).setQuantity(datum.getItemSelectedList().get(adapterPos).getQuantity() + 1);
//                        }
//
//
//                    Dependencies.addCartItem(activity, datum);
//                    setSubTotal();
//                    notifyItemChanged(adapterPos);
//                    if (isQuantityEditable) {
//                        holder.tvQuantity.setText(etQuantity.getText().toString());
//                    }
//
//                } else {
//                    Utils.showToast(activity, StorefrontCommonData.getString(activity, R.string.order_quantity_limited));
//                }
//            } else {
//                String message = (StorefrontCommonData.getString(activity, R.string.maximum_quantity_error))
//                        .replace(NAME, datum.getName())
//                        .replace(QUANTITY, datum.getMaxProductquantity() + "")
//                        .replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct());
//                Utils.snackBar(activity, message);
//
//            }
        }
    }

    private void openBottomSheet(final TextView textViewQuantity, final int adapterPosition, final Datum productData, final int tag, final ViewHolder holder) {
        View view = activity.getLayoutInflater().inflate(R.layout.addremove_bottom_sheet, null);
        dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.show();

        final Button btnContinue = dialog.findViewById(R.id.btnContinue);
        btnContinue.setText(StorefrontCommonData.getString(activity, R.string.save));

        etQuantity = dialog.findViewById(R.id.etQuantity);
        etQuantity.setText(textViewQuantity.getText().toString());

        TextView tvItemName = dialog.findViewById(R.id.tvItemName);
        TextView tvItemDescription = dialog.findViewById(R.id.tvItemDescription);
        ImageView ivItemImage = dialog.findViewById(R.id.ivItemImage);
        ImageView ivCloseSheet = dialog.findViewById(R.id.ivCloseSheet);
        TextView tvEditQuantity = dialog.findViewById(R.id.tvEditQuantity);
        tvEditQuantity.setText(StorefrontCommonData.getString(activity, R.string.edit_quantity));


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
                    actionItem(tag, (Integer.parseInt(etQuantity.getText().toString())), adapterPosition, true, holder);
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

    private void notifyWhenRemovePressed(int adapterPos) {
        if (datum.getItemSelectedList().size() > 0) {
            if (datum.getItemSelectedList().get(adapterPos).getQuantity() <= 0) {
                datum.getItemSelectedList().get(adapterPos).setQuantity(0);

            } else {
                datum.getItemSelectedList().get(adapterPos).setQuantity(datum.getItemSelectedList().get(adapterPos).getQuantity() - 1);
            }
        }

        if (datum.getSelectedQuantity() <= 0) {
            //TODO

            //    ((CheckOutActivity) activity).makeSideOrderProductVisible(datum.getProductId());
        }

//                if (datum.getItemSelectedList().size() > 0 && datum.getItemSelectedList().get(adapterPos).getQuantity() == 0) {
//                    datum.getItemSelectedList().remove(adapterPos);
//                }
        notifyDataSetChanged();

        Dependencies.addCartItem(activity, datum);
        setSubTotal();
        if (cartRecyclerAdapter != null) {
            cartRecyclerAdapter.notifyDataSetChanged();
        }

        //notifyItemChanged(position);

        if (Dependencies.getSelectedProductsArrayList().size() == 0) {
            activity.onBackPressed();
        }
    }

    public void setSubTotal() {
        if (cartRecyclerAdapter != null) {
            cartRecyclerAdapter.setSubtotal();
        }
    }

    private double getSubTotal(int adapterPos) {
        double productTotalCalculatedPrice = 0.0;
        if (datum.getItemSelectedList().size() > 0) {

            if (datum.getServiceTime() > 0) {
                productTotalCalculatedPrice = (((datum.getPrice().doubleValue() * Dependencies.getPredefiendInterval(datum)) + (datum.getItemSelectedList().get(adapterPos).getCustomizationPrice())) * datum.getItemSelectedList().get(adapterPos).getQuantity());

            } else if (Dependencies.getInterval(datum) == 0.0) {
                productTotalCalculatedPrice = (((datum.getPrice().doubleValue() * 1) + (datum.getItemSelectedList().get(adapterPos).getCustomizationPrice())) * datum.getItemSelectedList().get(adapterPos).getQuantity());

            } else {
                productTotalCalculatedPrice = (((datum.getPrice().doubleValue() * Dependencies.getInterval(datum)) + (datum.getItemSelectedList().get(adapterPos).getCustomizationPrice())) * datum.getItemSelectedList().get(adapterPos).getQuantity());
            }
            productTotalCalculatedPrice = productTotalCalculatedPrice + datum.getQuestionnaireTemplateCost();
            productTotalCalculatedPrice = productTotalCalculatedPrice + (datum.getSurgeAmount() * datum.getItemSelectedList().get(adapterPos).getQuantity());
        }
        return productTotalCalculatedPrice;
    }

    @Override
    public int getItemCount() {
        if (datum.getItemSelectedList().size() > 0) {
            return datum.getItemSelectedList().size();
        } else {
            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCartName, tvCartDesc, tvQuantity, tvInitialPrice,
                tvTotalPrice, tvSingleSelectionButton, tvsurgeAmount,
                tvSingleSelectionBtnCheckout, tvProductStartTime, tvMinProductQuan, tvMaxProductQuan,
                viewQuestionnaireTV, questionnaireAmountTV, tvEditCustomization;
        private LinearLayout linearLayoutQuantitySelector, llMultiSelectLayout, llProductTiming;
        private RelativeLayout rlRemoveItem, rlAddItem, questionnaireRL;
        private CheckBox rbtnReturn;
        private ImageView ivDeleteItem;
        private CardView imageCV;
        private TextView addText;


        private RecyclerView rvProductTaxes;
        private ImageView ivNLevelImage;

        ViewHolder(View itemView) {
            super(itemView);
            tvSingleSelectionButton = itemView.findViewById(R.id.tvSingleSelectionButton);
            tvsurgeAmount = itemView.findViewById(R.id.tvsurgeAmount);
            tvSingleSelectionBtnCheckout = itemView.findViewById(R.id.tvSingleSelectionBtnCheckout);
            linearLayoutQuantitySelector = itemView.findViewById(R.id.linearLayoutQuantitySelector);
            llMultiSelectLayout = itemView.findViewById(R.id.llMultiSelectLayout);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartDesc = itemView.findViewById(R.id.tvCartDesc);
            tvEditCustomization = itemView.findViewById(R.id.tvEditCustomization);
            tvQuantity = itemView.findViewById(R.id.textViewQuantity);
            addText = itemView.findViewById(R.id.addText);


            tvInitialPrice = itemView.findViewById(R.id.tvInitialPrice);
            questionnaireAmountTV = itemView.findViewById(R.id.questionnaireAmountTV);
            viewQuestionnaireTV = itemView.findViewById(R.id.viewQuestionnaireTV);
            questionnaireRL = itemView.findViewById(R.id.questionnaireRL);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvTotalPrice.setVisibility(View.GONE);
            rlRemoveItem = itemView.findViewById(R.id.rlRemoveItem);
            rlAddItem = itemView.findViewById(R.id.rlAddItem);
            rbtnReturn = itemView.findViewById(R.id.rbtnReturn);
            ivDeleteItem = itemView.findViewById(R.id.ivDeleteItem);

            tvMinProductQuan = itemView.findViewById(R.id.tvMinProductQuan);
            tvMaxProductQuan = itemView.findViewById(R.id.tvMaxProductQuan);

            llProductTiming = itemView.findViewById(R.id.llProductTiming);
            tvProductStartTime = itemView.findViewById(R.id.tvProductStartTime);

            rvProductTaxes = itemView.findViewById(R.id.rvProductTaxes);
            rvProductTaxes.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

            imageCV = itemView.findViewById(R.id.imageCV);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);
        }

    }
}
