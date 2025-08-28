package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.LeaveTypeSQL;
import com.ismet.domain.LeaveType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveTypeServiceImpl extends AbstractService implements LeaveTypeService{


    @Override
    public void insert(LeaveType lt) throws Exception {
        String v = lt.validate();
        if (v != null) throw new Exception(v);

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveTypeSQL.INSERT);
            lt.populateInsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void update(LeaveType lt) throws Exception {
        String v = lt.validate();
        if (v != null) throw new Exception(v);
        if (lt.getId() == null || lt.getId().isEmpty())
            throw new Exception("type_id (id) must be provided for update");

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveTypeSQL.UPDATE);
            lt.populateUpdatePs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public LeaveType getById(String id) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveTypeSQL.GET_BY_ID);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return new LeaveType(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<LeaveType> getAll() throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<LeaveType> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveTypeSQL.GET_ALL);
            rs = ps.executeQuery();
            while (rs.next()) out.add(new LeaveType(rs, ""));
        } finally { close(rs, ps, con); }
        return out;
    }

    @Override
    public void delete(String id) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(LeaveTypeSQL.DELETE);
            ps.setString(1, id);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }
}
