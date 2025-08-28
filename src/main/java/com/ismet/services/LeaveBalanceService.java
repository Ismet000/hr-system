package com.ismet.services;

import com.ismet.domain.LeaveBalance;

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalance upsert(LeaveBalance lb) throws Exception;

    LeaveBalance getById(String balanceId) throws Exception;
    LeaveBalance getByKeys(String employeeId, String leaveTypeId, int year) throws Exception;

    List<LeaveBalance> listByEmployee(String employeeId) throws Exception;
    List<LeaveBalance> listByEmployeeYear(String employeeId, int year) throws Exception;

    void useDays(String balanceId, int days) throws Exception;     // consume
    void releaseDays(String balanceId, int days) throws Exception; // give back
    void setTotalDays(String balanceId, int total) throws Exception;

    void delete(String balanceId) throws Exception;
}
