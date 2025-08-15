package com.ismet.common;

import java.sql.*;

public abstract class AbstractService {

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // <-- explicitly load driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hrdatabase?useSSL=false&serverTimezone=UTC",
                "root",
                "Ismet@11235@cicmo"
        );
    }

    public void close(ResultSet rs, PreparedStatement ps, Connection connection) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        close(ps, connection);
    }

    public void close(PreparedStatement ps, Connection connection) throws SQLException {
        if (ps != null) {
            ps.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
