package com.tookancustomer.modules.customerSubscription;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


public class CustomerPlansAdapter extends RecyclerView.Adapter<CustomerPlansAdapter.ViewHolder> {
    ArrayList<CustomerPlan> customerPlan;
    private Activity activity;
    private ArrayList<PlanList> dataList;

    public CustomerPlansAdapter(Activity activity, ArrayList<PlanList> dataList, ArrayList<CustomerPlan> customerPlan) {
        this.activity = activity;
        this.dataList = dataList;
        this.customerPlan = customerPlan;
    }

    @Override
    public CustomerPlansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.customer_plans_list, parent, false);
        return new CustomerPlansAdapter.ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(CustomerPlansAdapter.ViewHolder holder, int position) {
        PlanList list = dataList.get(holder.getAdapterPosition());

        holder.planNameTV.setText(list.getPlanName());
        holder.choosePlanBT.setText(StorefrontCommonData.getString(activity, R.string.choose_plan));


        holder.planAmountTV.setText(Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(list.getAmount()));

        holder.planDescriptionTV.setText(list.getDescription());
        if (list.getNumberOfOrders() != null && list.getNumberOfOrders() > 0)
            holder.validityTV.setText(list.getPlanDuration() + " (" + StorefrontCommonData.getString(activity, R.string.in_days) + ") | "
                    + list.getNumberOfOrders() + " (" + StorefrontCommonData.getString(activity, R.string.allowed_orders).replace(TerminologyStrings.ORDERS, StorefrontCommonData.getTerminology().getOrders().toLowerCase()) + ")");
        else
            holder.validityTV.setText(list.getPlanDuration() + " (" + StorefrontCommonData.getString(activity, R.string.in_days) + ")");


        if (list.getImageUrl().isEmpty()) {
            holder.planIV.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
        } else {

            new GlideUtil.GlideUtilBuilder(holder.planIV)
                    .setLoadItem(list.getImageUrl())
                    .setPlaceholder(R.drawable.ic_loading_image)
                    .setError(R.drawable.ic_image_placeholder)
                    .setFallback(R.drawable.ic_image_placeholder)
                    .build();
        }

        holder.choosePlanBT.setTag(position);
        holder.choosePlanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (activity instanceof CustomerSubscriptionActivity)
                    ((CustomerSubscriptionActivity) activity).selectPlan(pos);
            }


        });


    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView planNameTV, planAmountTV, planDescriptionTV, validityTV, tvBrowsePlan;
        Button choosePlanBT;
        ImageView planIV;

        ViewHolder(View itemView) {
            super(itemView);

            planIV = itemView.findViewById(R.id.planIV);
            planNameTV = itemView.findViewById(R.id.planNameTV);
            planAmountTV = itemView.findViewById(R.id.planAmountTV);
            planDescriptionTV = itemView.findViewById(R.id.planDescriptionTV);
            validityTV = itemView.findViewById(R.id.validityTV);
            choosePlanBT = itemView.findViewById(R.id.choosePlanBT);

        }

    }
}
