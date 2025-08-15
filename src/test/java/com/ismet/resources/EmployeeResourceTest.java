package com.ismet.resources;

import jakarta.ws.rs.core.Response;
import junit.runner.BaseTestRunner;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeResourceTest   {
 EmployeeResource resource = new EmployeeResource();

    @Test
    public void insertEmployee() throws  Exception {
        String payload = "{}";
        try(Response response = resource.insertEmployee(payload)){
            System.out.println(response.getEntity());
        }
    }


}