package ifood.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ifood"; // Confirme o nome do seu BD
    private static final String USER = "root"; // SEU USUÁRIO MYSQL
    private static final String PASSWORD = "1234"; // SUA SENHA MYSQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Garante que o driver está carregado
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}