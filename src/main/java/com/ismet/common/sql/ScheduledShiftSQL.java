package com.ismet.common.sql;

public class ScheduledShiftSQL {
    public static final String INSERT =
            "INSERT INTO scheduled_shifts (shift_id, employee_id, shift_date, start_time, end_time, status, note) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE =
            "UPDATE scheduled_shifts SET shift_date=?, start_time=?, end_time=?, status=?, note=? WHERE shift_id=?";

    public static final String DELETE = "DELETE FROM scheduled_shifts WHERE shift_id=?";

    public static final String GET_BY_ID = "SELECT * FROM scheduled_shifts WHERE shift_id=?";

    public static final String LIST_BY_EMP_RANGE =
            "SELECT * FROM scheduled_shifts WHERE employee_id=? AND shift_date BETWEEN ? AND ? ORDER BY shift_date,start_time";

    public static final String LIST_BY_DATE =
            "SELECT * FROM scheduled_shifts WHERE shift_date=? ORDER BY start_time";
}
