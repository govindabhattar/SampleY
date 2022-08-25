package com.tookancustomer.models.ProductCatalogueData;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.checkoutTemplate.model.Template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shankar on 1/12/17.
 */

public class ItemSelected implements Serializable {
    private Number unitCount = 1;
    private Double productTotalCalculatedPrice = 0.0;

    @SerializedName(value = "restaurant_item_id", alternate = {"item_id"})
    @Expose
    private Integer restaurantItemId = 0;
    @SerializedName(value = "customize_items", alternate = {"customisations_json"})
    @Expose
    private List<CustomizeItemSelected> customizeItemSelectedList;

    //    @SerializedName(value = "questionnaire_items", alternate = {"questionnaire_json"})
//    @Expose
//    private ArrayList<Template> questionnaireTemplate;
    @SerializedName(value = "quantity", alternate = {"item_quantity"})
    @Expose
    private Integer quantity;
    @SerializedName(value = "totalPrice", alternate = {"item_amount"})
    @Expose
    private Double totalPrice;
    @SerializedName(value = "customizationPrice", alternate = {"customization_amount"})
    @Expose
    private Double customizationPrice;
    @SerializedName("customizeText")
    @Expose
    private String customizeText;
    private String subCustomText;

    public Integer getRestaurantItemId() {
        return restaurantItemId;
    }

    public void setRestaurantItemId(Integer restaurantItemId) {
        this.restaurantItemId = restaurantItemId;
    }

    public Double getCustomizationPrice() {
        return customizationPrice;
    }

    public void setCustomizationPrice(Double customizationPrice) {
        this.customizationPrice = customizationPrice;
    }

    public List<CustomizeItemSelected> getCustomizeItemSelectedList() {
        if (customizeItemSelectedList == null) {
            customizeItemSelectedList = new ArrayList<>();
        }
        return customizeItemSelectedList;
    }

    public void setCustomizeItemSelectedList(List<CustomizeItemSelected> customizeItemSelectedList) {
        this.customizeItemSelectedList = customizeItemSelectedList;
    }


//    public ArrayList<Template> getQuestionnaireTemplate() {
//        return questionnaireTemplate;
//    }
//
//    public void setQuestionnaireTemplate(ArrayList<Template> questionnaireTemplate) {
//        this.questionnaireTemplate = questionnaireTemplate;
//    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemSelected) {
            ItemSelected io = (ItemSelected) o;
            if (io.restaurantItemId.equals(restaurantItemId)) {
                if (io.getCustomizeItemSelectedList().size() != getCustomizeItemSelectedList().size()) {
                    return false;
                }
                for (CustomizeItemSelected customizeItemSelected : io.getCustomizeItemSelectedList()) {
                    if (!getCustomizeItemSelectedList().contains(customizeItemSelected)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Integer getQuantity() {
        if (quantity == null) {
            quantity = 0;
        }
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Number getUnitCount() {
        if (unitCount == null) {
            unitCount = 1;
        }
        return unitCount;
    }

    public void setUnitCount(Number unitCount) {
        this.unitCount = unitCount;
    }

    public Double getTotalPriceWithQuantityWithCount() {
        return totalPrice * ((double) quantity) * (double) getUnitCount();
    }

    public Double getTotalPriceWithQuantity() {
        return totalPrice * ((double) quantity);
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getCustomizeText(Datum data) {

        if (/*TextUtils.isEmpty(customizeText) &&*/ getCustomizeItemSelectedList().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (CustomizeItemSelected customizeItemSelected : getCustomizeItemSelectedList()) {
                CustomizeItem customizeItem = new CustomizeItem();
                customizeItem.setCustomizeId(customizeItemSelected.getCustomizeId());
                int index = data.getCustomizeItem().indexOf(customizeItem);
                if (index > -1) {
                    customizeItem = data.getCustomizeItem().get(index);
                    StringBuilder sbOp = new StringBuilder();
                    for (Integer option : customizeItemSelected.getCustomizeOptions()) {
                        CustomizeOption customizeOption = new CustomizeOption();
                        customizeOption.setCustomizeOptionId(option);
                        int index1 = customizeItem.getCustomizeOptions().indexOf(customizeOption);
                        if (index1 > -1) {
                            customizeOption = customizeItem.getCustomizeOptions().get(index1);
                            if (sbOp.length() > 0) {
                                sbOp.append(", ");
                            }
                            if (sb.length() > 0) {
                                sb.append("\n");
                            }
                            sbOp.append(customizeOption.getCustomizeOptionName());
                        }
                    }
                    if (sbOp.length() != 0) {
                        sb.append(customizeItem.getCustomizeItemName()).append(": ").append(sbOp);
                    }

                }
            }
            setCustomizeText(sb.toString());
        }
        return customizeText;

    }

    public String getCustomizeText(Datum data, Integer custId) {
        String subCustomText = "";

//        if (TextUtils.isEmpty(subCustomText) &&
        if (getCustomizeItemSelectedList().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (CustomizeItemSelected customizeItemSelected : getCustomizeItemSelectedList()) {
                CustomizeItem customizeItem = new CustomizeItem();
                customizeItem.setCustomizeId(customizeItemSelected.getCustomizeId());
                int index = data.getCustomizeItem().indexOf(customizeItem);
                if (index > -1) {
                    customizeItem = data.getCustomizeItem().get(index);
                    StringBuilder sbOp = new StringBuilder();

                    for (Integer option : customizeItemSelected.getCustomizeOptions()) {
                        if (option.equals(custId)) {
                            CustomizeOption customizeOption = new CustomizeOption();
                            customizeOption.setCustomizeOptionId(option);


                            int index1 = customizeItem.getCustomizeOptions().indexOf(customizeOption);
                            if (index1 > -1) {
                                customizeOption = customizeItem.getCustomizeOptions().get(index1);
                                sbOp.append(customizeOption.getCustomizeOptionName());
                            }
                        }
                    }
//                    if (sb.length() > 0) {
//                        sb.append("\n");
//                    }
                    sb.append(sbOp);
                }
            }
            setSubCustomText(sb.toString());
            return sb.toString();
        }
        return subCustomText;
    }

    public void setSubCustomText(String subCustomText) {
        this.subCustomText = subCustomText;
    }

    public void setCustomizeText(String customizeText) {
        this.customizeText = customizeText;
    }

    public Double getProductTotalCalculatedPrice() {
        return productTotalCalculatedPrice;
    }

    public void setProductTotalCalculatedPrice(Double productTotalCalculatedPrice) {
        this.productTotalCalculatedPrice = productTotalCalculatedPrice;
    }
}