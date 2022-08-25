package com.tookancustomer.customviews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.dialog.AlertDialogSignUp;
import com.tookancustomer.models.ProductCatalogueData.CustomField;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;

import java.util.ArrayList;


public class CustomFieldTVMultiPDP {

    private final String TAG = CustomFieldTVMultiPDP.class.getSimpleName();
    private View view;
    private TextView tvCustomFieldLabel, tvCustomFieldValue;
    Context context;
    private CustomField item;
    private View vwBotton;
    private ArrayList<CustomField> amenitiesCustomFieldArrayList;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldTVMultiPDP(Context context) {
        this.context = context;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_field_pdp_multiselect, null);

        tvCustomFieldLabel = view.findViewById(R.id.tvCustomFieldLabel);
        tvCustomFieldValue = view.findViewById(R.id.tvCustomFieldValue);
        vwBotton = view.findViewById(R.id.vwBotton);
        vwBotton.setVisibility(View.GONE);
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(final ArrayList<CustomField> amenitiesCustomFieldArrayList) {
        this.amenitiesCustomFieldArrayList = amenitiesCustomFieldArrayList;
        ArrayList<String> multiSelectList = new ArrayList<>();

        for (int i = 0; i < amenitiesCustomFieldArrayList.size(); i++) {
            try {
                multiSelectList = ((ArrayList<String>) amenitiesCustomFieldArrayList.get(i).getValue());
            } catch (Exception e) {
                multiSelectList = new ArrayList<>();
            }
        }

        String value = "";
        if (multiSelectList != null)
            for (int i = 0; i < multiSelectList.size(); i++) {
                value = value + multiSelectList.get(i) + "\t\t";
            }


        Log.e("ameneties<><>", value);
        tvCustomFieldValue.setText(value);

        tvCustomFieldLabel.setText(amenitiesCustomFieldArrayList.get(0).getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + amenitiesCustomFieldArrayList.get(0).getDisplayName().replace("_", " ").substring(1).toLowerCase());


        return view;
    }

}
