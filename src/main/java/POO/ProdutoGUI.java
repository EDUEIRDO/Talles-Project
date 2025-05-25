package POO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;

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

class ProdutoNaoEncontradoException extends Exception {
    public ProdutoNaoEncontradoException(String message) {
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

public class ProdutoGUI extends JFrame {
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private JTextField idField;
    private JTextField nomeField;
    private JTextField precoField;
    private JTextArea displayArea;
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;

    public ProdutoGUI() {
        setTitle("Gerenciamento de Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        inputPanel.add(nomeField);
        inputPanel.add(new JLabel("Preço:"));
        precoField = new JTextField();
        inputPanel.add(precoField);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        createButton = new JButton("Criar Produto");
        updateButton = new JButton("Atualizar Produto");
        deleteButton = new JButton("Excluir Produto");

        actionPanel.add(createButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarProduto();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarProduto();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                produtoDAO.salvarProdutos();
            }
        });

        refreshProductList();
        setVisible(true);
    }
    private void criarProduto() {
        try {
            String id = idField.getText().trim();
            String nome = nomeField.getText().trim();
            double preco = Double.parseDouble(precoField.getText().trim());

            if (id.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID e Nome não podem ser vazios.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
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
            clearFields();
            refreshProductList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Por favor, digite um número.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        try {
            String id = idField.getText().trim();
            String nome = nomeField.getText().trim();
            double preco = Double.parseDouble(precoField.getText().trim());

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID não pode ser vazio para atualização.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto produtoAtualizado = new Produto(id, nome, preco);
            produtoDAO.update(produtoAtualizado);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            clearFields();
            refreshProductList();
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
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID não pode ser vazio para exclusão.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto com ID: " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                produtoDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                clearFields();
                refreshProductList();
            }
        } catch (ProdutoNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nomeField.setText("");
        precoField.setText("");
    }

    private void refreshProductList() {
        displayArea.setText("");
        List<Produto> produtos = produtoDAO.read();
        if (produtos.isEmpty()) {
            displayArea.append("Nenhum produto cadastrado.\n");
        } else {
            for (Produto p : produtos) {
                displayArea.append(p.toString() + "\n");
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(ProdutoGUI::new);
    }
}
