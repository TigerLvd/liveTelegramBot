package model.liveInfo.db;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "FIELD", schema = "live_ntc")
public class Field implements Serializable {
    private Long id;

    private String name;

    private String text;

    private String photoPath;

    public Long parentId;

    private List<Field> childes;

    public Field() {
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "TEXT")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Transient
    public boolean hasPhoto() {
        return null != getPhotoPath();
    }

    @Column(name = "PHOTO_PATH")
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoUrlPath) {
        this.photoPath = photoUrlPath;
    }

    @Column(name = "PARENT_ID")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    public List<Field> getChildes() {
        return childes;
    }

    public void setChildes(List<Field> childes) {
        this.childes = childes;
    }
}
