package com.tookancustomer.filter.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.filter.model.AllowedDataList;
import com.tookancustomer.filter.model.Result;

import java.util.ArrayList;

public class OptionsRecyclerAdater extends RecyclerView.Adapter<OptionsRecyclerAdater.MyViewHolder> {

    private ArrayList<Result> filterList;
    private ArrayList<AllowedDataList> allowedDataList;
    private int filterPosition;
    private OptionSelectionListener listener;

    public OptionsRecyclerAdater(ArrayList<Result> filterList, OptionSelectionListener listener) {
        this.filterList = filterList;
        this.listener = listener;
        allowedDataList = filterList.size() > 0 ? filterList.get(0).getAllowedDataList() : new ArrayList<AllowedDataList>();
    }

    public void setFilterPosition(int filterPosition) {
        this.filterPosition = filterPosition;
        allowedDataList = filterList.get(filterPosition).getAllowedDataList();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_options_recycler_adapter, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int adapterPos = holder.getAdapterPosition();
        final AllowedDataList allowedData = allowedDataList.get(adapterPos);
        if (allowedData.isSelected()) {
            if (filterList.get(filterPosition).getDataType().equals("Single-Select")) {
                holder.ivOptionImage.setImageResource(R.drawable.ic_filter_radio_on_btn);
            } else {
                holder.ivOptionImage.setImageResource(R.drawable.ic_icon_checkbox_ticked);
            }
        } else {
            if (filterList.get(filterPosition).getDataType().equals("Multi-Select")) {
                holder.ivOptionImage.setImageResource(R.drawable.ic_icon_checkbox_unticked);
            } else {
                holder.ivOptionImage.setImageResource(R.drawable.ic_option_empty);
            }
        }

        holder.tvOptionName.setText(allowedData.getValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterList.get(filterPosition).getDataType().equals("Single-Select")) {
                    if (!allowedData.isSelected())
                        for (int i = 0; i < allowedDataList.size(); i++) {
                            allowedDataList.get(i).setSelected(false);
                        }
                }
                allowedData.setSelected(!allowedData.isSelected());

                if (listener != null)
                    listener.onOptionSelected(filterPosition);


                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return allowedDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivOptionImage;
        private TextView tvOptionName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivOptionImage = itemView.findViewById(R.id.ivOptionImage);
            tvOptionName = itemView.findViewById(R.id.tvOptionName);
        }
    }


    public interface OptionSelectionListener {
        void onOptionSelected(int filterPosition);
    }
}
