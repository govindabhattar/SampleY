
package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LayoutData implements Serializable {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("is_enabled")
    @Expose
    public Integer isEnabled;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("lines")
    @Expose
    public ArrayList<Line> lines = new ArrayList<>();
    @SerializedName("images")
    @Expose
    public ArrayList<Image> images = new ArrayList<>();
    @SerializedName("buttons")
    @Expose
    public ArrayList<Button> buttons = new ArrayList<>();

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ArrayList<Line> getLines() {
        return lines != null ? lines : new ArrayList<Line>();
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public ArrayList<Image> getImages() {
        return images != null ? images : new ArrayList<Image>();
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<Button> getButtons() {
        return buttons != null ? buttons : new ArrayList<Button>();
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }
}
