package com.tookancustomer.retrofit2;

import com.tookancustomer.mapfiles.placeapi.AutoComplete;
import com.tookancustomer.mapfiles.placeapi.PlaceDetails;
import com.tookancustomer.models.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * ApiInterface
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiInventory.MERCHANT_WISHLIST)
    Call<BaseModel> merchantWishlist(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CANCEL_CUSTOMER_SUBSCRIPTION)
    Call<BaseModel> cancelSubscriptionPlan(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.FETCH_APP_CONFIGURATION)
    Call<BaseModel> fetchAppConfig(@FieldMap Map<String, String> map);

    @GET(ApiInventory.GET_COUNTRY_CODE)
    Call<BaseModel> getCountryCode();

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_LOGOUT)
    Call<BaseModel> vendorLogout(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_LOGIN_VIA_ACCESS_TOKEN)
    Call<BaseModel> loginViaAccessToken(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.USER_EXIST)
    Call<BaseModel> userExist(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_SIGNUP)
    Call<BaseModel> signup(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_SIGNUP)
    Call<BaseModel> dualUserSignUp(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_LOGIN)
    Call<BaseModel> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_LOGIN)
    Call<BaseModel> loginDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SOCIAL_LOGIN)
    Call<BaseModel> socialLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_INSTAGRAM_LOGIN)
    Call<BaseModel> instagramLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_GOOGLE_LOGIN)
    Call<BaseModel> googleLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_SOCIAL_LOGIN)
    Call<BaseModel> socialLoginDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_INSTAGRAM_LOGIN)
    Call<BaseModel> instagramLoginDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_GOOGLE_LOGIN)
    Call<BaseModel> googleLoginDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SOCIAL_REGISTER)
    Call<BaseModel> socialRegister(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_INSTAGRAM_REGISTER)
    Call<BaseModel> instagramRegister(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_GOOGLE_REGISTER)
    Call<BaseModel> googleRegister(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_SOCIAL_REGISTER)
    Call<BaseModel> socialRegisterDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_INSTAGRAM_REGISTER)
    Call<BaseModel> instagramRegisterDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_GOOGLE_REGISTER)
    Call<BaseModel> googleRegisterDualUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VERIFY_OTP)
    Call<BaseModel> verifyOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VERIFY_OTP_LOGIN)
    Call<BaseModel> verifyOTPLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SEND_LOGIN_OTP)
    Call<BaseModel> sendLoginOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VERIFY_OTP_FOR_FORGOT_PASSWORD)
    Call<BaseModel> verifyOTPForForgotPassword(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_VERIFY_OTP)
    Call<BaseModel> dualUserVerifyOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.RESEND_OTP)
    Call<BaseModel> resendOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_RESEND_OTP)
    Call<BaseModel> dualUserResendOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CHANGE_NUMBER)
    Call<BaseModel> changePhoneNumber(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CHANGE_EMAIL_VERIFICATION)
    Call<BaseModel> changeEmailForVerification(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.RESEND_EMAIL_VERIFICATION_LINK)
    Call<BaseModel> resendEmailVerificationLink(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_FORGOT_PASSWORD)
    Call<BaseModel> forgotPassword(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CUSTOMER_RESET_PASSWORD)
    Call<BaseModel> resetPassword(@FieldMap Map<String, String> map);

    @Multipart
    @POST(ApiInventory.VENDOR_UPDATE_PROFILE)
    Call<BaseModel> updateProfile(@PartMap HashMap<String, RequestBody> dataMap);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_UPDATE_PROFILE)
    Call<BaseModel> updateProfileCustomFields(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_CHANGE_PASSWORD)
    Call<BaseModel> changePassword(@FieldMap Map<String, String> map);

    @Multipart
    @POST(ApiInventory.UPLOAD_REFERENCE_IMAGES)
    Call<BaseModel> getImageUrl(@PartMap HashMap<String, RequestBody> dataMap);

    @FormUrlEncoded
    @POST(ApiInventory.GET_FAV_LOCATIONS)
    Call<BaseModel> getFavLocations(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.ADD_FAV_LOCATION)
    Call<BaseModel> addFavLocation(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.EDIT_FAV_LOCATION)
    Call<BaseModel> editFavLocation(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DELETE_FAV_LOCATIONS)
    Call<BaseModel> deleteFavLocation(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.FETCH_MERCHANT_CARDS)
    Call<BaseModel> fetchMerchantCards(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DELETE_MERCHANT_CARDS)
    Call<BaseModel> deleteMerchantCards(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DELETE_MERCHANT_CARDS_AUTHORISE_DOT_NET)
    Call<BaseModel> deleteMerchantCards_AuthorizeDotNet(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DELETE_MERCHANT_CARDS_FAC)
    Call<BaseModel> deleteMerchantCards_FAC(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.LAUNDRY_GET_STOREFRONT_TIMESLOTS)
    Call<BaseModel> getLaundryStorefrontTimeSlots(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_STOREFRONT_TIMESLOTS_V2)
    Call<BaseModel> getStorefrontTimeSlots(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_TIMESLOTS)
    Call<BaseModel> getProductTimeSlots(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_TIMERANGE)
    Call<BaseModel> getProductTimeRange(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_APP_NOTIFICATIONS)
    Call<BaseModel> getAppNotifications(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.UPDATE_APP_NOTIFICATIONS)
    Call<BaseModel> updateAppNotifications(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VENDOR_TASK_HISTORY)
    Call<BaseModel> getAllTasks(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_ORDER_DETAILS)
    Call<BaseModel> getJobDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_TRACKING_DETAILS)
    Call<BaseModel> getTrackingDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_CANCELLATION_REASON)
    Call<BaseModel> getCancellationReason(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CANCEL_ORDER)
    Call<BaseModel> cancelOrder(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SEND_PAYMENT_FOR_TASK)
    Call<BaseModel> sendPaymentForTask(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.VALIDATE_SERVING_DISTANCE)
    Call<BaseModel> validateServingDistance(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_TASK_VIA_VENDOR)
    Call<BaseModel> createTaskViaVendor(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(ApiInventory.CREATE_TASK_LAUNDRY_CUSTOM)
    Call<BaseModel> createTaskLaundryCustom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_TASK_LAUNDRY)
    Call<BaseModel> createTaskLaundry(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_TASK_VIA_VENDOR_CUSTOM)
    Call<BaseModel> createTaskViaVendorCustom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_APP_CATALOGUE)
    Call<BaseModel> getAppCatalogue(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCTS_FOR_CATEGORY)
    Call<BaseModel> getProductCatalogue(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SEARCH_PRODUCTS)
    Call<BaseModel> searchProducts(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SEARCH_PRODUCTS_V2)
    Call<BaseModel> searchProductsV2(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SEARCH_MERCHANT)
    Call<BaseModel> searchMerchant(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SEARCH_PRODUCT_GLOBAL)
    Call<BaseModel> searchProductGlobal(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.BILL_BREAKDOWN)
    Call<BaseModel> getBillBreakDown(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SUBMIT_VENDER_SIGNUP_TEMPLATE)
    Call<BaseModel> submitVenderSignupTemplate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_DUMMY_USER_DETAILS_FOR_DEMO)
    Call<BaseModel> getDummyUserDetailsForDemo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.MARKETPLACE_GET_PRODUCTS)
    Call<BaseModel> getMarketplaceProducts(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.MARKETPLACE_GET_STOREFRONTS)
    Call<BaseModel> getMarketplaceStorefronts(@FieldMap Map<String, String> map, @Field("filters") JSONObject jsonObject);

    @FormUrlEncoded
    @POST(ApiInventory.SINGLE_MARKETPLACE_GET_STOREFRONTS)
    Call<BaseModel> getSingleMarketplaceStorefronts(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_AGENTS)
    Call<BaseModel> getAgents(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_CUSTOMER_ORDER_REVIEW)
    Call<BaseModel> createCustomerOrderReview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SKIP_ORDER_REVIEW)
    Call<BaseModel> skipOrderReview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_STOREFRONT_REVIEW)
    Call<BaseModel> createStorefrontReview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_STORE_ALL_REVIEWS)
    Call<BaseModel> getStoreAllReviews(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SET_CUSTOMER_USER_RIGHTS)
    Call<BaseModel> setCustomerUserRights(@FieldMap Map<String, String> map);

//    @FormUrlEncoded
//    @POST(ApiInventory.PRODUCT_VIEW)
//    Call<BaseModel> getProductDetail(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.PRODUCT_FILTERS)
    Call<BaseModel> getProductFilters(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.LANGUAGE_CHANGE)
    Call<BaseModel> languageChange(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_ADMIN_MERCHANT_CATALOG)
    Call<BaseModel> getAdminMerchantCatalog(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_ADMIN_MERCHANT_LIST)
    Call<BaseModel> getAdminMerchantList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_VIEW_MARKETPLACE_PROFILE)
    Call<BaseModel> viewOwnerProfile(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.PRODUCT_GET_AVAILABLE_DATES)
    Call<BaseModel> getAvailableDates(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.ORDER_CHECK_TIME_SLOTS)
    Call<BaseModel> checkTimeSlots(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.DUAL_USER_TOGGLE)
    Call<BaseModel> dualUserToggle(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_WITH_SELLERS)
    Call<BaseModel> getProductWithSeller(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_REVIEWS)
    Call<BaseModel> getProductReviews(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_PRODUCT_REVIEW)
    Call<BaseModel> createProductReview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_LAST_REVIEWS)
    Call<BaseModel> getProductLastReviews(@FieldMap Map<String, String> map);

    @GET(ApiInventory.INITIATE_PAYFORT_PAYMENT)
    Call<BaseModel> initatePayfortPayment(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.INITIATE_PAYMENT)
    Call<BaseModel> initatePayment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_BILLPLZ_CHARGE)
    Call<BaseModel> getBillPlzCharge(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_ORDERS)
    Call<BaseModel> getRequests(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_TYPE)
    Call<BaseModel> getProductType(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.ACCEPT_REJECT)
    Call<BaseModel> acceptRejectOrder(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(ApiInventory.CREATE_REQUEST)
    Call<BaseModel> createRequest(@FieldMap Map<String, String> map, @Field("multi_image_url") JSONArray array);

    @FormUrlEncoded
    @POST(ApiInventory.CATEGORY_LISTING)
    Call<BaseModel> getCategoryListing(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.RAZORPAY_PAYMENT)
    Call<BaseModel> razorpayPayment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.FAC_PAYMENT)
    Call<BaseModel> FACPayment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.PAYTM_REQUEST_OTP)
    Call<BaseModel> paytmRequestOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.PAYTM_LOGIN_WITH_OTP)
    Call<BaseModel> paytmLoginWithOTP(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.PAYTM_CHECK_BALANCE)
    Call<BaseModel> paytmCheckBalance(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_TERMS_AND_CONDITION)
    Call<BaseModel> getTermsAndCondition(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_BUSINESS_CATEGORIES)
    Call<BaseModel> getBusinessCategories(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_BANNERS)
    Call<BaseModel> getBanners(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_STATIC_ADDRESSES)
    Call<BaseModel> getStaticAddresses(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_FILTERS)
    Call<BaseModel> getFilters(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_ONGOING_ORDERS)
    Call<BaseModel> getOnGoingOrders(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_CHECKOUT_TEMPLATE)
    Call<BaseModel> getCheckoutTemplate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PRODUCT_TEMPLATE)
    Call<BaseModel> getProductTemplate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_CHECKOUT_TEMPLATE_STATUS)
    Call<BaseModel> getCheckoutTemplateStatus(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_SUBCATEGORY)
    Call<BaseModel> getSubcategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_USER_PAGES)
    Call<BaseModel> getUserPages(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.UPDATE_VENDOR_SUBSCRIPTION)
    Call<BaseModel> updateVendorSubscription(@FieldMap Map<String, String> map);


    @GET(ApiInventory.GOOGLE_AUTOCOMPLETE_API)
    Call<AutoComplete> getAddressFromGooglePlaceApi(@QueryMap Map<String, String> map);

    @GET(ApiInventory.FLIGHTMAP_PLACESEARCH_API)
    Call<AutoComplete> getAddressFromFlightMapSearchApi(@QueryMap Map<String, String> map);

    @GET(ApiInventory.JUNGLE_PLACE_GEOCODE)
    Call<PlaceDetails> getPlaceDetailsFlightMapFromPlaceID(@QueryMap Map<String, String> map);

    @GET(ApiInventory.GOOGLE_PLACE_DETAILS_API)
    Call<PlaceDetails> getPlaceDetailsFromPlacexxID(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_LOYALTY_POINTS)
    Call<BaseModel> getLoyaltyPoints(@FieldMap Map<String, String> map);


//    @FormUrlEncoded
//    @POST(ApiInventory.PAYPAL_PAYMENT)
//    Call<BaseModel> initiatePaypal(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CANCELLATION_POLICY_DETAILS_BY_JOB_ID)
    Call<BaseModel> getCancellationPolicy(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_CANCELLATION_CHARGES)
    Call<BaseModel> getCancellationCharges(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PAYU_LATAM_DATA)
    Call<BaseModel> initiatePayulatam(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.INITIATE_INSTAPAY_PAYMENT)
    Call<BaseModel> InstaPayPayment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.INITIATE_PAYFAST_PAYMENT)
    Call<BaseModel> PayfastPayment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_WALLET_TXN_HISTORY)
    Call<BaseModel> getWalletTxnHistory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PAYMENT_URL)
    Call<BaseModel> getPaymentUrl(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.ADD_WALLET_MONEY)
    Call<BaseModel> addWalletMoney(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_GIFT_CARD_TXN_HISTORY)
    Call<BaseModel> getGiftCardTxnHistory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.REDEEM_GIFT_CARD)
    Call<BaseModel> redeemGiftCard(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.CREATE_CHARGE)
    Call<BaseModel> createCharge(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_MERCHANT_CATEGORIES)
    Call<BaseModel> getMerchantCategories(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.TOOKAN_RATE_AGENT)
    Call<BaseModel> rateAgent(@FieldMap Map<String, String> map);

    @GET
    Call<BaseModel> fetchIp(@Url String url);

    @FormUrlEncoded
    @POST(ApiInventory.GET_REWARD_PLANS)
    Call<BaseModel> getRewardPlans(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.BUY_REWARD_PLANS)
    Call<BaseModel> buyRewardPlan(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.RECURRING_SLOTS)
    Call<BaseModel> getRecurringSlots(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SAVE_RECURRING_TASK)
    Call<BaseModel> saveRecurringTask(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_RECURRING_TASK)
    Call<BaseModel> getRecurringTask(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_RECURRING_DETAILS)
    Call<BaseModel> getRecurringDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.UPDATE_RECURRING_RULE)
    Call<BaseModel> updateRecurringRule(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.ADD_VACATION_RULE)
    Call<BaseModel> addVacationRule(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_VACATION_RULE)
    Call<BaseModel> getVacationRule(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_FILTERED_VACATION_RULE)
    Call<BaseModel> getFilteredVacationRule(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_RECURRING_JOB_HISTORY)
    Call<BaseModel> getRecurringJobHistory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.ERROR_LOG)
    Call<BaseModel> errorLog(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_DEBT_LIST)
    Call<BaseModel> getDebtList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_PAYMENT_METHODS)
    Call<BaseModel> getPaymentMethods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_ADDITIONAL_PAYMENT_STATUS)
    Call<BaseModel> getAdditionalPaymentStatus(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_SUBSCRIPTION_PLAN)
    Call<BaseModel> getSubscriptionPlans(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.GET_RECURRING_SURGE_DETAILS)
    Call<BaseModel> getRecurringSurgeDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @PUT(ApiInventory.UPDATE_ORDER)
    Call<BaseModel> updateOrder(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.JOB_EXPIRED)
    Call<BaseModel> setJobExpired(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SOCHITEL_GET_KEY)
    Call<BaseModel> getSochitelKey(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SOCHITEL_ADD_KEY)
    Call<BaseModel> addSochitelKey(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SOCHITEL_UPDATE_KEY)
    Call<BaseModel> updateSochitelKey(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.SOCHITEL_GET_OPERATORS)
    Call<BaseModel> getSochitelOperators(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.PAYTMTID)
    Call<BaseModel> sendTransactionIdPaytm(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiInventory.RAZORPAY_PAYMENT_STATUS)
    Call<BaseModel> razorpayPaymentStatus(@FieldMap Map<String, String> map);


}