package com.tookancustomer.questionnaireTemplate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.BaseActivity;
import com.tookancustomer.DatesOnCalendarActivity;
import com.tookancustomer.R;
import com.tookancustomer.ScheduleTimeActivity;
import com.tookancustomer.agentListing.AgentListingActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.checkoutTemplate.constant.CheckoutTemplateConstants;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldCheckListFilterCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldCheckboxCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldDocumentCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldImageCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomFieldTextViewCheckout;
import com.tookancustomer.checkoutTemplate.customViews.CustomViewsConstants;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.questionnaireTemplate.model.QuestionnaireTemplate;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.PathUtil;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QuestionnaireTemplateActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private LinearLayout llCustomFields;
    private Button btnSubmit;
    private ArrayList<Template> templateDataList;
    private int[] selectedQuantity;
    private int customFieldPosition;

    private RelativeLayout rlBack;
    private TextView tvHeading;
    private boolean isForDisplay;
    private boolean isCustomOrder = false;
    private int itemPos;
    private Datum productDataItem;
    private String startDate;
    private ItemSelected itemSelected;
    private boolean updateQuestionnaire;
    private boolean isSERVICE_AS_PRODUCT;
    private double latitude, longitude;
    private String questionnaireCurrencySymbol = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_template);
        init();
        getData();
        setData();


    }

    private void getData() {
        if (getIntent().hasExtra(KEY_ITEM_POSITION)) {
            itemPos = getIntent().getIntExtra(KEY_ITEM_POSITION, 0);
        }
        if (getIntent().hasExtra(PRODUCT_CATALOGUE_DATA)) {
            productDataItem = (Datum) getIntent().getSerializableExtra(PRODUCT_CATALOGUE_DATA);
        }
        if (getIntent().hasExtra(UPDATE_QUESTIONNAIRE)) {
            updateQuestionnaire = getIntent().getBooleanExtra(UPDATE_QUESTIONNAIRE, false);
        }
        if (getIntent().hasExtra("SERVICE_AS_PRODUCT")) {
            isSERVICE_AS_PRODUCT = getIntent().getBooleanExtra("SERVICE_AS_PRODUCT", false);
        }
        if (getIntent().hasExtra("selectedQuantity")) {
            selectedQuantity = getIntent().getIntArrayExtra("selectedQuantity");
        }
        if (getIntent().hasExtra("questionnaireCurrencySymbol")) {
            questionnaireCurrencySymbol = getIntent().getStringExtra("questionnaireCurrencySymbol");
        }
        if (getIntent().hasExtra(PICKUP_LATITUDE) && getIntent().hasExtra(PICKUP_LONGITUDE)) {
            latitude = getIntent().getExtras().getDouble(PICKUP_LATITUDE);
            longitude = getIntent().getExtras().getDouble(PICKUP_LONGITUDE);
        }

    }

    private void setData() {
        if (productDataItem != null)
            tvHeading.setText(productDataItem.getName());
        else {
            if (getIntent().getStringExtra("product_title") != null)
                tvHeading.setText(getIntent().getStringExtra("product_title"));
        }
        btnSubmit.setText(getStrings(R.string.submit));

        if (getIntent().hasExtra(CheckoutTemplateConstants.IS_CUSTOM_ORDER)) {
            isCustomOrder = true;
        }

//        callbackForCheckoutTemplate();
//
        if (!getIntent().hasExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST)) {
            callbackForCheckoutTemplate();
        } else {
            isForDisplay = getIntent().getBooleanExtra(CheckoutTemplateConstants.EXTRA_BOOLEAN_FOR_DISPLAY, false);
            templateDataList = (ArrayList<Template>) getIntent().getSerializableExtra(CheckoutTemplateConstants.EXTRA_TEMPLATE_LIST);
            renderCustomFieldsUi(templateDataList);

            if (isForDisplay)
                btnSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void calculateCost(Datum item) {
        productDataItem.setQuestionnaireTemplate(templateDataList);
        productDataItem.setQuestionnaireTemplateCost(getCustomTemplateCost(templateDataList));
    }

    private double getCustomTemplateCost(ArrayList<Template> templateDataList) {
        double cost = 0;
        try {
            for (int i = 0; i < templateDataList.size(); i++) {
                Template template = templateDataList.get(i);
                if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT)
                        || template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {

                    cost += template.getCost();
                }
            }
        } catch (Exception e) {
        }
        return cost;
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
            case Codes.Request.OPEN_AGENT_LIST_ACTIVITY:

                if (resultCode == RESULT_OK) {
                    com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;

                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                    }


                    if (data.hasExtra(KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                    returnIntent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                    setResult(RESULT_OK, returnIntent);
                    onBackPressed();
                }
                break;
            case Codes.Request.OPEN_SCHEDULE_TIME_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    com.tookancustomer.models.ProductCatalogueData.Datum productDataItem = null;
                    if (data.hasExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA)) {
                        productDataItem = (com.tookancustomer.models.ProductCatalogueData.Datum) data.getSerializableExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA);
                        productDataItem.setSelectedQuantity(productDataItem.getSelectedQuantity() + 1);
                        Dependencies.addCartItem(mActivity, productDataItem);
                    }
                    if (data.hasExtra(KEY_ITEM_POSITION)) {
                        itemPos = data.getIntExtra(KEY_ITEM_POSITION, 0);
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
                    returnIntent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
                    setResult(RESULT_OK, returnIntent);
                    onBackPressed();
                }
                break;
        }
    }


    private void init() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        llCustomFields = findViewById(R.id.llCustomFields);
        btnSubmit = findViewById(R.id.btnSubmit);
        Utils.setOnClickListener(this, btnSubmit, rlBack);
    }


    private void renderCustomFieldsUi(ArrayList<Template> templateDataList) {
        llCustomFields.removeAllViews();
        for (int i = 0; i < templateDataList.size(); i++) {
            Template template = templateDataList.get(i);
            switch (template.getDataType()) {

                case CustomViewsConstants.SINGLE_SELCT:
                    template.setAllowedValuesWithIsSelected();
                case CustomViewsConstants.CHECKBOX:
                    Log.e(i + "=====" + template.getDataType(), template.getData().toString());
                    CustomFieldCheckboxCheckout checkboxFreelancer = new CustomFieldCheckboxCheckout(mActivity, isForDisplay);

                    if (isForDisplay && !questionnaireCurrencySymbol.isEmpty()) {
                        llCustomFields.addView(checkboxFreelancer.render(template, null, questionnaireCurrencySymbol));

                    } else
                        llCustomFields.addView(checkboxFreelancer.render(template, productDataItem));
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
                    Log.e(i + "=====" + template.getDataType(), template.getData().toString());
                    template.setAllowedValuesWithIsSelected();
                    CustomFieldCheckListFilterCheckout customFieldCheckListFilter = new CustomFieldCheckListFilterCheckout(mActivity);
                    if (isForDisplay && !questionnaireCurrencySymbol.isEmpty()) {
                        llCustomFields.addView(customFieldCheckListFilter.render(template, isForDisplay, null, questionnaireCurrencySymbol));
                    } else
                        llCustomFields.addView(customFieldCheckListFilter.render(template, isForDisplay, productDataItem));

                    break;

                case CustomViewsConstants.IMAGE:
                    Log.e(i + "=====" + template.getDataType(), template.getData().toString());
                    CustomFieldImageCheckout customFieldImageFreelancer =
                            new CustomFieldImageCheckout(mActivity, false, isForDisplay);
                    llCustomFields.addView(customFieldImageFreelancer.render(template));
                    break;
                case CustomViewsConstants.DOCUMENT:
                    Log.e(i + "=====" + template.getDataType(), template.getData().toString());
                    CustomFieldDocumentCheckout customFieldDocumentCheckout =
                            new CustomFieldDocumentCheckout(mActivity, false, isForDisplay);
                    llCustomFields.addView(customFieldDocumentCheckout.render(template));
                    break;

            }
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
        builder.add("user_id", productDataItem.getStorefrontData().getStorefrontUserId());

        builder.add("vendor_id", productDataItem.getVendorId());
        builder.add("product_id", productDataItem.getProductId());

        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            builder.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));


        }

        RestClient.getApiInterface(this).getProductTemplate(builder.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(this, true, false) {
                    @Override
                    public void success(BaseModel baseModel) {

                        ArrayList<QuestionnaireTemplate> questionnaireTemplates = new Gson().fromJson(new Gson().toJson(baseModel.data),
                                new TypeToken<ArrayList<QuestionnaireTemplate>>() {
                                }.getType());
                        templateDataList = questionnaireTemplates.get(0).getTemplate();

                        renderCustomFieldsUi(templateDataList);

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
                    calculateCost(productDataItem);
                    if (productDataItem.getSelectedQuantity() == 0 &&
                            productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                            (productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                    || productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.APPOINTMENT)) {


                        if (productDataItem.getStorefrontData().getPdOrAppointment() == Constants.ServiceFlow.PICKUP_DELIVERY
                                && (Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.FIXED)
                                && productDataItem.getEnableTookanAgent() == 0) {
                            openDatePicker();

                        } else if (productDataItem.getIsAgentsOnProductTagsEnabled() == 1) {
                            setAgentList(itemPos);
                        } else {
                            if (productDataItem.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE
                                    && (Constants.ProductsUnitType.getUnitType(productDataItem.getUnitType()) == Constants.ProductsUnitType.PER_DAY)
                                    && UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
                                Intent intent = new Intent(mActivity, DatesOnCalendarActivity.class);
                                intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                                intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                                intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                                intent.putExtra(Keys.Extras.IS_START_TIME, true);
                                intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                            } else {
                                Intent intent = new Intent(mActivity, ScheduleTimeActivity.class);
                                intent.putExtra(Keys.Extras.KEY_ITEM_POSITION, itemPos);
                                intent.putExtra(Keys.Extras.PRODUCT_CATALOGUE_DATA, productDataItem);
                                intent.putExtra(Keys.Extras.IS_SCHEDULING_FROM_CHECKOUT, false);
                                intent.putExtra(Keys.Extras.IS_START_TIME, true);
                                intent.putExtra(Keys.Extras.SELECTED_DATE, "");
                                intent.putExtra("service_time", productDataItem.getServiceTime());
                                intent.putExtra(Keys.Extras.AGENT_ID, productDataItem.getAgentId());
                                startActivityForResult(intent, OPEN_SCHEDULE_TIME_ACTIVITY);
                            }

                        }
                    } else {
                        addToCart();
                    }

                }
                break;

            case R.id.rlBack:
                onBackPressed();
                break;

        }
    }


    public void setAgentList(int itemPos) {
        Intent intent = new Intent(mActivity, AgentListingActivity.class);
        intent.putExtra(KEY_ITEM_POSITION, itemPos);
        intent.putExtra(PICKUP_LATITUDE, latitude);
        intent.putExtra(PICKUP_LONGITUDE, longitude);
        intent.putExtra(UPDATE_QUESTIONNAIRE, true);
        intent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
        startActivityForResult(intent, OPEN_AGENT_LIST_ACTIVITY);
    }

    private void addToCart() {
        if (isSERVICE_AS_PRODUCT) {
            if (productDataItem.getMinProductquantity() > 1 &&
                    selectedQuantity[0] < productDataItem.getMinProductquantity()) {
                selectedQuantity[0] = selectedQuantity[0] + productDataItem.getMinProductquantity();
            } else {
                selectedQuantity[0]++;
            }
            productDataItem.setSelectedQuantity(selectedQuantity[0]);
            Dependencies.addCartItem(mActivity, productDataItem);
        }

        Dependencies.addCartItem(mActivity, productDataItem);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_ITEM_POSITION, itemPos);
        returnIntent.putExtra(PRODUCT_CATALOGUE_DATA, productDataItem);
        setResult(RESULT_OK, returnIntent);
        onBackPressed();
    }


    public void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        datePickerFragment.show(getSupportFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        if (datePicker.isShown()) {
            openTimePicker();
        }
    }

    public void openTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (isValidTime(startDate + " " + hourOfDay + ":" + minute)) {
            startDate = startDate + " " + hourOfDay + ":" + minute;
            Date productStartDate = DateUtils.getInstance().getDateFromString(startDate, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC);

            productDataItem.setProductStartDate(productStartDate);
            productDataItem.setProductEndDate(productStartDate);

            addToCart();

        } else {
            Utils.snackBar(mActivity, getStrings(R.string.invalid_selected_date));
        }
    }

    private boolean isValidTime(String date) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();

            if (DateUtils.getInstance().getDateFromString(date, Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC).getTime() < calendar.getTime().getTime())
                isValidDate = false;
        }
        return isValidDate;
    }

    private boolean verifyData() {
        boolean isVerified = true;
        String error = "";
        try {
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
        } catch (Exception e) {
            return true;
        }
    }

}
