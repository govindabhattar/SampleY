package com.tookancustomer.cancellationPolicy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tookancustomer.appdata.StorefrontCommonData;
import com.tookancustomer.utility.Log;

public class CancellationRules {
    @SerializedName("order_status")
    @Expose
    private int status;
    @SerializedName("percentage_cancellation_fees")
    @Expose
    private double percentageCharge;
    @SerializedName("fixed_cancellation_fees")
    @Expose
    private double fixedCharge;
    @SerializedName("cancellation_threshold")
    @Expose
    private int minutes;
    private int days;
    private int hours;
    private String merchant;
    private String order;
    private String dispatched;


    public CancellationRules(int status, String merchant, String order, String dispatched, double percentageCharge, double fixedCharge) {
        this.status = status;
        this.merchant = merchant;
        this.order = order;
        this.dispatched = dispatched;
        this.percentageCharge = percentageCharge;
        this.fixedCharge = fixedCharge;
    }

    public CancellationRules(int status, String merchant, String order, String dispatched,
                             double percentageCharge, double fixedCharge,
                             int minutes) {
        this.status = status;
        this.merchant = merchant;
        this.order = order;
        this.dispatched = dispatched;
        this.percentageCharge = percentageCharge;
        this.fixedCharge = fixedCharge;
        setDayHoursMinutes(minutes);
    }

    private void setDayHoursMinutes(int minutes) {
        this.days = minutes / (24 * 60);
        this.hours = (minutes % (24 * 60)) / 60;
        this.minutes = (minutes % (24 * 60)) % 60;
        Log.e("===========", "days==" + days + "hours===" + hours + "minutes===" + this.minutes);

    }

    public int getStatus() {
        return status;
    }

    public String getMerchant() {
        return merchant = StorefrontCommonData.getTerminology().getMerchant();
    }

    public String getOrder() {
        return order = StorefrontCommonData.getTerminology().getOrder();
    }

    public String getDispatched() {
        return dispatched = StorefrontCommonData.getTerminology().getDispatched();
    }

    public double getPercentageCharge() {
        return percentageCharge;
    }

    public double getFixedCharge() {
        return fixedCharge;
    }

    public int getDays() {
        return days = minutes / (24 * 60);
    }

    public int getHours() {
        return hours = (minutes % (24 * 60)) / 60;
    }

    public int getMinutes() {
        return (minutes % (24 * 60)) % 60;
    }

    public int getCurrentThreshold(){
        return minutes;
    }
}
