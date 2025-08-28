package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AttendanceException extends AbstractEntity {

    @SerializedName("employee_id")  private String employeeId;
    @SerializedName("date")         private String date;          // yyyy-MM-dd
    @SerializedName("requested_in") private String requestedIn;   // nullable
    @SerializedName("requested_out")private String requestedOut;  // nullable
    @SerializedName("reason")       private String reason;
    @SerializedName("status")       private String status;        // pending/approved/denied
    @SerializedName("approver_id")  private String approverId;
    @SerializedName("decision_date")private String decisionDate;  // read-only

    public AttendanceException() {}

    public AttendanceException(ResultSet rs, String a) throws SQLException {
        a = (a!=null && !a.isEmpty()) ? (a.endsWith(".")?a:a+".") : "";
        setId(rs.getString(a+"exception_id"));
        employeeId  = rs.getString(a+"employee_id");
        date        = rs.getString(a+"date");
        requestedIn = rs.getString(a+"requested_in");
        requestedOut= rs.getString(a+"requested_out");
        reason      = rs.getString(a+"reason");
        status      = rs.getString(a+"status");
        try { approverId = rs.getString(a+"approver_id"); } catch (SQLException ignore){}
        try { decisionDate = rs.getString(a+"decision_date"); } catch (SQLException ignore){}
    }

    @Override
    public String validate() {
        if (employeeId==null || employeeId.isBlank()) return "employee_id is required";
        if (date==null || date.isBlank()) return "date is required";
        if ((requestedIn==null || requestedIn.isBlank()) &&
                (requestedOut==null || requestedOut.isBlank())) return "requested_in or requested_out is required";
        return null;
    }

    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId()==null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, date);
        ps.setString(4, requestedIn);
        ps.setString(5, requestedOut);
        ps.setString(6, reason);
    }

    public void populateDecisionPs(PreparedStatement ps, String newStatus, String approver) throws Exception {
        if (getId()==null || getId().isEmpty()) throw new Exception("id required");
        ps.setString(1, newStatus);
        ps.setString(2, approver);
        ps.setString(3, getId());
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public String getApproverId() {
        return approverId;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getRequestedOut() {
        return requestedOut;
    }

    public String getRequestedIn() {
        return requestedIn;
    }

    public String getDate() {
        return date;
    }


    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRequestedIn(String requestedIn) {
        this.requestedIn = requestedIn;
    }

    public void setRequestedOut(String requestedOut) {
        this.requestedOut = requestedOut;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }
}
