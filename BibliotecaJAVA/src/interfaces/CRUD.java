package interfaces;

import java.util.List;

public interface CRUD<T> {
    void adicionar(T t);
    void excluir(int id) throws Exception;
    List<T> listar();
}
