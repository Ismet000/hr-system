package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.DepartmentSQL;
import com.ismet.domain.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentServiceImpl extends AbstractService implements DepartmentService{
    @Override
    public void insertDepartment(Department department) throws Exception {
        String validate = department.validate();
        if (validate != null) throw new Exception(validate);

        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(DepartmentSQL.INSERT_DEPARTMENT);
            department.populateInsertPs(ps); // (department_id, name, manager_id)
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

    @Override
    public void updateDepartment(Department department) throws Exception {
        String validate = department.validate();
        if (validate != null) throw new Exception(validate);
        if (department.getId() == null || department.getId().isEmpty())
            throw new Exception("department_id (id) must be provided for update");

        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(DepartmentSQL.UPDATE_DEPARTMENT);
            department.populateUpdatePs(ps); // (name, manager_id, department_id)
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

    @Override
    public Department getDepartmentById(String departmentId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(DepartmentSQL.GET_DEPARTMENT_BY_ID);
            ps.setString(1, departmentId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Department(rs, ""); // reads department_id, name, manager_id, created_at
            }
        } finally {
            close(rs, ps, con);
        }
        return null;
    }

    @Override
    public List<Department> getAllDepartments() throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        List<Department> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(DepartmentSQL.GET_ALL_DEPARTMENTS);
            rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new Department(rs, ""));
            }
        } finally {
            close(rs, ps, con);
        }
        return out;
    }

    @Override
    public void deleteDepartment(String departmentId) throws Exception {
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(DepartmentSQL.DELETE_DEPARTMENT);
            ps.setString(1, departmentId);
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }
}
