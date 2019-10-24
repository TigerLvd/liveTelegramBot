package model.homeGroups.db;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import model.homeGroups.DayOfTheWeek;

@Entity
@Table(name = "HOME_GROUP", schema = "live_ntc")
public class HomeGroup {
    public static final String LIEDER_FIELD = "lieder";

    private Long id;

    private User lieder;

    private String liederPhoneNumber;

    private String address;

    private DayOfTheWeek dayOfTheWeek = DayOfTheWeek.TU;

    private Time time = new Time(19, 0, 0);

    private String comment;

    private Date startDate;

    public HomeGroup() {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIEDER_ID")
    public User getLieder() {
        return lieder;
    }

    public void setLieder(User lieder) {
        this.lieder = lieder;
    }

    @Column(name = "LIEDER_PHONE_NUMBER")
    public String getLiederPhoneNumber() {
        return liederPhoneNumber;
    }

    public void setLiederPhoneNumber(String liederPhoneNumber) {
        this.liederPhoneNumber = liederPhoneNumber;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "DAY_OF_THE_WEEK")
    @Enumerated(EnumType.STRING)
    public DayOfTheWeek getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(DayOfTheWeek dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    @Column(name = "TIME")
    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Column(name = "COMMENT")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "HomeGroup{" +
                "id=" + id +
                ", liederPhoneNumber='" + liederPhoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", dayOfTheWeek=" + dayOfTheWeek.getTitle() +
                ", time=" + time +
                '}';
    }
}
