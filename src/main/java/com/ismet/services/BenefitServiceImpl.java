package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.BenefitSQL;
import com.ismet.domain.Benefit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BenefitServiceImpl extends AbstractService implements BenefitService{


    @Override
    public void create(Benefit b) throws Exception {
        String v = b.validate();
        if (v != null) throw new Exception(v);
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(BenefitSQL.INSERT);
            b.populateInsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void update(Benefit b) throws Exception {
        String v = b.validate();
        if (v != null) throw new Exception(v);
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(BenefitSQL.UPDATE);
            b.populateUpdatePs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public Benefit getById(String id) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(BenefitSQL.GET_BY_ID);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return new Benefit(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<Benefit> listByEmployee(String employeeId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Benefit> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(BenefitSQL.LIST_BY_EMP);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            while (rs.next()) out.add(new Benefit(rs, ""));
        } finally { close(rs, ps, con); }
        return out;
    }

    @Override
    public List<Benefit> listActiveInPeriod(String employeeId, String from, String to) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Benefit> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(BenefitSQL.LIST_ACTIVE_IN_PERIOD);
            ps.setString(1, employeeId);
            ps.setString(2, to);     // effective_date <= to
            ps.setString(3, from);   // end_date     >= from
            rs = ps.executeQuery();
            while (rs.next()) out.add(new Benefit(rs, ""));
        } finally { close(rs, ps, con); }
        return out;
    }

    @Override
    public void delete(String id) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(BenefitSQL.DELETE);
            ps.setString(1, id);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void setStatus(String id, String status) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("UPDATE benefits SET status=? WHERE benefit_id=?");
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }
}
