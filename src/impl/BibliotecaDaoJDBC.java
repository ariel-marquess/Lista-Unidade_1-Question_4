package impl;

import dao.BibliotecaDao;
import db.DB;
import exceptions.DbException;
import exceptions.EmprestimoInvalidoException;
import exceptions.LivroNaoEncontradoException;
import model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaDaoJDBC implements BibliotecaDao {
    private final Connection conn;

    public BibliotecaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void adicionar(Livro obj) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement("INSERT INTO livro(titulo, autor, estaOcupado) VALUES (?, ?, ?)");

            stmt.setString(1, obj.getTitulo());
            stmt.setString(2, obj.getAutor());
            stmt.setBoolean(3, obj.isEstaEmprestado());

            int row = stmt.executeUpdate();
            if (row > 0) {
                ResultSet rs = stmt.getGeneratedKeys();

                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
            } else {
                throw new DbException("ERRO: sem linhas afetadas no banco de dados.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
    }

    private Livro instantiateLivro(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));
        livro.setEstaEmprestado(rs.getBoolean("estaOcupado"));

        return livro;
    }

    @Override
    public List<Livro> buscar(String str, String operacao) throws SQLException, LivroNaoEncontradoException, InterruptedException {   // A operação revela se a pesquisa é por título ou por autor
        PreparedStatement ps;
        ResultSet rs;

        if (operacao.equals("autor")) {
            ps = conn.prepareStatement("SELECT * FROM livro WHERE autor = ?");
        } else if (operacao.equals("titulo")) {
            ps = conn.prepareStatement("SELECT * FROM livro WHERE titulo = ?");
        } else {
            throw new LivroNaoEncontradoException();
        }

        ps.setString(1, str);
        rs = ps.executeQuery();

        List<Livro> livros = new ArrayList<>();

        while (rs.next()) {
            livros.add(instantiateLivro(rs));
        }

        DB.closeServices(ps, rs);
        Thread.sleep(500);

        return livros;
    }

    @Override
    public void emprestar(Integer id) throws SQLException, EmprestimoInvalidoException, InterruptedException {    // Utilizo dessa forma, ao invés de utilizar o bloco try, para que a execução o controle de excessões seja excutado nas classes da pasta "application"
        PreparedStatement ps;
        ResultSet rs;

        ps = conn.prepareStatement("SELECT estaOcupado FROM livro WHERE id = ?");
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (rs.getInt("estaOcupado") == 0) {
            ps = conn.prepareStatement("UPDATE livro SET estaOcupado = ? WHERE id = ?");

            ps.setBoolean(1, true);
            ps.setInt(2, id);
            int row = ps.executeUpdate();

            DB.closeServices(ps, rs);
            Thread.sleep(500);     // Espera até que os recursos sejam realmente fechados

            if (!(row > 0)) {
                throw new DbException("ERRO: sem linhas afetadas no banco de dados.");
            }
        } else {
            DB.closeServices(ps, rs);
            Thread.sleep(500);    // Espera até que os recursos sejam realmente fechados

            throw new EmprestimoInvalidoException("livro ocupado");
        }

    }

    @Override
    public void devolver(Integer id) throws SQLException, InterruptedException {    // Mesma estrutura do método anterior
        PreparedStatement ps;
        ResultSet rs;

        ps = conn.prepareStatement("SELECT estaOcupado FROM livro WHERE id = ?");
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (rs.getInt("estaOcupado") == 1) {
            ps = conn.prepareStatement("UPDATE livro SET estaOcupado = ? WHERE id = ?");

            ps.setBoolean(1, false);
            ps.setInt(2, id);
            int row = ps.executeUpdate();

            DB.closeServices(ps, rs);
            Thread.sleep(500);     // Espera até que os recursos sejam realmente fechados

            if (!(row > 0)) {
                throw new DbException("ERRO: sem linhas afetadas no banco de dados.");
            }
        } else {
            DB.closeServices(ps, rs);   // Entendi a grande ajuda que o bloco finaly proporciona
            Thread.sleep(500);    // Espera até que os recursos sejas realmente fechados

            throw new DbException("ERRO: o livro indicado não está emprestado.");
        }
    }
}
