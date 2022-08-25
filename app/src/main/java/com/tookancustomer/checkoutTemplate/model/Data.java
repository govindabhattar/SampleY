
package com.tookancustomer.checkoutTemplate.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("template")
    @Expose
    private ArrayList<Template> template = null;

    public ArrayList<Template> getTemplate() {
        return template;
    }

    public void setTemplate(ArrayList<Template> template) {
        this.template = template;
    }

}
