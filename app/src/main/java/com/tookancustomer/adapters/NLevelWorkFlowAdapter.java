package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tookancustomer.MyApplication;
import com.tookancustomer.NLevelWorkFlowActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.NLevelWorkFlowModel.Datum;
import com.tookancustomer.models.NLevelWorkFlowModel.NLevelWorkFlowData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Codes.Request.OPEN_NLEVEL_ACTIVITY_AGAIN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DATE_TIME;
import static com.tookancustomer.appdata.Keys.Extras.CATEGORY_LEVEL;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_NAME;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_DATA_OBJECT;
import static com.tookancustomer.appdata.Keys.Extras.PARENT_ID;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_ADDRESS;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LATITUDE;
import static com.tookancustomer.appdata.Keys.Extras.PICKUP_LONGITUDE;
import static com.tookancustomer.appdata.Keys.Extras.SHOW_PRODUCT_IMAGES;
import static com.tookancustomer.appdata.Keys.Extras.STOREFRONT_DATA;

//import static com.bumptech.glide.load.engine.DiskCacheStrategy.SOURCE;

/**
 * Created by cl-macmini-25 on 19/12/16.
 */

public class NLevelWorkFlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity;

    private TextView tvNoCatalogueFound;
    private ArrayList<ArrayList<Datum>> dataList;
    private final UserData userData;
    private final NLevelWorkFlowData nLevelWorkFlowData;
    private final int categoryLevel;
    public ArrayList<Integer> parentId = new ArrayList<Integer>();
    private String searchString = "";
    private Double pickupLatitude = 0.0, pickupLongitude = 0.0;
    private String pickupAddress = "";
    com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private String preOrderDateTime;
    public NLevelWorkFlowAdapter(String utcDateTime, Activity activity, TextView tvNoCatalogueFound, UserData userData,
                                 NLevelWorkFlowData nLevelWorkFlowData, int categoryLevel, ArrayList<Integer> parentId,
                                 String searchString, Double pickupLatitude, Double pickupLongitude, String pickupAddress,
                                 com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData) {
        this.preOrderDateTime = utcDateTime;
        this.activity = activity;
        this.storefrontData = storefrontData;
        this.tvNoCatalogueFound = tvNoCatalogueFound;
        this.userData = userData;
        this.nLevelWorkFlowData = nLevelWorkFlowData;
        this.dataList = nLevelWorkFlowData.getData();
        this.categoryLevel = categoryLevel;
        this.parentId = parentId;
        this.searchString = searchString;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.pickupAddress = pickupAddress;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(Constants.NLevelLayoutType.getLayoutModeByValue(activity, viewType), parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position1) {
        final int position = holder.getAdapterPosition();
        final ArrayList<Datum> arrayList = getArrayList();

        if (holder instanceof GenericViewHolder) {
            GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
        } else if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;

            Utils.setVisibility(View.GONE, viewHolder.tvDescription1, viewHolder.tvDescription2, viewHolder.tvDescription3);
            Utils.setVisibility(View.GONE, viewHolder.linearLayoutQuantitySelector, viewHolder.ivForwardArrowButton);
            Utils.setVisibility(View.GONE, viewHolder.rlNLevelImageParent);


            if (arrayList.get(position).getLayoutData() != null) {
                if (arrayList.get(position).getLayoutData().getImages().size() > 0) {
                    if (arrayList.get(position).getLayoutData().getImages().get(0).getSize() != (Constants.NLevelImageStyles.NONE.appStyleValue)) {
                        Utils.setVisibility(View.VISIBLE, viewHolder.rlNLevelImageParent);
                        try {
                            viewHolder.ivNLevelImage.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (arrayList.get(position).getThumbUrl().isEmpty()) {
                                        viewHolder.ivNLevelImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                                    } else {
                                        /*Glide.with(activity).load(arrayList.get(position).getThumbUrl())
                                                .asBitmap()
                                                .centerCrop()
//                                                .fitCenter()
                                                .diskCacheStrategy(SOURCE)
                                                .placeholder(AppCompatResources.getDrawable(activity,R.drawable.ic_loading_image))
                                                .into(new BitmapImageViewTarget(viewHolder.ivNLevelImage) {
                                                    @Override
                                                    protected void setResource(Bitmap resource) {
                                                        if (!activity.isDestroyed()) {
                                                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                                            circularBitmapDrawable.setCornerRadius(4);
                                                            viewHolder.ivNLevelImage.setImageDrawable(circularBitmapDrawable);
                                                        }
                                                    }

                                                    @Override
                                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                        super.onLoadFailed(e, errorDrawable);
                                                        viewHolder.ivNLevelImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_image_placeholder));
                                                    }
                                                });*/

                                        new GlideUtil.GlideUtilBuilder(viewHolder.ivNLevelImage)
                                                .setFitCenter(true)
                                                .setTransformation(new RoundedCorners(4))
                                                .setPlaceholder(R.drawable.ic_loading_image)
                                                .setError(R.drawable.ic_image_placeholder)
                                                .setFallback(R.drawable.ic_image_placeholder)
                                                .setLoadItem(arrayList.get(position).getThumbUrl())
                                                .build();


                                    }
                                }
                            });

                        } catch (Exception e) {

                               Utils.printStackTrace(e);
                        }
                    }
                }

                for (int i = 0; i < arrayList.get(position).getLayoutData().getLines().size(); i++) {
                    switch (i) {
                        case 0:
                            if (arrayList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription1);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription1);
                            }
                            viewHolder.tvDescription1.setText(arrayList.get(position).getLayoutData().getLines().get(i).getData());
                            viewHolder.tvDescription1.setTypeface(viewHolder.tvDescription1.getTypeface(), Font.BOLD);
                            break;
                        case 1:
                            if (arrayList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription2);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription2);
                            }
                            viewHolder.tvDescription2.setText(arrayList.get(position).getLayoutData().getLines().get(i).getData());
//                            viewHolder.tvDescription2.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, arrayList.get(position).getLayoutData().getLines().get(i).getStyle())));
                            break;
                        case 2:
                            if (arrayList.get(position).getLayoutData().getLines().get(i).getData().isEmpty()) {
                                Utils.setVisibility(View.GONE, viewHolder.tvDescription3);
                            } else {
                                Utils.setVisibility(View.VISIBLE, viewHolder.tvDescription3);
                            }
                            viewHolder.tvDescription3.setText(arrayList.get(position).getLayoutData().getLines().get(i).getData());
//                            viewHolder.tvDescription3.setTypeface(Font.getTypeFaceNLevel(activity, Constants.NLevelAppStyles.getAppFontByValue(activity, arrayList.get(position).getLayoutData().getLines().get(i).getStyle())));
                            break;

                    }
                }
            }

            for (int i = 0; i < arrayList.get(position).getLayoutData().getButtons().size(); i++) {
                if (i == 0) {
                    int i1 = Constants.NLevelButtonType.getButtonIdByValue(activity, arrayList.get(position).getLayoutData().getButtons().get(0).getType());
                    if (i1 == Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue) {
                    } else if (i1 == Constants.NLevelButtonType.NEXT_ARROW_BUTTON.buttonValue) {
                        Utils.setVisibility(View.VISIBLE, viewHolder.ivForwardArrowButton);
                    }
                }
            }

            viewHolder.llNLevelParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.CATEGORY_CLICK, arrayList.get(position).getName());

                    if (!Utils.internetCheck(activity)) {
                        new AlertDialog.Builder(activity).message(StorefrontCommonData.getString(activity, R.string.no_internet_try_again)).build().show();
                        return;
                    }
                    ArrayList<Integer> currentParentId = new ArrayList<Integer>();
                    currentParentId.add(getArrayList().get(position).getCatalogueId());

//                    for (int i = 0; i < getArrayList().size(); i++) {
//                        if (getArrayList().get(i).getCatalogueId().equals(getArrayList().get(position).getCatalogueId())) {
//                        }
//                    }

//                    if (dataList.size() > categoryLevel + 1) {
                    Bundle extras = new Bundle();
                    extras.putSerializable(UserData.class.getName(), userData);
                    extras.putSerializable(STOREFRONT_DATA, storefrontData);
                    extras.putSerializable(NLevelWorkFlowData.class.getName(), nLevelWorkFlowData);
                    extras.putInt(CATEGORY_LEVEL, categoryLevel + 1);
                    extras.putBoolean(SHOW_PRODUCT_IMAGES, arrayList.get(position).getProductsHasImage() == 1);
                    extras.putIntegerArrayList(PARENT_ID, currentParentId);
                    extras.putString(HEADER_NAME, getArrayList().get(holder.getAdapterPosition()).getName());
                    extras.putSerializable(PARENT_DATA_OBJECT, getArrayList().get(holder.getAdapterPosition()));
                    extras.putDouble(PICKUP_LATITUDE, pickupLatitude);
                    extras.putDouble(PICKUP_LONGITUDE, pickupLongitude);
                    extras.putString(PICKUP_ADDRESS, pickupAddress);
                    extras.putString(DATE_TIME, preOrderDateTime);


                    Intent intent = new Intent(activity, NLevelWorkFlowActivity.class);
                    intent.putExtras(extras);
                    activity.startActivityForResult(intent, OPEN_NLEVEL_ACTIVITY_AGAIN);
                    AnimationUtils.forwardTransition(activity);

//                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getArrayList() == null ? 0 : getArrayList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return getParentLayout(position);
    }

    private int getParentLayout(int position) {
        if (dataList != null && dataList.size() > 0) {
            try {
                if (parentId.size() > 0) {
                    for (int i = 0; i < dataList.get(categoryLevel).size(); i++) {
                        for (int j = 0; j < parentId.size(); j++) {
                            if (dataList.get(categoryLevel).get(i).getParentCategoryId().equals(parentId.get(j))) {
                                return dataList.get(categoryLevel).get(i).getLayoutType();
                            }
                        }
                    }
                } else {
                    return dataList.get(categoryLevel).get(0).getLayoutType();
                }
//                return dataList.get(categoryLevel).get(position).getLayoutType();
            } catch (Exception e) {
            }
//                if (parentId.size() > 0) {
//                for (int i = 0; i < dataList.get(categoryLevel - 1).size(); i++) {
//                    for (int j = 0; j < parentId.size(); j++) {
//                        if (dataList.get(categoryLevel - 1).get(i).getCatalogueId().equals(parentId.get(j))) {
//                            return dataList.get(categoryLevel - 1).get(i).getChildLayoutType();
//                        }
//                    }
//                }
//            }
        }
//        if (categoryLevel == 0) {
//            for (int i = 0; i < dataList.get(categoryLevel).size(); i++) {
//                return dataList.get(categoryLevel).get(0).getLayoutType();
//            }
//        } else {
//
//            for (int i = 0; i < dataList.get(categoryLevel).size(); i++) {
//                return dataList.get(categoryLevel).get(i).getLayoutType();
//
//            }
//        }


//                if (parentId.size() > 0) {
//                for (int i = 0; i < dataList.get(categoryLevel - 1).size(); i++) {
//                    for (int j = 0; j < parentId.size(); j++) {
//                        if (dataList.get(categoryLevel - 1).get(i).getCatalogueId().equals(parentId.get(j))) {
//                            return dataList.get(categoryLevel - 1).get(i).getChildLayoutType();
//                        }
//                    }
//                }
//            }

        return Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue;
    }

    private ArrayList<Datum> getArrayList() {
        ArrayList<Datum> datumArrayList = new ArrayList<>();

        if (searchString != null) {
            if (categoryLevel == 0 || parentId.size() == 0) {
                for (int i = 0; i < dataList.get(categoryLevel).size(); i++) {
                    if (dataList.get(categoryLevel).get(i).getIsDummy() == 0) {
                        if (searchString.isEmpty()) {
                            datumArrayList.add(dataList.get(categoryLevel).get(i));
                        } else {
                            if (dataList.get(categoryLevel).get(i).getName().toLowerCase().contains(searchString.toLowerCase())) {
                                datumArrayList.add(dataList.get(categoryLevel).get(i));
                            }
                        }

                    }
                }
            } else {
                for (int i = 0; i < dataList.get(categoryLevel).size(); i++) {
                    for (int j = 0; j < parentId.size(); j++) {
                        if (dataList.get(categoryLevel).get(i).getParentCategoryId().equals(parentId.get(j)) && dataList.get(categoryLevel).get(i).getIsDummy() == 0) {

                            if (searchString.isEmpty()) {
                                datumArrayList.add(dataList.get(categoryLevel).get(i));
                            } else {
                                if (dataList.get(categoryLevel).get(i).getName().toLowerCase().contains(searchString.toLowerCase())) {
                                    datumArrayList.add(dataList.get(categoryLevel).get(i));
                                }
                            }

                        }
                    }
                }
            }
        }

        if (datumArrayList.size() == 0) {
            tvNoCatalogueFound.setVisibility(View.VISIBLE);
        } else {
            tvNoCatalogueFound.setVisibility(View.GONE);
        }
        return datumArrayList;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        GenericViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llNLevelParent;
        private RelativeLayout rlNLevelImageParent;
        private ImageView ivNLevelImage;

        private TextView tvDescription1, tvDescription2, tvDescription3;

        private RelativeLayout rlQuantitySelector;
        private LinearLayout linearLayoutQuantitySelector;
        private RelativeLayout rlRemoveItem, rlAddItem;
        private TextView textViewQuantity;
        private ImageView ivForwardArrowButton;

        ViewHolder(View itemView) {
            super(itemView);
            llNLevelParent = itemView.findViewById(R.id.llNLevelParent);
            rlNLevelImageParent = itemView.findViewById(R.id.rlNLevelImageParent);
            ivNLevelImage = itemView.findViewById(R.id.ivNLevelImage);

            tvDescription1 = itemView.findViewById(R.id.tvDescription1);
            tvDescription2 = itemView.findViewById(R.id.tvDescription2);
            tvDescription3 = itemView.findViewById(R.id.tvDescription3);

            rlQuantitySelector = itemView.findViewById(R.id.rlQuantitySelector);
            linearLayoutQuantitySelector = itemView.findViewById(R.id.linearLayoutQuantitySelector);
            rlRemoveItem = itemView.findViewById(R.id.rlRemoveItem);
            rlAddItem = itemView.findViewById(R.id.rlAddItem);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            ivForwardArrowButton = itemView.findViewById(R.id.ivForwardArrowButton);

        }
    }
}