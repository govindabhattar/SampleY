package com.tookancustomer.appdata;

/**
 * Created by cl-macmini-83 on 19/11/16.
 */
public interface Keys {

    interface Prefs {
        String APP_MODE = "app_mode";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String DEVICE_TOKEN = "device_token";
        String ACCESS_TOKEN = "access_token";
        String ACCESS_TOKEN_GUEST = "access_token_guest";
        String GUEST_CHECKOUT_FLOW_ONGOING = "guest_checkout_flow_ongoing";
        String VENDOR_ID_GUEST = "vendor_id_guest";

        String USER_OPTIONS = "user_options";
        String DELIVERY_OPTIONS = "delivery_options";
        String SELECTED_PRODUCT_LIST = "selected_product_list";
        String MARKETPLACE_REF_ID = "marketplace_reference_id";
        String IS_MP_FIRST_INSTALL = "is_mp_first_install";
        String TOGGLE_VIEW = "toggle_view";
        String IS_LOCK_ENABLED="IS_LOCK_ENABLED";
        String EXTRA_REFERENCE_ID = "EXTRA_REFERENCE_ID";
        String OLD_DELIVERY_CHARGE = "old_delivery_charge";
        String REF_ID = "ref_id";
        String ADDRESS = "address";
    }

    interface CustomField {
        /**
         * Custom Field: DataTypes
         */
        interface EditableType {
            String READABLE = "0";
            String WRITABLE = "1";

            String READ_ONLY = "Read Only";
        }
    }

    /**
     * Custom Field: DataTypes
     */
    interface DataType {
        String NUMBER = "Number";
        String TEXT = "Text";
        String IMAGE = "Image";
        String DATE = "Date";
        String DROP_DOWN = "Dropdown";
        String SINGLE_SELECT= "Single-Select";
        String CHECKBOX = "Checkbox";
        String TELEPHONE = "Telephone";
        String EMAIL = "Email";
        String URL = "URL";
        String DATE_FUTURE = "Date-Future";
        String DATE_PAST = "Date-Past";
        String DATE_TODAY = "Date-Today";
        String CHECKLIST = "Checklist";
        String TABLE = "Table";
        String DATETIME = "Date-Time";
        String DATETIME_FUTURE = "Datetime-Future";
        String DATETIME_PAST = "Datetime-Past";
        String BARCODE = "Barcode";
        String MULTI_SELECT = "Multi-Select";
        String TEXT_AREA = "TextArea";
        String TIME = "Time";
    }


    interface Extras {
        String IS_ADMIN_VERIFICATION_REQUIRED = "is_admin_verification_required";
        String SUCCESS_MESSAGE = "success_message";
        String FAILURE_MESSAGE = "failure_message";
        String NEUTRAL_MESSAGE = "neutral_message";
        String JOB_ID = "job_id";
        String PAYMENT_METHOD_DATA = "payment_method_data";
        String CARD_DATA = "card_data";
        String URL_WEBVIEW = "url_webview";
        String HEADER_WEBVIEW = "header_webview";
        String IS_TNC = "isTNC";
        String IS_HTML = "isHTML";
        String CATEGORY_LEVEL = "category_level";
        String HEADER_NAME = "header_name";
        String PARENT_DATA_OBJECT = "parent_data";
        String PARENT_ID = "parent_id";
        String SHOW_PRODUCT_IMAGES = "showProductImages";
        String PARENT_CATEGORY_ID = "category_id";
        String TIP_OPTION_LIST = "tipOptionList";
        String OLD_DELIVERY_CHARGE = "old_delivery_charge";


        String SEND_PAYMENT_FOR_TASK = "send_payment";
        String VALUE_PAYMENT = "VALUE_PAYMENT";
        String FROM_ACCOUNT_SCREEN = "from_account_screen";
        String FACEBOOK_DETAILS = "FACEBOOK_DETAILS";
        String INSTAGRAM_DETAILS = "INSTAGRAM_DETAILS";
        String CREATE_TASK_BUILDER = "create_task_builder";
        String FARE_ESTIMATE_BUILDER = "fare_estimate_builder";
        String CATEGORY_DETAIL = "categoryDetail";
        String TASK_DETAILS = "task_details";
        String PICKUP_LATITUDE = "pickup_latitude";
        String PICKUP_LONGITUDE = "pickup_longitude";
        String PICKUP_ADDRESS = "pickup_address";
        String SEARCHED_STRING = "searched_string";
        String IS_HOME_ADDED = "isHomeAdded";
        String IS_WORK_ADDED = "isWorkAdded";
        String KEY_ITEM_POSITION = "item_position";
        String PRODUCT_CATALOGUE_DATA = "productCatalogueData";
        String PRODUCT_QUANTITY = "productquantity";
        String PRODUCT_PRICE = "productprice";
        String IS_EDIT_CUSTOMIZATION = "iseditcustomization";
        String HEADER_LOGO = "header_logo";
        String STOREFRONT_DATA = "storefront_data";
        String PRODUCT_DATA = "product_data";
        String STOREFRONT_DATA_ITEM_POS = "storefront_data_item_pos";
        String IS_SIDE_MENU = "is_side_menu";
        String IS_LOGIN_FROM_CHECKOUT = "is_login_from_checkout";
        String IS_OTP_FROM_PROFILE = "is_otp_from_profile";
        String IS_ONBOARDING_FROM_PROFILE = "is_onboarding_from_profile";
        String SHOW_RATE_STARS = "rate_stars";
        String IS_SCHEDULING_FROM_CHECKOUT = "is_scheduling_from_checkout";
        String IS_START_TIME = "is_start_time";
        String SELECTED_DATE = "selectedDate";
        String PRODUCT_DETAIL_DATA = "product_detail_data";
        String CHECK_IN_DATE = "check_in_date";
        String SERVICE_TIME = "service_time";
        String CHECK_OUT_DATE = "check_out_date";
        String MIN_PRICE = "min_price";
        String MAX_PRICE = "max_price";
        //        String SELECTED_FILTER_LIST = "selected_filter_list";
        String FILTER_LIST_MAP = "filter_list_map";
        String FILTER_MIN_PRICE = "filter_min_price";
        String FILTER_MAX_PRICE = "filter_max_price";
        String FILTER_DATA = "filter_data";
        String ADMIN_CATALOGUE = "admin_catalogue";
        String IS_FROM_OWNER_PROFILE = "is_from_owner_profile";
        String ADMIN_CATALOGUE_SELECTED_CATEGORIES = "admin_catalogue_selected_categories";
        String BUSINESS_CATEGORY_ID = "business_category_id";
        String OPEN_OTP_FOR_PAYTM = "OPEN_OTP_FOR_PAYTM";
        String OPEN_OTP_FOR_FORGOT_PASSWORD = "OPEN_OTP_FOR_FORGOT_PASSWORD";
        String TOTAL_AMOUNT = "total_amount";
        String EXTRA_IS_LAUNDRY_EDIT_ORDER = "EXTRA_IS_LAUNDRY_EDIT_ORDER";
        String ADDRESS = "address";
        String BALANCE_TO_BE_ADDED = "balance_to_be_added";
        String STOREFRONT_MODEL = "storefrontModel";
        String PARENT_CATEGORY_DATA = "parentCategoryData";
        String FROM_WALLET_APP_SCREEN = "fromAppWalletScreen";
        String TRANSACTION_ID = "transactionId";
        String JOB_PAYMENT_DETAIL_ID = "jobPaymentDetailId";
        String PAYMENT_FOR_FLOW = "paymentForFlow";
        String USER_DEBT_DATA = "userDebtData";
        String AGENT_ID = "agent_id";
        String IS_FROM_MANDATORY_CATEGORY = "is_from_mandatory";
        String IS_WISHLIST_CHANGED = "IS_WISHLIST_CHANGED";
        String RULE_ID = "rule_id";
        String UPDATE_QUESTIONNAIRE = "update_questionnaire";
        String IS_AGENT_SELECTED = "is_agentSelected";
        String IS_EDIT_ORDER = "is_edit_order";
        String SOCHITEL_OPERATOR = "SOCHITEL_OPERATOR";
        String EDIT_JOB_ID = "edit_job_id";
        String IS_PD_FLOW = "IS_PD_FLOW";
        String IS_SELF_PICKUP = "IS_SELF_PICKUP";
        String FROM_SEARCH_SCREEN = "FROM_SEARCH_SCREEN";
        String FROM_AGENT_SCREEN = "FROM_AGENT_SCREEN";
        String IS_OFTEN_BOUGHT = "IS_OFTEN_BOUGHT";


    }

    interface MetaDataKeys {
        String PAYMENT_METHOD = "paymentMethod";
        String PAYMENT_MODE = "paymentMode";
        String BASE_FARE = "baseFare";
        String DISTANCE_FARE = "distanceFare";
        String TIME_FARE = "timeFare";
        String DESTINATION_ADDRESS = "destinationAddress";
        String DESTINATION_LATITUDE = "destinationLatitude";
        String DESTINATION_LONGITUDE = "destinationLongitude";
        String CATEGORY_ID = "categoryId";
        String CANCELLATION_CHARGES = "cancellationCharges";
    }

    interface APIFieldKeys {
        String CUSTOME_PICKUP_ADDRESS = "custom_pickup_address";
        String CUSTOME_PICKUP_LONGITUDE = "custom_pickup_longitude";
        String CUSTOME_PICKUP_LATITUDE = "custom_pickup_latitude";
        String CUSTOME_PICKUP_NAME = "custom_pickup_name";
        String CUSTOME_PICKUP_EMAIL = "custom_pickup_email";
        String CUSTOME_PICKUP_PHONE = "custom_pickup_phone";


        String IS_SCHEDULED = "is_scheduled";
        String HAS_PICKUP = "has_pickup";
        String HAS_DELIVERY = "has_delivery";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String AUTO_ASSIGNMENT = "auto_assignment";
        String VERTICAL = "vertical";
        String CUSTOMER_USERNAME = "customer_username";
        String CUSTOMER_ADDRESS = "customer_address";
        String CUSTOMER_EMAIL = "customer_email";
        String CUSTOMER_PHONE = "customer_phone";
        String JOB_PICKUP_NAME = "job_pickup_name";
        String JOB_PICKUP_ADDRESS = "job_pickup_address";
        String JOB_PICKUP_EMAIL = "job_pickup_email";
        String JOB_PICKUP_PHONE = "job_pickup_phone";
        String JOB_PICKUP_LATITUDE = "job_pickup_latitude";
        String JOB_PICKUP_LONGITUDE = "job_pickup_longitude";
        String JOB_DELIVERY_LATITUDE = "job_delivery_latitude";
        String JOB_DELIVERY_LONGITUDE = "job_delivery_longitude";
        String JOB_DELIVERY_ADDRESS = "job_delivery_address";
        String JOB_DELIVERY_NAME = "job_delivery_name";
        String JOB_DELIVERY_PHONE = "job_delivery_phone";
        String JOB_DELIVERY_EMAIL = "job_delivery_email";
        String JOB_PICKUP_DATETIME = "job_pickup_datetime";
        String JOB_DELIVERY_DATETIME = "job_delivery_datetime";
        String JOB_DESCRIPTION_FIELD = "job_description";
        String LAYOUT_TYPE = "layout_type";
        String PICKUP_CUSTOM_FIELD_TEMPLATE = "pickup_custom_field_template";
        String CUSTOM_FIELD_TEMPLATE = "custom_field_template";
        String PICKUP_META_DATA = "pickup_meta_data";
        String META_DATA = "meta_data";
        String PAYMENT_METHOD_FIELD = "payment_method";
        String ACCESS_TOKEN = "access_token";
        String APP_ACCESS_TOKEN = "app_access_token";
        String APP_VERSION = "app_version";
        String YELO_APP_TYPE = "yelo_app_type";
        String APP_TYPE = "app_type";
        String IS_DEMO_APP = "is_demo_app";
        String VENDOR_ID = "vendor_id";
        String LANGUAGE = "language";
        String ACCOUNT_ID = "account_id";
        String USER_ID = "user_id";
        String IS_QR_CODE = "skip_geofence";
        String PRODUCT_ID = "product_id";
        String FORM_ID = "form_id";
        String REFERENCE_ID = "reference_id";
        String TIMEZONE = "timezone";
        String MARKETPLACE_REF_ID = "marketplace_reference_id";
        String MARKETPLACE_USER_ID = "marketplace_user_id";
        String CITY_ID = "city_id";
        String MODE_PAYMENT = "MODE_PAYMENT";
        String CITY_NAME = "city_name";
        String SEARCH_TEXT = "search_text";
        String MAP_VIEW_FLAG = "map_view_flag";
        String SECOND_RADIUS = "second_radius";
        String FIRST_RADIUS = "first_radius";
        String DATE = "date";
        String CURRENT_LATITUDE = "current_latitude";
        String CURRENT_LONGITUDE = "current_longitude";
        String COMPANY_LATITUDE = "company_latitude";
        String COMPANY_LONGITUDE = "company_longitude";
        String FILTER_START_DATE = "start_date";
        String FILTER_END_DATE = "end_date";
        String DUAL_USER_KEY = "dual_user_key";
        String CURRENCY_ID = "currency_id";
        String START_TIME = "start_time";
        String END_TIME = "end_time";
        String USER_TYPE = "user_type";
        String CANCELLED = "cancelled";
        String COMPLETED = "completed";
        String DISPATCHED = "dispatched";
        String PENDING = "pending";
        String PRODUCT_TYPE_ID = "product_type_id";
        String DATE_TIME = "date_time";
        String IS_PREORDER_SELECTED_FOR_MENU = "is_preorder_selected_for_menu";
        String IS_APP_MENU_ENABLED = "is_app_menu_enabled";
        String BUSINESS_API_VERSION = "version";
        String RULE_ID = "rule_id";
        String PREV_JOB_ID = "prev_job_id";

    }

    interface TransistionsKeys {
        String COUNTRY_CODE = "country_code";
        String CONTINENT_CODE = "continent_code";
        String IS_PHONE = "is_phone";
        String EMAIL_OR_PHONE = "email_or_phone";
        String ACTIVITY_EMAIL_TRANSITION = "activity_email_transition";
        String ACTIVITY_PHONE_TRANSITION = "activity_phone_transition";
    }

    interface GACategories {
        String SIGNIN_SUCCESS = "Signin Success";
        String SIGNIN_FAILURE = "Signin Failure";
        String SIGNUP_SUCCESS = "Signup Success";
        String SIGNUP_FAILURE = "Signup Failure";

        String ADD_QUANTITY = "Add Quantity";
        String REMOVE_QUANTITY = "Remove Quantity";

        String GO_TO_CHECKOUT = "Go to checkout";
        String GO_TO_PAYMENT = "Go to payment";

        String CATEGORY_CLICK = "category Click";
        String ADD_ADDRESS = "Add Address";

        String ORDER_CREATED_SUCCESS = "Order Created Success";
        String ORDER_CREATED_FAILURE = "Order Created Failure";

        String RESTAURANT_CLICK = "Restaurant click";
        String RESTAURANT_ORDER_ONLINE = "Restaurant detail order online";
        String SELECT_AVAILABLE_DATE = "Select available date";
    }

}