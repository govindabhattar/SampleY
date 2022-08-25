
package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Keys;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.List;

public class SignupTemplateData implements Serializable {

    @SerializedName("display_name")
    @Expose
    private String displayName;
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
//    @SerializedName("value")
//    @Expose
//    private Integer value;
    @SerializedName("data")
    @Expose
    private Object data;
    //    @SerializedName("input")
//    @Expose
//    private Input input;
    @SerializedName("template_id")
    @Expose
    private String templateId;
    @SerializedName("allowed_values")
    @Expose
    private List<SingleSelect> allowedvalues = null;

    @SerializedName("dropdown")
    @Expose
    private List<Dropdown> dropdown = null;
    @SerializedName("attribute")
    @Expose
    private Integer attribute;

    private transient Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Object dataToShow;

    public Object getDataToShow() {
        return dataToShow;
    }

    public void setDataToShow(final Object dataToShow) {
        this.dataToShow = dataToShow;
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
     * @return The label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return The dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType The data_type
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return The appSide
     */
    public String getAppSide() {
        return appSide;
    }

    /**
     * @param appSide The app_side
     */
    public void setAppSide(String appSide) {
        this.appSide = appSide;
    }

    /**
     * @return The required
     */
    public Integer getRequired() {
        return required;
    }

    /**
     * @param required The required
     */
    public void setRequired(Integer required) {
        this.required = required;
    }

//    /**
//     * @return The value
//     */
//    public Integer getValue() {
//        return value;
//    }
//
//    /**
//     * @param value The value
//     */
//    public void setValue(Integer value) {
//        this.value = value;
//    }

    /**
     * @return The data
     */
    public Object getData() {

        return data == null ? "" : data;

    }

    /**
     * @param data The data
     */
    public void setData(Object data) {
        this.data = data;
    }

//    /**
//     *
//     * @return
//     *     The input
//     */
//    public Input getInput() {
//        return input;
//    }
//
//    /**
//     *
//     * @param input
//     *     The input
//     */
//    public void setInput(Input input) {
//        this.input = input;
//    }

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
     * @return The dropdown
     */
    public List<Dropdown> getDropdown() {
        return dropdown;
    }

    /**
     * @param dropdown The dropdown
     */
    public void setDropdown(List<Dropdown> dropdown) {
        this.dropdown = dropdown;
    }

    public List<SingleSelect> getAllowedvalues() {
        return allowedvalues;
    }

    public void setAllowedvalues(List<SingleSelect> allowedvalues) {
        this.allowedvalues = allowedvalues;
    }


    public boolean isShow() {
        return Utils.toInt(appSide) != 2;
    }


    public Integer getAttribute() {
        return attribute;
    }

    public void setAttribute(Integer attribute) {
        this.attribute = attribute;
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

    public String getDisplayName() {
        return (displayName != null ? displayName : Utils.capitaliseWords(label.replace("_"," ")));
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
