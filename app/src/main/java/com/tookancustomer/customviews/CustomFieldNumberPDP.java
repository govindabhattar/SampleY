package com.tookancustomer.customviews;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.tookancustomer.R;
import com.tookancustomer.adapters.CustomFieldNumberPDPAdapter;
import com.tookancustomer.models.ProductCatalogueData.CustomField;

import java.util.ArrayList;


public class CustomFieldNumberPDP {

    private final String TAG = CustomFieldNumberPDP.class.getSimpleName();
    private View view;
    private RecyclerView rvGridLayout;
    Context context;
    private ArrayList<CustomField> numberCustomFieldArrayList;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldNumberPDP(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pdp_grid, null);
        rvGridLayout = view.findViewById(R.id.rvGridLayout);

    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(Activity mActivity, ArrayList<CustomField> numberCustomFieldArrayList) {
        this.numberCustomFieldArrayList = numberCustomFieldArrayList;

        rvGridLayout.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvGridLayout.setNestedScrollingEnabled(false);

        CustomFieldNumberPDPAdapter customFieldNumberPDPAdapter = new CustomFieldNumberPDPAdapter(mActivity, numberCustomFieldArrayList);
        rvGridLayout.setAdapter(customFieldNumberPDPAdapter);

        return view;
    }

}
