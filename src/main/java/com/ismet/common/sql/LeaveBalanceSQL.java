package com.ismet.common.sql;

public class LeaveBalanceSQL {

    public static final String UPSERT =
            "INSERT INTO leave_balances " +
                    "(balance_id, employee_id, leave_type_id, `year`, total_days, used_days) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    " total_days = VALUES(total_days), " +
                    " used_days  = VALUES(used_days)";

    public static final String GET_BY_ID =
            "SELECT * FROM leave_balances WHERE balance_id = ?";

    public static final String GET_ONE_BY_KEYS =
            "SELECT * FROM leave_balances WHERE employee_id = ? AND leave_type_id = ? AND `year` = ?";

    public static final String LIST_BY_EMP =
            "SELECT * FROM leave_balances WHERE employee_id = ? ORDER BY `year` DESC, leave_type_id";

    public static final String LIST_BY_EMP_YEAR =
            "SELECT * FROM leave_balances WHERE employee_id = ? AND `year` = ? ORDER BY leave_type_id";

    public static final String ADD_USED =
            "UPDATE leave_balances SET used_days = used_days + ? WHERE balance_id = ?";

    public static final String RELEASE_USED =
            "UPDATE leave_balances SET used_days = used_days - ? WHERE balance_id = ?";

    public static final String SET_TOTAL =
            "UPDATE leave_balances SET total_days = ? WHERE balance_id = ?";

    public static final String DELETE =
            "DELETE FROM leave_balances WHERE balance_id = ?";
}
