package bookstore.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtils {
    private static final String USER = "postgre"; //PgAdmin Username
    private static final String DATABASE = "book"; //Database name
    private static Connection connection;
    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "1234"); //password
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
