package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Holiday extends AbstractEntity {
    @SerializedName("date")       private String date; // yyyy-MM-dd
    @SerializedName("description") private String description;

    public Holiday(ResultSet rs, String a) throws SQLException {
        String alias = (a != null && !a.isEmpty()) ? (a.endsWith(".") ? a : a + ".") : "";
        setId(rs.getString(alias + "holiday_id"));
        this.date = rs.getString(alias + "date");          // <-- column name is `date`
        this.description = rs.getString(alias + "description");
    }

    @Override
    public String validate() {
        if (date == null || date.isBlank()) return "date is mandatory (yyyy-MM-dd)";
        return null;
    }

    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(java.util.UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, date);
        ps.setString(3, description);
    }

    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("holiday_id (id) is required for update");
        ps.setString(1, date);
        ps.setString(2, description);
        ps.setString(3, getId());
    }
}
