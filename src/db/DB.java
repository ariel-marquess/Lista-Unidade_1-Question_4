package db;

import exceptions.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    public static Connection conn = null;

    public static Connection getConnection() throws DbException {
        if(conn == null){
            try {
                Properties prop = loadProperties();

                String url = prop.getProperty("dburl");
                conn = DriverManager.getConnection(url, prop);

            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }

        return conn;
    }

    public static void closeConnection() throws DbException {
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static Properties loadProperties() throws DbException {
        try (FileInputStream fs = new FileInputStream("db.properties")){
            Properties prop = new Properties();

            prop.load(fs);

            return  prop;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement(Statement stmt) throws DbException {
        if (stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) throws DbException {
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeServices(Statement st, ResultSet rs) throws DbException {
        if (st != null) closeStatement(st);
        if (rs != null) closeResultSet(rs);
    }
}
