package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.Holiday;
import com.ismet.services.HolidayService;
import com.ismet.services.HolidayServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;


@Path("/holidays")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class HolidayResource extends LeaveTypeResource {

    HolidayService service = new HolidayServiceImpl();

    @POST
    @Path("/insert")
    public Response insert(String payload) {
        try {
            Holiday h = gson().fromJson(payload, Holiday.class);
            if (h.getId() == null || h.getId().isBlank()) h.setId(UUID.randomUUID().toString());
            service.insert(h);
            return Response.ok("{\"id\":\"" + h.getId() + "\"}").build();
        } catch (Exception e) { return bad(e); }
    }

    @PUT
    @Path("/{id}/update")
    public Response update(@PathParam("id") String id, String payload) {
        try {
            Holiday h = gson().fromJson(payload, Holiday.class);
            h.setId(id);
            service.update(h);
            return Response.ok().build();
        } catch (Exception e) { return bad(e); }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            Holiday h = service.getById(id);
            if (h == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(h).build();
        } catch (Exception e) { return server(e); }
    }

    @GET
    public Response query(@QueryParam("date") String date,
                          @QueryParam("from") String from,
                          @QueryParam("to") String to) {
        try {
            if (date != null && !date.isBlank()) {
                Holiday h = service.getByDate(date);
                if (h == null) return Response.status(Response.Status.NOT_FOUND).build();
                return Response.ok(h).build();
            }
            if (from != null && to != null) {
                List<Holiday> list = service.listInRange(from, to);
                return Response.ok(list).build();
            }
            return Response.ok(service.listAll()).build();
        } catch (Exception e) { return server(e); }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try { service.delete(id); return Response.noContent().build(); }
        catch (Exception e) { return bad(e); }
    }

    // same helpers you used in other resources
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
