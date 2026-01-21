package pharmacie.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/pharmacy_db";
    private static final String USER = "pharmacy_user";
    private static final String PASSWORD = "pharmacy_pass";

    private static Connection connection;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load driver explicitly just in case
                Class.forName("org.mariadb.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MariaDB JDBC Driver not found", e);
            }
        }
        return connection;
    }
}
