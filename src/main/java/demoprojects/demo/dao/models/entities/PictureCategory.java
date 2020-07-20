package demoprojects.demo.dao.models.entities;

import javax.persistence.*;
import java.util.Set;
@Entity
@Table(name = "picategories")
public class PictureCategory extends BaseEntity{
    private PictureCategoryName name;
    private String description;
    private Set<Picture> pictures;

    public PictureCategory() {
    }

    public PictureCategory(PictureCategoryName name, String description) {
        this.name = name;
        this.description = description;
    }

    @Column(name = "category", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    public PictureCategoryName getName() {
        return name;
    }

    public void setName(PictureCategoryName name) {
        this.name = name;
    }
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @ManyToMany(mappedBy = "categories",targetEntity = Picture.class)
    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }
}
