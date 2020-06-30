package demoprojects.demo.dao.models.entities;

import com.fasterxml.jackson.databind.ser.Serializers;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    private CategoryName name;
    private String description;
    private Set<Post> posts;

    public Category() {
    }
    public Category(CategoryName name, String description) {
        this.name = name;
        this.description = description;
    }

    @ManyToMany(mappedBy = "categories",targetEntity = Post.class)
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Column(name = "category", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    public CategoryName getName() {
        return name;
    }

    public void setName(CategoryName name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
