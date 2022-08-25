package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Utils;


public class GenericMessageDialog {

    private static final String TAG = GenericMessageDialog.class.getSimpleName();

    private Dialog alertDialog;

    /**
     * The instance of the Activity on which the
     * AlertDialog will be displayed
     */
    private Activity activity;

    /**
     * The receiver to which the AlertDialog
     * will return the CallBacks
     * <p/>
     * Note: listener should be an instance of
     * AlertDialog.Listener
     */
    private Listener listener;

    /**
     * The code to differentiate the various CallBacks
     * from different Dialogs
     */
    private int purpose;

    /**
     * The title to be set on the Dialog
     */
    private String title;

    /**
     * The message to be set on the Dialog
     */
    private String message;
    private SpannableString spannableString;

    /**
     * The text to be set on the Action Button
     */
    private String actionButton;

    /**
     * The data to sent via the Dialog from the
     * remote parts of the Activity to other
     * parts
     */
    private Bundle backpack;

    /**
     * Method to create and display the alert alertDialog
     *
     * @return
     */
    private GenericMessageDialog init() {

        try {

            alertDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            alertDialog.setContentView(R.layout.dialog_generic_message);

            Window dialogWindow = alertDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
//            layoutParams.dimAmount = 0.6f;
            layoutParams.dimAmount = 0.8f;

            dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;

            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);

            TextView tvTitle = alertDialog.findViewById(R.id.tvTitle);
            TextView tvMessage = alertDialog.findViewById(R.id.tvMessage);
            TextView tvClose = alertDialog.findViewById(R.id.tvClose);
            ImageView ivClose = alertDialog.findViewById(R.id.ivClose);

            if (title != null) {
                tvTitle.setText(title);
                tvTitle.setTypeface(Font.getBold(activity));
                tvTitle.setVisibility(View.VISIBLE);
            }
            if (spannableString != null) {
                tvMessage.setText(spannableString);
            } else {
                String messageString = message.replace("\\n", "\n").replace("\n", "<br>");

                tvMessage.setText(Html.fromHtml(messageString));
            }
//            tvMessage.setText(Html.fromHtml(message));
            tvMessage.setTypeface(Font.getRegular(activity));

            if (actionButton != null) {
                tvClose.setVisibility(View.VISIBLE);
                tvClose.setText(actionButton);
                tvClose.setTypeface(Font.getRegular(activity));
                ivClose.setVisibility(View.GONE);
            } else {
                tvClose.setVisibility(View.GONE);
                ivClose.setVisibility(View.VISIBLE);
            }

            tvClose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();

                    if (listener != null)
                        listener.performPostAlertAction(purpose, backpack);
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();

                    if (listener != null)
                        listener.performPostAlertAction(purpose, backpack);
                }
            });

        } catch (Exception e) {


                               Utils.printStackTrace(e);
        }

        return this;
    }

    /**
     * Method to init the initialized alertDialog
     */
    public Dialog show() {

        // Check if activity lives
        if (activity != null)
            // Check if alertDialog contains data
            if (alertDialog != null) {
                try {
                    // Show the Dialog
                    alertDialog.show();

                    return alertDialog;
                } catch (Exception ex) {
//                    ex.printStackTrace();
                }
            }

        return alertDialog;
    }


    /**
     * @return return alert dialog status
     */
    public boolean isShowing() {

        if (alertDialog == null)
            return false;

        return alertDialog.isShowing();
    }

    /**
     * Method to dismiss the AlertDialog, if possible
     */
    public void dismiss() {

        // Check if activity lives
        if (activity != null)
            // Check if the Dialog is null
            if (alertDialog != null)
                // Check whether the alertDialog is visible
                if (alertDialog.isShowing()) {
                    try {
                        // Dismiss the Dialog
                        alertDialog.dismiss();
                        alertDialog = null;
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                }
    }

    /**
     * Interfaces the events from the AlertDialog
     * to the Calling Context
     */
    public interface Listener {

        /**
         * Override this method to listen to
         * the events fired from AlertDialog
         *
         * @param purpose
         * @param backpack
         */
        void performPostAlertAction(int purpose, Bundle backpack);
    }

    /**
     * Class to help building the Alert Dialog
     */
    public static class Builder extends GenericMessageDialog {

        private GenericMessageDialog alertDialog = new GenericMessageDialog();

        /**
         * Constructor to build a alertDialog for Activity
         *
         * @param activity
         */
        public Builder(Activity activity) {

            alertDialog.activity = activity;

            if (activity instanceof Listener)
                alertDialog.listener = (Listener) activity;
        }

        /**
         * Constructor to build a alertDialog for Fragment
         *
         * @param fragment
         */
        public Builder(Fragment fragment) {

            alertDialog.activity = fragment.getActivity();

            if (fragment instanceof Listener)
                alertDialog.listener = (Listener) fragment;
        }

        /**
         * Sets the a unique purpose code to differentiate
         * between the CallBacks
         *
         * @param purpose
         * @return
         */
        public Builder purpose(int purpose) {
            alertDialog.purpose = purpose;
            return this;
        }

        /**
         * Sets the a custom listener to receive the CallBacks
         *
         * @param listener
         * @return
         */
        public Builder listener(Listener listener) {
            alertDialog.listener = listener;
            return this;
        }

        /**
         * Set the data to be sent via callback
         *
         * @param backpack
         * @return
         */
        public Builder backpack(Bundle backpack) {
            alertDialog.backpack = backpack;
            return this;
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param resourceId
         * @return
         */
        public Builder title(int resourceId) {
            return title(StorefrontCommonData.getString(alertDialog.activity, resourceId));
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param title
         * @return
         */
        public Builder title(String title) {
            alertDialog.title = title;
            return this;
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param resourceId
         * @return
         */
        public Builder message(int resourceId) {
            return message(StorefrontCommonData.getString(alertDialog.activity, resourceId));
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param message
         * @return
         */
        public Builder message(String message) {
            if (message == null)
                message = StorefrontCommonData.getString(alertDialog.activity, R.string.unexpected_error_occurred);
            alertDialog.message = message;
            return this;
        }

        public Builder message(SpannableString spannableString) {
            alertDialog.spannableString = spannableString;

            alertDialog.message = spannableString.toString();

            return this;
        }

        /**
         * Set the actionButton for the AlertDialog
         *
         * @param resourceId
         * @return
         */
        public Builder button(int resourceId) {
            return button(StorefrontCommonData.getString(alertDialog.activity, resourceId));
        }

        /**
         * Set the actionButton for the AlertDialog
         *
         * @param button
         * @return
         */
        public Builder button(String button) {
            alertDialog.actionButton = button;
            return this;
        }

        /**
         * Method to build an AlertDialog and display
         * it on the screen. The instance returned can
         * be used to manipulate the alertDialog in future.
         *
         * @return
         */
        public GenericMessageDialog build() {

            alertDialog.message = Utils.assign(alertDialog.message, getString(R.string.message));
//            alertDialog.actionButton = Utils.assign(alertDialog.actionButton, getString(R.string.close));

            return alertDialog.init();
        }


        /**
         * Method to retrieve a String Resource
         *
         * @param resourceId
         * @return
         */
        private String getString(int resourceId) {
            return StorefrontCommonData.getString(alertDialog.activity, resourceId);
        }
    }

    public interface OnActionPerformed {
        void positive();


    }
}


