package com.tookancustomer.appdata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tookancustomer.BuildConfig;
import com.tookancustomer.MyApplication;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.userdata.SignupTemplateData;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.models.userdata.UserOptions;
import com.tookancustomer.retrofit2.CommonParams;
import com.tookancustomer.retrofit2.MultipartParams;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.UIManager;
import com.tookancustomer.utility.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import io.paperdb.Paper;

import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_ACCESS_TOKEN;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_TYPE;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.APP_VERSION;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.DUAL_USER_KEY;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.FORM_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.IS_DEMO_APP;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.MARKETPLACE_USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.REFERENCE_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.USER_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.VENDOR_ID;
import static com.tookancustomer.appdata.Keys.APIFieldKeys.YELO_APP_TYPE;

/**
 * Fetches data for which the App is dependent to communicate with Server
 *
 * @author Rishabh
 */
public class Dependencies implements Keys.Prefs {
    /*      Specifies the Device Type being used     */
    // device type 0 for pickup delivery
    // device type 4 for appointments

    /*      Specifies the Version Details at Play Store     */
    public static final int STORE_VERSION = 218;
    public static final String STORE_VERSION_NAME = "2.1.8";
    private static final int referenceId = 1;
    public static boolean isAppFirstInstall = true;
    private static String marketplaceReferenceId;
    private static Bundle fuguBundle;
    private static boolean isMarketplaceFirstInstall = true;
    private static boolean isDemoRunning = false;
    private static ArrayList<Datum> selectedProductsArrayList;
    private static boolean isPreorderSelecetedForMenu;
    private static int selectedDeliveryMode;

    public static boolean isIsMarketplaceFirstInstall() {
        return Paper.book().read(IS_MP_FIRST_INSTALL, true);
    }

    public static void setIsMarketplaceFirstInstall(boolean isMarketplaceFirstInstall) {
        Dependencies.isMarketplaceFirstInstall = isMarketplaceFirstInstall;
        Paper.book().write(IS_MP_FIRST_INSTALL, isMarketplaceFirstInstall);
    }

    public static String getMarketplaceReferenceId() {
        if (marketplaceReferenceId == null)
            marketplaceReferenceId = Paper.book().read(MARKETPLACE_REF_ID, BuildConfig.MARKETPLACE_REF_ID);
        return marketplaceReferenceId;
    }

    public static void setMarketplaceReferenceId(String marketplaceReferenceId) {
        Dependencies.marketplaceReferenceId = marketplaceReferenceId;
        Paper.book().write(MARKETPLACE_REF_ID, marketplaceReferenceId);
    }

    public static boolean isDemoApp() {
        return Dependencies.getMarketplaceReferenceId().equals("dbsekfns7ty4r3uxensno3ye8qp2upuost") ||
                Dependencies.getMarketplaceReferenceId().equals("654ry54efgy654efy65eeeeecddfffnsno3ye8qp2upuost") ||
                Dependencies.getMarketplaceReferenceId().equals("rrrrdfsjhxgsfusfhindhuicddfffnsno3ye8qp2upuost");
    }

    public static boolean isDemoAppGradel() {
        return BuildConfig.MARKETPLACE_REF_ID.equals("dbsekfns7ty4r3uxensno3ye8qp2upuost") ||
                BuildConfig.MARKETPLACE_REF_ID.equals("654ry54efgy654efy65eeeeecddfffnsno3ye8qp2upuost") ||
                BuildConfig.MARKETPLACE_REF_ID.equals("rrrrdfsjhxgsfusfhindhuicddfffnsno3ye8qp2upuost");
    }


    public static boolean isDemoGARequired() {
        return !BuildConfig.DEBUG;
    }

    public static boolean isTryDemoEnabled() {
        return false;
    }

    public static boolean isDemoRunning() {
        return isDemoRunning;
    }

    public static void setDemoRun(boolean demoRun) {
        Log.e("demorun : ", demoRun + "");
        isDemoRunning = demoRun;
    }

    public static int getReferenceId() {
        return referenceId;
    }

    public static int getVendorIdForGuest(Context context) {
        return Prefs.with(context).getInt(VENDOR_ID_GUEST, 0);
    }

    /**
     * Method to save Access Token
     *
     * @param vendorIdGuest
     */
    public static void saveVendorIdForGuest(Context context, int vendorIdGuest) {
        Prefs.with(context).save(VENDOR_ID_GUEST, vendorIdGuest);
    }

    /**
     * Extracts access_token of user from shared preferences
     *
     * @param context
     * @return access_token if exists else
     */
    public static String getAccessTokenGuest(Context context) {
        return Prefs.with(context).getString(ACCESS_TOKEN_GUEST, "");
    }

    /**
     * Method to save Access Token
     *
     * @param guestCheckoutFlowOngoing
     */
    public static void setGuestCheckoutFlowOngoing(Context context, int guestCheckoutFlowOngoing) {
        Prefs.with(context).save(GUEST_CHECKOUT_FLOW_ONGOING, guestCheckoutFlowOngoing);
    }

    /**
     * Extracts access_token of user from shared preferences
     *
     * @param context
     * @return access_token if exists else
     */
    public static int getGuestCheckoutFlowOngoing(Context context) {
        return Prefs.with(context).getInt(GUEST_CHECKOUT_FLOW_ONGOING, 0);
    }

    /**
     * Method to save Access Token
     *
     * @param accessTokenGuest
     */
    public static void saveAccessTokenGuest(Context context, String accessTokenGuest) {
        Prefs.with(context).save(ACCESS_TOKEN_GUEST, accessTokenGuest);
    }


    /**
     * Method to save Access Token
     *
     * @param accessToken
     */
    public static void saveAccessToken(Context context, String accessToken) {
        Prefs.with(context).save(ACCESS_TOKEN, accessToken);
    }

    /**
     * Extracts access_token of user from shared preferences
     *
     * @param context
     * @return access_token if exists else
     */
    public static String getAccessToken(Context context) {
        return Prefs.with(context).getString(ACCESS_TOKEN, "");
    }

    /**
     * Method to save userOptions
     *
     * @param userOptions
     */
    public static void saveUserOptions(Context context, UserOptions userOptions) {
        Prefs.with(context).save(USER_OPTIONS, userOptions);
    }

    /**
     * Extracts USER_OPTIONS of user from shared preferences
     *
     * @param context
     * @return USER_OPTIONS if exists else
     */
    public static UserOptions getUserOptions(Context context) {
        return Prefs.with(context).getObject(USER_OPTIONS, UserOptions.class);
    }

    /**
     * Method to save deliveryOptions
     *
     * @param deliveryOptions
     */
    public static void saveDeliveryOptions(Context context, UserOptions deliveryOptions) {
        Prefs.with(context).save(DELIVERY_OPTIONS, deliveryOptions);
    }

    /**
     * Extracts DeliveryOptions of user from shared preferences
     *
     * @param context
     * @return DeliveryOptions if exists else
     */
    public static UserOptions getDeliveryOptions(Context context) {
        return Prefs.with(context).getObject(DELIVERY_OPTIONS, UserOptions.class);
    }

    /**
     * Method to save the Device Token
     *
     * @param context
     * @param deviceToken
     */
    public static void saveDeviceToken(Context context, String deviceToken) {
        Prefs.with(context).save(DEVICE_TOKEN, deviceToken);
    }

    /**
     * Extracts or generates a device token of the Device
     *
     * @param context
     * @return
     */
    public static String getDeviceToken(Context context) {
        return Prefs.with(context).getString(DEVICE_TOKEN, "");
    }

    /**
     * Retrieves the name of the Device
     *
     * @return
     */
    public static String getDeviceName() {
        return Utils.capitaliseWords(android.os.Build.MANUFACTURER + "") + " " + android.os.Build.MODEL;
    }

    /**
     * Retrieves the OS Version of device
     *
     * @return
     */
    public static String getDeviceOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Retrieves the IMEI Number of the Device
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Activity activity) {
        if (AppManager.getInstance().isPermissionGranted(activity, Manifest.permission.READ_PHONE_STATE)) {
            return ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        return "Permission not granted";
    }

    /**
     * Retrieves the Version of the App
     *
     * @param context
     * @return
     * @throws NameNotFoundException
     */
    public static long getAppVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {

            Utils.printStackTrace(e);
            return STORE_VERSION;
        }
    }

    public static double getWalletBalance() {
        return StorefrontCommonData.getUserData().getData().getVendorDetails().getWalletBalance();
    }

    public static void setWalletBalance(double amount) {
        StorefrontCommonData.getUserData().getData().getVendorDetails().setWalletBalance(amount);
    }


    /**
     * Retrieves the Version of the App
     *
     * @param context
     * @return
     * @throws NameNotFoundException
     */
    public static String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {

            Utils.printStackTrace(e);
            return STORE_VERSION_NAME;
        }
    }

    /**
     * Retrieves the code of the Device's Country
     *
     * @param context
     * @return
     */
    public static String getCountryCode(Context context) {
        return context.getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry(Locale.getDefault());
    }

    /**
     * Method to get the TimeZone offset in minutes
     *
     * @return
     */
    public static long getTimeZoneInMinutes() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        long offsetInMillis = tz.getOffset(cal.getTimeInMillis());

        return -1 * (offsetInMillis / 60000);
    }


    public static ArrayList<Datum> getSelectedProductsArrayList() {
        if (selectedProductsArrayList == null)
            selectedProductsArrayList = Paper.book().read(SELECTED_PRODUCT_LIST, new ArrayList<Datum>());
        return selectedProductsArrayList;
    }

    public static void setSelectedProductsArrayList(ArrayList<Datum> selectedProductsArrayList) {
        Dependencies.selectedProductsArrayList = selectedProductsArrayList;
        Paper.book().write(SELECTED_PRODUCT_LIST, selectedProductsArrayList);
    }

    public static void clearSelectedProductArraylist() {
        //setSelectedProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
        selectedProductsArrayList.clear();
    }

    public static void addCartItem(Activity activity, Datum selectedProduct) {

        Boolean isProductAlreadyAdded = false;
        int index = 0;

        for (int i = 0; i < getSelectedProductsArrayList().size(); i++) {
            if (getSelectedProductsArrayList().get(i).getProductId().equals(selectedProduct.getProductId())) {
                isProductAlreadyAdded = true;
                index = i;
              /*  selectedProduct.setSelectedQuantity(selectedProduct.getItemSelectedList().get(index).getQuantity()
                        + selectedProduct.getSelectedQuantity());*/
            }
        }

        if (isProductAlreadyAdded) {
            if (selectedProduct.getSelectedQuantity() > selectedProductsArrayList.get(index).getSelectedQuantity()) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_QUANTITY, selectedProduct.getName());
            } else {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.REMOVE_QUANTITY, selectedProduct.getName());
            }

//            TODO
            if (getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                selectedProductsArrayList.clear();
                if (selectedProduct.getSelectedQuantity() != 0) {
                    if (selectedProduct.getItemSelectedList().size() > 0) {
                        for (int i = 0; i < selectedProduct.getItemSelectedList().size(); i++) {
                            if (selectedProduct.getItemSelectedList().get(i).getQuantity() == 0) {
                                selectedProduct.getItemSelectedList().remove(i);
                                i--;
                            }
                        }
                    }
                    selectedProductsArrayList.add(selectedProduct);
                }
            } else {
                selectedProductsArrayList.remove(index);

                if (selectedProduct.getSelectedQuantity() != 0) {
                    if (selectedProduct.getItemSelectedList().size() > 0) {
                        for (int i = 0; i < selectedProduct.getItemSelectedList().size(); i++) {
                            if (selectedProduct.getItemSelectedList().get(i).getQuantity() == 0) {
                                selectedProduct.getItemSelectedList().remove(i);
                                i--;
                            }
                        }
                    }
                    selectedProductsArrayList.add(index, selectedProduct);
                }
            }
            // selectedProduct.setSelectedQuantity();

        } else {
            //TODO
            if (getSelectedProductsArrayList().size() > 0 && getSelectedProductsArrayList().get(0).getStorefrontData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
//            if (getSelectedProductsArrayList().size() > 0 && true) {
                selectedProductsArrayList.clear();
            }
            if (selectedProduct.getSelectedQuantity() > 0) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_QUANTITY, selectedProduct.getName());
            } else {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.REMOVE_QUANTITY, selectedProduct.getName());
            }

            if (selectedProduct.getSelectedQuantity() != 0) {
                selectedProductsArrayList.add(selectedProduct);
            }

        }
        setSelectedProductsArrayList(selectedProductsArrayList);
    }


    public static String getProductListDescription() {
        String listDescription = "";
        for (int i = 0; i < getSelectedProductsArrayList().size(); i++) {
            if (getSelectedProductsArrayList().get(i).getSelectedQuantity() != 0) {
                if (getSelectedProductsArrayList().get(i).getItemSelectedList().size() > 0) {

                    double subTotal = 0;

                    for (int j = 0; j < getSelectedProductsArrayList().get(i).getItemSelectedList().size(); j++) {
                        subTotal = subTotal + getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getTotalPriceWithQuantity();
                    }

                    listDescription = listDescription
                            + getSelectedProductsArrayList().get(i).getSelectedQuantity() + "X\t"
                            + getSelectedProductsArrayList().get(i).getName() + "\t\t"
                            + Utils.getCurrencySymbol(getSelectedProductsArrayList().get(i).getPaymentSettings()) + subTotal + (i == getSelectedProductsArrayList().size() - 1 ? "" : "\n");


                } else {
                    listDescription = listDescription
                            + getSelectedProductsArrayList().get(i).getSelectedQuantity() + "X\t"
                            + getSelectedProductsArrayList().get(i).getName() + "\t\t"
                            + Utils.getCurrencySymbol(getSelectedProductsArrayList().get(i).getPaymentSettings()) + (getSelectedProductsArrayList().get(i).getSelectedQuantity() * getSelectedProductsArrayList().get(i).getPrice().doubleValue()) + (i == getSelectedProductsArrayList().size() - 1 ? "" : "\n");
                }
            }
        }
        return listDescription;
    }

//    public static double getProductListSubtotal() {
//        double subTotal = 0;
//        for (int i = 0; i < getSelectedProductsArrayList().size(); i++) {
//            if (getSelectedProductsArrayList().get(i).getSelectedQuantity() != 0) {
//                if (getSelectedProductsArrayList().get(i).getItemSelectedList().size() > 0) {
//                    for (int j = 0; j < getSelectedProductsArrayList().get(i).getItemSelectedList().size(); j++) {
//                        subTotal = subTotal + getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getTotalPriceWithQuantity();
//                    }
//                } else {
////                    subTotal = subTotal + (getSelectedProductsArrayList().get(i).getSelectedQuantity() * getSelectedProductsArrayList().get(i).getPrice().doubleValue());
//                    subTotal = subTotal + getIndividualItemSubtotal(getSelectedProductsArrayList().get(i));
//                }
//            }
//        }
//        return subTotal;
//    }

    public static double getProductListSubtotal() {
        double subTotal = 0;
        for (int i = 0; i < getSelectedProductsArrayList().size(); i++) {
            if (getSelectedProductsArrayList().get(i).getSelectedQuantity() != 0) {
                if (getSelectedProductsArrayList().get(i).getItemSelectedList().size() > 0) {
                    for (int j = 0; j < getSelectedProductsArrayList().get(i).getItemSelectedList().size(); j++) {
                        if (getSelectedProductsArrayList().get(i).getServiceTime() > 0) {

                            subTotal = subTotal + (((getSelectedProductsArrayList().get(i).getPrice().doubleValue() * getPredefiendInterval(getSelectedProductsArrayList().get(i)))
                                    + getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getCustomizationPrice())
                                    * getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getQuantity());

                        } else if (getInterval(getSelectedProductsArrayList().get(i)) == 0.0) {
                            subTotal = subTotal + (((getSelectedProductsArrayList().get(i).getPrice().doubleValue() * 1)
                                    + getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getCustomizationPrice())
                                    * getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getQuantity());
                        } else {

                            subTotal = subTotal + (((getSelectedProductsArrayList().get(i).getPrice().doubleValue() * getInterval(getSelectedProductsArrayList().get(i)))
                                    + getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getCustomizationPrice())
                                    * getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getQuantity());
                        }
                        if (getSelectedProductsArrayList().get(i).getIsProductTemplateEnabled() == 1 &&
                                getSelectedProductsArrayList().get(i).getQuestionnaireTemplate() != null) {
                            subTotal = subTotal + getSelectedProductsArrayList().get(i).getQuestionnaireTemplateCost();
                        }

                        subTotal = subTotal + (getSelectedProductsArrayList().get(i).getSurgeAmount() * getSelectedProductsArrayList().get(i).getItemSelectedList().get(j).getQuantity());
                    }
                } else {

                    subTotal = subTotal + getIndividualItemSubtotal(getSelectedProductsArrayList().get(i));
                    if (getSelectedProductsArrayList().get(i).getIsProductTemplateEnabled() == 1 &&
                            getSelectedProductsArrayList().get(i).getQuestionnaireTemplate() != null) {
                        subTotal = subTotal + getSelectedProductsArrayList().get(i).getQuestionnaireTemplateCost();

                    }
                    subTotal = subTotal + (getSelectedProductsArrayList().get(i).getSurgeAmount() * getSelectedProductsArrayList().get(i).getSelectedQuantity());
                }

            }
        }
        return subTotal;
    }


    public static double getIndividualItemSubtotal(Datum datum) {
        if (datum.getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                datum.getProductStartDate() != null && datum.getProductEndDate() != null) {
            double interval = getInterval(datum);
            double itemPrice;
            if (datum.getServiceTime() > 0) {
                itemPrice = Double.parseDouble(datum.getPrice().toString().replace("'", ",")) * getPredefiendInterval(datum) * datum.getSelectedQuantity();

            } else if (interval == 0.0) {
                itemPrice = Double.parseDouble(datum.getPrice().toString().replace("'", ",")) * 1 * datum.getSelectedQuantity();
            } else {
                itemPrice = Double.parseDouble(datum.getPrice().toString().replace("'", ",")) * interval * datum.getSelectedQuantity();
            }
            return itemPrice;
        } else {
            return Double.valueOf(Utils.getDoubleTwoDigits(datum.getSelectedQuantity().doubleValue() * datum.getPrice().doubleValue()));
        }
    }

    public static double getInterval(Datum datum) {
        double interval = 1.0;
        switch (datum.getUnitType()) {
            case Constants.ServiceTimeConstants.MINUTES:
                interval = Double.valueOf((datum.getProductEndDate().getTime() - datum.getProductStartDate().getTime()) / (60 * 1000)) /
                        Double.valueOf(datum.getUnit().intValue());
                break;
            case Constants.ServiceTimeConstants.HOURS:
                interval = Double.valueOf((datum.getProductEndDate().getTime() - datum.getProductStartDate().getTime()) / (60 * 1000)) /
                        Double.valueOf(datum.getUnit().intValue() * 60);
                break;
            case Constants.ServiceTimeConstants.DAYS:
                interval = Double.valueOf((datum.getProductEndDate().getTime() - datum.getProductStartDate().getTime()) / (60 * 1000)) /
                        Double.valueOf(datum.getUnit().intValue() * 24 * 60);
                break;
            case Constants.ServiceTimeConstants.WEEKS:
                interval = Double.valueOf((datum.getProductEndDate().getTime() - datum.getProductStartDate().getTime()) / (60 * 1000)) /
                        Double.valueOf(datum.getUnit().intValue() * 7 * 24 * 60);
                break;
            case Constants.ServiceTimeConstants.MONTHS:
                interval = Double.valueOf((datum.getProductEndDate().getTime() - datum.getProductStartDate().getTime()) / (60 * 1000)) /
                        Double.valueOf(datum.getUnit().intValue() * 30 * 24 * 60);
                break;
            case Constants.ServiceTimeConstants.YEARS:
                interval = Double.valueOf((datum.getProductEndDate().getTime() - datum.getProductStartDate().getTime()) / (60 * 1000)) /
                        Double.valueOf(datum.getUnit().intValue() * 365 * 24 * 60);
                break;
        }
        return interval;
    }

    public static double getPredefiendInterval(Datum datum) {
        double interval = 1.0;
        switch (datum.getUnitType()) {
            case Constants.ServiceTimeConstants.MINUTES:
                interval = Double.valueOf(datum.getServiceTime()) /
                        Double.valueOf(datum.getUnit().intValue());
                break;
            case Constants.ServiceTimeConstants.HOURS:
                interval = Double.valueOf(datum.getServiceTime()) /
                        Double.valueOf(datum.getUnit().intValue() * 60);
                break;
            case Constants.ServiceTimeConstants.DAYS:
                interval = Double.valueOf(datum.getServiceTime()) /
                        Double.valueOf(datum.getUnit().intValue() * 24 * 60);
                break;
            case Constants.ServiceTimeConstants.WEEKS:
                interval = Double.valueOf(datum.getServiceTime()) /
                        Double.valueOf(datum.getUnit().intValue() * 7 * 24 * 60);
                break;
            case Constants.ServiceTimeConstants.MONTHS:
                interval = Double.valueOf(datum.getServiceTime()) /
                        Double.valueOf(datum.getUnit().intValue() * 30 * 24 * 60);
                break;
            case Constants.ServiceTimeConstants.YEARS:
                interval = Double.valueOf(datum.getServiceTime()) /
                        Double.valueOf(datum.getUnit().intValue() * 365 * 24 * 60);
                break;
        }
        return interval;
    }

    public static int getCartSize() {
        int totalQuantity = 0;
        if (Dependencies.isLaundryApp()) {
            totalQuantity = Dependencies.getSelectedProductsArrayList().size();
        } else {
            for (int i = 0; i < Dependencies.getSelectedProductsArrayList().size(); i++) {
                totalQuantity = totalQuantity + Dependencies.getSelectedProductsArrayList().get(i).getSelectedQuantity();
            }
        }
        return totalQuantity;

    }

    public static int getCartItemSize() {
        return Dependencies.getSelectedProductsArrayList().size();
    }

    public static Bundle getFuguBundle() {
        return fuguBundle;
    }

    public static void setFuguBundle(Bundle fuguBundle) {
        Dependencies.fuguBundle = fuguBundle;
    }

    public static void saveSignupTemplates(ArrayList<SignupTemplateData> arrayList, Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);

        editor.putString("signup_template_data", json);
        editor.commit();
    }

    public static ArrayList<SignupTemplateData> getSignupTemplate(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("signup_template_data", null);
        Type type = new TypeToken<ArrayList<SignupTemplateData>>() {
        }.getType();
        ArrayList<SignupTemplateData> arrayList = gson.fromJson(json, type);
        return arrayList;
    }


//
//    /**
//     * Save PAPER_SERVER_URL
//     *
//     * @param serverUrl
//     */
//
//    public static void setServerUrl(String serverUrl) {
//
//        SERVER_URL = serverUrl;
//        Paper.book().write(STOREFRONT_SERVER_URL, SERVER_URL);
//
//    }
//
//    /**
//     * Gets PAPER_CONVERSATION_LIST
//     *
//     * @return the serverUrl
//     */
//
//    public static String getServerUrl() {
//
//        if (SERVER_URL.isEmpty()) {
//            SERVER_URL = Paper.book().read(STOREFRONT_SERVER_URL, "");
//        }
//        return SERVER_URL;
//    }


    public static CommonParams.Builder setCommonParamsForAPI(Context mActivity, UserData userData) {
        if (userData != null)
            userData = StorefrontCommonData.getUserData();

        CommonParams.Builder commonParams = new CommonParams.Builder()
                .add(APP_VERSION, Dependencies.getAppVersionCode(mActivity))
                .add(DEVICE_TOKEN, Dependencies.getDeviceToken(mActivity))
                .add(IS_DEMO_APP, Dependencies.isDemoApp() ? 1 : 0)
                .add(YELO_APP_TYPE, 2)
                .add(APP_TYPE, "ANDROID")
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable())
                .add(MARKETPLACE_REF_ID, getMarketplaceReferenceId());

        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                    .add(APP_ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
//                         .add(APP_ACCESS_TOKEN, userData.getData().getVendorDetails().getAppAccessToken());
        }

        if (getGuestCheckoutFlowOngoing(mActivity) == 1) {
            commonParams.add(VENDOR_ID, Dependencies.getVendorIdForGuest(mActivity));
            commonParams.add(ACCESS_TOKEN, Dependencies.getAccessTokenGuest(mActivity))
                    .add(APP_ACCESS_TOKEN, Dependencies.getAccessTokenGuest(mActivity));

        }


       /* if (StorefrontCommonData.getAppConfigurationData() != null &&
                StorefrontCommonData.getAppConfigurationData().getIsGuestCheckoutEnabled() == 1) {
            commonParams.add(VENDOR_ID, getVendorIdForGuest(mActivity));
        }*/

        if (userData != null && userData.getData() != null && userData.getData().getVendorDetails() != null) {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                commonParams.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
            }


            commonParams.add(USER_ID, StorefrontCommonData.getFormSettings().getUserId());

            commonParams.add(FORM_ID, StorefrontCommonData.getFormSettings().getFormId());

            if (userData.getData().getVendorDetails().getRefrenceId() != null && Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                commonParams.add(REFERENCE_ID, userData.getData().getVendorDetails().getRefrenceId());
            }
            commonParams.add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());

        }


        return commonParams;
    }


    public static CommonParams.Builder addCommonParameters(CommonParams.Builder builder, Context mActivity, UserData userData) {

        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            builder.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }

        if (userData != null && userData.getData() != null && userData.getData().getVendorDetails() != null) {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                builder.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
            }
        }

        return builder;
    }

    public static MultipartParams.Builder setMultiParamsForAPI(Activity mActivity, UserData userData, Boolean isUserIdRequired) {

        if (userData != null)
            userData = StorefrontCommonData.getUserData();

        MultipartParams.Builder multiParams = new MultipartParams.Builder()
                .add(APP_VERSION, Dependencies.getAppVersionCode(mActivity))
                .add(DEVICE_TOKEN, Dependencies.getDeviceToken(mActivity))
                .add(IS_DEMO_APP, Dependencies.isDemoApp() ? 1 : 0)
                .add(YELO_APP_TYPE, 2)
                .add(APP_TYPE, "ANDROID")
                .add(DUAL_USER_KEY, UIManager.isDualUserEnable())
                .add(MARKETPLACE_REF_ID, getMarketplaceReferenceId());

        if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
            multiParams.add(ACCESS_TOKEN, Dependencies.getAccessToken(mActivity))
                    .add(APP_ACCESS_TOKEN, Dependencies.getAccessToken(mActivity));
        }

        if (userData != null && userData.getData() != null && userData.getData().getVendorDetails() != null) {
            if (Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                multiParams.add(VENDOR_ID, userData.getData().getVendorDetails().getVendorId());
            }
            multiParams.add(FORM_ID, StorefrontCommonData.getFormSettings().getFormId());

            if (isUserIdRequired) {
                multiParams.add(USER_ID, userData.getData().getVendorDetails().getUserId());
            }

            if (userData.getData().getVendorDetails().getRefrenceId() != null && Dependencies.getAccessToken(mActivity) != null && !Dependencies.getAccessToken(mActivity).isEmpty()) {
                multiParams.add(REFERENCE_ID, userData.getData().getVendorDetails().getRefrenceId());
            }
            multiParams.add(MARKETPLACE_USER_ID, userData.getData().getVendorDetails().getMarketplaceUserId());

        }

        return multiParams;
    }

    public static String getAddPaymentURl() {
        try {
            return StorefrontCommonData.getUserData().getData().getAddCardLink();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * FOR ECOM
     */
    public static boolean isEcomApp() {
        return UIManager.getBusinessModelType().equals("ECOM") && UIManager.getNlevelEnabled() == 2;
//        return UIManager.getBusinessModelType().equals("ECOM") && StorefrontCommonData.getUserData().getData().getAdminCategoryEnabled() == 2;
    }


    /**
     * FOR LAUNDRY
     */
    public static boolean isLaundryApp() {
        try {
            return StorefrontCommonData.getAppConfigurationData().getBusinessModelType().equals("HYPERLOCAL_PRODUCT") && StorefrontCommonData.getAppConfigurationData().getOnboardingBusinessType() == Constants.OnboardingBusinessType.LAUNDRY;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Method to save the Referral Code
     *
     * @param context
     * @param refferalCode
     */
    public static void saveToggleView(Context context, int refferalCode) {
        Prefs.with(context).save(TOGGLE_VIEW, refferalCode);
    }

    public static int getToggleView(Context context) {
        return Prefs.with(context).getInt(TOGGLE_VIEW, 0);
    }

    public static boolean isPreorderSelecetedForMenu() {
        return isPreorderSelecetedForMenu;
    }

    public static int getIsPreorderSelecetedForMenu() {
        return isPreorderSelecetedForMenu ? 1 : 0;
    }

    public static void setIsPreorderSelecetedForMenu(boolean isPreorderSelecetedForMenu) {
        Dependencies.isPreorderSelecetedForMenu = isPreorderSelecetedForMenu;
    }

    public static void setSelectedDelivery(int selectedPickupMode) {
        Dependencies.selectedDeliveryMode = selectedPickupMode;
    }

    public static int getSelectedDeliveryMode() {
        return Dependencies.selectedDeliveryMode;
    }


    /**
     * Method to get CartOftenBoughtIds
     *
     * @return ArrayList<Integer> of ids
     */
    public static ArrayList<Integer> getCartOftenBoughtIds() {
        ArrayList<Integer> oftenIds = new ArrayList<>();
        for (int i = 0; i < getSelectedProductsArrayList().size(); i++) {
            if (getSelectedProductsArrayList().get(i).getOftenBoughtProducts().size() != 0) {
                oftenIds.addAll(getSelectedProductsArrayList().get(i).getOftenBoughtProducts());
            }
        }
        return oftenIds;
    }
}
