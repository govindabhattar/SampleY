package com.tookancustomer.adapters;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tookancustomer.HomeActivity;
import com.tookancustomer.R;
import com.tookancustomer.models.MarketplaceStorefrontModel.BannerImage;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;

import java.util.List;

public class StoresImagesAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;

    private List<BannerImage> imagesList;

    public StoresImagesAdapter(HomeActivity activity, List<BannerImage> imagesList) {
        this.imagesList = imagesList;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<BannerImage> getImagesList() {
        return imagesList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View customFieldImageView = layoutInflater.inflate(R.layout.list_item_custom_field_image_pdp, container, false);

        String imagePath = imagesList.get(position).getMobileImage();
        // View to display the Image
        ImageView imgSnapshot = customFieldImageView.findViewById(R.id.imgSnapshot);

        imgSnapshot.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Log.i("URL", "==" + imagePath);

        if (imagePath != null) {
            new GlideUtil.GlideUtilBuilder(imgSnapshot)
                    .setLoadItem(imagePath)
                    .setFitCenter(true)
                    .setPlaceholder(R.drawable.ic_image_placeholder)
                    .build();
        }


        // Add the View to container
        container.addView(customFieldImageView);

        return customFieldImageView;
    }


    @Override
    public int getCount() {
        return imagesList != null ? imagesList.size() : 0;
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
