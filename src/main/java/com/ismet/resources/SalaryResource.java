package com.ismet.resources;


import com.ismet.common.AbstractResource;
import com.ismet.domain.Salary;
import com.ismet.services.SalaryService;
import com.ismet.services.SalaryServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/salaries")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SalaryResource extends AbstractResource {

    SalaryService service = new SalaryServiceImpl();

    // POST /salaries/insert
    @POST
    @Path("/insert")
    public Response insert(String payload) {
        try {
            Salary s = gson().fromJson(payload, Salary.class);
            if (s.getId() == null || s.getId().isBlank()) s.setId(UUID.randomUUID().toString());
            service.insert(s);
            return Response.ok("{\"id\":\"" + s.getId() + "\"}").build();
        } catch (Exception e) { return bad(e); }
    }

    // PUT /salaries/{id}/update
    @PUT
    @Path("/{id}/update")
    public Response update(@PathParam("id") String id, String payload) {
        try {
            Salary s = gson().fromJson(payload, Salary.class);
            s.setId(id);
            service.update(s);
            return Response.ok().build();
        } catch (Exception e) { return bad(e); }
    }

    // GET /salaries/{id}
    @GET @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            Salary s = service.getById(id);
            if (s == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(s).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /salaries/current?employee_id=emp-001
    @GET @Path("/current")
    public Response getCurrent(@QueryParam("employee_id") String employeeId) {
        try {
            if (employeeId == null || employeeId.isBlank())
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"employee_id is required\"}").build();
            Salary s = service.getCurrentForEmployee(employeeId);
            if (s == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(s).build();
        } catch (Exception e) { return server(e); }
    }

    // GET /salaries/history?employee_id=emp-001
    @GET @Path("/history")
    public Response history(@QueryParam("employee_id") String employeeId) {
        try {
            if (employeeId == null || employeeId.isBlank())
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"employee_id is required\"}").build();
            List<Salary> list = service.listHistoryForEmployee(employeeId);
            return Response.ok(list).build();
        } catch (Exception e) { return server(e); }
    }

    // DELETE /salaries/{id}
    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try { service.delete(id); return Response.noContent().build(); }
        catch (Exception e) { return bad(e); }
    }

    private Response bad(Exception e) {
        String m = e.getMessage() == null ? "Request failed" : e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}")
                .build();
    }
    private Response server(Exception e) {
        String m = e.getMessage() == null ? "Server error" : e.getMessage();
        return Response.serverError()
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}")
                .build();
    }

}
