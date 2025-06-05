package ifood.dao;

import ifood.model.Address;
import ifood.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    public void addAddress(Address address) throws SQLException {
        String sql = "INSERT INTO Address (street, number, complement, neighborhood, city, state, zipCode) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getNumber());
            stmt.setString(3, address.getComplement());
            stmt.setString(4, address.getNeighborhood());
            stmt.setString(5, address.getCity());
            stmt.setString(6, address.getState());
            stmt.setString(7, address.getZipCode());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    address.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Address getById(int id) throws SQLException {
        String sql = "SELECT * FROM Address WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Address address = new Address();
                address.setId(rs.getInt("id"));
                address.setStreet(rs.getString("street"));
                address.setNumber(rs.getString("number"));
                address.setComplement(rs.getString("complement"));
                address.setNeighborhood(rs.getString("neighborhood"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setZipCode(rs.getString("zipCode"));
                return address;
            }
        }
        return null;
    }

    public List<Address> getAllAddresses() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM Address";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Address address = new Address();
                address.setId(rs.getInt("id"));
                address.setStreet(rs.getString("street"));
                address.setNumber(rs.getString("number"));
                address.setComplement(rs.getString("complement"));
                address.setNeighborhood(rs.getString("neighborhood"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setZipCode(rs.getString("zipCode"));
                addresses.add(address);
            }
        }
        return addresses;
    }

    public void updateAddress(Address address) throws SQLException {
        String sql = "UPDATE Address SET street = ?, number = ?, complement = ?, neighborhood = ?, city = ?, state = ?, zipCode = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getNumber());
            stmt.setString(3, address.getComplement());
            stmt.setString(4, address.getNeighborhood());
            stmt.setString(5, address.getCity());
            stmt.setString(6, address.getState());
            stmt.setString(7, address.getZipCode());
            stmt.setInt(8, address.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteAddress(int id) throws SQLException {
        String sql = "DELETE FROM Address WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void deleteAddressIfNotReferenced(int addressId) throws SQLException {
        // Check if address is referenced by any Client or Restaurant
        String checkSql = "SELECT COUNT(*) FROM Client WHERE addressId = ? UNION ALL SELECT COUNT(*) FROM Restaurant WHERE addressId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setInt(1, addressId);
            stmt.setInt(2, addressId);
            ResultSet rs = stmt.executeQuery();

            boolean isReferenced = false;
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    isReferenced = true;
                    break;
                }
            }

            if (!isReferenced) {
                deleteAddress(addressId);
            }
        }
    }
}