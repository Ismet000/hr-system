package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AttendanceRecord extends AbstractEntity {

    @SerializedName(value = "employee_id", alternate = {"employeeId"})
    private String employeeId;

    @SerializedName(value = "date", alternate = {"work_date"}) // yyyy-MM-dd
    private String workDate;

    @SerializedName(value = "clock_in", alternate = {"clockIn"})   // yyyy-MM-dd HH:mm:ss
    private String clockIn;

    @SerializedName(value = "clock_out", alternate = {"clockOut"}) // yyyy-MM-dd HH:mm:ss
    private String clockOut;

    @SerializedName(value = "hours_worked", alternate = {"hoursWorked"})
    private Double hoursWorked;

    @SerializedName("notes")
    private String notes;

    public AttendanceRecord() {}

    public AttendanceRecord(ResultSet rs, String a) throws SQLException {
        String alias = (a != null && !a.isEmpty()) ? (a.endsWith(".") ? a : a + ".") : "";
        setId(rs.getString(alias + "record_id"));
        employeeId  = rs.getString(alias + "employee_id");
        workDate    = rs.getString(alias + "date");
        clockIn     = rs.getString(alias + "clock_in");
        clockOut    = rs.getString(alias + "clock_out");
        try { hoursWorked = rs.getObject(alias + "hours_worked") == null ? null : rs.getDouble(alias + "hours_worked"); } catch (SQLException ignore) {}
        try { notes = rs.getString(alias + "notes"); } catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is required";
        if (workDate == null || workDate.isBlank())     return "work_date is required";
        return null;
    }

    /** INSERT: record_id, employee_id, work_date, clock_in, clock_out, hours_worked, notes */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, workDate);
        ps.setString(4, clockIn);
        ps.setString(5, clockOut);
        if (hoursWorked == null) ps.setNull(6, java.sql.Types.DECIMAL); else ps.setDouble(6, hoursWorked);
        ps.setString(7, notes);
    }

    /** UPDATE on clock-out: clock_out=?, hours_worked=? WHERE record_id=? */
    public void populateClockOutPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("record_id (id) required");
        ps.setString(1, clockOut);
        if (hoursWorked == null) ps.setNull(2, java.sql.Types.DECIMAL); else ps.setDouble(2, hoursWorked);
        ps.setString(3, getId());
    }

    // getters/setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getWorkDate() { return workDate; }
    public void setWorkDate(String workDate) { this.workDate = workDate; }
    public String getClockIn() { return clockIn; }
    public void setClockIn(String clockIn) { this.clockIn = clockIn; }
    public String getClockOut() { return clockOut; }
    public void setClockOut(String clockOut) { this.clockOut = clockOut; }
    public Double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Double hoursWorked) { this.hoursWorked = hoursWorked; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
