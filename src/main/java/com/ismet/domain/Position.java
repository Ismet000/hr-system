package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Position extends AbstractEntity {

    @SerializedName("title")
    private String title;

    @SerializedName(value = "salary_grade", alternate = {"salaryGrade"})
    private String salaryGrade;


    public Position() {}

    public Position(String title, String salaryGrade) {
        this.title = title;
        this.salaryGrade = salaryGrade;
    }

    /** Construct from a ResultSet row. Pass alias like "p" if you did "SELECT p.* FROM positions p" */
    public Position(ResultSet rs, String alias) throws SQLException {
        alias = generateAlias(alias);
        setId(rs.getString(alias + "position_id"));
        setTitle(rs.getString(alias + "title"));
        setSalaryGrade(rs.getString(alias + "salary_grade"));
    }


    @Override
    public String validate() {
        if (title == null || title.isEmpty()) {
            return "Position title is mandatory";
        }
        return null;
    }

    /** For: INSERT INTO positions (position_id, title, salary_grade) VALUES (?,?,?) */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) {
            setId(UUID.randomUUID().toString());
        }
        ps.setString(1, getId());          // position_id
        ps.setString(2, getTitle());       // title
        ps.setString(3, getSalaryGrade()); // salary_grade
    }

    /** For: UPDATE positions SET title=?, salary_grade=? WHERE position_id=? */
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) {
            throw new Exception("position_id (id) is required for update");
        }
        ps.setString(1, getTitle());
        ps.setString(2, getSalaryGrade());
        ps.setString(3, getId());
    }

    // --- getters/setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSalaryGrade() { return salaryGrade; }
    public void setSalaryGrade(String salaryGrade) { this.salaryGrade = salaryGrade; }
}
