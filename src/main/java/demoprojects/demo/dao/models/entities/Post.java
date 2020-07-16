package demoprojects.demo.dao.models.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post extends BaseEntity {
    private String title;
    private String preview;
    private String content;
    private User author;
    private String imgUrl;
    private Set<Category> categories;
    private Set<Comment> comments;
    private LocalDateTime postedOn;

    public Post() {
    }

    @Column(nullable = false, name = "imgUrl")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Column(nullable = false, name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false, name = "preview")
    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Lob
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "posted_on", nullable = false)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent(message = "The date cannot be in the future")
    public LocalDateTime getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(LocalDateTime postedOn) {
        this.postedOn = postedOn;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "posts_categories",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> category) {
        this.categories = category;
    }

    @OneToMany(mappedBy = "post", targetEntity = Comment.class,
            fetch = FetchType.EAGER)
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
