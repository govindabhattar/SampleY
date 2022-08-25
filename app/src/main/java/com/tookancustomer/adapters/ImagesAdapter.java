package com.tookancustomer.adapters;

/**
 * Created by neerajwadhwani on 26/07/18.
 */

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.tookancustomer.R;
import com.tookancustomer.utility.GlideUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by neerajwadhwani on 12/07/18.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {


    private final ArrayList<String> imagesList;
    private Context context;

    public ImagesAdapter(Activity context, ArrayList<String> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_package_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String addImageItem = imagesList.get(position);

        // load the image
        loadImage(addImageItem, holder);

        holder.ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

            }
        });

    }

    private void loadImage(String image, final MyViewHolder viewHolder) {

        // If if the image path is a Web Url
        if (image.startsWith("https://") || image.startsWith("http://")) {

            try {

                /*Glide.with(context).load(image).asBitmap()
                        .centerCrop()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(AppCompatResources.getDrawable(context,R.drawable.ic_image_placeholder))
                        .into(new BitmapImageViewTarget(viewHolder.ivImages) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                viewHolder.ivImages.setImageDrawable(circularBitmapDrawable);
                            }
                        });*/

                new GlideUtil.GlideUtilBuilder(viewHolder.ivImages)
                        .setLoadItem(image)
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setTransformation(new CircleCrop())
                        .build();


            } catch (Exception e) {
                viewHolder.ivImages.setVisibility(View.VISIBLE);
            }


            //Glide.with(activity).load(image).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        } else {
            // Load file from the Disk
            try {

                /*Glide.with(context).load(new File(image)).asBitmap()
                        .centerCrop()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(AppCompatResources.getDrawable(context,R.drawable.ic_image_placeholder))
                        .into(new BitmapImageViewTarget(viewHolder.ivImages) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewHolder.ivImages.setImageDrawable(circularBitmapDrawable);
                            }
                        });*/

                new GlideUtil.GlideUtilBuilder(viewHolder.ivImages)
                        .setLoadItem(new File(image))
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setTransformation(new CircleCrop())
                        .build();

            } catch (Exception e) {
                viewHolder.ivImages.setVisibility(View.VISIBLE);
            }

            //  Glide.with(activity).load(new File(image)).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImages;
        private final ImageView ivDeleteItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImages = itemView.findViewById(R.id.ivImages);
            ivDeleteItem = itemView.findViewById(R.id.ivDeleteItem);
        }
    }
}

