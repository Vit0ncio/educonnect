package dao;

import data.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class PessoaDAO {
    public boolean salvar(Pessoa pessoa, String tipoUsuario) {
        Connection c = null;
        PreparedStatement ps = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "insert into usuarios (nome, email, idade, senha, tipo) values (?, ?, ?, ?, ?)";
            ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEmail());
            ps.setInt(3, pessoa.getIdade());
            ps.setString(4, pessoa.getSenha());
            ps.setString(5, tipoUsuario);
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                    if (generatedKeys.next()) {
                        pessoa.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar usuário: " + sqle.getMessage());
            return false;
        } finally {
            ConexaoDAO.closeConnection(c, ps);
        }
    }
    
    public static Pessoa login(String email, String senha) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "select * from usuarios where email = ? and senha = ?";
            ps = c.prepareStatement(sql);
            
            ps.setString(1, email);
            ps.setString(2, senha);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Pessoa pessoa = new Pessoa(
                        rs.getInt("id"),
                        rs.getString("nome"), 
                        rs.getInt("idade"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
                return pessoa;
            }
            return null;
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Erro ao fazer login.");
            return null;
        } finally {
            ConexaoDAO.closeConnection(c, ps, rs);
        }
    }
    
    public Pessoa buscarPorEmail(String email) {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "select * from usuarios where email = ?";
            ps = c.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Pessoa pessoa = new Pessoa(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("idade"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
                return pessoa;
            }
        } catch (SQLException sqle) {
             JOptionPane.showMessageDialog(null, "Erro ao buscar usuário por email: " + sqle.getMessage());
        } finally {
            ConexaoDAO.closeConnection(c, ps, rs);
        }
        return null;
    }
}