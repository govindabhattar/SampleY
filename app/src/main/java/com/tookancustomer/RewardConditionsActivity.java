package com.tookancustomer;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.ExtraConstants.EXTRA_LONG_DESCRIPTION;
import static com.tookancustomer.appdata.ExtraConstants.EXTRA_PRODUCT_NAME;

public class RewardConditionsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLongDescription, tvHeading,tvCondition1,tvCondition2,tvCondition3,tvCondition4,tvCondition5;
    private RelativeLayout rlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reward_conditions);
        init();

    }

    private void init() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvCondition1 = findViewById(R.id.tvCondition1);
        tvCondition1.setText(getStrings(R.string.payment_method_disabled_pay_cash_to_admin));
        tvCondition2 = findViewById(R.id.tvCondition2);
        tvCondition3 = findViewById(R.id.tvCondition3);
        tvCondition4 = findViewById(R.id.tvCondition4);
        tvCondition5 = findViewById(R.id.tvCondition5);
        tvCondition2.setText(getStrings(R.string.payment_method_disabled_pay_cash_to_admin));
        tvCondition3.setText(getStrings(R.string.payment_method_disabled_pay_cash_to_admin));
        tvCondition4.setText(getStrings(R.string.payment_method_disabled_pay_cash_to_admin));
        tvCondition5.setText(getStrings(R.string.payment_method_disabled_pay_cash_to_admin));
        Utils.setOnClickListener(this, rlBack);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }
}