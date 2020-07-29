package demoprojects.demo.dao.models.entities;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class PostComment extends BaseEntity {
    private String username;
    private Post post;
    private LocalDateTime date;
    private String text;
    private Integer likes;
    private Integer dislikes;

    public PostComment() {
        this.likes = 0;
        this.dislikes = 0;
    }

    @Column(name = "likes")
    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Column(name = "dislikes")
    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }


    @ManyToOne(cascade = {CascadeType.REMOVE},fetch = FetchType.EAGER)
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Column
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Column
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "author_username",nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String userID) {
        this.username = userID;
    }
}
