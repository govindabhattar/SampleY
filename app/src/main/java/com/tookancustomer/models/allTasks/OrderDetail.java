
package com.tookancustomer.models.allTasks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("customizations")
    @Expose
    private List<Object> customizations = null;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;

    public List<Object> getCustomizations() {
        return customizations;
    }

    public void setCustomizations(List<Object> customizations) {
        this.customizations = customizations;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

}
