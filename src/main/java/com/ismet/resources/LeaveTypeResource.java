package com.ismet.resources;


import com.ismet.common.AbstractResource;
import com.ismet.domain.LeaveType;
import com.ismet.services.LeaveTypeService;
import com.ismet.services.LeaveTypeServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/leave-types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class LeaveTypeResource extends AbstractResource {

    LeaveTypeService service = new LeaveTypeServiceImpl();

    @POST
    @Path("/insert")
    public Response insert(String payload) {
        try {
            LeaveType lt = gson().fromJson(payload, LeaveType.class);
            service.insert(lt);
            return Response.ok(lt).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(err(e)).build();
        }
    }

    @PUT
    @Path("/{id}/update")
    public Response update(@PathParam("id") String id, String payload) {
        try {
            LeaveType lt = gson().fromJson(payload, LeaveType.class);
            lt.setId(id);
            service.update(lt);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(err(e)).build();
        }
    }

    @GET @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            LeaveType lt = service.getById(id);
            if (lt == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(lt).build();
        } catch (Exception e) {
            return Response.serverError().entity(err(e)).build();
        }
    }

    @GET @Path("/all")
    public Response getAll() {
        try {
            List<LeaveType> list = service.getAll();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError().entity(err(e)).build();
        }
    }

    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            service.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(err(e)).build();
        }
    }

    private String err(Exception e) {
        String m = (e.getMessage() == null ? "Request failed" : e.getMessage());
        return "{\"error\":\"" + m.replace("\"", "\\\"") + "\"}";
    }

}
