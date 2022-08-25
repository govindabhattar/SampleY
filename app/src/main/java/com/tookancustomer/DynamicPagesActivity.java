package com.tookancustomer;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.adapters.DynamicPagesAdapter;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.appConfiguration.DynamicPagesDetails;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

public class DynamicPagesActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rvDynamicPages;
    private DynamicPagesAdapter dynamicPagesAdapter;
    private ArrayList<DynamicPagesDetails> pagesList = new ArrayList<>();
    private String header;

    public static final String EXTRA_PAGE_LIST = "EXTRA_PAGE_LIST";
    public static final String EXTRA_HEADER_NAME = "EXTRA_HEADER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_pages);
        setParameters();
        init();

    }

    private void setParameters() {
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(EXTRA_PAGE_LIST)) {
            pagesList = (ArrayList<DynamicPagesDetails>) bundle.getSerializable(EXTRA_PAGE_LIST);
            header = bundle.getString(EXTRA_HEADER_NAME);
        } else {
            pagesList = StorefrontCommonData.getAppConfigurationData().getDynamicPagesDetails();
            header = StorefrontCommonData.getTerminology().getDynamicPages();
        }
    }

    private void init() {
        ((TextView) findViewById(R.id.tvHeading)).setText(header);
        rvDynamicPages = findViewById(R.id.rvDynamicPages);
        rvDynamicPages.setLayoutManager(new LinearLayoutManager(this));

        dynamicPagesAdapter = new DynamicPagesAdapter(this, pagesList);
        rvDynamicPages.setAdapter(dynamicPagesAdapter);

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                onBackPressed();
                break;
        }
    }
}
