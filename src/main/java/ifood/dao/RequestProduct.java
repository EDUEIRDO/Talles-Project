//package ifood.dao;
//
//import ifood.model.RequestProduct;
//import ifood.model.Product;
//import com.minifoodapp.util.DatabaseConnector;
//
//import java.math.BigDecimal;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class RequestProductDAO {
//
//    public void addItemsToRequest(int requestId, List<RequestProduct> items) throws SQLException {
//        String sql = "INSERT INTO RequestProduct (requestId, productId, quantity, unitValue) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            for (RequestProduct item : items) {
//                stmt.setInt(1, requestId);
//                stmt.setInt(2, item.getProduct().getId());
//                stmt.setInt(3, item.getQuantity());
//                stmt.setBigDecimal(4, item.getUnitValue());
//                stmt.addBatch();
//            }
//            stmt.executeBatch();
//        }
//    }
//
//    public void addRequestProduct(RequestProduct requestProduct) throws SQLException {
//        String sql = "INSERT INTO RequestProduct (requestId, productId, quantity, unitValue) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestProduct.getRequestId());
//            stmt.setInt(2, requestProduct.getProduct().getId());
//            stmt.setInt(3, requestProduct.getQuantity());
//            stmt.setBigDecimal(4, requestProduct.getUnitValue());
//            stmt.executeUpdate();
//        }
//    }
//
//    public List<RequestProduct> getItemsByRequestId(int requestId) throws SQLException {
//        List<RequestProduct> items = new ArrayList<>();
//        String sql = "SELECT * FROM RequestProduct WHERE requestId = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestId);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                RequestProduct item = new RequestProduct();
//                item.setRequestId(rs.getInt("requestId"));
//                item.setQuantity(rs.getInt("quantity"));
//                item.setUnitValue(rs.getBigDecimal("unitValue"));
//
//                // Load related Product
//                ProductDAO productDAO = new ProductDAO();
//                item.setProduct(productDAO.getById(rs.getInt("productId")));
//
//                items.add(item);
//            }
//        }
//        return items;
//    }
//
//    public RequestProduct getByRequestAndProduct(int requestId, int productId) throws SQLException {
//        String sql = "SELECT * FROM RequestProduct WHERE requestId = ? AND productId = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestId);
//            stmt.setInt(2, productId);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                RequestProduct item = new RequestProduct();
//                item.setRequestId(rs.getInt("requestId"));
//                item.setQuantity(rs.getInt("quantity"));
//                item.setUnitValue(rs.getBigDecimal("unitValue"));
//
//                ProductDAO productDAO = new ProductDAO();
//                item.setProduct(productDAO.getById(productId));
//
//                return item;
//            }
//        }
//        return null;
//    }
//
//    public void updateRequestProduct(RequestProduct requestProduct) throws SQLException {
//        String sql = "UPDATE RequestProduct SET quantity = ?, unitValue = ? WHERE requestId = ? AND productId = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestProduct.getQuantity());
//            stmt.setBigDecimal(2, requestProduct.getUnitValue());
//            stmt.setInt(3, requestProduct.getRequestId());
//            stmt.setInt(4, requestProduct.getProduct().getId());
//            stmt.executeUpdate();
//        }
//    }
//
//    public void deleteRequestProduct(int requestId, int productId) throws SQLException {
//        String sql = "DELETE FROM RequestProduct WHERE requestId = ? AND productId = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestId);
//            stmt.setInt(2, productId);
//            stmt.executeUpdate();
//        }
//    }
//
//    public void deleteItemsByRequestId(int requestId) throws SQLException {
//        String sql = "DELETE FROM RequestProduct WHERE requestId = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestId);
//            stmt.executeUpdate();
//        }
//    }
//
//    public BigDecimal calculateRequestTotal(int requestId) throws SQLException {
//        String sql = "SELECT SUM(quantity * unitValue) as total FROM RequestProduct WHERE requestId = ?";
//        try (Connection conn = DatabaseConnector.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, requestId);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                BigDecimal total = rs.getBigDecimal("total");
//                return total != null ? total : BigDecimal.ZERO;
//            }
//        }
//        return BigDecimal.ZERO;
//    }
//}
//                request.setMoment(rs.getTimestamp("moment").toLocalDateTime());
//        request.setTotalValue(rs.getBigDecimal("totalValue"));
//
//// Load related entities
//ClientDAO clientDAO = new ClientDAO();
//                request.setClient(clientDAO.getClientById(rs.getInt("clientId")));
//
//RestaurantDAO restaurantDAO = new RestaurantDAO();
//                request.setRestaurant(restaurantDAO.getById(rs.getInt("restaurantId")));
//
//StatusRequestDAO statusRequestDAO = new StatusRequestDAO();
//                request.setStatusRequest(statusRequestDAO.getById(rs.getInt("statusRequestId")));
//
//StatusPaymentDAO statusPaymentDAO = new StatusPaymentDAO();
//                request.setStatusPayment(statusPaymentDAO.getById(rs.getInt("statusPaymentId")));
//
//TypePaymentDAO typePaymentDAO = new TypePaymentDAO();
//                request.setTypePayment(typePaymentDAO.getById(rs.getInt("typePaymentId")));
//
//        return request;
//            }
//                    }
//                    return null;
//                    }
//
//public List<Request> getAllRequests() throws SQLException {
//    List<Request> requests = new ArrayList<>();
//    String sql = "SELECT * FROM Request ORDER BY moment DESC";
//    try (Connection conn = DatabaseConnector.getConnection();
//         Statement stmt = conn.createStatement();
//         ResultSet rs = stmt.executeQuery(sql)) {
//        while (rs.next()) {
//            Request request = new Request();
//            request.setId(rs.getInt("id"));