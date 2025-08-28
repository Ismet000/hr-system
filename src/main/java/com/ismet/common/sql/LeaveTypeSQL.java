package com.ismet.common.sql;

public class LeaveTypeSQL {
    public static final String INSERT =
            "INSERT INTO leave_types (type_id, name, max_days, is_paid) VALUES (?, ?, ?, ?)";
    public static final String UPDATE =
            "UPDATE leave_types SET name=?, max_days=?, is_paid=? WHERE type_id=?";
    public static final String GET_BY_ID =
            "SELECT * FROM leave_types WHERE type_id = ?";
    public static final String GET_ALL =
            "SELECT * FROM leave_types";
    public static final String GET_BY_NAME =
            "SELECT * FROM leave_types WHERE name = ?";
    public static final String DELETE =
            "DELETE FROM leave_types WHERE type_id = ?";
}
