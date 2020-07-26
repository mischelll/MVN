package demoprojects.demo.dao.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
public class PostCategory extends BaseEntity {
    private PostCategoryName name;
    private String description;


    public PostCategory() {
    }
    public PostCategory(PostCategoryName name, String description) {
        this.name = name;
        this.description = description;
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
