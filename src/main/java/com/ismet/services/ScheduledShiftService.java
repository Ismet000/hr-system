package com.ismet.services;

import com.ismet.domain.ScheduledShift;

import java.util.List;

public interface ScheduledShiftService {
    String create(ScheduledShift s) throws Exception;
    void update(ScheduledShift s) throws Exception;
    ScheduledShift getById(String id) throws Exception;
    List<ScheduledShift> listByEmployeeRange(String empId, String from, String to) throws Exception;
    List<ScheduledShift> listByDate(String date) throws Exception;
    void delete(String id) throws Exception;
}
