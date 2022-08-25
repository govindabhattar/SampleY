package com.tookancustomer.comparators;

import com.tookancustomer.models.ProductCatalogueData.Datum;

import java.util.Comparator;

public class ItemComparePriceLowToHighProduct implements Comparator<Datum> {
    public int compare(Datum item1, Datum item2) {
        return Double.compare(item1.getPrice().doubleValue(), item2.getPrice().doubleValue());
    }
}