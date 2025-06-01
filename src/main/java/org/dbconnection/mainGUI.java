package org.dbconnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

interface Identifiable extends Serializable {
    String getId();
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
        return "ID: " + id + ", Nome: " + nome + ", Preço: R$" + String.format("%.2f", preco);
    }
}

class Cliente implements Identifiable{
    private final String id;
    private String nome;
    private String telefone;
    private String email;

    public Cliente (String id, String nome, String telefone, String email){
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
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
    @Override
    public String toString() {
        return "ID: " + id + "Nome: " + nome + "Telefone: " + telefone + "Email: " + email;
    }

}
class Restaurante implements Identifiable {
    private final String id;
    private String nome;
    private String endereco;
    private String tipoCozinha;

    public Restaurante(String id, String nome, String endereco, String tipoCozinha) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Endereço: " + endereco + ", Cozinha: " + tipoCozinha;
    }
}
class Funcionario implements Identifiable {
    private final String id;
    private String nome;
    private String cargo;
    private double salario;

    public Funcionario(String id, String nome, String cargo, double salario) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Cargo: " + cargo + ", Salário: R$" + String.format("%.2f", salario);
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
class RestauranteNaoEncontradoException extends Exception {
    public RestauranteNaoEncontradoException(String message) {
        super(message);
    }
}
class FuncionarioNaoEncontradoException extends Exception {
    public FuncionarioNaoEncontradoException(String message) {
        super(message);
    }
}

class ProdutoDAO {
    private List<Produto> produtos;
    private static final String ARQUIVO_DADOS = "produtos.ser";

    public ProdutoDAO() {

        produtos = carregarProdutos();
    }

    public void salvarProdutos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(produtos);
            System.out.println("Dados dos produtos salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos produtos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Produto> carregarProdutos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            return (List<Produto>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados não encontrado. Criando nova lista de produtos.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public void create(Produto produto) {

        produtos.add(produto);
    }

    public List<Produto> read() {
        return new ArrayList<>(produtos);
    }
    public void update(Produto produtoAtualizado) throws ProdutoNaoEncontradoException {
        boolean encontrado = false;
        for (int i = 0; i < produtos.size(); i++) {

            if (produtos.get(i).getId().equals(produtoAtualizado.getId())) {
                produtos.set(i, produtoAtualizado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new ProdutoNaoEncontradoException("Produto com ID " + produtoAtualizado.getId() + " não encontrado para atualização.");
        }
    }

    public void delete(String idProduto) throws ProdutoNaoEncontradoException {
        Produto produtoParaRemover = null;
        for (Produto p : produtos) {
            if (p.getId().equals(idProduto)) {
                produtoParaRemover = p;
                break;
            }
        }
        if (produtoParaRemover != null) {
            produtos.remove(produtoParaRemover);
        } else {
            throw new ProdutoNaoEncontradoException("Produto com ID " + idProduto + " não encontrado para exclusão.");
        }
    }
}

class ClienteDAO {
    private List<Cliente> clientes;
    private static final String ARQUIVO_DADOS = "clientes.ser";

    public ClienteDAO() {
        clientes = carregarClientes();
    }

    public void salvarClientes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(clientes);
            System.out.println("Dados dos clientes salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos clientes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Cliente> carregarClientes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            return (List<Cliente>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados de clientes não encontrado. Criando nova lista.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void create(Cliente cliente) {
        clientes.add(cliente);
    }

    public List<Cliente> read() {
        return new ArrayList<>(clientes);
    }

    public void update(Cliente clienteAtualizado) throws ClienteNaoEncontradoException {
        boolean encontrado = false;
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId().equals(clienteAtualizado.getId())) {
                clientes.set(i, clienteAtualizado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + clienteAtualizado.getId() + " não encontrado para atualização.");
        }
    }

    public void delete(String idCliente) throws ClienteNaoEncontradoException {
        Cliente clienteParaRemover = null;
        for (Cliente c : clientes) {
            if (c.getId().equals(idCliente)) {
                clienteParaRemover = c;
                break;
            }
        }
        if (clienteParaRemover != null) {
            clientes.remove(clienteParaRemover);
        } else {
            throw new ClienteNaoEncontradoException("Cliente com ID " + idCliente + " não encontrado para exclusão.");
        }
    }
}

class RestauranteDAO {
    private List<Restaurante> restaurantes;
    private static final String ARQUIVO_DADOS = "restaurantes.ser";

    public RestauranteDAO() {
        restaurantes = carregarRestaurantes();
    }

    public void salvarRestaurantes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(restaurantes);
            System.out.println("Dados dos restaurantes salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos restaurantes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Restaurante> carregarRestaurantes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            return (List<Restaurante>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados de restaurantes não encontrado. Criando nova lista.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos restaurantes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void create(Restaurante restaurante) {
        restaurantes.add(restaurante);
    }

    public List<Restaurante> read() {
        return new ArrayList<>(restaurantes);
    }

    public void update(Restaurante restauranteAtualizado) throws RestauranteNaoEncontradoException {
        boolean encontrado = false;
        for (int i = 0; i < restaurantes.size(); i++) {
            if (restaurantes.get(i).getId().equals(restauranteAtualizado.getId())) {
                restaurantes.set(i, restauranteAtualizado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new RestauranteNaoEncontradoException("Restaurante com ID " + restauranteAtualizado.getId() + " não encontrado para atualização.");
        }
    }

    public void delete(String idRestaurante) throws RestauranteNaoEncontradoException {
        Restaurante restauranteParaRemover = null;
        for (Restaurante r : restaurantes) {
            if (r.getId().equals(idRestaurante)) {
                restauranteParaRemover = r;
                break;
            }
        }
        if (restauranteParaRemover != null) {
            restaurantes.remove(restauranteParaRemover);
        } else {
            throw new RestauranteNaoEncontradoException("Restaurante com ID " + idRestaurante + " não encontrado para exclusão.");
        }
    }
}

class FuncionarioDAO {
    private List<Funcionario> funcionarios;
    private static final String ARQUIVO_DADOS = "funcionarios.ser";

    public FuncionarioDAO() {
        funcionarios = carregarFuncionarios();
    }

    public void salvarFuncionarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(funcionarios);
            System.out.println("Dados dos funcionários salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos funcionários: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Funcionario> carregarFuncionarios() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            return (List<Funcionario>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados de funcionários não encontrado. Criando nova lista.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos funcionários: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void create(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    public List<Funcionario> read() {
        return new ArrayList<>(funcionarios);
    }

    public void update(Funcionario funcionarioAtualizado) throws FuncionarioNaoEncontradoException {
        boolean encontrado = false;
        for (int i = 0; i < funcionarios.size(); i++) {
            if (funcionarios.get(i).getId().equals(funcionarioAtualizado.getId())) {
                funcionarios.set(i, funcionarioAtualizado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new FuncionarioNaoEncontradoException("Funcionário com ID " + funcionarioAtualizado.getId() + " não encontrado para atualização.");
        }
    }

    public void delete(String idFuncionario) throws FuncionarioNaoEncontradoException {
        Funcionario funcionarioParaRemover = null;
        for (Funcionario f : funcionarios) {
            if (f.getId().equals(idFuncionario)) {
                funcionarioParaRemover = f;
                break;
            }
        }
        if (funcionarioParaRemover != null) {
            funcionarios.remove(funcionarioParaRemover);
        } else {
            throw new FuncionarioNaoEncontradoException("Funcionário com ID " + idFuncionario + " não encontrado para exclusão.");
        }
    }
}

class GerenciadorEntidadesGUI extends JFrame {

    private ProdutoDAO produtoDAO;
    private ClienteDAO clienteDAO;
    private RestauranteDAO restauranteDAO;
    private FuncionarioDAO funcionarioDAO;

    // Fields for Produto Tab
    private JTextField prodIdField, prodNomeField, prodPrecoField;
    private JTextArea prodDisplayArea;

    // Fields for Cliente Tab
    private JTextField cliIdField, cliNomeField, cliTelefoneField, cliEmailField;
    private JTextArea cliDisplayArea;

    // Fields for Restaurante Tab
    private JTextField restIdField, restNomeField, restEnderecoField, restTipoCozinhaField;
    private JTextArea restDisplayArea;

    // Fields for Funcionario Tab
    private JTextField funcIdField, funcNomeField, funcCargoField, funcSalarioField;
    private JTextArea funcDisplayArea;


    public GerenciadorEntidadesGUI() {
        produtoDAO = new ProdutoDAO();
        clienteDAO = new ClienteDAO();
        restauranteDAO = new RestauranteDAO();
        funcionarioDAO = new FuncionarioDAO();

        setTitle("Gerenciador de Cadastros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600); // Increased size for tabs
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Produto Tab ---
        JPanel produtoPanel = createProdutoPanel();
        tabbedPane.addTab("Produtos", produtoPanel);

        // --- Cliente Tab ---
        JPanel clientePanel = createClientePanel();
        tabbedPane.addTab("Clientes", clientePanel);

        // --- Restaurante Tab ---
        JPanel restaurantePanel = createRestaurantePanel();
        tabbedPane.addTab("Restaurantes", restaurantePanel);

        // --- Funcionario Tab ---
        JPanel funcionarioPanel = createFuncionarioPanel();
        tabbedPane.addTab("Funcionários", funcionarioPanel);


        add(tabbedPane);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                produtoDAO.salvarProdutos();
                clienteDAO.salvarClientes();
                restauranteDAO.salvarRestaurantes();
                funcionarioDAO.salvarFuncionarios();
                System.out.println("Todos os dados foram salvos.");
            }
        });

        refreshAllLists();
        setVisible(true);
    }

    // --- Panel Creation Methods ---

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


    private JPanel createRestaurantePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Restaurante"));
        inputPanel.add(new JLabel("ID:"));
        restIdField = new JTextField();
        inputPanel.add(restIdField);
        inputPanel.add(new JLabel("Nome:"));
        restNomeField = new JTextField();
        inputPanel.add(restNomeField);
        inputPanel.add(new JLabel("Endereço:"));
        restEnderecoField = new JTextField();
        inputPanel.add(restEnderecoField);
        inputPanel.add(new JLabel("Tipo de Cozinha:"));
        restTipoCozinhaField = new JTextField();
        inputPanel.add(restTipoCozinhaField);
        panel.add(inputPanel, BorderLayout.NORTH);

        restDisplayArea = new JTextArea(15, 40);
        restDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(restDisplayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton createButton = new JButton("Criar Restaurante");
        JButton updateButton = new JButton("Atualizar Restaurante");
        JButton deleteButton = new JButton("Excluir Restaurante");
        actionPanel.add(createButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> criarRestaurante());
        updateButton.addActionListener(e -> atualizarRestaurante());
        deleteButton.addActionListener(e -> excluirRestaurante());

        return panel;
    }

    private JPanel createFuncionarioPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));
        inputPanel.add(new JLabel("ID:"));
        funcIdField = new JTextField();
        inputPanel.add(funcIdField);
        inputPanel.add(new JLabel("Nome:"));
        funcNomeField = new JTextField();
        inputPanel.add(funcNomeField);
        inputPanel.add(new JLabel("Cargo:"));
        funcCargoField = new JTextField();
        inputPanel.add(funcCargoField);
        inputPanel.add(new JLabel("Salário:"));
        funcSalarioField = new JTextField();
        inputPanel.add(funcSalarioField);
        panel.add(inputPanel, BorderLayout.NORTH);

        funcDisplayArea = new JTextArea(15, 40);
        funcDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(funcDisplayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton createButton = new JButton("Criar Funcionário");
        JButton updateButton = new JButton("Atualizar Funcionário");
        JButton deleteButton = new JButton("Excluir Funcionário");
        actionPanel.add(createButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);

        createButton.addActionListener(e -> criarFuncionario());
        updateButton.addActionListener(e -> atualizarFuncionario());
        deleteButton.addActionListener(e -> excluirFuncionario());

        return panel;
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


    // --- CRUD Methods for Restaurante ---
    private void criarRestaurante() {
        try {
            String id = restIdField.getText().trim();
            String nome = restNomeField.getText().trim();
            String endereco = restEnderecoField.getText().trim();
            String tipoCozinha = restTipoCozinhaField.getText().trim();

            if (id.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID e Nome do Restaurante não podem ser vazios.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Restaurante r : restauranteDAO.read()) {
                if (r.getId().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Restaurante com este ID já existe.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Restaurante restaurante = new Restaurante(id, nome, endereco, tipoCozinha);
            restauranteDAO.create(restaurante);
            JOptionPane.showMessageDialog(this, "Restaurante criado com sucesso!");
            clearRestauranteFields();
            refreshRestauranteList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar restaurante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarRestaurante() {
        try {
            String id = restIdField.getText().trim();
            String nome = restNomeField.getText().trim();
            String endereco = restEnderecoField.getText().trim();
            String tipoCozinha = restTipoCozinhaField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Restaurante não pode ser vazio para atualização.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Restaurante restauranteAtualizado = new Restaurante(id, nome, endereco, tipoCozinha);
            restauranteDAO.update(restauranteAtualizado);
            JOptionPane.showMessageDialog(this, "Restaurante atualizado com sucesso!");
            clearRestauranteFields();
            refreshRestauranteList();
        } catch (RestauranteNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar restaurante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirRestaurante() {
        try {
            String id = restIdField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Restaurante não pode ser vazio para exclusão.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o restaurante com ID: " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                restauranteDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Restaurante excluído com sucesso!");
                clearRestauranteFields();
                refreshRestauranteList();
            }
        } catch (RestauranteNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir restaurante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearRestauranteFields() {
        restIdField.setText("");
        restNomeField.setText("");
        restEnderecoField.setText("");
        restTipoCozinhaField.setText("");
    }

    private void refreshRestauranteList() {
        restDisplayArea.setText("");
        List<Restaurante> restaurantes = restauranteDAO.read();
        if (restaurantes.isEmpty()) {
            restDisplayArea.append("Nenhum restaurante cadastrado.\n");
        } else {
            for (Restaurante r : restaurantes) {
                restDisplayArea.append(r.toString() + "\n");
            }
        }
    }


    // --- CRUD Methods for Funcionario ---
    private void criarFuncionario() {
        try {
            String id = funcIdField.getText().trim();
            String nome = funcNomeField.getText().trim();
            String cargo = funcCargoField.getText().trim();
            double salario = Double.parseDouble(funcSalarioField.getText().trim());

            if (id.isEmpty() || nome.isEmpty() || cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID, Nome e Cargo do Funcionário não podem ser vazios.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Funcionario f : funcionarioDAO.read()) {
                if (f.getId().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Funcionário com este ID já existe.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            Funcionario funcionario = new Funcionario(id, nome, cargo, salario);
            funcionarioDAO.create(funcionario);
            JOptionPane.showMessageDialog(this, "Funcionário criado com sucesso!");
            clearFuncionarioFields();
            refreshFuncionarioList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salário inválido. Por favor, digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar funcionário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarFuncionario() {
        try {
            String id = funcIdField.getText().trim();
            String nome = funcNomeField.getText().trim();
            String cargo = funcCargoField.getText().trim();
            double salario = Double.parseDouble(funcSalarioField.getText().trim());

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Funcionário não pode ser vazio para atualização.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Funcionario funcionarioAtualizado = new Funcionario(id, nome, cargo, salario);
            funcionarioDAO.update(funcionarioAtualizado);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            clearFuncionarioFields();
            refreshFuncionarioList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salário inválido. Por favor, digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (FuncionarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Atualização", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirFuncionario() {
        try {
            String id = funcIdField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID do Funcionário não pode ser vazio para exclusão.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o funcionário com ID: " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                funcionarioDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
                clearFuncionarioFields();
                refreshFuncionarioList();
            }
        } catch (FuncionarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir funcionário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFuncionarioFields() {
        funcIdField.setText("");
        funcNomeField.setText("");
        funcCargoField.setText("");
        funcSalarioField.setText("");
    }

    private void refreshFuncionarioList() {
        funcDisplayArea.setText("");
        List<Funcionario> funcionarios = funcionarioDAO.read();
        if (funcionarios.isEmpty()) {
            funcDisplayArea.append("Nenhum funcionário cadastrado.\n");
        } else {
            for (Funcionario f : funcionarios) {
                funcDisplayArea.append(f.toString() + "\n");
            }
        }
    }

    // --- Utility Methods ---
    private void refreshAllLists() {
        refreshProdutoList();
        refreshClienteList();
        refreshRestauranteList();
        refreshFuncionarioList();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GerenciadorEntidadesGUI::new);
    }
}