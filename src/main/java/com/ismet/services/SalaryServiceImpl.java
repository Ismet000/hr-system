package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.SalarySQL;
import com.ismet.domain.Salary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SalaryServiceImpl extends AbstractService implements SalaryService {
    @Override
    public void insert(Salary s) throws Exception {
        String v = s.validate();
        if (v != null) throw new Exception(v);

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(SalarySQL.INSERT);
            s.populateInsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void update(Salary s) throws Exception {
        String v = s.validate();
        if (v != null) throw new Exception(v);
        if (s.getId() == null || s.getId().isBlank())
            throw new Exception("salary_id (id) is required");

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(SalarySQL.UPDATE);
            s.populateUpdatePs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void delete(String salaryId) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(SalarySQL.DELETE);
            ps.setString(1, salaryId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public Salary getById(String salaryId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(SalarySQL.GET_BY_ID);
            ps.setString(1, salaryId);
            rs = ps.executeQuery();
            if (rs.next()) return new Salary(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public Salary getCurrentForEmployee(String employeeId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(SalarySQL.GET_CURRENT_FOR_EMP);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next()) return new Salary(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<Salary> listHistoryForEmployee(String employeeId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Salary> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(SalarySQL.LIST_HISTORY_FOR_EMP);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new Salary(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }
}
