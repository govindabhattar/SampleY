
package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Extras implements Serializable {

    @SerializedName("req_popup")
    @Expose
    private List<String> reqPopup = null;
    @SerializedName("invoice_html")
    @Expose
    private String invoiceHtml;

    public List<String> getReqPopup() {
        return reqPopup;
    }

    public void setReqPopup(List<String> reqPopup) {
        this.reqPopup = reqPopup;
    }

    public String getInvoiceHtml() {
        return invoiceHtml;
    }

    public void setInvoiceHtml(String invoiceHtml) {
        this.invoiceHtml = invoiceHtml;
    }

}
