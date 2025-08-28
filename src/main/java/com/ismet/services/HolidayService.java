package com.ismet.services;

import com.ismet.domain.Holiday;

import java.util.List;

public interface HolidayService {
    void insert(Holiday h) throws Exception;
    void update(Holiday h) throws Exception;
    void delete(String holidayId) throws Exception;

    Holiday getById(String holidayId) throws Exception;
    Holiday getByDate(String date) throws Exception; // yyyy-MM-dd
    List<Holiday> listInRange(String from, String to) throws Exception; // yyyy-MM-dd
    List<Holiday> listAll() throws Exception;
}
