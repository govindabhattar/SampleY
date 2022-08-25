package com.tookancustomer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hippo.HippoNotificationConfig;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.models.superCategories.SuperCategoriesData;
import com.tookancustomer.utility.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

//import com.ice.restring.Restring;

//import com.ice.restring.Restring;


/**
 * Developer: Rishabh
 * Dated: 13/11/15.
 */
public class BaseActivity extends AppCompatActivity implements Keys.GACategories, TerminologyStrings, Codes.Request, Codes.ResultCodes, Keys.APIFieldKeys, Keys.Extras, Keys.TransistionsKeys {
    protected final String TAG = getClass().getSimpleName();
    public static SuperCategoriesData superCategoriesData;
    public Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        Utils.setLanguage(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init(0); // Initialize customizations, 0 - invalid resourceId
        render(savedInstanceState);
    }

    public void fuguNotificationHandle() {
        HippoNotificationConfig.handleHippoPushNotification(mActivity, Dependencies.getFuguBundle());
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_LIGHT_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
        return false;
    }

    /**
     * Method to render the Information
     *
     * @param savedInstanceState
     */
    protected void render(Bundle savedInstanceState) {
        // do stuff in here
    }

    /**
     * Method to initialize all the Views
     * in the Layout of this Activity
     *
     * @param layoutResourceId
     */
    protected void init(int layoutResourceId) {

        // Set the ContentView Layout
        if (layoutResourceId > 0)
            setContentView(layoutResourceId);
    }

//    /**
//     * Method to retrieve the instance of AppManager
//     *
//     * @return
//     */
//    protected AppManager getAppManager() {
//        return AppManager.getInstance();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.setLanguage(this);

        // Sets currently running activity
        getAppManager().resume(this);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("refresh"));
    }

    /**
     * Method to retrieve the instance of AppManager
     *
     * @return
     */
    protected AppManager getAppManager() {
        return AppManager.getInstance();
    }

    @Override
    protected void onPause() {

        // Sets currently paused activity
//        getAppManager().pause(this);

        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Extract data included in the Intent
//
//            Integer flag = intent.getIntExtra("flag", -1);
//
//            if (flag == Constants.NotificationFlags.USER_DEBT_PENDING) {
//                Intent intent1 = new Intent(mActivity, UserDebtActivity.class);
//                intent1.putExtras(intent);
//                startActivity(intent1);
//            }
//        }
//    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        try {
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

//            super.attachBaseContext(Restring.wrapContext(CalligraphyContextWrapper.wrap(newBase)));
//            super.attachBaseContext(CalligraphyContextWrapper.wrap(Restring.wrapContext(newBase)));
        } catch (Exception e) {

            Utils.printStackTrace(e);
            super.attachBaseContext(newBase);
        }
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean ret = super.dispatchTouchEvent(event);
        View view = getCurrentFocus();
        try {
            if (view != null && view instanceof EditText) {
                View w = getCurrentFocus();
                int[] scrcoords = new int[2];
                assert w != null;
                w.getLocationOnScreen(scrcoords);
                float x = event.getRawX() + w.getLeft() - scrcoords[0];
                float y = event.getRawY() + w.getTop() - scrcoords[1];

                if (event.getAction() == MotionEvent.ACTION_UP
                        && (x < w.getLeft() || x >= w.getRight()
                        || y < w.getTop() || y > w.getBottom())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            } else {
//                if (!(this.getClass().getSimpleName().equalsIgnoreCase("WebviewActivity")))
//                    Utils.hideSoftKeyboard(this);
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
        return ret;
    }

    public String getStrings(int key) {
        return getStrings(this, key);

    }

    public static String getStrings(Context mContext, int key) {
        return StorefrontCommonData.getString(mContext, key);
    }

    public void setSuperCategoriesData(SuperCategoriesData superCategoriesData) {
        this.superCategoriesData = superCategoriesData;
    }


    public SuperCategoriesData getSuperCategoriesData() {
        return superCategoriesData;
    }


    public void startShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
    }

    public void stopShimmerAnimation(ShimmerFrameLayout shimmerLayout) {
        shimmerLayout.setVisibility(View.GONE);
        shimmerLayout.stopShimmerAnimation();
    }

}