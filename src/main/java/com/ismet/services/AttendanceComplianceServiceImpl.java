package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.AttendanceSQL;
import com.ismet.common.sql.HolidaySQL;
import com.ismet.domain.AttendanceComplianceReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AttendanceComplianceServiceImpl extends AbstractService implements AttendanceComplianceService {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public AttendanceComplianceReport compute(String employeeId,
                                              String from, String to,
                                              Double dailyLimit, Double weeklyLimit,
                                              boolean skipWeekends, boolean skipHolidays) throws Exception {
        if (employeeId == null || employeeId.isBlank()) throw new Exception("employee_id is required");
        if (from == null || from.isBlank()) throw new Exception("from is required (yyyy-MM-dd)");
        if (to == null || to.isBlank()) throw new Exception("to is required (yyyy-MM-dd)");

        final double dailyMax  = (dailyLimit  == null ? 12.0 : dailyLimit);
        final double weeklyMax = (weeklyLimit == null ? 40.0 : weeklyLimit);

        LocalDate fromDate = LocalDate.parse(from, DF);
        LocalDate toDate   = LocalDate.parse(to, DF);
        if (toDate.isBefore(fromDate)) throw new Exception("to must be >= from");

        // Optional: don't count future days as absent
        LocalDate today = LocalDate.now();
        if (toDate.isAfter(today)) toDate = today;

        // 1) Load daily hours from attendance_records
        Map<LocalDate, Double> daily = loadDailyHours(employeeId, from, to);

        // 2) Load holidays in range (if requested)
        Set<LocalDate> holidaySet = skipHolidays ? loadHolidays(from, to) : Collections.emptySet();

        AttendanceComplianceReport rep = new AttendanceComplianceReport();
        rep.employeeId   = employeeId;
        rep.from         = from;
        rep.to           = to;
        rep.dailyLimit   = dailyMax;
        rep.weeklyLimit  = weeklyMax;
        rep.absentDays   = new ArrayList<>();
        rep.excessiveDays = new ArrayList<>();
        rep.weeklyOvertime = new ArrayList<>();

        // 3) Walk each day and classify
        Map<LocalDate, Double> weekTotals = new HashMap<>();
        LocalDate cursor = fromDate;

        while (!cursor.isAfter(toDate)) {
            boolean isWeekend = cursor.getDayOfWeek() == DayOfWeek.SATURDAY || cursor.getDayOfWeek() == DayOfWeek.SUNDAY;
            if ((skipWeekends && isWeekend) || (skipHolidays && holidaySet.contains(cursor))) {
                cursor = cursor.plusDays(1);
                continue; // not a workday for this report
            }

            double hours = daily.getOrDefault(cursor, 0.0);
            if (!daily.containsKey(cursor)) {
                // mark absent only if it's a considered workday
                rep.absentDays.add(cursor.format(DF));
            } else if (hours > dailyMax) {
                rep.excessiveDays.add(new AttendanceComplianceReport.DayHours(cursor.format(DF), round2(hours)));
            }

            // accumulate into week (Mon→Sun)
            LocalDate weekStart = cursor.with(DayOfWeek.MONDAY);
            weekTotals.merge(weekStart, hours, Double::sum);

            cursor = cursor.plusDays(1);
        }

        // 4) Compute weekly overtime (Mon→Sun)
        for (Map.Entry<LocalDate, Double> e : weekTotals.entrySet()) {
            double total = e.getValue();
            if (total > weeklyMax) {
                LocalDate ws = e.getKey();
                LocalDate we = ws.plusDays(6);
                rep.weeklyOvertime.add(
                        new AttendanceComplianceReport.WeekHours(
                                ws.format(DF), we.format(DF),
                                round2(total), round2(total - weeklyMax)
                        )
                );
            }
        }

        // sort outputs for nicer JSON (optional)
        Collections.sort(rep.absentDays);
        rep.excessiveDays.sort(Comparator.comparing(d -> d.date));
        rep.weeklyOvertime.sort(Comparator.comparing(w -> w.weekStart));

        return rep;
    }

    // --- helpers -------------------------------------------------------------

    private Map<LocalDate, Double> loadDailyHours(String employeeId, String from, String to) throws Exception {
        Map<LocalDate, Double> map = new HashMap<>();
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(AttendanceSQL.GET_BY_EMPLOYEE_RANGE);
            ps.setString(1, employeeId);
            ps.setString(2, from);
            ps.setString(3, to);
            rs = ps.executeQuery();
            while (rs.next()) {
                // column is named `date` in DB
                LocalDate d = rs.getDate("date").toLocalDate();
                double hw = rs.getDouble("hours_worked"); // may be null -> 0.0 via getDouble()
                map.merge(d, hw, Double::sum);
            }
        } finally { close(rs, ps, con); }
        return map;
    }

    private Set<LocalDate> loadHolidays(String from, String to) throws Exception {
        Set<LocalDate> set = new HashSet<>();
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.LIST_IN_RANGE);
            ps.setString(1, from);
            ps.setString(2, to);
            rs = ps.executeQuery();
            while (rs.next()) set.add(rs.getDate("date").toLocalDate());
        } finally { close(rs, ps, con); }
        return set;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}