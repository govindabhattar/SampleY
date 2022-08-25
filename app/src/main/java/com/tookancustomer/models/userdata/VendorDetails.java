package com.tookancustomer.models.userdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;
import com.tookancustomer.models.appConfiguration.Terminology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VendorDetails extends BaseModel implements Serializable {
    private String updatedPhoneNo = "";
    private String updatedEmail = "";

    @SerializedName("marketplace_user_id")
    @Expose
    private int marketplaceUserId;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("referrer_id")
    @Expose
    private Integer referrerId;
    @SerializedName("credits")
    @Expose
    private Number credits;
    @SerializedName("pending_amount")
    @Expose
    private double pendingAmount;
    @SerializedName("is_phone_verified")
    @Expose
    private int isPhoneVerified;
    @SerializedName("is_email_verified")
    @Expose
    private int isEmailVerified;
    @SerializedName("is_vendor_verified")
    @Expose
    private int isVendorVerified;
    @SerializedName("subscriptionPlan")
    @Expose
    private ArrayList<SubscriptionPlan> subscriptionPlan = new ArrayList<>();
    @SerializedName("last_payment_method")
    @Expose
    private long lastPaymentMethod = 0;
    @SerializedName("vendor_id")
    @Expose
    private int vendorId = 0;
    @SerializedName("dual_user_current_status")
    @Expose
    private int dualUserCurrentStatus = 0;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("password_reset_token")
    @Expose
    private Object passwordResetToken;
    @SerializedName("is_blocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("device_type")
    @Expose
    private Object deviceType;
    @SerializedName("appDeviceType")
    @Expose
    private Object appDeviceType;
    @SerializedName("registration_status")
    @Expose
    private Integer registrationStatus;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("app_versioncode")
    @Expose
    private String appVersioncode;
    @SerializedName("app_access_token")
    @Expose
    private String appAccessToken;
    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("average_rating")
    @Expose
    private Number averageRating = 0;

    @SerializedName("language_code")
    @Expose
    private String languageCode;
    @SerializedName("language_name")
    @Expose
    private String languageName;
    @SerializedName("language_display_name")
    @Expose
    private String languageDisplayName;
    @SerializedName("last_login_date_time")
    @Expose
    private String lastLoginDateTime;
    @SerializedName("creation_date_time")
    @Expose
    private String creationDateTime;
    @SerializedName("vendor_image")
    @Expose
    private String vendorImage;
    @SerializedName("vendor_api_key")
    @Expose
    private Object vendorApiKey;
    @SerializedName("fb_token")
    @Expose
    private Object fbToken;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;

    @SerializedName("is_dummy_password")
    @Expose
    private int isDummyPassword = 0;

    @SerializedName("language_string")
    @Expose
    private Map<String, String> languageString;
    @SerializedName("terminology")
    @Expose
    private Terminology terminology;
    @SerializedName("wallet_balance")
    @Expose
    private double walletBalance;
    @SerializedName("user_rights")
    @Expose
    private List<UserRights> userRights = new ArrayList<>();
    @SerializedName("is_reward_active_for_customer")
    @Expose
    private int isRewardActive;
    @SerializedName("debt_amount")
    @Expose
    private double debtAmount = 0;

    public int getIsCustomerSubscriptionPlanExpired() {
        return isCustomerSubscriptionPlanExpired;
    }

    public void setIsCustomerSubscriptionPlanExpired(int isCustomerSubscriptionPlanExpired) {
        this.isCustomerSubscriptionPlanExpired = isCustomerSubscriptionPlanExpired;
    }

    @SerializedName("is_customer_subscription_plan_expired")
    @Expose
    private int isCustomerSubscriptionPlanExpired = 0;

    @SerializedName("vendor_cash_tag_enabled")
    @Expose
    private int vendorCashTagEnabled;

    @SerializedName("vendor_paylater_tag_enabled")
    @Expose
    private int vendorPaylatertagEnabled;


    public int getVendorCashTagEnabled() {
        return vendorCashTagEnabled;
    }


    public int getVendorPaylatertagEnabled() {
        return vendorPaylatertagEnabled;
    }

    public boolean isDummyPassword() {
        if (isDummyPassword == 1)
            return false;
        return true;
    }

    public void setIsDummyPassword(int isDummyPassword) {
        this.isDummyPassword = isDummyPassword;
    }

    public Number getAverageRating() {
        return averageRating;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode != null ? languageCode : "en";
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageDisplayName() {
        return languageDisplayName;
    }

    public void setLanguageDisplayName(String languageDisplayName) {
        this.languageDisplayName = languageDisplayName;
    }

    public Map<String, String> getLanguageString() {
        return languageString;
    }

    public void setLanguageString(Map<String, String> languageString) {
        this.languageString = languageString;
    }

    public Terminology getTerminology() {
        return terminology != null ? terminology : new Terminology();
    }

    public void setTerminology(Terminology terminology) {
        this.terminology = terminology;
    }


    public void setReferrerId(Integer referrerId) {
        this.referrerId = referrerId;
    }

    public Object getAppDeviceType() {
        return appDeviceType;
    }

    public void setAppDeviceType(Object appDeviceType) {
        this.appDeviceType = appDeviceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getRegistrationStatus() {
        return registrationStatus != null ? registrationStatus : 2;
    }

    public void setRegistrationStatus(Integer registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public Object getVendorApiKey() {
        return vendorApiKey;
    }

    public void setVendorApiKey(Object vendorApiKey) {
        this.vendorApiKey = vendorApiKey;
    }

    public Object getFbToken() {
        return fbToken;
    }

    public void setFbToken(Object fbToken) {
        this.fbToken = fbToken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(int isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public int getIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(int isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * @return The vendorId
     */
    public Integer getVendorId() {
        return vendorId;
    }

    /**
     * @param vendorId The vendor_id
     */
    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    /**
     * @param firstName The first_name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    /**
     * @param lastName The last_name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email != null ? email : "";
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The phoneNo
     */
    public String getPhoneNo() {
        return phoneNo != null ? phoneNo : "";
    }

    /**
     * @param phoneNo The phone_no
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company The company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The passwordResetToken
     */
    public Object getPasswordResetToken() {
        return passwordResetToken;
    }

    /**
     * @param passwordResetToken The password_reset_token
     */
    public void setPasswordResetToken(Object passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    /**
     * @return The isBlocked
     */
    public Integer getIsBlocked() {
        return isBlocked;
    }

    /**
     * @param isBlocked The is_blocked
     */
    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The deviceType
     */
    public Object getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType The device_type
     */
    public void setDeviceType(Object deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return The deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * @param deviceToken The device_token
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * @return The appVersioncode
     */
    public String getAppVersioncode() {
        return appVersioncode;
    }

    /**
     * @param appVersioncode The app_versioncode
     */
    public void setAppVersioncode(String appVersioncode) {
        this.appVersioncode = appVersioncode;
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
     * @return The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return The lastLoginDateTime
     */
    public String getLastLoginDateTime() {
        return lastLoginDateTime;
    }

    /**
     * @param lastLoginDateTime The last_login_date_time
     */
    public void setLastLoginDateTime(String lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }

    /**
     * @return The creationDateTime
     */
    public String getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * @param creationDateTime The creation_date_time
     */
    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public void setVendorImage(String vendorImage) {
        this.vendorImage = vendorImage;
    }

    public Number getCredits() {
        return credits;
    }

    public void setCredits(Number credits) {
        this.credits = credits;
    }

    public Integer getReferrerId() {
        return referrerId;
    }

    public void setReffererId(Integer referrerId) {
        this.referrerId = referrerId;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getRefrenceId() {
        return "" + referenceId;
    }

    public int getMarketplaceUserId() {
        return marketplaceUserId;
    }

    public void setMarketplaceUserId(int marketplaceUserId) {
        this.marketplaceUserId = marketplaceUserId;
    }

    public int getDualUserCurrentStatus() {
        return dualUserCurrentStatus;
    }

    public String getUpdatedPhoneNo() {
        return updatedPhoneNo != null ? updatedPhoneNo : "";
    }

    public void setUpdatedPhoneNo(String updatedPhoneNo) {
        this.updatedPhoneNo = updatedPhoneNo;
    }

    public String getUpdatedEmail() {
        return updatedEmail != null ? updatedEmail : "";
    }

    public void setUpdatedEmail(String updatedEmail) {
        this.updatedEmail = updatedEmail;
    }

    public long getLastPaymentMethod() {
        return lastPaymentMethod;
    }

    public ArrayList<SubscriptionPlan> getSubscriptionPlan() {
        SubscriptionPlan subscriptionPlanObject = new SubscriptionPlan(0.0, 1);
        ArrayList<SubscriptionPlan> newArraylist = new ArrayList<>();
        newArraylist.add(0, subscriptionPlanObject);
        return subscriptionPlan != null && subscriptionPlan.size() > 0 ? subscriptionPlan : newArraylist;
    }

    public void setSubscriptionPlan(ArrayList<SubscriptionPlan> subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public List<UserRights> getUserRights() {
        return userRights;
    }

    public void setUserRights(List<UserRights> userRights) {
        this.userRights = userRights;
    }

    public boolean getIsRewardActive() {
        return isRewardActive == 1;
    }

    public int getIsVendorVerified() {
        return isVendorVerified;
    }

    public double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }
}
