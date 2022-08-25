package com.tookancustomer.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.transition.Transition;
import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.SignupCustomFieldsActivity;
import com.tookancustomer.dialog.ViewImagesDialogSignup;
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
public class CustomFieldImagesAdapterSignup extends RecyclerView.Adapter<CustomFieldImagesAdapterSignup.ViewHolder> {

    private Context context;
    private final int SQUARE_EDGE;

    private Activity activity;
    private ArrayList<String> imagesList;
    private final SignupTemplateData item;

    public CustomFieldImagesAdapterSignup(Activity activity, @NonNull ArrayList<String> imagesList, SignupTemplateData item) {
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

        viewHolder.ivDeleteItem.setOnClickListener(v -> {
            if (activity instanceof SignupCustomFieldsActivity) {
                ((SignupCustomFieldsActivity) activity).deleteCustomFieldImageSignup(position);
            } else if (activity instanceof RegistrationOnboardingActivity) {
                ((RegistrationOnboardingActivity) activity).deleteCustomFieldImageSignup(position);
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
                new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setTransformation(new CenterCrop())
                        .setLoadItem(image)
                        .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                            @Override
                            public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {
                                viewHolder.pbProgressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) {

                            }

                            @Override
                            public void onLoadFailed() {
                                viewHolder.pbProgressBar.setVisibility(View.GONE);
                            }
                        }).build();


            } catch (Exception e) {
                viewHolder.imgSnapshot.setVisibility(View.VISIBLE);
            }

        } else {
            // Load file from the Disk
            try {

                new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setTransformation(new CenterCrop())
                        .setLoadItem(new File(image))
                        .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                            @Override
                            public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {
                                viewHolder.pbProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) { }

                            @Override
                            public void onLoadFailed() {
                                viewHolder.pbProgressBar.setVisibility(View.GONE);
                            }
                        }).build();


            } catch (Exception e) {
                viewHolder.imgSnapshot.setVisibility(View.VISIBLE);
            }

        }
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
        private ImageView imgSnapshot, ivDeleteItem;
        private ProgressBar pbProgressBar;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.snapshotView = itemView;
            imgSnapshot = snapshotView.findViewById(R.id.imgSnapshot);
            pbProgressBar = snapshotView.findViewById(R.id.pbProgressBar);
            ivDeleteItem = snapshotView.findViewById(R.id.ivDeleteItem);
        }
    }

    /**
     * Method to view Images that the User has Uploaded
     *
     * @param position
     */
    private void viewImages(int position) {
        if (activity instanceof SignupCustomFieldsActivity) {
            ((SignupCustomFieldsActivity) activity).setCustomFieldPosition(item);
        } else if (activity instanceof RegistrationOnboardingActivity) {
            ((RegistrationOnboardingActivity) activity).setCustomFieldPosition(item);
        }
        new ViewImagesDialogSignup.Builder(activity).images(imagesList).title("").position(position).build().show();
    }
}
