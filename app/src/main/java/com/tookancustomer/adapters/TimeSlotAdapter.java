package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.ScheduleTimeActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.models.SortedDatesModel;
import com.tookancustomer.models.TimeSlotsResponseModel.Slot;
import com.tookancustomer.models.subscription.SubscriptionSlots;
import com.tookancustomer.models.tookanSchedulingModel.Datum;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;

import java.util.ArrayList;
import java.util.Date;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<SortedDatesModel> dataList;
    private ArrayList<Datum> tookanAgentTimeSlotsList;
    private ArrayList<Slot> laundryTimeSlotsList;
    private Callback callback;
    private CallbackTookanAgent callbackTookanAgent;
    private CallbackSubscription callbackSubscription;
    private boolean isTookanAgentFlow = false;
    private boolean autoSelect = false;
    private boolean isSubscriptionFlow = false;
    private ArrayList<SubscriptionSlots> subscriptionSlots;


    public TimeSlotAdapter(Activity activity, ArrayList<SortedDatesModel> dataList, boolean autoSelect, Callback callback) {
        this.activity = activity;
        this.autoSelect = autoSelect;
        this.callback = callback;
        this.dataList = dataList;
        isTookanAgentFlow = false;
    }

    public TimeSlotAdapter(ArrayList<Datum> tookanAgentTimeSlotsList, Activity activity, boolean autoSelect, CallbackTookanAgent callbackTookanAgent) {
        this.activity = activity;
        this.autoSelect = autoSelect;
        this.callbackTookanAgent = callbackTookanAgent;
        this.tookanAgentTimeSlotsList = tookanAgentTimeSlotsList;
        isTookanAgentFlow = true;
    }

    public TimeSlotAdapter(ArrayList<Slot> laundryTimeSlotsList, Activity activity, CallbackTookanAgent callbackTookanAgent) {
        this.activity = activity;
        this.callbackTookanAgent = callbackTookanAgent;
        this.laundryTimeSlotsList = laundryTimeSlotsList;
        isTookanAgentFlow = false;
    }

    public TimeSlotAdapter(final Activity activity, final ArrayList<SubscriptionSlots> subscriptionSlots, final CallbackSubscription callback) {
        this.activity = activity;
        this.callbackSubscription = callback;
        this.subscriptionSlots = subscriptionSlots;
        isSubscriptionFlow = true;
        this.callbackSubscription = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_schedule_time_slots, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();

        if (isTookanAgentFlow) {

            if (activity instanceof ScheduleTimeActivity
                    && ((ScheduleTimeActivity) activity).selectedDateToBeShown != null
                    && ((ScheduleTimeActivity) activity).selectedDateToBeShown.equals(tookanAgentTimeSlotsList.get(adapterPos).getStartTimeDate())
                    && ((ScheduleTimeActivity) activity).isStartDate
                    && !((ScheduleTimeActivity) activity).isSchedulingFromCheckout) {

                viewHolder.tvScheduleTimeSlot.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));

            } else {
                viewHolder.tvScheduleTimeSlot.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.selector_time_slots));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.nine_grey));

            }

            if (autoSelect) {
                viewHolder.tvScheduleTimeSlot.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));
            }
//            viewHolder.tvScheduleTimeSlot.setText(DateUtils.getInstance().getFormattedDate(tookanAgentTimeSlotsList.get(adapterPos).getStartTimeDate(),
//                    UIManager.getTimeFormat()) + " - " + DateUtils.getInstance().getFormattedDate(tookanAgentTimeSlotsList.get(adapterPos).getEndTimeDate(),
//                    UIManager.getTimeFormat()));

            viewHolder.tvScheduleTimeSlot.setText(DateUtils.getInstance().getFormattedDate(tookanAgentTimeSlotsList.get(adapterPos).getStartTimeDate(),
                    UIManager.getTimeFormat()));

            viewHolder.tvScheduleTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackTookanAgent.onTimeSlotSelected(tookanAgentTimeSlotsList.get(adapterPos).getStartTimeDate(),
                            tookanAgentTimeSlotsList.get(adapterPos).getEndTimeDate());
                }
            });

//            if (autoSelect) {
//                autoSelect=false;
//                viewHolder.tvScheduleTimeSlot.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        viewHolder.tvScheduleTimeSlot.performClick();
//
//                    }
//                });
//            }

        } else if(isSubscriptionFlow){

            viewHolder.tvScheduleTimeSlot.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.selector_time_slots));
            viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.nine_grey));

            viewHolder.tvScheduleTimeSlot.setText(DateUtils.getInstance().parseDateAs(subscriptionSlots.get(adapterPos).getSlotTime(), STANDARD_DATE_FORMAT_TZ,Constants.DateFormat.TIME_FORMAT_12_without_ampm));
            viewHolder.tvScheduleTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callbackSubscription.onTimeSlotSelected(subscriptionSlots.get(adapterPos).getSlotTime());
                }
            });

        }
        else if (Dependencies.isLaundryApp()||  (Dependencies.getSelectedProductsArrayList().size()> 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null &&
                Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getDisplayRangeIntervals() == 1)) {

            if (laundryTimeSlotsList.get(adapterPos).getIsBooked() == 1) {
                viewHolder.tvScheduleTimeSlot.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.busy_time_slots));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));

            } else if (activity instanceof ScheduleTimeActivity
                    && ((ScheduleTimeActivity) activity).selectedDateToBeShown != null
                    && ((ScheduleTimeActivity) activity).selectedDateToBeShown.equals(laundryTimeSlotsList.get(adapterPos).getStartTimeDate())
                    && ((ScheduleTimeActivity) activity).isStartDate
                    && !((ScheduleTimeActivity) activity).isSchedulingFromCheckout) {

                viewHolder.tvScheduleTimeSlot.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));

            } else {
                viewHolder.tvScheduleTimeSlot.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.selector_time_slots));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.nine_grey));
            }


            viewHolder.tvScheduleTimeSlot.setText(DateUtils.getInstance().getFormattedDate(laundryTimeSlotsList.get(adapterPos).getStartTimeDate(),
                    UIManager.getTimeFormat()) + " - " + DateUtils.getInstance().getFormattedDate(laundryTimeSlotsList.get(adapterPos).getEndTimeDate(),
                    UIManager.getTimeFormat()));

            viewHolder.tvScheduleTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (laundryTimeSlotsList.get(adapterPos).getIsBooked() == 0)
                        callbackTookanAgent.onTimeSlotSelected(laundryTimeSlotsList.get(adapterPos).getStartTimeDate(), laundryTimeSlotsList.get(adapterPos).getEndTimeDate());
                }
            });
        }
        //TODO
        else {

            if (dataList.size() > 0 && dataList.get(adapterPos).isBooked()) {
                viewHolder.tvScheduleTimeSlot.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.busy_time_slots));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));

            } else if ((activity instanceof ScheduleTimeActivity
                    && ((ScheduleTimeActivity) activity).selectedDateToBeShown != null
                    && ((ScheduleTimeActivity) activity).selectedDateToBeShown.equals(dataList.get(adapterPos).getDateTime())
                    && ((ScheduleTimeActivity) activity).isStartDate
                    && !((ScheduleTimeActivity) activity).isSchedulingFromCheckout)
            ) {
                viewHolder.tvScheduleTimeSlot.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));
            } else {
                viewHolder.tvScheduleTimeSlot.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.selector_time_slots));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.nine_grey));
            }


            if (autoSelect) {
                viewHolder.tvScheduleTimeSlot.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                viewHolder.tvScheduleTimeSlot.setTextColor(activity.getResources().getColor(R.color.white));
            }

            viewHolder.tvScheduleTimeSlot.setText(DateUtils.getInstance().getFormattedDate(dataList.get(adapterPos).getDateTime(),
                    UIManager.getTimeFormatWithoutAmPm()));


            viewHolder.tvScheduleTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!dataList.get(adapterPos).isBooked())
                        callback.onTimeSlotSelected(dataList.get(adapterPos).getDateTime());
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (isTookanAgentFlow)
            return tookanAgentTimeSlotsList != null ? tookanAgentTimeSlotsList.size() : 1;
        else if (isSubscriptionFlow)
            return subscriptionSlots != null ? subscriptionSlots.size() : 1;
        else if (Dependencies.isLaundryApp() ||  (Dependencies.getSelectedProductsArrayList().size()> 0 && Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData() != null &&
                Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getDisplayRangeIntervals() == 1))
            return laundryTimeSlotsList != null ? laundryTimeSlotsList.size() : 1;
        else
            return dataList != null ? dataList.size() : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvScheduleTimeSlot;

        public ViewHolder(View itemView) {
            super(itemView);
            tvScheduleTimeSlot = itemView.findViewById(R.id.tvScheduleTimeSlot);
        }
    }

    public interface Callback {
        void onTimeSlotSelected(Date date);
    }

    public interface CallbackSubscription {
        void onTimeSlotSelected(String date);
    }


    public interface CallbackTookanAgent {
        void onTimeSlotSelected(Date startDate, Date endDate);
    }
}