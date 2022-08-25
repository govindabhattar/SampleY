package com.tookancustomer.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tookancustomer.CheckOutActivityOld;
import com.tookancustomer.R;
import com.tookancustomer.dialog.ViewImagesDialogPickup;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Developer: Rishabh
 * Dated: 22/07/15.
 */
public class CustomFieldImagesAdapterPickup extends RecyclerView.Adapter<CustomFieldImagesAdapterPickup.ViewHolder> {

    private final int SQUARE_EDGE;
    private final Item item;
    private Context context;
    private CheckOutActivityOld activity;
    private ArrayList<String> imagesList;

    public CustomFieldImagesAdapterPickup(CheckOutActivityOld activity, @NonNull ArrayList<String> imagesList, Item item) {
        this.activity = activity;
        this.imagesList = imagesList;
        this.item = item;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        SQUARE_EDGE = dm.widthPixels / 2;
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
                UIManager.isPickup = true;
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
        if (image.startsWith("https://") || image.startsWith("http://")) {

            try {

                /*Glide.with(activity).load(image).asBitmap()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(AppCompatResources.getDrawable(activity,R.drawable.ic_image_placeholder))
                        .into(new BitmapImageViewTarget(viewHolder.imgSnapshot) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewHolder.imgSnapshot.setImageDrawable(circularBitmapDrawable);
                            }
                        });*/

                new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                        .setLoadItem(image)
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();

            } catch (Exception e) {
                viewHolder.imgSnapshot.setVisibility(View.VISIBLE);
            }


            //Glide.with(activity).load(image).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        } else {
            // Load file from the Disk
            try {

                /*Glide.with(activity).load(new File(image)).asBitmap()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(AppCompatResources.getDrawable(activity,R.drawable.ic_image_placeholder))
                        .into(new BitmapImageViewTarget(viewHolder.imgSnapshot) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewHolder.imgSnapshot.setImageDrawable(circularBitmapDrawable);
                            }
                        });*/

                new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                        .setLoadItem(new File(image))
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();

            } catch (Exception e) {
                viewHolder.imgSnapshot.setVisibility(View.VISIBLE);
            }

            //  Glide.with(activity).load(new File(image)).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    /**
     * Method to view Images that the User has Uploaded
     *
     * @param position
     */
    private void viewImages(int position) {
        activity.setCustomFieldPositionPickup(item);
        new ViewImagesDialogPickup.Builder(activity)
                .images(imagesList).title("")
                .position(position).build().show();
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
            imgSnapshot = (ImageView) snapshotView.findViewById(R.id.imgSnapshot);
        }
    }
}
