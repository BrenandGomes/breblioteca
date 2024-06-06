package servicos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmprestimoServico {
    private final Connection connection;

    public EmprestimoServico(Connection connection) {
        this.connection = connection;
    }

    public void emprestarLivro(int usuarioId, int livroId) {
        String sql = "INSERT INTO emprestimos (id_usuario, id_livro) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, livroId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void devolverLivro(int emprestimoId) {
        String sql = "UPDATE emprestimos SET data_devolucao = CURRENT_TIMESTAMP WHERE id_emprestimo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, emprestimoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
