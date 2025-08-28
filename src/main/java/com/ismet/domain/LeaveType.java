package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LeaveType extends AbstractEntity {

    @SerializedName(value = "name",      alternate = {"leaveTypeName"})
    private String name;

    @SerializedName(value = "max_days",  alternate = {"maxDays"})
    private Integer maxDays;

    @SerializedName(value = "is_paid",   alternate = {"isPaid"})
    private Boolean isPaid;

    @SerializedName("created_at")
    private String createdAt; // read-only

    public LeaveType() {}

    public LeaveType(ResultSet rs, String alias) throws SQLException {
        alias = (alias != null && !alias.isEmpty()) ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(alias + "type_id"));
        name     = rs.getString(alias + "name");
        maxDays  = rs.getInt(alias + "max_days");
        isPaid   = rs.getBoolean(alias + "is_paid");
        try { createdAt = rs.getString(alias + "created_at"); } catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (name == null || name.isBlank()) return "Leave type name is mandatory";
        if (maxDays == null || maxDays < 0) return "max_days must be >= 0";
        if (isPaid == null) return "is_paid must be provided";
        return null;
    }

    // INSERT
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, getName());
        ps.setInt(3, getMaxDays());
        ps.setBoolean(4, getIsPaid());
    }

    // UPDATE: SET name=?, max_days=?, is_paid=? WHERE type_id=?
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("type_id (id) is required for update");
        ps.setString(1, getName());
        ps.setInt(2, getMaxDays());
        ps.setBoolean(3, getIsPaid());
        ps.setString(4, getId());
    }

    // getters/setters
    public String getName() { return name; }
    public Integer getMaxDays() { return maxDays; }
    public Boolean getIsPaid() { return isPaid; }
    public String getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setMaxDays(Integer maxDays) { this.maxDays = maxDays; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
