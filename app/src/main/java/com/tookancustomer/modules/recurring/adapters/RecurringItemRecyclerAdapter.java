package com.tookancustomer.modules.recurring.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.allrecurringdata.ProductJson;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class RecurringItemRecyclerAdapter extends RecyclerView.Adapter<RecurringItemRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<ProductJson> productJsonList;


    public RecurringItemRecyclerAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recurring_item, viewGroup, false);
        context = viewGroup.getContext();
        return new ViewHolder(taskItem);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder mHolder, int position) {
        final int adapterPos = mHolder.getAdapterPosition();

        ProductJson productJson = productJsonList.get(adapterPos);

        if (productJsonList.get(productJsonList.size() - 1) == productJsonList.get(position)) {
            mHolder.imageViewSep.setVisibility(View.GONE);
        } else {
            mHolder.imageViewSep.setVisibility(View.VISIBLE);
        }
//
        mHolder.textViewItemName.setText(productJson.getName());

        if (productJson.getCustomizations().size() > 0) {
            mHolder.tvItemCustomisations.setVisibility(View.VISIBLE);
            String customisationString = "";
            for (int i = 0; i < productJson.getCustomizations().size(); i++) {
                if (!customisationString.isEmpty())
                    customisationString = customisationString + ", ";
//                customisationString = customisationString + productJson.getCustomizations().get(i).getCustName();
            }
            mHolder.tvItemCustomisations.setText(customisationString);
        } else {
            mHolder.tvItemCustomisations.setVisibility(View.GONE);
        }

        mHolder.textViewItemQuantity.setText("x " + productJson.getQuantity());


        mHolder.textViewItemPrice.setText(Utils.getCurrencySymbol() + "" +
                Utils.getDoubleTwoDigits(getItemPriceWithCustomisation(productJson)));

        mHolder.textViewItemTotalPrice.setText(Utils.getCurrencySymbol() + "" + Utils
                .getDoubleTwoDigits(getItemPriceWithCustomisation(productJsonList.get(adapterPos))
                        * productJson.getQuantity()));

        if (StorefrontCommonData.getFormSettings().getShowProductPrice() == 0 && getItemPriceWithCustomisation(productJson) <= 0) {
            mHolder.textViewItemTotalPrice.setVisibility(View.GONE);
            mHolder.textViewItemPrice.setVisibility(View.GONE);
            mHolder.tvItemChargeText.setVisibility(View.GONE);
            mHolder.textViewItemQuantity.setVisibility(View.GONE);
        } else {
            mHolder.textViewItemTotalPrice.setVisibility(View.VISIBLE);
            mHolder.textViewItemPrice.setVisibility(View.VISIBLE);
            mHolder.tvItemChargeText.setVisibility(View.VISIBLE);
            mHolder.textViewItemQuantity.setVisibility(View.VISIBLE);
        }

    }

    public void setData(final ArrayList<ProductJson> productJsonList) {
        this.productJsonList = productJsonList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return productJsonList != null ? productJsonList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewItemName, textViewItemPrice, textViewItemQuantity, textViewItemTotalPrice,
                tvItemCustomisations, tvItemStartTime, tvItemEndTime, tvOrderStatus, tvServiceTrackLink, tvItemChargeText;
        private View imageViewSep;
        private Button tvProductCancelBtn;
        //        private LinearLayout llServiceLayout;
        private CheckBox rbtnReturn;
        private RecyclerView rvProductTaxesList;

        public ViewHolder(View itemView) {
            super(itemView);

//            llServiceLayout = itemView.findViewById(R.id.llServiceLayout);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            tvItemCustomisations = itemView.findViewById(R.id.tvItemCustomisations);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
            tvItemChargeText = itemView.findViewById(R.id.tvItemChargeText);
            tvItemStartTime = itemView.findViewById(R.id.tvItemStartTime);
            tvItemEndTime = itemView.findViewById(R.id.tvItemEndTime);
            textViewItemTotalPrice = itemView.findViewById(R.id.textViewItemTotalPrice);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvServiceTrackLink = itemView.findViewById(R.id.tvServiceTrackLink);
            tvProductCancelBtn = itemView.findViewById(R.id.tvProductCancelBtn);
            tvProductCancelBtn.setText(StorefrontCommonData.getString(context, R.string.cancel_text));
            imageViewSep = itemView.findViewById(R.id.imageViewSep);
            rbtnReturn = itemView.findViewById(R.id.rbtnReturn);
            rvProductTaxesList = itemView.findViewById(R.id.rvProductTaxesList);
            rvProductTaxesList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }
    }

    private double getItemPriceWithCustomisation(ProductJson productJson) {
        double itemPrice = 0.0;

        if (productJson != null) {
            itemPrice = itemPrice + productJson.getUnitPrice();
            if (productJson.getCustomizations() != null && productJson.getCustomizations().size() > 0) {
                for (int i = 0; i < productJson.getCustomizations().size(); i++) {
//                    itemPrice = itemPrice + productJson.getCustomizations().get(i).getUnitPrice();
                }
            }
        }
        return itemPrice;
    }


}