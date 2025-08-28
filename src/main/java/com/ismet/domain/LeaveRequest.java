package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class LeaveRequest extends AbstractEntity {
    @SerializedName(value = "employee_id",   alternate = {"employeeId"})
    private String employeeId;

    @SerializedName(value = "leave_type_id", alternate = {"leaveTypeId"})
    private String leaveTypeId;

    @SerializedName(value = "start_date",    alternate = {"startDate"})
    private String startDate; // yyyy-MM-dd

    @SerializedName(value = "end_date",      alternate = {"endDate"})
    private String endDate;   // yyyy-MM-dd

    @SerializedName("status")
    private String status;

    @SerializedName(value = "approver_id",   alternate = {"approverId"})
    private String approverId;

    @SerializedName("comments")
    private String comments;

    @SerializedName("request_date")  /
    private String requestDate;

//    @SerializedName(value = "decision_date",   alternate = {"decisionDate"})
//    private Timestamp decisionDate;


    public LeaveRequest() {}

    public LeaveRequest(ResultSet rs, String alias) throws SQLException {
        alias = (alias != null && !alias.isEmpty()) ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(alias + "request_id"));
        employeeId  = rs.getString(alias + "employee_id");
        leaveTypeId = rs.getString(alias + "leave_type_id");
        startDate   = rs.getString(alias + "start_date");
        endDate     = rs.getString(alias + "end_date");
        status      = rs.getString(alias + "status");
        approverId  = rs.getString(alias + "approver_id");
        comments    = rs.getString(alias + "comments");
        try { requestDate = rs.getString(alias + "request_date"); } catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is required";
        if (leaveTypeId == null || leaveTypeId.isBlank()) return "leave_type_id is required";
        if (startDate == null || startDate.isBlank())     return "start_date is required";
        if (endDate == null || endDate.isBlank())         return "end_date is required";
        if (startDate.compareTo(endDate) > 0)             return "start_date must be <= end_date";
        return null;
    }

    /** INSERT */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        if (status == null || status.isBlank()) status = "pending";
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, leaveTypeId);
        ps.setString(4, startDate);
        ps.setString(5, endDate);
        ps.setString(6, status);
        ps.setString(7, approverId); /
        ps.setString(8, comments);
    }

    /** DECISION */
    public void populateDecisionPs(PreparedStatement ps, String newStatus, String approver) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("request_id (id) required");
        ps.setString(1, newStatus);
        ps.setString(2, approver);
        ps.setString(3, getId());
    }

    // getters/setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(String leaveTypeId) { this.leaveTypeId = leaveTypeId; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApproverId() { return approverId; }
    public void setApproverId(String approverId) { this.approverId = approverId; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public String getRequestDate() { return requestDate; }
}
