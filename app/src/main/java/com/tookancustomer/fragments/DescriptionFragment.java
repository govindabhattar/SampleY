package com.tookancustomer.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;

public class DescriptionFragment extends Fragment {
    private Activity mActivity;
    private TextView tvDescription,tvNoDescription;
    private String description;
    private static final int MAX_LINES = 4;


    public DescriptionFragment(String description) {
        this.description = description;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_description, container, false);
        mActivity = getActivity();
        initViews(rootView);
        return rootView;
    }

    public void initViews(View v) {
        tvDescription = v.findViewById(R.id.tvDescription);
        tvNoDescription = v.findViewById(R.id.tvNoDescription);
        tvNoDescription.setText(StorefrontCommonData.getString(mActivity,R.string.no_description_available));

        if (description.equalsIgnoreCase("")) {
            tvNoDescription.setVisibility(View.VISIBLE);
            tvDescription.setVisibility(View.GONE);
        } else {
            tvNoDescription.setVisibility(View.GONE);
            tvDescription.setVisibility(View.VISIBLE);
        }


        tvDescription.setText(description);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                tvDescription.getParent().requestDisallowInterceptTouchEvent(true);
//                tvDescription.setScrollIndicators(View.SCROLL_INDICATOR_END);

                return false;
            }
        });
//        tvDescription.post(new Runnable() {
//            @Override
//            public void run() {
//                int lineCount = tvDescription.getLineCount();
//                // Use lineCount here
//                if (tvDescription.getLineCount() > MAX_LINES) {
//                    //added textview , Max Lines(you want to show at normal),and view more label true so that you can expand
//                    ResizableCustomView.doResizeTextView(mActivity, tvDescription, MAX_LINES, getString(R.string.read_more), true);
//                }
//            }
//        });
    }
}