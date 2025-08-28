package com.ismet.domain;

import com.google.gson.annotations.SerializedName;
import com.ismet.common.AbstractEntity;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Payroll extends AbstractEntity {

    @SerializedName("employee_id")  private String employeeId;
    @SerializedName("period_start") private String periodStart;  // yyyy-MM-dd
    @SerializedName("period_end")   private String periodEnd;    // yyyy-MM-dd

    @SerializedName("gross_pay")    private BigDecimal grossPay;
    @SerializedName("taxes")        private BigDecimal taxes;
    @SerializedName("net_pay")      private BigDecimal netPay;

    @SerializedName("generated_date") private String generatedDate;

    public Payroll() {}

    public Payroll(ResultSet rs, String alias) throws SQLException {
        String a = (alias != null && !alias.isEmpty()) ? (alias.endsWith(".") ? alias : alias + ".") : "";
        setId(rs.getString(a + "payroll_id"));
        employeeId    = rs.getString(a + "employee_id");
        periodStart   = rs.getString(a + "period_start");
        periodEnd     = rs.getString(a + "period_end");
        grossPay      = rs.getBigDecimal(a + "gross_pay");
        taxes         = rs.getBigDecimal(a + "taxes");
        netPay        = rs.getBigDecimal(a + "net_pay");
        try { generatedDate = rs.getString(a + "generated_date"); } catch (SQLException ignore) {}
    }

    @Override
    public String validate() {
        if (employeeId == null || employeeId.isBlank()) return "employee_id is mandatory";
        if (periodStart == null || periodStart.isBlank()) return "period_start is mandatory (yyyy-MM-dd)";
        if (periodEnd == null || periodEnd.isBlank()) return "period_end is mandatory (yyyy-MM-dd)";
        if (grossPay == null) return "gross_pay is mandatory";
        if (taxes == null) return "taxes is mandatory";
        if (netPay == null) return "net_pay is mandatory";
        return null;
    }

    /** INSERT/UPSERT */
    public void populateUpsertPs(PreparedStatement ps) throws Exception {
        if (getId() == null || getId().isEmpty()) setId(UUID.randomUUID().toString());
        ps.setString(1,  getId());
        ps.setString(2,  employeeId);
        ps.setString(3,  periodStart);
        ps.setString(4,  periodEnd);
        ps.setBigDecimal(5,  grossPay);
        ps.setBigDecimal(6,  taxes);
        ps.setBigDecimal(7,  netPay);
    }

    // getters/setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getPeriodStart() { return periodStart; }
    public void setPeriodStart(String periodStart) { this.periodStart = periodStart; }
    public String getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(String periodEnd) { this.periodEnd = periodEnd; }
    public BigDecimal getGrossPay() { return grossPay; }
    public void setGrossPay(BigDecimal grossPay) { this.grossPay = grossPay; }
    public BigDecimal getTaxes() { return taxes; }
    public void setTaxes(BigDecimal taxes) { this.taxes = taxes; }
    public BigDecimal getNetPay() { return netPay; }
    public void setNetPay(BigDecimal netPay) { this.netPay = netPay; }
    public String getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(String generatedDate) { this.generatedDate = generatedDate; }
}
