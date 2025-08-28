package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.AttendanceSummary;
import com.ismet.domain.LeaveSummary;
import com.ismet.domain.PayrollSummary;
import com.ismet.services.ReportingService;
import com.ismet.services.ReportingServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReportingResource extends AbstractResource {

    private final ReportingService service = new ReportingServiceImpl();

    // GET /api/reports/attendance-summary?employee_id=emp-001&from=2025-09-01&to=2025-09-30&weekly_limit=40
    @GET
    @Path("/attendance-summary")
    public Response attendanceSummary(@QueryParam("employee_id") String employeeId,
                                      @QueryParam("from") String from,
                                      @QueryParam("to") String to,
                                      @QueryParam("weekly_limit") @DefaultValue("40") double weeklyLimit) {
        try {
            if (isBlank(employeeId) || isBlank(from) || isBlank(to))
                return bad400("employee_id, from, to are required");
            AttendanceSummary s = service.getAttendanceSummary(employeeId, from, to, weeklyLimit);
            return Response.ok(s).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /api/reports/leave-summary?employee_id=emp-001&from=2025-09-01&to=2025-09-30
    @GET @Path("/leave-summary")
    public Response leaveSummary(@QueryParam("employee_id") String employeeId,
                                 @QueryParam("from") String from,
                                 @QueryParam("to") String to) {
        try {
            if (isBlank(employeeId) || isBlank(from) || isBlank(to))
                return bad400("employee_id, from, to are required");
            LeaveSummary s = service.getLeaveSummary(employeeId, from, to);
            return Response.ok(s).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /api/reports/payroll-summary?from=2025-09-01&to=2025-09-30[&employee_id=emp-001]
    @GET @Path("/payroll-summary")
    public Response payrollSummary(@QueryParam("from") String from,
                                   @QueryParam("to") String to,
                                   @QueryParam("employee_id") String employeeId) {
        try {
            if (isBlank(from) || isBlank(to))
                return bad400("from and to are required");
            PayrollSummary s = service.getPayrollSummary(from, to, employeeId);
            return Response.ok(s).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /api/reports/alerts/overdue-leave?days=3
    @GET @Path("/alerts/overdue-leave")
    public Response overdue(@QueryParam("days") @DefaultValue("3") int days) {
        try {
            return Response.ok(service.listOverdueApprovals(days)).build();
        } catch (Exception e) { return server(e); }
    }

    // --- helpers ---
    private boolean isBlank(String s){ return s==null || s.isBlank(); }
    private Response bad400(String msg) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + msg + "\"}").build();
    }
    private Response server(Exception e) {
        String m = e.getMessage()==null ? "Server error" : e.getMessage();
        return Response.serverError().entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }

    // GET /api/reports/roster?date=2025-09-02
    @GET @Path("/roster")
    public Response roster(@QueryParam("date") String date) {
        try { return Response.ok(service.roster(date)).build(); }
        catch (Exception e) { return bad(e); }
    }

    private Response bad(Exception e) {
        String m = e.getMessage()==null ? "Request failed" : e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
}
