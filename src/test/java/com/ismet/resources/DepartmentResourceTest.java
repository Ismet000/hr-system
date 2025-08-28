package com.ismet.resources;

import jakarta.ws.rs.core.Response;
import org.junit.Test;

public class DepartmentResourceTest {

    DepartmentResource resource = new DepartmentResource();

    @Test
    public void insertDepartment() throws Exception {
        String unique = "Finance-" + System.currentTimeMillis();
        String uniqueId = "dept-" + System.currentTimeMillis();

        String payload = "{\n" +
                "  \"id\": \"" + uniqueId + "\",\n" +
                "  \"name\": \"" + unique + "\",\n" +
                "  \"manager_id\": null\n" +
                "}\n";

        try (Response response = resource.insertDepartment(payload)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }



    @Test
    public void updateDepartment() throws Exception {
        String payload = "{\n" +
                "  \"name\": \"Finance Updated\",\n" +
                "  \"manager_id\": \"emp-test-1\"\n" +
                "}\n";

        try (Response response = resource.updateDepartment("dept-test-1", payload)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }

    @Test
    public void getDepartmentById() throws Exception {
        try (Response response = resource.getDepartmentById("dept-test-1")) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }

    @Test
    public void getAllDepartments() throws Exception {
        try (Response response = resource.getAllDepartments()) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }

    @Test
    public void deleteDepartment() throws Exception {
        try (Response response = resource.deleteDepartment("dept-test-1")) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }
}
