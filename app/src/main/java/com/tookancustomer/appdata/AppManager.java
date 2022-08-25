package com.tookancustomer.appdata;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.hippo.HippoConfig;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.R;
import com.tookancustomer.SplashActivity;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.utility.AnimationUtils;
import com.tookancustomer.utility.Log;

import java.util.ArrayList;

/**
 * Developer: Rishabh
 * Dated: 24/06/15.
 */
public class AppManager {

    // Tag for App Manager
    private static final String TAG = "AppManager";

    //  The singleton instance of AppManager
    private static AppManager appManager;

    //  Activity that runs currently on front
    private Activity activity;

    /**
     * Method to capture the instance of AppManager
     *
     * @return
     */
    public static AppManager getInstance() {
        if (appManager == null)
            appManager = new AppManager();

        return appManager;
    }

    /**
     * Method to set the State of the Activity
     */
    public void pause(Activity activity) {
        this.activity = activity;
        Log.e(TAG, activity.getClass().getSimpleName() + ": PAUSED");
    }

    /**
     * Method to set the State of the Activity
     */
    public void resume(Activity activity) {

        this.activity = activity;

        Log.e(TAG, activity.getClass().getSimpleName() + ": RESUMED");
    }

    /**
     * Method to check pre-requisites.
     *
     * @return False if any condition is not met,
     * true otherwise
     */


    /**
     * Method to return the instance of Currently
     * running Activity with which the AppManager
     * was initialized
     *
     * @return
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Method to safely logout the User
     *
     * @param activity
     * @param isSessionExpired
     */


    /**
     * Method to get the App Status
     *
     * @return
     */


    /**
     * Method to check whether the Permission is Granted by the User
     * <p/>
     * permission type: DANGEROUS
     *
     * @param activity
     * @param permission
     * @return
     */
    public boolean isPermissionGranted(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Method to check whether the Permission is Granted by the User
     * <p/>
     * permission type: DANGEROUS
     *
     * @param activity
     * @param permission
     * @return
     */
    public boolean askUserToGrantPermission(Activity activity, String permission, String explanation, int code) {
        return askUserToGrantPermission(activity, new String[]{permission}, explanation, code);
    }

    /**
     * Method to check whether the Permission is Granted by the User
     * <p/>
     * permission type: DANGEROUS
     *
     * @param activity
     * @param permissions
     * @param explanation
     * @param requestCode
     * @return
     */
    public boolean askUserToGrantPermission(Activity activity, String[] permissions, String explanation, int requestCode) {

        String permissionRequired = null;

        for (String permission : permissions)
            if (!isPermissionGranted(activity, permission)) {
                permissionRequired = permission;
                break;
            }

        // Check if the Permission is ALREADY GRANTED
        if (permissionRequired == null) return true;

        // Check if there is a need to show the PERMISSION DIALOG
        boolean explanationRequired = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionRequired);
        Log.e(TAG, "askUserToGrantPermission: explanationRequired(" + explanationRequired + "): " + permissionRequired);

        // Convey the EXPLANATION if required
        if (explanationRequired) {

            if (explanation == null) explanation = "Please grant permission";
            Toast.makeText(activity, explanation, Toast.LENGTH_SHORT).show();
        } else {

            // We can request the permission, if no EXPLANATIONS required
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }

        return false;
    }


    public synchronized void invalidateSession(Activity activity, boolean isSessionExpired, String message) {

        performPreLogoutAction(activity, isSessionExpired);
        if (isSessionExpired) {
            showLogoutMessageToUser(activity, message);
        } else {
            performLogoutAction(activity);
        }
    }

    /**
     * Method to logout the User
     */
    private void performLogoutAction(Activity activity) {
//        StorefrontConfig storefrontConfig = new StorefrontConfig();
//        storefrontConfig.onSessionExpired(activity);

        Intent intent = new Intent(activity.getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
        AnimationUtils.exitTransition(activity);
    }

    /**
     * Method to erase all the ListingData specific to the User
     *
     * @param activity
     * @param isSessionExpired
     */
    public void performPreLogoutAction(Activity activity, boolean isSessionExpired) {

        /*  Erase the access token so that a Fresh Login could be started  */
        Dependencies.saveAccessToken(activity, "");
        HippoConfig.clearHippoData(activity);
        if (Dependencies.isDemoAppGradel()) {
            Dependencies.setMarketplaceReferenceId(BuildConfig.MARKETPLACE_REF_ID);

        }
//        Config.setAppMode(activity, Config.getCurrentAppMode(activity).name());
        PaymentMethodsClass.clearPaymentHashMaps();
        Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());
        StorefrontCommonData.setAppConfigurationData(null);
    }

    /**
     * Method to init message dialog to the User regarding the Logout
     */
    private void showLogoutMessageToUser(final Activity activity, final String message) {
        if (activity instanceof SplashActivity)
            return;
        new AlertDialog.Builder(activity)
                .message(message)
                .button(StorefrontCommonData.getString(activity, R.string.ok_text))
                .listener(new AlertDialog.Listener() {
                    @Override
                    public void performPostAlertAction(int purpose, Bundle backpack) {
                        performLogoutAction(activity);
                    }
                }).build().show();
    }

}
