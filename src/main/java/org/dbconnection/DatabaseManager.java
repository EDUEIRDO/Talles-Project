package org.dbconnection;

import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager{

    private static void InsertInto(Connection connection ) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("INSERT INTO Client (name) VALUES ('Avast');");
            statement.executeUpdate("INSERT INTO Client (name) VALUES ('Carlos');");
            statement.executeUpdate("INSERT INTO Client (name) VALUES ('Free');");
            statement.executeUpdate("INSERT INTO Client (name) VALUES ('Leticia');");
            statement.executeUpdate("INSERT INTO Client (name) VALUES ('REPO');");
            statement.executeUpdate("INSERT INTO Restaurant (name, description, openingHours) VALUES ('Baronni', 'Um otimo restaurante para a familia', '18:00 ate 02:00');");
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

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Restaurant;");
            StringBuilder output = new StringBuilder();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String openingHours = resultSet.getString("openingHours");
                output.append("ID do Restaurante: ").append(id).append("\n");
                output.append("Nome do Restaurante: ").append(name).append("\n");
                output.append("Descricao do Restaurante: ").append(description).append("\n");
                output.append("Horario de funcionamento: ").append(openingHours).append("\n\n");

//                System.out.println("ID do individuo: " + resultSet.getInt("id"));
//                System.out.println("Nome do individuo: " + resultSet.getString("name"));
            }
            if (!output.isEmpty()) {
                JOptionPane.showMessageDialog(null, output.toString(), "Dados do Banco", JOptionPane.INFORMATION_MESSAGE);

            }
            else {
                JOptionPane.showMessageDialog(null, "Nenhum dado encontrado na tabela.", "Dados do Banco", JOptionPane.WARNING_MESSAGE);

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("CRUD operation failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro ao acessar o banco de dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

            // Table Client
            statement.executeUpdate("DROP TABLE IF EXISTS Client");
            statement.executeUpdate("CREATE TABLE Client (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");

            // Table Restaurant
            statement.executeUpdate("DROP TABLE IF EXISTS Restaurant;");
            statement.executeUpdate("CREATE TABLE Restaurant (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, openingHours TEXT, restaurantAddress TEXT);");

            // Table Categories
            statement.executeUpdate("DROP TABLE IF EXISTS Categories;");
            statement.executeUpdate("CREATE TABLE Categories (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, type TEXT);");

            // Table Address
            // Restaurants are included in this table
            statement.executeUpdate("DROP TABLE IF EXISTS Address;");
            statement.executeUpdate("CREATE TABLE Address (id INTEGER PRIMARY KEY AUTOINCREMENT, street TEXT, district TEXT, city TEXT, state TEXT, houseNumber INTEGER);");

            // Table Product
            // Add categories_id and restaurant_id
            statement.executeUpdate("DROP TABLE IF EXISTS Product;");
            statement.executeUpdate("CREATE TABLE Product (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, description TEXT );");

            // Table Request
            // Add address_id, restaurant_id, statusRequest_id, cupom_id, statusPayment and typeOfPayment_id
            statement.executeUpdate("DROP TABLE IF EXISTS Request;");
            statement.executeUpdate("CREATE TABLE Request (id INTEGER PRIMARY KEY AUTOINCREMENT, totalValue REAL, deliveryValue REAL);");

            // Table statusRequest
            statement.executeUpdate("DROP TABLE IF EXISTS statusRequest;");
            statement.executeUpdate("CREATE TABLE statusRequest (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");

            // Table productRequest
            // Add product_id and request_id fk
            statement.executeUpdate("DROP TABLE IF EXISTS productRequest;");
            statement.executeUpdate("CREATE TABLE productRequest (id INTEGER PRIMARY KEY AUTOINCREMENT);");

            // Table typePayment
            statement.executeUpdate("DROP TABLE IF EXISTS typePayment;");
            statement.executeUpdate("CREATE TABLE typePayment (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");

            // Table statusPayment
            statement.executeUpdate("DROP TABLE IF EXISTS statusPayment;");
            statement.executeUpdate("CREATE TABLE statusPayment (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");

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
            // Input Dialog
            //JOptionPane.showInputDialog(null, "Type here");
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

}