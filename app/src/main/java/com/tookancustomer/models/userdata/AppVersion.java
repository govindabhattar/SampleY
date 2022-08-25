
package com.tookancustomer.models.userdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AppVersion implements Serializable {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("form_id")
    @Expose
    private Integer formId;
    @SerializedName("app_device_type")
    @Expose
    private Integer appDeviceType;
    @SerializedName("app_version")
    @Expose
    private Integer appVersion;
    @SerializedName("is_force")
    @Expose
    private Integer isForce;

    /**
     * 
     * @return
     *     The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The formId
     */
    public Integer getFormId() {
        return formId;
    }

    /**
     * 
     * @param formId
     *     The form_id
     */
    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    /**
     * 
     * @return
     *     The appDeviceType
     */
    public Integer getAppDeviceType() {
        return appDeviceType;
    }

    /**
     * 
     * @param appDeviceType
     *     The app_device_type
     */
    public void setAppDeviceType(Integer appDeviceType) {
        this.appDeviceType = appDeviceType;
    }

    /**
     * 
     * @return
     *     The appVersion
     */
    public Integer getAppVersion() {
        return appVersion;
    }

    /**
     * 
     * @param appVersion
     *     The app_version
     */
    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * 
     * @return
     *     The isForce
     */
    public Integer getIsForce() {
        return isForce;
    }

    /**
     * 
     * @param isForce
     *     The is_force
     */
    public void setIsForce(Integer isForce) {
        this.isForce = isForce;
    }

}
