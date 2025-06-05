package ifood.dao;

import ifood.model.Request;
import ifood.model.Client;
import ifood.model.Restaurant;
import ifood.model.StatusRequest;
import ifood.model.StatusPayment;
import ifood.model.TypePayment;
import ifood.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public void addRequest(Request request) throws SQLException {
        String sql = "INSERT INTO Request (moment, totalValue, clientId, restaurantId, statusRequestId, statusPaymentId, typePaymentId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(request.getMoment()));
            stmt.setBigDecimal(2, request.getTotalValue());
            stmt.setInt(3, request.getClient().getId());
            stmt.setInt(4, request.getRestaurant().getId());
            stmt.setInt(5, request.getStatusRequest().getId());
            stmt.setInt(6, request.getStatusPayment().getId());
            stmt.setInt(7, request.getTypePayment().getId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    request.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Request getById(int id) throws SQLException {
        String sql = "SELECT * FROM Request WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setMoment(rs.getTimestamp("moment").toLocalDateTime());
                request.setTotalValue(rs.getBigDecimal("totalValue"));

                // Load related entities
                ClientDAO clientDAO = new ClientDAO();
                request.setClient(clientDAO.getClientById(rs.getInt("clientId")));

                RestaurantDAO restaurantDAO = new RestaurantDAO();
                request.setRestaurant(restaurantDAO.getById(rs.getInt("restaurantId")));

                StatusRequestDAO statusRequestDAO = new StatusRequestDAO();
                request.setStatusRequest(statusRequestDAO.getById(rs.getInt("statusRequestId")));

                StatusPaymentDAO statusPaymentDAO = new StatusPaymentDAO();
                request.setStatusPayment(statusPaymentDAO.getById(rs.getInt("statusPaymentId")));

                TypePaymentDAO typePaymentDAO = new TypePaymentDAO();
                request.setTypePayment(typePaymentDAO.getById(rs.getInt("typePaymentId")));

                requests.add(request);
            }
        }
        return requests;
    }

    public List<Request> getRequestsByClient(int clientId) throws SQLException {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM Request WHERE clientId = ? ORDER BY moment DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Request request = new Request();
                request.setId(rs.getInt("id"));
                request.setMoment(rs.getTimestamp("moment").toLocalDateTime());
                request.setTotalValue(rs.getBigDecimal("totalValue"));

                ClientDAO clientDAO = new ClientDAO();
                request.setClient(clientDAO.getClientById(clientId));

                RestaurantDAO restaurantDAO = new RestaurantDAO();
                request.setRestaurant(restaurantDAO.getById(rs.getInt("restaurantId")));

                StatusRequestDAO statusRequestDAO = new StatusRequestDAO();
                request.setStatusRequest(statusRequestDAO.getById(rs.getInt("statusRequestId")));

                StatusPaymentDAO statusPaymentDAO = new StatusPaymentDAO();
                request.setStatusPayment(statusPaymentDAO.getById(rs.getInt("statusPaymentId")));

                TypePaymentDAO typePaymentDAO = new TypePaymentDAO();
                request.setTypePayment(typePaymentDAO.getById(rs.getInt("typePaymentId")));

                requests.add(request);
            }
        }
        return requests;
    }

    public void updateRequest(Request request) throws SQLException {
        String sql = "UPDATE Request SET moment = ?, totalValue = ?, clientId = ?, restaurantId = ?, statusRequestId = ?, statusPaymentId = ?, typePaymentId = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(request.getMoment()));
            stmt.setBigDecimal(2, request.getTotalValue());
            stmt.setInt(3, request.getClient().getId());
            stmt.setInt(4, request.getRestaurant().getId());
            stmt.setInt(5, request.getStatusRequest().getId());
            stmt.setInt(6, request.getStatusPayment().getId());
            stmt.setInt(7, request.getTypePayment().getId());
            stmt.setInt(8, request.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteRequest(int id) throws SQLException {
        // First delete related RequestProduct items
        RequestProductDAO requestProductDAO = new RequestProductDAO();
        requestProductDAO.deleteItemsByRequestId(id);

        String sql = "DELETE FROM Request WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}