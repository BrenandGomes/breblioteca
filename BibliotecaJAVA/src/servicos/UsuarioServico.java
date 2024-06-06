package servicos;

import entidades.Usuario;
import excecoes.UsuarioNaoEncontradoException;
import interfaces.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioServico implements CRUD<Usuario> {
    private final Connection connection;

    public UsuarioServico(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void adicionar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, data_cadastro) VALUES (?, ?, ?, DEFAULT)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluir(int id) throws UsuarioNaoEncontradoException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getTimestamp("data_cadastro").toLocalDateTime()
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}
