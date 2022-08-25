
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.List;

public class Coupons implements Serializable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("app_side")
    @Expose
    private String appSide;
    @SerializedName("required")
    @Expose
    private Integer required;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("template_id")
    @Expose
    private String templateId;
    @SerializedName("dropdown")
    @Expose
    private List<Dropdown> dropdown = null;

    private transient Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    /**
     * Method to get the Current View of this Class
     *
     * @return
     */
    public Object getView() {

        if (listener != null)
            return listener.getView();

        return null;
    }

    /**
     *
     * @return
     *     The label
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param label
     *     The label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return
     *     The dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     *
     * @param dataType
     *     The data_type
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     *
     * @return
     *     The appSide
     */
    public String getAppSide() {
        return appSide;
    }

    /**
     *
     * @param appSide
     *     The app_side
     */
    public void setAppSide(String appSide) {
        this.appSide = appSide;
    }

    /**
     *
     * @return
     *     The required
     */
    public Integer getRequired() {
        return required;
    }

    /**
     *
     * @param required
     *     The required
     */
    public void setRequired(Integer required) {
        this.required = required;
    }

    /**
     *
     * @return
     *     The value
     */
    public Integer getValue() {
        return value;
    }

    /**
     *
     * @param value
     *     The value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     *
     * @return
     *     The data
     */
    public Object getData() {

        return data==null?"":data;

    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     *
     * @return
     *     The templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     *
     * @param templateId
     *     The template_id
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     *
     * @return
     *     The dropdown
     */
    public List<Dropdown> getDropdown() {
        return dropdown;
    }

    /**
     *
     * @param dropdown
     *     The dropdown
     */
    public void setDropdown(List<Dropdown> dropdown) {
        this.dropdown = dropdown;
    }

    public boolean isShow() {
        return Utils.toInt(appSide) != 2;
    }

    /**
     * Interface to make the CustomFields
     * Listen to the external events
     */
    public interface Listener {

        Object getView();

    }


    /**
     * Method to check whether the Field is Read-Only
     *
     * @return
     */
    public boolean isReadOnly() {

        String value = Utils.assign(appSide);
        return value.equals(Keys.CustomField.EditableType.READ_ONLY) || value.equals(Keys.CustomField.EditableType.READABLE);
    }

}
