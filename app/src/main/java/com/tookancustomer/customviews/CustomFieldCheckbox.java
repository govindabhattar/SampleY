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

import com.tookancustomer.CheckOutActivityOld;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.userdata.Dropdown;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.util.List;

/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomFieldCheckbox implements Item.Listener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = CustomFieldCheckbox.class.getSimpleName();
    private Spinner spnCustomFieldValues;
    private View view;
    private TextView tvLabel;
    private TextView tvPlaceHolder;
    private ImageView vCustomFieldIcon;
    private final CheckOutActivityOld activity;
    private Context context;
    private Item item;
    private int lastPosition = 0;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldCheckbox(Context context) {
        this.context = context;
        if ((activity = (CheckOutActivityOld) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_checkbox, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        tvPlaceHolder = view.findViewById(R.id.tvPlaceHolder);
        vCustomFieldIcon = view.findViewById(R.id.vCustomFieldIcon);
        spnCustomFieldValues = view.findViewById(R.id.spnCustomFieldValues);
        spnCustomFieldValues.setVisibility(View.VISIBLE);
        Utils.setOnClickListener(this, view.findViewById(R.id.rlParent));
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(Item item) {
        this.item = item;
        this.item.setListener(this);
        switch (item.getDataType()) {
            case Keys.DataType.CHECKBOX:
                boolean isChecked = item.getData().toString().equalsIgnoreCase("true");
                vCustomFieldIcon.setTag(isChecked ? R.drawable.ic_icon_checkbox_ticked : R.drawable.ic_icon_checkbox_unticked);
                vCustomFieldIcon.setBackgroundResource(isChecked ? R.drawable.ic_icon_checkbox_ticked : R.drawable.ic_icon_checkbox_unticked);
                tvPlaceHolder.setTextColor(ContextCompat.getColor(context, isChecked ? R.color.primary_text_color : R.color.colorHint));
                tvPlaceHolder.setText(item.getDisplayName());
                tvPlaceHolder.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));

                tvLabel.setVisibility(View.GONE);
                break;
            case Keys.DataType.DROP_DOWN:
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_dropdown_closed);
                List<Dropdown> dropdownList = item.getDropdown();

                if (dropdownList != null) {

                    int size = dropdownList.size();

                    String[] dropDownItems = new String[size + 1];
                    dropDownItems[0] = StorefrontCommonData.getString(activity, R.string.select_text);

                    for (int position = 0; position < size; position++) {
                        String value = dropdownList.get(position).getValue();
                        dropDownItems[position + 1] = value;
                        if (item.getData().toString().trim().equalsIgnoreCase(value.trim()))
                            lastPosition = position + 1;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                            R.layout.layout_custom_field_drop_down_list_item, dropDownItems);
                    spnCustomFieldValues.setAdapter(adapter);
                    spnCustomFieldValues.setOnItemSelectedListener(this);
                    Log.i("DropDown", "==" + item.getData().toString());
                    spnCustomFieldValues.setSelection(lastPosition);
                }
                tvLabel.setText(item.getDisplayName());
                break;
        }


        if (item.isReadOnly()) {
            view.findViewById(R.id.rlParent).setEnabled(false);
        }
        return view;
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.rlParent) {
            switch (item.getDataType()) {
                case Keys.DataType.DROP_DOWN:
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

        }
    }

    @Override
    public Object getView() {
        return this;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        item.setData(position == 0 ? "" : item.getDropdown().get(position - 1).getValue());
        tvPlaceHolder.setText(item.getData().toString().isEmpty() ? StorefrontCommonData.getString(activity, R.string.select_text) : item.getData().toString());
//        vCustomFieldIcon.setRotation(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
