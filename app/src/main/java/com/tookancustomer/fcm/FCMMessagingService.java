/**
 * Copyright Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tookancustomer.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hippo.HippoNotificationConfig;
import com.tookancustomer.R;
import com.tookancustomer.SplashActivity;
import com.tookancustomer.TasksActivity;
import com.tookancustomer.UserDebtActivity;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.Utils;

import java.util.List;

import static com.tookancustomer.appdata.Keys.Extras.JOB_ID;

public class FCMMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFMService";
    public static NotificationManager mNotificationManager;
    private static final String DEFAULT_CHANNEL_ID = "default_01";
    private static final long[] NOTIFICATION_VIBRATION_PATTERN = new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400};
    private static final String TAG_ID = "MyFirebaseIIDService";
    private static FCMTokenInterface fcmTokenCallback;
    private static Handler handlerOs = new Handler();

    public static void clearNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG_ID, "FCM Token: " + token);

        if (token != null && fcmTokenCallback != null) {
            fcmTokenCallback.onTokenReceived(token);
            fcmTokenCallback = null;
            //clearHandler();
        }
    }

//    public static void setCallback(FCMTokenInterface callback) {
//        try {
//            String token = FirebaseInstanceId.getInstance().getToken();
//            if (token != null && !token.isEmpty()) {
//                callback.onTokenReceived(token);
//                return;
//            }
//        } catch (Exception e) {
//            Log.v("SetCallback EXP= ", e.toString());
//        }
//        fcmTokenCallback = callback;
//        startHandler();
//    }
//
//
//    private static void startHandler() {
//        handlerOs.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    fcmTokenCallback.onFailure();
//                    fcmTokenCallback = null;
//                } catch (Exception e) {
//                    if (BuildConfig.DEBUG)
//                                Utils.printStackTrace(e);
//                }
//            }
//        }, 10000);
//    }
//
//    private static void clearHandler() {
//        handlerOs.removeCallbacksAndMessages(null);
//    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM remoteMessage " + remoteMessage);
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM ListingData Message: " + remoteMessage.getData());

//        FuguNotificationConfig fuguNotificationConfig = new FuguNotificationConfig();
//        if (fuguNotificationConfig.isFuguNotification(remoteMessage.getData())) {
//            fuguNotificationConfig.setSmallIcon(R.mipmap.icon_push); //your icon drawable
//            fuguNotificationConfig.setLargeIcon(R.mipmap.ic_launcher);
//            fuguNotificationConfig.setNotificationSoundEnabled(true);
//            fuguNotificationConfig.setPriority(NotificationCompat.PRIORITY_HIGH);
//            fuguNotificationConfig.showNotification(getApplicationContext(), remoteMessage.getData());
//        } else {

        HippoNotificationConfig fuguNotificationConfig = new HippoNotificationConfig();
        if (fuguNotificationConfig.isHippoNotification(remoteMessage.getData())) {
            fuguNotificationConfig.setSmallIcon(R.mipmap.ic_notif); //your icon drawable
            fuguNotificationConfig.setLargeIcon(R.mipmap.ic_launcher);
            fuguNotificationConfig.setNotificationSoundEnabled(true);
            fuguNotificationConfig.setPriority(NotificationCompat.PRIORITY_HIGH);
            fuguNotificationConfig.showNotification(getApplicationContext(), remoteMessage.getData());
        } else {
            // Your logic goes here
            String jobStatus = "0", jobID = "0", flag = "0", title = "", message = "", pushID = "";
//            {job_status=6, job_id=1556, flag=16, title=Order Placed, message=Order placed successfully.}


//            job_status=10, job_id=55787, push_id=57546, flag=68, title=Attention required!, message=Pending amount of   has been added against your order ID . Need your attention}
            try {
                jobStatus = String.valueOf(remoteMessage.getData().get("job_status"));
                jobID = String.valueOf(remoteMessage.getData().get("job_id"));
                flag = String.valueOf(remoteMessage.getData().get("flag"));
                title = String.valueOf(remoteMessage.getData().get("title"));
                message = String.valueOf(remoteMessage.getData().get("message"));
                pushID = String.valueOf(remoteMessage.getData().get("push_id"));

                if (jobID.equalsIgnoreCase("null") || jobID.isEmpty()) {
                    jobID = "0";
                }


                if (jobStatus != null && jobID != null && flag != null && title != null && message != null && pushID != null)
                    handlePush(jobID, title, message, Integer.valueOf(flag), Integer.valueOf(jobStatus), pushID);

            } catch (Exception e) {

                               Utils.printStackTrace(e);
            }
        }
    }

    protected void handlePush(final String jobID, final String title, final String message, final int flag, final int jobStatus, final String pushID) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (!Dependencies.getAccessToken(getApplicationContext()).isEmpty()) {
                    boolean background = isAppIsInBackground(getApplicationContext());

                    if (!background) {
                        dismissNotificationDelay();
                    }

                    makeNotification(getApplicationContext(), jobID, title, message, flag, jobStatus, pushID);
                }
            }
        });
    }

    protected void makeNotification(Context context, final String jobID, final String title, final String message, final int flag, final int jobStatus, final String pushID) {
        try {
//            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
//            String activityName = "";
//            if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
//                Log.e("ACTIVITY_NAME", taskList.get(0).topActivity.getClassName());
//                activityName = taskList.get(0).topActivity.getClassName();
//            } else {
//                Log.e("ACTIVITY_NAME1", taskList.get(0).topActivity.getClassName());
//                activityName = taskList.get(0).topActivity.getClassName();
//            }

            Intent intent = new Intent("refresh");
            intent.putExtra("message", message);
            intent.putExtra("jobId", jobID);
            intent.putExtra("jobStatus", jobStatus);
            intent.putExtra("flag", flag);
            intent.putExtra("pushID", pushID);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            int requestID = (int) System.currentTimeMillis();
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(message);
            boolean background = isAppIsInBackground(getApplicationContext());
            Intent dismissIntent;
            if (background) {
                dismissIntent = new Intent(context, SplashActivity.class);
            } else {
                if (flag == Constants.NotificationFlags.USER_DEBT_PENDING) {
                    dismissIntent = new Intent(context, UserDebtActivity.class);
                } else if ((flag == Constants.NotificationFlags.RULE_ACCEPTED) || (flag == Constants.NotificationFlags.RULE_REJECTED)
                        || (flag == Constants.NotificationFlags.RECURRING_TASK_CREATION_FAIL) || (flag == Constants.NotificationFlags.RULE_CREATED)) {
                    dismissIntent = new Intent(context, TasksActivity.class);

                } else {
                    dismissIntent = new Intent(context, Transition.launchOrderDetailsActivity());
                }
            }
            dismissIntent.putExtra(JOB_ID, Integer.valueOf(jobID));
            if (background) {
                dismissIntent.putExtra("from", "notification");
            } else {
                dismissIntent.putExtra("from", 1);
            }
            dismissIntent.putExtra("pushID", pushID);
            dismissIntent.putExtra("flag", flag);
            dismissIntent.putExtra("jobStatus", jobStatus);
//            dismissIntent.putExtra("form_id", StorefrontCommonData.getUserData().getData().getFormSettings().get(0).getFormId());
            dismissIntent.putExtra(UserData.class.getName(), StorefrontCommonData.getUserData());

            dismissIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

//            if (!background && getApplicationContext() instanceof TaskDetailsActivity) {
//            }

            if (!background) {
                dismissIntent.setAction(Intent.ACTION_MAIN);
                dismissIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            }
            PendingIntent piDismiss = PendingIntent.getActivity(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // The user-visible name of the channel.
                CharSequence name = StorefrontCommonData.getString(this, R.string.notification_channel_default);
                // The user-visible description of the channel.
                String description = StorefrontCommonData.getString(this, R.string.notification_channel_description_default);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(DEFAULT_CHANNEL_ID, name, importance);
                // Configure the notification channel.
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(NOTIFICATION_VIBRATION_PATTERN);
                mNotificationManager.createNotificationChannel(mChannel);
            }


            NotificationCompat.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_notif)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("" + message)
//                        .setAutoCancel(true)
                        .setStyle(bigTextStyle);

            } else {
                builder = new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_notif)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("" + message)
                        .setColor(Color.rgb(255, 153, 255))
                        .setStyle(bigTextStyle);
            }
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setVibrate(new long[]{1000, 1000});
            builder.setContentIntent(piDismiss);
            mNotificationManager.notify(Integer.parseInt(pushID), builder.build());

        } catch (Exception e) {

                               Utils.printStackTrace(e);
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            if (runningProcesses != null)
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    private void dismissNotificationDelay() {
        new CountDownTimer(7000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.e("TICK", millisUntilFinished + "");
            }

            public void onFinish() {
                Log.e(TAG, "finished");
                clearNotification();
            }

        }.start();
    }

}