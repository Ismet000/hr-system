package com.ismet.common.sql;

public class SalarySQL {

    public static final String INSERT =
            "INSERT INTO salaries (salary_id, employee_id, base_salary, effective_date, currency) " +
                    "VALUES (?, ?, ?, ?, ?)";

    public static final String UPDATE =
            "UPDATE salaries SET base_salary = ?, effective_date = ?, currency = ? WHERE salary_id = ?";

    public static final String DELETE =
            "DELETE FROM salaries WHERE salary_id = ?";

    public static final String GET_BY_ID =
            "SELECT * FROM salaries WHERE salary_id = ?";

    public static final String GET_CURRENT_FOR_EMP =
            "SELECT * FROM salaries WHERE employee_id = ? ORDER BY effective_date DESC LIMIT 1";

    public static final String LIST_HISTORY_FOR_EMP =
            "SELECT * FROM salaries WHERE employee_id = ? ORDER BY effective_date DESC";
}
