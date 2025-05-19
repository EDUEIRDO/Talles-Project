package org.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static void InsertInto(Connection connection ) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("INSERT INTO users (name) VALUES ('Avast');");
        } catch (SQLException e) {
            System.err.println("CRUD operation failed: " + e.getMessage());
        }
    }
    private static void Update(Connection connection ) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("UPDATE users SET name = 'John Dee' WHERE id = 1;");
        } catch (SQLException e) {
            System.err.println("CRUD operation failed: " + e.getMessage());
        }
    }

    private static void Delete(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("DELETE from users WHERE id = 1");
        } catch (SQLException e) {
            System.err.println("CRUD operation failed: " + e.getMessage());
        }
    }

    private static void ShowDatabase(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");
            while (resultSet.next()) {
                System.out.println("ID do individuo: " + resultSet.getInt("id"));
                System.out.println("Nome do individuo: " + resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("CRUD operation failed: " + e.getMessage());
        }
    }

    private static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            System.out.println("Connected successfully.");
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            System.err.println(e.getMessage());
        }
        return connection;
    }

    private static void Options(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
//
            statement.executeUpdate("DROP TABLE IF EXISTS users");
            statement.executeUpdate("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
//            statement.executeUpdate("INSERT INTO users(name) VALUES ('Marcos')");
//            statement.executeUpdate("INSERT INTO users(name) VALUES ('Igor')");
//            statement.executeUpdate("INSERT INTO users(name) VALUES ('Jorge')");
//            statement.executeUpdate("INSERT INTO users(name) VALUES ('Carlos')");
//
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");
//            while(resultSet.next()) {
//                System.out.println("Id do invididuo: " + resultSet.getInt("id"));
//                System.out.println("Nome do individuo: " + resultSet.getString("name"));
            /*Tabelas do ifood
             * Cliente
             * Restaurantes
             * Pedidos
             * Pagamento
             * Entregas
             * Credencial
             * */

            InsertInto(connection);
//            Update(connection);
//            Delete(connection);
            ShowDatabase(connection);

        } catch (SQLException e) {
            System.err.println("CRUD operation failed: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Connection connection = connect();
        if (connection != null) {
            Options(connection);
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

}