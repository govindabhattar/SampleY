package com.tookancustomer.agentListing;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.utility.GlideUtil;

import java.util.ArrayList;

/**
 * Created by Utkarsh Shukla on 2019-10-17.
 */
public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.ViewHolder> implements TerminologyStrings {


    private Activity activity;
    private ArrayList<AgentData> agentDataArrayList;

    public AgentListAdapter(Activity activity, ArrayList<AgentData> agentDataArrayList) {
        this.activity = activity;
        this.agentDataArrayList = agentDataArrayList;
    }

    @NonNull
    @Override
    public AgentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_agent_list, viewGroup, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentListAdapter.ViewHolder viewHolder, int position) {
        int adapterPosition = viewHolder.getAdapterPosition();
        AgentData agentData = agentDataArrayList.get(adapterPosition);
        viewHolder.parentLL.setTag(adapterPosition);
        viewHolder.viewMoreTV.setTag(adapterPosition);

        viewHolder.emailTV.setText(agentData.getEmail());
        viewHolder.contact_numberTV.setText(agentData.getPhone());
        viewHolder.agentNameTV.setText(agentData.getName());
        viewHolder.viewMoreTV.setText(StorefrontCommonData.getString(activity, R.string.view_more));

        if (agentData.getTemplate().size() > 0)
            viewHolder.viewMoreTV.setVisibility(View.VISIBLE);
        else
            viewHolder.viewMoreTV.setVisibility(View.GONE);

        if ((int) agentData.getRatings() > 0) {
            viewHolder.ratingTV.setVisibility(View.VISIBLE);
            viewHolder.ratingTV.setText(agentData.getRatings() + "");
        } else {
            viewHolder.ratingTV.setVisibility(View.GONE);
        }

        viewHolder.emailLabelTV.setText(StorefrontCommonData.getString(activity, R.string.email));
        viewHolder.contactlabelTV.setText(StorefrontCommonData.getString(activity, R.string.phone));

        if ((agentData.getFleetImage() != null) && (!agentData.getFleetImage().equals(""))) {

            new GlideUtil.GlideUtilBuilder(viewHolder.agentImageIV)
                    .setPlaceholder(R.drawable.ic_profile_placeholder)
                    .setCenterCrop(true)
                    .setLoadItem(agentData.getFleetImage())
                    .setTransformation(new CircleCrop())
                    .build();

        } else {
            viewHolder.agentImageIV.setImageResource(R.drawable.ic_profile_placeholder);
        }


        viewHolder.parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                if (activity instanceof AgentListingActivity)
                    ((AgentListingActivity) activity).onAgentSelected(agentDataArrayList.get(pos).getFleetId());
                else if (activity instanceof AgentListingCheckoutActivity)
                    ((AgentListingCheckoutActivity) activity).onAgentSelected(agentDataArrayList.get(pos).getFleetId(), agentDataArrayList.get(pos));
            }
        });
        viewHolder.viewMoreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                if (agentDataArrayList.get(pos).getTemplate().size() > 0)
                    ((AgentListingActivity) activity).viewAgentDetails(agentDataArrayList.get(pos));

            }
        });


    }

    @Override
    public int getItemCount() {
        return agentDataArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parentLL;
        TextView emailTV, contact_numberTV, ratingTV, agentNameTV;
        ImageView agentImageIV;
        TextView contactlabelTV, emailLabelTV, viewMoreTV;

        ViewHolder(View itemView) {
            super(itemView);
            parentLL = itemView.findViewById(R.id.parentLL);
            viewMoreTV = itemView.findViewById(R.id.viewMoreTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            contact_numberTV = itemView.findViewById(R.id.contact_numberTV);
            agentNameTV = itemView.findViewById(R.id.agentNameTV);
            ratingTV = itemView.findViewById(R.id.ratingTV);
            agentImageIV = itemView.findViewById(R.id.agentImageIV);
            contactlabelTV = itemView.findViewById(R.id.contactlabelTV);
            emailLabelTV = itemView.findViewById(R.id.emailLabelTV);

        }
    }

}
