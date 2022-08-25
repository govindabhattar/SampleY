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

public class SingleSelectAdapter extends RecyclerView.Adapter<SingleSelectAdapter.ViewHolder> {
    private String questionnaireCurrencySymbol = "";
    private Datum productDataItem;
    private ArrayList<AllowValue> allowedValueList;
    private boolean forDisplay;
    private Activity activity;
    onclickSingleSelect onclickSingleSelect;

    public SingleSelectAdapter(final ArrayList<AllowValue> allowedValueList) {
        this.allowedValueList = allowedValueList;
    }

    public SingleSelectAdapter(ArrayList<AllowValue> allowedValueList, boolean forDisplay, Activity activity) {
        this.allowedValueList = allowedValueList;
        this.forDisplay = forDisplay;
        this.activity = activity;
    }

    public SingleSelectAdapter(ArrayList<AllowValue> allowedValueList, boolean forDisplay, Activity activity, Datum productDataItem) {
        this.allowedValueList = allowedValueList;
        this.forDisplay = forDisplay;
        this.activity = activity;
        this.productDataItem = productDataItem;
    }

    public SingleSelectAdapter(ArrayList<AllowValue> allowedValueList, boolean forDisplay, Activity activity, String questionnaireCurrencySymbol) {
        this.allowedValueList = allowedValueList;
        this.forDisplay = forDisplay;
        this.activity = activity;
        this.questionnaireCurrencySymbol = questionnaireCurrencySymbol;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_radiobutton, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        int adapterPos = viewHolder.getAdapterPosition();
        AllowValue mAllowValue = allowedValueList.get(adapterPos);

        if (mAllowValue.getCost() != 0) {

            if (activity instanceof QuestionnaireTemplateActivity && productDataItem != null) {
                viewHolder.tvItem.setText(mAllowValue.getDisplayName() + " - " +
                        UIManager.getCurrency(Utils.getCurrencySymbolNew(((productDataItem.getPaymentSettings() != null
                                && productDataItem.getPaymentSettings().getSymbol() != null) ?
                                productDataItem.getPaymentSettings().getSymbol() : "")) + Utils.getDoubleTwoDigits(mAllowValue.getCost())));

            } else if (!questionnaireCurrencySymbol.isEmpty()) {
                viewHolder.tvItem.setText(mAllowValue.getDisplayName() + " - " + questionnaireCurrencySymbol + Utils.getDoubleTwoDigits(mAllowValue.getCost()));

            } else {

                viewHolder.tvItem.setText(mAllowValue.getDisplayName() + " - " +
                        UIManager.getCurrency(Utils.getCurrencySymbolNew(((Dependencies.getSelectedProductsArrayList().size() > 0
                                && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings() != null
                                && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getSymbol() != null) ?
                                Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getSymbol() : "")) + Utils.getDoubleTwoDigits(mAllowValue.getCost())));
            }
        } else
            viewHolder.tvItem.setText(mAllowValue.getDisplayName());

        viewHolder.ivSelectItem.setImageResource(mAllowValue.isChecked() ?
                R.drawable.ic_filter_radio_on_btn : R.drawable.ic_option_empty);

        viewHolder.tvItem.setTextColor(mAllowValue.isChecked() ?
                activity.getResources().getColor(R.color.colorAccent) : activity.getResources().getColor(R.color.black_80));


        viewHolder.itemView.setTag(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!forDisplay) {
                    int pos = (int) view.getTag();

                    for (int i = 0; i < allowedValueList.size(); i++) {
                        if (i == pos)
                            allowedValueList.get(i).setChecked(!allowedValueList.get(pos).isChecked());
                        else
                            allowedValueList.get(i).setChecked(false);

                    }

//                    allowedValueList.get(pos).setChecked(!allowedValueList.get(pos).isChecked());
//                    mAllowValue.setChecked(!mAllowValue.isChecked());
                    notifyItemChanged(pos);
                    notifyDataSetChanged();
                    onclickSingleSelect.onclick(pos);

                }
            }
        });
    }

    public void onclickListner(onclickSingleSelect onclickSingleSelect) {
        this.onclickSingleSelect = onclickSingleSelect;
    }

    public interface onclickSingleSelect {
        void onclick(int pos);
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
