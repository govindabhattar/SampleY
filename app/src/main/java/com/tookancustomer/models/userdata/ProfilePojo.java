package com.tookancustomer.models.userdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.models.BaseModel;

import java.io.Serializable;

/**
 * Created by cl-macmini-25 on 14/12/16.
 */

public class ProfilePojo extends BaseModel implements Serializable
{
	@SerializedName("data")
	@Expose
	private VendorDetails data;
	private final static long serialVersionUID = 5924269971725908688L;

	public VendorDetails getData() {
		return data;
	}

	public void setData(VendorDetails data) {
		this.data = data;
	}

}
