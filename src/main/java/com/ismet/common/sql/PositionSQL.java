package com.ismet.common.sql;

public class PositionSQL {
    // Insert new position
    public static final String INSERT_POSITION =
            "INSERT INTO positions (position_id, title, salary_grade) VALUES (?, ?, ?)";

    // Update existing position
    public static final String UPDATE_POSITION =
            "UPDATE positions " +
                    "SET title = ?, " +
                    "salary_grade = ? " +
                    "WHERE position_id = ?";

    // Get single position by ID
    public static final String GET_POSITION_BY_ID =
            "SELECT * FROM positions WHERE position_id = ?";

    // Get all positions
    public static final String GET_ALL_POSITIONS =
            "SELECT * FROM positions";

    // Optional: Get position by title (since title is unique)
    public static final String GET_POSITION_BY_TITLE =
            "SELECT * FROM positions WHERE title = ?";
}
