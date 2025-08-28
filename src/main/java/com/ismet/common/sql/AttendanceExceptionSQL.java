package com.ismet.common.sql;

public class AttendanceExceptionSQL {

    public static final String INSERT =
            "INSERT INTO attendance_exceptions " +
                    "(exception_id, employee_id, `date`, requested_in, requested_out, reason) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    public static final String APPROVE =
            "UPDATE attendance_exceptions SET status='approved', approver_id=?, decision_date=NOW() WHERE exception_id=?";

    public static final String DENY =
            "UPDATE attendance_exceptions SET status='denied', approver_id=?, decision_date=NOW() WHERE exception_id=?";

    public static final String GET_BY_ID =
            "SELECT * FROM attendance_exceptions WHERE exception_id=?";

    public static final String LIST_PENDING =
            "SELECT * FROM attendance_exceptions WHERE status='pending' ORDER BY `date` DESC";
}
