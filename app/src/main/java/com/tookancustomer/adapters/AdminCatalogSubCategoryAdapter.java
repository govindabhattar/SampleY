package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


/**
 * Created by cl-macmini-25 on 19/12/16.
 */

public class AdminCatalogSubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<Datum> dataList;
    private Callback callback;

    public AdminCatalogSubCategoryAdapter(Activity activity, ArrayList<Datum> dataList, Callback callback) {
        this.activity = activity;
        this.dataList = dataList;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_admin_subcategory, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();
        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            try {
                viewHolder.ivCategoryImage.post(new Runnable() {
                    @Override
                    public void run() {
                        if (dataList.get(position).getThumbUrl().isEmpty()) {
                            viewHolder.ivCategoryImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                        } else {

                            new GlideUtil.GlideUtilBuilder(viewHolder.ivCategoryImage)
                                    .setLoadItem(dataList.get(position).getThumbUrl())
                                    .setFitCenter(true)
                                    .setPlaceholder(R.drawable.ic_loading_image)
                                    .setError(R.drawable.ic_image_placeholder)
                                    .setFallback(R.drawable.ic_image_placeholder)
                                    .setTransformation(new RoundedCorners(4))
                                    .build();
                        }
                    }
                });

            } catch (Exception e) {

                               Utils.printStackTrace(e);
            }
            if (dataList.get(position).isSelected()) {
                viewHolder.ivCategoryImageShadow.setVisibility(View.VISIBLE);
                viewHolder.tvCategoryName.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
            } else {
                viewHolder.ivCategoryImageShadow.setVisibility(View.GONE);
                viewHolder.tvCategoryName.setTextColor(ContextCompat.getColor(activity, R.color.secondary_text_color));
            }

            viewHolder.tvCategoryName.setText(dataList.get(position).getName());

            viewHolder.llAdminSubCategoryParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.internetCheck(activity)) {
                        new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                        return;
                    }
                    dataList.get(position).setSelected(!dataList.get(position).isSelected());
                    notifyItemChanged(position);
                    callback.onCategorySelected(dataList);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llAdminSubCategoryParent;
        private ImageView ivCategoryImage, ivCategoryImageShadow;
        private TextView tvCategoryName;

        ViewHolder(View itemView) {
            super(itemView);
            llAdminSubCategoryParent = itemView.findViewById(R.id.llAdminSubCategoryParent);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            ivCategoryImageShadow = itemView.findViewById(R.id.ivCategoryImageShadow);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    public interface Callback {
        void onCategorySelected(ArrayList<Datum> dataList);
    }
}