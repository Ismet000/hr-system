package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Salary extends AbstractEntity {
    @SerializedName("employee_id")
    private String employeeId;

    @SerializedName("base_salary")
    private BigDecimal baseSalary;

    @SerializedName("currency")
    private String currency;

    @SerializedName("effective_date")
    private String effectiveDate;   // yyyy-MM-dd

    @SerializedName("created_at")
    private String createdAt;

    public Salary() {}

    public Salary(ResultSet rs, String alias) throws SQLException {
        String a = (alias != null && !alias.isEmpty()) ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(a + "salary_id"));
        this.employeeId   = rs.getString(a + "employee_id");
        this.baseSalary   = rs.getBigDecimal(a + "base_salary");
        try { this.currency = rs.getString(a + "currency"); } catch (SQLException ignore) {}
        this.effectiveDate= rs.getString(a + "effective_date");
        try { this.createdAt   = rs.getString(a + "created_at"); } catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is mandatory";
        if (effectiveDate == null || effectiveDate.isBlank()) return "effective_date is mandatory (yyyy-MM-dd)";
        if (baseSalary == null) return "base_salary is mandatory";
        if (baseSalary.compareTo(BigDecimal.ZERO) < 0) return "base_salary cannot be negative";
        if (currency != null && !currency.isBlank() && currency.length() != 3) return "currency must be a 3-letter code";
        return null;
    }

    /** INSERT: (salary_id, employee_id, base_salary, effective_date, currency) */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setBigDecimal(3, baseSalary);
        ps.setString(4, effectiveDate);
        // let DB default 'USD' if null/blank:
        if (currency == null || currency.isBlank()) ps.setString(5, "USD");
        else ps.setString(5, currency.toUpperCase());
    }

    /** UPDATE: SET base_salary=?, effective_date=?, currency=? WHERE salary_id=? */
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("salary_id (id) is required for update");
        ps.setBigDecimal(1, baseSalary);
        ps.setString(2, effectiveDate);
        ps.setString(3, (currency == null || currency.isBlank()) ? "USD" : currency.toUpperCase());
        ps.setString(4, getId());
    }

    // getters / setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
