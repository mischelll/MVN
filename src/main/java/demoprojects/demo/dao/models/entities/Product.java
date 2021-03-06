package demoprojects.demo.dao.models.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private String title;
    private String description;
    private String preview;
    private Integer views;
    private BigDecimal price;
    private User seller;
    private User buyer;
    private String telephoneNumber;
    private Set<ProductCategory> categories;
    private Set<ProductComment> comments;
    private List<Image> productImages;
    private LocalDateTime created;
    private LocalDateTime sold;
    private Boolean isSold;

    public Product() {
        this.views = 0;
        this.isSold = false;
    }


    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @OneToMany(targetEntity = Image.class, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<Image> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<Image> productImages) {
        this.productImages = productImages;
    }

    @Lob
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "views")
    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @Column(name = "sold_on")
    public LocalDateTime getSold() {
        return sold;
    }

    public void setSold(LocalDateTime sold) {
        this.sold = sold;
    }

    @Column(name = "is_sold")
    public Boolean getIsSold() {
        return this.isSold;
    }

    public void setIsSold(Boolean isSold) {
        this.isSold = isSold;
    }

    @ManyToOne
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


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
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

    @Column(name = "preview", nullable = false)
    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Column(name = "contact_number", nullable = false)
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
