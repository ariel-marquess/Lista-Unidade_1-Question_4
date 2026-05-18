package dao;

import exceptions.EmprestimoInvalidoException;
import exceptions.LivroNaoEncontradoException;
import model.Livro;

import java.sql.SQLException;
import java.util.List;

public interface BibliotecaDao {
    void adicionar(Livro livro);
    List<Livro> buscar(String str, String operacao) throws EmprestimoInvalidoException, SQLException, LivroNaoEncontradoException, InterruptedException;
    void emprestar(Integer id) throws SQLException, EmprestimoInvalidoException, InterruptedException;
    void devolver(Integer id) throws SQLException, InterruptedException;
}
