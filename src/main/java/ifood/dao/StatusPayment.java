//package ifood.dao;
//
//import ifood.model.StatusPayment;
//import ifood.util.DatabaseConnection;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class StatusPaymentDAO {
//
//    public void addStatusPayment(StatusPayment statusPayment) throws SQLException {
//        String sql = "INSERT INTO StatusPayment (description) VALUES (?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, statusPayment.getDescription());
//            stmt.executeUpdate();
//
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    statusPayment.setId(generatedKeys.getInt(1));
//                }
//            }
//        }
//    }
//
//    public StatusPayment getById(int id) throws SQLException {
//        String sql = "SELECT * FROM StatusPayment WHERE id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                StatusPayment statusPayment = new StatusPayment();
//                statusPayment.setId(rs.getInt("id"));
//                statusPayment.setDescription(rs.getString("description"));
//                return statusPayment;
//            }
//        }
//        return null;
//    }
//
//    public List<StatusPayment> getAllStatusPayments() throws SQLException {
//        List<StatusPayment> statusPayments = new ArrayList<>();
//        String sql = "SELECT * FROM StatusPayment";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                StatusPayment statusPayment = new StatusPayment();
//                statusPayment.setId(rs.getInt("id"));
//                statusPayment.setDescription(rs.getString("description"));
//                statusPayments.add(statusPayment);
//            }
//        }
//        return statusPayments;
//    }
//
//    public void updateStatusPayment(StatusPayment statusPayment) throws SQLException {
//        String sql = "UPDATE StatusPayment SET description = ? WHERE id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, statusPayment.getDescription());
//            stmt.setInt(2, statusPayment.getId());
//            stmt.executeUpdate();
//        }
//    }
//
//    public void deleteStatusPayment(int id) throws SQLException {
//        String sql = "DELETE FROM StatusPayment WHERE id = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            stmt.executeUpdate();
//        }
//    }
//}