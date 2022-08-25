package com.tookancustomer.models.NLevelWorkFlowModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;

public class ButtonOptions implements Serializable {

    @SerializedName("add")
    @Expose
    public String add;
    @SerializedName("remove")
    @Expose
    public String remove;

    public ButtonOptions(String add,String remove){
        this.add=add;
        this.remove=remove;

    }

    public String getAdd() {
        return add != null ? Utils.capitaliseWords(add) : "Add";
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getRemove() {
        return remove != null ? Utils.capitaliseWords(remove) : "Remove";
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }

}
