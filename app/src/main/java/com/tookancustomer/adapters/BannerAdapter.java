package com.tookancustomer.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tookancustomer.R;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.interfaces.RestaurantListingInterface;
import com.tookancustomer.models.bannersData.BannersData;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;

import java.util.ArrayList;

public class BannerAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private final RestaurantListingInterface restaurantListingInterface;
    private final BannersData bannersData;

    private Activity activity;
    private ArrayList<String> imagesList;

    public BannerAdapter(RestaurantListingActivity activity, RestaurantListingInterface restaurantListingInterface,ArrayList<String> imagesList, BannersData bannersData) {
        this.activity = activity;
        this.imagesList = imagesList;
        this.restaurantListingInterface = restaurantListingInterface;
        this.bannersData = bannersData;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View customFieldImageView = layoutInflater.inflate(R.layout.list_item_custom_field_image_pdp, container, false);

        String imagePath = imagesList.get(position);
        // View to display the Image
        ImageView imgSnapshot = customFieldImageView.findViewById(R.id.imgSnapshot);


        Log.i("URL", "==" + imagePath);
        if (bannersData.getBanner() != null && bannersData.getBanner().get(position).getMobileImage() != null && !bannersData.getBanner().get(position).getMobileImage().isEmpty()) {
            new GlideUtil.GlideUtilBuilder(imgSnapshot)
                    .setLoadItem(bannersData.getBanner().get(position).getMobileImage())
                    .setFitCenter(true)
                    .setPlaceholder(R.drawable.ic_image_placeholder)
                    .build();
        } else {
            if (imagePath != null) {
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
            }
        }

        imgSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // new ViewImagesDialogProducts.Builder(activity).images(imagesList).title("").position(position).build().show();
                restaurantListingInterface.redirectToMerchant(bannersData.getBanner().get(position).getMerchantId(), position,bannersData.getBanner().get(position));

            }
        });

        // Add the View to container
        container.addView(customFieldImageView);

        return customFieldImageView;
    }


    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
