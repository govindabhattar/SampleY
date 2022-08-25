package com.tookancustomer.appdata;

/**
 * Class to store REQUEST_CODE(s) for various transition events
 *
 * @author Rishabh
 */
public interface Codes {

    /**
     * Status Codes of the Response sent by the
     * Server. These status codes will be used
     * for segregation of the Actions to be
     * performed after getting .
     */
    enum StatusCode {

        // No Error
        NONE(0),

        // Server Errors
        ACTION_COMPLETE(200),
        USER_NOT_REGISTERED(105),
        SHOW_ERROR_MESSAGE(201),
        SHOW_REQUIRED_CATALOUGE(202),
        INVALID_ACCESS_TOKEN(101),
        PICK_UP_INCOMPLETE(410),
        BILLING_PLAN_EXPIRED(401),
        FB_NOT_REGISTERED(405),
        INSTAGRAM_NOT_REGISTERED(405),
        GOOGLE_NOT_REGISTERED(405),
        PARAMETER_MISSING(100),
        TASK_DELETED(501),
        AVAIBALITY_STATUS_CHANGED(210),

        // Retrofit Errors
        REQUEST_ERROR(400),
        EXECUTION_ERROR(404),
        NETWORK_ERROR(411),
        PARSING_ERROR(413),
        NO_DATA_FOUND(400),
        ALREADY_REGISTERED_AS_GUEST(800);



        private final int statusCode;

        /**
         * Constructor
         *
         * @param statusCode
         */
        StatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        /**
         * @param statusCode
         * @return
         */
        public static StatusCode get(int statusCode) {
            StatusCode status = NONE;

            for (StatusCode value : values()) {
                if (value.getStatusCode() == statusCode) {
                    status = value;
                    break;
                }
            }

            return status;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }


    /**
     * Stores all the Codes that differentiate the Permissions
     */
    interface Permission {
        int LOCATION = 1;
        int CAMERA = 2;
        int READ_FILE = 3;
        int OPEN_GALLERY = 4;
        int SAVE_BITMAP = 5;
        int READ_PHONE_STATE = 6;
        int WRITE_STORAGE = 7;
    }

    /**
     * Image Selection codes are used for segregating
     * the events according to the resources.
     * <p/>
     * In series of 300
     */
    interface ImageSelection {
        int CAPTURE_FROM_CAMERA = 301;
        int PICK_FROM_GALLERY = 302;
    }

    /**
     * Request codes are used for segregating the various
     * requests used to perform actions and getting the
     * results back through that request code.
     * <p/>
     * In series of 500
     */
    interface Request {
        int RESULT_PAYMENT_ERROR = 200;  /*Whenever transaction failed in case of payment gateway either add card or transaction in process*/
        int RESULT_ERROR = 201;
        int WISHLIST_REQUEST_CODE = 1547;



        //used by custom fiels
        int OPEN_STORAGE_DOCUMENT = 499;

        int UPDATE_APP_FROM_PLAY_STORE = 500;
        int PLAY_SERVICES_ERROR = 501;
        int LOCATION_ACCESS_REQUEST = 502;
        int OPEN_DEVELOPER_SETTINGS = 503;

        int OPEN_CAMERA_ACTIVITY = 505;

        int OPEN_TASK_ACTIVITY = 507;
        int OPEN_SETTINGS_ACTIVITY = 508;
        int OPEN_TUTORIALS_ACTIVITY = 509;
        int OPEN_NOTIFICATION_TONES_ACTIVITY = 510;

        int RESTART_LOCATION_SERVICE = 511;

        // Used by CustomFieldImages
        int OPEN_CAMERA_CUSTOM_FIELD_IMAGE = 512;
        int OPEN_GALLERY_CUSTOM_FIELD_IMAGE = 513;


        // Used by AddImagesFragment
        int OPEN_CAMERA_ADD_IMAGE = 514;
        int OPEN_GALLERY_ADD_IMAGE = 515;
        int OPEN_SCAN_CARD_SCREEN = 516;
        int OPEN_CHECKLIST_ACTIVITY = 517;
        int OPEN_FILTER_ACTIVITY = 518;
        int OPEN_ADD_TASK_ACTIVITY = 519;
        int OPEN_GET_ADDRESS_ACTIVITY = 520;
        int OPEN_SUBTASK_ACTIVITY = 521;
        int OPEN_NAVIGATION_ACTIVITY = 522;

        //Barcodes
        int OPEN_SCANNER_ADD_BARCODE = 523;
        int OPEN_SCANNER_EDIT_BARCODE = 524;
        int OPEN_SCANNER_CUSTOMFIELD_BARCODE = 525;
        int OPEN_VEHICLE_INFO_ACTIVITY = 526;
        int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 527;
        int OPEN_EDIT_AVAILABILITY_ACTIVITY = 528;
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 531;
        int OPEN_TASK_DETAIL_ACTIVITY = 532;
        int OPEN_MAKE_PAYMENT_ACTIVITY = 534;
        int OPEN_PROFILE_ACTIVITY = 537;
        int OPEN_LOCATION_ACTIVITY = 538;
        int OPEN_LOCATION_ACTIVITY_FROM_DELIVERY_POPUP = 1004;
        int ADD_FROM_MAP_ACTIVITY = 539;
        int OPEN_CHECKOUT_SCREEN = 540;
        int OPEN_CONFIRM_ADDRESS = 541;
        int OPEN_WEBVIEW_ACTIVITY = 542;
        int OPEN_SIGN_UP_FROM_DEMO_ACTIVITY = 543;
        int OPEN_CUSTOMISATION_ACTIVITY = 544;
        int OPEN_NLEVEL_ACTIVITY_AGAIN = 546;
        int OPEN_HOME_ACTIVITY = 547;
        int OPEN_SCHEDULE_TIME_ACTIVITY = 549;
        int OPEN_LOGIN_BEFORE_CHECKOUT = 550;
        int OPEN_SEARCH_PRODUCT_ACTIVITY = 551;
        int OPEN_SIGN_UP_CUSTOM_FIELD_ACTIVITY = 552;
        int OPEN_ADD_MERCHANT_RATE_REVIEW = 553;
        int OPEN_ALL_MERCHANT_RATE_REVIEW = 554;
        int OPEN_OTP_SCREEN = 555;
        int OPEN_ONBOARDING_SCREEN = 556;
        int OPEN_PRODUCT_DETAILS_SCREEN = 557;
        int OPEN_SHOW_PRODUCT_ON_MAP_SCREEN = 558;
        int OPEN_CALENDER_SCREEN = 559;
        int OPEN_FILTER_SCREEN = 560;
        int OPEN_ADMIN_CATEGORY_ACTIVITY = 561;
        int OPEN_MERCHANTS_LISTING = 562;
        int OPEN_DATES_ON_CALENDAR_SCREEN = 563;
        int OPEN_PAYFORT_WEBVIEW_ACTIVITY = 564;
        int OPEN_RAZORPAY_WEBVIEW_ACTIVITY = 565;
        int OPEN_OTP_FOR_PAYTM = 566;
        int OPEN_PAYTM_ADD_MONEY_WEBVIEW_ACTIVITY = 567;
        int OPEN_BILLPLZ_WEBVIEW_ACTIVITY = 568;
        int OPEN_CUSTOM_CHECKOUT_ACTIVITY = 569;
        int OPEN_PAYPAL_WEBVIEW_ACTIVITY = 570;

        int OPEN_FAC_WEBVIEW_ACTIVITY = 571;// FAC
        int OPEN_INSTAPAY_WEBVIEW_ACTIVITY = 572;
        int OPEN_PAYFAST_WEBVIEW_ACTIVITY = 573;// PAYFAST

        int OPEN_PAYULATAM_WEBVIEW_ACTIVITY = 574;

        int OPEN_WALLET_ADD_MONEY_ACTIVITY = 575;

        int OPEN_GIFT_CARD_ACTIVITY = 576;
        int OPEN_GIFT_CARD_PAYMENT_ACTIVITY = 577;
        int SUBSCRIPTION_ACTIVITY = 578;
        int ALL_SUBSCRIPTION_ACTIVITY = 579;

        int OPEN_MERCHANT_CATALOG_ACTIVITY = 578;

        int OPEN_ADD_PAYMENT_CARD_ACTIVITY = 579;

        int OPEN_PROCESSING_PAYMENT_ACTIVITY = 580;

        int OPEN_REPAYMENT_ACTIVITY = 581;
        int OPEN_TASKDETAILS_ACTIVITY = 582;
        int MANDATORY_CATEGORY_ACTIVITY = 583;

        int OPEN_PAYMENT_ACTIVITY_USER_DEBT = 584;

        int OPEN_STATIC_ADDRESS_ACTIVITY = 585;
        int OPEN_RESET_PASSWORD = 586;
        int OPEN_QUESTIONNAIRE_ACTIVITY = 586;
        int OPEN_LOGIN_OTP_ACTIVITY = 587;
        int OPEN_AGENT_LIST_ACTIVITY = 588;
        int OPEN_ACTIVITY_USER_SUBSCRIPTION = 589;
        int OPEN_POPUP = 590;
        int OFTEN_VIEW_MORE_ACTIVITY = 591;
        int OPEN_PAYTM_SDK = 591;



    }

    /**
     * Purpose Codes to init the Message Dialogs and perform
     * the actions accordingly
     * <p/>
     * In series of 600
     */
    interface Purpose {

        int CHECK_INTERNET = 601;
        int DISABLE_MOCK_LOCATIONS = 602;

        int ASK_UPDATE = 603;
        int FORCE_UPDATE = 604;

        int CHECK_PASSWORD = 605;
        int ASK_USER_CHECK_EMAIL = 606;

        int PROFILE_FIELDS_EMPTY = 607;
        int PROFILE_CONTACT_INVALID = 608;
        int ASK_USER_DISCARD_CHANGES = 609;
        int NOTE_EXISTS = 610;
    }

    /**
     * Notifications Codes are used to segregate the Notification
     * messages and perform actions accordingly.
     * <p/>
     * In series of 700
     */
    interface Notification {

        int TASK_ASSIGNED = 701;
        int TASK_RESCHEDULED = 702;
        int TASK_DELETED = 703;
    }

    /**
     * Class to accumulate all the errors on a single place
     * <p/>
     * In series of 800
     */
    interface Error {

        int NO_FLEET_INFO = 801;
        int JSON_PARSING = 802;
        int NO_ACCESS_TOKEN = 803;
        int PRE_REQUISITES_ERR = 804;
        int RE_LOGIN = 805;
    }

    interface ResultCodes {

        int RESULT_CANCELED_OTP = 10;

    }

    interface PaytmStatus {
        String SUCCESS = "TXN_SUCCESS";
        String FAILED = "TXN_FAILURE";
        String PENDING = "PENDING";
    }

}
