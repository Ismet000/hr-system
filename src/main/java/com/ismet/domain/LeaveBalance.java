package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LeaveBalance extends AbstractEntity {
    @SerializedName("employee_id")   private String employeeId;
    @SerializedName("leave_type_id") private String leaveTypeId;
    @SerializedName("year")          private Integer year;

    @SerializedName("total_days")    private Integer totalDays;  // new name
    @SerializedName("used_days")     private Integer usedDays;


    @SerializedName("remaining_days") private transient Integer remainingDays;

    @SerializedName("created_at")    private String createdAt;

    public LeaveBalance() {}

    public LeaveBalance(ResultSet rs, String alias) throws SQLException {
        String a = alias != null && !alias.isEmpty() ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(a + "balance_id"));
        employeeId  = rs.getString(a + "employee_id");
        leaveTypeId = rs.getString(a + "leave_type_id");
        year        = rs.getInt(a + "year");
        totalDays   = rs.getInt(a + "total_days");
        usedDays    = rs.getInt(a + "used_days");
        try { createdAt = rs.getString(a + "created_at"); } catch (SQLException ignore) {}

        remainingDays = (totalDays != null ? totalDays : 0) - (usedDays != null ? usedDays : 0);
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is mandatory";
        if (leaveTypeId == null || leaveTypeId.isBlank()) return "leave_type_id is mandatory";
        if (year == null || year < 2000) return "year is mandatory and must be >= 2000";
        if (totalDays == null || totalDays < 0) return "total_days must be >= 0";
        if (usedDays == null) usedDays = 0;
        if (usedDays < 0) return "used_days must be >= 0";
        if (usedDays > totalDays) return "used_days cannot exceed total_days";
        remainingDays = totalDays - usedDays;
        return null;
    }

    /** For UPSERT: balance_id, employee_id, leave_type_id, year, total_days, used_days */
    public void populateUpsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        if (usedDays == null) usedDays = 0;
        if (totalDays == null) totalDays = 0;
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, leaveTypeId);
        ps.setInt(4, year);
        ps.setInt(5, totalDays);
        ps.setInt(6, usedDays);
    }

    // getters/setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String v) { employeeId = v; }
    public String getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(String v) { leaveTypeId = v; }
    public Integer getYear() { return year; }
    public void setYear(Integer v) { year = v; }
    public Integer getTotalDays() { return totalDays; }
    public void setTotalDays(Integer v) { totalDays = v; }
    public Integer getUsedDays() { return usedDays; }
    public void setUsedDays(Integer v) { usedDays = v; }
    public Integer getRemainingDays() { return remainingDays; }
    public void setRemainingDays(Integer v) { remainingDays = v; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String v) { createdAt = v; }
}
