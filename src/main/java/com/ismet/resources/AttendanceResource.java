package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.AttendanceRecord;
import com.ismet.services.AttendanceService;
import com.ismet.services.AttendanceServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/attendance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendanceResource extends AbstractResource {

    AttendanceService service = new AttendanceServiceImpl();

    @POST
    @Path("/clock-in")
    public Response clockIn(String payload) {
        try {
            AttendanceRecord rec = gson().fromJson(payload, AttendanceRecord.class);
            String id = service.clockIn(rec);
            return Response.ok("{\"record_id\":\"" + id + "\"}").build();
        } catch (Exception e) { return bad(e); }
    }

    @PUT
    @Path("/{id}/clock-out")
    public Response clockOut(@PathParam("id") String id, @QueryParam("clock_out") String clockOutTs) {
        try {
            service.clockOut(id, clockOutTs);
            return Response.ok().build();
        } catch (Exception e) { return bad(e); }
    }

    @GET @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            var rec = service.getById(id);
            if (rec == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(rec).build();
        } catch (Exception e) { return server(e); }
    }

    @GET
    public Response getByEmployeeRange(@QueryParam("employee_id") String employeeId,
                                       @QueryParam("from") String fromDate,
                                       @QueryParam("to") String toDate,
                                       @QueryParam("work_date") String workDate) {
        try {
            if (employeeId == null || employeeId.isBlank())
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"employee_id is required\"}").build();

            if (workDate != null && !workDate.isBlank())
                return Response.ok(service.getByEmployeeOnDay(employeeId, workDate)).build();

            if (fromDate != null && toDate != null)
                return Response.ok(service.getByEmployeeInRange(employeeId, fromDate, toDate)).build();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Provide either work_date or from/to range\"}").build();
        } catch (Exception e) { return server(e); }
    }

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
