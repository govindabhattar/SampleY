package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.models.PromosModel;

import java.util.List;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class NewPromosAdapter extends RecyclerView.Adapter<NewPromosAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys {
    private Activity activity;
    private List<PromosModel> dataList;

    public NewPromosAdapter(Activity activity, List<PromosModel> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_promos, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();

        holder.tvPromoCode.setText(dataList.get(adapterPos).getPromoCode());
        if (dataList.get(adapterPos).getIsPromo() == 1) {
            holder.tvPromoDescription.setVisibility(View.VISIBLE);
            holder.tvPromoDescription.setText(dataList.get(adapterPos).getDescription());
        } else {
            holder.tvPromoDescription.setVisibility(View.GONE);
        }
        if (activity instanceof MakePaymentActivity) {
            if (dataList.get(position).isSelected()) {
                holder.tvSelectedPromo.setBackgroundResource(R.drawable.ic_radio_button_filled);
            } else {
                holder.tvSelectedPromo.setBackgroundResource(R.drawable.ic_radio_button_unfilled);
            }
            holder.llPromoCodeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataList.get(adapterPos).getIsPromo() == 1) {
                        ((MakePaymentActivity) activity).getBillBreakdowns(dataList.get(adapterPos).isSelected ? null : dataList.get(adapterPos).getPromoId(), "", "");
                    } else {
                        ((MakePaymentActivity) activity).getBillBreakdowns(null, dataList.get(adapterPos).isSelected ? "" : dataList.get(adapterPos).getPromoCode(), "");
                    }

//                    for (int i = 0; i < dataList.size(); i++) {
//                        if (position == i) {
//                            if (dataList.get(i).isSelected()) {
//                                dataList.get(i).setSelected(false);
//                                ((MakePaymentActivity) activity).setPromoReferalCode(null, "");
//                                ((MakePaymentActivity) activity).clearPromos();
//                            } else {
//                                dataList.get(i).setSelected(true);
//                                if (dataList.get(i).getIsPromo() == 1) {
//                                    ((MakePaymentActivity) activity).setPromoReferalCode(dataList.get(i).getPromoId(), "");
//                                    ((MakePaymentActivity) activity).applyPromoCode(dataList.get(i).getPromoId());
//                                } else {
//                                    ((MakePaymentActivity) activity).setPromoReferalCode(null, dataList.get(i).getPromoCode());
//                                    ((MakePaymentActivity) activity).applyReferral(dataList.get(i).getPromoCode());
//                                }
//                            }
//                        } else {
//                            dataList.get(i).setSelected(false);
//                        }
//                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSelectedPromo;
        private TextView tvPromoCode, tvPromoDescription;
        private LinearLayout llPromoCodeView;

        ViewHolder(View itemView) {
            super(itemView);
            tvSelectedPromo = itemView.findViewById(R.id.tvSelectedPromo);
            tvPromoCode = itemView.findViewById(R.id.tvPromoCode);
            tvPromoDescription = itemView.findViewById(R.id.tvPromoDescription);
            llPromoCodeView = itemView.findViewById(R.id.llPromoCodeView);
        }
    }
}