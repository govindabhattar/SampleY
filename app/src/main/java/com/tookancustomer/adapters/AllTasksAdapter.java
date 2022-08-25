package com.tookancustomer.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.alltaskdata.Data;
import com.tookancustomer.modules.recurring.fragments.AllTaskFragment;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.tookancustomer.BaseActivity.getStrings;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.ViewHolder> implements TerminologyStrings {
    private Activity activity;
    private ArrayList<ArrayList<Data>> dataList;
    private Fragment fragment;
    private Dialog mDialog;
    private FlexibleAdapter flexibleAdapter;

    public AllTasksAdapter(Activity activity, ArrayList<ArrayList<Data>> dataList, Fragment fragment) {
        this.activity = activity;
        this.dataList = dataList;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_all_tasks, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<Data> list = dataList.get(holder.getAdapterPosition());
        final Data data = list.get(0);
        /* Status will be hidden for services and appointment flow ,or if show status key is 1 otherwise it will be visible. */
       /* if ((data.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE && data.getPd_or_appointment() == Constants.ServiceFlow.APPOINTMENT)
                || data.getShowStatus() != 1
                || data.getJobStatus() == -1) {
            holder.rlStatusLayout.setVisibility(View.VISIBLE);
            holder.tvOrderStatus.setText((Constants.TaskStatus.getTaskStatusByValue(data.getJobStatus()).getPassive(activity)));
            holder.tvOrderStatus.setTextColor(activity.getResources().getColor(Constants.TaskStatus.getColorRes(data.getJobStatus())));
        } else {*/
            holder.rlStatusLayout.setVisibility(View.VISIBLE);
            holder.tvOrderStatus.setText((Constants.TaskStatus.getTaskStatusByValue(data.getJobStatus()).getPassive(activity)));
            holder.tvOrderStatus.setTextColor(activity.getResources().getColor(Constants.TaskStatus.getColorRes(data.getJobStatus())));
       // }

        holder.chatIV.setVisibility(UIManager.isFuguChatEnabled() ? View.VISIBLE : View.GONE);
        holder.chatIV.setTag(position);
        holder.chatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                Utils.startChat(dataList.get(pos).get(0).getHippoTransectionId(), dataList.get(pos).get(0).getMerchantName()
                        , dataList.get(pos).get(0).getJobId(), dataList.get(pos).get(0).getIsCustomOrder(), dataList.get(pos).get(0).getGroupingTags());

            }
        });

        if (data.getJobStatus() == Constants.TaskStatus.CANCELLED.value ||
                data.getJobStatus() == Constants.TaskStatus.REJECTED.value ||
                data.getJobStatus() == Constants.TaskStatus.SUCCESSFUL.value ||
                data.getJobStatus() == Constants.TaskStatus.FAILED.value ||
                data.getJobStatus() == Constants.TaskStatus.PENDING_STATUS.value ||
                data.getJobStatus() == Constants.TaskStatus.DECLINED.value ||
                data.getJobStatus() == Constants.TaskStatus.DELIVERED.value)
            if (data.getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE
                    && data.getIsCustomOrder() == 0 && data.getIsMenuEnabled() == 0 &&
                    !Dependencies.isLaundryApp()) {
                if (data.getJobStatus() == Constants.TaskStatus.PENDING_STATUS.value && data.getEditJobStatus() == 1) {
                    holder.tvreorder.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_icon_edit_row, 0);
                    holder.tvreorder.setText(StorefrontCommonData.getTerminology().getEditOrder());
                    holder.tvreorder.setVisibility(View.VISIBLE);

                } else if (data.getJobStatus() != Constants.TaskStatus.PENDING_STATUS.value) {
                    holder.tvreorder.setText(StorefrontCommonData.getTerminology().getReorder());
                    holder.tvreorder.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_refresh, 0);
                    holder.tvreorder.setVisibility(View.VISIBLE);

                } else {
                    holder.tvreorder.setVisibility(View.GONE);
                }
            } else
                holder.tvreorder.setVisibility(View.GONE);
        else
            holder.tvreorder.setVisibility(View.GONE);


        holder.tvreorder.setTag(position);
        holder.tvreorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (fragment instanceof AllTaskFragment) {

                    ((AllTaskFragment) fragment).getMarketplaceStorefronts(pos, data, (data.getJobStatus() == Constants.TaskStatus.PENDING_STATUS.value & data.getEditJobStatus() == 1));
                }
            }
        });

        /*  Order id will be shown to customers here */
        holder.tvOrderId.setText(data.getJobId() + "");


        /*  Handling of pickup date and delivery date  */
        if (data.getBusinessType() == Constants.BusinessType.PRODUCTS_BUSINESS_TYPE) {
            /*  Product Flow  */

            if (data.getIsStartEndTimeEnable().equals(1)) {
                Utils.setVisibility(View.VISIBLE, holder.rlPickupTask, holder.rlDeliveryTask);
                holder.tvPickupDetails.setText(StorefrontCommonData.getTerminology().getStartTime(true) + ":");
                holder.tvDeliveryDetails.setText(StorefrontCommonData.getTerminology().getEndTime(true) + ":");

                String date = DateUtils.getInstance().parseDateAs(data.getJobPickupDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());
                holder.tvPickupDate.setText(date);

                String endDate = DateUtils.getInstance().parseDateAs(data.getJobDeliveryDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());
                holder.tvDeliveryDate.setText(endDate);
            } else {
                Utils.setVisibility(View.VISIBLE, holder.rlPickupTask);
                holder.tvPickupDetails.setText((StorefrontCommonData.getString(activity, R.string.order_time) + StorefrontCommonData.getString(activity, R.string.colon)).replace(ORDER, Utils.getCallTaskAs(true, true)));
//                String date = DateUtils.getInstance().parseDateAs(data.getJobPickupDatetime(), STANDARD_DATE_FORMAT_TZ, END_USER_DATE_FORMAT2);
                String date = DateUtils.getInstance().convertToLocal(data.getCreationDatetime());
                holder.tvPickupDate.setText(date);

                if (data.getIsCustomOrder() == 0) {
                    Utils.setVisibility(View.VISIBLE, holder.rlDeliveryTask);
//                    holder.tvDeliveryDetails.setText(StorefrontCommonData.getString(activity, R.string.scheduled_time) + ":");
                    String scheduledDate, deliveryDeails;
                    if (data.getTaskType() == Constants.TaskTypeConstants.FOOD) {
                        deliveryDeails = StorefrontCommonData.getString(activity, R.string.scheduled_time) + StorefrontCommonData.getString(activity, R.string.colon);
                        scheduledDate = DateUtils.getInstance().parseDateAs(data.getJobDeliveryDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());
                    } else if (data.getTaskType() == Constants.TaskTypeConstants.PICKUP) {
                        deliveryDeails = (StorefrontCommonData.getString(activity, R.string.pickup_time) + StorefrontCommonData.getString(activity, R.string.colon)).replace(PICKUP, StorefrontCommonData.getTerminology().getPickup());
                        scheduledDate = DateUtils.getInstance().parseDateAs(data.getJobPickupDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());
                    } else {
                        deliveryDeails = (StorefrontCommonData.getString(activity, R.string.delivery_time) + StorefrontCommonData.getString(activity, R.string.colon)).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery());
                        scheduledDate = DateUtils.getInstance().parseDateAs(data.getJobDeliveryDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());
                    }
                    holder.tvDeliveryDetails.setText(deliveryDeails);
                    holder.tvDeliveryDate.setText(scheduledDate);
                } else {
                    Utils.setVisibility(View.GONE, holder.rlDeliveryTask);
                }
            }
        } else {
            /*  Services Flow
             * Only creation datetime will be showing */
            Utils.setVisibility(View.VISIBLE, holder.rlPickupTask);
            if (data.getPd_or_appointment() == Constants.ServiceFlow.SERVICE_AS_PRODUCT) {
                String scheduledDate, deliveryDeails;
                Utils.setVisibility(View.VISIBLE, holder.rlDeliveryTask);

                deliveryDeails = StorefrontCommonData.getString(activity, R.string.scheduled_time) + StorefrontCommonData.getString(activity, R.string.colon);
                scheduledDate = DateUtils.getInstance().parseDateAs(data.getJobDeliveryDatetime(), STANDARD_DATE_FORMAT_TZ, UIManager.getDateTimeFormat());

                holder.tvDeliveryDetails.setText(deliveryDeails);
                holder.tvDeliveryDate.setText(scheduledDate);
            } else {
                Utils.setVisibility(View.GONE, holder.rlDeliveryTask);
            }
            holder.tvPickupDetails.setText((StorefrontCommonData.getString(activity, R.string.order_created_at) + StorefrontCommonData.getString(activity, R.string.colon)).replace(ORDER, Utils.getCallTaskAs(true, true)));
            String date = DateUtils.getInstance().convertToLocal(data.getCreationDatetime());
            holder.tvPickupDate.setText(date);
        }


        /*  Order total amount  will be shown to customers in case of total price > 0 and show product price key ==1. */
        if (StorefrontCommonData.getUserData().getData().getFormSettings().get(0).getShowProductPrice() == 0 && Double.valueOf(data.getTotalAmount()) <= 0) {
            holder.rlPrice.setVisibility(View.GONE);
        } else {
            holder.rlPrice.setVisibility(View.VISIBLE);
            if (data.getOrderCurrencySymbol() != null)
                holder.tvOrderPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbolNew(data.getOrderCurrencySymbol()) + data.getTotalAmount() + ""));
            else
                holder.tvOrderPrice.setText(UIManager.getCurrency(Utils.getCurrencySymbolNew(null) + data.getTotalAmount() + ""));
        }


        /*  Order ratings will be shown to customers if it is already rated. */
        if (data.showRatings()) {
            holder.rlRatings.setVisibility(View.VISIBLE);
            Utils.addStarsToLayout(activity, holder.llShowRatings, data.getCustomerRating().doubleValue());
        } else {
            holder.rlRatings.setVisibility(View.GONE);
        }


        holder.tvOrderStatus.setVisibility(data.getShowStatus() == 1 ? View.VISIBLE : View.GONE);
        holder.tvOrderStatus.setText((Constants.TaskStatus.getTaskStatusByValue(data.getJobStatus()).getPassive(activity)));
        holder.tvOrderStatus.setTextColor(ContextCompat.getColor(activity, Constants.TaskStatus.getColorRes(data.getJobStatus())));

        try {
            SpannableString spannableString = new SpannableString(getStrings(activity, R.string.click_here));
            spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);

            SpannableStringBuilder summary = new SpannableStringBuilder();
            summary.append(getStrings(activity, R.string.to_view_order_history_please).replace(TerminologyStrings.ORDER,
                    StorefrontCommonData.getTerminology().getOrder()) + " ")
                    .append(spannableString);

            holder.tv_trackOrder.setText(summary);

        } catch (Exception e) {
            Utils.printStackTrace(e);
        }

        if (data.getOrderHistory() != null && !data.getOrderHistory().isEmpty())
            holder.tv_trackOrder.setVisibility(View.VISIBLE);
        else holder.tv_trackOrder.setVisibility(View.GONE);

        holder.tv_trackOrder.setOnClickListener(v -> {
            showOrderTracking(data);
        });

    }

    private void showOrderTracking(Data data) {
        mDialog = new Dialog(activity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_order_tracking);
        mDialog.setCanceledOnTouchOutside(true);

        ImageView ivCloseDialog = mDialog.findViewById(R.id.ivCloseDialog);
        ivCloseDialog.setOnClickListener(v -> mDialog.dismiss());
        TextView tv_orderId = mDialog.findViewById(R.id.tvorderId);
        tv_orderId.setText(((StorefrontCommonData.getString(activity, R.string.orderId)
                + StorefrontCommonData.getString(activity,
                R.string.colon)).replace(ORDER, Utils.getCallTaskAs(true, true)) + " " + "#" + data.getJobId()));
        RecyclerView milestones_list = mDialog.findViewById(R.id.milestones_list);
        milestones_list.setLayoutManager(new SmoothScrollLinearLayoutManager(activity));
        flexibleAdapter = new FlexibleAdapter(getItems(data), this, true);
        milestones_list.setAdapter(flexibleAdapter);
        milestones_list.setHasFixedSize(true);

        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    private List getItems(Data data) {
        ArrayList items = new ArrayList();
        if (data.getOrderHistory() != null && !data.getOrderHistory().isEmpty()) {
            for (int i = 0; i < data.getOrderHistory().size(); i++) {
                items.add(new MilestoneCompareItem(activity, data.getOrderHistory().get(i)));
            }
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvParent;
        RelativeLayout rlStatusLayout, rlPickupTask, rlDeliveryTask, rlPrice, rlRatings;
        TextView textViewStatus, textViewId, tvPickupDetails, tvDeliveryDetails, tvPrice, tvRatings;
        TextView tvOrderStatus, tvOrderId, tvPickupDate, tvDeliveryDate, tvOrderPrice, tvreorder, tv_trackOrder;
        LinearLayout llShowRatings;
        ImageView chatIV;

        ViewHolder(View itemView) {
            super(itemView);
            cvParent = itemView.findViewById(R.id.cvParent);
            chatIV = itemView.findViewById(R.id.chatIV);
            tvreorder = itemView.findViewById(R.id.tvreorder);
            cvParent.setOnClickListener(this);

            rlStatusLayout = itemView.findViewById(R.id.rlStatusLayout);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewStatus.setText(StorefrontCommonData.getString(activity, R.string.status_semicolon));
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);

            textViewId = itemView.findViewById(R.id.textViewId);
            textViewId.setText((StorefrontCommonData.getString(activity, R.string.orderId) + StorefrontCommonData.getString(activity, R.string.colon)).replace(ORDER, Utils.getCallTaskAs(true, true)));
            tvOrderId = itemView.findViewById(R.id.tvOrderId);

            rlPickupTask = itemView.findViewById(R.id.rlPickupTask);
            tvPickupDetails = itemView.findViewById(R.id.tvPickupDetails);
            tvPickupDate = itemView.findViewById(R.id.tvPickupDate);

            rlDeliveryTask = itemView.findViewById(R.id.rlDeliveryTask);
            tvDeliveryDetails = itemView.findViewById(R.id.tvDeliveryDetails);
            tvDeliveryDetails.setText(StorefrontCommonData.getString(activity, R.string.delivery_details_semicolon));
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);

            rlPrice = itemView.findViewById(R.id.rlPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPrice.setText((StorefrontCommonData.getString(activity, R.string.orderAmount) + StorefrontCommonData.getString(activity, R.string.colon)).replace(ORDER, Utils.getCallTaskAs(true, true)));
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);

            rlRatings = itemView.findViewById(R.id.rlRatings);
            tvRatings = itemView.findViewById(R.id.tvRatings);
            tvRatings.setText(StorefrontCommonData.getString(activity, R.string.rating_semicolon));
            llShowRatings = itemView.findViewById(R.id.llShowRatings);
            tv_trackOrder = itemView.findViewById(R.id.tv_trackOrder);
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
            Bundle extras = new Bundle();
            ArrayList<Data> list = dataList.get(getAdapterPosition());

            extras.putInt(Keys.Extras.JOB_ID, list.get(0).getJobId());
            if (list.get(0).getTrackingLink() != null) {
                extras.putString("TRACKING_LINK", list.get(0).getTrackingLink());
            }
            extras.putInt("from", 1);
            extras.putInt("form_id", list.get(0).getFormId());
            Transition.startActivity(activity, Transition.launchOrderDetailsActivity(), extras, false);
        }
    }
}
