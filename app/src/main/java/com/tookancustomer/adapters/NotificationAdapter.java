package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.TasksActivity;
import com.tookancustomer.UserDebtActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.NotificationsModelResponse.Datum;
import com.tookancustomer.models.userDebt.UserDebtData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_TZ;

/**
 * Created by cl-macmini-25 on 11/01/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> implements TerminologyStrings {
    private Activity activity;
    private ArrayList<Datum> dataList;

    public NotificationAdapter(Activity activity, ArrayList<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View taskItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_notification, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();

        viewHolder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataList.get(adapterPos).getJobId() != 0) {

                    if (!Utils.preventMultipleClicks()) {
                        return;
                    }
                    if (!Utils.internetCheck(activity)) {
                        new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                        return;
                    }

                    dataList.get(adapterPos).setIsRead(1);
                    notifyItemChanged(adapterPos);

                    int pushType = dataList.get(adapterPos).getPushType();

                    if (pushType == Constants.NotificationFlags.USER_DEBT_PENDING) {
                        getJobIds();
                    } else if (isRecurring(pushType)) {
                        Bundle extras = new Bundle();
                        extras.putInt("flag", pushType);
                        Intent intent = new Intent(activity, TasksActivity.class);
                        intent.putExtras(extras);
                        activity.startActivity(intent);
                    } else if (pushType == Constants.NotificationFlags.ORDER_FAILURE) {
                        Utils.snackBar(activity, dataList.get(adapterPos).getBody(), false);
                    } else {

                        Bundle extras = new Bundle();
                        extras.putInt(Keys.Extras.JOB_ID, dataList.get(adapterPos).getJobId());
                        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                        extras.putInt("from", 1);
                        extras.putInt("form_id", StorefrontCommonData.getFormSettings().getFormId());
                        Transition.transitForResult(activity, Transition.launchOrderDetailsActivity(), Codes.Request.OPEN_TASK_DETAIL_ACTIVITY, extras, false);
                    }
                }else{
                    Utils.snackBar(activity,dataList.get(adapterPos).getBody());
                }

            }
        });

        if (isRecurring(dataList.get(adapterPos).getPushType())) {
            viewHolder.tvOrderId.setVisibility(View.GONE);
        } else {
            if (dataList.get(adapterPos).getJobId() > 0) {
                viewHolder.tvOrderId.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvOrderId.setVisibility(View.GONE);
            }

        }
        viewHolder.tvOrderId.setText((StorefrontCommonData.getString(activity, R.string.orderId) + StorefrontCommonData.getString(activity, R.string.colon)).replace(ORDER, Utils.getCallTaskAs(true, true)) + " " + dataList.get(adapterPos).getJobId());


        viewHolder.tvHeaderNotification.setText(dataList.get(adapterPos).getTitle());
        viewHolder.tvBodyNotification.setText(dataList.get(adapterPos).getBody());
        viewHolder.tvDateNotification.setText(DateUtils.getInstance().convertToLocal(dataList.get(adapterPos).getCreationDatetime(), STANDARD_DATE_FORMAT_TZ));
        if (dataList.get(adapterPos).getIsRead() == 1) {
            viewHolder.cvParent.setCardBackgroundColor(activity.getResources().getColor(R.color.white));
        } else {
            int halfTransparentColor = Utils.adjustAlpha(0xFFCCCCCC, 0.5f);
            viewHolder.cvParent.setCardBackgroundColor(halfTransparentColor);
//            viewHolder.cvParent.setCardBackgroundColor(activity.getResources().getColor(R.color.black_05));
        }
    }

    boolean isRecurring(int pushType) {
        if ((pushType == Constants.NotificationFlags.RULE_ACCEPTED) || (pushType == Constants.NotificationFlags.RULE_REJECTED)
                || (pushType == Constants.NotificationFlags.RECURRING_TASK_CREATION_FAIL) || (pushType == Constants.NotificationFlags.RULE_CREATED)) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvParent;
        TextView tvOrderId, tvHeaderNotification, tvBodyNotification, tvDateNotification;

        public ViewHolder(View itemView) {
            super(itemView);
            cvParent = itemView.findViewById(R.id.cvParent);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvHeaderNotification = itemView.findViewById(R.id.tvHeaderNotification);
            tvBodyNotification = itemView.findViewById(R.id.tvBodyNotification);
            tvDateNotification = itemView.findViewById(R.id.tvDateNotification);
        }
    }

    private void getJobIds() {
        CommonParams.Builder commonParams = new CommonParams.Builder();
        commonParams.add("access_token", Dependencies.getAccessToken(activity))
                .add("marketplace_user_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId())
                .add("vendor_id", StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId());

        RestClient.getApiInterface(activity).getDebtList(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(activity, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserDebtData userDebtData = baseModel.toResponseModel(UserDebtData.class);
                StorefrontCommonData.getUserData().getData().getVendorDetails().setDebtAmount(userDebtData.getDebtAmount());

                if (userDebtData.getDebtAmount() > 0) {
                    Bundle extras = new Bundle();
                    Transition.transitForResult(activity, UserDebtActivity.class, Codes.Request.OPEN_TASK_DETAIL_ACTIVITY, extras, false);
                } else {
                    new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.your_debt_already_cleared)).build().show();

                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }


}