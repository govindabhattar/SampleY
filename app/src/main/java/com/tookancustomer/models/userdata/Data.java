package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Data implements Serializable {
    @SerializedName("storefront_user_id_array")
    @Expose
    private ArrayList<Integer> storefrontUserIdArray = new ArrayList<>();
    @SerializedName("user_rights")
    @Expose
    private List<UserRights> userRights = new ArrayList<>();
    @SerializedName("nlevel_enabled")
    @Expose
    private Integer adminCategoryEnabled = 0;
    @SerializedName("store_latitude")
    @Expose
    private String storeLatitude = "0.0";
    @SerializedName("store_longitude")
    @Expose
    private String storeLongitude = "0.0";
    @SerializedName("new_user_id")
    @Expose
    private Integer newUserId;
    @SerializedName("android_device_type")
    @Expose
    private Object androidDeviceType;
    @SerializedName("welcome_pop_up")
    @Expose
    private String welcomePopUp;
    @SerializedName("add_card_link")
    @Expose
    private String addCardLink = "";
    @SerializedName("signin_link")
    @Expose
    private String signinLink;
    @SerializedName("is_create_your_own_app")
    @Expose
    private Integer isCreateYourOwnApp = 0;
    @SerializedName("marketplace_reference_id")
    @Expose
    private String marketplaceReferenceId;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("credits")
    @Expose
    private String credits;
    @SerializedName("app_access_token")
    @Expose
    private String appAccessToken;
    @SerializedName("app_version")
    @Expose
    private AppVersion appVersion;
    @SerializedName("referral")
    @Expose
    private ReferralShareData referral;
    @SerializedName("vendor_details")
    @Expose
    private VendorDetails vendorDetails;
    @SerializedName("fav_locations")
    @Expose
    private List<FavLocation> favLocations = new ArrayList<FavLocation>();
    @SerializedName("formSettings")
    @Expose
    private List<Datum> formSettings = new ArrayList<Datum>();
    @SerializedName("store_name")
    @Expose
    private String storeName = "";
    @SerializedName("userOptions")
    @Expose
    private UserOptions userOptions;
    @SerializedName("deliveryOptions")
    @Expose
    private UserOptions deliveryOptions;
    @SerializedName("active_task")
    @Expose
    private Object activeTask;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<>();

    @SerializedName("signup_template_data")
    @Expose
    private ArrayList<SignupTemplateData> signupTemplateData = new ArrayList<>();
    @SerializedName("signup_template_name")
    @Expose
    private String signupTemplateName;


    @SerializedName("is_dummy_password")
    @Expose
    private int isDummyPassword = 0;
    @SerializedName("vendor_signup_info")
    @Expose
    private String vendorSignupInfo;
    @SerializedName("vendorPromos")
    @Expose
    private VendorPromos vendorPromos;
    @SerializedName("language_string")
    @Expose
    private Map<String, String> languageStrings;


    public Map<String, String> getLanguageStrings() {
        return languageStrings;
    }

    public void setLanguageStrings(Map<String, String> languageStrings) {
        this.languageStrings = languageStrings;
    }

    public boolean isDummyPassword() {
        if (isDummyPassword == 1)
            return true;
        return false;
    }

    public void setIsDummyPassword(int isDummyPassword) {
        this.isDummyPassword = isDummyPassword;
    }

    public Integer getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(Integer newUserId) {
        this.newUserId = newUserId;
    }

    public String getMarketplaceReferenceId() {
        return marketplaceReferenceId;
    }

    public void setMarketplaceReferenceId(String marketplaceReferenceId) {
        this.marketplaceReferenceId = marketplaceReferenceId;
    }

    public Object getAndroidDeviceType() {
        return androidDeviceType;
    }

    public void setAndroidDeviceType(Object androidDeviceType) {
        this.androidDeviceType = androidDeviceType;
    }

    public String getWelcomePopUp() {
        return welcomePopUp;
    }

    public void setWelcomePopUp(String welcomePopUp) {
        this.welcomePopUp = welcomePopUp;
    }


    public String getVendorSignupInfo() {
        return vendorSignupInfo;
    }

    public void setVendorSignupInfo(String vendorSignupInfo) {
        this.vendorSignupInfo = vendorSignupInfo;
    }

    public ArrayList<SignupTemplateData> getSignupTemplateData() {
        return signupTemplateData != null ? signupTemplateData : new ArrayList<SignupTemplateData>();
    }

    public void setSignupTemplateData(ArrayList<SignupTemplateData> signupTemplateData) {
        this.signupTemplateData = signupTemplateData;
    }

    public VendorPromos getVendorPromos() {
        return vendorPromos != null ? vendorPromos : new VendorPromos();
    }

    public void setVendorPromos(VendorPromos vendorPromos) {
        this.vendorPromos = vendorPromos;
    }


    public String getSignupTemplateName() {
        return signupTemplateName;
    }

    public void setSignupTemplateName(String signupTemplateName) {
        this.signupTemplateName = signupTemplateName;
    }

    /**
     * @return The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return The appAccessToken
     */
    public String getAppAccessToken() {
        return appAccessToken;
    }

    /**
     * @param appAccessToken The app_access_token
     */
    public void setAppAccessToken(String appAccessToken) {
        this.appAccessToken = appAccessToken;
    }

    /**
     * @return The vendorDetails
     */
    public VendorDetails getVendorDetails() {
        return vendorDetails;
    }

    /**
     * @param vendorDetails The vendor_details
     */
    public void setVendorDetails(VendorDetails vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    /**
     * @return The favLocations
     */
    public List<FavLocation> getFavLocations() {
        return favLocations;
    }

    /**
     * @param favLocations The fav_locations
     */
    public void setFavLocations(List<FavLocation> favLocations) {
        this.favLocations = favLocations;
    }

    /**
     * @return The appVersion
     */
    public AppVersion getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion The app_version
     */
    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return The formSettings
     */
    public List<Datum> getFormSettings() {
        return formSettings;
    }

    /**
     * @param formSettings The formSettings
     */
    public void setFormSettings(List<Datum> formSettings) {
        this.formSettings = formSettings;
    }

    /**
     * @return The userOptions
     */
    public UserOptions getUserOptions() {
        if (userOptions != null && userOptions.getItems() == null) {
            return null;
        }
        return userOptions;
    }

    /**
     * @param userOptions The userOptions
     */
    public void setUserOptions(UserOptions userOptions) {
        this.userOptions = userOptions;
    }

    public UserOptions getDeliveryOptions() {
        if (userOptions != null && userOptions.getItems() == null) {
            userOptions = null;
        }
        if (deliveryOptions != null && deliveryOptions.getItems() == null) {
            deliveryOptions = null;
        }
        return getFormSettings().get(0).getWorkFlow() == Constants.LayoutType.PICKUP_DELIVERY ? deliveryOptions : userOptions;
    }

    public void setDeliveryOptions(UserOptions deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public boolean hasTemplates() {
        return userOptions != null;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }


    public String getSigninLink() {
//        return signinLink != null ? signinLink : "https://www.google.co.in/";
        return signinLink != null ? signinLink : "";
    }

    public void setSigninLink(String signinLink) {
        this.signinLink = signinLink;
    }

    public Integer getIsCreateYourOwnApp() {
        return isCreateYourOwnApp != null ? isCreateYourOwnApp : 0;
    }

    public void setIsCreateYourOwnApp(Integer isCreateYourOwnApp) {
        this.isCreateYourOwnApp = isCreateYourOwnApp;
    }

    public Double getStoreLatitude() {
        try {
            if (storeLatitude != null && !storeLongitude.isEmpty()) {
                return Double.valueOf(storeLatitude);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void setStoreLatitude(String storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public Double getStoreLongitude() {
        try {
            if (storeLongitude != null && !storeLongitude.isEmpty()) {
                return Double.valueOf(storeLongitude);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void setStoreLongitude(String storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public String getStoreName() {
        return storeName != null ? Utils.capitaliseWords(storeName) : "";
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddCardLink() {
        return addCardLink != null ? addCardLink : "";
    }

    public void setAddCardLink(String addCardLink) {
        this.addCardLink = addCardLink;
    }

    public ReferralShareData getReferral() {
        return referral != null ? referral : new ReferralShareData();
    }

    public void setReferral(ReferralShareData referral) {
        this.referral = referral;
    }

    public Integer getAdminCategoryEnabled() {
        return adminCategoryEnabled;
    }

    public void setAdminCategoryEnabled(Integer adminCategoryEnabled) {
        this.adminCategoryEnabled = adminCategoryEnabled;
    }

    public ArrayList<Integer> getStorefrontUserIdArray() {
        return storefrontUserIdArray;
    }

    public void setStorefrontUserIdArray(ArrayList<Integer> storefrontUserIdArray) {
        this.storefrontUserIdArray = storefrontUserIdArray;
    }

    public List<UserRights> getUserRights() {
        return userRights;
    }

    public void setUserRights(List<UserRights> userRights) {
        this.userRights = userRights;
    }

}
