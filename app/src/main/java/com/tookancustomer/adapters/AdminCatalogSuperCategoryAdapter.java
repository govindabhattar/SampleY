package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tookancustomer.AdminCategoryActivity;
import com.tookancustomer.NLevelWorkFlowActivity;
import com.tookancustomer.R;
import com.tookancustomer.RentalHomeActivity;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Codes.Request.OPEN_ADMIN_CATEGORY_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_MERCHANTS_LISTING;
import static com.tookancustomer.appdata.Keys.Extras.ADMIN_CATALOGUE;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_NAME;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_CATEGORY_ID;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by cl-macmini-25 on 19/12/16.
 */

public class AdminCatalogSuperCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<Datum> dataList;

    public AdminCatalogSuperCategoryAdapter(Activity activity, ArrayList<Datum> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_view_admin_supercategory, parent, false);
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
                        if (dataList.get(position).getThumbUrl400().isEmpty()) {
                            viewHolder.ivCategoryImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                        } else {
                            /*Glide.with(activity).load(dataList.get(position).getThumbUrl400())
                                    .asBitmap()
                                    .fitCenter()
                                    .diskCacheStrategy(SOURCE)
                                    .placeholder(AppCompatResources.getDrawable(activity, R.drawable.ic_loading_image))
                                    .into(new BitmapImageViewTarget(viewHolder.ivCategoryImage) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            if (!activity.isDestroyed()) {
                                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                                circularBitmapDrawable.setCornerRadius(4);
                                                viewHolder.ivCategoryImage.setImageDrawable(circularBitmapDrawable);
                                            }
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            super.onLoadFailed(e, errorDrawable);
                                            viewHolder.ivCategoryImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                                        }
                                    });*/

                            new GlideUtil.GlideUtilBuilder(viewHolder.ivCategoryImage)
                                    .setLoadItem(dataList.get(position).getThumbUrl400())
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

            viewHolder.tvCategoryName.setText(dataList.get(position).getName());

            viewHolder.rlCategoryParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Utils.internetCheck(activity)) {
                        new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                        return;
                    }

                    boolean hasMoreThanOneCategory = false;

                    if (dataList.get(position).getSubCategories().size() > 0) {
                        hasMoreThanOneCategory = true;
                    }
                    //TODO check with PM for functionality
                   /* for (int i = 0; i < dataList.get(position).getSubCategories().size(); i++) {
                        if (!dataList.get(position).getSubCategories().get(i).getSubCategories().isEmpty()) {
                            hasMoreThanOneCategory = true;
                            break;
                        }
                    }*/

                    Bundle extras = new Bundle();
                    extras.putSerializable(ADMIN_CATALOGUE, dataList.get(position).getSubCategories());
                    extras.putString(HEADER_NAME, dataList.get(position).getName());
                    extras.putInt(PARENT_ID, dataList.get(position).getCatalogueId());
                    /**
                     * For ECOM flow
                     * Seller view with seller name and price
                     * and other sellers view
                     */
                    extras.putString(PARENT_CATEGORY_ID, dataList.get(position).getCatalogueId().toString());
                    if (activity instanceof AdminCategoryActivity) {
                        extras.putDouble(PICKUP_LATITUDE, ((AdminCategoryActivity) activity).latitude);
                        extras.putDouble(PICKUP_LONGITUDE, ((AdminCategoryActivity) activity).longitude);
                        extras.putString(PICKUP_ADDRESS, ((AdminCategoryActivity) activity).address);
                    }
                    Intent intent;

                    if (hasMoreThanOneCategory) {
                        intent = new Intent(activity, AdminCategoryActivity.class);
                        intent.putExtras(extras);
                        activity.startActivityForResult(intent, OPEN_ADMIN_CATEGORY_ACTIVITY);
                    } else {
                        if (StorefrontCommonData.getFormSettings().getProductView() == 1) {
                            if (Dependencies.isEcomApp())
                                intent = new Intent(activity, NLevelWorkFlowActivity.class);
                            else
                                intent = new Intent(activity, RentalHomeActivity.class);
                        } else
                            intent = new Intent(activity, RestaurantListingActivity.class);

                        intent.putExtras(extras);
                        activity.startActivityForResult(intent, OPEN_MERCHANTS_LISTING);
                    }

                    AnimationUtils.forwardTransition(activity);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlCategoryParent;
        private ImageView ivCategoryImage;
        private TextView tvCategoryName;

        ViewHolder(View itemView) {
            super(itemView);
            rlCategoryParent = itemView.findViewById(R.id.rlCategoryParent);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}