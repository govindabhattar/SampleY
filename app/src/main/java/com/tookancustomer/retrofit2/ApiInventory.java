package com.tookancustomer.retrofit2;

public interface ApiInventory {
    // Apis used by App
    String MERCHANT_WISHLIST = "/merchant/addToWishlist";

    String FETCH_APP_CONFIGURATION = "/marketplace_fetch_app_configuration";

    String VENDOR_LOGIN_VIA_ACCESS_TOKEN = "/marketplace_vendor_login_via_access_token";

    String USER_EXIST = "/marketplace_vendor_email_exist";

    String VENDOR_SIGNUP = "/marketplace_vendor_signup";

    String DUAL_USER_SIGNUP = "/dualusersignup";

    String VENDOR_LOGIN = "/marketplace_vendor_login";

    String DUAL_USER_LOGIN = "/dualuserlogin";

    String GET_DUMMY_USER_DETAILS_FOR_DEMO = "marketplace_get_app_config";

    String VENDOR_FORGOT_PASSWORD = "/customer/forgotPassword"; //"/marketplace_vendor_forgot_password";
    String CUSTOMER_RESET_PASSWORD = "/customer/resetPassword";

    String VENDOR_CHANGE_PASSWORD = "/marketplace_change_vendor_password";

    String GET_APP_CATALOGUE = "/get_app_catalogue";

    String GET_PRODUCTS_FOR_CATEGORY = "/get_products_for_category";

    String CREATE_TASK_VIA_VENDOR_CUSTOM = "/task/customOrder";

    String CREATE_TASK_VIA_VENDOR = "/create_task_via_vendor_v2";
    String CREATE_TASK_LAUNDRY = "/laundry/createTask";

    String VENDOR_TASK_HISTORY = "/get_service_job_history";

    String GET_ORDER_DETAILS = "/get_service_job_history";
    String GET_CANCELLATION_REASON = "/marketplace/getCancellationReason";
    String CANCEL_ORDER = "/cancel_order";
    String GET_TRACKING_DETAILS = "/task/getTrackingDetails";

    //    String GET_COUNTRY_CODE = "/json/";
    String GET_COUNTRY_CODE = "/requestCountryCodeGeoIP2";

    String VENDOR_LOGOUT = "/vendor_logout";
    String SOCIAL_LOGIN = "/marketplace_vendor_facebook_login";
    String DUAL_USER_SOCIAL_LOGIN = "/dualUserFacebookLogin";

    String SOCIAL_REGISTER = "/marketplace_vendor_facebook_register";

    String DUAL_USER_SOCIAL_REGISTER = "/dualUserFacebookSignup";

    String VERIFY_OTP = "/marketplace_vendor_verify_otp";
    String VERIFY_OTP_LOGIN = "customer/verifyOtp";
    String DUAL_USER_VERIFY_OTP = "/dualUser_otp_verification";
    String VERIFY_OTP_FOR_FORGOT_PASSWORD = "/customer/verifyOtp";

    String RESEND_OTP = "/marketplace_vendor_resend_otp";
    String DUAL_USER_RESEND_OTP = "/dualUser_resend_otp";

    String CHANGE_EMAIL_VERIFICATION = "/vendor/changeEmail";
    String RESEND_EMAIL_VERIFICATION_LINK = "/vendor/resendEmail";

    String CHANGE_NUMBER = "/marketplace_vendor_change_phone";
    String SUBMIT_VENDER_SIGNUP_TEMPLATE = "/submit_signup_template";

    String VENDOR_UPDATE_PROFILE = "/edit_vendor_profile";
    String UPLOAD_REFERENCE_IMAGES = "/upload_images";

    String GET_FAV_LOCATIONS = "/get_fav_location";
    String ADD_FAV_LOCATION = "/add_fav_location";
    String EDIT_FAV_LOCATION = "/edit_fav_location";
    String DELETE_FAV_LOCATIONS = "/delete_fav_location";

    String FETCH_MERCHANT_CARDS = "/get_customer_cards";
    String DELETE_MERCHANT_CARDS = "/delete_customer_card";
    String DELETE_MERCHANT_CARDS_AUTHORISE_DOT_NET = "/authorizeNet/deleteCard ";
    String DELETE_MERCHANT_CARDS_FAC = "/facPayment/deleteCreditCards";

    String SEND_PAYMENT_FOR_TASK = "/send_payment_for_task_v2";
    String VALIDATE_SERVING_DISTANCE = "/order/serviceableCheck";
    String BILL_BREAKDOWN = "/get_bill_breakdown";

    String GET_APP_NOTIFICATIONS = "/get_app_notifications";
    String UPDATE_APP_NOTIFICATIONS = "/update_app_notifications";

    //    String MARKETPLACE_GET_PRODUCTS = "/marketplace_get_products";
    String MARKETPLACE_GET_PRODUCTS = "/product/getAll";

    String DUAL_USER_VIEW_MARKETPLACE_PROFILE = "/merchant/viewProfile";

    //    String MARKETPLACE_GET_STOREFRONTS = "/marketplace_get_city_storefronts_v2";
    String MARKETPLACE_GET_STOREFRONTS = "/marketplace/marketplace_get_city_storefronts_v3";
    String SINGLE_MARKETPLACE_GET_STOREFRONTS = "/marketplace_get_city_storefronts_single_v2";

    String GET_STOREFRONT_TIMESLOTS = "/get_storefront_timeslots";
    String GET_PRODUCT_TIMESLOTS = "/get_product_timeslots";
    String GET_PRODUCT_TIMERANGE = "/v3/merchant/timeslots";

    String SEARCH_PRODUCTS = "/search_products";
    String SEARCH_PRODUCTS_V2 = "/product/search";
    String SEARCH_MERCHANT = "/search/global/merchants";
    String SEARCH_PRODUCT_GLOBAL = "/search/global/product";

    String CREATE_CUSTOMER_ORDER_REVIEW = "/create_customer_order_review";
    String SKIP_ORDER_REVIEW = "/skip_order_review";
    String GET_STORE_ALL_REVIEWS = "/get_store_all_reviews";
    String CREATE_STOREFRONT_REVIEW = "/create_storefront_review";
    String SET_CUSTOMER_USER_RIGHTS = "/set_customer_user_rights";
    //    String PRODUCT_VIEW = "/product/view";
    String PRODUCT_FILTERS = "/product/getFilters";

    String LANGUAGE_CHANGE = "/language/editVendor";
    String LAUNDRY_GET_STOREFRONT_TIMESLOTS = "/laundry/getStorefrontTimeslots";
    String GET_STOREFRONT_TIMESLOTS_V2 = "timeSlot/getStorefrontTimeslotsV2";
    String GET_ADMIN_MERCHANT_CATALOG = "AdminCatalog/getMerchantCatalog";
    String GET_ADMIN_MERCHANT_LIST = "AdminCatalog/getMerchantList";
    String PRODUCT_GET_AVAILABLE_DATES = "/product/getAvailableDates";
    String ORDER_CHECK_TIME_SLOTS = "/order/checkTimeSlots";
    String DUAL_USER_TOGGLE = "/dualUserToggle";

    String GET_PRODUCT_WITH_SELLERS = "/AdminCatalog/getProductsWithSellers";
    String INITIATE_PAYFORT_PAYMENT = "/payFort/initiatePayfortPayment";
    String INITIATE_INSTAPAY_PAYMENT = "/innstapay/makePaymentUrl";
    String INITIATE_PAYFAST_PAYMENT = "/payFast/createPaymentUrl";
    String INITIATE_PAYMENT = "/initiate_payment";
    String GET_BILLPLZ_CHARGE = "/billplz/get_billplz_charge_status";

    String GET_ORDERS = "/marketplace_get_orders_by_state_v4";
    String GET_PRODUCT_TYPE = "productType/get";
    String GET_PRODUCT_LAST_REVIEWS = "/get_product_last_reviews";
    String CREATE_PRODUCT_REVIEW = "/create_product_review";
    String GET_PRODUCT_REVIEWS = "/get_product_reviews";

    String ACCEPT_REJECT = "accept_reject_order";
    String CREATE_REQUEST = "/add_products";
    String CATEGORY_LISTING = "products_for_category_dashboard";

    String RAZORPAY_PAYMENT = "/razorPay/razorpayPayment";
    String FAC_PAYMENT = "/facPayment/getAuthorizationToken";

    String PAYTM_REQUEST_OTP = "/paytm/request_otp";
    String PAYTM_LOGIN_WITH_OTP = "/paytm/login_with_otp";
    String PAYTM_CHECK_BALANCE = "/paytm/checkBalance";

    String GET_TERMS_AND_CONDITION = "/termAndCondition/getTermAndConditionOpen";

    String GET_BUSINESS_CATEGORIES = "/businessCategory/getCategory";
    String GET_BANNERS = "/banner/getBanner";

    String VENDOR_INSTAGRAM_REGISTER = "/vendor/instagramRegister";
    String VENDOR_INSTAGRAM_LOGIN = "/vendor/instagramLogin";
    String DUAL_USER_INSTAGRAM_LOGIN = "/dualUserInstagramLogin";
    String DUAL_USER_INSTAGRAM_REGISTER = "/dualUserInstagramSignup";

    String VENDOR_GOOGLE_REGISTER = "/vendor/googleRegister";
    String VENDOR_GOOGLE_LOGIN = "/vendor/googleLogin";
    String DUAL_USER_GOOGLE_REGISTER = "/dualUserGoogleSignup";
    String DUAL_USER_GOOGLE_LOGIN = "/dualUserGoogleLogin";


    String GET_STATIC_ADDRESSES = "/staticAddress/getStaticAddressForCustomer";
    String GET_FILTERS = "/vendor/getFilters";
    String GET_ONGOING_ORDERS = "task/getOngoingOrders";
    String GET_CHECKOUT_TEMPLATE = "/template/getCheckoutTemplate";
    String GET_PRODUCT_TEMPLATE = "/product/template/get";
    String GET_CHECKOUT_TEMPLATE_STATUS = "/template/checkTemplateStatus";


    String GET_SUBCATEGORY = "/merchant/getSubCategory";

    String GET_USER_PAGES = "/userPages/get";

    String GOOGLE_AUTOCOMPLETE_API = "/maps/api/place/autocomplete/json";
    String FLIGHTMAP_PLACESEARCH_API = "/api/search";
    String JUNGLE_PLACE_GEOCODE = "/api/geocode";

    String GOOGLE_PLACE_DETAILS_API = "/maps/api/place/details/json";

    String UPDATE_VENDOR_SUBSCRIPTION = "subscription/updateVendorSubscription";

    String GET_LOYALTY_POINTS = "/loyaltyPoints/fetchCriteria";
    String PAYPAL_PAYMENT = "/paypal/makePayment";
    String CANCELLATION_POLICY_DETAILS_BY_JOB_ID = "/cancelPolicyDetailsByJobId";
    String GET_CANCELLATION_CHARGES = "cancelPolicy/getCancellationCharges";
    String GET_PAYU_LATAM_DATA = "/payULatam/getPaymentData";

    String CREATE_TASK_LAUNDRY_CUSTOM = "/laundry/createTaskForCustomOrder";

    String GET_WALLET_TXN_HISTORY = "/vendor/getWalletTxnHistory";
    String ADD_WALLET_MONEY = "/payment/createCharge";
    String GET_PAYMENT_URL = "/payment/getPaymentUrl";

    String GET_GIFT_CARD_TXN_HISTORY = "/giftCard/getGiftCardTxnHistory";
    String REDEEM_GIFT_CARD = "giftCard/vendorRedeemGiftCard";
    String CREATE_CHARGE = "/payment/createCharge";

    String GET_MERCHANT_CATEGORIES = "/catalogue/get";
    String TOOKAN_RATE_AGENT = "tookan/rateAgent";
    String GET_REWARD_PLANS = "/reward/customerPlans";
    String BUY_REWARD_PLANS = "/reward/selectPlanByCustomer";
    String GET_DEBT_LIST = "/customer/getDebtList";

    String ERROR_LOG = "/error/log";


    String RECURRING_SLOTS = "/recurring/getRecurringSlots";
    String SAVE_RECURRING_TASK = "/recurring/saveRecurringTask";

    String GET_RECURRING_TASK = "/recurring/getRecurringRules";
    String GET_RECURRING_DETAILS = "recurring/getRuleDetails";
    String UPDATE_RECURRING_RULE = "/recurring/updateRecurringRule";
    String GET_VACATION_RULE = "/recurring/getVacationRule";
    String ADD_VACATION_RULE = "/recurring/addVacationRule";
    String GET_FILTERED_VACATION_RULE = "/recurring/filteredVacationRule";
    String GET_RECURRING_JOB_HISTORY = "recurring/get_recurr_service_job_history";
    String GET_AGENTS = "product/getAgents";
    String SEND_LOGIN_OTP = "customer/send_login_otp";
    String GET_PAYMENT_METHODS = "payment/getActivePaymentMethods";
    String GET_SUBSCRIPTION_PLAN = "customerSubscription/get";
    String GET_RECURRING_SURGE_DETAILS = "recurring/surgeDetails";
    String GET_ADDITIONAL_PAYMENT_STATUS = "payment/getAdditionalPaymentStatus";
    String UPDATE_ORDER = "/task/updateOrder";
    String mTRACKING = "/mtracking.gif";
    String SUBMIT = "/form/submit";
    String JOB_EXPIRED = "/job/expired";


    String SOCHITEL_GET_OPERATORS = "/sochitel/get-operators";
    String SOCHITEL_UPDATE_KEY = "/sochitel/update_vendor_config";
    String SOCHITEL_GET_KEY = "/sochitel/get_vendor_config";
    String SOCHITEL_ADD_KEY = "/sochitel/add_vendor_config";
    String PAYTMTID = "paytmupi/transactionInfo";


    String CANCEL_CUSTOMER_SUBSCRIPTION = "/customerSubscription/editCustomerPlan";
    String RAZORPAY_PAYMENT_STATUS = "razorpay_upi/getPaymentStatus";
}
