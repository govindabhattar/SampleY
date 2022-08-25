package com.tookancustomer.modules.customerSubscription;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerSubscriptionData implements Serializable {

    @SerializedName("plans")
    @Expose
    private ArrayList<PlanList> plans = null;
    @SerializedName("customerPlan")
    @Expose
    private ArrayList<CustomerPlan> customerPlan = null;

    public ArrayList<PlanList> getPlans() {
        return plans;
    }

    public void setPlans(ArrayList<PlanList> plans) {
        this.plans = plans;
    }

    public ArrayList<CustomerPlan> getCustomerPlan() {
        return customerPlan;
    }

    public void setCustomerPlan(ArrayList<CustomerPlan> customerPlan) {
        this.customerPlan = customerPlan;
    }

}