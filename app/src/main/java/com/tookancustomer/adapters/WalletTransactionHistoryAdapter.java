package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.models.WalletTransactionResponse.TxnHistory;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class WalletTransactionHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TerminologyStrings {
    private Activity activity;
    private ArrayList<TxnHistory> dataList;

    public WalletTransactionHistoryAdapter(Activity activity, ArrayList<TxnHistory> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.itemview_wallet_transaction, parent, false);
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


            viewHolder.ivTransactionType.setBackgroundResource(Constants.WalletTransactionStatus.getWalletTransactionData(dataList.get(position).getType()).drawableValue);


            if (dataList.get(position).getType() == Constants.WalletTransactionStatus.DEDUCTION.transactionType) {
                viewHolder.tvTransactionDetailDesc.setText(StorefrontCommonData.getString(activity, Constants.WalletTransactionStatus.getWalletTransactionData(dataList.get(position).getType()).stringValue)
                        .replace(ORDER,  Utils.getCallTaskAs(true, true) + " #" + dataList.get(position).getJobId()));
            } else if (dataList.get(position).getType() == Constants.WalletTransactionStatus.DEDUCTION_TO_BUY_REWARD.transactionType) {
                if (dataList.get(position).getJobId() == 0)
                    viewHolder.tvTransactionDetailDesc.setText(StorefrontCommonData.getString(activity, Constants.WalletTransactionStatus.getWalletTransactionData(dataList.get(position).getType()).stringValue) +
                            " " + StorefrontCommonData.getTerminology().getRewards());
            } else if (dataList.get(position).getType() == Constants.WalletTransactionStatus.CASHBACK_FOR_ORDER.transactionType) {
                viewHolder.tvTransactionDetailDesc.setText(StorefrontCommonData.getString(activity, Constants.WalletTransactionStatus.getWalletTransactionData(dataList.get(position).getType()).stringValue) +
                        " " + Utils.getCallTaskAs(true, true) + " #" + dataList.get(position).getJobId());
            } else {
                viewHolder.tvTransactionDetailDesc.setText(StorefrontCommonData.getString(activity, Constants.WalletTransactionStatus.getWalletTransactionData(dataList.get(position).getType()).stringValue).replace(GIFT_CARD, StorefrontCommonData.getTerminology().getGiftCard()));
            }

            viewHolder.tvTransactionClosingBalance.setText(time
                    + "  " + StorefrontCommonData.getString(activity, R.string.closing_balance) + ": "
                    + (UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(dataList.get(position).getWalletBalanceAfterTxn()))));

            viewHolder.tvTransactionBalance.setText(Constants.WalletTransactionStatus.getWalletTransactionData(dataList.get(position).getType()).transactionMoneySymbol
                    + (UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(dataList.get(position).getTransactingAmount()))));
            viewHolder.tvTransactionDate.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivTransactionType;
        private TextView tvTransactionDetailDesc, tvTransactionClosingBalance;
        private TextView tvTransactionBalance, tvTransactionDate;

        ViewHolder(View itemView) {
            super(itemView);
            ivTransactionType = itemView.findViewById(R.id.ivTransactionType);
            tvTransactionDetailDesc = itemView.findViewById(R.id.tvTransactionDetailDesc);
            tvTransactionClosingBalance = itemView.findViewById(R.id.tvTransactionClosingBalance);
            tvTransactionBalance = itemView.findViewById(R.id.tvTransactionBalance);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
        }
    }
}