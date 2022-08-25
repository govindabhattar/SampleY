package com.tookancustomer.socialLogin.facebook;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FacebookLogin {
    /**
     * =========================== How to use  ============================
     * =============Declare these variables in your Activity /fragment===================
     * private CallbackManager callbackManager;
     * FacebookLogin socialLogin;
     * <p>
     * ===========Add this in your Activity /fragment=======================
     *
     * @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
     * super.onActivityResult(requestCode, resultCode, data);
     * callbackManager.onActivityResult(requestCode, resultCode, data);
     * }
     * <p>
     * ===========call this in onCreate of Activity/Fragment ===================
     * <p>
     * private void initCallbackManager() {
     * <p>
     * socialLogin = new FacebookLogin(getActivity());
     * <p>
     * // Just pass the id of your button, for which to handle the FACEBOOK LOGIN
     * callbackManager = facebookLogin.loginViaFacebook(new OnFacebookLoginListener() {
     * @Override public void onSocialLogin(int type, FacebookLoginData loginData) {
     * Log.v("TAG FACEBOOK", "COMING------" + loginData.getSocialUserID());
     * //Perform here what you want to do
     * }
     * }
     * @Override public void onCancel() {
     * //Perform here what you want to do
     * <p>
     * }
     * @Override public void onError() {
     * //Perform here what you want to do
     * }
     *
     * });
     * }
     * <p>
     * ===========To perform facebook login put this code on fb button click===================
     *
     * if (CommonUtil.isConnectedToInternet(LoginActivity.this)) {
     * socialLogin.doLogin();
     * <p>
     * } else {
     * SingleBtnDialog.with(LoginActivity.this).setMessage(getString(R.string.noInternetLabel)).show();                }
     */

    //=========================== Private variable ============================
    public static final int FACEBOOK_LOGIN = 6;

    private Activity activity;
    private Fragment fragment;
    private FacebookLoginData loginData;
    private CallbackManager callbackManager;
    private OnFacebookLoginListener listener;
    private android.app.Fragment appFragment;
    private String[] permissionList = {"email, public_profile"};
    private final int profilePicWidth = 400;
    private final int profilePicHeight = 400;

    //=========================== Constructors ============================
    public FacebookLogin(Activity activity) {
        this.activity = activity;
    }

    public FacebookLogin(Fragment fragment) {
        this.fragment = fragment;
    }

    public FacebookLogin(android.app.Fragment appFragment) {
        this.appFragment = appFragment;
    }


    /**
     * Method to login the user through facebook
     */
    public CallbackManager loginViaFacebook(OnFacebookLoginListener listener) {
        this.listener = listener;
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        Log.v("ACCESS TOKEN", loginResult.getAccessToken().getToken());

                        loginData = new FacebookLoginData();
                        loginData.setAccessToken(loginResult.getAccessToken().getToken());

                        requestFacebookUserData(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("CANCEL", "CANCEL");
                        loginData = new FacebookLoginData();
                    }


                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.v("fb ERROR", "ERROR" + exception);
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }

                        //  CustomDialog.showAlertDialog(activity, "Error", "Please try other alternatives.", RequestCodes.DEFAULT);
                    }
                });


        return callbackManager;
    }


    public void doLogin() {

        if (activity != null)
            LoginManager.getInstance().logInWithReadPermissions(activity,
                    Arrays.asList(permissionList));
        else if (fragment != null)
            LoginManager.getInstance().logInWithReadPermissions(fragment,
                    Arrays.asList(permissionList));
        else if (appFragment != null)
            LoginManager.getInstance().logInWithReadPermissions(appFragment,
                    Arrays.asList(permissionList));
    }

    /**
     * Method to request the UserData from Facebook
     *
     * @param token
     */
    public void requestFacebookUserData(final AccessToken token) {

        GraphRequest request = GraphRequest.newMeRequest(
                token, new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code

                        if (object != null) {
                            try {

                                Log.v("GRAPH RESPONSE", response.toString());
                                Log.v("JSON OBJECT", object.toString());

                                loginData.setAccessToken(token.getToken());
                                String key;

                                key = "id";
                                loginData.setSocialUserID(getValue(key, object));


                                key = "name";
                                String name = getValue(key, object);

                                key = "email";
                                loginData.setEmail(getValue(key, object));

                                key = "first_name";
                                loginData.setFirstName(getValue(key, object));

                                key = "last_name";
                                loginData.setLastName(getValue(key, object));

                                setProfilePicUrl();
                                returnFacebookUserData();
                            } catch (JSONException jEx) {
                                if (fragment == null)
                                    Toast.makeText(activity, StorefrontCommonData.getString(activity,R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(fragment.getActivity(), StorefrontCommonData.getString(fragment.getActivity(),R.string.please_try_again),
                                            Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, first_name, last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Method to safely set the the values to the Fields
     *
     * @param key
     * @param jObj
     * @return
     * @throws JSONException
     */
    private String getValue(String key, JSONObject jObj) throws JSONException {

        if (jObj.has(key))
            return jObj.getString(key);

        return "";
    }

    /**
     * Return the User data to the User
     */
    private void returnFacebookUserData() {
//        setProfilePicUrl();
        LoginManager.getInstance().logOut();
        if (listener != null) {
            listener.onSocialLogin(FACEBOOK_LOGIN, loginData);
        }

    }

    private void setProfilePicUrl() {
        String pic = null;

        try {
            pic = Profile.getCurrentProfile().getProfilePictureUri(profilePicWidth, profilePicHeight).toString();

            Log.e("fb pic url", "==" + pic);

        } catch (Exception e) {

                               Utils.printStackTrace(e);

            pic = "https://graph.facebook.com/" + loginData.getSocialUserID() + "/picture?type=large";
        }

        loginData.setPicBig(pic);
    }
}
