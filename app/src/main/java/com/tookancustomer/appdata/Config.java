package com.tookancustomer.appdata;

import android.content.Context;

import com.tookancustomer.utility.Prefs;

/**
 * Class to configure the App in various modes
 */
public class Config {
    //    public static String zohoServerUrl="https://freegeoip.net/"; //zohoURL(Base URL)
    public static String zohoServerUrl = "https://ip.tookanapp.com:8000/"; //zohoURL(Base URL)

    /**
     * Method to return default app mode
     *
     * @return
     */
    private static AppMode getDefaultAppMode() {

        return AppMode.LIVE;
    }

    /**
     * Method tell whether the Build is release or not
     *
     * @return
     */
    public static boolean isRelease() {
        return getDefaultAppMode() == AppMode.LIVE;
    }

    /**
     * Method to retrieve last saved AppMode
     *
     * @param context
     * @return
     */
    public static AppMode getCurrentAppMode(Context context) {
        AppMode appMode;

        if (isRelease()) appMode = getDefaultAppMode();
        else appMode = getAppMode(context);

        setAppMode(context, appMode.name());

        return appMode;
    }

    /**
     * Method to retrieve last saved AppMode
     *
     * @param context
     * @return
     */
    public static String getServerUrl(Context context) {
        return getCurrentAppMode(context).serverUrl;
    }

    public static String getZohoServerUrl(Context context) {
        return zohoServerUrl;
    }

    /**
     * Method to retrieve last saved AppMode
     *
     * @param context
     * @return
     */
    public static String getMqttServerUrl(Context context) {
        return getCurrentAppMode(context).mqttServerUrl;
    }

    /**
     * Method to save the App Mode
     *
     * @param context
     * @param appModeName
     */
    public static void setAppMode(Context context, String appModeName) {
        Prefs.with(context).save(Keys.Prefs.APP_MODE, appModeName);
    }

    /**
     * Method to return app mode
     *
     * @return
     */
    public static AppMode getAppMode(Context context) {
        return AppMode.valueOf(getCurrentAppModeName(context));
    }

    /**
     * Method to evaluate the Currently used AppMode
     *
     * @param context
     * @return
     */
    public static String getCurrentAppModeName(Context context) {
        return Prefs.with(context).getString(Keys.Prefs.APP_MODE, getDefaultAppMode().name());
    }

    /**
     * Various Modes to specify the Server Url
     * intended for the End User
     */
    public enum AppMode {
        LIVE("https://api.yelo.red/", "app"),
        BETA("https://beta-api.yelo.red/", "beta"),
        BETA_2("https://beta-api-3010.yelo.red", "beta"),
        TEST("https://test-api.yelo.red/", "test"),
        TEST_ECOM("https://ecom-api.yelo.red/", "test"),
        TEST_2026("https://test-api-2026.yelo.red/", "test"),
        TEST_3001("http://test-api.yelo.red:3001/", "test"),
        TEST_3002("http://test-api.yelo.red:3002/", "test"),
        TEST_3003("http://test-api.yelo.red:3003/", "test"),
        TEST_3004("https://test-api-3004.yelo.red/", "test"),
        TEST_3006("http://test-api.yelo.red:3006/", "test"),
        TEST_3007("http://test-api.yelo.red:3007/", "test"),
        TEST_3008("https://test-api-3008.yelo.red/", "test"),
        TEST_3009("https://test-api-3009.yelo.red/", "test"),
        TEST_3010("https://test-api-3010.yelo.red/", "test"),
        TEST_3014("http://test-api.yelo.red:3014/", "test"),
        TEST_3019("https://test-api-3019.yelo.red", "test"),
        TEST_3022("http://test-api.yelo.red:3022/", "test"),
        TEST_3023("https://test-api-3023.yelo.red/", "test"),
        TEST_3025("https://paystack-api-3025.yelo.red", "test"),
        TEST_3026("https://test-api-3026.yelo.red/", "test"),
        TEST_3027("https://test-api-3027.yelo.red", "test"),
        TEST_3028("https://test-api-3028.yelo.red", "test"),
        TEST_3030("http://test-api.yelo.red:3030/", "test"),
        TEST_3031("https://test-api-3031.yelo.red/", "test"),
        TEST_3032("https://test-api-3032.yelo.red/", "test"),
        TEST_3034("https://test-api-3034.yelo.red/", "test"),
        TEST_3040("https://test-api-3040.yelo.red/", "test"),
        TEST_2024("https://rachit-test-2024.yelo.red/", "test"),
        TEST_3036("https://test-api-3036.yelo.red/", "test"),
        TEST_DHEERAJ("https://dheeraj-api.yelo.red/", "test"),
        TEST_RACHIT("https://rachit-test-2024.yelo.red/", "test"),
        TEST_3022_BILLPLZ("https://yelo-api-3022.taxi-hawk.com/", "test"),
        TEST_2025_ROHIT("https://rohit-test-2025.yelo.red/", "test"),
        PAYSTACK("https://paystack-api-3025.yelo.red/", "test"),
        INSTAPAY("https://test-payment-gateway.yelo.red/", "test");

        private final String serverUrl;
        private final String mqttServerUrl;

        AppMode(String serverUrl, String mqttServerUrl) {
            this.serverUrl = serverUrl;
            this.mqttServerUrl = mqttServerUrl;
        }
    }

}