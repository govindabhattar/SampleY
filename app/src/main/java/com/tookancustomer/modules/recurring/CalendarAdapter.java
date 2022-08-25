package com.tookancustomer.modules.recurring;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.dialog.AlertDialog;

import java.util.ArrayList;
import java.util.Locale;

import com.tookancustomer.modules.recurring.listener.CalenderListener;
import com.tookancustomer.modules.recurring.model.DateInfo;
import com.tookancustomer.modules.recurring.model.DateItem;

/**
 * Developer: Rishabh
 * Dated: 17/07/15.
 */
public class CalendarAdapter extends BaseAdapter {


    private AlertDialog alertDialog;
    private ArrayList<DateInfo> dateInfoList;
    private TextView tvPreviousItem;
    private Context mContext;
    private CalenderListener calenderListener;
    private Activity mActivity;


    /**
     * Constructor to initialize items
     *
     * @param dateInfoList
     */
    public CalendarAdapter(CalenderListener calenderListener, ArrayList<DateInfo> dateInfoList) {

        this.dateInfoList = dateInfoList;
        this.mActivity = (Activity) calenderListener;
        this.calenderListener = calenderListener;
    }

    @Override
    public int getCount() {
        return dateInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return dateInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View dateItemView = convertView;

        ViewHolder holder;
        mContext = parent.getContext();

        if (dateItemView == null) {

            dateItemView = LayoutInflater.from(mContext).inflate(R.layout.layout_date_item, parent, false);

            holder = new ViewHolder();
            holder.tvDay = dateItemView.findViewById(R.id.tvDay);
            holder.imgDot = dateItemView.findViewById(R.id.imgDot);
            holder.rlDateItem = dateItemView.findViewById(R.id.rlDateItem);

            dateItemView.setTag(holder);

        } else {

            holder = (ViewHolder) dateItemView.getTag();
        }

        holder.position = position;

        render(holder);

        return dateItemView;
    }

    /**
     * Method to render data inside the Holder
     *
     * @param holder
     */
    private void render(final ViewHolder holder) {
        final DateInfo dateInfo = dateInfoList.get(holder.position);

        final DateItem dateItem = dateInfo.getDateItem();
//        final DateTasks dateTasks = dateInfo.getDateTasks();


//        if (mActivity instanceof HomeActivity) {
//            if (dateTasks != null && dateTasks.getIncomplete_tasks() != null && Integer.parseInt(dateTasks.getIncomplete_tasks()) > 0) {
//                holder.imgDot.setImageResource(R.drawable.accent_oval);
//            } else {
//                holder.imgDot.setImageDrawable(null);
//            }
//        } else {
//            holder.imgDot.setImageDrawable(null);
//        }
        holder.imgDot.setImageDrawable(null);

        if (calenderListener.isDateSelected(dateItem)) {
            holder.tvDay.setBackground(ContextCompat.getDrawable(mContext, R.drawable.black_20_color_oval));
            holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.white));
//            calenderListener.updateCalender(dateTasks);
            tvPreviousItem = holder.tvDay;
        } else {
            holder.tvDay.setBackground(null);
            holder.tvDay.setTextColor(Color.LTGRAY);
//            holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }

        if (dateInfo.isExtra()) {
            holder.tvDay.setTextColor(Color.LTGRAY);
            holder.rlDateItem.setEnabled(false);
            holder.tvDay.setVisibility(View.INVISIBLE);
//            holder.tvDay.setVisibility(View.VISIBLE);
        }
//        else if (dateTasks != null) {
//            String totalTasks = dateTasks.getTotal_tasks();
//
//            if (!totalTasks.equals("0")) {
//                holder.rlDateItem.setEnabled(true);
//            }
//            if (alertDialog != null) {
//                alertDialog.dismiss();
//            }
//
//            holder.tvDay.setVisibility(View.VISIBLE);
//        }
        else {
            holder.tvDay.setVisibility(View.VISIBLE);
        }

        holder.rlDateItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                if (dateTasks == null) {
//                    String message;
////                    if (mActivity instanceof HomeActivity) {
////                        message = AppManager.getInstance().isEnglishLanguage(mContext) ? Restring.getString(mContext, R.string.fetching) + " " + AppManager.getInstance().callTaskas(mContext).toLowerCase(new Locale(AppManager.getInstance().getLanguage(mContext))) + "...." + Restring.getString(mContext, R.string.please_wait_text) : Restring.getString(mContext, R.string.fetching_task_please_wait);
////                    } else {
////                        message = mActivity.getString(R.string.fetching_transaction_please_wait);
////                    }
//                    message = "Please wait";
////                    Utils.snackBar(mActivity, message);
//                } else {
//                    if (tvPreviousItem != null) {
//                        tvPreviousItem.setBackground(null);
//                        tvPreviousItem.setTextColor(ContextCompat.getColor(mContext, R.color.black));
//                    }
//                    tvPreviousItem = holder.tvDay;
//                    holder.tvDay.setBackground(ContextCompat.getDrawable(mContext, R.drawable.black_20_color_oval));
//                    holder.tvDay.setTextColor(ContextCompat.getColor(mContext, R.color.white));
////                    calenderListener.updateCalender(dateTasks);
//
//
//                }

                calenderListener.dateChecked(dateInfo, holder.position);

            }
        });
        holder.tvDay.setText(String.format(Locale.getDefault(), "%d", dateItem.getDay()));
        Log.e("date: ", Integer.toString(dateItem.getDay()));


        //            holder.rlDateItem.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));

        if (dateInfo.isActive()) {
        if (dateInfo.isSelected()) {
//                holder.rlDateItem.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.status_successful));
            holder.tvDay.setBackground(ContextCompat.getDrawable(mContext, R.drawable.accent_oval));
            holder.tvDay.setTextColor(ContextCompat.getColor(mActivity, R.color.white));

        } else {
//                holder.rlDateItem.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.status_assigned));
            holder.tvDay.setBackground(ContextCompat.getDrawable(mContext, R.drawable.transparent_background));
            holder.tvDay.setTextColor(ContextCompat.getColor(mActivity, R.color.black));

        }
    }

        else {
//            holder.rlDateItem.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));

        }

    }

    /**
     * Class to hold the values of data
     * for Views in the ListAdapter
     */
    private static class ViewHolder {
        int position;

        TextView tvDay;
        ImageView imgDot;
        RelativeLayout rlDateItem;
    }
}
