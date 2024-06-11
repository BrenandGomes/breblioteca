package biblioteca;

import entidades.Livro;
import entidades.Usuario;
import excecoes.LivroNaoEncontradoException;
import excecoes.UsuarioNaoEncontradoException;
import excecoes.LivroIndisponivelException;
import servicos.LivroServico;
import servicos.UsuarioServico;
import servicos.EmprestimoServico;
import database.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Connection connection;
    private static LivroServico livroServico;
    private static UsuarioServico usuarioServico;
    private static EmprestimoServico emprestimoServico;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (!testarConexao()) {
            System.out.println("Erro: Não foi possível estabelecer conexão com o banco de dados. Verifique suas configurações e tente novamente.");
            return;
        }

        livroServico = new LivroServico(connection);
        usuarioServico = new UsuarioServico(connection);
        emprestimoServico = new EmprestimoServico(connection);

        int option;

        do {
            mostrarMenu();
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            try {
                switch (option) {
                    case 1:
                        adicionarLivro();
                        break;
                    case 2:
                        adicionarUsuario();
                        break;
                    case 3:
                        emprestarLivro();
                        break;
                    case 4:
                        devolverLivro();
                        break;
                    case 5:
                        excluirLivro();
                        break;
                    case 6:
                        excluirUsuario();
                        break;
                    case 7:
                        listarLivros();
                        break;
                    case 8:
                        listarUsuarios();
                        break;
                    case 9:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (option != 9);

        Database.close();
        scanner.close();
    }

    private static boolean testarConexao() {
        try {
            connection = Database.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexão ao banco de dados estabelecida com sucesso.");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void mostrarMenu() {
        System.out.println("Menu:");
        System.out.println("1. Adicionar Livro");
        System.out.println("2. Adicionar Usuário");
        System.out.println("3. Emprestar Livro");
        System.out.println("4. Devolver Livro");
        System.out.println("5. Excluir Livro");
        System.out.println("6. Excluir Usuário");
        System.out.println("7. Listar Livros");
        System.out.println("8. Listar Usuários");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void adicionarLivro() {
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("Categoria ID: ");
        int categoriaId = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Quantidade Disponível: ");
        int quantidade = scanner.nextInt();

        Livro livro = new Livro(0, titulo, autor, categoriaId, isbn, quantidade);
        livroServico.adicionar(livro);
        System.out.println("Livro adicionado com sucesso.");
    }

    private static void adicionarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = new Usuario(0, nome, email, senha);
        usuarioServico.adicionar(usuario);
        System.out.println("Usuário adicionado com sucesso.");
    }

    private static void emprestarLivro() {
        System.out.print("ID do Usuário: ");
        int usuarioId = scanner.nextInt();
        System.out.print("ID do Livro: ");
        int livroId = scanner.nextInt();

        try {
            emprestimoServico.emprestar(usuarioId, livroId);
            System.out.println("Livro emprestado com sucesso.");
        } catch (LivroIndisponivelException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void devolverLivro() {
        System.out.print("ID do Empréstimo: ");
        int emprestimoId = scanner.nextInt();

        emprestimoServico.devolver(emprestimoId);
        System.out.println("Livro devolvido com sucesso.");
    }

    private static void excluirLivro() {
        System.out.print("ID do Livro: ");
        int livroId = scanner.nextInt();

        try {
            livroServico.excluir(livroId);
            System.out.println("Livro excluído com sucesso.");
        } catch (LivroNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void excluirUsuario() {
        System.out.print("ID do Usuário: ");
        int usuarioId = scanner.nextInt();

        try {
            usuarioServico.excluir(usuarioId);
            System.out.println("Usuário excluído com sucesso.");
        } catch (UsuarioNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void listarLivros() {
        List<Livro> livros = livroServico.listar();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            for (Livro livro : livros) {
                System.out.println(
                    "ID: " + livro.getId() +
                    " | Título: " + livro.getTitulo() +
                    " | Autor: " + livro.getAutor() +
                    " | Categoria ID: " + livro.getCategoriaId() +
                    " | ISBN: " + livro.getIsbn() +
                    " | Quantidade Disponível: " + livro.getQuantidadeDisponivel()
                );
            }
        }
    }

    private static void listarUsuarios() {
        List<Usuario> usuarios = usuarioServico.listar();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário encontrado.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println(
                    "ID: " + usuario.getId() +
                    " | Nome: " + usuario.getNome() +
                    " | Email: " + usuario.getEmail() +
                    " | Data de Cadastro: " + usuario.getDataCadastro()
                );
            }
        }
    }
}
