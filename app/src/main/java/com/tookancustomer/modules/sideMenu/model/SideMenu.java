package com.tookancustomer.modules.sideMenu.model;

public class SideMenu {
    private String option;
    private int optionDrawable;

    public SideMenu(String option, int optionDrawable) {
        this.option = option;
        this.optionDrawable = optionDrawable;
    }

    public String getOption() {
        return option;
    }

    public int getOptionDrawable() {
        return optionDrawable;
    }
}
