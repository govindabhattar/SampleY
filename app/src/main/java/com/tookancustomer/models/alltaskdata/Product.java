package com.tookancustomer.models.alltaskdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.checkoutTemplate.model.Template;
import com.tookancustomer.models.TaxesModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by socomo on 05/10/17.
 */

public class Product implements Serializable {


    @SerializedName("order_item_id")
    @Expose
    private Integer orderItemId;

    @SerializedName("job_id")
    @Expose
    private Integer jobId;


    @SerializedName("unit")
    @Expose
    private Number unit = 1;
    @SerializedName("unit_count")
    @Expose
    private Number unitCount = 1;
    @SerializedName("unit_type")
    @Expose
    private Integer unitType = 1;
    @SerializedName("task_type")
    @Expose
    private Integer taskType = 1;

    @SerializedName("enable_tookan_agent")
    @Expose
    private Integer enableTookanAgent = 0;

    @SerializedName("is_product_template_enabled")
    @Expose
    private int isProductTemplateEnabled = 0;
    @SerializedName("template")
    @Expose
    private ArrayList<Template> productTemplate;
    @SerializedName("template_cost")
    @Expose
    private double templateCost;


    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("cust_id")
    @Expose
    private Integer custId;

    @SerializedName("unit_price")
    @Expose
    private Double unitPrice;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("total_price")
    @Expose
    private Double totalPrice;


    @SerializedName("surge_amount")
    @Expose
    private double surgeAmount;

    @SerializedName("customization_linking")
    @Expose
    private String customizationLinking;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("task_start_time")
    @Expose
    private String taskStartTime;
    @SerializedName("task_end_time")
    @Expose
    private String taskEndTime;
    @SerializedName("task_start_time_local")
    @Expose
    private String taskStartTimeLocal;
    @SerializedName("task_end_time_local")
    @Expose
    private String taskEndTimeLocal;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("services")
    @Expose
    private Services services;
    @SerializedName("taxes")
    @Expose
    private List<TaxesModel> taxesArrayList = new ArrayList<>();


    public Integer getEnableTookanAgent() {
        return enableTookanAgent;
    }

    public void setEnableTookanAgent(Integer enableTookanAgent) {
        this.enableTookanAgent = enableTookanAgent;
    }

    public int getIsProductTemplateEnabled() {
        return isProductTemplateEnabled;
    }

    public void setIsProductTemplateEnabled(int isProductTemplateEnabled) {
        this.isProductTemplateEnabled = isProductTemplateEnabled;
    }

    public ArrayList<Template> getProductTemplate() {
        return productTemplate;
    }

    public void setProductTemplate(ArrayList<Template> productTemplate) {
        this.productTemplate = productTemplate;
    }

    public double getTemplateCost() {
        return templateCost;
    }

    public void setTemplateCost(double templateCost) {
        this.templateCost = templateCost;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getCustId() {
        return custId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public double getSurgeAmount() {
        return surgeAmount;
    }

    public String getCustomizationLinking() {
        return customizationLinking;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getProductName() {
        return productName;
    }

    public Number getUnit() {
        return unit;
    }

    public void setUnit(Number unit) {
        this.unit = unit;
    }

    public Number getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Number unitCount) {
        this.unitCount = unitCount;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getTaskStartTimeLocal() {
        return taskStartTimeLocal != null ? taskStartTimeLocal : "";
    }

    public void setTaskStartTimeLocal(String taskStartTimeLocal) {
        this.taskStartTimeLocal = taskStartTimeLocal;
    }

    public String getTaskEndTimeLocal() {
        return taskEndTimeLocal != null ? taskEndTimeLocal : "";
    }

    public void setTaskEndTimeLocal(String taskEndTimeLocal) {
        this.taskEndTimeLocal = taskEndTimeLocal;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }


    public List<TaxesModel> getTaxesArrayList() {
        return taxesArrayList;
    }

    public void setTaxesArrayList(List<TaxesModel> taxesArrayList) {
        this.taxesArrayList = taxesArrayList;
    }
}
