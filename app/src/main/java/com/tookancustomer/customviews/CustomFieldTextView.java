package com.tookancustomer.customviews;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tookancustomer.CheckOutActivityOld;
import com.tookancustomer.R;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.fragment.picker.DatePickerFragment;
import com.tookancustomer.fragment.picker.TimePickerFragment;
import com.tookancustomer.models.userdata.Item;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.Calendar;

/**
 * Created by Nadeem Khan on 02/12/16.
 */

public class CustomFieldTextView implements Item.Listener, TextWatcher, View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        View.OnFocusChangeListener {

    private final String TAG = com.tookancustomer.customviews.CustomFieldTextView.class.getSimpleName();
    private View view;
    private TextView tvLabel;
    private EditText etCustomFieldValue;
    private ImageView vCustomFieldIcon;
    private final CheckOutActivityOld activity;
    Context context;
    private Item item;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    /**
     * Method to initialize the CustomField
     */
    public CustomFieldTextView(Context context) {
        this.context = context;
        if ((activity = (CheckOutActivityOld) context) == null) return;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_field_text, null);
        tvLabel = view.findViewById(R.id.tvLabel);
        etCustomFieldValue = view.findViewById(R.id.etCustomFieldValue);
        etCustomFieldValue.addTextChangedListener(this);
        vCustomFieldIcon = view.findViewById(R.id.vCustomFieldIcon);

    }

    /**
     * Method to render the data of the Custom Field
     *
     * @return
     */
    public View render(Item item) {
        this.item = item;
        this.item.setListener(this);

        etCustomFieldValue.setHint(StorefrontCommonData.getString(activity,R.string.enter) + " " + item.getDisplayName());
        etCustomFieldValue.setText(item.getData().toString());
//        if (item.getLabel().equalsIgnoreCase("task_details") && (StorefrontUtils.isMultipleCategoryApp()||) {
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

        tvLabel.setText(item.getDisplayName());

        if (item.isReadOnly()) {
            etCustomFieldValue.setEnabled(false);
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
        item.setData(editable.toString());
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.etCustomFieldValue) {
            activity.setBarcodeRequestCode(Codes.Request.OPEN_SCANNER_CUSTOMFIELD_BARCODE);
            activity.setCustomFieldPositionPickup(item);
            activity.scanBarcodePopup(StorefrontCommonData.getString(activity,R.string.add_barcode));

        }
    }


    /**
     * Method to create Listener
     *
     * @return
     */
    private View.OnClickListener getListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setListener(CustomFieldTextView.this);

                long taskTime = System.currentTimeMillis();
                final long MILLISECONDS_PER_DAY = 1000L * 60 * 60 * 24;
                switch (item.getDataType()) {

                    case Keys.DataType.DATE_PAST:
                        datePickerFragment.setMaxDate(taskTime);
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
            String dateSelected = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
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
        String dateSelected = (month + 1) + "/" + day + "/" + year;
        switch (item.getDataType()) {
            case Keys.DataType.DATE_PAST:
                if (year >= taskDate.get(Calendar.YEAR) && month >= taskDate.get(Calendar.MONTH) && day > taskDate.get(Calendar.DAY_OF_MONTH)) {
                    dateSelected = "";
                    Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.please_select_past_date), Toast.LENGTH_LONG).show();
                }
                break;
            case Keys.DataType.DATE_FUTURE:
                if (year <= taskDate.get(Calendar.YEAR) && month <= taskDate.get(Calendar.MONTH) && day < taskDate.get(Calendar.DAY_OF_MONTH)) {
                    dateSelected = "";
                    Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.please_select_future_date), Toast.LENGTH_LONG).show();
                }
                break;
            case Keys.DataType.DATE_TODAY:
                if (!(year == taskDate.get(Calendar.YEAR) && month == taskDate.get(Calendar.MONTH) && day == taskDate.get(Calendar.DAY_OF_MONTH))) {
                    dateSelected = "";
                    Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.please_select_today_date), Toast.LENGTH_LONG).show();
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

                    Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.please_select_past_datetime), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.please_select_future_datetime), Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
        String timeSelected = UIManager.getTimeFormat(hour, minute);
        String dateSelected = (month + 1) + "/" + day + "/" + year;

        etCustomFieldValue.setText(dateSelected + " " + timeSelected);


    }

    public void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(CustomFieldTextView.this);

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
