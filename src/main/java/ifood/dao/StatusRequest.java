package ifood.dao;

import ifood.model.StatusRequest;
import ifood.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusRequestDAO {

    public void addStatusRequest(StatusRequest statusRequest) throws SQLException {
        String sql = "INSERT INTO StatusRequest (description) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, statusRequest.getDescription());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    statusRequest.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public StatusRequest getById(int id) throws SQLException {
        String sql = "SELECT * FROM StatusRequest WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                StatusRequest statusRequest = new StatusRequest();
                statusRequest.setId(rs.getInt("id"));
                statusRequest.setDescription(rs.getString("description"));
                return statusRequest;
            }
        }
        return null;
    }

    public List<StatusRequest> getAllStatusRequests() throws SQLException {
        List<StatusRequest> statusRequests = new ArrayList<>();
        String sql = "SELECT * FROM StatusRequest";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                StatusRequest statusRequest = new StatusRequest();
                statusRequest.setId(rs.getInt("id"));
                statusRequest.setDescription(rs.getString("description"));
                statusRequests.add(statusRequest);
            }
        }
        return statusRequests;
    }

    public void updateStatusRequest(StatusRequest statusRequest) throws SQLException {
        String sql = "UPDATE StatusRequest SET description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusRequest.getDescription());
            stmt.setInt(2, statusRequest.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteStatusRequest(int id) throws SQLException {
        String sql = "DELETE FROM StatusRequest WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}