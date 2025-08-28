package com.ismet.common.sql;

public class    TimesheetSQL {

    public static final String INSERT =
            "INSERT INTO timesheets (timesheet_id, employee_id, week_start_date, total_hours, overtime_hours) " +
                    "VALUES (?, ?, ?, ?, ?)";

    public static final String UPDATE_BY_ID =
            "UPDATE timesheets SET total_hours = ?, overtime_hours = ? WHERE timesheet_id = ?";

    public static final String UPSERT_BY_UNIQUE =
            "INSERT INTO timesheets (timesheet_id, employee_id, week_start_date, total_hours, overtime_hours) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE total_hours = VALUES(total_hours), overtime_hours = VALUES(overtime_hours)";

    public static final String GET_BY_ID =
            "SELECT * FROM timesheets WHERE timesheet_id = ?";

    public static final String GET_BY_EMPLOYEE_WEEK =
            "SELECT * FROM timesheets WHERE employee_id = ? AND week_start_date = ?";

    public static final String LIST_BY_EMPLOYEE_RANGE =
            "SELECT * FROM timesheets " +
                    "WHERE employee_id = ? AND week_start_date BETWEEN ? AND ? " +
                    "ORDER BY week_start_date DESC";

    public static final String DELETE =
            "DELETE FROM timesheets WHERE timesheet_id = ?";

    /* Aggregation from attendance_records */
    public static final String SUM_HOURS_FOR_WEEK =
            "SELECT COALESCE(SUM(hours_worked), 0) AS total " +
                    "FROM attendance_records " +
                    "WHERE employee_id = ? " +
                    "  AND `date` BETWEEN ? AND DATE_ADD(?, INTERVAL 6 DAY)";
}
