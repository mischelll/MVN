package demoprojects.demo.dao.models.entities;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "prcategories")
public class ProductCategory extends BaseEntity {
    private ProductCategoryName name;
    private String description;
    private Set<Product> products;

    public ProductCategory() {
    }

    public ProductCategory(ProductCategoryName name, String description) {
        this.name = name;
        this.description = description;
    }

    @Column(name = "category", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    public ProductCategoryName getName() {
        return name;
    }

    public void setName(ProductCategoryName name) {
        this.name = name;
    }
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @ManyToMany(mappedBy = "categories",targetEntity = Product.class, fetch = FetchType.EAGER)
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
