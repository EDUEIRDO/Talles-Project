package ifood.dao;

import ifood.model.Product;
import ifood.model.Category;
import ifood.model.Restaurant;
import ifood.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product (name, description, price, categoryId, restaurantId) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getCategory().getId());
            stmt.setInt(5, product.getRestaurant().getId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Product getById(int id) throws SQLException {
        String sql = "SELECT * FROM Product WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));

                // Load related Category
                CategoryDAO categoryDAO = new CategoryDAO();
                product.setCategory(categoryDAO.getById(rs.getInt("categoryId")));

                // Load related Restaurant
                RestaurantDAO restaurantDAO = new RestaurantDAO();
                product.setRestaurant(restaurantDAO.getById(rs.getInt("restaurantId")));

                return product;
            }
        }
        return null;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));

                CategoryDAO categoryDAO = new CategoryDAO();
                product.setCategory(categoryDAO.getById(rs.getInt("categoryId")));

                RestaurantDAO restaurantDAO = new RestaurantDAO();
                product.setRestaurant(restaurantDAO.getById(rs.getInt("restaurantId")));

                products.add(product);
            }
        }
        return products;
    }

    public List<Product> getProductsByRestaurant(int restaurantId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE restaurantId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));

                CategoryDAO categoryDAO = new CategoryDAO();
                product.setCategory(categoryDAO.getById(rs.getInt("categoryId")));

                RestaurantDAO restaurantDAO = new RestaurantDAO();
                product.setRestaurant(restaurantDAO.getById(restaurantId));

                products.add(product);
            }
        }
        return products;
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Product SET name = ?, description = ?, price = ?, categoryId = ?, restaurantId = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getCategory().getId());
            stmt.setInt(5, product.getRestaurant().getId());
            stmt.setInt(6, product.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM Product WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
