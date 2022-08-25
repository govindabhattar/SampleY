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
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;

import java.util.ArrayList;

/**
 * Created by cl-macmini-25 on 16/12/16.
 */

public class PDPPhotoAdapter extends RecyclerView.Adapter<PDPPhotoAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<String> imagesList;

    public PDPPhotoAdapter(Activity activity, ArrayList<String> imagesList) {
        this.activity = activity;
        this.imagesList = imagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View taskItem = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.itemview_pdp_photo, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int adapterPos = holder.getAdapterPosition();

        if (imagesList.get(adapterPos) != null) {
            Log.i("URL", "==" + imagesList.get(adapterPos));

            if (imagesList.get(adapterPos).equalsIgnoreCase("")) {
                holder.tvNoPhotos.setVisibility(View.VISIBLE);
                holder.imgSnapshot.setVisibility(View.GONE);

            } else {
                holder.tvNoPhotos.setVisibility(View.GONE);
                holder.imgSnapshot.setVisibility(View.VISIBLE);
                // Verify whether a web url
                if (imagesList.get(adapterPos).startsWith("http://") || imagesList.get(adapterPos).startsWith("https://")) {
                    /*Glide.with(activity).load(imagesList.get(adapterPos))
                    .asBitmap().fitCenter().placeholder(R.drawable.ic_image_placeholder)
                    .into(new BitmapImageViewTarget(holder.imgSnapshot));
                   */ // Load from Storage
                    new GlideUtil.GlideUtilBuilder(holder.imgSnapshot)
                            .setLoadItem(imagesList.get(adapterPos))
                            .setFitCenter(true)
                            .setPlaceholder(R.drawable.ic_image_placeholder)
                            .build();
                } else {
                    /*Glide.with(activity).load(imagesList.get(adapterPos))
                    .asBitmap().fitCenter().placeholder(R.drawable.ic_image_placeholder)
                    .into(new BitmapImageViewTarget(holder.imgSnapshot));
                     */
                    new GlideUtil.GlideUtilBuilder(holder.imgSnapshot)
                            .setLoadItem(imagesList.get(adapterPos))
                            .setFitCenter(true)
                            .setPlaceholder(R.drawable.ic_image_placeholder)
                            .build();
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        if (imagesList.size() > 0) {
            return imagesList.size();
        } else {
            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSnapshot;
        private TextView tvNoPhotos;

        ViewHolder(View itemView) {
            super(itemView);
            imgSnapshot = itemView.findViewById(R.id.imgSnapshot);
            tvNoPhotos = itemView.findViewById(R.id.tvNoPhotos);
            tvNoPhotos.setText(StorefrontCommonData.getString(activity, R.string.no_photo_available));
        }
    }
}
