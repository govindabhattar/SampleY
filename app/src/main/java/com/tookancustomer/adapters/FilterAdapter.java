package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductFilters.AllowValue;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private String cuerrencySymbol = "";
    private ArrayList<AllowValue> allowedValueList;
    private boolean forDisplay;
    private Activity activity;
    private Datum productDataItem;

    public FilterAdapter(final ArrayList<AllowValue> allowedValueList, Activity activity) {
        this.allowedValueList = allowedValueList;
        this.activity = activity;

    }

    public FilterAdapter(ArrayList<AllowValue> allowedValueList, boolean forDisplay, Activity activity) {
        this.allowedValueList = allowedValueList;
        this.forDisplay = forDisplay;
        this.activity = activity;
    }

    public FilterAdapter(ArrayList<AllowValue> allowedValueList, boolean forDisplay, Activity activity, Datum productDataItem) {
        this.allowedValueList = allowedValueList;
        this.forDisplay = forDisplay;
        this.activity = activity;
        this.productDataItem = productDataItem;
    }

    public FilterAdapter(ArrayList<AllowValue> allowedValueList, boolean forDisplay, Activity activity, String cuerrencySymbol) {
        this.allowedValueList = allowedValueList;
        this.forDisplay = forDisplay;
        this.activity = activity;
        this.cuerrencySymbol = cuerrencySymbol;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_checkbox, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final int adapterPos = viewHolder.getAdapterPosition();
        final AllowValue mAllowValue = allowedValueList.get(adapterPos);

        if (mAllowValue.getCost() != 0) {
            if (activity instanceof QuestionnaireTemplateActivity && productDataItem != null) {
                viewHolder.tvItem.setText(mAllowValue.getDisplayName() + " - " +
                        (UIManager.getCurrency(Utils.getCurrencySymbolNew(((
                                productDataItem.getPaymentSettings() != null
                                        && productDataItem.getPaymentSettings().getSymbol() != null) ?
                                productDataItem.getPaymentSettings().getSymbol() : "")) + Utils.getDoubleTwoDigits(mAllowValue.getCost()))));

            } else if ( !cuerrencySymbol.isEmpty()) {
                viewHolder.tvItem.setText(mAllowValue.getDisplayName() + " - " +
                        cuerrencySymbol + Utils.getDoubleTwoDigits(mAllowValue.getCost()));

            } else
                viewHolder.tvItem.setText(mAllowValue.getDisplayName() + " - " +
                        (UIManager.getCurrency(Utils.getCurrencySymbolNew(((Dependencies.getSelectedProductsArrayList().size() > 0
                                && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings() != null
                                && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getSymbol() != null) ?
                                Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getSymbol() : "")) + Utils.getDoubleTwoDigits(mAllowValue.getCost()))));
        } else
            viewHolder.tvItem.setText(mAllowValue.getDisplayName());

        viewHolder.ivSelectItem.setImageResource(mAllowValue.isChecked() ?
                R.drawable.ic_icon_checkbox_ticked_accent : R.drawable.ic_icon_checkbox_unticked);

        viewHolder.tvItem.setTextColor(mAllowValue.isChecked() ?
                activity.getResources().getColor(R.color.colorAccent) : activity.getResources().getColor(R.color.black_80));


        viewHolder.itemView.setTag(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!forDisplay) {
                    int pos = (int) view.getTag();
                    allowedValueList.get(pos).setChecked(!allowedValueList.get(pos).isChecked());
//                    mAllowValue.setChecked(!mAllowValue.isChecked());
                    notifyItemChanged(adapterPos);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allowedValueList != null ? allowedValueList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItem;
        public ImageView ivSelectItem;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            ivSelectItem = itemView.findViewById(R.id.ivSelectItem);
        }
    }

    /**
     * clear filters
     */
    public void clearFilterList() {
        for (int i = 0; i < allowedValueList.size(); i++) {
            allowedValueList.get(i).setChecked(false);
        }
        notifyDataSetChanged();
    }
}
