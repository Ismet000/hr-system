package com.ismet.services;

import com.ismet.domain.Salary;

import java.util.List;

public interface SalaryService {

    void insert(Salary s) throws Exception;
    void update(Salary s) throws Exception;
    void delete(String salaryId) throws Exception;

    Salary getById(String salaryId) throws Exception;
    Salary getCurrentForEmployee(String employeeId) throws Exception;
    List<Salary> listHistoryForEmployee(String employeeId) throws Exception;
}
