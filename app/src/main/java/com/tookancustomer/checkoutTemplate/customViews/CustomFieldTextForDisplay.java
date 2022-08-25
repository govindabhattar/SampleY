//package com.tookancustomer.checkoutTemplate.customViews;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//import com.tookancustomer.R;
//import com.tookancustomer.utility.DateUtils;
//import com.tookancustomer.utility.Utils;
//
//import java.util.ArrayList;
//
//import yelo_freelancer.constants.TemplateConstants;
//import yelo_freelancer.model.projectTemplateData.Template;
//
//public class CustomFieldTextForDisplay {
//    private Activity mActivity;
//    private View view;
//    private TextView tvLabel, tvValue, tvValue1;
//
//    public CustomFieldTextForDisplay(Activity mActivity) {
//        this.mActivity = mActivity;
//        view = LayoutInflater.from(mActivity).inflate(R.layout.layout_text_custom_field_details, null);
//        initViews(view);
//    }
//
//    private void initViews(View view) {
//        tvLabel = view.findViewById(R.id.tvLabel);
//        tvValue = view.findViewById(R.id.tvValue);
//        tvValue1 = view.findViewById(R.id.tvValue1);
//    }
//
//    public View render(Template template) {
//        tvLabel.setText(template.getDisplayName() + " :");
//        setDataOnViews(template);
//        return view;
//    }
//
//    private void setDataOnViews(Template template) {
//        if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT) ||
//                template.getDataType().equals(CustomViewsConstants.TEXT) ||
//                template.getDataType().equals(CustomViewsConstants.NUMBER) ||
//                template.getDataType().equals(CustomViewsConstants.EMAIL) ||
//                template.getDataType().equals(CustomViewsConstants.TELEPHONE) ||
//                template.getDataType().equals(CustomViewsConstants.URL)) {
//            if (template.getValue().toString().length() > 100) {
//                showNextLine(true, template.getValue().toString());
//            } else {
//                if (template.getLabel().equals(TemplateConstants.labelBudget)) {
//                    showNextLine(false, Utils.getCurrencySymbol() + template.getValue().toString());
//                } else
//                    showNextLine(false, template.getValue().toString());
//            }
//        } else if (template.getDataType().equals(CustomViewsConstants.DATE) ||
//                template.getDataType().equals(CustomViewsConstants.DATE_FUTURE) ||
//                template.getDataType().equals(CustomViewsConstants.DATE_PAST) ||
//                template.getDataType().equals(CustomViewsConstants.DATE_TODAY) ||
//                template.getDataType().equals(CustomViewsConstants.DATETIME) ||
//                template.getDataType().equals(CustomViewsConstants.DATETIME_FUTURE) ||
//                template.getDataType().equals(CustomViewsConstants.DATETIME_PAST)) {
//
//            tvValue.setText(DateUtils.getInstance().convertToLocal(template.getValue().toString()));
//
//        } else if (template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {
//            if (!template.getData().toString().isEmpty() && template.getData() instanceof ArrayList) {
//                String value = "";
//                ArrayList<String> selectedValues = (ArrayList<String>) template.getData();
//                for (int i = 0; i < selectedValues.size(); i++) {
//                    if (i == 0)
//                        value = value + selectedValues.get(i);
//                    else
//                        value = value + ", " + selectedValues.get(i);
//                }
//                value = value + ".";
//
//                if (value.length() > 100) {
//                    showNextLine(true, value);
//                } else {
//                    showNextLine(false, value);
//                }
//            }
//        }
//    }
//
//
//    private void showNextLine(boolean isNextLine, String value) {
//        if (isNextLine) {
//            tvValue.setVisibility(View.GONE);
//            tvValue1.setVisibility(View.VISIBLE);
//            tvValue1.setText(value);
//        } else {
//            tvValue.setVisibility(View.VISIBLE);
//            tvValue1.setVisibility(View.GONE);
//            tvValue.setText(value);
//        }
//
//
//    }
//
//}
