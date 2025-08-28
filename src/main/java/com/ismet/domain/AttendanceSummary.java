package com.ismet.domain;

import com.google.gson.annotations.SerializedName;

public class AttendanceSummary {

    @SerializedName("employee_id") public String employeeId;
    @SerializedName("from")        public String from;
    @SerializedName("to")          public String to;
    @SerializedName("total_hours") public double totalHours;
    @SerializedName("days_worked") public int daysWorked;
    @SerializedName("overtime_hours") public double overtimeHours;
    @SerializedName("weekly_limit")   public double weeklyLimit;

    public AttendanceSummary(String employeeId, String from, String to,
                             double totalHours, int daysWorked,
                             double overtimeHours, double weeklyLimit) {
        this.employeeId = employeeId;
        this.from = from;
        this.to = to;
        this.totalHours = totalHours;
        this.daysWorked = daysWorked;
        this.overtimeHours = overtimeHours;
        this.weeklyLimit = weeklyLimit;
    }
}
