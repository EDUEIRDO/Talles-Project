package org.dbconnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ifood";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

interface Identifiable {
    String getId();
}

// --- Address Entity and DAO ---
class Address {
    private int id;
    private String street;
    private String district;
    private String city;
    private String state;
    private int houseNumber;

    public Address(int id, String street, String district, String city, String state, int houseNumber) {
        this.id = id;
        this.street = street;
        this.district = district;
        this.city = city;
        this.state = state;
        this.houseNumber = houseNumber;
    }
    public Address(String street, String district, String city, String state, int houseNumber) {
        this(-1, street, district, city, state, houseNumber);
    }
    public int getId() { return id; }
    public String getStreet() { return street; }
    public String getDistrict() { return district; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public int getHouseNumber() { return houseNumber; }
}

class AddressDAO {
    public int create(Address address) {
        String sql = "INSERT INTO Address (street, district, city, state, houseNumber) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getDistrict());
            stmt.setString(3, address.getCity());
            stmt.setString(4, address.getState());
            stmt.setInt(5, address.getHouseNumber());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar endereço: " + e.getMessage());
        }
        return -1;
    }
}

class Produto implements Identifiable {
    private final String id;
    private String nome;
    private double preco;

    public Produto(String id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (R$ " + String.format("%.2f", preco) + ")";
    }
}

class Cliente implements Identifiable {
    private final String id;
    private String nome;
    private String telefone;
    private String email;
    private int addressId;

    public Cliente(String id, String nome, String telefone, String email, int addressId) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.addressId = addressId;
    }
    // Constructor without addressId, for existing clients or if address is optional
    public Cliente(String id, String nome, String telefone, String email) {
        this(id, nome, telefone, email, -1); // -1 indicates no address yet
    }
    @Override
    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAddressId() { // Getter for addressId
        return addressId;
    }
    public void setAddressId(int addressId) { // Setter for addressId
        this.addressId = addressId;
    }
    @Override
    public String toString() {
        return id + " - " + nome;
    }
}

class ProdutoNaoEncontradoException extends Exception {
    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
class ClienteNaoEncontradoException extends Exception {
    public ClienteNaoEncontradoException(String message) {
        super(message);
    }
}

// Add this class to your package
class ViaCEPService {
    private static final String VIACEP_URL = "https://viacep.com.br/ws/";

    public static Address getAddressByCep(String cep) {
        try {
            // Remove any non-digit characters from the CEP
            cep = cep.replaceAll("\\D", "");

            URL url = new URL(VIACEP_URL + cep + "/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                System.err.println("Erro ao consultar ViaCEP: HTTP error code : " + conn.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();

            JSONObject json = new JSONObject(sb.toString());

            // Check for error in JSON response (e.g., if CEP not found)
            if (json.has("erro") && json.getBoolean("erro")) {
                JOptionPane.showMessageDialog(null, "CEP não encontrado.", "Erro ViaCEP", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            String street = json.getString("logradouro");
            String district = json.getString("bairro");
            String city = json.getString("localidade");
            String state = json.getString("uf");

            // House number is not provided by ViaCEP, so we'll need to get it from the user.
            // For now, we'll pass 0 or handle it separately.
            return new Address(street, district, city, state, 0); // House number is 0 for now
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar ViaCEP: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}

class ProdutoDAO {
    public void create(Produto produto) {
        String sql = "INSERT INTO Product (id, name, price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(produto.getId()));
            stmt.setString(2, produto.getNome());
            stmt.setDouble(3, produto.getPreco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir produto: " + e.getMessage());
        }
    }

    public List<Produto> read() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, name, price FROM Product";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String nome = rs.getString("name");
                double preco = rs.getDouble("price");
                produtos.add(new Produto(id, nome, preco));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler produtos: " + e.getMessage());
        }
        return produtos;
    }

    public void update(Produto produtoAtualizado) throws ProdutoNaoEncontradoException {
        String sql = "UPDATE Product SET name=?, price=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produtoAtualizado.getNome());
            stmt.setDouble(2, produtoAtualizado.getPreco());
            stmt.setInt(3, Integer.parseInt(produtoAtualizado.getId()));
            int affected = stmt.executeUpdate();
            if (affected == 0) throw new ProdutoNaoEncontradoException("Produto não encontrado");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void delete(String idProduto) throws ProdutoNaoEncontradoException {
        String sql = "DELETE FROM Product WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idProduto));
            int affected = stmt.executeUpdate();
            if (affected == 0) throw new ProdutoNaoEncontradoException("Produto não encontrado");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir produto: " + e.getMessage());
        }
    }
}

class ClienteDAO {
    public void create(Cliente cliente) {
        String sql = "INSERT INTO Client (id, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(cliente.getId()));
            stmt.setString(2, cliente.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir cliente: " + e.getMessage());
        }
    }

    public List<Cliente> read() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, name FROM Client";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String nome = rs.getString("name");
                clientes.add(new Cliente(id, nome, "", ""));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler clientes: " + e.getMessage());
        }
        return clientes;
    }

    public void update(Cliente clienteAtualizado) throws ClienteNaoEncontradoException {
        String sql = "UPDATE Client SET name=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, clienteAtualizado.getNome());
            stmt.setInt(2, Integer.parseInt(clienteAtualizado.getId()));
            int affected = stmt.executeUpdate();
            if (affected == 0) throw new ClienteNaoEncontradoException("Cliente não encontrado");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public void delete(String idCliente) throws ClienteNaoEncontradoException {
        String sql = "DELETE FROM Client WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idCliente));
            int affected = stmt.executeUpdate();
            if (affected == 0) throw new ClienteNaoEncontradoException("Cliente não encontrado");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir cliente: " + e.getMessage());
        }
    }
}

// --- Classes auxiliares para pedidos ---
class PedidoProduto {
    Produto produto;
    int quantidade;

    public PedidoProduto(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }
}

class PedidoDAO {
    public int createPedido(int clientId, double deliveryValue) {
        String sql = "INSERT INTO Request (totalValue, deliveryValue, clientId, statusRequestId, typePaymentId, statusPaymentId, requestDate) VALUES (?, ?, ?, ?, ?, ?, NOW())";
        int pedidoId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, 0); // totalValue será atualizado depois
            stmt.setDouble(2, deliveryValue);
            stmt.setInt(3, clientId);
            stmt.setInt(4, 1); // statusRequestId: aguardando confirmação
            stmt.setInt(5, 1); // typePaymentId: cartão de crédito (exemplo)
            stmt.setInt(6, 1); // statusPaymentId: pendente
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) pedidoId = rs.getInt(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar pedido: " + e.getMessage());
        }
        return pedidoId;
    }

    public void addProdutoAoPedido(int pedidoId, int productId, int quantidade, double itemPrice) {
        String sql = "INSERT INTO RequestProduct (requestId, productId, quantity, itemPrice) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantidade);
            stmt.setDouble(4, itemPrice);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar produto ao pedido: " + e.getMessage());
        }
    }

    public void updateTotalPedido(int pedidoId, double total) {
        String sql = "UPDATE Request SET totalValue=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, total);
            stmt.setInt(2, pedidoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar total do pedido: " + e.getMessage());
        }
    }
}

// --- GUI ---
class GerenciadorEntidadesGUI extends JFrame {

    private ProdutoDAO produtoDAO;
    private ClienteDAO clienteDAO;

    // Fields for Produto Tab
    private JTextField prodIdField, prodNomeField, prodPrecoField;
    private JTextArea prodDisplayArea;

    // Fields for Cliente Tab
    private JTextField cliIdField, cliNomeField, cliTelefoneField, cliEmailField;
    private JTextArea cliDisplayArea;

    // Pedido
    private JComboBox<Cliente> pedidoClienteCombo;
    private JComboBox<Produto> pedidoProdutoCombo;
    private JTextField pedidoQuantidadeField, pedidoDeliveryValueField;
    private JTextArea pedidoResumoArea;
    private List<PedidoProduto> produtosDoPedido = new ArrayList<>();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    public GerenciadorEntidadesGUI() {
        produtoDAO = new ProdutoDAO();
        clienteDAO = new ClienteDAO();

        setTitle("Gerenciador de Cadastros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Produto Tab
        JPanel produtoPanel = createProdutoPanel();
        tabbedPane.addTab("Produtos", produtoPanel);

        // Pedido Tab
        JPanel pedidoPanel = createPedidoPanel();
        tabbedPane.addTab("Pedidos", pedidoPanel);

        // Cliente Tab
        JPanel clientePanel = createClientePanel();
        tabbedPane.addTab("Clientes", clientePanel);

        add(tabbedPane);

        refreshAllLists();
        setVisible(true);
    }

    private JPanel createProdutoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        inputPanel.add(new JLabel("ID:"));
        prodIdField = new JTextField();
        inputPanel.add(prodIdField);
        inputPanel.add(new JLabel("Nome:"));
        prodNomeField = new JTextField();
        inputPanel.add(prodNomeField);
        inputPanel.add(new JLabel("Preço:"));
        prodPrecoField = new JTextField();
        inputPanel.add(prodPrecoField);
        panel.add(inputPanel, BorderLayout.NORTH);

        prodDisplayArea = new JTextArea(15, 40);
        prodDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(prodDisplayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton createButton = new JButton("Criar Produto");
        JButton updateButton = new JButton("Atualizar Produto");
        JButton deleteButton = new JButton("Excluir Produto");
        actionPanel.add(createButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> criarProduto());
        updateButton.addActionListener(e -> atualizarProduto());
        deleteButton.addActionListener(e -> excluirProduto());

        return panel;
    }

    private JPanel createClientePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        inputPanel.add(new JLabel("ID:"));
        cliIdField = new JTextField();
        inputPanel.add(cliIdField);
        inputPanel.add(new JLabel("Nome:"));
        cliNomeField = new JTextField();
        inputPanel.add(cliNomeField);
        inputPanel.add(new JLabel("Telefone:"));
        cliTelefoneField = new JTextField();
        inputPanel.add(cliTelefoneField);
        inputPanel.add(new JLabel("Email:"));
        cliEmailField = new JTextField();
        inputPanel.add(cliEmailField);
        panel.add(inputPanel, BorderLayout.NORTH);

        cliDisplayArea = new JTextArea(15, 40);
        cliDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(cliDisplayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton createButton = new JButton("Criar Cliente");
        JButton updateButton = new JButton("Atualizar Cliente");
        JButton deleteButton = new JButton("Excluir Cliente");
        actionPanel.add(createButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> criarCliente());
        updateButton.addActionListener(e -> atualizarCliente());
        deleteButton.addActionListener(e -> excluirCliente());

        return panel;
    }

    // -------- ABA DE PEDIDO --------
    private JPanel createPedidoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Novo Pedido"));

        inputPanel.add(new JLabel("Cliente:"));
        pedidoClienteCombo = new JComboBox<>();
        inputPanel.add(pedidoClienteCombo);

        inputPanel.add(new JLabel("Produto:"));
        pedidoProdutoCombo = new JComboBox<>();
        inputPanel.add(pedidoProdutoCombo);

        inputPanel.add(new JLabel("Quantidade:"));
        pedidoQuantidadeField = new JTextField("1");
        inputPanel.add(pedidoQuantidadeField);

        inputPanel.add(new JLabel("Taxa de entrega:"));
        pedidoDeliveryValueField = new JTextField("5.00");
        inputPanel.add(pedidoDeliveryValueField);

        JButton addProdutoBtn = new JButton("Adicionar Produto");
        inputPanel.add(addProdutoBtn);
        addProdutoBtn.addActionListener(e -> adicionarProdutoAoPedido());

        panel.add(inputPanel, BorderLayout.NORTH);

        pedidoResumoArea = new JTextArea(10, 40);
        pedidoResumoArea.setEditable(false);
        panel.add(new JScrollPane(pedidoResumoArea), BorderLayout.CENTER);

        JButton finalizarBtn = new JButton("Finalizar Pedido");
        finalizarBtn.addActionListener(e -> finalizarPedido());
        panel.add(finalizarBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void adicionarProdutoAoPedido() {
        Produto produto = (Produto) pedidoProdutoCombo.getSelectedItem();
        int qtd;
        try {
            qtd = Integer.parseInt(pedidoQuantidadeField.getText().trim());
            if (qtd <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        produtosDoPedido.add(new PedidoProduto(produto, qtd));
        atualizarResumoPedido();
    }

    private void atualizarResumoPedido() {
        pedidoResumoArea.setText("");
        double total = 0;
        for (PedidoProduto pp : produtosDoPedido) {
            pedidoResumoArea.append(pp.produto.getNome() + " x" + pp.quantidade + " - R$" + String.format("%.2f", pp.produto.getPreco() * pp.quantidade) + "\n");
            total += pp.produto.getPreco() * pp.quantidade;
        }
        pedidoResumoArea.append("\nTotal (sem entrega): R$" + String.format("%.2f", total));
    }

    private void finalizarPedido() {
        if (produtosDoPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Cliente cliente = (Cliente) pedidoClienteCombo.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double deliveryValue;
        try {
            deliveryValue = Double.parseDouble(pedidoDeliveryValueField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Taxa de entrega inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int pedidoId = pedidoDAO.createPedido(Integer.parseInt(cliente.getId()), deliveryValue);
        if (pedidoId == -1) {
            JOptionPane.showMessageDialog(this, "Erro ao criar o pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double total = 0;
        for (PedidoProduto pp : produtosDoPedido) {
            pedidoDAO.addProdutoAoPedido(pedidoId, Integer.parseInt(pp.produto.getId()), pp.quantidade, pp.produto.getPreco());
            total += pp.produto.getPreco() * pp.quantidade;
        }
        pedidoDAO.updateTotalPedido(pedidoId, total);
        JOptionPane.showMessageDialog(this, "Pedido realizado com sucesso! ID: " + pedidoId);
        produtosDoPedido.clear();
        atualizarResumoPedido();
    }

    // --- CRUD Methods for Produto ---
    private void criarProduto() {
        try {
            String id = prodIdField.getText().trim();
            String nome = prodNomeField.getText().trim();
            double preco = Double.parseDouble(prodPrecoField.getText().trim());

            if (id.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID e Nome do Produto não podem ser vazios.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Produto p : produtoDAO.read()) {
                if (p.getId().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Produto com este ID já existe.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Produto produto = new Produto(id, nome, preco);
            produtoDAO.create(produto);
            JOptionPane.showMessageDialog(this, "Produto criado com sucesso!");
            clearProdutoFields();
            refreshProdutoList();
            refreshPedidoProdutos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Por favor, digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        try {
            String id = prodIdField.getText().trim();
            String nome = prodNomeField.getText().trim();
            double preco = Double.parseDouble(prodPrecoField.getText().trim());

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Produto não pode ser vazio para atualização.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Produto produtoAtualizado = new Produto(id, nome, preco);
            produtoDAO.update(produtoAtualizado);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            clearProdutoFields();
            refreshProdutoList();
            refreshPedidoProdutos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Por favor, digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        try {
            String id = prodIdField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Produto não pode ser vazio para exclusão.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto com ID: " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                produtoDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                clearProdutoFields();
                refreshProdutoList();
                refreshPedidoProdutos();
            }
        } catch (ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearProdutoFields() {
        prodIdField.setText("");
        prodNomeField.setText("");
        prodPrecoField.setText("");
    }

    private void refreshProdutoList() {
        prodDisplayArea.setText("");
        List<Produto> produtos = produtoDAO.read();
        if (produtos.isEmpty()) {
            prodDisplayArea.append("Nenhum produto cadastrado.\n");
        } else {
            for (Produto p : produtos) {
                prodDisplayArea.append(p.toString() + "\n");
            }
        }
    }

    // --- CRUD Methods for Cliente ---
    private void criarCliente() {
        try {
            String id = cliIdField.getText().trim();
            String nome = cliNomeField.getText().trim();
            String telefone = cliTelefoneField.getText().trim();
            String email = cliEmailField.getText().trim();

            if (id.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID e Nome do Cliente não podem ser vazios.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Cliente c : clienteDAO.read()) {
                if (c.getId().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente com este ID já existe.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Cliente cliente = new Cliente(id, nome, telefone, email);
            clienteDAO.create(cliente);
            JOptionPane.showMessageDialog(this, "Cliente criado com sucesso!");
            clearClienteFields();
            refreshClienteList();
            refreshPedidoClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarCliente() {
        try {
            String id = cliIdField.getText().trim();
            String nome = cliNomeField.getText().trim();
            String telefone = cliTelefoneField.getText().trim();
            String email = cliEmailField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Cliente não pode ser vazio para atualização.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Cliente clienteAtualizado = new Cliente(id, nome, telefone, email);
            clienteDAO.update(clienteAtualizado);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            clearClienteFields();
            refreshClienteList();
            refreshPedidoClientes();
        } catch (ClienteNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCliente() {
        try {
            String id = cliIdField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Cliente não pode ser vazio para exclusão.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente com ID: " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clienteDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                clearClienteFields();
                refreshClienteList();
                refreshPedidoClientes();
            }
        } catch (ClienteNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearClienteFields() {
        cliIdField.setText("");
        cliNomeField.setText("");
        cliTelefoneField.setText("");
        cliEmailField.setText("");
    }

    private void refreshClienteList() {
        cliDisplayArea.setText("");
        List<Cliente> clientes = clienteDAO.read();
        if (clientes.isEmpty()) {
            cliDisplayArea.append("Nenhum cliente cadastrado.\n");
        } else {
            for (Cliente c : clientes) {
                cliDisplayArea.append(c.toString() + "\n");
            }
        }
    }

    // Métodos auxiliares para Pedido
    private void refreshPedidoProdutos() {
        pedidoProdutoCombo.removeAllItems();
        for (Produto p : produtoDAO.read()) {
            pedidoProdutoCombo.addItem(p);
        }
    }

    private void refreshPedidoClientes() {
        pedidoClienteCombo.removeAllItems();
        for (Cliente c : clienteDAO.read()) {
            pedidoClienteCombo.addItem(c);
        }
    }

    private void refreshAllLists() {
        refreshProdutoList();
        refreshClienteList();
        refreshPedidoProdutos();
        refreshPedidoClientes();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GerenciadorEntidadesGUI::new);
    }
}