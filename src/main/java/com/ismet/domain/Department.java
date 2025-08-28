package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Department extends AbstractEntity {


    @SerializedName("name")
    private String name;

    @SerializedName("manager_id")
    private String managerId;

    @SerializedName("created_at")
    private String createdAt;

    public Department() {}

    /** If you SELECT with alias "d" (e.g. SELECT d.* FROM departments d), pass "d"; otherwise pass "" */
    public Department(ResultSet rs, String alias) throws SQLException {
        alias = generateAlias(alias);
        setId(rs.getString(alias + "department_id"));
        this.name = rs.getString(alias + "name");
        this.managerId = rs.getString(alias + "manager_id");
        // created_at can be null in RS getString if not selected
        try { this.createdAt = rs.getString(alias + "created_at"); } catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (name == null || name.trim().isEmpty()) return "Department name is mandatory";
        return null;
    }

    /** INSERT */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) {
            setId(UUID.randomUUID().toString());
        }
        ps.setString(1, getId());
        ps.setString(2, getName());
        ps.setString(3, getManagerId());
    }


    /** UPDATE: SET name=?, manager_id=? WHERE department_id=? */
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("department_id (id) is required for update");
        ps.setString(1, getName());
        ps.setString(2, getManagerId());
        ps.setString(3, getId());
    }

    // getters/setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

}
