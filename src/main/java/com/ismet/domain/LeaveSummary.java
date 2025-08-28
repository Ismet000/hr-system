package com.ismet.domain;

import com.google.gson.annotations.SerializedName;

public class LeaveSummary {
    @SerializedName("employee_id") public String employeeId;
    @SerializedName("from")        public String from;
    @SerializedName("to")          public String to;

    @SerializedName("pending_count")   public int pendingCount;
    @SerializedName("pending_days")    public int pendingDays;

    @SerializedName("approved_count")  public int approvedCount;
    @SerializedName("approved_days")   public int approvedDays;

    @SerializedName("denied_count")    public int deniedCount;
    @SerializedName("denied_days")     public int deniedDays;

    @SerializedName("cancelled_count") public int cancelledCount;
    @SerializedName("cancelled_days")  public int cancelledDays;

    public LeaveSummary(String employeeId, String from, String to) {
        this.employeeId = employeeId; this.from = from; this.to = to;
    }
}
