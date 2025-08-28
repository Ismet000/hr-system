package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.ScheduledShiftSQL;
import com.ismet.domain.ScheduledShift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ScheduledShiftServiceImpl extends AbstractService implements ScheduledShiftService{
    @Override
    public String create(ScheduledShift s) throws Exception {
        String v = s.validate(); if (v!=null) throw new Exception(v);
        PreparedStatement ps=null; Connection con=null;
        try { con=getConnection(); ps=con.prepareStatement(ScheduledShiftSQL.INSERT);
            s.fillInsertPs(ps); ps.executeUpdate(); return s.getId();
        } finally { close(ps, con); }
    }

    @Override
    public void update(ScheduledShift s) throws Exception {
        String v = s.validate(); if (v!=null) throw new Exception(v);
        PreparedStatement ps=null; Connection con=null;
        try { con=getConnection(); ps=con.prepareStatement(ScheduledShiftSQL.UPDATE);
            s.fillUpdatePs(ps); ps.executeUpdate();
        } finally { close(ps, con); }

    }

    @Override
    public ScheduledShift getById(String id) throws Exception {
        ResultSet rs=null; PreparedStatement ps=null; Connection con=null;
        try { con=getConnection(); ps=con.prepareStatement(ScheduledShiftSQL.GET_BY_ID);
            ps.setString(1,id); rs=ps.executeQuery(); if (rs.next()) return new ScheduledShift(rs,"");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<ScheduledShift> listByEmployeeRange(String empId, String from, String to) throws Exception {
        ResultSet rs=null; PreparedStatement ps=null; Connection con=null; List<ScheduledShift> out=new ArrayList<>();
        try { con=getConnection(); ps=con.prepareStatement(ScheduledShiftSQL.LIST_BY_EMP_RANGE);
            ps.setString(1,empId); ps.setString(2,from); ps.setString(3,to); rs=ps.executeQuery();
            while (rs.next()) out.add(new ScheduledShift(rs,""));
        } finally { close(rs, ps, con); }
        return out;
    }

    @Override
    public List<ScheduledShift> listByDate(String date) throws Exception {
        ResultSet rs=null; PreparedStatement ps=null; Connection con=null; List<ScheduledShift> out=new ArrayList<>();
        try { con=getConnection(); ps=con.prepareStatement(ScheduledShiftSQL.LIST_BY_DATE);
            ps.setString(1,date); rs=ps.executeQuery(); while (rs.next()) out.add(new ScheduledShift(rs,""));
        } finally { close(rs, ps, con); }
        return out;

    }

    @Override
    public void delete(String id) throws Exception {
        PreparedStatement ps=null; Connection con=null;
        try { con=getConnection(); ps=con.prepareStatement(ScheduledShiftSQL.DELETE);
            ps.setString(1,id); ps.executeUpdate();
        } finally { close(ps, con); }
    }
}
