package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.TimesheetSQL;
import com.ismet.domain.Timesheet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimesheetServiceImpl  extends AbstractService implements TimesheetService{
    @Override
    public Timesheet generate(String employeeId, String weekStartDate) throws Exception {
        if (employeeId == null || employeeId.isBlank()) throw new Exception("employee_id is required");
        if (weekStartDate == null || weekStartDate.isBlank()) throw new Exception("week_start_date is required");

        // 1) Sum weekly hours from attendance_records
        double total = sumWeeklyHours(employeeId, weekStartDate);
        double overtime = Math.max(0d, total - 40d);

        // 2) Upsert into timesheets (by unique (employee_id, week_start_date))
        PreparedStatement ps = null; Connection con = null;
        String tsId = UUID.randomUUID().toString();
        try {
            con = getConnection();
            ps = con.prepareStatement(TimesheetSQL.UPSERT_BY_UNIQUE);
            ps.setString(1, tsId);
            ps.setString(2, employeeId);
            ps.setString(3, weekStartDate);
            ps.setDouble(4, total);
            ps.setDouble(5, overtime);
            ps.executeUpdate();
        } finally { close(ps, con); }

        // 3) Return the row
        Timesheet ts = getByEmployeeWeek(employeeId, weekStartDate);
        if (ts == null) throw new Exception("Failed to upsert timesheet");
        return ts;
    }


    private double sumWeeklyHours(String employeeId, String weekStartDate) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(TimesheetSQL.SUM_HOURS_FOR_WEEK);
            ps.setString(1, employeeId);
            ps.setString(2, weekStartDate);
            ps.setString(3, weekStartDate);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } finally { close(rs, ps, con); }
        return 0d;
    }

    @Override
    public Timesheet getById(String id) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(TimesheetSQL.GET_BY_ID);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return new Timesheet(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public Timesheet getByEmployeeWeek(String employeeId, String weekStartDate) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(TimesheetSQL.GET_BY_EMPLOYEE_WEEK);
            ps.setString(1, employeeId);
            ps.setString(2, weekStartDate);
            rs = ps.executeQuery();
            if (rs.next()) return new Timesheet(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<Timesheet> listByEmployeeRange(String employeeId, String fromWeek, String toWeek) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Timesheet> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(TimesheetSQL.LIST_BY_EMPLOYEE_RANGE);
            ps.setString(1, employeeId);
            ps.setString(2, fromWeek);
            ps.setString(3, toWeek);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new Timesheet(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public void delete(String id) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(TimesheetSQL.DELETE);
            ps.setString(1, id);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }
}
