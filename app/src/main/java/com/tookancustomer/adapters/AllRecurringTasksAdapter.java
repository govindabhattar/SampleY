package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.TasksActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.allrecurringdata.Result;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import com.tookancustomer.modules.recurring.RecurringTaskDetailsActivity;

import static com.tookancustomer.appdata.Constants.DateFormat.ONLY_DATE;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;
import static com.tookancustomer.appdata.TerminologyStrings.DELIVERY;

public class AllRecurringTasksAdapter extends RecyclerView.Adapter<AllRecurringTasksAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<Result> dataList;

    public AllRecurringTasksAdapter(Activity activity, ArrayList<Result> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_all_ruccring_task, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        Result result = dataList.get(adapterPosition);


//        holder.tvOrderStatus.setText(activity.getString(R.string.subscribe));
        holder.tvOrderStatus.setText(Constants.RecurringStatus.getTaskStatusByValue(result.getStatus()).getPassive(activity));
        holder.tvOrderStatus.setTextColor(activity.getResources().getColor(Constants.RecurringStatus.getColorRes(result.getStatus())));
        holder.tvOrderId.setText(result.getRuleId() + "");

        holder.tvStartDate.setText(DateUtils.getInstance().parseDateAs(result.getStartSchedule(), STANDARD_DATE_FORMAT_TZ,
                ONLY_DATE));


//        if (result.getOccurenceCount() != null && result.getOccurenceCount() != 0) {
        if (result.getScheduleType() == Constants.RecurringType.OCCURENCE_COUNT) {
            holder.rlEndDate.setVisibility(View.GONE);
            holder.rlEndOccurrence.setVisibility(View.VISIBLE);
            holder.tvEndOccurrence.setText(result.getOccurenceCount() + "");

        } else {
            holder.rlEndDate.setVisibility(View.VISIBLE);
            holder.rlEndOccurrence.setVisibility(View.GONE);
            holder.tvEndDate.setText(DateUtils.getInstance().parseDateAs(result.getEndSchedule(), STANDARD_DATE_FORMAT_TZ,
                    ONLY_DATE));

        }

//        holder.tvEndOccurrence.setText(result.getOccurenceCount() + "");
//
//        holder.tvEndDate.setText(DateUtils.getInstance().parseDateAs(result.getEndSchedule(), STANDARD_DATE_FORMAT_TZ,
//                Constants.DateFormat.ONLY_DATE));

//
//        holder.tvDeliveryTime.setText(DateUtils.getInstance().parseDateAs(result.getScheduleTime(), STANDARD_DATE_FORMAT_TZ,
//                UIManager.getTimeFormat()));

        holder.tvDeliveryTime.setText(DateUtils.getInstance().parseDateAs(result.getScheduleTime(), Constants.DateFormat.TIME_FORMAT_24_WITHOUT_SECOND,
                UIManager.getTimeFormat()));

        if (result.getOrderCurrencySymbol() != null)
            holder.tvOrderPrice.setText(Utils.getCurrencySymbolNew(result.getOrderCurrencySymbol()) + "" + result.getAmount() + "");
        else
            holder.tvOrderPrice.setText(Utils.getCurrencySymbolNew(null) + "" + result.getAmount() + "");

    }

    public void setData(ArrayList<Result> dataList) {

        this.dataList = dataList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvParent;
        RelativeLayout rlStatusLayout, rlPrice, rlEndOccurrence, rlEndDate;
        TextView textViewStatus, textViewId, tvPrice;
        TextView tvOrderStatus, tvOrderId, tvDeliveryDate, tvOrderPrice, tvStartDateLabel, tvEndOccurrence, tvEndOccurrenceLabel, tvStartDate, tvEndDateLabel, tvEndDate, tvDeliveryTimeLabel, tvDeliveryTime;


        ViewHolder(View itemView) {
            super(itemView);
            cvParent = itemView.findViewById(R.id.cvParent);
            cvParent.setOnClickListener(this);

            rlStatusLayout = itemView.findViewById(R.id.rlStatusLayout);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewStatus.setText(StorefrontCommonData.getString(activity, R.string.status_semicolon));
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);

            textViewId = itemView.findViewById(R.id.textViewId);
            textViewId.setText(Utils.getCallTaskAs(true, true) + " " + StorefrontCommonData.getString(activity, R.string.order_id));
            tvOrderId = itemView.findViewById(R.id.tvOrderId);

            tvStartDateLabel = itemView.findViewById(R.id.tvStartDateLabel);
            tvStartDateLabel.setText(((TasksActivity) activity).getStrings(R.string.start_date));
            tvStartDate = itemView.findViewById(R.id.tvStartDate);

            tvEndDateLabel = itemView.findViewById(R.id.tvEndDateLabel);
            tvEndDateLabel.setText(((TasksActivity) activity).getStrings(R.string.end_date));
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);

            tvEndOccurrenceLabel = itemView.findViewById(R.id.tvEndOccurrenceLabel);
            tvEndOccurrenceLabel.setText(((TasksActivity) activity).getStrings(R.string.occurrences));
            tvEndOccurrence = itemView.findViewById(R.id.tvEndOccurrence);

            tvDeliveryTimeLabel = itemView.findViewById(R.id.tvDeliveryTimeLabel);
            tvDeliveryTimeLabel.setText(((TasksActivity) activity).getStrings(R.string.delivery_time).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery(true)));
            tvDeliveryTime = itemView.findViewById(R.id.tvDeliveryTime);

            rlEndDate = itemView.findViewById(R.id.rlEndDate);
            rlEndOccurrence = itemView.findViewById(R.id.rlEndOccurrence);

            rlPrice = itemView.findViewById(R.id.rlPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPrice.setText(Utils.getCallTaskAs(true, true) + " " + StorefrontCommonData.getString(activity, R.string.order_amount));
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);


        }

        @Override
        public void onClick(View v) {
            if (!Utils.preventMultipleClicks()) {
                return;
            }
            if (!Utils.internetCheck(activity)) {
                new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                return;
            }

            Intent recurringDetailsIntent = new Intent(activity, RecurringTaskDetailsActivity.class);
            recurringDetailsIntent.putExtra(Keys.Extras.RULE_ID, dataList.get(getAdapterPosition()).getRuleId());
            activity.startActivity(recurringDetailsIntent);


        }
    }
}