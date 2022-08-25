
package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.Constants;

import java.io.Serializable;

public class Button implements Serializable {

    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("button_names")
    @Expose
    public ButtonOptions buttonNames;

    public Integer getType() {
        return type != null ? type : Constants.NLevelButtonType.HIDDEN_BUTTON.buttonValue;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ButtonOptions getButtonNames() {
        return buttonNames != null ? buttonNames : new ButtonOptions("", "");
    }

    public void setButtonNames(ButtonOptions buttonNames) {
        this.buttonNames = buttonNames;
    }
}
