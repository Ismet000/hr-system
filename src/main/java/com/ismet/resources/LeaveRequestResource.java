package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.LeaveRequest;
import com.ismet.services.LeaveRequestService;
import com.ismet.services.LeaveRequestServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/leave/requests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LeaveRequestResource extends AbstractResource {

    LeaveRequestService service = new LeaveRequestServiceImpl();



    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok("{\"msg\":\"LeaveRequestResource alive\"}").build();
    }


    @POST @Path("/submit")
    public Response submit(String payload) {
        try {
            LeaveRequest lr = gson().fromJson(payload, LeaveRequest.class); // <-- MUST be LeaveRequest.class
            service.submit(lr);                                             // <-- MUST call LeaveRequest service
            return Response.ok(lr).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + (e.getMessage() == null ? "Request failed" : e.getMessage()) + "\"}")
                    .build();
        }
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

    @PUT @Path("/{id}/cancel")
    public Response cancel(@PathParam("id") String id) {
        try { service.cancel(id); return Response.ok().build(); }
        catch (Exception e) { return bad(e); }
    }

    @GET @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            LeaveRequest lr = service.getById(id);
            if (lr == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(lr).build();
        } catch (Exception e) { return server(e); }
    }

    @GET @Path("/all")
    public Response getAll() {
        try { return Response.ok(service.getAll()).build(); }
        catch (Exception e) { return server(e); }
    }

    @GET
    public Response getByEmployee(@QueryParam("employee_id") String employeeId,
                                  @QueryParam("status") String status) {
        try {
            if (employeeId != null && !employeeId.isBlank())
                return Response.ok(service.getByEmployee(employeeId)).build();
            if (status != null && !status.isBlank())
                return Response.ok(service.getByStatus(status)).build();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Provide employee_id or status\"}").build();
        } catch (Exception e) { return server(e); }
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
