
package com.tookancustomer.models.userpages;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.appConfiguration.DynamicPagesDetails;

public class UserPagesData {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("template_data")
    @Expose
    private ArrayList<DynamicPagesDetails> templateData = null;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DynamicPagesDetails> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(ArrayList<DynamicPagesDetails> templateData) {
        this.templateData = templateData;
    }

}
