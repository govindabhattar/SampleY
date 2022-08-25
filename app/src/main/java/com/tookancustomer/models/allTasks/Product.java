
package com.tookancustomer.models.allTasks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("order_item_id")
    @Expose
    private Integer orderItemId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("seller_id")
    @Expose
    private Integer sellerId;
    @SerializedName("cust_id")
    @Expose
    private Object custId;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;
    @SerializedName("cost_price")
    @Expose
    private Object costPrice;
    @SerializedName("mrp")
    @Expose
    private Integer mrp;
    @SerializedName("unit_type")
    @Expose
    private Integer unitType;
    @SerializedName("unit_count")
    @Expose
    private Integer unitCount;
    @SerializedName("unit")
    @Expose
    private Integer unit;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("quantity_for_each_product")
    @Expose
    private Integer quantityForEachProduct;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("customization_linking")
    @Expose
    private String customizationLinking;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("creat 2021-06-21 20:32:07.892 20489-20718/com.jungleworks.yelo I/okhttp.OkHttpClient: ed_at")
    @Expose
    private String creat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("inventory_enabled")
    @Expose
    private Integer inventoryEnabled;
    @SerializedName("task_start_time")
    @Expose
    private Object taskStartTime;
    @SerializedName("task_end_time")
    @Expose
    private Object taskEndTime;
    @SerializedName("task_start_time_local")
    @Expose
    private Object taskStartTimeLocal;
    @SerializedName("task_end_time_local")
    @Expose
    private Object taskEndTimeLocal;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("enable_tookan_agent")
    @Expose
    private Integer enableTookanAgent;
    @SerializedName("business_type")
    @Expose
    private Object businessType;
    @SerializedName("is_product_template_enabled")
    @Expose
    private Integer isProductTemplateEnabled;
    @SerializedName("template_cost")
    @Expose
    private Integer templateCost;
    @SerializedName("template")
    @Expose
    private List<Object> template = null;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("name_json")
    @Expose
    private String nameJson;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("catalogue_id")
    @Expose
    private Object catalogueId;
    @SerializedName("category_name")
    @Expose
    private Object categoryName;
    @SerializedName("category_name_json")
    @Expose
    private Object categoryNameJson;
    @SerializedName("surge_type")
    @Expose
    private Integer surgeType;
    @SerializedName("surge_amount")
    @Expose
    private Integer surgeAmount;
    @SerializedName("include_intervals")
    @Expose
    private Object includeIntervals;
    @SerializedName("multiPrice")
    @Expose
    private List<MultiPrice> multiPrice = null;
    @SerializedName("group_customization")
    @Expose
    private List<Object> groupCustomization = null;
    @SerializedName("taxes")
    @Expose
    private List<Object> taxes = null;

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public Object getCustId() {
        return custId;
    }

    public void setCustId(Object custId) {
        this.custId = custId;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Object getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Object costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
        this.unitCount = unitCount;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityForEachProduct() {
        return quantityForEachProduct;
    }

    public void setQuantityForEachProduct(Integer quantityForEachProduct) {
        this.quantityForEachProduct = quantityForEachProduct;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomizationLinking() {
        return customizationLinking;
    }

    public void setCustomizationLinking(String customizationLinking) {
        this.customizationLinking = customizationLinking;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt() {
        return creat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt;
    }

    public void setCreat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt(String creat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt) {
        this.creat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt = creat202106212032078922048920718ComJungleworksYeloIOkhttpOkHttpClientEdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getInventoryEnabled() {
        return inventoryEnabled;
    }

    public void setInventoryEnabled(Integer inventoryEnabled) {
        this.inventoryEnabled = inventoryEnabled;
    }

    public Object getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Object taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public Object getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Object taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public Object getTaskStartTimeLocal() {
        return taskStartTimeLocal;
    }

    public void setTaskStartTimeLocal(Object taskStartTimeLocal) {
        this.taskStartTimeLocal = taskStartTimeLocal;
    }

    public Object getTaskEndTimeLocal() {
        return taskEndTimeLocal;
    }

    public void setTaskEndTimeLocal(Object taskEndTimeLocal) {
        this.taskEndTimeLocal = taskEndTimeLocal;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getEnableTookanAgent() {
        return enableTookanAgent;
    }

    public void setEnableTookanAgent(Integer enableTookanAgent) {
        this.enableTookanAgent = enableTookanAgent;
    }

    public Object getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Object businessType) {
        this.businessType = businessType;
    }

    public Integer getIsProductTemplateEnabled() {
        return isProductTemplateEnabled;
    }

    public void setIsProductTemplateEnabled(Integer isProductTemplateEnabled) {
        this.isProductTemplateEnabled = isProductTemplateEnabled;
    }

    public Integer getTemplateCost() {
        return templateCost;
    }

    public void setTemplateCost(Integer templateCost) {
        this.templateCost = templateCost;
    }

    public List<Object> getTemplate() {
        return template;
    }

    public void setTemplate(List<Object> template) {
        this.template = template;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNameJson() {
        return nameJson;
    }

    public void setNameJson(String nameJson) {
        this.nameJson = nameJson;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(Object catalogueId) {
        this.catalogueId = catalogueId;
    }

    public Object getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public Object getCategoryNameJson() {
        return categoryNameJson;
    }

    public void setCategoryNameJson(Object categoryNameJson) {
        this.categoryNameJson = categoryNameJson;
    }

    public Integer getSurgeType() {
        return surgeType;
    }

    public void setSurgeType(Integer surgeType) {
        this.surgeType = surgeType;
    }

    public Integer getSurgeAmount() {
        return surgeAmount;
    }

    public void setSurgeAmount(Integer surgeAmount) {
        this.surgeAmount = surgeAmount;
    }

    public Object getIncludeIntervals() {
        return includeIntervals;
    }

    public void setIncludeIntervals(Object includeIntervals) {
        this.includeIntervals = includeIntervals;
    }

    public List<MultiPrice> getMultiPrice() {
        return multiPrice;
    }

    public void setMultiPrice(List<MultiPrice> multiPrice) {
        this.multiPrice = multiPrice;
    }

    public List<Object> getGroupCustomization() {
        return groupCustomization;
    }

    public void setGroupCustomization(List<Object> groupCustomization) {
        this.groupCustomization = groupCustomization;
    }

    public List<Object> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Object> taxes) {
        this.taxes = taxes;
    }

}
