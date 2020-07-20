package demoprojects.demo.dao.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String title;
    private String description;
    private BigDecimal price;
    private User seller;
    private User buyer;
    private Set<ProductCategory> categories;
    private Set<ProductComment> comments;
    private LocalDateTime created;
    private LocalDateTime sold;


    public Product() {
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @Column(name = "created_on", nullable = false)
    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Column(name = "sold_on", nullable = false)
    public LocalDateTime getSold() {
        return sold;
    }

    public void setSold(LocalDateTime sold) {
        this.sold = sold;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    public Set<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<ProductCategory> categories) {
        this.categories = categories;
    }

    @OneToMany(mappedBy = "product", targetEntity = ProductComment.class,
            fetch = FetchType.EAGER)
    public Set<ProductComment> getComments() {
        return comments;
    }

    public void setComments(Set<ProductComment> comments) {
        this.comments = comments;
    }
}
