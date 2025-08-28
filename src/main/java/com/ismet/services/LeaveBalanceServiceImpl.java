package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.LeaveBalanceSQL;
import com.ismet.domain.LeaveBalance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveBalanceServiceImpl extends AbstractService implements LeaveBalanceService{


    @Override
    public LeaveBalance upsert(LeaveBalance lb) throws Exception {
        String err = lb.validate();
        if (err != null) throw new Exception(err);

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(  LeaveBalanceSQL.UPSERT);
            lb.populateUpsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }

        return getByKeys(lb.getEmployeeId(), lb.getLeaveTypeId(), lb.getYear());
    }

    @Override
    public LeaveBalance getById(String balanceId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.GET_BY_ID);
            ps.setString(1, balanceId);
            rs = ps.executeQuery();
            if (rs.next()) return new LeaveBalance(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public LeaveBalance getByKeys(String employeeId, String leaveTypeId, int year) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.GET_ONE_BY_KEYS);
            ps.setString(1, employeeId);
            ps.setString(2, leaveTypeId);
            ps.setInt(3, year);
            rs = ps.executeQuery();
            if (rs.next()) return new LeaveBalance(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<LeaveBalance> listByEmployee(String employeeId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<LeaveBalance> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.LIST_BY_EMP);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new LeaveBalance(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public List<LeaveBalance> listByEmployeeYear(String employeeId, int year) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<LeaveBalance> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.LIST_BY_EMP_YEAR);
            ps.setString(1, employeeId);
            ps.setInt(2, year);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new LeaveBalance(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public void useDays(String balanceId, int days) throws Exception {
        if (days <= 0) throw new Exception("days must be > 0");
        LeaveBalance lb = getById(balanceId);
        if (lb == null) throw new Exception("leave balance not found");
        int remaining = lb.getTotalDays() - (lb.getUsedDays() == null ? 0 : lb.getUsedDays());
        if (remaining < days) throw new Exception("insufficient remaining days");

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.ADD_USED);
            ps.setInt(1, days);
            ps.setString(2, balanceId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void releaseDays(String balanceId, int days) throws Exception {
        if (days <= 0) throw new Exception("days must be > 0");
        LeaveBalance lb = getById(balanceId);
        if (lb == null) throw new Exception("leave balance not found");
        int used = (lb.getUsedDays() == null ? 0 : lb.getUsedDays());
        if (used < days) throw new Exception("cannot release more than used");

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.RELEASE_USED);
            ps.setInt(1, days);
            ps.setString(2, balanceId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void setTotalDays(String balanceId, int total) throws Exception {
        if (total < 0) throw new Exception("total must be >= 0");
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.SET_TOTAL);
            ps.setInt(1, total);
            ps.setString(2, balanceId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void delete(String balanceId) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveBalanceSQL.DELETE);
            ps.setString(1, balanceId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }
}
