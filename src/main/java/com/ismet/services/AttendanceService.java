package com.ismet.services;

import com.ismet.domain.AttendanceRecord;

import java.util.List;

public interface AttendanceService {
    String clockIn(AttendanceRecord rec) throws Exception;          // returns record_id
    void clockOut(String recordId, String clockOutTs) throws Exception;
    AttendanceRecord getById(String recordId) throws Exception;
    List<AttendanceRecord> getByEmployeeInRange(String employeeId, String fromDate, String toDate) throws Exception;
    List<AttendanceRecord> getByEmployeeOnDay(String employeeId, String workDate) throws Exception;
    void delete(String recordId) throws Exception;
}
