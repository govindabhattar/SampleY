package com.tookancustomer.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.RecurringSurgeListData;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_12;
import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_24_no_seconds;

public class RecuringSurgeDetailAdapter extends RecyclerView.Adapter<RecuringSurgeDetailAdapter.MyViewHolder> {

    private ArrayList<RecurringSurgeListData> recurringSurgeListData;
    private Activity activity;
    private int noOfFields;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView amountTV, occurrenceTV, amountChargeTV, timeTV, dayTV;


        public MyViewHolder(View view) {
            super(view);
            amountTV = view.findViewById(R.id.amountTV);
            occurrenceTV = view.findViewById(R.id.occurrenceTV);
            amountChargeTV = view.findViewById(R.id.amountChargeTV);
            timeTV = view.findViewById(R.id.timeTV);
            dayTV = view.findViewById(R.id.dayTV);

        }
    }


    public RecuringSurgeDetailAdapter(Activity activity, ArrayList<RecurringSurgeListData> recurringSurgeListData, int noOfFields) {
        this.recurringSurgeListData = recurringSurgeListData;
        this.activity = activity;
        this.noOfFields = noOfFields;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (noOfFields == 3)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recuring_surge_details_three, parent, false);
        else if (noOfFields == 4)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recuring_surge_details_four, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recuring_surge_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.dayTV.setText(getDayText(recurringSurgeListData.get(position).getDayId()));
        holder.occurrenceTV.setText(recurringSurgeListData.get(position).getOccurances() + "");
        holder.timeTV.setText(DateUtils.getInstance().convertTimeTodesiredFormet(recurringSurgeListData.get(position).getScheduleTime(),
                TIME_FORMAT_12, TIME_FORMAT_24_no_seconds));
        if (noOfFields == 3) {
            holder.amountChargeTV.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits((recurringSurgeListData.get(position).getDeliveryCharges()))));
        } else
            holder.amountChargeTV.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits((recurringSurgeListData.get(position).getAmount()))));
        holder.amountTV.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits((recurringSurgeListData.get(position).getTotalAmount()))));

        if ((position / 2) * 2 == position) {
            //white
            holder.amountTV.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.occurrenceTV.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.amountChargeTV.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.timeTV.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.dayTV.setBackgroundColor(activity.getResources().getColor(R.color.white));
        } else {
            //gray
            holder.amountTV.setBackgroundColor(activity.getResources().getColor(R.color.gray_));
            holder.occurrenceTV.setBackgroundColor(activity.getResources().getColor(R.color.gray_));
            holder.amountChargeTV.setBackgroundColor(activity.getResources().getColor(R.color.gray_));
            holder.timeTV.setBackgroundColor(activity.getResources().getColor(R.color.gray_));
            holder.dayTV.setBackgroundColor(activity.getResources().getColor(R.color.gray_));

        }
    }

    @Override
    public int getItemCount() {
        return recurringSurgeListData.size();
    }

    private String getDayText(int dayId) {
        if (dayId == 0)
            return StorefrontCommonData.getString(activity, R.string.sunday_text);
        else if (dayId == 1)
            return StorefrontCommonData.getString(activity, R.string.monday_text);
        else if (dayId == 2)
            return StorefrontCommonData.getString(activity, R.string.tuesday_text);
        else if (dayId == 3)
            return StorefrontCommonData.getString(activity, R.string.wednesday_text);
        else if (dayId == 4)
            return StorefrontCommonData.getString(activity, R.string.thursday_text);
        else if (dayId == 5)
            return StorefrontCommonData.getString(activity, R.string.friday_text);
        else
            return StorefrontCommonData.getString(activity, R.string.saturday_text);
    }
}
