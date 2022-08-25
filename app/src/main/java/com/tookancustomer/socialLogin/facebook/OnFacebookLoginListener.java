package com.tookancustomer.socialLogin.facebook;


public interface OnFacebookLoginListener {

    /**
     * Override this method to get Login ListingData
     *
     * @param type
     * @param loginData
     */
    public void onSocialLogin(int type, FacebookLoginData loginData);

    public void onCancel();

    public void onError();

}
