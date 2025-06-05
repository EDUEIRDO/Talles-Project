package ifood.dao;

import ifood.model.Restaurant;
import ifood.model.Address;
import ifood.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    public void addRestaurant(Restaurant restaurant) throws SQLException {
        String sql = "INSERT INTO Restaurant (name, description, addressId) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getDescription());
            if (restaurant.getAddress() != null) {
                stmt.setInt(3, restaurant.getAddress().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    restaurant.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Restaurant getById(int id) throws SQLException {
        String sql = "SELECT * FROM Restaurant WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setDescription(rs.getString("description"));

                int addressId = rs.getInt("addressId");
                if (!rs.wasNull()) {
                    AddressDAO addressDAO = new AddressDAO();
                    restaurant.setAddress(addressDAO.getById(addressId));
                }

                return restaurant;
            }
        }
        return null;
    }

    public List<Restaurant> getAllRestaurants() throws SQLException {
        List<Restaurant> restaurants = new ArrayList<>();
        String sql = "SELECT * FROM Restaurant";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getInt("id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setDescription(rs.getString("description"));

                int addressId = rs.getInt("addressId");
                if (!rs.wasNull()) {
                    AddressDAO addressDAO = new AddressDAO();
                    restaurant.setAddress(addressDAO.getById(addressId));
                }

                restaurants.add(restaurant);
            }
        }
        return restaurants;
    }

    public void updateRestaurant(Restaurant restaurant) throws SQLException {
        String sql = "UPDATE Restaurant SET name = ?, description = ?, addressId = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getDescription());
            if (restaurant.getAddress() != null) {
                stmt.setInt(3, restaurant.getAddress().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, restaurant.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteRestaurant(int id) throws SQLException {
        String sql = "DELETE FROM Restaurant WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
