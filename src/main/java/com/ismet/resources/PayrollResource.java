package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.Payroll;
import com.ismet.services.PayrollService;
import com.ismet.services.PayrollServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/payrolls")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PayrollResource extends AbstractResource {

    private final PayrollService service = new PayrollServiceImpl();

    @POST
    @Path("/generate")
    public Response generate(@QueryParam("employee_id") String employeeId,
                             @QueryParam("start") String start,
                             @QueryParam("end") String end) {
        try {
            Payroll p = service.generate(employeeId, start, end, null); // currency ignored
            return Response.ok(p).build();
        } catch (Exception e) {
            return bad(e);
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        try {
            Payroll p = service.getById(id);
            if (p == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(p).build();
        } catch (Exception e) {
            return server(e);
        }
    }

    @GET
    public Response list(@QueryParam("employee_id") String employeeId,
                         @QueryParam("from") String from,
                         @QueryParam("to") String to) {
        try {
            if (from == null || from.isBlank() || to == null || to.isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"from and to are required (YYYY-MM-DD)\"}")
                        .build();
            }
            if (employeeId != null && !employeeId.isBlank()) {
                List<Payroll> list = service.listByEmployeeInPeriod(employeeId, from, to);
                return Response.ok(list).build();
            } else {
                return Response.ok(service.listInPeriod(from, to)).build();
            }
        } catch (Exception e) {
            return server(e);
        }
    }

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

    // Keep these helpers local or move them to AbstractResource as protected methods.
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


    @GET
    @Path("/{id}/payslip.csv")
    @Produces("text/csv")
    public Response exportPayslipCsv(@PathParam("id") String id) {
        try {
            Payroll p = service.getById(id);
            if (p == null) return Response.status(Response.Status.NOT_FOUND).build();


            StringBuilder sb = new StringBuilder();
            sb.append("Payslip ID,Employee ID,Period Start,Period End,Gross,Taxes,Net,Generated At\n");
            sb.append(escape(p.getId())).append(',')
                    .append(escape(p.getEmployeeId())).append(',')
                    .append(escape(p.getPeriodStart())).append(',')
                    .append(escape(p.getPeriodEnd())).append(',')
                    .append(p.getGrossPay()).append(',')
                    .append(p.getTaxes()).append(',')
                    .append(p.getNetPay()).append(',')
                    .append(escape(p.getGeneratedDate() == null ? "" : p.getGeneratedDate()))
                    .append('\n');

            return Response.ok(sb.toString(), "text/csv")
                    .header("Content-Disposition", "attachment; filename=\"payslip_" + p.getId() + ".csv\"")
                    .build();
        } catch (Exception e) {
            return server(e);
        }
    }

    @GET
    @Path("/{id}/payment-file.csv")
    @Produces("text/csv")
    public Response exportPaymentFile(@PathParam("id") String id) {
        try {
            Payroll p = service.getById(id);
            if (p == null) return Response.status(Response.Status.NOT_FOUND).build();


            String reference = "PAY-" + p.getId().substring(0, Math.min(8, p.getId().length()));

            StringBuilder sb = new StringBuilder();
            sb.append("employee_id,net_pay,pay_period_end,reference\n");
            sb.append(escape(p.getEmployeeId())).append(',')
                    .append(p.getNetPay()).append(',')
                    .append(escape(p.getPeriodEnd())).append(',')
                    .append(escape(reference))
                    .append('\n');

            return Response.ok(sb.toString(), "text/csv")
                    .header("Content-Disposition", "attachment; filename=\"payment_" + p.getId() + ".csv\"")
                    .build();
        } catch (Exception e) {
            return server(e);
        }
    }


    private static String escape(String v) {
        if (v == null) return "";
        boolean needsQuotes = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        String out = v.replace("\"", "\"\"");
        return needsQuotes ? "\"" + out + "\"" : out;
    }
}
