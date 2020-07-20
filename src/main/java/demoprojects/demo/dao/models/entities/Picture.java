package demoprojects.demo.dao.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
    private String title;
    private String description;
    private FileFormat fileFormat;
    private String pictureName;
    private Set<PictureCategory> categories;
    private Set<PictureComment> comments;
    private User author;
    private Integer likes;

    public Picture() {
        this.likes = 0;
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

    @Column(name = "file_format", nullable = false)
    @Enumerated(EnumType.STRING)
    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    @Column(name = "pic_name", nullable = false)
    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pictures_categories",
            joinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    public Set<PictureCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<PictureCategory> categories) {
        this.categories = categories;
    }

    @OneToMany(mappedBy = "picture", targetEntity = PictureComment.class,
            fetch = FetchType.EAGER)
    public Set<PictureComment> getComments() {
        return comments;
    }

    public void setComments(Set<PictureComment> comments) {
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "likes")
    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
