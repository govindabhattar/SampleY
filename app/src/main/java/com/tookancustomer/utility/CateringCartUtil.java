package com.tookancustomer.utility;

import android.app.Activity;

import com.tookancustomer.R;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.appdata.TerminologyStrings;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItem;
import com.tookancustomer.models.ProductCatalogueData.CustomizeItemSelected;
import com.tookancustomer.models.ProductCatalogueData.Datum;
import com.tookancustomer.models.ProductCatalogueData.ItemSelected;

public class CateringCartUtil implements TerminologyStrings {

    public static boolean validateDataAtAddOnsPage(ItemSelected itemSelected, Datum productDataItem, Activity activity) {
        if (itemSelected.getQuantity() < productDataItem.getMinProductquantity()) {
            Utils.snackBar(activity,
                    StorefrontCommonData.getString(activity, R.string.error_msg_product_quantity_less_than_minimum_quantity) +
                            StorefrontCommonData.getString(activity, R.string.empty_space) +
                            productDataItem.getMinProductquantity());
            return false;
        } else {
            for (int i = 0; i < productDataItem.getCustomizeItem().size(); i++) {
                CustomizeItemSelected customizeItemSelected = null;
                CustomizeItem customizeItem = productDataItem.getCustomizeItem().get(i);
                for (int j = 0; j < itemSelected.getCustomizeItemSelectedList().size(); j++) {

                    if (customizeItem.getCustomizeId() ==
                            itemSelected.getCustomizeItemSelectedList().get(j).getCustomizeId() &&
                            itemSelected.getCustomizeItemSelectedList().get(j).getCustomizeOptions().size() > 0) {
                        customizeItemSelected = itemSelected.getCustomizeItemSelectedList().get(j);
                        break;
                    }
                }

                if (customizeItemSelected != null) {

                    if (customizeItem.getMinimumSelectionRequired() == 1) {
                        if (customizeItem.getIsCheckBox() == 1)
                            if (customizeItemSelected.getCustomizeOptions().size() != customizeItem.getMinimumSelection()) {
                                Utils.snackBar(activity,
                                        StorefrontCommonData.getString(activity, R.string.in_name_selected_quantity_equal_to_number)
                                                .replace(NAME, customizeItem.getCustomizeItemName())
                                                .replace(QUANTITY, customizeItem.getMinimumSelection() + ""));
                                return false;
                            }

                    }

                } else if (customizeItem.getMinimumSelectionRequired() == 1) {
                    Utils.snackBar(activity, StorefrontCommonData.getString(activity, R.string.itemName_is_required).replace(NAME, customizeItem.getCustomizeItemName()));
                    return false;
                }

            }

        }

        return true;
    }


}
