package model.homeGroups.db;

import utils.Utils;

import java.util.Objects;

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
@Table(name = "USER", schema = "live_ntc")
public class User implements HasComment, HasId {

    private Long id;

    private boolean admin;

    private boolean leader;

    private String firstName;

    private String lastName;

    private HomeGroup homeGroup;

    private String nickName;

    private Long telegramUserId;

    private String comment;

    private boolean notificationEnabled;

    public User() {
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

    @Column(name = "TELEGRAM_USER_ID")
    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "NICK_NAME")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String userName) {
        this.nickName = userName;
    }

    @Column(name = "IS_ADMIN")
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Column(name = "IS_LEADER")
    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

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

    @Column(name = "IS_NOTIFICATION_ENABLED")
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean enableNotification) {
        this.notificationEnabled = enableNotification;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + "'" +
                ", telegramUserId=" + telegramUserId +
                ", firstName='" + firstName + "'" +
                ", lastName='" + lastName + "'" +
                ", nickName='" + nickName + "'" +
                (comment == null ? "" : ", comment='" + comment + "'") +
                '}';
    }

    public boolean hasHomeGroup() {
        return Utils.isField(getHomeGroup());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return admin == user.admin &&
                leader == user.leader &&
                notificationEnabled == user.notificationEnabled &&
                id.equals(user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(homeGroup, user.homeGroup) &&
                Objects.equals(nickName, user.nickName) &&
                telegramUserId.equals(user.telegramUserId) &&
                Objects.equals(comment, user.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, admin, leader, firstName, lastName, homeGroup, nickName, telegramUserId, comment, notificationEnabled);
    }
}
