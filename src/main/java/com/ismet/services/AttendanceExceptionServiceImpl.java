package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.AttendanceExceptionSQL;
import com.ismet.domain.AttendanceException;
import com.ismet.domain.AttendanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AttendanceExceptionServiceImpl extends AbstractService implements AttendanceExceptionService{
    @Override
    public void submit(AttendanceException ex) throws Exception {
        String v = ex.validate();
        if (v != null) throw new Exception(v);
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceExceptionSQL.INSERT);
            ex.populateInsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void approve(String id, String approverId) throws Exception {
        // 1) load the exception row
        AttendanceException ex = getById(id);
        if (ex == null) throw new Exception("exception not found");

        // 2) mark approved (bind ONLY 2 params)
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceExceptionSQL.APPROVE);
            ps.setString(1, approverId);  // approver_id
            ps.setString(2, id);          // exception_id
            ps.executeUpdate();
            close(ps, null);

            // 3) apply correction to attendance_records
            AttendanceService att = new AttendanceServiceImpl();
            List<AttendanceRecord> day = att.getByEmployeeOnDay(ex.getEmployeeId(), ex.getDate());
            AttendanceRecord rec = day.isEmpty() ? null : day.get(0);

            if (rec == null) {
                // create new row and optionally clock-out
                AttendanceRecord r = new AttendanceRecord();
                r.setEmployeeId(ex.getEmployeeId());
                r.setWorkDate(ex.getDate());
                r.setClockIn(ex.getRequestedIn());
                r.setClockOut(ex.getRequestedOut());
                att.clockIn(r);
                if (ex.getRequestedOut() != null) {
                    // must pass full "yyyy-MM-dd HH:mm:ss"
                    att.clockOut(r.getId(), ex.getRequestedOut());
                }
            } else {
                // patch existing row
                if (ex.getRequestedIn()  != null) rec.setClockIn(ex.getRequestedIn());
                if (ex.getRequestedOut() != null) rec.setClockOut(ex.getRequestedOut());

                if (rec.getClockOut() != null) {
                    // recompute hours via service (uses its own connection)
                    att.clockOut(rec.getId(), rec.getClockOut());
                } else {
                    // only clock_in changed -> update directly
                    PreparedStatement ps2 = null;
                    try {
                        ps2 = con.prepareStatement("UPDATE attendance_records SET clock_in=? WHERE record_id=?");
                        ps2.setString(1, rec.getClockIn());
                        ps2.setString(2, rec.getId());
                        ps2.executeUpdate();
                    } finally { close(ps2, null); }
                }
            }
        } finally { close(ps, con); }
    }

    @Override
    public void deny(String id, String approverId) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceExceptionSQL.DENY);
            ps.setString(1, approverId);
            ps.setString(2, id);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }


    @Override
    public AttendanceException getById(String id) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection(); ps = con.prepareStatement(AttendanceExceptionSQL.GET_BY_ID);
            ps.setString(1, id); rs = ps.executeQuery();
            if (rs.next()) return new AttendanceException(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<AttendanceException> listPending() throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<AttendanceException> list = new ArrayList<>();
        try {
            con = getConnection(); ps = con.prepareStatement(AttendanceExceptionSQL.LIST_PENDING);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new AttendanceException(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }
}
