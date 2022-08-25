package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.interfaces.DateListners;
import com.tookancustomer.models.MarketplaceStorefrontModel.Datum;
import com.tookancustomer.utility.DateUtils;
import com.tookancustomer.utility.GlideUtil;
import com.tookancustomer.utility.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectPreOrderTimeDialog extends Dialog implements View.OnClickListener {

    private TextView tvSelectDate, tvSelectTime,
            tvStoreName, tvStoreAddress,
            tvScheduleDelivery, tvPreorderText;
    private ImageView ivStoreImage, ivClose;
    private Button btnSubmit;
    private OnPreOrderTimeSelectionListener timeSelectionListener;

    private Date preOrderDateObject = new Date();
    private Date bufferdate = new Date();

    private Activity activity;


    public SelectPreOrderTimeDialog(@NonNull Context context,
                                    OnPreOrderTimeSelectionListener timeSelectionListener) {
        super(context);
        setContentView(R.layout.layout_dialog_select_pre_order_time);
        activity = (Activity) context;
        this.timeSelectionListener = timeSelectionListener;
        setWindow();
        initViews();
        setData();
    }

    public SelectPreOrderTimeDialog setStorefrontData(Datum storefrontData) {
        setStoreData(storefrontData);
        return this;
    }

    private void setStoreData(Datum storefrontData) {
        tvStoreName.setText(storefrontData.getStoreName());
        tvStoreAddress.setText(storefrontData.getAddress());

        if (storefrontData.getInstantTask() == 0
                && storefrontData.getScheduledTask() == 1) {

            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
//        c.setTime(currentDate);
            c.add(Calendar.MINUTE, storefrontData.getPreBookingBuffer());
            currentDate = c.getTime();

            bufferdate = currentDate;
            preOrderDateObject = currentDate;
        }
        new GlideUtil.GlideUtilBuilder(ivStoreImage)
                .setPlaceholder(R.drawable.ic_image_placeholder)
                .setError(R.drawable.ic_image_placeholder)
                .setLoadItem(storefrontData.getLogo())
                .build();

        setData();

    }

    private void setWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        wlp.gravity = Gravity.CENTER;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = (int) (displayMetrics.widthPixels * .90);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//to avoid black backgorund during animation
        window.setAttributes(wlp);
    }

    private void initViews() {
        tvSelectDate = findViewById(R.id.tvSelectDate);
        tvSelectTime = findViewById(R.id.tvSelectTime);
        tvScheduleDelivery = findViewById(R.id.tvScheduleDelivery);
        tvPreorderText = findViewById(R.id.tvPreorderText);

        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreAddress = findViewById(R.id.tvStoreAddress);

        ivStoreImage = findViewById(R.id.ivStoreImage);
        ivClose = findViewById(R.id.ivClose);

        btnSubmit = findViewById(R.id.btnSubmit);

        Utils.setOnClickListener(this, tvSelectDate, tvSelectTime, btnSubmit, ivClose);
    }

    public void setData() {
        try {
            tvSelectDate.setText(DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                    "yyyy-MM-dd"));
            tvSelectTime.setText(DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                    "hh:mm a"));
        } catch (ParseException e) {
            Utils.printStackTrace(e);
        }

        tvPreorderText.setText(StorefrontCommonData.getString(activity, R.string.pre_order));
        tvScheduleDelivery.setText(StorefrontCommonData.getString(activity, R.string.text_schedule_your_delivery));
        btnSubmit.setText(StorefrontCommonData.getString(activity, R.string.submit));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSelectDate:
                openDatePicker();
                break;
            case R.id.tvSelectTime:
                openTimePicker();
                break;
            case R.id.btnSubmit:
                if (timeSelectionListener != null) {

                    if (preOrderDateObject.before(bufferdate)) {
                        try {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.selected_time_less_then) + DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                                    "hh:mm a"));
                        } catch (ParseException e) {
                            Utils.printStackTrace(e);
                        }
                        return;
                    }

                    try {
                        Dependencies.setIsPreorderSelecetedForMenu(true);
                        String preOrderDateTime = DateUtils.getInstance().convertDateObjectToUtc(preOrderDateObject);
                        Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
                        timeSelectionListener.onDateTimeSelected(preOrderDateTime);
                    } catch (ParseException e) {
                        Utils.printStackTrace(e);
                    }
                }

                dismiss();
                break;
            case R.id.ivClose:
                dismiss();
                break;
        }
    }

    private void openTimePicker() {

        DateUtils.getInstance().openTimePicker((AppCompatActivity) activity,
                preOrderDateObject != null ? preOrderDateObject : new Date(),
                new DateListners.OnTimeSelectedListener() {
                    @Override
                    public void onTimeSelected(Date date) {

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);

//                        if (sdf.format(new Date()).equals(sdf.format(preOrderDateObject))) {
                        if (date.before(bufferdate)) {
                            try {
                                tvSelectDate.setText(DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                                        "yyyy-MM-dd"));
                                Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.selected_time_less_then) + DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                                        "hh:mm a"));
                            } catch (ParseException e) {
                                Utils.printStackTrace(e);
                            }
                            return;
                        }

                        preOrderDateObject = date;
                        try {
                            tvSelectTime.setText(DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                                    "hh:mm a"));
                        } catch (ParseException e) {
                            Utils.printStackTrace(e);
                        }
                    }
                });
    }

    private void openDatePicker() {
        DateUtils.getInstance().openDatePicker((AppCompatActivity) activity,
                bufferdate != null ? bufferdate : new Date(),
                new DateListners.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date date) {
                        preOrderDateObject = date;
                        try {
                            tvSelectDate.setText(DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                                    "yyyy-MM-dd"));
                            tvSelectTime.setText(DateUtils.getInstance().displayDateInCustomFormat(preOrderDateObject,
                                    "hh:mm a"));
                        } catch (ParseException e) {
                            Utils.printStackTrace(e);
                        }
                    }
                });
    }


    public interface OnPreOrderTimeSelectionListener {
        void onDateTimeSelected(String dateTime);
    }


}
