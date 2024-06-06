package entidades;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime dataCadastro;

    public Usuario(int id, String nome, String email, String senha, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
    }

    public Usuario(int id, String nome, String email, String senha) {
        this(id, nome, email, senha, LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
