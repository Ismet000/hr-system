package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.ReportSQL;
import com.ismet.domain.AttendanceSummary;
import com.ismet.domain.LeaveSummary;
import com.ismet.domain.OverdueApproval;
import com.ismet.domain.PayrollSummary;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ReportingServiceImpl extends AbstractService implements ReportingService{
    @Override
    public AttendanceSummary getAttendanceSummary(String employeeId, String from, String to, double weeklyLimit) throws Exception {
        double totalHours = 0d;
        int daysWorked = 0;

        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();

            // Totals
            ps = con.prepareStatement(ReportSQL.ATTENDANCE_TOTALS);
            ps.setString(1, employeeId);
            ps.setString(2, from);
            ps.setString(3, to);
            rs = ps.executeQuery();
            if (rs.next()) {
                totalHours = rs.getDouble("total_hours");
                daysWorked = rs.getInt("days_worked");
            }
            close(rs, ps, null);

            // Weekly overtime (grouped in SQL, threshold applied here)
            double overtime = 0d;
            ps = con.prepareStatement(ReportSQL.ATTENDANCE_BY_WEEK);
            ps.setString(1, employeeId);
            ps.setString(2, from);
            ps.setString(3, to);
            rs = ps.executeQuery();
            while (rs.next()) {
                double weekHrs = rs.getDouble("hours");
                if (weekHrs > weeklyLimit) overtime += (weekHrs - weeklyLimit);
            }

            return new AttendanceSummary(employeeId, from, to, totalHours, daysWorked, overtime, weeklyLimit);
        } finally {
            close(rs, ps, con);
        }
    }

    @Override
    public LeaveSummary getLeaveSummary(String employeeId, String from, String to) throws Exception {
        LeaveSummary out = new LeaveSummary(employeeId, from, to);

        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(ReportSQL.LEAVE_SUMMARY);
            // params order: to, from, to, from, employeeId, to, from
            ps.setString(1, to);
            ps.setString(2, from);
            ps.setString(3, to);
            ps.setString(4, from);
            ps.setString(5, employeeId);
            ps.setString(6, to);
            ps.setString(7, from);

            rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int cnt = rs.getInt("cnt");
                int days = rs.getInt("days");

                if ("approved".equalsIgnoreCase(status)) {
                    out.approvedCount = cnt; out.approvedDays = days;
                } else if ("pending".equalsIgnoreCase(status)) {
                    out.pendingCount = cnt; out.pendingDays = days;
                } else if ("denied".equalsIgnoreCase(status)) {
                    out.deniedCount = cnt; out.deniedDays = days;
                } else if ("cancelled".equalsIgnoreCase(status)) {
                    out.cancelledCount = cnt; out.cancelledDays = days;
                }
            }
            return out;
        } finally {
            close(rs, ps, con);
        }
    }

    @Override
    public PayrollSummary getPayrollSummary(String from, String to, String employeeIdOrNull) throws Exception {
        PayrollSummary sum = new PayrollSummary();
        sum.from = from; sum.to = to; sum.employeeId = employeeIdOrNull;

        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            if (employeeIdOrNull == null || employeeIdOrNull.isBlank()) {
                ps = con.prepareStatement(ReportSQL.PAYROLL_SUMMARY_ALL);
                ps.setString(1, from);
                ps.setString(2, to);
            } else {
                ps = con.prepareStatement(ReportSQL.PAYROLL_SUMMARY_EMP);
                ps.setString(1, employeeIdOrNull);
                ps.setString(2, from);
                ps.setString(3, to);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                sum.payrollsCount = rs.getInt("payrolls_count");
                sum.grossTotal    = rs.getBigDecimal("gross_total");
                sum.taxTotal      = rs.getBigDecimal("tax_total");
                sum.netTotal      = rs.getBigDecimal("net_total");
                if (sum.grossTotal == null) sum.grossTotal = BigDecimal.ZERO;
                if (sum.taxTotal   == null) sum.taxTotal   = BigDecimal.ZERO;
                if (sum.netTotal   == null) sum.netTotal   = BigDecimal.ZERO;
            }
            return sum;
        } finally {
            close(rs, ps, con);
        }
    }

    @Override
    public List<OverdueApproval> listOverdueApprovals(int olderThanDays) throws Exception {
        List<OverdueApproval> out = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(ReportSQL.OVERDUE_APPROVALS);
            ps.setInt(1, olderThanDays);
            rs = ps.executeQuery();
            while (rs.next()) {
                OverdueApproval oa = new OverdueApproval();
                oa.requestId   = rs.getString("request_id");
                oa.employeeId  = rs.getString("employee_id");
                oa.leaveTypeId = rs.getString("leave_type_id");
                oa.requestDate = rs.getString("request_date");
                oa.daysPending = rs.getInt("days_pending");
                out.add(oa);
            }
            return out;
        } finally {
            close(rs, ps, con);
        }
    }

    @Override
    public Map<String, Object> roster(String onDate) throws Exception {
        if (onDate == null || onDate.isBlank()) throw new Exception("date is required (yyyy-MM-dd)");

        // Use sets so we can exclude on-leave from working.
        Set<String> working = new LinkedHashSet<>();
        Set<String> onLeave = new LinkedHashSet<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();

            // Employees with an attendance record (present)
            ps = con.prepareStatement(ReportSQL.PRESENT_ON_DAY);
            ps.setString(1, onDate);
            rs = ps.executeQuery();
            while (rs.next()) working.add(rs.getString(1));
            close(rs, ps, null);

            // Employees with approved leave that covers the date
            ps = con.prepareStatement(ReportSQL.ON_LEAVE_ON_DAY);
            ps.setString(1, onDate);
            rs = ps.executeQuery();
            while (rs.next()) onLeave.add(rs.getString(1));
        } finally {
            close(rs, ps, con);
        }

        // Business rule: leave overrides presence
        working.removeAll(onLeave);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("date", onDate);
        out.put("working", new ArrayList<>(working));
        out.put("on_leave", new ArrayList<>(onLeave));
        return out;
    }
}
