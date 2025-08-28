package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.AttendanceSQL;
import com.ismet.domain.AttendanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceServiceImpl extends AbstractService implements AttendanceService{

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm:ss");





    @Override
    public String clockIn(AttendanceRecord rec) throws Exception {
        String v = rec.validate();
        if (v != null) throw new Exception(v);
        if (rec.getClockIn() == null || rec.getClockIn().isBlank())
            throw new Exception("clock_in is required (yyyy-MM-dd HH:mm:ss)");

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.INSERT);
            rec.populateInsertPs(ps); // will set id if missing
            ps.executeUpdate();
            return rec.getId();
        } finally { close(ps, con); }
    }


    //news
    @Override
    public void clockOut(String recordId, String clockOutTs) throws Exception {
        AttendanceRecord existing = getById(recordId);
        if (existing == null) throw new Exception("Attendance record not found");
        if (existing.getClockIn() == null || existing.getClockIn().isBlank())
            throw new Exception("Cannot clock-out without clock-in");
        if (clockOutTs == null || clockOutTs.isBlank())
            throw new Exception("clock_out is required");

        // Combine the date column with time-only values
        LocalDate workDate = LocalDate.parse(existing.getWorkDate());              // from `date` column
        LocalTime inTime   = parseTime(existing.getClockIn());                 // "09:05:00"
        LocalDateTime in   = LocalDateTime.of(workDate, inTime);

        LocalDateTime out;
        if (clockOutTs.contains(" ")) {
            // client sent full "yyyy-MM-dd HH:mm:ss"
            out = LocalDateTime.parse(clockOutTs, TS);
        } else {
            // client sent time-only "HH:mm:ss" (or "HH:mm")
            LocalTime outTime = parseTime(clockOutTs);
            out = LocalDateTime.of(workDate, outTime);
            if (out.isBefore(in)) out = out.plusDays(1); // handle past-midnight shifts
        }

        double hours = Duration.between(in, out).toMinutes() / 60.0;

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.CLOCK_OUT);
            // store clock_out back as TIME "HH:mm:ss" and computed hours
            existing.setClockOut(out.toLocalTime().format(TF));
            existing.setHoursWorked(hours);
            existing.populateClockOutPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    private LocalTime parseTime(String s) {
        // allow "HH:mm" or "HH:mm:ss"
        if (s.length() == 5) return LocalTime.parse(s + ":00");
        return LocalTime.parse(s);
    }

    @Override
    public AttendanceRecord getById(String recordId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.GET_BY_ID);
            ps.setString(1, recordId);
            rs = ps.executeQuery();
            if (rs.next()) return new AttendanceRecord(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<AttendanceRecord> getByEmployeeInRange(String employeeId, String fromDate, String toDate) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<AttendanceRecord> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.GET_BY_EMPLOYEE_RANGE);
            ps.setString(1, employeeId);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new AttendanceRecord(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public List<AttendanceRecord> getByEmployeeOnDay(String employeeId, String workDate) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<AttendanceRecord> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.GET_BY_EMPLOYEE_DAY);
            ps.setString(1, employeeId);
            ps.setString(2, workDate);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new AttendanceRecord(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public void delete(String recordId) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.DELETE);
            ps.setString(1, recordId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }
}
