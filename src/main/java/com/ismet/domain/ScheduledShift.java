package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduledShift extends AbstractEntity {

    @SerializedName("employee_id") private String employeeId;
    @SerializedName("shift_date")  private String shiftDate; // yyyy-MM-dd
    @SerializedName("start_time")  private String startTime; // HH:mm:ss
    @SerializedName("end_time")    private String endTime;   // HH:mm:ss
    @SerializedName("status")      private String status;    // planned/confirmed/cancelled
    @SerializedName("note")        private String note;

    public ScheduledShift() {}
    public ScheduledShift(ResultSet rs, String a) throws SQLException {
        String p = (a!=null && !a.isEmpty()) ? (a.endsWith(".")?a:a+".") : "";
        setId(rs.getString(p+"shift_id"));
        employeeId = rs.getString(p+"employee_id");
        shiftDate  = rs.getString(p+"shift_date");
        startTime  = rs.getString(p+"start_time");
        endTime    = rs.getString(p+"end_time");
        try { status = rs.getString(p+"status"); } catch (SQLException ignore){}
        try { note   = rs.getString(p+"note"); }   catch (SQLException ignore){}
    }

    @Override public String validate() {
        if (employeeId==null || employeeId.isBlank()) return "employee_id is required";
        if (shiftDate==null  || shiftDate.isBlank())  return "shift_date is required";
        if (startTime==null  || startTime.isBlank())  return "start_time is required";
        if (endTime==null    || endTime.isBlank())    return "end_time is required";
        return null;
    }

    public void fillInsertPs(PreparedStatement ps) throws Exception {
        if (getId()==null || getId().isBlank()) setId(java.util.UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, shiftDate);
        ps.setString(4, startTime);
        ps.setString(5, endTime);
        ps.setString(6, status==null? "planned": status);
        ps.setString(7, note);
    }
    public void fillUpdatePs(PreparedStatement ps) throws Exception {
        if (getId()==null || getId().isBlank()) throw new Exception("shift_id required");
        ps.setString(1, shiftDate); ps.setString(2, startTime); ps.setString(3, endTime);
        ps.setString(4, status==null? "planned": status); ps.setString(5, note);
        ps.setString(6, getId());
    }

    // getters/setters...

    public String getEmployeeId() {
        return employeeId;
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }


    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
