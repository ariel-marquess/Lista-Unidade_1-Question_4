package dao;

import db.DB;
import exceptions.DbException;
import impl.BibliotecaDaoJDBC;

public class DaoFactory {
    public static BibliotecaDao createBibliotecaDao() throws DbException {
        return new BibliotecaDaoJDBC(DB.getConnection());
    }
}
