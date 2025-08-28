package com.ismet.resources;

import com.ismet.common.AbstractResource;
import com.ismet.domain.Department;
import com.ismet.services.DepartmentService;
import com.ismet.services.DepartmentServiceImpl;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/departments")
public class DepartmentResource extends AbstractResource {

    DepartmentService departmentService = new DepartmentServiceImpl();

    @POST
    @Path("/insert")
    public Response insertDepartment(String payload) throws Exception {
        Department department = gson().fromJson(payload, Department.class);
        departmentService.insertDepartment(department);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/update")
    public Response updateDepartment(@PathParam("id") String id, String payload) throws Exception {
        Department department = gson().fromJson(payload, Department.class);
        department.setId(id); // assign path variable as department_id
        departmentService.updateDepartment(department);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getDepartmentById(@PathParam("id") String id) throws Exception {
        Department department = departmentService.getDepartmentById(id);
        return Response.ok(department).build();
    }

    @GET
    @Path("/all")
    public Response getAllDepartments() throws Exception {
        List<Department> departments = departmentService.getAllDepartments();
        return Response.ok(departments).build();
    }

    @DELETE
    @Path("/{id}/delete")
    public Response deleteDepartment(@PathParam("id") String id) throws Exception {
        departmentService.deleteDepartment(id);
        return Response.ok().build();
    }
}
