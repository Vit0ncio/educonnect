package data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Forum {
    private int id;
    private final String topico;
    private List<ForumPost> posts = new ArrayList<>();
    
    public Forum(String topico) {
        this.topico = topico;
        this.posts = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;   
    }
    
    public List<ForumPost> getPosts() {
        return posts;
    }
    public void setPosts(List<ForumPost> posts) {
        this.posts = posts;
    }
    
    public String getTopico() {
        return topico;
    }
    
    public void addPost(ForumPost post) {
        String mensagem = String.format("Um novo post sobre %s de %s está disponível",
            post.getTitulo(),
            post.getAutor().getNome());
        
        posts.add(post);
        JOptionPane.showMessageDialog(null, mensagem);
    }
}