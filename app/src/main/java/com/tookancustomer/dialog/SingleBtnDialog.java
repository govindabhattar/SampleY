package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.customCalender.customviews.DateRangeCalendarView;
import com.tookancustomer.utility.DateUtils;

import java.util.Calendar;
import java.util.Date;


/**
 * This class is used to open and handle calender view.
 */
public abstract class SingleBtnDialog extends Dialog implements View.OnClickListener {
    private Button btApply;
    private Activity context;
    private DateRangeCalendarView calendar;
    private Date selectStartDate, selectedEndDate;
    private static final double widthPixel = 0.99;
    private String startDate;
    private String endDate;
    private RelativeLayout rlBack;


    /**
     * Constructor of the class.
     *
     * @param context the context
     */
    public SingleBtnDialog(@NonNull final Activity context, final String startDateSelected, final String endDateSelected) {
        super(context);
        this.context = context;
        this.startDate = startDateSelected;
        this.endDate = endDateSelected;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * widthPixel);
        setContentView(R.layout.custom_single_btn_layout);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        init();
    }

    /**
     * initialize all views in dialog.
     */
    private void init() {
        btApply = (Button) findViewById(R.id.btPositive);
        btApply.setText(StorefrontCommonData.getString(context, R.string.save));
        ((TextView)findViewById(R.id.tvSelectDate)).setText(StorefrontCommonData.getString(context, R.string.select_date));
        calendar = findViewById(R.id.calendar);
        rlBack = findViewById(R.id.rlBack);
//        calendar.setCalendarListener(this);

        if (startDate != null && endDate != null)
            calendar.drawSelectedDateRange(DateUtils.getInstance().convertStringToCalender(startDate), DateUtils.getInstance().convertStringToCalender(endDate), true);

        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                selectStartDate = startDate.getTime();
                selectedEndDate = startDate.getTime();
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {

                selectStartDate = startDate.getTime();
                selectedEndDate = endDate.getTime();
            }

            @Override
            public void onCancel() {

            }
        });
        setListener();
    }

    /**
     * Set listener of views in dialog.
     */
    private void setListener() {
        btApply.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btPositive:
                if (selectStartDate != null && selectedEndDate != null) {
                    dismiss();
                    onSuccessApply(selectStartDate, selectedEndDate);
                } else {
                    new AlertDialog.Builder(context).message(StorefrontCommonData.getString(context,R.string.please_select_checkin_checkout_date)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
//                            fetchFCMToken();
                        }
                    }).build().show();
                }
                break;

            case R.id.rlBack:
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * This method is used handle success of date.
     * <p>
     * //     * @param startDate the startDate
     * //     * @param endDate   the endDate
     */
    public abstract void onSuccessApply(final Date startDate, final Date endDate);


    public interface CalendarListener {
        void onDateRangeSelected(Calendar startDate, Calendar endDate);

        void onCancel();
    }

}
