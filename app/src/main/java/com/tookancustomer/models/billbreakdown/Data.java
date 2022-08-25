package com.tookancustomer.models.billbreakdown;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.PromosModel;
import com.tookancustomer.models.RecurringSurgeListData;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Data implements Serializable {
    public BigDecimal paybleAmountBeforeDiscount;
    @SerializedName("DELIVERY_CHARGE")
    @Expose
    public BigDecimal deliveryCharge;
    @SerializedName("DELIVERY_DISCOUNT")
    @Expose
    public BigDecimal deliveryDiscount;
    @SerializedName("ACTUAL_AMOUNT")
    @Expose
    public BigDecimal actualAmount;
    @SerializedName("DISCOUNT")
    @Expose
    public BigDecimal discount;
    @SerializedName("TIP")
    @Expose
    public BigDecimal tip;
    @SerializedName("NET_PAYABLE_AMOUNT")
    @Expose
    public BigDecimal paybleAmount;
    @SerializedName("CREDITS_USED")
    @Expose
    public String creditUsed;
    @SerializedName("SHOW_PROMO_BTN")
    @Expose
    public boolean showPromoBtn;
    @SerializedName("LOYALTY_POINT_DISCOUNT")
    @Expose
    public BigDecimal loyaltyPointDiscount;
    @SerializedName("prev_job_amount")
    @Expose
    public BigDecimal prevJobAmount;
    @SerializedName("LOYALTY_POINT_USED")
    @Expose
    public int loyaltyPointUsed;

    @SerializedName("TAXABLE_AMOUNT")
    @Expose
    private BigDecimal taxableAmount;
    @SerializedName("CURRENCY")
    @Expose
    private PaymentSettings paymentSettings;
    @SerializedName("TAX_PERCENT")
    @Expose
    private String taxPercent;
    @SerializedName("TAX")
    @Expose
    private BigDecimal tax;
    @SerializedName("CREDITS_TO_ADD")
    @Expose
    private BigDecimal creditsToAdd;
    @SerializedName("VAT")
    @Expose
    private BigDecimal vat;
    @SerializedName("SERVICE_TAX")
    @Expose
    private BigDecimal serviceTax;
    @SerializedName("BENEFIT_TYPE")
    @Expose
    private Integer benefitType;
    @SerializedName("DISCOUNTED_AMOUNT")
    @Expose
    private double discountedAmmount;
    @SerializedName("USER_TAXES")
    @Expose
    private ArrayList<TaxesModel> userTaxesArray = new ArrayList<>();
    @SerializedName("DELIVERY_TAXES")
    @Expose
    private ArrayList<TaxesModel> deliveryTaxesArray = new ArrayList<>();
    @SerializedName("PROMOS")
    @Expose
    private ArrayList<PromosModel> promosArray = new ArrayList<>();
    @SerializedName("REFERRAL")
    @Expose
    private ArrayList<PromosModel> referralArray = new ArrayList<>();
    @SerializedName("TIP_OPTION_LIST")
    @Expose
    private ArrayList<TipModel> tipOptionList = new ArrayList<>();
    @SerializedName("APPLIED_PROMOS")
    @Expose
    private ArrayList<PromosModel> appliedPromos = new ArrayList<>();
    @SerializedName("TIP_ENABLE_DISABLE")
    @Expose
    private int tipEnableDisable = 0;
    @SerializedName("TIP_OPTION_ENABLE")
    @Expose
    private int tipOptionEnable = 0; //0 for manual ,,1 for options,, 2 for options + manual tip
    @SerializedName("TIP_TYPE")
    @Expose
    private int tipType = 2; //1 for percentage ,, 2 for price in amount
    @SerializedName("MINIMUM_TIP")
    @Expose
    private Double minimumTip = 0.0;
    @SerializedName("DELIVERY_CHARGES_FORMULA_FIELDS")
    @Expose
    private Object deliveryChargesFormulaFields;
    @SerializedName("ADDITIONAL_AMOUNT")
    @Expose
    private BigDecimal additionalAmount;
    @SerializedName("SURGE_AMOUNT")
    @Expose
    private BigDecimal surgeAmount;
    @SerializedName("DELIVERY_CHARGE_SURGE_AMOUNT")
    @Expose
    private BigDecimal deliverySurgeAmount;
    @SerializedName("LOYALTY_POINT_EARNED")
    @Expose
    private int loyaltyPointEarned;
    @SerializedName("MAX_USABLE_POINT")
    @Expose
    private int loyaltyMaxPoints;
    @SerializedName("LOYALTY_POINTS")
    @Expose
    private int loyaltyPoints;
    @SerializedName("PROMO_MODE")
    @Expose
    private int promoMode = 0;
    @SerializedName("PROMO_ON")
    @Expose
    private int promoOn = 0;
    @SerializedName("PROMO_DESCRIPTION")
    @Expose
    private String promoDescription = "";
    @SerializedName("WALLET_DETAILS")
    @Expose
    private WalletDetails walletDetails;
    @SerializedName("HOLD_AMOUNT")
    @Expose
    private String holdAmount = "0";
    @SerializedName("HOLD_PAYMENT")
    @Expose
    private int holdPayment;
    @SerializedName("TRANSACTIONAL_CHARGES_INFO")
    @Expose
    private TransactionalChangesInfo transactionalChargesInfo;
    @SerializedName("ADDITIONAL_CHARGES")
    @Expose
    private AdditionalCharges additionalCharges;
    @SerializedName("RECURRING_SURGE_DETAIL")
    @Expose
    private ArrayList<RecurringSurgeListData> recurringSurgeListData;
    @SerializedName("OCCURRENCE_COUNT")
    @Expose
    private int occurrenceCount;
    @SerializedName("TOTAL_RECURRING_AMOUNT")
    @Expose
    private BigDecimal totalRecurringAmount;
    public Data(BigDecimal deliveryCharge, BigDecimal actualAmount, BigDecimal paybleAmount) {
        this.deliveryCharge = deliveryCharge;
        this.actualAmount = actualAmount;
        this.paybleAmount = paybleAmount;
    }

    public ArrayList<TaxesModel> getDeliveryTaxesArray() {
        return deliveryTaxesArray;
    }

    public void setDeliveryTaxesArray(ArrayList<TaxesModel> deliveryTaxesArray) {
        this.deliveryTaxesArray = deliveryTaxesArray;
    }

    public AdditionalCharges getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(AdditionalCharges additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public PaymentSettings getPaymentSettings() {
        return paymentSettings;
    }

    public void setPaymentSettings(PaymentSettings paymentSettings) {
        this.paymentSettings = paymentSettings;
    }


    public TransactionalChangesInfo getTransactionalChargesInfo() {
        return transactionalChargesInfo;
    }

    public ArrayList<RecurringSurgeListData> getRecurringSurgeListData() {
        return recurringSurgeListData;
    }

    public void setRecurringSurgeListData(ArrayList<RecurringSurgeListData> recurringSurgeListData) {
        this.recurringSurgeListData = recurringSurgeListData;
    }

    public BigDecimal getSurgeAmount() {
        return surgeAmount;
    }

    public void setSurgeAmount(BigDecimal surgeAmount) {
        this.surgeAmount = surgeAmount;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public BigDecimal getTotalRecurringAmount() {
        return totalRecurringAmount;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public BigDecimal getLoyaltyPointDiscount() {
        return loyaltyPointDiscount;
    }

    public void setLoyaltyPointDiscount(BigDecimal loyaltyPointDiscount) {
        this.loyaltyPointDiscount = loyaltyPointDiscount;
    }

    public int getLoyaltyPointEarned() {
        return loyaltyPointEarned;
    }

    public void setLoyaltyPointEarned(int loyaltyPointEarned) {
        this.loyaltyPointEarned = loyaltyPointEarned;
    }

    public int getLoyaltyPointUsed() {
        return loyaltyPointUsed;
    }

    public void setLoyaltyPointUsed(int loyaltyPointUsed) {
        this.loyaltyPointUsed = loyaltyPointUsed;
    }

    public int getLoyaltyMaxPoints() {
        return loyaltyMaxPoints;
    }

    public void setLoyaltyMaxPoints(int loyaltyMaxPoints) {
        this.loyaltyMaxPoints = loyaltyMaxPoints;
    }

    public Double getMinimumTip() {
        return minimumTip != null ? Double.valueOf(Utils.getDoubleTwoDigits(minimumTip)) : 0;
    }

    public void setMinimumTip(Double minimumTip) {
        this.minimumTip = minimumTip;
    }

    public int getTipEnableDisable() {
        return tipEnableDisable;
    }

    public void setTipEnableDisable(int tipEnableDisable) {
        this.tipEnableDisable = tipEnableDisable;
    }

    public BigDecimal getDeliverySurgeAmount() {
        return deliverySurgeAmount;
    }

    public void setDeliverySurgeAmount(BigDecimal deliverySurgeAmount) {
        this.deliverySurgeAmount = deliverySurgeAmount;
    }

    public BigDecimal getPrevJobAmount() {
        if (prevJobAmount != null)
            return prevJobAmount;
        return BigDecimal.valueOf(0);

    }

    public void setPrevJobAmount(BigDecimal prevJobAmount) {
        this.prevJobAmount = prevJobAmount;
    }

    public String getActualAmount() {
        return actualAmount != null ? Utils.getDoubleTwoDigits(actualAmount) : "0";
    }

    public BigDecimal getActualAmountBigdecimal() {
        if (actualAmount != null)
            return actualAmount;
        return BigDecimal.valueOf(0);
    }

    public String getTaxPercent() {
        return taxPercent != null ? taxPercent : "0";
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getCreditUsed() {
        return creditUsed != null ? creditUsed : "0";
    }

    public String getDiscount() {
        return discount != null ? Utils.getDoubleTwoDigits(discount) : "0";
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getVat() {
        return vat != null ? Utils.getDoubleTwoDigits(vat) : "0";
    }

    public String getServiceTax() {
        return serviceTax != null ? Utils.getDoubleTwoDigits(serviceTax) : "0";
    }

    public String getTip() {
        return tip != null ? Utils.getDoubleTwoDigits(tip) : "0";
    }

    public String getTax() {
        return tax != null ? Utils.getDoubleTwoDigits(tax) : "0";
    }

    public String getDeliveryCharge() {
        return deliveryCharge != null ? Utils.getDoubleTwoDigits(deliveryCharge) : "0";
    }

    public BigDecimal getDeliveryChargeAfterDiscount() {
        if (deliveryDiscount != null) {
            return BigDecimal.valueOf(Double.valueOf(deliveryCharge.doubleValue()) - Double.valueOf(deliveryDiscount.doubleValue()));
        } else {
            return deliveryCharge;
        }
    }

    public String getPaybleAmount() {
        return paybleAmount != null ? Utils.getDoubleTwoDigits(paybleAmount) : "0";
    }

    public void setPaybleAmount(BigDecimal paybleAmount) {
        this.paybleAmount = paybleAmount;
    }

    public String getPaybleAmountBeforeDiscount() {
        return paybleAmountBeforeDiscount != null ? Utils.getDoubleTwoDigits(paybleAmountBeforeDiscount) : "0";
    }

    public void setPaybleAmountBeforeDiscount(BigDecimal paybleAmountBeforeDiscount) {
        this.paybleAmountBeforeDiscount = paybleAmountBeforeDiscount;
    }

    public ArrayList<TaxesModel> getUserTaxesArray() {
        return userTaxesArray;
    }

    public void setUserTaxesArray(ArrayList<TaxesModel> userTaxesArray) {
        this.userTaxesArray = userTaxesArray;
    }

    public ArrayList<PromosModel> getPromosArray() {
        return promosArray;
    }

    public void setPromosArray(ArrayList<PromosModel> promosArray) {
        this.promosArray = promosArray;
    }

    public ArrayList<PromosModel> getReferralArray() {
        return referralArray;
    }

    public void setReferralArray(ArrayList<PromosModel> referralArray) {
        this.referralArray = referralArray;
    }

    public Object getDeliveryChargesFormulaFields() {
        return deliveryChargesFormulaFields;
    }

    public void setDeliveryChargesFormulaFields(Object deliveryChargesFormulaFields) {
        this.deliveryChargesFormulaFields = deliveryChargesFormulaFields;
    }

    public ArrayList<TipModel> getTipOptionList() {
        return tipOptionList;
    }

    public void setTipOptionList(ArrayList<TipModel> tipOptionList) {
        this.tipOptionList = tipOptionList;
    }

    public int getTipType() {
        return tipType;
    }

    public void setTipType(int tipType) {
        this.tipType = tipType;
    }

    public int getTipOptionEnable() {
        return tipOptionEnable;
    }

    public void setTipOptionEnable(int tipOptionEnable) {
        this.tipOptionEnable = tipOptionEnable;
    }

    public BigDecimal getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(BigDecimal additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public boolean isShowPromoBtn() {
        return showPromoBtn;
    }

    public void setShowPromoBtn(boolean showPromoBtn) {
        this.showPromoBtn = showPromoBtn;
    }

    public int getPromoMode() {
        return promoMode;
    }

    public int getPromoOn() {
        return promoOn;
    }

    public String getPromoDescription() {
        return promoDescription != null ? promoDescription : "";
    }

    public ArrayList<PromosModel> getAppliedPromos() {
        return appliedPromos != null ? appliedPromos : new ArrayList<PromosModel>();
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public WalletDetails getWalletDetails() {
        return walletDetails;
    }

    public void setWalletDetails(WalletDetails walletDetails) {
        this.walletDetails = walletDetails;
    }

    public String getHoldAmount() {
        return holdAmount;
    }

    public boolean getHoldPayment() {
        return holdPayment == 1;
    }
}