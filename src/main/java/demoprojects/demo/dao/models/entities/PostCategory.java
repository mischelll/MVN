package demoprojects.demo.dao.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
public class PostCategory extends BaseEntity {
    private PostCategoryName name;
    private String description;
    private Set<Post> posts;

    public PostCategory() {
    }
    public PostCategory(PostCategoryName name, String description) {
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
    public PostCategoryName getName() {
        return name;
    }

    public void setName(PostCategoryName name) {
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
