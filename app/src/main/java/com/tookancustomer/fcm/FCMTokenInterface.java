package com.tookancustomer.fcm;

/**
 * Created by clicklabs on 5/27/16.
 */
public interface FCMTokenInterface {


    void onTokenReceived(String token);

    void onFailure();
}
