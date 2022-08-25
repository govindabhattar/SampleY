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
import com.tookancustomer.models.ProductFilters.AllowValue;
import com.tookancustomer.models.ProductFilters.FilterAndValue;
import com.tookancustomer.models.userdata.Item;

import java.util.ArrayList;

public class CustomFieldCheckListFilter {

    private Activity mActivity;
    private FilterAndValue filterData;
    private View view;
    private TextView tvLabel;
    private RecyclerView rvCheckList;
    private Item item;
    private FilterAdapter filterAdapter;
    private ArrayList<AllowValue> selectedFilterList = new ArrayList<>();
    private String filterHeading;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldCheckListFilter(Activity mActivity) {
        this.mActivity = mActivity;
        view = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_checklist, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        rvCheckList = view.findViewById(R.id.rvCheckList);
        rvCheckList.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(FilterAndValue filterData) {
        this.filterData = filterData;
        tvLabel.setText(filterData.getDisplayName());
        filterAdapter = new FilterAdapter(filterData.getAllowedValuesWithIsSelected(),mActivity);
        rvCheckList.setAdapter(filterAdapter);
        return view;
    }

    /**
     * clear Filters list
     */
    public void clearFilters() {
        if (filterAdapter != null) {
            filterAdapter.clearFilterList();
        }
    }
}