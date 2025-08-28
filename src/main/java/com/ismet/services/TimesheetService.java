package com.ismet.services;

import com.ismet.domain.Timesheet;

import java.util.List;

public interface TimesheetService {

    Timesheet generate(String employeeId, String weekStartDate) throws Exception;

    Timesheet getById(String id) throws Exception;
    Timesheet getByEmployeeWeek(String employeeId, String weekStartDate) throws Exception;
    List<Timesheet> listByEmployeeRange(String employeeId, String fromWeek, String toWeek) throws Exception;

    void delete(String id) throws Exception;
}
