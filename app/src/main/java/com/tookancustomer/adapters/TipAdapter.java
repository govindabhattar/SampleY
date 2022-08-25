package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.billbreakdown.TipModel;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<TipModel> dataList;
    public int tipType = 1;
    public int selectedItemPos = -1;
    private Callback callback;
    private String currency_symbol;

    public TipAdapter(Activity activity, ArrayList<TipModel> dataList, int tipType, String currency_symbol, Callback callback) {
        this.activity = activity;
        this.dataList = dataList;
        this.tipType = tipType;
        this.callback = callback;
        this.currency_symbol = currency_symbol;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_tip, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();

        if (tipType == Constants.TipType.PERCENTAGE) {
            viewHolder.tvTipAmount.setText(dataList.get(adapterPos).getValueString() + "%");
        } else {
            viewHolder.tvTipAmount.setText(UIManager.getCurrency(Utils.getCurrencySymbolNew(currency_symbol) + dataList.get(adapterPos).getValueString()));
        }

        if (selectedItemPos == adapterPos) {
            viewHolder.tvTipAmount.setTextColor(activity.getResources().getColor(R.color.white));
            viewHolder.tvTipAmount.setBackgroundResource(R.drawable.boundary_green_tip_fill);
        } else {
            viewHolder.tvTipAmount.setTextColor(activity.getResources().getColor(R.color.colorTip));
            viewHolder.tvTipAmount.setBackgroundResource(R.drawable.boundary_grey);
        }

        viewHolder.tvTipAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemPos == adapterPos) {
                    selectedItemPos = -1;
                    callback.onTipSelected(null);
                } else {
                    selectedItemPos = adapterPos;
                    callback.onTipSelected(dataList.get(adapterPos));
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTipAmount = itemView.findViewById(R.id.tvTipAmount);
        }
    }

    public interface Callback {
        void onTipSelected(TipModel tipModel);
    }
}