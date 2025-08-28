package com.ismet.services;

import com.ismet.domain.Benefit;

import java.util.List;

public interface BenefitService {

    void create(Benefit b) throws Exception;
    void update(Benefit b) throws Exception;
    Benefit getById(String id) throws Exception;
    List<Benefit> listByEmployee(String employeeId) throws Exception;
    List<Benefit> listActiveInPeriod(String employeeId, String from, String to) throws Exception;
    void delete(String id) throws Exception;
    void setStatus(String id, String status) throws Exception; // 'active'/'inactive'
}
