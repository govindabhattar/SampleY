package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tookancustomer.BuildConfig;
import com.tookancustomer.R;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;


/**
 * Class that holds ready to use dialogs
 *
 * @author Rishabh
 */
public class ProgressDialog {

    private static final String TAG = ProgressDialog.class.getSimpleName();

    private static Activity activity;

    private static Dialog progressDialog;
    private static TextView tvProgress;

    /**
     * Shows the progress dialog
     *
     * @param activity
     * @return the {@link TextView} on which progress has to be set
     */
    public static void show(Activity activity) {

//        show(activity, StorefrontCommonData.getString(activity,R.string.loading_text));
        show(activity, "");
    }

    /**
     * Method to init the progress dialog with a message
     *
     * @param context
     * @param message
     * @return
     */
    public static void show(final Activity context, final String message) {

        activity = context;

        try {

            /* Check if the last instance is alive */
            if (progressDialog != null)
                if (progressDialog.isShowing()) {
                    // tvProgress.setText(message);
                    return;
                }

            /*  Ends Here   */

            progressDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);

            progressDialog.setContentView(R.layout.dialog_progress);

            tvProgress = progressDialog.findViewById(R.id.tvProgress);
            tvProgress.setTypeface(Font.getRegular(activity));
            tvProgress.setText(message);
            tvProgress.setVisibility(View.GONE);

//            ((ProgressWheel) progressDialog.findViewById(R.id.progress_wheel)).spin();

            Window dialogWindow = progressDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();


        } catch (WindowManager.BadTokenException e) {
            Utils.printStackTrace(e);
            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (Exception e) {
            Utils.printStackTrace(e);
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Dismisses the Progress Dialog
     */
    public static void dismiss() {

        // Check if activity lives
        if (activity != null)
            // Check if the Dialog is null
            if (progressDialog != null)
                // Check whether the progressDialog is visible
                if (progressDialog.isShowing()) {
                    try {
                        // Dismiss the Dialog
                        progressDialog.dismiss();
                        progressDialog = null;
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                }
    }
}
