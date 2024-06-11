package servicos;

import database.Database;
import entidades.Emprestimo;
import excecoes.LivroIndisponivelException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoServico {
    private Connection connection;

    public EmprestimoServico(Connection connection) {
        this.connection = connection;
    }

    public void emprestar(int usuarioId, int livroId) throws LivroIndisponivelException {
        try {
            String checkAvailabilitySql = "SELECT quantidade_disponivel FROM livros WHERE id_livro = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilitySql);
            checkStmt.setInt(1, livroId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int quantidadeDisponivel = rs.getInt("quantidade_disponivel");
                if (quantidadeDisponivel <= 0) {
                    throw new LivroIndisponivelException("Esse livro não tem exemplares disponíveis.");
                }
            }

            String insertSql = "INSERT INTO emprestimos (id_usuario, id_livro) VALUES (?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setInt(1, usuarioId);
            insertStmt.setInt(2, livroId);
            insertStmt.executeUpdate();

            String updateSql = "UPDATE livros SET quantidade_disponivel = quantidade_disponivel - 1 WHERE id_livro = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setInt(1, livroId);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void devolver(int emprestimoId) {
        try {
            // Recuperar o livro associado ao empréstimo
            String selectSql = "SELECT id_livro FROM emprestimos WHERE id_emprestimo = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
            selectStmt.setInt(1, emprestimoId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int livroId = rs.getInt("id_livro");

                // Atualizar a quantidade disponível do livro
                String updateSql = "UPDATE livros SET quantidade_disponivel = quantidade_disponivel + 1 WHERE id_livro = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setInt(1, livroId);
                updateStmt.executeUpdate();

                // Marcar o empréstimo como devolvido
                String updateEmprestimoSql = "UPDATE emprestimos SET data_devolucao = CURRENT_TIMESTAMP WHERE id_emprestimo = ?";
                PreparedStatement updateEmprestimoStmt = connection.prepareStatement(updateEmprestimoSql);
                updateEmprestimoStmt.setInt(1, emprestimoId);
                updateEmprestimoStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM emprestimos WHERE data_devolucao IS NULL";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id_emprestimo");
                int usuarioId = rs.getInt("id_usuario");
                int livroId = rs.getInt("id_livro");
                Timestamp dataEmprestimo = rs.getTimestamp("data_emprestimo");
                Timestamp dataDevolucao = rs.getTimestamp("data_devolucao");
                Emprestimo emprestimo = new Emprestimo(id, usuarioId, livroId, dataEmprestimo, dataDevolucao);
                emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprestimos;
    }
}

