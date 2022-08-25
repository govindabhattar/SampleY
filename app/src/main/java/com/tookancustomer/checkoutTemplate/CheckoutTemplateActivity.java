package com.tookancustomer.checkoutTemplate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.BaseActivity;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldCheckListFilterCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldCheckboxCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldDocumentCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldImageCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldTextViewCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsConstants;
import com.tookancustomer.checkoutTemplate.model.Data;
import com.tookancustomer.checkoutTemplate.model.Option;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.PathUtil;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CheckoutTemplateActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llCustomFields, ll_placeHolder;
    private Button btnSubmit;
    private ArrayList<Template> templateDataList;
    private int customFieldPosition;

    private RelativeLayout rlBack;
    private TextView tvHeading;
    private boolean isForDisplay;
    private boolean isCustomOrder = false;
    private String OrderCurrencySymbol = "";
    private boolean isCustomOrderMerchantLevel = false;
    private Datum storefrontData;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_template);

        init();
        setData();

    }

    private void setData() {
        tvHeading.setText(StorefrontCommonData.getTerminology().getCheckoutTemplate());
        btnSubmit.setText(getStrings(R.string.submit));

        if (getIntent().hasExtra(CheckoutTemplateConstants.IS_CUSTOM_ORDER)) {
            isCustomOrder = true;
        }
        isCustomOrderMerchantLevel = (getIntent().getBooleanExtra("isCustomOrderMerchantLevel", false));

        if (getIntent().hasExtra(STOREFRONT_DATA))
            storefrontData = (Datum) getIntent().getSerializableExtra(STOREFRONT_DATA);


        if (getIntent().hasExtra("OrderCurrencySymbol")) {
            OrderCurrencySymbol = getIntent().getStringExtra("OrderCurrencySymbol");
        }

        if (!getIntent().hasExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST)) {
            callbackForCheckoutTemplate();
        } else {
            isForDisplay = getIntent().getBooleanExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, false);

            templateDataList = (ArrayList<Template>) getIntent().getSerializableExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST);

            if (isForDisplay)
                renderCustomFieldsUi(templateDataList);
            else
                createTemplateAccToCurrency();


            if (isForDisplay)
                btnSubmit.setVisibility(View.GONE);
        }
    }

    private void createTemplateAccToCurrency() {

        if (!isCustomOrder && Dependencies.getSelectedProductsArrayList().size() > 0
                && Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings() != null) {
            for (int i = 0; i < templateDataList.size(); i++) {
                if (templateDataList.get(i).getOption() != null && templateDataList.get(i).getOption().size() > 0) {
                    int k = 0;
                    for (Iterator<Option> j = templateDataList.get(i).getOption().iterator(); j.hasNext(); k++) {
                        if (Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getCurrencyId() != j.next().getCurrency_id()) {
                            j.remove();
                            templateDataList.get(i).getAllowedValues().remove(k--);
                        }
                    }
                }

            }
        } else if (isCustomOrder && !isCustomOrderMerchantLevel) {
            OrderCurrencySymbol = Utils.getCurrencySymbol();

            if (StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1)
                for (int i = 0; i < templateDataList.size(); i++) {
                    if (templateDataList.get(i).getOption() != null && templateDataList.get(i).getOption().size() > 0) {
                        int k = 0;
                        for (Iterator<Option> j = templateDataList.get(i).getOption().iterator(); j.hasNext(); k++) {
                            if (!Utils.getCurrencyId().isEmpty() && Integer.parseInt(Utils.getCurrencyId()) != j.next().getCurrency_id()) {
                                j.remove();
                                templateDataList.get(i).getAllowedValues().remove(k--);
                            }
                        }
                    }

                }

        } else if (isCustomOrder && isCustomOrderMerchantLevel && storefrontData != null) {

            if (storefrontData.getPaymentSettings() != null && storefrontData.getPaymentSettings().getSymbol() != null) {
                OrderCurrencySymbol = storefrontData.getPaymentSettings().getSymbol();
                for (int i = 0; i < templateDataList.size(); i++) {
                    if (templateDataList.get(i).getOption() != null && templateDataList.get(i).getOption().size() > 0) {
                        int k = 0;
                        for (Iterator<Option> j = templateDataList.get(i).getOption().iterator(); j.hasNext(); k++) {
                            if (storefrontData.getPaymentSettings().getCurrencyId() != j.next().getCurrency_id()) {
                                j.remove();
                                templateDataList.get(i).getAllowedValues().remove(k--);
                            }
                        }
                    }
                }
            }
        }

        renderCustomFieldsUi(templateDataList);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Codes.Request.OPEN_CAMERA_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {

                    CustomFieldImageCheckout cFIVCamera = getCustomFieldImageView();
                    if (cFIVCamera != null) {
                        cFIVCamera.compressAndSaveImageBitmap();
                    }
                }
                break;
            case Codes.Request.OPEN_GALLERY_CUSTOM_FIELD_IMAGE:
                if (resultCode == RESULT_OK) {
                    CustomFieldImageCheckout cFIMVGallery = getCustomFieldImageView();
                    if (cFIMVGallery != null) {
                        try {
                            cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                            cFIMVGallery.compressAndSaveImageBitmap();
                        } catch (IOException e) {

                            Utils.printStackTrace(e);
                            Utils.toast(mActivity, getStrings(R.string.could_not_read_image));
                        }
                    }

                }
                break;
            case Codes.Request.OPEN_STORAGE_DOCUMENT:
                if (resultCode == RESULT_OK) {
                    CustomFieldDocumentCheckout cFIMVGallery = getCustomFieldDocumentView();
                    if (cFIMVGallery != null) {
                        try {
                            Log.e("doc", "doc");
                            Uri selectedFileURI = data.getData();
                            String fullPath = PathUtil.getPath(this, selectedFileURI);
                            File file = new File(fullPath);

//                            cFIMVGallery.imageUtils.copyFileFromUri(data.getData());
                            cFIMVGallery.uploadCustomFieldImage(file);
                        } catch (Exception e) {

                            Utils.printStackTrace(e);
                            Utils.toast(mActivity, getStrings(R.string.could_not_read_file));
                        }
                    }

                }
                break;
        }
    }


    private void init() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        llCustomFields = findViewById(R.id.llCustomFields);
        btnSubmit = findViewById(R.id.btnSubmit);

        ll_placeHolder = findViewById(R.id.ll_placeHolder);

        Utils.setOnClickListener(this, btnSubmit, rlBack);
    }


    private void renderCustomFieldsUi(ArrayList<Template> templateDataList) {
        llCustomFields.removeAllViews();
        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);
            switch (template.getDataType()) {

                case CustomViewsConstants.CHECKBOX:
                case CustomViewsConstants.SINGLE_SELCT:
                case CustomViewsConstants.SINGLE_SELECT_DELIVERY:
                    template.setAllowedValuesWithIsSelected();
                    CustomFieldCheckboxCheckout checkboxFreelancer = new CustomFieldCheckboxCheckout(mActivity, isForDisplay);
                    if (template.getAllowedValues().size() > 0) {
                        if (!OrderCurrencySymbol.isEmpty())
                            llCustomFields.addView(checkboxFreelancer.render(template, null, OrderCurrencySymbol));
                        else
                            llCustomFields.addView(checkboxFreelancer.render(template));
                    }
                    break;
                case CustomViewsConstants.NUMBER:
                case CustomViewsConstants.EMAIL:
                case CustomViewsConstants.TELEPHONE:
                case CustomViewsConstants.URL:
                case CustomViewsConstants.DATE:
                case CustomViewsConstants.DATE_FUTURE:
                case CustomViewsConstants.DATE_PAST:
                case CustomViewsConstants.DATE_TODAY:
                case CustomViewsConstants.DATETIME:
                case CustomViewsConstants.DATETIME_FUTURE:
                case CustomViewsConstants.DATETIME_PAST:
                case CustomViewsConstants.TEXT:
                    Log.e(i + "=====" + template.getDataType(), template.getData().toString());
                    CustomFieldTextViewCheckout editTextFreelancer = new CustomFieldTextViewCheckout(mActivity, isForDisplay);
                    llCustomFields.addView(editTextFreelancer.render(template));
                    break;
                case CustomViewsConstants.MULTI_SELECT:
                    template.setAllowedValuesWithIsSelected();
                    CustomFieldCheckListFilterCheckout customFieldCheckListFilter = new CustomFieldCheckListFilterCheckout(mActivity);
                    if (template.getAllowedValues().size() > 0) {
                        if (!OrderCurrencySymbol.isEmpty())
                            llCustomFields.addView(customFieldCheckListFilter.render(template, isForDisplay, null, OrderCurrencySymbol));
                        else
                            llCustomFields.addView(customFieldCheckListFilter.render(template, isForDisplay));
                    }
                    break;

                case CustomViewsConstants.IMAGE:
                    CustomFieldImageCheckout customFieldImageFreelancer =
                            new CustomFieldImageCheckout(mActivity, false, isForDisplay);
                    llCustomFields.addView(customFieldImageFreelancer.render(template));
                    break;
                case CustomViewsConstants.DOCUMENT:
                    CustomFieldDocumentCheckout customFieldDocumentCheckout =
                            new CustomFieldDocumentCheckout(mActivity, false, isForDisplay);
                    llCustomFields.addView(customFieldDocumentCheckout.render(template));
                    break;

             /*   case CustomViewsConstants.SINGLE_SELECT_DELIVERY:
                    template.setAllowedValuesWithIsSelected();
                    CustomFieldCheckboxCheckout checkboxFreelancer1 = new CustomFieldCheckboxCheckout(mActivity, isForDisplay);
                    if (template.getAllowedValues().size() > 0) {

                            llCustomFields.addView(checkboxFreelancer1.render(template));
                    }
                    break;*/

            }
        }

        dataNotFound();

    }

    private void dataNotFound() {

        int count = llCustomFields.getChildCount();
        if (count > 0) {
            ll_placeHolder.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
            llCustomFields.setVisibility(View.VISIBLE);
        } else {
            nestedScrollView.setVisibility(View.GONE);
            llCustomFields.setVisibility(View.GONE);
            ll_placeHolder.setVisibility(View.VISIBLE);
        }
    }


    public void setCustomFieldPosition(Template item) {
        this.customFieldPosition = templateDataList.indexOf(item);
    }

    /**
     * Method to get the CustomFieldImageViewPickup via Listeners
     *
     * @return
     */
    private CustomFieldImageCheckout getCustomFieldImageView() {
        List<Template> customFieldsList = templateDataList;
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPosition).getView();
        return customFieldView instanceof CustomFieldImageCheckout ? (CustomFieldImageCheckout) customFieldView : null;
    }

    private CustomFieldDocumentCheckout getCustomFieldDocumentView() {
        List<Template> customFieldsList = templateDataList;
        if (customFieldsList == null || customFieldsList.isEmpty()) {
            return null;
        }
        Object customFieldView = customFieldsList.get(customFieldPosition).getView();
        return customFieldView instanceof CustomFieldDocumentCheckout ? (CustomFieldDocumentCheckout) customFieldView : null;
    }

    public void deleteCustomFieldImageSignup(int position) {
        CustomFieldImageCheckout cFIVDeleteImage = getCustomFieldImageView();
        if (cFIVDeleteImage != null) {
            cFIVDeleteImage.deleteImage(position);
        }
    }

    public void deleteCustomFieldDocumentSignup(int position) {
        CustomFieldDocumentCheckout cFIVDeleteDocument = getCustomFieldDocumentView();
        if (cFIVDeleteDocument != null) {
            cFIVDeleteDocument.deleteImage(position);
        }
    }


    private void callbackForCheckoutTemplate() {
        UserData userData = StorefrontCommonData.getUserData();
        CommonParams.Builder builder = new CommonParams.Builder()
                .add("marketplace_user_id", userData.getData().getVendorDetails().getMarketplaceUserId());
        if (isCustomOrder) {
            builder.add("user_id", userData.getData().getVendorDetails().getMarketplaceUserId());
            builder.add("type", 1);
        } else {
            builder.add("user_id", Dependencies.getSelectedProductsArrayList().get(0).getUserId());
            builder.add("type", 0);
        }

        if (Dependencies.getSelectedProductsArrayList().size() > 0) {
            if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getSelectedPickupMode() == Constants.SelectedPickupMode.SELF_PICKUP)
                builder.add("self_pickup", 1);
            else
                builder.add("self_pickup", 0);
        } else {
            builder.add("self_pickup", 0);
        }


        RestClient.getApiInterface(this).getCheckoutTemplate(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, true) {
                    @Override
                    public void success(BaseModel baseModel) {
                        Data checkoutData = baseModel.toResponseModel(Data.class);
                        templateDataList = checkoutData.getTemplate();

                        createTemplateAccToCurrency();
                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {

                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (verifyData()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST, templateDataList);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
                break;

            case R.id.rlBack:
                onBackPressed();
                break;

        }
    }

    private boolean verifyData() {
        boolean isVerified = true;
        String error = "";
        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);

            if (template.getDataType().equals(CustomViewsConstants.CHECKBOX)
                    || template.getDataType().equals(CustomViewsConstants.NUMBER)
                    || template.getDataType().equals(CustomViewsConstants.EMAIL)
                    || template.getDataType().equals(CustomViewsConstants.URL)
                    || template.getDataType().equals(CustomViewsConstants.DATE)
                    || template.getDataType().equals(CustomViewsConstants.DATE_FUTURE)
                    || template.getDataType().equals(CustomViewsConstants.DATE_PAST)
                    || template.getDataType().equals(CustomViewsConstants.DATE_TODAY)
                    || template.getDataType().equals(CustomViewsConstants.DATETIME)
                    || template.getDataType().equals(CustomViewsConstants.DATETIME_FUTURE)
                    || template.getDataType().equals(CustomViewsConstants.DATETIME_PAST)
                    || template.getDataType().equals(CustomViewsConstants.TEXT)) {

                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = getStrings(R.string.enter) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }

                if (template.getDataType().equals(CustomViewsConstants.TELEPHONE)) {
                    Log.e("PHONE======", template.getValue().toString());
                }

            } else if (template.getDataType().equals(CustomViewsConstants.TELEPHONE)) {
                if (template.isRequired() && template.getData().toString().replace(template.getCountryCode().toString(), "").trim().isEmpty()) {
                    isVerified = false;
                    error = getStrings(R.string.enter) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }

                if (template.getDataType().equals(CustomViewsConstants.TELEPHONE)) {
                    Log.e("PHONE======", template.getValue().toString());
                }
            } else if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT)) {
                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = getStrings(R.string.Select_terminology).replace(TERMINOLOGY, template.getDisplayName());
                    break;
                }

            } else if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELECT_DELIVERY)) {
                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = getStrings(R.string.Select_terminology).replace(TERMINOLOGY, template.getDisplayName());
                    break;
                }

            } else if (template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {
                boolean isAnySelected = false;
                double cost = 0;
                ArrayList<String> selectedValues = new ArrayList<>();
                for (int j = 0; j < template.getAllowedValuesWithIsSelected().size(); j++) {
                    if (template.getAllowedValuesWithIsSelected().get(j).isChecked()) {
                        isAnySelected = true;
                        cost += template.getOption().get(j).getCost();
                        selectedValues.add(template.getAllowedValuesWithIsSelected().get(j).getDisplayName());
                    }
                }

                if (template.isRequired()) {
                    if (!isAnySelected) {
                        isVerified = false;
                        error = getStrings(R.string.select_atleast_one) + getStrings(R.string.empty_space) + template.getDisplayName();
                        break;
                    }
                }

//                if (isAnySelected) {
                template.setData(selectedValues);
                template.setCost(cost);
//                }

            } else if (template.getDataType().equals(CustomViewsConstants.IMAGE)) {
                if (template.isRequired() && (template.getData().toString().isEmpty() || template.getData().toString().equals("[]"))) {
                    isVerified = false;
                    error = getStrings(R.string.select_atleast_one_image_for) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }
            } else if (template.getDataType().equals(CustomViewsConstants.DOCUMENT)) {
                if (template.isRequired() && (template.getData().toString().isEmpty() || template.getData().toString().equals("[]"))) {
                    isVerified = false;
                    error = getStrings(R.string.select_atleast_one_document_for) + getStrings(R.string.empty_space) + template.getDisplayName();
                    break;
                }
            }


        }
        if (!isVerified)
            Utils.snackBar(mActivity, error);
        return isVerified;
    }


}
