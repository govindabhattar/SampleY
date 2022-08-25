package com.tookancustomer.customviews;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.tookancustomer.R;
import com.tookancustomer.models.ProductCatalogueData.CustomField;


public class CustomFieldGridPDP {

    private final String TAG = CustomFieldGridPDP.class.getSimpleName();
    private View view;
    Context context;
    private CustomField item;
    private RecyclerView rvGridLayout;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldGridPDP(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pdp_grid, null);
        rvGridLayout = view.findViewById(R.id.tvCustomFieldLabel);
        rvGridLayout.setLayoutManager(new LinearLayoutManager(context));
        rvGridLayout.setNestedScrollingEnabled(false);
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(CustomField item) {
        this.item = item;

//        switch (item.getD)

//        merchantReviewsAdapter = new MerchantReviewsAdapter(mActivity, merchantReviewsList);
//        rvReviewsList.setAdapter(merchantReviewsAdapter);

//        tvCustomFieldLabel.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
//        tvHouseRules.setText(item.getValue() + "");


        return view;
    }

}
