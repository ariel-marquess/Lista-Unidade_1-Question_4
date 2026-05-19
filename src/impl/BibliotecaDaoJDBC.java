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
    public void closeConnection() throws SQLException {
        if (this.conn != null) {
            this.conn.close();
        }
    }

    @Override
    public void adicionar(Livro obj) throws SQLException, DbException {
        PreparedStatement st;

        st = conn.prepareStatement(
                "INSERT INTO livro(titulo, autor, estaOcupado) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        st.setString(1, obj.getTitulo());
        st.setString(2, obj.getAutor());
        st.setBoolean(3, obj.isEstaEmprestado());

        int row = st.executeUpdate();
        if (row > 0) {
            ResultSet rs = st.getGeneratedKeys();

            if (rs.next()) {
                obj.setId(rs.getInt(1));
            }

            DB.closeServices(st, rs);
        } else {
            DB.closeStatement(st);
            throw new DbException("ERRO: sem linhas afetadas no banco de dados.");
        }
    }

    private Livro instantiateLivro(ResultSet rs) throws SQLException {
        return new Livro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"), rs.getBoolean("estaOcupado"));
    }

    @Override
    public List<Livro> buscar(String str, char operacao) throws SQLException, LivroNaoEncontradoException, DbException {   // A operação revela se a pesquisa é por título ou por autor
        PreparedStatement ps;
        ResultSet rs;

        if (operacao == 'a') {
            ps = conn.prepareStatement("SELECT * FROM livro WHERE autor = ?");
        } else if (operacao == 't') {
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
        return livros;
    }

    @Override
    public void emprestar(Integer id) throws SQLException, EmprestimoInvalidoException, DbException {    // Utilizo dessa forma, ao invés de utilizar o bloco try, para que a execução o controle de excessões seja excutado nas classes da pasta "application"
        PreparedStatement ps;
        ResultSet rs;

        ps = conn.prepareStatement("SELECT estaOcupado FROM livro WHERE id = ?");
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (rs.next()) {
            if (rs.getInt("estaOcupado") == 0) {
                DB.closeStatement(ps);
                ps = conn.prepareStatement("UPDATE livro SET estaOcupado = ? WHERE id = ?");

                ps.setBoolean(1, true);
                ps.setInt(2, id);
                int row = ps.executeUpdate();

                DB.closeServices(ps, rs);
                if (!(row > 0)) {
                    throw new DbException("ERRO: sem linhas afetadas no banco de dados.");
                }
            } else {
                DB.closeServices(ps, rs);
                throw new EmprestimoInvalidoException("livro ocupado");
            }
        } else {
            DB.closeServices(ps, rs);
            throw new DbException("Livro não encontrado com ID: " + id);
        }

    }

    @Override
    public void devolver(Integer id) throws SQLException, DbException {    // Mesma estrutura do método anterior
        PreparedStatement ps;
        ResultSet rs;

        ps = conn.prepareStatement("SELECT estaOcupado FROM livro WHERE id = ?");
        ps.setInt(1, id);
        rs = ps.executeQuery();

        if (rs.next()) {
            if (rs.getInt("estaOcupado") == 1) {
                DB.closeStatement(ps);
                ps = conn.prepareStatement("UPDATE livro SET estaOcupado = ? WHERE id = ?");

                ps.setBoolean(1, false);
                ps.setInt(2, id);
                int row = ps.executeUpdate();

                DB.closeServices(ps, rs);
                if (!(row > 0)) {
                    throw new DbException("ERRO: sem linhas afetadas no banco de dados.");
                }
            } else {
                DB.closeServices(ps, rs);   // Entendi a grande ajuda que o bloco finaly proporciona
                throw new DbException("ERRO: o livro indicado não está emprestado.");
            }
        } else {
            DB.closeServices(ps, rs);
            throw new DbException("Livro não encontrado com ID: " + id);
        }
    }

    @Override
    public List<Livro> listarAcervo() throws SQLException, DbException {
        List<Livro> livros = new ArrayList<>();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM livro");

        while (rs.next()) {
            livros.add(instantiateLivro(rs));
        }

        DB.closeServices(st, rs);
        return livros;
    }
}
