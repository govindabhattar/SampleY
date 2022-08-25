package com.tookancustomer.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.adapters.PDPPhotoAdapter;
import com.tookancustomer.appdata.StorefrontCommonData;

import java.util.ArrayList;

public class PDPPhotosFragment extends Fragment {
    private Activity mActivity;
    private TextView tvNoPhotos;
    public RecyclerView rvPDPPhoto;
    private ArrayList<String> imagesList;
    private PDPPhotoAdapter pdpPhotoAdapter;

    public PDPPhotosFragment(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_pdp_photo, container, false);
        mActivity = getActivity();
        initViews(rootView);
        return rootView;
    }

    public void initViews(View v) {
        rvPDPPhoto = v.findViewById(R.id.rvPDPPhoto);
        tvNoPhotos = v.findViewById(R.id.tvNoPhotos);
        tvNoPhotos.setText(StorefrontCommonData.getString(mActivity,R.string.no_photo_available));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPDPPhoto.setLayoutManager(gridLayoutManager);
//        rvPDPPhoto.setLayoutManager(new GridLayoutManager(mActivity, 3));
//        rvPDPPhoto.setLayoutManager(new GridLayoutManager(mActivity,2, LinearLayoutManager.HORIZONTAL,false));


        rvPDPPhoto.setNestedScrollingEnabled(true);
        rvPDPPhoto.setHasFixedSize(false);


        if (imagesList.size() == 1 && imagesList.get(0).equalsIgnoreCase("")) {
            tvNoPhotos.setVisibility(View.VISIBLE);
            rvPDPPhoto.setVisibility(View.GONE);
        } else {
            tvNoPhotos.setVisibility(View.GONE);
            rvPDPPhoto.setVisibility(View.VISIBLE);
            pdpPhotoAdapter = new PDPPhotoAdapter(mActivity, imagesList);
            rvPDPPhoto.setAdapter(pdpPhotoAdapter);
        }


//        pdpPhotoAdapter = new PDPPhotoAdapter(mActivity, imagesList);
//        rvPDPPhoto.setAdapter(pdpPhotoAdapter);

    }
}