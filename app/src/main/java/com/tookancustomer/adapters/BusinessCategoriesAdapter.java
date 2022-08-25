package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.models.businessCategoriesData.Result;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.getColor;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;

public class BusinessCategoriesAdapter extends RecyclerView.Adapter<BusinessCategoriesAdapter.MyViewHolder> {

    private ArrayList<Result> dataList = new ArrayList<>();
    private BusinessCategoryListener listener;
    private Activity activity;

    public BusinessCategoriesAdapter(Activity mActivity, ArrayList<Result> dataList, Integer businessCategoryId, BusinessCategoryListener listener) {
        /**
         * +1 for item "All"
         */

        for (Result data : dataList) {
            if (data.getId() == businessCategoryId) {
                data.setSelected(true);
                break;
//                }
            }
        }
        this.dataList.addAll(dataList);
        this.listener = listener;
        this.activity = mActivity;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_business_categories_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        final Result businessCategory = dataList.get(pos);


        if (businessCategory.getThumb_list().get400x400() != null && !businessCategory.getThumb_list().get400x400().isEmpty()) {
            new GlideUtil.GlideUtilBuilder(holder.ivCategoryImage)
                    .setLoadItem(businessCategory.getThumb_list().get400x400())
                    .setError(R.drawable.ic_image_placeholder)
                    .setPlaceholder(R.drawable.ic_image_placeholder)
                    .build();
        } else {
            new GlideUtil.GlideUtilBuilder(holder.ivCategoryImage)
                    .setLoadItem(businessCategory.getIcon())
                    .setError(R.drawable.ic_image_placeholder)
                    .setPlaceholder(R.drawable.ic_image_placeholder)
                    .build();
        }

        holder.tvCategoryName.setText(businessCategory.getName());
//        }

        if (businessCategory.isSelected()) {
            holder.ivCategoryImage.setBorderColor(getColor(activity, R.color.colorPrimary));
            holder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.ivCategoryImage.setBorderColor(getColor(activity, R.color.colorHint));
            holder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.black));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {

                    if (businessCategory.getExternalLink() != null && !businessCategory.getExternalLink().isEmpty()) {
                        Intent intentExternalLink = new Intent(activity, WebViewActivity.class);
                        intentExternalLink.putExtra(URL_WEBVIEW, businessCategory.getExternalLink());
                        intentExternalLink.putExtra(HEADER_WEBVIEW, businessCategory.getName());
                        activity.startActivity(intentExternalLink);
                        AnimationUtils.forwardTransition((RestaurantListingActivity) activity);

                    } else {

                        boolean isAll = true;
                        if (businessCategory.getIs_all_category() == 0)
                            isAll = false;
                        listener.onBusinessCategorySelected(pos, businessCategory, isAll);
                        if (businessCategory.getIsCustomOrderActive() == 0) {
                            unselectAll();
                            dataList.get(pos).setSelected(true);
                            notifyDataSetChanged();
                        }
                    }

                }

            }
        });

    }

    private void unselectAll() {
        for (int i = 0; i < dataList.size(); i++)
            dataList.get(i).setSelected(false);
    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    public interface BusinessCategoryListener {
        void onBusinessCategorySelected(int pos, Result businessCategory, boolean isAllBusinessCategory);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivCategoryImage;
        private TextView tvCategoryName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }


}
