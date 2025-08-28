package com.ismet.services;

import com.ismet.domain.LeaveRequest;

import java.util.List;

public interface LeaveRequestService {
    void submit(LeaveRequest lr) throws Exception;
    void approve(String requestId, String approverId) throws Exception;
    void deny(String requestId, String approverId) throws Exception;
    void cancel(String requestId) throws Exception;

    LeaveRequest getById(String requestId) throws Exception;
    List<LeaveRequest> getAll() throws Exception;
    List<LeaveRequest> getByEmployee(String employeeId) throws Exception;
    List<LeaveRequest> getByStatus(String status) throws Exception;
}
