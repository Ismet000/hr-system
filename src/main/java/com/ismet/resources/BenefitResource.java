package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.Benefit;
import com.ismet.services.BenefitService;
import com.ismet.services.BenefitServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/benefits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BenefitResource extends AbstractResource {


    private final BenefitService service = new BenefitServiceImpl();

    @POST
    public Response create(String payload) {
        try {
            Benefit b = gson().fromJson(payload, Benefit.class);
            service.create(b);
            return Response.ok(b).build();
        } catch (Exception e) { return bad(e); }
    }

    @PUT @Path("/{id}")
    public Response update(@PathParam("id") String id, String payload) {
        try {
            Benefit b = gson().fromJson(payload, Benefit.class);
            b.setId(id);
            service.update(b);
            return Response.ok(b).build();
        } catch (Exception e) { return bad(e); }
    }

    @GET @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        try {
            Benefit b = service.getById(id);
            if (b == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(b).build();
        } catch (Exception e) { return server(e); }
    }

    @GET
    public Response listByEmployee(@QueryParam("employee_id") String employeeId) {
        try {
            if (employeeId == null || employeeId.isBlank())
                return bad400("employee_id is required");
            return Response.ok(service.listByEmployee(employeeId)).build();
        } catch (Exception e) { return server(e); }
    }

    @GET @Path("/active")
    public Response listActive(@QueryParam("employee_id") String employeeId,
                               @QueryParam("from") String from,
                               @QueryParam("to") String to) {
        try {
            if (employeeId == null || employeeId.isBlank() || from == null || from.isBlank() || to == null || to.isBlank())
                return bad400("employee_id, from, to are required");
            return Response.ok(service.listActiveInPeriod(employeeId, from, to)).build();
        } catch (Exception e) { return server(e); }
    }

    @PUT @Path("/{id}/status")
    public Response setStatus(@PathParam("id") String id, @QueryParam("value") String value) {
        try {
            if (value == null || value.isBlank()) return bad400("value is required (active|inactive)");
            service.setStatus(id, value);
            return Response.ok().build();
        } catch (Exception e) { return bad(e); }
    }

    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try { service.delete(id); return Response.noContent().build(); }
        catch (Exception e) { return bad(e); }
    }

    // helpers
    private Response bad(Exception e){
        String m = e.getMessage()==null ? "Request failed" : e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
    private Response server(Exception e){
        String m = e.getMessage()==null ? "Server error" : e.getMessage();
        return Response.serverError()
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
    private Response bad400(String msg){
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + msg + "\"}").build();
    }
}
