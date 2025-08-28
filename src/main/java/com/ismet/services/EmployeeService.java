package com.ismet.services;

import com.ismet.domain.Employee;

import java.util.List;

public interface EmployeeService {
    void insertEmployee(Employee employee) throws Exception;

    void updateEmployee(Employee employee) throws Exception;

    Employee getEmployeeById(String employeeId) throws Exception;

    List<Employee> getAllEmployees() throws Exception;
}
