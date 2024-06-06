package entidades;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int categoriaId;
    private String isbn;
    private int quantidadeDisponivel;

    public Livro(int id, String titulo, String autor, int categoriaId, String isbn, int quantidadeDisponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoriaId = categoriaId;
        this.isbn = isbn;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
}
