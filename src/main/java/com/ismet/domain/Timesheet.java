package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Timesheet extends AbstractEntity {
    @SerializedName("employee_id")
    private String employeeId;

    @SerializedName("week_start_date")
    private String weekStartDate;         // yyyy-MM-dd

    @SerializedName("total_hours")
    private Double totalHours;

    @SerializedName("overtime_hours")
    private Double overtimeHours;

    public Timesheet() {}

    public Timesheet(ResultSet rs, String alias) throws SQLException {
        String a = (alias != null && !alias.isEmpty()) ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(a + "timesheet_id"));
        employeeId    = rs.getString(a + "employee_id");
        weekStartDate = rs.getString(a + "week_start_date");
        totalHours    = rs.getDouble(a + "total_hours");
        overtimeHours = rs.getDouble(a + "overtime_hours");
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is mandatory";
        if (weekStartDate == null || weekStartDate.isBlank()) return "week_start_date is mandatory (yyyy-MM-dd)";
        if (totalHours == null) totalHours = 0d;
        if (overtimeHours == null) overtimeHours = 0d;
        return null;
    }

    /** INSERT:  */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        if (totalHours == null) totalHours = 0d;
        if (overtimeHours == null) overtimeHours = 0d;

        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, weekStartDate);
        ps.setDouble(4, totalHours);
        ps.setDouble(5, overtimeHours);
    }

    /** UPDATE: SET total_hours=?, overtime_hours=? WHERE timesheet_id=? */
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("timesheet_id (id) is required");
        if (totalHours == null) totalHours = 0d;
        if (overtimeHours == null) overtimeHours = 0d;

        ps.setDouble(1, totalHours);
        ps.setDouble(2, overtimeHours);
        ps.setString(3, getId());
    }

    // getters / setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(String weekStartDate) { this.weekStartDate = weekStartDate; }
    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }
    public Double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(Double overtimeHours) { this.overtimeHours = overtimeHours; }
}
