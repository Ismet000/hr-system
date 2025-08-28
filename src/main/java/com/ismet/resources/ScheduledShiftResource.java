package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.ScheduledShift;
import com.ismet.services.ScheduledShiftService;
import com.ismet.services.ScheduledShiftServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/shifts")
@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
public class ScheduledShiftResource extends AbstractResource {

    private final ScheduledShiftService service = new ScheduledShiftServiceImpl();

    @POST
    public Response create(String payload) {
        try {
            ScheduledShift s = gson().fromJson(payload, ScheduledShift.class);
            String id = service.create(s);
            return Response.ok("{\"shift_id\":\""+id+"\"}").build();
        } catch (Exception e) { return bad(e); }
    }

    @PUT @Path("/{id}") public Response update(@PathParam("id") String id, String payload) {
        try {
            ScheduledShift s = gson().fromJson(payload, ScheduledShift.class);
            s.setId(id); service.update(s); return Response.ok().build();
        } catch (Exception e) { return bad(e); }
    }

    @GET @Path("/{id}") public Response get(@PathParam("id") String id) {
        try {
            var s = service.getById(id); if (s==null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(s).build();
        } catch (Exception e) { return server(e); }
    }

    @GET public Response list(@QueryParam("employee_id") String empId,
                              @QueryParam("from") String from,
                              @QueryParam("to") String to,
                              @QueryParam("date") String date) {
        try {
            if (date!=null && !date.isBlank()) return Response.ok(service.listByDate(date)).build();
            if (empId==null || empId.isBlank() || from==null || to==null)
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Provide ?date=YYYY-MM-DD OR employee_id&from&to\"}").build();
            return Response.ok(service.listByEmployeeRange(empId, from, to)).build();
        } catch (Exception e) { return server(e); }
    }

    @DELETE @Path("/{id}") public Response delete(@PathParam("id") String id) {
        try { service.delete(id); return Response.noContent().build(); }
        catch (Exception e) { return bad(e); }
    }

    private Response bad(Exception e){
        String m = e.getMessage()==null?"Request failed":e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\""+m.replace("\"","\\\"")+"\"}").build();
    }
    private Response server(Exception e){
        String m = e.getMessage()==null?"Server error":e.getMessage();
        return Response.serverError().entity("{\"error\":\""+m.replace("\"","\\\"")+"\"}").build();
    }
}
