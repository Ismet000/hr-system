package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.Position;
import com.ismet.services.PositionService;
import com.ismet.services.PositionServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/positions")
public class PositionResource extends AbstractResource {

    PositionService positionService = new PositionServiceImpl();

    @POST
    @Path("/insert")
    public Response insertPosition(String payload) throws Exception {
        Position position = gson().fromJson(payload, Position.class);
        positionService.insertPosition(position);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/update")
    public Response updatePosition(@PathParam("id") String id, String payload) throws Exception {
        Position position = gson().fromJson(payload, Position.class);
        position.setId(id);
        positionService.updatePosition(position);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getPositionById(@PathParam("id") String id) throws Exception {
        Position position = positionService.getPositionById(id);
        if (position == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(position).build();
    }

    @GET
    @Path("/all")
    public Response getAllPositions() throws Exception {
        List<Position> positions = positionService.getAllPositions();
        return Response.ok(positions).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePosition(@PathParam("id") String id) throws Exception {
        positionService.deletePosition(id);
        return Response.noContent().build();
    }
}
