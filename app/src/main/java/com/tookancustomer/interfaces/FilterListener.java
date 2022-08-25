package com.tookancustomer.interfaces;


import com.tookancustomer.models.ProductFilters.AllowValue;

import java.util.ArrayList;

public interface FilterListener {
    void setFilterValues(ArrayList<AllowValue> al, String filterHeader);
}