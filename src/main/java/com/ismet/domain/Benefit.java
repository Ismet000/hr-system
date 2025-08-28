package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Benefit extends AbstractEntity {
    @SerializedName(value = "employee_id",   alternate = {"employeeId"})
    private String employeeId;

    @SerializedName(value = "benefit_type",  alternate = {"benefitType"})
    private String benefitType;

    // Optional for pure-CRUD; required if used in payroll:
    @SerializedName("amount")
    private BigDecimal amount;

    @SerializedName("currency")
    private String currency;

    @SerializedName("taxable")
    private Boolean taxable;

    @SerializedName(value = "effective_date", alternate = {"effectiveDate"})
    private String effectiveDate; // yyyy-MM-dd

    @SerializedName(value = "end_date",       alternate = {"endDate"})
    private String endDate;       // yyyy-MM-dd or null

    @SerializedName("status") // 'active' | 'inactive'
    private String status;


    @SerializedName("details")
    private String details;

    @SerializedName("created_at")
    private String createdAt; // read-only

    public Benefit() {}

    public Benefit(ResultSet rs, String alias) throws SQLException {
        String a = (alias != null && !alias.isEmpty())
                ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(a + "benefit_id"));
        employeeId  = rs.getString(a + "employee_id");
        benefitType = rs.getString(a + "benefit_type");

        try { amount       = rs.getBigDecimal(a + "amount"); } catch (SQLException ignore) {}
        try { currency     = rs.getString(a + "currency"); }  catch (SQLException ignore) {}
        try { taxable      = rs.getBoolean(a + "taxable"); }  catch (SQLException ignore) {}
        try { effectiveDate= rs.getString(a + "effective_date"); } catch (SQLException ignore) {}
        try { endDate      = rs.getString(a + "end_date"); }       catch (SQLException ignore) {}
        try { status       = rs.getString(a + "status"); }         catch (SQLException ignore) {}
        try { details      = rs.getString(a + "details"); }        catch (SQLException ignore) {}
        try { createdAt    = rs.getString(a + "created_at"); }     catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is mandatory";
        if (benefitType == null || benefitType.isBlank()) return "benefit_type is mandatory";
        // If using payroll math, enforce these:
        if (amount == null) amount = BigDecimal.ZERO;
        if (currency == null || currency.isBlank()) currency = "USD";
        if (taxable == null) taxable = Boolean.FALSE;
        // effective_date required for period selection in payroll:
        if (effectiveDate == null || effectiveDate.isBlank())
            return "effective_date is mandatory (yyyy-MM-dd)";
        if (status == null || status.isBlank()) status = "active";
        return null;
    }

    /** INSERT */
    public void populateInsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1, getId());
        ps.setString(2, employeeId);
        ps.setString(3, benefitType);
        ps.setBigDecimal(4, amount == null ? BigDecimal.ZERO : amount);
        ps.setString(5, (currency == null || currency.isBlank()) ? "USD" : currency.toUpperCase());
        ps.setBoolean(6, taxable != null && taxable);
        ps.setString(7, effectiveDate);
        ps.setString(8, endDate);
        ps.setString(9, details);
        ps.setString(10, status == null ? "active" : status);
    }

    /** UPDATE columns */
    public void populateUpdatePs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) throw new Exception("benefit_id (id) is required");
        ps.setString(1, benefitType);
        ps.setBigDecimal(2, amount == null ? BigDecimal.ZERO : amount);
        ps.setString(3, (currency == null || currency.isBlank()) ? "USD" : currency.toUpperCase());
        ps.setBoolean(4, taxable != null && taxable);
        ps.setString(5, effectiveDate);
        ps.setString(6, endDate);
        ps.setString(7, details);
        ps.setString(8, status == null ? "active" : status);
        ps.setString(9, getId());
    }

    // getters/setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String v) { employeeId = v; }
    public String getBenefitType() { return benefitType; }
    public void setBenefitType(String v) { benefitType = v; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal v) { amount = v; }
    public String getCurrency() { return currency; }
    public void setCurrency(String v) { currency = v; }
    public Boolean getTaxable() { return taxable; }
    public void setTaxable(Boolean v) { taxable = v; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String v) { effectiveDate = v; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String v) { endDate = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public String getDetails() { return details; }
    public void setDetails(String v) { details = v; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String v) { createdAt = v; }
}
