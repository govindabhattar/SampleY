package com.tookancustomer.modules.merchantCatalog.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.transition.Transition;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants;
import com.tookancustomer.modules.merchantCatalog.models.categories.Result;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants.CategoryLayoutTypes.GRID_LAYOUT;
import static com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants.CategoryLayoutTypes.LIST_LAYOUT;
import static com.tookancustomer.modules.merchantCatalog.constants.MerchantCatalogConstants.CategoryLayoutTypes.getLayoutModeByValue;

public class MerchantCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private Datum storefrontData;
    private ArrayList<Result> categoriesList = new ArrayList<>();
    private boolean showImages;
    private Callback mCallback;

    public MerchantCategoriesAdapter(Activity activity, Datum storefrontData, ArrayList<Result> categoriesList, boolean showImages, Callback mCallback) {
        this.activity = activity;
        this.storefrontData = storefrontData;
        this.categoriesList = categoriesList;
        this.showImages = showImages;
        this.mCallback = mCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getLayoutModeByValue(activity, storefrontData.getCategoryLayoutType()), parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();

        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (showImages) {
                Utils.setVisibility(View.VISIBLE, viewHolder.rlImageParent);
                try {
                    viewHolder.ivCategoryImage.post(new Runnable() {
                        @Override
                        public void run() {
                            if (categoriesList.get(position).getThumbUrl().isEmpty()) {
                                viewHolder.ivCategoryImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                            } else {
                                /* For center crop we have to pass transformation method like this */
                                MultiTransformation multi = new MultiTransformation<>(
                                        new RoundedCorners(10));
                                if (getLayoutModeByValue(activity, storefrontData.getCategoryLayoutType()) == GRID_LAYOUT.layoutValue
                                        || getLayoutModeByValue(activity, storefrontData.getCategoryLayoutType()) == LIST_LAYOUT.layoutValue) {
                                    new GlideUtil.GlideUtilBuilder(viewHolder.ivCategoryImage)
                                            .setError(R.drawable.ic_image_placeholder)
                                            .setFallback(R.drawable.ic_image_placeholder)
                                            .setLoadItem(categoriesList.get(position).getImageUrl())
                                            .setTransformation(multi)
                                            .setThumbnailItem(categoriesList.get(position).getThumbUrl())
                                            .setPlaceholder(R.drawable.ic_loading_image)
                                            .build();
                                } else
                                    new GlideUtil.GlideUtilBuilder(viewHolder.ivCategoryImage)
                                            .setError(R.drawable.ic_image_placeholder)
                                            .setFallback(R.drawable.ic_image_placeholder)
                                            .setLoadItem(categoriesList.get(position).getImageUrl())
                                            .setTransformation(multi)
                                            .setLoadCompleteListener(new GlideUtil.OnLoadCompleteListener() {
                                                @Override
                                                public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition) {

                                                }

                                                @Override
                                                public void onLoadCompleted(@NonNull Object resource, @Nullable Transition transition, ImageView view) {

                                                }

                                                @Override
                                                public void onLoadFailed() {

                                                }
                                            })
                                            .setThumbnailItem(categoriesList.get(position).getThumbUrl())
                                            .setPlaceholder(R.drawable.ic_loading_image)
                                            .build();


                            }
                        }
                    });

                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }
            } else {
                Utils.setVisibility(View.GONE, viewHolder.rlImageParent);
            }

            viewHolder.tvCategoryName.setVisibility(categoriesList.get(position).getName().isEmpty() ? View.GONE : View.VISIBLE);
            viewHolder.tvCategoryName.setText(categoriesList.get(position).getName());
            viewHolder.tvCategoryName.setTypeface(viewHolder.tvCategoryName.getTypeface(), Typeface.BOLD);

            if (storefrontData.getCategoryLayoutType() == GRID_LAYOUT.layoutValue) {
                Utils.setVisibility(View.GONE, viewHolder.tvCategoryDescription);

                ViewTreeObserver viewTreeObserver = viewHolder.llParentLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            viewHolder.llParentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) (viewHolder.llParentLayout.getWidth()), viewHolder.llParentLayout.getWidth()); // or set height to any fixed value you want
                            viewHolder.llParentLayout.setLayoutParams(lp);
                        }
                    });
                }

            } else {
                viewHolder.tvCategoryDescription.setVisibility(categoriesList.get(position).getDescription().isEmpty() ? View.GONE : View.VISIBLE);
                viewHolder.tvCategoryDescription.setText(categoriesList.get(position).getDescription());
            }

            if (storefrontData.getCategoryButtonType() == MerchantCatalogConstants.ButtonTypes.NEXT_ARROW_BUTTON.buttonValue
                    && storefrontData.getCategoryLayoutType() == LIST_LAYOUT.layoutValue) {
                Utils.setVisibility(View.VISIBLE, viewHolder.ivForwardArrow);
            } else {
                Utils.setVisibility(View.GONE, viewHolder.ivForwardArrow);
            }

            viewHolder.llParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.CATEGORY_CLICK, categoriesList.get(position).getName());
                    mCallback.onCategoryClick(categoriesList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoriesList == null ? 0 : categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llParentLayout;
        private RelativeLayout rlImageParent;
        private ImageView ivCategoryImage;

        private TextView tvCategoryName, tvCategoryDescription;
        private ImageView ivForwardArrow;

        ViewHolder(View itemView) {
            super(itemView);

            llParentLayout = itemView.findViewById(R.id.llParentLayout);
            rlImageParent = itemView.findViewById(R.id.rlImageParent);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);

            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryDescription = itemView.findViewById(R.id.tvCategoryDescription);

            ivForwardArrow = itemView.findViewById(R.id.ivForwardArrow);
        }
    }

    public interface Callback {
        void onCategoryClick(Result categoryData);
    }
}