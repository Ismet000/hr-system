package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.AttendanceException;
import com.ismet.services.AttendanceExceptionService;
import com.ismet.services.AttendanceExceptionServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/attendance-exceptions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendanceExceptionResource extends AbstractResource {


    private final AttendanceExceptionService service = new AttendanceExceptionServiceImpl();

    @POST
    @Path("/submit")
    public Response submit(String payload) {
        try {
            AttendanceException ex = gson().fromJson(payload, AttendanceException.class);
            service.submit(ex);
            return Response.ok(ex).build();
        } catch (Exception e) { return bad(e); }
    }

    @PUT
    @Path("/{id}/approve")
    public Response approve(@PathParam("id") String id, @QueryParam("approver_id") String approverId) {
        try { service.approve(id, approverId); return Response.ok().build(); }
        catch (Exception e) { return bad(e); }
    }

    @PUT @Path("/{id}/deny")
    public Response deny(@PathParam("id") String id, @QueryParam("approver_id") String approverId) {
        try { service.deny(id, approverId); return Response.ok().build(); }
        catch (Exception e) { return bad(e); }
    }

    @GET @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            var ex = service.getById(id);
            if (ex == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(ex).build();
        } catch (Exception e) { return bad(e); }
    }

    @GET @Path("/pending")
    public Response listPending() {
        try { return Response.ok(service.listPending()).build(); }
        catch (Exception e) { return bad(e); }
    }

    private Response bad(Exception e) {
        String m = e.getMessage()==null ? "Request failed" : e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
}
