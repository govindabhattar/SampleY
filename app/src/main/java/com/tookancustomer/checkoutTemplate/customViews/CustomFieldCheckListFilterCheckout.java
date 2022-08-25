package com.tookancustomer.checkoutTemplate.customViews;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.adapters.FilterAdapter;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductFilters.AllowValue;

import java.util.ArrayList;


public class CustomFieldCheckListFilterCheckout {

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
    public CustomFieldCheckListFilterCheckout(Activity mActivity) {
        this.mActivity = mActivity;
        view = ((LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_checklist, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        rvCheckList = view.findViewById(R.id.rvCheckList);
        rvCheckList.setNestedScrollingEnabled(false);
        rvCheckList.setLayoutManager(new LinearLayoutManager(mActivity));
//        GridLayoutManager gridLayoutManager =
//                new GridLayoutManager(mActivity, 2);
// Attach the layout manager to the recycler view
//        rvCheckList.setLayoutManager(gridLayoutManager);
    }


    /**
     * clear Filters list
     */
    public void clearFilters() {
        if (filterAdapter != null) {
            filterAdapter.clearFilterList();
        }
    }

    public View render(Template template, boolean isForDisplay) {
        return render(template, isForDisplay, null);
    }

    public View render(Template template, boolean isForDisplay, Datum productDataItem) {
        return render(template, isForDisplay, productDataItem, "");
    }

    public View render(Template template, boolean forDisplay, Datum productDataItem, String getOrderCurrencySymbol) {
        tvLabel.setText(template.getDisplayName());
        if (productDataItem != null) {
            filterAdapter = new FilterAdapter(template.getAllowedValuesWithIsSelected(), forDisplay, mActivity, productDataItem);
        } else if (!getOrderCurrencySymbol.isEmpty()) {
            filterAdapter = new FilterAdapter(template.getAllowedValuesWithIsSelected(), forDisplay, mActivity, getOrderCurrencySymbol);

        } else {
            filterAdapter = new FilterAdapter(template.getAllowedValuesWithIsSelected(), forDisplay, mActivity);
        }
        rvCheckList.setAdapter(filterAdapter);


        if (template.isRequired()) {
            tvLabel.setText(CustomViewsUtil.createSpan(mActivity, template.getDisplayName(), "*"));
        } else {
            tvLabel.setText(template.getDisplayName());
        }


        return view;
    }
}