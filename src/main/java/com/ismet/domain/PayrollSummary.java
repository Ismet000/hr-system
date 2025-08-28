package com.ismet.domain;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PayrollSummary {
    @SerializedName("from")        public String from;
    @SerializedName("to")          public String to;
    @SerializedName("employee_id") public String employeeId; // optional

    @SerializedName("payrolls_count") public int payrollsCount;
    @SerializedName("gross_total")    public BigDecimal grossTotal;
    @SerializedName("tax_total")      public BigDecimal taxTotal;
    @SerializedName("net_total")      public BigDecimal netTotal;
}
