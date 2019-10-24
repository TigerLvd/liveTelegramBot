package model.homeGroups.db;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STAT_INFO", schema = "live_ntc")
public class StatInfo {
    private Long id;
    private Date eventDate;
    private int saverId;
    private Timestamp saveDate;
    private HomeGroup homeGroup;
    private int count;
    private String comment;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "EVENT_DATE")
    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    @Basic
    @Column(name = "SAVER_ID")
    public int getSaverId() {
        return saverId;
    }

    public void setSaverId(int saverId) {
        this.saverId = saverId;
    }

    @Basic
    @Column(name = "SAVE_DATE")
    public Timestamp getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Timestamp saveDate) {
        this.saveDate = saveDate;
    }

    @Basic
    @Column(name = "COUNT")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Basic
    @Column(name = "COMMENT")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HOME_GROUP_ID")
    public HomeGroup getHomeGroup() {
        return homeGroup;
    }

    public void setHomeGroup(HomeGroup homeGroup) {
        this.homeGroup = homeGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatInfo that = (StatInfo) o;

        if (id != that.id) return false;
        if (saverId != that.saverId) return false;
        if (count != that.count) return false;
        if (eventDate != null ? !eventDate.equals(that.eventDate) : that.eventDate != null) return false;
        if (saveDate != null ? !saveDate.equals(that.saveDate) : that.saveDate != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (eventDate != null ? eventDate.hashCode() : 0);
        result = 31 * result + saverId;
        result = 31 * result + (saveDate != null ? saveDate.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
