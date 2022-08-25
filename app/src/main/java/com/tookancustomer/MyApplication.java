package com.tookancustomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hippo.activity.HippoActivityLifecycleCallback;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.AdditionalPaymentStatusData;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.taskdetails.TaskData;
import com.tookancustomer.models.taskdetails.TaskDetails;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.wonderkiln.blurkit.BlurKit;

import net.danlew.android.joda.JodaTimeAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.paperdb.Paper;

import static com.hippo.constant.FuguAppConstant.FUGU_CUSTOM_ACTION_PAYLOAD;
import static com.hippo.constant.FuguAppConstant.FUGU_CUSTOM_ACTION_SELECTED;

public class MyApplication extends MultiDexApplication {
    public static Application mContext;
    private static MyApplication mInstance;
    private static GoogleAnalytics sAnalytics, clientGoogleAnalytics;
    public boolean isShowOftenBoughtDialog = true;
    // private static Tracker sTracker, clientTracker;
    private Activity mCurrentActivity = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Toast toast;
    private int selectedPickUpMode = 0;
    private int deepLinkMerchantId = 0;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public int getDeepLinkMerchantId() {
        return deepLinkMerchantId;
    }

    public void setDeepLinkMerchantId(int deepLinkMerchantId) {
        this.deepLinkMerchantId = deepLinkMerchantId;
    }

    public boolean isShowOftenBoughtDialog() {
        return isShowOftenBoughtDialog;
    }

    public void setShowOftenBoughtDialog(boolean showOftenBoughtDialog) {
        isShowOftenBoughtDialog = showOftenBoughtDialog;
    }

    public int getSelectedPickUpMode() {
        return selectedPickUpMode;
    }

    public void setSelectedPickUpMode(int selectedPickUpMode) {
        this.selectedPickUpMode = selectedPickUpMode;
    }


    @Override
    public void onCreate() {
        HippoActivityLifecycleCallback.register(this);
        super.onCreate();
        mContext = this;
//sAnalytics = GoogleAnalytics.getInstance(this);
//clientGoogleAnalytics = GoogleAnalytics.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        JodaTimeAndroid.init(this);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Dependencies.saveAccessTokenGuest(getApplicationContext(), "");
        Dependencies.setGuestCheckoutFlowOngoing(getApplicationContext(), 0);


/**
 * Initialize Paper db first berfore performing database addition or deletion
 */
        Paper.init(getApplicationContext());

/**
 * Initialize the SDK before executing any other operations,
 */
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mInstance = this;

        BlurKit.init(this);
        try {

            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());

        } catch (Exception e) {
            Utils.printStackTrace(e);
        }
        if (BuildConfig.DEBUG)
            Utils.printKeyHash(this);


        this.registerReceiver(new BroadcastReceiver() {
            @SuppressLint("LongLogTag")
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("onReceive==PayViaHippoReceiver", intent.getStringExtra(FUGU_CUSTOM_ACTION_PAYLOAD));
                JSONObject payViaHippoObject = null;
                try {
                    payViaHippoObject = new JSONObject(intent.getStringExtra(FUGU_CUSTOM_ACTION_PAYLOAD));

                    if (payViaHippoObject.has("is_custom_order") && payViaHippoObject.getInt("is_custom_order") == 1) {

                        getAdditionalPaymentStatus(payViaHippoObject, payViaHippoObject.getString("order_id"));

                    } else {

                        getTaskDetails(payViaHippoObject, payViaHippoObject.getString("order_id"), false);
                    }

//                    getPaymentMethods(payViaHippoObject, payViaHippoObject.getString("order_id"));

                } catch (JSONException e) {
                    Utils.printStackTrace(e);
                }
// Intent paymentViaHippoIntent = new Intent(MyApplication.this, MakePaymentActivity.class);
// paymentViaHippoIntent.setAction("PAYMENT_VIA_HIPPO");
// paymentViaHippoIntent.putExtra("PAYMENT_VIA_HIPPO_DATA", intent.getStringExtra(FUGU_CUSTOM_ACTION_PAYLOAD));
// startActivity(paymentViaHippoIntent);
            }
        }, new IntentFilter(FUGU_CUSTOM_ACTION_SELECTED));

    }


    private void getAdditionalPaymentStatus(final JSONObject payload, final String orderId) {

        HashMap<String, String> paymenthashMap = new HashMap<>();

        paymenthashMap.put(Keys.Prefs.ACCESS_TOKEN, Dependencies.getAccessToken(this));
        paymenthashMap.put(Keys.APIFieldKeys.MARKETPLACE_USER_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getMarketplaceUserId() + "");
        paymenthashMap.put(Keys.APIFieldKeys.VENDOR_ID, StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "");
        paymenthashMap.put("job_id", orderId);

        try {
            paymenthashMap.put("additonal_payment_id", payload.getInt("additionalpaymentId") + "" + "");
        } catch (JSONException e) {
            Utils.printStackTrace(e);
        }

        RestClient.getApiInterface(this).getAdditionalPaymentStatus(paymenthashMap).enqueue(new ResponseResolver<BaseModel>() {
            @Override
            public void success(BaseModel baseModel) {
                Log.e("sdsds", baseModel + "");

                ArrayList<AdditionalPaymentStatusData> additionalPaymentStatusData = new Gson().fromJson(new Gson().toJson(baseModel.data),
                        new TypeToken<ArrayList<AdditionalPaymentStatusData>>() {
                        }.getType());

                if (additionalPaymentStatusData.size() > 0 && additionalPaymentStatusData.get(0).getTransactionStatus() == 0) {
                    getTaskDetails(payload, orderId, additionalPaymentStatusData.get(0).getTransactionStatus() == 0);

                } else {
                    Toast.makeText(mContext, StorefrontCommonData.getString(mContext, R.string.payment_already_completed), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {

            }
        });
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    public Toast getToast() {
        return toast;
    }

    public void setToast(Toast toast) {
        this.toast = toast;
    }


    /* *//**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     *//*
    synchronized public Tracker getGoogleAnalyticsTracker() {
// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.app_tracker);
        }

        return sTracker;
    }*/

    /*  *//**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     *//*
    synchronized public Tracker getClientGoogleAnalyticsTracker() {
// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (clientTracker == null) {
            clientTracker = clientGoogleAnalytics.newTracker(StorefrontCommonData.getAppConfigurationData().getGoogleAnalyticsTrackingId());
        }

        return clientTracker;
    }*/

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
/*if (Dependencies.isDemoGARequired()) {
Tracker tracker = getGoogleAnalyticsTracker();
trackScreenView(tracker, "ANDROID-" + UIManager.getBusinessModelType() + "-" + getString(R.string.app_name) + "-" + screenName);
}
if (StorefrontCommonData.getAppConfigurationData() != null)
if (StorefrontCommonData.getAppConfigurationData().getGoogleAnalyticsIsActive() == 1) {
Tracker clientTracker = getClientGoogleAnalyticsTracker();
trackScreenView(clientTracker, "ANDROID-" + screenName);
}*/
    }

    public void trackScreenView(Tracker t, String screenName) {
/*// Set screen name.
t.setScreenName(screenName);

// Send a screen view.
t.send(new HitBuilders.ScreenViewBuilder().build());
*/
// GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param
     */
   /* public void trackException(Exception e) {
        if (Dependencies.isDemoGARequired()) {
            if (e != null) {
                Tracker t = getGoogleAnalyticsTracker();

                t.send(new HitBuilders.ExceptionBuilder()
                        .setDescription(new StandardExceptionParser(this, null).getDescription(Thread.currentThread().getName(), e))
                        .setFatal(false)
                        .build()
                );
            }
        }
    }*/
    public void trackEvent(Constants.GoogleAnalyticsValues trackEventValue) {
        trackEvent(trackEventValue, "0");
    }

    public void trackEvent(Constants.GoogleAnalyticsValues trackEventValue, Bundle bundle) {
        if (Dependencies.isDemoGARequired()) {
            mFirebaseAnalytics.logEvent(trackEventValue.gaString, bundle);
        }
    }

    public void trackEvent(Constants.GoogleAnalyticsValues trackEventValue, String label) {

// Build and send an Event.
        if (Dependencies.isDemoGARequired()) {
            Bundle bundle = new Bundle();
            bundle.putString(trackEventValue.gaString, label);
            mFirebaseAnalytics.logEvent(trackEventValue.gaString, bundle);
        }


        try {
            if (StorefrontCommonData.getAppConfigurationData() != null &&
                    StorefrontCommonData.getAppConfigurationData().getGoogleAnalyticsIsActive() == 1) {
                // Tracker clientTracker = getClientGoogleAnalyticsTracker();

                if (UIManager.getGoogleAnalyticsOptionsHashMap().containsKey(trackEventValue.gaId) && UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getIsActive() == 1) {
               /* trackEvent(clientTracker, UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getCategoryName(),
                        UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getActionName(), label, 0);*/
                    Bundle mBundle = new Bundle();
                    mBundle.putString("category_name", UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getCategoryName());
                    mBundle.putString("action_name", UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getActionName());
                    mBundle.putString("label", label);
                    mFirebaseAnalytics.logEvent(trackEventValue.gaString, mBundle);
                }

            }

          /*  @Override
            public void logFeatureSelectedEvent(String categoryName, String actionName, long value) {
                Bundle bundle = new Bundle();
                bundle.putString(EventTrackingKeys.EventTypes.CATEGORY, categoryName);
                bundle.putString(EventTrackingKeys.EventTypes.ACTION, actionName);
                bundle.putLong(EventTrackingKeys.EventTypes.VALUE, value);
                mFirebaseAnalytics.setUserProperty(EventTrackingKeys.EventTypes.CATEGORY, categoryName);
                mFirebaseAnalytics.setUserProperty(EventTrackingKeys.EventTypes.ACTION, actionName);
                mFirebaseAnalytics.setUserProperty(EventTrackingKeys.EventTypes.VALUE, value);
                mFirebaseAnalytics.logEvent(EventTrackingKeys.EventAnalyticTypes.FEATURE_SELECTED_EVENT, bundle);
            }*/
/*if (!Dependencies.isDemoGARequired()) {
Tracker t = getGoogleAnalyticsTracker();
trackEvent(t, "ANDROID-" + UIManager.getBusinessModelType() + "-" + getString(R.string.app_name), trackEventValue.gaString, label, 0);
}

if (StorefrontCommonData.getAppConfigurationData().getGoogleAnalyticsIsActive() == 1) {
Tracker clientTracker = getClientGoogleAnalyticsTracker();

if (UIManager.getGoogleAnalyticsOptionsHashMap().containsKey(trackEventValue.gaId) && UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getIsActive() == 1) {
trackEvent(clientTracker, UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getCategoryName(),
UIManager.getGoogleAnalyticsOptionsHashMap().get(trackEventValue.gaId).getActionName(), label, 0);
}
}*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trackEvent(Tracker t, String category, String action, String label, long value) {

        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).setValue(value).build());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("LOW", "onLowMemory called");
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }


    private void getTaskDetails(final JSONObject payload, String orderId, final boolean isCustomOrderRepayment) {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(this, StorefrontCommonData.getUserData());
        commonParams.add("job_id", orderId);
        commonParams.add("business_model_type", UIManager.getBusinessModelType());

        RestClient.getApiInterface(this).getJobDetails(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>() {
                    @Override
                    public void success(BaseModel baseModel) {

                        TaskDetails taskDetails = new TaskDetails();
                        TaskData[] taskDataa = baseModel.toResponseModel(TaskData[].class);
                        taskDetails.setData(new ArrayList<TaskData>(Arrays.asList(taskDataa)));

                        TaskData taskData = taskDetails.getData().get(0);

                        if (isCustomOrderRepayment) {
                            navigateToPaymentsPage(payload, taskData, isCustomOrderRepayment);

                        } else if (taskData.getTransactionStatus() == Constants.TransactionStatus.TRANSACTION_COMPLETE ||
                                taskData.getTransactionStatus() == Constants.TransactionStatus.ORDER_COMPlETE) {

                            if (taskData.getOverallTransactionStatus() == Constants.TransactionStatus.TRANSACTION_COMPLETE ||
                                    taskData.getOverallTransactionStatus() == Constants.TransactionStatus.ORDER_COMPlETE) {
                                Toast
                                        .makeText(mContext, StorefrontCommonData.getString(mContext, R.string.payment_already_completed), Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                navigateToPaymentsPage(payload, taskData, isCustomOrderRepayment);
                            }

                        } else {
                            if (taskData.getJobStatus() == Constants.TaskStatus.DELIVERED.value ||
                                    taskData.getJobStatus() == Constants.TaskStatus.REJECTED.value ||
                                    taskData.getJobStatus() == Constants.TaskStatus.CANCELLED.value) {
                                Toast
                                        .makeText(mContext, StorefrontCommonData.getString(mContext, R.string.payment_already_completed), Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                navigateToPaymentsPage(payload, taskData, isCustomOrderRepayment);
                            }
                        }


                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                    }
                });
    }


    private void navigateToPaymentsPage(final JSONObject payload, TaskData taskData, final boolean isCustomOrderRepayment) {
        try {
            payload.put("payment_method", taskData.getPaymentType());
        } catch (JSONException e) {
//e.printStackTrace();
        }

        if (taskData != null && taskData.getUserTaxes().size() > 0) {
            for (int i = 0; i < taskData.getUserTaxes().size(); i++)
                taskData.getUserTaxes().get(i).setCurrencySymbol(taskData.getOrderCurrencySymbol());
        }
        Intent paymentViaHippoIntent = new Intent(MyApplication.this, MakePaymentActivity.class);
        paymentViaHippoIntent.setAction("PAYMENT_VIA_HIPPO");
        paymentViaHippoIntent.putExtra("PAYMENT_VIA_HIPPO_DATA", payload.toString());
        if (!isCustomOrderRepayment) {
            paymentViaHippoIntent.putExtra("HIPPO_TAXES_LIST", taskData.getUserTaxes());
            paymentViaHippoIntent.putExtra("PAYMENT_METHODS", (Serializable) taskData.getPaymentMethods());
            paymentViaHippoIntent.putExtra("USER_ID", taskData.getUserId());
            paymentViaHippoIntent.putExtra("CURRENCY_SYMBOL", taskData.getOrderCurrencySymbol());
        }
        paymentViaHippoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(paymentViaHippoIntent);


    }

}
