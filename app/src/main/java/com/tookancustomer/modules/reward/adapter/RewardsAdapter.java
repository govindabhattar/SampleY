package com.tookancustomer.modules.reward.adapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.modules.reward.model.rewardPlans.Datum;

import java.util.ArrayList;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> {

    private OnItemSelectedListener listener;
    private ArrayList<Datum> plansList = new ArrayList<>();

    public RewardsAdapter(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public void setPlansList(ArrayList<Datum> plansList) {
        this.plansList = plansList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RewardsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_reward_plans, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.MyViewHolder holder, int position) {
        final Datum plan = plansList.get(holder.getAdapterPosition());
        holder.tvPlanName.setText(plan.getRewardName());
        holder.tvPlanDesc.setText(plan.getDescription());

        int actionBtnTextColor;
        if (plan.getIsPurchased()) {
            holder.itemView.setSelected(true);
//            holder.tvActivate.setSelected(true);
            holder.ivTick.setVisibility(View.VISIBLE);
            holder.ivRewardImage.setImageResource(R.drawable.ic_reward_highlighted);

            if (plan.getIsExpired()) {
                holder.tvActivate.setText(StorefrontCommonData.getString(holder.tvActivate.getContext(), R.string.text_expired));
                actionBtnTextColor = R.color.color_red;
            } else {
                holder.tvActivate.setText(StorefrontCommonData.getString(holder.tvActivate.getContext(), R.string.text_activated));
                actionBtnTextColor = R.color.colorAccent;
            }


        } else {
            actionBtnTextColor = R.color.text_color;

            holder.itemView.setSelected(false);
//            holder.tvActivate.setSelected(false);
            holder.ivTick.setVisibility(View.GONE);
            holder.ivRewardImage.setImageResource(R.drawable.ic_reward_unhighlighted);
            holder.tvActivate.setText(StorefrontCommonData.getString(holder.tvActivate.getContext(), R.string.view_details));
        }

        holder.tvActivate.setTextColor(ContextCompat.getColor(holder.tvActivate.getContext(), actionBtnTextColor));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemSelected(plan);
            }
        });

        holder.tvActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemSelected(plan);
            }
        });


    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPlanName, tvPlanDesc;
        private ImageView ivRewardImage, ivTick;
        private TextView tvActivate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvPlanDesc = itemView.findViewById(R.id.tvPlanDesc);
            ivRewardImage = itemView.findViewById(R.id.ivRewardImage);
            ivTick = itemView.findViewById(R.id.ivTick);
            tvActivate = itemView.findViewById(R.id.tvActivate);
        }
    }


    public interface OnItemSelectedListener {
        void onItemSelected(Datum datum);

    }

}
