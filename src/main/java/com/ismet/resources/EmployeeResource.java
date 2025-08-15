package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.Employee;
import com.ismet.services.EmployeeService;
import com.ismet.services.EmployeeServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/employees")
public class EmployeeResource extends AbstractResource {
    EmployeeService employeeService = new EmployeeServiceImpl();

    @POST
    @Path("/insert")
    public Response insertEmployee(String payload) throws Exception {
        Employee employee = gson().fromJson(payload, Employee.class);
        employee.setId(UUID.randomUUID().toString());
        employeeService.saveEmployee(employee);
        return Response.ok().build();
    }


    @PUT
    @Path("/{id}/update")
    public Response updateEmployee(@PathParam("id") String id, String payload) throws Exception {
        Employee employee = gson().fromJson(payload, Employee.class);
        employee.setId(id);
        employeeService.updateEmployee(employee);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getEmployeeById(@PathParam("id") String id) throws Exception {
        Employee employee = employeeService.getEmployeeById(id);
        return Response.ok(employee).build();
    }

    @GET
    @Path("/all")
    public Response getAllEmployees() throws Exception {
        List<Employee> employees = employeeService.getAllEmployees();
        return Response.ok(employees).build();
    }
}
