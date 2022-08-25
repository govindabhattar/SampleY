
package com.tookancustomer.models.taskdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.utility.Utils;

import java.io.Serializable;
import java.util.List;

public class CustomField implements Serializable
{
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
	@SerializedName("value")
	@Expose
	private Integer value;
	@SerializedName("data")
	@Expose
	private Object data;
	@SerializedName("fleet_data")
	@Expose
	private Object fleetData;

	@SerializedName("template_id")
	@Expose
	private String templateId;
	@SerializedName("dropdown")
	@Expose
	private List<Dropdown> dropdown = null;

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getAppSide()
	{
		return appSide;
	}

	public void setAppSide(String appSide)
	{
		this.appSide = appSide;
	}

	public Integer getRequired()
	{
		return required;
	}

	public void setRequired(Integer required)
	{
		this.required = required;
	}

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		this.value = value;
	}

	public String getData()
	{
		return data == null ? "" : data.toString();
	}

	public void setData(String data)
	{
		this.data = data;
	}


	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public List<Dropdown> getDropdown()
	{
		return dropdown;
	}

	public void setDropdown(List<Dropdown> dropdown)
	{
		this.dropdown = dropdown;
	}

	public String getFleetData()
	{
		return fleetData != null && fleetData.toString().isEmpty() ? "-" : fleetData!=null?fleetData.toString():null;
	}

	public void setFleetData(String fleetData)
	{
		this.fleetData = fleetData;
	}

	public boolean isShow() {
		return Utils.toInt(appSide) != 2;
	}

	public String getDisplayName() {
		return (displayName != null ? displayName : Utils.capitaliseWords(label.replace("_"," ")));
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
