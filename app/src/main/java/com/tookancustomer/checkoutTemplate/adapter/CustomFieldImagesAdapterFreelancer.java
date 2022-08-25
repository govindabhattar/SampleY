package com.tookancustomer.checkoutTemplate.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.transition.Transition;
import com.tookancustomer.CheckOutCustomActivity;
import com.tookancustomer.R;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.dialog.ViewImagesDialogFreelancer;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.util.ArrayList;


/**
 * Developer: Rishabh
 * Dated: 22/07/15.
 */
public class CustomFieldImagesAdapterFreelancer extends RecyclerView.Adapter<CustomFieldImagesAdapterFreelancer.ViewHolder> {

    private Context context;
    private final int SQUARE_EDGE;

    private Activity activity;
    private ArrayList<String> imagesList;
    //    private final SignupTemplateData item;
    private final Template item;
    private boolean isForDisplay;

    public CustomFieldImagesAdapterFreelancer(Activity activity, @NonNull ArrayList<String> imagesList,
                                              Template item, boolean isForDisplay) {
        this.activity = activity;
        this.imagesList = imagesList;
        this.item = item;
        this.isForDisplay = isForDisplay;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        SQUARE_EDGE = dm.widthPixels / 2;
    }
//public CustomFieldImagesAdapterFreelancer(SignupCustomFieldsActivity activity, @NonNull ArrayList<String> imagesList, SignupTemplateData item) {
//        this.activity = activity;
//        this.imagesList = imagesList;
//        this.item = item;
//
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        SQUARE_EDGE = dm.widthPixels / 2;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View taskItem = LayoutInflater.from(context).inflate(R.layout.horizontal_scroll_image_item, parent, false);
        return new ViewHolder(taskItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final String addImageItem = imagesList.get(position);

        if (isForDisplay)
            viewHolder.ivDeleteItem.setVisibility(View.GONE);
        else
            viewHolder.ivDeleteItem.setVisibility(View.VISIBLE);

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
        if (image.startsWith("https://") || image.startsWith("http://")) {

            try {
                new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                        .setLoadItem(image)
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setTransformation(new CircleCrop())
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
                        .setLoadItem(new File(image))
                        .setCenterCrop(true)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .setTransformation(new CircleCrop())
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
            ivDeleteItem = snapshotView.findViewById(R.id.ivDeleteItem);
            pbProgressBar = snapshotView.findViewById(R.id.pbProgressBar);
        }
    }

    /**
     * Method to view Images that the User has Uploaded
     *
     * @param position
     */
    private void viewImages(int position) {
        if (activity instanceof CheckoutTemplateActivity)
            ((CheckoutTemplateActivity) activity).setCustomFieldPosition(item);
        else if (activity instanceof CheckOutCustomActivity)
            ((CheckOutCustomActivity) activity).setCustomFieldPosition(item);
        else if (activity instanceof QuestionnaireTemplateActivity)
            ((QuestionnaireTemplateActivity) activity).setCustomFieldPosition(item);
        /**
         * ViewImagesDialogFreelancer is used to view full image already selected in custom fiels
         */
        new ViewImagesDialogFreelancer.Builder(activity)
                .images(imagesList)
                .isForDisplay(isForDisplay)
                .title("").position(position).build().show();
    }
}
