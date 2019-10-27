package model.homeGroups.db;

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
public class User {

    private Long id;

    private boolean admin;

    private boolean leader;

    private String firstName;

    private String lastName;

    private HomeGroup homeGroup;

    private String nickName;

    private Long telegramUserId;

    private String comment;

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
}
