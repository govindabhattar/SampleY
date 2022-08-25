package com.tookancustomer.appdata;

/**
 * Created by shwetaaggarwal on 19/09/17.
 */

public interface StorefrontKeys {

    /* Here version string will be updated whenever there are some changes in paper db table */
    String version = "_v1";

    interface PaperDbKeys {
        String APP_CONFIGURATION_DATA = "app_configuration_data" + version;
        String GET_COUNTRY_CODE = "get_country_code" + version;
        String STOREFRONT_USER_DATA = "yelo_marketplace_user_data" + version;
        String APP_CONFIG_TERMINOLOGY = "app_config_terminology" + version;
        String LANGUAGE_STRINGS = "yelo_language_string" + version;
        String LANGUAGE_CODE = "yelo_language_code" + version;
        String LAST_PAYMENT_METHOD = "last_payment_method" + version;
        String CART_DATA = "cart_data" + version;
        String POSITION = "position" + version;
    }
}