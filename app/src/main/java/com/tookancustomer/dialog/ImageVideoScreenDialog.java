package com.tookancustomer.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.CheckOutActivityOld;
import com.tookancustomer.R;
import com.tookancustomer.adapter.ViewImagesVideoAdapter;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ImagesAndVedios;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class ImageVideoScreenDialog extends DialogFragment {


    private ViewPager vpImages;
    private ArrayList<ImagesAndVedios> imagesList = new ArrayList<>();
    private Activity activity;
    private ViewImagesVideoAdapter viewImagesAdapter;
    private LinearLayout llIndicators;
    private int currentItem = 0;
    private RelativeLayout rlBack;
    private TextView tvTitle;
    private RelativeLayout rlDelete;
    private String title = "";

    public ImageVideoScreenDialog(Datum productData, Activity activity) {
        this.activity = activity;
        for (int i = 0; i < productData.getMultiImageUrl().size(); i++) {
            ImagesAndVedios imagesAndVedios = new ImagesAndVedios();
            imagesAndVedios.setImageVideoUrl(productData.getMultiImageUrl().get(i));
            imagesAndVedios.setType(1);
            imagesList.add(imagesAndVedios);
        }
        for (int i = 0; i < productData.getMultiVideoUrl().size(); i++) {
            ImagesAndVedios imagesAndVedios = new ImagesAndVedios();
            imagesAndVedios.setImageVideoUrl(productData.getMultiVideoUrl().get(i).getUrl());

            if (!productData.getMultiVideoUrl().get(i).getThumbUrls().get250x250().isEmpty())
                imagesAndVedios.setVideoThumb(productData.getMultiVideoUrl().get(i).getThumbUrls().get250x250());
            else
                imagesAndVedios.setVideoThumb(productData.getMultiVideoUrl().get(i).getThumbUrls().get400x400());

            imagesAndVedios.setType(2);
            if (productData.getMultiVideoUrl().get(i).getUrl() != null && !productData.getMultiVideoUrl().get(i).getUrl().isEmpty())
                imagesList.add(imagesAndVedios);
        }

        this.title = productData.getName();

    }


   /* public ImageVideoScreenDialog(Datum imagesList, AppCompatActivity activity) {

    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //  mDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        return super.onCreateDialog(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_view_images_products, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llIndicators = view.findViewById(R.id.llIndicators);
        vpImages = view.findViewById(R.id.vpImages);
        rlBack = view.findViewById(R.id.rlBack);
        tvTitle = view.findViewById(R.id.tvTitle);
        rlDelete = view.findViewById(R.id.rlDelete);
        tvTitle.setText(title);
        List<Fragment> framnetList = new ArrayList<>();
        for (int i = 0; i < imagesList.size(); i++) {
            framnetList.add(new VideoImageItemFragment(imagesList.get(i)));
        }
        viewImagesAdapter = new ViewImagesVideoAdapter(getChildFragmentManager(), framnetList, activity);
        vpImages.setAdapter(viewImagesAdapter);
        vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                inflateIndicators(position, llIndicators);
                currentItem = position;



               /* Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vpImages + ":" + vpImages.getCurrentItem());
                if (page != null) {
                    ((VideoImageItemFragment) page).setData(imagesList.get(position));


                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (activity instanceof CheckOutActivityOld) {

            rlDelete.setVisibility(View.VISIBLE);
            rlDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkDeleteStatus(currentItem);
                }
            });
        }

        inflateIndicators(0, llIndicators);
        vpImages.setCurrentItem(currentItem);

        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vpImages + ":" + vpImages.getCurrentItem());
        if (page != null) {
            ((VideoImageItemFragment) page).setData(imagesList.get(0));


        }
    }

    private void inflateIndicators(int position, LinearLayout llIndicators) {
//        tvTitle.setText((position + 1) + " " + StorefrontCommonData.getString(activity, R.string.of) + " " + imagesList.size());

        if (imagesList.size() <= 1) {
            llIndicators.setVisibility(View.GONE);
        } else {
            llIndicators.setVisibility(View.VISIBLE);
            llIndicators.removeAllViews();

            int pixels = Utils.convertDpToPixels(llIndicators.getContext(), 8);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels, pixels);

            for (int index = 0; index < imagesList.size(); index++) {
                View view = new View(activity);
                view.setLayoutParams(params);
                view.setBackgroundResource(index == position ? R.drawable.switcher_filled : R.drawable.switcher);
                llIndicators.addView(view);
            }
        }
    }

    private void checkDeleteStatus(final int position) {
        final String addImageItem = imagesList.get(position).getImageVideoUrl();
        new OptionsDialog.Builder(activity)
                .message(StorefrontCommonData.getString(activity, R.string.delete_this_image_text))
                .listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        dismiss();
                        CheckOutActivityOld checkOutActivity = (CheckOutActivityOld) activity;
                        checkOutActivity.deleteCustomFieldImagePickup(position);
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                    }
                }).build().show();
    }
}
