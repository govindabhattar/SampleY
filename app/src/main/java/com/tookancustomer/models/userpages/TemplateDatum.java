
package com.tookancustomer.models.userpages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TemplateDatum {

    @SerializedName("page_id")
    @Expose
    private int pageId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("template_data")
    @Expose
    private String templateData;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("creation_date_time")
    @Expose
    private String creationDateTime;
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("is_admin_page")
    @Expose
    private int isAdminPage;

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIsAdminPage() {
        return isAdminPage;
    }

    public void setIsAdminPage(int isAdminPage) {
        this.isAdminPage = isAdminPage;
    }

}
