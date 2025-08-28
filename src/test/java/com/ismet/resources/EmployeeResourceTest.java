package com.ismet.resources;

import jakarta.ws.rs.core.Response;
import junit.runner.BaseTestRunner;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeResourceTest   {
 EmployeeResource resource = new EmployeeResource();

    @Test
    public void insertEmployee() throws Exception {
        String payload = "{\n" +
                "  \"id\": \"emp-test-1\",\n" +
                "  \"first_name\": \"Ismet\",\n" +
                "  \"last_name\": \"Memedi\",\n" +
                "  \"email\": \"ism@gmail.com\",\n" +
                "  \"contact\": \"555-0000\",\n" +
                "  \"position_id\": \"pos-analyst\",\n" +        // must exist in positions table
                "  \"department_id\": \"dept-fin\",\n" +        // must exist in departments table
                "  \"hire_date\": \"2023-03-01\",\n" +          // required NOT NULL
                "  \"employment_type\": \"full_time\",\n" +
                "  \"status\": \"active\"\n" +
                "}\n";

        try (Response response = resource.insertEmployee(payload)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }



    @Test
    public void updateEmployee() throws Exception {
        String payload = "{\n" +
                "  \"first_name\": \"John upd\",\n" +
                "  \"last_name\": \"Doe upd\",\n" +
                "  \"email\": \"john@example.com\",\n" +
                "  \"contact\": \"555-1111\",\n" +
                "  \"position_id\": \"pos-analyst\",\n" +
                "  \"department_id\": \"dept-fin\",\n" +
                "  \"hire_date\": \"2024-05-01\",\n" +
                "  \"employment_type\": \"full_time\",\n" +
                "  \"status\": \"active\"\n" +
                "}\n";

        try (Response response = resource.updateEmployee(
                "c188bea5-7578-4eda-81d5-df1cfadb30d2", payload)) {

            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
        }
    }


    @Test
    public void getAllEmployees() throws  Exception {
        String payload = "{}";
        try(Response response = resource.getAllEmployees()){
            System.out.println(response.getEntity());
        }
    }


}