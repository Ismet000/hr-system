package com.ismet.resources;

import jakarta.ws.rs.core.Response;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionResourceTest {
    PositionResource resource = new PositionResource();

    @Test
    public void insertPosition() throws Exception {
        String payload = "{\n" +
                "  \"id\": \"pos-test-1\",\n" +
                "  \"title\": \"Senior Analyst\",\n" +
                "  \"salary_grade\": \"G8\"\n" +
                "}\n";

        try (Response response = resource.insertPosition(payload)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
            // Optional assertion
            assertTrue(response.getStatus() == 200 || response.getStatus() == 201);
        }
    }

    @Test
    public void updatePosition() throws Exception {
        // Replace with an existing position_id in your DB before running
        String existingPositionId = "pos-analyst";

        String payload = "{\n" +
                "  \"title\": \"Analyst (Updated)\",\n" +
                "  \"salary_grade\": \"G7\"\n" +
                "}\n";

        try (Response response = resource.updatePosition(existingPositionId, payload)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    public void getAllPositions() throws Exception {
        try (Response response = resource.getAllPositions()) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
            assertEquals(200, response.getStatus());
        }
    }

    @Test
    public void getPositionById() throws Exception {
        // Replace with an existing position_id in your DB before running
        String existingPositionId = "pos-analyst";

        try (Response response = resource.getPositionById(existingPositionId)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
            assertTrue(response.getStatus() == 200 || response.getStatus() == 404);
        }
    }

    @Test
    public void deletePosition() throws Exception {
        // Create then delete, or use a known test id
        String toDeleteId = "pos-test-1"; // must exist before this test

        try (Response response = resource.deletePosition(toDeleteId)) {
            System.out.println("HTTP Status: " + response.getStatus());
            System.out.println("Entity: " + response.getEntity());
            // 204 No Content is common; your resource may return 200
            assertTrue(response.getStatus() == 200 || response.getStatus() == 204);
        }
    }
}
