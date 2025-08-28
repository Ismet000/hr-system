package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.HolidaySQL;
import com.ismet.domain.Holiday;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HolidayServiceImpl extends AbstractService implements HolidayService{
    @Override
    public void insert(Holiday h) throws Exception {
        String v = h.validate();
        if (v != null) throw new Exception(v);

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.INSERT);
            h.populateInsertPs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void update(Holiday h) throws Exception {
        String v = h.validate();
        if (v != null) throw new Exception(v);
        if (h.getId() == null || h.getId().isBlank())
            throw new Exception("holiday_id (id) is required");

        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.UPDATE);
            h.populateUpdatePs(ps);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public void delete(String holidayId) throws Exception {
        PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.DELETE);
            ps.setString(1, holidayId);
            ps.executeUpdate();
        } finally { close(ps, con); }
    }

    @Override
    public Holiday getById(String holidayId) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.GET_BY_ID);
            ps.setString(1, holidayId);
            rs = ps.executeQuery();
            if (rs.next()) return new Holiday(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public Holiday getByDate(String date) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.GET_BY_DATE);
            ps.setString(1, date);
            rs = ps.executeQuery();
            if (rs.next()) return new Holiday(rs, "");
        } finally { close(rs, ps, con); }
        return null;
    }

    @Override
    public List<Holiday> listInRange(String from, String to) throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Holiday> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.LIST_IN_RANGE);
            ps.setString(1, from);
            ps.setString(2, to);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new Holiday(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }

    @Override
    public List<Holiday> listAll() throws Exception {
        ResultSet rs = null; PreparedStatement ps = null; Connection con = null;
        List<Holiday> list = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(HolidaySQL.LIST_ALL);
            rs = ps.executeQuery();
            while (rs.next()) list.add(new Holiday(rs, ""));
        } finally { close(rs, ps, con); }
        return list;
    }
}
