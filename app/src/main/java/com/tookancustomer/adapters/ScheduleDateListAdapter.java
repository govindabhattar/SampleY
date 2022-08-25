package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.utility.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class ScheduleDateListAdapter extends RecyclerView.Adapter<ScheduleDateListAdapter.ViewHolder> {
    private Activity activity;
    private List<Date> dataList;
    private Callback callback;
    int selectedPosition = 0;
    private boolean isSchedulingFromCheckout = true;

    public ScheduleDateListAdapter(Activity activity, boolean isSchedulingFromCheckout, List<Date> dataList, Callback callback) {
        this.activity = activity;
        this.callback = callback;
        this.dataList = dataList;
        this.isSchedulingFromCheckout = isSchedulingFromCheckout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_schedule_date_slots, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataList.get(adapterPos));

        viewHolder.llScheduleDateSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = adapterPos;
                notifyDataSetChanged();
                callback.onDateSelected(dataList.get(adapterPos));
            }
        });

        String weekday = DateUtils.getInstance().getDateFormatSymbols().getShortWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
        String monthDay = DateUtils.getInstance().getDateFormatSymbols().getShortMonths()[cal.get(Calendar.MONTH)];

        viewHolder.tvScheduleDay.setText(weekday);
        viewHolder.tvScheduleDate.setText(isSchedulingFromCheckout ? cal.get(Calendar.DATE) + "" : +cal.get(Calendar.DATE) + "");
        viewHolder.tvScheduleMonth.setText(monthDay);

        viewHolder.tvScheduleDay.setTextSize(TypedValue.COMPLEX_UNIT_PX,isSchedulingFromCheckout ? activity.getResources().getDimension(R.dimen.text_size_large):activity.getResources().getDimension(R.dimen.text_size_normal));
        viewHolder.tvScheduleDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,isSchedulingFromCheckout ? activity.getResources().getDimension(R.dimen.text_size_large):activity.getResources().getDimension(R.dimen.text_size_normal));
        viewHolder.tvScheduleMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX,isSchedulingFromCheckout ? activity.getResources().getDimension(R.dimen.text_size_large):activity.getResources().getDimension(R.dimen.text_size_normal));

        viewHolder.tvScheduleMonth.setVisibility(isSchedulingFromCheckout ? View.GONE : View.VISIBLE);

        if (adapterPos == selectedPosition) {
            viewHolder.llScheduleDateSlots.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
            viewHolder.vwScheduleDateFooter.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
            viewHolder.vwScheduleDateHeader.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
            viewHolder.tvScheduleDay.setTextColor(activity.getResources().getColor(R.color.white));
            viewHolder.tvScheduleDate.setTextColor(activity.getResources().getColor(R.color.white));
            viewHolder.tvScheduleMonth.setTextColor(activity.getResources().getColor(R.color.white));
        } else {
            viewHolder.llScheduleDateSlots.setBackgroundColor(activity.getResources().getColor(R.color.white));
            viewHolder.vwScheduleDateFooter.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shadow_gradient));
            viewHolder.vwScheduleDateHeader.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shadow_gradient));
            viewHolder.tvScheduleDay.setTextColor(activity.getResources().getColor(R.color.medium_grey));
            viewHolder.tvScheduleDate.setTextColor(activity.getResources().getColor(R.color.medium_grey));
            viewHolder.tvScheduleMonth.setTextColor(activity.getResources().getColor(R.color.medium_grey));
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llScheduleDateSlots;
        TextView tvScheduleDay, tvScheduleDate, tvScheduleMonth;
        View vwScheduleDateFooter, vwScheduleDateHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            llScheduleDateSlots = itemView.findViewById(R.id.llScheduleDateSlots);
            tvScheduleDay = itemView.findViewById(R.id.tvScheduleDay);
            tvScheduleDate = itemView.findViewById(R.id.tvScheduleDate);
            tvScheduleMonth = itemView.findViewById(R.id.tvScheduleMonth);
            vwScheduleDateFooter = itemView.findViewById(R.id.vwScheduleDateFooter);
            vwScheduleDateHeader = itemView.findViewById(R.id.vwScheduleDateHeader);
        }
    }

    public interface Callback {
        void onDateSelected(Date date);
    }
}