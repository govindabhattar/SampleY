package com.tookancustomer.checkoutTemplate.customViews;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.checkoutTemplate.model.Template;


public class CustomFieldMultiSelectCheckout {
    private Activity activity;

    private View view;
    private TextView tvLabel;
    private RecyclerView rvCheckList;

    public CustomFieldMultiSelectCheckout(Activity activity) {
        this.activity = activity;
        view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_checklist, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        rvCheckList = view.findViewById(R.id.rvCheckList);
        rvCheckList.setLayoutManager(new LinearLayoutManager(activity));
    }


    public View render(Template template) {


        return view;
    }

}
