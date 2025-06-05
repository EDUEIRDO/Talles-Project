package org.dbconnection;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    // Ajuste conforme seu ambiente
    private static final String URL = "jdbc:mysql://localhost:3306/ifood";
    private static final String USER = "root";
    private static final String PASS = "1234";

    // Cria um novo pedido e vincula produtos
    public static int createOrder(double totalValue, double deliveryValue, int clientId, int restaurantId, int addressDeliveryId,
                                  int statusRequestId, int typePaymentId, int statusPaymentId, Timestamp requestDate,
                                  List<OrderItem> items) throws SQLException {
        int orderId = -1;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            try {
                String sql = "INSERT INTO Request (totalValue, deliveryValue, clientId, restaurantId, addressDeliveryId, statusRequestId, typePaymentId, statusPaymentId, requestDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDouble(1, totalValue);
                    ps.setDouble(2, deliveryValue);
                    ps.setInt(3, clientId);
                    ps.setInt(4, restaurantId);
                    ps.setInt(5, addressDeliveryId);
                    ps.setInt(6, statusRequestId);
                    ps.setInt(7, typePaymentId);
                    ps.setInt(8, statusPaymentId);
                    ps.setTimestamp(9, requestDate);
                    ps.executeUpdate();
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
                // Insere os produtos do pedido
                String sqlItem = "INSERT INTO RequestProduct (requestId, productId, notes, quantity, itemPrice) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    for (OrderItem item : items) {
                        psItem.setInt(1, orderId);
                        psItem.setInt(2, item.productId);
                        psItem.setString(3, item.notes);
                        psItem.setInt(4, item.quantity);
                        psItem.setDouble(5, item.itemPrice);
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
        return orderId;
    }

    // Lê um pedido pelo ID
    public static void readOrder(int orderId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT * FROM Request WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Pedido #" + rs.getInt("id") + " | Cliente: " + rs.getInt("clientId") + " | Restaurante: " + rs.getInt("restaurantId"));
                    System.out.println("Total: " + rs.getDouble("totalValue") + " | Entrega: " + rs.getDouble("deliveryValue"));
                    System.out.println("Status: " + rs.getInt("statusRequestId") + " | Pagamento: " + rs.getInt("statusPaymentId"));
                    System.out.println("Produtos:");
                    // Busca os produtos do pedido
                    String sqlProd = "SELECT * FROM RequestProduct WHERE requestId = ?";
                    try (PreparedStatement psProd = conn.prepareStatement(sqlProd)) {
                        psProd.setInt(1, orderId);
                        ResultSet rsProd = psProd.executeQuery();
                        while (rsProd.next()) {
                            System.out.println("- Produto: " + rsProd.getInt("productId") + " | Qtd: " + rsProd.getInt("quantity") + " | Preço: " + rsProd.getDouble("itemPrice"));
                        }
                    }
                } else {
                    System.out.println("Pedido não encontrado.");
                }
            }
        }
    }

    // Atualiza o status do pedido
    public static void updateOrderStatus(int orderId, int newStatusRequestId, int newStatusPaymentId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "UPDATE Request SET statusRequestId = ?, statusPaymentId = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, newStatusRequestId);
                ps.setInt(2, newStatusPaymentId);
                ps.setInt(3, orderId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Status do pedido atualizado.");
                } else {
                    System.out.println("Pedido não encontrado.");
                }
            }
        }
    }

    // Deleta um pedido e seus produtos
    public static void deleteOrder(int orderId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM RequestProduct WHERE requestId = ?")) {
                    ps.setInt(1, orderId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Request WHERE id = ?")) {
                    ps.setInt(1, orderId);
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.println("Pedido deletado.");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Classe auxiliar para itens do pedido
    public static class OrderItem {
        public int productId;
        public String notes;
        public int quantity;
        public double itemPrice;

        public OrderItem(int productId, String notes, int quantity, double itemPrice) {
            this.productId = productId;
            this.notes = notes;
            this.quantity = quantity;
            this.itemPrice = itemPrice;
        }
    }

// Classe auxiliar para Cliente
public static class Cliente {
    public int id;
    public String nome;
    public String email;
    public String telefone;

    public Cliente(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
}

// Cadastrar cliente
public static int createCliente(String nome, String email, String telefone) throws SQLException {
    int clienteId = -1;
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
        String sql = "INSERT INTO Cliente (nome, email, telefone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, telefone);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                clienteId = rs.getInt(1);
            }
        }
    }
    return clienteId;
}

// Editar cliente
public static void updateCliente(int id, String nome, String email, String telefone) throws SQLException {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
        String sql = "UPDATE Cliente SET nome = ?, email = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, telefone);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }
}

// Listar clientes
public static List<Cliente> listClientes() throws SQLException {
    List<Cliente> clientes = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
        String sql = "SELECT * FROM Client";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("telefone")
                ));
            }
        }
    }
    return clientes;
}

// Excluir cliente
public static void deleteCliente(int id) throws SQLException {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// Interface gráfica para cadastro de cliente
public static void showClienteRegistrationUI() {
    javax.swing.SwingUtilities.invokeLater(() -> {
        javax.swing.JFrame frame = new javax.swing.JFrame("Cadastro de Cliente");
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        javax.swing.JLabel nomeLabel = new javax.swing.JLabel("Nome:");
        javax.swing.JTextField nomeField = new javax.swing.JTextField();

        javax.swing.JLabel emailLabel = new javax.swing.JLabel("Email:");
        javax.swing.JTextField emailField = new javax.swing.JTextField();

        javax.swing.JLabel telefoneLabel = new javax.swing.JLabel("Telefone:");
        javax.swing.JTextField telefoneField = new javax.swing.JTextField();

        javax.swing.JButton cadastrarBtn = new javax.swing.JButton("Cadastrar");
        javax.swing.JLabel statusLabel = new javax.swing.JLabel();

        cadastrarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String email = emailField.getText().trim();
            String telefone = telefoneField.getText().trim();
            if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
                statusLabel.setText("Preencha todos os campos.");
                return;
            }
            try {
                int id = createCliente(nome, email, telefone);
                if (id > 0) {
                    statusLabel.setText("Cliente cadastrado! ID: " + id);
                    nomeField.setText("");
                    emailField.setText("");
                    telefoneField.setText("");
                } else {
                    statusLabel.setText("Erro ao cadastrar.");
                }
            } catch (Exception ex) {
                statusLabel.setText("Erro: " + ex.getMessage());
            }
        });

        frame.add(nomeLabel);
        frame.add(nomeField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(telefoneLabel);
        frame.add(telefoneField);
        frame.add(new javax.swing.JLabel());
        frame.add(cadastrarBtn);
        frame.add(statusLabel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    });
}


    // Exemplo de uso
    public static void main(String[] args) throws Exception {
        // Criar pedido
        List<OrderItem> items = Arrays.asList(
            new OrderItem(1, "Sem cebola", 2, 35.0),
            new OrderItem(3, null, 1, 6.0)
        );
        int orderId = createOrder(
            76.0, 5.0, 1, 1, 1, 1, 1, 2, new Timestamp(System.currentTimeMillis()), items
        );
        System.out.println("Pedido criado com ID: " + orderId);

        // Ler pedido
        readOrder(orderId);

        // Atualizar status
        updateOrderStatus(orderId, 2, 2);

        showClienteRegistrationUI();



        // Deletar pedido
        // deleteOrder(orderId);
    }
}
