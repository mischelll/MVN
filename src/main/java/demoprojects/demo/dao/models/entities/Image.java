package demoprojects.demo.dao.models.entities;

import javax.persistence.*;

@Entity
@Table(name="uploaded_images")
public class Image extends BaseEntity {
    private String name;
    private FileFormat format;
    private String imgUrl;

    public Image() {
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    @Enumerated(EnumType.STRING)
    public FileFormat getFormat() {
        return format;
    }

    public void setFormat(FileFormat format) {
        this.format = format;
    }

    @Column(name = "img_url")
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
