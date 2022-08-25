package com.tookancustomer.comparators;

import com.tookancustomer.models.ProductCatalogueData.Datum;

import java.util.Comparator;

public class ItemComparePriceHighToLowProduct implements Comparator<Datum> {
    public int compare(Datum item1, Datum item2) {
        return Double.compare(item2.getPrice().doubleValue(), item1.getPrice().doubleValue());
    }
}