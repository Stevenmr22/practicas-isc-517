package util;

import java.sql.DriverManager;
import java.sql.Connection;
import  java.sql.SQLException;

public class ConexionJDBC {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:tcp://localhost/~/crud-simple");
    }
}
