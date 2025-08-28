package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.AlertsSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlertServiceImpl extends AbstractService implements AlertService{

    @Override
    public Map<String, Object> runDailySweep() throws Exception {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("overdueApprovals", query(AlertsSQL.OVERDUE_APPROVALS, rs -> {
            Map<String,Object> x = new LinkedHashMap<>();
            x.put("request_id", rs.getString("request_id"));
            x.put("employee_id", rs.getString("employee_id"));
            x.put("request_date", rs.getString("request_date"));
            return x;
        }));

        out.put("forgottenClockOut", query(AlertsSQL.FORGOTTEN_CLOCK_OUT, rs -> {
            Map<String,Object> x = new LinkedHashMap<>();
            x.put("record_id", rs.getString("record_id"));
            x.put("employee_id", rs.getString("employee_id"));
            x.put("date", rs.getString("date"));
            x.put("clock_in", rs.getString("clock_in"));
            return x;
        }));

        out.put("excessiveDaily", query(AlertsSQL.EXCESSIVE_DAILY, rs -> {
            Map<String,Object> x = new LinkedHashMap<>();
            x.put("record_id", rs.getString("record_id"));
            x.put("employee_id", rs.getString("employee_id"));
            x.put("date", rs.getString("date"));
            x.put("hours_worked", rs.getString("hours_worked"));
            return x;
        }));

        // If you want to persist into a notifications table, insert here.
        return out;
    }

    private interface RowMap { Map<String,Object> map(ResultSet rs) throws Exception; }

    private List<Map<String,Object>> query(String sql, RowMap mapper) throws Exception {
        List<Map<String,Object>> list = new ArrayList<>();
        PreparedStatement ps = null; ResultSet rs = null; Connection con = null;
        try {
            con = getConnection(); ps = con.prepareStatement(sql); rs = ps.executeQuery();
            while (rs.next()) list.add(mapper.map(rs));
        } finally { close(rs, ps, con); }
        return list;
    }
}
