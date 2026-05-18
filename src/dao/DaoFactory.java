package dao;

import db.DB;
import impl.BibliotecaDaoJDBC;

public class DaoFactory {
    public static BibliotecaDao createBibliotecaDao() {
        return new BibliotecaDaoJDBC(DB.getConnection());
    }
}
