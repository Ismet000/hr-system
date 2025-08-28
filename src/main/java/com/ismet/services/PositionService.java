package com.ismet.services;

import com.ismet.domain.Position;

import java.util.List;

public interface PositionService {
    void insertPosition(Position position) throws Exception;
    void updatePosition(Position position) throws Exception;
    Position getPositionById(String positionId) throws Exception;
    List<Position> getAllPositions() throws Exception;
    void deletePosition(String positionId) throws Exception;

}
