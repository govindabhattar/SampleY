package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tookancustomer.CheckOutActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.utility.CustomTypefaceSpan;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.Calendar;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys, TerminologyStrings {
    private Activity activity;
    private Datum datum;
    private CartRecyclerAdapter cartRecyclerAdapter;

    public CartAdapter(CartRecyclerAdapter cartRecyclerAdapter, Activity activity, Datum datum) {
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

        if (datum.getLayoutData().getButtons().get(0).getType() != Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
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
        } else {
            holder.tvSingleSelectionBtnCheckout.setVisibility(View.GONE);
            holder.llMultiSelectLayout.setVisibility(View.VISIBLE);
        }

        holder.tvCartName.setText(datum.getName());
        if (datum.getStorefrontData().getBusinessType().equals(Constants.BusinessType.SERVICES_BUSINESS_TYPE)) {
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
            holder.tvProductStartTime.setText(StorefrontCommonData.getTerminology().getStartTime(true) + ": " +
                    DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(), UIManager.getDateTimeFormat()));
        } else {
            String productStartTimeString = "";

            if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                if (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH) && startCal.get(Calendar.DATE) == endCal.get(Calendar.DATE)) {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ": " + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(), Constants.DateFormat.CHECKOUT_DATE_FORMAT);
                } else {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ":\n" + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(), Constants.DateFormat.CHECKOUT_DATE_FORMAT) + " -\n" + DateUtils.getInstance().getFormattedDate(datum.getProductEndDate(), Constants.DateFormat.CHECKOUT_DATE_FORMAT);
                }
            } else {
                if (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH) && startCal.get(Calendar.DATE) == endCal.get(Calendar.DATE)) {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ": " + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(),
                            UIManager.getDateTimeFormat()) + " - " + DateUtils.getInstance().getFormattedDate(datum.getProductEndDate(),
                            UIManager.getTimeFormat());
                } else {
                    productStartTimeString = StorefrontCommonData.getString(activity, R.string.duration) + ":\n" + DateUtils.getInstance().getFormattedDate(datum.getProductStartDate(),
                            UIManager.getDateTimeFormat() + " -\n" + DateUtils.getInstance().getFormattedDate(datum.getProductEndDate(),
                                    UIManager.getDateTimeFormat()));
                }
            }


            Spannable sb = new SpannableString(productStartTimeString);
//        sb.setSpan((Font.getSemiBold(getActivity())), checkoutString.indexOf(getString(R.string.checkout)), (getString(R.string.checkout).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
            sb.setSpan(new CustomTypefaceSpan("", Font.getBold(activity)), productStartTimeString.indexOf(StorefrontCommonData.getString(activity, R.string.duration)), (StorefrontCommonData.getString(activity, R.string.duration).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //bold
            sb.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.medium_grey)), productStartTimeString.indexOf(StorefrontCommonData.getString(activity, R.string.duration)), (StorefrontCommonData.getString(activity, R.string.duration).length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvProductStartTime.setText(sb);
        }


        try {
            if (datum.getItemSelectedList().size() > 0) {
                holder.tvCartDesc.setText(datum.getItemSelectedList().get(adapterPos).getCustomizeText(datum));
                holder.tvCartDesc.setVisibility(holder.tvCartDesc.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
                holder.tvQuantity.setText(datum.getItemSelectedList().get(adapterPos).getQuantity() + "");
                holder.tvInitialPrice.setText((UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(datum.getItemSelectedList().get(adapterPos).getTotalPrice()) + "")));

                if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && datum.getItemSelectedList().get(adapterPos).getTotalPrice() <= 0) {
                    holder.tvInitialPrice.setVisibility(View.GONE);
                } else {
                    holder.tvInitialPrice.setVisibility(View.VISIBLE);
                }
                if (datum.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(datum.getUnitType()) != Constants.ProductsUnitType.FIXED) {
                    holder.tvTotalPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(datum.getItemSelectedList().get(adapterPos).getTotalPriceWithQuantity())) + " "
                            + (StorefrontCommonData.getString(activity, R.string.per_unitType).replace(UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(activity, datum.getUnit().intValue(), datum.getUnitType(), false))));
                } else {
                    holder.tvTotalPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(datum.getItemSelectedList().get(adapterPos).getTotalPriceWithQuantity())));
                }

                if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && datum.getItemSelectedList().get(adapterPos).getTotalPriceWithQuantity() <= 0) {
                    holder.tvTotalPrice.setVisibility(View.GONE);
                } else {
                    holder.tvTotalPrice.setVisibility(View.VISIBLE);
                }

            } else {
                holder.tvCartDesc.setVisibility(View.GONE);
                holder.tvQuantity.setText(datum.getSelectedQuantity() + "");
                holder.tvInitialPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(datum.getPrice().doubleValue())));
                if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && datum.getPrice().doubleValue() <= 0) {
                    holder.tvInitialPrice.setVisibility(View.GONE);
                } else {
                    holder.tvInitialPrice.setVisibility(View.VISIBLE);
                }

                if (datum.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && Constants.ProductsUnitType.getUnitType(datum.getUnitType()) != Constants.ProductsUnitType.FIXED) {
                    holder.tvTotalPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits((datum.getPrice().doubleValue() * datum.getSelectedQuantity().doubleValue()))) + " " +
                            (StorefrontCommonData.getString(activity, R.string.per_unitType).replace(UNIT_TYPE, Constants.ProductsUnitType.getUnitTypeText(activity, datum.getUnit().intValue(), datum.getUnitType(), false))));
                } else {
                    holder.tvTotalPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits((datum.getPrice().doubleValue() * datum.getSelectedQuantity().doubleValue()))));
                }
                if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && (datum.getPrice().doubleValue() * datum.getSelectedQuantity().doubleValue()) <= 0) {
                    holder.tvTotalPrice.setVisibility(View.GONE);
                } else {
                    holder.tvTotalPrice.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
        }

        final int totalSelectedQuantity = datum.getSelectedQuantity();

        setSubTotal();

        holder.rlAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datum.getLayoutData().getButtons().get(0).getType() == Constants.NLevelButtonType.ADD_AND_REMOVE_BUTTON.buttonValue) {
                    if (datum.getMaxProductquantity() == 0 || datum.getSelectedQuantity() < datum.getMaxProductquantity()) {
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
                }
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

               /* if (datum.getItemSelectedList().size() > 0) {
                    if (datum.getItemSelectedList().get(adapterPos).getQuantity() <= 0) {
                        datum.getItemSelectedList().get(adapterPos).setQuantity(0);
                    } else {
                        datum.getItemSelectedList().get(adapterPos).setQuantity(datum.getItemSelectedList().get(adapterPos).getQuantity() - 1);
                    }
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
                    ((CheckOutActivity) activity).performBackAction();
                }*/
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

        holder.tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(holder.tvQuantity, holder, holder.getAdapterPosition(), datum);
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

       /* holder.rbtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rbtnReturn.setChecked(!holder.rbtnReturn.isChecked());
                datum.setIsReturn(holder.rbtnReturn.isChecked() ? 1 : 0);
            }
        });*/

        holder.rbtnReturn.setChecked(datum.getIsReturn() == 1);


        if (datum.getMinProductquantity() > 1) {
            holder.tvMinProductQuan.setVisibility(View.VISIBLE);
            holder.tvMinProductQuan.setText(StorefrontCommonData.getString(activity, R.string.text_minimum_product_quantity) + datum.getMinProductquantity());
        } else {
            holder.tvMinProductQuan.setVisibility(View.GONE);
        }

    }


    private void notifyWhenRemovePressed(int adapterPos) {
        if (datum.getItemSelectedList().size() > 0) {
            if (datum.getItemSelectedList().get(adapterPos).getQuantity() <= 0) {
                datum.getItemSelectedList().get(adapterPos).setQuantity(0);
            } else {
                datum.getItemSelectedList().get(adapterPos).setQuantity(datum.getItemSelectedList().get(adapterPos).getQuantity() - 1);
            }
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
            ((CheckOutActivity) activity).onBackPressed();
        }
    }

    public void setSubTotal() {
        if (cartRecyclerAdapter != null) {
            cartRecyclerAdapter.setSubtotal();
        }
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
                tvTotalPrice, tvSingleSelectionButton,
                tvSingleSelectionBtnCheckout, tvProductStartTime, tvMinProductQuan;
        private LinearLayout linearLayoutQuantitySelector, llMultiSelectLayout, llProductTiming;
        private RelativeLayout rlRemoveItem, rlAddItem;
        private CheckBox rbtnReturn;

        ViewHolder(View itemView) {
            super(itemView);
            tvSingleSelectionButton = itemView.findViewById(R.id.tvSingleSelectionButton);
            tvSingleSelectionBtnCheckout = itemView.findViewById(R.id.tvSingleSelectionBtnCheckout);
            linearLayoutQuantitySelector = itemView.findViewById(R.id.linearLayoutQuantitySelector);
            llMultiSelectLayout = itemView.findViewById(R.id.llMultiSelectLayout);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartDesc = itemView.findViewById(R.id.tvCartDesc);
            tvQuantity = itemView.findViewById(R.id.textViewQuantity);
            tvInitialPrice = itemView.findViewById(R.id.tvInitialPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            rlRemoveItem = itemView.findViewById(R.id.rlRemoveItem);
            rlAddItem = itemView.findViewById(R.id.rlAddItem);
            rbtnReturn = itemView.findViewById(R.id.rbtnReturn);

            tvMinProductQuan = itemView.findViewById(R.id.tvMinProductQuan);

            llProductTiming = itemView.findViewById(R.id.llProductTiming);
            tvProductStartTime = itemView.findViewById(R.id.tvProductStartTime);
        }
    }


    private void openBottomSheet(final TextView textViewQuantity, ViewHolder holder, final int adapterPosition, final Datum productData) {
        View view = activity.getLayoutInflater().inflate(R.layout.addremove_bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.DialogStyle);
        dialog.setContentView(view);
        dialog.show();

        Button btnContinue = dialog.findViewById(R.id.btnContinue);
        TextView tvEditQuantity = dialog.findViewById(R.id.tvEditQuantity);
        tvEditQuantity.setText(StorefrontCommonData.getString(activity,R.string.edit_quantity));

        final EditText etQuantity = dialog.findViewById(R.id.etQuantity);
        btnContinue.setText(StorefrontCommonData.getString(activity,R.string.save));


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks
                if (etQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                } else if (etQuantity.getText().toString().equals("0")) {
                    Toast.makeText(activity, "Please enter a valid quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    textViewQuantity.setText(etQuantity.getText().toString());
                    dialog.dismiss();//to hide it
                }
            }
        });
    }
}