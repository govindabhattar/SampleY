package com.tookancustomer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.tookancustomer.adapters.ImagesAdapter;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customfield.AddProductData;
import com.tookancustomer.customfield.CustomField;
import com.tookancustomer.customfield.CustomFieldCheckbox;
import com.tookancustomer.customfield.CustomFieldTextView;
import com.tookancustomer.customfield.Datum;
import com.tookancustomer.customviews.CustomFieldTextViewPickup;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.dialog.SingleBtnDialog;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.ImageUrl;
import com.tookancustomer.models.ImageUrlPojo;
import com.tookancustomer.models.producttypedata.ProductTypeData;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.MultipartParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;
import com.tookancustomer.utility.imagepicker.ImageChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tookancustomer.customviews.CustomFieldCheckListFilterFreelancer;
import com.tookancustomer.customviews.CustomViewsConstants;




/**
 * Created by neerajwadhwani on 25/07/18.
 */

public class AddProductActivity extends BaseActivity implements ImageChooser.OnImageSelectListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String UNIT_TYPE = "unit_type";
    private static final String UNIT = "unit";
    private static final String INVENTORY = "inventory_enabled";
    private static final String AVAILABILITY = "available_quantity";
    private static final String ENABLE_TOOKAN = "enable_tookan_agent";
    private static final String TIME_SLOT = "time_slot";
    private static final String CUSTOM_FIELD = "custom_fields";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private ProductTypeData productTypeData;
    private Spinner spSelectTemplate;
    private String[] userTypes;
    private String[] priceTypes;
    private ArrayList<Datum> productTypeList;
    private ArrayList<CustomField> templateDataList;
    private ImagesAdapter imagesAdapter;
    private LinearLayout llCustomFields;
    private int customFieldPositionPickup;
    private int barcodeRequestCode;
    private Button btnAddProduct;
    private int customFieldPosition;
    private RecyclerView rvImages;
    private ArrayList<String> imagesList;
    private ImageChooser mImageChooser;
    private ImageView ivAttach;
    private String timeSelectedStart, dateSelectedStart, timeSelectedEnd, dateSelectedEnd;

    private int pos;
    private EditText etPrice, etPriceType, etName, etDescription;
    private RelativeLayout rlBack;
    private TextView tvHeading;
    private Spinner spPriceType;

    private LinearLayout llUnits;
    private MaterialEditText etFilterDate;
    private String checkInDate, checkOutDate = "";
    private SingleBtnDialog calenderDialog;
    private TextView etStartDate, etEndDate;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = -1;
    private int endYear = 0;
    private int endMonth = 0;
    private int endDay = 0;
    private int endHour = 0;
    private int endMinute = -1;
    private boolean isStartDate;
    private JSONObject jsonDate;
    private EditText etUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        init();
        listeners();
        setAdapter();
        getTypeData();
    }

    private void listeners() {
        Utils.setOnClickListener(this, etStartDate, etEndDate, rlBack, btnAddProduct, ivAttach, etFilterDate);
        spPriceType.setOnItemSelectedListener(this);
        spSelectTemplate.setOnItemSelectedListener(this);
    }

    private void init() {
        imagesList = new ArrayList<>();
        mImageChooser = new ImageChooser(this);
        productTypeList = new ArrayList<Datum>();
        templateDataList = new ArrayList<CustomField>();
        spSelectTemplate = findViewById(R.id.spSelectTemplate);
        etFilterDate = findViewById(R.id.etFilterDate);
        spPriceType = findViewById(R.id.spPriceType);
        llUnits = findViewById(R.id.llUnits);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        ivAttach = findViewById(R.id.ivAttach);
        rvImages = findViewById(R.id.rvImages);
        etPrice = findViewById(R.id.etPrice);
        etUnit = findViewById(R.id.etUnit);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText(getStrings(R.string.add_product_text) + " " + StorefrontCommonData.getTerminology().getProduct());
        etName = findViewById(R.id.etProductName);
        etDescription = findViewById(R.id.etDescription);
        llCustomFields = findViewById(R.id.llCustomFields);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setText(getStrings(R.string.add_product_text) + " " + StorefrontCommonData.getTerminology().getProduct());
        rlBack = findViewById(R.id.rlBack);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.spPriceType:
                if (position != 0) {
                    llUnits.setVisibility(View.VISIBLE);
                } else {
                    llUnits.setVisibility(View.GONE);
                }

            case R.id.spSelectTemplate:
                pos = position;
                setCustomFields(position);
                llCustomFields.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etStartDate:
                isStartDate = true;
                openDatePicker();
                break;
            case R.id.etEndDate:
                isStartDate = false;
                openDatePicker();
                break;
            case R.id.rlBack:
                finish();
                break;
            case R.id.btnAddProduct:
                if (validate() && verifyData())
                    addProductApi();
                break;
            case R.id.ivAttach:
                mImageChooser.selectImage();
                break;
            case R.id.etFilterDate:
                if (calenderDialog != null) {
                    calenderDialog.show();
                } else {
                    if (checkInDate != null && checkOutDate != null) {
                        openCalenderDialog(checkInDate, checkOutDate);
                    } else {
                        openCalenderDialog(null, null);
                    }
                }

                calenderDialog.setCanceledOnTouchOutside(false);
                calenderDialog.setCancelable(false);
                calenderDialog.show();
                break;


        }
    }

    public void openDatePicker() {

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setListener(this);
        datePickerFragment.setMinDate(System.currentTimeMillis());
        if (isStartDate) {
            if (year > 0 && month > 0 && day > 0) {
                datePickerFragment.setDay(day);
                datePickerFragment.setMonth(month);
                datePickerFragment.setYear(year);
            }
        } else {
            if (endYear > 0 && endMonth > 0 && endDay > 0) {
                datePickerFragment.setDay(endDay);
                datePickerFragment.setMonth(endMonth);
                datePickerFragment.setYear(endYear);
            }
        }
        datePickerFragment.show(this.getSupportFragmentManager(), TAG);
    }

    public void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        if (isStartDate) {
            if (hour > 0 && minute > 0) {
                timePickerFragment.setHour(hour);
                timePickerFragment.setMinute(minute);
            }
        } else {
            if (endHour > 0 && endMinute > 0) {
                timePickerFragment.setHour(endHour);
                timePickerFragment.setMinute(endMinute);
            }
        }
        timePickerFragment.show(this.getSupportFragmentManager(), TAG);
    }

    public boolean isValidDate(int year, int monthOfYear, int dayOfMonth) {
        boolean isValidDate = true;
        if (UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP_MR1) {
            Calendar calendar = Calendar.getInstance();
            if (year < calendar.get(Calendar.YEAR)) {
                isValidDate = false;
            } else if (year == calendar.get(Calendar.YEAR)) {
                if (monthOfYear < calendar.get(Calendar.MONTH)) {
                    isValidDate = false;
                } else if (monthOfYear == calendar.get(Calendar.MONTH)) {
                    if (dayOfMonth < calendar.get(Calendar.DAY_OF_MONTH)) {
                        isValidDate = false;
                    }
                }
            }
        }
        return isValidDate;
    }

    public void setDateVariables(int year, int monthOfYear, int dayOfMonth) {
        if (isStartDate) {
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;
        } else {
            this.endYear = year;
            this.endMonth = monthOfYear;
            this.endDay = dayOfMonth;
        }
    }

    public void setTimeVariables(int hour, int minute) {
        if (isStartDate) {
            this.hour = hour;
            this.minute = minute;
        } else {
            this.endHour = hour;
            this.endMinute = minute;
        }
    }

    public void setDateTimeText() {
        if (isStartDate) {
            String timeSelectedStart = hour + " " + minute;
            dateSelectedStart = day + "/" + (month + 1) + "/" + year;
            etStartDate.setText(DateUtils.getInstance().parseDateAs(timeSelectedStart + " " + dateSelectedStart, "dd/MM/yyyy HH mm", Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC));
        } else {
            timeSelectedEnd = endHour + " " + endMinute;
            dateSelectedEnd = endDay + "/" + (endMonth + 1) + "/" + endYear;
            etEndDate.setText(DateUtils.getInstance().parseDateAs(dateSelectedEnd + " " + timeSelectedEnd, "dd/MM/yyyy HH mm", Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC));
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        //Check if date is valid in lollipop devices
        if (isValidDate(year, monthOfYear, dayOfMonth)) {
            setDateVariables(year, monthOfYear, dayOfMonth);
            if (datePicker.isShown()) {
                showTimePicker();
            }
        } else {
            new AlertDialog.Builder(this).message(getStrings(R.string.invalid_selected_date)).button(getStrings(R.string.ok_text)).listener(new AlertDialog.Listener() {
                @Override
                public void performPostAlertAction(int purpose, Bundle backpack) {
                    openDatePicker();
                }
            }).build().show();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (isValidTime(hour, minute)) {
            setTimeVariables(hour, minute);
            setDateTimeText();
        } else {
            new AlertDialog.Builder(this).message(getStrings(R.string.invalid_selected_date)).button(getStrings(R.string.ok_text)).listener(new AlertDialog.Listener() {
                @Override
                public void performPostAlertAction(int purpose, Bundle backpack) {
                    showTimePicker();
                }
            }).build().show();
        }
    }

    public boolean isValidTime(int hour, int minute) {
        boolean isValidTime = true;
        Calendar calendar = Calendar.getInstance();
        if (isStartDate) {
            if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
                if (hour < calendar.get(Calendar.HOUR_OF_DAY)) {
                    isValidTime = false;
                } else if (hour == calendar.get(Calendar.HOUR_OF_DAY)) {
                    if (minute < calendar.get(Calendar.MINUTE)) {
                        isValidTime = false;
                    }
                }
            }
        } else {
            if (endYear == calendar.get(Calendar.YEAR) && endMonth == calendar.get(Calendar.MONTH) && endDay == calendar.get(Calendar.DAY_OF_MONTH)) {
                if (hour < calendar.get(Calendar.HOUR_OF_DAY)) {
                    isValidTime = false;
                } else if (hour == calendar.get(Calendar.HOUR_OF_DAY)) {
                    if (minute < calendar.get(Calendar.MINUTE)) {
                        isValidTime = false;
                    }
                }
            }
        }
        return isValidTime;
    }


    private boolean validate() {
        ValidateClass validateClass = new ValidateClass(this);
        if (!validateClass.checkName(etName))
            return false;
        if (etPrice.getText().toString().isEmpty()) {
            validateClass.showError(etPrice, "Price cannot be empty");
            return false;
        }
        return true;
    }

    /**
     * @param startDate startDate if already selected
     * @param endDate   endDate if already selected
     */
    private void openCalenderDialog(String startDate, String endDate) {
        calenderDialog = new SingleBtnDialog(this, startDate, endDate) {

            @Override
            public void onSuccessApply(Date startDate, Date endDate) {
                Log.e("data<><><>", startDate + "  " + endDate);
                String selectStartDate = DateUtils.getInstance().getFormattedDate(startDate, Constants.DateFormat.DATE_FORMAT);
                String selectedEndDate = DateUtils.getInstance().getFormattedDate(endDate, Constants.DateFormat.DATE_FORMAT);
                etFilterDate.setFloatingLabel(MaterialEditText.FLOATING_LABEL_NORMAL);
                etFilterDate.setFloatingLabelText(getStrings(R.string.selected_date));
                etFilterDate.setText(selectStartDate + " - " + selectedEndDate);

            }
        };
    }


    private void setAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ImagesAdapter imagesAdapter = new ImagesAdapter(this, imagesList);
        rvImages.setLayoutManager(manager);
        rvImages.setAdapter(imagesAdapter);
    }

    private void addProductApi() {
        jsonDate = new JSONObject();
        try {
            jsonDate.put(Keys.APIFieldKeys.START_TIME, DateUtils.getInstance().parseDateAs(timeSelectedStart, "HH:mm", Constants.DateFormat.TIME_FORMAT_24_no_seconds));
            jsonDate.put(START_DATE, DateUtils.getInstance().parseDateAs(dateSelectedStart, "dd/MM/yyyy", Constants.DateFormat.ONLY_DATE));
            jsonDate.put(Keys.APIFieldKeys.END_TIME, DateUtils.getInstance().parseDateAs(timeSelectedEnd, "HH:mm", Constants.DateFormat.TIME_FORMAT_24_no_seconds));
            jsonDate.put(END_DATE, DateUtils.getInstance().parseDateAs(dateSelectedEnd, "dd/MM/yyyy", Constants.DateFormat.ONLY_DATE));
        } catch (JSONException e) {

                               Utils.printStackTrace(e);
        }
        JSONArray jArrayDate = new JSONArray();
        jArrayDate.put(jsonDate);
        JSONArray jsonArrayImageList = new JSONArray(imagesList);
        //JSONArray jsonArray=new JSONArray(templateDataList);
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(templateDataList).getAsJsonArray();
        CommonParams.Builder cParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        cParams.add(ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getAppAccessToken());
        cParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getUserId());
        cParams.add(USER_TYPE, 3);
        cParams.add(USER_ID, StorefrontCommonData.getFormSettings().getUserId());
        cParams.add(PRODUCT_TYPE_ID, productTypeList.get(pos).getProductTypeId());
        cParams.add(NAME, etName.getText().toString());
        cParams.add(DESCRIPTION, etDescription.getText().toString());
        cParams.add(PRICE, etPrice.getText().toString());
        cParams.add(UNIT_TYPE, getIdFromName(spPriceType.getSelectedItem().toString()));
        cParams.add(UNIT, etUnit.getText().toString().isEmpty() ? "1" : etUnit.getText().toString());
        cParams.add(INVENTORY, false);
        cParams.add(AVAILABILITY, "0");
        cParams.add(ENABLE_TOOKAN, "0");
        cParams.add(TIME_SLOT, jArrayDate);
        cParams.add(CUSTOM_FIELD, myCustomArray);
        RestClient.getApiInterface(this).createRequest(cParams.build().getMap(), jsonArrayImageList).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                Utils.snackbarSuccess(mActivity, getStrings(R.string.product_added_success));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Transition.startActivity(AddProductActivity.this, ListingActivity.class, null, true);
                    }
                }, 2000);

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });

    }

    private void getTypeData() {
        CommonParams.Builder cParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        cParams.add(ACCESS_TOKEN, StorefrontCommonData.getUserData().getData().getAppAccessToken());
        cParams.add(MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getUserId());
        cParams.add(USER_TYPE, 3);

        RestClient.getApiInterface(this).getProductType(cParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                AddProductData allTaskResponse = new AddProductData();
                try {
                    ArrayList<Datum> mList = new Gson().fromJson(new Gson().toJson(baseModel.data),
                            new TypeToken<ArrayList<Datum>>() {
                            }.getType());
                    allTaskResponse.setData(mList);
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                productTypeList.addAll(allTaskResponse.getData());
                setupDropDown();

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    private void setupDropDown() {
        userTypes = new String[productTypeList.size()];
        priceTypes = new String[]{"FIXED", "MINUTE", "HOUR", "DAY", "MONTH", "YEAR"};
        if (productTypeList != null) {
            for (int i = 0; i < productTypeList.size(); i++) {
                userTypes[i] = productTypeList.get(i).getName();
            }
            final List<String> typesList = new ArrayList<>(Arrays.asList(userTypes));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1, typesList);
            spSelectTemplate.setAdapter(adapter);
        }
        final List<String> priceType = new ArrayList<>(Arrays.asList(priceTypes));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1, priceType);
        spPriceType.setAdapter(adapter);
    }

    void setCustomFields(int position) {
//        for (int i = 0; i < productTypeList.size(); i++) {
        templateDataList = new ArrayList<CustomField>();
        templateDataList.addAll(productTypeList.get(position).getCustomFields());
        renderCustomFieldsUi(templateDataList);
        // }


    }


    private void renderCustomFieldsUi(ArrayList<CustomField> list) {

        llCustomFields.removeAllViews();
        if (list == null) {
            return;
        }
        for (CustomField item : list) {
            if (true) {
                View view = null;
                switch (item.getDataType()) {
                    case Keys.DataType.NUMBER:
                    case Keys.DataType.EMAIL:
                    case Keys.DataType.TELEPHONE:
                    case Keys.DataType.TEXT:
                    case Keys.DataType.URL:
                    case Keys.DataType.DATE:
                    case Keys.DataType.DATE_FUTURE:
                    case Keys.DataType.DATE_PAST:
                    case Keys.DataType.DATE_TODAY:
                    case Keys.DataType.DATETIME:
                    case Keys.DataType.DATETIME_FUTURE:
                    case Keys.DataType.DATETIME_PAST:
                        view = new CustomFieldTextView(this).render(item);
                        break;
                    case Keys.DataType.CHECKBOX:
                    case Keys.DataType.DROP_DOWN:
                        view = new CustomFieldCheckbox(this).render(item);
                        break;
                    case CustomViewsConstants.MULTI_SELECT:
                        item.setAllowedValuesWithIsSelected();
                        view = new CustomFieldCheckListFilterFreelancer(this).render(item);
                        break;

//                    case Keys.DataType.IMAGE:
//                        view = new CustomFieldImagePickup(this).render(item);
//                        break;
                }
                if (view != null) {
                    llCustomFields.addView(view);
                }
            }
        }
    }

    public void setCustomFieldPositionPickup(CustomField item) {
        this.customFieldPositionPickup = templateDataList.indexOf(item);
    }

    public void setCustomFieldPositionDelivery(Item item) {
        this.customFieldPositionPickup = templateDataList.indexOf(item);
    }

    /**
     * Method to set the Listener for a CustomFieldImageView
     *
     * @param barcodeRequestCode
     */
    public void setBarcodeRequestCode(int barcodeRequestCode) {
        this.barcodeRequestCode = barcodeRequestCode;
    }

    /**
     * Method to ask the User to logout
     *
     * @param message
     */
    public void scanBarcodePopup(String message) {
        // Utils.hideSoftKeyboard(this, mStartTimeView);
        new OptionsDialog.Builder(this).message(message).positiveButton(getStrings(R.string.scan)).negativeButton(getStrings(R.string.manually))
                .listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        IntentIntegrator integrator = new IntentIntegrator(mActivity);
                        integrator.setOrientationLocked(false);
                        integrator.initiateScan();
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                        if (UIManager.isPickup) {
                            CustomFieldTextViewPickup cfBarcode = getCustomFieldBarcodePickup();
                            if (cfBarcode != null)
                                cfBarcode.addBarcodeManually();
                        }
//
                    }
                }).build().show();
    }

    private CustomFieldTextViewPickup getCustomFieldBarcodePickup() {
        return null;
    }

    public boolean verifyData() {
        boolean isVerified = true;
        String error = "";
        for (int i = 0; i < templateDataList.size(); i++) {
            CustomField template = templateDataList.get(i);
            if (template.getDataType().equals(CustomViewsConstants.SINGLE_SELCT)) {
                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = "Please select " + template.getDisplayName();
                    break;
                }
            } else if (template.getDataType().equals(CustomViewsConstants.MULTI_SELECT)) {

                boolean isAnySelected = false;
                ArrayList<String> selectedValues = new ArrayList<>();
                for (int j = 0; j < template.getAllowedValuesWithIsSelected().size(); j++) {
                    if (template.getAllowedValuesWithIsSelected().get(j).isChecked()) {
                        isAnySelected = true;
                        selectedValues.add(template.getAllowedValuesWithIsSelected().get(j).getDisplayName());
                    }
                }

                if (!isAnySelected && template.isRequired()) {
                    isVerified = false;
                    error = "Please select atleast one " + template.getDisplayName();
                    break;
                } else {
                    template.setData(selectedValues);
                }


            } else if (template.getDataType().equals(CustomViewsConstants.TELEPHONE)) {
                if (template.isRequired() &&
                        (template.getData().toString().isEmpty() || template.getData().toString().length() <= 5)) {
                    isVerified = false;
                    error = "Please enter " + template.getDisplayName();
                    break;
                }
            } else if (template.getDataType().equals(CustomViewsConstants.NUMBER) ||
                    template.getDataType().equals(CustomViewsConstants.EMAIL) ||
                    template.getDataType().equals(CustomViewsConstants.URL) ||
                    template.getDataType().equals(CustomViewsConstants.TELEPHONE) ||
                    template.getDataType().equals(CustomViewsConstants.DATE) ||
                    template.getDataType().equals(CustomViewsConstants.DATE_FUTURE) ||
                    template.getDataType().equals(CustomViewsConstants.DATE_PAST) ||
                    template.getDataType().equals(CustomViewsConstants.DATE_TODAY) ||
                    template.getDataType().equals(CustomViewsConstants.DATETIME) ||
                    template.getDataType().equals(CustomViewsConstants.DATETIME_FUTURE) ||
                    template.getDataType().equals(CustomViewsConstants.DATETIME_PAST) ||
                    template.getDataType().equals(CustomViewsConstants.NUMBER) ||
                    template.getDataType().equals(CustomViewsConstants.TEXT)) {
                if (template.isRequired() && template.getData().toString().isEmpty()) {
                    isVerified = false;
                    error = "Please enter " + template.getDisplayName();
                    break;
                }
            }  else if (template.getDataType().equals(CustomViewsConstants.IMAGE)) {
                if (template.isRequired() && (template.getData().toString().isEmpty() || template.getData().toString().equals("[]"))) {
                    isVerified = false;
                    error = "Please select atleast one image for " + template.getDisplayName();
                    break;
                }
            }

        }
        if (!isVerified)
            Utils.snackBar(this, error);
        Log.e("ERROR===", error);
        return isVerified;
    }

    public void recreateCustomFields() {

        for (int i = 0; i < templateDataList.size(); i++) {
            CustomField template = templateDataList.get(i);
            template.setData(null);

        }
        renderCustomFieldsUi(templateDataList);
    }

    public void setCustomFieldPosition(CustomField item) {
        this.customFieldPosition = templateDataList.indexOf(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Picker.PICK_IMAGE_DEVICE:
                mImageChooser.onActivityResult(requestCode, resultCode, data);
                break;
            case Picker.PICK_IMAGE_CAMERA:
                mImageChooser.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    @Override
    public void loadImage(List<ChosenImage> list) {
        uploadImage(list.get(0).getThumbnailPath());
    }

    private void uploadImage(final String image) {

        MultipartParams multipartParams = new MultipartParams.Builder().addFile("ref_image", new File(image)).build();
        RestClient.getApiInterface(this).getImageUrl(multipartParams.getMap()).enqueue(new ResponseResolver<BaseModel>(this, true, true) {
            @Override
            public void success(BaseModel baseModel) {
                ImageUrlPojo imageUrlPojo = new ImageUrlPojo();
                try {
                    imageUrlPojo.setData(baseModel.toResponseModel(ImageUrl.class));
                } catch (Exception e) {

                               Utils.printStackTrace(e);
                }
                imagesList.add(imageUrlPojo.getData().getImageUrl());
                if (imagesAdapter != null) {
                    imagesAdapter.notifyDataSetChanged();
                } else {
                    rvImages.setAdapter(imagesAdapter = new ImagesAdapter(AddProductActivity.this, imagesList));
                    rvImages.setVisibility(View.VISIBLE);
                }
                rvImages.post(new Runnable() {
                    @Override
                    public void run() {
                        rvImages.smoothScrollToPosition(imagesList.size());
                    }
                });

            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
            }
        });
    }

    int getIdFromName(String name) {
        switch (name) {
            case "FIXED":
                return 1;
            case "MINUTE":
                return 2;
            case "HOUR":
                return 3;
            case "DAY":
                return 4;
            case "MONTH":
                return 5;
            case "YEAR":
                return 6;
            default:
                return 1;
        }
    }
}