package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.models.OnGoingOrdersData.OnGoingOrderData;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class OngoingTaskAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;

    private Activity activity;
    private ArrayList<OnGoingOrderData> onGoingOrderDatas;

    public OngoingTaskAdapter(Activity activity, ArrayList<OnGoingOrderData> onGoingOrderDatas) {
        this.activity = activity;
        this.onGoingOrderDatas = onGoingOrderDatas;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final OnGoingOrderData onGoingOrderData = onGoingOrderDatas.get(position);
        View taskView = layoutInflater.inflate(R.layout.ongoing_order_view, container, false);

        // View to display the Image
        //find items
        ImageView tracking_image_iv = taskView.findViewById(R.id.tracking_image_iv);
        TextView storeNameTV = taskView.findViewById(R.id.storeNameTV);
        TextView taskStatusTV = taskView.findViewById(R.id.taskStatusTV);
        ImageView chatIV = taskView.findViewById(R.id.chatIV);
        ImageView trackIV = taskView.findViewById(R.id.trackIV);
        chatIV.setVisibility(UIManager.isFuguChatEnabled() ? View.VISIBLE : View.GONE);

        if ((onGoingOrderData.getJobStatus() == Constants.TaskStatus.STARTED.value
                || onGoingOrderData.getJobStatus() == Constants.TaskStatus.DISPATCHED.value
                || onGoingOrderData.getJobStatus() == Constants.TaskStatus.ORDERASSIGNED.value
                || onGoingOrderData.getJobStatus() == Constants.TaskStatus.PROCESSED.value
                || onGoingOrderData.getJobStatus() == Constants.TaskStatus.PICKED_UP.value
                || onGoingOrderData.getJobStatus() == Constants.TaskStatus.ORDERED.value) && onGoingOrderData.getTrackingLink() != null
                && onGoingOrderData.getTrackingLink().size() > 0) {
            trackIV.setVisibility(View.VISIBLE);
        } else {
            trackIV.setVisibility(View.GONE);
        }

//        TextView taskActionTV = taskView.findViewById(R.id.taskActionTV);
        LinearLayout detailLL = taskView.findViewById(R.id.detailLL);

        storeNameTV.setText(onGoingOrderData.getDisplayName());
        taskStatusTV.setText(onGoingOrderData.getMessage());

        new GlideUtil.GlideUtilBuilder(tracking_image_iv)
                .setPlaceholder(R.drawable.ic_plcaeholder_marketplace_new)
                .setCenterCrop(true)
                .setLoadItem(onGoingOrderData.getLogo())
                .setTransformation(new CircleCrop())
                .build();

        chatIV.setTag(position);

        chatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Utils.startChat(onGoingOrderDatas.get(pos).getHippoTransectionId(), onGoingOrderDatas.get(pos).getStoreName()
                        , onGoingOrderDatas.get(pos).getJobId(), onGoingOrderDatas.get(pos).getIsCustomOrder(), onGoingOrderDatas.get(pos).getGroupingTags());
            }
        });

        detailLL.setTag(position);
        detailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Bundle extras = new Bundle();
                extras.putInt(Keys.Extras.JOB_ID, onGoingOrderDatas.get(pos).getJobId());
                extras.putInt("from", 1);
                extras.putInt("form_id", 0);
                Transition.startActivity(activity, Transition.launchOrderDetailsActivity(), extras, false);
            }
        });
        trackIV.setTag(position);
        trackIV.setOnClickListener(v -> {
            int pos = (int) v.getTag();
            Bundle extras = new Bundle();
            extras.putInt(Keys.Extras.JOB_ID, onGoingOrderDatas.get(pos).getJobId());
            extras.putInt("from", 1);
            extras.putInt("form_id", 0);
            Transition.startActivity(activity, Transition.launchOrderDetailsActivity(), extras, false);
        });


        // Add the View to container
        container.addView(taskView);

        return taskView;
    }


    @Override
    public int getCount() {
        return onGoingOrderDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}

