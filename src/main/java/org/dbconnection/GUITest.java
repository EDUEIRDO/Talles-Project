package org.dbconnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GUITest extends JFrame {

    private final JTextArea textArea;
    private final Connection connection;

    public GUITest() {
        setTitle("Database Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton insertButton = new JButton("Insert");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton showButton = new JButton("Show Database");
        buttonPanel.add(insertButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        setContentPane(mainPanel);

        // Connect to the database
        connection = connect();

        // Add action listeners to the buttons
        insertButton.addActionListener(e -> InsertInto(connection));
//        updateButton.addActionListener(e -> Update(connection));
//        deleteButton.addActionListener(e -> Delete(connection));
        showButton.addActionListener(e -> ShowDatabase(connection));
    }

    private void InsertInto(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("INSERT INTO users (name) VALUES ('Avast');");
            textArea.append("Inserted new user\n");
        } catch (SQLException e) {
            textArea.append("CRUD operation failed: " + e.getMessage() + "\n");
        }
    }
    private void ShowDatabase(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("SELECT * FROM users;");
            textArea.append("Database: \n");
        } catch (SQLException e) {
            textArea.append("CRUD operation failed: " + e.getMessage() + "\n");
        }
    }

    // Update, Delete, and ShowDatabase methods are similar to the InsertInto method

    private Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            textArea.append("Connected successfully.\n");
        } catch (SQLException e) {
            textArea.append("Error connecting to database\n");
            textArea.append(e.getMessage() + "\n");
        }
        return connection;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.setVisible(true);
        });
    }
}
