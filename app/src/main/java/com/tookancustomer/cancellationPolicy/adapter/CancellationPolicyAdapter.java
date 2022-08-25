package com.tookancustomer.cancellationPolicy.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.cancellationPolicy.model.CancellationRules;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class CancellationPolicyAdapter extends RecyclerView.Adapter {
    private ArrayList<CancellationRules> cancellationDataList;
    private int acceptedRulesListSize;
    private static final int VIEW_TYPE_NO_RESULT = 0;
    private static final int VIEW_TYPE_RESULT = 1;

    public CancellationPolicyAdapter(ArrayList<CancellationRules> cancellationDataList
            , int acceptedRulesListSize) {
        this.cancellationDataList = cancellationDataList;
        this.acceptedRulesListSize = acceptedRulesListSize;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_RESULT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_cancellation_policy_adapter,
                            parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view_no_result_found,
                            parent, false);
            return new NoResultViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            onBindViewHolderForResult((MyViewHolder) holder, position);
        } else {
            onBindViewHolderForNoResult((NoResultViewHolder) holder, position);
        }
    }

    private void onBindViewHolderForNoResult(NoResultViewHolder holder, int position) {
        holder.tvNoResultFound.setText(StorefrontCommonData.getString(holder.tvNoResultFound.getContext(),
                R.string.no_result_found));
    }

    private void onBindViewHolderForResult(MyViewHolder holder, int position) {
        int adapterPos = holder.getAdapterPosition();
        String message = "";
        String refundMessage = "";
        holder.tvSerialNo.setText((adapterPos + 1)
                + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.full_stop)
                + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space));
        CancellationRules data = cancellationDataList.get(adapterPos);
        if (data.getStatus() == Constants.TaskStatus.PENDING_STATUS.value) {
            message += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.beforeConfirmationFromThe)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + data.getMerchant()
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.hiphen);

        } else if (data.getStatus() == Constants.TaskStatus.ORDERED.value) {
            if (data.getDays() != 0 || data.getHours() != 0 || data.getMinutes() != 0) {
                message += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.upto)
                        + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space);
                if (data.getDays() != 0) {
                    message += data.getDays()
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.dayss)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space);
                }
                if (data.getHours() != 0) {
                    message += data.getHours()
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.hours)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space);
                }
                if (data.getMinutes() != 0) {
                    message += data.getMinutes()
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.minutes)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space);
                }
                message += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.beforeThe)
                        + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                        + data.getOrder()
                        + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                        + data.getDispatched()
                        + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                        + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.hiphen);
            } else {
                if (acceptedRulesListSize > 1)
                    message += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.afterTheAboveMentionedTime)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space);
                else
                    message += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.text_after)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.text_the)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + data.getOrder()
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.is_confirmed)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                            + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.hiphen);
            }

        } else if (data.getStatus() == Constants.TaskStatus.DISPATCHED.value) {
            message += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.text_after)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + data.getOrder()
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + data.getDispatched()
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.hiphen);
        }

        if (data.getFixedCharge() != 0 && data.getPercentageCharge() != 0) {
            refundMessage = Utils.getDoubleTwoDigits(data.getPercentageCharge())/*data.getPercentageCharge()*/
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.percentage)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.text_of_the)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + data.getOrder()
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.amount)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.text_or)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(data.getFixedCharge()))
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.comma)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.whichever_is_higher)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.full_stop);
        } else if (data.getFixedCharge() != 0) {
            refundMessage = UIManager.getCurrency(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(data.getFixedCharge()))
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.full_stop);
        } else if (data.getPercentageCharge() != 0) {
            refundMessage = Utils.getDoubleTwoDigits(data.getPercentageCharge())/*data.getPercentageCharge()*/
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.percentage)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.text_of_the)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + data.getOrder()
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.empty_space)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.amount)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.full_stop);
        } else {
            refundMessage += StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.no_amount_will_be_deducted)
                    + StorefrontCommonData.getString(holder.tvPolicy.getContext(), R.string.full_stop);
        }

        holder.tvPolicy.setText(message + refundMessage);

    }

    @Override
    public int getItemCount() {
        return cancellationDataList != null ? cancellationDataList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (cancellationDataList != null) {
            return VIEW_TYPE_RESULT;
        } else {
            return VIEW_TYPE_NO_RESULT;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPolicy, tvSerialNo;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPolicy = itemView.findViewById(R.id.tvPolicy);
            tvSerialNo = itemView.findViewById(R.id.tvSerialNo);
        }
    }

    public class NoResultViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNoResultFound;

        public NoResultViewHolder(View itemView) {
            super(itemView);
            tvNoResultFound = itemView.findViewById(R.id.tvNoResultFound);
        }
    }
}
