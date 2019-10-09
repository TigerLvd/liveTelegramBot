package model.homeGroups;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;

@Entity
@Table(name = "LIEDER")
public class Lieder extends User {

    private String phoneNumber;

    private String address;

    private DayOfTheWeek dayOfTheWeek = DayOfTheWeek.TU;

    private Time time = new Time(19, 0, 0);

    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "DAY_OF_THE_WEEK")
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

    public Lieder(String firstName, String lastName, String userName, Long userId) {
        super(firstName, lastName, userName, userId);
    }

    public Lieder() {
    }
}
