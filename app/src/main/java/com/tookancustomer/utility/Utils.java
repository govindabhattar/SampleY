package com.tookancustomer.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.NumberParseException;
import com.hippo.AdditionalInfo;
import com.hippo.CaptureUserData;
import com.hippo.ChatByUniqueIdAttributes;
import com.hippo.HippoColorConfig;
import com.hippo.HippoConfig;
import com.hippo.HippoConfigAttributes;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.MakePaymentActivity;
import com.tookancustomer.MyApplication;
import com.tookancustomer.R;
import com.tookancustomer.WebViewActivity;
import com.tookancustomer.appdata.Codes;
import com.tookancustomer.appdata.Config;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.FuguColorConfigStrings;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.mapfiles.placeapi.PlaceSearchActivity;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.appConfiguration.MapObject;
import com.tookancustomer.models.userdata.PaymentMethod;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.models.userdata.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tookancustomer.modules.payment.constants.PaymentConstants.PaymentValue.CASH;


public class Utils {

    private static final String TAG = "Utils";
    private static final String EMPTY_STRING = "";
    private static final int MULTIPLE_CLICK_THRESHOLD = 800; // in miliseconds
    public static Dialog dialog;
    public static Dialog adDialog = null;
    // variable to track event time
    private static long mLastClickTime = 0;
    private static DecimalFormat decimalFormatDisplay, decimalFormatCalculation, decimalFormatForPercentage;
    private static DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);

    /**
     * Method to prevent multiple clicks
     */
    public static boolean preventMultipleClicks() {
        // Preventing multiple clicks, using threshold of MULTIPLE_CLICK_THRESHOLD  second
        Log.e("TimeDifference", "TimeDifference :" + (SystemClock.elapsedRealtime() - mLastClickTime));
        if (SystemClock.elapsedRealtime() - mLastClickTime < MULTIPLE_CLICK_THRESHOLD) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    public static double getVisablemapRadius(VisibleRegion visibleRegion) {
        float[] distanceWidth = new float[1];
        float[] distanceHeight = new float[1];

        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
        );

        Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
        );
        return Math.sqrt(Math.pow(distanceWidth[0], 2) + Math.pow(distanceHeight[0], 2)) / 2;

    }

    public static double getVisableFlightmapRadius(com.mapbox.mapboxsdk.geometry.VisibleRegion visibleRegion) {
        float[] distanceWidth = new float[1];
        float[] distanceHeight = new float[1];

        com.mapbox.mapboxsdk.geometry.LatLng farRight = visibleRegion.farRight;
        com.mapbox.mapboxsdk.geometry.LatLng farLeft = visibleRegion.farLeft;
        com.mapbox.mapboxsdk.geometry.LatLng nearRight = visibleRegion.nearRight;
        com.mapbox.mapboxsdk.geometry.LatLng nearLeft = visibleRegion.nearLeft;

        Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
        );

        Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
        );
        return Math.sqrt(Math.pow(distanceWidth[0], 2) + Math.pow(distanceHeight[0], 2)) / 2;

    }

    public static void setLightStatusBar(Activity activity, View view) {


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
//            activity.getWindow().setStatusBarColor(Color.WHITE);
//        }
    }

    public static void clearLightStatusBar(Activity activity, View view) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            Window window = activity.getWindow();
//            window.setStatusBarColor(ContextCompat
//                    .getColor(activity,R.color.colorPrimaryDark));
//
//        }
    }

    public static double getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        LatLng latLng1 = new LatLng(lat1, lng1);
        LatLng latLng2 = new LatLng(lat2, lng2);
        return getDistance(latLng1, latLng2);
    }

    public static double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;
    }

    public static Boolean isForceSoftUpdate() {
        return false;
    }

    /**
     * Method that binds a {@link TextWatcher} to the {@link EditText}s so that
     * the user is not able to enter spaces
     *
     * @param editTexts array of {@link EditText}s to which the {@link TextWatcher} is
     *                  added
     */
    public static void addSpaceWatcher(EditText... editTexts) {

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(getSpaceWatcher(editText));
        }
    }

    public static String calculateHourMinsFrmMins(int mins) {
        if (mins > 0) {

            int hours = mins / 60;
            int minutes = mins % 60; // 5 in this case.
            String minHours;
            String hr, min;
            if (hours > 1)
                hr = StorefrontCommonData.getString(getApplicationContext(), R.string.hrs_short);
            else hr = StorefrontCommonData.getString(getApplicationContext(), R.string.hr_short);
            if (minutes > 1)
                min = StorefrontCommonData.getString(getApplicationContext(), R.string.mins_short);
            else min = StorefrontCommonData.getString(getApplicationContext(), R.string.min_short);

            if (hours > 0 && minutes > 0)
                minHours = hours + " " + hr + " " + minutes + " " + min;
            else if (hours == 0 && minutes > 0)
                minHours = minutes + " " + min;
            else if (hours > 0 && minutes == 0)
                minHours = hours + " " + hr;
            else
                minHours = "";

            return minHours;
        }
        return "";
    }

    /**
     * Creates a new {@link TextWatcher} for the {@link EditText}
     *
     * @param editText
     * @return
     */
    private static TextWatcher getSpaceWatcher(final EditText editText) {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    editText.setText(result);
                    editText.setSelection(result.length());
                }
            }
        };
    }

    /**
     * Method to bind an error watcher
     *
     * @param editTexts
     */
    public static void addErrorWatcher(EditText... editTexts) {

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(getErrorWatcher(editText));
        }
    }

    private static TextWatcher getErrorWatcher(final EditText editText) {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setError(null);
            }
        };
    }

    /**
     * Method to set error to null when the focus is changed on the
     * {@link EditText}. e.g., {@link OnFocusChangeListener}
     */
    public static void bindFocusChangeListener(EditText... editTexts) {

        for (EditText editText : editTexts) {
            focusChangeAction(editText);
        }
    }

    private static void focusChangeAction(final EditText editText) {

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editText.setError(null);
            }
        });

    }

    /**
     * Listener to transfer focus on next
     */
    public static OnEditorActionListener transferFocusTo(final EditText toEditText) {

        return new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {

                    case EditorInfo.IME_ACTION_NEXT:
                        toEditText.requestFocus();
                        break;

                    default:
                }
                return false;
            }
        };
    }

    /**
     * Method to set errors on the fields
     *
     * @param editText
     * @param message
     */
    public static void setErrorOn(EditText editText, String message) {

        editText.requestFocus();

//        if (message.trim().equals(EMPTY_STRING))
////            editText.setError(AppManager.getInstance().getActivity().getString(R.string.field_cannot_be_left_blank));
//        else
//            editText.setError(message);
    }

    /**
     * Method to set errors on the fields
     *
     * @param editText
     */
    public static void setErrorOn(EditText editText) {

        setErrorOn(editText, EMPTY_STRING);
    }

    /**
     * Method to check for empty {@link EditText}s
     *
     * @param editTexts
     * @return
     */
    public static boolean areEmpty(EditText... editTexts) {

        boolean isEmpty = false;

        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                setErrorOn(editText);
                isEmpty = true;
                break;
            }
        }

        return isEmpty;
    }

    /**
     * Method to check for empty {@link EditText}s
     *
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {

        return isEmpty(editText, EMPTY_STRING);
    }

    public static boolean isEmpty(EditText editText, String message) {

        if (get(editText).isEmpty()) {
            setErrorOn(editText, message);
            return true;
        }

        return false;
    }

    public static boolean isEmpty(String string) {
        if (string == null)
            return true;

        if (string.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Validates the character sequence with email format
     *
     * @param email
     * @return true, if the string entered by user is syntactically correct as
     * email, false otherwise
     */
    public static boolean isEmailValid(String email) {

        // Check whether the Email is valid
        if (email == null) {
            return false;
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validates the character sequence with url format
     *
     * @param url
     * @return true, if the string entered by user is syntactically correct as
     * url, false otherwise
     */
    public static boolean isUrlValid(String url) {

        // Check whether the Email is valid
        if (url == null) {
            return false;
        }

        return Patterns.WEB_URL.matcher(url).matches();
    }

    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 6 || target.length() > 15) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    public final static boolean isValidPhoneNumberWithCountryCode(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            String number[] = target.toString().split(" ");
            if (number.length < 2) {
                return false;
            } else if (number[1].length() < 6 || number[1].length() > 14) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Method to bind a phone watcher to the PHONE EDIT TEXT
     *
     * @param editText
     */
    public static void bindPhoneWatcher(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                String phone = s.toString();

                phone = extractNumber(phone);

//                String res = formatPhoneNumber(phone);
                String res = standardFormat(phone);

                if (!s.toString().equals(res)) {
                    editText.setText(res);
                    editText.setSelection(res.length());
                }
            }
        });
    }

    /**
     * Extracts the phone number
     *
     * @param number
     * @return
     */
    public static String extractNumber(String number) {

        String phone = number.replaceAll(" ", "").replaceAll("\\(", "").replace(")", "").replace("-", "");

        return phone;
    }

    /**
     * Formats a given number
     */
    public static String formatNumber(String number) {

        String phone = "";

        if (number != null) {
            phone = extractNumber(number);
        }

        String res = "";
        for (int i = 0; i < phone.length(); i++) {

            if (i == 3 || i == 6) {
                res = res + " " + phone.charAt(i);
                continue;
            }

            res = res + phone.charAt(i);
        }

        return res;
    }

    /**
     * Method to dissociate a number into "code, number"
     *
     * @param number the number to be evaluated
     * @return {"code", "phone"}
     */
    public static String[] unFormatNumber(String number) {

        String[] codePhone = new String[]{EMPTY_STRING, EMPTY_STRING};

        if (number == null) {
            return codePhone;
        }

        if (number.contains("(")) {
            codePhone = number.split("\\(");
            codePhone[0] = extractNumber(codePhone[0]);
            codePhone[1] = extractNumber(codePhone[1]);
        }

        return codePhone;
    }

    /**
     * Formats a given number
     */
    public static String formatNumberWithCountryCode(String countryCode, String phoneNumber) {

        String code = "";
        String phone = "";

        if (countryCode != null) {
            code = "+" + extractNumber(countryCode);
        }

        if (phoneNumber != null) {
            phone = extractNumber(phoneNumber);
        }

        return code + standardFormat(phone);
    }

    /**
     * Method to format a number according to length
     *
     * @param phone
     * @return
     */
    private static String formatPhoneNumber(String phone) {

        return format433(phone);
    }

    private static String format433(String phone) {

        String result = "";

        for (int i = 0; i < phone.length(); i++) {

            if (i == 4 || i == 7) {
                result = result + " ";
            }

            result = result + phone.charAt(i);
        }

        return result;
    }

    /**
     * Method to format a number as XXX XXXX XXXX
     *
     * @param phone
     * @return
     */
    private static String format344(String phone) {

        String result = "";

        for (int i = 0; i < phone.length(); i++) {

            if (i == 3 || i == 7) {
                result = result + " ";
            }

            result = result + phone.charAt(i);
        }

        return result;
    }

    /**
     * Method to format a number as XXX XXX XXXX
     *
     * @param phone
     * @return
     */
    private static String format334(String phone) {

        String result = "";

        for (int i = 0; i < phone.length(); i++) {

            if (i == 3 || i == 6) {
                result = result + " ";
            }

            result = result + phone.charAt(i);
        }

        return result;
    }

    /**
     * Method formats the number into "(XXX) XXX-XXXX" format
     *
     * @param number
     * @return
     */
    public static String standardFormat(String number) {

        String formattedNumber = EMPTY_STRING;

        if (number == null) {
            return formattedNumber;
        }

        for (int position = 0; position < number.length(); position++) {

            switch (position) {

                case 0:
                    formattedNumber = formattedNumber + "(";
                    break;

                case 3:
                    formattedNumber = formattedNumber + ") ";
                    break;

                case 6:
                    formattedNumber = formattedNumber + "-";
                    break;
            }

            formattedNumber = formattedNumber + number.charAt(position);

        }

        return formattedNumber;
    }

    /**
     * Method to format a number as XXX XXX XXX
     *
     * @param phone
     * @return
     */
    private static String format333(String phone) {

        String result = "";

        for (int i = 0; i < phone.length(); i++) {

            if (i == 3 || i == 6 || i == 9) {
                result = result + " ";
            }

            result = result + phone.charAt(i);
        }

        return result;
    }

    /**
     * Capitalizes the first letter of each word
     *
     * @param sentence
     * @return
     */
    public static String capitaliseWords(String sentence) {

        if (sentence == null) {
            return "";
        }

        String[] words = sentence.split(" ");
        ArrayList<String> result = new ArrayList<>();

        for (String word : words) {

            word = word.trim();

            if (word.length() > 0) {
                String firstLetter = Character.toString(word.charAt(0));
                try {
                    word = word.replaceFirst(firstLetter, firstLetter.toUpperCase());
                } catch (Exception e) {
                }
                result.add(word);
            }
        }

        return TextUtils.join(" ", result);
    }

    /**
     * Capitalizes the first letter of sentence
     *
     * @param sentence
     * @return
     */
    public static String capitaliseSentence(String sentence) {

        if (sentence == null) {
            return "";
        }

        String[] words = sentence.split(" ");
        ArrayList<String> result = new ArrayList<>();

        for (String word : words) {

            word = word.trim();

            if (word.length() > 0) {
                String firstLetter = Character.toString(word.charAt(0));
                word = word.replaceFirst(firstLetter, firstLetter.toUpperCase());
                result.add(word);
            }
        }

        return TextUtils.join(" ", result);
    }

    /**
     * Lowercase the first letter of each word
     *
     * @param sentence
     * @return
     */
    public static String convertToLowerCase(String sentence) {

        if (sentence == null) {
            return "";
        }

        String[] words = sentence.split(" ");
        ArrayList<String> result = new ArrayList<>();

        for (String word : words) {

            word = word.trim();

            if (word.length() > 0) {
                String firstLetter = Character.toString(word.charAt(0));
                word = word.replaceFirst(firstLetter, firstLetter.toLowerCase());
                result.add(word);
            }
        }

        return TextUtils.join(" ", result);
    }

    /**
     * Method used to hide keyboard if outside touched.
     *
     * @param view
     */
    public static void hideKeyboardOnTouchOutside(View view, final Activity activity) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }
        // If a layout container, iterate over children and seed recursion.

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardOnTouchOutside(innerView, activity);

            }
        }
    }

    /**
     * Method used to hide keyboard if outside touched.
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {

        View focusedView = activity.getCurrentFocus();
        if (focusedView == null) {
            return;
        }

        IBinder windowToken = focusedView.getWindowToken();
        if (windowToken == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);


    }

    /**
     * Method used to hide keyboard if outside touched.
     *
     * @param activity
     */
    public static void showSoftKeyboard(Activity activity) {

        try {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
    }

    /**
     * Method used to show generic dialog.
     *
     * @param activity
     */
    public static void showGenericDialog(final Activity activity, final boolean isPromoView) {
        showGenericDialog(activity, isPromoView, 0, 0);
    }

    public static void showGenericDialog(final Activity activity, final boolean isPromoView, final double minTipAmount, final double tip) {

        dialog = new Dialog(activity, R.style.NotificationDialogTheme);
        dialog.setContentView(R.layout.custom_dialog_view);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        params.gravity = Gravity.BOTTOM;

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(params);

        final EditText etAmount = dialog.findViewById(R.id.etAmount);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        btCancel.setText(StorefrontCommonData.getString(activity, R.string.cancel));

        Button btAdd = dialog.findViewById(R.id.btAdd);
        btAdd.setText(StorefrontCommonData.getString(activity, R.string.add));

        if (isPromoView) {
            etAmount.setHint(StorefrontCommonData.getString(activity, R.string.enter_code));
            btAdd.setText(StorefrontCommonData.getString(activity, R.string.apply));
            etAmount.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        } else {
            etAmount.setText(tip > minTipAmount ? Utils.getDoubleTwoDigits(tip) + "" : Utils.getDoubleTwoDigits(minTipAmount) + "");
            etAmount.setSelection(etAmount.length());
            if (UIManager.getTipType() == 0) {
                etAmount.setHint(StorefrontCommonData.getTerminology().getTip() + " " + StorefrontCommonData.getString(activity, R.string.amount_text));
            } else if (UIManager.getTipType() == 1) {
                etAmount.setHint(StorefrontCommonData.getTerminology().getTip() + "" + StorefrontCommonData.getString(activity, R.string.percentage_text));
            }
        }
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(activity);

                if (isPromoView) {
                    if (etAmount.getText().toString().isEmpty()) {
//                        Utils.hideSoftKeyboard(activity);
//                        new AlertDialog.Builder(activity).message(R.string.please_enter_code).build().show();
                        etAmount.setError(StorefrontCommonData.getString(activity, R.string.please_enter_code));

//                        Utils.snackBar(activity, activity.getString(R.string.please_enter_code));
                    } else {
                        ((MakePaymentActivity) activity).apply(etAmount.getText().toString());
                        dialog.dismiss();
                    }
                } else {
                    if (etAmount.getText().toString().isEmpty()) {
                        if (UIManager.getTipType() == 0) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_amount));
                        } else if (UIManager.getTipType() == 1) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_percentage_amount));
                        }

                    } else if (etAmount.getText().toString().equals(".")) {
                        if (UIManager.getTipType() == 0) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_valid_amount));
                        } else if (UIManager.getTipType() == 1) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_valid_percentage_amount));
                        }
                    } else if (Double.parseDouble(etAmount.getText().toString()) == 0) {
                        if (UIManager.getTipType() == 0) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_valid_amount));
                        } else if (UIManager.getTipType() == 1) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_valid_percentage_amount));
                        }
                    } else if (Double.parseDouble(etAmount.getText().toString()) < minTipAmount) {
                        if (UIManager.getTipType() == 0) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.minimum) + " " + StorefrontCommonData.getTerminology().getTip() + " " + StorefrontCommonData.getString(activity, R.string.amount) + " " + StorefrontCommonData.getString(activity, R.string.is_text) + " " + Utils.getCurrencySymbol() + Utils.getDoubleTwoDigits(minTipAmount));
                        } else if (UIManager.getTipType() == 1) {
                            Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.please_enter_valid_percentage_amount));
                        }
                    } else {
                        if (activity instanceof MakePaymentActivity) {
                            ((MakePaymentActivity) activity).notifyTip(Double.parseDouble(etAmount.getText().toString()));
                        }
                        dialog.dismiss();
                    }
                }


            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 200);

    }

    public static float convertDpToPx(Context context, int i) {
        final float density = context.getResources().getDisplayMetrics().density;
        return density * i + 0.5f;
    }

    /**
     * Method to assign Strings safely
     *
     * @param assignable
     * @param alternative
     * @return
     */
    public static String assign(String assignable, String alternative) {

        return assignable == null ? (alternative == null ? EMPTY_STRING : alternative) : (assignable.equals("null") ? assign(alternative) : assignable);
    }

    /**
     * Method to assign Strings safely
     *
     * @param assignable
     * @param filterAssignable
     * @param alternative
     * @return
     */
    public static String assign(String assignable, String filterAssignable, String alternative) {

        return assignable == null ? (alternative == null ? EMPTY_STRING : alternative) : (assignable.equals(filterAssignable) ? assign(alternative) : assignable);
    }

    /**
     * Method to assign strings Safely
     *
     * @param assignable
     * @return
     */
    public static String assign(String assignable) {

        return assignable == null || assignable.equalsIgnoreCase("[]") ? EMPTY_STRING : assignable;
    }

    /**
     * Method to extract the Text from TextView
     *
     * @param textView
     * @return
     */
    public static String get(TextView textView) {
        return textView.getText().toString();
    }

    /**
     * Method to extract the Text from TextView
     *
     * @param editText
     * @return
     */
    public static String get(EditText editText) {

        if (editText == null) {
            return "";
        }

        return editText.getText().toString().trim();
    }

    /**
     * Method to extract the Text from TextView
     *
     * @param editTexts
     * @return
     */
    public static String get(EditText... editTexts) {
        return get(null, editTexts);
    }

    /**
     * Method to extract the Text from TextView
     *
     * @param editTexts
     * @return
     */
    public static String get(String join, EditText... editTexts) {

        String text = EMPTY_STRING;

        if (join == null) {
            join = EMPTY_STRING;
        }

        for (EditText editText : editTexts) {
            text = text + join + get(editText);
        }

        return text.replaceFirst(join, EMPTY_STRING);
    }

    /**
     * Method to convert a String to Integer
     *
     * @param value
     * @return
     */
    public static int toInt(String value) {
        return toInt(value, -1);
    }

    /**
     * Method to convert a String to Integer
     *
     * @param value
     * @param defValue
     * @return
     */
    public static int toInt(String value, int defValue) {

        int parsed;

        try {
            parsed = Integer.parseInt(value);
        } catch (Exception ex) {
            parsed = defValue;
            Utils.printStackTrace(ex);
        }
        return parsed;
    }

    /**
     * Method to convert a String to Integer
     *
     * @param convertible
     * @return
     */
    public static double toDouble(String convertible) {

        double converted;

        try {
            converted = Double.parseDouble(convertible);
        } catch (Exception ex) {
            converted = 0.0;
            Utils.printStackTrace(ex);
        }

        return converted;
    }

    /**
     * Method to open the Dialer
     */
    public static void openMessagingApplication(Context context, String phone) {
        try {
//        AppManager.getInstance().logFirebaseEvent(Keys.FirebaseEvents.OPEN_MESSAGE_APP);
            String number = Utils.extractNumber(phone);

            Uri telNumber = Uri.parse("smsto:" + number);
            Intent openSms = new Intent(Intent.ACTION_SENDTO, telNumber);
            context.startActivity(openSms);
        } catch (Exception ex) {
            Utils.printStackTrace(ex);
        }
    }

    /**
     * Method to open the Call Dialer
     */
    public static void openCallDialer(Context context, String phone) {
        try {
            if (phone.isEmpty()) {
                return;
            }

            String number = Utils.extractNumber(phone);

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * Method to init toast to the User
     *
     * @param activity
     * @param message
     */
    public static void toast(final Activity activity, final String message) {

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(activity, assign(message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method to init toast to the User
     *
     * @param activity
     * @param message
     */
    public static void longToast(final Activity activity, final String message) {

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(activity, assign(message), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Method to split the string via first character occurrence
     *
     * @param sentence
     * @param c
     * @return
     */
    public static String[] splitViaFirstCharacter(String sentence, char c) {

        // Checks whether the String received is null
        if (sentence == null) {
            return new String[]{EMPTY_STRING, EMPTY_STRING};
        }

        // Checks whether the given character exists in the String
        int firstOccurrence = sentence.indexOf(c);

        if (firstOccurrence == -1) {
            return new String[]{sentence, EMPTY_STRING};
        }

        String firstPhrase = sentence.substring(0, firstOccurrence);
        String secondPhrase = sentence.substring(firstOccurrence + 1, sentence.length());

        return new String[]{firstPhrase, secondPhrase};
    }

    /**
     * Method to convert the value from
     * density pixels to pixels
     *
     * @param dpValue
     * @return
     */
    public static int convertDpToPixels(Context context, float dpValue) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * Method to set the Cursor to the End of the text
     *
     * @param editText
     */
    public static void adjustCursor(EditText editText) {

        if (editText == null) {
            return;
        }

        editText.setSelection(get(editText).length());
    }

    /**
     * Method to set the Cursor to the End of the text
     *
     * @param editTexts
     */
    public static void adjustCursor(EditText... editTexts) {

        for (EditText editText : editTexts) {
            adjustCursor(editText);
        }
    }

    /**
     * Method to set same OnClickListener on multiple views
     *
     * @param listener
     * @param views
     */
    public static void setOnClickListener(View.OnClickListener listener, View... views) {

        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * Method to set same Visibility for Multiple Views
     *
     * @param visibility
     * @param views
     */
    public static void setVisibility(int visibility, View... views) {

        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    public static void hideSoftKeyboard(Activity activity, View view) {

        try {

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
    }

    /**
     * Method to redirect the User to the page from
     * where a new version of App could be downloaded.
     */
    public static void redirectUserToUrl(Activity activity, String webUrl) {

        if (webUrl == null) {
            return;
        }

        if (webUrl.isEmpty()) {
            return;
        }

        boolean isRecognizable = webUrl.startsWith("http://") || webUrl.startsWith("https://");
        webUrl = (isRecognizable ? "" : "http://") + webUrl;

        try {
            Intent openWebUrl = new Intent(Intent.ACTION_VIEW);
            openWebUrl.setData(Uri.parse(webUrl));
            activity.startActivity(openWebUrl);
//            activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } catch (ActivityNotFoundException e) {
//            toast(activity, activity.getString(R.string.no_app_found_to_open_the_link));

            Utils.printStackTrace(e);
        }
    }

    /**
     * Method to open the Email Activity
     *
     * @param context
     * @param subject
     * @param receivers
     * @param message
     */
    public static void openEmailApp(Context context, String subject, String[] receivers, String message) throws Exception {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
    }

    /**
     * Method to redirect the User to Developer Settings
     */
    public static void redirectToDeveloperSetting(Activity activity) {

        Intent developerSettingPage = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
//        activity.startActivityForResult(developerSettingPage, Codes.Request.OPEN_DEVELOPER_SETTINGS);
    }

    /**
     * Method to convert the String Boolean Values Safely with an alternative
     *
     * @param value
     * @return
     */
    public static boolean toBoolean(String value, boolean alternative) {

        return value == null ? alternative : toBoolean(value);
    }

    /**
     * Method to convert the String Boolean Values
     *
     * @param value
     * @return
     */
    public static boolean toBoolean(String value) {

        try {
            return Boolean.parseBoolean(value);
        } catch (Exception ex) {
            Utils.printStackTrace(ex);
            return false;
        }
    }

    /**
     * Method to convert a String into Long
     *
     * @param value
     * @return
     */
    public static long toLong(String value) {

        try {
            return Long.parseLong(value);
        } catch (Exception e) {

            Utils.printStackTrace(e);
            return 0;
        }
    }

    /**
     * Method to convert a String into Float
     *
     * @param value
     */
    public static float toFloat(String value) {

        try {
            return Float.parseFloat(value);
        } catch (Exception e) {

            Utils.printStackTrace(e);
            return 0;
        }
    }

    /**
     * Method to check whether a String is number
     *
     * @param value
     */
    public static boolean isNumeric(String value) {

        if (value == null) {
            return false;
        }

        return Pattern.matches("-?\\d+(\\.\\d+)?", value);
    }

    /**
     * Method to set background to a View according to
     * the System API Level
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Method to
     *
     * @param context
     * @return
     */
    public static int getBatteryLevel(Context context) {

        float batteryPercentage;

        try {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            if (batteryStatus == null) {
                return -1;
            }

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level < 0 || scale < 0) {
                batteryPercentage = -1;
            } else {
                batteryPercentage = (level / (float) scale) * 100;
            }

        } catch (Exception e) {

            batteryPercentage = -1;
        }

        return (int) batteryPercentage;
    }

    /**
     * Method to convert the time in second to watch
     * (implementation: Forward Calculation with Graceful Degradation)
     *
     * @param timeInSeconds
     */
    public static String getTimeAsCounter(long timeInSeconds) {

        // forward calculation
        long seconds = timeInSeconds;
        long minutes = seconds >= 60 ? seconds / 60 : 0;
        long hours = minutes >= 60 ? minutes / 60 : 0;
        long days = hours >= 24 ? hours / 24 : 0;
        long weeks = days >= 7 ? days / 7 : 0;
        long years = weeks >= 52 ? weeks / 52 : 0;
        long decades = years >= 10 ? years / 10 : 0;
        long centuries = decades >= 10 ? decades / 10 : 0;

        // backward calculation
        long gracefulDegradation;
        decades = decades - (gracefulDegradation = centuries * 10);
        years = years - (gracefulDegradation = (decades + gracefulDegradation) * 10);
        weeks = weeks - (gracefulDegradation = (years + gracefulDegradation) * 52);
        days = days - (gracefulDegradation = (weeks + gracefulDegradation) * 7);
        hours = hours - (gracefulDegradation = (days + gracefulDegradation) * 24);
        minutes = minutes - (gracefulDegradation = (hours + gracefulDegradation) * 60);
        seconds = seconds - (minutes + gracefulDegradation) * 60;

        // Representing time
        String time = "";
        time += centuries > 0 ? (centuries > 9 ? "" : "0") + centuries + ":" : "";
        time += decades > 0 ? (decades > 9 ? "" : "0") + decades + ":" : (time.isEmpty() ? "" : "00:");
        time += years > 0 ? (years > 9 ? "" : "0") + years + ":" : (time.isEmpty() ? "" : "00:");
        time += weeks > 0 ? (weeks > 9 ? "" : "0") + weeks + ":" : (time.isEmpty() ? "" : "00:");
        time += days > 0 ? (days > 9 ? "" : "0") + days + ":" : (time.isEmpty() ? "" : "00:");
        time += hours > 0 ? (hours > 9 ? "" : "0") + hours + ":" : (time.isEmpty() ? "" : "00:");
        time += minutes > 0 ? ((minutes > 9 ? "" : "0") + minutes + ":") : (time.isEmpty() ? "" : "00:");
        time += seconds > 9 ? "" + seconds : "0" + seconds;


        return time;
    }

    /**
     * Method to set the Text to a field
     *
     * @param text
     * @param textViews
     */
    public static void setCommonText(String text, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setText(text);
        }
    }

    public static void setText(String text, TextView textView) {
        setText(text, null, null, textView);
    }

    public static void setText(String text, String alternative, TextView textView) {
        setText(text, null, alternative, textView);
    }

    public static void setText(String text, String filter, String alternative, TextView textView) {

        if (textView == null) {
            return;
        }

        if (alternative == null) {
            textView.setText(Utils.assign(text));
        } else if (filter == null) {
            textView.setText(Utils.assign(text, alternative));
        } else {
            textView.setText(Utils.assign(text, filter, alternative));
        }

    }

    /**
     * Method to enable/disable a list of Views
     *
     * @param enabled
     * @param views
     */
    public static void setEnabled(boolean enabled, View... views) {
        for (View view : views) {
            view.setEnabled(enabled);
        }
    }

//    public static void searchPlace(Activity activity, int mode, LatLng currentLatLng) {
//        try {
//            Intent intent = new PlaceAutocomplete.IntentBuilder(mode).setBoundsBias(toBounds(new LatLng(currentLatLng.latitude, currentLatLng.longitude))).build(activity);
//            activity.startActivityForResult(intent, Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE);
//        } catch (GooglePlayServicesRepairableException e) {
//            // TODO: Handle the error.
//        } catch (GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
//    }

    /**
     * Set the specified text color to all the TextView(s)
     *
     * @param textColor
     * @param textViews
     */
    public static void setTextColor(Context mContext, int textColor, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTextColor(ContextCompat.getColor(mContext, textColor));
        }
    }

    /**
     * Method to join elements of an ArrayList using join
     *
     * @param list
     * @param fieldName
     * @param join
     * @return
     */
    public static String join(ArrayList list, String fieldName, String join) {

        if (list == null || list.isEmpty()) {
            return null;
        }

        String result = "";

        try {
            for (Object objT : list) {
                Field field = objT.getClass().getDeclaredField(fieldName);
                if (field.getType().isAssignableFrom(String.class)) {
                    field.setAccessible(true);
                    String value = (String) field.get(objT);
                    result += value + join;
                }
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }

        return result.isEmpty() ? null : result.substring(0, result.lastIndexOf(join));
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static void searchPlace(Activity activity) {
        MapObject mapObject = StorefrontCommonData.getAppConfigurationData().getMapObject();
        if (StorefrontCommonData.getAppConfigurationData().getMapType() == Constants.MapConstants.GOOGLEMAP) {
            if (mapObject == null || Utils.isEmpty(mapObject.getGoogleApiKey())) {
                Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.google_map_key_is_empty));
                return;
            }
        } else if (StorefrontCommonData.getAppConfigurationData().getMapType() == Constants.MapConstants.FLIGHTMAP) {
            if (mapObject == null || Utils.isEmpty(mapObject.getAndroidMapApiKey())) {
                Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.flightmap_key_is_empty));
                return;
            }
        }
        Transition.transitForResult(activity, PlaceSearchActivity.class, Codes.Request.PLACE_AUTOCOMPLETE_REQUEST_CODE, null);
    }

    public static LatLngBounds toBounds(LatLng center) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(center);
        return builder.build();
    }

    /**
     * Method to init toast to the User
     *
     * @param activity
     * @param message
     */
    public static void snackBar(Activity activity, String message) {
        try {
            snackBar(activity, message, true, activity.getWindow().getDecorView().findViewById(android.R.id.content));
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
    }

    public static void snackBar(Activity activity, String message, boolean sendErrorLogToBackend) {
        try {
            snackBar(activity, message, sendErrorLogToBackend, activity.getWindow().getDecorView().findViewById(android.R.id.content));
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
    }

    public static void snackBar(final Activity activity, final String message, boolean sendErrorLogToBackend, final View view) {

        if (activity == null) {
            return;
        }

        if (sendErrorLogToBackend) {
            ErrorLogs.errorLog(activity, message);
        }


        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                TextView tv = view.findViewById(R.id.snackbar_text);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                Font.bind(activity, Font.LIGHT, tv);
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.snackbar_bg_color_failure));

                snackbar.show();
            }
        });
    }

    /**
     * Method to init toast to the User
     *
     * @param activity
     * @param message
     */
    public static void snackbarSuccess(final Activity activity, final String message) {
        snackbarSuccess(activity, message, activity.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public static void snackbarSuccess(final Activity activity, final String message, final View view) {

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                TextView tv = view.findViewById(R.id.snackbar_text);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setTextColor(ContextCompat.getColor(activity, R.color.white));
                Font.bind(activity, Font.LIGHT, tv);
                view.setBackgroundColor(ContextCompat.getColor(activity, R.color.snackbar_bg_color_success));
                snackbar.show();
            }
        });
    }

    public static boolean internetCheck(Context act) {
        boolean net;

        try {
            ConnectivityManager ConMgr = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (ConMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                    || ConMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                net = true;
            else {
                net = false;
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
            return false;
        }
        //TODO CHange this
        return net;
//		return true;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static void printKeyHash(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            final PackageInfo PackageInfo = manager.getPackageInfo(context.getPackageName(), 0);

            PackageInfo info = context.getPackageManager().getPackageInfo(PackageInfo.packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEY HASH ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            Utils.printStackTrace(e);
        } catch (NoSuchAlgorithmException e) {

            Utils.printStackTrace(e);
        }
    }

    public static boolean isCashPayment(int paymentMethod) {
        return paymentMethod == CASH.intValue;
    }

    public static void saveUserInfo(Activity activity, UserData userData) {
        saveUserInfo(activity, userData, true);
    }

    public static void saveUserInfo(Activity activity, UserData userData, boolean isLoginScreenAtFirst) {
        saveUserInfo(activity, userData, isLoginScreenAtFirst, 1);
    }

    public static void saveUserInfo(Activity activity, UserData userData, boolean isLoginScreenAtFirst, int isFuguChatInitialise) {

//        HippoConfig.clearHippoData(activity);

        StorefrontCommonData.setUserData(userData);
        Dependencies.saveAccessToken(activity, userData.getData().getAppAccessToken());
        Dependencies.saveDeviceToken(activity, Dependencies.getDeviceToken(activity));
        StorefrontCommonData.setLastPaymentMethod(userData.getData().getVendorDetails().getLastPaymentMethod());
        StorefrontCommonData.setLastPaymentMethod(userData.getData().getVendorDetails().getLastPaymentMethod());
        LanguagesCode selectedLanguageCode = new LanguagesCode(userData.getData().getVendorDetails().getLanguageCode(), userData.getData().getVendorDetails().getLanguageName(), userData.getData().getVendorDetails().getLanguageDisplayName());
        StorefrontCommonData.setLanguageCode(selectedLanguageCode);

        if (userData.getData().getLanguageStrings() != null)
            StorefrontCommonData.setLanguageStrings(activity, userData.getData().getLanguageStrings());

        if (userData.getData().getVendorDetails() != null) {
            Dependencies.saveToggleView(activity, userData.getData().getVendorDetails().getDualUserCurrentStatus());
        }

        if (userData.getData().getUserOptions() != null)
            Dependencies.saveUserOptions(activity, userData.getData().getUserOptions());
        if (userData.getData().getDeliveryOptions() != null)
            Dependencies.saveDeliveryOptions(activity, userData.getData().getDeliveryOptions());
        if (userData.getData().getSignupTemplateData() != null)
            Dependencies.saveSignupTemplates(userData.getData().getSignupTemplateData(), activity);

        if (Dependencies.isDemoApp()) {
            if (userData.getData().getMarketplaceReferenceId() != null) {
                Dependencies.setMarketplaceReferenceId(userData.getData().getMarketplaceReferenceId());
            }
            if (userData.getData().getNewUserId() != null)
                userData.getData().getVendorDetails().setUserId(userData.getData().getNewUserId());
        }
        if (UIManager.isFuguChatEnabled() && isFuguChatInitialise == 1) {

            CaptureUserData fuguUserData;
            if (userData.getData().getVendorDetails().getVendorId() != null && !userData.getData().getVendorDetails().getVendorId().toString().isEmpty() && userData.getData().getVendorDetails().getVendorId() != 0) {
                fuguUserData = new CaptureUserData.Builder()
                        .fullName(userData.getData().getVendorDetails().getFirstName())
                        .userUniqueKey(userData.getData().getVendorDetails().getVendorId() + "")
                        .email(userData.getData().getVendorDetails().getEmail())
                        .phoneNumber(userData.getData().getVendorDetails().getPhoneNo())
                        .build();
            } else {
                String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                fuguUserData = new CaptureUserData.Builder()
                        .userUniqueKey(androidId)
                        .build();
            }


            String userId = StorefrontCommonData.getAppConfigurationData().getUserId() + "00" + "19";


            AdditionalInfo additionalInfo = new AdditionalInfo.Builder()
                    .hasChannelPager(true)
                    .hasCreateNewChat(true)
                    .setCreateChatBtnText(StorefrontCommonData.getString(activity, R.string.support))
                    .setEmptyChannelList(StorefrontCommonData.getString(activity, R.string.no_data_found_backend_message))
                    .build();


            HippoConfigAttributes.Builder attributesBuilder = new HippoConfigAttributes.Builder()
                    .setAppType(userId)
                    .setAppKey(StorefrontCommonData.getFormSettings().getFuguChatToken())
                    .setCaptureUserData(fuguUserData)
                    .setProvider(BuildConfig.APPLICATION_ID + ".provider")
                    .setUnreadCount(true)
                    .setShowLog(BuildConfig.DEBUG)
                    .setAdditionalInfo(additionalInfo)
                    .setDeviceToken(Dependencies.getDeviceToken(activity));

            if (!BuildConfig.isForking) {
                /*HashMap<String, String> customAttributes = new HashMap<>();
                customAttributes.put("app_name", activity.getResources().getString(R.string.app_name));
                customAttributes.put("bundle_android_id", BuildConfig.APPLICATION_ID);
                customAttributes.put("role", "1");
                customAttributes.put("user_id", StorefrontCommonData.getAppConfigurationData().getUserId() + "");
                customAttributes.put("source_type", "YELO");*/
                if (fuguUserData != null) {
                  //  fuguUserData.setCustom_attributes(customAttributes);
                    attributesBuilder.setCaptureUserData(fuguUserData);
                }


            }

            HippoColorConfig fuguColorConfig = new HippoColorConfig.Builder()
                    .hippoActionBarBg(FuguColorConfigStrings.FUGU_ACTION_BAR_BG)
                    .hippoActionBarText(FuguColorConfigStrings.FUGU_ACTION_BAR_TEXT)
                    .hippoBgMessageYou(FuguColorConfigStrings.FUGU_BG_MESSAGE_YOU)
                    .hippoBgMessageFrom(FuguColorConfigStrings.FUGU_BG_MESSAGE_FROM)
                    .hippoPrimaryTextMsgYou(FuguColorConfigStrings.FUGU_PRIMARY_TEXT_MSG_YOU)
                    .hippoMessageRead(FuguColorConfigStrings.FUGU_MESSAGE_READ)
                    .hippoPrimaryTextMsgFrom(FuguColorConfigStrings.FUGU_PRIMARY_TEXT_MSG_FROM)
                    .hippoSecondaryTextMsgYou(FuguColorConfigStrings.FUGU_SECONDARY_TEXT_MSG_YOU)
                    .hippoSecondaryTextMsgFrom(FuguColorConfigStrings.FUGU_SECONDARY_TEXT_MSG_FROM)
                    .hippoTextColorPrimary(FuguColorConfigStrings.FUGU_TEXT_COLOR_PRIMARY)
                    .hippoChannelDateText(FuguColorConfigStrings.FUGU_CHANNEL_DATE_TEXT)
                    .hippoChatBg(FuguColorConfigStrings.FUGU_CHAT_BG)
                    .hippoBorderColor(FuguColorConfigStrings.FUGU_BORDER_COLOR)
                    .hippoChatDateText(FuguColorConfigStrings.FUGU_CHAT_DATE_TEXT)
                    .hippoThemeColorPrimary(FuguColorConfigStrings.FUGU_THEME_COLOR_PRIMARY)
                    .hippoThemeColorSecondary(FuguColorConfigStrings.FUGU_THEME_COLOR_SECONDARY)
                    .hippoTypeMessageBg(FuguColorConfigStrings.FUGU_TYPE_MESSAGE_BG)
                    .hippoTypeMessageHint(FuguColorConfigStrings.FUGU_TYPE_MESSAGE_HINT)
                    .hippoTypeMessageText(FuguColorConfigStrings.FUGU_TYPE_MESSAGE_TEXT)
                    .hippoStatusBar(FuguColorConfigStrings.FUGU_STATUS_BAR)
                    .hippoSecondaryTextMsgFromName(FuguColorConfigStrings.FUGU_SECONDARY_TEXT_MSG_FROM)
                    .hippoChannelBg(FuguColorConfigStrings.FUGU_CHANNEL_BG)
                    .hippoStatusBar(FuguColorConfigStrings.FUGU_STATUS_BAR)
                    .hippoSecondaryTextMsgFromName(FuguColorConfigStrings.FUGU_SECONDARY_TEXT_MSG_FROM)
                    .hippoSendBtnBg(FuguColorConfigStrings.FUGU_SEND_BTN)
                    .hippoToolbarHighlighted(FuguColorConfigStrings.FUGU_ACTION_BAR_TEXT)
                    .hippoToolbardisable(FuguColorConfigStrings.FUGU_ACTION_BAR_TEXT)
                    .hippoFloatingBtnBg(FuguColorConfigStrings.FUGU_BG_MESSAGE_FROM)
                    .hippoFloatingBtnText(FuguColorConfigStrings.FUGU_BG_MESSAGE_YOU)
                    .build();


            attributesBuilder.setColorConfig(fuguColorConfig);


            String serverType = "test";
            /**
             * //TODO
             * server type is live
             */
            if (Config.getCurrentAppModeName(activity).equals("LIVE") || Config.getCurrentAppModeName(activity).equals("BETA")) {
                serverType = "live";
            } else {
                serverType = "test";
            }


            attributesBuilder.setEnvironment(serverType);

            HippoConfig.progressLoader = false;
            HippoConfigAttributes attributes = attributesBuilder.build();
            HippoConfig.initHippoConfig(activity, attributes);

        }


        if (isLoginScreenAtFirst) {
            //to clear cart when app is killed
            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                if (Dependencies.getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                    Dependencies.clearSelectedProductArraylist();
                }
            }
            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
                if (Dependencies.getSelectedProductsArrayList().get(0).getVendorId() != 0 &&
                        Dependencies.getSelectedProductsArrayList().get(0).getVendorId() != userData.getData().getVendorDetails().getVendorId()) {
                    Dependencies.setSelectedProductsArrayList(new ArrayList<Datum>());
                }
            }

            if (Dependencies.getSelectedProductsArrayList() != null && Dependencies.getSelectedProductsArrayList().size() > 0
                    && !Dependencies.getSelectedProductsArrayList().get(0).getFormId().equals(StorefrontCommonData.getFormSettings().getFormId())) {
                Dependencies.setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
            }
        } else {


//            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
//                StorefrontCommonData.getFormSettings().setUserId(Dependencies.getSelectedProductsArrayList().get(0).getSellerId());
//            }
//            if (Dependencies.getSelectedProductsArrayList().size() > 0) {
//                if (Dependencies.getSelectedProductsArrayList().get(0).getVendorId() != userData.getData().getVendorDetails().getVendorId()) {
//                    Dependencies.getSelectedProductsArrayList().get(0).setVendorId(userData.getData().getVendorDetails().getVendorId());
//                    Dependencies.getSelectedProductsArrayList().get(0).setUserId(userData.getData().getVendorDetails().getUserId());
//                    Dependencies.getSelectedProductsArrayList().get(0).setFormId(userData.getData().getFormSettings().get(0).getFormId());
//                }
//            }

        }


    }

    //    hippoTransactionId   jobId getIsCustomOrder getMerchantName getJobId getGroupingTags
    public static void startChat(String hippoTransactionId, String merchantName, int jobId, int isCustomOrder, ArrayList<String> groupingTags) {
        if (UIManager.isFuguChatEnabled() && HippoConfig.getInstance() != null) {
            ChatByUniqueIdAttributes attributes;
            if (isCustomOrder != 2) {
//                    Utils.saveUserInfo(mActivity, StorefrontCommonData.getUserData(), true, 1);
//                    FuguConfig.getInstance().openChatByTransactionId("<transaction_id>", "<user_unique_key>", "<Chat Title>", tags, groupingTags)
//                        FuguConfig.getInstance().openChatByTransactionId(currentTask.getHippoTransactionId(),
//                                StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "",
//                                currentTask.getMerchantName() + " #" + currentTask.getJobId(),
//                                null,
//                                currentTask.getGroupingTags());
                attributes = new ChatByUniqueIdAttributes.Builder()
                        .setTransactionId(hippoTransactionId)
                        .setUserUniqueKey(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString())
                        .setChannelName(merchantName + " #" + jobId)
                        .setGroupingTags(groupingTags)
                        .build();

            } else {

                attributes = new ChatByUniqueIdAttributes.Builder()
                        .setTransactionId(jobId + "")
                        .setUserUniqueKey(StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId().toString())
                        .setChannelName("#" + jobId)
                        .setGroupingTags(groupingTags)
                        .build();
//                        FuguConfig.getInstance().openChatByTransactionId(currentTask.getJobId().toString(),
//                                StorefrontCommonData.getUserData().getData().getVendorDetails().getVendorId() + "",
//                                "#Testing",
//                                null,
//                                new ArrayList<String>());
            }
            HippoConfig.getInstance().openChatByUniqueId(attributes);
        }
    }

    public static HashMap<Long, Integer> getPaymentMethodsMap() {
        HashMap<Long, Integer> hashMap = new HashMap<>();

        for (PaymentMethod paymentMethod : StorefrontCommonData.getFormSettings().getPaymentMethods()) {

            if (paymentMethod.getEnabled() == 1)
                hashMap.put(paymentMethod.getValue(), paymentMethod.getEnabled());
        }

        return hashMap;
    }

    public static String getCurrencySymbol() {
        return getCurrencySymbol(null);
    }

    public static String getCurrencySymbolNew(String currenySymbol) {

        if (StorefrontCommonData.getFormSettings().getPaymentSettings() != null && StorefrontCommonData.getFormSettings().getPaymentSettings().size() > 0) {
            if (currenySymbol != null && !currenySymbol.isEmpty() && StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                return currenySymbol;
            } else
                return StorefrontCommonData.getFormSettings().getPaymentSettings().get(0).getSymbol();

        } else if (StorefrontCommonData.getAppConfigurationData().getPaymentSettings() != null && StorefrontCommonData.getAppConfigurationData().getPaymentSettings().size() > 0) {
            if (currenySymbol != null && !currenySymbol.isEmpty() && StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                return currenySymbol;
            } else
                return StorefrontCommonData.getAppConfigurationData().getPaymentSettings().get(0).getSymbol();
        }
        return "";
    }

    public static String getCurrencySymbol(PaymentSettings paymentSettings) {

        if (StorefrontCommonData.getFormSettings().getPaymentSettings() != null && StorefrontCommonData.getFormSettings().getPaymentSettings().size() > 0) {
            if (paymentSettings != null && StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                return paymentSettings.getSymbol();
            } else
                return StorefrontCommonData.getFormSettings().getPaymentSettings().get(0).getSymbol();

        } else if (StorefrontCommonData.getAppConfigurationData().getPaymentSettings() != null && StorefrontCommonData.getAppConfigurationData().getPaymentSettings().size() > 0) {
            if (paymentSettings != null && StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {
                return paymentSettings.getSymbol();
            } else
                return StorefrontCommonData.getAppConfigurationData().getPaymentSettings().get(0).getSymbol();
        }
        return "";
    }

    public static String getCurrencyCode() {
        if (StorefrontCommonData.getFormSettings().getPaymentSettings() != null && StorefrontCommonData.getFormSettings().getPaymentSettings().size() > 0) {
            return StorefrontCommonData.getFormSettings().getPaymentSettings().get(0).getCode();
        } else if (StorefrontCommonData.getAppConfigurationData().getPaymentSettings() != null && StorefrontCommonData.getAppConfigurationData().getPaymentSettings().size() > 0) {
            return StorefrontCommonData.getAppConfigurationData().getPaymentSettings().get(0).getCode();
        }
        return "";
    }

    public static String getCurrencyCodeNew() {
        if (StorefrontCommonData.getFormSettings().getPaymentSettings() != null && StorefrontCommonData.getFormSettings().getPaymentSettings().size() > 0) {
            return StorefrontCommonData.getFormSettings().getPaymentSettings().get(0).getCode();
        } else if (StorefrontCommonData.getAppConfigurationData().getPaymentSettings() != null && StorefrontCommonData.getAppConfigurationData().getPaymentSettings().size() > 0) {
            return StorefrontCommonData.getAppConfigurationData().getPaymentSettings().get(0).getCode();
        }
        return "";
    }

    public static String getCurrencyId() {
        if (StorefrontCommonData.getFormSettings().getPaymentSettings() != null && StorefrontCommonData.getFormSettings().getPaymentSettings().size() > 0) {

            if (Dependencies.getSelectedProductsArrayList().size() > 0
                    && StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1) {

                if (Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings() != null)
                    return Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getCurrencyId() + "";
                else
                    return StorefrontCommonData.getFormSettings().getPaymentSettings().get(0).getCurrencyId() + "";
            } else
                return StorefrontCommonData.getFormSettings().getPaymentSettings().get(0).getCurrencyId() + "";

        } else if (StorefrontCommonData.getAppConfigurationData().getPaymentSettings() != null && StorefrontCommonData.getAppConfigurationData().getPaymentSettings().size() > 0) {

            if (Dependencies.getSelectedProductsArrayList().size() > 0
                    && StorefrontCommonData.getAppConfigurationData().getIsMultiCurrencyEnabled() == 1)

                return Dependencies.getSelectedProductsArrayList().get(0).getPaymentSettings().getCurrencyId() + "";
            else
                return StorefrontCommonData.getAppConfigurationData().getPaymentSettings().get(0).getCurrencyId() + "";
        }
        return "";
    }

    public static String showIntegerValue(String text) {
        try {
            DecimalFormat formater = new DecimalFormat("#");
// text is a number variable and not a String in this case
            return formater.format(Double.valueOf(text));
        } catch (Exception e) {
            return text;
        }
    }

    public static String getCallTaskAs(Boolean showSingular, Boolean isCapWord) {
        String getCallTasksAs;
        if (showSingular) {
            getCallTasksAs = StorefrontCommonData.getTerminology().getOrder();
        } else {
            getCallTasksAs = StorefrontCommonData.getTerminology().getOrders();
        }

        try {
            getCallTasksAs = isCapWord ? capitaliseWords(getCallTasksAs) : convertToLowerCase(getCallTasksAs);
        } catch (Exception e) {
        }

        return getCallTasksAs;
    }

    public static String getCallTaskAs(UserData userData, Boolean showSingular, Boolean isCapWord) {
        return getCallTaskAs(showSingular, isCapWord);
    }

    public static String callAgentsAs(Boolean isCapWord) {
        String getCallAgentAs = StorefrontCommonData.getTerminology().getMerchant();

        try {
            getCallAgentAs = isCapWord ? capitaliseWords(getCallAgentAs) : convertToLowerCase(getCallAgentAs);
        } catch (Exception e) {
        }

        return getCallAgentAs;
    }

    public static Drawable getRatingBackgroundDrawable(Context context, int colorResourceId) {
        GradientDrawable drawable = new GradientDrawable();
//        drawable.setShape(GradientDrawable.OVAL);
        drawable.setCornerRadius(Utils.convertDpToPixels(context, 12));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColor(context.getResources().getColor(colorResourceId, null));
        } else {
            drawable.setColor(ContextCompat.getColor(context, colorResourceId));
        }
        return drawable;
    }

    public static Drawable getTaskStatusPin(Context context, int colorResourceId) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(Utils.convertDpToPixels(context, 3));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColor(context.getResources().getColor(colorResourceId, null));
        } else {
            drawable.setColor(ContextCompat.getColor(context, colorResourceId));
        }
        return drawable;
    }

    public static void onSessionExpired(Activity fromActivity, Class<?> toClass) {
        Intent intent = new Intent(fromActivity, toClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromActivity.startActivity(intent);
        ActivityCompat.finishAffinity(fromActivity);
    }

    public static void showToast(Context context, String string) {
        showToast(context, string, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String string, int duration) {
        try {
            if (MyApplication.getInstance().getToast() != null) {
                MyApplication.getInstance().getToast().cancel();
            }


            MyApplication.getInstance().setToast(
                    Toast.makeText(context, string, duration));
            MyApplication.getInstance().getToast().show();
        } catch (Exception e) {

            Utils.printStackTrace(e);
        }
    }

    /**
     * Method to redirect the User to
     * google maps app
     */
    public static void openGoogleMapsApp(final Context context, final LatLng destination) {

        try {
            // Check whether google maps app is installed on the fly
            if (isGoogleMapsInstalled(context)) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="
                        + destination.latitude + "," + destination.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
                Activity activity = (Activity) context;
                AnimationUtils.forwardTransition(activity);
            } else {
                // Show the Toast
                Utils.redirectUserToUrl((Activity) context, "http://maps.google.com/maps?saddr=" + destination.latitude + "," + destination.longitude + "&daddr=" +
                        destination.latitude + "," + destination.longitude);
            }
        } catch (Exception e) {

            Utils.printStackTrace(e);
            Utils.redirectUserToUrl((Activity) context, "http://maps.google.com/maps?saddr=" + destination.latitude + "," + destination.longitude + "&daddr=" + destination
                    .latitude + "," + destination.longitude);

        }

    }

    /**
     * Method to check whether google maps is installed
     *
     * @param context
     * @return
     */
    public static boolean isGoogleMapsInstalled(Context context) {

        return isPackageInstalled(context, "com.google.android.apps.maps");

    }

    /**
     * Method to determine whether a Package Exists or not
     *
     * @param packageName
     * @param context
     * @return
     */
    private static boolean isPackageInstalled(Context context, String packageName) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getCountryCode(Context ctx, String phoneNumber) {
        String countryCode = "";

        String[] rl = ctx.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (phoneNumber.replace("+", "").trim().startsWith(g[0].trim())) {
                countryCode = g[0];
                break;
            }
        }
        Log.e("Country Code", "+" + countryCode);
        if (countryCode.equalsIgnoreCase(""))
            return getDefaultCountryCode(ctx);
        else
            return "+" + countryCode;
    }

    public static String getDefaultCountryCode(Context context) {
//        String countryCode = "+1";
//
//        Country country = Country.getCountryFromSIM(context);
//
//        if (country != null) {
//            countryCode = country.getDialCode() != null ? country.getDialCode() : "+1";
//        }
//
//        return countryCode;

        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return "+" + (CountryZipCode.isEmpty() ? "1" : CountryZipCode);

    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static DecimalFormat getDecimalFormatDisplay() {
        if (decimalFormatDisplay == null) {
            String decimalPattern = "0.";

            if (UIManager.getDecimalDisplayPrecisionPoint() > 0) {
                for (int i = 0; i < UIManager.getDecimalDisplayPrecisionPoint(); i++) {
                    decimalPattern = decimalPattern + "0";
                }
            } else {
                decimalPattern = "0";
            }

            decimalFormatDisplay = new DecimalFormat(decimalPattern);
//            decimalFormatMoney = new DecimalFormat("0.00");
        }
        dfs.setDecimalSeparator('.');
        decimalFormatDisplay.setDecimalFormatSymbols(dfs);
        return decimalFormatDisplay;
    }

    public static DecimalFormat getDecimalFormatForPercentage() {
        if (decimalFormatForPercentage == null) {
            decimalFormatForPercentage = new DecimalFormat("0");
        }
        dfs.setDecimalSeparator('.');
        decimalFormatForPercentage.setDecimalFormatSymbols(dfs);
        return decimalFormatForPercentage;
    }

    public static DecimalFormat getDecimalFormatCalculation() {
        if (decimalFormatCalculation == null) {
            String decimalPattern = "0.";

            if (UIManager.getDecimalCalculationPrecisionPoint() > 0) {
                for (int i = 0; i < UIManager.getDecimalCalculationPrecisionPoint(); i++) {
                    decimalPattern = decimalPattern + "0";
                }
            } else {
                decimalPattern = "0";
            }

            decimalFormatCalculation = new DecimalFormat(decimalPattern);
//            decimalFormatMoney = new DecimalFormat("0.00");
        }
        dfs.setDecimalSeparator('.');
        decimalFormatCalculation.setDecimalFormatSymbols(dfs);
        return decimalFormatCalculation;
    }

    public static String getDoubleTwoDigits(BigDecimal amount) {
        double amountDouble = Double.valueOf(arabicToDecimal(amount + ""));

        double multiplier = Math.pow(10, UIManager.getDecimalCalculationPrecisionPoint());
        amountDouble = Math.round(amountDouble * multiplier) / multiplier;

        return getDecimalFormatDisplay().format(Double.valueOf(amountDouble));

    }

    public static String getDoubleTwoDigits(double amount) {

        amount = Double.valueOf(arabicToDecimal(amount + ""));
        double multiplier = Math.pow(10, UIManager.getDecimalCalculationPrecisionPoint());
        amount = Math.round(amount * multiplier) / multiplier;
        return (getDecimalFormatDisplay().format(Double.valueOf(amount)));

    }

    //    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static void printStackTrace(Exception e) {
        if (BuildConfig.DEBUG)
            e.printStackTrace();
    }

    public static void printStackTrace(Throwable e) {
        if (BuildConfig.DEBUG)
            e.printStackTrace();
    }

    public static void addStarsToLayout(Activity mActivity, LinearLayout llCollapRatingStars, Double rating) {
        addStarsToLayout(mActivity, llCollapRatingStars, rating, R.dimen._20dp);
    }

    public static void addStarsToLayout(Activity mActivity, LinearLayout llCollapRatingStars, Double rating, int starDimensions) {
        int halfStarRes = R.drawable.ic_half_star_green_grey;
        int halfStarResMedium = R.drawable.ic_half_star_yellow_grey;
        int halfStarResLow = R.drawable.ic_half_star_red_grey;
        int blankStarRes = R.drawable.ic_empty_star_grey;

        llCollapRatingStars.removeAllViews();
        double ratingInt = rating.intValue();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(mActivity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mActivity.getResources().getDimensionPixelSize(starDimensions), mActivity.getResources().getDimensionPixelSize(starDimensions));
            if (i == 0) {
                params.setMargins(mActivity.getResources().getDimensionPixelSize(R.dimen._0dp), 0, 0, 0);
            } else {
                params.setMargins(mActivity.getResources().getDimensionPixelSize(R.dimen._1dp), 0, 0, 0);
            }
            if (i < ratingInt) {
                star.setImageResource(blankStarRes);
                star.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mActivity, Constants.RatingColorValues.getColorResourceValue(mActivity, rating)), PorterDuff.Mode.SRC_IN));
            } else if (i == ratingInt) {
                double decimal = Math.round((rating - Math.floor(rating)) * 10.0) / 10.0;
                if (decimal < 0.3) {
                    star.setImageResource(blankStarRes);
                } else if (decimal < 0.8) {
                    if (rating < 1) {
                        star.setImageResource(halfStarResLow);
                    } else if (rating < 3.3) {
                        star.setImageResource(halfStarResMedium);
                    } else {
                        star.setImageResource(halfStarRes);
                    }

                } else {
                    star.setImageResource(blankStarRes);
                    star.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mActivity, Constants.RatingColorValues.getColorResourceValue(mActivity, rating)), PorterDuff.Mode.SRC_IN));
                }
            } else {
                star.setImageResource(blankStarRes);
            }
            llCollapRatingStars.addView(star, params);
        }
    }

    public static void addSingleStarToLayout(Activity mActivity, LinearLayout llCollapRatingStars, Double rating, int starDimensions) {
        int blankStarRes = R.drawable.ic_empty_star_grey;
        llCollapRatingStars.removeAllViews();
        double ratingInt = rating.intValue();
        ImageView star = new ImageView(mActivity);
        star.setImageResource(blankStarRes);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mActivity.getResources().getDimensionPixelSize(starDimensions), mActivity.getResources().getDimensionPixelSize(starDimensions));
        params.setMargins(mActivity.getResources().getDimensionPixelSize(R.dimen._0dp), 0, 0, 0);
        star.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mActivity, Constants.RatingColorValues.getColorResourceValue(mActivity, rating)), PorterDuff.Mode.SRC_IN));
        llCollapRatingStars.addView(star, params);
    }


    public static Drawable getVectorDrawable(Activity mActivity, int drawableId) {
        return VectorDrawableCompat.create(mActivity.getResources(), drawableId, mActivity.getTheme());
    }

    public static Drawable getVectorDrawableAppCompat(Activity mActivity, int drawableId) {
        return AppCompatResources.getDrawable(mActivity, drawableId);
//        ((TextView) mActivity.findViewById(R.id.tvHome)).setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);
    }

    public static Map<String, String> toMap(JSONObject jsonobj) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();
        Iterator<String> keys = jsonobj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value.toString());
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    @SuppressWarnings("deprecation")
    public static void setLocale(Locale locale) {
//        SharedPrefUtils.saveLocale(locale); // optional - Helper method to save the selected language to SharedPreferences in case you might need to attach to activity context (you will need to code this)
        Resources resources = getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }

    public static void setLanguage(Context context) {

        String languageCode = "en";
        if (StorefrontCommonData.getSelectedLanguageCode() != null) {
            languageCode = StorefrontCommonData.getSelectedLanguageCode().getLanguageCode();
        }
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        Resources resources = context.getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * @param imageUrl url
     * @return
     */
    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {

            Utils.printStackTrace(e);
            return null;
        }
    }

    public static String[] splitNumberByCodeNew(final Context context, final String phoneId) throws NumberParseException {
        String[] strings = new String[2];
        strings = phoneId.split(" ");
        return strings;
    }

//    public static String[] splitNumberByCode(final Context context, final String phoneId) throws NumberParseException {
//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//        String phoneNumber = phoneId;
//        if (!phoneNumber.startsWith("+")) {
//            phoneNumber = "+" + phoneNumber;
//        }
//        String[] strings = new String[2];
//
//        Log.e(TAG, "Number :: " + phoneId);
//        Phonenumber.PhoneNumber mPhoneNumber = phoneUtil.parse(phoneNumber.trim(), "");
//        strings[0] = String.valueOf(mPhoneNumber.getCountryCode());
//
//        if (strings[0].isEmpty()) {
//            Country country = Country.getCountryFromSIM(context);
//            strings[0] = country != null ? country.getDialCode() : "+1";
//        } else if (!strings[0].startsWith("+")) {
//            strings[0] = "+" + strings[0];
//        }
//
//        strings[1] = String.valueOf(mPhoneNumber.getNationalNumber());
//
//        return strings;
//    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d(TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public static void makeViewNonEditable(View view) {
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
        view.setClickable(false);
        view.setLongClickable(false);
    }

    public static String getServiceTime(int minutes, Context context) {
        String serviceTime = "";
        int days, hours, min;

        days = minutes / (24 * 60);
        hours = (minutes % (24 * 60)) / 60;
        min = (minutes % (24 * 60)) % 60;

        if (days != 0) {
            serviceTime += days + StorefrontCommonData.getString(context, R.string.dayss);
        }
        if (hours != 0) {
            serviceTime += (days != 0 ? " " : "") + hours + StorefrontCommonData.getString(context, R.string.hour);
        }
        if (min != 0) {
            serviceTime += ((days != 0 || hours != 0) ? " " : "") + min + StorefrontCommonData.getString(context, R.string.minute);
        }

        return serviceTime;
    }

    public static String getServiceTime(int minutes, Context context, int unitType) {
        if (Dependencies.isLaundryApp()) {
            return getServiceTimeForLaundary(minutes, context);
        } else {
            return getServiceTimeForHyperlocal(minutes, context, unitType);
        }

    }

    public static String getServiceTimeForLaundary(int minutes, Context context) {
        String serviceTime = "";
        int days, hours, min;
        days = minutes / (24 * 60);
        hours = (minutes % (24 * 60)) / 60;
        min = (minutes % (24 * 60)) % 60;

        if (days != 0) {
            serviceTime += days +
                    (days > 1 ? StorefrontCommonData.getString(context, R.string.dayss) : StorefrontCommonData.getString(context, R.string.dayss));
        }
        if (hours != 0) {
            serviceTime += (days != 0 ? " " : "") + hours
                    + (hours > 1 ? StorefrontCommonData.getString(context, R.string.hours) : StorefrontCommonData.getString(context, R.string.hour));
        }
        if (min != 0) {
            serviceTime += ((days != 0 || hours != 0) ? " " : "") + min
                    + (min > 1 ? StorefrontCommonData.getString(context, R.string.minutes) : StorefrontCommonData.getString(context, R.string.minute));
        }

        return serviceTime;
    }

    public static String getServiceTimeForHyperlocal(int minutes, Context context, int unitType) {
        String serviceTime = "";
        switch (unitType) {
            case Constants.ServiceTimeConstants.FIXED:
            case Constants.ServiceTimeConstants.MINUTES:
                serviceTime += minutes +
                        (minutes > 1 ? StorefrontCommonData.getString(context, R.string.minutes) : StorefrontCommonData.getString(context, R.string.minute));
                break;
            case Constants.ServiceTimeConstants.HOURS:
                int hours = minutes / 60;
                serviceTime += hours +
                        (hours > 1 ? StorefrontCommonData.getString(context, R.string.hours) : StorefrontCommonData.getString(context, R.string.hour));
                break;
            case Constants.ServiceTimeConstants.DAYS:
                int days = minutes / 1440;
                serviceTime += days +
                        (days > 1 ? StorefrontCommonData.getString(context, R.string.dayss) : StorefrontCommonData.getString(context, R.string.dayss));
                break;
            case Constants.ServiceTimeConstants.WEEKS:
                int weeks = minutes / 10080;
                serviceTime += weeks +
                        (weeks > 1 ? StorefrontCommonData.getString(context, R.string.weekss) : StorefrontCommonData.getString(context, R.string.week));
                break;
            case Constants.ServiceTimeConstants.MONTHS:
                int months = minutes / 43200;
                serviceTime += months +
                        (months > 1 ? StorefrontCommonData.getString(context, R.string.months) : StorefrontCommonData.getString(context, R.string.month));
                break;
            case Constants.ServiceTimeConstants.YEARS:
                int years = minutes / 525600;
                serviceTime += years +
                        (years > 1 ? StorefrontCommonData.getString(context, R.string.yearss) : StorefrontCommonData.getString(context, R.string.yearss));
                break;
          /*  case Constants.ServiceTimeConstants.KILOGRAM:
                int kilogram = minutes / 525600;
                serviceTime += kilogram +
                        (kilogram > 1 ? StorefrontCommonData.getString(context, R.string.kilograms) : StorefrontCommonData.getString(context, R.string.kilogram));
                break;
            case Constants.ServiceTimeConstants.POUND:
                int pound = minutes / 525600;
                serviceTime += pound +
                        (pound > 1 ? StorefrontCommonData.getString(context, R.string.pounds) : StorefrontCommonData.getString(context, R.string.pound));
                break;
            case Constants.ServiceTimeConstants.PERSON:
                int person = minutes / 525600;
                serviceTime += person +
                        (person > 1 ? StorefrontCommonData.getString(context, R.string.persons) : StorefrontCommonData.getString(context, R.string.person));
                break;
            case Constants.ServiceTimeConstants.KILOMETER:
                int kilometer = minutes / 525600;
                serviceTime += kilometer +
                        (kilometer > 1 ? StorefrontCommonData.getString(context, R.string.kilometers) : StorefrontCommonData.getString(context, R.string.kilometer));
                break;*/
            case Constants.ServiceTimeConstants.FEET:
                int feet = minutes;
                serviceTime += feet +
                        (feet > 1 ? StorefrontCommonData.getString(context, R.string.text_feett) : StorefrontCommonData.getString(context, R.string.text_feett));
                break;
            case Constants.ServiceTimeConstants.SQUARE_FEET:
                int squarefeet = minutes;
                serviceTime += squarefeet +
                        (squarefeet > 1 ? StorefrontCommonData.getString(context, R.string.text_square_eet) : StorefrontCommonData.getString(context, R.string.text_square_eet));
                break;
            case Constants.ServiceTimeConstants.LINEAR_FEET:
                int linearfeet = minutes;
                serviceTime += linearfeet +
                        (linearfeet > 1 ? StorefrontCommonData.getString(context, R.string.text_linear_feett) : StorefrontCommonData.getString(context, R.string.text_linear_feett));
                break;
            case Constants.ServiceTimeConstants.PER_SQM:
                int persquaremeter = minutes;
                serviceTime += persquaremeter +
                        (persquaremeter > 1 ? StorefrontCommonData.getString(context, R.string.per_square_meter) : StorefrontCommonData.getString(context, R.string.per_square_meter));
                break;

            case Constants.ServiceTimeConstants.METRE:
                int metre = minutes;
                serviceTime += metre +
                        (metre > 1 ? StorefrontCommonData.getString(context, R.string.meter) : StorefrontCommonData.getString(context, R.string.meter));
                break;
        }

        return serviceTime;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
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

    public static String getFormattedDoubleValue(String percentageCashback) {
        double temp = Double.valueOf(percentageCashback);
        if (temp % 1 > 0)
            return Utils.getDoubleTwoDigits(temp);
        else {
            return String.valueOf((int) temp);
        }
    }

    public static String getFormattedPriceGerman(String amount) {
        //Scanner sc=new Scanner(System.in);
        StringBuilder stbr = new StringBuilder();
        stbr.append(getDecimalFormattedNumber(amount));
        char comma = ',', dot = '.';
        int n = stbr.length();
        int start = 0, end = n - 1;
        while (start <= end) {
            int fs = 0, fe = 0;
            if (stbr.charAt(start) == dot || stbr.charAt(start) == comma) {
                fs = 1;
            }
            if (stbr.charAt(end) == dot || stbr.charAt(end) == comma) {
                fe = 1;
            }
            if ((fs == 1 && fe == 1)) {
                if (stbr.charAt(start) == stbr.charAt(end)) {
                    if (stbr.charAt(start) == comma) {
                        stbr.setCharAt(start, dot);
                    } else {
                        stbr.setCharAt(start, comma);
                        stbr.setCharAt(end, comma);
                    }
                } else {
                    char temp = stbr.charAt(start);
                    stbr.setCharAt(start, stbr.charAt(end));
                    stbr.setCharAt(end, temp);
                }
                start++;
                end--;


            }
            if (fs == 0)
                start++;
            if (fe == 0)
                end--;
        }
        return stbr.toString();
    }

    public static String getDecimalFormattedNumber(String number) {
        StringBuilder stbr = new StringBuilder();
        int start = 0;
        stbr.append(number);
        int l;
        if (stbr.toString().contains("."))
            l = stbr.lastIndexOf(".");
        else {
            l = stbr.length();
//            stbr.append(".00");
        }
        for (int i = 0; i < l; i++) {
            char x = stbr.toString().charAt(i);

            if (Character.isDigit(x)) {
                start = i;
                break;
            }
        }
        for (int i = l; i > start; i = i - 3) {

            if (i != l)
                stbr.insert(i, ",");
        }

        return stbr.toString();
    }

    public static boolean hasData(String travelMode) {
        return travelMode != null && !travelMode.trim().isEmpty();
    }

    public static boolean hasData(List travelMode) {
        return travelMode != null && !travelMode.isEmpty();
    }

    public static void showPopup(Activity mContext, int code, adDialogInterface listener) {
        if (StorefrontCommonData.getAppConfigurationData() !=null && (!StorefrontCommonData.getAppConfigurationData().isPopupEnabled())) {
            return;
        }
        String image_url = StorefrontCommonData.getAppConfigurationData().getPopup_image_url();
        String redirectUrl = StorefrontCommonData.getAppConfigurationData().getPopup_redirect_link();
        if (image_url == null || image_url.equals("")) {
            return;
        }
        adDialog = new Dialog(mContext);
        adDialog.setContentView(R.layout.dialog_popup);
        adDialog.setCancelable(true);
        adDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        AppCompatImageView ivPopup = adDialog.findViewById(R.id.ivPopup);
        AppCompatImageView ivClose = adDialog.findViewById(R.id.ivClose);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivPopup.setClipToOutline(true);
        }
        Glide.with(mContext).load(image_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .into(ivPopup);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adDialog.dismiss();
            }
        });

        ivPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!redirectUrl.isEmpty()) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Keys.Extras.URL_WEBVIEW, redirectUrl);
                    intent.putExtra(Keys.Extras.HEADER_WEBVIEW, "");
                    mContext.startActivityForResult(intent, code);
                }
            }
        });

        adDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                listener.onAdDialogDismiss();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adDialog.show();
            }
        }, 2000);
    }

    public static void dismissAdDialog() {
        if (adDialog != null) {
            adDialog.dismiss();
        }
    }

    public void animateMarker(GoogleMap mMap, final Marker marker, final LatLng toPosition) {
        if (marker == null)
            return;
        if (toPosition == null)
            return;

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                try {
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                } catch (Exception e) {

                    Utils.printStackTrace(e);
                }
            }
        });
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_icon_pin_location);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
// Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
// vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
// vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static interface adDialogInterface {
        void onAdDialogDismiss();
    }

}