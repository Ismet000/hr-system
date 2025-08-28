package com.ismet.services;

import com.ismet.domain.Payroll;

import java.util.List;

public interface PayrollService {
    Payroll generate(String employeeId, String periodStart, String periodEnd, String currencyIgnored) throws Exception;

    Payroll getById(String payrollId) throws Exception;
    List<Payroll> listByEmployeeInPeriod(String employeeId, String from, String to) throws Exception;
    List<Payroll> listInPeriod(String from, String to) throws Exception;

    void delete(String payrollId) throws Exception;
}
