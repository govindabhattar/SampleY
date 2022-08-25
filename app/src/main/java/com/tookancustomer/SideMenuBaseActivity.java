package com.tookancustomer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.SideMenuTransition;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

public class SideMenuBaseActivity extends BaseActivity {
    private RelativeLayout rlTopLayout;
    private DrawerLayout mDrawerLayout;
    private long lastBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidemenu);

        rlTopLayout = findViewById(R.id.rlTopLayout);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        SideMenuTransition.setDualUserToggle(mActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SideMenuTransition.setSliderUI(mActivity, StorefrontCommonData.getUserData());
    }

    public void addTopLayout(Activity mActivity, int childLayout) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(childLayout, null);
        rlTopLayout.addView(view);
    }

    public void sideMenuClick(View v) {
        SideMenuTransition.sideMenuClick(v, mActivity);
    }

    public void performMainBackPress() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeSideMenu();
        } else {
            long currentTimeStamp = System.currentTimeMillis();
            long difference = currentTimeStamp - lastBackPressed;

            if (difference > 2000) {
                Utils.snackBar(this, getStrings(R.string.tap_again_to_exit_text),false);
                lastBackPressed = currentTimeStamp;
            } else {
                ActivityCompat.finishAffinity(this);
                Transition.exit(this);
            }
        }
    }

    public void openSideMenu(){
        mDrawerLayout.openDrawer(Gravity.START);
        Utils.setLightStatusBar(mActivity, mDrawerLayout);
    }

    public void closeSideMenu(){
        mDrawerLayout.closeDrawer(Gravity.START);
        Utils.setLightStatusBar(mActivity, mDrawerLayout);
    }

    public void lockSideMenu(){
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
   public void unlockSideMenu(){
       mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void restartActivity() {
        mDrawerLayout.closeDrawer(Gravity.START);

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}