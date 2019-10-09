package model.homeGroups;

public class Helper extends User {

    private String phoneNumber;

    private Lieder lieder;

    public Helper(String firstName, String lastName, String userName, Long userId, String phoneNumber) {
        super(firstName, lastName, userName, userId);
        this.phoneNumber = phoneNumber;
    }

    public Helper() {
    }

    public Lieder getLieder() {
        return lieder;
    }

    public void setLieder(Lieder lieder) {
        this.lieder = lieder;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
