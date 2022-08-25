package com.tookancustomer.checkoutTemplate.customViews;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.adapters.SingleSelectAdapter;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.utility.Utils;


/**
 * Created by Mohit Kr. Dhiman on 02/12/16.
 */

public class CustomFieldCheckboxCheckout implements SignupTemplateData.Listener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = CustomFieldCheckboxCheckout.class.getSimpleName();
    private final Activity activity;
    View vwTop, vwBottom;
    //    private Spinner spnCustomFieldValues;
    private RecyclerView rvCheckList;
    private View view;
    private TextView tvLabel;
    private TextView tvPlaceHolder;
    private ImageView vCustomFieldIcon, vCustomFieldIconEnd;
    private Context context;
    private int lastPosition = 0;

    private Template templateData;
    private boolean isForDisplay;
    private SingleSelectAdapter filterAdapter;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldCheckboxCheckout(Context context, boolean isForDisplay) {
        this.context = context;
        this.isForDisplay = isForDisplay;
        if ((activity = (Activity) context) == null) return;

        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_custom_field_checkbox, null);

        tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        vwTop = (View) view.findViewById(R.id.vwTop);
        vwBottom = (View) view.findViewById(R.id.vwBottom);
        vwBottom.setVisibility(View.VISIBLE);
        vwTop.setVisibility(View.GONE);
        tvPlaceHolder = (TextView) view.findViewById(R.id.tvPlaceHolder);
        vCustomFieldIcon = (ImageView) view.findViewById(R.id.vCustomFieldIcon);
        vCustomFieldIconEnd = (ImageView) view.findViewById(R.id.vCustomFieldIconEnd);
        vCustomFieldIconEnd.setVisibility(View.GONE);
//        spnCustomFieldValues = (Spinner) view.findViewById(R.id.spnCustomFieldValues);


        rvCheckList = view.findViewById(R.id.rvCheckList);
        rvCheckList.setVisibility(View.VISIBLE);
        rvCheckList.setNestedScrollingEnabled(false);
        rvCheckList.setLayoutManager(new LinearLayoutManager(activity));


        Utils.setOnClickListener(this, view.findViewById(R.id.rlParent));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlParent:
                if (!isForDisplay)
                    switch (templateData.getDataType()) {
//                        case Keys.DataType.DROP_DOWN:
////                            spnCustomFieldValues.performClick();
////                        vCustomFieldIcon.setRotation(180);
//                            break;
//                        case CustomViewsConstants.SINGLE_SELCT:
////                            spnCustomFieldValues.performClick();
////                        vCustomFieldIcon.setRotation(180);
//                            break;
                        case Keys.DataType.CHECKBOX:
                            if (vCustomFieldIcon.getTag().equals(R.drawable.ic_filter_radio_on_btn)) {
                                vCustomFieldIcon.setTag(R.drawable.ic_option_empty);
                                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_option_empty);
                                tvPlaceHolder.setTextColor(ContextCompat.getColor(context, R.color.black_80));
                                templateData.setData("false");

                            } else {
                                vCustomFieldIcon.setTag(R.drawable.ic_filter_radio_on_btn);
                                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_filter_radio_on_btn);
                                tvPlaceHolder.setTextColor(ContextCompat.getColor(context, R.color.black_80));
                                templateData.setData("true");

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


//        vCustomFieldIcon.setRotation(0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public View render(Template template) {
        return render(template, null, "");
    }

    public View render(Template template, Datum productDataItem) {
        return render(template, productDataItem, "");
    }

    public View render(Template template, Datum productDataItem, String questionnaireCurrencySymbol) {
        this.templateData = template;
        if (isForDisplay)
            view.findViewById(R.id.rlParent).setClickable(false);

        switch (templateData.getDataType()) {
            case Keys.DataType.CHECKBOX:
                boolean isChecked = templateData.getData().toString().equalsIgnoreCase("true");
                vCustomFieldIcon.setTag(isChecked ? R.drawable.ic_filter_radio_on_btn : R.drawable.ic_option_empty);
                vCustomFieldIcon.setBackgroundResource(isChecked ? R.drawable.ic_filter_radio_on_btn : R.drawable.ic_option_empty);
                if (!isForDisplay)
                    tvPlaceHolder.setTextColor(ContextCompat.getColor(context, isChecked ? R.color.black_80 : R.color.colorHint));
                else
                    tvPlaceHolder.setTextColor(ContextCompat.getColor(context, R.color.black_80));
                tvPlaceHolder.setText(templateData.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase()
                        + templateData.getDisplayName().replace("_", " ").substring(1).toLowerCase());
                tvPlaceHolder.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_normal));

                tvLabel.setVisibility(View.GONE);
                templateData.setData(String.valueOf(isChecked));
                break;
            case CustomViewsConstants.SINGLE_SELCT:
            case CustomViewsConstants.SINGLE_SELECT_DELIVERY:

                if (productDataItem != null)
                    filterAdapter = new SingleSelectAdapter(template.getAllowedValuesWithIsSelected(), isForDisplay, activity, productDataItem);
                else if (!questionnaireCurrencySymbol.isEmpty())
                    filterAdapter = new SingleSelectAdapter(template.getAllowedValuesWithIsSelected(), isForDisplay, activity, questionnaireCurrencySymbol);
                else
                    filterAdapter = new SingleSelectAdapter(template.getAllowedValuesWithIsSelected(), isForDisplay, activity);
                rvCheckList.setAdapter(filterAdapter);
                filterAdapter.onclickListner(new SingleSelectAdapter.onclickSingleSelect() {
                    @Override
                    public void onclick(int position) {
                        if (templateData.getAllowedValuesWithIsSelected().get(position).isChecked()) {
                            templateData.setCost(templateData.getOption().get(position).getCost());
                            templateData.setData(templateData.getAllowedValues().get(position));
                        } else {
                            templateData.setCost(0.0);
                            templateData.setData("");

                        }


                    }
                });
                tvPlaceHolder.setVisibility(View.GONE);
                view.findViewById(R.id.rlParent).setVisibility(View.GONE);
                tvLabel.setText(templateData.getDisplayName());
                break;

        }


        if (templateData.isRequired()) {
            tvLabel.setText(CustomViewsUtil.createSpan(activity, templateData.getDisplayName(), "*"));
        } else {
            tvLabel.setText(templateData.getDisplayName());
        }


        return view;

    }

}
