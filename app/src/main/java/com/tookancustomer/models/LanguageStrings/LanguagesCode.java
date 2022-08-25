
package com.tookancustomer.models.LanguageStrings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguagesCode {
    @SerializedName("language_code")
    @Expose
    private String languageCode;
    @SerializedName("language_name")
    @Expose
    private String languageName;
    @SerializedName("language_display_name")
    @Expose
    private String languageDisplayName;

    public LanguagesCode(String languageCode,String languageName,String languageDisplayName){
        this.languageCode=languageCode;
        this.languageName=languageName;
        this.languageDisplayName=languageDisplayName;
    }

    public String getLanguageCode() {
        return languageCode != null ? languageCode : "en";
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName != null ? languageName : "English";
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageDisplayName() {
        return languageDisplayName != null ? languageDisplayName : "English";
    }

    public void setLanguageDisplayName(String languageDisplayName) {
        this.languageDisplayName = languageDisplayName;
    }
}
