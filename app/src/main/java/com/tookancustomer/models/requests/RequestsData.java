
package com.tookancustomer.models.requests;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestsData implements Serializable {

    @SerializedName("pending_count")
    @Expose
    private Integer pendingCount;
    @SerializedName("dispatched_count")
    @Expose
    private Integer dispatchedCount;
    @SerializedName("completed_count")
    @Expose
    private Integer completedCount;
    @SerializedName("cancelled_count")
    @Expose
    private Integer cancelledCount;
    @SerializedName("pending")
    @Expose
    private List<Pending> pending = null;
    @SerializedName("dispatched")
    @Expose
    private List<Pending> dispatched = null;
    @SerializedName("completed")
    @Expose
    private List<Pending> completed = null;
    @SerializedName("cancelled")
    @Expose
    private List<Pending> cancelled = null;

    public Integer getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Integer getDispatchedCount() {
        return dispatchedCount;
    }

    public void setDispatchedCount(Integer dispatchedCount) {
        this.dispatchedCount = dispatchedCount;
    }

    public Integer getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
    }

    public Integer getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(Integer cancelledCount) {
        this.cancelledCount = cancelledCount;
    }

    public List<Pending> getPending() {
        return pending;
    }

    public void setPending(List<Pending> pending) {
        this.pending = pending;
    }

    public List<Pending> getDispatched() {
        return dispatched;
    }

    public void setDispatched(List<Pending> dispatched) {
        this.dispatched = dispatched;
    }

    public List<Pending> getCompleted() {
        return completed;
    }

    public void setCompleted(List<Pending> completed) {
        this.completed = completed;
    }

    public List<Pending> getCancelled() {
        return cancelled;
    }

    public void setCancelled(List<Pending> cancelled) {
        this.cancelled = cancelled;
    }

}
