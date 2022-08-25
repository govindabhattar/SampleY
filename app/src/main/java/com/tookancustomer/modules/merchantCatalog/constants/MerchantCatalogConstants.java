package com.tookancustomer.modules.merchantCatalog.constants;

import android.content.Context;

import com.tookancustomer.R;

public interface MerchantCatalogConstants {

    /**
     * Lists all the category Layout Types
     * Layout types handled for categories are--> LIST, BANNER, GRID, MENU (Menu will be handled at activity level only)
     */
    enum CategoryLayoutTypes {
        LIST_LAYOUT(1, R.layout.itemview_category_list),
        BANNER_LAYOUT(2, R.layout.itemview_category_banner),
        GRID_LAYOUT(5, R.layout.itemview_category_grid);

        public final int layoutValue;
        public final int layoutMode;

        CategoryLayoutTypes(int layoutValue, int layoutMode) {
            this.layoutValue = layoutValue;
            this.layoutMode = layoutMode;
        }

        public static int getLayoutModeByValue(Context context, int layoutValue) {
            CategoryLayoutTypes layoutType = null;
            for (CategoryLayoutTypes status : values()) {
                if (status.layoutValue == layoutValue) {
                    layoutType = status;
                    break;
                }
            }
            return layoutType == null ? LIST_LAYOUT.layoutMode : layoutType.layoutMode;
        }
    }

    /**
     * Lists all the product Layout Types
     * Layout types handled for product are--> LIST, BANNER , GRID
     */
    enum ProductLayoutTypes {
        LIST_LAYOUT(1, R.layout.itemview_product_list),
        BANNER_LAYOUT(2, R.layout.itemview_product_banner),
        GRID_LAYOUT(3, R.layout.itemview_product_grid);

        public final int layoutValue;
        public final int layoutMode;

        ProductLayoutTypes(int layoutValue, int layoutMode) {
            this.layoutValue = layoutValue;
            this.layoutMode = layoutMode;
        }

        public static int getLayoutModeByValue(Context context, int layoutValue) {
            ProductLayoutTypes layoutType = null;
            for (ProductLayoutTypes status : values()) {
                if (status.layoutValue == layoutValue) {
                    layoutType = status;
                    break;
                }
            }
            return layoutType == null ? LIST_LAYOUT.layoutMode : layoutType.layoutMode;
        }
    }

    /**
     * List all the ButtonTypes
     */
    enum ButtonTypes {
        SELECT_TEXT_BUTTON(1), //Add remove button
        ADD_AND_REMOVE_BUTTON(2), // + -  button
        NEXT_ARROW_BUTTON(3),
        HIDDEN_BUTTON(4);

        public final int buttonValue;

        ButtonTypes(int buttonValue) {
            this.buttonValue = buttonValue;
        }

    }
}