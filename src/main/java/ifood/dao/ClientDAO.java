package ifood.dao;

import ifood.model.Address; // Assumindo que AddressDAO existe e tem um método getById
import ifood.model.Client;
import ifood.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // Helper method to populate Address (can be in AddressDAO too)
    private Address getAddressById(int addressId) {
        // Implementar: buscar endereço no AddressDAO ou diretamente aqui
        // Este é um exemplo simplificado. O ideal é chamar AddressDAO.getById(addressId)
        AddressDAO addressDAO = new AddressDAO(); // Ou injetar dependência
        return addressDAO.getById(addressId);
    }


    public void addClient(Client client) throws SQLException {
        // Primeiro, salve o endereço para obter o ID, se ainda não existir ou for novo
        // Esta parte precisa de lógica mais robusta: o Address já existe? É para criar um novo?
        // Para simplificar o exemplo, assumiremos que o client.getAddress() já tem um ID se existir
        // ou que o AddressDAO cuidará de inserir e retornar o ID.

        // Se o endereço do cliente for novo e não tiver ID, precisa ser inserido primeiro.
        // AddressDAO addressDAO = new AddressDAO();
        // if (client.getAddress() != null && client.getAddress().getId() == 0) {
        //     addressDAO.addAddress(client.getAddress()); // addAddress deve setar o ID no objeto Address
        // }
        String sql = "INSERT INTO Client (name, addressId) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getName());
            if (client.getAddress() != null) {
                stmt.setInt(2, client.getAddress().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Client getClientById(int id) throws SQLException {
        String sql = "SELECT * FROM Client WHERE id = ?";
        Client client = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                int addressId = rs.getInt("addressId");
                if (!rs.wasNull()) {
                    // Aqui você precisaria buscar o objeto Address completo
                    // Usando um AddressDAO, por exemplo:
                    AddressDAO addressDAO = new AddressDAO();
                    client.setAddress(addressDAO.getById(addressId));
                }
            }
        }
        return client;
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                int addressId = rs.getInt("addressId");
                if (!rs.wasNull()) {
                    AddressDAO addressDAO = new AddressDAO();
                    client.setAddress(addressDAO.getById(addressId));
                }
                clients.add(client);
            }
        }
        return clients;
    }

    public void updateClient(Client client) throws SQLException {
        // Lembre-se de atualizar o Address também, se necessário e se for gerenciado aqui
        // AddressDAO addressDAO = new AddressDAO();
        // if (client.getAddress() != null) {
        //     addressDAO.updateAddress(client.getAddress());
        // }

        String sql = "UPDATE Client SET name = ?, addressId = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getName());
            if (client.getAddress() != null) {
                stmt.setInt(2, client.getAddress().getId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, client.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteClient(int id) throws SQLException {
        // Considere o que acontece com o endereço. Ele deve ser deletado se nenhum outro cliente/restaurante o usa?
        // Para este projeto, podemos assumir que o endereço pode permanecer ou ser deletado se não houver FKs.
        // Se houver FK constraint, você precisará deletar o cliente primeiro ou setar addressId para NULL.
        // Verifique as constraints ON DELETE do seu banco.
        // A FK em Client para Address não tem ON DELETE CASCADE no seu DDL,
        // então você não pode deletar um Address se um Client ainda o referencia.
        // Primeiro, você poderia desassociar o endereço:
        // UPDATE Client SET addressId = NULL WHERE id = ?
        // Ou, se o endereço é exclusivo do cliente, deletar o endereço DEPOIS de desassociar
        // ou ANTES de deletar o cliente se a constraint permitir (o que geralmente não é o caso).

        // Primeiro, obtenha o ID do endereço para possível exclusão posterior se não for compartilhado
        Client clientToDelete = getClientById(id); // Reutiliza o método para pegar o cliente e seu endereço

        String sql = "DELETE FROM Client WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Opcional: deletar o endereço se não for mais usado.
        // Isso requer verificar se algum outro Client ou Restaurant usa este Address.
        // if (clientToDelete != null && clientToDelete.getAddress() != null) {
        //    AddressDAO addressDAO = new AddressDAO();
        //    addressDAO.deleteAddressIfNotReferenced(clientToDelete.getAddress().getId());
        // }
    }
}