package com.tookancustomer.models.LanguageStrings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.util.HashMap;
import java.util.Map;

public class LanguageStringsModel  {
//    @SerializedName("data")
//    @Expose
    private Map<String,String> languageStrings;

    public Map<String,String> getLanguageStrings() {
        return languageStrings;
    }

    public void setLanguageStrings(Map<String,String> languageStrings) {
        this.languageStrings = languageStrings;
    }
}
