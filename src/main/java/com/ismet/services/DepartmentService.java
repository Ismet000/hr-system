package com.ismet.services;

import com.ismet.domain.Department;

import java.util.List;

public interface DepartmentService {
    void insertDepartment(Department department) throws Exception;
    void updateDepartment(Department department) throws Exception;
    Department getDepartmentById(String departmentId) throws Exception;
    List<Department> getAllDepartments() throws Exception;
    void deleteDepartment(String departmentId) throws Exception;
}
