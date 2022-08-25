package com.tookancustomer.models.ProductCatalogueData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shankar on 1/12/17.
 */

public class CustomizeItem implements Serializable {

    @SerializedName("customize_id")
    @Expose
    private Integer customizeId;
    @SerializedName(value = "name", alternate = {"customize_item_name"})
    @Expose
    private String customizeItemName;
    @SerializedName("customize_options")
    @Expose
    private List<CustomizeOption> customizeOptions = null;
    @SerializedName("is_check_box")
    @Expose
    private Integer isCheckBox;
    @SerializedName("minimum_selection")
    @Expose
    private int minimumSelection = 1;
    @SerializedName("minimum_selection_required")
    @Expose
    private int minimumSelectionRequired;
    @SerializedName("type")
    @Expose
    private String type;


    public Integer getCustomizeId() {
        return customizeId;
    }

    public void setCustomizeId(Integer customizeId) {
        this.customizeId = customizeId;
    }

    public String getCustomizeItemName() {
        return customizeItemName != null ? customizeItemName : "";
    }

    public void setCustomizeItemName(String customizeItemName) {
        this.customizeItemName = customizeItemName;
    }

    public List<CustomizeOption> getCustomizeOptions() {
        if (customizeOptions == null) {
            customizeOptions = new ArrayList<>();
        }
        return customizeOptions;
    }

    public void setCustomizeOptions(List<CustomizeOption> customizeOptions) {
        this.customizeOptions = customizeOptions;
    }

    public Integer getIsCheckBox() {
        if (isCheckBox == null) {
            return 0;
        }
        return isCheckBox;
    }

    public void setIsCheckBox(Integer isCheckBox) {
        this.isCheckBox = isCheckBox;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomizeItem) {
            return ((CustomizeItem) o).customizeId.equals(customizeId);
        } else {
            return false;
        }
    }

    public int getMinimumSelection() {
//        return minimumSelection != 1 ? minimumSelection : 2;
        return minimumSelection;
    }

    public void setMinimumSelection(int minimumSelection) {
        this.minimumSelection = minimumSelection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinimumSelectionRequired() {
//        return minimumSelectionRequired == 0 ? 1 : minimumSelectionRequired;
        return minimumSelectionRequired;
    }

    public void setMinimumSelectionRequired(int minimumSelectionRequired) {
        this.minimumSelectionRequired = minimumSelectionRequired;
    }
}