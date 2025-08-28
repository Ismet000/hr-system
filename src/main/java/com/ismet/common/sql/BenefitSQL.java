package com.ismet.common.sql;

public class BenefitSQL {

    public static final String INSERT =
            "INSERT INTO benefits " +
                    "(benefit_id, employee_id, benefit_type, amount, currency, taxable, effective_date, end_date, details, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE =
            "UPDATE benefits SET benefit_type=?, amount=?, currency=?, taxable=?, " +
                    "effective_date=?, end_date=?, details=?, status=? WHERE benefit_id=?";

    public static final String DELETE =
            "DELETE FROM benefits WHERE benefit_id=?";

    public static final String GET_BY_ID =
            "SELECT * FROM benefits WHERE benefit_id=?";

    public static final String LIST_BY_EMP =
            "SELECT * FROM benefits WHERE employee_id=? ORDER BY effective_date DESC";

    // Benefits active at any point in [from, to]
    public static final String LIST_ACTIVE_IN_PERIOD =
            "SELECT * FROM benefits " +
                    "WHERE employee_id=? AND status='active' " +
                    "AND effective_date <= ? " +
                    "AND (end_date IS NULL OR end_date >= ?) " +
                    "ORDER BY effective_date DESC";
}
