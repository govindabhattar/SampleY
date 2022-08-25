package com.tookancustomer.models.ProductCatalogueData;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.MarketplaceStorefrontModel.LastReviewRating;
import com.tookancustomer.models.NLevelWorkFlowModel.LayoutData;
import com.tookancustomer.models.TaxesModel;
import com.tookancustomer.models.ThumbList;
import com.tookancustomer.models.userdata.PaymentSettings;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Datum implements Serializable {

    private final static long serialVersionUID = -3223744352156332843L;
    @SerializedName("layout_id")
    @Expose
    public String layoutId;
    @SerializedName("layout_data")
    @Expose
    public LayoutData layoutData;
    private boolean isHeader = false;
    private Number unitCount = 1;
    private Integer selectedQuantity = 0;
    private Integer isReturn = 0;
    private int vendorId;
    private String tag = "";
    private int units = 0;
    //    private Integer sellerId = 0;
    private Date productStartDate = new Date(0), productEndDate = new Date(0);
    private com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData;
    private Double productTotalCalculatedPrice = 0.0;


    private double surgeAmount = 0.0;


    @SerializedName(value = "seller_id")
    @Expose
    private Integer sellerId = 0;
    @SerializedName(value = "agent_id")
    @Expose
    private Integer agentId = 0;
    @SerializedName("enable_tookan_agent")
    @Expose
    private Integer enableTookanAgent = 0;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("seller_user_id")
    @Expose
    private int sellerUserId = 0;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String displayName = "";
    @SerializedName("store_name")
    @Expose
    private String storeName = "";
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("long_description")
    @Expose
    private String longDescription = "";
    @SerializedName("parent_category_id")
    @Expose
    private Integer parentCategoryId = 0;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;
    @SerializedName("product_enabled")
    @Expose
    private Integer productEnabled = 1;
    @SerializedName("layout_type")
    @Expose
    private Integer layoutType;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    //TODO
//    @SerializedName("thumb_list1")
    @SerializedName("thumb_list")
    @Expose
    private ThumbList thumbList;
    //TODO
//    @SerializedName("multi_image_url1")
    @SerializedName("multi_image_url")
    @Expose
    private ArrayList<String> multiImageUrl = new ArrayList<>();
    @SerializedName("multi_video_url")
    @Expose
    private ArrayList<MultiVideoUrl> multiVideoUrl = new ArrayList<>();
    @SerializedName("geofence_id")
    @Expose
    private Object geofenceId;
    @SerializedName("priority")
    @Expose
    private Object priority;
    @SerializedName("creation_datetime")
    @Expose
    private String creationDatetime;
    @SerializedName("max_discount_amount")
    @Expose
    private float maxDiscountAmount = 0.0f;
    @SerializedName("unit_description")
    @Expose
    private String unitDescription;
    @SerializedName("display_address")
    @Expose
    private String displayAddress;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("unit")
    @Expose
    private Number unit = 1;
    @SerializedName("unit_type")
    @Expose
    private int unitType = 1;
    @SerializedName("price")
    @Expose
    private Number price;
    @SerializedName("pricing_id")
    @Expose
    private Integer pricingId;
    @SerializedName("base_unit_id")
    @Expose
    private Integer baseUnitId;
    @SerializedName("conversion_factor")
    @Expose
    private Double conversionFactor;
    @SerializedName("parent_category_name")
    @Expose
    private String parentCategoryName;
    private int previousId;
    @SerializedName(value = "customization", alternate = {"customize_item"})
    @Expose
    private List<CustomizeItem> customizeItem;
    @SerializedName("itemSelectedList")
    @Expose
    private List<ItemSelected> itemSelectedList = new ArrayList<>();
    @SerializedName("is_veg")
    @Expose
    private Integer isVeg;
    @SerializedName("menu_enabled_product")
    @Expose
    private int menuEnabledProduct = 1;
    @SerializedName("inventory_enabled")
    @Expose
    private int inventoryEnabled = 0;
    @SerializedName("available_quantity")
    @Expose
    private Integer availableQuantity = 0;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("sellers")
    @Expose
    private ArrayList<Seller> sellers = new ArrayList<>();
    @SerializedName("custom_fields")
    @Expose
    private ArrayList<CustomField> customFields = new ArrayList<>();
    @SerializedName("total_ratings_count")
    @Expose
    private Number totalRatingsCount = 0;
    @SerializedName("total_review_count")
    @Expose
    private Number totalReviewCount = 0;
    @SerializedName("last_review_ratings")
    @Expose
    private ArrayList<LastReviewRating> lastReviewRating = new ArrayList<>();
    @SerializedName("product_rating")
    @Expose
    private Number productRating = 0;
    @SerializedName("my_review")
    @Expose
    private String myReview = "";
    @SerializedName("my_rating")
    @Expose
    private Number myRating = 0;
    @SerializedName("is_review_rating_enabled")
    @Expose
    private int is_review_rating_enabled = 0;
    @SerializedName("return_enabled")
    @Expose
    private int return_enabled = 0;
    @SerializedName("discount")
    @Expose
    private float productDiscount;
    @SerializedName("original_price")
    @Expose
    private float originalPrice;
    @SerializedName("is_static_address_enabled")
    @Expose
    private int isStaticAddressEnabled;
    @SerializedName("delivery_by_merchant")
    @Expose
    private int deliveryByMerchant;
    @SerializedName("minimum_quantity")
    @Expose
    private int minProductquantity;
    @SerializedName("maximum_quantity")
    @Expose
    private int maxProductquantity = 0;
    @SerializedName("is_menu_enabled")
    @Expose
    private int isMenuEnabled;
    @SerializedName("date_time")
    @Expose
    private String preorderDateTime;
    @SerializedName("is_preorder_selected_for_menu")
    @Expose
    private int isPreorderSelecetedForMenu;
    @SerializedName("is_cancelation_policy_enabled")
    @Expose
    private int isCancelationPolicyEnabled;
    @SerializedName("service_time")
    @Expose
    private int serviceTime;
    @SerializedName("taxes")
    @Expose
    private List<TaxesModel> taxesArrayList = new ArrayList<>();
    @SerializedName("is_recurring_enabled")
    @Expose
    private int recurring_enabled;
    @SerializedName("is_agents_on_product_tags_enabled")
    @Expose
    private int isAgentsOnProductTagsEnabled;
    @SerializedName("is_product_template_enabled")
    @Expose
    private int isProductTemplateEnabled;
    @SerializedName("payment_settings")
    @Expose
    private PaymentSettings paymentSettings;
    private ArrayList<Template> questionnaireTemplate;
    private double questionnaireTemplateCost = 0.0;
    private boolean isAgentSelected = false;
    private int SelectedAgentId = 0;

    @SerializedName("often_bought_products")
    private ArrayList<Integer> oftenBoughtProducts = new ArrayList<Integer>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public PaymentSettings getPaymentSettings() {
        if (paymentSettings != null && paymentSettings.getCode() != null)
            return paymentSettings;
        else
            return null;
    }

    public void setPaymentSettings(PaymentSettings paymentSettings) {
        this.paymentSettings = paymentSettings;
    }

    public boolean isAgentSelected() {
        return isAgentSelected;
    }

    public void setAgentSelected(boolean agentSelected) {
        isAgentSelected = agentSelected;
    }

    public int getSelectedAgentId() {
        return SelectedAgentId;
    }

    public void setSelectedAgentId(int selectedAgentId) {
        SelectedAgentId = selectedAgentId;
    }

    public double getQuestionnaireTemplateCost() {
        return questionnaireTemplateCost;
    }

    public void setQuestionnaireTemplateCost(double questionnaireTemplateCost) {
        this.questionnaireTemplateCost = questionnaireTemplateCost;
    }

    public ArrayList<Template> getQuestionnaireTemplate() {
        return questionnaireTemplate;
    }

    public void setQuestionnaireTemplate(ArrayList<Template> questionnaireTemplate) {
        this.questionnaireTemplate = questionnaireTemplate;
    }

    public int getIsProductTemplateEnabled() {
        return isProductTemplateEnabled;
    }

    public void setIsProductTemplateEnabled(int isProductTemplateEnabled) {
        this.isProductTemplateEnabled = isProductTemplateEnabled;
    }

    public float getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(float maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public int getIsAgentsOnProductTagsEnabled() {
        return isAgentsOnProductTagsEnabled;
    }

    public void setIsAgentsOnProductTagsEnabled(int isAgentsOnProductTagsEnabled) {
        this.isAgentsOnProductTagsEnabled = isAgentsOnProductTagsEnabled;
    }

    public int getRecurring_enabled() {
        return recurring_enabled;
    }

    public void setRecurring_enabled(int recurring_enabled) {
        this.recurring_enabled = recurring_enabled;
    }

    public int getPreviousCategoryID() {
        return previousId;
    }

    public void setPreviousCategoryID(int id) {
        previousId = id;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Integer getEnableTookanAgent() {
        return enableTookanAgent;
    }

    public void setEnableTookanAgent(Integer enableTookanAgent) {
        this.enableTookanAgent = enableTookanAgent;
    }

    public String getDisplayAddress() {
        return displayAddress != null ? displayAddress : "";
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    public String getPhone() {
        return phone != null ? phone : "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMenuEnabledProduct() {
        return menuEnabledProduct;
    }

    public void setMenuEnabledProduct(int menuEnabledProduct) {
        this.menuEnabledProduct = menuEnabledProduct;
    }

    public Date getProductStartDate() {
        return productStartDate;
    }

    public void setProductStartDate(Date productStartDate) {
        this.productStartDate = productStartDate;
    }

    public int getMaxProductquantity() {
        return maxProductquantity;
    }

    public void setMaxProductquantity(int maxProductquantity) {
        this.maxProductquantity = maxProductquantity;
    }

    public Date getProductEndDate() {
        return productEndDate;
    }

    public void setProductEndDate(Date productEndDate) {
        this.productEndDate = productEndDate;
    }

    public int getInventoryEnabled() {
        if (storefrontData != null && storefrontData.getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE) {
            return 0;
        }
        return inventoryEnabled;
    }

    public void setInventoryEnabled(int inventoryEnabled) {
        this.inventoryEnabled = inventoryEnabled;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity != null ? (availableQuantity <= 0 ? 0 : availableQuantity) : 0;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public int getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(int sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getDisplayName() {
        return displayName != null ? displayName : "";
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(ArrayList<Seller> sellers) {
        this.sellers = sellers;
    }

    public ArrayList<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CustomField> customFields) {
        this.customFields = customFields;
    }

    public Integer getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Integer isVeg) {
        this.isVeg = isVeg;
    }

    public List<CustomizeItem> getCustomizeItem() {
        if (customizeItem == null) {
            customizeItem = new ArrayList<>();
        }
        return customizeItem;
    }

    public void setCustomizeItem(List<CustomizeItem> customizeItem) {
        this.customizeItem = customizeItem;
    }

    public List<ItemSelected> getItemSelectedList() {
        if (itemSelectedList == null) {
            itemSelectedList = new ArrayList<>();
        }
        return itemSelectedList;
    }

    public void setItemSelectedList(List<ItemSelected> itemSelectedList) {
        this.itemSelectedList = itemSelectedList;
    }

    public Integer getTotalQuantity() {
        int total = 0;
        for (ItemSelected itemSelected : getItemSelectedList()) {
            total = total + itemSelected.getQuantity();
        }
        return total;
    }

    public Double getSuperTotalPrice() {
        double total = 0;
        for (ItemSelected itemSelected : getItemSelectedList()) {
            total = total + itemSelected.getTotalPriceWithQuantity();
        }
        return total;
    }

    public double getCustomizeItemsSelectedTotalPriceForItemSelected(ItemSelected itemSelected) {
        double totalPrice = getPrice().doubleValue();
        for (CustomizeItem customizeItem : getCustomizeItem()) {
            CustomizeItemSelected customizeItemSelected = getCustomizeItemSelected(customizeItem, false, itemSelected);
            for (CustomizeOption customizeOption : customizeItem.getCustomizeOptions()) {
                if (customizeItemSelected.getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                    totalPrice = totalPrice + customizeOption.getCustomizePrice();
                }
            }
        }
        return totalPrice;
    }

    public double getCustomizeItemsSelectedPriceForItemSelected(ItemSelected itemSelected) {
        double totalPrice = 0.0;
        for (CustomizeItem customizeItem : getCustomizeItem()) {
            CustomizeItemSelected customizeItemSelected = getCustomizeItemSelected(customizeItem, false, itemSelected);

            for (CustomizeOption customizeOption : customizeItem.getCustomizeOptions()) {
                if (customizeItemSelected.getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                    totalPrice = totalPrice + customizeOption.getCustomizePrice();
                }
            }
        }
        return totalPrice;
    }

    public double getCustomizeItemsSelectedPriceForItemSelectedIfEdit(ItemSelected itemSelected, Datum item, int itemPos) {
        double totalPrice = 0.0;
        for (CustomizeItem customizeItem : getCustomizeItem()) {
            CustomizeItemSelected customizeItemSelected = new CustomizeItemSelected();
            customizeItemSelected.setCustomizeOptions(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(0).getCustomizeOptions());
            customizeItemSelected.setCustomizeId(item.getItemSelectedList().get(itemPos).getCustomizeItemSelectedList().get(0).getCustomizeId());
            for (CustomizeOption customizeOption : customizeItem.getCustomizeOptions()) {
                if (customizeItemSelected.getCustomizeOptions().contains(customizeOption.getCustomizeOptionId())) {
                    totalPrice = totalPrice + customizeOption.getCustomizePrice();
                }
            }
        }
        return totalPrice;
    }

    public CustomizeItemSelected getCustomizeItemSelected(CustomizeItem customizeItem, boolean addSelected, ItemSelected itemSelected) {
        CustomizeItemSelected customizeItemSelected = new CustomizeItemSelected(customizeItem.getCustomizeId());
        int index = itemSelected.getCustomizeItemSelectedList().indexOf(customizeItemSelected);
        if (index > -1) {
            customizeItemSelected = itemSelected.getCustomizeItemSelectedList().get(index);
        } else if (addSelected) {
            itemSelected.getCustomizeItemSelectedList().add(customizeItemSelected);
        }
        return customizeItemSelected;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getLayoutType() {
        return layoutType != null ? layoutType : Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Integer getProductId() {
        return productId != null ? productId : 0;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name != null ? Utils.capitaliseWords(name) : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId != null ? parentCategoryId : 0;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(final Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getParentCategoryName() {
        return parentCategoryName != null ? parentCategoryName : "";
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl : "";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl() {
        if (thumbList != null && !thumbList.get250x250().isEmpty()
//                && !ThumbList.get400x400().isEmpty()
        ) {
            thumbUrl = thumbList.get250x250();
//            if (getLayoutType() == Constants.NLevelLayoutType.LIST_LAYOUT.layoutValue) {
//                thumbUrl = ThumbList.get250x250();
//            } else {
//                thumbUrl = ThumbList.get400x400();
//            }
        }
//        return imageUrl != null ? imageUrl : "";
        return thumbUrl != null ? thumbUrl : "";
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Object getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(Object geofenceId) {
        this.geofenceId = geofenceId;
    }

    public Object getPriority() {
        return priority;
    }

    public void setPriority(Object priority) {
        this.priority = priority;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(String layoutId) {
        this.layoutId = layoutId;
    }

    public LayoutData getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(LayoutData layoutData) {
        this.layoutData = layoutData;
    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;

    }


    public Object getDescription() {
        if (description != null) {
            Spanned spannedDesc = Html.fromHtml(description.toString().trim());
            return spannedDesc.toString();

        } else {
            return "";
        }
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public void setUnitDescription(String unitDescription) {
        this.unitDescription = unitDescription;
    }

//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }


    public Number getUnit() {
        return unit;
    }

    public void setUnit(Number unit) {
        this.unit = unit;
    }

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public Number getPrice() {
        return price != null ? price : 0;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public Number getPriceWithCount() {
        return price != null ? price.doubleValue() * getUnitCount().intValue() : 0;
    }

    public Number getTotalPrice() {
        if (price != null) {
            return price.doubleValue() * selectedQuantity;
        } else {
            return 0;
        }
    }

    public Number getTotalPriceWithCount() {
        if (price != null) {
            return price.doubleValue() * selectedQuantity * getUnitCount().intValue();
        } else {
            return 0;
        }
    }

    public Number getUnitCount() {
        unitCount = 1;
        if (getStorefrontData().getBusinessType() == Constants.BusinessType.SERVICES_BUSINESS_TYPE &&
                getUnitType() != Constants.ProductsUnitType.FIXED.value) {

            long differenceMultiple = Constants.ProductsUnitType.getStartEndDifferenceMultiple(getUnitType(), getUnit().intValue());
            long currentDifference = getProductEndDate().getTime() - getProductStartDate().getTime();
            unitCount = currentDifference / differenceMultiple;
            if (unitCount.intValue() < 1) unitCount = 1;
        }
        return unitCount;
    }

    public Integer getPricingId() {
        return pricingId;
    }

    public void setPricingId(Integer pricingId) {
        this.pricingId = pricingId;
    }

    public Integer getBaseUnitId() {
        return baseUnitId;
    }

    public void setBaseUnitId(Integer baseUnitId) {
        this.baseUnitId = baseUnitId;
    }

    public Double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(Double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getSurgeAmount() {
        return surgeAmount;
    }

    public void setSurgeAmount(double surgeAmount) {
        this.surgeAmount = surgeAmount;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getProductEnabled() {
        return productEnabled;
    }

    public void setProductEnabled(Integer productEnabled) {
        this.productEnabled = productEnabled;
    }

    public Integer getSellerId() {
        return sellerId != null && sellerId > 0 ? sellerId : getUserId();
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public com.tookancustomer.models.MarketplaceStorefrontModel.Datum getStorefrontData() {
        return storefrontData;
    }

    public void setStorefrontData(com.tookancustomer.models.MarketplaceStorefrontModel.Datum storefrontData) {
        this.storefrontData = storefrontData;
    }

    public ArrayList<String> getMultiImageUrl() {
        return multiImageUrl != null ? multiImageUrl : new ArrayList<String>();
    }

    public void setMultiImageUrl(ArrayList<String> multiImageUrl) {
        this.multiImageUrl = multiImageUrl;
    }

    public ArrayList<MultiVideoUrl> getMultiVideoUrl() {
        return multiVideoUrl != null ? multiVideoUrl : new ArrayList<MultiVideoUrl>();
    }

    public void setMultiVideoUrl(ArrayList<MultiVideoUrl> multiVideoUrl) {
        this.multiVideoUrl = multiVideoUrl;
    }

    public ThumbList getThumbList() {
        return thumbList != null ? thumbList : new ThumbList();
    }

    public void setThumbList(ThumbList thumbList) {
        this.thumbList = thumbList;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public Number getProductRating() {
        return productRating != null ? productRating : 0;
    }

    public void setProductRating(Number productRating) {
        this.productRating = productRating;
    }

    public ArrayList<LastReviewRating> getLastReviewRating() {
        return lastReviewRating != null ? lastReviewRating : new ArrayList<LastReviewRating>();
    }

    public void setLastReviewRating(ArrayList<LastReviewRating> lastReviewRating) {
        this.lastReviewRating = lastReviewRating;
    }

    public int getTotalRatingsCount() {
        return totalRatingsCount != null ? totalRatingsCount.intValue() : 0;
    }

    public void setTotalRatingsCount(int totalRatingsCount) {
        this.totalRatingsCount = totalRatingsCount;
    }

    public int getTotalReviewCount() {
        return totalReviewCount != null ? totalReviewCount.intValue() : 0;
    }

    public void setTotalReviewCount(int totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public String getMyReview() {
        return myReview != null ? myReview : "";
    }

    public void setMyReview(String myReview) {
        this.myReview = myReview;
    }

    public Number getMyRating() {
        return myRating != null ? myRating : 0;
    }

    public void setMyRating(Number myRating) {
        this.myRating = myRating;
    }

    public int getReturn_enabled() {
        return return_enabled;
    }

    public void setReturn_enabled(int return_enabled) {
        this.return_enabled = return_enabled;
    }

    public float getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(float productDiscount) {
        this.productDiscount = productDiscount;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getIsStaticAddressEnabled() {
        return isStaticAddressEnabled;
    }

    public void setIsStaticAddressEnabled(int isStaticAddressEnabled) {
        this.isStaticAddressEnabled = isStaticAddressEnabled;
    }

    public boolean isStaticAddressEnabled() {
        return isStaticAddressEnabled == 1;
    }

    public int getDeliveryByMerchant() {
        return deliveryByMerchant;
    }

    public void setDeliveryByMerchant(int deliveryByMerchant) {
        this.deliveryByMerchant = deliveryByMerchant;
    }

    public boolean isDeliveryByAdmin() {
        return deliveryByMerchant == 0;
    }

    public int getMinProductquantity() {
        return minProductquantity;
//        return minProductquantity != 1 ? minProductquantity : 5;
    }

    public void setMinProductquantity(int minProductquantity) {
        this.minProductquantity = minProductquantity;
    }

    public String getLongDescription() {
//        return longDescription == null ? description.toString() : longDescription;
        if (longDescription != null) {
            Spanned spannedDesc = Html.fromHtml(longDescription.trim());
            return spannedDesc.toString();

        } else {
            return "";
        }

    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public int getIsMenuEnabled() {
        return isMenuEnabled;
    }

    public void setIsMenuEnabled(int isMenuEnabled) {
        this.isMenuEnabled = isMenuEnabled;
    }

    public String getPreorderDateTime() {
        return preorderDateTime;
    }

    public void setPreorderDateTime(String preorderDateTime) {
        this.preorderDateTime = preorderDateTime;
    }

    public int getIsPreorderSelecetedForMenu() {
        return isPreorderSelecetedForMenu;
    }

    public void setIsPreorderSelecetedForMenu(int isPreorderSelecetedForMenu) {
        this.isPreorderSelecetedForMenu = isPreorderSelecetedForMenu;
    }

    public boolean getIsCancelationPolicyEnabled() {
        return isCancelationPolicyEnabled == 1;
    }

    public List<TaxesModel> getTaxesArrayList() {
        return taxesArrayList;
    }

    public void setTaxesArrayList(List<TaxesModel> taxesArrayList) {
        this.taxesArrayList = taxesArrayList;
    }

    public Double getProductTotalCalculatedPrice() {
        return productTotalCalculatedPrice;
    }

    public void setProductTotalCalculatedPrice(Double productTotalCalculatedPrice) {
        this.productTotalCalculatedPrice = productTotalCalculatedPrice;
    }

    public ArrayList<Integer> getOftenBoughtProducts() {
        return oftenBoughtProducts;
    }

}
