package com.tookancustomer.customviews;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.adapters.FilterAdapter;
import com.tookancustomer.customfield.CustomField;
import com.tookancustomer.models.ProductFilters.AllowValue;

import java.util.ArrayList;



public class CustomFieldCheckListFilterFreelancer {

    private Activity mActivity;
    private View view;
    private TextView tvLabel;
    private RecyclerView rvCheckList;
    private FilterAdapter filterAdapter;
    private ArrayList<AllowValue> selectedFilterList = new ArrayList<>();
    private String filterHeading;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldCheckListFilterFreelancer(Activity mActivity) {
        this.mActivity = mActivity;
        view = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_checklist, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        rvCheckList = view.findViewById(R.id.rvCheckList);
        rvCheckList.setNestedScrollingEnabled(false);
        rvCheckList.setLayoutManager(new LinearLayoutManager(mActivity));
    }

//    /**
//     * Method to render the data of the Custom Field
//     *
//     * @return
//     */
//    public View render(FilterAndValue filterData) {
//        this.filterData = filterData;
//        tvLabel.setText(filterData.getDisplayName());
//        filterAdapter = new FilterAdapter(filterData.getAllowedValuesWithIsSelected());
//        rvCheckList.setAdapter(filterAdapter);
//        return view;
//    }

    /**
     * clear Filters list
     */
    public void clearFilters() {
        if (filterAdapter != null) {
            filterAdapter.clearFilterList();
        }
    }

    public View render(CustomField template) {
        tvLabel.setText(template.getDisplayName());
        filterAdapter = new FilterAdapter(template.getAllowedValuesWithIsSelected(),mActivity);
        rvCheckList.setAdapter(filterAdapter);
        return view;
    }
}