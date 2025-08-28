package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.AttendanceComplianceReport;
import com.ismet.services.AttendanceComplianceService;
import com.ismet.services.AttendanceComplianceServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/attendance/compliance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendanceComplianceResource extends AbstractResource {

    private final AttendanceComplianceService service = new AttendanceComplianceServiceImpl();

    /**
     * GET /api/attendance/compliance?employee_id=emp-001&from=2025-09-01&to=2025-09-30
     *    [&daily_limit=12] [&weekly_limit=40] [&skip_weekends=true] [&skip_holidays=true]
     */
    @GET
    public Response compute(@QueryParam("employee_id") String employeeId,
                            @QueryParam("from") String from,
                            @QueryParam("to") String to,
                            @QueryParam("daily_limit") Double dailyLimit,
                            @QueryParam("weekly_limit") Double weeklyLimit,
                            @QueryParam("skip_weekends") String skipWeekendsStr,
                            @QueryParam("skip_holidays") String skipHolidaysStr) {
        try {
            boolean skipWeekends = parseBool(skipWeekendsStr, false);
            boolean skipHolidays = parseBool(skipHolidaysStr, false);

            AttendanceComplianceReport rep = service.compute(
                    employeeId, from, to, dailyLimit, weeklyLimit, skipWeekends, skipHolidays
            );
            return Response.ok(rep).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + (e.getMessage() == null ? "Request failed" : e.getMessage()).replace("\"","\\\"") + "\"}")
                    .build();
        }
    }


    private boolean parseBool(String v, boolean defVal) {
        if (v == null || v.isBlank()) return defVal;
        String s = v.trim().toLowerCase();
        return s.equals("true") || s.equals("1") || s.equals("yes") || s.equals("on");
    }
}
