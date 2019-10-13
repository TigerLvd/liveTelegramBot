package model.homeGroups.db;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "statistics_info", schema = "live_ntc")
public class StatisticsInfo {
    private Long id;
    private Date eventDate;
    private int saverId;
    private Timestamp saveDate;
    private int count;
    private String comment;

    @Id
    @Column(name = "id")
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
    @Column(name = "saver_Id")
    public int getSaverId() {
        return saverId;
    }

    public void setSaverId(int saverId) {
        this.saverId = saverId;
    }

    @Basic
    @Column(name = "saveDate")
    public Timestamp getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Timestamp saveDate) {
        this.saveDate = saveDate;
    }

    @Basic
    @Column(name = "count")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticsInfo that = (StatisticsInfo) o;

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
