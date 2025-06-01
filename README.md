# Talles-Project

## Descrição

Este projeto é um sistema de cadastro visual (CRUD) desenvolvido em Java utilizando Swing para a interface gráfica e serialização para persistência dos dados. Ele permite gerenciar quatro tipos de entidades:

- **Produtos**
- **Clientes**
- **Animes**
- **Funcionários**

Cada entidade possui seu próprio CRUD completo (Criar, Ler, Atualizar, Excluir).

---

## Funcionalidades

- **Interface Gráfica:**  
  O sistema utiliza Java Swing, com abas separadas para cada entidade, facilitando a navegação e o uso.

- **CRUD Completo:**  
  Para cada entidade é possível:
  - Cadastrar novos registros
  - Listar todos os registros cadastrados
  - Atualizar registros existentes
  - Excluir registros

- **Persistência de Dados:**  
  Todos os dados são salvos em arquivos locais utilizando serialização de objetos (`.ser`), garantindo que as informações não sejam perdidas ao fechar o programa.

- **Tratamento de Erros:**  
  O sistema exibe mensagens de erro amigáveis para entradas inválidas, duplicidade de IDs, e operações inválidas.

---

## Estrutura do Código

- **Entidades:**  
  - `Produto`, `Cliente`, `Anime`, `Funcionario`  
  Cada classe representa uma entidade do sistema e implementa a interface `Identifiable`.

- **DAOs:**  
  - `ProdutoDAO`, `ClienteDAO`, `AnimeDAO`, `FuncionarioDAO`  
  Responsáveis por gerenciar as listas de objetos, salvar e carregar os dados dos arquivos, e realizar as operações CRUD.

- **Exceções Personalizadas:**  
  Exceções específicas para cada entidade, facilitando o tratamento de erros.

- **Interface Gráfica:**  
  - Classe principal: `GerenciadorEntidadesGUI`  
  Responsável por montar a janela principal, abas, campos de entrada, áreas de exibição e botões de ação.

---

## Como Executar

1. **Requisitos:**  
   - Java 8 ou superior

2. **Compilação:**  
   Compile o projeto com sua IDE, Terminal ainda apresenta erros de compilação
---

## Observações

- Os dados são salvos automaticamente ao fechar a janela do programa.
- Os arquivos de dados são criados na mesma pasta do projeto (`produtos.ser`, `clientes.ser`, `anime.txt`, `funcionarios.ser`).
- O sistema foi desenvolvido para fins didáticos, cobrindo tópicos como: orientação a objetos, interface gráfica, tratamento de exceções, serialização, encapsulamento, polimorfismo e uso de interfaces.

---

## Autor

Projeto desenvolvido para fins acadêmicos.
