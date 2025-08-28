package com.ismet.common.sql;

public class EmployeeSQL {

//        public static final String INSERT_EMPLOYEE =
//                "INSERT INTO employees (id, employee_name, employee_last_name, employee_email) VALUES (?, ?, ?, ?)";
//
//    public static final String UPDATE_EMPLOYEE =
//            "UPDATE employees " +
//                    "SET employee_name = ?, " +
//                    "employee_last_name = ?, " +
//                    "employee_email = ? " +
//                    "WHERE id = ?";
//
//
//    public static final String GET_EMPLOYEE_BY_ID =
//                "SELECT * FROM employees WHERE id = ?";
//
//        public static final String GET_ALL_EMPLOYEES =
//                "SELECT * FROM employees";



    public static final String INSERT_EMPLOYEE =
            "INSERT INTO employees " +
                    "(employee_id, first_name, last_name, email, contact, position_id, department_id, hire_date, employment_type, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_EMPLOYEE =
            "UPDATE employees SET " +
                    "first_name = ?, " +
                    "last_name = ?, " +
                    "email = ?, " +
                    "contact = ?, " +
                    "position_id = ?, " +
                    "department_id = ?, " +
                    "hire_date = ?, " +
                    "employment_type = ?, " +
                    "status = ? " +
                    "WHERE employee_id = ?";

    public static final String GET_EMPLOYEE_BY_ID =
            "SELECT * FROM employees WHERE employee_id = ?";

    public static final String GET_ALL_EMPLOYEES =
            "SELECT * FROM employees";

    public static final String DELETE_EMPLOYEE =
            "DELETE FROM employees WHERE employee_id = ?";

}
