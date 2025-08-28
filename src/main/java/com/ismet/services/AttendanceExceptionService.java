package com.ismet.services;

import com.ismet.domain.AttendanceException;

import java.util.List;

public interface AttendanceExceptionService {
    void submit(AttendanceException ex) throws Exception;
    void approve(String id, String approverId) throws Exception;
    void deny(String id, String approverId) throws Exception;
    AttendanceException getById(String id) throws Exception;
    List<AttendanceException> listPending() throws Exception;
}
