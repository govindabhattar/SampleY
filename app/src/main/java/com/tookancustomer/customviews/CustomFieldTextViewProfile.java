package com.tookancustomer.customviews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tookancustomer.ProfileActivity;
import com.tookancustomer.R;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT;
import static com.tookancustomer.appdata.Keys.Extras.HEADER_WEBVIEW;
import static com.tookancustomer.appdata.Keys.Extras.URL_WEBVIEW;

/**
 * Created by Mohit Kr. Dhiman on 02/12/16.
 */

public class CustomFieldTextViewProfile implements SignupTemplateData.Listener, TextWatcher, View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        View.OnFocusChangeListener {

    private final String TAG = CustomFieldTextViewProfile.class.getSimpleName();
    //    private final MapActivity activity;
    private final ProfileActivity activity;
    Context context;
    private View view;
    private TextView tvLabel;
    private View vwDeliveryDetailsEmail, vwBotton;
    private EditText etCustomFieldValue;
    private ImageView vCustomFieldIcon;
    private SignupTemplateData item;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private LinearLayout llCountryCode;
    private TextView tvCountryCode;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldTextViewProfile(Context context, int pos) {

        this.context = context;
        if ((activity = (ProfileActivity) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_text, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        vwDeliveryDetailsEmail = view.findViewById(R.id.vwDeliveryDetailsEmail);
        vwBotton = view.findViewById(R.id.vwBotton);
        etCustomFieldValue = view.findViewById(R.id.etCustomFieldValue);
        tvCountryCode = view.findViewById(R.id.tvCountryCode);
        llCountryCode = view.findViewById(R.id.llCountryCode);
        etCustomFieldValue.addTextChangedListener(this);
        vCustomFieldIcon = view.findViewById(R.id.vCustomFieldIcon);
        vwDeliveryDetailsEmail.setVisibility(View.GONE);
        vwBotton.setVisibility(View.VISIBLE);
//        Country country = countryPicker.getUserCountryInfo(context);
//        tvCountryCode.setText(country != null ? country.getDialCode() : "+1");
//        llCountryCode.setOnClickListener(this);
    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(SignupTemplateData item, boolean setEnable) {
        this.item = item;
        this.item.setListener(this);

        // etCustomFieldValue.setHint(context.getString(R.string.enter) + " " + item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
        etCustomFieldValue.setHint(" - ");
        if (item.getData().toString().equals(null) || item.getData().toString().isEmpty()) {
            if (item.getDataType().equals(Keys.DataType.CHECKBOX)) {
                if (item.getData().toString().equals("true")) {
                    etCustomFieldValue.setText(StorefrontCommonData.getString(activity, R.string.selected));
                } else {
                    etCustomFieldValue.setText(StorefrontCommonData.getString(activity, R.string.not_selected));
                }
            }
            etCustomFieldValue.setText(item.getData().toString());
        } else {
            if (item.getDataType().equals(Keys.DataType.TELEPHONE)) {

                Log.e("telephone no. data>>>>", item.getData().toString());
                String[] phoneNumber = item.getData().toString().split(" ");
                try {
                    if (phoneNumber.length > 1) {
                        tvCountryCode.setText(phoneNumber[0]);
                        etCustomFieldValue.setText(phoneNumber[1]);
                    } else {
                        if (item.getData().toString().startsWith("+")) {
                        } else {
                            tvCountryCode.setText(Utils.getDefaultCountryCode(context));

                            etCustomFieldValue.setText(item.getData().toString());
                        }
                    }

                } catch (Exception e) {
                }


            } else if (item.getDataType().equals(Keys.DataType.NUMBER)) {
                try {
                    etCustomFieldValue.setText(item.getData().toString());
                } catch (Exception e) {
                    etCustomFieldValue.setText(item.getData().toString().replace("_", " ").substring(0, 1).toUpperCase() + item.getData().toString().replace("_", " ").substring(1).toLowerCase());
                }
            }
//            else if (item.getDataType().equals(Keys.DataType.DATE) || item.getDataType().equals(Keys.DataType.DATE_FUTURE) || item.getDataType().equals(Keys.DataType.DATE_PAST) || item.getDataType().equals(Keys.DataType.DATE_TODAY))
//            {
//                etCustomFieldValue.setText(DateUtils.getInstance().parseDateAs(item.getData().toString(),ONLY_DATE_mm_dd_yy));
//            }

            else if (item.getDataType().equals(Keys.DataType.DATETIME) || item.getDataType().equals(Keys.DataType.DATETIME_FUTURE)
                    || item.getDataType().equals(Keys.DataType.DATETIME_PAST)) {


                String formattedDATE = new SimpleDateFormat(STANDARD_DATE_FORMAT).format(DateUtils.getInstance().getDate(item.getData().toString()));

                etCustomFieldValue.setText(DateUtils.getInstance().parseDateAs(formattedDATE, UIManager.getStandardDateTimeFormat()));
            }

            else {
                etCustomFieldValue.setText(item.getData().toString().replace("_", " ").substring(0, 1).toUpperCase() + item.getData().toString().replace("_", " ").substring(1).toLowerCase());
            }
        }

//        if (item.getLabel().equalsIgnoreCase("task_details") && (Utils.isMultipleCategoryApp()||) {
        if (item.getLabel().equalsIgnoreCase("task_details")) {
            etCustomFieldValue.setSingleLine(false);
        }
        switch (item.getDataType()) {
            case Keys.DataType.NUMBER:
                etCustomFieldValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_number_inactive);
                break;
            case Keys.DataType.EMAIL:
                etCustomFieldValue.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_email_inactive);
                break;
            case Keys.DataType.TELEPHONE:
                llCountryCode.setVisibility(View.VISIBLE);
                etCustomFieldValue.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_CLASS_NUMBER);
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_contact_unfilled);
                break;
            case Keys.DataType.BARCODE:
                etCustomFieldValue.setFocusableInTouchMode(false);
                etCustomFieldValue.setOnFocusChangeListener(this);
                etCustomFieldValue.setOnClickListener(this);
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_scan_inactive);
                break;
            case Keys.DataType.TEXT:
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_text_inactive);
                break;
            case Keys.DataType.URL:
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_url_inactive);
                break;
            case Keys.DataType.DATE:
            case Keys.DataType.DATE_FUTURE:
            case Keys.DataType.DATE_PAST:
            case Keys.DataType.DATE_TODAY:
            case Keys.DataType.DATETIME:
            case Keys.DataType.DATETIME_FUTURE:
            case Keys.DataType.DATETIME_PAST:
                etCustomFieldValue.setFocusableInTouchMode(false);
                etCustomFieldValue.setOnClickListener(getListener());
                vCustomFieldIcon.setBackgroundResource(R.drawable.ic_icon_pickup_before_inactive);
                break;
        }

        tvLabel.setText(item.getDisplayName().replace("_", " ").substring(0, 1).toUpperCase() + item.getDisplayName().replace("_", " ").substring(1).toLowerCase());
        if (setEnable)
            etCustomFieldValue.setEnabled(true);
        else
            etCustomFieldValue.setEnabled(false);
        if (item.isReadOnly()) {
            //etCustomFieldValue.setEnabled(false);
            if (etCustomFieldValue.getText().toString().isEmpty()) {
                etCustomFieldValue.setVisibility(View.GONE);
            }
            if (item.getDataType().equals(Keys.DataType.URL)) {
                //  etCustomFieldValue.setEnabled(true);
                etCustomFieldValue.setEnabled(false);
                etCustomFieldValue.setTextColor(activity.getResources().getColor(R.color.colorAccent));
                etCustomFieldValue.setFocusableInTouchMode(false);
                etCustomFieldValue.setOnClickListener(getListener());
            }
        }

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (item.getDataType().equals(Keys.DataType.TELEPHONE)) {
            item.setData(tvCountryCode.getText().toString() + " " + editable.toString());
        } else {
            item.setData(editable.toString());
        }
    }

    @Override
    public void onClick(View view) {
        UIManager.isPickup = true;
        switch (view.getId()) {
            case R.id.etCustomFieldValue:
                //activity.setBarcodeRequestCode(Codes.Request.OPEN_SCANNER_CUSTOMFIELD_BARCODE);
                activity.setCustomFieldPosition(item);
                // activity.scanBarcodePopup(activity.getString(R.string.add_barcode));
                break;
            case R.id.llCountryCode:
                Utils.hideSoftKeyboard(activity, llCountryCode);
                CountrySelectionDailog.getInstance(activity, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvCountryCode.setText(country.getCountryCode());
                        etCustomFieldValue.requestFocus();
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();
                break;
        }
    }


    /**
     * Method to create Listener
     *
     * @return
     */
    private View.OnClickListener getListener() {
        UIManager.isPickup = true;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datePickerFragment = null;
                if (!item.getDataType().equals(Keys.DataType.URL)) {
                    datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setListener(CustomFieldTextViewProfile.this);
                }
                long taskTime = System.currentTimeMillis();
                final long MILLISECONDS_PER_DAY = 1000L * 60 * 60 * 24;
                switch (item.getDataType()) {

                    case Keys.DataType.DATE_PAST:
                        datePickerFragment.setMaxDate(taskTime);
                        break;
                    case Keys.DataType.URL:
                        if (etCustomFieldValue.getText().toString().contains("http")) {
                            Intent intentPayment = new Intent(activity, WebViewActivity.class);
                            intentPayment.putExtra(URL_WEBVIEW, etCustomFieldValue.getText().toString());
                            intentPayment.putExtra(HEADER_WEBVIEW, StorefrontCommonData.getString(activity, R.string.app_name));
                            activity.startActivity(intentPayment);
                            AnimationUtils.forwardTransition(activity);
                        } else {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.invalid_url));
                        }
                        break;
                    case Keys.DataType.DATE_FUTURE:
                        datePickerFragment.setMinDate(taskTime);
                        break;
                    case Keys.DataType.DATE_TODAY:
                        datePickerFragment.setMinDate(taskTime);
                        datePickerFragment.setMaxDate(taskTime);
                        break;

                    case Keys.DataType.DATETIME_FUTURE:
                        datePickerFragment.setMinDate(taskTime);
                        break;
                    case Keys.DataType.DATETIME_PAST:
                        datePickerFragment.setMaxDate(taskTime + 1000l * 60);
                        break;

                    default:
                        // TODO
                        break;
                }


                if (year > 0 && month > 0 && day > 0) {
                    datePickerFragment.setDay(day);
                    datePickerFragment.setMonth(month);
                    datePickerFragment.setYear(year);
                }

                if (datePickerFragment != null)
                    datePickerFragment.show(activity.getSupportFragmentManager(), TAG);

            }
        };
    }

    @Override
    public Object getView() {
        return this;
    }

    public void addBarcode(String contents) {

        etCustomFieldValue.setText(contents);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        String dataType = item.getDataType();
        if (dataType.equals(Keys.DataType.DATETIME) || dataType.equals(Keys.DataType.DATETIME_PAST) || dataType.equals(Keys.DataType.DATETIME_FUTURE)) {
            //Added this check because onDateSet method call twice in 4.4 and below
            if (view.isShown()) {
                showTimePicker();
            }

        } else {
//            String dateSelected = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
            String dateSelected = year + "-" + (month + 1) + "-" + day;

            //Check if date is selected from lollipop devices
            if (UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP || UIManager.DEVICE_API_LEVEL == Build.VERSION_CODES.LOLLIPOP_MR1) {
                dateSelected = getSelectedDateInLollipopDevices();
            }

            Log.e(TAG, dateSelected);
            etCustomFieldValue.setText(dateSelected);
        }
    }

    public String getSelectedDateInLollipopDevices() {

        Calendar taskDate = Calendar.getInstance();
        taskDate.setTimeInMillis(System.currentTimeMillis());

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        Log.i("selectedDate", "=" + selectedDate);
        Log.i("taskDate", "=" + taskDate);
        Log.i("taskDateYaer", "=" + taskDate.get(Calendar.YEAR));
        Log.i("taskDateMonth", "=" + taskDate.get(Calendar.MONTH));
        Log.i("taskDateDay", "=" + taskDate.get(Calendar.DAY_OF_MONTH));
//        String dateSelected = (month + 1) + "/" + day + "/" + year;
        String dateSelected = year + "-" + (month + 1) + "-" + day;

        switch (item.getDataType()) {
            case Keys.DataType.DATE_PAST:
                if (year >= taskDate.get(Calendar.YEAR) && month >= taskDate.get(Calendar.MONTH) && day > taskDate.get(Calendar.DAY_OF_MONTH)) {
                    dateSelected = "";
                    Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.please_select_past_date), Toast.LENGTH_LONG).show();
                }
                break;
            case Keys.DataType.DATE_FUTURE:
                if (year <= taskDate.get(Calendar.YEAR) && month <= taskDate.get(Calendar.MONTH) && day < taskDate.get(Calendar.DAY_OF_MONTH)) {
                    dateSelected = "";
                    Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.please_select_future_date), Toast.LENGTH_LONG).show();
                }
                break;
            case Keys.DataType.DATE_TODAY:
                if (!(year == taskDate.get(Calendar.YEAR) && month == taskDate.get(Calendar.MONTH) && day == taskDate.get(Calendar.DAY_OF_MONTH))) {
                    dateSelected = "";
                    Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.please_select_today_date), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        return dateSelected;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.hour = hour;
        this.minute = minute;


        switch (item.getDataType()) {
            case Keys.DataType.DATETIME_PAST:
                boolean isValidDateTime = (year >= Calendar.getInstance().get(Calendar.YEAR) && month >= Calendar.getInstance().get(Calendar.MONTH) && day >= Calendar.getInstance
                        ().get(Calendar.DAY_OF_MONTH)
                        && hour >= Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                if (hour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                    isValidDateTime = isValidDateTime && minute > Calendar.getInstance().get(Calendar.MINUTE);
                }
                if (isValidDateTime) {

                    Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.please_select_past_date_time), Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case Keys.DataType.DATETIME_FUTURE:
                boolean isValid = (year <= Calendar.getInstance().get(Calendar.YEAR) && month <= Calendar.getInstance().get(Calendar.MONTH) && day <= Calendar.getInstance().get
                        (Calendar.DAY_OF_MONTH)
                        && hour <= Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                if (hour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                    isValid = isValid && minute < Calendar.getInstance().get(Calendar.MINUTE);
                }
                if (isValid) {
                    Toast.makeText(activity, StorefrontCommonData.getString(activity, R.string.please_select_future_date_time), Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
        String timeSelected = UIManager.getTimeFormat(hour, minute);
        String dateSelected = year + "-" + (month + 1) + "-" + day;
//        String dateSelected = (month + 1) + "/" + day + "/" + year;

        etCustomFieldValue.setText(dateSelected + " " + timeSelected);


    }

    public void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(CustomFieldTextViewProfile.this);

        if (hour > 0 && minute > 0) {
            timePickerFragment.setHour(hour);
            timePickerFragment.setMinute(minute);
        }
        timePickerFragment.show(activity.getSupportFragmentManager(), TAG);
    }

    public void addBarcodeManually() {
        etCustomFieldValue.setFocusableInTouchMode(true);
        etCustomFieldValue.requestFocus();
        Utils.showSoftKeyboard(activity);

    }

    @Override
    public void onFocusChange(View view, boolean isFocus) {
        if (!isFocus)
            etCustomFieldValue.setFocusableInTouchMode(false);
    }

}
