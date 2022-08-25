package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.adapter.RecuringSurgeDetailAdapter;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.RecurringSurgeListData;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tookancustomer.appdata.TerminologyStrings.DELIVERY;

public class RecuringSurgeDetailsDialog extends Dialog implements View.OnClickListener {

    private Button okBT;
    private SelectionListner listner;

    private ArrayList<RecurringSurgeListData> recurringSurgeData;
    private Activity activity;

    private LinearLayout headingLayoutLL;
    private TextView amountTV, occurrenceTV, amountChargeTV, tileTV, dayTV, textTV, headingTV;
    private RecyclerView surgeListRV;
    private int noOfFields;

    public RecuringSurgeDetailsDialog(@NonNull Context context,
                                      SelectionListner listner, ArrayList<RecurringSurgeListData> recurringSurgeData, int noOfFields) {
        super(context);
        if (noOfFields == 3)
            setContentView(R.layout.layout_recuring_surge_details_three);
        else if (noOfFields == 4)
            setContentView(R.layout.layout_recuring_surge_details_four);
        else
            setContentView(R.layout.layout_recuring_surge_details);
        activity = (Activity) context;
        this.listner = listner;
        this.noOfFields = noOfFields;
        this.recurringSurgeData = recurringSurgeData;
        setWindow();
        initViews();
    }


    private void setWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//to avoid black backgorund during animation
        window.setAttributes(wlp);
    }

    private void initViews() {
        okBT = findViewById(R.id.okBT);
        amountTV = findViewById(R.id.amountTV);
        occurrenceTV = findViewById(R.id.occurrenceTV);
        amountChargeTV = findViewById(R.id.amountChargeTV);
        tileTV = findViewById(R.id.tileTV);
        dayTV = findViewById(R.id.dayTV);
        headingTV = findViewById(R.id.headingTV);
        textTV = findViewById(R.id.textTV);
        headingLayoutLL = findViewById(R.id.headingLayoutLL);
        surgeListRV = findViewById(R.id.surgeListRV);
        okBT.setOnClickListener(this);

        okBT.setText(StorefrontCommonData.getString(activity, R.string.ok_text));
        amountTV.setText(StorefrontCommonData.getString(activity, R.string.total_amount));
        occurrenceTV.setText(StorefrontCommonData.getString(activity, R.string.no_of_occurrences));
        amountChargeTV.setText(StorefrontCommonData.getString(activity, R.string.amount_inc_of_delivery_charge));
        tileTV.setText(StorefrontCommonData.getString(activity, R.string.time));
        dayTV.setText(StorefrontCommonData.getString(activity, R.string.day_Text));
        textTV.setText(StorefrontCommonData.getString(activity, R.string.delivery_charges_may).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()));
        headingTV.setText(StorefrontCommonData.getString(activity, R.string.delivery_surge_charges).replace(DELIVERY, StorefrontCommonData.getTerminology().getDelivery()));


        RecuringSurgeDetailAdapter mAdapter = new RecuringSurgeDetailAdapter(activity, recurringSurgeData, noOfFields);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        surgeListRV.setLayoutManager(mLayoutManager);
        surgeListRV.setItemAnimator(new DefaultItemAnimator());
        surgeListRV.setAdapter(mAdapter);
        surgeListRV.setNestedScrollingEnabled(false);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.okBT:
                if (listner != null) {
                    listner.onDialogDismiss();
                }

                dismiss();

        }
    }


    public interface SelectionListner {
        void onDialogDismiss();
    }


}
