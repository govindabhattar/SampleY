package com.tookancustomer;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tookancustomer.utility.Utils;

import static com.tookancustomer.appdata.ExtraConstants.EXTRA_LONG_DESCRIPTION;
import static com.tookancustomer.appdata.ExtraConstants.EXTRA_PRODUCT_NAME;

public class LongDescriptionActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLongDescription, tvHeading;
    private RelativeLayout rlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_description);
        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(EXTRA_LONG_DESCRIPTION)) {
            tvLongDescription.setText(bundle.getString(EXTRA_LONG_DESCRIPTION).replace("\\n", "\n"));
            tvHeading.setText(bundle.getString(EXTRA_PRODUCT_NAME));
        }

    }

    private void init() {
        rlBack = findViewById(R.id.rlBack);
        tvHeading = findViewById(R.id.tvHeading);
        tvLongDescription = findViewById(R.id.tvLongDescription);

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
