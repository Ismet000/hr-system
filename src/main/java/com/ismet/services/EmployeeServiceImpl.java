package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.EmployeeSQL;
import com.ismet.domain.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeServiceImpl extends AbstractService implements EmployeeService {

//    @Override
//    public void insertEmployee(Employee employee) throws Exception {
//        String validate = employee.validate();
//        if (validate != null) {
//            throw new Exception(validate);
//        }
//        PreparedStatement ps = null;
//        Connection con = null;
//        try {
//            con = getConnection();
//            ps = con.prepareStatement(EmployeeSQL.INSERT_EMPLOYEE);
//            employee.populateInsertPs(ps); //populatePs
////            ps.setString(1, employee.getId());
////            ps.setString(2, employee.getEmployeeName());
////            ps.setString(3, employee.getEmployeeLastName());
////            ps.setString(4, employee.getEmployeeEmail());
//            ps.executeUpdate();
//        } finally {
//            close(ps, con);
//        }
//    }


    @Override
    public void insertEmployee(Employee employee) throws Exception {
        String validate = employee.validate();
        if (validate != null) {
            throw new Exception(validate);
        }
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(EmployeeSQL.INSERT_EMPLOYEE);
            employee.populateInsertPs(ps); // fills all fields for INSERT
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

//    @Override
//    public void updateEmployee(Employee employee) throws Exception {
//        employee.validate();
//        // te burda yap bi  validam id yogise employiin ovakit yap throw error (id must be provided)
//        PreparedStatement ps = null;
//        Connection con = null;
//        try {
//            con = getConnection();
//            ps = con.prepareStatement(EmployeeSQL.UPDATE_EMPLOYEE);
////            ps.setString(1, employee.getEmployeeName());
////            ps.setString(2, employee.getEmployeeLastName());
////            ps.setString(3, employee.getEmployeeEmail());
////            ps.setString(4, employee.getId());
//            employee.populateUpdatePs(ps);
//            ps.executeUpdate();
//        } finally {
//            close(ps, con);
//        }
//    }

    @Override
    public void updateEmployee(Employee employee) throws Exception {
        String validate = employee.validate();
        if (validate != null) {
            throw new Exception(validate);
        }
        if (employee.getId() == null || employee.getId().isEmpty()) {
            throw new Exception("employee_id must be provided for update");
        }
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(EmployeeSQL.UPDATE_EMPLOYEE);
            employee.populateUpdatePs(ps); // fills all fields for UPDATE
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }


//    @Override
//    public Employee getEmployeeById(String employeeId) throws Exception {
//        ResultSet rs = null;
//        PreparedStatement ps = null;
//        Connection con = null;
//        try {
//            con = getConnection();
//            ps = con.prepareStatement(EmployeeSQL.GET_EMPLOYEE_BY_ID);
//            ps.setString(1, employeeId);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                Employee emp = new Employee();
//                emp.setId(rs.getString("employee_id"));
//                emp.setFirstName(rs.getString("employee_name"));
//                emp.setLastName(rs.getString("employee_last_name"));
//                emp.setEmail(rs.getString("employee_email"));
//                return emp;
//            }
//        } finally {
//            close(rs, ps, con);
//        }
//        return null;
//    }


    @Override
    public Employee getEmployeeById(String employeeId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(EmployeeSQL.GET_EMPLOYEE_BY_ID);
            ps.setString(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Employee(rs, ""); // constructor reads directly from ResultSet
            }
        } finally {
            close(rs, ps, con);
        }
        return null;
    }


//    @Override
//    public List<Employee> getAllEmployees() throws Exception {
//        ResultSet rs = null;
//        PreparedStatement ps = null;
//        Connection con = null;
//        List<Employee> employees = new ArrayList<>();
//        try {
//            con = getConnection();
//            ps = con.prepareStatement(EmployeeSQL.GET_ALL_EMPLOYEES);
//            rs = ps.executeQuery();
//            while (rs.next()) {
////                Employee emp = new Employee();
////                emp.setId(rs.getString("employee_id"));
////                emp.setEmployeeName(rs.getString("employee_name"));
////                emp.setEmployeeLastName(rs.getString("employee_last_name"));
////                emp.setEmployeeEmail(rs.getString("employee_email"));
//                employees.add(new Employee(rs,""));
//            }
//        } finally {
//            close(rs, ps, con);
//        }
//        return employees;
//    }



    @Override
    public List<Employee> getAllEmployees() throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        List<Employee> employees = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(EmployeeSQL.GET_ALL_EMPLOYEES);
            rs = ps.executeQuery();
            while (rs.next()) {
                employees.add(new Employee(rs, ""));
            }
        } finally {
            close(rs, ps, con);
        }
        return employees;
    }

}
