package demoprojects.demo.dao.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products_comments")
public class ProductComment extends BaseEntity {
    private User author;
    private Product product;
    private LocalDateTime date;
    private String text;
    private Integer likes;
    private Integer dislikes;

    public ProductComment() {
        this.likes = 0;
        this.dislikes = 0;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
}