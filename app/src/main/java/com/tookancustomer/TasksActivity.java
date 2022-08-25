package com.tookancustomer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import com.tookancustomer.modules.recurring.fragments.AllTaskFragment;

import java.util.ArrayList;


public class TasksActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private TextView tvHeading;
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        mActivity = this;

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();

//            flag = getIntent().getIntExtra("flag", -1);
            flag = bundle.getInt("flag");
        }
        initView();
        setTabSelection();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case OPEN_NLEVEL_ACTIVITY_AGAIN:
                Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());

                break;
        }

    }

    private void initView() {

        tvHeading = findViewById(R.id.tvHeading);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(Utils.getCallTaskAs(StorefrontCommonData.getUserData(), false, true)));
        tabLayout.addTab(tabLayout.newTab().setText(StorefrontCommonData.getTerminology().getSubscriptions()));

        if ((flag == Constants.NotificationFlags.RULE_ACCEPTED) || (flag == Constants.NotificationFlags.RULE_REJECTED)
                || (flag == Constants.NotificationFlags.RECURRING_TASK_CREATION_FAIL) || (flag == Constants.NotificationFlags.RULE_CREATED)) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new AllRecurringTaskPaginationFragment()).commit();
            AllTaskFragment allTaskFragment = new AllTaskFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isRecurring", true);
            allTaskFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, allTaskFragment).commit();

            tvHeading.setText(StorefrontCommonData.getTerminology().getSubscriptions());

            tabLayout.getTabAt(1).select();


        } else {

            AllTaskFragment allTaskFragment = new AllTaskFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isRecurring", false);
            allTaskFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, allTaskFragment).commit();

            tvHeading.setText(Utils.getCallTaskAs(StorefrontCommonData.getUserData(), false, true));
            tabLayout.getTabAt(0).select();

        }

        if (!UIManager.getRecurringTaskActive()) {
            findViewById(R.id.llTaskTabs).setVisibility(View.GONE);
        }

        Utils.setOnClickListener(this, findViewById(R.id.rlBack));

    }

    private void setTabSelection() {


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {


                    AllTaskFragment allTaskFragment = new AllTaskFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isRecurring", false);
                    allTaskFragment.setArguments(bundle);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContainer, allTaskFragment);
                    transaction.commit();

                    tvHeading.setText(Utils.getCallTaskAs(StorefrontCommonData.getUserData(), false, true));


                } else {
                    AllTaskFragment allTaskFragment = new AllTaskFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isRecurring", true);
                    allTaskFragment.setArguments(bundle);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.flContainer, new AllRecurringTaskPaginationFragment());
                    transaction.replace(R.id.flContainer, allTaskFragment);
                    transaction.commit();

                    tvHeading.setText(StorefrontCommonData.getTerminology().getSubscriptions());

                }


            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rlBack) {
            onBackPressed();
        }


    }


}
