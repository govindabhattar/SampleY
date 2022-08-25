package com.tookancustomer.models.subscription;

import java.io.Serializable;

/**
 * Created by Ashutosh Ojha on 2/14/19.
 */
public class Days implements Serializable {
    private boolean isSelected;
    private boolean isActive;
    private String displayValue, displayValueShort;
    private int dayId;

    public Days(final boolean isSelected, final boolean isActive, final String displayValue, String displayValueShort, final int dayId) {
        this.isSelected = isSelected;
        this.isActive = isActive;
        this.displayValue = displayValue;
        this.displayValueShort = displayValueShort;
        this.dayId = dayId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(final boolean selected) {
        isSelected = selected;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(final String displayValue) {
        this.displayValue = displayValue;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(final boolean active) {
        isActive = active;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(final int dayId) {
        this.dayId = dayId;
    }

    public String getDisplayValueShort() {
        return displayValueShort;
    }

    public void setDisplayValueShort(final String displayValueShort) {
        this.displayValueShort = displayValueShort;
    }


}
