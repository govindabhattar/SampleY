package com.tookancustomer.appdata;

import android.content.Context;

import com.tookancustomer.MyApplication;
import com.tookancustomer.models.CartData;
import com.tookancustomer.models.LanguageStrings.LanguagesCode;
import com.tookancustomer.models.MerchantCachedData;
import com.tookancustomer.models.appConfiguration.Datum;
import com.tookancustomer.models.appConfiguration.Terminology;
import com.tookancustomer.models.getCountryCode.GetCountryCode;
import com.tookancustomer.models.userdata.UserData;
import com.tookancustomer.utility.Log;
import com.tookancustomer.utility.Prefs;
import com.tookancustomer.utility.Utils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by shwetaaggarwal on 19/09/17.
 */

public class StorefrontCommonData implements StorefrontKeys.PaperDbKeys {

    /*
     * get Fetch app config data
     */
    private static Datum appConfigurationData = null;
    /*
     * get  Zoho Tookan country code data
     */
    private static GetCountryCode getCountryCode = null;
    /*
     *  get login response
     * */
    private static UserData userData = null;
    /*
     * get terminology data
     * */
    private static Terminology terminology = null;
    /*
     * Get selected language code
     * */
    private static LanguagesCode languageCode = null;
    /*
     * Get selected position
     * */
    private static int position;
    /*
     * Get last payment method used for create task
     * */
    private static long lastPaymentMethod = 0;
    /*
     * Get selected language strings
     * */
    private static Map<String, String> languageStrings = null;
    private static MerchantCachedData lastMerchantCachedData;
    /**
     * Get cart products list and its merchant data
     */
    private static CartData cartData = null;

    public static Datum getAppConfigurationData() {
        try {
            appConfigurationData = Paper.book().read(APP_CONFIGURATION_DATA, null);
        } catch (Exception e) {
            Utils.printStackTrace(e);
            appConfigurationData = null;
        }
        return appConfigurationData;

    }

    public static void setAppConfigurationData(Datum appConfigurationData) {
        if (appConfigurationData == null) {
            Paper.book().delete(APP_CONFIGURATION_DATA);
        } else {
            Paper.book().write(APP_CONFIGURATION_DATA, appConfigurationData);
        }
        StorefrontCommonData.appConfigurationData = appConfigurationData;
    }

    public static GetCountryCode getCountryCode() {
        if (getCountryCode == null)
            getCountryCode = Paper.book().read(GET_COUNTRY_CODE, null);
        return getCountryCode;
    }

    public static void setCountryCode(GetCountryCode getCountryCode) {
        Paper.book().write(GET_COUNTRY_CODE, getCountryCode);
        StorefrontCommonData.getCountryCode = getCountryCode;
    }

    public static UserData getUserData() {

        if (userData == null) {
            userData = Paper.book().read(STOREFRONT_USER_DATA, null);
        }
        return userData;
    }

    public static void setUserData(UserData userData) {
        Log.e("" + userData.getData().toString(), "..........................set");
        Paper.book().write(STOREFRONT_USER_DATA, userData);
        StorefrontCommonData.userData = userData;
    }

    public static com.tookancustomer.models.userdata.Datum getFormSettings() {
        if (userData == null)
            userData = Paper.book().read(STOREFRONT_USER_DATA, null);

        if (userData != null && userData.getData().getFormSettings() != null && userData.getData().getFormSettings().size() > 0) {
            return userData.getData().getFormSettings().get(0);
        } else {
            try {
                if (appConfigurationData == null)
                    appConfigurationData = Paper.book().read(APP_CONFIGURATION_DATA, null);

                com.tookancustomer.models.userdata.Datum formSettings = new com.tookancustomer.models.userdata.Datum(
                        appConfigurationData.getFormId(), appConfigurationData.getUserId(), appConfigurationData.getDomainName(),
                        appConfigurationData.getWorkFlow(), appConfigurationData.getLogo(), appConfigurationData.getPickupDeliveryFlag(),
                        Integer.valueOf(appConfigurationData.getForcePickupDelivery()), appConfigurationData.getVertical(), appConfigurationData.getProductView(),
                        appConfigurationData.getScheduledTask(), appConfigurationData.getBufferSchedule(), appConfigurationData.getFormName(),
                        appConfigurationData.getPaymentSettings(), appConfigurationData.getIsReviewRatingEnabled(), appConfigurationData.getIsFuguChatEnabled(),
                        appConfigurationData.getFuguChatToken());

                return formSettings;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public static Terminology getTerminology() {
        if (terminology == null)
            terminology = Paper.book().read(APP_CONFIG_TERMINOLOGY, null);
        return terminology;
    }

    public static void setTerminology(Terminology terminology) {
        Paper.book().write(APP_CONFIG_TERMINOLOGY, terminology);
        StorefrontCommonData.terminology = terminology;
    }

    public static LanguagesCode getSelectedLanguageCode() {
        if (languageCode == null)
            languageCode = Paper.book().read(LANGUAGE_CODE, null);
        return languageCode;
    }

    public static void setLanguageCode(LanguagesCode languageCode) {
        Paper.book().write(LANGUAGE_CODE, languageCode);
        StorefrontCommonData.languageCode = languageCode;
    }

    public static int getLanguagePosition(Context context) {
        position = Prefs.with(context).getInt(POSITION, 0);
        return position;
    }

    public static void setLanguagePosition(int position, Context context) {
        Prefs.with(context).save(POSITION, position);
        StorefrontCommonData.position = position;
    }

    public static long getLastPaymentMethod() {
        try {

            if (lastPaymentMethod == 0)
                lastPaymentMethod = Paper.book().read(LAST_PAYMENT_METHOD, 0L);
            return lastPaymentMethod;
        } catch (Exception e) {
            return 0L;
        }

    }

    public static void setLastPaymentMethod(long lastPaymentMethod) {
        Paper.book().write(LAST_PAYMENT_METHOD, lastPaymentMethod);
        StorefrontCommonData.lastPaymentMethod = lastPaymentMethod;
    }

    public static Map<String, String> getLanguageStrings() {
        if (languageStrings == null)
            languageStrings = Paper.book().read(LANGUAGE_STRINGS, null);
        return languageStrings;
    }

    public static void setLanguageStrings(Context context, Map<String, String> languageStrings) {
        Paper.book().write(LANGUAGE_STRINGS, languageStrings);
        StorefrontCommonData.languageStrings = languageStrings;
        Utils.setLanguage(context);

//        Gson gson = new GsonBuilder().create();
//        String json = gson.toJson(languageStrings);
//
//        try {
//            JSONObject obj = new JSONObject(json);
//            Map<String, String> result = Utils.toMap(obj);
//            if (StorefrontCommonData.getSelectedLanguageCode() != null) {
//                Restring.setStrings(StorefrontCommonData.getSelectedLanguageCode().getLanguageCode(), result);
//            }
//            Restring.setStrings("en", result);

//        } catch (Exception e) {
//        }

        Locale locale = new Locale(StorefrontCommonData.getSelectedLanguageCode() != null ? StorefrontCommonData.getSelectedLanguageCode().getLanguageCode() : "en");
        Locale.setDefault(locale);
        Utils.setLocale(locale);
    }

    public static String getString(Context context, int key) {
        if (context == null) {
            return "";
        }
        String value = "";
        String resName = context.getResources().getResourceEntryName(key);

        Map<String, String> translationStrings = getLanguageStrings();

        if (translationStrings != null) {
            value = translationStrings.get(resName);
        }
        return Utils.isEmpty(value) ? context.getString(key) : value;
    }

    public static MerchantCachedData getLastMerchantCachedData() {
        return lastMerchantCachedData;
    }

    public static void setLastMerchantCachedData(MerchantCachedData lastMerchantCachedData) {
        StorefrontCommonData.lastMerchantCachedData = lastMerchantCachedData;
    }

    public static CartData getCartData() {
        if (cartData == null)
            cartData = Paper.book().read(CART_DATA, new CartData());
        return cartData;
    }

    public static void setCartData(CartData cartData) {
        StorefrontCommonData.cartData = cartData;
        Paper.book().write(CART_DATA, cartData);
    }

    public static com.tookancustomer.models.MarketplaceStorefrontModel.Datum getCartMerchantData() {
        return getCartData().getMerchantData();
    }

    public static void setCartMerchantData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum merchantData) {
        getCartData().setMerchantData(merchantData);
    }

    public static ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum> getCartProductsList() {
        return getCartData().getProductsArrayList();
    }

    public static void setCartProductsList(ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum> productsArrayList) {
        getCartData().setProductsArrayList(productsArrayList);
    }

    public static void clearCart() {
        getCartData().setProductsArrayList(new ArrayList<com.tookancustomer.models.ProductCatalogueData.Datum>());
        getCartData().setMerchantData(null);
    }

    public static int getCartTotalSize() {
        int totalQuantity = 0;
        for (int i = 0; i < getCartProductsList().size(); i++) {
            totalQuantity = totalQuantity + getCartProductsList().get(i).getSelectedQuantity();
        }
        return totalQuantity;
    }

    public static double getCartSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < getCartProductsList().size(); i++) {
            if (getCartProductsList().get(i).getSelectedQuantity() != 0) {
                if (getCartProductsList().get(i).getItemSelectedList().size() > 0) {
                    for (int j = 0; j < getCartProductsList().get(i).getItemSelectedList().size(); j++) {
                        subtotal = subtotal + getCartProductsList().get(i).getItemSelectedList().get(j).getTotalPriceWithQuantity();
                    }
                } else {
                    subtotal = subtotal + (getCartProductsList().get(i).getSelectedQuantity() * getCartProductsList().get(i).getPrice().doubleValue());
                }
            }
        }
        return subtotal;
    }

    public static void addCartItem(com.tookancustomer.models.ProductCatalogueData.Datum selectedProduct) {
        boolean isProductAlreadyAdded = false;
        int index = 0;

        for (int i = 0; i < getCartProductsList().size(); i++) {
            if (getCartProductsList().get(i).getProductId().equals(selectedProduct.getProductId())) {
                isProductAlreadyAdded = true;
                index = i;
                break;
            }
        }

        if (isProductAlreadyAdded) {
            if (selectedProduct.getSelectedQuantity() > getCartProductsList().get(index).getSelectedQuantity()) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_QUANTITY, selectedProduct.getName());
            } else {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.REMOVE_QUANTITY, selectedProduct.getName());
            }

            if (getCartMerchantData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                getCartProductsList().clear();
                if (selectedProduct.getSelectedQuantity() != 0) {
                    if (selectedProduct.getItemSelectedList().size() > 0) {
                        for (int i = 0; i < selectedProduct.getItemSelectedList().size(); i++) {
                            if (selectedProduct.getItemSelectedList().get(i).getQuantity() == 0) {
                                selectedProduct.getItemSelectedList().remove(i);
                                i--;
                            }
                        }
                    }
                    getCartProductsList().add(selectedProduct);
                }
            } else {
                getCartProductsList().remove(index);

                if (selectedProduct.getSelectedQuantity() != 0) {
                    if (selectedProduct.getItemSelectedList().size() > 0) {
                        for (int i = 0; i < selectedProduct.getItemSelectedList().size(); i++) {
                            if (selectedProduct.getItemSelectedList().get(i).getQuantity() == 0) {
                                selectedProduct.getItemSelectedList().remove(i);
                                i--;
                            }
                        }
                    }
                    getCartProductsList().add(index, selectedProduct);
                }
            }
        } else {
            if (getCartMerchantData().getMultipleProductInSingleCart() == Constants.ProductAddedInCart.SINGLE_PRODUCT) {
                getCartProductsList().clear();
            }
            if (selectedProduct.getSelectedQuantity() > 0) {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.ADD_QUANTITY, selectedProduct.getName());
            } else {
                MyApplication.getInstance().trackEvent(Constants.GoogleAnalyticsValues.REMOVE_QUANTITY, selectedProduct.getName());
            }

            if (selectedProduct.getSelectedQuantity() != 0) {
                getCartProductsList().add(selectedProduct);
            }
        }
        setCartProductsList(getCartProductsList());
    }

  /*  public static String getCartDescription() {
        String listDescription = "";
        for (int i = 0; i < getCartProductsList().size(); i++) {
            if (getCartProductsList().get(i).getSelectedQuantity() != 0) {
                if (getCartProductsList().get(i).getItemSelectedList().size() > 0) {

                    double subTotal = 0;

                    for (int j = 0; j < getCartProductsList().get(i).getItemSelectedList().size(); j++) {
                        subTotal = subTotal + getCartProductsList().get(i).getItemSelectedList().get(j).getTotalPriceWithQuantity();
                    }

                    listDescription = listDescription
                            + getCartProductsList().get(i).getSelectedQuantity() + "X\t"
                            + getCartProductsList().get(i).getName() + "\t\t"
                            + Utils.getCurrencySymbol() + subTotal + (i == getCartProductsList().size() - 1 ? "" : "\n");


                } else {
                    listDescription = listDescription
                            + getCartProductsList().get(i).getSelectedQuantity() + "X\t"
                            + getCartProductsList().get(i).getName() + "\t\t"
                            + Utils.getCurrencySymbol() + (getCartProductsList().get(i).getSelectedQuantity() * getCartProductsList().get(i).getPrice().doubleValue()) + (i == getCartProductsList().size() - 1 ? "" : "\n");
                }
            }
        }
        return listDescription;
    }*/

}