package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.tcqq.timelineview.TimelineView;
import com.tookancustomer.R;
import com.tookancustomer.models.alltaskdata.Data;
import com.tookancustomer.models.alltaskdata.OrderHistory;
import com.tookancustomer.models.alltaskdata.VectorDrawableUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

public final class MilestoneCompareItem extends AbstractFlexibleItem<MilestoneCompareItem.ViewHolder> {
    private String orderStatus, dateTime;
    private Activity activity;
    private OrderHistory orderHistory;

    public MilestoneCompareItem(Activity activity, OrderHistory orderHistory) {

        this.activity = activity;
        this.orderHistory = orderHistory;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_milestone;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ViewHolder holder, int position, List<Object> payloads) {
        holder.tvOrderStatus.setText(orderHistory.getLabel());
        if(orderHistory.getCreationDatetime() != null)
            dateTime = DateUtils.getInstance().parseDateAs(orderHistory.getCreationDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());

        holder.tvOrderTime.setText(dateTime);
        holder.timeline.initLine(TimelineView.getTimeLineViewType(position, adapter.getItemCount()));
        if(orderHistory.getStatusCompleted()==1){
            holder.timeline.setMarker(activity.getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
        }else {
            holder.timeline.setMarker(activity.getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
        }

    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new MilestoneCompareItem.ViewHolder(view, adapter);
    }


    public static final class ViewHolder extends FlexibleViewHolder {
        TimelineView timeline;
        AppCompatTextView tvOrderStatus, tvOrderTime;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            timeline = view.findViewById(R.id.timeline);
            tvOrderStatus = view.findViewById(R.id.tv_orderStatus);
            tvOrderTime = view.findViewById(R.id.tv_orderTime);
        }
    }
}
