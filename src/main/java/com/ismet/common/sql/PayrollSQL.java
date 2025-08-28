package com.ismet.common.sql;

public class PayrollSQL {

    public static final String INSERT =
            "INSERT INTO payrolls " +
                    "(payroll_id, employee_id, period_start, period_end, gross_pay, taxes, net_pay) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPSERT =
            "INSERT INTO payrolls " +
                    "(payroll_id, employee_id, period_start, period_end, gross_pay, taxes, net_pay) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    " gross_pay=VALUES(gross_pay), taxes=VALUES(taxes), net_pay=VALUES(net_pay)";

    public static final String GET_BY_ID =
            "SELECT * FROM payrolls WHERE payroll_id = ?";

    public static final String LIST_BY_EMP_PERIOD =
            "SELECT * FROM payrolls " +
                    "WHERE employee_id = ? AND period_start >= ? AND period_end <= ? " +
                    "ORDER BY period_start DESC";

    public static final String LIST_IN_PERIOD =
            "SELECT * FROM payrolls WHERE period_start >= ? AND period_end <= ? ORDER BY employee_id, period_start";

    public static final String DELETE =
            "DELETE FROM payrolls WHERE payroll_id = ?";

    // still needed to compute pay from attendance (we just don't store the hours anymore)
    public static final String SUM_HOURS_FOR_PERIOD =
            "SELECT COALESCE(SUM(hours_worked),0) AS total " +
                    "FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` BETWEEN ? AND ?";


    public static final String GET_BY_UNIQUE =
            "SELECT * FROM payrolls WHERE employee_id = ? AND period_start = ? AND period_end = ?";


    public static final String ATTENDANCE_FOR_PERIOD =
            "SELECT `date`, COALESCE(hours_worked,0) AS hours_worked " +
                    "FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` BETWEEN ? AND ? " +
                    "ORDER BY `date`";



    // Sum benefits amount from JSON field {"amount": 123.45}
// Uses everything created up to (and including) the pay period end.
    public static final String SUM_BENEFITS_FOR_PERIOD =
            "SELECT COALESCE(SUM(CAST(JSON_UNQUOTE(JSON_EXTRACT(details,'$.amount')) AS DECIMAL(10,2))),0) AS total " +
                    "FROM benefits " +
                    "WHERE employee_id = ? AND created_at <= ?";

}
