package com.ismet.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AttendanceComplianceReport {

    @SerializedName("employee_id")
    public String employeeId;

    @SerializedName("from")
    public String from;

    @SerializedName("to")
    public String to;

    @SerializedName("daily_limit")
    public double dailyLimit;

    @SerializedName("weekly_limit")
    public double weeklyLimit;

    @SerializedName("absent_days")
    public List<String> absentDays = new ArrayList<>();

    @SerializedName("excessive_hours_days")
    public List<DayHours> excessiveDays = new ArrayList<>();

    @SerializedName("weekly_overtime")
    public List<WeekHours> weeklyOvertime = new ArrayList<>();

    public static class DayHours {
        @SerializedName("date")  public String date;
        @SerializedName("hours") public double hours;
        public DayHours(String d, double h) { this.date = d; this.hours = h; }
    }

    public static class WeekHours {
        @SerializedName("week_start") public String weekStart;
        @SerializedName("total")      public double total;
        @SerializedName("overtime")   public double overtime;
        public WeekHours(String ws, String format, double t, double ot) {
            this.weekStart = ws; this.total = t; this.overtime = ot;
        }
    }
}
