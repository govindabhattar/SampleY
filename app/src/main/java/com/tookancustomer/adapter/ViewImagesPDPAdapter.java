package com.tookancustomer.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tookancustomer.R;
import com.tookancustomer.dialog.ViewImagesDialogProducts;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Log;

import java.util.ArrayList;

public class ViewImagesPDPAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;

    private Activity activity;
    private ArrayList<String> imagesList;

    public ViewImagesPDPAdapter(Activity activity, ArrayList<String> imagesList) {
        this.activity = activity;
        this.imagesList = imagesList;

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

        if (imagePath == null) {

        } else {
            Log.i("URL", "==" + imagePath);
            // Verify whether a web url

            if (imagePath.startsWith("http://") || imagePath.startsWith("https://"))
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
                // Load from Storage
            else
                new GlideUtil.GlideUtilBuilder(imgSnapshot)
                        .setLoadItem(imagePath)
                        .setFitCenter(true)
                        .setPlaceholder(R.drawable.ic_image_placeholder)
                        .build();
        }

        imgSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new ViewImagesDialogProducts.Builder(activity).images(imagesList).title("").position(position).build().show();

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
