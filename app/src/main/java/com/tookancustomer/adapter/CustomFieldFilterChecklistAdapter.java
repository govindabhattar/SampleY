package com.tookancustomer.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;

import java.util.List;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class CustomFieldFilterChecklistAdapter extends RecyclerView.Adapter<CustomFieldFilterChecklistAdapter.ViewHolder> implements Keys.Extras, Keys.MetaDataKeys {
    private Activity activity;
    private List<String> dataList;

    public CustomFieldFilterChecklistAdapter(Activity activity, List<String> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.itemview_textview, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int adapterPos = holder.getAdapterPosition();
        holder.tvCustomField.setText(dataList.get(adapterPos));

        if (dataList.get(adapterPos).isEmpty()) {
            holder.tvCustomField.setCompoundDrawables(activity.getDrawable(R.drawable.ic_green_tick), null, null, null);
        } else {
            holder.tvCustomField.setCompoundDrawables(activity.getDrawable(R.drawable.ic_option_empty), null, null, null);
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomField;

        ViewHolder(View itemView) {
            super(itemView);
            tvCustomField = itemView.findViewById(R.id.tvCustomField);
        }
    }
}