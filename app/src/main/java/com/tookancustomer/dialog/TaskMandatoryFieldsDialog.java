package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Font;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;

/**
 * Developer: Rishabh
 * Dated: 23/07/15.
 */
public class TaskMandatoryFieldsDialog {


    private static final String TAG = TaskMandatoryFieldsDialog.class.getSimpleName();


    private Dialog mDialog;

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
     * The message to be set on the Dialog
     */
    private String message;

    /**
     * List to store the Items to be shown
     */
    private ArrayList<Builder.ListItem> itemsList = new ArrayList<>();

    /**
     * Fields to hold the bullets
     */
    private char itemBullet;
    private char subItemBullet;

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
     * Method to create and display the alert mDialog
     *
     * @return
     */
    private TaskMandatoryFieldsDialog init() {

        try {

            mDialog = new Dialog(activity, R.style.DialogTheme);
            mDialog.setContentView(R.layout.dialog_task_mandatory_fields);

            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;

            dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;

            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = mDialog.findViewById(R.id.tvMessage);
            tvMessage.setTypeface(Font.getBold(activity));
            tvMessage.setText(Html.fromHtml(message));

            Button btnAction = mDialog.findViewById(R.id.btnAction);
            btnAction.setText(actionButton);
            btnAction.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mDialog.dismiss();

                    if (listener != null)
                        listener.performPostAlertAction(backpack);
                }
            });

            if (itemsList != null && !itemsList.isEmpty()) {

                LinearLayout llList = mDialog.findViewById(R.id.llMandatoryList);

                for (Builder.ListItem listItem : itemsList) {

                    TextView tvHeaderItem = new TextView(activity);
                    tvHeaderItem.setTypeface(Font.getBold(activity));
                    String bullet = itemBullet == '\u0000' ? "" : (itemBullet + " ");
                    tvHeaderItem.setText(bullet + Utils.assign(listItem.getItem()));

                    View.OnClickListener listener = listItem.getListener();
                    if (listener != null) tvHeaderItem.setOnClickListener(listener);

                    // Add the Header Item to the Header List
                    llList.addView(tvHeaderItem);

                    String[] subItems = listItem.getSubItems();
                    if (subItems != null && subItems.length != 0) {

                        LinearLayout llSubList = new LinearLayout(activity);
                        llSubList.setOrientation(LinearLayout.VERTICAL);
                        llSubList.setPadding(Utils.convertDpToPixels(activity, 10), 0, 0, 0);

                        for (String subItem : subItems) {

                            TextView tvSubItem = new TextView(activity);
                            tvSubItem.setTypeface(Font.getMedium(activity));
                            String subBullet = subItemBullet == '\u0000' ? "" : (subItemBullet + " ");
                            tvSubItem.setText(subBullet + Utils.assign(subItem));

                            // Add the Sub Item to the Sub-list
                            llSubList.addView(tvSubItem);
                        }

                        // Add the Sub-list to the Header List
                        llList.addView(llSubList);
                    }
                }
            }
        } catch (Exception e) {


                               Utils.printStackTrace(e);

            Log.e(TAG, "init: " + e);
        }

        return this;
    }

    /**
     * Method to init the initialized mDialog
     */
    public void show() {

        // Check if activity lives
        if (activity != null)
            // Check if mDialog contains data
            if (mDialog != null) {
                try {
                    // Show the Dialog
                    mDialog.show();
                } catch (Exception ex) {
//                    ex.printStackTrace();
                }
            }
    }

    /**
     * Method to dismiss the AlertDialog, if possible
     */
    public void dismiss() {

        // Check if activity lives
        if (activity != null)
            // Check if the Dialog is null
            if (mDialog != null)
                // Check whether the mDialog is visible
                if (mDialog.isShowing()) {
                    try {
                        // Dismiss the Dialog
                        mDialog.dismiss();
                        mDialog = null;
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
         * @param backpack
         */
        void performPostAlertAction(Bundle backpack);
    }

    /**
     * Class to help building the Alert Dialog
     */
    public static class Builder {

        private TaskMandatoryFieldsDialog dialog = new TaskMandatoryFieldsDialog();

        /**
         * Constructor to build a mDialog for Activity
         *
         * @param activity
         */
        public Builder(Activity activity) {

            dialog.activity = activity;

            if (activity instanceof Listener)
                dialog.listener = (Listener) activity;
        }

        /**
         * Constructor to build a mDialog for Fragment
         *
         * @param fragment
         */
        public Builder(Fragment fragment) {

            dialog.activity = fragment.getActivity();

            if (fragment instanceof Listener)
                dialog.listener = (Listener) fragment;
        }

        /**
         * Sets the a custom listener to receive the CallBacks
         *
         * @param listener
         * @return
         */
        public Builder listener(Listener listener) {
            dialog.listener = listener;
            return this;
        }

        public Builder add(String item, String[] subItems, View.OnClickListener listener) {
            ListItem listItem = new ListItem(item, subItems, listener);
            dialog.itemsList.add(listItem);
            return this;
        }

        /**
         * Set the data to be sent via callback
         *
         * @param backpack
         * @return
         */
        public Builder backpack(Bundle backpack) {
            dialog.backpack = backpack;
            return this;
        }

        /**
         * Set the message for the AlertDialog
         *
         * @param message
         * @return
         */
        public Builder message(String message) {
            dialog.message = message;
            return this;
        }

        /**
         * Set the actionButton for the AlertDialog
         *
         * @param button
         * @return
         */
        public Builder button(String button) {
            dialog.actionButton = button;
            return this;
        }

        /**
         * Method to build an AlertDialog and display
         * it on the screen. The instance returned can
         * be used to manipulate the mDialog in future.
         *
         * @return
         */
        public TaskMandatoryFieldsDialog build() {

            dialog.message = Utils.assign(dialog.message, StorefrontCommonData.getString(dialog.activity, R.string.message));
            dialog.actionButton = Utils.assign(dialog.actionButton, StorefrontCommonData.getString(dialog.activity, R.string.ok_text));

            return dialog.init();
        }

        public Builder itemBullet(char bullet) {
            dialog.itemBullet = bullet;
            return this;
        }

        public Builder subItemBullet(char bullet) {
            dialog.subItemBullet = bullet;
            return this;
        }

        /**
         * Class to hold the List Items
         */
        public class ListItem {

            private String item;
            private String[] subItems;
            private View.OnClickListener listener;

            public ListItem(String item, String[] subItems, View.OnClickListener listener) {
                this.item = item;
                this.subItems = subItems;
                this.listener = listener;
            }

            public View.OnClickListener getListener() {
                return listener;
            }

            public String getItem() {
                return item;
            }

            public String[] getSubItems() {
                return subItems;
            }
        }
    }

}


