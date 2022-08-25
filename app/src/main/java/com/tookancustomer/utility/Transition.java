package com.tookancustomer.utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.tookancustomer.AdminCategoryActivity;
import com.tookancustomer.CheckOutActivity;
import com.tookancustomer.CheckOutCustomActivity;
import com.tookancustomer.HostHomeActivity;
import com.tookancustomer.HyperlocalLandingActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.RentalHomeActivity;
import com.tookancustomer.RestaurantListingActivity;
import com.tookancustomer.SignInActivity;
import com.tookancustomer.SplashActivity;
import com.tookancustomer.TaskDetailsNewActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.modules.recurring.RecurringTaskDetailsActivity;

import java.util.ArrayList;

import static com.tookancustomer.appdata.Codes.Request.OPEN_CHECKOUT_SCREEN;
import static com.tookancustomer.appdata.Codes.Request.OPEN_CUSTOM_CHECKOUT_ACTIVITY;
import static com.tookancustomer.appdata.Codes.Request.OPEN_LOGIN_BEFORE_CHECKOUT;


public class Transition {

    /**
     * Jump to back to an {@link Activity} followed by Animation when
     * <code>isBack</code> is true
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param isBack      <code>true</code>, if firing a backevent
     */
    public static void transit(Activity fromContext, Class<?> toClass, boolean isBack, Bundle extras) {

        Intent intention = new Intent(fromContext, toClass);

        if (extras != null) {
            intention.putExtras(extras);
        }

        fromContext.startActivity(intention);

        fromContext.finish();

        if (isBack) {
            AnimationUtils.exitTransition(fromContext);
        } else {
            AnimationUtils.forwardTransition(fromContext);
        }
    }

    public static void transitBookingSuccess(Activity fromContext, Bundle extras) {
        Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());

//        if (UIManager.getIsNLevelApp()) {
//            Utils.launchStoreFront((UserData) extras.getSerializable(UserData.class.getName()), fromContext);
////            transit(fromContext, NLevelWorkFlowActivity.class, true, extras, true);
//        } else {
//            transit(fromContext, launchHomeActivity(), true, extras, true);
//        }

    }


    public static void transit(Activity fromContext, Class<?> toClass, boolean isBack, Bundle extras, boolean finishAffinity) {

        Intent intention = new Intent(fromContext, toClass);

        if (extras != null) {
            intention.putExtras(extras);
        }
        if (finishAffinity) {
            intention.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        fromContext.startActivity(intention);


        if (finishAffinity) {
            ActivityCompat.finishAffinity(fromContext);
        } else {
            fromContext.finish();
        }

        if (isBack) {
            AnimationUtils.exitTransition(fromContext);
        } else {
            AnimationUtils.forwardTransition(fromContext);
        }
    }

    /**
     * Jump to back to an {@link Activity} followed by Animation when
     * <code>isBack</code> is true
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param isBack      <code>true</code>, if firing a backevent
     */
    public static void transit(Activity fromContext, Class<?> toClass, boolean isBack) {

        transit(fromContext, toClass, isBack, null);
    }

    /**
     * Jump forward to an activity
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     */
    public static void transit(Activity fromContext, Class<?> toClass) {

        transit(fromContext, toClass, false, null);
    }

    /**
     * Jump forward to an activity
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param extra       The extras {@link String} parameter to be transmit
     */
    public static void transit(Activity fromContext, Class<?> toClass, String extra) {

        Bundle extras = new Bundle();
        extras.putBoolean(extra, true);

        transit(fromContext, toClass, false, extras);
    }

    /**
     * Jump forward to an activity
     *
     * @param fromContext Current {@link Activity}
     * @param toClass     Intended {@link Activity}
     * @param extras      The extras {@link String} parameter to be transmit
     */
    public static void transit(Activity fromContext, Class<?> toClass, Bundle extras) {

        transit(fromContext, toClass, false, extras);
    }

    /**
     * Transit forward to an Activity with some data,leaving current one alive
     *
     * @param fromContext current activity
     * @param toClass     the intended activity
     * @param requestCode the request code to look up
     * @param extras      the data to be tunneled towards the intended activity
     */
    public static void transitForResult(Activity fromContext, Class<?> toClass, int requestCode, Bundle extras) {

        Intent intention = new Intent(fromContext, toClass);

        if (extras != null) {
            intention.putExtras(extras);
        }

        fromContext.startActivityForResult(intention, requestCode);
        AnimationUtils.forwardTransition(fromContext);
    }

    /**
     * Transit forward to an Activity with some data,leaving current one alive
     *
     * @param fromContext current activity
     * @param toClass     the intended activity
     * @param requestCode the request code to look up
     * @param extras      the data to be tunneled towards the intended activity
     */
    public static void transitForResult(Activity fromContext, Class<?> toClass, int requestCode, Bundle extras, boolean finish) {

        Intent intention = new Intent(fromContext, toClass);

        if (extras != null) {
            intention.putExtras(extras);
        }

        fromContext.startActivityForResult(intention, requestCode);

        if (finish) {
            fromContext.finish();
        }

        AnimationUtils.forwardTransition(fromContext);
    }

    /**
     * Exits the current activity
     *
     * @param fromContext the activity to be finished
     */
    public static void exit(Activity fromContext) {
        fromContext.finish();
        AnimationUtils.exitTransition(fromContext);
    }

    /**
     * Exits the current activity
     *
     * @param fromContext the activity to be finished
     */
    public static void exit(Activity fromContext, Class<?> toClass, Bundle extras) {
        Intent intention = new Intent(fromContext, toClass);
        if (extras != null) {
            intention.putExtras(extras);
        }
        fromContext.startActivity(intention);
        fromContext.finish();
        AnimationUtils.exitTransition(fromContext);
    }

    public static void startActivity(Activity fromContext, Class<?> toClass) {
        Intent intention = new Intent(fromContext, toClass);
        fromContext.startActivity(intention);
        AnimationUtils.forwardTransition(fromContext);
    }

    public static void startActivity(Activity fromContext, Class<?> toClass, Bundle extras, boolean finish) {
        Intent intention = new Intent(fromContext, toClass);
        if (extras != null) {
            intention.putExtras(extras);
        }

        fromContext.startActivity(intention);

        if (finish) {
            fromContext.finish();
        }

        AnimationUtils.forwardTransition(fromContext);
    }

    public static Class<?> launchHomeActivity() {
        if (StorefrontCommonData.getUserData() != null && Dependencies.isEcomApp()/*StorefrontCommonData.getUserData().getData().getAdminCategoryEnabled() == 1*/) {
            return AdminCategoryActivity.class;
        } else if (UIManager.getNlevelEnabled() == 1/*StorefrontCommonData.getUserData().getData().getAdminCategoryEnabled() == 1*/) {
            return AdminCategoryActivity.class;
        } else if (StorefrontCommonData.getUserData() != null && StorefrontCommonData.getFormSettings().getProductView() == 1) {
            if (StorefrontCommonData.getUserData().getData().getVendorDetails().getDualUserCurrentStatus() == 0) {
                return RentalHomeActivity.class;
            } else {
                return HostHomeActivity.class;
            }
            //  return NLevelWorkFlowActivity.class;
        } else {/* For hyperlocal case including laundry */
            if (StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 1 && UIManager.getCustomOrderActive()) {
                return HyperlocalLandingActivity.class;
            } else if (UIManager.getCustomOrderActive() && StorefrontCommonData.getAppConfigurationData().getIsLandingPageEnable() == 0 &&
                    StorefrontCommonData.getAppConfigurationData().getEnabledMarketplaceStorefront().size() == 0) {
                return CheckOutCustomActivity.class;
            } else {
                return RestaurantListingActivity.class;
            }
        }
    }

    public static Class<?> launchOrderDetailsActivity() {
        return TaskDetailsNewActivity.class;
    }


    public static Class<?> launchRecurringDetailsActivity() {
        return RecurringTaskDetailsActivity.class;
    }


    public static void openCheckoutActivity(final Activity mActivity, final Bundle extraa) {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_CHECKOUT);

        if (UIManager.isCustomerLoginRequired() || (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())
                || (Dependencies.getGuestCheckoutFlowOngoing(mActivity) == 1 && Dependencies.getAccessTokenGuest(mActivity) != null && !Dependencies.getAccessTokenGuest(mActivity).isEmpty())) {
            Intent intent1 = new Intent(mActivity, CheckOutActivity.class);
            intent1.putExtras(extraa);
            mActivity.startActivityForResult(intent1, OPEN_CHECKOUT_SCREEN);
        } else {
            Dependencies.setDemoRun(false);

            Intent intent;
            if (UIManager.isFacebookAvailable() || UIManager.isInstagramAvailable() || UIManager.isGPlusAvailable() || UIManager.isOtpLoginAvailable()) {
                intent = new Intent(mActivity, SplashActivity.class);
            } else {
                intent = new Intent(mActivity, SignInActivity.class);
            }
            intent.putExtra(CheckOutActivity.class.getName(), true);
            intent.putExtras(extraa);
            mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
        }
        AnimationUtils.forwardTransition(mActivity);


    }


    public static void openCustomCheckoutActivity(final Activity mActivity, final Bundle extraa) {
        MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.GO_TO_CHECKOUT);

        if (UIManager.isCustomerLoginRequired() || (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty())) {
            Intent intent1 = new Intent(mActivity, CheckOutCustomActivity.class);
            intent1.putExtras(extraa);
            mActivity.startActivityForResult(intent1, OPEN_CUSTOM_CHECKOUT_ACTIVITY);
            AnimationUtils.forwardTransition(mActivity);
        } else {
            Dependencies.setDemoRun(false);

            Intent intent;
            if (UIManager.isFacebookAvailable() || UIManager.isInstagramAvailable() || UIManager.isGPlusAvailable() || UIManager.isOtpLoginAvailable()) {
                intent = new Intent(mActivity, SplashActivity.class);
            } else {
                intent = new Intent(mActivity, SignInActivity.class);
            }
            intent.putExtra(CheckOutActivity.class.getName(), true);
            intent.putExtras(extraa);
            mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
            AnimationUtils.forwardTransition(mActivity);


//            new OptionsDialog.Builder(mActivity).message(R.string.not_logged_in_user_please_login_first).listener(new OptionsDialog.Listener() {
//                @Override
//                public void performPositiveAction(int purpose, Bundle backpack) {
//                }
//
//                @Override
//                public void performNegativeAction(int purpose, Bundle backpack) {
//                }
//            }).build().show();

        }
    }

//    public static void openTimeAvailabelActivity(final Activity mActivity) {
//        MyApplication.getInstance().trackEvent(SELECT_AVAILABLE_DATE);
//
//        Intent intent;
//        if (UIManager.getBusinessModelType().equalsIgnoreCase("Rental")) {
//            intent = new Intent(mActivity, DatesOnCalendarActivity.class);
//        } else {
//            intent = new Intent(mActivity, ScheduleTimeActivity.class);
//        }
//
//        intent.putExtra(KEY_ITEM_POSITION, itemPos);
//        intent.putExtra(PRODUCT_CATALOGUE_DATA, menusItemCustomizeAdapter.getItem());
//        intent.putExtra(IS_SCHEDULING_FROM_CHECKOUT, false);
//        intent.putExtra(IS_START_TIME, true);
//        intent.putExtra(SELECTED_DATE, "");
//        mActivity.startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
//        AnimationUtils.forwardTransition(mActivity);
//
//    }


}
