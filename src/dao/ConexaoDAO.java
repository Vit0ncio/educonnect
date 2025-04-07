package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConexaoDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/educonnect";
    private static final String USER = "root";
    private static final String PASSWORD = "12032008_Vitor";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException cnfe) {
            throw new SQLException("Driver JDBC não encontrado.", cnfe);
        }
    }
    
    public static void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + sqle.getMessage());
            }
        }
    }
    
    public static void closeConnection(Connection c, PreparedStatement ps) {
        closeConnection(c);
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar statement: " + sqle.getMessage());
            }
        }
    }
    
    public static void closeConnection(Connection c, PreparedStatement ps, ResultSet rs) {
        closeConnection(c, ps);
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar statement: " + sqle.getMessage());
            }
        }
    }
}