package com.tookancustomer.customviews;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tookancustomer.ProfileActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.userdata.Dropdown;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.SingleSelect;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.List;

/**
 * Created by Mohit Kr. Dhiman on 02/12/16.
 */

public class CustomFieldCheckboxProfile implements SignupTemplateData.Listener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = CustomFieldCheckboxProfile.class.getSimpleName();
    private final ProfileActivity activity;
    View vwTop, vwBottom;
    private Spinner spnCustomFieldValues;
    private View view;
    private TextView tvLabel;
    private TextView tvPlaceHolder;
    private ImageView vCustomFieldIcon,vCustomFieldIconEnd;
    private Context context;
    private SignupTemplateData item;
    private int lastPosition = 0;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldCheckboxProfile(Context context, boolean setEnable) {
        this.context = context;
        if ((activity = (ProfileActivity) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_checkbox, null);
        tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        vwTop = (View) view.findViewById(R.id.vwTop);
        vwBottom = (View) view.findViewById(R.id.vwBottom);
        vwTop.setVisibility(View.GONE);
        vwBottom.setVisibility(View.VISIBLE);
        tvPlaceHolder = (TextView) view.findViewById(R.id.tvPlaceHolder);
        vCustomFieldIcon = (ImageView) view.findViewById(R.id.vCustomFieldIcon);
        vCustomFieldIconEnd = (ImageView) view.findViewById(R.id.vCustomFieldIconEnd);
        spnCustomFieldValues = (Spinner) view.findViewById(R.id.spnCustomFieldValues);
        spnCustomFieldValues.setVisibility(View.VISIBLE);
        view.findViewById(R.id.rlParent).setEnabled(false);
        if (setEnable) {
            tvPlaceHolder.setEnabled(true);
            view.findViewById(R.id.rlParent).setEnabled(true);
        } else {
            tvPlaceHolder.setEnabled(false);
            view.findViewById(R.id.rlParent).setEnabled(false);
        }
        Utils.setOnClickListener(this, view.findViewById(R.id.rlParent));
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(SignupTemplateData item) {
        this.item = item;
        this.item.setListener(this);
        switch (item.getDataType()) {
            case Keys.DataType.CHECKBOX:
                boolean isChecked = item.getData().toString().equalsIgnoreCase("true");
                vCustomFieldIcon.setTag(isChecked ? R.drawable.ic_icon_checkbox_ticked : R.drawable.ic_icon_checkbox_unticked);
                vCustomFieldIcon.setBackgroundResource(isChecked ? R.drawable.ic_icon_checkbox_ticked : R.drawable.ic_icon_checkbox_unticked);
                tvPlaceHolder.setTextColor(ContextCompat.getColor(context, isChecked ? R.color.primary_text_color : R.color.colorHint));
                tvPlaceHolder.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
                tvPlaceHolder.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));

                tvLabel.setVisibility(View.GONE);
                break;
            case Keys.DataType.DROP_DOWN:
            case Keys.DataType.SINGLE_SELECT:
                vCustomFieldIconEnd.setBackgroundResource(R.drawable.ic_icon_dropdown_closed1);

                String[] dropDownItems=null ;
                if(item.getDataType().equals(Keys.DataType.DROP_DOWN)  && item.getDropdown() != null){
                    dropDownItems = new String[item.getDropdown().size() + 1];
                    dropDownItems[0] = StorefrontCommonData.getString(activity, R.string.select_text);
                    for (int position = 0; position < item.getDropdown().size() ; position++) {
                        String value = item.getDropdown().get(position).getValue();
                        dropDownItems[position + 1] = value;
                        if (item.getData().toString().trim().equalsIgnoreCase(value.trim()))
                            lastPosition = position + 1;
                    }
                }
                else  if(item.getDataType().equals(Keys.DataType.SINGLE_SELECT) && item.getAllowedvalues() != null){
                    dropDownItems = new String[item.getAllowedvalues().size() + 1];
                    dropDownItems[0] = StorefrontCommonData.getString(activity, R.string.select_text);
                    for (int position = 0; position < item.getAllowedvalues().size() ; position++) {
                        String value = item.getAllowedvalues().get(position).getName();
                        dropDownItems[position + 1] = value;
                        if (item.getData().toString().trim().equalsIgnoreCase(value.trim()))
                            lastPosition = position + 1;
                    }

                }

                if(dropDownItems != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.layout_custom_field_drop_down_list_item, dropDownItems);
                    spnCustomFieldValues.setAdapter(adapter);
                    spnCustomFieldValues.setOnItemSelectedListener(this);
                    // Log.i("DropDown", "==" + item.getData().toString());
                    spnCustomFieldValues.setSelection(lastPosition);
                }
                tvLabel.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
                break;
        }


        if (item.isReadOnly()) {
            view.findViewById(R.id.rlParent).setEnabled(false);
        }
        return view;
    }


    @Override
    public void onClick(View view) {
        UIManager.isPickup = true;
        switch (view.getId()) {
            case R.id.rlParent:
                switch (item.getDataType()) {
                    case Keys.DataType.DROP_DOWN:
                    case Keys.DataType.SINGLE_SELECT:
                        spnCustomFieldValues.performClick();
//                        vCustomFieldIcon.setRotation(180);
                        break;
                    case Keys.DataType.CHECKBOX:
                        if (vCustomFieldIcon.getTag().equals(R.drawable.ic_icon_checkbox_ticked)) {
                            vCustomFieldIcon.setTag(R.drawable.ic_icon_checkbox_unticked);
                            vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_checkbox_unticked);
                            tvPlaceHolder.setTextColor(ContextCompat.getColor(context, R.color.colorHint));
                            item.setData("false");

                        } else {
                            vCustomFieldIcon.setTag(R.drawable.ic_icon_checkbox_ticked);
                            vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_checkbox_ticked);
                            tvPlaceHolder.setTextColor(ContextCompat.getColor(context, R.color.primary_text_color));
                            item.setData("true");

                        }
                        break;
                }
                break;
        }
    }

    @Override
    public Object getView() {
        return this;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        if(item.getDataType().equals(Keys.DataType.DROP_DOWN))
            item.setData(position == 0 ? "" : item.getDropdown().get(position - 1).getValue());
        else
            item.setData(position == 0 ? "" : item.getAllowedvalues().get(position - 1).getName());

        tvPlaceHolder.setText(item.getData().toString().isEmpty() ? StorefrontCommonData.getString(activity, R.string.select_text) : item.getData().toString());
//        vCustomFieldIcon.setRotation(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
