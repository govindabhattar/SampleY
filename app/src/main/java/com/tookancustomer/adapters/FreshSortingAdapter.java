package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.models.SortResponseModel;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


/**
 * Created by Gurmail S. Kang on 5/4/16.
 */
public class FreshSortingAdapter extends RecyclerView.Adapter<FreshSortingAdapter.ViewHolderSort> implements ItemListener {

    private Activity activity;
    private ArrayList<SortResponseModel> sortArray;
    private RecyclerView recyclerView;
    private Callback callback;

    public FreshSortingAdapter(Activity activity, ArrayList<SortResponseModel> sortArray, RecyclerView recyclerView, Callback callback) {
        this.activity = activity;
        this.sortArray = sortArray;
        this.recyclerView = recyclerView;
        this.callback = callback;
    }

    public void setList(ArrayList<SortResponseModel> sortArray) {
        this.sortArray = sortArray;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderSort onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fresh_sort, parent, false);
        return new ViewHolderSort(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolderSort holder, int position) {
        try {
            SortResponseModel slot = sortArray.get(position);

            holder.tvSortType.setText(slot.name);
            if (!slot.check) {
                holder.tvSortType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                holder.tvSortType.setTextColor(ContextCompat.getColor(activity, R.color.primary_text_color));
            } else {
                holder.tvSortType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_green_tick, 0);
                holder.tvSortType.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            }

            holder.viewDivider.setVisibility(position == sortArray.size() - 1 ? View.GONE : View.VISIBLE);

        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }

    }

    @Override
    public int getItemCount() {
        return sortArray == null ? 0 : sortArray.size();
    }

    @Override
    public void onClickItem(View viewClicked, View parentView) {
        int position = recyclerView.getChildLayoutPosition(parentView);
        if (position != RecyclerView.NO_POSITION) {
            if (viewClicked.getId() == R.id.linear) {
                callback.onSlotSelected(position, sortArray.get(position));
                for (int i = 0; i < sortArray.size(); i++) {
                    sortArray.get(i).setCheck(i == position);
                }
                notifyDataSetChanged();
            }
        }
    }


    class ViewHolderSort extends RecyclerView.ViewHolder {
        public LinearLayout linear;
        public TextView tvSortType;
        public View viewDivider;

        public ViewHolderSort(final View itemView, final ItemListener itemListener) {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            tvSortType = itemView.findViewById(R.id.tvSortType);
            viewDivider = itemView.findViewById(R.id.viewDivider);
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onClickItem(linear, itemView);
                }
            });
        }
    }


    public interface Callback {
        void onSlotSelected(int position, SortResponseModel sort);
    }

}
