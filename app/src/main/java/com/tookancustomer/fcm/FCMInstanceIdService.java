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

import android.os.Handler;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

public class FCMInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    private static FCMTokenInterface fcmTokenCallback;
    private static Handler handlerOs = new Handler();
    private static String mtoken = "";

    public static void setCallback(FCMTokenInterface callback) {
        try {
            String token = mtoken;
            if (token != null && !token.isEmpty()) {
                callback.onTokenReceived(token);
                return;
            }
        } catch (Exception e) {
            Log.v("SetCallback EXP= ", e.toString());
        }
        fcmTokenCallback = callback;
        startHandler();
    }

    private static void startHandler() {
        handlerOs.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    fcmTokenCallback.onFailure();
                    fcmTokenCallback = null;
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }
        }, 10000);
    }

    private static void clearHandler() {
        handlerOs.removeCallbacksAndMessages(null);
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        mtoken = token;

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // sendRegistrationToServer(token);
        if (token != null && fcmTokenCallback != null) {
            fcmTokenCallback.onTokenReceived(token);
            fcmTokenCallback = null;
            clearHandler();
        }
    }

}
