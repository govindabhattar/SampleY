
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserOptions implements Serializable {

    @SerializedName("template")
    @Expose
    private String template;
    @SerializedName("template_id")
    @Expose
    private String templateId;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<>();

    @SerializedName("extras")
    @Expose
    private Extras extras;
    @SerializedName("payment_formula")
    @Expose
    private String paymentFormula;

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }

    public String getPaymentFormula() {
        return paymentFormula;
    }

    public void setPaymentFormula(String paymentFormula) {
        this.paymentFormula = paymentFormula;
    }

    /**
     * @return The template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template The template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * @return The templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId The template_id
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }


    public UserOptions(String template, String templateId, List<Item> items) {
        this.template = template;
        this.templateId = templateId;
        this.items = items;

    }

    public UserOptions(UserOptions userOptions) {
        this.template = userOptions.getTemplate();
        this.templateId = userOptions.getTemplateId();
        this.items = userOptions.getItems();
    }

}
