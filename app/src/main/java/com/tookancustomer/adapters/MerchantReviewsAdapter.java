package com.tookancustomer.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.MarketplaceStorefrontModel.LastReviewRating;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

/**
 * Created by cl-macmini-25 on 22/02/18.
 */

public class MerchantReviewsAdapter extends RecyclerView.Adapter<MerchantReviewsAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<LastReviewRating> dataList;

    public MerchantReviewsAdapter(Activity activity, ArrayList<LastReviewRating> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemview_merchant_rate_reviews, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int pos) {
        final int adapterPos = viewHolder.getAdapterPosition();

        viewHolder.tvCustomerName.setText(dataList.get(adapterPos).getCustomerName());
        if (dataList.get(adapterPos).getReview().isEmpty()) {
            viewHolder.tvReviewMessage.setVisibility(View.GONE);
        } else {
            viewHolder.tvReviewMessage.setVisibility(View.VISIBLE);
        }
        viewHolder.tvReviewMessage.setText(dataList.get(adapterPos).getReview());
        Utils.addStarsToLayout(activity, viewHolder.llShowCustomerRatings, dataList.get(adapterPos).getRating().doubleValue());

        viewHolder.tvRatingText.setText(dataList.get(adapterPos).getRating().floatValue() + "");
        viewHolder.tvRatingText.setBackground(Utils.getRatingBackgroundDrawable(activity, Constants.RatingColorValues.getColorResourceValue(activity, dataList.get(adapterPos).getRating().intValue())));

//        if (!dataList.get(adapterPos).getCreationDatetime().isEmpty())
//            viewHolder.tvReviewTime.setText(DateUtils.getInstance().getRelativeTimeWithCurrentTime(DateUtils.getInstance().getDate(dataList.get(adapterPos).getCreationDatetime())));
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvReviewTime, tvRatingText, tvReviewMessage;
        LinearLayout llShowCustomerRatings;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvReviewTime = itemView.findViewById(R.id.tvReviewTime);
            llShowCustomerRatings = itemView.findViewById(R.id.llShowCustomerRatings);
            tvRatingText = itemView.findViewById(R.id.tvRatingText);
            tvReviewMessage = itemView.findViewById(R.id.tvReviewMessage);
        }
    }
}