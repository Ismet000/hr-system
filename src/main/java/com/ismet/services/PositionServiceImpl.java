package com.ismet.services;

import com.ismet.common.AbstractService;
import com.ismet.common.sql.PositionSQL;
import com.ismet.domain.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PositionServiceImpl extends AbstractService implements PositionService {


    @Override
    public void insertPosition(Position position) throws Exception {
        String validate = position.validate();
        if (validate != null) {
            throw new Exception(validate);
        }
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PositionSQL.INSERT_POSITION);
            position.populateInsertPs(ps);   // sets (position_id, title, salary_grade)
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

    @Override
    public void updatePosition(Position position) throws Exception {
        String validate = position.validate();
        if (validate != null) {
            throw new Exception(validate);
        }
        if (position.getId() == null || position.getId().isEmpty()) {
            throw new Exception("position_id (id) must be provided for update");
        }
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PositionSQL.UPDATE_POSITION);
            position.populateUpdatePs(ps);   // sets (title, salary_grade, position_id)
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }

    @Override
    public Position getPositionById(String positionId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(PositionSQL.GET_POSITION_BY_ID);
            ps.setString(1, positionId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Position(rs, ""); // reads position_id, title, salary_grade
            }
        } finally {
            close(rs, ps, con);
        }
        return null;
    }

    @Override
    public List<Position> getAllPositions() throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection con = null;
        List<Position> positions = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement(PositionSQL.GET_ALL_POSITIONS);
            rs = ps.executeQuery();
            while (rs.next()) {
                positions.add(new Position(rs, ""));
            }
        } finally {
            close(rs, ps, con);
        }
        return positions;
    }

    @Override
    public void deletePosition(String positionId) throws Exception {
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("DELETE FROM positions WHERE position_id = ?");
            ps.setString(1, positionId);
            ps.executeUpdate();
        } finally {
            close(ps, con);
        }
    }
}
