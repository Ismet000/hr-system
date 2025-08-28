package com.ismet.services;

import com.ismet.domain.AttendanceSummary;
import com.ismet.domain.LeaveSummary;
import com.ismet.domain.OverdueApproval;
import com.ismet.domain.PayrollSummary;

import java.util.List;
import java.util.Map;

public interface ReportingService {
    AttendanceSummary getAttendanceSummary(String employeeId, String from, String to,
                                           double weeklyLimit) throws Exception;

    LeaveSummary getLeaveSummary(String employeeId, String from, String to) throws Exception;

    PayrollSummary getPayrollSummary(String from, String to, String employeeIdOrNull) throws Exception;

    List<OverdueApproval> listOverdueApprovals(int olderThanDays) throws Exception;

    Map<String, Object> roster(String onDate) throws Exception; // { working:[], on_leave:[] }

}
