package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.domain.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeServiceImpl extends AbstractService implements EmployeeService {

    @Override
    public void saveEmployee(Employee employee) throws Exception {
        String validate = employee.validate();
        if (validate != null) {
            throw new Exception(validate);
        }
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(Sql.INSERT_EMPLOYEE);
            ps.setString(1, employee.getId());
            ps.setString(2, employee.getEmployeeName());
            ps.setString(3, employee.getEmployeeLastName());
            ps.setString(4, employee.getEmployeeEmail());
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

    @Override
    public void updateEmployee(Employee employee) throws Exception {
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(Sql.UPDATE_EMPLOYEE);
            ps.setString(1, employee.getEmployeeName());
            ps.setString(2, employee.getEmployeeLastName());
            ps.setString(3, employee.getEmployeeEmail());
            ps.setString(4, employee.getId());
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

    @Override
    public Employee getEmployeeById(String employeeId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(Sql.GET_EMPLOYEE_BY_ID);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getString("employee_id"));
                emp.setEmployeeName(rs.getString("employee_name"));
                emp.setEmployeeLastName(rs.getString("employee_last_name"));
                emp.setEmployeeEmail(rs.getString("employee_email"));
                return emp;
            }
        } finally {
            close(rs, ps, con);
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        List<Employee> employees = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(Sql.GET_ALL_EMPLOYEES);
            rs = ps.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getString("employee_id"));
                emp.setEmployeeName(rs.getString("employee_name"));
                emp.setEmployeeLastName(rs.getString("employee_last_name"));
                emp.setEmployeeEmail(rs.getString("employee_email"));
                employees.add(emp);
            }
        } finally {
            close(rs, ps, con);
        }
        return employees;
    }

    public static class Sql {
        public static final String INSERT_EMPLOYEE =
                "INSERT INTO employees (employee_id, employee_name, employee_last_name, employee_email) VALUES (?, ?, ?, ?)";

        public static final String UPDATE_EMPLOYEE = """
                UPDATE employees
                SET employee_name = ?,
                    employee_last_name = ?,
                    employee_email = ?
                WHERE employee_id = ?
                """;

        public static final String GET_EMPLOYEE_BY_ID =
                "SELECT * FROM employees WHERE employee_id = ?";

        public static final String GET_ALL_EMPLOYEES =
                "SELECT * FROM employees";
    }
}
