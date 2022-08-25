package com.tookancustomer.customviews;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.models.taskdetails.CustomField;
import com.tookancustomer.utility.Utils;

/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomFieldTaskDetails {

    private final String TAG = CustomFieldTaskDetails.class.getSimpleName();

    private View view;
    private TextView tvLabel;
    private TextView tvPlaceHolder;
    private ImageView vCustomFieldIcon;

    Context context;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldTaskDetails(Context context) {
        this.context = context;

        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_task_details, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        tvPlaceHolder = view.findViewById(R.id.tvPlaceHolder);
        vCustomFieldIcon = view.findViewById(R.id.vCustomFieldIcon);
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(CustomField customField) {
        if (customField.getDataType().equalsIgnoreCase(Keys.DataType.CHECKBOX)) {
            tvLabel.setVisibility(View.GONE);
            tvPlaceHolder.setText(customField.getDisplayName().replace("_"," ").substring(0,1).toUpperCase()+customField.getDisplayName().replace("_"," ").substring(1).toLowerCase());
            tvPlaceHolder.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.text_size_normal));

            String data = Utils.assign(customField.getFleetData(), customField.getData().isEmpty() ? "-" : customField.getData());

            vCustomFieldIcon.setBackgroundResource(data.equalsIgnoreCase("true") ? R.drawable.ic_icon_checkbox_ticked : R.drawable.ic_icon_checkbox_unticked);
            tvPlaceHolder.setVisibility(data.equalsIgnoreCase("true") ?View.VISIBLE : View.GONE);
            vCustomFieldIcon.setVisibility(data.equalsIgnoreCase("true") ?View.VISIBLE : View.GONE);

        } else {
            tvLabel.setVisibility(View.VISIBLE);
            vCustomFieldIcon.setVisibility(View.GONE);
            tvPlaceHolder.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.text_size_large));

            tvLabel.setText(customField.getDisplayName().replace("_"," ").substring(0,1).toUpperCase()+customField.getDisplayName().replace("_"," ").substring(1).toLowerCase());
            tvPlaceHolder.setText(Utils.assign(customField.getFleetData(), customField.getData().isEmpty() ? "-" : customField.getData()));
        }
        return view;
    }
}
