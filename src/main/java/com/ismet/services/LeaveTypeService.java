package com.ismet.services;

import com.ismet.domain.LeaveType;

import java.util.List;

public interface LeaveTypeService {
    void insert(LeaveType lt) throws Exception;
    void update(LeaveType lt) throws Exception;
    LeaveType getById(String id) throws Exception;
    List<LeaveType> getAll() throws Exception;
    void delete(String id) throws Exception;
}
