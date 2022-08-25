package com.tookancustomer.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.tookancustomer.ProfileActivity;
import com.tookancustomer.R;
import com.tookancustomer.dialog.ViewImagesDialogProfile;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Developer: Rishabh
 * Dated: 22/07/15.
 */
public class CustomFieldImagesAdapterProfile extends RecyclerView.Adapter<CustomFieldImagesAdapterProfile.ViewHolder> {

    private final int SQUARE_EDGE;
    private final SignupTemplateData item;
    private final boolean isEnable;
    private Context context;
    private ProfileActivity activity;
    private ArrayList<String> imagesList;

    public CustomFieldImagesAdapterProfile(ProfileActivity activity, @NonNull ArrayList<String> imagesList, SignupTemplateData item, boolean isEnable) {
        this.activity = activity;
        this.imagesList = imagesList;
        this.item = item;
        this.isEnable = isEnable;

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
        viewHolder.ivDeleteItem.setVisibility(isEnable ? View.VISIBLE : View.GONE);
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

        viewHolder.ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesList.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());

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
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setLoadItem(image)
                        .setTransformation(new CenterCrop())
                        .build();


            } catch (Exception e) {
                viewHolder.imgSnapshot.setVisibility(View.VISIBLE);
            }


            //Glide.with(activity).load(image).asBitmap().fitCenter().placeholder(R.mipmap.image_placeholder).into(new BitmapImageViewTarget(viewHolder.imgSnapshot));
        } else {
            // Load file from the Disk
            try {

               /* Glide.with(activity).load(new File(image)).asBitmap()
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
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setLoadItem(new File(image))
                        .setTransformation(new CenterCrop())
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
        activity.setCustomFieldPosition(item);
        new ViewImagesDialogProfile.Builder(activity).images(imagesList).title("").position(position).build().show();
    }

    /**
     * Class to hold the Views
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View snapshotView;
        private ImageView imgSnapshot;
        private ProgressBar pbProgressBar;
        public ImageView ivDeleteItem;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.snapshotView = itemView;
            imgSnapshot = (ImageView) snapshotView.findViewById(R.id.imgSnapshot);
            pbProgressBar = snapshotView.findViewById(R.id.pbProgressBar);
            ivDeleteItem = snapshotView.findViewById(R.id.ivDeleteItem);
            pbProgressBar.setVisibility(View.GONE);
        }
    }
}
