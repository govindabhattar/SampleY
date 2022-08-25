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
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.dialog.AlertDialogSignUp;
import com.tookancustomer.models.ProductCatalogueData.CustomField;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.UIManager;

import java.util.ArrayList;


public class CustomFieldTextViewPDP {

    private final String TAG = CustomFieldTextViewPDP.class.getSimpleName();
    private View view;
    private TextView tvCustomFieldLabel, tvCustomFieldValue;
    Context context;
    private CustomField item;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldTextViewPDP(Context context, String dataType) {
        this.context = context;
        if (dataType.equals(Keys.DataType.MULTI_SELECT) || dataType.equals(Keys.DataType.TEXT) || dataType.equals(Keys.DataType.NUMBER)) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_field_pdp_multiselect, null);
        } else {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_field_pdp_text, null);
        }
        tvCustomFieldLabel = view.findViewById(R.id.tvCustomFieldLabel);
        tvCustomFieldValue = view.findViewById(R.id.tvCustomFieldValue);
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(final Activity activity, final CustomField item) {
        this.item = item;

        switch (item.getDataType()) {
            case Keys.DataType.MULTI_SELECT:
                ArrayList<String> multiSelectList = new ArrayList<>();
                try {
                    multiSelectList = ((ArrayList<String>) item.getValue());
                } catch (Exception e) {
                    multiSelectList = new ArrayList<>();
                }
                String value = "";
                if (multiSelectList != null)
                    for (int i = 0; i < multiSelectList.size(); i++) {
                        value = value + multiSelectList.get(i) + "\t\t";
                    }
                tvCustomFieldValue.setText(value);
                break;

            case Keys.DataType.NUMBER:
                tvCustomFieldValue.setText(item.getValue() + "");
                break;

            case Keys.DataType.TIME:
                tvCustomFieldValue.setText(DateUtils.getInstance().parseDateAs(item.getValue().toString(),   UIManager.getTimeFormat()));
                break;

            case Keys.DataType.DATE:
            case Keys.DataType.DATE_FUTURE:
            case Keys.DataType.DATE_PAST:
            case Keys.DataType.DATE_TODAY:
                tvCustomFieldValue.setText(DateUtils.getInstance().parseDateAs(item.getValue().toString(), Constants.DateFormat.ONLY_DATE));
                break;

            case Keys.DataType.DATETIME:
            case Keys.DataType.DATETIME_FUTURE:
            case Keys.DataType.DATETIME_PAST:
                tvCustomFieldValue.setText(DateUtils.getInstance().parseDateAs(item.getValue().toString(), UIManager.getDateTimeFormat()));
                break;


            case Keys.DataType.TEXT_AREA:
                String text = StorefrontCommonData.getString(activity,R.string.read);
                final SpannableString contentSpannable = new SpannableString(text);
                contentSpannable.setSpan(new UnderlineSpan(), 0, text.length(), 0);
                tvCustomFieldValue.setText(contentSpannable);
                tvCustomFieldValue.setTextColor(context.getResources().getColor(R.color.colorAccent));

                tvCustomFieldValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialogSignUp.Builder(activity)
                                .title(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase())
                                .message(item.getValue() + "")
//                            .button("Close")
                                .listener(new AlertDialogSignUp.Listener() {
                                    @Override
                                    public void performPostAlertAction(int purpose, Bundle backpack) {


                                    }
                                })
                                .build().show();
                    }
                });

                break;
            default:
                tvCustomFieldValue.setText(item.getValue() + "");
                break;
        }
        tvCustomFieldLabel.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());


        return view;
    }

}
