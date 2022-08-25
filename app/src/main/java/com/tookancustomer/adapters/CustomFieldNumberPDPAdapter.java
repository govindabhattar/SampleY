package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.models.ProductCatalogueData.CustomField;

import java.util.List;


/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class CustomFieldNumberPDPAdapter extends RecyclerView.Adapter<CustomFieldNumberPDPAdapter.ViewHolder> {
    private Activity activity;
    private List<CustomField> dataList;

    public CustomFieldNumberPDPAdapter(Activity activity, List<CustomField> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_textview, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int adapterPos = viewHolder.getAdapterPosition();
        viewHolder.tvCustomField.setText(dataList.get(adapterPos).getValue().toString()+" "+dataList.get(adapterPos).getLabel());
    }


    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomField;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomField = itemView.findViewById(R.id.tvCustomField);
        }
    }
}