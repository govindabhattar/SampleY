package com.tookancustomer.filter.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.filter.model.Result;

import java.util.ArrayList;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.MyViewHolder> implements
        OptionsRecyclerAdater.OptionSelectionListener {

    private ArrayList<Result> filterList;
    private FilterSelectionListener listener;

    public FilterRecyclerAdapter(ArrayList<Result> filterList, FilterSelectionListener listener) {
        this.filterList = filterList;
        this.listener = listener;
    }

    public ArrayList<Result> getFilterList() {
        return filterList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_filter_recycler_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int adapterPos = holder.getAdapterPosition();
        final Result filterData = filterList.get(adapterPos);

        if (filterData.isSelected()) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        if (isAnySelected(filterData)) {
            holder.viewAnySelected.setVisibility(View.VISIBLE);
        } else {
            holder.viewAnySelected.setVisibility(View.INVISIBLE);
        }

        holder.tvFilterName.setText(filterData.getDisplayName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectAllExcept(filterData);
                if (listener != null)
                    listener.onFilterSelected(filterData, adapterPos);

                notifyDataSetChanged();
            }
        });
    }

    private boolean isAnySelected(Result result) {
        boolean isAnySelected = false;

        for (int i = 0; i < result.getAllowedDataList().size(); i++) {
            if (result.getAllowedDataList().get(i).isSelected()) {
                isAnySelected = true;
                break;
            }
        }

        return isAnySelected;
    }

    private void unselectAllExcept(Result result) {
        for (int i = 0; i < filterList.size(); i++)
            filterList.get(i).setSelected(false);

        result.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public void onOptionSelected(int filterPosition) {
        notifyItemChanged(filterPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFilterName;
        private View viewAnySelected;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvFilterName = itemView.findViewById(R.id.tvFilterName);
            viewAnySelected = itemView.findViewById(R.id.viewAnySelected);
        }
    }

    public interface FilterSelectionListener {
        void onFilterSelected(Result filter, int filterPos);
    }

}
