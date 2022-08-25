package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tookancustomer.R;
import com.tookancustomer.adapters.UnavailableProductsItemAdapter;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.UnavailableProductData.UnavailableProductData;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tookancustomer.appdata.TerminologyStrings.PRODUCT;

public class UnavailableProductsDialog extends Dialog {

    private UnavailableProductData unavailableProductData;
    private BackPressListner listner;
    TextView doneTV, availabilityTV, productNameHeadingTV, headingTV;
    RecyclerView productListRV;
    Activity activity;

    public UnavailableProductsDialog(@NonNull Context context, UnavailableProductData unavailableProductData, Activity activity, BackPressListner listner) {
        super(context);

        setContentView(R.layout.layout_unavailable_products_dialog);

        this.unavailableProductData = unavailableProductData;
        this.activity = activity;
        this.listner = listner;

        setWindow();
        initViews();
    }


    private void setWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        wlp.gravity = Gravity.CENTER;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//to avoid black backgorund during animation
        window.setAttributes(wlp);
    }

    private void initViews() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        productListRV = findViewById(R.id.productListRV);

        doneTV = findViewById(R.id.doneTV);
        availabilityTV = findViewById(R.id.availabilityTV);
        productNameHeadingTV = findViewById(R.id.productNameHeadingTV);
        headingTV = findViewById(R.id.headingTV);

        doneTV.setText(StorefrontCommonData.getString(activity, R.string.back));
        productNameHeadingTV.setText(StorefrontCommonData.getString(activity, R.string.product_name).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()));
        headingTV.setText(StorefrontCommonData.getString(activity, R.string.product_not_available).replace(PRODUCT, StorefrontCommonData.getTerminology().getProduct()));
        availabilityTV.setText(StorefrontCommonData.getString(activity, R.string.availability));

        UnavailableProductsItemAdapter mAdapter = new UnavailableProductsItemAdapter(activity, unavailableProductData.getUnavailableProducts());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        productListRV.setLayoutManager(mLayoutManager);
        productListRV.setItemAnimator(new DefaultItemAnimator());
        productListRV.setAdapter(mAdapter);

        doneTV.setOnClickListener(v -> {
            if (listner != null) {
                listner.onDialogDismiss();
            }
            dismiss();
        });
    }

    public interface BackPressListner {
        void onDialogDismiss();
    }

}
