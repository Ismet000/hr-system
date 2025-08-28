package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.LeaveRequestSQL;
import com.ismet.domain.LeaveBalance;
import com.ismet.domain.LeaveRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestServiceImpl extends AbstractService implements LeaveRequestService{

    private final LeaveBalanceService balanceService = new LeaveBalanceServiceImpl();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // --- helper: inclusive day count (simple version; no weekends/holidays) ---
    private int countDaysInclusive(String start, String end) {
        LocalDate s = LocalDate.parse(start, DF);
        LocalDate e = LocalDate.parse(end, DF);
        if (e.isBefore(s)) return 0;
        return (int) (ChronoUnit.DAYS.between(s, e) + 1);
    }

    private int yearOf(String date) {
        return LocalDate.parse(date, DF).getYear();
    }

    @Override
    public void submit(LeaveRequest lr) throws Exception {
        String v = lr.validate();
        if (v != null) throw new Exception(v);

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveRequestSQL.INSERT);
            lr.populateInsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void approve(String requestId, String approverId) throws Exception {
        // 1) load request
        LeaveRequest lr = getById(requestId);
        if (lr == null) throw new Exception("leave request not found");
        if (!"pending".equalsIgnoreCase(lr.getStatus()))
            throw new Exception("only pending requests can be approved");

        // 2) compute days and check balance
        int days = countDaysInclusive(lr.getStartDate(), lr.getEndDate());
        int year = yearOf(lr.getStartDate());
        LeaveBalance lb = balanceService.getByKeys(lr.getEmployeeId(), lr.getLeaveTypeId(), year);
        if (lb == null) throw new Exception("leave balance not found for employee/year/type");
        int remaining = (lb.getTotalDays() == null ? 0 : lb.getTotalDays()) -
                (lb.getUsedDays()  == null ? 0 : lb.getUsedDays());
        if (remaining < days) throw new Exception("insufficient remaining leave days");

        // 3) flip status
        decide(requestId, approverId, "approved");

        // 4) deduct from balance
        balanceService.useDays(lb.getId(), days);
    }

    @Override
    public void deny(String requestId, String approverId) throws Exception {
        LeaveRequest lr = getById(requestId);
        if (lr == null) throw new Exception("leave request not found");
        if (!"pending".equalsIgnoreCase(lr.getStatus()))
            throw new Exception("only pending requests can be denied");

        decide(requestId, approverId, "denied");
        // no balance change
    }



    //old
//    @Override
//    public void approve(String requestId, String approverId) throws Exception {
//        decide(requestId, approverId, "approved");
//    }
//
//    @Override
//    public void deny(String requestId, String approverId) throws Exception {
//        decide(requestId, approverId, "denied");
//
//    }



    /// //////

//    private void decide(String requestId, String approverId, String status) throws Exception {
//        PreparedStatement ps = null; Connection con = null;
//        try {
//            con = getConnection();
//            ps = con.prepareStatement("approved".equals(status) ? LeaveRequestSQL.APPROVE : LeaveRequestSQL.DENY);
//            LeaveRequest tmp = new LeaveRequest();
//            tmp.setId(requestId);
//            tmp.populateDecisionPs(ps, status, approverId);
//            ps.executeUpdate();
//        } finally { close(ps, con); }
//    }

    private void decide(String requestId, String approverId, String status) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            String sql = "approved".equals(status) ? LeaveRequestSQL.APPROVE : LeaveRequestSQL.DENY;
            ps = con.prepareStatement(sql);
            ps.setString(1, approverId);
            ps.setString(2, requestId);
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }



    @Override
    public void cancel(String requestId) throws Exception {
        // if you want to allow cancelling approved requests and return days:
        LeaveRequest lr = getById(requestId);
        if (lr == null) throw new Exception("leave request not found");

        boolean wasApproved = "approved".equalsIgnoreCase(lr.getStatus());

        // update status -> cancelled (you can relax your SQL if needed)
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("UPDATE leave_requests SET status='cancelled' WHERE request_id=?");
            ps.setString(1, requestId);
            ps.executeUpdate();
        } finally { close(ps, con); }

        if (wasApproved) {
            int days = countDaysInclusive(lr.getStartDate(), lr.getEndDate());
            int year = yearOf(lr.getStartDate());
            LeaveBalance lb = balanceService.getByKeys(lr.getEmployeeId(), lr.getLeaveTypeId(), year);
            if (lb != null) {
                balanceService.releaseDays(lb.getId(), days);
            }
        }
    }

    // old
//    @Override
//    public void cancel(String requestId) throws Exception {
//        PreparedStatement ps = null; Connection con = null;
//        try {
//            con = getConnection();
//            ps = con.prepareStatement(LeaveRequestSQL.CANCEL);
//            ps.setString(1, requestId);
//            ps.executeUpdate();
//        } finally { close(ps, con); }
//    }

    @Override
    public LeaveRequest getById(String requestId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveRequestSQL.GET_BY_ID);
            ps.setString(1, requestId);
            rs = ps.executeQuery();
            if (rs.next()) return new LeaveRequest(rs, "");
        } finally {
            close(rs, ps, con);
        }
        return null;
    }

    @Override
    public List<LeaveRequest> getAll() throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<LeaveRequest> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveRequestSQL.GET_ALL);
            rs = ps.executeQuery();
            while (rs.next()) out.add(new LeaveRequest(rs, ""));
        } finally { close(rs, ps, con); }
        return out;
    }

    @Override
    public List<LeaveRequest> getByEmployee(String employeeId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<LeaveRequest> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveRequestSQL.GET_BY_EMPLOYEE);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            while (rs.next()) out.add(new LeaveRequest(rs, ""));
        } finally { close(rs, ps, con); }
        return out;
    }

    @Override
    public List<LeaveRequest> getByStatus(String status) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<LeaveRequest> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveRequestSQL.GET_BY_STATUS);
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) out.add(new LeaveRequest(rs, ""));
        } finally { close(rs, ps, con); }
        return out;
    }
}
