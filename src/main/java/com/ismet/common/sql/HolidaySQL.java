package com.ismet.common.sql;

public class HolidaySQL {

    public static final String INSERT =
            "INSERT INTO holidays (holiday_id, `date`, description) VALUES (?, ?, ?)";

    public static final String UPDATE =
            "UPDATE holidays SET `date` = ?, description = ? WHERE holiday_id = ?";

    public static final String DELETE =
            "DELETE FROM holidays WHERE holiday_id = ?";

    public static final String GET_BY_ID =
            "SELECT * FROM holidays WHERE holiday_id = ?";

    public static final String GET_BY_DATE =
            "SELECT * FROM holidays WHERE `date` = ?";

    public static final String LIST_IN_RANGE =
            "SELECT * FROM holidays WHERE `date` BETWEEN ? AND ? ORDER BY `date`";

    public static final String LIST_ALL =
            "SELECT * FROM holidays ORDER BY `date` DESC";
}
