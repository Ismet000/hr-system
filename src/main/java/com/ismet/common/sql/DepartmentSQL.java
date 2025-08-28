package com.ismet.common.sql;

public class DepartmentSQL {
    // INSERT (created_at is auto by DB)
    public static final String INSERT_DEPARTMENT =
            "INSERT INTO departments (department_id, name, manager_id) VALUES (?, ?, ?)";

    // UPDATE by PK
    public static final String UPDATE_DEPARTMENT =
            "UPDATE departments SET name = ?, manager_id = ? WHERE department_id = ?";

    // GET by id
    public static final String GET_DEPARTMENT_BY_ID =
            "SELECT * FROM departments WHERE department_id = ?";

    // GET all
    public static final String GET_ALL_DEPARTMENTS =
            "SELECT * FROM departments";

    // Optional helpers
    public static final String GET_DEPARTMENT_BY_NAME =
            "SELECT * FROM departments WHERE name = ?";

    public static final String DELETE_DEPARTMENT =
            "DELETE FROM departments WHERE department_id = ?";
}
