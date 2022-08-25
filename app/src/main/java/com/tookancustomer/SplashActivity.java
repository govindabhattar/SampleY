package com.tookancustomer;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.tookancustomer.adapters.LanguageSpinnerAdapter;
import com.tookancustomer.appdata.AppManager;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Config;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.countryCodePicker.adapter.CountryPickerAdapter;
import com.tookancustomer.countryCodePicker.dialog.CountrySelectionDailog;
import com.tookancustomer.countryCodePicker.model.Country;
import com.tookancustomer.dialog.AlertDialog;
import com.tookancustomer.dialog.AlertDialogSignUp;
import com.tookancustomer.dialog.OptionsDialog;
import com.tookancustomer.dialog.ProgressDialog;
import com.tookancustomer.fcm.FCMMessagingService;
import com.tookancustomer.instagram.InstagramApp;
import com.tookancustomer.location.LocationAccess;
import com.tookancustomer.location.LocationFetcher;
import com.tookancustomer.location.LocationUtils;
import com.tookancustomer.mapfiles.MapUtils;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.LanguageStrings.LanguageStringsModel;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.appConfiguration.AppConfigurationModel;
import com.tookancustomer.models.appConfiguration.Datum;
import com.tookancustomer.models.getCountryCode.Data;
import com.tookancustomer.models.getCountryCode.GetCountryCode;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.plugin.MaterialEditText;
import com.tookancustomer.retrofit2.APIError;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.ResponseResolver;
import com.tookancustomer.retrofit2.RestClient;
import com.tookancustomer.socialLogin.facebook.FacebookLogin;
import com.tookancustomer.socialLogin.facebook.FacebookLoginData;
import com.tookancustomer.socialLogin.facebook.OnFacebookLoginListener;
import com.tookancustomer.socialLogin.google.GoogleUtil;
import com.tookancustomer.utility.CommonAPIUtils;
import com.tookancustomer.utility.FilterUtils;
import com.tookancustomer.utility.RootUtil;
import com.tookancustomer.utility.Transition;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;
import com.tookancustomer.utility.ValidateClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.tookancustomer.appdata.Codes.StatusCode.FB_NOT_REGISTERED;
import static com.tookancustomer.appdata.Codes.StatusCode.GOOGLE_NOT_REGISTERED;
import static com.tookancustomer.appdata.Codes.StatusCode.INSTAGRAM_NOT_REGISTERED;
import static com.tookancustomer.utility.CommonAPIUtils.getSuperCategories;

public class SplashActivity extends BaseActivity implements View.OnClickListener, OnFacebookLoginListener,
        Constants, AdapterView.OnItemSelectedListener, GoogleUtil.GoogleLoginListener, LocationFetcher.OnLocationChangedListener
        , PaymentResultListener {
    private final int iFacebook = R.id.llFacebook, iGPlus = R.id.llGPlus, iInstagram = R.id.llInstagram;
    public Double latitude = 0.0, longitude = 0.0;
    int check = 0;
    boolean isLanguageChanged = false, hideGuestCheckout = false;
    private LinearLayout llFacebook, llGPlus, llInstagram, llWelcomeTo, continueLL, llNoInternet;
    private TextView tvAppName, tvWelcomeTo, tvContinue, tvContinueWith, tvServer, tvReferenceId, tvChooseLanguage;
    private View llAccessTokenLogin, colorVW;
    private RelativeLayout llActionButtons, rlChooseLanguage, rlBackDemo;
    private CallbackManager callbackManager;
    private FacebookLogin socialLogin;
    private FacebookLoginData fbLoginData;
    private MaterialEditText etEmail;
    private ImageView imgSplashText;
    private String countryCode = "", continentCode = "";
    private RelativeLayout rlActionBar, rlBack;
    private androidx.appcompat.app.AlertDialog changeServerDialog;
    private ValidateClass validateClass;
    private Button btnRetryConnection;
    private Spinner btnChooseLanguage;
    private LanguageSpinnerAdapter languageSpinnerAdapter;
    private View layoutSplashNormal, layoutSplashDemo;
    private boolean isOpenCheckoutActivity = false;
    ResponseResolver<BaseModel> signupApiResponse = new ResponseResolver<BaseModel>(mActivity, true, false) {
        @Override
        public void success(BaseModel baseModel) {
            MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_UP_SUCCESS);

            if (UIManager.getIsEmailVerificationRequried() == 1) {
                new AlertDialogSignUp.Builder(mActivity)
                        .title(getStrings(R.string.email_not_verified))
                        .message(getStrings(R.string.please_verify_email))
                        .listener(new AlertDialogSignUp.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {

                                finish();
                            }
                        })
                        .build().show();
                return;
            }

            final UserData userData = new UserData();
            try {
                userData.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }

            if (Dependencies.isDemoApp() && userData.getData().getWelcomePopUp() != null && !userData.getData().getWelcomePopUp().isEmpty()) {
                new AlertDialogSignUp.Builder(mActivity)
//                            .title("Hi" + " " + userData.getData().getVendorDetails().getFirstName() + "!")
                        .message(userData.getData().getWelcomePopUp())
//                            .button("Close")
                        .listener(new AlertDialogSignUp.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {

                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(mActivity);
//                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);

                            }
                        })
                        .build().show();

            } else {
                saveUserInfo(mActivity, userData, !isOpenCheckoutActivity);
                CommonAPIUtils.userLoginNavigation(mActivity, isOpenCheckoutActivity);
            }
            ProgressDialog.dismiss();
        }

        @Override
        public void failure(APIError error, BaseModel baseModel) {
            ProgressDialog.dismiss();
            Utils.snackBar(mActivity, error.getMessage());
        }
    };
    private int counter = 1, fetchCounter = 1;
    private LocationFetcher locationFetcher;
    private LinearLayout llCountryCode;
    private TextView tvCountryCode;
    //instagram
    private InstagramApp instaObj;
    //TODO google plus
    private GoogleUtil googleUtil;
    private GoogleSignInAccount googleAccount;
    private boolean appConfigData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);


        setContentView(R.layout.activity_splash);
//        installProvider();
        Log.e("start", "...................................");
        try {
            if (FCMMessagingService.mNotificationManager != null) {
                FCMMessagingService.mNotificationManager.cancel(Integer.parseInt(getIntent().getExtras().getString("pushID")));
                FCMMessagingService.clearNotification();
            }
        } catch (Exception e) {
        }

        validateClass = new ValidateClass(mActivity);
        rlActionBar = findViewById(R.id.rlActionBar);

        ((TextView) findViewById(R.id.tvFacebook)).setText(getStrings(R.string.facebook));
        ((TextView) findViewById(R.id.tvGoogle)).setText(getStrings(R.string.google_plus));
        ((TextView) findViewById(R.id.tvInstagram)).setText(getStrings(R.string.instagram));

        if (getIntent() != null &&
                (getIntent().hasExtra(CheckOutActivity.class.getName()) || getIntent().hasExtra(CheckOutCustomActivity.class.getName()))) {

            if (getIntent().hasExtra(CheckOutActivity.class.getName()))
                isOpenCheckoutActivity = getIntent().getBooleanExtra(CheckOutActivity.class.getName(), false);
            else
                isOpenCheckoutActivity = getIntent().getBooleanExtra(CheckOutCustomActivity.class.getName(), false);

            initializeViews();
            setStrings();
            setUI();
            openEmailScreen();
            if (isOpenCheckoutActivity) {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                imgSplashText.animate().setDuration(600).scaleX(0.7f).scaleY(0.7f).translationY(-(metrics.heightPixels / 2 - Utils.convertDpToPx(mActivity, 120)));
                languageSpinnerAdapter = new LanguageSpinnerAdapter(mActivity, android.R.layout.simple_spinner_item, StorefrontCommonData.getAppConfigurationData().getLanguages());
                languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                btnChooseLanguage.setAdapter(languageSpinnerAdapter);
                for (int i = 0; i < StorefrontCommonData.getAppConfigurationData().getLanguages().size(); i++) {
                    if (StorefrontCommonData.getSelectedLanguageCode() != null && StorefrontCommonData.getAppConfigurationData().getLanguages().get(i).getLanguageCode().equalsIgnoreCase(StorefrontCommonData.getSelectedLanguageCode().getLanguageCode())) {
                        btnChooseLanguage.setSelection(i);
                        break;
                    }

                }
            }
            return;
        }

        if (getIntent().getExtras() != null) {
            isLanguageChanged = getIntent().getExtras().getBoolean("isLanguageChanged", false);
            hideGuestCheckout = getIntent().getExtras().getBoolean("hideGuestCheckout", false);
        }
        checkIsDeviceRooted();

        // startPayment();
      //  startPaymentPaytm();


    }

    private void startPaymentPaytm() {
        PaytmOrder paytmOrder = new PaytmOrder("OREDRID_98765", "jcoIPQ69687627461081", "fe795335ed3049c78a57271075f2199e1526969112097",
                "1.00", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=OREDRID_98765");
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(@Nullable  Bundle bundle) {
                Toast.makeText(SplashActivity.this, "Response (onTransactionResponse) : "+bundle.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {

            }
        });// code statement);
       // transactionManager.setAppInvokeEnabled(false);
        transactionManager.setEnableAssist(true);

        transactionManager.startTransaction(this, 3034);

    }

    private void startPayment() {
         /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private void checkIsDeviceRooted() {
        if (RootUtil.isDeviceRooted() && !BuildConfig.DEBUG) {
            new AlertDialog.Builder(mActivity).message(getStrings(R.string.error_device_rooted)).button(R.string.text_exit).listener(new AlertDialog.Listener() {
                @Override
                public void performPostAlertAction(int purpose, Bundle backpack) {
                    finish();
                }
            }).build().show();
        } else {
            startLocationFetcher();
            Dependencies.setFuguBundle(getIntent().getExtras());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utils.setLanguage(this);
    }

    @Override
    public void onBackPressed() {
        if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
            Dependencies.setDemoRun(true);
            super.onBackPressed();
        } else {
            if ((findViewById(R.id.rlActionBar)).getVisibility() == View.VISIBLE) {
                setBakcHandelingFromContinue();
            } else if ((findViewById(R.id.rlBackDemo)).getVisibility() == View.VISIBLE) {
                openSplashScreen();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void openSplashScreen() {
        Dependencies.setDemoRun(false);
        YoYo.with(Techniques.SlideOutRight).duration(400).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                layoutSplashNormal.setVisibility(View.GONE);
                imgSplashText.setVisibility(View.VISIBLE);
                llWelcomeTo.setVisibility(View.GONE);
                llActionButtons.setVisibility(View.GONE);
                imgSplashText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }).playOn(layoutSplashNormal);
    }

    private void fetchFCMToken() {
        if (Utils.internetCheck(mActivity)) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            try {
                                if (task.isSuccessful()) {
                                    String newToken = task.getResult();
                                    Log.e("Token = ", newToken);
                                    Dependencies.saveDeviceToken(mActivity, newToken);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            initializeData();
                            llNoInternet.setVisibility(View.GONE);
                        }
                    });
            FirebaseMessaging.getInstance().getToken()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Dependencies.saveDeviceToken(mActivity, "whdgfweljfw");
                            initializeData();
                        }
                    });
        } else {
            findViewById(R.id.llNoInternet).setVisibility(View.VISIBLE);
            findViewById(R.id.btnRetryConnection).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchFCMToken();
                }
            });
        }

    }

    private void startLocationFetcher() {
        if (!checkLocationPermissions()) {
            return;
        }

        if (!LocationUtils.isGPSEnabled(mActivity)) {
            LocationAccess.showImproveAccuracyDialog(mActivity);
            return;
        }
        fetchFCMToken();
        // Start fetching the location
        if (locationFetcher == null) {
            locationFetcher = new LocationFetcher(this, Constants.TimeRange.LOCATION_FETCH_INTERVAL, Constants.LocationPriority.BEST);
        }
        locationFetcher.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (requestCode == Codes.Permission.LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationFetcher();
            else
                fetchFCMToken();
        }

    }

    private boolean checkLocationPermissions() {
        /** Code to check whether the TrackingData Permission is Granted */
        String[] permissionsRequired = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        return AppManager.getInstance().askUserToGrantPermission(mActivity, permissionsRequired, getStrings(R.string.please_grant_permission_location_text), Codes.Permission.LOCATION);
    }

    public void onLocationChanged(Location location, int priority) {
//        Log.e("onLocationChanged", "=" + location);
        if (location == null) {
            return;
        }

        // Check if no location is fetched
        if (location.getLatitude() == 0 && location.getLongitude() == 0) {
            return;
        }
        LocationUtils.saveLocation(mActivity, location);

        if (latitude == null || longitude == null || latitude == 0.0 || longitude == 0.0) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            UIManager.setLatitude(latitude);
            UIManager.setLongitude(longitude);
            executeSetAddress();
        }
        locationFetcher.destroy();
    }

    public void executeSetAddress() {
        //Log.e("executeSetAddress", "executeSetAddress called");
        SetAddress setAddress = new SetAddress();
        setAddress.execute();
    }

    private void initializeViews() {

        socialLogin = new FacebookLogin(mActivity);
        callbackManager = socialLogin.loginViaFacebook(this);
        imgSplashText = findViewById(R.id.imgSplashText);
        colorVW = findViewById(R.id.colorVW);
        llActionButtons = findViewById(R.id.llActionsButton);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setFilters(new InputFilter[]{FilterUtils.emojiFilter});
        continueLL = findViewById(R.id.continueLL);
        tvAppName = findViewById(R.id.tvAppName);
        tvWelcomeTo = findViewById(R.id.tvWelcomeTo);
        llNoInternet = findViewById(R.id.llNoInternet);
        rlBack = findViewById(R.id.rlBack);
        llAccessTokenLogin = findViewById(R.id.llAccessTokenLogin);
        tvContinueWith = findViewById(R.id.tvContinueWith);
        llFacebook = findViewById(iFacebook);
        llGPlus = findViewById(iGPlus);
        llInstagram = findViewById(iInstagram);
        tvServer = findViewById(R.id.tvServerName);
        tvReferenceId = findViewById(R.id.tvReferenceId);
        layoutSplashNormal = findViewById(R.id.layoutSplashNormal);
        rlChooseLanguage = findViewById(R.id.rlChooseLanguage);
        tvChooseLanguage = findViewById(R.id.tvChooseLanguage);
        tvChooseLanguage.setText(getStrings(R.string.choose_language));
        btnChooseLanguage = findViewById(R.id.btnChooseLanguage);
        btnRetryConnection = findViewById(R.id.btnRetryConnection);
        btnChooseLanguage.setOnItemSelectedListener(this);
        rlBackDemo = findViewById(R.id.rlBackDemo);
        tvServer.setOnClickListener(this);
        tvReferenceId.setOnClickListener(this);
        Utils.setOnClickListener(this, llFacebook, llGPlus, llInstagram, continueLL, tvChooseLanguage);
        llWelcomeTo = findViewById(R.id.llWelcomeTo);
        tvContinue = findViewById(R.id.tvContinue);
        tvContinue.setText(getStrings(R.string.continu));
        llWelcomeTo.setVisibility(View.INVISIBLE);
        llCountryCode = findViewById(R.id.llCountryCode);
        tvCountryCode = findViewById(R.id.tvCountryCode);
        tvCountryCode.setText(Utils.getDefaultCountryCode(this));
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                llCountryCode.setVisibility(validateClass.checkIfPhoneNumber(s.toString()) && !Dependencies.isDemoApp() ? View.VISIBLE : View.GONE);
            }
        });
        Utils.setOnClickListener(this, llCountryCode);


        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validate()) {
                        checkUserExist();
                    }
                }
                return false;
            }
        });

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rlBackDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Dependencies.isDemoApp()) {
                    tvAppName.setText(getStrings(R.string.signin_demo_account_message));
                } else {
                    if (!Dependencies.isAppFirstInstall) {
                        tvWelcomeTo.setVisibility(View.VISIBLE);
                        tvAppName.setText(getStrings(R.string.sign_in_to_continue));
                    } else {
                        tvWelcomeTo.setVisibility(View.VISIBLE);
                        tvAppName.setText(getStrings(R.string.enter_email_phone_to_continue));
                    }
                }
            }
        });
        setSplashBackground();


    }

    private void setInstagramListener() {

        InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {

                Log.e("Userid", instaObj.getId());
                Log.e("Name", instaObj.getName());
                Log.e("UserName", instaObj.getUserName());

//                ProgressDialog.show(mActivity);

                instagramLogin();

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        };

        instaObj = new InstagramApp(this, getStrings(R.string.instagram_client_id),
                getStrings(R.string.instagram_secret_id), getStrings(R.string.instagram_redirection_url));
        instaObj.setListener(listener);

    }

    private void initializeData() {
        initializeViews();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvServer.setVisibility(Config.isRelease() ? View.GONE : View.VISIBLE);
                tvReferenceId.setVisibility(Config.isRelease() ? View.GONE : View.VISIBLE);
                tvServer.setText(Config.getCurrentAppModeName(mActivity));
                tvReferenceId.setText("Ref Id " + Dependencies.getMarketplaceReferenceId());
                setUI();
            }
        });
        if (StorefrontCommonData.getAppConfigurationData() != null) {
            Log.e("No-api", "....................No-api");
            setStrings();
            languageSpinnerAdapter = new LanguageSpinnerAdapter(mActivity, android.R.layout.simple_spinner_item, StorefrontCommonData.getAppConfigurationData().getLanguages());
            languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            btnChooseLanguage.setAdapter(languageSpinnerAdapter);
            FacebookSdk.setApplicationId(getFacebookId());
            setInstagramListener();

            //  Log.e("key", "key" + BuildConfig.googleClientId);
            if (StorefrontCommonData.getAppConfigurationData().getLanguages().size() > 0) {
                for (int i = 0; i < StorefrontCommonData.getAppConfigurationData().getLanguages().size(); i++) {
                    if (StorefrontCommonData.getSelectedLanguageCode() != null && StorefrontCommonData.getAppConfigurationData().getLanguages().get(i).getLanguageCode().equalsIgnoreCase(StorefrontCommonData.getSelectedLanguageCode().getLanguageCode())) {
                        btnChooseLanguage.setSelection(i);
                        break;
                    }

                }
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUI();
                }
            });
            getCountryCode(StorefrontCommonData.getAppConfigurationData());

        }

        fetchAppConfig();


    }

    private void setBakcHandelingFromContinue() {
        final AlphaAnimation fade_out = new AlphaAnimation(0.0f, 1.0f);
        final AlphaAnimation fade_in = new AlphaAnimation(1.0f, 0.0f);
        if (imgSplashText.getVisibility() == View.INVISIBLE) {
            runOnUiThread(new Runnable() {
                public void run() {
                    fade_out.setDuration(250);
                    fade_in.setDuration(250);
                    fade_out.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation arg0) {
                            if (!Dependencies.isAppFirstInstall)
                                tvWelcomeTo.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, UIManager.llWelcomeToTopMargin, 0, 0);
                            llWelcomeTo.setLayoutParams(params);

                        }

                        public void onAnimationRepeat(Animation arg0) {
                        }

                        public void onAnimationEnd(Animation arg0) {
                            imgSplashText.setVisibility(View.VISIBLE);
                            tvAppName.setVisibility(View.VISIBLE);
                            if (tvContinueWith.getVisibility() == View.INVISIBLE) {
                                tvContinueWith.setVisibility(View.VISIBLE);
                            }


                            if (llFacebook.getVisibility() == View.INVISIBLE) {
                                if (showFacebook()) {
                                    llFacebook.setVisibility(View.VISIBLE);
                                }

                            }
                            if (llGPlus.getVisibility() == View.INVISIBLE) {
                                if (showGPlus()) {
                                    llGPlus.setVisibility(View.VISIBLE);
                                }
                            }

                            if (llInstagram.getVisibility() == View.INVISIBLE) {
                                if (showInstagram()) {
                                    llInstagram.setVisibility(View.VISIBLE);
                                }
                            }


                            if (!Dependencies.isAppFirstInstall)
                                tvWelcomeTo.startAnimation(fade_in);
                            continueLL.startAnimation(fade_in);
                            fade_in.setAnimationListener(new Animation.AnimationListener() {
                                public void onAnimationStart(Animation arg0) {
                                }

                                public void onAnimationRepeat(Animation arg0) {
                                }

                                public void onAnimationEnd(Animation arg0) {
                                    continueLL.setVisibility(View.VISIBLE);
                                    rlActionBar.setVisibility(View.GONE);
                                    if (!Dependencies.isAppFirstInstall)
                                        tvWelcomeTo.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                    imgSplashText.startAnimation(fade_out);
                    tvAppName.startAnimation(fade_out);
                    if (!Dependencies.isAppFirstInstall)
                        tvWelcomeTo.startAnimation(fade_out);

                    if (!showInstagram() && !showGPlus() && !showFacebook()) {
                        if (UIManager.isOtpLoginAvailable())
                            tvContinueWith.setVisibility(View.INVISIBLE);
                    }

                    if (tvContinueWith.getVisibility() == View.INVISIBLE) {
                        tvContinueWith.startAnimation(fade_out);
                    }

                    if (llFacebook.getVisibility() == View.INVISIBLE) {
                        if (UIManager.isFacebookAvailable()) {
                            llFacebook.startAnimation(fade_out);
                        }
                    }
                    if (llGPlus.getVisibility() == View.INVISIBLE) {
                        if (UIManager.isGPlusAvailable()) {
                            llGPlus.startAnimation(fade_out);
                        }
                    }

                    if (llInstagram.getVisibility() == View.INVISIBLE) {
                        if (UIManager.isInstagramAvailable()) {
                            llInstagram.startAnimation(fade_out);
                        }
                    }
                }
            });
        }
    }

    private void setSplashBackground() {
        colorVW.setVisibility(View.VISIBLE);
    }

    private void fetchAppConfig() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llAccessTokenLogin.setVisibility(View.VISIBLE);

            }
        });
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        RestClient.getApiInterface(this).fetchAppConfig(commonParams.build().getMap())
                .enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                    @Override
                    public void success(BaseModel baseModel) {
                        AppConfigurationModel appConfig = new AppConfigurationModel();
                        try {
                            appConfig.setData(baseModel.toResponseModel(Datum.class));
                        } catch (Exception e) {
                            Utils.printStackTrace(e);
                        }
                        StorefrontCommonData.setTerminology(appConfig.getData().getTerminology());

                        UIManager.setGoogleAnalyticsOptionsHashMap(appConfig.getData().getGoogleAnalyticsOptionsArrayList());

                        if (appConfig.getData().getLanguages().size() == 0) {
                            StorefrontCommonData.setLanguageCode(new LanguagesCode("en", "English", "English"));
                        } else if (appConfig.getData().getLanguages().size() == 1) {
                            StorefrontCommonData.setLanguageCode(appConfig.getData().getLanguages().get(0));
                        }

                        if (appConfig.getData().getLanguageStrings() != null)
                            StorefrontCommonData.setLanguageStrings(mActivity, appConfig.getData().getLanguageStrings());
                        appConfigData = StorefrontCommonData.getAppConfigurationData() == null;
                        StorefrontCommonData.setAppConfigurationData(appConfig.getData());


                        if (appConfigData) {

                            Log.e("api", "....................api");
                            setStrings();

                            languageSpinnerAdapter = new LanguageSpinnerAdapter(mActivity, android.R.layout.simple_spinner_item, appConfig.getData().getLanguages());
                            languageSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            btnChooseLanguage.setAdapter(languageSpinnerAdapter);
                            FacebookSdk.setApplicationId(getFacebookId());
                            setInstagramListener();
                           // Log.e("key", "key" + mActivity.getResources().getString(R.string.google_app_id));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setUI();
                                }
                            });
                            getCountryCode(appConfig.getData());
                        }


                    }

                    @Override
                    public void failure(APIError error, BaseModel baseModel) {
                        if (appConfigData = StorefrontCommonData.getAppConfigurationData() == null) {
                            llAccessTokenLogin.setVisibility(View.GONE);
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            imgSplashText.animate().setDuration(600).scaleX(0.7f).scaleY(0.7f).translationY(-(metrics.heightPixels / 2 - Utils.convertDpToPx(mActivity, 120)));
                            new AlertDialog.Builder(mActivity).message(error.getMessage()).button(getStrings(R.string.retry_text)).listener(new AlertDialog.Listener() {
                                @Override
                                public void performPostAlertAction(int purpose, Bundle backpack) {
                                    fetchAppConfig();
                                }
                            }).build().show();
                        }
                        if (fetchCounter <= 3) {
                            fetchAppConfig();
                            ++fetchCounter;
                        }
                    }


                });

    }

    private void getCountryCode(final Datum appConfigurationModel) {
        if (StorefrontCommonData.getCountryCode() != null && StorefrontCommonData.getCountryCode().getData() != null) {
            continentCode = StorefrontCommonData.getCountryCode().getData().getContinentCode();
            countryCode = StorefrontCommonData.getCountryCode().getData().getCountryCode();
            getAppVersion(appConfigurationModel);
        } else if (!Dependencies.isDemoApp()) {
            continentCode = "";
            countryCode = "";
            getAppVersion(appConfigurationModel);
        } else {
            RestClient.getZohoApiInterface(this).getCountryCode().enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
                @Override
                public void success(BaseModel baseModel) {
                    GetCountryCode getCountryCode = new GetCountryCode();
                    try {
                        getCountryCode.setData(baseModel.toResponseModel(Data.class));
                        getCountryCode.setGeo(baseModel.getGeo());
                        getCountryCode.setOriginal(baseModel.getOriginal());
                    } catch (Exception e) {
                        Utils.printStackTrace(e);
                    }

                    StorefrontCommonData.setCountryCode(getCountryCode);

                    continentCode = getCountryCode.getData().getContinentCode();
                    countryCode = getCountryCode.getData().getCountryCode();
                    getAppVersion(appConfigurationModel);
//                setLanguageChange();
                    Log.v("Success", "CountryCodeSuccess");
                }

                @Override
                public void failure(APIError error, BaseModel baseModel) {
                    llAccessTokenLogin.setVisibility(View.GONE);

                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    imgSplashText.animate().setDuration(500).scaleX(0.7f).scaleY(0.7f).translationY(-(metrics.heightPixels / 2 - Utils.convertDpToPx(mActivity, 120)));
                    new AlertDialog.Builder(mActivity).message(error.getMessage()).button(getStrings(R.string.retry_text)).listener(new AlertDialog.Listener() {
                        @Override
                        public void performPostAlertAction(int purpose, Bundle backpack) {
                            getCountryCode(appConfigurationModel);
                        }
                    }).build().show();

                }
            });
        }
    }

    private void setLanguageChange(final LanguagesCode languageCode, final int position) {
        llAccessTokenLogin.setVisibility(View.VISIBLE);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add("language", languageCode.getLanguageCode());

        RestClient.getApiInterface(this).languageChange(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, false) {
            @Override
            public void success(BaseModel baseModel) {
                LanguageStringsModel languageStringsModel = new LanguageStringsModel();
                try {
                    languageStringsModel.setLanguageStrings((Map<String, String>) baseModel.data);
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }
                if (!(StorefrontCommonData.getSelectedLanguageCode() != null ? StorefrontCommonData.getSelectedLanguageCode().getLanguageCode() : "en").equals(languageCode.getLanguageCode())) {
                    Bundle extras = new Bundle();
                    extras.putBoolean("isLanguageChanged", true);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(extras);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    startActivity(getIntent().putExtra("isLanguageChanged", true));
                }

                StorefrontCommonData.setLanguageCode(languageCode);

                StorefrontCommonData.setLanguageStrings(mActivity, languageStringsModel.getLanguageStrings());
                setStrings();
                ifAccessTokenEmpty();
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                llAccessTokenLogin.setVisibility(View.GONE);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                imgSplashText.animate().setDuration(500).scaleX(0.7f).scaleY(0.7f).translationY(-(metrics.heightPixels / 2 - Utils.convertDpToPx(mActivity, 120)));
                new AlertDialog.Builder(mActivity).message(error.getMessage()).button(getStrings(R.string.retry_text)).listener(new AlertDialog.Listener() {
                    @Override
                    public void performPostAlertAction(int purpose, Bundle backpack) {
                        setLanguageChange(languageCode, StorefrontCommonData.getLanguagePosition(mActivity));
                    }
                }).build().show();

            }
        });
    }

    private void loginViaAccessToken() {
        llAccessTokenLogin.setVisibility(View.VISIBLE);
        boolean userDataNull;

        Location location = LocationUtils.getLastLocation(this);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, StorefrontCommonData.getUserData());
        commonParams.add("lat", location.getLatitude())
                .add("lng", location.getLongitude());

        if (Dependencies.isDemoApp()) {
            final JSONObject ipConfigObject = new JSONObject();
            try {
                Log.v("country_code", countryCode);
                Log.v("continent_code", continentCode);

                ipConfigObject.put("country", countryCode);
                ipConfigObject.put("continent_code", continentCode);
            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }

            commonParams.add("ipConfig", ipConfigObject);
        }
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        }
        RestClient.getApiInterface(this).loginViaAccessToken(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, false, true) {
            @Override
            public void success(BaseModel baseModel) {
                UserData userData = new UserData();
                try {
                    userData.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));

                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }
                boolean userDataNull = StorefrontCommonData.getUserData() == null;
                saveUserInfo(mActivity, userData, true);

                if (userDataNull) {
                    Bundle extras = new Bundle();
                    extras.putSerializable(UserData.class.getName(), userData);

                    if ((UIManager.isOTPAvailable() && userData.getData().getVendorDetails().getIsPhoneVerified() == 0)
                            || (UIManager.getIsEmailVerificationRequried() == 1 && userData.getData().getVendorDetails().getIsEmailVerified() == 0)
                            || (userData.getData().getVendorDetails().getRegistrationStatus() != RegistrationStatus.VERIFIED && userData.getData().getSignupTemplateData() != null && !userData.getData().getSignupTemplateData().isEmpty())
                            || (StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1 && userData.getData().getVendorDetails().getSubscriptionPlan().get(0).getPaid() == 0)
                            || (StorefrontCommonData.getAppConfigurationData().getIsDebtEnabled() == 1 && userData.getData().getVendorDetails().getDebtAmount() > 0)
                            || (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionEnabled() == 1 && StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionMandatory() == 1)

                    ) {

                        CommonAPIUtils.userLoginNavigation(mActivity, isOpenCheckoutActivity);

//                        Dependencies.saveAccessToken(mActivity, "");
//                        performAccessTokenOrWelcomeScreen();

                    } else {
                        Bundle extra = getIntent().getExtras();
                        if (extra != null && extra.getInt(JOB_ID, 0) > 0) {
                            try {
                                if (FCMMessagingService.mNotificationManager != null) {
                                    FCMMessagingService.mNotificationManager.cancel(Integer.parseInt(getIntent().getExtras().getString("pushID")));
                                    FCMMessagingService.clearNotification();
                                }
                            } catch (Exception e) {
                            }
                            try {
                                int jobID = Integer.valueOf(extra.getInt(JOB_ID, 0));
                                int flag = extra.getInt("flag", -1);
                                int status = extra.getInt("jobStatus");
                                Class<?> toClass = Transition.launchOrderDetailsActivity();
                                if (flag == Constants.NotificationFlags.JOB_DELETED) {
                                    toClass = Transition.launchHomeActivity();
                                } else if (flag == NotificationFlags.USER_DEBT_PENDING) {
                                    toClass = UserDebtActivity.class;
                                } else if ((flag == Constants.NotificationFlags.RULE_ACCEPTED) || (flag == Constants.NotificationFlags.RULE_REJECTED)
                                        || (flag == Constants.NotificationFlags.RECURRING_TASK_CREATION_FAIL) || (flag == Constants.NotificationFlags.RULE_CREATED)) {
                                    toClass = TasksActivity.class;
                                } else {
                                    toClass = Transition.launchOrderDetailsActivity();
                                }
                                extras.putString("pushID", extra.getString("pushID"));
                                extras.putInt(JOB_ID, jobID);
                                Transition.transit(mActivity, toClass, extras);
                            } catch (Exception e) {
                                getSuperCategories(userData, mActivity);
                            }
                        } else {
                            getSuperCategories(userData, mActivity);
                        }
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (StorefrontCommonData.getUserData() == null) {
                    if (error.getStatusCode() == 900 && error.getMessage().equalsIgnoreCase(getStrings(R.string.socket_timeout_connection))) {
                        new AlertDialog.Builder(mActivity).message(error.getMessage()).button(getStrings(R.string.retry_text)).listener(new AlertDialog.Listener() {
                            @Override
                            public void performPostAlertAction(int purpose, Bundle backpack) {
                                loginViaAccessToken();
                            }
                        }).build().show();
                    } else {
                        Dependencies.saveAccessToken(mActivity, "");
                        performAccessTokenOrWelcomeScreen();
                    }
                } else {
                    if (counter <= 3) {
                        loginViaAccessToken();
                        ++counter;
                    }

                }
            }
        });
    }

    private void saveUserInfo(Activity mActivity, UserData userData, boolean isLoginScreenAtFirst) {
        Utils.saveUserInfo(mActivity, userData, isLoginScreenAtFirst);
        fuguNotificationHandle();

    }


    private void setUI() {
//        imgSplashText.setVisibility(UIManager.isSplashTextAvailable() ? View.VISIBLE : View.GONE);
        if (!UIManager.isFacebookAvailable() && !UIManager.isGPlusAvailable() && !UIManager.isInstagramAvailable()) {
            llFacebook.setVisibility(showFacebook() ? View.VISIBLE : View.INVISIBLE);
            llGPlus.setVisibility(showGPlus() ? View.VISIBLE : View.INVISIBLE);
            llInstagram.setVisibility(showInstagram() ? View.VISIBLE : View.INVISIBLE);
        } else {
            llFacebook.setVisibility(showFacebook() ? View.VISIBLE : View.GONE);
            llGPlus.setVisibility(showGPlus() ? View.VISIBLE : View.GONE);
            llInstagram.setVisibility(showInstagram() ? View.VISIBLE : View.GONE);
        }
//        tvContinueWith.setVisibility(
//                (UIManager.isSignUpAvailable() || UIManager.isSignInAvailable())
//                &&
//                (UIManager.isFacebookAvailable() || UIManager.isGPlusAvailable()) ?
//                View.VISIBLE : View.GONE);

        tvContinueWith.setVisibility(showFacebook() || showGPlus() || showInstagram() ? View.VISIBLE : View.GONE);
        tvContinueWith.setText(getStrings(R.string.continue_with));

        //TODO google plus
//        llGPlus.setVisibility(View.VISIBLE);
        googleUtil = new GoogleUtil.GoogleBuilder(this)
                .build();
    }

    @Override
    public void onClick(View view) {
        if (!Utils.preventMultipleClicks()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tvServerName:
                openChangeServerDialog();
                break;
            case R.id.tvReferenceId:
                openChangeDeviceTypeDialog();
                break;
            case iFacebook:
                //TODO
                // Just pass the id of your button, for which to handle the FACEBOOK LOGIN
                if (Utils.internetCheck(mActivity)) {
                    socialLogin.doLogin();
                } else {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
                }
                break;
            case iGPlus:
                googleUtil.login();
                break;

            case iInstagram:
                if (Utils.internetCheck(mActivity)) {

                    Utils.clearCookies(mActivity);

                    if (instaObj.hasAccessToken()) {
                        instaObj.resetAccessToken();
                    }
                    instaObj.authorize();
                } else {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
                }
                break;
            case R.id.tvChooseLanguage:
                btnChooseLanguage.performClick();
                break;
            case R.id.continueLL:
                if (validate()) {
                    checkUserExist();
                }
                break;
            case R.id.rlBackDemo:
                if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
                    Dependencies.setDemoRun(true);
                    super.onBackPressed();
                } else {
                    openSplashScreen();
                }
                break;
            case R.id.llCountryCode:
                Utils.hideSoftKeyboard(this);
                CountrySelectionDailog.getInstance(this, new CountryPickerAdapter.OnCountrySelectedListener() {
                    @Override
                    public void onCountrySelected(Country country) {
                        tvCountryCode.setText(country.getCountryCode());
                        Utils.hideSoftKeyboard(SplashActivity.this);
                        CountrySelectionDailog.dismissDialog();
                    }
                }).show();


                break;
        }
    }

    private void openEmailScreen() {
        Dependencies.setDemoRun(false);
        YoYo.with(Techniques.SlideInRight).duration(400).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                layoutSplashNormal.setVisibility(View.VISIBLE);
                openWelcomeOrStartHomeActivity();
                tvReferenceId.setVisibility(View.GONE);
                tvServer.setVisibility(View.GONE);
                rlBackDemo.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }).playOn(layoutSplashNormal);
    }

    Boolean validate() {
        if (Dependencies.isDemoApp()) {
            if (!validateClass.checkEmail(etEmail, getStrings(R.string.enter_valid_email))) {
                return false;
            }
        } else {
            if (!validateClass.checkEmailPhoneNumber(etEmail)) {
                return false;
            }
        }
        return true;
    }

    public void checkUserExist() {
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        if (llCountryCode.getVisibility() == View.VISIBLE) {
            commonParams.add("phone_no", tvCountryCode.getText().toString().trim() + " " + etEmail.getText().toString().trim());
        } else {
            commonParams.add("email", etEmail.getText().toString().trim());
        }

        RestClient.getApiInterface(this).userExist(commonParams.build().getMap()).enqueue(new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel userData) {
                if (userData.getStatus() == 200) {
                    Intent intent;

                    if (UIManager.isOtpLoginAvailable()) {
                        intent = new Intent(mActivity, LoginOTPActivity.class);
                        intent.putExtra(IS_PHONE, llCountryCode.getVisibility() == View.VISIBLE);
                        intent.putExtra("FROM_SPLASH", 1);


                    } else
                        intent = new Intent(mActivity, SignInActivity.class);

                    intent.putExtra(EMAIL_OR_PHONE, etEmail.getText().toString().trim());
                    intent.putExtra(COUNTRY_CODE, countryCode);
                    intent.putExtra(CONTINENT_CODE, continentCode);


                    if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
                        intent.putExtras(getIntent().getExtras());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View, String> p1;
                        if (llCountryCode.getVisibility() == View.VISIBLE) {
                            intent.putExtra(EMAIL_OR_PHONE, tvCountryCode.getText().toString().trim() + " " + etEmail.getText().toString().trim());
                            etEmail.setTransitionName(ACTIVITY_PHONE_TRANSITION);
                            p1 = Pair.create((View) etEmail, ACTIVITY_PHONE_TRANSITION);
                        } else {
                            etEmail.setTransitionName(ACTIVITY_EMAIL_TRANSITION);
                            p1 = Pair.create((View) etEmail, ACTIVITY_EMAIL_TRANSITION);
                        }

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, p1);
                        if (isOpenCheckoutActivity) {
                            intent.putExtra(Keys.Extras.IS_LOGIN_FROM_CHECKOUT, true);
                            startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT, options.toBundle());
                        } else if (UIManager.isOtpLoginAvailable()) {
                            startActivityForResult(intent, OPEN_LOGIN_OTP_ACTIVITY);
                        } else {
                            startActivity(intent, options.toBundle());
                        }

                    } else {
                        if (isOpenCheckoutActivity) {
                            intent.putExtra(Keys.Extras.IS_LOGIN_FROM_CHECKOUT, true);
                            startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
                        } else if (UIManager.isOtpLoginAvailable()) {
                            startActivityForResult(intent, OPEN_LOGIN_OTP_ACTIVITY);
                        } else {
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                Log.e("error - ", error.getStatusCode() + "");
                if (error.getStatusCode() == 400) {
                    Intent intent = new Intent(mActivity, SignUpActivity.class);
                    intent.putExtra(EMAIL_OR_PHONE, etEmail.getText().toString().trim());
                    intent.putExtra(COUNTRY_CODE, countryCode);
                    intent.putExtra(CONTINENT_CODE, continentCode);
                    if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
                        intent.putExtras(getIntent().getExtras());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View, String> p1;
                        if (llCountryCode.getVisibility() == View.VISIBLE) {
                            intent.putExtra(EMAIL_OR_PHONE, tvCountryCode.getText().toString().trim() + " " + etEmail.getText().toString().trim());
                            etEmail.setTransitionName(ACTIVITY_PHONE_TRANSITION);
                            p1 = Pair.create((View) etEmail, ACTIVITY_PHONE_TRANSITION);
                        } else {
                            etEmail.setTransitionName(ACTIVITY_EMAIL_TRANSITION);
                            p1 = Pair.create((View) etEmail, ACTIVITY_EMAIL_TRANSITION);
                        }
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, p1);
                        if (isOpenCheckoutActivity) {
                            startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT, options.toBundle());
                        } else {
                            startActivity(intent, options.toBundle());
                        }
                    } else {
                        if (isOpenCheckoutActivity) {
                            startActivityForResult(intent, OPEN_LOGIN_BEFORE_CHECKOUT);
                        } else {
                            startActivity(intent);
                        }
                    }
                } else {
                    new AlertDialog.Builder(SplashActivity.this).message(error.getMessage()).build().show();
                }
            }
        });
    }

    @Override
    public void onSocialLogin(int type, FacebookLoginData loginData) {
        Log.e("facebook data", "" + loginData);
        fbLoginData = loginData;
        facebookLogin();
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 3034 && data != null) {
                Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
            }
            if (callbackManager != null)
                callbackManager.onActivityResult(requestCode, resultCode, data);
            if (requestCode == LOCATION_ACCESS_REQUEST) {
                if (resultCode == Activity.RESULT_OK) {
                    startLocationFetcher();
                } else
                    finish();
            }
            if (requestCode == UPDATE_APP_FROM_PLAY_STORE) {
                fetchAppConfig();
            } else if (requestCode == OPEN_LOGIN_BEFORE_CHECKOUT
                    || requestCode == OPEN_SIGN_UP_FROM_DEMO_ACTIVITY
                    || requestCode == OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY
                    || requestCode == OPEN_LOGIN_OTP_ACTIVITY
                    || requestCode == OPEN_OTP_SCREEN) {
                if (resultCode == Activity.RESULT_OK) {
                    Dependencies.setDemoRun(false);
                    Bundle bundleExtra = getIntent().getExtras();
                    bundleExtra.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else if (resultCode == RESULT_CANCELED_OTP) {
                    Bundle bundleExtra = getIntent().getExtras();
                    Intent returnIntent = new Intent();
                    if (bundleExtra != null)
                        returnIntent.putExtras(bundleExtra);
                    setResult(RESULT_CANCELED_OTP, returnIntent);
                    if (UIManager.isOtpLoginAvailable()) {
                        performAccessTokenOrWelcomeScreen();
                    } else if (isOpenCheckoutActivity) {
                        finish();
                    } else {
                        performAccessTokenOrWelcomeScreen();
                    }
                }
            } else if (requestCode == GoogleUtil.RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                googleUtil.onActivityResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void facebookLogin() {
        Location location = LocationUtils.getLastLocation(this);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);

        if (Dependencies.isDemoApp()) {
            final JSONObject ipConfigObject = new JSONObject();
            try {
                Log.v("country_code", countryCode);
                Log.v("continent_code", continentCode);

                ipConfigObject.put("country", countryCode);
                ipConfigObject.put("continent_code", continentCode);
//            ipConfigArray.put(ipConfigObject);
            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }
            commonParams.add("ipConfig", ipConfigObject);
        }

        commonParams.add("email", fbLoginData.getEmail())
                .add("fb_token", fbLoginData.getSocialUserID())
                .add("lat", location.getLatitude())
                .add("lng", location.getLongitude());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }
        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(this).socialLogin(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        } else {
            RestClient.getApiInterface(this).socialLoginDualUser(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        }
    }

    private void instagramLogin() {
        Location location = LocationUtils.getLastLocation(this);
        final JSONObject ipConfigObject = new JSONObject();
        try {
            ipConfigObject.put("country", countryCode);
            Log.v("country_code", countryCode.toString());
            ipConfigObject.put("continent_code", continentCode);
            Log.v("continent_code", continentCode.toString());
        } catch (JSONException e) {
            Utils.printStackTrace(e);
        }
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add("instagram_token", instaObj.getId())
                .add("ipConfig", ipConfigObject)
                .add("latitude", location.getLatitude())
                .add("longitude", location.getLongitude());

        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(this).instagramLogin(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        } else {
            RestClient.getApiInterface(this).instagramLoginDualUser(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        }

        ProgressDialog.dismiss();
    }

    private ResponseResolver<BaseModel> finalSignInResponseResolver() {
        return new ResponseResolver<BaseModel>(mActivity, true, false) {
            @Override
            public void success(BaseModel baseModel) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.SIGN_IN_SUCCESS);
                final UserData userData = new UserData();
                try {
                    userData.setData(baseModel.toResponseModel(com.tookancustomer.models.userdata.Data.class));
                } catch (Exception e) {
                    Utils.printStackTrace(e);
                }

                saveUserInfo(mActivity, userData, !isOpenCheckoutActivity);

                CommonAPIUtils.userLoginNavigation(mActivity, isOpenCheckoutActivity);
                Dependencies.isAppFirstInstall = false;
            }

            @Override
            public void failure(APIError error, BaseModel baseModel) {
                if (error.getStatusCode() == 900) {
                    new AlertDialog.Builder(mActivity).message(getStrings(R.string.no_internet_try_again)).build().show();
                } else if (error.getStatusCode() == FB_NOT_REGISTERED.getStatusCode() && fbLoginData != null && (!fbLoginData.getSocialUserID().equals("")
                        && fbLoginData.getSocialUserID() != null)) {

                    if (UIManager.isDualUserEnable() == 0 && fbLoginData.getEmail() != null && !fbLoginData.getEmail().isEmpty()
                            && !UIManager.isOTPAvailable()
                            && !isPhoneRequired()) {
                        signupSocial();
                    } else {
                        Intent in = new Intent(getApplicationContext(), SignUpActivity.class);
                        Bundle bundle = new Bundle();
                        if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
                            bundle.putAll(getIntent().getExtras());
                        }
                        bundle.putSerializable(FACEBOOK_DETAILS, fbLoginData);
                        in.putExtras(bundle);
                        if (isOpenCheckoutActivity) {
                            startActivityForResult(in, OPEN_LOGIN_BEFORE_CHECKOUT);
                        } else {
                            startActivity(in);
                        }
                    }
                } else if (error.getStatusCode() == GOOGLE_NOT_REGISTERED.getStatusCode() && googleAccount != null) {
                    if (UIManager.isDualUserEnable() == 0 && googleAccount.getEmail() != null && !googleAccount.getEmail().isEmpty()
                            && !UIManager.isOTPAvailable()
                            && !isPhoneRequired()) {
                        signupSocial();
                    } else {
                        Intent in = new Intent(getApplicationContext(), SignUpActivity.class);
                        Bundle bundle = new Bundle();
                        if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
                            bundle.putAll(getIntent().getExtras());
                        }
                        bundle.putParcelable("googleAccount", googleAccount);
//                    bundle.putSerializable(INSTAGRAM_DETAILS, instaObj);
                        in.putExtras(bundle);
                        if (isOpenCheckoutActivity) {
                            startActivityForResult(in, OPEN_LOGIN_BEFORE_CHECKOUT);
                        } else {
                            startActivity(in);
                        }
                    }

                } else if (error.getStatusCode() == INSTAGRAM_NOT_REGISTERED.getStatusCode()
                        && instaObj != null) {

                    if (UIManager.isDualUserEnable() == 0
                            && !UIManager.isOTPAvailable()
                            && !isPhoneRequired()) {
                        signupSocial();
                    } else {
                        Intent in = new Intent(getApplicationContext(), SignUpActivity.class);
                        Bundle bundle = new Bundle();
                        if (getIntent() != null && (getIntent().hasExtra(CheckOutCustomActivity.class.getName()) || getIntent().hasExtra(CheckOutActivity.class.getName()))) {
                            bundle.putAll(getIntent().getExtras());
                        }
                        bundle.putString("name", instaObj.getName());
                        bundle.putString("instaId", instaObj.getId());
                        bundle.putString("instaProfileImage", instaObj.getUserPicture());
                        in.putExtras(bundle);
                        if (isOpenCheckoutActivity) {
                            startActivityForResult(in, OPEN_LOGIN_BEFORE_CHECKOUT);
                        } else {
                            startActivity(in);
                        }
//                    }
                    }
                } else {
                    new AlertDialog.Builder(mActivity).message(error.getMessage()).build().show();
                }
            }
        };
    }

    private boolean isPhoneRequired() {
        return (UIManager.getSignupField() == SignupFields.PHONE_ONLY || UIManager.getSignupField() == SignupFields.EMAIL_PHONE_BOTH);
    }


    private void signupSocial() {
        Location location = LocationUtils.getLastLocation(this);

        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        if (Dependencies.isDemoApp()) {
            final JSONObject ipConfigObject = new JSONObject();
            try {
                Log.v("country_code", countryCode);
                Log.v("continent_code", continentCode);

                ipConfigObject.put("country", countryCode);
                ipConfigObject.put("continent_code", continentCode);
            } catch (JSONException e) {
                Utils.printStackTrace(e);
            }
            commonParams.add("ipConfig", ipConfigObject);
        }


        ProgressDialog.show(mActivity);

        String firstName = null, lastName = null, email = null;

        if (fbLoginData != null) {
            firstName = fbLoginData.getFirstName().trim();
            lastName = fbLoginData.getLastName().trim();
            email = fbLoginData.getEmail().trim();
        } else if (googleAccount != null) {
            String[] name = googleAccount.getDisplayName().trim().split(" ");
            firstName = name[0];
            lastName = name[1];
            email = googleAccount.getEmail().trim();
        } else {
            String[] name = instaObj.getName().trim().split(" ");
            firstName = name[0];
            lastName = name[1];
            email = "";
        }


//        final String firstName = fbLoginData.getFirstName().trim();
//        String lastName = fbLoginData.getLastName().trim();
        commonParams.add("email", email)
                .add(TIMEZONE, Dependencies.getTimeZoneInMinutes())
                .add("first_name", FilterUtils.toCamelCase(firstName))
                .add("last_name", FilterUtils.toCamelCase(lastName))
                .add("lat", location.getLatitude())
                .add("lng", location.getLongitude());
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            commonParams.add("language", StorefrontCommonData.getSelectedLanguageCode().getLanguageCode());
        } else {
            commonParams.add("language", "en");
        }

        if (fbLoginData != null) {
            commonParams.add("fb_token", fbLoginData.getSocialUserID())
                    .add("vendor_image", fbLoginData.getPicBig());
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).socialRegister(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).socialRegisterDualUser(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        } else if (googleAccount != null) {
            commonParams.add("google_token", googleAccount.getId());
            if (googleAccount.getPhotoUrl() != null)
                commonParams.add("vendor_image", googleAccount.getPhotoUrl());
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).googleRegister(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).googleRegisterDualUser(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        } else {
            commonParams.add("instagram_token", instaObj.getId());
            if (instaObj.getUserPicture() != null)
                commonParams.add("vendor_image", instaObj.getUserPicture());
            if (UIManager.isDualUserEnable() == 0) {
                RestClient.getApiInterface(this).instagramRegister(commonParams.build().getMap()).enqueue(signupApiResponse);
            } else {
                RestClient.getApiInterface(this).instagramRegisterDualUser(commonParams.build().getMap()).enqueue(signupApiResponse);
            }
        }


    }

    /**
     * Method to change the Server
     */
    private void openChangeServerDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mActivity);
        Config.AppMode[] values = Config.AppMode.values();
        int appModesCount = values.length;
        final String[] appModes = new String[appModesCount];
        int currentPosition = 0;
        String currentAppModeName = Config.getCurrentAppModeName(mActivity);
        for (int position = 0; position < appModesCount; position++) {
            String appModeName = values[position].name();
            appModes[position] = appModeName;
            if (currentAppModeName.equals(appModeName))
                currentPosition = position;
        }
        builder.setSingleChoiceItems(appModes, currentPosition, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                String appModeName = appModes[position];
                tvServer.setText(appModeName);
                Config.setAppMode(mActivity, appModeName);
                changeServerDialog.dismiss();
//                finish();
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        changeServerDialog = builder.create();
        changeServerDialog.show();
    }

    /**
     * Method to change the Server
     */
    private void openChangeDeviceTypeDialog() {
        Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setContentView(R.layout.layout_change_device_type);
        dialog.setTitle("Change Device Type/Ref ID");
        final EditText etDeviceType = (EditText) dialog.findViewById(R.id.etDeviceType);
        Button btSet = (Button) dialog.findViewById(R.id.btSet);
        etDeviceType.setText(Dependencies.getMarketplaceReferenceId());
        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dependencies.setMarketplaceReferenceId(etDeviceType.getText().toString());

                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        dialog.show();
    }

    private void getAppVersion(final Datum appConfigurationModel) {
        try {
            PackageManager manager = this.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            if (appConfigurationModel.getAppVersion() != null && appConfigurationModel.getAppVersion() > info.versionCode && appConfigurationModel.getIsForce() == 1) {
                String message = "";
                if (BuildConfig.isPlayStoreUploaded) {
                    message = getStrings(R.string.force_update_msg_play_store).replace(APP_NAME, getString(R.string.app_name));
                } else {
                    message = getStrings(R.string.force_update_msg_demo).replace(APP_NAME, getString(R.string.app_name))
                            + (appConfigurationModel.getWhatsNew() != null && !appConfigurationModel.getWhatsNew().isEmpty() ? "\n\n" + getStrings(R.string.whats_new) + "\n" + appConfigurationModel.getWhatsNew() : "");
                }
                SpannableString whatsNewMessage = new SpannableString(message);
                if (message.indexOf(getStrings(R.string.whats_new)) > 0) {
                    whatsNewMessage.setSpan(new UnderlineSpan(), message.indexOf(getStrings(R.string.whats_new)), message.indexOf(getStrings(R.string.whats_new)) + getStrings(R.string.whats_new).length(), 0);
                }
                new AlertDialog.Builder(mActivity).message(whatsNewMessage).button(getStrings(R.string.download)).listener(new AlertDialog.Listener() {
                    @Override
                    public void performPostAlertAction(int purpose, Bundle backpack) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("market://details?id=" + info.packageName));
                        intent.setData(Uri.parse(appConfigurationModel.getAppUrl()));
                        try {
                            startActivityForResult(intent, UPDATE_APP_FROM_PLAY_STORE);
                        } catch (Exception e) {
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + info.packageName));
                        }
                    }
                }).build().show();
            } else if (appConfigurationModel.getAppVersion() != null && appConfigurationModel.getAppVersion() > info.versionCode && appConfigurationModel.getIsForce() == 0) {
                String message = "";
                if (BuildConfig.isPlayStoreUploaded) {
                    message = getStrings(R.string.soft_update_msg_play_store).replace(APP_NAME, getString(R.string.app_name));
                } else {
                    message = getStrings(R.string.soft_update_msg_demo).replace(APP_NAME, getString(R.string.app_name))
                            + (appConfigurationModel.getWhatsNew() != null && !appConfigurationModel.getWhatsNew().isEmpty() ? "\n\n" + getStrings(R.string.whats_new) + "\n" + appConfigurationModel.getWhatsNew() : "");
                }
                SpannableString whatsNewMessage = new SpannableString(message);
                if (message.indexOf(getStrings(R.string.whats_new)) > 0) {
                    whatsNewMessage.setSpan(new UnderlineSpan(), message.indexOf(getStrings(R.string.whats_new)), message.indexOf(getStrings(R.string.whats_new)) + getStrings(R.string.whats_new).length(), 0);
                }
                new OptionsDialog.Builder(mActivity).negativeButton(getStrings(R.string.cancel)).positiveButton(getStrings(R.string.download)).message(whatsNewMessage).listener(new OptionsDialog.Listener() {
                    @Override
                    public void performPositiveAction(int purpose, Bundle backpack) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(appConfigurationModel.getAppUrl()));
//                        intent.setData(Uri.parse("market://details?id=" + info.packageName));
                        try {
                            startActivityForResult(intent, UPDATE_APP_FROM_PLAY_STORE);
                        } catch (Exception e) {
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + info.packageName));
                        }
                    }

                    @Override
                    public void performNegativeAction(int purpose, Bundle backpack) {
                        performAccessTokenOrWelcomeScreen();
                    }
                }).build().show();
            } else {
                performAccessTokenOrWelcomeScreen();

            }
        } catch (PackageManager.NameNotFoundException e) {
//
            Utils.printStackTrace(e);
        }
    }

    private void performAccessTokenOrWelcomeScreen() {
        if (Dependencies.getAccessToken(mActivity).isEmpty() || StorefrontCommonData.getUserData() == null) {
            ifAccessTokenEmpty();
        } else {


            loginViaAccessToken();
            if (StorefrontCommonData.getUserData() != null) {
                redirectAfterAccessToken();
            }
        }
    }

    private void redirectAfterAccessToken() {
        Bundle extras = new Bundle();
        extras.putSerializable(UserData.class.getName(), StorefrontCommonData.getUserData());

        if ((UIManager.isOTPAvailable() && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsPhoneVerified() == 0)
                || (UIManager.getIsEmailVerificationRequried() == 1 && StorefrontCommonData.getUserData().getData().getVendorDetails().getIsEmailVerified() == 0)
                || (StorefrontCommonData.getUserData().getData().getVendorDetails().getRegistrationStatus() != RegistrationStatus.VERIFIED && StorefrontCommonData.getUserData().getData().getSignupTemplateData() != null && !StorefrontCommonData.getUserData().getData().getSignupTemplateData().isEmpty())
                || (StorefrontCommonData.getAppConfigurationData().getIsSubscriptionEnabled() == 1 && StorefrontCommonData.getUserData().getData().getVendorDetails().getSubscriptionPlan().get(0).getPaid() == 0)
                || (StorefrontCommonData.getAppConfigurationData().getIsDebtEnabled() == 1 && StorefrontCommonData.getUserData().getData().getVendorDetails().getDebtAmount() > 0)
                || (StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionEnabled() == 1 && StorefrontCommonData.getAppConfigurationData().getIsCustomerSubscriptionMandatory() == 1)
        ) {
            CommonAPIUtils.userLoginNavigation(mActivity, isOpenCheckoutActivity);


        } else {
            Bundle extra = getIntent().getExtras();
            if (extra != null && extra.getInt(JOB_ID, 0) > 0) {
                try {
                    if (FCMMessagingService.mNotificationManager != null) {
                        FCMMessagingService.mNotificationManager.cancel(Integer.parseInt(getIntent().getExtras().getString("pushID")));
                        FCMMessagingService.clearNotification();
                    }
                } catch (Exception e) {
                }
                try {
                    int jobID = Integer.valueOf(extra.getInt(JOB_ID, 0));
                    int flag = extra.getInt("flag", -1);
                    int status = extra.getInt("jobStatus");
                    Class<?> toClass = Transition.launchOrderDetailsActivity();
                    if (flag == Constants.NotificationFlags.JOB_DELETED) {
                        toClass = Transition.launchHomeActivity();
                    } else if (flag == NotificationFlags.USER_DEBT_PENDING) {
                        toClass = UserDebtActivity.class;
                    } else if ((flag == Constants.NotificationFlags.RULE_ACCEPTED) || (flag == Constants.NotificationFlags.RULE_REJECTED)
                            || (flag == Constants.NotificationFlags.RECURRING_TASK_CREATION_FAIL) || (flag == Constants.NotificationFlags.RULE_CREATED)) {
                        toClass = TasksActivity.class;
                    } else {
                        toClass = Transition.launchOrderDetailsActivity();
                    }
                    extras.putString("pushID", extra.getString("pushID"));
                    extras.putInt(JOB_ID, jobID);
                    Transition.transit(mActivity, toClass, extras);
                } catch (Exception e) {
                    getSuperCategories(StorefrontCommonData.getUserData(), mActivity);
                }
            } else {

                getSuperCategories(StorefrontCommonData.getUserData(), mActivity);

            }
        }
    }

    private void ifAccessTokenEmpty() {

        if (UIManager.isCustomerLoginRequired()) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            imgSplashText.animate().setDuration(600).scaleX(0.7f).scaleY(0.7f).translationY(-(metrics.heightPixels / 2 - Utils.convertDpToPx(mActivity, 120)));
            llAccessTokenLogin.setVisibility(View.GONE);
            final AlphaAnimation fade_out = new AlphaAnimation(1.0f, 0.0f);
            final AlphaAnimation fade_in = new AlphaAnimation(0.0f, 1.0f);
            runOnUiThread(new Runnable() {
                public void run() {
                    fade_in.setDuration(500);
                    fade_out.setDuration(500);
                    fade_in.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation arg0) {
                            llActionButtons.setVisibility(View.INVISIBLE);
                        }

                        public void onAnimationRepeat(Animation arg0) {
                        }

                        public void onAnimationEnd(Animation arg0) {
                            llAccessTokenLogin.setVisibility(View.GONE);
                            openWelcomeOrStartHomeActivity();

                        }
                    });

                    llWelcomeTo.startAnimation(fade_in);
                    llActionButtons.startAnimation(fade_in);
                }
            });
        } else {
            openWelcomeOrStartHomeActivity();
        }
    }

    private void openWelcomeOrStartHomeActivity() {

        if (UIManager.isCustomerLoginRequired() || isOpenCheckoutActivity) {
            llAccessTokenLogin.setVisibility(View.GONE);
            Dependencies.setDemoRun(false);
            llWelcomeTo.setVisibility(View.VISIBLE);
            llActionButtons.setVisibility(View.VISIBLE);
            if (UIManager.isCustomerLoginRequired() && UIManager.getLanguagesArrayList().size() > 1) {
                rlChooseLanguage.setVisibility(View.VISIBLE);
            }
            Log.e("end", "..............................");
        } else {
            getDummyUserDetails();
        }
    }

    private void getDummyUserDetails() {
        CommonAPIUtils.getDummyUserDetailsForDemo(this, countryCode, continentCode);
        if (StorefrontCommonData.getUserData() != null) {
            Dependencies.setDemoRun(true);
            getSuperCategories(StorefrontCommonData.getUserData(), mActivity);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LanguagesCode langCode = (LanguagesCode) parent.getItemAtPosition(position);
        tvChooseLanguage.setText(langCode.getLanguageDisplayName());
        if (++check > 1)
            setLanguageChange(langCode, position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setStrings() {
        tvWelcomeTo.setText(getStrings(Dependencies.isDemoApp() ? R.string.welcome_back : R.string.welcome_back));
        tvContinue.setText(getStrings(R.string.continu));
        tvContinueWith.setText(getStrings(R.string.continue_with));
        etEmail.setHint(getStrings(Dependencies.isDemoApp() ? R.string.your_email : R.string.your_email_or_phone));
        etEmail.setFloatingLabelText(getStrings(Dependencies.isDemoApp() ? R.string.your_email : R.string.your_email_or_phone));

        if (Dependencies.isDemoApp()) {
            tvAppName.setText(getStrings(R.string.signin_demo_account_message));
        } else {
            if (!Dependencies.isAppFirstInstall) {
                tvAppName.setText(getStrings(R.string.sign_in_to_continue));
            } else {
                tvAppName.setText(getStrings(R.string.enter_email_phone_to_continue));
            }
        }
    }

    @Override
    public void onLogin(GoogleSignInAccount account) {
        this.googleAccount = account;
        Log.e("GOOGLE ID", account.getId() + account.getDisplayName() + "--" +
                account.getGivenName() + "---" + account.getFamilyName() + "-----" + account.getPhotoUrl());

        callbackForGoogleLogin();
        signOutGoogle();
    }

    private void signOutGoogle() {
        googleUtil.signOut();
    }

    public void callbackForGoogleLogin() {

        Location location = LocationUtils.getLastLocation(this);
        final JSONObject ipConfigObject = new JSONObject();
        try {
            ipConfigObject.put("country", countryCode);
            Log.v("country_code", countryCode.toString());
            ipConfigObject.put("continent_code", continentCode);
            Log.v("continent_code", continentCode.toString());
        } catch (JSONException e) {
            Utils.printStackTrace(e);
        }
        CommonParams.Builder commonParams = Dependencies.setCommonParamsForAPI(mActivity, null);
        commonParams.add("google_token", googleAccount.getId())
                .add("ipConfig", ipConfigObject);

        if (UIManager.isDualUserEnable() == 0) {
            RestClient.getApiInterface(this).googleLogin(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        } else {
            RestClient.getApiInterface(this).googleLoginDualUser(commonParams.build().getMap()).enqueue(finalSignInResponseResolver());
        }

        ProgressDialog.dismiss();

    }

    private boolean showFacebook() {
        return (UIManager.isFacebookAvailable() && StorefrontCommonData.getAppConfigurationData().getFacebookAppId() != null);
    }

    private boolean showInstagram() {
        return (UIManager.isInstagramAvailable() && StorefrontCommonData.getAppConfigurationData().getInstagramAppId() != null
                && StorefrontCommonData.getAppConfigurationData().getInstagramAppId().equalsIgnoreCase(mActivity.getResources().
                getString(R.string.instagram_client_id)));
    }

    private boolean showGPlus() {
        return (UIManager.isGPlusAvailable() && StorefrontCommonData.getAppConfigurationData().getGoogleClientAppId() != null);
    }

    private String getFacebookId() {
        return showFacebook() && !StorefrontCommonData.getAppConfigurationData().getFacebookAppId().isEmpty() ? StorefrontCommonData.getAppConfigurationData().getFacebookAppId() : getStrings(R.string.facebook_app_id);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(SplashActivity.this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int code, String response) {

    }

    private class SetAddress extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            Log.i("doInBackground", "doInBackground");
            return MapUtils.getGapiJson(new LatLng(latitude, longitude), mActivity);
//            return LocationUtils.getAddressFromLatLng(CreateTaskActivity.this,latlng);
        }

        @Override
        protected void onPostExecute(final JSONObject jsonObject) {

            if (jsonObject != null && jsonObject.has("address"))
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String address = jsonObject.getString("address");
                            UIManager.setAddress(address);
                        } catch (JSONException e) {
                            Utils.printStackTrace(e);
                        }
                    }
                });
        }
    }

}
