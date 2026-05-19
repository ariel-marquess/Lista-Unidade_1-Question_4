package dao;

import exceptions.DbException;
import exceptions.EmprestimoInvalidoException;
import exceptions.LivroNaoEncontradoException;
import model.Livro;

import java.sql.SQLException;
import java.util.List;

public interface BibliotecaDao {
    void adicionar(Livro livro) throws SQLException, EmprestimoInvalidoException, DbException;
    List<Livro> buscar(String str, char operacao) throws EmprestimoInvalidoException, SQLException, LivroNaoEncontradoException, DbException;
    void emprestar(Integer id) throws SQLException, EmprestimoInvalidoException, DbException;
    void devolver(Integer id) throws SQLException, DbException;
    List<Livro> listarAcervo() throws SQLException, DbException;
    void closeConnection() throws SQLException;
}
