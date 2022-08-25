package com.tookancustomer.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.R;
import com.tookancustomer.TaskDetailsNewActivity;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.billbreakdown.BillBreakdownData;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by cl-macmini-25 on 22/02/18.
 */

public class BillBreakdownAdapter extends RecyclerView.Adapter<BillBreakdownAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<BillBreakdownData> billBreakdownDataArrayList;
    private boolean atProductLevel = false;

    public BillBreakdownAdapter(Activity activity, ArrayList<BillBreakdownData> billBreakdownDataArrayList) {
        this.activity = activity;
        this.billBreakdownDataArrayList = billBreakdownDataArrayList;
        atProductLevel = false;
    }

    public BillBreakdownAdapter(Activity activity, ArrayList<BillBreakdownData> billBreakdownDataArrayList, boolean atProductLevel) {
        this.activity = activity;
        this.billBreakdownDataArrayList = billBreakdownDataArrayList;
        this.atProductLevel = atProductLevel;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem;
        if (activity instanceof TaskDetailsNewActivity) {
            taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_taxes_orderdetails, viewGroup, false);
        } else {
            taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_billbreakdown_taxes, viewGroup, false);
        }
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();
        int upSymbolUnicode = 0x2191;
        String text = Character.toString((char) upSymbolUnicode);


        viewHolder.tvTaxTitle.setText(billBreakdownDataArrayList.get(adapterPos).getName() + (billBreakdownDataArrayList.get(adapterPos).getTaxPercentage() > 0 && billBreakdownDataArrayList.get(adapterPos).getTaxType() == 0 ? " @" + Utils.getDoubleTwoDigits(billBreakdownDataArrayList.get(adapterPos).getTaxPercentage()) + "%" : ""));
        if (activity instanceof TaskDetailsNewActivity) {
            String taxValue = ((billBreakdownDataArrayList.get(adapterPos).isDiscount() ? "- " : "")
                    + UIManager.getCurrency(Utils.getCurrencySymbolNew(billBreakdownDataArrayList.get(adapterPos).getCurrencySymbol() != null ? billBreakdownDataArrayList.get(adapterPos).getCurrencySymbol()
                    :Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getSymbol()) + Utils.getDoubleTwoDigits(billBreakdownDataArrayList.get(adapterPos).getAmount())));
            viewHolder.tvTaxValue.setText(taxValue);
        } else {
            if (!Utils.getCurrencySymbol().isEmpty()) {
                String taxValue = ((billBreakdownDataArrayList.get(adapterPos).isDiscount() ? "- " : "")
                        + Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(billBreakdownDataArrayList.get(adapterPos).getAmount()));
                viewHolder.tvTaxValue.setText(taxValue);

            }
        }
        if (activity instanceof MakePaymentActivity || activity instanceof TaskDetailsNewActivity) {
            if (billBreakdownDataArrayList.get(adapterPos).getIsDeliverySurge() != null && billBreakdownDataArrayList.get(adapterPos).getIsDeliverySurge().compareTo(BigDecimal.ZERO) > 0
                    && billBreakdownDataArrayList.get(adapterPos).getName().equals(StorefrontCommonData.getTerminology().getDeliveryCharge())
            ) {
                String texttt = billBreakdownDataArrayList.get(adapterPos).getName() + (billBreakdownDataArrayList.get(adapterPos).getTaxPercentage() > 0 && billBreakdownDataArrayList.get(adapterPos).getTaxType() == 0 ? " @" + Utils.getDoubleTwoDigits(billBreakdownDataArrayList.get(adapterPos).getTaxPercentage()) + "%" : "");

                Spannable wordtoSpan = new SpannableString(texttt + text);

                wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), texttt.length(), (texttt + text).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                viewHolder.tvTaxTitle.setText(wordtoSpan);

            }
        }


        if (activity instanceof MakePaymentActivity || activity instanceof TaskDetailsNewActivity) {
            viewHolder.tvTaxSubTitle.setVisibility(billBreakdownDataArrayList.get(adapterPos).getDescription().isEmpty() ? View.GONE : View.VISIBLE);

            if (billBreakdownDataArrayList.get(adapterPos).isSurge()) {
                viewHolder.tvTaxSubTitle.setTextColor(activity.getResources().getColor(R.color.color_red));
                viewHolder.tvTaxSubTitle.setText("(" + billBreakdownDataArrayList.get(adapterPos).getDescription() + ")" + text);
            } else {
                viewHolder.tvTaxSubTitle.setTextColor(activity.getResources().getColor(R.color.subtext_billbreakdown));
                viewHolder.tvTaxSubTitle.setText("(" + billBreakdownDataArrayList.get(adapterPos).getDescription() + ")");
            }

        } else {
            viewHolder.tvTaxSubTitle.setVisibility(View.GONE);
        }

        if (activity instanceof TaskDetailsNewActivity) {
            viewHolder.viewSeperator.setVisibility(View.GONE);
        } else {
            viewHolder.viewSeperator.setVisibility(atProductLevel ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return billBreakdownDataArrayList != null ? billBreakdownDataArrayList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaxTitle, tvTaxSubTitle, tvTaxValue;
        View viewSeperator;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTaxTitle = itemView.findViewById(R.id.tvTaxTitle);
            tvTaxSubTitle = itemView.findViewById(R.id.tvTaxSubTitle);
            tvTaxValue = itemView.findViewById(R.id.tvTaxValue);
            viewSeperator = itemView.findViewById(R.id.viewSeperator);
        }
    }
}
