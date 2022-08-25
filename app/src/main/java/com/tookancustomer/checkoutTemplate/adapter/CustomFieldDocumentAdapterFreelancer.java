package com.tookancustomer.checkoutTemplate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tookancustomer.CheckOutCustomActivity;
import com.tookancustomer.R;
import com.tookancustomer.checkoutTemplate.CheckoutTemplateActivity;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.questionnaireTemplate.QuestionnaireTemplateActivity;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


/**
 * Developer: Rishabh
 * Dated: 22/07/15.
 */
public class CustomFieldDocumentAdapterFreelancer extends RecyclerView.Adapter<CustomFieldDocumentAdapterFreelancer.ViewHolder> {

    private Context context;
    private final int SQUARE_EDGE;

    private Activity activity;
    private ArrayList<String> imagesList;
    //    private final SignupTemplateData item;
    private final Template item;
    private boolean isForDisplay;

    public CustomFieldDocumentAdapterFreelancer(Activity activity, @NonNull ArrayList<String> imagesList,
                                                Template item, boolean isForDisplay) {
        this.activity = activity;
        this.imagesList = imagesList;
        this.item = item;
        this.isForDisplay = isForDisplay;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        SQUARE_EDGE = dm.widthPixels / 2;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View taskItem = LayoutInflater.from(context).inflate(R.layout.horizontal_scroll_document_item, parent, false);
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
                viewDocs(position);
            }
        });

        viewHolder.ivDeleteItem.setTag(position);
        viewHolder.ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();

                if (activity instanceof CheckoutTemplateActivity)
                    ((CheckoutTemplateActivity) activity).deleteCustomFieldDocumentSignup(position);
                else if (activity instanceof CheckOutCustomActivity)
                    ((CheckOutCustomActivity) activity).deleteCustomFieldDocumentSignup(position);


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
                        .setLoadItem(R.drawable.ic_document)
                        .setPlaceholder(R.drawable.ic_document)
                        .setCenterCrop(false)
                        .setFitCenter(true)
                        .build();

            } catch (Exception e) {
                viewHolder.imgSnapshot.setVisibility(View.VISIBLE);
            }
        } else {
            // Load file from the Disk
            try {
                new GlideUtil.GlideUtilBuilder(viewHolder.imgSnapshot)
                        .setLoadItem(R.drawable.ic_document)
                        .setPlaceholder(R.drawable.ic_document)
                        .setCenterCrop(false)
                        .setFitCenter(true)
                        .build();

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
//        private ProgressBar pbProgressBar;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.snapshotView = itemView;
            imgSnapshot = snapshotView.findViewById(R.id.imgSnapshot);
            ivDeleteItem = snapshotView.findViewById(R.id.ivDeleteItem);
//            pbProgressBar = snapshotView.findViewById(R.id.pbProgressBar);
        }
    }

    /**
     * Method to view Images that the User has Uploaded
     *
     * @param position
     */
    private void viewDocs(int position) {
        if (activity instanceof CheckoutTemplateActivity)
            ((CheckoutTemplateActivity) activity).setCustomFieldPosition(item);
        else if (activity instanceof CheckOutCustomActivity)
            ((CheckOutCustomActivity) activity).setCustomFieldPosition(item);
        else if (activity instanceof QuestionnaireTemplateActivity)
            ((QuestionnaireTemplateActivity) activity).setCustomFieldPosition(item);

        //open doucment in browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imagesList.get(position)));

        activity.startActivity(browserIntent);

    }
}
