package model.homegroups.db;

import model.homegroups.DayOfTheWeek;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "HOME_GROUP", schema = "live_ntc")
public class HomeGroup implements HasComment, HasId{
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeGroup homeGroup = (HomeGroup) o;
        return Objects.equals(id, homeGroup.id) &&
                Objects.equals(liederPhoneNumber, homeGroup.liederPhoneNumber) &&
                Objects.equals(address, homeGroup.address) &&
                dayOfTheWeek == homeGroup.dayOfTheWeek &&
                Objects.equals(time, homeGroup.time) &&
                Objects.equals(comment, homeGroup.comment) &&
                Objects.equals(startDate, homeGroup.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, liederPhoneNumber, address, dayOfTheWeek, time, comment, startDate);
    }
}
