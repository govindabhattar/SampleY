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

import java.util.ArrayList;

import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;

public class BusinessCategoriesMapAdapter extends RecyclerView.Adapter<BusinessCategoriesMapAdapter.MyViewHolder>  {

    private ArrayList<Result> dataList = new ArrayList<>();
    private BusinessCategoryMapListener listener;
    private Activity activity;


    public BusinessCategoriesMapAdapter(Activity mActivity, ArrayList<Result> dataList, Integer businessCategoryId, BusinessCategoryMapListener listener) {
        /**
         * +1 for item "All"
         */
//        if (businessCategoryId == null || businessCategoryId == 0) {
//            this.dataList.add(new Result(true));
//        } else {
//            this.dataList.add(new Result(false));
        for (Result data : dataList) {
            if (data.getId() == businessCategoryId) {
                data.setSelected(true);
                break;
//                }
            }
        }
        this.dataList.addAll(dataList);
        this.listener = listener;
        this.activity=mActivity;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_business_categories_map_adapter,
                viewGroup, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final int pos = holder.getAdapterPosition();
        final Result businessCategory = dataList.get(pos);
        holder.tvCategoryName.setText(businessCategory.getName());
//        }

        if (businessCategory.isSelected()) {
            holder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.colorReferText));
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
                        listener.onBusinessCategorySelectedMap(pos, businessCategory, isAll);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    public interface BusinessCategoryMapListener {
        void onBusinessCategorySelectedMap(int pos, Result businessCategory,boolean isAllBusinessCategory);
    }

}
