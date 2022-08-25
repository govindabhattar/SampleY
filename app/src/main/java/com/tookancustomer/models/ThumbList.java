
package com.tookancustomer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ThumbList  implements Serializable {

    @SerializedName("400x400")
    @Expose
    private String _400x400 = "";
    @SerializedName("250x250")
    @Expose
    private String _250x250 = "";

    public String get400x400() {
        return _400x400 != null ? _400x400 : "";
    }

    public void set400x400(String _400x400) {
        this._400x400 = _400x400;
    }

    public String get250x250() {
        return _250x250 != null ? _250x250 : "";
    }

    public void set250x250(String _250x250) {
        this._250x250 = _250x250;
    }

}
