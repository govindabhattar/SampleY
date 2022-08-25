package com.tookancustomer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tookancustomer.R;
import com.tookancustomer.RegistrationOnboardingActivity;
import com.tookancustomer.SignupCustomFieldsActivity;
import com.tookancustomer.adapter.ViewImagesAdapterSignup;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;


/**
 * Developer: Mohit Kr. Dhiman
 * Dated: 23/07/15.
 */
public class ViewImagesDialogProfile {

    private static final String TAG = ViewImagesDialogProfile.class.getSimpleName();


    private Dialog mDialog;

    /**
     * The instance of the Activity on which the
     * AlertDialog will be displayed
     */
    private Activity activity;
    private Fragment fragment;

    /**
     * Title for the Dialog
     */
    private String title;

    /**
     * The receiver to which the AlertDialog
     * will return the CallBacks
     * <p>
     * Note: listener should be an instance of
     * AlertDialog.Listener
     */
    private Listener listener;

    /**
     * The code to differentiate the various CallBacks
     * from different Dialogs
     */
    private ArrayList<String> imagesList = new ArrayList<>();

    /**
     * The data to sent via the Dialog from the
     * remote parts of the Activity to other
     * parts
     */
    private Bundle backpack;

    /**
     * Separates different requests
     */
    private int purpose;

    /**
     * Stores the Position of Current Item
     */
    private int currentItem;

    private ViewPager vpImages;
    private TextView tvTitle;
    private ViewImagesAdapterSignup viewImagesAdapter;

    /**
     * Method to create and display the alert mDialog
     *
     * @return
     */
    private ViewImagesDialogProfile init() {

        try {

            mDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
            mDialog.setContentView(R.layout.dialog_view_images);

            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialogWindow.getAttributes().windowAnimations = R.style.CustomDialog;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);

            tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);

            View rlBack = mDialog.findViewById(R.id.rlBack);
            rlBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            if (activity instanceof SignupCustomFieldsActivity || activity instanceof RegistrationOnboardingActivity) {
                View rlDelete = mDialog.findViewById(R.id.rlDelete);
                // rlDelete.setVisibility(View.VISIBLE);
                rlDelete.setVisibility(View.GONE);
                rlDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkDeleteStatus(currentItem);
                    }
                });
            }

            inflateIndicators(currentItem);

            vpImages = (ViewPager) mDialog.findViewById(R.id.vpImages);
            vpImages.setAdapter(viewImagesAdapter = new ViewImagesAdapterSignup(activity, imagesList));
            vpImages.setCurrentItem(currentItem);

            vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    inflateIndicators(position);
                    currentItem = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {

            Log.e(TAG, "init(): " + e);
        }

        return this;
    }

    /**
     * Method to inflate the indicator for the
     */
    private void inflateIndicators(int position) {

        tvTitle.setText(position + 1 + " " + StorefrontCommonData.getString(activity, R.string.of_text) + " " + imagesList.size());
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

    public void checkDeleteStatus(final int position) {


        final String addImageItem = imagesList.get(position);


        new OptionsDialog.Builder(activity)
                .message(StorefrontCommonData.getString(activity, R.string.delete_this_image_text))
                .listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {


                        dismiss();
                        if (activity instanceof SignupCustomFieldsActivity) {
                            SignupCustomFieldsActivity signupCustomFieldsActivity = (SignupCustomFieldsActivity) activity;
                            UIManager.isPickup = true;
                            signupCustomFieldsActivity.deleteCustomFieldImageSignup(position);
                        } else if (activity instanceof RegistrationOnboardingActivity) {
                            RegistrationOnboardingActivity registrationOnboardingActivity = (RegistrationOnboardingActivity) activity;
                            UIManager.isPickup = true;
                            registrationOnboardingActivity.deleteCustomFieldImageSignup(position);
                        }
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {

                    }
                }).build().show();


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
        void onBackPressed(int purpose, Bundle backpack);
    }

    /**
     * Class to help building the Alert Dialog
     */
    public static class Builder {


        private ViewImagesDialogProfile viewImagesDialog = new ViewImagesDialogProfile();

        /**
         * Constructor to build a mDialog for Activity
         *
         * @param activity
         */
        public Builder(Activity activity) {

            viewImagesDialog.activity = activity;


            if (activity instanceof Listener)
                viewImagesDialog.listener = (Listener) activity;
        }

        /**
         * Constructor to build a mDialog for Fragment
         *
         * @param fragment
         */
        public Builder(Fragment fragment) {

            viewImagesDialog.activity = fragment.getActivity();
            viewImagesDialog.fragment = fragment;


            if (fragment instanceof Listener)
                viewImagesDialog.listener = (Listener) fragment;
        }

        /**
         * Sets the a unique purpose code to differentiate
         * between the CallBacks
         *
         * @param title
         * @return
         */
        public Builder title(String title) {
            viewImagesDialog.title = title;
            return this;
        }

        /**
         * Sets the a unique purpose code to differentiate
         * between the CallBacks
         *
         * @param purpose
         * @return
         */
        public Builder purpose(int purpose) {
            viewImagesDialog.purpose = purpose;
            return this;
        }

        /**
         * The position of the image to be displayed
         *
         * @param currentItem
         * @return
         */
        public Builder position(int currentItem) {
            viewImagesDialog.currentItem = currentItem;
            return this;
        }

        /**
         * Sets the a custom listener to receive the CallBacks
         *
         * @param listener
         * @return
         */
        public Builder listener(Listener listener) {
            viewImagesDialog.listener = listener;
            return this;
        }

        /**
         * Set the data to be sent via callback
         *
         * @param backpack
         * @return
         */
        public Builder backpack(Bundle backpack) {
            viewImagesDialog.backpack = backpack;
            return this;
        }

        /**
         * Method to build an AlertDialog and display
         * it on the screen. The instance returned can
         * be used to manipulate the mDialog in future.
         *
         * @return
         */
        public ViewImagesDialogProfile build() {

            viewImagesDialog.title = Utils.assign(viewImagesDialog.title,
                    StorefrontCommonData.getString(viewImagesDialog.activity, R.string.view_images_text));

            return viewImagesDialog.init();
        }

        public Builder images(ArrayList<String> imagesList) {
            viewImagesDialog.imagesList = imagesList;
            return this;
        }
    }
}


