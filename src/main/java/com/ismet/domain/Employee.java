package com.ismet.domain;


import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Employee extends AbstractEntity {

    @SerializedName(value = "first_name",  alternate = {"employee_name"})
    private String firstName;

    @SerializedName(value = "last_name",   alternate = {"employee_last_name"})
    private String lastName;

    @SerializedName(value = "email",       alternate = {"employee_email"})
    private String email;

    @SerializedName("contact")
    private String contact;

    @SerializedName("position_id")
    private String positionId;

    @SerializedName("department_id")
    private String departmentId;

    @SerializedName("hire_date")
    private String hireDate;

    @SerializedName("employment_type")
    private String employmentType;

    @SerializedName("status")
    private String status;

    // --- ctors ---
    public Employee() {}

    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName; this.lastName = lastName; this.email = email;
    }

    // Construct from ResultSet (optionally using alias like "e")
    public Employee(ResultSet rs, String alias) throws SQLException {
        alias = generateAlias(alias);
        // PK column name is employee_id in DB
        setId(rs.getString(alias + "employee_id"));
        setFirstName(rs.getString(alias + "first_name"));
        setLastName(rs.getString(alias + "last_name"));
        setEmail(rs.getString(alias + "email"));
        setContact(rs.getString(alias + "contact"));
        setPositionId(rs.getString(alias + "position_id"));
        setDepartmentId(rs.getString(alias + "department_id"));
        setHireDate(rs.getString(alias + "hire_date"));
        setEmploymentType(rs.getString(alias + "employment_type"));
        setStatus(rs.getString(alias + "status"));
    }

    @Override
    public String validate() {
        if (firstName == null || firstName.isEmpty())  return "First name is mandatory";
        if (lastName  == null || lastName.isEmpty())   return "Last name is mandatory";
        if (email     == null || email.isEmpty())      return "Email is mandatory";
        // Optional light checks (you can harden later)
        // if (hireDate == null || !hireDate.matches("\\d{4}-\\d{2}-\\d{2}")) return "hire_date must be YYYY-MM-DD";
        return null;
    }

    /** Fills INSERT prepared statement in the exact order of EmployeeSQL.INSERT */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1,  getId());         // employee_id
        ps.setString(2,  getFirstName());
        ps.setString(3,  getLastName());
        ps.setString(4,  getEmail());
        ps.setString(5,  getContact());
        ps.setString(6,  getPositionId());
        ps.setString(7,  getDepartmentId());
        ps.setString(8,  getHireDate());
        ps.setString(9,  getEmploymentType());
        ps.setString(10, getStatus());
    }

    /** Fills UPDATE prepared statement in the exact order of EmployeeSQL.UPDATE */
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("employee_id (id) is required for update");
        ps.setString(1,  getFirstName());
        ps.setString(2,  getLastName());
        ps.setString(3,  getEmail());
        ps.setString(4,  getContact());
        ps.setString(5,  getPositionId());
        ps.setString(6,  getDepartmentId());
        ps.setString(7,  getHireDate());
        ps.setString(8,  getEmploymentType());
        ps.setString(9,  getStatus());
        ps.setString(10, getId());         // WHERE employee_id = ?
    }

    // --- getters/setters ---
    public String getFirstName() { return firstName; }
    public void setFirstName(String v) { this.firstName = v; }

    public String getLastName() { return lastName; }
    public void setLastName(String v) { this.lastName = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getContact() { return contact; }
    public void setContact(String v) { this.contact = v; }

    public String getPositionId() { return positionId; }
    public void setPositionId(String v) { this.positionId = v; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String v) { this.departmentId = v; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String v) { this.hireDate = v; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String v) { this.employmentType = v; }

    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }

    // --- helpers ---
//    public String generateAlias(String alias){
//        return alias!=null && !alias.isEmpty() ? (alias.contains(".") ? alias : alias + ".") : "";
//    }
}
