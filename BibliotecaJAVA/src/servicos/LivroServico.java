package servicos;

import entidades.Livro;
import excecoes.LivroNaoEncontradoException;
import interfaces.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroServico implements CRUD<Livro> {
    private final Connection connection;

    public LivroServico(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void adicionar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, categoria_id, isbn, quantidade_disponivel) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getCategoriaId());
            stmt.setString(4, livro.getIsbn());
            stmt.setInt(5, livro.getQuantidadeDisponivel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(int id) throws LivroNaoEncontradoException {
        String sql = "DELETE FROM livros WHERE id_livro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LivroNaoEncontradoException("Livro não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Livro> listar() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id_livro"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("categoria_id"),
                    rs.getString("isbn"),
                    rs.getInt("quantidade_disponivel")
                );
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }
}
