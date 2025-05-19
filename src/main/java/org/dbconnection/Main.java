package org.dbconnection;

import java.sql.*;

public class Main{
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

    public static void main(String[] args){
        Connection connection = connect();
        if (connection != null) {
//            Delete(connection)
//            InsertInto(connection);
//            Update(connection);
            //ShowDatabase(connection);


            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }


    }
}