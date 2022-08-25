package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.models.AvailableTimeSlotsModelResponse;
import com.tookancustomer.models.LaundryScheduleTimeSlotsResponse;
import com.tookancustomer.models.TookanScheduleTimeSlotsResponse;
import com.tookancustomer.models.subscription.TimeSlotsSubscription;

import java.util.ArrayList;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class ScheduleTimeSlotsAdapter extends RecyclerView.Adapter<ScheduleTimeSlotsAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<AvailableTimeSlotsModelResponse> dataList;
    private ArrayList<TookanScheduleTimeSlotsResponse> tookanAgentArrayList;
    private ArrayList<LaundryScheduleTimeSlotsResponse> laundryArrayList;
    private TimeSlotAdapter.Callback callback;
    private TimeSlotAdapter.CallbackSubscription callbackSubscription;
    private TimeSlotAdapter.CallbackTookanAgent callbackTookanAgent;
    private int selectedPos = 0;
    private boolean isTookanAgentFlow = false;
    public boolean isHeaderVisibilityManage = true;
    public boolean autoSelect = false;
    private boolean isSubscriptionFlow = false;
    private ArrayList<TimeSlotsSubscription> subscriptionSlotsArrayList;

    public ScheduleTimeSlotsAdapter(Activity activity, ArrayList<AvailableTimeSlotsModelResponse> dataList, boolean autoSelect, TimeSlotAdapter.Callback callback) {
        this.activity = activity;
        this.callback = callback;
        this.autoSelect = autoSelect;
        this.dataList = dataList;
        isTookanAgentFlow = false;
    }

    public ScheduleTimeSlotsAdapter(ArrayList<TookanScheduleTimeSlotsResponse> tookanAgentArrayList, Activity activity, boolean autoSelect, TimeSlotAdapter.CallbackTookanAgent callbackTookanAgent) {
        this.activity = activity;
        this.callbackTookanAgent = callbackTookanAgent;
        this.autoSelect = autoSelect;
        this.tookanAgentArrayList = tookanAgentArrayList;
        isTookanAgentFlow = true;
    }

    public ScheduleTimeSlotsAdapter(ArrayList<LaundryScheduleTimeSlotsResponse> laundryArrayList, Activity activity, TimeSlotAdapter.CallbackTookanAgent callbackTookanAgent) {
        this.activity = activity;
        this.callbackTookanAgent = callbackTookanAgent;
        this.laundryArrayList = laundryArrayList;
        isTookanAgentFlow = false;
    }

    public ScheduleTimeSlotsAdapter(final ArrayList<TimeSlotsSubscription> subscriptionSlotsArrayLists, final Activity activity,final TimeSlotAdapter.CallbackSubscription callbackSubscription) {
        this.activity = activity;
        isSubscriptionFlow = true;
        this.callbackSubscription=callbackSubscription;
        this.subscriptionSlotsArrayList = subscriptionSlotsArrayLists;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_schedule_timeslots_body, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();

        if (isTookanAgentFlow) {
            viewHolder.tvDayLabel.setText(tookanAgentArrayList.get(adapterPos).getHeader());
            viewHolder.tvDayLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPos = adapterPos;
                    isHeaderVisibilityManage = true;
                    notifyDataSetChanged();
                }
            });

            TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(tookanAgentArrayList.get(adapterPos).getTimeSlotsArrayList(), activity, autoSelect, callbackTookanAgent);
            viewHolder.rvScheduleTimeSlotsList.setAdapter(timeSlotAdapter);

            if (isHeaderVisibilityManage)
                if (adapterPos == selectedPos) {
                    if (viewHolder.rvScheduleTimeSlotsList.getVisibility() == View.GONE) {
                        viewHolder.rvScheduleTimeSlotsList.setVisibility(View.VISIBLE);
                        viewHolder.ibArrow.setRotation(180);
                    } else {
                        viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                        viewHolder.ibArrow.setRotation(0);
                    }
                } else {
                    viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                    viewHolder.ibArrow.setRotation(0);
                }

        }
        //Subscription flow
        else if(isSubscriptionFlow){
            viewHolder.tvDayLabel.setText(subscriptionSlotsArrayList.get(adapterPos).getHeader());
            viewHolder.tvDayLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPos = adapterPos;
                    isHeaderVisibilityManage = true;
                    notifyDataSetChanged();
                }
            });

            TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(activity, subscriptionSlotsArrayList.get(adapterPos).getSortedDatesArrayList(),  callbackSubscription);
            viewHolder.rvScheduleTimeSlotsList.setAdapter(timeSlotAdapter);


            if (isHeaderVisibilityManage)
                if (adapterPos == selectedPos) {
                    if (viewHolder.rvScheduleTimeSlotsList.getVisibility() == View.GONE) {
                        viewHolder.rvScheduleTimeSlotsList.setVisibility(View.VISIBLE);
                        viewHolder.ibArrow.setRotation(180);
                    } else {
                        viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                        viewHolder.ibArrow.setRotation(0);
                    }
                } else {
                    viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                    viewHolder.ibArrow.setRotation(0);
                }
        } else {

            if (Dependencies.isLaundryApp()|| (Dependencies.getSelectedProductsArrayList().size()> 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null &&
                    Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getDisplayRangeIntervals() == 1)) {
                viewHolder.tvDayLabel.setText(laundryArrayList.get(adapterPos).getHeader());
                viewHolder.tvDayLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedPos = adapterPos;
                        isHeaderVisibilityManage = true;
                        notifyDataSetChanged();
                    }
                });

                TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(laundryArrayList.get(adapterPos).getTimeSlotsArrayList(), activity, callbackTookanAgent);
                viewHolder.rvScheduleTimeSlotsList.setAdapter(timeSlotAdapter);

                if (isHeaderVisibilityManage)
                    if (adapterPos == selectedPos) {
                        if (viewHolder.rvScheduleTimeSlotsList.getVisibility() == View.GONE) {
                            viewHolder.rvScheduleTimeSlotsList.setVisibility(View.VISIBLE);
                            viewHolder.ibArrow.setRotation(180);
                        } else {
                            viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                            viewHolder.ibArrow.setRotation(0);
                        }
                    } else {
                        viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                        viewHolder.ibArrow.setRotation(0);
                    }
            }
            else {
                viewHolder.tvDayLabel.setText(dataList.get(adapterPos).getHeader());
                viewHolder.tvDayLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedPos = adapterPos;
                        isHeaderVisibilityManage = true;
                        notifyDataSetChanged();
                    }
                });

                TimeSlotAdapter timeSlotAdapter = new TimeSlotAdapter(activity, dataList.get(adapterPos).getSortedDatesArrayList(), autoSelect, callback);
                viewHolder.rvScheduleTimeSlotsList.setAdapter(timeSlotAdapter);


                if (isHeaderVisibilityManage)
                    if (adapterPos == selectedPos) {
                        if (viewHolder.rvScheduleTimeSlotsList.getVisibility() == View.GONE) {
                            viewHolder.rvScheduleTimeSlotsList.setVisibility(View.VISIBLE);
                            viewHolder.ibArrow.setRotation(180);
                        } else {
                            viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                            viewHolder.ibArrow.setRotation(0);
                        }
                    } else {
                        viewHolder.rvScheduleTimeSlotsList.setVisibility(View.GONE);
                        viewHolder.ibArrow.setRotation(0);
                    }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isTookanAgentFlow)
            return tookanAgentArrayList != null ? tookanAgentArrayList.size() : 1;
        else if(isSubscriptionFlow)
            return subscriptionSlotsArrayList != null ? subscriptionSlotsArrayList.size() : 1;
        else if (Dependencies.isLaundryApp()|| (Dependencies.getSelectedProductsArrayList().size()> 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null &&
                Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getDisplayRangeIntervals() == 1))
            return laundryArrayList != null ? laundryArrayList.size() : 1;
        else
            return dataList != null ? dataList.size() : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ibArrow;
        TextView tvDayLabel;
        RecyclerView rvScheduleTimeSlotsList;

        public ViewHolder(View itemView) {
            super(itemView);
            ibArrow = itemView.findViewById(R.id.ibArrow);
            tvDayLabel = itemView.findViewById(R.id.tvDayLabel);
            rvScheduleTimeSlotsList = itemView.findViewById(R.id.rvScheduleTimeSlotsList);
            if ( Dependencies.isLaundryApp()||  (Dependencies.getSelectedProductsArrayList().size()> 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null &&
                    Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getDisplayRangeIntervals() == 1)) {
                rvScheduleTimeSlotsList.setLayoutManager(new GridLayoutManager(activity, 2));
            }
            else {
                //isTookanAgentFlow case also handled in else
                rvScheduleTimeSlotsList.setLayoutManager(new GridLayoutManager(activity, 4));
            }
        }
    }
}