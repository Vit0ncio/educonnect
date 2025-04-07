package dao;

import data.ForumPost;
import data.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ForumPostDAO {
     public void adicionarPost(ForumPost post) {
        Connection c = null;
        PreparedStatement ps = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "INSERT INTO posts_forum (topico_id, autor_id, titulo, conteudo, fixado, upvotes, favoritado) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, post.getForum().getId());
            ps.setInt(2, post.getAutor().getId());
            ps.setString(3, post.getTitulo());
            ps.setString(4, post.getDescricao());
            ps.setBoolean(5, post.isFixado());
            ps.setInt(6, post.getUpvotes());
            ps.setBoolean(7, post.isFavoritado());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar post: " + sqle.getMessage());
        } finally {
            ConexaoDAO.closeConnection(c, ps);
        }
    }
     
    public List<ForumPost> listarPostsPorForum(int forumId) {
        List<ForumPost> posts = new ArrayList<>();
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            c = ConexaoDAO.getConnection();
            String sql = "SELECT p.*, u.nome as autor_nome FROM posts_forum p " +
                         "JOIN usuarios u ON p.autor_id = u.id " +
                         "WHERE p.topico_id = ?";
            ps = c.prepareStatement(sql);
            ps.setInt(1, forumId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ForumPost post = new ForumPost();
                post.setId(rs.getInt("id"));
                post.setTitulo(rs.getString("titulo"));
                post.setDescricao(rs.getString("conteudo"));
                post.setFixado(rs.getBoolean("fixado"));
                post.setUpvotes(rs.getInt("upvotes"));
                post.setFavoritado(rs.getBoolean("favoritado"));
                
                // Criar objeto Pessoa básico para o autor
                Pessoa autor = new Pessoa(
                    0,
                    rs.getString("autor_nome"),
                    0,
                    "",
                    ""
                );
                autor.setId(rs.getInt("autor_id"));
                post.setAutor(autor);
                
                posts.add(post);
            }
            
            return posts;
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Erro ao listar posts: " + sqle.getMessage());
            return posts;
        } finally {
            ConexaoDAO.closeConnection(c, ps, rs);
        }
    }
}