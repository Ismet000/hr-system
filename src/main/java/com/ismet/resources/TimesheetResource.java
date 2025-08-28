package com.ismet.resources;


import com.ismet.common.AbstractResource;
import com.ismet.domain.Timesheet;
import com.ismet.services.TimesheetService;
import com.ismet.services.TimesheetServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/timesheets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TimesheetResource extends AbstractResource {

    TimesheetService service = new TimesheetServiceImpl();

    // POST /timesheets/generate?employee_id=emp-001&week_start=2025-08-18
    @POST
    @Path("/generate")
    public Response generate(@QueryParam("employee_id") String employeeId,
                             @QueryParam("week_start") String weekStart) {
        try {
            Timesheet ts = service.generate(employeeId, weekStart);
            return Response.ok(ts).build();
        } catch (Exception e) { return bad(e); }
    }

    // GET /timesheets/{id}
    @GET @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            Timesheet ts = service.getById(id);
            if (ts == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(ts).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /timesheets/one?employee_id=emp-001&week_start=2025-08-18
    @GET @Path("/one")
    public Response getByEmployeeWeek(@QueryParam("employee_id") String employeeId,
                                      @QueryParam("week_start") String weekStart) {
        try {
            Timesheet ts = service.getByEmployeeWeek(employeeId, weekStart);
            if (ts == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(ts).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /timesheets?employee_id=emp-001&from=2025-08-01&to=2025-09-30
    @GET
    public Response listRange(@QueryParam("employee_id") String employeeId,
                              @QueryParam("from") String fromWeek,
                              @QueryParam("to") String toWeek) {
        try {
            if (employeeId == null || employeeId.isBlank())
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"employee_id is required\"}").build();
            if (fromWeek == null || toWeek == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"from and to are required (week_start dates)\"}").build();

            List<Timesheet> list = service.listByEmployeeRange(employeeId, fromWeek, toWeek);
            return Response.ok(list).build();
        } catch (Exception e) { return server(e); }
    }

    // DELETE /timesheets/{id}
    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try { service.delete(id); return Response.noContent().build(); }
        catch (Exception e) { return bad(e); }
    }

    private Response bad(Exception e) {
        String m = e.getMessage() == null ? "Request failed" : e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
    private Response server(Exception e) {
        String m = e.getMessage() == null ? "Server error" : e.getMessage();
        return Response.serverError().entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
}
