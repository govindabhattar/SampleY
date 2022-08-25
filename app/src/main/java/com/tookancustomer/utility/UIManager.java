package com.tookancustomer.utility;

import android.os.Build;

import com.tookancustomer.R;
import com.tookancustomer.appdata.Dependencies;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.appConfiguration.CustomOrderPage;
import com.tookancustomer.models.appConfiguration.GoogleAnalyticsOptions;

import java.util.HashMap;
import java.util.List;

import static com.tookancustomer.appdata.Constants.DateFormat.END_USER_DATE_FORMAT;
import static com.tookancustomer.appdata.Constants.DateFormat.END_USER_DATE_FORMAT_24;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_12;
import static com.tookancustomer.appdata.Constants.DateFormat.STANDARD_DATE_FORMAT_NO_SEC;
import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_12;
import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_12_without_ampm;
import static com.tookancustomer.appdata.Constants.DateFormat.TIME_FORMAT_24_WITHOUT_SECOND;

/**
 * Created by Nadeem Khan on 16/11/16.
 */

public class UIManager {
    private final static boolean isMapThemeLight = false;  //true = light map theme , false = dark map theme
    private final static int splashBackground = 0;  // 0 for color  , 1 for image , 2 for video
    public static int DEVICE_API_LEVEL = Build.VERSION.SDK_INT;
    public static boolean isMarketplaceSingleRestaurant = false;
    public static boolean isPickup;
    public static boolean isReviewRatingRequired = false;
    public static int llWelcomeToTopMargin;
    public static int totalStores = 1;
    private static double latitude;
    private static double longitude;
    private static String address;
    private static double merchetApiLatitudeSplash;
    private static double merchantApiLongitudeSplash;
    private static HashMap<Integer, GoogleAnalyticsOptions> googleAnalyticsOptionsHashMap = new HashMap<>();

    public static boolean isMapThemeLight() {
        return isMapThemeLight;
    }

    public static int getMapStyle() {
        return isMapThemeLight() ? R.raw.map_style_light : R.raw.map_style_dark;
    }


    public static double getMerchetApiLatitudeSplash() {
        return merchetApiLatitudeSplash;
    }

    public static void setMerchetApiLatitudeSplash(double merchetApiLatitudeSplash) {
        UIManager.merchetApiLatitudeSplash = merchetApiLatitudeSplash;
    }

    public static double getMerchantApiLongitudeSplash() {
        return merchantApiLongitudeSplash;
    }

    public static void setMerchantApiLongitudeSplash(double merchantApiLongitudeSplash) {
        UIManager.merchantApiLongitudeSplash = merchantApiLongitudeSplash;
    }

    public static int getSplashBackground() {
        return splashBackground;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        UIManager.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        UIManager.longitude = longitude;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        UIManager.address = address;
    }

    public static HashMap<Integer, GoogleAnalyticsOptions> getGoogleAnalyticsOptionsHashMap() {
        return googleAnalyticsOptionsHashMap;
    }

    public static void setGoogleAnalyticsOptionsHashMap(List<GoogleAnalyticsOptions> googleAnalyticsOptionsArrayList) {
        UIManager.googleAnalyticsOptionsHashMap = new HashMap<>();

        for (GoogleAnalyticsOptions googleAnalyticsOptions : googleAnalyticsOptionsArrayList) {
            googleAnalyticsOptionsHashMap.put(googleAnalyticsOptions.getGaMasterOptionId(), googleAnalyticsOptions);
        }
    }


    public static boolean getIsReviewRatingRequired() {
        return StorefrontCommonData.getFormSettings().getIsReviewRatingEnabled() == 1;
    }

    public static int getTotalStores() {
        return totalStores;
    }

    public static void setTotalStores(int totalStores) {
        UIManager.totalStores = totalStores;
    }

    public static Integer getTipType() {
        return 0;
    }

    public static int getNlevelEnabled() {
        /**
         * is_nlevel key added for ECOM flow
         * will be 1 or 0
         * for ECOM value=1
         */
        return StorefrontCommonData.getAppConfigurationData().getNlevelEnabled();
    }

    public static boolean isFuguChatEnabled() {
        return StorefrontCommonData.getFormSettings().getIsFuguChatEnabled() == 1;
    }

    public static int isDualUserEnable() {
        return StorefrontCommonData.getAppConfigurationData() != null ? StorefrontCommonData.getAppConfigurationData().getIsDualUserEnable() : 0;
    }

    public static int getIsEmailVerificationRequried() {
        return StorefrontCommonData.getAppConfigurationData().getIsEmailVerificationRequried();
    }

    public static int getMerchantViewProfile() {
        return StorefrontCommonData.getAppConfigurationData().getMerchantViewProfile();
    }

    public static boolean isSignUpAvailable() {
        if (!Dependencies.isDemoApp() && StorefrontCommonData.getAppConfigurationData() != null)
            return StorefrontCommonData.getAppConfigurationData().getSignupAllow() == 1;
        else return true;
//        return isSignUpAvailable;
    }

    public static boolean isTnCAvailable() {
        return StorefrontCommonData.getAppConfigurationData().getShowTNC() == 1;
    }

    public static boolean isFacebookAvailable() {
        return StorefrontCommonData.getAppConfigurationData() != null && StorefrontCommonData.getAppConfigurationData().getIsFacebookRequired() == 1;
    }

    public static boolean isGPlusAvailable() {
        return StorefrontCommonData.getAppConfigurationData() != null && StorefrontCommonData.getAppConfigurationData().getIsGoogleRequired() == 1;
    }

    public static boolean isInstagramAvailable() {
        return StorefrontCommonData.getAppConfigurationData() != null && StorefrontCommonData.getAppConfigurationData().getIsInstagramRequired() == 1;
    }

    public static boolean isOtpLoginAvailable() {
        return StorefrontCommonData.getAppConfigurationData() != null && StorefrontCommonData.getAppConfigurationData().getVendorOTPLoginSignup() == 1;
    }

    public static String getTNC() {
        return StorefrontCommonData.getAppConfigurationData().getContactDetails().getTnc();
    }

    public static boolean isOTPAvailable() {
        return StorefrontCommonData.getAppConfigurationData().getIsOtpRequired() == 1;
    }

    public static int getVertical() {
        return StorefrontCommonData.getFormSettings().getVertical();
    }

    public static String getBusinessModelType() {
        if (StorefrontCommonData.getAppConfigurationData() != null
                && StorefrontCommonData.getAppConfigurationData().getBusinessModelType() !=null) {
            return StorefrontCommonData.getAppConfigurationData().getBusinessModelType();
        }
        return "";
    }

    public static List<LanguagesCode> getLanguagesArrayList() {
        return StorefrontCommonData.getAppConfigurationData().getLanguages();
    }

    public static boolean isShowDateFilter() {
        return StorefrontCommonData.getAppConfigurationData().getShowDateFilter() == 1;
    }

    public static Integer getIsTnCActive() {
        if (StorefrontCommonData.getAppConfigurationData() != null
                && StorefrontCommonData.getAppConfigurationData().getIsTncActive() != 0) {
            return StorefrontCommonData.getAppConfigurationData().getIsTncActive();
        }
        return 0;
    }

    public static int getSignupField() {
        //0 Email reqd , 1 Phone Reqd , 2 Both email and phone reqd
        return StorefrontCommonData.getAppConfigurationData().getSignupField();
    }

    public static int getCancellationReasonType() {
        //0 -- Self explanatory reason only, 1 -- Reasons from backend
        return StorefrontCommonData.getAppConfigurationData().getCancellationReasonType();
    }

    public static boolean getIsBannerEnabled() {
        return StorefrontCommonData.getFormSettings().getIsBannersEnabled() == 1;
    }

    public static boolean getIsBusinessCategoryEnabled() {
        return StorefrontCommonData.getFormSettings().getIsBusinessCategoryEnabled() == 1;
    }

    public static int getDecimalCalculationPrecisionPoint() {
        return StorefrontCommonData.getAppConfigurationData().getDecimalCalculationPrecisionPoint();
    }

    public static int getDecimalDisplayPrecisionPoint() {
        return StorefrontCommonData.getAppConfigurationData().getDecimalDisplayPrecisionPoint();
    }

    public static boolean isStaticAddressEnabledForAdmin() {
        return StorefrontCommonData.getAppConfigurationData().getIsStaticAddressEnabled() == 1;
    }

    public static boolean getCustomOrderActive() {
        return StorefrontCommonData.getAppConfigurationData().getIsCustomOrderActive() == 1;
    }

    public static int getIsLoyaltyEnable() {
        return StorefrontCommonData.getAppConfigurationData().getIsLoyaltyEnable();
    }

    public static boolean isMenuEnabled() {
        return StorefrontCommonData.getAppConfigurationData().getIsMenuEnabled() == 1;
    }

    public static boolean getSideOrderActive() {
        return StorefrontCommonData.getAppConfigurationData().getSideOrder() > 0;
    }

    public static boolean getRecurringTaskActive() {
        return StorefrontCommonData.getAppConfigurationData().getRecurringTask() > 0;
    }

    public static boolean isCustomQuotationEnabled() {
        return StorefrontCommonData.getAppConfigurationData().getCustomQuotationEnabled() == 1;
    }

    public static boolean isHoldPaymentEnabled() {
        return StorefrontCommonData.getAppConfigurationData().getIsHoldAmountActive() == 1;
    }

    public static boolean isTookanActive() {
        return StorefrontCommonData.getAppConfigurationData().getIsTookanActive() == 1;
    }

    public static boolean isCancellationPolicyEnabled() {
        return StorefrontCommonData.getAppConfigurationData().getIsCancellationPolicyEnabled() == 1;
    }

    public static boolean showServiceTime() {
        return StorefrontCommonData.getAppConfigurationData().getShowServiceTime() == 1;
    }

    public static CustomOrderPage getCustomOrderPage() {
        return StorefrontCommonData.getAppConfigurationData().getCustomOrderPage();
    }

    public static boolean isRewardsActive() {
        return StorefrontCommonData.getAppConfigurationData().getIsRewardActive() == 1;
//        return true;
    }

    //timeFormat 1->12 hour format
    //timeFormat 2->24 hour format
    public static String getDateTimeFormat() {

        String format = END_USER_DATE_FORMAT;
        if (!is12HourFormat()) {
            format = END_USER_DATE_FORMAT_24;
        }
        return format;
    }


    public static String getTimeFormat() {

        String format = TIME_FORMAT_12;
        if (!is12HourFormat()) {
            format = TIME_FORMAT_24_WITHOUT_SECOND;
        }
        return format;
    }

    public static String getTimeFormat(int hour, int minute) {

        return is12HourFormat() ? DateUtils.getInstance().getTimeIn12Hours(hour, minute)
                : DateUtils.getInstance().getTimeIn24Hours(hour, minute);


    }

    public static String getStandardDateTimeFormat() {
//        String STANDARD_DATE_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";
//        String STANDARD_DATE_FORMAT_12 = "yyyy-MM-dd hh:mm aa";
        return is12HourFormat() ? STANDARD_DATE_FORMAT_12 : STANDARD_DATE_FORMAT_NO_SEC;


    }

    public static String getTimeFormatWithoutAmPm() {

        String format = TIME_FORMAT_12_without_ampm;
        if (!is12HourFormat()) {
            format = TIME_FORMAT_24_WITHOUT_SECOND;
        }
        return format;
    }

    public static boolean is12HourFormat() {
        return StorefrontCommonData.getAppConfigurationData().getTimeFormat() == 1;
    }

    public static boolean isShowDeliveryTimeEnable() {
        return StorefrontCommonData.getAppConfigurationData().getShowDeliveryTime() == 1;
    }

    public static boolean isCustomerLoginRequired() {
        if (Dependencies.isDemoApp())
            return true;
        else
            return StorefrontCommonData.getAppConfigurationData().getIsCustomerLoginRequired() == 1;
    }

    public static boolean isMerchantPaymentMethodsEnabled() {
        return StorefrontCommonData.getAppConfigurationData().getMerchantSelectPaymentMethod() == 1;

    }


    /**
     * if admin has enabled that customer should be verified befor placing order
     * then check is customer verified or not
     * if not then show popup
     * else continue
     *
     * @return
     */
    public static boolean isCutomerVerifactionRequired() {
        return StorefrontCommonData.getAppConfigurationData().getIsCustomerVerificationRequired() == 1 &&
                StorefrontCommonData.getUserData().getData().getVendorDetails().getIsVendorVerified() == 0;
//        return true;
    }

    // Default (id : 1)
    // Comma Separated - 2
    // Dot Separated -3
    //Quote Separated - 4
    public static Integer getCurrencyFormat() {
        return StorefrontCommonData.getAppConfigurationData().getCurrency_format();
        // return 3;
    }

    public static String getCurrency(String amount) {
        String formatted_amount = amount;
        if (getCurrencyFormat() == 2) {
            formatted_amount = Utils.getDecimalFormattedNumber(amount);
            return formatted_amount;
        } else if (getCurrencyFormat() == 3) {
            formatted_amount = Utils.getFormattedPriceGerman(amount);
            return formatted_amount;
        } else if (getCurrencyFormat() == 4) {
            formatted_amount = Utils.getDecimalFormattedNumber(amount).replace(",", "'");
            return formatted_amount;
        } else
            return formatted_amount;
    }

}
