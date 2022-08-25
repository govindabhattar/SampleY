package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsUtil;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.giftCardTransactionResponse.TxnHistory;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


public class GiftCardHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<TxnHistory> dataList;

    public GiftCardHistoryAdapter(Activity activity, ArrayList<TxnHistory> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.itemview_giftcard_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            String date = DateUtils.getInstance().convertToLocal(dataList.get(position).getCreationDatetime(),
                    Constants.DateFormat.STANDARD_DATE_FORMAT_TZ, Constants.DateFormat.DATE_FORMAT_MMM_dd);

            String time = DateUtils.getInstance().convertToLocal(dataList.get(position).getCreationDatetime(),
                    Constants.DateFormat.STANDARD_DATE_FORMAT_TZ,   UIManager.getTimeFormat());


            if ((dataList.get(position).getSenderVendorId() == StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())
                    && (dataList.get(position).getReceiverVendorId() == StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId())) {
                viewHolder.ivTransactionType.setBackgroundResource(R.drawable.ic_giftcard_self);
                viewHolder.tvTransactionDetailName.setText(StorefrontCommonData.getString(activity, R.string.self_redeemed));

            } else if (dataList.get(position).getSenderVendorId() == StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId()) {
                viewHolder.ivTransactionType.setBackgroundResource(R.drawable.ic_giftcard_sent);
                if (dataList.get(position).getIsRedeemed() == 1) {
                    Spannable string = CustomViewsUtil.createSpanForBoldText(activity, StorefrontCommonData.getString(activity, R.string.redeemed_by) + ": " + dataList.get(position).getReceiverName(), dataList.get(position).getReceiverName());
                    viewHolder.tvTransactionDetailName.setText(string != null ? string : "");
                } else {
                    Spannable string = CustomViewsUtil.createSpanForBoldText(activity, StorefrontCommonData.getString(activity, R.string.sent_to) + ": " + dataList.get(position).getReceiverName(), dataList.get(position).getReceiverName());
                    viewHolder.tvTransactionDetailName.setText(string);
                }
            } else {
                viewHolder.ivTransactionType.setBackgroundResource(R.drawable.ic_giftcard_received);
                Spannable string = CustomViewsUtil.createSpanForBoldText(activity, StorefrontCommonData.getString(activity, R.string.received_from) + ": " + dataList.get(position).getSenderName(), dataList.get(position).getSenderName());
                viewHolder.tvTransactionDetailName.setText(string);
            }

            viewHolder.tvTransactionMessage.setText(dataList.get(position).getMessage());
            viewHolder.tvTransactionBalance.setText(UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(Double.valueOf(dataList.get(position).getAmount()))));

            viewHolder.tvTransactionTime.setText(time);
            viewHolder.tvTransactionDate.setText(date);

            viewHolder.cvParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .title(StorefrontCommonData.getString(activity, R.string.message))
                            .message(dataList.get(position).getMessage())
                            .button(StorefrontCommonData.getString(activity, R.string.close)).build().show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvParentView;
        private ImageView ivTransactionType;
        private TextView tvTransactionDetailName, tvTransactionMessage, tvTransactionTime;
        private TextView tvTransactionBalance, tvTransactionDate;

        ViewHolder(View itemView) {
            super(itemView);
            cvParentView = itemView.findViewById(R.id.cvParentView);
            ivTransactionType = itemView.findViewById(R.id.ivTransactionType);
            tvTransactionDetailName = itemView.findViewById(R.id.tvTransactionDetailName);
            tvTransactionMessage = itemView.findViewById(R.id.tvTransactionMessage);
            tvTransactionTime = itemView.findViewById(R.id.tvTransactionTime);
            tvTransactionBalance = itemView.findViewById(R.id.tvTransactionBalance);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
        }
    }

}