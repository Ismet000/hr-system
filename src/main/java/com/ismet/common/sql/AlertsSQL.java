package com.ismet.common.sql;

public class AlertsSQL {

    // pending > 48h
    public static final String OVERDUE_APPROVALS =
            "SELECT request_id, employee_id, request_date " +
                    "FROM leave_requests " +
                    "WHERE status = 'pending' AND TIMESTAMPDIFF(HOUR, request_date, NOW()) > 48";

    // clocked-in more than 12h without clock_out (today and earlier)
    public static final String FORGOTTEN_CLOCK_OUT =
            "SELECT record_id, employee_id, `date`, clock_in " +
                    "FROM attendance_records " +
                    "WHERE clock_out IS NULL AND TIMESTAMPDIFF(HOUR, STR_TO_DATE(clock_in, '%Y-%m-%d %H:%i:%s'), NOW()) > 12";

    // daily hours > 12
    public static final String EXCESSIVE_DAILY =
            "SELECT record_id, employee_id, `date`, hours_worked " +
                    "FROM attendance_records " +
                    "WHERE hours_worked IS NOT NULL AND hours_worked > 12";
}
