package com.tookancustomer.countryCodePicker.model;

import java.util.Comparator;

public class Country implements Comparator<Country> {
    private String countryName,countryCode,countryShortCode;

    public Country(String countryName, String countryCode, String countryShortCode) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.countryShortCode = countryShortCode;
    }

    public Country() {
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryShortCode() {
        return countryShortCode;
    }

    public void setCountryShortCode(String countryShortCode) {
        this.countryShortCode = countryShortCode;
    }

    @Override
    public int compare(Country o1, Country o2) {
        return o1.countryName.compareToIgnoreCase(o2.countryName);
    }
}
