package com.ismet.common.sql;

public class ReportSQL {

    // Attendance totals in a date range
    public static final String ATTENDANCE_TOTALS =
            "SELECT COALESCE(SUM(hours_worked),0) AS total_hours, " +
                    "       COUNT(DISTINCT `date`)        AS days_worked " +
                    "FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` BETWEEN ? AND ?";

    // Group attendance by ISO week to compute weekly overtime in Java
    public static final String ATTENDANCE_BY_WEEK =
            "SELECT YEARWEEK(`date`, 3) AS yw, " +
                    "       COALESCE(SUM(hours_worked),0) AS hours " +
                    "FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` BETWEEN ? AND ? " +
                    "GROUP BY YEARWEEK(`date`, 3)";

    // Leave summary with overlap-safe day counting
    // Params order: (to, from, to, from, employeeId, to, from)
    public static final String LEAVE_SUMMARY =
            "SELECT status, " +
                    "       COUNT(*) AS cnt, " +
                    "       SUM(CASE WHEN end_date < ? OR start_date > ? THEN 0 " +
                    "                ELSE DATEDIFF(LEAST(end_date, ?), GREATEST(start_date, ?)) + 1 END) AS days " +
                    "FROM leave_requests " +
                    "WHERE employee_id = ? " +
                    "  AND NOT (end_date < ? OR start_date > ?) " +
                    "GROUP BY status";

    // Payroll summary (all employees)
    public static final String PAYROLL_SUMMARY_ALL =
            "SELECT COUNT(*) AS payrolls_count, " +
                    "       COALESCE(SUM(gross_pay),0) AS gross_total, " +
                    "       COALESCE(SUM(taxes),0)     AS tax_total, " +
                    "       COALESCE(SUM(net_pay),0)   AS net_total " +
                    "FROM payrolls " +
                    "WHERE period_start >= ? AND period_end <= ?";

    // Payroll summary (one employee)
    public static final String PAYROLL_SUMMARY_EMP =
            "SELECT COUNT(*) AS payrolls_count, " +
                    "       COALESCE(SUM(gross_pay),0) AS gross_total, " +
                    "       COALESCE(SUM(taxes),0)     AS tax_total, " +
                    "       COALESCE(SUM(net_pay),0)   AS net_total " +
                    "FROM payrolls " +
                    "WHERE employee_id = ? " +
                    "  AND period_start >= ? AND period_end <= ?";

    // Overdue (pending) leave approvals feed
    public static final String OVERDUE_APPROVALS =
            "SELECT request_id, employee_id, leave_type_id, request_date, " +
                    "       TIMESTAMPDIFF(DAY, request_date, NOW()) AS days_pending " +
                    "FROM leave_requests " +
                    "WHERE status = 'pending' AND request_date < (NOW() - INTERVAL ? DAY) " +
                    "ORDER BY request_date ASC";


    // Anyone who has an attendance record for that date (present at any time)
    public static final String PRESENT_ON_DAY =
            "SELECT DISTINCT ar.employee_id " +
                    "FROM attendance_records ar " +
                    "WHERE ar.`date` = ? AND ar.clock_in IS NOT NULL";

    // Approved leaves that overlap the date
    public static final String ON_LEAVE_ON_DAY =
            "SELECT DISTINCT lr.employee_id " +
                    "FROM leave_requests lr " +
                    "WHERE lr.status = 'approved' AND ? BETWEEN lr.start_date AND lr.end_date";
}
