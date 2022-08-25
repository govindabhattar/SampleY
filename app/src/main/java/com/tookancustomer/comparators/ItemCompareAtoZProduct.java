package com.tookancustomer.comparators;

import com.tookancustomer.models.ProductCatalogueData.Datum;

import java.util.Comparator;

public class ItemCompareAtoZProduct implements Comparator<Datum> {
    public int compare(Datum item1, Datum item2) {
        return item1.getName().compareToIgnoreCase(item2.getName());
    }
}