package com.tookancustomer.models.listingdata;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LayoutData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_enabled")
    @Expose
    private Integer isEnabled;
    @SerializedName("is_default")
    @Expose
    private Integer isDefault;
    @SerializedName("layout_type")
    @Expose
    private Integer layoutType;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("lines")
    @Expose
    private List<Line> lines = null;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

}
