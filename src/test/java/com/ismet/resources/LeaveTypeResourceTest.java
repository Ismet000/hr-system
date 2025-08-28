//package com.ismet.resources;
//
//import jakarta.ws.rs.core.Response;
//import org.junit.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class LeaveTypeResourceTest {
//
//    private final LeaveTypeResource resource = new LeaveTypeResource();
//
//    @Test
//    public void crudLifecycle() throws Exception {
//        String id = "lt-" + System.currentTimeMillis();
//        String name = "Vacation-" + System.currentTimeMillis();
//
//        // INSERT
//        String insertPayload = "{\n" +
//                "  \"id\": \"" + id + "\",\n" +
//                "  \"name\": \"" + name + "\",\n" +
//                "  \"max_days\": 25\n" +
//                "}\n";
//        try (Response r = resource.insert(insertPayload)) {
//            assertEquals(200, r.getStatus()); // adjust if you return CREATED (201)
//        }
//
//        // GET BY ID
//        try (Response r = resource.getLeaveTypeById(id)) {
//            assertEquals(200, r.getStatus());
//        }
//
//        // UPDATE
//        String updatePayload = "{\n" +
//                "  \"name\": \"" + name + "-Updated\",\n" +
//                "  \"max_days\": 40\n" +
//                "}\n";
//        try (Response r = resource.updateLeaveType(id, updatePayload)) {
//            assertEquals(200, r.getStatus());
//        }
//
//        // GET ALL
//        try (Response r = resource.getAllLeaveTypes()) {
//            assertEquals(200, r.getStatus());
//        }
//
//        // DELETE
//        try (Response r = resource.deleteLeaveType(id)) {
//            assertTrue(r.getStatus() == 200 || r.getStatus() == 204);
//        }
//
//        // GET AFTER DELETE (should be 404 or 200 with null depending on impl)
//        try (Response r = resource.getLeaveTypeById(id)) {
//            assertTrue(r.getStatus() == 200 || r.getStatus() == 404);
//        }
//    }
//}
