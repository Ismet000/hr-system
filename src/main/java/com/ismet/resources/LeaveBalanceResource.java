package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.LeaveBalance;
import com.ismet.services.LeaveBalanceService;
import com.ismet.services.LeaveBalanceServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/leave-balances")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LeaveBalanceResource extends AbstractResource {

    private final LeaveBalanceService service = new LeaveBalanceServiceImpl();

    /** Create or update a balance row (by employee_id + leave_type_id + year) */
    @POST
    @Path("/upsert")
    public Response upsert(String payload) {
        try {
            LeaveBalance lb = gson().fromJson(payload, LeaveBalance.class);
            LeaveBalance saved = service.upsert(lb);
            return Response.ok(saved).build();
        } catch (Exception e) {
            return bad(e);
        }
    }

    /** Get one balance by primary key */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            LeaveBalance lb = service.getById(id);
            if (lb == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(lb).build();
        } catch (Exception e) {
            return server(e);
        }
    }

    /** List balances for an employee, optionally filter by year */
    @GET
    public Response list(@QueryParam("employee_id") String employeeId,
                         @QueryParam("year") Integer year) {
        try {
            if (employeeId == null || employeeId.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"employee_id is required\"}")
                        .build();
            }
            if (year != null) {
                return Response.ok(service.listByEmployeeYear(employeeId, year)).build();
            } else {
                return Response.ok(service.listByEmployee(employeeId)).build();
            }
        } catch (Exception e) {
            return server(e);
        }
    }

    /** Consume N days (e.g., on approval) */
    @PUT
    @Path("/{id}/use")
    public Response use(@PathParam("id") String id, @QueryParam("days") Integer days) {
        try {
            if (days == null) return bad400("days is required");
            service.useDays(id, days);
            return Response.ok().build();
        } catch (Exception e) {
            return bad(e);
        }
    }

    /** Give back N days (e.g., on cancellation/denial) */
    @PUT
    @Path("/{id}/release")
    public Response release(@PathParam("id") String id, @QueryParam("days") Integer days) {
        try {
            if (days == null) return bad400("days is required");
            service.releaseDays(id, days);
            return Response.ok().build();
        } catch (Exception e) {
            return bad(e);
        }
    }

    /** Admin: set the yearly total days (recomputes remaining on read) */
    @PUT
    @Path("/{id}/set-total")
    public Response setTotal(@PathParam("id") String id, @QueryParam("value") Integer value) {
        try {
            if (value == null) return bad400("value is required");
            service.setTotalDays(id, value);
            return Response.ok().build();
        } catch (Exception e) {
            return bad(e);
        }
    }

    /** Delete a balance row */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            service.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return bad(e);
        }
    }

    // --- small helpers (same style you’ve used elsewhere) ---
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
    private Response bad400(String msg) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + msg + "\"}")
                .build();
    }
}
