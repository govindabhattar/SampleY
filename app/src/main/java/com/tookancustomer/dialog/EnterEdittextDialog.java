package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

/**
 * Class that holds ready to use dialogs
 *
 * @author Rishabh
 */
public class EnterEdittextDialog {

    private static final String TAG = EnterEdittextDialog.class.getSimpleName();

    private Dialog optionsDialog;

    /*
     * The context which would listen to the Events
     * of the AlertDialog (must be an implementation of
     * Listener)
     */
    private Listener listener;

    /*
     * The activity on which the AlertDialog would be displayed
     */
    private Activity activity;

    /*
     * A unique purpose code to identify the action for which
     * the Dialog is Created
     */
    private int purposeCode;

    // The title for the Options Dialog
    private String title;

    private String editTextHint;

    // The message to be conveyed to the User
    private String message;
    // The text for Ok button
    private String positiveButton;
    // The text for Cancel button
    private String negativeButton;

    private Bundle backpack;

    /**
     * Method to showOn a multi-event Dialog
     */
    private EnterEdittextDialog init() {

        try {

            optionsDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            optionsDialog.setContentView(R.layout.dialog_enter_edittext);

            Window dialogWindow = optionsDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;

            dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;

            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            optionsDialog.setCancelable(true);
            optionsDialog.setCanceledOnTouchOutside(true);

            TextView tvTitle = optionsDialog.findViewById(R.id.tvTitle);
            TextView tvMessage = optionsDialog.findViewById(R.id.tvMessage);
            final EditText etEditText = optionsDialog.findViewById(R.id.etEditText);

            etEditText.setHint(editTextHint);
            etEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvTitle.setText(title);
            tvMessage.setText(Html.fromHtml(message));

            Button btnOk = optionsDialog.findViewById(R.id.btnOk);
            Button btnCancel = optionsDialog.findViewById(R.id.btnCancel);

            btnOk.setText(positiveButton);
            btnCancel.setText(negativeButton);


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    optionsDialog.dismiss();

                    if (listener != null)
                        listener.performPositiveAction(purposeCode, backpack, etEditText.getText().toString());
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    optionsDialog.dismiss();

                    if (listener != null)
                        listener.performNegativeAction(purposeCode, backpack);
                }
            });

        } catch (Exception e) {
            //
                               Utils.printStackTrace(e);
            Log.e(TAG, "OPTIONS DIALOG: " + e.getMessage());
        }

        return this;
    }

    /**
     * Method to init the initialized dialog
     */
    public void show() {

        // Check if activity lives
        //May throw BadTokenException if activity is finished
        try {
            if (activity != null)
                // Check if dialog contains data
                if (optionsDialog != null)
                    // Show the Dialog
                    optionsDialog.show();
        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }
    }

    /**
     * Listener to listen to the events when a main_button
     * on the Options Dialog was pressed
     * <p>
     * Developer: Rishabh
     * Dated: 19/05/15.
     */
    public interface Listener {

        /**
         * Override this method to perform operations
         * after OK button was pressed
         *
         * @param purpose
         */
        void performPositiveAction(int purpose, Bundle backpack, String editTextString);


        /**
         * Override this method to perform operations
         * after CANCEL button was pressed
         *
         * @param purpose
         */
        void performNegativeAction(int purpose, Bundle backpack);
    }

    /**
     * Class to build the OptionsDialog
     */
    public static class Builder {

        private EnterEdittextDialog optionsDialog = new EnterEdittextDialog();

        /**
         * Constructor to initialize the OptionsDialog
         * (for an Activity)
         *
         * @param activity
         */
        public Builder(Activity activity) {

            optionsDialog.activity = activity;

            if (activity instanceof Listener)
                optionsDialog.listener = (Listener) activity;
        }

        /**
         * Constructor to initialize the OptionsDialog
         * (for a Fragment)
         *
         * @param fragment
         */
        public Builder(Fragment fragment) {

            optionsDialog.activity = fragment.getActivity();

            if (fragment instanceof Listener)
                optionsDialog.listener = (Listener) fragment;
        }

        /**
         * Method to set purpose code to identify the Dialog
         *
         * @param purposeCode
         * @return
         */
        public Builder purpose(int purposeCode) {
            optionsDialog.purposeCode = purposeCode;
            return this;
        }

        /**
         * Method to retrieve the String from a resource
         *
         * @param resourceId
         * @return
         */
        private String getString(int resourceId) {
            return StorefrontCommonData.getString(optionsDialog.activity, resourceId);
        }

        /**
         * Method to set a custom listener to listen
         * the events from OptionsDialog
         *
         * @param listener
         * @return
         */
        public Builder listener(Listener listener) {
            optionsDialog.listener = listener;
            return this;
        }

        /**
         * Method to set a backpack containing the data for
         * the intended action
         *
         * @param backpack
         * @return
         */
        public Builder backpack(Bundle backpack) {
            optionsDialog.backpack = backpack;
            return this;
        }

        /**
         * Method to set title to the Title
         *
         * @param resourceId
         * @return
         */
        public Builder title(int resourceId) {
            return title(getString(resourceId));
        }

        /**
         * Method to set title to the Title
         *
         * @param title
         * @return
         */
        public Builder title(String title) {
            optionsDialog.title = title;
            return this;
        }

        /**
         * Method to set message to the Dialog
         *
         * @param resourceId
         * @return
         */
        public Builder message(int resourceId) {
            return message(getString(resourceId));
        }

        /**
         * Method to set message to the Dialog
         *
         * @param message
         * @return
         */
        public Builder message(String message) {
            optionsDialog.message = message;
            return this;
        }

        /**
         * Method to set edittext hint to the Dialog
         *
         * @param message
         * @return
         */
        public Builder editTextHint(String message) {
            optionsDialog.editTextHint = message;
            return this;
        }

        /**
         * Method to set text to the Positive Button
         *
         * @return
         */
        public Builder positiveButton(int resourcedId) {
            return positiveButton(getString(resourcedId));
        }

        /**
         * Method to set text to the Positive Button
         *
         * @param buttonText
         * @return
         */
        public Builder positiveButton(String buttonText) {
            optionsDialog.positiveButton = buttonText;
            return this;
        }

        /**
         * Method to set Text to the Negative Button
         *
         * @param resourceId
         * @return
         */
        public Builder negativeButton(int resourceId) {
            return negativeButton(getString(resourceId));
        }

        /**
         * Method to set Text to the Negative Button
         *
         * @param cancel
         * @return
         */
        public Builder negativeButton(String cancel) {
            optionsDialog.negativeButton = cancel;
            return this;
        }

        /**
         * Method to init the Options Dialog
         *
         * @return
         */
        public EnterEdittextDialog build() {

            // Check for null and assign default values
            optionsDialog.title = Utils.assign(optionsDialog.title, "");
            optionsDialog.message = Utils.assign(optionsDialog.message, getString(R.string.are_you_sure_text));
            optionsDialog.editTextHint = Utils.assign(optionsDialog.editTextHint, "");
            optionsDialog.positiveButton = Utils.assign(optionsDialog.positiveButton, getString(R.string.yes_text));
            optionsDialog.negativeButton = Utils.assign(optionsDialog.negativeButton, getString(R.string.no_text));

            return optionsDialog.init();
        }
    }
}
