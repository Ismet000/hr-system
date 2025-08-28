package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.PayrollSQL;
import com.ismet.domain.Payroll;
import com.ismet.domain.Salary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

public class PayrollServiceImpl extends AbstractService implements PayrollService {

    private static final boolean PAY_SALARIED = true;
    private static final int WEEKLY_LIMIT = 40;

    private static final String SALARY_ON_DATE =
            "SELECT * FROM salaries WHERE employee_id = ? AND effective_date <= ? " +
                    "ORDER BY effective_date DESC LIMIT 1";

    @Override
    public Payroll generate(String employeeId, String periodStart, String periodEnd, String currencyIgnored) throws Exception {
        if (employeeId == null || employeeId.isBlank()) throw new Exception("employee_id is required");
        if (periodStart == null || periodStart.isBlank()) throw new Exception("period_start is required");
        if (periodEnd == null || periodEnd.isBlank()) throw new Exception("period_end is required");

        Salary sal = getSalaryOnDate(employeeId, periodEnd);
        if (sal == null) throw new Exception("No salary found for employee on " + periodEnd);

        List<DayHours> days = loadDayHours(employeeId, periodStart, periodEnd);
        double[] split = splitRegularAndOvertimeWeekly(days);
        BigDecimal regularHours  = BigDecimal.valueOf(split[0]);
        BigDecimal overtimeHours = BigDecimal.valueOf(split[1]);

        BigDecimal standardMonthlyHours = new BigDecimal("160.00");
        BigDecimal hourly = sal.getBaseSalary().divide(standardMonthlyHours, 6, RoundingMode.HALF_UP);

        BigDecimal overtimeRate = hourly.multiply(new BigDecimal("1.5"));
        BigDecimal overtimePay  = overtimeRate.multiply(overtimeHours);

        BigDecimal benefits = sumBenefitsForPeriod(employeeId, periodStart, periodEnd);

        BigDecimal gross;
        if (PAY_SALARIED) {
            gross = sal.getBaseSalary().add(overtimePay).add(benefits);
        } else {
            BigDecimal regularPay = hourly.multiply(regularHours);
            gross = regularPay.add(overtimePay).add(benefits);
        }
        gross = gross.setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxes = gross.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal net   = gross.subtract(taxes).setScale(2, RoundingMode.HALF_UP);

        Payroll p = new Payroll();
        p.setEmployeeId(employeeId);
        p.setPeriodStart(periodStart);
        p.setPeriodEnd(periodEnd);
        p.setGrossPay(gross);
        p.setTaxes(taxes);
        p.setNetPay(net);

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.UPSERT);
            p.setId(UUID.randomUUID().toString());
            p.populateUpsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }

        return getByUnique(employeeId, periodStart, periodEnd);
    }

    @Override
    public Payroll getById(String payrollId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.GET_BY_ID);
            ps.setString(1, payrollId);
            rs = ps.executeQuery();
            if (rs.next()) return new Payroll(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<Payroll> listByEmployeeInPeriod(String employeeId, String from, String to) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Payroll> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.LIST_BY_EMP_PERIOD);
            ps.setString(1, employeeId);
            ps.setString(2, from);
            ps.setString(3, to);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new Payroll(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public List<Payroll> listInPeriod(String from, String to) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Payroll> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.LIST_IN_PERIOD);
            ps.setString(1, from);
            ps.setString(2, to);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new Payroll(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public void delete(String payrollId) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.DELETE);
            ps.setString(1, payrollId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    private Payroll getByUnique(String employeeId, String start, String end) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.GET_BY_UNIQUE);
            ps.setString(1, employeeId);
            ps.setString(2, start);
            ps.setString(3, end);
            rs = ps.executeQuery();
            if (rs.next()) return new Payroll(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    private Salary getSalaryOnDate(String employeeId, String onDate) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(SALARY_ON_DATE);
            ps.setString(1, employeeId);
            ps.setString(2, onDate);
            rs = ps.executeQuery();
            if (rs.next()) return new Salary(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    private static final class DayHours {
        final LocalDate date; final double hours;
        DayHours(LocalDate d, double h) { this.date = d; this.hours = h; }
    }

    private List<DayHours> loadDayHours(String employeeId, String from, String to) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<DayHours> out = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.ATTENDANCE_FOR_PERIOD);
            ps.setString(1, employeeId);
            ps.setString(2, from);
            ps.setString(3, to);
            rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate d = rs.getDate("date").toLocalDate();
                double h = rs.getDouble("hours_worked");
                out.add(new DayHours(d, h));
            }
        } finally { close(rs, ps, con); }
        return out;
    }

    private double[] splitRegularAndOvertimeWeekly(List<DayHours> days) {
        if (days == null || days.isEmpty()) return new double[]{0d, 0d};

        WeekFields wf = WeekFields.ISO;
        Map<String, Double> weeklyTotals = new HashMap<>();

        for (DayHours d : days) {
            int wk = d.date.get(wf.weekOfWeekBasedYear());
            int yr = d.date.get(wf.weekBasedYear());
            String key = yr + "-" + wk;
            weeklyTotals.merge(key, d.hours, Double::sum);
        }

        double regular = 0d, overtime = 0d;
        for (double tot : weeklyTotals.values()) {
            if (tot > WEEKLY_LIMIT) {
                overtime += (tot - WEEKLY_LIMIT);
                regular  += WEEKLY_LIMIT;
            } else {
                regular += tot;
            }
        }
        return new double[]{regular, overtime};
    }

    private BigDecimal sumBenefitsForPeriod(String employeeId, String from, String to) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PayrollSQL.SUM_BENEFITS_FOR_PERIOD);
            ps.setString(1, employeeId);
            ps.setString(2, to); // or bind both if you switch to BETWEEN in SQL
            rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total == null ? BigDecimal.ZERO : total;
            }
            return BigDecimal.ZERO;
        } finally { close(rs, ps, con); }
    }



}


