package com.ismet.common.sql;

public class LeaveRequestSQL {

//    public static final String INSERT =
//            "INSERT INTO leave_requests " +
//                    "(request_id, employee_id, leave_type_id, start_date, end_date, status, approver_id, comments) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//    public static final String GET_BY_ID =
//            "SELECT * FROM leave_requests WHERE request_id = ?";
//
//    public static final String GET_ALL =
//            "SELECT * FROM leave_requests ORDER BY request_date DESC";
//
//    public static final String GET_BY_EMPLOYEE =
//            "SELECT * FROM leave_requests WHERE employee_id = ? ORDER BY request_date DESC";
//
//    public static final String GET_BY_STATUS =
//            "SELECT * FROM leave_requests WHERE status = ? ORDER BY request_date DESC";
//
//    public static final String GET_IN_PERIOD =
//            "SELECT * FROM leave_requests WHERE (start_date <= ? AND end_date >= ?) ORDER BY start_date";
//
//    public static final String APPROVE =
//            "UPDATE leave_requests SET status = ?, approver_id = ?, request_date = request_date, decision_date = NOW() WHERE request_id = ?";
//
//    public static final String DENY =
//            "UPDATE leave_requests SET status = ?, approver_id = ?, request_date = request_date, decision_date = NOW() WHERE request_id = ?";
//
//    public static final String CANCEL =
//            "UPDATE leave_requests SET status = 'cancelled' WHERE request_id = ? AND status = 'pending'";
//
//    public static final String DELETE =
//            "DELETE FROM leave_requests WHERE request_id = ?";


    public static final String INSERT =
            "INSERT INTO leave_requests " +
                    "(request_id, employee_id, leave_type_id, start_date, end_date, status, approver_id, comments) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_BY_ID =
            "SELECT * FROM leave_requests WHERE request_id = ?";

    public static final String GET_ALL =
            "SELECT * FROM leave_requests ORDER BY request_date DESC";

    public static final String GET_BY_EMPLOYEE =
            "SELECT * FROM leave_requests WHERE employee_id = ? ORDER BY request_date DESC";

    public static final String GET_BY_STATUS =
            "SELECT * FROM leave_requests WHERE status = ? ORDER BY request_date DESC";

    // ✅ No decision_date here
    public static final String APPROVE =
            "UPDATE leave_requests SET status = 'approved', approver_id = ? WHERE request_id = ?";

    // ✅ No decision_date here
    public static final String DENY =
            "UPDATE leave_requests SET status = 'denied', approver_id = ? WHERE request_id = ?";

    // (your cancel rule – pending-only)
    public static final String CANCEL =
            "UPDATE leave_requests SET status = 'cancelled' WHERE request_id = ? AND status = 'pending'";

    public static final String DELETE =
            "DELETE FROM leave_requests WHERE request_id = ?";


    // path: src/main/java/com/ismet/common/sql/LeaveRequestSQL.java
    public static final String CHECK_OVERLAP =
            "SELECT 1 FROM leave_requests " +
                    " WHERE employee_id = ? AND status IN ('approved','pending')" +
                    "   AND NOT (end_date < ? OR start_date > ?)" +
                    "   AND request_id <> ?" +
                    " LIMIT 1";

}
