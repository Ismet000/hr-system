package com.ismet.services;

import com.ismet.domain.AttendanceComplianceReport;

public interface AttendanceComplianceService {

    AttendanceComplianceReport compute(String employeeId, String from, String to,
                                       Double dailyLimit, Double weeklyLimit,
                                       boolean skipWeekends, boolean skipHolidays) throws Exception;
}
