package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.services.AlertService;
import com.ismet.services.AlertServiceImpl;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/alerts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class AlertResource  extends AbstractResource {

    private final AlertService service = new AlertServiceImpl();


    @GET
    @Path("/run-daily")
    public Response runDaily() {
        try { return Response.ok(service.runDailySweep()).build(); }
        catch (Exception e) { return bad(e); }
    }

    private Response bad(Exception e) {
        String m = e.getMessage()==null ? "Request failed" : e.getMessage();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + m.replace("\"","\\\"") + "\"}").build();
    }
}
