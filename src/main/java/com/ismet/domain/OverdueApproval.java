package com.ismet.domain;

import com.google.gson.annotations.SerializedName;

public class OverdueApproval {
    @SerializedName("request_id")   public String requestId;
    @SerializedName("employee_id")  public String employeeId;
    @SerializedName("leave_type_id")public String leaveTypeId;
    @SerializedName("request_date") public String requestDate;
    @SerializedName("days_pending") public int daysPending;
}
