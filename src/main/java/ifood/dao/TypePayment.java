package ifood.dao;

import ifood.model.TypePayment;
import ifood.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypePaymentDAO {

    public void addTypePayment(TypePayment typePayment) throws SQLException {
        String sql = "INSERT INTO TypePayment (description) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, typePayment.getDescription());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    typePayment.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public TypePayment getById(int id) throws SQLException {
        String sql = "SELECT * FROM TypePayment WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TypePayment typePayment = new TypePayment();
                typePayment.setId(rs.getInt("id"));
                typePayment.setDescription(rs.getString("description"));
                return typePayment;
            }
        }
        return null;
    }

    public List<TypePayment> getAllTypePayments() throws SQLException {
        List<TypePayment> typePayments = new ArrayList<>();
        String sql = "SELECT * FROM TypePayment";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TypePayment typePayment = new TypePayment();
                typePayment.setId(rs.getInt("id"));
                typePayment.setDescription(rs.getString("description"));
                typePayments.add(typePayment);
            }
        }
        return typePayments;
    }

    public void updateTypePayment(TypePayment typePayment) throws SQLException {
        String sql = "UPDATE TypePayment SET description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, typePayment.getDescription());
            stmt.setInt(2, typePayment.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteTypePayment(int id) throws SQLException {
        String sql = "DELETE FROM TypePayment WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}