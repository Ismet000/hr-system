package com.ismet.common.sql;

public class AttendanceSQL {

//    public static final String INSERT_CLOCK_IN =
//            "INSERT INTO attendance_records " +
//                    "(record_id, employee_id, work_date, clock_in, clock_out, hours_worked, notes) " +
//                    "VALUES (?, ?, ?, ?, NULL, NULL, ?)";

//    public static final String INSERT =
//            "INSERT INTO attendance_records " +
//                    "(record_id, employee_id, `date`, clock_in, clock_out, hours_worked, note) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//
//
//    public static final String CLOCK_OUT =
//            "UPDATE attendance_records " +
//                    "SET clock_out = ?, hours_worked = ? " +
//                    "WHERE record_id = ?";
//
//    public static final String GET_BY_ID =
//            "SELECT * FROM attendance_records WHERE record_id = ?";
//
//    public static final String GET_BY_EMPLOYEE_RANGE =
//            "SELECT * FROM attendance_records " +
//                    "WHERE employee_id = ? AND work_date BETWEEN ? AND ? " +
//                    "ORDER BY work_date, clock_in";
//
//    public static final String GET_BY_EMPLOYEE_DAY =
//            "SELECT * FROM attendance_records " +
//                    "WHERE employee_id = ? AND work_date = ? " +
//                    "ORDER BY clock_in";
//
//    public static final String DELETE =
//            "DELETE FROM attendance_records WHERE record_id = ?";



    // 7 placeholders to match your binder (record_id, employee_id, `date`, clock_in, clock_out, hours_worked, note)
    public static final String INSERT =
            "INSERT INTO attendance_records " +
                    "(record_id, employee_id, `date`, clock_in, clock_out, hours_worked, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String CLOCK_OUT =
            "UPDATE attendance_records " +
                    "SET clock_out = ?, hours_worked = ? " +
                    "WHERE record_id = ?";

    public static final String GET_BY_ID =
            "SELECT * FROM attendance_records WHERE record_id = ?";

    public static final String GET_BY_EMPLOYEE_RANGE =
            "SELECT * FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` BETWEEN ? AND ? " +
                    "ORDER BY `date`, clock_in";

    public static final String GET_BY_EMPLOYEE_DAY =
            "SELECT * FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` = ? " +
                    "ORDER BY clock_in";

    public static final String DELETE =
            "DELETE FROM attendance_records WHERE record_id = ?";

    // NEW: daily hours in a range (one row per day due to UNIQUE(employee_id, date))
    public static final String LIST_DAILY_HOURS_RANGE =
            "SELECT `date`, COALESCE(hours_worked,0) AS hours " +
                    "FROM attendance_records " +
                    "WHERE employee_id = ? AND `date` BETWEEN ? AND ? " +
                    "ORDER BY `date`";
}
