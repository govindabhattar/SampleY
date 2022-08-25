package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.tookancustomer.R;
import com.tookancustomer.models.subscription.Days;

import java.util.ArrayList;

public class SubscriptionDaysAdapter extends RecyclerView.Adapter<SubscriptionDaysAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Days> subscriptionData;
    private Callback callback;

    public SubscriptionDaysAdapter(Activity activity, ArrayList<Days> subscriptionData, Callback callback) {
        this.activity = activity;
        this.subscriptionData = subscriptionData;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subscription_days, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();
        Days days = subscriptionData.get(adapterPos);
        viewHolder.rbSubscriptionDay.setText(days.getDisplayValue());


        if (days.isActive()) {
            viewHolder.rbSubscriptionDay.setTextColor(ContextCompat.getColor(activity, R.color.black));
            viewHolder.rbSubscriptionDay.setButtonDrawable(ContextCompat.getDrawable(activity, R.drawable.custom_btn_radio));

            viewHolder.rbSubscriptionDay.setHighlightColor(ContextCompat.getColor(activity, R.color.grey_bg));
            viewHolder.rbSubscriptionDay.setChecked(days.isSelected());
            viewHolder.rbSubscriptionDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    if (subscriptionData.get(adapterPos).isSelected()) {
                        subscriptionData.get(adapterPos).setSelected(false);
                    } else {
                        subscriptionData.get(adapterPos).setSelected(true);
                    }

                    notifyDataSetChanged();
                    callback.onDaySelected(subscriptionData);
                }
            });
        } else {
            viewHolder.rbSubscriptionDay.setTextColor(ContextCompat.getColor(activity, R.color.light_grey));
            viewHolder.rbSubscriptionDay.setButtonDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_radio_unactive));

            viewHolder.rbSubscriptionDay.setOnClickListener(null);

        }


    }

    public void setData(ArrayList<Days> subscriptionData) {
//        this.subscriptionData.clear();
        this.subscriptionData = subscriptionData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return subscriptionData != null ? subscriptionData.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbSubscriptionDay;

        public ViewHolder(View itemView) {
            super(itemView);
            rbSubscriptionDay = itemView.findViewById(R.id.rbSubscriptionDay);
        }
    }

    public interface Callback {
        void onDaySelected(ArrayList<Days> subscriptionData);
    }
}