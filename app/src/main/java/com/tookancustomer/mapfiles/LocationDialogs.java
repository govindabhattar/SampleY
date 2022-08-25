package com.tookancustomer.mapfiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Utils;

/**
 * Created by Nadeem on 12/24/15.
 */


public class LocationDialogs {


    AlertDialog googlePlayAlertDialog;
    AlertDialog locationAlertDialog;

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showGooglePlayErrorAlert(final Activity mContext) {
        try {
            if (googlePlayAlertDialog != null && googlePlayAlertDialog.isShowing()) {
                googlePlayAlertDialog.dismiss();
            }
            AlertDialog.Builder alertDialogPrepare = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialogPrepare.setTitle(StorefrontCommonData.getString(mContext, R.string.google_play_services_error));
            alertDialogPrepare.setCancelable(false);

            // Setting Dialog Message
            alertDialogPrepare.setMessage(StorefrontCommonData.getString(mContext, R.string.please_install_google_play_services));

            // On pressing Settings button
            alertDialogPrepare.setPositiveButton(StorefrontCommonData.getString(mContext, R.string.ok_text), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=com.google.android.gms"));
                        mContext.startActivity(intent);
                    } catch (android.content.ActivityNotFoundException anfe) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms")));
                    }
                }
            });

            // on pressing cancel button
            alertDialogPrepare.setNegativeButton(StorefrontCommonData.getString(mContext, R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mContext.finish();
                }
            });

            googlePlayAlertDialog = alertDialogPrepare.create();

            // Showing Alert Message
            googlePlayAlertDialog.show();
        } catch (Exception e) {
            Utils.printStackTrace(e);
        }
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showLocationSettingsAlert(final Context mContext) {
        try {
            if (!((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    &&
                    !((LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (locationAlertDialog != null && locationAlertDialog.isShowing()) {
                    locationAlertDialog.dismiss();
                }
                AlertDialog.Builder alertDialogPrepare = new AlertDialog.Builder(mContext);

                // Setting Dialog Title
                alertDialogPrepare.setTitle(StorefrontCommonData.getString(mContext, R.string.location_settings));
                alertDialogPrepare.setCancelable(false);

                // Setting Dialog Message
                alertDialogPrepare.setMessage(StorefrontCommonData.getString(mContext, R.string.location_not_enabled_enable_from_settings));

                // On pressing Settings button
                alertDialogPrepare.setPositiveButton(StorefrontCommonData.getString(mContext, R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                locationAlertDialog = alertDialogPrepare.create();

                // Showing Alert Message
                locationAlertDialog.show();
            }
        } catch (Exception e) {
            Utils.printStackTrace(e);
        }
    }
}
