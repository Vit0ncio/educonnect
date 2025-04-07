package dao;

import data.Forum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ForumDAO {
    public void adicionarForum(Forum forum) {
        Connection c = null;
        PreparedStatement ps = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "INSERT INTO topicos_forum (titulo, descricao) VALUES (?, ?)";
            ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, forum.getTopico());
            ps.setString(2, "");
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        forum.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar fórum: " + sqle.getMessage());
        } finally {
            ConexaoDAO.closeConnection(c, ps);
        }
    }
    
    public List<Forum> listarTodosForuns() {
        List<Forum> forums = new ArrayList<>();
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "SELECT * FROM topicos_forum";
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Forum forum = new Forum(rs.getString("titulo"));
                forum.setId(rs.getInt("id"));
                forums.add(forum);
            }
            
            // Carregar posts para cada fórum
            ForumPostDAO postDAO = new ForumPostDAO();
            for (Forum forum : forums) {
                forum.setPosts(postDAO.listarPostsPorForum(forum.getId()));
            }
            
            return forums;
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Erro ao listar fóruns: " + sqle.getMessage());
            return forums;
        } finally {
            ConexaoDAO.closeConnection(c, ps, rs);
        }
    }
}