package com.tookancustomer.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tookancustomer.R;
import com.tookancustomer.dialog.ViewImagesDialog;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Developer: Rishabh
 * Dated: 22/07/15.
 */
public class CustomFieldImagesAdapterDetails extends RecyclerView.Adapter<CustomFieldImagesAdapterDetails.ViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> imagesList;

    public CustomFieldImagesAdapterDetails(Activity activity, @NonNull ArrayList<String> imagesList) {
        this.activity = activity;
        this.imagesList = imagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View taskItem = LayoutInflater.from(context).inflate(R.layout.horizontal_scroll_image_item, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final String addImageItem = imagesList.get(position);

        // load the image
        loadImage(addImageItem, viewHolder);
        viewHolder.imgSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.preventMultipleClicks()) {
                    return;
                }
                viewImages(position);
            }
        });
    }

    /**
     * Method to load the image
     *
     * @param viewHolder
     */
    private void loadImage(final String image, final ViewHolder viewHolder) {
        // If if the image path is a Web Url
        /*if (image.startsWith("https://") || image.startsWith("http://")) {
            Glide.with(activity).load(image).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        } else {
            // Load file from the Disk
            Glide.with(activity).load(new File(image)).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        }*/

        if (image.startsWith("http://") || image.startsWith("https://"))
            new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                    .setLoadItem(image)
                    .setFitCenter(true)
                    .setPlaceholder(R.mipmap.image_placeholder)
                    .build();
            // Load from Storage
        else
            new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                    .setLoadItem(new File(image))
                    .setFitCenter(true)
                    .setPlaceholder(R.mipmap.image_placeholder)
                    .build();
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    /**
     * Class to hold the Views
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View snapshotView;
        private ImageView imgSnapshot;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.snapshotView = itemView;
            imgSnapshot = snapshotView.findViewById(R.id.imgSnapshot);
        }
    }

    /**
     * Method to view Images that the User has Uploaded
     *
     * @param position
     */
    private void viewImages(int position) {
        new ViewImagesDialog.Builder(activity)
                .images(imagesList).title("")
                .position(position).build().show();
    }
}
